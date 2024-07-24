// import 'core-js/stable'
// import 'regenerator-runtime/runtime'

import Vue from 'vue';
import App from './App.vue';
import store from './store';
import router from './router';
import './registerServiceWorker';

import ElementUI from 'element-ui' //element-ui的全部组件
import 'element-ui/lib/theme-chalk/index.css'//element-ui的css
Vue.use(ElementUI) //使用elementUI

//import echarts from 'echarts'
import * as echarts from 'echarts'
Vue.prototype.$echarts = echarts

import './icons';
import './filters';
import './plugins/dayjs';
import './plugins/element';
import './plugins/precision';
import './styles/index.scss';

import * as api from './api';
Vue.prototype.$api = api;
Vue.prototype.$bus = new Vue();

import Animate from 'animate.css';
Vue.use(Animate);
/*
import SyChart from '@/components/Chart/Echart5';
Vue.component('sy-chart', SyChart);
*/
import SyPlayer from '@/components/Player/JessiPlayer';
Vue.component('sy-player', SyPlayer);

import { isDev } from './config';
Vue.config.performance = isDev;
Vue.config.productionTip = isDev;
Vue.config.errorHandler = (err, vm, info) => {
  console.error('VueErrorHandler: ', err, info);
};

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount('#app');
