<template>
  <Dialog visible :style="{ width: '500px' }" :header="header" :modal="true" class="p-fluid">
    <Form @submit="saveEdition()" :validation-schema="schema" v-slot="{ errors }">
      <div class="flex flex-column gap-5 py-4">
        <div class="fieldWrapper">
          <Field v-model="isbn13" name="isbn">
            <span class="p-float-label">
              <InputText v-model="isbn13" aria-describedby="title-help" :class="{ 'p-invalid': errors.isbn }" />
              <label>Numer isbn</label>
            </span>
          </Field>
          <small id="isbn-help" class="p-error">
            {{ errors.isbn }}
          </small>
        </div>

        <div class="flex justify-between gap-3">
          <div class="fieldWrapper">
            <Field v-model="language" name="language">
              <span class="p-float-label">
                <Dropdown v-model="language" :options="languages" optionLabel="name" filter
                aria-describedby="language-help" :class="{ 'p-invalid': errors.language }" />
                <label>Język</label>
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
            <Field v-model="binding" name="binding">
              <span class="p-float-label">
                <Dropdown v-model="binding" :options="bindings" optionLabel="name" 
                aria-describedby="binding-help" :class="{ 'p-invalid': errors.binding }" />
                <label>Oprawa</label>
              </span>
            </Field>
            <small id="binding-help" class="p-error">
              {{ errors.binding }}
            </small>
          </div>
          <div class="flex gap-2">
            <Button v-if="binding" icon="pi pi-pencil" severity="info" 
            @click="editChildDialog=true; bindingDialog=true" outlined />
            <Button icon="pi pi-plus" severity="success" @click="bindingDialog=true" outlined />
          </div>
        </div>

        <div class="flex justify-between gap-3">
          <div class="fieldWrapper">
            <Field v-model="book" name="book">
              <span class="p-float-label">
                <Dropdown v-model="book" :options="books" optionLabel="title" filter
                aria-describedby="book-help" :class="{ 'p-invalid': errors.book }" />
                <label>Książka</label>
              </span>
            </Field>
            <small id="book-help" class="p-error">
              {{ errors.book }}
            </small>
          </div>
          <div class="flex gap-2">
            <Button v-if="book" icon="pi pi-pencil" severity="info" 
            @click="editChildDialog=true; bookDialog=true" outlined />
            <Button icon="pi pi-plus" severity="success" @click="bookDialog = true" outlined />
          </div>
        </div>

        <div class="flex justify-between gap-3">
          <div class="fieldWrapper">
            <Field v-model="publisher" name="publisher">
              <span class="p-float-label">
                <Dropdown v-model="publisher" :options="publishers" optionLabel="name" filter
                aria-describedby="publisher-help" :class="{ 'p-invalid': errors.publisher }" />
                <label>Wydawnictwo</label>
              </span>
            </Field>
            <small id="publisher-help" class="p-error">
              {{ errors.publisher }}
            </small>
          </div>
          <div class="flex gap-2">
            <Button v-if="publisher" icon="pi pi-pencil" severity="info" 
            @click="editChildDialog=true; publisherDialog=true" outlined />
            <Button icon="pi pi-plus" severity="success" @click="publisherDialog=true" outlined />
          </div>
        </div>

        <div class="flex justify-between gap-3">
          <div class="fieldWrapper">
            <Field v-model="translator" name="translator">
              <span class="p-float-label">
                <Dropdown v-model="translator" :options="translators" optionLabel="name" filter
                aria-describedby="translator-help" :class="{ 'p-invalid': errors.translator }" />
                <label>Tłumacz</label>
              </span>
            </Field>
            <small id="translator-help" class="p-error">
              {{ errors.translator }}
            </small>
          </div>
          <div class="flex gap-2">
            <Button v-if="translator" icon="pi pi-pencil" severity="info" 
            @click="editChildDialog=true; translatorDialog=true" outlined />
            <Button icon="pi pi-plus" severity="success" @click="translatorDialog=true" outlined />
          </div>
        </div>

        <div class="fieldWrapper">
          <Field v-model="releaseYear" name="releaseYear">
            <span class="p-float-label">
              <InputText v-model="releaseYear" aria-describedby="releaseYear-help" :class="{ 'p-invalid': errors.releaseYear }" />
              <label>Rok wydania</label>
            </span>
          </Field>
          <small id="releaseYear-help" class="p-error">
            {{ errors.releaseYear }}
          </small>
        </div>

        <div class="fieldWrapper">
          <Field v-model="numberOfPages" name="numberOfPages">
            <span class="p-float-label">
              <InputText v-model="numberOfPages" aria-describedby="numberOfPages-help" :class="{ 'p-invalid': errors.numberOfPages }" />
              <label>Ilość stron</label>
            </span>
          </Field>
          <small id="numberOfPages-help" class="p-error">
            {{ errors.numberOfPages }}
          </small>
        </div>

        <div class="fieldWrapper">
          <Field v-model="weight" name="weight">
            <span class="p-float-label">
              <InputText v-model="weight" aria-describedby="weight-help" :class="{ 'p-invalid': errors.weight }" />
              <label>Waga</label>
            </span>
          </Field>
          <small id="weight-help" class="p-error">
            {{ errors.weight }}
          </small>
        </div>

        <div class="fieldWrapper">
          <Field v-model="dimensions" name="dimensions">
            <span class="p-float-label">
              <InputText v-model="dimensions" aria-describedby="dimensions-help" :class="{ 'p-invalid': errors.dimensions }" />
              <label>Wymiary</label>
            </span>
          </Field>
          <small id="dimensions-help" class="p-error">
            {{ errors.dimensions }}
          </small>
        </div>
      </div>

      <div class="footer">
        <Button label="Anuluj" class="p-button-secondary" icon="pi pi-times" @click="hideDialog()" outlined />
        <Button type="submit" label="Zapisz" icon="pi pi-check" outlined />
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

  <span v-if="bindingDialog">
    <span v-if="editChildDialog">
      <BindingDialog :editMode="true" />
    </span>
    <span v-else>
      <BindingDialog />
    </span>
  </span>

  <span v-if="bookDialog">
    <span v-if="editChildDialog">
      <BookDialog :editMode="true" />
    </span>
    <span v-else>
      <BookDialog />
    </span>
  </span>

  <span v-if="publisherDialog">
    <span v-if="editChildDialog">
      <PublisherDialog :editMode="true" />
    </span>
    <span v-else>
      <PublisherDialog />
    </span>
  </span>

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
import LanguageService from '@/services/LanguageService';
import BindingService from '@/services/BindingService';
import PublisherService from '@/services/PublisherService';
import TranslatorService from '@/services/TranslatorService';
import LanguageDialog from "./LanguageDialog.vue";
import BindingDialog from "./BindingDialog.vue";
import BookDialog from "./BookDialog.vue";
import PublisherDialog from "./PublisherDialog.vue";
import TranslatorDialog from "./TranslatorDialog.vue";
import BookService from '@/services/BookService';
import EditionService from '@/services/EditionService';
import { Form, Field } from 'vee-validate';
import * as yup from 'yup';

