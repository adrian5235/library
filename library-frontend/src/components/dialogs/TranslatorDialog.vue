<template>
  <Dialog visible :style="{ width: '450px' }" :header="header" :modal="true" class="p-fluid">
    <Form @submit="saveTranslator()" :validation-schema="schema" v-slot="{ errors }">
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
import TranslatorService from '@/services/TranslatorService';

export default {
  name: "TranslatorDialog",
  components: {
    Form, Field
  },
  props: {
    editMode: Boolean
  },
  data() {
    const schema = yup.object({
      name: yup.string().trim().required('Name is required')
    });

    return {
      schema,
      translator: {
        name: null
      },
      name: null,
      header: null
    };
  },
  methods: {
    saveTranslator() {
      this.translator.name = this.name;

      if (this.editMode) /* update */ {
        TranslatorService.update(this.translator).then((response) => {
          if (response.status == 200) {
            this.$toast.add({severity:'success', summary: 'Success', detail: 'The translator has been updated', life: 10000});
            for (var i = 0; i < this.$parent.translators.length; i++) {
              if (this.$parent.translators[i].id == response.data.id) {
                this.$parent.translators[i] = response.data;
              }
            }
            if (this.$parent.edition) {
              this.$parent.edition.translator = response.data;
            }
          } else {
            this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not update the translator', life: 10000});
          }
        })
      } else /* create */ {
        TranslatorService.create(this.translator).then((response) => {
          if (response.status == 200) {
            this.$toast.add({severity:'success', summary: 'Success', detail: 'The new translator has been saved', life: 10000});
            this.$parent.translators.push(response.data);
            this.$parent.translator = response.data;
          } else {
            this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not save the new translator', life: 10000});
          }
        });
      }
      this.hideDialog();
    },
    hideDialog() {
      this.$parent.translatorDialog = false;
      this.$parent.editChildDialog = false;
    }
  },
  mounted() {
    if (this.editMode) {
      this.header = 'Edit translator';
      this.translator = this.$parent.translator;
      this.name = this.translator.name;
    } else {
      this.header = 'New translator';
    }
  }
};
</script>