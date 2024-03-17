<template>
  <Header />
  <Form @submit="login()" :validation-schema="schema" v-slot="{ errors }">
    <div class="w-full flex flex-column align-items-center gap-5">
      <h3 style="margin-bottom: 15px;">Log in</h3>
      <div>
        <Field name="email" v-slot="{ field }">
          <span class="p-float-label">
            <InputText v-bind="field" v-model="email"
            aria-describedby="email-help" :class="{ 'p-invalid': errors.email }" />
            <label>Email</label>
          </span>
        </Field>
        <small id="email-help" class="p-error">
          {{ errors.email }}
        </small>
      </div>

      <div>
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

      <div class="footer">
        <Button label="Reset" type="reset" class="p-button-secondary" outlined />
        <Button label="Submit" type="submit" outlined />
      </div>

      <Button label="Register" @click="$router.push({ name: 'register'})" icon="pi pi-user-plus" severity="success" outlined 
      style="margin-top: -15px" />

      <Button label="Forgot password?" @click="forgotPasswordDialog = true" outlined 
      style="margin-top: -15px" />
    </div>
  </Form>

  <span v-if="forgotPasswordDialog">
    <ForgotPasswordDialog />
  </span>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import AuthenticationService from "@/services/AuthenticationService";
import { Form, Field } from 'vee-validate';
import * as yup from 'yup';
import ForgotPasswordDialog from '@/components/dialogs/ForgotPasswordDialog.vue';

export default {
  name: "Login",
  components: {
    Header, Form, Field,
    ForgotPasswordDialog
  },
  data() {
    const schema = yup.object({
      email: yup.string().trim().required('The email is required').email('The email is incorrect'),
      password: yup.string().trim().required('The password is required')
    });

    return {
      schema,
      email: null,
      password: null,
      forgotPasswordDialog: false
    };
  },
  methods: {
    async login() {
      const response = await AuthenticationService.login(this.email, this.password);
      if (response && response.status == 200) {
        localStorage.setItem("user-token", JSON.stringify(response.data.token));
        localStorage.setItem("user-id", response.data.userId);
        localStorage.setItem("user-role", response.data.role);
        localStorage.setItem("user-name", response.data.name);
        this.$router.push({ name: "home" });
      } else {
        this.$toast.add({
          severity: "error",
          summary: "Could not log in",
          detail: "The user of given email and password does not exist",
          life: 10000
        });
      }
    }
  },
  mounted() {
    const userToken = localStorage.getItem("user-token");
    if (userToken) {
      this.$router.push({ name: "home" });
    }
  },
};
</script>