import axios from 'axios'

const AUTH_URL = 'http://localhost:8080/auth';

class AuthenticationService {

  login(email, password) {
    const response = axios({
      method: 'post',
      url: AUTH_URL + '/authenticate',
      data: {
        email: email,
        password: password
      }
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  register(email, firstName, lastName, password) {
    const response = axios({
      method: 'post',
      url: AUTH_URL + '/register',
      data: {
        email: email,
        firstName: firstName[0].toUpperCase() + firstName.slice(1).toLowerCase(),
        lastName: lastName[0].toUpperCase() + lastName.slice(1).toLowerCase(),
        password: password
      }
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  changePassword(currentPassword, newPassword, confirmPassword) {
    const response = axios({
      method: 'patch',
      url: AUTH_URL + '/changePassword',
      data: {
        currentPassword: currentPassword,
        newPassword: newPassword,
        confirmationPassword: confirmPassword
      },
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      }
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  resetPasswordRequest(email) {
    const response = axios({
      method: 'patch',
      url: AUTH_URL + '/resetPasswordRequest',
      params: {
        email: email
      }
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  resetPassword(token, password, confirmPassword) {
    const response = axios({
      method: 'patch',
      url: AUTH_URL + '/resetPassword',
      data: {
        newPassword: password,
        confirmationPassword: confirmPassword
      },
      params: {
        token: token
      }
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }
}

export default new AuthenticationService();