import axios from 'axios';

const httpClient = axios.create({
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json; charset=utf-8',
  },
});

export default httpClient;
