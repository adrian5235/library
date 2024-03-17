<template>
  <Header />
  <Form @submit="register()" :validation-schema="schema" v-slot="{ errors }">
    <div class="w-full flex flex-column align-items-center gap-5">
      <h3 style="margin-bottom: 15px;">Register</h3>
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

      <div style="width: 212px">
        <Field name="firstName" v-slot="{ field }">
          <span class="p-float-label">
            <InputText v-bind="field" v-model="firstName"
            aria-describedby="firstName-help" :class="{ 'p-invalid': errors.firstName }" />
            <label>First name</label>
          </span>
        </Field>
        <small id="firstName-help" class="p-error">
          {{ errors.firstName }}
        </small>
      </div>

      <div>
        <Field name="lastName" v-slot="{ field }">
          <span class="p-float-label">
            <InputText v-bind="field" v-model="lastName"
            aria-describedby="lastName-help" :class="{ 'p-invalid': errors.lastName }" />
            <label>Last name</label>
          </span>
        </Field>
        <small id="lastName-help" class="p-error">
          {{ errors.lastName }}
        </small>
      </div>

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
            aria-describedby="confirmPassword-help" :class="{ 'p-invalid': errors.confirmPassword }" />
            <label>Confirm password</label>
          </span>
        </Field>
        <small id="confirmPassword-help" class="p-error">
          {{ errors.confirmPassword }}
        </small>
      </div>

      <div class="footer">
        <Button label="Reset" type="reset" class="p-button-secondary" outlined />
        <Button label="Submit" type="submit" severity="success" outlined />
      </div>
      
      <Button label="Log in" @click="$router.push({ name: 'login'})" icon="pi pi-user" outlined style="margin-top: -15px" />
    </div>
  </Form>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import AuthenticationService from "@/services/AuthenticationService";
import { Form, Field } from 'vee-validate';
import * as yup from 'yup';
import YupPassword from 'yup-password'
YupPassword(yup) // extend yup

export default {
  name: "SignUp",
  components: {
    Header, Form, Field
  },
  data() {
    const schema = yup.object({
      email: yup.string().trim().required('The email is required').email('The email is incorrect'),
      firstName: yup.string().required('The first name is required').matches(/^[a-zA-Z ]*$/, 'The first name must consist of character'),
      lastName: yup.string().required('The last name is required').matches(/^[a-zA-Z ]*$/, 'The last name must consist of characters'),
      password: yup.string().required('The password is required')
        .min(8, 'The password must consist of at least 8 characters ')
        .minUppercase(1, 'The password must consist of at least one uppercase character')
        .minNumbers(1, 'The password must consist of at least one digit')
        .minSymbols(1, 'The password must consist of at least one special character'),
      confirmPassword: yup.string().required('Confirm the password').oneOf([yup.ref('password')], 'The passwords differ')
    });

  return { 
    schema,
    email: null,
    firstName: null,
    lastName: null,
    password: null,
    confirmPassword: null
    }
  },
  methods: {
    async register() {
      const response = await AuthenticationService.register(this.email, this.firstName, this.lastName, this.password);
      if (response && response.status == 200) {
        localStorage.setItem("user-token", JSON.stringify(response.data.token));
        localStorage.setItem("user-id", response.data.userId);
        localStorage.setItem("user-role", response.data.role);
        localStorage.setItem("user-name", response.data.name);
        this.$router.push({ name: "home" });
      } else {
        this.$toast.add({
          severity: "error",
          summary: "Could not register",
          detail: "The user of the given email address exists",
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