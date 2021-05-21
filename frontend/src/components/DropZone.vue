<template>
  <div>
    <vue-dropzone
      ref="dropzone"
      id="dropzone"
      :options="options"
      @vdropzone-drop="fileAddedEvent"
      @vdropzone-success="successHandler"
      @vdropzone-error="errorHandler"
      @vdropzone-upload-progress="progressHandler"
    ></vue-dropzone>
    <button id="fileUpload" @click="fileUpload">upload</button>
    <div>
      <progress-bar
        :progress="progress"
        :successCount="successCount"
        :failCount="failCount"
      ></progress-bar>
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
      progress: 0,
      successCount: 0,
      failCount: 0,
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
        autoProcessQueue: false,
        acceptedFiles: '.csv',
      },
    };
  },
  methods: {
    fileAddedEvent: async function () {
      if (
        this.$refs.dropzone.getAcceptedFiles().length >= 1 ||
        this.$refs.dropzone.getRejectedFiles().length >= 1
      ) {
        this.$refs.dropzone.removeAllFiles();
      }
    },
    fileUpload: async function () {
      if (this.$refs.dropzone.getAcceptedFiles() < 1) {
        alert('업로드 할 수 없습니다.');
        return;
      }
      const res = await UploadApi.inquireUUID();
      this.uuid = res.body;
      this.$refs.dropzone.setOption('headers', {
        'X-UPLOAD-UUID': this.uuid,
      });
      this.$refs.dropzone.processQueue();
      this.intarval = setInterval(this.polling, 1000);
    },
    successHandler: function (file) {
      console.log(file);
      const res = JSON.parse(file.xhr.response);
      clearInterval(this.intarval);
      this.updateCount(res.body);
      console.log(res.body);
    },
    errorHandler: function () {
      clearInterval(this.intarval);
      this.clearCount();
    },
    progressHandler: function (file, progress) {
      this.progress = progress;
    },
    polling: async function () {
      const countInfo = await UploadApi.getCount(this.uuid);
      this.updateCount(countInfo.body);
    },
    clearCount: function () {
      this.updateCount({ successCount: 0, failCount: 0 });
      this.progress = 0;
    },
    updateCount: function (obj) {
      this.successCount = obj.successCount;
      this.failCount = obj.failCount;
    },
  },
};
</script>

<style scoped></style>
