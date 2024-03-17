<template>
  <div style="background-color: #333; margin-bottom: 35px">
    <Menubar :model="itemsLeft" class="p-menubar" style="background-color: #333; border: none; font-size: 17px">
      <template #start>
        <div class="sm:ml-8">
          <svg width="35" height="40" viewBox="0 0 35 40" fill="none" xmlns="http://www.w3.org/2000/svg" class="h-2rem">
            <path
              d="M25.87 18.05L23.16 17.45L25.27 20.46V29.78L32.49 23.76V13.53L29.18 14.73L25.87 18.04V18.05ZM25.27 35.49L29.18 31.58V27.67L25.27 30.98V35.49ZM20.16 17.14H20.03H20.17H20.16ZM30.1 5.19L34.89 4.81L33.08 12.33L24.1 15.67L30.08 5.2L30.1 5.19ZM5.72 14.74L2.41 13.54V23.77L9.63 29.79V20.47L11.74 17.46L9.03 18.06L5.72 14.75V14.74ZM9.63 30.98L5.72 27.67V31.58L9.63 35.49V30.98ZM4.8 5.2L10.78 15.67L1.81 12.33L0 4.81L4.79 5.19L4.8 5.2ZM24.37 21.05V34.59L22.56 37.29L20.46 39.4H14.44L12.34 37.29L10.53 34.59V21.05L12.42 18.23L17.45 26.8L22.48 18.23L24.37 21.05ZM22.85 0L22.57 0.69L17.45 13.08L12.33 0.69L12.05 0H22.85Z"
              fill="var(--primary-color)"
            />
            <path
              d="M30.69 4.21L24.37 4.81L22.57 0.69L22.86 0H26.48L30.69 4.21ZM23.75 5.67L22.66 3.08L18.05 14.24V17.14H19.7H20.03H20.16H20.2L24.1 15.7L30.11 5.19L23.75 5.67ZM4.21002 4.21L10.53 4.81L12.33 0.69L12.05 0H8.43002L4.22002 4.21H4.21002ZM21.9 17.4L20.6 18.2H14.3L13 17.4L12.4 18.2L12.42 18.23L17.45 26.8L22.48 18.23L22.5 18.2L21.9 17.4ZM4.79002 5.19L10.8 15.7L14.7 17.14H14.74H15.2H16.85V14.24L12.24 3.09L11.15 5.68L4.79002 5.2V5.19Z"
              fill="var(--text-color)"
            />
          </svg>
        </div>
      </template>
      <template #end>
        <div class="sm:mr-8">
          <span v-if="returnDeadlineIsComing" style="color:orangered; margin-right: 20px; font-size: 20px">
           !!! The return deadline is approaching, days remaining: {{ daysToReturnDeadline }} !!!</span>
          <span v-else-if="returnDeadlineHasPassed" style="color:red; margin-right: 20px; font-size: 20px">!!! The return deadline has passed !!!</span>
          <Menu v-if="loggedIn" ref="menu" id="overlay_menu" :model="itemsRight" :popup="true" style="font-size: 17px" />
          <Avatar v-if="loggedIn" :label="avatarLabel" shape="circle" @click="toggle($event)" />
          <Button v-else @click="login()" icon="pi pi-sign-in" style="background-color: #333; border: none" label="Log in"></Button>
        </div>
      </template>
    </Menubar>
  </div>
</template>

<script>
import BorrowingService from '@/services/BorrowingService';

