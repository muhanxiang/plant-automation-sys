// 推送类型
export const NoticeActionMap = {
  'patrol.alarm': '任务巡视告警推送',
  'patrol.task.item': '任务巡视结果推送',
  'patrol.task.index': '任务巡视状态推送',
  'patrol.single.result': '单点识别结果推送',
  'status.platform.channel': '平台通道状态推送',
  'database.common.change': '数据库更新推送',
};
// 告警类型
export const AlarmTypeMap = {
  0: '巡视告警',
  1: '信号告警',
  2: '设备告警',
  3: '系统告警',
};
// 告警等级
export const AlarmLevelMap = {
  1: 'success',
  2: 'info',
  3: 'warning',
  4: 'error',
};
