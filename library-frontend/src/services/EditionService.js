import axios from 'axios'

const EDITIONS_URL = 'http://localhost:8080/editions';
let response;

class EditionService {

  getAll() {
    const response = axios({
      method: 'get',
      url: EDITIONS_URL
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  async get(editionId) {
    try {
      response = await axios.get(EDITIONS_URL + "/" + editionId);
    } catch (error) {
      console.log(error.message);
    }
    return response;
  }
  
  getByBookId(bookId) {
    const response = axios({
      method: 'get',
      url: EDITIONS_URL + "/book/" + bookId
    })
    .catch(function (error) {
      console.log(error);
    });
    
    return response;
  }

  getOnePerBook() {
    const response = axios({
      method: 'get',
      url: EDITIONS_URL + "/onePerBook"
    })
    .catch(function (error) {
      console.log(error);
    });
    
    return response;
  }

  getByCopyId(copyId) {
    const response = axios({
      method: 'get',
      url: EDITIONS_URL + '/copy' + copyId,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  create(edition) {
    const response = axios({
      method: 'post',
      url: EDITIONS_URL,
      data: edition,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });
    return response;
  }

  update(edition) {
    const response = axios({
      method: 'put',
      url: EDITIONS_URL,
      data: edition,
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

export default new EditionService();
