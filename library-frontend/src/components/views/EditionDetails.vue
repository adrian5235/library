<template>
  <Header />
  <div class="flex justify-content-center">
    <div style="padding: 20px; background-color: white; border-radius: 10px">
      <div class="flex gap-5">
        <div style="width: 400px; height: 770px; ">
          <img v-if="edition.image" class="shadow-2 border-round" style="width: 400px; height: 600px" :src="require('@/assets/img/' + edition.image.name)" />
          <img v-else class="shadow-2 border-round" style="width: 400px; height: 600px" src="@/assets/no-image.png" />
        </div>

        <div>
          <h3 style="margin-bottom: 15px">{{ book.title }}</h3>
          <p v-if="book.authors.length == 1">Author: {{ authors }}</p>
          <p v-else>Authors: {{ authors }}</p>
          <p v-if="book.genres.length == 1">Genre: {{ genres }} </p>
          <p v-else>Genres: {{ genres }} </p>
          <p>Language: {{ edition.language.name }}</p>

          <div class="flex gap-3">
            <Button v-if="edition.quantity > 0" label="Borrow" @click="showBorrowingDialog()" outlined></Button>
            <Button v-else-if="edition.quantity == 0" label="Reserve" @click="showReservationDialog()" outlined></Button>
            <Button label="Other editions" @click="this.$router.push({
              name: 'bookEditions',
              params: {
                book: JSON.stringify(this.book),
                bookId: this.book.id,
                editionId: this.edition.id
              }
            })" outlined>
            </Button>
          </div>

          <h4 style="margin-top: 50px; margin-bottom: 20px">Book details</h4>
          <p>ISBN-13: {{ edition.isbn13 }}</p>
          <p v-if="book.titleOriginal">Original title: {{ book.titleOriginal }}</p>
          <p v-if="edition.translator">Translated by: {{ edition.translator.name }}</p>
          <p>Binding: {{ edition.binding.name }}</p>
          <p>Publisher: {{ edition.publisher.name }}</p>
          <p v-if="edition.name">Edition name: {{ edition.name }}</p>
          <p>Release year: {{ edition.releaseYear }}</p>
          <p>Number of pages: {{ edition.numberOfPages }}</p>
          <p>Weight: {{ edition.weight }}</p>
          <p>Dimensions: {{ edition.dimensions }}</p>

          <div v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'" style="margin-top: 20px">
            <FileUpload accept="image/*" :maxFileSize="1000000" @select="onFileSelected($event)">
              <template #header="{ chooseCallback, clearCallback, files }">
                <div class="flex flex-wrap justify-content-between align-items-center flex-1 gap-2">
                  <div class="flex gap-2">
                    <Button @click="chooseCallback()" icon="pi pi-images" rounded outlined></Button>
                    <Button @click="uploadEditionImage()" icon="pi pi-cloud-upload" rounded outlined severity="success" :disabled="!files || files.length === 0"></Button>
                    <Button @click="clearCallback()" icon="pi pi-times" rounded outlined severity="danger" :disabled="!files || files.length === 0"></Button>
                  </div>
                  <div>
                    <Button @click="editionDialog = true" icon="pi pi-pencil" rounded outlined></Button>
                  </div>
                </div>
              </template>
              <template #empty>
                <p>Drag and drop the image here to upload it</p>
              </template>
            </FileUpload>
          </div>
        </div>
      </div>
      <div v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'" style="width: 800px; overflow-wrap: break-word; margin-top: 10px">
        <Accordion v-if="book.description">
          <AccordionTab header="Description">{{ book.description }}</AccordionTab>
        </Accordion>
      </div>
      <div v-else style="width: 800px; overflow-wrap: break-word; margin-top: -125px">
        <Accordion v-if="book.description">
          <AccordionTab header="Description">{{ book.description }}</AccordionTab>
        </Accordion>
      </div>
    </div>
  </div>

  <Dialog v-model:visible="borrowingDialog" :style="{width: '450px'}" header="Borrowing" :modal="true" class="p-fluid">
    <div v-if="userRole == 'LIBRARIAN' || userRole == 'ADMIN'" class="field">
      <label for="user">Borrow as</label>
      <Dropdown v-model="user" :options="readers" optionLabel="name" placeholder="Choose" filter />
    </div>
    <div class="field">
      <label for="title">Title</label>
      <InputText id="title" v-model="book.title" readonly />
    </div>
    <div class="field">
      <label for="edition" :set="editionInfo = edition.publisher.name + ', ' + edition.binding.name + ' ' + edition.releaseYear">Edition</label>
      <InputText id="edition" v-model="editionInfo" readonly />
    </div>
    <div class="field">
      <label for="waitingForPickUpUntil">Waiting for pick up until</label>
      <InputText id="waitingForPickUpUntil" v-model="waitingForPickUpUntil" readonly />
    </div>
    <template #footer>
      <Button label="Cancel" class="p-button-secondary" icon="pi pi-times" @click="hideDialog()" outlined />
      <Button label="Confirm" icon="pi pi-check" @click="borrow()" outlined />
    </template>
  </Dialog>

  <Dialog v-model:visible="reservationDialog" :style="{width: '450px'}" header="Reservation" :modal="true" class="p-fluid">
    <div class="field">
      <label for="title">Title</label>
      <InputText id="title" v-model="book.title" readonly />
    </div>
    <div class="field">
      <label for="edition" :set="editionInfo = edition.publisher.name + ', ' + edition.binding.name + ' ' + edition.releaseYear">Edition</label>
      <InputText id="edition" v-if="checked" v-model="editionInfo" readonly />
      <InputText id="edition" v-else readonly placeholder="The first edition to be available" />
      <InputSwitch v-if="disable" disabled v-model="checked" style="margin-top:15px" />
      <InputSwitch v-else v-model="checked" style="margin-top:15px" />
    </div>
    <div class="field">
      <label for="waitDeadline">Wait deadline</label>
      <div class="card flex justify-content-center">
        <Calendar id="waitDeadline" v-model="waitDeadline" :manualInput="false" showIcon showButtonBar placeholder="Choose date" />
      </div>
    </div>
    <template #footer>
      <Button label="Cancel" class="p-button-secondary" icon="pi pi-times" @click="hideDialog(); waitDeadline = null" outlined />
      <Button label="Confirm" icon="pi pi-check" @click="reserve()" outlined />
    </template>
  </Dialog>

  <span v-if="editionDialog">
    <EditionDialog :editMode="true" />
  </span>
