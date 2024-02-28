import axios from 'axios'

const STORAGE_URL = 'http://localhost:8080/storage';

class StorageService {

  uploadBookImage(image, bookId) {
    const formData = new FormData();
    formData.append('image', image);
    formData.append('bookId', bookId);

    axios({
      method: 'post',
      url: STORAGE_URL,
      data: formData,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });
  }

  uploadEditionImage(image, editionId) {
    const formData = new FormData();
    formData.append('image', image);
    formData.append('editionId', editionId);

    const result = axios({
      method: 'post',
      url: STORAGE_URL,
      data: formData,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });
    return result;
  }
}

export default new StorageService();