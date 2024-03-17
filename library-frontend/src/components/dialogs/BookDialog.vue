<template>
  <Dialog visible :style="{ width: '500px' }" :header="header" :modal="true" class="p-fluid">
    <Form @submit="saveBook()" :validation-schema="schema" v-slot="{ errors }">
      <div class="flex flex-column py-4 gap-5">
        <div class="fieldWrapper">
          <Field v-model="title" name="title">
            <span class="p-float-label">
              <InputText v-model="title" aria-describedby="title-help" :class="{ 'p-invalid': errors.title }" />
              <label>Title</label>
            </span>
          </Field>
          <small id="title-help" class="p-error">
            {{ errors.title }}
          </small>
        </div>

      <div class="flex justify-between gap-3">
        <div class="fieldWrapper">
          <Field v-model="titleOriginal" name="titleOriginal">
            <span class="p-float-label">
              <InputText v-model="titleOriginal" aria-describedby="titleOriginal-help" :class="{ 'p-invalid': errors.titleOriginal }" />
              <label>Original title</label>
            </span>
          </Field>
          <small id="titleOriginal-help" class="p-error">
            {{ errors.titleOriginal }}
          </small>
        </div>
        </div>

        <div class="flex justify-between gap-3">
          <div class="fieldWrapper">
            <Field v-model="language" name="language">
              <span class="p-float-label">
                <Dropdown v-model="language" :options="languages" optionLabel="name" filter
                aria-describedby="language-help" :class="{ 'p-invalid': errors.language }" />
                <label>Original language</label>
              </span>
            </Field>
            <small id="language-help" class="p-error">
              {{ errors.language }}
            </small>
          </div>
          <div class="flex gap-2">
            <Button v-if="language" icon="pi pi-pencil" severity="info" 
            @click="editChildDialog=true; languageDialog=true" outlined />
            <Button icon="pi pi-plus" severity="success" @click="languageDialog=true" outlined />
          </div>
        </div>

        <div class="flex justify-between gap-3">
          <div class="fieldWrapper">
            <Field v-model="bookAuthors" name="authors">
              <span class="p-float-label">
                <MultiSelect v-model="bookAuthors" :options="authors" optionLabel="name" filter 
                aria-describedby="authors-help" :class="{ 'p-invalid': errors.authors }" />
                <label>Authors</label>
              </span>
            </Field>
            <small id="authors-help" class="p-error">
              {{ errors.authors }}
            </small>
          </div>
          <Button icon="pi pi-plus" severity="success" @click="authorDialog = true" outlined />
        </div>

        <div class="flex justify-between gap-3">
          <div class="fieldWrapper">
             <Field v-model="bookGenres" name="genres">
              <span class="p-float-label">
                <MultiSelect v-model="bookGenres" :options="genres" optionLabel="name" filter 
                aria-describedby="genres-help" :class="{ 'p-invalid': errors.genres }" />
                <label>Genres</label>
              </span>
            </Field>
            <small id="genres-help" class="p-error">
              {{ errors.genres }}
            </small>
          </div>
          <Button icon="pi pi-plus" severity="success" @click="genreDialog = true" outlined />
        </div>

        <div >
          <Field v-model="description" name="description">
            <span class="p-float-label">
              <Textarea v-model="description" rows="5" cols="30"
              aria-describedby="description-help" :class="{ 'p-invalid': errors.description }" />
              <label>Description</label>
            </span>
          </Field>
          <small id="description-help" class="p-error">
            {{ errors.description }}
          </small>
        </div>
      </div>

      <div class="footer">
        <Button label="Cancel" class="p-button-secondary" icon="pi pi-times" @click="hideDialog()" outlined />
        <Button type="submit" label="Save" icon="pi pi-check" outlined />
      </div>
    </Form>
  </Dialog>

  <span v-if="languageDialog">
    <span v-if="editChildDialog">
      <LanguageDialog :editMode="true" />
    </span>
    <span v-else>
      <LanguageDialog />
    </span>
  </span>

  <span v-if="authorDialog">
    <AuthorDialog />
  </span>

  <span v-if="genreDialog">
    <GenreDialog />
  </span>
</template>

