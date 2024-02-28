<template>
  <Dialog visible :style="{ width: '450px' }" :header="header" :modal="true" class="p-fluid">
    <Form @submit="saveAuthor()" :validation-schema="schema" v-slot="{ errors }">
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
import AuthorService from '@/services/AuthorService';

export default {
  name: "AuthorDialog",
  components: {
    Form, Field
  },
  props: {
    editMode: Boolean
  },
  data() {
    const schema = yup.object({
      name: yup.string().required('Nazwa jest wymagana')
    });

    return {
      schema,
      author: {
        name: null
      },
      name: null,
      header: null
    };
  },
  methods: {
    saveAuthor() {
      this.author.name = this.name;
      
      if (this.editMode) /* update */ {
        AuthorService.update(this.author).then((response) => {
          if (response.status == 200) {
            this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Zaktualizowano autora', life: 10000});
            for (var i = 0; i < this.$parent.authors.length; i++) {
              if (this.$parent.authors[i].id == response.data.id) {
                this.$parent.authors[i] = response.data;
              }
            }
          } else {
            this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Zaktualizowanie autora nie powiodło się', life: 10000});
          }
        })
      } else /* create */ {
        // to check if a record isn't being duplicated
        var existingAuthor = this.$parent.authors.find((author) => author.name == this.author.name);

        if (!existingAuthor) {
          AuthorService.create(this.author).then((response) => {
            if (response.status == 200) {
              this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Zapisano nowego autora', life: 10000});
              this.$parent.authors.push(response.data);
              if (this.$parent.book) {
                this.$parent.bookAuthors.push(response.data);
              }
            } else {
              this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Zapisanie nowego autora nie powiodło się', life: 10000});
            }
          })
        } else {
          this.$parent.author = existingAuthor;
        }
      }
      this.hideDialog();
    },
    hideDialog() {
      this.$parent.authorDialog = false;
      this.$parent.editChildDialog = false;
    }
  },
  mounted() {
    if (this.editMode) {
      this.header = 'Edycja autora';
      this.author = this.$parent.author;
      this.name = this.author.name;
    } else {
      this.header = 'Nowy autor';
    }
  }
};
</script>