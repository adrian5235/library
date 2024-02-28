<template>
  <Header />
  <div class="flex justify-content-center">
    <div class="card">
      <Toolbar class="mb-4">
        <template #start>
          <Button label="Nowy" icon="pi pi-plus" severity="success" @click="languageDialog=true" outlined />
        </template>
      </Toolbar>

      <DataTable :value="languages" tableStyle="width: 90vw" paginator :rows="10" :rowsPerPageOptions="[10, 20, 30]" 
      removableSort v-model:filters="filters" filterDisplay="row" :globalFilterFields="['name']">
        <template #header>
          <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
            <h4 class="m-0">Języki</h4>
            <div class="flex justify-content-end gap-2">
              <span class="p-input-icon-left">
                <i class="pi pi-search" />
                <InputText v-model="filters['global'].value" placeholder="Słowo klucz" />
              </span>
            </div>
          </div>
        </template>
        <template #empty> Nie znaleziono żadnych języków. </template>
        <template #loading> Trwa ładowanie danych. Proszę zaczekać. </template>

        <Column field="name" header="Nazwa" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Nazwa" />
          </template>
        </Column>

        <Column :exportable="false">
          <template #body="slotProps">
            <Button @click="language=slotProps.data; editChildDialog=true; languageDialog=true"
            icon="pi pi-pencil" outlined rounded class="mr-2" />
          </template>
        </Column>
      </DataTable>
    </div>
  </div>

  <span v-if="languageDialog">
    <span v-if="editChildDialog">
      <LanguageDialog :editMode="true" />
    </span>
    <span v-else>
      <LanguageDialog />
    </span>
  </span>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import { FilterMatchMode } from 'primevue/api';
import LanguageDialog from '@/components/dialogs/LanguageDialog.vue';
import LanguageService from '@/services/LanguageService';

export default {
  name: "LanguagesTable",
  components: {
    Header,
    LanguageDialog
  },
  data() {
    return {
      filters: {
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        name: { value: null, matchMode: FilterMatchMode.CONTAINS }
      },
      languages: null,
      language: null,
      languageDialog: false,
      editChildDialog: false
    };
  },
  methods: {
    getLanguages() {
      LanguageService.getAll().then((response) => {
        this.languages = response.data;
      });
    }
  },
  mounted() {
    this.getLanguages();
  },
};
</script>