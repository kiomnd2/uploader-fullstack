import HttpClient from './client';

const UploadApi = class {
  #client;
  constructor(client) {
    this.#client = client;
  }
  inquireUUID() {
    return this.#client.get('/api/uuid').then(({ data }) => data || {});
  }
};

export default new UploadApi(HttpClient);
