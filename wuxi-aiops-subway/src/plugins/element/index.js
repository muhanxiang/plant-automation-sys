import Vue from 'vue';
import { typeOf } from '@/utils';
import ElementUI, { Loading, Message, MessageBox, Notification } from 'element-ui';

Vue.use(ElementUI);

import SyEmpty from '@/components/ElementUI/Empty';
Vue.component('sy-empty', SyEmpty);

import SyImage from '@/components/ElementUI/Image';
Vue.component('sy-image', SyImage);

import SyTree from '@/components/ElementUI/Tree/tree';
Vue.component('sy-tree', SyTree);

import SyProgress from '@/components/ElementUI/Progress';
Vue.component('sy-progress', SyProgress);

import SyCalendar from '@/components/ElementUI/Calendar/main';
Vue.component('sy-calendar', SyCalendar);

Vue.prototype.$loading = (target, options = {}) =>
  Loading.service({
    target: target || document.body,
    // text: '正在加载...',
    // spinner: 'el-icon-loading',
    background: 'rgba(0,0,0,0.5)',
    customClass: 'sy-loading',
    ...options,
  });

export const message = (options = {}) => {
  const { customClass } = options;
  return Message({
    ...options,
    center: true,
    customClass: `sy-message ${customClass || ''}`,
  });
};
['success', 'warning', 'info', 'error'].forEach((type) => {
  message[type] = (options) => {
    if (typeOf(options) === 'String') {
      return message({ type, message: options });
    } else {
      return message({ ...options, type });
    }
  };
});
Vue.prototype.$message = message;

Vue.prototype.$alert = (message, title, options) => {
  if (typeof title === 'object') {
    options = title;
    title = '';
  } else if (title === undefined) {
    title = '';
  }
  const { customClass, cancelButtonClass, confirmButtonClass } = options;
  return MessageBox.alert(message, title, {
    ...options,
    customClass: `sy-alert ${customClass || ''}`,
    cancelButtonClass: `sy-btn-regular ${cancelButtonClass || 'el-button--warning'}`,
    confirmButtonClass: `sy-btn-regular ${confirmButtonClass || ''}`,
  });
};

Vue.prototype.$prompt = (message, title, options) => {
  if (typeof title === 'object') {
    options = title;
    title = '';
  } else if (title === undefined) {
    title = '';
  }
  const { customClass, cancelButtonClass, confirmButtonClass } = options;
  return MessageBox.prompt(message, title, {
    ...options,
    customClass: `sy-prompt ${customClass || ''}`,
    cancelButtonClass: `sy-btn-regular ${cancelButtonClass || 'el-button--warning'}`,
    confirmButtonClass: `sy-btn-regular ${confirmButtonClass || ''}`,
  });
};

Vue.prototype.$confirm = (message, title, options) => {
  if (typeof title === 'object') {
    options = title;
    title = '';
  } else if (title === undefined) {
    title = '';
  }
  const { customClass, cancelButtonClass, confirmButtonClass } = options;
  return MessageBox.confirm(message, title, {
    ...options,
    customClass: `sy-confirm ${customClass || ''}`,
    cancelButtonClass: `sy-btn-regular ${cancelButtonClass || 'el-button--warning'}`,
    confirmButtonClass: `sy-btn-regular ${confirmButtonClass || ''}`,
  });
};

Vue.prototype.$notify = (options) => {
  const { customClass } = options;
  return Notification({
    ...options,
    position: 'bottom-right',
    customClass: `sy-notice ${customClass || ''}`,
  });
};
