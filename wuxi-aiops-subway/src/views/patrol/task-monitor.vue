<template>
  <el-row id="task_monitor" class="sy-row" :gutter="10">
    <el-col class="sy-col-vertical" :span="6" v-loading="taskLoad">
      <div class="task-info sy-view">
        <div class="task-info-title sy-title">任务信息</div>
        <div class="task-info-main">
          <div class="task-info-main__left">
            <div class="task-info-progress">
              <sy-progress
                type="circle"
                start-color="#035fa9"
                stop-color="#008cff"
                :percentage="100"
                :format="() => taskData.patrolpoint_total || '0'"
              ></sy-progress>
              <div class="task-info-progress__label">总测点数量</div>
            </div>
            <div class="task-info-progress">
              <sy-progress
                type="circle"
                start-color="#035fa9"
                stop-color="#008cff"
                :percentage="100"
                :format="() => taskData.task_progress || '0'"
              ></sy-progress>
              <div class="task-info-progress__label">已巡视数量</div>
            </div>
          </div>
          <ul class="task-info-main__right">
            <li class="task-info-status" @click="handleStatusClick(0)">
              <label>成功</label>
              <span>{{ taskCount[patrolStatus.SUCCESS] || 0 }}</span>
            </li>
            <li class="task-info-status" @click="handleStatusClick(2)">
              <label>告警</label>
              <span>{{ taskCount[patrolStatus.WARNING] || 0 }}</span>
            </li>
            <li class="task-info-status" @click="handleStatusClick(3)">
              <label>失败</label>
              <span>{{ taskCount[patrolStatus.FAILURE] || 0 }}</span>
            </li>
          </ul>
        </div>

        <div class="task-info-detail">
          <el-collapse value="1">
            <el-collapse-item title="任务详情" name="1">
              <div class="task-info-detail__content">
                <div class="task-info-detail__list">
                  <template v-if="taskData">
                    <div class="task-info-detail__item">
                      <label class="task-label">任务名称：</label>
                      <span>{{ taskData.task_name }}</span>
                    </div>
                    <div class="task-info-detail__item">
                      <label class="task-label">执行类型：</label>
                      <span>{{ taskTypeMap[taskData.task_type] }}</span>
                    </div>
                    <div class="task-info-detail__item">
                      <label class="task-label">开始时间：</label>
                      <span>{{ taskData.task_start_time | formatDateTime }}</span>
                    </div>
                    <div class="task-info-detail__item">
                      <label class="task-label">结束时间：</label>
                      <span>{{ taskData.task_end_time | formatDateTime }}</span>
                    </div>
                    <div class="task-info-detail__item">
                      <label class="task-label">任务进度：</label>
                      <sy-progress :percentage="taskProgress"></sy-progress>
                    </div>
                  </template>
                  <sy-empty v-else></sy-empty>
                </div>
                <div class="task-info-detail__action">
                  <span class="task-info-detail__btn" @click="handleTaskAction('pause')">
                    <img src="@/assets/img/icon_stop.png" alt="#" />
                    <span>暂停任务</span>
                  </span>
                  <span class="task-info-detail__btn" @click="handleTaskAction('resume')">
                    <img src="@/assets/img/icon_start.png" alt="#" />
                    <span>继续任务</span>
                  </span>
                  <span class="task-info-detail__btn" @click="handleTaskAction('terminate')">
                    <img src="@/assets/img/icon_stop.png" alt="#" />
                    <span>终止任务</span>
                  </span>
                </div>
              </div>
            </el-collapse-item>
          </el-collapse>
        </div>
      </div>

      <div class="task-data sy-view">
        <div class="task-data-title sy-title">任务数据</div>
        <div class="task-data-result">
          <div class="task-data-result__header">
            <span class="snum">序号</span>
            <span class="point">巡视点位</span>
            <span class="value">巡视结果</span>
            <span class="state">巡视结论</span>
          </div>
          <div class="task-data-result__wrapper">
            <RecycleScroller
              v-if="patrolItems.length > 0"
              class="task-data-result__list"
              ref="scroller"
              :item-size="40"
              :items="patrolItems"
            >
              <template v-slot="{ item, index }">
                <div
                  class="task-data-result__item"
                  :class="index > 2 ? 'text-secondary' : 'text-primary'"
                  @click="handleDetailClick(item)"
                >
                  <span class="snum">{{ index + 1 }}</span>
                  <span class="point" :title="item.patrolpoint_name">{{ item.patrolpoint_name }}</span>
                  <span class="value" :title="item.value_disp || item.value">
                    {{ item.value_disp || item.value || '-' }}
                  </span>
                  <span class="state" :class="`sy-status-${item.patrol_status}`">{{ item.patrolStatus }}</span>
                </div>
              </template>
            </RecycleScroller>
            <sy-empty v-else></sy-empty>
          </div>
        </div>
      </div>
    </el-col>

    <el-col class="sy-col-vertical" :span="18">
      <div class="task-monitor-header">
        <CameraSelect
          class="task-monitor-select"
          :value="cameraSelect"
          :executeId="executeId"
          :patrolType="patrolType"
          @change="handleSelectCamera"
          @loaded="handleLoadCamera"
        ></CameraSelect>
        <div class="task-monitor-layout">
          <sy-icon
            v-for="(item, key) in displayModes"
            :key="key"
            :name="`split-screen-${item}`"
            :class="{ active: displayMode === item }"
            @click="handleSwitchMode(item)"
          />
        </div>
      </div>
      <div class="task-monitor-wrapper">
        <div class="task-monitor-list" :class="`layout-${displayMode}`">
          <sy-player
            v-for="(item, index) in videoList"
            v-bind="item"
            :key="item.guid"
            :video-url="item.videoUrl"
            :class="{ active: videoIndex === index }"
            @click.native="videoIndex = index"
          ></sy-player>
        </div>
      </div>
    </el-col>

    <PatrolDetails :visible.sync="showDetail" :title="taskDetail.patrolpoint_name" :data="taskDetail"></PatrolDetails>
  </el-row>
