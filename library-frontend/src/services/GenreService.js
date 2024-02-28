import axios from 'axios'

const GENRE_URL = 'http://localhost:8080/genres';

class GenreService {

  getAll() {
    const response = axios({
      method: 'get',
      url: GENRE_URL,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  create(genre) {
    const response = axios({
      method: 'post',
      url: GENRE_URL,
      data: genre,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  update(genre) {
    const response = axios({
      method: 'put',
      url: GENRE_URL,
      data: genre,
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

export default new GenreService();