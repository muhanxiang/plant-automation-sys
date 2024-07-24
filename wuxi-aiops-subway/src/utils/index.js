import dayjs from 'dayjs';
import crypto from 'crypto-js';
import { throttle } from 'lodash';
import { assetUrl } from '@/config';
import { SM2, SM3, SM4 } from 'gm-crypto';

const AES_IV = 'Skesj(eE%32sLOap';
const AES_KEY = 'JeF8U9wHFOMfs2Y8';
const SM4_KEY = 'KfdeIdsfPsafWfsa';
const SM4_IV = '0123456789abcdeffedcba9876543210';

// 非法日期时间
export const ILLEGAL_DATETIME = [
  '',
  '0000-00-00 00:00:00',
  '0000-00-00T00:00:00',
  '0001-01-01 00:00:00',
  '1901-01-01 00:00:00',
  '2006-01-02 15:04:05',
];

const { publicKey, privateKey } = SM2.generateKeyPair();
// SM2加密
export function sm2Encrypt(data, options = {}) {
  const encryptData = SM2.encrypt(data, publicKey, {
    mode: SM2.constants.C1C3C2, // C1C3C2 | C1C2C3
    inputEncoding: 'utf8',
    outputEncoding: 'base64',
    pc: false,
    ...options,
  });
  return encryptData;
}
// SM2解密
export function sm2Decrypt(data, options = {}) {
  const decryptData = SM2.decrypt(data, privateKey, {
    mode: SM2.constants.C1C3C2, // C1C3C2 | C1C2C3
    inputEncoding: 'base64',
    outputEncoding: 'utf8',
    pc: false,
    ...options,
  });
  return decryptData;
}
// SM3加密
export function sm3Digest(data, inputEncoding = 'utf8', outputEncoding = 'hex') {
  const encryptData = SM3.digest(data, inputEncoding, outputEncoding);
  return encryptData;
}
// SM4加密
export function sm4Encrypt(data, options = {}) {
  const sm4Key = strToHex(SM4_KEY);
  const sm4Mode = options.mode || SM4.constants.ECB;
  let encryptData;
  if (sm4Mode === SM4.constants.ECB) {
    encryptData = SM4.encrypt(data, sm4Key, {
      mode: sm4Mode, // ECB | CBC
      inputEncoding: 'utf8',
      outputEncoding: 'hex',
      ...options,
    });
  } else {
    encryptData = SM4.encrypt(data, sm4Key, {
      mode: sm4Mode, // ECB | CBC
      inputEncoding: 'utf8',
      outputEncoding: 'hex',
      iv: SM4_IV,
      ...options,
    });
  }
  return encryptData;
}
// SM4解密
export function sm4Decrypt(data, options = {}) {
  const sm4Key = strToHex(SM4_KEY);
  const sm4Mode = options.mode || SM4.constants.ECB;
  let decryptData;
  if (sm4Mode === SM4.constants.ECB) {
    decryptData = SM4.decrypt(data, sm4Key, {
      mode: sm4Mode,
      inputEncoding: 'hex',
      outputEncoding: 'utf8',
      ...options,
    });
  } else {
    decryptData = SM4.decrypt(data, sm4Key, {
      mode: sm4Mode,
      inputEncoding: 'hex',
      outputEncoding: 'utf8',
      iv: SM4_IV,
      ...options,
    });
  }
  return decryptData;
}
// AES加密
export function aesEncrypt(data) {
  const iv = crypto.enc.Utf8.parse(AES_IV);
  const key = crypto.enc.Utf8.parse(AES_KEY);
  data = crypto.enc.Utf8.parse(data);
  const encryptData = crypto.AES.encrypt(data, key, {
    iv,
    mode: crypto.mode.CBC,
    padding: crypto.pad.Pkcs7,
  });
  return encryptData.ciphertext.toString();
}
// AES解密
export function aesDecrypt(data) {
  const iv = crypto.enc.Utf8.parse(AES_IV);
  const key = crypto.enc.Utf8.parse(AES_KEY);
  data = crypto.enc.Base64.stringify(crypto.enc.Hex.parse(data));
  const decryptData = crypto.AES.decrypt(data, key, {
    iv,
    mode: crypto.mode.CBC,
    padding: crypto.pad.Pkcs7,
  });
  return decryptData.toString(crypto.enc.Utf8);
}
// DOM绑定事件
export function addEvent(element, event, handler, capture = false) {
  if (!element || !event || !handler) return;
  if (document.addEventListener) {
    element.addEventListener(event, handler, capture);
  } else {
    element.attachEvent('on' + event, handler);
  }
}
// DOM移除事件
export function removeEvent(element, event, handler, capture = false) {
  if (!element || !event) return;
  if (document.removeEventListener) {
    element.removeEventListener(event, handler, capture);
  } else {
    element.detachEvent('on' + event, handler);
  }
}
// DOM单次事件
export function onceEvent(element, event, handler) {
  const listener = function () {
    if (handler) {
      handler.apply(this, arguments);
    }
    removeEvent(element, event, listener);
  };
  addEvent(element, event, listener);
}
// 添加元素类名
export function addClass(element, clsName) {
  if (element.classList) {
    element.classList.add(clsName);
  } else if (!hasClass(element, clsName)) {
    element.className += ` ${clsName}`;
  }
}
// 判断元素类名
export function hasClass(element, clsName) {
  if (element.classList) {
    return element.classList.contains(clsName);
  } else {
    const regex = new RegExp(`(\\s|^)${clsName}(\\s|$)`);
    return !!element.className.match(regex);
  }
}
// 删除元素类名
export function removeClass(element, clsName) {
  if (element.classList) {
    element.classList.remove(clsName);
  } else if (hasClass(element, clsName)) {
    const regex = new RegExp(`(\\s|^)${clsName}(\\s|$)`);
    element.className = element.className.replace(regex, ' ');
  }
}

