import axios from 'axios'

const USERS_URL = 'http://localhost:8080/users';
let response;

class UserService {
  
  async getAll() {
    try {
      response = await axios.get(USERS_URL, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('user-token')}`
        }
      });
    } catch(error) {
      console.error(error.message);
    }
    return response;
  }

  async get(userId) {
    try {
      response = await axios.get(USERS_URL + '/' + userId, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('user-token')}`
        }
      });
    } catch(error) {
      console.error(error.message);
    }
    return response;
  }

  async getReaders() {
    try {
      response = await axios.get(USERS_URL + "/readers", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('user-token')}`
        }
      });
    } catch(error) {
      console.error(error.message);
    }
    return response;
  }

  grantUserRole(userId) {
    const response = axios({
      method: 'put',
      url: USERS_URL + '/' + userId + '/grantUserRole',
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  grantReaderRole(userId) {
    const response = axios({
      method: 'put',
      url: USERS_URL + '/' + userId + '/grantReaderRole',
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  grantLibrarianRole(userId) {
    const response = axios({
      method: 'put',
      url: USERS_URL + '/' + userId + '/grantLibrarianRole',
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }
}

export default new UserService();
