import axios from 'axios';

const HttpClient = axios.create({
  timeout: 30000,
  headers: {
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json; charset = utf-8',
  },
});

export default HttpClient;
