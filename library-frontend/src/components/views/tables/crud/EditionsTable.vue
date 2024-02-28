<template>
  <Header />
  <div class="flex justify-content-center">
    <div class="card">
      <Toolbar class="mb-4">
        <template #start>
          <Button label="Nowe" icon="pi pi-plus" severity="success" @click="editionDialog=true" outlined />
        </template>
      </Toolbar>

      <DataTable :value="editions" tableStyle="width: 90vw" paginator :rows="10" :rowsPerPageOptions="[10, 20, 30]" 
      removableSort v-model:filters="filters" filterDisplay="row"
      :globalFilterFields="['isbn13', 'book.title']">
        <template #header>
          <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
            <h4 class="m-0">Wydania</h4>
            <div class="flex justify-content-end gap-2">
              <Button v-if="showDetails" @click="showDetails = false" label="Szczegóły" rounded class="mr-2" />
              <Button v-else outlined @click="showDetails = true" label="Szczegóły" rounded class="mr-2" />
              <span class="p-input-icon-left">
                <i class="pi pi-search" />
                <InputText v-model="filters['global'].value" placeholder="Słowo klucz" />
              </span>
            </div>
          </div>
        </template>
        <template #empty> Nie znaleziono żadnych wydań. </template>
        <template #loading> Trwa ładowanie danych. Proszę zaczekać. </template>

        <Column field="book" header="Tytuł" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Tytuł" />
          </template>
          <template #body="slotProps">
            {{ slotProps.data.book.title }}
          </template>
        </Column>

        <Column field="isbn13" header="Numer ISBN" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Numer ISBN" />
          </template>
        </Column>

        <Column field="releaseYear" header="Rok wydania" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Rok wydania" />
          </template>
        </Column>

        <Column field="binding" header="Oprawa" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Oprawa" />
          </template>
          <template #body="slotProps">
            {{ slotProps.data.binding.name }}
          </template>
        </Column>

        <Column field="language" header="Język" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Język" />
          </template>
          <template #body="slotProps">
            {{ slotProps.data.language.name }}
          </template>
        </Column>

        <Column field="translator" header="Tłumacz" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Tłumacz" />
          </template>
          <template #body="slotProps">
            <span v-if="slotProps.data.translator">
              {{ slotProps.data.translator.name }}
            </span>
          </template>
        </Column>

        <Column field="numberOfPages" header="Ilość stron" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Ilość stron" />
          </template>
        </Column>

        <Column v-if="showDetails" field="weight" header="Waga" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Waga" />
          </template>
        </Column>

        <Column v-if="showDetails" field="dimensions" header="Wymiary" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Wymiary" />
          </template>
        </Column>

        <Column field="quantity" header="Ilość dostępnych egzemplarzy" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Ilość dostępnych egzemplarzy" />
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