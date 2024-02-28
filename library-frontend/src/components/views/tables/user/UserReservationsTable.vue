<template>
  <Header />
  <div style="display: flex; justify-content: center">
    <div class="card">
      <DataTable :value="reservations" tableStyle="width: 90vw" paginator :rows="10" :rowsPerPageOptions="[10, 20, 30]" 
      sortField="status.name" :sortOrder="1" removableSort v-model:filters="filters" filterDisplay="row" 
      :globalFilterFields="['createdOn', 'waitDeadline', 'status.name', 'book.title', 'edition.date']">
        <template #header>
          <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
            <h4 v-if="isUserReservationsOwner" class="m-0">Moje rezerwacje</h4>
            <h4 v-else class="m-0">Rezerwacje użytkownika {{ user.name }}</h4>
            <span class="p-input-icon-left">
              <i class="pi pi-search" />
              <InputText v-model="filters['global'].value" placeholder="Słowo klucz" />
            </span>
          </div>
        </template>
        <template #empty> Nie znaleziono żadnych rezerwacji. </template>
        <template #loading> Trwa ładowanie danych. Proszę zaczekać. </template>
        <Column field="book.title" header="Tytuł" style="width: 18%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Tytuł" />
          </template>
        </Column>
        <Column field="edition.isbn13" header="Wydanie" style="width: 18%" sortable>
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
            placeholder="Numer ISBN" />
          </template>
        </Column>
        <Column field="createdOn" header="Data utworzenia" style="width: 18%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Data utworzenia" />
          </template>
        </Column>
        <Column field="waitDeadline" header="Termin oczekiwania" style="width: 18%" sortable>
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
            placeholder="Termin oczekiwania" />
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
            <span v-if="slotProps.data.status.name == 'aktywna'">
              <Button 
                icon="pi pi-times" 
                outlined rounded class="mr-2" 
                @click="showConfirmationDialog(slotProps.data)" 
                style="margin-right: 5px" />
            </span>
          </template>
        </Column>
      </DataTable>

      <Dialog v-model:visible="confirmationDialog" :style="{width: '450px'}" header="Potwierdzenie" 
      :modal="true" class="p-fluid">
        <p>Czy na pewno chcesz anulować tę rezerwację?</p>
        <template #footer>
          <Button label="Nie" icon="pi pi-times" outlined @click="hideDialog()"/>
          <Button label="Tak" icon="pi pi-check" outlined @click="cancelReservation()" />
        </template>
      </Dialog>

      <Dialog v-model:visible="notesDialog" :style="{width: '450px'}" header="Uwagi" 
      :modal="true" class="p-fluid">
        <div class="field">
          <Textarea v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'" v-model="reservation.notes" rows="5" cols="30" />
          <Textarea v-else disabled v-model="reservation.notes" rows="5" cols="30" />
        </div>
        <template #footer>
          <Button v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'" label="Anuluj" icon="pi pi-times" outlined @click="hideDialog()"/>
          <Button v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'" label="Zapisz" icon="pi pi-check" outlined @click="updateReservation()" />
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
  name: "UserLoansView",
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
        this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Rezerwacja została zaktualizowana', life: 3000});
      } else {
        this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Nie udało się zaktualizować rezerwacji', life: 3000});
      }
    },
    async cancelReservation() {
      const response = await ReservationService.cancel(this.reservation.id);

      if (response.status == 200) {
        this.$router.go()
        this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Rezerwacja została anulowana', life: 3000});
      } else {
        this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Nie udało się anulować rezerwacji', life: 3000});
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