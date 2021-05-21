import Vue from 'vue';
import App from './App.vue';
import { BootstrapVue } from 'bootstrap-vue';

import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';
import 'vue2-dropzone/dist/vue2Dropzone.min.css';

Vue.use(BootstrapVue);

new Vue({
  render: (h) => h(App),
}).$mount('#app');
