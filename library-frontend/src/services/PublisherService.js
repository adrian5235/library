import axios from 'axios'

const PUBLISHERS_URL = 'http://localhost:8080/publishers';

class PublisherService {
  
  getAll() {
    const response = axios({
      method: 'get',
      url: PUBLISHERS_URL,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  create(publisher) {
    const response = axios({
      method: 'post',
      url: PUBLISHERS_URL,
      data: publisher,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  update(publisher) {
    const response = axios({
      method: 'put',
      url: PUBLISHERS_URL,
      data: publisher,
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

export default new PublisherService();
