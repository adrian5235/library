<template>
  <Header />
  <div class="flex justify-content-center">
    <div>
      <h3 style="margin-bottom: 15px;">{{ book.title }}'s editions</h3>
      <div class="card">
        <DataView :value="editions" layout="grid" paginator :rows="6" :rowsPerPageOptions="[6, 12, 18, 24]" style="width: 90vw">
          <template #header>
            <div class="flex align-items-center justify-content-center gap-2">
              <div class="flex align-items-center gap-2">
                <Button v-if="showOnlyAvailable" rounded class="mr-2" @click="showOnlyAvailable = false; filter()" label="Only the available" />
                <Button v-else rounded outlined class="mr-2" @click="showOnlyAvailable = true; filter()" label="Only the available" />
                <Dropdown v-model="selectedLanguage" :options="languages" optionLabel="name" showClear filter @change="filter()" placeholder="Language(s)" class="w-full md:w-14rem" />
                <Dropdown v-model="selectedBinding" :options="bindings" optionLabel="name" showClear filter @change="filter()" placeholder="Binding(s)" class="w-full md:w-14rem" />
              </div>
              <div class="align-items-center">
                <span class="p-input-icon-left">
                  <i class="pi pi-search" />
                    <InputText v-model="searchValue" placeholder="ISBN-13" @keyup="filter()" />
                </span>
              </div>
            </div>
          </template>

          <template #empty>
            <span style="margin-left: 5px">Could not find any editions</span>
          </template>

          <template #grid="slotProps">
            <div v-if="slotProps.data.id != editionId" class="col-12 sm:col-6 lg:col-4 xl:col-2 p-2">
              <div class="p-4 border-1 surface-border surface-card border-round">
                <div class="flex flex-wrap align-items-center justify-content-between gap-2">
                  <span class="overflow-hidden white-space-nowrap text-overflow-ellipsis" style="max-width: 70%">{{ slotProps.data.language.name }}</span>
                    <Tag v-if="slotProps.data.quantity > 0" value="available" severity="success"></Tag>
                    <Tag v-else value="unavailable" severity="danger"></Tag>
                </div>
                <div class="flex flex-column align-items-center gap-3 py-5">
                  <img v-if="slotProps.data.image" class="shadow-2 border-round" style="height: 150px" :src="require('@/assets/img/' + slotProps.data.image.name)" />
                  <img v-else class="shadow-2 border-round" style="height: 150px" src="@/assets/no-image.png" />                            
                  <span class="overflow-hidden white-space-nowrap text-overflow-ellipsis" style="max-width: 100%">{{ slotProps.data.binding.name }}</span>
                  <Button label="Choose" @click="this.$router.push({
                    name: 'edition',
                    params: {
                      edition: JSON.stringify(slotProps.data),
                      editionId: slotProps.data.id,
                      book: JSON.stringify(slotProps.data.book),
                      bookId: slotProps.data.book.id
                    }
                  })" outlined>
                  </Button>
                </div>
              </div>
            </div>
          </template>
        </DataView>
      </div>
    </div>
  </div>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import EditionService from "@/services/EditionService";

export default {
  name: "EditionsGridView",
  components: {
    Header
  },
  data() {
    return {
      book: {
        title: null
      },
      editionId: null,
      editions: null,
      allEditions: null,
      isFetching: true,
      showOnlyAvailable: false,
      searchValue: null,
      selectedLanguage: null,
      selectedBinding: null
    };
  },
  methods: {
    getEditions() {
      EditionService.getByBookId(this.book.id).then((response) => {
        this.editions = response.data;
        this.editions = this.editions.filter((edition) => edition.id != this.editionId);
        this.allEditions = this.editions;
      });
    },
    filter() {
      var filteredEditions = this.allEditions;

      if (this.showOnlyAvailable) {
        filteredEditions = filteredEditions.filter(edition => edition.quantity > 0);
      }

      if (this.searchValue) {
        filteredEditions = filteredEditions.filter((edition) => edition.isbn13 == this.searchValue.trim());
      }

      if (this.selectedLanguage) {
        filteredEditions = filteredEditions.filter(edition => edition.language.name == this.selectedLanguage.name);
      }

      if (this.selectedBinding) {
        filteredEditions = filteredEditions.filter(edition => edition.binding.name == this.selectedBinding.name);
      }

      this.editions = filteredEditions;
    }
  },
  mounted() {
    var book = this.$route.params.book;

    if (book) {
      this.book = JSON.parse(book);
      localStorage.setItem('book', JSON.stringify(this.book));
    } else {
      this.book = JSON.parse(localStorage.getItem('book'));
    }

    var editionId = this.$route.params.editionId;
    
    if (editionId) {
      this.editionId = JSON.parse(editionId);
      localStorage.setItem('edition-id', JSON.stringify(this.editionId));
    } else {
      this.editionId = JSON.parse(localStorage.getItem('edition-id'));
    }

    this.getEditions();
  },
  computed: {
    languages() {
      const languages = [];

      for (var i in this.allEditions) {
        var language = this.allEditions[i].language;
        if (!languages.some((existingLanguage) => existingLanguage.name == language.name)) {
          languages.push(language);
        }
      }
      return languages;
    },
    
    bindings() {
      const bindings = [];

      for (var i in this.allEditions) {
        var binding = this.allEditions[i].binding;
        if (!bindings.some((existingBinding) => existingBinding.name == binding.name)) {
          bindings.push(binding);
        }
      }
      return bindings;
    }
  }
};
</script>