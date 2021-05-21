<template>
  <div>
    <!-- 업로드 진행률 -->
    <b-progress
      :value="progress"
      :max="100"
      show-progress
      animated
    ></b-progress>

    <!-- 업로드 성공/실패 건수 비율 -->
    <b-progress class="mt-2" :max="maxCount" show-value>
      <b-progress-bar :value="successCount" variant="success"></b-progress-bar>
      <b-progress-bar :value="failCount" variant="danger"></b-progress-bar>
    </b-progress>
  </div>
</template>

<script>
export default {
  name: 'ProgressBar',
  props: {
    progress: {
      type: Number,
      default: 0,
    },
    successCount: {
      type: Number,
      default: 0,
    },
    failCount: {
      type: Number,
      default: 0,
    },
  },
  computed: {
    maxCount() {
      return (this.successCount + this.failCount) * (100 / this.progress);
    },
  },
};
</script>

<style scoped></style>
