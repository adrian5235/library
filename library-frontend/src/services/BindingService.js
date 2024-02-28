import axios from 'axios'

const BINDING_URL = 'http://localhost:8080/bindings';

class BindingService {
  
  getAll() {
    const response = axios({
      method: 'get',
      url: BINDING_URL,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  create(binding) {
    const response = axios({
      method: 'post',
      url: BINDING_URL,
      data: binding,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  update(binding) {
    const response = axios({
      method: 'put',
      url: BINDING_URL,
      data: binding,
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

export default new BindingService();
