import axios from 'axios'

const COPY_URL = 'http://localhost:8080/copies';

class CopyService {

  getAll() {
    const response = axios({
      method: 'get',
      url: COPY_URL,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  getByEditionId(editionId) {
    const response = axios({
      method: 'get',
      url: COPY_URL + "/edition/" + editionId,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      }
    })
    .catch(function (error) {
      console.log(error);
    });
    
    return response;
  }

  create(copy) {
    console.log(copy);
    const response = axios({
      method: 'post',
      url: COPY_URL,
      data: copy,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  update(copy) {
    const response = axios({
      method: 'put',
      url: COPY_URL,
      data: copy,
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

export default new CopyService();