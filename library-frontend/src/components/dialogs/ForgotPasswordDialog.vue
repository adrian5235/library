<template>
  <Dialog visible :style="{ width: '450px' }" header="Reset password" :modal="true" class="p-fluid">
    <p>A mail containing reset password link will be sent on the given email address.</p>
    <Form @submit="resetPasswordRequest()" :validation-schema="schema" v-slot="{ errors }">
      <div class="py-4">
        <Field v-model="email" name="email">
          <span class="p-float-label">
            <InputText v-model="email" autofocus aria-describedby="email-help" :class="{ 'p-invalid': errors.email }" />
            <label>Email</label>
          </span>
        </Field>
        <small id="email-help" class="p-error">
          {{ errors.email }}
        </small>
      </div>

      <div class="footer">
        <Button label="Cancel" class="p-button-secondary" icon="pi pi-times" @click="hideDialog()" outlined />
        <Button type="submit" label="Send" icon="pi pi-check" outlined />
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
      email: yup.string().required('E-mail is required')
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
          this.$toast.add({severity:'success', summary: 'Success', detail: 'The mail has been sent on the given email address', life: 10000});
        } else {
          this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not send the mail on the given email address', life: 10000});
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