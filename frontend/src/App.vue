<template>
  <div id="app" class="container">
    <Main></Main>
  </div>
</template>

<script>
import Main from './view/Main';

export default {
  name: 'App',
  components: { Main },
};
</script>

<style>
#app {
  text-align: center;
  margin-top: 40px;
}
</style>
