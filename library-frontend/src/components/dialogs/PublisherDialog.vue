<template>
  <Dialog visible :style="{ width: '450px' }" :header="header" :modal="true" class="p-fluid">
    <Form @submit="savePublisher()" :validation-schema="schema" v-slot="{ errors }">
      <div class="py-4">
        <Field v-model="name" name="name">
          <span class="p-float-label">
            <InputText v-model="name" autofocus aria-describedby="name-help" :class="{ 'p-invalid': errors.name }" />
            <label>Nazwa</label>
          </span>
        </Field>
        <small id="name-help" class="p-error">
          {{ errors.name }}
        </small>
      </div>

      <div class="footer">
        <Button label="Anuluj" class="p-button-secondary" icon="pi pi-times" @click="hideDialog()" outlined />
        <Button type="submit" label="Zapisz" icon="pi pi-check" outlined />
      </div>
    </Form>
  </Dialog>
</template>

<script>
import { Form, Field } from 'vee-validate';
import * as yup from 'yup';
import PublisherService from '@/services/PublisherService';

export default {
  name: "PublisherDialog",
  components: {
    Form, Field
  },
  props: {
    editMode: Boolean
  },
  data() {
    const schema = yup.object({
      name: yup.string().trim().required('Nazwa jest wymagana')
    });

    return {
      schema,
      publisher: {
        name: null
      },
      name: null,
      header: null
    };
  },
  methods: {
    savePublisher() {
      this.publisher.name = this.name;

      if (this.editMode) /* update */ {
        PublisherService.update(this.publisher).then((response) => {
          if (response.status == 200) {
            this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Zaktualizowano wydawnictwo', life: 10000});
            for (var i = 0; i < this.$parent.publishers.length; i++) {
              if (this.$parent.publishers[i].id == response.data.id) {
                this.$parent.publishers[i] = response.data;
              }
            }
            if (this.$parent.edition) {
              this.$parent.edition.publisher = response.data;
            }
          } else {
            this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Zaktualizowanie wydawnictwa nie powiodło się', life: 10000});
          }
        })
      } else /* create */ {
        PublisherService.create(this.publisher).then((response) => {
          if (response.status == 200) {
            this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Zapisano nowe wydawnictwo', life: 10000});
            this.$parent.publishers.push(response.data);
            this.$parent.publisher = response.data;
          } else {
            this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Zapisanie nowego wydawnictwa nie powiodło się', life: 10000});
          }
        });
      }
      this.hideDialog();
    },
    hideDialog() {
      this.$parent.publisherDialog = false;
      this.$parent.editChildDialog = false;
    }
  },
  mounted() {
    if (this.editMode) {
      this.header = 'Edycja wydawnictwa';
      this.publisher = this.$parent.publisher;
      this.name = this.publisher.name;
    } else {
      this.header = 'Nowe wydawnictwo';
    }
  }
};
</script>