<template>
  <Header></Header>
  <div>
    <div class="flex justify-content-center">
      <div>
        <h3 style="margin-bottom: 15px">About</h3>
        <div style="width: 90vw; background-color: white; border-radius: 10px;">
          <span v-if="userRole == 'ADMIN'">
            <Editor v-model="miscellaneous.about" editorStyle="height: 600px" />
          </span>
          <span v-else>
            <div v-html="miscellaneous.about" style="height: 600px; padding: 20px"></div>
          </span>
        </div>
      </div>
    </div>
    <div class="flex justify-content-end mt-4 mb-4" style="width: 95vw">
      <Button v-if="userRole == 'ADMIN'" @click="save()" label="Save" outlined></Button>
    </div>
  </div>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import MiscellaneousService from "@/services/MiscellaneousService";

export default {
  name: "Rules",
  components: {
    Header
  },
  data() {
    return {
      miscellaneous: {
        about: null
      },
      userRole: null
    }
  },
  methods: {
    save() {
      MiscellaneousService.update(this.miscellaneous).then((response) => {
        if (response.status == 200) {
          this.$toast.add({severity:'success', summary: 'Success', detail: 'The content has been updated', life: 10000});
        } else {
          this.$toast.add({severity:'success', summary: 'Success', detail: 'Could not update the content', life: 10000});
        }
      })
    },
    getMiscellaneous() {
      MiscellaneousService.get().then((response) => {
        if (response.data.length > 0) {
          this.miscellaneous = response.data[0];
        }
      })
    }
  },
  mounted() {
    this.userRole = localStorage.getItem('user-role');
    this.getMiscellaneous();
  }
}
</script>