<template>
  <Header />
  <div class="flex justify-content-center">
    <div class="card">
      <Toolbar class="mb-4">
        <template #start>
          <Button label="Add" icon="pi pi-plus" severity="success" @click="bindingDialog=true" outlined />
        </template>
      </Toolbar>

      <DataTable :value="bindings" tableStyle="width: 90vw" paginator :rows="10" :rowsPerPageOptions="[10, 20, 30]" 
      removableSort v-model:filters="filters" filterDisplay="row" :globalFilterFields="['name']">
        <template #header>
          <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
            <h4 class="m-0">Bindings</h4>
            <div class="flex justify-content-end gap-2">
              <span class="p-input-icon-left">
                <i class="pi pi-search" />
                <InputText v-model="filters['global'].value" placeholder="Keyword" />
              </span>
            </div>
          </div>
        </template>
        <template #empty> Could not find any bindings </template>
        <template #loading> Loading data, please wait. </template>

        <Column field="name" header="Nazwa" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Nazwa" />
          </template>
        </Column>

        <Column :exportable="false">
          <template #body="slotProps">
            <Button @click="binding=slotProps.data; editChildDialog=true; bindingDialog=true"
            icon="pi pi-pencil" outlined rounded class="mr-2" />
          </template>
        </Column>
      </DataTable>
    </div>
  </div>

  <span v-if="bindingDialog">
    <span v-if="editChildDialog">
      <BindingDialog :editMode="true" />
    </span>
    <span v-else>
      <BindingDialog />
    </span>
  </span>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import { FilterMatchMode } from 'primevue/api';
import BindingDialog from '@/components/dialogs/BindingDialog.vue';
import BindingService from '@/services/BindingService';

export default {
  name: "BindingsTable",
  components: {
    Header,
    BindingDialog
  },
  data() {
    return {
      filters: {
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        name: { value: null, matchMode: FilterMatchMode.CONTAINS }
      },
      bindings: null,
      binding: null,
      bindingDialog: false,
      editChildDialog: false
    };
  },
  methods: {
    getBindings() {
      BindingService.getAll().then((response) => {
        this.bindings = response.data;
      });
    }
  },
  mounted() {
    this.getBindings();
  },
};
</script>