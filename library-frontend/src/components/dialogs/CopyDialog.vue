<template>
  <Dialog visible :style="{ width: '500px' }" :header="header" :modal="true" class="p-fluid">
    <Form @submit="saveCopy()" :validation-schema="schema" v-slot="{ errors }">
      <div class="flex flex-column gap-5 py-4">
        <div class="fieldWrapper">
          <Field v-model="signature" name="signature">
            <span class="p-float-label">
              <InputText v-model="signature" aria-describedby="signature-help" :class="{ 'p-invalid': errors.signature }" />
              <label>Sygnatura</label>
            </span>
          </Field>
          <small id="signature-help" class="p-error">
            {{ errors.signature }}
          </small>
        </div>

        <div class="fieldWrapper">
          <Field v-model="purchaseDate" name="purchaseDate" v-slot="{ field }">
            <span class="p-float-label">
              <Calendar v-bind="field" v-model="purchaseDate" :manualInput="false" showIcon showButtonBar
              aria-describedby="purchaseDate-help" :class="{ 'p-invalid': errors.purchaseDate }" />
              <label>Data zakupu</label>
            </span>
          </Field>
          <small id="purchaseDate-help" class="p-error">
            {{ errors.purchaseDate }}
          </small>
        </div>

        <div class="fieldWrapper">
          <Field v-model="price" name="price">
            <span class="p-float-label">
              <InputText v-model="price" aria-describedby="price-help" :class="{ 'p-invalid': errors.price }" />
              <label>Cena zakupu</label>
            </span>
          </Field>
          <small id="price-help" class="p-error">
            {{ errors.price }}
          </small>
        </div>

        <div class="flex justify-between gap-3">
          <div class="fieldWrapper">
            <Field v-model="edition" name="edition" >
              <span class="p-float-label">
                <Dropdown v-model="edition" :options="editions" optionLabel="isbn13" filter 
                aria-describedby="edition-help" :class="{ 'p-invalid': errors.edition }" />
                <label>Wydanie</label>
              </span>
            </Field>
            <small id="edition-help" class="p-error">
              {{ errors.edition }}
            </small>
          </div>
          <div class="flex gap-2">
            <Button v-if="edition" icon="pi pi-pencil" severity="info" 
            @click="editChildDialog=true; editionDialog=true" outlined />
            <Button icon="pi pi-plus" severity="success" @click="editionDialog=true" outlined />
          </div>
        </div>

        <div class="fieldWrapper">
          <Field v-model="available" name="available">
            <div class="flex flex-wrap gap-3">
              <div class="flex align-items-center">
                <RadioButton v-model="available" :value="true" />
                <label class="ml-2">Dostępny</label>
              </div>
              <div class="flex align-items-center">
                <RadioButton v-model="available" :value="false" />
                <label class="ml-2">Niedostępny</label>
              </div>
            </div>
          </Field>
          <small id="available-help" class="p-error">
            {{ errors.available }}
          </small>
        </div>
      </div>

      <div class="footer">
        <Button label="Anuluj" class="p-button-secondary" icon="pi pi-times" @click="hideDialog()" outlined />
        <Button type="submit" label="Zapisz" icon="pi pi-check" outlined />
      </div>
    </Form>
  </Dialog>

  <span v-if="editionDialog">
    <span v-if="editChildDialog">
      <EditionDialog :editMode="true" />
    </span>
    <span v-else>
      <EditionDialog />
    </span>
  </span>
</template>

<script>
import { Form, Field } from 'vee-validate';
import * as yup from 'yup';
import CopyService from '@/services/CopyService';
import EditionDialog from "./EditionDialog.vue";
import EditionService from '@/services/EditionService';
import StorageService from '@/services/StorageService';

export default {
  name: "CopyDialog",
  components: {
    Form, Field,
    EditionDialog
  },
  props: {
    editMode: Boolean
  },
  data() {
    const schema = yup.object({
      signature: yup.string().trim().required('Sygnatura jest wymagana'),
      purchaseDate: yup.date().required('Data zakupu jest wymagana'),
      price: yup.number().typeError('Cena zakupu musi być liczbą').required('Cena zakupu jest wymagana'),
      edition: yup.object().required('Wydanie jest wymagane')
    });

    return {
      schema,
      copy: {
        signature: null
      },
      signature: null,
      purchaseDate: null,
      price: null,
      edition: null,
      available: true,
      editionImage: null,
      header: null,
      editions: [],
      editionDialog: false,
      editChildDialog: false
    };
  },
  methods: {
    saveCopy() {
      this.copy.signature = this.signature;
      this.copy.purchaseDate = this.purchaseDate;
      this.copy.price = this.price;
      this.copy.edition = this.edition;
      this.copy.available = this.available;

      if (this.editMode) /* update */ {
        CopyService.update(this.copy).then((response) => {
          if (response.status == 200) {
            this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Zaktualizowano egzemplarz', life: 10000});
            
          } else {
            this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Nie udało się zaktualizować egzemplarza', life: 10000});
          }
        })
      } else /* create */ {
        CopyService.create(this.copy).then((response) => {
          if (response.status == 200) {
            this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Zapisano nowy egzemplarz', life: 10000});
            if (this.$parent.copy) {
              this.$parent.copies.push(response.data);
            }
            if (this.editionImage) {
              this.uploadEditionImage(response.data.edition.id);
            }
          } else {
            this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Nie udało się zapisać nowego egzemplarza', life: 10000});
          }
        })
      }
      this.hideDialog();
    },
    uploadEditionImage(edition) {
      StorageService.uploadEditionImage(this.editionImage, edition);
    },
    getEditions() {
      EditionService.getAll().then((response) => {
        this.editions = response.data;
      })
    },
    hideDialog() {
      this.$parent.copyDialog = false;
      this.$parent.editChildDialog = false;
    }
  },
  mounted() {
    if (this.editMode) {
      this.header = 'Edycja egzemplarza';
      this.copy = this.$parent.copy;
      this.signature = this.copy.signature;
      this.purchaseDate = this.copy.purchaseDate;
      this.price = this.copy.price;
      this.edition = this.copy.edition;
      this.available = this.copy.available;
    } else {
      this.header = 'Nowy egzemplarz';
    }

    this.getEditions();
  }
};
</script>