<template>
  <Header />
  <div style="display: flex; justify-content: center">
    <div class="card">
      <DataTable :value="loans" tableStyle="width: 90vw" paginator :rows="10" :totalRecords="120" :rowsPerPageOptions="[10, 20, 30]" 
      sortField="status.name" :sortOrder="1" removableSort v-model:filters="filters" filterDisplay="row" 
      :globalFilterFields="['copy.edition.book.title', 'copy.signature', 'id', 'pickUpDeadline', 'loanedOn', 'returnDeadline', 'returnedOn', 'status.name', 'charge']">
        <template #header>
          <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
            <h4 v-if="isUserLoansOwner" class="m-0">Moje wypożyczenia</h4>
            <h4 v-else class="m-0">Wypożyczenia użytkownika {{ user.name }}</h4>
            <span class="p-input-icon-left">
              <Button v-if="showDetails" rounded class="mr-2" @click="showDetails = false" style="margin-right: 5px" label="Szczegóły" />
              <Button v-else outlined rounded class="mr-2" @click="showDetails = true" style="margin-right: 5px" label="Szczegóły" />
              <i class="pi pi-search" style="margin-left:125px" />
              <InputText v-model="filters['global'].value" placeholder="Słowo klucz" />
            </span>
          </div>
        </template>
        <template #empty> Nie znaleziono żadnych wypożyczeń. </template>
        <template #loading> Trwa ładowanie danych. Proszę zaczekać. </template>
        <Column v-if="showDetails" field="copy.edition.book.title" header="Tytuł" style="width: 11%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Tytuł" />
          </template>
        </Column>
        <Column v-else field="copy.edition.book.title" header="Tytuł" style="width: 27%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Tytuł" />
          </template>
        </Column>
        <Column v-if="showDetails" field="copy.signature" header="Egzemplarz" style="width: 11%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Sygnatura" />
          </template>
        </Column>
        <Column v-if="showDetails" field="createdOn" header="Data utworzenia" style="width: 11%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Data utworzenia" />
          </template>
        </Column>
        <Column v-if="showDetails" field="pickUpDeadline" header="Termin odbioru" style="width: 11%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" 
            placeholder="Termin odbioru" />
          </template>
        </Column>
        <Column v-if="showDetails" field="pickedUpOn" header="Data odbioru" style="width: 11%" sortable>
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
            placeholder="Data odbioru" />
          </template>
        </Column>
        <Column v-if="showDetails" field="returnDeadline" header="Termin zwrotu" style="width: 11%" sortable>
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
            placeholder="Termin zwrotu" />
          </template>
        </Column>
        <Column v-if="showDetails" field="returnedOn" header="Data zwrotu" style="width: 11%" sortable>
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
            placeholder="Data zwrotu" />
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
        <Column v-if="showDetails" field="charge" header="Kara" style="width: 11%" sortable>
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
            placeholder="Kara" />
          </template>
        </Column>
        <Column v-else field="charge" header="Kara" style="width: 27%" sortable>
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
            placeholder="Kara" />
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
            <span v-if="slotProps.data.status.name == 'aktywne'">
              <Button 
                v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'"
                icon="pi pi-check" 
                outlined rounded class="mr-2" 
                @click="showActiveConfirmationDialog(slotProps.data)" 
                style="margin-right: 5px" />
            </span>
            <span v-if="slotProps.data.status.name == 'oczekujące'">
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

      <Dialog v-model:visible="notesDialog" :style="{width: '450px'}" header="Uwagi" 
      :modal="true" class="p-fluid">
        <div class="field">
          <Textarea v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'" v-model="loan.notes" rows="5" cols="30" />
          <Textarea v-else disabled v-model="loan.notes" rows="5" cols="30" />
        </div>
        <template #footer>
          <Button v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'" label="Anuluj" icon="pi pi-times" @click="hideDialog()" outlined />
          <Button v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'" label="Zapisz" icon="pi pi-check" @click="updateLoan()" outlined />
        </template>
      </Dialog>

      <Dialog v-model:visible="pendingConfirmationDialog" :style="{width: '450px'}" header="Potwierdzenie" 
      :modal="true" class="p-fluid">
        <p v-if="finalizing">Czy na pewno chcesz aktywować to wypożyczenie?</p>
        <p v-else>Czy na pewno chcesz anulować to wypożyczenie?</p>
        <template #footer>
          <Button label="Nie" icon="pi pi-times" outlined @click="hideDialog()"/>
          <Button v-if="finalizing" label="Tak" icon="pi pi-check" outlined @click="activateLoan()" />
          <Button v-else label="Tak" icon="pi pi-check" outlined @click="cancelLoan()" />
        </template>
      </Dialog>

      <Dialog v-model:visible="activeConfirmationDialog" :style="{width: '450px'}" header="Potwierdzenie" 
      :modal="true" class="p-fluid">
        <p>Czy na pewno chcesz sfinalizować to wypożyczenie?</p>
        <template #footer>
          <Button label="Nie" icon="pi pi-times" outlined @click="hideDialog()"/>
          <Button label="Tak" icon="pi pi-check" outlined @click="finalizeLoan()" />
        </template>
      </Dialog>

      <Dialog v-model:visible="chargeConfirmationDialog" :style="{width: '450px'}" header="Potwierdzenie" 
      :modal="true" class="p-fluid">
        <p>Czy na pewno chcesz oznaczyć karę jako uiszczoną?</p>
        <template #footer>
          <Button label="Nie" icon="pi pi-times" outlined @click="hideDialog()"/>
          <Button label="Tak" icon="pi pi-check" outlined @click="setChargeAsPaid()" />
        </template>
      </Dialog>
    </div>
  </div>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import { FilterMatchMode } from 'primevue/api';
