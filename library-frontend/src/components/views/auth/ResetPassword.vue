<template>
  <Header />
  <Form @submit="resetPassword()" :validation-schema="schema" v-slot="{ errors }">
    <div class="w-full flex flex-column align-items-center gap-5">
      <h3 style="margin-bottom: 15px;">Resetowanie hasła</h3>
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
            aria-describedby="confirmPassword-help" :class="{ 'p-invalid': errors.pasconfirmPasswordsword }" />
            <label>Powtórz hasło</label>
          </span>
        </Field>
        <small id="confirmPassword-help" class="p-error">
          {{ errors.confirmPassword }}
        </small>
      </div>

      <div class="footer">
        <Button label="Wyczyść" type="reset" class="p-button-secondary" outlined />
        <Button label="Potwierdź" type="submit" outlined />
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
      password: yup.string().required('Hasło jest wymagane')
        .min(8, 'Hasło musi składać się z przynajmniej 8 znaków')
        .minUppercase(1, 'Hasło musi zawierać przynajmniej jedną dużą literę')
        .minNumbers(1, 'Hasło musi zawierać przynajmniej jedną cyfrę')
        .minSymbols(1, 'Hasło musi zawierać przynajmniej jeden znak specjalny'),
      confirmPassword: yup.string().required('Należy powtórzyć hasło').oneOf([yup.ref('password')], 'Hasła różnią się')
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
          this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Pomyślnie zmieniono hasło', life: 10000});
          this.$router.push({ name: "login" });
        } else {
          this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Zmiana hasła nie powiodła się', life: 10000});
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