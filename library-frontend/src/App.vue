<template>
  <Toast />
  <router-view />
  <Footer />
</template>

<script>
import Footer from "./components/nav/Footer.vue";

export default {
  name: "App",
  components: {
    Footer
  }
};
</script>