/**
 * 数组转对象 {key,value}
 * @param {*} arr
 * @param {*} key 对象键属性
 * @param {*} val 对象值属性
 * @returns
 */
export function arrToMap(arr, key = 'key', val = 'value') {
  return arr.reduce((acc, next) => ((acc[next[key]] = next[val]), acc), {});
}

export function bufferToHex(buffer) {
  return [...new Uint8Array(buffer)].map((num) => num.toString(16)).join('');
}

export function checkTimeout(timeout, callback) {
  let prevTime = new Date().getTime();
  let currentTime = new Date().getTime();
  const onMouseover = throttle(
    () => {
      // console.log(e);
      prevTime = new Date().getTime();
    },
    5000,
    { trailing: false }
  );
  const interval = setInterval(() => {
    currentTime = new Date().getTime();
    if (currentTime - prevTime >= timeout) {
      callback && callback();
      clearInterval(interval);
      document.removeEventListener('mouseover', onMouseover);
    }
  }, 1000);
  document.addEventListener('mouseover', onMouseover);
}

export function downloadFile(url, name) {
  const link = document.createElement('a');
  link.href = encodeURI(url);
  link.download = name;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

export function downloadFileWithBlob(url, name) {
  const xhr = new XMLHttpRequest();
  xhr.open('GET', url);
  xhr.responseType = 'blob';
  xhr.send();
  xhr.onload = () => {
    if (xhr.status === 200) {
      if (navigator.msSaveOrOpenBlob) {
        navigator.msSaveOrOpenBlob(url, name);
      } else {
        const objUrl = URL.createObjectURL(xhr.response);
        downloadFile(objUrl, name);
        URL.revokeObjectURL(objUrl);
      }
    }
  };
}

export function downloadFileWithData(url, name) {
  const xhr = new XMLHttpRequest();
  xhr.open('GET', url);
  xhr.responseType = 'blob';
  xhr.send();
  xhr.onload = () => {
    if (xhr.status === 200) {
      const fileReader = new FileReader();
      fileReader.readAsDataURL(xhr.response);
      fileReader.onload = (e) => {
        downloadFile(e.target.result, name);
      };
    }
  };
}
/**
 * 提取日期字符串
 * @param {*} datetime
 * @returns
 */
export function extractDate(datetime) {
  const res = datetime.match(/^\d{4}-\d{2}-\d{2}/);
  return res ? res[0] : dayjs(datetime).format('YYYY-MM-DD');
}
/**
 * 提取时间字符串
 * @param {*} datetime
 * @returns
 */
export function extractTime(datetime) {
  const res = datetime.match(/\d{2}:\d{2}:\d{2}/);
  return res ? res[0] : dayjs(datetime).format('HH:mm:ss');
}
/**
 * 日期时间格式化
 * @param {*} datetime
 * @param {*} format
 * @returns
 */
export function formatDateTime(datetime, format = 'YYYY-MM-DD HH:mm:ss') {
  return datetime && !ILLEGAL_DATETIME.includes(datetime) ? dayjs(datetime).format(format) : '-';
}
/**
 * 返回毫秒持续时长
 * eg: 0时0分0秒
 * @param {*} msec
 * @returns
 */
export function formatDuration(msec = 0) {
  const units = [
    { unitName: '天', unit: 86400000 },
    { unitName: '小时', unit: 3600000 },
    { unitName: '分钟', unit: 60000 },
    { unitName: '秒', unit: 1000 },
    { unitName: '毫秒', unit: 1 },
  ];
  let str = '';
  let calced = 0;
  units.forEach((item) => {
    const residue = msec - calced;
    const unit = Math.floor(residue / item.unit);
    if (unit > 0) {
      str = `${str}${unit}${item.unitName}`;
      calced += item.unit * unit;
    }
  });
  return str || '-';
}

export function fixChromePassive() {
  if (typeof EventTarget !== 'undefined') {
    const addEventListener = EventTarget.prototype.addEventListener;
    EventTarget.prototype.addEventListener = function (type, fn, capture) {
      if (typeof capture !== 'boolean') {
        capture = capture || {};
        capture.passive = false;
      }
      addEventListener(type, fn, capture);
    };
  }
}
// 数字转千分位
export function toThousands(num) {
  return num ? num.toString().replace(/(\d)(?=(\d{3})+$)/g, '$1,') : '-';
}
/**
 * 拼接FIR文件路径
 * @param {*} file
 * @returns
 */
export function getFirFullPath(file, dir = 'patrol') {
  return `${assetUrl}/pictures/${dir}/${file}`;
}
/**
 * 拼接图片文件路径
 * @param {*} file
 * @returns
 */
export function getImgFullPath(file, dir = 'patrol') {
  return `${assetUrl}/pictures/${dir}/${file}`;
}
/**
 * 拼接报表文件路径
 * @param {*} file
 * @returns
 */
export function getReportFullPath(file) {
  return `${assetUrl}/report/${file}`;
}

export function getLocalIP(callback) {
  // RTCPeerConnection是WebRTC用于构建点对点之间稳定、高效的流传输的组件。兼容火狐、谷歌等
  let myPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;
  // 创建点对点连接的RTCPeerConnection的实例
  // webRTC使用了ICE协议框架，包括STUN 和 TURN两个协议。我这里连接的是STUN协议服务器。STUN Server的作用是接受客户端的请求，并且把客户端的公网IP、Port封装到ICECandidate中。
  let pc = new myPeerConnection({ iceServers: [{ url: 'stun:stun.l.google.com:19302' }] });
  let noop = function () {};
  let localIPs = {}; // 记录有没有被调用到callback这个listener上
  let ipRegex = /([0-9]{1,3}(\.[0-9]{1,3}){3}|[a-f0-9]{1,4}(:[a-f0-9]{1,4}){7})/g;
  function ipIterate(ip) {
    if (!localIPs[ip]) callback(ip);
    localIPs[ip] = true;
  }
  pc.createDataChannel(''); // 创建数据信道
  pc.createOffer().then(function (sdp) {
    sdp.sdp.split('\n').forEach(function (line) {
      if (line.indexOf('candidate') < 0) return;
      line.match(ipRegex).forEach(ipIterate);
    });
    pc.setLocalDescription(sdp, noop, noop);
  });
  pc.onicecandidate = function (ice) {
    if (!ice || !ice.candidate || !ice.candidate.candidate || !ice.candidate.candidate.match(ipRegex)) return;
    ice.candidate.candidate.match(ipRegex).forEach(ipIterate);
  };
}

export function hexToStr(hex) {
  let arr = [];
  for (let i = 0, len = hex.length; i < len; i += 2) {
    arr.push(String.fromCharCode(parseInt(hex.substr(i, 2), 16)));
  }
  return arr.join('');
}
/**
 * /**
 * JSON解析
 * @param {String} json
 * @param {Boolean} jsonArray 是否JSON 数组
 * @returns Boolean , JSON , JSONArray
 */
export function jsonParse(json, jsonArray = false) {
  try {
    let jsonObj = JSON.parse(json);
    if (jsonArray && !Array.isArray(jsonObj)) return false;
    return jsonObj;
  } catch (e) {
    return false;
  }
}

/**
 * 判断是否可以JSON化
 */
export function isJSON(params) {
  try {
    return JSON.parse(params);
  } catch (e) {
    return false;
  }
}

export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path);
}

