<template>
  <Header />
  <div class="flex justify-content-center">
    <div class="card">
      <Toolbar class="mb-4">
        <template #start>
          <Button label="Nowy" icon="pi pi-plus" severity="success" @click="translatorDialog=true" outlined />
        </template>
      </Toolbar>

      <DataTable :value="translators" tableStyle="width: 90vw" paginator :rows="10" :rowsPerPageOptions="[10, 20, 30]" 
      removableSort v-model:filters="filters" filterDisplay="row" :globalFilterFields="['name']">
        <template #header>
          <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
            <h4 class="m-0">Tłumacze</h4>
            <div class="flex justify-content-end gap-2">
              <span class="p-input-icon-left">
                <i class="pi pi-search" />
                <InputText v-model="filters['global'].value" placeholder="Słowo klucz" />
              </span>
            </div>
          </div>
        </template>
        <template #empty> Nie znaleziono żadnych tłumaczy. </template>
        <template #loading> Trwa ładowanie danych. Proszę zaczekać. </template>

        <Column field="name" header="Imię i nazwisko" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Imię i nazwisko" />
          </template>
        </Column>

        <Column :exportable="false">
          <template #body="slotProps">
            <Button @click="translator=slotProps.data; editChildDialog=true; translatorDialog=true"
            icon="pi pi-pencil" outlined rounded class="mr-2" />
          </template>
        </Column>
      </DataTable>
    </div>
  </div>

  <span v-if="translatorDialog">
    <span v-if="editChildDialog">
      <TranslatorDialog :editMode="true" />
    </span>
    <span v-else>
      <TranslatorDialog />
    </span>
  </span>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import { FilterMatchMode } from 'primevue/api';
import TranslatorDialog from '@/components/dialogs/TranslatorDialog.vue';
import TranslatorService from '@/services/TranslatorService';

export default {
  name: "TranslatorsTable",
  components: {
    Header,
    TranslatorDialog
  },
  data() {
    return {
      filters: {
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        name: { value: null, matchMode: FilterMatchMode.CONTAINS }
      },
      translators: null,
      translator: null,
      translatorDialog: false,
      editChildDialog: false
    };
  },
  methods: {
    getTranslators() {
      TranslatorService.getAll().then((response) => {
        this.translators = response.data;
      });
    }
  },
  mounted() {
    this.getTranslators();
  },
};
</script>