import axios from 'axios'

const AUTHORS_URL = 'http://localhost:8080/authors';

class AuthorService {
  
  async getAll() {
    const response = await axios.get(AUTHORS_URL);
    return response;
  }

  create(author) {
    const response = axios({
      method: 'post',
      url: AUTHORS_URL,
      data: author,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  update(author) {
    const response = axios({
      method: 'put',
      url: AUTHORS_URL,
      data: author,
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

export default new AuthorService();
