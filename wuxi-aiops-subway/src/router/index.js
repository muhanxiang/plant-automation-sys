import Vue from 'vue';
import VueRouter from 'vue-router';
import Layout from '@/layout';
import store from '@/store';
import StationManagement from '@/views/StationManagement.vue'
import Test1 from '@/views/Test1.vue'
import HistoryQuery from '@/views/HistoryQuery.vue'
import QHDJH from '@/views/qhdjh.vue'
import AlertMessage from '@/views/AlertMessage.vue'
import VersionControl from '@/views/VersionManagement.vue'
// import VersionManagement from '@/views/VersionManagement.vue'
import d5000check from '@/views/d5000check.vue'
import pointManagement from '@/views/PointManagement.vue'
import checkQuery from '@/views/CheckQuery.vue'
import alertManagement from '@/views/alertManagement.vue'
import Home from '@/views/Home.vue'
// import UserManage from '@/views/UserManage.vue'

// 添加路由重复跳转错误处理
const rawPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return rawPush.call(this, location).catch((err) => err);
};
// 添加路由重复替换错误处理
const rawReplace = VueRouter.prototype.replace;
VueRouter.prototype.replace = function replace(location) {
  return rawReplace.call(this, location).catch((err) => err);
};

Vue.use(VueRouter);

export const baseRoutes = [
  {
    path: '/login',
    name: 'UserLogin',
    component: () => import('@/views/user/login'),
  },
  {
    path: '/',
    redirect: '/login',
  },
  {
    path: '/system',
    component: Layout,
    meta: { title: '系统配置' },
    children: [
      {
        path: 'user-manage',
        name: 'UserManage',
        component: () => import('@/views/system/user-manage'),
        meta: { title: '用户管理', subtitle: 'User Manage', hidden: true },
      },
    ],
  },
  {
    path: '/Test1',
    name: 'Test1',
    component: Test1,
    hidden : true
},
{
    path: '/StationManagement',
    name: 'StationManagement',
    component: StationManagement,
    hidden : true
},
{
  path: '/Home',
  name: 'Home',
  component: Home,
  hidden : true
},
// {
//   path: '/VersionManagement',
//   name: 'VersionManagement',
//   component: VersionManagement,
//   hidden : true
// },
{
  path: '/alertManagement',
  name: 'alertManagement',
  component: alertManagement,
  hidden : true
},
{
  path: '/HistoryQuery',
  name: 'HistoryQuery',
  component: HistoryQuery,
  hidden : true
},
{
  path: '/checkQuery',
  name: 'checkQuery',
  component: checkQuery,
  hidden : true
},
{
  path: '/d5000check',
  name: 'd5000check',
  component: d5000check,
  hidden : true
},
{
  path: '/pointManagement',
  name: 'pointManagement',
  component: pointManagement,
  hidden : true
},
{
  path: '/VersionControl',
  name: 'VersionControl',
  component: VersionControl,
  hidden : true
},
{
  path: '/AlertMessage',
  name: 'AlertMessage',
  component: AlertMessage,
  hidden : true
},
{
    path: '/QHDJH',
    name: 'QHDJH',
    component: QHDJH,
    hidden : true
}
];

export const alarmRoutes = [
  {
    name: 'StatusAlarm',
    path: 'status-alarm',
    component: () => import('@/views/events/alarm/status-alarm'),
    meta: { title: '状态告警' },
  },
  {
    name: 'PatrolAlarm',
    path: 'patrol-alarm',
    component: () => import('@/views/events/alarm/patrol-alarm'),
    meta: { title: '巡视告警' },
  },
  {
    name: 'AuditRecord',
    path: 'audit-record',
    component: () => import('@/views/events/alarm/audit-record'),
    meta: { title: '审核记录' },
  },
  {
    name: 'DefectRecord',
    path: 'defect-record',
    component: () => import('@/views/events/alarm/defect-record'),
    meta: { title: '缺陷记录', hidden: true },
  },
];

export const patrolRoutes = [
  {
    name: 'PatrolTask',
    path: 'patrol-task',
    component: () => import('@/views/events/patrol/patrol-task'),
    meta: { title: '巡视任务' },
  },
  {
    name: 'PatrolDetails',
    path: 'patrol-details',
    component: () => import('@/views/events/patrol/patrol-details'),
    meta: { title: '巡视详情', hidden: true },
  },
  {
    name: 'PatrolPoint',
    path: 'patrol-point',
    component: () => import('@/views/events/patrol/patrol-point'),
    meta: { title: '巡视点位' },
  },
];

export const deviceRoutes = [
  {
    name: 'DiagnosisStation',
    path: 'diagnosis-station',
    component: () => import('@/views/diagnosis/overview/station'),
    meta: { title: '站点', hidden: true },
  },
  {
    name: 'DiagnosisArea',
    path: 'diagnosis-area',
    component: () => import('@/views/diagnosis/overview/area'),
    meta: { title: '区域', hidden: true },
  },
  {
    name: 'DiagnosisBay',
    path: 'diagnosis-bay',
    component: () => import('@/views/diagnosis/overview/bay'),
    meta: { title: '间隔', hidden: true },
  },
];