</template>

<script>
import { arrToMap } from '@/utils';
import { mediaUrl } from '@/config';
import { uniqueId, round } from 'lodash';
import { DisplayMode, PatrolStatus, MessageAction } from '@/enums';
import Option from '@/enums/option';
import CameraSelect from './components/CameraSelect';

import { RecycleScroller } from 'vue-virtual-scroller';
import 'vue-virtual-scroller/dist/vue-virtual-scroller.css';

export default {
  name: 'TaskMonitor',
  components: {
    CameraSelect,
    RecycleScroller,
    PatrolDetails: () => import('./components/PatrolDetails'),
  },
  computed: {
    displayModes() {
      return DisplayMode;
    },
    patrolStatus() {
      return PatrolStatus;
    },
    taskTypeMap() {
      return arrToMap(Option.TaskTypes);
    },
    patrolStateMap() {
      return arrToMap(Option.PatrolStates);
    },
    taskProgress() {
      return this.taskData.task_progress && this.taskData.patrolpoint_total
        ? Math.min(round((this.taskData.task_progress / this.taskData.patrolpoint_total) * 100), 100)
        : 0;
    },
    patrolItems() {
      return this.activeStatus !== ''
        ? this.patrolResults.filter((item) => item.patrol_status === this.activeStatus)
        : this.patrolResults;
    },
    videoList() {
      // 如果筛选的摄像机为空，则自适应展示，否则展示选择的摄像机
      let cameraData = this.cameraChecked ? this.cameraSelect : this.cameraList;

      let cameras = cameraData.slice(0, this.displayMode).map((item) => ({
        ...item,
        title: item.camera_name,
        nvrId: item.nvr_id,
        cameraId: item.camera_id,
        channelNo: item.channel_no,
        videoUrl: `${mediaUrl}/${item.nvr_id}/${item.channel_no}.flv`,
      }));
      if (cameraData.length >= this.displayMode) {
        return cameras;
      } else {
        return [
          ...cameras,
          ...Array.from({ length: this.displayMode - cameraData.length }).map((_, index) => ({
            camera_id: index,
            title: '',
            videoUrl: '',
          })),
        ];
      }
    },
  },
  watch: {
    $route: {
      handler() {
        this.taskId = this.$route.query.taskId;
        this.executeId = this.$route.query.executeId;
        this.patrolType = this.$route.query.patrolType;
        this.patrolResults = [];
        this.getDataInit();
      },
      immediate: true,
    },
  },
  data() {
    return {
      displayMode: DisplayMode.FOUR,
      videoIndex: 0,
      showDetail: false,
      cameraList: [],
      cameraSelect: [],
      cameraChecked: false,
      taskId: '',
      executeId: '',
      patrolType: '',
      activeStatus: '',
      taskLoad: false,
      taskData: {},
      taskCount: {
        [PatrolStatus.SUCCESS]: 0,
        [PatrolStatus.WARNING]: 0,
        [PatrolStatus.FAILURE]: 0,
      },
      taskDetail: {},
      patrolResults: [],
    };
  },
  mounted() {
    this.onStompPatrolPush();
  },
  methods: {
    // 状态点击
    handleStatusClick(status) {
      if (this.activeStatus === status) {
        this.activeStatus = '';
        return;
      }
      this.activeStatus = status;
    },
    // 实时数据点击
    handleDetailClick(item) {
      if (!item.file_path) {
        return this.$message.warning('暂无巡视结果！');
      }
      this.taskDetail = {
        ...item,
        patrolDevice: item.camera_name || item.robot_name || item.host_name || '-',
      };
      this.showDetail = true;
    },
    // 选择摄像机
    handleSelectCamera(cameras) {
      this.cameraSelect = cameras;
      if (cameras.length === 0) {
        this.cameraChecked = false;
        this.calcLayout(this.cameraList);
      } else {
        this.cameraChecked = true;
        this.calcLayout(this.cameraSelect);
      }
    },
    // 计算布局
    calcLayout(arr) {
      if (arr.length <= 1) {
        this.displayMode = 1;
      } else if (1 < arr.length && arr.length <= 4) {
        this.displayMode = 4;
      } else if (4 < arr.length && arr.length <= 9) {
        this.displayMode = 9;
      } else if (arr.length > 9) {
        this.displayMode = 16;
      }
    },
    // 加载摄像机完成
    handleLoadCamera(cameras) {
      const mode = this.calcLayout(cameras);
      this.cameraSelect = cameras.slice(0, mode);
      this.cameraChecked = false;
      this.cameraList = cameras;
    },
    // 接收到订阅消息
    onStompPatrolPush() {
      this.$bus.$on('stompPatrolPush', (body) => {
        const { action, data } = JSON.parse(body);
        if (!data || !data.length) return;
        switch (action) {
          case MessageAction.PATROL.ALARM:
            {
              const taskItems = data.filter((item) => item.execute_id === this.executeId);
              if (taskItems && taskItems.length > 0) {
                taskItems.forEach((item) => {
                  const taskIndex = this.patrolResults.findIndex(
                    (dataItem) =>
                      item.patrolpoint_id === dataItem.patrolpoint_id &&
                      ((item.camera_id && item.camera_id === dataItem.camera_id) ||
                        (item.robot_id && item.robot_id === dataItem.robot_id) ||
                        (item.host_id && item.robot_id === dataItem.host_id))
                  );
                  const taskItem = this.patrolResults[taskIndex];
                  if (taskIndex > -1) {
                    this.taskCount[PatrolStatus.SUCCESS] -= 1;
                    this.taskCount[PatrolStatus.WARNING] += 1;
                    this.patrolResults.splice(taskIndex, 1, {
                      ...taskItem,
                      patrol_status: item.patrol_status,
                      patrolResult: item.value_disp || '-',
                      patrolStatus: this.patrolStateMap[item.patrol_status] || '-',
                    });
                  }
                });
              }
            }
            break;
          case MessageAction.PATROL.TASK_ITEM:
            data
              .filter((item) => item.execute_id === this.executeId)
              .forEach((item) => {
                const recordIndex = this.patrolResults.findIndex(
                  (dataItem) =>
                    item.patrolpoint_id === dataItem.patrolpoint_id &&
                    ((item.camera_id && item.camera_id === dataItem.camera_id) ||
                      (item.robot_id && item.robot_id === dataItem.robot_id) ||
                      (item.host_id && item.robot_id === dataItem.host_id))
                );
                if (recordIndex > -1) {
                  const recordItem = this.patrolResults[recordIndex];
                  this.patrolResults.splice(recordIndex, 1, {
                    ...recordItem,
                    patrol_status: item.patrol_status,
                    patrolResult: item.value_disp || '-',
                    patrolStatus: this.patrolStateMap[item.patrol_status] || '-',
                  });
                } else {
                  this.taskCount[item.patrol_status] = (this.taskCount[item.patrol_status] || 0) + 1;
                  this.patrolResults.push({
                    ...item,
                    id: uniqueId(),
                    patrolResult: item.value_disp || '-',
                    patrolStatus: this.patrolStateMap[item.patrol_status] || '-',
                  });
                  requestAnimationFrame(() => {
                    this.$refs.scroller.scrollToBottom();
                  });
                }
              });
            break;
          case MessageAction.PATROL.TASK_INDEX:
            {
              const taskItem = data.find((item) => item.execute_id === this.executeId);
              if (taskItem) this.taskData = taskItem;
            }
            break;
          default:
            break;
        }
      });
      this.$once('hook:beforeDestroy', () => {
        this.$bus.$off('stompPatrolPush');
      });
    },
    handleTaskAction(action) {
      if (!this.executeId) {
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
        this.$api.postPatrolApi(`${action}/${this.executeId}`).then((res) => {
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
    },
    handleSwitchMode(type) {
      this.displayMode = type;
    },
    async getDataInit() {
      this.taskLoad = true;

      const [taskData, taskContent, taskCount] = await Promise.all([
        this.getTaskValue(),
        this.getTaskContent(),
        this.getCount(),
      ]);
      this.taskData = taskData.data?.[0] || {};
      this.patrolResults = taskContent.data
        ? taskContent.data.map((item) => {
            return {
              ...item,
              patrolResult: item.value_disp || '-',
              patrolStatus: this.patrolStateMap[item.patrol_status] || '-',
              patrolDevice: item.camera_name || item.robot_name || item.host_name || '-',
            };
          })
        : this.patrolResults;
      if (taskCount.data?.length) {
        this.taskCount = arrToMap(taskCount.data, 'patrol_status', 'count');
      }
      this.taskLoad = false;
    },
    // 获取统计数字
    getCount() {
      const params = {
        index: 'history.item',
        groupField: 'patrol_status',
        execute_id: this.executeId,
      };
      return this.$api.getHistoryApi(`queryCount`, params);
    },
    // 获取任务历史
    getTaskContent() {
      const params = {
        index: 'history.item',
        from: 0,
        size: 500,
        match: { execute_id: this.executeId },
        order: { patrol_time: 'desc' },
      };
      return this.$api.postHistoryApi('query', params);
    },
    // 获取任务基础数据
    getTaskValue() {
      const params = {
        index: 'history.task',
        match: {
          execute_id: this.executeId,
        },
        from: 0,
        size: 10,
      };
      return this.$api.postHistoryApi('query', params);
    },
  },
};
</script>

<style lang="scss" scoped>
.task-left {
  flex: none;
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  margin-right: 20px;
}

.task-info {
  position: relative;

  .task-info-main {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20px;
    background: {
      image: url('~@/assets/img/bg_detail_task.png');
      repeat: no-repeat;
      position: center;
      size: 100% 100%;
    }

    &__left {
      display: flex;
      align-items: center;
      justify-content: center;
      margin-top: 3vh;

      .task-info-progress {
        &:first-of-type {
          margin-right: 15px;
        }
        &__label {
          margin-top: 10px;
          font-size: 14px;
          text-align: center;
        }
        .sy-progress {
          ::v-deep .el-progress-circle {
            width: 100px !important;
            height: 100px !important;
          }
          ::v-deep .el-progress__text {
            font-size: 24px !important;
          }
        }
      }
    }
    &__right {
      .task-info-status {
        display: flex;
        align-items: center;
        justify-content: space-between;
        width: 150px;
        height: 50px;
        padding: 0 20px;
        font-size: 14px;
        cursor: pointer;
        background: {
          repeat: no-repeat;
          size: 100% 100%;
        }

        > span {
          font-size: 18px;
        }
        &:nth-of-type(1) {
          color: $color-primary;
          background-image: url('~@/assets/img/bg_outset_blue.png');
        }
        &:nth-of-type(2) {
          color: $color-danger;
          background-image: url('~@/assets/img/bg_outset_red.png');
        }
        &:nth-of-type(3) {
          color: $color-warning;
          background-image: url('~@/assets/img/bg_outset_yellow.png');
        }
      }
    }
  }

  .task-info-detail {
    padding: 15px;

    &__list {
      width: 100%;
      padding: 10px 20px;
    }
    &__item {
      display: flex;
      align-items: center;

      > label {
        width: 100px;
      }
      > span {
        flex: auto;
        @include text-ellipsis();
      }
      .sy-progress {
        flex: auto;
      }
    }
    &__action {
      display: flex;
      align-items: center;
      width: 100%;
      background-color: $bg-table;
      border-top: $border-default;
    }
    &__btn {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      flex: 1;
      height: 4vh;
      line-height: 1;
      cursor: pointer;

      &:hover {
        background-color: $bg-hover;
      }
      > img {
        width: 20px;
        height: 20px;
        margin-right: 10px;
      }
    }
  }
}

.task-data {
  flex: auto;
  position: relative;
  width: 100%;
  margin-top: 1vh;
  // padding-top: 4vh;

  .task-data-result {
    position: absolute;
    top: 4vh;
    left: 0;
    width: 100%;
    height: calc(100% - 4vh);

    .snum {
      flex: 1;
    }
    .point {
      flex: 3;
      @include text-ellipsis();
    }
    .value {
      flex: 2;
    }
    .state {
      flex: 1;
    }

    &__header {
      display: flex;
      align-items: center;
      width: 100%;
      height: 4vh;
      padding: 0 10px;
      font-size: 16px;
      text-align: center;
      white-space: nowrap;
      background: $bg-header;
    }
    &__wrapper {
      width: 100%;
      height: calc(100% - 4vh);
    }
    &__list {
      width: 100%;
      height: 100%;
      .text-primary {
        color: $text-primary;
      }
      .text-secondary {
        color: $text-secondary;
      }
    }
    &__item {
      display: flex;
      align-items: center;
      width: 100%;
      padding: 10px;
      font-size: 14px;
      text-align: center;
      cursor: pointer;

      &:hover {
        background-color: $bg-hover;
      }
    }
  }
}

.task-monitor {
  &-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    height: 5vh;
    padding: 10px 20px;
    background-color: $bg-base;
  }
  &-select {
    width: 250px;
  }
  &-layout {
    .sy-icon {
      font-size: 24px;

      &:hover,
      &.active {
        color: $color-white;
      }

      + .sy-icon {
        margin-left: 10px;
      }
    }
  }

  &-wrapper {
    flex: auto;
    width: 100%;
    margin-top: 1vh;
    background: $bg-base;
  }
  &-list {
    display: grid;
    width: 100%;
    height: 100%;

    &.layout-1 {
      grid-template-rows: repeat(1, 1fr);
      grid-template-columns: repeat(1, 1fr);
    }
    &.layout-4 {
      grid-template-rows: repeat(2, 1fr);
      grid-template-columns: repeat(2, 1fr);
    }
    &.layout-9 {
      grid-template-rows: repeat(3, 1fr);
      grid-template-columns: repeat(3, 1fr);
    }
    &.layout-16 {
      grid-template-rows: repeat(4, 1fr);
      grid-template-columns: repeat(4, 1fr);
    }
    .photo-item {
      border: $border-default;
    }
  }
}
</style>