export default {
  name: "Header",
  data() {
    return {
      itemsLeft: [
        {
          label: 'Homepage',
          icon: 'pi pi-home',
          command: () => {
            this.$router.push('/');
          }
        }
      ],
      itemsRight: [],
      loggedIn: false,
      avatarLabel: null,
      returnDeadlineIsComing: false,
      returnDeadlineHasPassed: false,
      daysToReturnDeadline: null
    };
  },
  methods: {
    toggle(event) {
      this.$refs.menu.toggle(event);
    },
    login() {
      this.$router.push('/login');
    }
  },
  mounted() {
    let userToken = localStorage.getItem('user-token');
    
    if (userToken) {
      this.loggedIn = true;
      this.userRole = localStorage.getItem('user-role');
      var userName = localStorage.getItem('user-name');

      this.avatarLabel = userName.charAt(0);

      if (this.userRole == 'LIBRARIAN' || this.userRole == 'ADMIN') {
        this.itemsLeft.push({
          label: 'Manage',
          icon: 'pi pi-folder',
          items: [
            {
              label: 'Users',
              icon: 'pi pi-folder-open',
              command: () => {
                this.$router.push('/users');
              }
            },
            {
              label: 'Copies',
              icon: 'pi pi-folder-open',
              command: () => {
                this.$router.push('/copies');
              }
            },
            {
              label: 'Editions',
              icon: 'pi pi-folder-open',
              command: () => {
                this.$router.push('/editions');
              }
            },
            {
              label: 'Books',
              icon: 'pi pi-folder-open',
              command: () => {
                this.$router.push('/books');
              }
            },
            {
              label: 'Authors',
              icon: 'pi pi-folder-open',
              command: () => {
                this.$router.push('/authors');
              }
            },
            {
              label: 'Genres',
              icon: 'pi pi-folder-open',
              command: () => {
                this.$router.push('/genres');
              }
            },
            {
              label: 'Languages',
              icon: 'pi pi-folder-open',
              command: () => {
                this.$router.push('/languages');
              }
            },
            {
              label: 'Publishers',
              icon: 'pi pi-folder-open',
              command: () => {
                this.$router.push('/publishers');
              }
            },
            {
              label: 'Translators',
              icon: 'pi pi-folder-open',
              command: () => {
                this.$router.push('/translators');
              }
            },
            {
              label: 'Bindings',
              icon: 'pi pi-folder-open',
              command: () => {
                this.$router.push('/bindings');
              }
            },
          ]
        });
      }

      this.itemsRight.push({
        label: localStorage.getItem("user-name"),
        icon: 'pi pi-user',
        items: []
      });

      if (this.userRole == 'LIBRARIAN' || this.userRole == 'ADMIN' || this.userRole == 'READER') {
        this.itemsLeft.push({
          label: 'Statistics',
          icon: 'pi pi-chart-bar',
          command: () => {
            this.$router.push('/stats');
          }
        });

        this.itemsRight[0].items.push({
          label: 'Borrowings',
          icon: 'pi pi-folder-open',
          command: () => {
            this.$router.push({
              name: "userBorrowings",
              params: { userId: localStorage.getItem("user-id") },
            });
          },
        });

        this.itemsRight[0].items.push({
          label: 'Reservations',
          icon: 'pi pi-folder-open',
          command: () => {
            this.$router.push({
              name: "userReservations",
              params: { userId: localStorage.getItem("user-id") },
            });
          }
        });
      }

      this.itemsRight[0].items.push({
          label: 'Change password',
          icon: 'pi pi-user',
          command: () => {
            this.$router.push({
              name: "changePassword"
            });
          }
        });

      this.itemsRight[0].items.push({
        label: 'Log out',
        icon: 'pi pi-sign-out',
        command: () => {
          localStorage.clear();
          this.$router.push('/login');
        }
      });

      BorrowingService.getByUserId(localStorage.getItem('user-id')).then((response) => {
        this.borrowings = response.data;
        if (this.borrowings.length > 0) {
          this.borrowings.forEach((borrowing) => {
            if (borrowing.status.name == "active") {
              var returnDeadline = Date.parse(borrowing.returnDeadline);
              var today = Date.parse(new Date());
              if (today > returnDeadline) {
                this.returnDeadlineHasPassed = true;
              } else {
                var daysDifference = Math.round((returnDeadline - today) / (1000 * 3600 * 24));
                if (daysDifference <= 7) {
                  this.daysToReturnDeadline = daysDifference;
                  this.returnDeadlineIsComing = true;
                }
              }
            }
          })
        }
      });
    }

    this.itemsLeft.push(
      {
        label: 'About',
        icon: 'pi pi-home',
        command: () => {
          this.$router.push('/about');
        }
      },
      {
        label: 'Rules',
        icon: 'pi pi-home',
        command: () => {
          this.$router.push('/rules');
        }
      },
      {
        label: 'Contact',
        icon: 'pi pi-home',
        command: () => {
          this.$router.push('/contact');
        }
      }
    )
  }
};
</script>

<style>
.p-menuitem-text {
  color: #f2f2f2 !important;
}

.p-menubar .p-menuitem-content, .p-menu {
  background-color: #333 !important;
}

.p-submenu-header, .p-menuitem-content {
  color: #f2f2f2 !important;
  background-color: #3b3b3b !important;
}

.p-menuitem-link:hover {
  background-color: #424242 !important;
}
</style>