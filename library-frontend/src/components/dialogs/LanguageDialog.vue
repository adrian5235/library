<template>
  <Dialog visible :style="{ width: '450px' }" :header="header" :modal="true" class="p-fluid">
    <Form @submit="saveLanguage()" :validation-schema="schema" v-slot="{ errors }">
      <div class="py-4">
        <Field v-model="name" name="name">
          <span class="p-float-label">
            <InputText v-model="name" autofocus aria-describedby="name-help" :class="{ 'p-invalid': errors.name }" />
            <label>Name</label>
          </span>
        </Field>
        <small id="name-help" class="p-error">
          {{ errors.name }}
        </small>
      </div>

      <div class="footer">
        <Button label="Cancel" class="p-button-secondary" icon="pi pi-times" @click="hideDialog()" outlined />
        <Button type="submit" label="Save" icon="pi pi-check" outlined />
      </div>
    </Form>
  </Dialog>
</template>

<script>
import { Form, Field } from 'vee-validate';
import * as yup from 'yup';
import LanguageService from '@/services/LanguageService';

export default {
  name: "LanguageDialog",
  components: {
    Form, Field
  },
  props: {
    editMode: Boolean
  },
  data() {
    const schema = yup.object({
      name: yup.string().required('Name is required')
    });

    return {
      schema,
      language: {
        name: null
      },
      name: null,
      header: null
    };
  },
  methods: {
    saveLanguage() {
      this.language.name = this.name;
      if (this.editMode) /* update */ {
        LanguageService.update(this.language).then((response) => {
          if (response.status == 200) {
            this.$toast.add({severity:'success', summary: 'Success', detail: 'The language has been updated', life: 10000});
            for (var i = 0; i < this.$parent.languages.length; i++) {
              if (this.$parent.languages[i].id == response.data.id) {
                this.$parent.languages[i] = response.data;
              }
            }
          } else {
            this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not update the language', life: 10000});
          }
        });
      } else /* create */ {
        // to check if a record isn't being duplicated
        var existingLanguage = this.$parent.languages.find((language) => language.name == this.language.name);
        if (!existingLanguage) {
          LanguageService.create(this.language).then((response) => {
            if (response.status == 200) {
              this.$toast.add({severity:'success', summary: 'Success', detail: 'The new language has been saved', life: 10000});
              this.$parent.languages.push(response.data);
              this.$parent.language = response.data;
            } else {
              this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not save the new language', life: 10000});
            }
          });
        } else {
          this.$parent.language = existingLanguage;
        }
      }
      this.hideDialog();
    },
    hideDialog() {
      this.$parent.languageDialog = false;
      this.$parent.editChildDialog = false;
    }
  },
  mounted() {
    if (this.editMode) {
      this.header = 'Edit language';
      this.language = this.$parent.language;
      this.name = this.language.name;
    } else {
      this.header = 'New language';
    }
  }
};
</script>