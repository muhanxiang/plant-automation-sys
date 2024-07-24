import qs from 'qs';
import axios from 'axios';
import { baseUrl } from '@/config';
import { Message } from 'element-ui';

const request = axios.create({
  baseURL: baseUrl,
  timeout: window.REQUEST_TIMEOUT || 30000,
  withCredentials: false,
  paramsSerializer: (params) => qs.stringify(params),
});

request.interceptors.request.use(
  (config) => {
    // console.log(config);
    // const token = store.getters.token;
    // if (token) {
    //   config.headers.Authorization = `Bearer ${token}`;
    //   config.headers['x-token'] = token;
    //   config.headers['x-user-id'] = store.getters.userData.ID;
    // }
    // if (store.getters.encrypt) {
    //   const encryptData = aesEncrypt(JSON.stringify(config.data));
    //   console.log(encryptData);
    //   config.data = encryptData;
    // }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

request.interceptors.response.use(
  (response) => {
    // console.log(response);
    const { data } = response;
    if (!data.success && data.detail) {
      Message({
        type: 'error',
        message: data.detail,
        customClass: 'sy-message',
      });
    }
    // if (store.getters.encrypt) {
    //   const decryptData = aesDecrypt(data);
    //   // const decryptData = SM4.decrypt(data, encryptKey, {
    //   //   mode: SM4.constants.ECB,
    //   //   inputEncoding: 'base64',
    //   //   outputEncoding: 'utf8',
    //   // });
    //   console.log(JSON.parse(decryptData));
    //   return Promise.resolve(JSON.parse(decryptData));
    // } else {
    //   return Promise.resolve(data);
    // }
    return Promise.resolve(data);
  },
  (error) => {
    // Message({
    //   type: 'error',
    //   message: error.message,
    //   customClass: 'sy-message',
    // });
    return Promise.reject(error);
  }
);

export default request;
