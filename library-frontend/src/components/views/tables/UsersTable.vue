<template>
  <Header />
  <div class="flex justify-content-center">
    <div class="card">
      <DataTable :value="users" tableStyle="width: 90vw" paginator :rows="10" :rowsPerPageOptions="[10, 20, 30]" 
      sortField="role" :sortOrder="1" removableSort v-model:filters="filters" filterDisplay="row"
      :globalFilterFields="['name', 'email', 'role']">
        <template #header>
          <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
            <h4 class="m-0">Users</h4>
            <span class="p-input-icon-left">
              <i class="pi pi-search" />
              <InputText v-model="filters['global'].value" placeholder="Keyword" />
            </span>
          </div>
        </template>
        <template #empty> Could not find any data </template>
        <template #loading> Loading data, please wait. </template>
        <Column field="name" header="Name" style="width: 25%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Name" />
          </template>
        </Column>
        <Column field="email" header="Email" style="width: 25%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Email" />
          </template>
        </Column>
        <Column field="role" header="Role" style="width: 25%" sortable>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()" class="p-column-filter" placeholder="Role" />
          </template>

          <template #body="slotProps">
            <span v-if="slotProps.data.role == 'USER'">{{ 'User' }}</span>
            <span v-else-if="slotProps.data.role == 'READER'">{{ 'Reader' }}</span>
            <span v-else-if="slotProps.data.role == 'LIBRARIAN'">{{ 'Librarian' }}</span>
            <span v-else-if="slotProps.data.role == 'ADMIN'">{{ 'Admin' }}</span>
          </template>
        </Column>
        <Column :exportable="false">
          <template #body="slotProps">
            <Button
              icon="pi pi-book"
              outlined rounded class="mr-2"
              @click="goToBorrowings(slotProps.data)"
              style="margin-right: 5px"
            />
            <Button 
              icon="pi pi-hourglass"
              outlined rounded class="mr-2"
              @click="goToReservations(slotProps.data.id, slotProps.data.name)"
              style="margin-right: 5px"
            />
            <Button
              v-if="slotProps.data.role == 'USER'"
              icon="pi pi-check"
              outlined rounded class="mr-2"
              @click="showGrantReaderRoleConfirmationDialog(slotProps.data)"
              style="margin-right: 5px"
            />
            <Button
              v-if="slotProps.data.role == 'READER'"
              icon="pi pi-times"
              outlined rounded class="mr-2"
              @click="showGrantReaderRoleConfirmationDialog(slotProps.data)"
              style="margin-right: 5px"
            />
            <Button
              v-if="slotProps.data.role == 'READER' && userRole == 'ADMIN'"
              icon="pi pi-check"
              outlined rounded class="mr-2"
              @click="showGrantLibrarianRoleConfirmationDialog(slotProps.data)"
              style="margin-right: 5px"
            />
            <Button
              v-if="slotProps.data.role == 'LIBRARIAN' && userRole == 'ADMIN'"
              icon="pi pi-times"
              outlined rounded class="mr-2"
              @click="showGrantReaderRoleConfirmationDialog(slotProps.data)"
              style="margin-right: 5px"
            />
          </template>
        </Column>
      </DataTable>
    </div>
  </div>

  <Dialog
    v-model:visible="grantReaderRoleConfirmationDialog"
    :style="{ width: '450px' }"
    header="Confirmation"
    :modal="true"
  >
    <div class="confirmation-content">
      <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
      <span v-if="user.role == 'USER'">Are you sure you want to grant reader's privileges to {{ user.name }}?
      </span>
      <span v-if="user.role == 'READER'">Are you sure you want to deny {{ user.name }} reader's privileges?
      </span>
      <span v-if="user.role == 'LIBRARIAN' && userRole == 'ADMIN'">Are you sure you want to deny {{ user.name }} librarian's privileges?
      </span>
    </div>
    <template #footer>
      <Button
        label="No"
        icon="pi pi-times"
        outlined
        @click="grantReaderRoleConfirmationDialog = false"
      />
      <Button 
        v-if="user.role == 'USER'" 
        label="Yes" 
        icon="pi pi-check"
        outlined 
        @click="grantReaderRole()" 
      />
      <Button 
        v-if="user.role == 'READER'" 
        label="Yes" 
        icon="pi pi-check"
        outlined 
        @click="grantUserRole()" 
      />
      <Button 
        v-if="user.role == 'LIBRARIAN' && userRole == 'ADMIN'" 
        label="Yes" 
        icon="pi pi-times"
        outlined 
        @click="grantReaderRole()" 
      />
    </template>
  </Dialog>

  <Dialog
    v-model:visible="grantLibrarianRoleConfirmationDialog"
    :style="{ width: '450px' }"
    header="Confirmation"
    :modal="true"
  >
    <div class="confirmation-content">
      <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
      <span>Are you sure you want to grant librarian's privileges to {{ user.name }}?
      </span>
    </div>
    <template #footer>
      <Button
        label="No"
        icon="pi pi-times"
        outlined
        @click="grantLibrarianRoleConfirmationDialog = false"
      />
      <Button 
        label="Yes" 
        icon="pi pi-check"
        outlined
        @click="grantLibrarianRole()" 
      />
    </template>
  </Dialog>
