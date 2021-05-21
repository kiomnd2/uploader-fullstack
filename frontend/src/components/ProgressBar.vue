<template>
  <div>
    <b-progress
      :value="progress"
      :max="max"
      show-progress
      animated
    ></b-progress>
    <b-progress class="mt-2" :max="max" show-value>
      <b-progress-bar
        :value="
          Math.floor((successCount / (failCount + successCount)) * progress)
        "
        variant="success"
      ></b-progress-bar>
      <b-progress-bar
        :value="
          progress -
          Math.floor((successCount / (failCount + successCount)) * progress)
        "
        variant="danger"
      ></b-progress-bar>
    </b-progress>
  </div>
</template>

<script>
export default {
  name: 'ProgressBar',
  props: {
    progress: Number,
    successCount: Number,
    failCount: Number,
  },
  data() {
    return {
      max: 100,
    };
  },
};
</script>

<style scoped></style>
