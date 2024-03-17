<template>
  <Dialog visible :style="{ width: '450px' }" :header="header" :modal="true" class="p-fluid">
    <Form @submit="saveBinding()" :validation-schema="schema" v-slot="{ errors }">
      <div class="py-4">
        <Field v-model="name" name="name" v-slot="{ field }">
          <span class="p-float-label">
            <InputText v-bind="field" v-model="name" autofocus 
            aria-describedby="name-help" :class="{ 'p-invalid': errors.name }" />
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
import BindingService from '@/services/BindingService';

export default {
  name: "BindingDialog",
  components: {
    Form, Field
  },
  props: {
    editMode: Boolean
  },
  data() {
    const schema = yup.object({
      name: yup.string().trim().required('The name is required')
    });

    return {
      schema,
      binding: {
        name: null
      },
      name: null,
      header: null
    };
  },
  methods: {
    saveBinding() {
      this.binding.name = this.name;
      if (this.editMode) /* update */ {
        BindingService.update(this.binding).then((response) => {
          if (response.status == 200) {
            this.$toast.add({severity:'success', summary: 'Success', detail: 'The binding has been updated', life: 10000});
            for (var i = 0; i < this.$parent.bindings.length; i++) {
              if (this.$parent.bindings[i].id == response.data.id) {
                this.$parent.bindings[i] = response.data;
              }
            }
            if (this.$parent.edition) {
              this.$parent.edition.binding = response.data;
            }
          } else {
            this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not update the binding', life: 10000});
          }
        });
      } else /* create */ {
        // to check if a record isn't being duplicated
        var existingBinding = this.$parent.bindings.find((binding) => binding.name == this.binding.name);
        if (!existingBinding) {
          BindingService.create(this.binding).then((response) => {
            this.$toast.add({severity:'success', summary: 'Success', detail: 'The new binding has been saved', life: 10000});
            if (response.status == 200) {
              this.$parent.bindings.push(response.data);
              this.$parent.binding = response.data;
            } else {
              this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not save the new binding', life: 10000});
            }
          });
        } else {
          this.$parent.binding = existingBinding;
        }
      }
      this.hideDialog();
    },
    hideDialog() {
      this.$parent.bindingDialog = false;
      this.$parent.editChildDialog = false;
    }
  },
  mounted() {
    if (this.editMode) {
      this.header = 'Edit binding';
      this.binding = this.$parent.binding;
      this.name = this.binding.name;
    } else {
      this.header = 'New binding';
    }
  }
};
</script>