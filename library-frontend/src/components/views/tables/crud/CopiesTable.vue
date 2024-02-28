<template>
  <Header />
  <div class="flex justify-content-center">
    <div class="card">
      <Toolbar class="mb-4">
        <template #start>
          <Button label="Nowy" icon="pi pi-plus" severity="success" @click="copyDialog=true" outlined />
        </template>
      </Toolbar>

      <DataTable :value="copies" tableStyle="width: 90vw" paginator :rows="10" :rowsPerPageOptions="[10, 20, 30]" 
      removableSort v-model:filters="filters" filterDisplay="row"
      :globalFilterFields="['signature', 'quality', 'purchaseDate', 'price']">
        <template #header>
          <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
            <h4 class="m-0">Egzemplarze</h4>
            <div class="flex justify-content-end gap-2">
              <Button v-if="showDetails" @click="showDetails = false" label="Szczegóły" rounded class="mr-2" />
              <Button v-else outlined @click="showDetails = true" label="Szczegóły" rounded class="mr-2" />
              <Button v-if="showOnlyAvailable" rounded class="mr-2" @click="showOnlyAvailable = false; filter()" label="Pokaż tylko dostępne" />
              <Button v-else rounded outlined class="mr-2" @click="showOnlyAvailable = true; filter()" label="Pokaż tylko dostępne" />
              <span class="p-input-icon-left">
                <i class="pi pi-search" />
                <InputText v-model="filters['global'].value" placeholder="Słowo klucz" />
              </span>
            </div>
          </div>
        </template>
        <template #empty> Nie znaleziono żadnych egzemplarzy. </template>
        <template #loading> Trwa ładowanie danych. Proszę zaczekać. </template>

        <Column field="signature" header="Sygnatura" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Sygnatura" />
          </template>
        </Column>

        <Column field="purchaseDate" header="Data zakupu" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Data zakupu" />
          </template>
        </Column>

        <Column field="price" header="Cena" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Cena" />
          </template>
        </Column>

        <Column v-if="showDetails" field="edition.isbn13" header="Wydanie" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Numer ISBN" />
          </template>
        </Column>

        <Column v-if="showDetails" field="edition.book.title" header="Książka" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Tytuł" />
          </template>
        </Column>

        <Column field="available" header="Dostępność" sortable>
          <template #body="slotProps">
            <div v-if="slotProps.data.available">{{ 'Dostępny' }}</div>
            <div v-else>{{ 'Niedostępny' }}</div>
          </template>
        </Column>

        <Column :exportable="false">
          <template #body="slotProps">
            <Button @click="copy=slotProps.data; editChildDialog=true; copyDialog=true"
            icon="pi pi-pencil" outlined rounded class="mr-2" />
          </template>
        </Column>

      </DataTable>
    </div>
  </div>

  <span v-if="copyDialog">
    <span v-if="editChildDialog">
      <CopyDialog :editMode="true" />
    </span>
    <span v-else>
      <CopyDialog />
    </span>
  </span>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import CopyService from '@/services/CopyService';
import EditionService from '@/services/EditionService';
import { FilterMatchMode } from 'primevue/api';
import CopyDialog from '@/components/dialogs/CopyDialog.vue';

export default {
  name: "CopiesTable",
  components: {
    Header,
    CopyDialog
  },
  data() {
    return {
      filters: {
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        signature: { value: null, matchMode: FilterMatchMode.CONTAINS },
        quality: { value: null, matchMode: FilterMatchMode.CONTAINS },
        purchaseDate: { value: null, matchMode: FilterMatchMode.CONTAINS },
        price: { value: null, matchMode: FilterMatchMode.CONTAINS },
        'edition.isbn13': { value: null, matchMode: FilterMatchMode.CONTAINS },
        'edition.book.title': { value: null, matchMode: FilterMatchMode.CONTAINS }
      },
      copies: null,
      allCopies: null,
      copy: null,
      copyDialog: false,
      editChildDialog: false,
      showOnlyAvailable: false,
      showDetails: false,
      edition: null
    };
  },
  methods: {
    filter() {
      var filteredCopies = this.allCopies;
      
      if (this.showOnlyAvailable) {
        filteredCopies = filteredCopies.filter((copy) => copy.available);
      }

      this.copies = filteredCopies;
    },
    getEdition() {
      EditionService.getByCopyId(this.copy.id).then((response) => {
        this.edition = response.data;
      });
    },
    getCopies() {
      CopyService.getAll().then((response) => {
        this.copies = response.data;
        this.allCopies = response.data;
      });
    }
  },
  mounted() {
    this.getCopies();
  }
};
</script>