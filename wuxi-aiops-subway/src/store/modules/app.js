import * as API from '@/api';
import Stomp from '@/utils/stomp';
import storage from '@/utils/storage';
import { BASE_URL } from '@/config';
import { asyncRoutes } from '@/router';
import variables from '@/styles/variables.scss';

const APP_SETUP = 'APP_SETUP';
const APP_SYSTEM = 'APP_SYSTEM';
const APP_STATION = 'APP_STATION';
const APP_COMPANY = 'APP_COMPANY';

const state = {
  setup: false, // 应用设置
  logo: 'sgcc.svg',
  mode: 'station',
  title: '智慧变电站在线监测系统',
  footer: '',
  system: { name: '智慧变电站在线监测系统', date: '2021-09-01' },
  company: storage.getSession(APP_COMPANY),
  station: storage.getSession(APP_STATION),

  stomp: null,
  theme: variables.theme,
  navMenu: null,
  activeMenu: null,
};

const generateRoutes = (menus, routes) => {
  let menuList = [];
  menus.forEach((menu) => {
    const route = routes.find((route) => route.name === menu.name);
    if (route) {
      if (route.children && menu.children) {
        route.children = generateRoutes(menu.children, route.children);
      }
      menuList.push(route);
    }
  });
  return menuList;
};

const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    if (key in state) {
      state[key] = value;
    }
  },
  SETUP_APP: (state, options) => {
    const { logo, mode, title, footer, system, company, station, menu_list } = options;
    // if (!state.station) state.station = station;
    state.mode = mode || 'station';
    state.logo = `${BASE_URL}svg/${logo || 'sgcc.svg'}`;
    state.title = document.title = title ?? '';
    state.footer = footer ?? '';
    state.system = system ?? {};
    state.station = station;
    state.company = company;
    storage.setSession(APP_STATION, station);
    storage.setSession(APP_COMPANY, company);

    if (menu_list && menu_list.length > 0) {
      state.navMenu = generateRoutes(menu_list, asyncRoutes);
    } else {
      state.navMenu = asyncRoutes;
    }
    // 保存应用设置状态
    state.setup = true;
    storage.setSession(APP_SETUP, options);
  },
  SET_STOMP: (state, options) => {
    state.stomp = new Stomp(options);
  },
  SET_SYSTEM: (state, system) => {
    state.system = system;
    storage.setSession(APP_SYSTEM, system);
  },
  SET_STATION: (state, station) => {
    state.station = station;
    storage.setSession(APP_STATION, station);
  },
  SET_NAVMENU: (state, routes) => {
    state.navMenu = routes;
  },
  SET_ACTIVEMENU: (state, route) => {
    state.activeMenu = route;
  },
};

const actions = {
  // 应用设置
  setupApp({ commit }) {
    return new Promise((resolve, reject) => {
      const setupData = storage.getSession(APP_SETUP);
      // console.log(setupData);
      if (setupData) {
        commit('SETUP_APP', setupData);
        resolve();
      } else {
        API.getJsonApi('setup.json', { local: true })
          .then((res) => {
            if (!res) return;
            commit('SETUP_APP', res);
            resolve();
          })
          .catch((err) => {
            reject(err);
          });
      }
    });
  },
  clearApp({ state }) {
    storage.clearSession();

    state.setup = false;
    state.station = null;
    state.stomp && state.stomp.destroy();
  },
  // 修改设置
  changeSetting({ commit }, data) {
    commit('CHANGE_SETTING', data);
  },
  // 创建STOMP客户端
  initStompClient({ commit }, options) {
    commit('SET_STOMP', options);
  },
  // 销毁STOMP客户端
  destroyStompClient({ state }) {
    state.stomp && state.stomp.destroy();
  },
  // 设置顶部导航次啊单
  setNavMenu({ commit }, routes) {
    commit('SET_NAVMENU', routes);
  },
  // 设置当前激活菜单
  setActiveMenu({ commit }, route) {
    commit('SET_ACTIVEMENU', route);
  },
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};
