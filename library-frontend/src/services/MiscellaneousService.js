import axios from 'axios'

const MISCELLANEOUS_URL = 'http://localhost:8080/miscellaneous';

class MiscellaneousService {
  
  get() {
    const response = axios({
      method: 'get',
      url: MISCELLANEOUS_URL
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  update(miscellaneous) {
    const response = axios({
      method: 'put',
      url: MISCELLANEOUS_URL,
      data: miscellaneous,
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

export default new MiscellaneousService();
