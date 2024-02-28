import axios from 'axios'

const LOANS_URL = 'http://localhost:8080/loans';
let response;

class LoanService {

  getByUserId(userId) {
    const response = axios({
      method: 'get',
      url: LOANS_URL + "/user/" + userId,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      }
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  create(userId, editionId) {
    const response = axios({
      method: 'post',
      url: LOANS_URL,
      params: {
        userId: userId,
        editionId: editionId
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

  update(loan) {
    const response = axios({
      method: 'put',
      url: LOANS_URL,
      data: loan,
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }
  
  // set loan status to finalized and its returnedOn date
  async finalize(loanId) {
    try {
      response = await axios.put(LOANS_URL + '/' + loanId + '/finalize', {}, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('user-token')}`
        },
      });
    } catch (error) {
      console.log(error.message);
    }
    return response;
  }

  // set loan status to active and its loanedOn date
  async activate(loanId) {
    console.log(loanId);
    try {
      response = await axios.put(LOANS_URL + "/" + loanId + '/activate', {}, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('user-token')}`
        },
      });
    } catch (error) {
      console.log(error.message);
    }
    return response;
  }

  // set loan status to canceled 
  async cancel(loanId) {
    try {
      response = await axios.put(LOANS_URL + "/" + loanId + '/cancel', {}, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('user-token')}`
        },
      });
    } catch (error) {
      console.log(error.message);
    }
    return response;
  }

  async setChargeAsPaid(loanId) {
    try {
      response = await axios.put(LOANS_URL + "/" + loanId + '/chargePaid', {}, {
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

export default new LoanService();
