import axios from 'axios'

const LANGUAGES_URL = 'http://localhost:8080/languages';

class LanguageService {
  
  getAll() {
    const response = axios({
      method: 'get',
      url: LANGUAGES_URL,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });
    return response;
  }

  create(language) {
    const response = axios({
      method: 'post',
      url: LANGUAGES_URL,
      data: language,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });
    return response;
  }

  update(language) {
    const response = axios({
      method: 'put',
      url: LANGUAGES_URL,
      data: language,
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

export default new LanguageService();
