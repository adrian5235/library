import axios from 'axios'

const TRANSLATORS_URL = 'http://localhost:8080/translators';

class TranslatorService {
  
  getAll() {
    const response = axios({
      method: 'get',
      url: TRANSLATORS_URL,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  create(translator) {
    const response = axios({
      method: 'post',
      url: TRANSLATORS_URL,
      data: translator,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  update(translator) {
    const response = axios({
      method: 'put',
      url: TRANSLATORS_URL,
      data: translator,
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

export default new TranslatorService();