</template>

<script>
import Header from "@/components/nav/Header.vue";
import EditionService from "@/services/EditionService";
import BookService from "@/services/BookService";
import BorrowingService from "@/services/BorrowingService";
import ReservationService from "@/services/ReservationService";
import StorageService from '@/services/StorageService';
import UserService from '@/services/UserService';
import EditionDialog from "../dialogs/EditionDialog.vue";

export default {
  name: "EditionDetailsView",
  components: {
    Header, EditionDialog
  },
  data() {
    return {
      userRole: null,
      user: null,
      bookId: this.$route.params.bookId,
      editionId: this.$route.params.editionId,
      edition: {
        quantity: null,
        language: {
          name: null
        },
        binding: {
          name: null
        },
        publisher: {
          name: null
        }
      },
      book: {
        title: null,
        authors: [],
        genres: []
      },
      available: this.$route.params.available,
      borrowingDialog: false,
      reservationDialog: false,
      waitingForPickUpUntil: new Date(),
      editionInfo: null,
      waitDeadline: null,
      checked: true,
      disable: false,
      editionImage: null,
      editionDialog: false,
      readers: []
    };
  },
  methods: {
    // if there is any available edition, disable the disable edition button
    areThereManyUnavailableBookEditions() {
      EditionService.getByBookId(this.bookId).then((response) => {
        if (response.data.filter((edition) => edition.quantity > 0).length > 0) {
          this.disable = true;
        }
      })
    },
    async getBook(bookId) {
      const response = await BookService.get(bookId);
      this.book = response.data;
    },
    getUser() {
      UserService.get(localStorage.getItem('user-id')).then((response) => {
        this.user = response.data;
        this.readers.push(response.data);
      })
    },
    getReaders() {
      UserService.getReaders().then((response) => {
        response.data.forEach((reader => {
          this.readers.push(reader);
        }))
      })
    },
    async borrow() {
      if (this.user.actionPoints < 1) {
        this.$toast.add({severity:'error', summary: 'Error',
        detail: 'Could borrow the book due to borrowings/reservations limit reached', life: 10000});
      } else {
        var response;
        if (this.user.name != localStorage.getItem("user-name")) {
          response = await BorrowingService.create(this.editionId, this.user.id);
        } else {
          response = await BorrowingService.create(this.editionId);
        }
        if (response && response.status == 200) {
          this.$toast.add({severity:'success', summary: 'Success', detail: 'The book has been borrowed', life: 10000});
          this.$router.push({
            name: "userBorrowings",
            params: { userId: this.user.id },
          });
        } else {
          this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not borrow the book', life: 10000});
          this.hideDialog();
        }
      }
    },
    async reserve() {
      if (this.user.actionPoints < 1) {
        this.$toast.add({severity:'error', summary: 'Error',
        detail: 'Could reserve the book due to borrowings/reservations limit reached', life: 10000});
      } else {
        if (!this.checked) {
          this.editionId = null;
        }
        const response = await ReservationService.create(this.bookId, this.editionId, this.waitDeadline);
        if (response && response.status == 200) {
          this.$toast.add({severity:'success', summary: 'Success', detail: 'The book has been reserved', life: 10000});
          this.$router.push({
            name: "userReservations",
            params: { userId: this.user.id },
          });
        } else {
          this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not reserve the book', life: 10000});
        }
      }
    },
    onFileSelected(event) {
      this.editionImage = event.files[0];
    },
    uploadEditionImage() {
      StorageService.uploadEditionImage(this.editionImage, this.edition.id).then((response) => {
        if (response.status == 200) {
          this.$toast.add({severity:'success', summary: 'Success', detail: 'The image has been uploaded', life: 10000});
        } else {
          this.$toast.add({severity:'error', summary: 'Error', detail: 'Could not upload the image', life: 10000});
        }
      });
    },
    showBorrowingDialog() {
      if (!localStorage.getItem("user-token")) {
        this.$router.push({
          name: "login",
        });
      } else if (localStorage.getItem('user-role') == 'USER') {
        this.$toast.add({severity:'error', summary: 'Error',
        detail: 'You have no permission to borrow the book, please contact the librarian', life: 10000});
      } else {
        this.borrowingDialog = true;
      }
    },
    showReservationDialog() {
      if (!localStorage.getItem("user-token")) {
        this.$router.push({
          name: "login",
        });
      } else if (localStorage.getItem('user-role') == 'USER') {
        this.$toast.add({severity:'error', summary: 'Error',
        detail: 'You have no permission to reserve the book, please contact the librarian', life: 10000});
      } else {
        this.reservationDialog = true;
      }
    },
    hideDialog() {
      this.borrowingDialog = false;
      this.reservationDialog = false;
      this.bookDialog = false;
    },
    getEdition() {
      EditionService.get(this.$route.params.editionId).then((response) => {
        this.edition = response.data;
      })
    }
  },
  mounted() {
    if (localStorage.getItem('user-token')) {
      this.userRole = localStorage.getItem('user-role');
      this.getUser();
      if (this.userRole == 'LIBRARIAN' || this.userRole == 'ADMIN') {
        this.getReaders();
      }
    }
    this.getEdition();
    let book = this.$route.params.book;
    if (book) {
      this.book = JSON.parse(book);
      localStorage.setItem('book', JSON.stringify(this.book));
    } else {
      this.book = JSON.parse(localStorage.getItem('book'));
    }

    this.areThereManyUnavailableBookEditions();
    this.waitingForPickUpUntil.setDate(this.waitingForPickUpUntil.getDate() + 7);
    this.waitingForPickUpUntil = this.waitingForPickUpUntil.toJSON().slice(0,10);
  },
  computed : {
    authors() {
      var authors = '';
      for (var i in this.book.authors) {
        if (i > 0) {
          authors += ', '
        }
        authors += this.book.authors[i].name;
      }
      return authors;
    },
    genres() {
      var genres = '';
      for (var i in this.book.genres) {
        if (i > 0) {
          genres += ', '
        }
        genres += this.book.genres[i].name;
      }
      return genres;
    }
  }
};
</script>

<style>
body p {
  margin-top: -7px;
}
</style>