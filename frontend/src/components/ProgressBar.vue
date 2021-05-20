<template>
  <div>
    <b-progress :value="value" :max="max" show-progress animated></b-progress>
    <b-progress class="mt-2" :max="max" show-value>
      <b-progress-bar
        :value="value * (6 / 10)"
        variant="success"
      ></b-progress-bar>
      <b-progress-bar
        :value="value * (2.5 / 10)"
        variant="warning"
      ></b-progress-bar>
    </b-progress>
  </div>
</template>

<script>
export default {
  name: 'ProgressBar',
  data() {
    return {
      value: 45,
      max: 100,
    };
  },
  methods: {},
};
</script>

<style scoped></style>
