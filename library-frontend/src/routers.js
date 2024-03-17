import { createRouter, createWebHistory } from 'vue-router';
import Register from './components/views/auth/Register.vue';
import Login from './components/views/auth/Login.vue';
import ChangePassword from './components/views/auth/ChangePassword.vue';
import ResetPassword from './components/views/auth/ResetPassword.vue';
import Unauthenticated from './components/views/auth/Unauthenticated.vue';
import Unauthorized from './components/views/auth/Unauthorized.vue';
import CopiesTable from './components/views/tables/crud/CopiesTable.vue';
import EditionsTable from './components/views/tables/crud/EditionsTable.vue';
import BooksTable from './components/views/tables/crud/BooksTable.vue';
import AuthorsTable from './components/views/tables/crud/AuthorsTable.vue';
import GenresTable from './components/views/tables/crud/GenresTable.vue';
import LanguagesTable from './components/views/tables/crud/LanguagesTable.vue';
import BindingsTable from './components/views/tables/crud/BindingsTable.vue';
import PublishersTable from './components/views/tables/crud/PublishersTable.vue';
import TranslatorsTable from './components/views/tables/crud/TranslatorsTable.vue';
import BooksGrid from './components/views/grids/BooksGrid.vue';
import EditionsGrid from './components/views/grids/EditionsGrid.vue';
import EditionDetails from './components/views/EditionDetails.vue';
import UsersTable from './components/views/tables/UsersTable';
import UserBorrowingsTable from './components/views/tables/user/UserBorrowingsTable.vue';
import UserReservationsTable from './components/views/tables/user/UserReservationsTable.vue';
import Statistics from './components/views/Statistics.vue';
import About from './components/views/About.vue';
import Rules from './components/views/Rules.vue';
import Contact from './components/views/Contact.vue';

function isAuthenticated() {
  if (localStorage.getItem('user-token')) {
    return true;
  }
  return false;
}

function isAuthorizedAs(requiredRole) {
  if (localStorage.getItem('user-role') == requiredRole) {
    return true;
  }
  return false;
}

