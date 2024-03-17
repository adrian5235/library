<template>
  <Header />
  <div style="display: flex; justify-content: center">
    <div class="card">
      <DataTable :value="reservations" tableStyle="width: 90vw" paginator :rows="10" :rowsPerPageOptions="[10, 20, 30]" 
      sortField="status.name" :sortOrder="1" removableSort v-model:filters="filters" filterDisplay="row" 
      :globalFilterFields="['createdOn', 'waitDeadline', 'status.name', 'book.title', 'edition.date']">
        <template #header>
          <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
            <h4 v-if="isUserReservationsOwner" class="m-0">My reservations</h4>
            <h4 v-else class="m-0">{{ user.name }}'s reservations'</h4>
            <span class="p-input-icon-left">
              <i class="pi pi-search" />
              <InputText v-model="filters['global'].value" placeholder="Keyword" />
            </span>
          </div>
        </template>
        <template #empty> Could not find any data </template>
        <template #loading> Loading data, please wait. </template>
        <Column field="book.title" header="Title" style="width: 18%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Title" />
          </template>
        </Column>
        <Column field="edition.isbn13" header="Edition" style="width: 18%" sortable>
          <template #body="slotProps">
            <template v-if="slotProps.data.edition == null">
              {{ '_' }}
            </template>
            <template v-else>
              {{ slotProps.data.edition.isbn13 }}
            </template>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="ISBN-13" />
          </template>
        </Column>
        <Column field="createdOn" header="Created on" style="width: 18%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Date" />
          </template>
        </Column>
        <Column field="waitDeadline" header="Wait deadline" style="width: 18%" sortable>
          <template #body="slotProps">
            <template v-if="slotProps.data.waitDeadline == null">
              {{ '_' }}
            </template>
            <template v-else>
              {{ slotProps.data.waitDeadline }}
            </template>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Date" />
          </template>
        </Column>
        <Column field="status.name" header="Status" style="width: 18%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Status" />
          </template>
        </Column>
        <Column style="height: 5rem">
          <template #body="slotProps">
            <Button
              v-if="slotProps.data.notes || userRole == 'LIBRARIAN' || userRole == 'ADMIN'"
              icon="pi pi-file-edit" 
              outlined rounded class="mr-2" 
              @click="showNotesDialog(slotProps.data)" 
              style="margin-right: 5px" 
            />
            <span v-if="slotProps.data.status.name == 'active'">
              <Button 
                icon="pi pi-times" 
                outlined rounded class="mr-2" 
                @click="showConfirmationDialog(slotProps.data)" 
                style="margin-right: 5px" />
            </span>
          </template>
        </Column>
      </DataTable>

      <Dialog v-model:visible="confirmationDialog" :style="{width: '450px'}" header="Confirmation" 
      :modal="true" class="p-fluid">
        <p>Are you sure you want to cancel this reservation?</p>
        <template #footer>
          <Button label="No" icon="pi pi-times" outlined @click="hideDialog()"/>
          <Button label="Yes" icon="pi pi-check" outlined @click="cancelReservation()" />
        </template>
      </Dialog>

      <Dialog v-model:visible="notesDialog" :style="{width: '450px'}" header="Notes" 
      :modal="true" class="p-fluid">
        <div class="field">
          <Textarea v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'" v-model="reservation.notes" rows="5" cols="30" />
          <Textarea v-else disabled v-model="reservation.notes" rows="5" cols="30" />
        </div>
        <template #footer>
          <Button v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'" label="Cancel" icon="pi pi-times" outlined @click="hideDialog()"/>
          <Button v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'" label="Save" icon="pi pi-check" outlined @click="updateReservation()" />
        </template>
      </Dialog>
    </div>
  </div>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import UserService from "@/services/UserService";
import { FilterMatchMode } from 'primevue/api';
import ReservationService from '@/services/ReservationService';

export default {
  name: "UserReservationsView",
  components: {
    Header
  },
  data() {
    return {
      reservations: [],
      userId: null,
      isUserReservationsOwner: false,
      user: {
        name: null
      },
      userRole: null,
      reservation: null,
      confirmationDialog: false,
      notesDialog: false,
      filters: {
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        'book.title': { value: null, matchMode: FilterMatchMode.CONTAINS },
        'edition.isbn13': { value: null, matchMode: FilterMatchMode.CONTAINS },
        createdOn: { value: null, matchMode: FilterMatchMode.CONTAINS },
        waitDeadline: { value: null, matchMode: FilterMatchMode.CONTAINS },
        'status.name': { value: null, matchMode: FilterMatchMode.CONTAINS }
      },
    };
  },
  methods: {
    getUser(userId) {
      UserService.get(userId).then((response) => {
        this.user = response.data;
      });
    },
    getReservations(userId) {
      ReservationService.getByUserId(userId).then((response) => {
        this.reservations = response.data;
      });
    },
    showNotesDialog(reservation) {
      this.notesDialog = true;
      this.reservation = reservation;
    },
    showConfirmationDialog(reservation) {
      this.confirmationDialog = true;
      this.reservation = reservation;
    },
    hideDialog() {
      this.confirmationDialog = false;
      this.reservation = null;
      this.notesDialog = false;
    },
    async updateReservation() {
      const response = await ReservationService.update(this.reservation);

      if (response.status == 200) {
        this.$router.go()
        this.$toast.add({severity:'success', summary: 'Success', detail: 'The reservation has been updated', life: 3000});
      } else {
        this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not update the reservation', life: 3000});
      }
    },
    async cancelReservation() {
      const response = await ReservationService.cancel(this.reservation.id);

      if (response.status == 200) {
        this.$router.go()
        this.$toast.add({severity:'success', summary: 'Success', detail: 'The reservation has been canceled', life: 3000});
      } else {
        this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not cancel the reservation', life: 3000});
      }
    }
  },
  mounted() {
    this.userRole = localStorage.getItem('user-role');
    this.userId = this.$route.params.userId;

    if (this.userId == localStorage.getItem('user-id')) {
      this.isUserReservationsOwner = true;
    }

    this.getUser(this.userId);
    this.getReservations(this.userId);
  }
};
</script>