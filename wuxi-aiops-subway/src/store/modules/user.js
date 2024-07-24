import * as API from '@/api';
import storage from '@/utils/storage';
import { SM3 } from 'gm-crypto';
import { sm4Encrypt } from '@/utils';

const USER_ID = 'USER_ID';
const USER_AUTH = 'USER_AUTH';
const USER_TOKEN = 'USER_TOKEN';
const USER_NAME = 'USER_NAME';
const USERNAME = 'USERNAME';
const PASSWORD = 'PASSWORD';

const state = {
  token: storage.getSession(USER_TOKEN),
  group: storage.getSession(USER_AUTH),
  uname: storage.getSession(USER_NAME),
  userId: storage.getSession(USER_ID),
  username: storage.getSession(USERNAME),
  password: storage.getSession(PASSWORD),
};

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token;
    storage.setSession(USER_TOKEN, token);
  },
  SET_UNAME: (state, uname) => {
    state.uname = uname;
    storage.setSession(USER_NAME, uname);
  },
  SET_USERID: (state, userId) => {
    state.userId = userId;
    storage.setSession(USER_ID, userId);
  },
  SET_USERAUTH: (state, group) => {
    state.group = group;
    storage.setSession(USER_AUTH, group);
  },
  SET_USERNAME: (state, username) => {
    storage.setSession(USERNAME, username);
    state.username = username;
  },
  SET_PASSWORD: (state, password) => {
    storage.setSession(PASSWORD, password);
    state.password = password;
  },
};

const actions = {
  // 用户登录
  login({ commit }, data) {
    const { username, password, captcha, captchaId } = data;
    const sm4Pwd = sm4Encrypt(password);
    const check = SM3.digest(`${username}${sm4Pwd}`, 'utf8', 'hex');
    return new Promise((resolve, reject) => {
      // Promise.all([API.getBaseApi('station')])
      API.postUserApi('login', {
        username,
        password: sm4Pwd,
        check,
        captcha,
        captcha_id: captchaId,
      })
        .then((res) => {
          const { data, detail, success } = res;
          if (success) {
            commit('SET_TOKEN', data.token);
            commit('SET_UNAME', data.user.name);
            commit('SET_USERID', data.user.userid);
            commit('SET_USERAUTH', data.user.group);
            commit('SET_USERNAME', data.user.username);
            commit('SET_PASSWORD', sm4Pwd);

            resolve();
          } else {
            reject(detail);
          }
        })
        .catch((err) => {
          reject(err);
        });
    });
  },
  // 用户登出
  logout({ state, commit, dispatch }) {
    return new Promise((resolve, reject) => {
      const params = {
        userId: state.userId,
      };
      API.postUserApi('logout', params)
        .then(() => {
          commit('SET_TOKEN', '');
          commit('SET_USERID', '');
          commit('SET_USERAUTH', '');
          commit('SET_USERNAME', '');
          commit('SET_PASSWORD', '');
          dispatch('app/clearApp', null, { root: true });

          resolve();
        })
        .catch((err) => {
          reject(err);
        });
    });
  },
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};
