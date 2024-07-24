import { typeOf } from '@/utils';

export default {
  getLocal: (key) => {
    try {
      const value = window.localStorage.getItem(key);
      return JSON.parse(value);
    } catch (error) {
      console.error(error);
    }
  },
  setLocal: (key, val) => {
    try {
      const value = JSON.stringify(val);
      window.localStorage.setItem(key, value);
    } catch (error) {
      console.error(error);
    }
  },
  eachLocal: (func) => {
    if (!func || typeOf(func) !== 'Function') {
      return console.error('传递参数非函数类型！');
    }
    for (let i = 0, len = window.localStorage.length; i < len; i++) {
      const key = window.localStorage.key(i);
      func(this.getLocal(key), key);
    }
  },
  clearLocal: () => {
    window.localStorage.clear();
  },
  removeLocal: (key) => {
    window.localStorage.removeItem(key);
  },

  getSession: (key) => {
    try {
      const value = window.sessionStorage.getItem(key);
      return JSON.parse(value);
    } catch (error) {
      console.error(error);
    }
  },
  setSession: (key, val) => {
    try {
      const value = JSON.stringify(val);
      window.sessionStorage.setItem(key, value);
    } catch (error) {
      console.error(error);
    }
  },
  eachSession: (func) => {
    if (!func || typeOf(func) !== 'Function') {
      return console.error('传递参数非函数类型！');
    }
    for (let i = 0, len = window.sessionStorage.length; i < len; i++) {
      const key = window.sessionStorage.key(i);
      func(this.getSession(key), key);
    }
  },
  clearSession: () => {
    window.sessionStorage.clear();
  },
  removeSession: (key) => {
    window.sessionStorage.removeItem(key);
  },
};
