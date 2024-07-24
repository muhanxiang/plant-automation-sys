<template>
  <div class="header-right">
    <el-popover class="header-right__popover" popper-class="sy-popper task-popper" trigger="hover">
      <div class="task-popper-wrapper">
        <el-table class="sy-table task-popper-table" :data="taskList" height="10vh" size="mini">
          <sy-empty slot="empty" description="暂无任务数据"></sy-empty>

          <el-table-column
            v-for="column in taskColumns"
            :key="column.prop"
            v-bind="column"
            align="center"
            show-overflow-tooltip
          >
            <template v-if="column.prop === 'task_progress'" v-slot="{ row }">
              <sy-progress :percentage="row.task_progress" text-inside></sy-progress>
            </template>
            <template v-else-if="column.prop === 'operation'" v-slot="{ row }">
              <el-button type="text" size="mini" :disabled="row.disabled" @click="handleTaskAction('check', row)"
                >查看
              </el-button>
              <el-button
                v-if="row.task_status === taskStatus.DOING"
                type="text"
                size="mini"
                :disabled="row.disabled"
                @click="handleTaskAction('pause', row)"
                >暂停
              </el-button>
              <el-button
                v-if="row.task_status === taskStatus.PAUSE"
                type="text"
                size="mini"
                :disabled="row.disabled"
                @click="handleTaskAction('resume', row)"
                >继续
              </el-button>
              <el-button
                v-if="
                  [taskStatus.UNDO, taskStatus.DOING, taskStatus.PAUSE, taskStatus.WAITING].includes(row.task_status)
                "
                type="text"
                size="mini"
                :disabled="row.disabled"
                @click="handleTaskAction('terminate', row)"
                >终止
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-badge
        class="header-right__badge"
        slot="reference"
        type="primary"
        :max="99"
        :value="taskCount"
        :hidden="taskCount === 0"
      >
        <i class="header-right__icon el-icon-s-claim" />
      </el-badge>
    </el-popover>

    <el-popover class="header-right__popover" popper-class="sy-popper alarm-popper" trigger="hover">
      <div class="alarm-popper-wrapper">
        <el-table class="sy-table task-popper-table" :data="alarmList" height="10vh" size="mini">
          <sy-empty slot="empty" description="暂无告警数据"></sy-empty>

          <el-table-column
            v-for="column in alarmColumns"
            :key="column.prop"
            v-bind="column"
            align="center"
            show-overflow-tooltip
          >
            <template v-if="column.prop === 'operation'" v-slot="{ row }">
              <el-button type="text" size="mini" @click="handleAlarmPush('check', row)">查看</el-button>
              <el-button v-if="row.commpoint" type="text" size="mini" @click="handleAlarmPush('maintain', row)"
                >维护
              </el-button>
              <el-button v-else type="text" size="mini" @click="handleAlarmPush('audit', row)">审核</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-badge
        class="header-right__badge"
        slot="reference"
        type="warning"
        :max="99"
        :value="alarmCount"
        :hidden="alarmCount === 0"
      >
        <i class="header-right__icon el-icon-message-solid" />
      </el-badge>
    </el-popover>

    <el-dropdown class="header-right__dropdown" @command="onDropdownCommand">
      <div>
        <sy-icon name="user" />
        <span class="uname">{{ uname || '未登录' }}</span>
        <i class="el-icon-arrow-down" />
      </div>
      <el-dropdown-menu class="sy-popper" slot="dropdown">
        <el-dropdown-item command="usermanage">用户管理</el-dropdown-item>
        <el-dropdown-item command="changepwd">修改密码</el-dropdown-item>
        <el-dropdown-item command="logout">退出账号</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

    <ModifyPwd :visible.sync="changePwd" @submit="handleChangePwd" />
    <CommPoint :visible.sync="commpointModal" :title="modalTitle" :guid="commpointId"></CommPoint>
    <MonitorInfo :visible.sync="monitorInfo" :title="modalTitle" :data="modalData" />
    <PatrolResult :visible.sync="patrolResult" :title="modalTitle" :data="modalData" />
    <PatrolDetails :visible.sync="patrolDetail" :title="modalTitle" :data="modalData" />
    <LinkageModal :visible.sync="linkageModal" :title="modalTitle" :linkage="linkageTask" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { uniqueId } from 'lodash';
