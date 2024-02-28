import axios from 'axios'

const STATISTICS_URL = 'http://localhost:8080/stats';

class StatisticsService {

  getMostPopularBooks() {
    const response = axios({
      method: 'get',
      url: STATISTICS_URL + '/mostPopularBooks',
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  getMostPopularBooksByPeriodOfTime(dateFrom, dateTo) {
    const response = axios({
      method: 'get',
      url: STATISTICS_URL + '/mostPopularBooks',
      params: {
        dateFrom: dateFrom,
        dateTo: dateTo
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

  getMostPopularAuthors() {
    const response = axios({
      method: 'get',
      url: STATISTICS_URL + '/mostPopularAuthors',
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('user-token')}`
      },
    })
    .catch(function (error) {
      console.log(error);
    });

    return response;
  }

  getMostPopularAuthorsByPeriodOfTime(dateFrom, dateTo) {
    const response = axios({
      method: 'get',
      url: STATISTICS_URL + '/mostPopularAuthors',
      params: {
        dateFrom: dateFrom,
        dateTo: dateTo
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

  getMostPopularGenres(dateFrom, dateTo) {
    const response = axios({
      method: 'get',
      url: STATISTICS_URL + '/mostPopularGenres',
      params: {
        dateFrom: dateFrom,
        dateTo: dateTo
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

  getMostPopularGenresByPeriodOfTime(dateFrom, dateTo) {
    const response = axios({
      method: 'get',
      url: STATISTICS_URL + '/mostPopularGenres',
      params: {
        dateFrom: dateFrom,
        dateTo: dateTo
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
}

export default new StatisticsService();