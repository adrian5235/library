<template>
  <div style="display: flex; justify-content: center">
    <div class="card">
      <DataTable :value="mostPopularAuthors" tableStyle="width: 90vw" paginator :rows="10" :totalRecords="120"
      :rowsPerPageOptions="[10, 20, 30]" :sortOrder="1" removableSort v-model:filters="filters" dataKey="id" filterDisplay="row">
        <template #header>
          <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
            <h4 class="m-0">Najpopularniejsi autorzy</h4>
            <Button :label="periodOfTimeFormatted" icon="pi pi-calendar" text @click="showDialog()" />
          </div>
        </template>
        <template #empty> Nie znaleziono żadnych danych. </template>
        <template #loading> Trwa ładowanie danych. Proszę zaczekać. </template>

        <Column field="position" header="Pozycja" sortable style="width: 15%">
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()"
            class="p-column-filter" placeholder="Pozycja" />
          </template>
        </Column>

        <Column field="name" header="Imię i nazwisko" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value"  type="text"  @input="filterCallback()" 
            class="p-column-filter" placeholder="Imię i nazwisko" />
          </template>
        </Column>

        <Column field="numberOfLoanedCopies" header="Ilość wypożyczonych egzemplarzy" sortable style="width: 20%">
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()"
            class="p-column-filter" placeholder="Min. ilość wypożyczonych egzemplarzy" />
          </template>
        </Column>
      </DataTable>
    </div>

    <Dialog v-model:visible="dialog" :style="{width: '450px'}" header="Wybierz przedział czasu" :modal="true" class="p-fluid">
      <div class="field">
        <label for="periodOfTime">Przedział czasu</label>
        <div class="card flex justify-content-center">
          <Calendar v-model="periodOfTime" selectionMode="range" showIcon showButtonBar :manualInput="false" placeholder="Wybierz przedział czasu od do" />
        </div>
      </div>
      <template #footer>
        <Button label="Anuluj" icon="pi pi-times" @click="cancel()" outlined />
        <Button label="Zatwierdź" icon="pi pi-check" @click="changePeriodOfTime()" outlined />
      </template>
    </Dialog>
  </div>
</template>

<script>
import { FilterMatchMode } from "primevue/api";
import StatisticsService from "@/services/StatisticsService"
import moment from 'moment'

var threeMonthsAgo = new Date();
threeMonthsAgo.setDate(threeMonthsAgo.getDate() - 90);

export default {
  name: "MostPopularAuthors",
  components: {
  },
  data() {
    return {
      periodOfTime: [threeMonthsAgo, new Date()],
      periodOfTimeTmp: null,
      dialog: false,
      mostPopularAuthors: null,
      filters: {
        position: { value: null, matchMode: FilterMatchMode.CONTAINS },
        name: { value: null, matchMode: FilterMatchMode.CONTAINS },
        numberOfLoanedCopies: { value: null, matchMode: FilterMatchMode.GREATER_THAN_OR_EQUAL_TO }
      },
    };
  },
  methods: {
    getMostPopularAuthors() {
      StatisticsService.getMostPopularAuthors().then((response) => {
        this.mostPopularAuthors = response.data
      });
    },
    changePeriodOfTime() {
      if (this.dateFrom && this.dateTo) {
          this.getMostPopularAuthorsByPeriodOfTime();
      } else {
        this.getMostPopularAuthors();
      }

      this.dialog = false;
    },
    getMostPopularAuthorsByPeriodOfTime() {
      StatisticsService.getMostPopularAuthorsByPeriodOfTime(this.dateFrom, this.dateTo).then((response) => {
        this.mostPopularAuthors = response.data
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
    this.getMostPopularAuthorsByPeriodOfTime();
  }
};
</script>