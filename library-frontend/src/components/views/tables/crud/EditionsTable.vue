<template>
  <Header />
  <div class="flex justify-content-center">
    <div class="card">
      <Toolbar class="mb-4">
        <template #start>
          <Button label="Add" icon="pi pi-plus" severity="success" @click="editionDialog=true" outlined />
        </template>
      </Toolbar>

      <DataTable :value="editions" tableStyle="width: 90vw" paginator :rows="10" :rowsPerPageOptions="[10, 20, 30]" 
      removableSort v-model:filters="filters" filterDisplay="row"
      :globalFilterFields="['isbn13', 'book.title']">
        <template #header>
          <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
            <h4 class="m-0">Editions</h4>
            <div class="flex justify-content-end gap-2">
              <Button v-if="showDetails" @click="showDetails = false" label="Details" rounded class="mr-2" />
              <Button v-else outlined @click="showDetails = true" label="Details" rounded class="mr-2" />
              <span class="p-input-icon-left">
                <i class="pi pi-search" />
                <InputText v-model="filters['global'].value" placeholder="Keyword" />
              </span>
            </div>
          </div>
        </template>
        <template #empty> Could not find any editions </template>
        <template #loading> Loading data, please wait. </template>

        <Column field="book" header="Title" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Title" />
          </template>
          <template #body="slotProps">
            {{ slotProps.data.book.title }}
          </template>
        </Column>

        <Column field="isbn13" header="ISBN-13" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="ISBN-13" />
          </template>
        </Column>

        <Column field="releaseYear" header="Release year" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Release year" />
          </template>
        </Column>

        <Column field="binding" header="Binding" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Binding" />
          </template>
          <template #body="slotProps">
            {{ slotProps.data.binding.name }}
          </template>
        </Column>

        <Column field="language" header="Language" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Language" />
          </template>
          <template #body="slotProps">
            {{ slotProps.data.language.name }}
          </template>
        </Column>

        <Column field="translator" header="Translator" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Translator" />
          </template>
          <template #body="slotProps">
            <span v-if="slotProps.data.translator">
              {{ slotProps.data.translator.name }}
            </span>
          </template>
        </Column>

        <Column field="numberOfPages" header="Pages" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Pages" />
          </template>
        </Column>

        <Column v-if="showDetails" field="weight" header="Weight" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Weight" />
          </template>
        </Column>

        <Column v-if="showDetails" field="dimensions" header="Dimensions" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Dimensions" />
          </template>
        </Column>

        <Column field="quantity" header="Copies available" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Copies available" />
          </template>
        </Column>

        <Column :exportable="false">
          <template #body="slotProps">
            <Button @click="edition=slotProps.data; editionDialog=true; editChildDialog=true"
            icon="pi pi-pencil" outlined rounded class="mr-2" />
          </template>
        </Column>

      </DataTable>
    </div>
  </div>

  <span v-if="editionDialog">
    <span v-if="editChildDialog">
      <EditionDialog :editMode="true" />
    </span>
    <span v-else>
      <EditionDialog />
    </span>
  </span>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import { FilterMatchMode } from 'primevue/api';
import EditionDialog from '@/components/dialogs/EditionDialog.vue';
import EditionService from '@/services/EditionService';

export default {
  name: "EditionsTable",
  components: {
    Header,
    EditionDialog
  },
  data() {
    return {
      filters: {
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        isbn13: { value: null, matchMode: FilterMatchMode.CONTAINS },
        releaseYear: { value: null, matchMode: FilterMatchMode.CONTAINS },
        binding: { value: null, matchMode: FilterMatchMode.CONTAINS },
        language: { value: null, matchMode: FilterMatchMode.CONTAINS },
        translator: { value: null, matchMode: FilterMatchMode.CONTAINS },
        numberOfPages: { value: null, matchMode: FilterMatchMode.CONTAINS },
        weight: { value: null, matchMode: FilterMatchMode.CONTAINS },
        dimensions: { value: null, matchMode: FilterMatchMode.CONTAINS },
        quantity: { value: null, matchMode: FilterMatchMode.CONTAINS },
        book: { value: null, matchMode: FilterMatchMode.CONTAINS }
      },
      editions: null,
      edition: null,
      editionDialog: false,
      editChildDialog: false,
      showDetails: false
    };
  },
  methods: {
    getEditions() {
      EditionService.getAll().then((response) => {
        this.editions = response.data;
      });
    }
  },
  mounted() {
    this.getEditions();
  },
};
</script>