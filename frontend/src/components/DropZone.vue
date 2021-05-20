<template>
  <div>
    <vue-dropzone
      ref="dropzone"
      id="dropzone"
      :options="options"
      @vdropzone-drop="fileAddedEvent"
      @vdropzone-complete="completeHandler"
    ></vue-dropzone>
    <button id="fileUpload" @click="fileUpload">upload</button>
    <div>
      <progress-bar></progress-bar>
    </div>
  </div>
</template>

<script>
import vue2Dropzone from 'vue2-dropzone';
import 'vue2-dropzone/dist/vue2Dropzone.min.css';
import UploadApi from '@/api/upload.api.js';
import ProgressBar from '@/components/ProgressBar';
export default {
  name: 'DropZone',
  components: {
    ProgressBar,
    vueDropzone: vue2Dropzone,
  },
  data: function () {
    return {
      uuid: '',
      intarval: '',
      options: {
        headers: {
          'X-UPLOAD-UUID': this.uuid,
        },
        url: '/api/upload',
        thumbnailWidth: 150,
        chunking: true,
        chunkSize: 1000000,
        maxFilesize: 10,
        maxFiles: 1,
        clickable: false,
        autoProcessQueue: false,
        acceptedFiles: '.csv',
      },
    };
  },
  methods: {
    fileAddedEvent: async function () {
      if (this.$refs.dropzone.getAcceptedFiles().length >= 1) {
        this.$refs.dropzone.removeAllFiles();
      }
    },
    fileUpload: async function () {
      const res = await UploadApi.inquireUUID();
      this.uuid = res.body;
      this.$refs.dropzone.setOption('headers', {
        'X-UPLOAD-UUID': this.uuid,
      });
      this.$refs.dropzone.processQueue();

      this.intarval = setInterval(this.polling, 1000);
    },
    completeHandler: function (response) {
      this.$refs.dropzone.removeAllFiles();
      const res = JSON.parse(response.xhr.response);
      clearInterval(this.intarval);
      console.log('result', res);
    },
    polling: async function () {
      const a = await UploadApi.getCount(this.uuid);
      console.log(a);
    },
  },
};
</script>

<style scoped></style>