const routes = [
  {
    name: 'about',
    component: About,
    path: '/about'
  },
  {
    name: 'rules',
    component: Rules,
    path: '/rules'
  },
  {
    name: 'contact',
    component: Contact,
    path: '/contact'
  },
  {
    name: 'stats',
    component: Statistics,
    path: '/stats',
      beforeEnter(to, from, next) {
      if (isAuthenticated()) {
        if (isAuthorizedAs('LIBRARIAN') || isAuthorizedAs('ADMIN') || isAuthorizedAs('READER')) {
          next();
        } else {
          next('/error/unauthorized');
        }
      } else {
        next('/error/unauthenticated');
      }
    }
  },
  {
    name: 'register',
    component: Register,
    path: '/register'
  },
  {
    name: 'login',
    component: Login,
    path: '/login'
  },
  {
    name: 'changePassword',
    component: ChangePassword,
    path: '/password/change'
  },
  {
    name: 'resetPassword',
    component: ResetPassword,
    path: '/password/reset'
  },
  {
    name: 'unauthenticated',
    component: Unauthenticated,
    path: '/error/unauthenticated'
  },
  {
    name: 'unauthorized',
    component: Unauthorized,
    path: '/error/unauthorized'
  },
  {
    name: 'home',
    component: BooksGrid,
    path: '/',
  },
  {
    name: 'bookEditions',
    component: EditionsGrid,
    path: '/books/:bookId/editions',
  },
  {
    name: 'edition',
    component: EditionDetails,
    path: '/books/:bookId/editions/:editionId'
  },
  {
    name: 'users',
    component: UsersTable,
    path: '/users',
    beforeEnter(to, from, next) {
      if (isAuthenticated()) {
        if (isAuthorizedAs('LIBRARIAN') || isAuthorizedAs('ADMIN')) {
          next();
        } else {
          next('/error/unauthorized');
        }
      } else {
        next('/error/unauthenticated');
      }
    }
  },
  {
    name: 'userBorrowings',
    component: UserBorrowingsTable,
    path: '/users/:userId/borrowings',
    beforeEnter(to, from, next) {
      if (isAuthenticated()) {
        if (isAuthorizedAs('READER')) {
          if (to.params.userId == localStorage.getItem('user-id')) {
            next();
          } else {
            next('/error/unauthorized');
          }
        } else if (isAuthorizedAs('LIBRARIAN') || isAuthorizedAs('ADMIN')) {
          next();
        } else {
          next('/error/unauthorized');
        }
      } else {
        next('/error/unauthenticated');
      }
    }
  },
  {
    name: 'userReservations',
    component: UserReservationsTable,
    path: '/users/:userId/reservations',
    beforeEnter(to, from, next) {
      if (isAuthenticated()) {
        if (isAuthorizedAs('READER')) {
          if (to.params.userId == localStorage.getItem('user-id')) {
            next();
          } else {
            next('/error/unauthorized');
          }
        } else if (isAuthorizedAs('LIBRARIAN') || isAuthorizedAs('ADMIN')) {
          next();
        } else {
          next('/error/unauthorized');
        }
      } else {
        next('/error/unauthenticated');
      }
    }
  },
  {
    name: 'copies',
    component: CopiesTable,
    path: '/copies',
    beforeEnter(to, from, next) {
      if (isAuthenticated()) {
        if (isAuthorizedAs('LIBRARIAN') || isAuthorizedAs('ADMIN')) {
          next();
        } else {
          next('/error/unauthorized');
        }
      } else {
        next('/error/unauthenticated');
      }
    }
  },
  {
    name: 'editions',
    component: EditionsTable,
    path: '/editions',
    beforeEnter(to, from, next) {
      if (isAuthenticated()) {
        if (isAuthorizedAs('LIBRARIAN') || isAuthorizedAs('ADMIN')) {
          next();
        } else {
          next('/error/unauthorized');
        }
      } else {
        next('/error/unauthenticated');
      }
    }
  },
  {
    name: 'books',
    component: BooksTable,
    path: '/books',
    beforeEnter(to, from, next) {
      if (isAuthenticated()) {
        if (isAuthorizedAs('LIBRARIAN') || isAuthorizedAs('ADMIN')) {
          next();
        } else {
          next('/error/unauthorized');
        }
      } else {
        next('/error/unauthenticated');
      }
    }
  },
  {
    name: 'authors',
    component: AuthorsTable,
    path: '/authors',
    beforeEnter(to, from, next) {
      if (isAuthenticated()) {
        if (isAuthorizedAs('LIBRARIAN') || isAuthorizedAs('ADMIN')) {
          next();
        } else {
          next('/error/unauthorized');
        }
      } else {
        next('/error/unauthenticated');
      }
    }
  },
  {
    name: 'genres',
    component: GenresTable,
    path: '/genres',
    beforeEnter(to, from, next) {
      if (isAuthenticated()) {
        if (isAuthorizedAs('LIBRARIAN') || isAuthorizedAs('ADMIN')) {
          next();
        } else {
          next('/error/unauthorized');
        }
      } else {
        next('/error/unauthenticated');
      }
    }
  },
  {
    name: 'languages',
    component: LanguagesTable,
    path: '/languages',
    beforeEnter(to, from, next) {
      if (isAuthenticated()) {
        if (isAuthorizedAs('LIBRARIAN') || isAuthorizedAs('ADMIN')) {
          next();
        } else {
          next('/error/unauthorized');
        }
      } else {
        next('/error/unauthenticated');
      }
    }
  },
  {
    name: 'publishers',
    component: PublishersTable,
    path: '/publishers',
    beforeEnter(to, from, next) {
      if (isAuthenticated()) {
        if (isAuthorizedAs('LIBRARIAN') || isAuthorizedAs('ADMIN')) {
          next();
        } else {
          next('/error/unauthorized');
        }
      } else {
        next('/error/unauthenticated');
      }
    }
  },
  {
    name: 'translators',
    component: TranslatorsTable,
    path: '/translators',
    beforeEnter(to, from, next) {
      if (isAuthenticated()) {
        if (isAuthorizedAs('LIBRARIAN') || isAuthorizedAs('ADMIN')) {
          next();
        } else {
          next('/error/unauthorized');
        }
      } else {
        next('/error/unauthenticated');
      }
    }
  },
  {
    name: 'bindings',
    component: BindingsTable,
    path: '/bindings',
    beforeEnter(to, from, next) {
      if (isAuthenticated()) {
        if (isAuthorizedAs('LIBRARIAN') || isAuthorizedAs('ADMIN')) {
          next();
        } else {
          next('/error/unauthorized');
        }
      } else {
        next('/error/unauthenticated');
      }
    }
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;