import axios from 'axios'

const BORROWINGS_URL = 'http://localhost:8080/borrowings';
let response;

class BorrowingService {

  getByUserId(userId) {
    const response = axios({
      method: 'get',
      url: BORROWINGS_URL + "/user/" + userId,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      }
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  create(editionId, userId) {
    const response = axios({
      method: 'post',
      url: BORROWINGS_URL,
      params: {
        editionId: editionId,
        userId: userId
      },
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  update(borrowing) {
    const response = axios({
      method: 'put',
      url: BORROWINGS_URL,
      data: borrowing,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }
  
  // set borrowing status to finalized and its returnedOn date
  async finalize(borrowingId) {
    try {
      response = await axios.put(BORROWINGS_URL + '/' + borrowingId + '/finalize', {}, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('user-token')}`
        },
      });
    } catch (error) {
      console.log(error.message);
    }
    return response;
  }

  // set borrowing status to active and its borrowedOn date
  async activate(borrowingId) {
    console.log(borrowingId);
    try {
      response = await axios.put(BORROWINGS_URL + "/" + borrowingId + '/activate', {}, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('user-token')}`
        },
      });
    } catch (error) {
      console.log(error.message);
    }
    return response;
  }

  // set borrowing status to canceled 
  async cancel(borrowingId) {
    try {
      response = await axios.put(BORROWINGS_URL + "/" + borrowingId + '/cancel', {}, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('user-token')}`
        },
      });
    } catch (error) {
      console.log(error.message);
    }
    return response;
  }

  async setChargeAsPaid(borrowingId) {
    try {
      response = await axios.put(BORROWINGS_URL + "/" + borrowingId + '/chargePaid', {}, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('user-token')}`
        },
      });
    } catch (error) {
      console.log(error.message);
    }
    return response;
  }
}

export default new BorrowingService();
