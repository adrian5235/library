<template>
  <div style="display: flex; justify-content: center">
    <div class="card">
      <DataTable :value="mostPopularGenres" tableStyle="width: 90vw" paginator :rows="10" :totalRecords="120"
      :rowsPerPageOptions="[10, 20, 30]" :sortOrder="1" removableSort v-model:filters="filters" dataKey="id" filterDisplay="row">
        <template #header>
          <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
            <h4 class="m-0">The most popular genres</h4>
            <Button :label="periodOfTimeFormatted" icon="pi pi-calendar" text @click="showDialog()" />
          </div>
        </template>
        <template #empty> Could not find any data </template>
        <template #loading> Loading data, please wait. </template>

        <Column field="position" header="Position" sortable style="width: 15%">
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()"
            class="p-column-filter" placeholder="Position" />
          </template>
        </Column>

        <Column field="name" header="Name" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text"  @input="filterCallback()" 
            class="p-column-filter" placeholder="Name" />
          </template>
        </Column>

        <Column field="numberOfBorrowedCopies" header="Borrowed copies" sortable style="width: 20%">
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()"
            class="p-column-filter" placeholder="Min. borrowed copies" />
          </template>
        </Column>
      </DataTable>

      <Dialog v-model:visible="dialog" :style="{width: '450px'}" header="Change time period" :modal="true" class="p-fluid">
      <div class="field">
        <label for="periodOfTime">Time period</label>
        <div class="card flex justify-content-center">
          <Calendar v-model="periodOfTime" selectionMode="range" showIcon showButtonBar :manualInput="false" placeholder="Choose time period" />
        </div>
      </div>
      <template #footer>
        <Button label="Cancel" icon="pi pi-times" @click="cancel()" outlined />
        <Button label="Confirm" icon="pi pi-check" @click="changePeriodOfTime()" outlined />
      </template>
    </Dialog>
    </div>
  </div>
</template>

<script>
import { FilterMatchMode } from "primevue/api";
import StatisticsService from "@/services/StatisticsService"
import moment from 'moment'

var threeMonthsAgo = new Date();
threeMonthsAgo.setDate(threeMonthsAgo.getDate() - 90);

export default {
  name: "MostPopularGenres",
  components: {
  },
  data() {
    return {
      periodOfTime: [threeMonthsAgo, new Date()],
      periodOfTimeTmp: null,
      dialog: false,
      mostPopularGenres: null,
      filters: {
        position: { value: null, matchMode: FilterMatchMode.CONTAINS },
        name: { value: null, matchMode: FilterMatchMode.CONTAINS },
        numberOfBorrowedCopies: { value: null, matchMode: FilterMatchMode.GREATER_THAN_OR_EQUAL_TO }
      },
    };
  },
  methods: {
    getMostPopularGenres() {
      StatisticsService.getMostPopularGenres().then((response) => {
        this.mostPopularGenres = response.data
      });
    },
    changePeriodOfTime() {
      if (this.dateFrom && this.dateTo) {
          this.getMostPopularGenresByPeriodOfTime();
      } else {
        this.getMostPopularGenres();
      }

      this.dialog = false;
    },
    getMostPopularGenresByPeriodOfTime() {
      StatisticsService.getMostPopularGenresByPeriodOfTime(this.dateFrom, this.dateTo).then((response) => {
        this.mostPopularGenres = response.data
      });
    },
    showDialog() {
      this.periodOfTimeTmp = this.periodOfTime;
      this.dialog = true;
    },
    cancel() {
      this.periodOfTime = this.periodOfTimeTmp;
      this.dialog = false;
    }
  },
  computed: {
    dateFrom() {
      if (this.periodOfTime) {
        return this.periodOfTime[0];
      }
      return null;
    },
    dateTo() {
      if (this.periodOfTime && this.periodOfTime[1]) {
        return this.periodOfTime[1];
      }
      return null;
    },
    periodOfTimeFormatted() {
      if (this.dateTo) {
        return moment(String(this.dateFrom)).format('DD/MM/YYYY') + ' - ' + moment(String(this.dateTo)).format('DD/MM/YYYY');
      }
      return 'Ostatni rok';
    }
  },
  mounted() {
    this.getMostPopularGenresByPeriodOfTime();
  }
};
</script>