export const asyncRoutes = [
  {
    name: 'Dashboard',
    path: '/dashboard',
    component: Layout,
    redirect: '/dashboard',
    meta: { title: '首页总览', submenu: false },
    children: [
      {
        path: '',
        component: () => import('@/views/dashboard/index'),
      },
    ],
  },
  {
    name: 'Patrol',
    path: '/patrol',
    component: Layout,
    redirect: '/patrol/task-manage',
    meta: { title: '系统监控', submenu: true },
    children: [
      {
        name: 'TaskCalendar',
        path: 'task-calendar',
        component: () => import('@/views/patrol/task-calendar'),
        meta: { title: '任务日历', subtitle: 'Task Calendar' },
      },
      {
        name: 'TaskManage',
        path: 'task-manage',
        component: () => import('@/views/patrol/task-manage'),
        meta: { title: '任务管理', subtitle: 'Task Manage' },
      },
      {
        name: 'TaskMonitor',
        path: 'task-monitor',
        component: () => import('@/views/patrol/task-monitor'),
        meta: { title: '任务监控', subtitle: 'Task Monitor', hidden: true },
      },
      {
        name: 'TaskIdentify',
        path: 'task-identify',
        component: () => import('@/views/patrol/task-identify'),
        meta: { title: '单点监测', subtitle: 'Task Identify' },
      },
    ],
  },
  {
    name: 'Monitor',
    path: '/monitor',
    component: Layout,
    redirect: '/monitor/video-monitor',
    meta: { title: '设备监控', submenu: true },
    children: [
      {
        name: 'VideoMonitor',
        path: 'video-monitor',
        component: () => import('@/views/monitor/video-monitor'),
        meta: { title: '视频监控', subtitle: 'Video Monitor' },
      },
      {
        name: 'VideoPlayback',
        path: 'video-playback',
        component: () => import('@/views/monitor/video-playback'),
        meta: { title: '录像回放', subtitle: 'Video Playback' },
      },
      {
        name: 'GraphMonitor',
        path: 'graph-monitor',
        component: () => import('@/views/monitor/graph-monitor'),
        meta: { title: '状态监测', subtitle: 'Graphic Monitor' },
      },
    ],
  },
  {
    name: 'Linkage',
    path: '/linkage',
    component: Layout,
    redirect: '/linkage/linkage-manage',
    meta: { title: '机器人巡视', submenu: true },
    children: [
      {
        name: 'LinkageManage',
        path: 'linkage-manage',
        component: () => import('@/views/linkage/linkage-manage'),
        meta: { title: '视频监控', subtitle: 'Linkage Manage' },
      },
      {
        name: 'LinkageRecord',
        path: 'linkage-record',
        component: () => import('@/views/linkage/linkage-record'),
        meta: { title: '录像回放', subtitle: 'Linkage Record' },
      },
      // {
      //   name: 'LinkageTask',
      //   path: 'linkage-task',
      //   component: () => import('@/views/linkage/linkage-task'),
      //   meta: { title: '联动任务', subtitle: 'Linkage Task' },
      // },
    ],
  },
  {
    name: 'Diagnosis',
    path: '/diagnosis',
    component: Layout,
    redirect: '/diagnosis/overview',
    meta: { title: '视频监控', submenu: true },
    children: [
      {
        path: 'overview',
        name: 'DiagnosisOverview',
        component: () => import('@/views/diagnosis/overview/index'),
        meta: { title: '视频监控', subtitle: 'Diagnosis Overview' },

      },
      // {
      //   path: 'diagnosis-record',
      //   name: 'Diagnosis-Record',
      //   component: () => import('@/views/diagnosis/diagnosis-record'),
      //   meta: { title: '录像回放', subtitle: 'Diagnosis Record' },
      // },

    ],
  },
  {
    name: 'Events',
    path: '/events',
    component: Layout,
    redirect: '/events/patrol',
    meta: { title: '历史查询', submenu: true },
    children: [
      {
        name: 'PatrolRecord',
        path: 'patrol',
        redirect: '/events/patrol/patrol-task',
        component: () => import('@/views/events/patrol/index'),
        meta: { title: '巡视记录' },
        children: patrolRoutes,
      },

      {
        path: 'alarm',
        name: 'AlarmRecord',
        redirect: '/events/alarm/status-alarm',
        component: () => import('@/views/events/alarm/index'),
        meta: { title: '告警记录', subtitle: 'Alarm Record' },
        children: alarmRoutes,
      },
    ],
  },
];

const createRouter = () =>
  new VueRouter({
    mode: 'hash',
    base: process.env.BASE_URL,
    scrollBehavior: () => ({ y: 0 }),
    routes: [...baseRoutes, ...asyncRoutes, { path: '*', redirect: '/Home', hidden: true }],
  });

export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher;
}

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  scrollBehavior: () => ({ y: 0 }),
  routes: [...baseRoutes, ...asyncRoutes, { path: '*', redirect: '/Home', hidden: true }],
});


router.beforeEach(async (to, from, next) => {
  // console.log('beforeEach: ', to, from);
  if (!store.getters.setup) {
    await store.dispatch('app/setupApp');
  }

  if (store.getters.token) {
    if (to.matched && to.matched.length > 0) {
      // const matches = to.path.match(/^\/\w+/g);
      const matchRoute = to.matched[0];
      const activeRoute = store.getters.navMenu.find((route) => route.path === matchRoute.path);
      store.dispatch('app/setActiveMenu', activeRoute || matchRoute);
    }
    if (to.path === '/login') {
      next({ path: '/Home', replace: true });
    } else {
      next();
    }
  } else {
    if (to.path !== '/login') {
      next('/login');
    } else {
      next();
    }
  }
}
);

export default router;
