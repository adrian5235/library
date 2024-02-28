<template>
  <Header />
  <div class="flex justify-content-center">
    <div class="card">
      <Toolbar class="border-top-none; border-left-none; border-right-none">
        <template #start>
          <h3 class="m-0">Książki</h3>
        </template>
        <template #center>
          <div class="flex flex-wrap gap-2">
            <Button v-if="showOnlyAvailable" rounded class="mr-2" @click="showOnlyAvailable = false; filter()" label="Pokaż tylko dostępne" />
            <Button v-else rounded outlined class="mr-2" @click="showOnlyAvailable = true; filter()" label="Pokaż tylko dostępne" />
            <MultiSelect v-model="selectedGenres" :options="genres" optionLabel="name" placeholder="Wybierz gatunki"
              filter @change="filter()" />
            <Button v-if="selectedGenres && selectedGenres.length" rounded outlined class="mr-2" icon="pi pi-times" @click="selectedGenres = null; filter()" />
            <MultiSelect v-model="selectedAuthors" :options="authors" optionLabel="name" placeholder="Wybierz autorów"
              filter @change="filter()" />
            <Button v-if="selectedAuthors && selectedAuthors.length" rounded outlined class="mr-2" icon="pi pi-times" @click="selectedAuthors = null; filter()" />
            <span class="p-input-icon-left">
              <i class="pi pi-search" />
              <InputText v-model="searchValue" placeholder="Słowo klucz" @keyup="filter()" />
            </span>
          </div>
        </template>
      </Toolbar>
      
      <DataView :value="editions" layout="grid" paginator :rows="6" :rowsPerPageOptions="[6, 12, 18, 24]" style="width: 90vw">
        <template #grid="slotProps">
          <div class="col-12 sm:col-6 lg:col-4 xl:col-2 p-2">
            <div class="p-4 border-1 surface-border surface-card border-round">
              <div class="flex flex-wrap align-items-center justify-content-between gap-2">
                <span class="overflow-hidden white-space-nowrap text-overflow-ellipsis" style="max-width: 56%">{{ genresToString(slotProps.data.book.genres) }}</span>
                <Tag v-if="slotProps.data.quantity > 0" value="dostępna" severity="success"></Tag>
                <Tag v-else value="niedostępna" severity="danger"></Tag>
              </div>
              <div class="flex flex-column align-items-center gap-3 py-5">
                <img v-if="slotProps.data.image" class="shadow-2 border-round" style="height: 150px" :src="require('@/assets/img/' + slotProps.data.image.name)" />
                <img v-else class="shadow-2 border-round" style="height: 150px" src="@/assets/no-image.png" />                            
                <span class="font-bold overflow-hidden white-space-nowrap text-overflow-ellipsis" style="max-width: 100%">{{ slotProps.data.book.title }}</span>
                <span class="overflow-hidden white-space-nowrap text-overflow-ellipsis" style="max-width: 100%">{{ authorsToString(slotProps.data.book.authors) }}</span>
                <div class="flex gap-3">
                  <Button label="Wybierz" @click="this.$router.push({
                    name: 'edition',
                    params: {
                      edition: JSON.stringify(slotProps.data),
                      editionId: slotProps.data.id,
                      book: JSON.stringify(slotProps.data.book),
                      bookId: slotProps.data.book.id
                    }
                  })" outlined>
                  </Button>
                </div>
              </div>
            </div>
          </div>
        </template>

        <template #paginatorstart />
        <template #paginatorend>
          <Button v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'" @click="copyDialog = true" icon="pi pi-plus" severity="success" outlined />
        </template>
      </DataView>
    </div>
  </div>

  <span v-if="copyDialog">
    <CopyDialog />
  </span>
</template>


<script>
import Header from "@/components/nav/Header.vue";
import CopyDialog from "@/components/dialogs/CopyDialog.vue";
import EditionService from '@/services/EditionService';

export default {
  name: "BooksGridView",
  components: {
    Header,
    CopyDialog
  },
  data() {
    return {
      userRole: null,
      edition: null,
      editions: null,
      allEditions: null,
      allEditions2: null,
      showOnlyAvailable: false,
      searchValue: null,
      selectedAuthors: null,
      selectedGenres: null,
      copyDialog: false
    };
  },
  methods: {
    async getEditions() {
      await EditionService.getOnePerBook().then((response) => {
        this.editions = response.data;
        this.allEditions = response.data;
      });
    },
    getAllEditions() {
      EditionService.getAll().then((response) => {
        this.allEditions2 = response.data;
      })
    },
    authorsToString(authors) {
      var string = '';
      for (var i in authors) {
        if (i > 0) {
          string += ', '
        }
        string += authors[i].name;
      }
      return string;
    },
    genresToString(genres) {
      var string = '';
      for (var i in genres) {
        if (i > 0) {
          string += ', '
        }
        string += genres[i].name;
      }
      return string;
    },
    filter() {
      var filteredEditions = this.allEditions;
      
      if (this.showOnlyAvailable) {
        filteredEditions = filteredEditions.filter((edition) => edition.quantity > 0);
      }

      if (this.searchValue) {
        filteredEditions = filteredEditions.filter((edition) => edition.book.title.toLowerCase().includes(this.searchValue.toLowerCase().trim()) 
          || edition.book.genres.some((genre) => genre.name.toLowerCase().includes(this.searchValue.toLowerCase().trim()))
          || edition.book.authors.some((author) => author.name.toLowerCase().includes(this.searchValue.toLowerCase().trim()))
          || edition.isbn13 == this.searchValue);

        if (filteredEditions.length == 0 && this.searchValue.length == 13) {
          filteredEditions = this.allEditions2.filter((edition) => edition.isbn13 == this.searchValue);
        }
      }

      if (this.selectedGenres && this.selectedGenres.length) {
        filteredEditions = filteredEditions.filter(edition => edition.book.genres.some(genre => this.selectedGenresNames.includes(genre.name)));
      }

      if (this.selectedAuthors && this.selectedAuthors.length) {
        filteredEditions = filteredEditions.filter(edition => edition.book.authors.some(author => this.selectedAuthorsNames.includes(author.name)));
      }

      this.editions = filteredEditions;
    }
  },
  mounted() {
    this.userRole = localStorage.getItem('user-role');
    this.getEditions();
    this.getAllEditions();
  },
  computed: {
    genres() {
      const genres = [];
      for (var i in this.allEditions) {
        for (var j in this.allEditions[i].book.genres) {
          var bookGenre = this.allEditions[i].book.genres[j];
          if (!genres.some(genre => genre.name == bookGenre.name)) {
            genres.push(bookGenre);
          }
        }
      }
      return genres;
    },
    authors() {
      const authors = [];
      for (var i in this.allEditions) {
        for (var j in this.allEditions[i].book.authors) {
          var bookAuthor = this.allEditions[i].book.authors[j];
          if (!authors.some(author => author.name == bookAuthor.name)) {
            authors.push(bookAuthor);
          }
        }
      }
      return authors;
    },
    selectedGenresNames() {
      const names = [];
      for (var i in this.selectedGenres) {
        names.push(this.selectedGenres[i].name);
      }
      return names;
    },
    selectedAuthorsNames() {
      const names = [];
      for (var i in this.selectedAuthors) {
        names.push(this.selectedAuthors[i].name);
      }
      return names;
    }
  }
};
</script>