</template>

<script>
import Header from '@/components/nav/Header.vue';
import UserService from '@/services/UserService';
import { FilterMatchMode } from 'primevue/api';

export default {
  name: "UsersTableView",
  components: {
    Header
  },
  data() {
    return {
      userRole: null,
      users: null,
      user: null,
      grantReaderRoleConfirmationDialog: false,
      grantLibrarianRoleConfirmationDialog: false,
      filters: {
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        name: { value: null, matchMode: FilterMatchMode.CONTAINS },
        email: { value: null, matchMode: FilterMatchMode.CONTAINS },
        role: { value: null, matchMode: FilterMatchMode.CONTAINS }
      },
    };
  },
  methods: {
    getUsers() {
      UserService.getAll().then((response) => {
        this.users = response.data;
      });
    },
    goToBorrowings(user) {
      this.$router.push({
        name: "userBorrowings",
        params: { 
          userId: user.id
        },
      });
    },
    goToReservations(userId) {
      this.$router.push({
        name: "userReservations",
        params: { 
          userId: userId
        },
      });
    },
    showGrantReaderRoleConfirmationDialog(user) {
      this.user = user;
      this.grantReaderRoleConfirmationDialog = true;
    },
    showGrantLibrarianRoleConfirmationDialog(user) {
      this.user = user;
      this.grantLibrarianRoleConfirmationDialog = true;
    },
    hideConfirmationDialog() {
      this.user = null,
      this.grantReaderRoleConfirmationDialog = false;
      this.grantLibrarianRoleConfirmationDialog = false;
    },
    async grantUserRole() {
      const response = await UserService.grantUserRole(this.user.id);

      if (response && response.status == 200) {
        this.user.role = 'USER';
        this.$toast.add({
          severity: "success",
          summary: "Success",
          detail: "The role has been taken away",
          life: 3000
        });
      } else {
        this.$toast.add({
          severity: "error",
          summary: "Error",
          detail: "Could not take away the role",
          life: 3000
        });
      }

      this.hideConfirmationDialog();
    },
    async grantReaderRole() {
      const response = await UserService.grantReaderRole(this.user.id);

      if (response && response.status == 200) {
        this.user.role = 'READER';
        this.$toast.add({
          severity: "success",
          summary: "Success",
          detail: "The role has been granted",
          life: 3000
        });
      } else {
        this.$toast.add({
          severity: "error",
          summary: "Error",
          detail: "Could not grant the role",
          life: 3000
        });
      }

      this.hideConfirmationDialog();
    },
    async grantLibrarianRole() {
      const response = await UserService.grantLibrarianRole(this.user.id);

      if (response && response.status == 200) {
        this.user.role = 'LIBRARIAN';
        this.$toast.add({
          severity: "success",
          summary: "Success",
          detail: "The role has been granted",
          life: 3000
        });
      } else {
        this.$toast.add({
          severity: "error",
          summary: "Error",
          detail: "Could not grant the role",
          life: 3000
        });
      }

      this.hideConfirmationDialog();
    }
  },
  mounted() {
    this.userRole = localStorage.getItem('user-role');
    this.getUsers();
  }
};
</script>