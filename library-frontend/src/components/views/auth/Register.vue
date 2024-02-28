<template>
  <Header />
  <Form @submit="register()" :validation-schema="schema" v-slot="{ errors }">
    <div class="w-full flex flex-column align-items-center gap-5">
      <h3 style="margin-bottom: 15px;">Rejestracja</h3>
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
        <Field name="firstName" v-slot="{ field }">
          <span class="p-float-label">
            <InputText v-bind="field" v-model="firstName"
            aria-describedby="firstName-help" :class="{ 'p-invalid': errors.firstName }" />
            <label>Imię</label>
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
            <label>Nazwisko</label>
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
            <label>Hasło</label>
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
            <label>Powtórz hasło</label>
          </span>
        </Field>
        <small id="confirmPassword-help" class="p-error">
          {{ errors.confirmPassword }}
        </small>
      </div>

      <div class="footer">
        <Button label="Wyczyść" type="reset" class="p-button-secondary" outlined />
        <Button label="Zarejestruj" type="submit" severity="success" outlined />
      </div>
      
      <Button label="Zaloguj się" @click="$router.push({ name: 'login'})" icon="pi pi-user" outlined style="margin-top: -15px" />
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
      email: yup.string().trim().required('Email jest wymagany').email('Niepoprawny adres email'),
      firstName: yup.string().required('Imię jest wymagane').matches(/^[a-zA-Z ]*$/, 'Imię musi składać się z liter'),
      lastName: yup.string().required('Nazwisko jest wymagane').matches(/^[a-zA-Z ]*$/, 'Nazwisko musi składać się z liter'),
      password: yup.string().required('Hasło jest wymagane')
        .min(8, 'Hasło musi składać się z przynajmniej 8 znaków')
        .minUppercase(1, 'Hasło musi zawierać przynajmniej jedną dużą literę')
        .minNumbers(1, 'Hasło musi zawierać przynajmniej jedną cyfrę')
        .minSymbols(1, 'Hasło musi zawierać przynajmniej jeden znak specjalny'),
      confirmPassword: yup.string().required('Należy powtórzyć hasło').oneOf([yup.ref('password')], 'Hasła różnią się')
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
          summary: "Rejestracja nie powiodła się",
          detail: "Użytkownik o podanym adresie e-mail już istnieje.",
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