<template>
  <div>
    <VueDropzone
      ref="dropzone"
      id="dropzone"
      :options="dropzoneOptions"
      @vdropzone-drop="onFileDropHandler"
      @vdropzone-success="onUploadSuccessHandler"
      @vdropzone-complete="onUploadCompleteHandler"
      @vdropzone-error="onUploadErrorHandler"
      @vdropzone-upload-progress="onUploadProgressHandler"
    ></VueDropzone>
    <ProgressBar
      :progress="progress.upload"
      :successCount="progress.success"
      :failCount="progress.fail"
    ></ProgressBar>
  </div>
</template>

<script>
import VueDropzone from 'vue2-dropzone';
import ProgressBar from '@/components/ProgressBar';
import UploadApi from '@/api/upload.api.js';

export default {
  components: {
    VueDropzone,
    ProgressBar,
  },
  data() {
    return {
      interval: -1,
      uuid: null,
      progress: {
        upload: 0,
        success: 0,
        fail: 0,
      },
      dropzoneOptions: {
        headers: {
          'X-UPLOAD-UUID': this.uuid,
        },
        url: '/api/upload',
        thumbnailWidth: 150,
        chunking: true,
        chunkSize: 1000000,
        maxFilesize: 10,
        maxFiles: 1,
        parallelUploads: 1,
        autoProcessQueue: false,
        acceptedFiles: '.csv',
      },
    };
  },
  methods: {
    async fileUpload() {
      this.clearCount();
      this.$refs.dropzone.disable();
      try {
        this.uuid = await UploadApi.generateUploadUUID();
        this.$refs.dropzone.setOption('headers', {
          'X-UPLOAD-UUID': this.uuid,
        });

        // 파일 업로드
        this.$refs.dropzone.processQueue();

        // 업로드 상태 표시를 위한 타이머 실행
        this.startPollingTimer();
      } catch (e) {
        this.$bvToast.toast(`업로드 중 오류 발생: ${e.message}`, {
          title: '업로드 오류',
          variant: 'danger',
          toaster: 'b-toaster-bottom-full',
          appendToast: true,
          solid: true,
          noCloseButton: true,
          autoHideDelay: 2000,
        });
      }
    },
    onFileDropHandler() {
      if (
        this.$refs.dropzone.getAcceptedFiles().length >= 1 ||
        this.$refs.dropzone.getRejectedFiles().length >= 1
      ) {
        this.$refs.dropzone.removeAllFiles();
      }
      this.fileUpload();
    },

    onUploadProgressHandler(file, progress) {
      this.progress.upload = progress;
    },

    onUploadSuccessHandler(res) {
      const data = JSON.parse(res.xhr.response);
      this.updateCount(data.body);
      this.stopPollingTimer();
    },

    onUploadErrorHandler() {
      this.stopPollingTimer();
      this.clearCount();
    },

    onUploadCompleteHandler() {
      this.$refs.dropzone.enable();
    },
    startPollingTimer() {
      this.interval = setInterval(async () => {
        const info = await UploadApi.getUploadStatus(this.uuid);
        this.updateCount(info);
      }, 1000);
    },

    stopPollingTimer() {
      clearInterval(this.interval);
      this.interval = -1;
    },

    clearCount: function () {
      this.progress.upload = 0;
      this.progress.success = 0;
      this.progress.fail = 0;
    },

    updateCount: function (obj) {
      this.progress.success = obj.successCount;
      this.progress.fail = obj.failCount;
    },
  },
};
</script>

<style scoped></style>
