import Vue from 'vue';
import dayjs from 'dayjs';
import { ILLEGAL_DATETIME } from '@/utils';

const format = (date, pattern = 'YYYY-MM-DD HH:mm:ss') =>
  date && !ILLEGAL_DATETIME.includes(date) ? dayjs(date).format(pattern) : '-';

Vue.filter('formatDateTime', (date) => format(date));
Vue.filter('formatDate', (date) => format(date, 'YYYY-MM-DD'));
Vue.filter('formatTime', (date) => format(date, 'HH:mm:ss'));
