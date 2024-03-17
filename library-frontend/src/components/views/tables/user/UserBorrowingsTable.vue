<template>
  <Header />
  <div style="display: flex; justify-content: center">
    <div class="card">
      <DataTable :value="borrowings" tableStyle="width: 90vw" paginator :rows="10" :totalRecords="120" :rowsPerPageOptions="[10, 20, 30]" 
      sortField="status.name" :sortOrder="1" removableSort v-model:filters="filters" filterDisplay="row" 
      :globalFilterFields="['copy.edition.book.title', 'copy.signature', 'id', 'pickUpDeadline', 'borrowedOn', 'returnDeadline', 'returnedOn', 'status.name', 'charge']">
        <template #header>
          <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
            <h4 v-if="isUserBorrowingsOwner" class="m-0">My borrowings</h4>
            <h4 v-else class="m-0">{{ user.name }}'s borrowings</h4>
            <span class="p-input-icon-left">
              <Button v-if="showDetails" rounded class="mr-2" @click="showDetails = false" style="margin-right: 5px" label="Details" />
              <Button v-else outlined rounded class="mr-2" @click="showDetails = true" style="margin-right: 5px" label="Details" />
              <i class="pi pi-search" style="margin-left:125px" />
              <InputText v-model="filters['global'].value" placeholder="Keyword" />
            </span>
          </div>
        </template>
        <template #empty> Could not find any data </template>
        <template #loading> Loading data, please wait. </template>
        <Column v-if="showDetails" field="copy.edition.book.title" header="Title" style="width: 11%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Title" />
          </template>
        </Column>
        <Column v-else field="copy.edition.book.title" header="Title" style="width: 27%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Title" />
          </template>
        </Column>
        <Column v-if="showDetails" field="copy.signature" header="Copy" style="width: 11%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Copy" />
          </template>
        </Column>
        <Column v-if="showDetails" field="createdOn" header="Created on" style="width: 11%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Date" />
          </template>
        </Column>
        <Column v-if="showDetails" field="pickUpDeadline" header="Pick up deadline" style="width: 11%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Date" />
          </template>
        </Column>
        <Column v-if="showDetails" field="pickedUpOn" header="Picked up on" style="width: 11%" sortable>
          <template #body="slotProps">
            <template v-if="slotProps.data.pickedUpOn == null">
              {{ '_' }}
            </template>
            <template v-else>
              {{ slotProps.data.pickedUpOn }}
            </template>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Date" />
          </template>
        </Column>
        <Column v-if="showDetails" field="returnDeadline" header="Return deadline" style="width: 11%" sortable>
          <template #body="slotProps">
            <template v-if="slotProps.data.returnDeadline == null">
              {{ '_' }}
            </template>
            <template v-else>
              {{ slotProps.data.returnDeadline }}
            </template>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Date" />
          </template>
        </Column>
        <Column v-if="showDetails" field="returnedOn" header="Returned on" style="width: 11%" sortable>
          <template #body="slotProps">
            <template v-if="slotProps.data.returnedOn == null">
              {{ '_' }}
            </template>
            <template v-else>
              {{ slotProps.data.returnedOn }}
            </template>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Date" />
          </template>
        </Column>
        <Column v-if="showDetails" field="status.name" header="Status" style="width: 11%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Status" />
          </template>
        </Column>
        <Column v-else field="status.name" header="Status" style="width: 27%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Status" />
          </template>
        </Column>
        <Column v-if="showDetails" field="charge" header="Charge" style="width: 11%" sortable>
          <template #body="slotProps">
            <div v-if="slotProps.data.charge > 0">
              <div v-if="slotProps.data.hasChargeBeenPaid">
                <span style="color: green">
                  {{ slotProps.data.charge }}
                </span>
              </div>
              <div v-else>
                <span style="color: red">
                  <!-- calculate remaining charge to be paid -->
                  {{ slotProps.data.charge - slotProps.data.chargePaid }}
                </span>
              </div>
            </div>
            <div v-else>
              <span>
                {{ slotProps.data.charge }}
              </span>
            </div>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Charge" />
          </template>
        </Column>
        <Column v-else field="charge" header="Charge" style="width: 27%" sortable>
          <template #body="slotProps">
            <div v-if="slotProps.data.charge > 0">
              <div v-if="slotProps.data.hasChargeBeenPaid">
                <span style="color: green">
                  {{ slotProps.data.charge }}
                </span>
              </div>
              <div v-else>
                <span style="color: red">
                  <!-- calculate remaining charge to be paid -->
                  {{ slotProps.data.charge - slotProps.data.chargePaid }}
                </span>
              </div>
            </div>
            <div v-else>
              <span>
                {{ slotProps.data.charge }}
              </span>
            </div>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Charge" />
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
                v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'"
                icon="pi pi-check" 
                outlined rounded class="mr-2" 
                @click="showActiveConfirmationDialog(slotProps.data)" 
                style="margin-right: 5px" />
            </span>
            <span v-if="slotProps.data.status.name == 'awaiting'">
              <Button 
                v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'"
                icon="pi pi-sign-in" 
                outlined rounded class="mr-2" 
                @click="showPendingConfirmationDialog(slotProps.data, true)" 
                style="margin-right: 5px" 
              />
              <Button
                icon="pi pi-times" 
                outlined rounded class="mr-2" 
                @click="showPendingConfirmationDialog(slotProps.data, false)" 
                style="margin-right: 5px" 
              />
            </span>
            <span v-if="slotProps.data.charge > 0 && slotProps.data.hasChargeBeenPaid == 0">
              <Button 
                v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'"
                icon="pi pi-dollar" 
                outlined rounded class="mr-2" 
                @click="showChargeConfirmationDialog(slotProps.data)" 
                style="margin-right: 5px" 
              />
            </span>
          </template>
        </Column>
      </DataTable>

      <Dialog v-model:visible="notesDialog" :style="{width: '450px'}" header="Notes" 
      :modal="true" class="p-fluid">
        <div class="field">
          <Textarea v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'" v-model="borrowing.notes" rows="5" cols="30" />
          <Textarea v-else disabled v-model="borrowing.notes" rows="5" cols="30" />
        </div>
        <template #footer>
          <Button v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'" label="Cancel" icon="pi pi-times" @click="hideDialog()" outlined />
          <Button v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'" label="Save" icon="pi pi-check" @click="updateBorrowing()" outlined />
        </template>
      </Dialog>

      <Dialog v-model:visible="pendingConfirmationDialog" :style="{width: '450px'}" header="Confirmation" 
      :modal="true" class="p-fluid">
        <p v-if="finalizing">Are you sure you want to activate this borrowing?</p>
        <p v-else>Are you sure you want to cancel this borrowing?</p>
        <template #footer>
          <Button label="No" icon="pi pi-times" outlined @click="hideDialog()"/>
          <Button v-if="finalizing" label="Yes" icon="pi pi-check" outlined @click="activateBorrowing()" />
          <Button v-else label="Yes" icon="pi pi-check" outlined @click="cancelBorrowing()" />
        </template>
      </Dialog>

      <Dialog v-model:visible="activeConfirmationDialog" :style="{width: '450px'}" header="Confirmation" 
      :modal="true" class="p-fluid">
        <p>Are you sure you want to finalize this borrowing?</p>
        <template #footer>
          <Button label="No" icon="pi pi-times" outlined @click="hideDialog()"/>
          <Button label="Yes" icon="pi pi-check" outlined @click="finalizeBorrowing()" />
        </template>
      </Dialog>

      <Dialog v-model:visible="chargeConfirmationDialog" :style="{width: '450px'}" header="Confirmation" 
      :modal="true" class="p-fluid">
        <p>Are you sure you want to set the charge as paid?</p>
        <template #footer>
          <Button label="No" icon="pi pi-times" outlined @click="hideDialog()"/>
          <Button label="Yes" icon="pi pi-check" outlined @click="setChargeAsPaid()" />
        </template>
      </Dialog>
    </div>
  </div>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import { FilterMatchMode } from 'primevue/api';
