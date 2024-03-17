<template>
  <Header />
  <Form @submit="resetPassword()" :validation-schema="schema" v-slot="{ errors }">
    <div class="w-full flex flex-column align-items-center gap-5">
      <h3 style="margin-bottom: 15px;">Reset password</h3>
      <div style="width: 212px">
        <Field name="password" v-slot="{ field }">
          <span class="p-float-label">
            <InputText v-bind="field" v-model="password" type="password"
            aria-describedby="password-help" :class="{ 'p-invalid': errors.password }" />
            <label>Password</label>
          </span>
        </Field>
        <small id="password-help" class="p-error">
          {{ errors.password }}
        </small>
      </div>

      <div>
        <Field name="confirmPassword" v-slot="{ field }">
          <span class="p-float-label">
            <InputText v-bind="field" v-model="confirmPassword" type="password"
            aria-describedby="confirmPassword-help" :class="{ 'p-invalid': errors.pasconfirmPasswordsword }" />
            <label>Confirm password</label>
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
  name: "ResetPassword",
  components: {
    Header, Form, Field
  },
  data() {
    const schema = yup.object({
      password: yup.string().required('The password is required')
        .min(8, 'The password must consist of at least 8 characters ')
        .minUppercase(1, 'The password must consist of at least one uppercase character')
        .minNumbers(1, 'The password must consist of at least one digit')
        .minSymbols(1, 'The password must consist of at least one special character'),
      confirmPassword: yup.string().required('Confirm the password').oneOf([yup.ref('password')], 'The passwords differ')
    });

    return {
      schema,
      token: null,
      password: null,
      confirmPassword: null
    };
  },
  methods: {
    async resetPassword() {
      AuthenticationService.resetPassword(this.token, this.password, this.confirmPassword).then((response) => {
        if (response.status == 200) {
          this.$toast.add({severity:'success', summary: 'Success', detail: 'The password has been changed', life: 10000});
          this.$router.push({ name: "login" });
        } else {
          this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not change the password', life: 10000});
        }
      });
    }
  },
  mounted() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    this.token = urlParams.get('token')
  }
};
</script>