import Vue from 'vue';
import BigNumber from 'bignumber.js';

Vue.prototype.$bignum = BigNumber.config({
  DECIMAL_PLACES: 2, // 小数位树
  ROUNDING_MODE: 4, // 四舍五入
});
