<template>
  <Header />
  <Form @submit="changePassword()" :validation-schema="schema" v-slot="{ errors }">
    <div class="w-full flex flex-column align-items-center gap-5">
      <h3 style="margin-bottom: 15px;">Zmiana hasła</h3>
      <div>
        <Field name="currentPassword" v-slot="{ field }">
          <span class="p-float-label">
            <InputText v-bind="field" v-model="currentPassword" type="password"
            aria-describedby="currentPassword-help" :class="{ 'p-invalid': errors.currentPassword }" />
            <label>Stare Hasło</label>
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
            <label>Nowe Hasło</label>
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
            aria-describedby="confirmPassword-help" :class="{ 'p-invalid': errors.pasconfirmPasswordsword }" />
            <label>Powtórz nowe hasło</label>
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
  name: "ChangePassword",
  components: {
    Header, Form, Field
  },
  data() {
    const schema = yup.object({
      currentPassword: yup.string().required('Stare hasło jest wymagane'),
      newPassword: yup.string().required('Nowe hasło jest wymagane')
        .min(8, 'Hasło musi składać się z przynajmniej 8 znaków')
        .minUppercase(1, 'Hasło musi zawierać przynajmniej jedną dużą literę')
        .minNumbers(1, 'Hasło musi zawierać przynajmniej jedną cyfrę')
        .minSymbols(1, 'Hasło musi zawierać przynajmniej jeden znak specjalny'),
      confirmPassword: yup.string().required('Należy powtórzyć nowe hasło').oneOf([yup.ref('newPassword')], 'Hasła różnią się')
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
          this.$toast.add({severity:'success', summary: 'Sukces', detail: 'Pomyślnie zmieniono hasło', life: 10000});
          this.$router.push({ name: "login" });
        } else {
          this.$toast.add({severity:'error', summary: 'Błąd', detail: 'Zmiana hasła nie powiodła się', life: 10000});
        }
      });
    }
  },
  mounted() {
  }
};
</script>