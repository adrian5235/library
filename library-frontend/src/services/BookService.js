import axios from 'axios'

const BOOKS_URL = 'http://localhost:8080/books';

class BookService {
  
  getAll() {
    const response = axios({
      method: 'get',
      url: BOOKS_URL,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  get(id) {
    const response = axios({
      method: 'get',
      url: BOOKS_URL + '/' + id,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  create(book) {
    const response = axios({
      method: 'post',
      url: BOOKS_URL,
      data: book,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  update(book) {
    const response = axios({
      method: 'put',
      url: BOOKS_URL,
      data: book,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  delete(book) {
    const response = axios({
      method: 'delete',
      url: BOOKS_URL + "/" + book.id,
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

export default new BookService();
