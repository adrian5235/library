<template>
  <Header />
  <div class="flex justify-content-center">
    <div style="width: 90vw">
      <h3 style="margin-bottom: 15px;">Statystyki</h3>
      <Toolbar class="mb-4">
        <template #start>
          <Button v-if="mostPopularBooks == true" label="Najpopularniejsze książki" @click="hideAll(); mostPopularBooks = true" />
          <Button v-else label="Najpopularniejsze książki" @click="hideAll(); mostPopularBooks = true" outlined />
          <Button v-if="mostPopularAuthors == true" label="Najpopularniejsi autorzy" @click="hideAll(); mostPopularAuthors = true" style="margin-left: 15px" />
          <Button v-else label="Najpopularniejsi autorzy" @click="hideAll(); mostPopularAuthors = true" style="margin-left: 15px" outlined />
          <Button v-if="mostPopularGenres == true" label="Najpopularniejsze gatunki" @click="hideAll(); mostPopularGenres = true" style="margin-left: 15px" />
          <Button v-else label="Najpopularniejsze gatunki" @click="hideAll(); mostPopularGenres = true" style="margin-left: 15px" outlined />
        </template>
      </Toolbar>
    </div>
  </div>

  <div v-if="mostPopularBooks">
    <MostPopularBooks />
  </div>
  <div v-else-if="mostPopularAuthors">
    <MostPopularAuthors />
  </div>
  <div v-else-if="mostPopularGenres">
    <MostPopularGenres />
  </div>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import MostPopularBooks from "./tables/stats/MostPopularBooks.vue";
import MostPopularAuthors from "./tables/stats/MostPopularAuthors.vue";
import MostPopularGenres from "./tables/stats/MostPopularGenres.vue";

export default {
  name: "Statistics",
  components: {
    Header,
    MostPopularBooks,
    MostPopularAuthors,
    MostPopularGenres
  },
  data() {
    return {
      mostPopularBooks: false,
      mostPopularAuthors: false,
      mostPopularGenres: false,
    };
  },
  methods: {
    hideAll() {
      this.mostPopularBooks = false;
      this.mostPopularAuthors = false;
      this.mostPopularGenres = false;
    },
    hideDialog() {
      this.dialog = false;
    }
  },
  mounted() {
    this.dialog = true;
  }
};
</script>