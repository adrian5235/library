<template>
  <Header />
  <div class="flex justify-content-center">
    <div class="card">
      <Toolbar class="mb-4">
        <template #start>
          <Button label="Nowy" icon="pi pi-plus" severity="success" @click="publisherDialog=true" outlined />
        </template>
      </Toolbar>

      <DataTable :value="publishers" tableStyle="width: 90vw" paginator :rows="10" :rowsPerPageOptions="[10, 20, 30]" 
      removableSort v-model:filters="filters" filterDisplay="row" :globalFilterFields="['name']">
        <template #header>
          <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
            <h4 class="m-0">Wydawnictwa</h4>
            <div class="flex justify-content-end gap-2">
              <span class="p-input-icon-left">
                <i class="pi pi-search" />
                <InputText v-model="filters['global'].value" placeholder="Słowo klucz" />
              </span>
            </div>
          </div>
        </template>
        <template #empty> Nie znaleziono żadnych wydawnictw. </template>
        <template #loading> Trwa ładowanie danych. Proszę zaczekać. </template>

        <Column field="name" header="Nazwa" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Nazwa" />
          </template>
        </Column>

        <Column :exportable="false">
          <template #body="slotProps">
            <Button @click="publisher=slotProps.data; editChildDialog=true; publisherDialog=true"
            icon="pi pi-pencil" outlined rounded class="mr-2" />
          </template>
        </Column>
      </DataTable>
    </div>
  </div>

  <span v-if="publisherDialog">
    <span v-if="editChildDialog">
      <PublisherDialog :editMode="true" />
    </span>
    <span v-else>
      <PublisherDialog />
    </span>
  </span>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import { FilterMatchMode } from 'primevue/api';
import PublisherDialog from '@/components/dialogs/PublisherDialog.vue';
import PublisherService from '@/services/PublisherService';

export default {
  name: "PublishersTable",
  components: {
    Header,
    PublisherDialog
  },
  data() {
    return {
      filters: {
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        name: { value: null, matchMode: FilterMatchMode.CONTAINS }
      },
      publishers: null,
      publisher: null,
      publisherDialog: false,
      editChildDialog: false
    };
  },
  methods: {
    getPublishers() {
      PublisherService.getAll().then((response) => {
        this.publishers = response.data;
      });
    }
  },
  mounted() {
    this.getPublishers();
  },
};
</script>