export function isEmptyObject(obj) {
  return obj && Object.keys(obj).length === 0;
}

/**
 * 判断日期是否为默认日期
 */
export function isValidDate(date) {
  return !ILLEGAL_DATETIME.includes(date);
}

export function randomInteger(num) {
  return Math.round(Math.random() * num);
}

export function reverseStr(str) {
  return str.split('').reverse().join('');
}

export function strToHex(str) {
  return [...str].map((char) => char.charCodeAt().toString(16)).join('');
}

export function strToUint16(str) {
  const arr = new Uint16Array(new ArrayBuffer(str.length * 2));
  for (let i = 0, len = str.length; i < len; i++) {
    arr[i] = str.charCodeAt(i).toString(16);
  }
  return arr;
}

export function sprintf(str, ...args) {
  for (let i = 0; i < args.length; i++) {
    str = str.replace(/%d/, args[i]);
  }
  return str;
}

export function typeOf(target) {
  return Object.prototype.toString.call(target).slice(8, -1);
}

// 数字转中文
export const toChinesNum = (num) => {
  const changeNum = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九'];
  const unit = ['', '十', '百', '千', '万'];
  num = parseInt(num);
  let getWan = (temp) => {
    let strArr = temp.toString().split('').reverse();
    let newNum = '';
    for (var i = 0; i < strArr.length; i++) {
      newNum =
        (i == 0 && strArr[i] == 0
          ? ''
          : i > 0 && strArr[i] == 0 && strArr[i - 1] == 0
          ? ''
          : changeNum[strArr[i]] + (strArr[i] == 0 ? unit[0] : unit[i])) + newNum;
    }
    return newNum;
  };
  let overWan = Math.floor(num / 10000);
  let noWan = num % 10000;
  if (noWan.toString().length < 4) {
    noWan = '0' + noWan;
  }
  return overWan ? getWan(overWan) + '万' + getWan(noWan) : getWan(num);
};

export function underline(str) {
  return str.replace(/\B([A-Z])/g, '_$1').toLowerCase();
}
