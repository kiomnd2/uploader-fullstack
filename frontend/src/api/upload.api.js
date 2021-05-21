import HttpClient from './client';

class UploadAPI {
  #client;

  constructor(client) {
    this.#client = client;
  }

  /**
   * 업로드용 UUID 발급
   */
  async generateUploadUUID() {
    const { data } = await this.#client.get('/api/uuid');
    return data.body;
  }

  /**
   * UUID에 대한 업로드 상태 조회
   * @param {string} uuid 업로드 UUID
   */
  async getUploadStatus(uuid) {
    const headers = { 'X-UPLOAD-UUID': uuid };
    const { data } = await this.#client.get('/api/inquire', { headers });
    return data.body || {};
  }
}

export default new UploadAPI(HttpClient);