import { arrToMap, formatDateTime } from '@/utils';
import { AlarmLevelMap, NoticeActionMap } from '@/enums/map';
import { PatrolStatus, TaskStatus, MessageDest, MessageAction, CustomPatrolTypes, LinkagePatrolTypes } from '@/enums';

import Option from '@/enums/option';

export default {
  name: 'AppMessage',
  components: {
    ModifyPwd: () => import('@/components/Modal/ModifyPwd'),
    LinkageModal: () => import('@/components/Modal/Linkage'),
    CommPoint: () => import('@/views/monitor/components/CommPoint'),
    MonitorInfo: () => import('@/views/events/components/MonitorInfo'),
    PatrolResult: () => import('@/views/events/components/PatrolResult'),
    PatrolDetails: () => import('@/views/patrol/components/PatrolDetails'),
  },
  computed: {
    ...mapGetters(['stomp', 'uname', 'stationId']),
    taskStatus() {
      return TaskStatus;
    },
    taskCount() {
      return this.taskList.length;
    },
    alarmCount() {
      return this.alarmList.length;
    },
    alarmTypeMap() {
      return arrToMap(Option.AlarmTypes);
    },
    taskColumns() {
      return [
        // { prop: 'station_name', label: '站所名称', minWidth: 200 },
        { prop: 'task_name', label: '任务名称', minWidth: 200 },
        { prop: 'task_progress', label: '任务进度', minWidth: 150 },
        { prop: 'task_start_time', label: '开始时间', minWidth: 150 },
        { prop: 'operation', label: '人工操作', minWidth: 150 },
      ];
    },
    alarmColumns() {
      return [
        { prop: 'alarmType', label: '告警类型', minWidth: 150 },
        { prop: 'alarmContent', label: '告警内容', minWidth: 200 },
        { prop: 'alarmTime', label: '告警时间', minWidth: 150 },
        { prop: 'operation', label: '人工操作', minWidth: 150 },
      ];
    },
  },
  data() {
    return {
      changePwd: false,
      taskList: [],
      alarmList: [],
      alarmNotice: null,
      modalData: {},
      modalTitle: '',
      monitorInfo: false,
      patrolDetail: false,
      patrolResult: false,
      commpointId: '',
      commpointModal: false,
      linkageTask: {},
      linkageModal: false,
    };
  },
  created() {
    this.subStompPush();
    this.getTaskListData();
    this.getAlarmListData();
  },
  methods: {
    subStompPush() {
      this.$store.dispatch('app/initStompClient', {
        onConnect: () => {
          if (this.alarmNotice) {
            this.alarmNotice.close();
            this.alarmNotice = null;
            this.$message({
              type: 'success',
              message: 'WS与服务器连接恢复！',
            });
          }

          let stompPatrol = this.stomp.subscribe({
            destination: MessageDest.PATROL_ALL,
            callback: this.onStompPatrolPush,
          });
          let stompStatus = this.stomp.subscribe({
            destination: MessageDest.STATUS_ALL,
            callback: this.onStompStatusPush,
          });

          this.$once('hook:beforeDestroy', () => {
            if (stompPatrol) {
              stompPatrol.unsubscribe();
              stompPatrol = null;
            }
            if (stompStatus) {
              stompStatus.unsubscribe();
              stompStatus = null;
            }
          });
        },
        onStompError: () => {
          if (!this.alarmNotice) {
            this.alarmNotice = this.$message({
              type: 'error',
              message: 'WS与服务器连接中断！',
              customClass: 'sy-animation-alarm',
              duration: 0,
            });
          }
        },
      });
    },
    handleChangePwd(model) {
      this.$api.postUserApi('update/password', model).then((res) => {
        if (res?.success) {
          this.$message({
            type: 'success',
            message: '密码修改成功！',
          });
          this.changePwd = false;
        } else {
          this.$message({
            type: 'error',
            message: '密码修改失败！',
          });
        }
      });
    },
    handleAlarmPush(action, alarm) {
      switch (action) {
        case 'check':
          if (alarm.commpoint) {
            this.commpointId = alarm.commpoint?.guid;
            this.modalTitle = alarm.commpoint?.name;
            this.commpointModal = true;
          } else {
            this.modalData = alarm;
            this.modalTitle = alarm.patrolpoint_name;
            this.patrolDetail = true;
          }
          break;
        case 'audit':
          this.modalData = alarm;
          this.modalTitle = alarm.patrolpoint_name;
          this.patrolResult = true;
          break;
        case 'maintain':
          this.modalData = alarm;
          this.modalTitle = alarm.commpoint?.name;
          this.monitorInfo = true;
          break;
        case 'clear':
          this.alarmList = [];
          break;
        default:
          break;
      }
    },
    handleTaskAction(action, row) {
      // console.log('handleTaskAction: ', action, row);
      switch (action) {
        case 'check':
          {
            const { task_id, execute_id, patrol_type, task_status } = row;
            if (!task_id && !execute_id) return;

            if (LinkagePatrolTypes.includes(patrol_type)) {
              this.modalTitle = row?.name;
              this.linkageTask = row;
              this.linkageModal = true;
            } else if ([TaskStatus.DOING, TaskStatus.PAUSE, TaskStatus.WAITING].includes(task_status)) {
              this.$router.push({
                path: '/patrol/task-monitor',
                query: { taskId: task_id, executeId: execute_id },
              });
            } else {
              this.$router.push({
                path: '/events/patrol/patrol-details',
                query: { taskId: task_id, executeId: execute_id },
              });
            }
          }
          break;
        case 'pause':
        case 'resume':
        case 'terminate':
          {
            if (!row.execute_id) {
              return this.$message({
                type: 'warning',
                message: '未找到任务执行ID！',
              });
            }
            let message = '';
            switch (action) {
              case 'pause':
                message = '确认暂停任务?';
                break;
              case 'resume':
                message = '确认继续任务?';
                break;
              case 'terminate':
                message = '确认终止任务?';
                break;
              default:
                break;
            }
            this.$confirm(message, {
              type: 'warning',
            }).then(() => {
              this.$api.postPatrolApi(`${action}/${row.execute_id}`).then((res) => {
                if (res?.success) {
                  this.$message({
                    type: 'success',
                    message: '任务操作成功！',
                  });
                } else {
                  this.$message({
                    type: 'error',
                    message: '任务操作失败！',
                  });
                }
              });
            });
          }
          break;
      }
    },
    onDropdownCommand(cmd) {
      switch (cmd) {
        case 'usermanage': {
          this.$router.push('/system/user-manage');
          break;
        }
        case 'changepwd':
          this.changePwd = true;
          break;
        case 'logout':
          this.$confirm('确认退出登录?', {
            type: 'warning',
          }).then(() => {
            this.$store.dispatch('user/logout').then(() => {
              this.$router.replace('/login');
            });
          });
          break;
        default:
          break;
      }
    },
    onStompPatrolPush(msg) {
      this.$bus.$emit('stompPatrolPush', msg.body);
      const { action, data } = JSON.parse(msg.body);
      // console.log('onStompPatrolPush: ', action, data);

      if (!data || !data.length) return;
      switch (action) {
        case MessageAction.PATROL.ALARM:
          data.forEach((item) => {
            if (item.alarm && item.alarm.length > 0) {
              item.alarm.forEach((alarm, index) => {
                const { name, type, level } = alarm;
                // console.log('alarm: ', alarm);
                setTimeout(() => {
                  const alarmType = this.alarmTypeMap[type] || '巡视告警';
                  const alarmLevel = AlarmLevelMap[level] || 'info';
                  const alarmContent = `${item.patrolpoint_name}【${name}】`;
                  this.alarmList.unshift({
                    ...item,
                    alarmType: alarmType,
                    alarmLevel: alarmLevel,
                    alarmContent: alarmContent,
                    alarmTime: formatDateTime(item.patrol_time),
                  });
                  this.$notify({
                    type: alarmLevel,
                    title: alarmType,
                    message: alarmContent,
                  });
                }, index * 1000);
              });
            }
          });
          break;
        case MessageAction.PATROL.TASK_ITEM:
          data.forEach((item) => {
            const { patrol_status, patrol_time, patrolpoint_name, value_disp } = item;
            switch (patrol_status) {
              case PatrolStatus.WARNING:
                this.alarmList.unshift({
                  ...item,
                  id: uniqueId('alarm-'),
                  alarmType: '巡视告警',
                  alarmLevel: 'warning',
                  alarmContent: `${patrolpoint_name}【${value_disp || '-'}】`,
                  alarmTime: formatDateTime(patrol_time),
                });
                break;
              case PatrolStatus.FAILURE:
                this.alarmList.unshift({
                  ...item,
                  id: uniqueId('alarm-'),
                  alarmType: '巡视告警',
                  alarmLevel: 'error',
                  alarmContent: `${patrolpoint_name}【${value_disp || '-'}】`,
                  alarmTime: formatDateTime(patrol_time),
                });
                break;
              default:
                break;
            }
          });
          break;
        case MessageAction.PATROL.TASK_INDEX:
          data.forEach((item) => {
            if (CustomPatrolTypes.includes(item.patrol_type)) return;

            const { execute_id, task_name, task_status, task_progress, patrolpoint_total } = item;
            switch (task_status) {
              case TaskStatus.UNDO:
                this.$notify({
                  type: 'success',
                  title: NoticeActionMap[action],
                  message: `${task_name}等待执行！`,
                });
                break;
              case TaskStatus.DONE:
                this.$notify({
                  type: 'success',
                  title: NoticeActionMap[action],
                  message: `${task_name}执行完成！`,
                });
                break;
              case TaskStatus.DOING:
                if (task_progress === 0) {
                  const h = this.$createElement;
                  const notice = this.$notify({
                    type: 'info',
                    title: NoticeActionMap[action],
                    dangerouslyUseHTMLString: true,
                    message: h('div', { class: 'sy-notice-task' }, [
                      h('div', { class: 'sy-notice-task__content' }, `${task_name}开始执行！`),
                      h('div', { class: 'sy-notice-task__action' }, [
                        h(
                          'el-button',
                          {
                            class: 'sy-btn-regular el-button--warning el-button--mini',
                            on: {
                              click: () => {
                                notice.close();
                              },
                            },
                          },
                          '取消'
                        ),
                        h(
                          'el-button',
                          {
                            class: 'sy-btn-regular el-button--primary el-button--mini',
                            on: {
                              click: () => {
                                notice.close();
                                this.handleTaskAction('check', item);
                              },
                            },
                          },
                          '查看'
                        ),
                      ]),
                    ]),
                  });
                }
                break;
              case TaskStatus.PAUSE:
                this.$notify({
                  type: 'warning',
                  title: NoticeActionMap[action],
                  message: `${task_name}执行暂停！`,
                });
                break;
              case TaskStatus.WAITING:
                this.$notify({
                  type: 'warning',
                  title: NoticeActionMap[action],
                  message: `${task_name}执行暂停！`,
                });
                break;
              case TaskStatus.OVERDUE:
                this.$notify({
                  type: 'warning',
                  title: NoticeActionMap[action],
                  message: `${task_name}执行超期！`,
                });
                break;
              case TaskStatus.TERMINATE:
                this.$notify({
                  type: 'warning',
                  title: NoticeActionMap[action],
                  message: `${task_name}执行终止！`,
                });
                break;
              default:
                break;
            }
            // 合并任务
            const index = this.taskList.findIndex((item) => item.execute_id === execute_id);
            if ([TaskStatus.DONE, TaskStatus.OVERDUE, TaskStatus.TERMINATE].includes(task_status)) {
              if (index >= 0) this.taskList.splice(index, 1);
            } else {
              const progress =
                task_progress && patrolpoint_total ? Math.round((task_progress / patrolpoint_total) * 100) : 0;
              const taskItem = {
                ...item,
                task_id: item.task_id,
                task_name: item.task_name,
                task_status: item.task_status,
                task_start_time: formatDateTime(item.task_estimated_time),
                task_progress: progress > 100 ? 100 : progress,
              };
              if (index >= 0) {
                this.taskList.splice(index, 1, taskItem);
              } else {
                this.taskList.push(taskItem);
              }
            }
          });
          break;
        default:
          break;
      }
    },
    onStompStatusPush(msg) {
      this.$bus.$emit('stompStatusPush', msg.body);
    },
    getTaskListData() {
      this.$api.getPatrolApi('list').then((res) => {
        if (!res?.results) return;
        this.taskList = res.results
          .filter((item) => item.execute_id)
          .map((item) => {
            const progress = item.progress && item.total ? Math.round((item.progress / item.total) * 100) : 0;
            return {
              ...item,
              task_id: item.id,
              task_name: item.name,
              task_status: item.status,
              task_start_time: '-',
              task_progress: progress > 100 ? 100 : progress,
            };
          });
      });
    },
    getAlarmListData() {
      Promise.all([
        this.$api.postHistoryApi('query', {
          index: 'history.item',
          from: 0,
          size: 10,
          match: {
            station_id: this.stationId,
            modified: false,
            patrol_status: [2, 3],
          },
          order: { patrol_time: 'desc' },
        }),
        this.$api.getBaseApi('commpointalarm', {
          limit: 10,
          offset: 0,
          ordering: '-time',
        }),
      ]).then(([res1, res2]) => {
        // console.log('res1, res2: ', res1, res2);
        let patrolAlarmList = [],
          statusAlarmList = [];
        if (res1?.data) {
          patrolAlarmList = res1.data.map((item) => {
            const { patrol_time, patrolpoint_name, value_disp } = item;
            return {
              ...item,
              alarmType: '巡视告警',
              alarmlevel: 'warning',
              alarmTime: formatDateTime(patrol_time),
              alarmContent: `${patrolpoint_name}【${value_disp || '-'}】`,
            };
          });
        }
        if (res2?.results) {
          statusAlarmList = res2.results.map((item) => {
            const { alarm, time, commpoint, value_disp } = item;
            return {
              ...item,
              alarmTime: formatDateTime(time),
              alarmType: this.alarmTypeMap[alarm?.alarm_type] || '状态告警',
              alarmLevel: AlarmLevelMap[alarm?.alarm_level] || 'warning',
              alarmContent: `${commpoint?.name}【${value_disp || '-'}】`,
            };
          });
        }
        this.alarmList = [...patrolAlarmList, ...statusAlarmList].sort((a, b) =>
          b.alarmTime.localeCompare(a.alarmTime)
        );
      });
    },
    getPatrolAlarmData() {
      this.$api
        .postHistoryApi('query', {
          index: 'history.item',
          from: 0,
          size: 20,
          match: {
            station_id: this.stationId,
            modified: false,
            patrol_status: [2, 3],
          },
          order: { patrol_time: 'desc' },
        })
        .then((res) => {
          if (!res?.data) return;
          this.alarmList = res.data.map((item) => {
            const { patrol_time, patrolpoint_name, value_disp } = item;
            return {
              ...item,
              alarmType: '巡视告警',
              alarmlevel: 'warning',
              alarmTime: formatDateTime(patrol_time),
              alarmContent: `${patrolpoint_name}【${value_disp || '-'}】`,
            };
          });
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.header-right {
  display: flex;
  align-items: center;
  position: absolute;
  top: 0;
  right: 20px;
  height: 100%;
  padding-bottom: 2vh;

  &__popover {
    z-index: 20;
    margin-right: 15px;
  }
  &__icon {
    color: $text-secondary;
    font-size: 24px;
    cursor: pointer;

    &:hover {
      color: $text-primary;
    }
  }
  &__dropdown {
    color: $text-primary;
    cursor: pointer;

    .el-dropdown-selfdefine {
      display: flex;
      align-items: center;
    }
    .sy-icon {
      color: $text-primary;
      font-size: 24px;
      cursor: pointer;
    }
    .uname {
      max-width: 100px;
      margin: 0 5px;
      font-size: 20px;
      text-align: center;
      @include text-ellipsis();
    }
  }
}
.task-popper {
  &-wrapper {
    width: 660px;
  }
  &-list {
    width: 100%;
    height: 100%;
    overflow-y: auto;
  }
  &-table {
    width: 100%;
    height: auto !important;
    max-height: 40vh;

    ::v-deep .cell {
      padding: 0 5px;
    }
  }
}
.alarm-popper {
  &-wrapper {
    width: 660px;
  }
  &-title {
    display: flex;
    align-items: center;
    justify-content: space-between;
    color: $text-primary;
    font-weight: 600;
    border-bottom: $border-default;
  }
  &-list {
    width: 100%;
    max-height: 30vh;
    overflow-y: auto;
  }
  &-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    height: 3vh;
    padding: 0 5px;
    font-size: 12px;
    border-bottom: $border-default;

    &.success {
      color: $color-success;
    }
    &.warning {
      color: $color-warning;
    }
    &.error {
      color: $color-danger;
    }
    &.info {
      color: $color-disable;
    }

    &__type {
      max-width: 100px;
      @include text-ellipsis();
    }
    &__info {
      max-width: 300px;
      margin: 0 10px;
      @include text-ellipsis();
    }
    &__check {
      margin-left: 10px;
    }
  }
}
</style>
