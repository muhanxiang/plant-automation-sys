import axios from './axios';
import { BASE_URL } from '@/config';

// JSON文件
function getJsonApi(file, params = {}) {
  if (params.local) {
    return axios.get(`/${file}`, { baseURL: BASE_URL });
  } else {
    return axios.get(`/${file}`, { params });
  }
}
// 基础接口
function getBaseApi(table, params = {}) {
  return axios.get(`/common/${table}/`, { params });
}
function postBaseApi(action, data = {}, config = {}) {
  return axios.post(`/common/query/${action}/`, data, config);
}
// 任务接口
function getTaskApi(id, params = {}) {
  return axios.get(`/task/${id}/`, { params });
}
function postTaskApi(action, data = {}, config = {}) {
  return axios.post(`/task/${action}/`, data, config);
}
// 用户接口
function getUserApi(action, params) {
  return axios.get(`/user/${action}`, { params });
}
function postUserApi(action, data = {}, config = {}) {
  return axios.post(`/user/${action}`, data, config);
}
// 视频接口
function getVideoApi(action, params = {}) {
  return axios.get(`/video/api/v2/${action}`, { params });
}
function getMediaApi(action, params = {}) {
  return axios.get(`/media/${action}`, { params });
}
function postMediaApi(action, data = {}, config = {}) {
  return axios.post(`/media/${action}/`, data, config);
}
// 图片接口
function postImageApi(action, data = {}, config = {}) {
  return axios.post(`/image/${action}/`, data, config);
}
// 巡检接口
function getPatrolApi(id, params = {}) {
  return axios.get(`/patrol/${id}`, { params });
}
function postPatrolApi(action, data = {}, config = {}) {
  return axios.post(`/patrol/${action}`, data, config);
}
// 历史数据接口
function getHistoryApi(action, params = {}) {
  return axios.get(`/history/${action}`, { params });
}
function postHistoryApi(action, data = {}, config = {}) {
  return axios.post(`/history/${action}`, data, config);
}
// 联动任务接口
function getLinkageApi(action, params = {}) {
  return axios.get(`/linkageaction/${action}/`, { params });
}
function postLinkageApi(action, data = {}, config = {}) {
  return axios.post(`/linkageaction/${action}/`, data, config);
}
// 实时数据接口
function getMeasureApi(action, params = {}) {
  return axios.get(`/measure/${action}`, { params });
}
// 机械设备接口
function getMechanicalApi(action, params = {}) {
  return axios.get(`/mechanical/${action}/`, { params });
}
// 传感器接口
function getSensorApi(action, params = {}) {
  return axios.get(`/sensor/${action}/`, { params });
}
function postSensorApi(action, data = {}, config = {}) {
  return axios.post(`/sensor/${action}/`, data, config);
}

export {
  getJsonApi,
  getBaseApi,
  postBaseApi,
  getTaskApi,
  postTaskApi,
  getUserApi,
  postUserApi,
  getVideoApi,
  getMediaApi,
  postMediaApi,
  postImageApi,
  getPatrolApi,
  postPatrolApi,
  getHistoryApi,
  postHistoryApi,
  getLinkageApi,
  postLinkageApi,
  getMeasureApi,
  getMechanicalApi,
  getSensorApi,
  postSensorApi,
};