import BorrowingService from '@/services/BorrowingService';
import UserService from '@/services/UserService';

export default {
  name: "UserBorrowingsView",
  components: {
    Header
  },
  data() {
    return {
      userId: null,
      isUserBorrowingsOwner: false,
      user: {
        name: null
      },
      borrowings: null,
      userRole: null,
      borrowing: null,
      pendingConfirmationDialog: false,
      finalizing: false,
      activeConfirmationDialog: false,
      chargeConfirmationDialog: false,
      hasChargeBeenPaid: false,
      showDetails: false,
      notesDialog: false,
      filters: {
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        id: { value: null, matchMode: FilterMatchMode.CONTAINS },
        createdOn: { value: null, matchMode: FilterMatchMode.CONTAINS },
        pickUpDeadline: { value: null, matchMode: FilterMatchMode.CONTAINS },
        pickedUpOn: { value: null, matchMode: FilterMatchMode.CONTAINS },
        returnDeadline: { value: null, matchMode: FilterMatchMode.CONTAINS },
        returnedOn: { value: null, matchMode: FilterMatchMode.CONTAINS },
        'status.name': { value: null, matchMode: FilterMatchMode.CONTAINS },
        charge: { value: null, matchMode: FilterMatchMode.GREATER_THAN_OR_EQUAL_TO },
        'copy.signature': { value: null, matchMode: FilterMatchMode.CONTAINS },
        'copy.edition.book.title': { value: null, matchMode: FilterMatchMode.CONTAINS },
      },
    };
  },
  methods: {
    getUser(userId) {
      UserService.get(userId).then((response) => {
        this.user = response.data;
      });
    },
    getBorrowings(userId) {
      BorrowingService.getByUserId(userId).then((response) => {
        this.borrowings = response.data;
      });
    },
    showNotesDialog(borrowing) {
      this.notesDialog = true;
      this.borrowing = borrowing;
    },
    showPendingConfirmationDialog(borrowing, finalizing) {
      if (finalizing) {
        this.finalizing = true;
      } else {
        this.finalizing = false;
      } 

      this.pendingConfirmationDialog = true;
      this.borrowing = borrowing;
    },
    showActiveConfirmationDialog(borrowing) {
      this.activeConfirmationDialog = true;
      this.borrowing = borrowing;
    },
    showChargeConfirmationDialog(borrowing) {
      this.chargeConfirmationDialog = true;
      this.borrowing = borrowing;

      if (borrowing.hasChargeBeenPaid == 1) {
        this.hasChargeBeenPaid = true;
      } else {
        this.hasChargeBeenPaid = false;
      }
    },
    hideDialog() {
      this.pendingConfirmationDialog = false;
      this.activeConfirmationDialog = false;
      this.chargeConfirmationDialog = false;
      this.notesDialog = false;
      this.borrowing = null;
    },
    hideChargeConfirmationDialog() {
      this.chargeConfirmationDialog = false;
      this.borrowing = null;
    },
    async updateBorrowing() {
      const response = await BorrowingService.update(this.borrowing);

      if (response.status == 200) {
        this.$router.go()
        this.$toast.add({severity:'success', summary: 'Success', detail: 'The borrowing has been updated', life: 3000});
      } else {
        this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not update the borrowing', life: 3000});
      }
    },
    async finalizeBorrowing() {
      if (this.borrowing.charge > 0 && !this.borrowing.hasChargeBeenPaid) {
        this.$toast.add({severity:'error', summary: 'Error', detail: 'To finalize the borrowing, the charge must be paid', life: 3000});
      } else {
        const response = await BorrowingService.finalize(this.borrowing.id);

        if (response.status == 200) {
          this.$router.go()
          this.$toast.add({severity:'success', summary: 'Success', detail: 'The borrowing has been finalized', life: 3000});
        } else {
          this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not finalize the borrowing', life: 3000});
        }
      }
    },
    async activateBorrowing() {
      const response = await BorrowingService.activate(this.borrowing.id);

      if (response.status == 200) {
        this.$router.go()
        this.$toast.add({severity:'success', summary: 'Success', detail: 'The borrowing has been activated', life: 3000});
      } else {
        this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not activate the borrowing', life: 3000});
      }
    },
    async cancelBorrowing() {
      const response = await BorrowingService.cancel(this.borrowing.id);

      if (response.status == 200) {
        this.$router.go()
        this.$toast.add({severity:'success', summary: 'Success', detail: 'The borrowing has been canceled', life: 3000});
      } else {
        this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not cancel the borrowing', life: 3000});
      }
    },
    async setChargeAsPaid() {
      const response = await BorrowingService.setChargeAsPaid(this.borrowing.id);

      if (response.status == 200) {
        this.$router.go()
        this.$toast.add({severity:'success', summary: 'Success', detail: 'The charge has been paid', life: 3000});
      } else {
        this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not pay the charge', life: 3000});
      }
    }
  },
  mounted() {
    this.userRole = localStorage.getItem('user-role');
    this.userId = this.$route.params.userId;

    if (this.userId == localStorage.getItem('user-id')) {
      this.isUserBorrowingsOwner = true;
    }

    this.getUser(this.userId);
    this.getBorrowings(this.userId);
  }
};
</script>