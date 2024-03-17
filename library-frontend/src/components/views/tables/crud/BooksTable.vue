<template>
  <Header />
  <div class="flex justify-content-center">
    <div class="card">
      <Toolbar class="mb-4">
        <template #start>
          <Button label="Add" icon="pi pi-plus" severity="success" @click="bookDialog=true" outlined />
        </template>
      </Toolbar>

      <DataTable :value="books" tableStyle="width: 90vw" paginator :rows="10" :rowsPerPageOptions="[10, 20, 30]" 
      removableSort v-model:filters="filters" filterDisplay="row"
      :globalFilterFields="['title']">
        <template #header>
          <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
            <h4 class="m-0">Books</h4>
            <div class="flex justify-content-end gap-2">
              <span class="p-input-icon-left">
                <i class="pi pi-search" />
                <InputText v-model="filters['global'].value" placeholder="Keyword" />
              </span>
            </div>
          </div>
        </template>
        <template #empty> Could not find any books </template>
        <template #loading> Loading data, please wait. </template>

        <Column field="title" header="Title" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Title" />
          </template>
        </Column>

        <Column field="titleOriginal" header="Original title" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Original title" />
          </template>
        </Column>

        <Column field="language" header="Original language" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Original language" />
          </template>
          <template #body="slotProps">
            {{ slotProps.data.language.name }}
          </template>
        </Column>

        <Column field="authors" header="Authors" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Authors" />
          </template>
          <template #body="slotProps">
            {{ authorsToString(slotProps.data) }}
          </template>
        </Column>

        <Column field="genres" header="Genres" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Genres" />
          </template>
          <template #body="slotProps">
            {{ genresToString(slotProps.data) }}
          </template>
        </Column>

        <Column :exportable="false">
          <template #body="slotProps">
            <Button @click="book=slotProps.data; bookDialog=true; editChildDialog=true"
            icon="pi pi-pencil" outlined rounded class="mr-2" />
          </template>
        </Column>
      </DataTable>
    </div>
  </div>

  <span v-if="bookDialog">
    <span v-if="editChildDialog">
      <BookDialog :editMode="true" />
    </span>
    <span v-else>
      <BookDialog />
    </span>
  </span>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import { FilterMatchMode } from 'primevue/api';
import BookDialog from '@/components/dialogs/BookDialog.vue';
import BookService from '@/services/BookService';

export default {
  name: "BooksTable",
  components: {
    Header,
    BookDialog
  },
  data() {
    return {
      filters: {
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        title: { value: null, matchMode: FilterMatchMode.CONTAINS },
        titleOriginal: { value: null, matchMode: FilterMatchMode.CONTAINS },
        language: { value: null, matchMode: FilterMatchMode.CONTAINS },
        authors: { value: null, matchMode: FilterMatchMode.CONTAINS },
        genres: { value: null, matchMode: FilterMatchMode.CONTAINS }
      },
      books: null,
      book: null,
      bookDialog: false,
      editChildDialog: false
    };
  },
  methods: {
    authorsToString(book) {
      var string = ''
      for (var i = 0; i < book.authors.length; i++) {
        string += book.authors[i].name;
        if (book.authors.length - 1 > i) {
          string += ', ';
        }
      }
      return string;
    },
    genresToString(book) {
      var string = ''
      for (var i = 0; i < book.genres.length; i++) {
        string += book.genres[i].name;
        if (book.genres.length - 1 > i) {
          string += ', ';
        }
      }
      return string;
    },
    getBooks() {
      BookService.getAll().then((response) => {
        this.books = response.data;
      });
    }
  },
  mounted() {
    this.getBooks();
  },
};
</script>