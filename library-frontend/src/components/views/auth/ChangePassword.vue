<template>
  <Header />
  <Form @submit="changePassword()" :validation-schema="schema" v-slot="{ errors }">
    <div class="w-full flex flex-column align-items-center gap-5">
      <h3 style="margin-bottom: 15px;">Change password</h3>
      <div>
        <Field name="currentPassword" v-slot="{ field }">
          <span class="p-float-label">
            <InputText v-bind="field" v-model="currentPassword" type="password"
            aria-describedby="currentPassword-help" :class="{ 'p-invalid': errors.currentPassword }" />
            <label>Current password</label>
          </span>
        </Field>
        <small id="currentPassword-help" class="p-error">
          {{ errors.currentPassword }}
        </small>
      </div>

      <div style="width: 212px">
        <Field name="newPassword" v-slot="{ field }">
          <span class="p-float-label">
            <InputText v-bind="field" v-model="newPassword" type="password"
            aria-describedby="newPassword-help" :class="{ 'p-invalid': errors.newPassword }" />
            <label>New password</label>
          </span>
        </Field>
        <small id="newPassword-help" class="p-error">
          {{ errors.newPassword }}
        </small>
      </div>

      <div>
        <Field name="confirmPassword" v-slot="{ field }">
          <span class="p-float-label">
            <InputText v-bind="field" v-model="confirmPassword" type="password"
            aria-describedby="confirmPassword-help" :class="{ 'p-invalid': errors.confirmPassword }" />
            <label>Confirm new password</label>
          </span>
        </Field>
        <small id="confirmPassword-help" class="p-error">
          {{ errors.confirmPassword }}
        </small>
      </div>

      <div class="footer">
        <Button label="Reset" type="reset" class="p-button-secondary" outlined />
        <Button label="Submit" type="submit" outlined />
      </div>
    </div>
  </Form>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import AuthenticationService from "@/services/AuthenticationService";
import { Form, Field } from 'vee-validate';
import * as yup from 'yup';

export default {
  name: "ChangePassword",
  components: {
    Header, Form, Field
  },
  data() {
    const schema = yup.object({
      currentPassword: yup.string().required('The current password is required'),
      newPassword: yup.string().required('The new password is required')
        .min(8, 'The password must consist of at least 8 characters')
        .minUppercase(1, 'The password must consist of at least 1 upper letter')
        .minNumbers(1, 'The password must consist of at least 1 digit')
        .minSymbols(1, 'The password must consist of at least 1 special character'),
      confirmPassword: yup.string().required('Confirm the password').oneOf([yup.ref('newPassword')], 'The passwords differ')
    });

    return {
      schema,
      token: null,
      currentPassword: null,
      newPassword: null,
      confirmPassword: null
    };
  },
  methods: {
    async changePassword() {
      AuthenticationService.changePassword(this.currentPassword, this.newPassword, this.confirmPassword).then((response) => {
        if (response.status == 200) {
          this.$toast.add({severity:'success', summary: 'Success', detail: 'The new password has been set', life: 10000});
          this.$router.push({ name: "login" });
        } else {
          this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not set the new password', life: 10000});
        }
      });
    }
  },
  mounted() {
  }
};
</script>