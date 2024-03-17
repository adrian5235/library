<template>
  <Dialog visible :style="{ width: '450px' }" :header="header" :modal="true" class="p-fluid">
    <Form @submit="saveGenre()" :validation-schema="schema" v-slot="{ errors }">
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
import GenreService from '@/services/GenreService';

export default {
  name: "GenreDialog",
  components: {
    Form, Field
  },
  props: {
    editMode: Boolean
  },
  data() {
    const schema = yup.object({
      name: yup.string().required('The name is required')
    });

    return {
      schema,
      genre: {
        name: null
      },
      name: null,
      header: null
    };
  },
  methods: {
    saveGenre() {
      this.genre.name = this.name;
      
      if (this.editMode) /* update */ {
        GenreService.update(this.genre).then((response) => {
          if (response.status == 200) {
            this.$toast.add({severity:'success', summary: 'Success', detail: 'The genre has been updated', life: 10000});
            for (var i = 0; i < this.$parent.genres.length; i++) {
              if (this.$parent.genres[i].id == response.data.id) {
                this.$parent.genres[i] = response.data;
              }
            }
          } else {
            this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not update the genre', life: 10000});
          }
        })
      } else /* create */ {
        // to check if a record isn't being duplicated
        var existingGenre = this.$parent.genres.find((genre) => genre.name == this.genre.name);

        if (!existingGenre) {
          GenreService.create(this.genre).then((response) => {
            if (response.status == 200) {
              this.$toast.add({severity:'success', summary: 'Success', detail: 'The new genre has been saved', life: 10000});
              this.$parent.genres.push(response.data);
              if (this.$parent.book) {
                this.$parent.bookGenres.push(response.data);
              }
            } else {
              this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not save the new genre', life: 10000});
            }
          })
        } else {
          this.$parent.genre = existingGenre;
        }
      }
      this.hideDialog();
    },
    hideDialog() {
      this.$parent.genreDialog = false;
      this.$parent.editChildDialog = false;
    }
  },
  mounted() {
    if (this.editMode) {
      this.header = 'Edit genre';
      this.genre = this.$parent.genre;
      this.name = this.genre.name;
    } else {
      this.header = 'New genre';
    }
  }
};
</script>