<template>
  <Dialog visible :style="{ width: '450px' }" header="Resetowanie hasła" :modal="true" class="p-fluid">
    <p>Na podany adres e-mail wysłany zostanie link, który posłuży do zmiany hasła.</p>
    <Form @submit="resetPasswordRequest()" :validation-schema="schema" v-slot="{ errors }">
      <div class="py-4">
        <Field v-model="email" name="email">
          <span class="p-float-label">
            <InputText v-model="email" autofocus aria-describedby="email-help" :class="{ 'p-invalid': errors.email }" />
            <label>E-mail</label>
          </span>
        </Field>
        <small id="email-help" class="p-error">
          {{ errors.email }}
        </small>
      </div>

      <div class="footer">
        <Button label="Anuluj" class="p-button-secondary" icon="pi pi-times" @click="hideDialog()" outlined />
        <Button type="submit" label="Potwierdź" icon="pi pi-check" style="padding-right: 50px;" outlined />
      </div>
    </Form>
  </Dialog>
</template>

<script>
import { Form, Field } from 'vee-validate';
import * as yup from 'yup';
import AuthenticationService from '@/services/AuthenticationService';

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
      email: yup.string().required('Email jest wymagany')
    });

    return {
      schema,
      email: null
    };
  },
  methods: {
    resetPasswordRequest() {
      AuthenticationService.resetPasswordRequest(this.email).then((response) => {
        if (response.status == 200) {
          this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Mail z linkiem został wysłany na podany adres e-mail', life: 10000});
        } else {
          this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Wysłanie maila nie powiodło się', life: 10000});
        }
      });
      this.hideDialog();
    },
    hideDialog() {
      this.$parent.forgotPasswordDialog = false;
    }
  },
  mounted() {
  }
};
</script>