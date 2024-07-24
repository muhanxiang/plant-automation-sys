import Vue from 'vue';
import dayjs from 'dayjs';

require('dayjs/locale/zh-cn');
const weekday = require('dayjs/plugin/weekday');
const isBetween = require('dayjs/plugin/isBetween');
const isSameOrAfter = require('dayjs/plugin/isSameOrAfter');
const isSameOrBefore = require('dayjs/plugin/isSameOrBefore');
const customParseFormat = require('dayjs/plugin/customParseFormat');
const updateLocale = require('dayjs/plugin/updateLocale');
const duration = require('dayjs/plugin/duration');

dayjs.locale('zh-cn');
dayjs.extend(weekday);
dayjs.extend(isBetween);
dayjs.extend(isSameOrAfter);
dayjs.extend(isSameOrBefore);
dayjs.extend(customParseFormat);
dayjs.extend(updateLocale);
dayjs.extend(duration);

dayjs.updateLocale('zh-cn', {
  weekdays: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
  weekStart: 1,
});

Vue.prototype.$dayjs = dayjs;