export default {
  name: "EditionDialog",
  components: {
    Form, Field,
    LanguageDialog,
    BindingDialog,
    BookDialog,
    PublisherDialog,
    TranslatorDialog
  },
  props: {
    editMode: Boolean
  },
  data() {
    const schema = yup.object({
      isbn: yup.string().trim().required('Numer isbn jest wymagany').length(13, 'Numer isbn musi składać się z 13 znaków'),
      book: yup.object().required('Książka jest wymagana'),
      publisher: yup.object().required('Wydawnictwo jest wymagane'),
      translator: yup.object().required('Tłumacz jest wymagany'),
      releaseYear: yup.number().typeError('Rok musi składać się z cyfr').required('Rok wydania jest wymagany'),
      language: yup.object().required('Język jest wymagany'),
      binding: yup.object().required('Oprawa jest wymgana'),
      numberOfPages: yup.number().typeError('Ilość stron musi składać się z cyfr').required('Ilość stron jest wymagana'),
      weight: yup.string().required('Waga jest wymagana'),
      dimensions: yup.string().required('Wymiary są wymagane')
    });

    return {
      schema,
      header: null,
      edition: {
        isbn13: null
      },
      isbn13: null,
      releaseYear: null,
      language: null,
      binding: null,
      numberOfPages: null,
      weight: null,
      dimensions: null,
      quantity: null,
      book: null,
      publisher: null,
      translator: null,
      books: null,
      languages: null,
      bindings: null,
      publishers: null,
      translators: null,
      editionImage: null,
      languageDialog: false,
      bindingDialog: false,
      bookDialog: false,
      publisherDialog: false,
      translatorDialog: false,
      editChildDialog: false
    };
  },
  methods: {
    saveEdition() {
      this.edition.isbn13 = this.isbn13;
      this.edition.releaseYear = this.releaseYear;
      this.edition.language = this.language;
      this.edition.binding = this.binding;
      this.edition.numberOfPages = this.numberOfPages;
      this.edition.weight = this.weight;
      this.edition.dimensions = this.dimensions;
      this.edition.quantity = this.quantity;
      this.edition.book = this.book;
      this.edition.publisher = this.publisher;
      this.edition.translator = this.translator;

      if (this.editMode) {
        // update
        EditionService.update(this.edition).then((response) => {
          if (response.status == 200) {
            this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Zaktualizowano wydanie', life: 10000});
            if (this.editionImage) {
              this.uploadEditionImage(response.data.id);
            }
            // this.$router.go();
          } else {
            this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Nie udało się zaktualizować wydania', life: 10000});
          }
        });
      } else /* create */ {
        EditionService.create(this.edition).then((response) => {
          if (response.status == 200) {
            this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Zapisano nowe wydanie', life: 10000});
            this.$parent.editions.push(response.data);
            if (this.$parent.copy) {
              this.$parent.edition = response.data;
              this.$parent.$parent.editions.push(response.data);
            }
            if (this.editionImage) {
              this.uploadEditionImage(response.data.edition.id);
            }
          } else {
            this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Nie udało się zapisać nowego wydania', life: 10000});
          }
        })
      }
      this.hideDialog();
    },
    hideDialog() {
      this.$parent.editionDialog = false;
      this.$parent.editChildDialog = false;
    },
    getBooks() {
      BookService.getAll().then((response) => {
        this.books = response.data;
      })
    },
    getLanguages() {
      LanguageService.getAll().then((response) => {
        this.languages = response.data;
      })
    },
    getBindings() {
      BindingService.getAll().then((response) => {
        this.bindings = response.data;
      })
    },
    getPublishers() {
      PublisherService.getAll().then((response) => {
        this.publishers = response.data;
      })
    },
    getTranslators() {
      TranslatorService.getAll().then((response) => {
        this.translators = response.data;
      })
    }
  },
  mounted() {
    this.getBooks();
    this.getLanguages();
    this.getBindings();
    this.getPublishers();
    this.getTranslators();

    if (this.editMode) {
      this.header = 'Edycja wydania';
      this.edition = this.$parent.edition;
      this.isbn13 = this.edition.isbn13;
      this.releaseYear = this.edition.releaseYear;
      this.language = this.edition.language;
      this.binding = this.edition.binding;
      this.numberOfPages = this.edition.numberOfPages;
      this.weight = this.edition.weight;
      this.dimensions = this.edition.dimensions;
      this.quantity = this.edition.quantity;
      BookService.get(this.edition.book.id).then((response) => {
        this.book = response.data;
      });
      this.publisher = this.edition.publisher;
      this.translator = this.edition.translator;
    } else {
      this.header = 'Nowe wydanie';
    }
  }
};
</script>