import LoanService from '@/services/LoanService';
import UserService from '@/services/UserService';

export default {
  name: "UserLoansView",
  components: {
    Header
  },
  data() {
    return {
      userId: null,
      isUserLoansOwner: false,
      user: {
        name: null
      },
      loans: null,
      userRole: null,
      loan: null,
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
    getLoans(userId) {
      LoanService.getByUserId(userId).then((response) => {
        this.loans = response.data;
      });
    },
    showNotesDialog(loan) {
      this.notesDialog = true;
      this.loan = loan;
    },
    showPendingConfirmationDialog(loan, finalizing) {
      if (finalizing) {
        this.finalizing = true;
      } else {
        this.finalizing = false;
      } 

      this.pendingConfirmationDialog = true;
      this.loan = loan;
    },
    showActiveConfirmationDialog(loan) {
      this.activeConfirmationDialog = true;
      this.loan = loan;
    },
    showChargeConfirmationDialog(loan) {
      this.chargeConfirmationDialog = true;
      this.loan = loan;

      if (loan.hasChargeBeenPaid == 1) {
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
      this.loan = null;
    },
    hideChargeConfirmationDialog() {
      this.chargeConfirmationDialog = false;
      this.loan = null;
    },
    async updateLoan() {
      const response = await LoanService.update(this.loan);

      if (response.status == 200) {
        this.$router.go()
        this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Wypożyczenie zostało zaktualizowane', life: 3000});
      } else {
        this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Nie udało się zaktualizować wypożyczenia', life: 3000});
      }
    },
    async finalizeLoan() {
      if (this.loan.charge > 0 && !this.loan.hasChargeBeenPaid) {
        this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Aby sfinalizować wypożyczenie, należy poprzednio uiścić karę', life: 3000});
      } else {
        const response = await LoanService.finalize(this.loan.id);

        if (response.status == 200) {
          this.$router.go()
          this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Wypożyczenie zostało sfinalizowane', life: 3000});
        } else {
          this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Nie udało się sfinalizować wypożyczenia', life: 3000});
        }
      }
    },
    async activateLoan() {
      const response = await LoanService.activate(this.loan.id);

      if (response.status == 200) {
        this.$router.go()
        this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Wypożyczenie zostało aktywowane', life: 3000});
      } else {
        this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Nie udało się aktywować wypożyczenia', life: 3000});
      }
    },
    async cancelLoan() {
      const response = await LoanService.cancel(this.loan.id);

      if (response.status == 200) {
        this.$router.go()
        this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Wypożyczenie zostało anulowane', life: 3000});
      } else {
        this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Nie udało się anulować wypożyczenia', life: 3000});
      }
    },
    async setChargeAsPaid() {
      const response = await LoanService.setChargeAsPaid(this.loan.id);

      if (response.status == 200) {
        this.$router.go()
        this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Opłata została uznana za uiszczoną', life: 3000});
      } else {
        this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Nie udało się uznać opłaty za uiszczoną', life: 3000});
      }
    }
  },
  mounted() {
    this.userRole = localStorage.getItem('user-role');
    this.userId = this.$route.params.userId;

    if (this.userId == localStorage.getItem('user-id')) {
      this.isUserLoansOwner = true;
    }

    this.getUser(this.userId);
    this.getLoans(this.userId);
  }
};
</script>