<script>
import AuthorService from '@/services/AuthorService';
import LanguageService from '@/services/LanguageService';
import LanguageDialog from "./LanguageDialog.vue";
import GenreService from '@/services/GenreService';
import BookService from '@/services/BookService';
import AuthorDialog from "./AuthorDialog.vue";
import GenreDialog from "./GenreDialog.vue";
import { Form, Field } from 'vee-validate';
import * as yup from 'yup';


export default {
  name: "BookDialog",
  components: {
    Form, Field,
    LanguageDialog,
    AuthorDialog,
    GenreDialog
  },
  props: {
    editMode: Boolean
  },
  data() {
    const schema = yup.object({
      title: yup.string().required('The title is required'),
      titleOriginal: yup.string().nullable(),
      language: yup.object().required('The language is required'),
      authors: yup.array().min(1, 'At least one author is required'),
      genres: yup.array().min(1, 'At least one genre is required'),
      description: yup.string().nullable()
    });

    return {
      schema,
      header: null,
      book: {
        title: null,
      },
      title: null,
      titleOriginal: null,
      language: null,
      bookAuthors: [],
      bookGenres: [],
      description: null,
      authors: null,
      genres: null,  
      languages: null,
      selectedAuthors: null,
      languageDialog: false,
      authorDialog: false,
      genreDialog: false,
      editChildDialog: false
    };
  },
  methods: {
    saveBook() {
      this.book.title = this.title;
      this.book.titleOriginal = this.titleOriginal;
      this.book.language = this.language;
      this.book.authors = this.bookAuthors;
      this.book.genres = this.bookGenres;
      this.book.description = this.description;

      if (this.editMode) /* update */ {
        BookService.update(this.book).then((response) => {
          this.$toast.add({severity:'success', summary: 'Success', detail: 'The book has been updated', life: 10000});
          if (response.status == 200) {
            BookService.get(response.data.id).then((result) => {
              for (var i = 0; i < this.$parent.books.length; i++) {
                if (this.$parent.books[i].id == result.data.id) {
                  this.$parent.books[i] = result.data;
                }
              }
              if (this.$parent.$parent.book) {
                this.$parent.$parent.book = result.data;
                if (localStorage.getItem('book')) {
                  localStorage.setItem('book', JSON.stringify(result.data))
                }
              }
            })
          } else {
            this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not update the book', life: 10000});
          }
        })
      } else /* create */ {
        BookService.create(this.book).then((response) => {
          if (response.status == 200) {
            this.$toast.add({severity:'success', summary: 'Success', detail: 'The new book has been saved', life: 10000});
            BookService.get(response.data.id).then((result) => {
              this.$parent.books.push(result.data);
              this.$parent.book = result.data;
              if (this.$parent.$parent.book) {
                this.$parent.$parent.book = result.data;
                if (localStorage.getItem('book')) {
                  localStorage.setItem('book', JSON.stringify(result.data))
                }
              }
            })
          } else {
            this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not save the new book', life: 10000});
          }
        });
      }
      this.hideDialog();
    },
    sameAuthors(book1, book2) {
      var authors1 = book1.authors;
      var authors2 = book2.authors;

      if (authors1.length != authors2.length) { return false; }

      for (var i = 0; i < authors1.length; i++) {
        var found = false;
        for (var j = 0; j < authors2.length; j++) {
          if (authors1[i].name == authors2[j].name) { found = true; }
        }
        if (!found) { return false; }
      }
      return true;
    },
    hideDialog() {
      this.$parent.bookDialog = false;
      this.$parent.editChildDialog = false;
    },
    getAuthors() {
      AuthorService.getAll().then((response) => {
        this.authors = response.data;
        this.loading = false;
      })
    },
    getGenres() {
      GenreService.getAll().then((response) => {
        this.genres = response.data;
      })
    },
    getLanguages() {
      LanguageService.getAll().then((response) => {
        this.languages = response.data;
      })
    },
  },
  mounted() {
    this.getLanguages();
    this.getAuthors();
    this.getGenres();

    if (this.editMode) {
      this.header = 'Edit book';
      this.book = this.$parent.book;
      this.title = this.book.title;
      this.titleOriginal = this.book.titleOriginal;
      this.language = this.book.language;
      this.bookAuthors = this.book.authors;
      this.bookGenres = this.book.genres;
      this.description = this.book.description;
    } else {
      this.header = 'New book';
    }
  }
};
</script>