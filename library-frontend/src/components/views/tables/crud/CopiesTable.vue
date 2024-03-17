<template>
  <Header />
  <div class="flex justify-content-center">
    <div class="card">
      <Toolbar class="mb-4">
        <template #start>
          <Button label="Add" icon="pi pi-plus" severity="success" @click="copyDialog=true" outlined />
        </template>
      </Toolbar>

      <DataTable :value="copies" tableStyle="width: 90vw" paginator :rows="10" :rowsPerPageOptions="[10, 20, 30]" 
      removableSort v-model:filters="filters" filterDisplay="row"
      :globalFilterFields="['signature', 'quality', 'purchaseDate', 'price']">
        <template #header>
          <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
            <h4 class="m-0">Copies</h4>
            <div class="flex justify-content-end gap-2">
              <Button v-if="showDetails" @click="showDetails = false" label="Details" rounded class="mr-2" />
              <Button v-else outlined @click="showDetails = true" label="Details" rounded class="mr-2" />
              <Button v-if="showOnlyAvailable" rounded class="mr-2" @click="showOnlyAvailable = false; filter()" label="Only the available" />
              <Button v-else rounded outlined class="mr-2" @click="showOnlyAvailable = true; filter()" label="Only the available" />
              <span class="p-input-icon-left">
                <i class="pi pi-search" />
                <InputText v-model="filters['global'].value" placeholder="Keyword" />
              </span>
            </div>
          </div>
        </template>
        <template #empty> Could not find any copies </template>
        <template #loading> Loading data, please wait. </template>

        <Column field="signature" header="Signature" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Signature" />
          </template>
        </Column>

        <Column field="purchaseDate" header="Purchase date" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Purchase date" />
          </template>
        </Column>

        <Column field="price" header="Price" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Price" />
          </template>
        </Column>

        <Column v-if="showDetails" field="edition.isbn13" header="Edition" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="ISBN-13" />
          </template>
        </Column>

        <Column v-if="showDetails" field="edition.book.title" header="Book" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Title" />
          </template>
        </Column>

        <Column field="available" header="Availability" sortable>
          <template #body="slotProps">
            <div v-if="slotProps.data.available">{{ 'Available' }}</div>
            <div v-else>{{ 'Unavailable' }}</div>
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