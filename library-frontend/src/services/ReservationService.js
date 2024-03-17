import axios from 'axios'

const RESERVATIONS_URL = 'http://localhost:8080/reservations';
let response;

class ReservationService {

  getByUserId(userId) {
    const response = axios({
      method: 'get',
      url: RESERVATIONS_URL + "/user/" + userId,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      }
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  create(bookId, editionId, waitDeadline) {
    const response = axios({
      method: 'post',
      url: RESERVATIONS_URL,
      params: {
        bookId: bookId,
        editionId: editionId,
        waitDeadline: waitDeadline
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

  update(reservation) {
    const response = axios({
      method: 'put',
      url: RESERVATIONS_URL,
      data: reservation,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  async cancel(reservationId) {
    try {
      response = await axios.put(RESERVATIONS_URL + "/" + reservationId + "/cancel", {
      }, {
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

export default new ReservationService();