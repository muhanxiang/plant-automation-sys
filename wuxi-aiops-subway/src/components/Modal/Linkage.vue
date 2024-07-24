<template>
  <el-dialog
    custom-class="sy-dialog sy-dialog--medium"
    :title="title"
    :visible.sync="isVisible"
    :append-to-body="appendToBody"
    :destroy-on-close="destroyOnClose"
    center
    @open="onDialogOpen"
    @close="onDialogClose"
  >
    <el-row class="sy-row smart-linkage-wrapper">
      <el-col class="sy-col left" :span="16">
        <template v-if="showRes">
          <FileViewer :data="device">
            <span slot="info">
              {{ `${device.patrolpoint_name}: ${device.value_disp || '-'}` }}
            </span>
          </FileViewer>
        </template>
        <template v-else>
          <sy-player v-bind="linkageVideo"></sy-player>
        </template>
      </el-col>
      <el-col class="sy-col" :span="8">
        <div class="right">
          <div class="right-top">
            <div class="top-item">
              <div class="text-light top-name">巡视点位</div>
              <div class="top-value text-over" :title="pointObj.name">{{ pointObj.name }}</div>
            </div>
            <div class="top-item">
              <div class="text-light top-name">联动信号</div>
              <div class="top-value text-over">{{ pointObj.linkageValue }}</div>
            </div>
            <div class="top-item">
              <div class="text-light top-name">信号类型</div>
              <div class="top-value text-over">{{ pointObj.signalLabel }}</div>
            </div>
          </div>
          <div class="right-center">
            <span class="text-light point-name">联动设备</span>
            <span class="text-light result">结果</span>
          </div>
          <div class="right-bottom">
            <div class="bottom-item" v-for="item in cameras" :key="item.camera_id">
              <div class="text-primary bottom-name" @click="handleNameClick(item)">{{ item.camera_name }}</div>
              <div class="bottom-value" :class="{ pointer: !!item.value_disp }" @click="handleResClick(item)">
                {{ item.value_disp || '-' }}
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button class="sy-btn-regular" type="warning" size="small" @click="isVisible = false">关 闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { arrToMap } from '@/utils';
import { mediaUrl } from '@/config';
import { FileType, MessageAction } from '@/enums';
import Option from '@/enums/option';
import FileViewer from '@/components/FileViewer';

export default {
  name: 'LinkageModal',
  components: {
    FileViewer,
  },
  computed: {
    isVisible: {
      get() {
        return this.visible;
      },
      set(bool) {
        this.$emit('update:visible', bool);
      },
    },
    fileType() {
      return FileType;
    },
    linkageSignalMap() {
      return arrToMap(Option.LinkageSignals);
    },
    linkageVideo() {
      return {
        title: this.device.camera_name,
        nvrId: this.device.nvr_id,
        cameraId: this.device.camera_id,
        channelNo: this.device.channel_no,
        videoUrl: `${mediaUrl}/${this.device.nvr_id}/${this.device.channel_no}.flv`,
      };
    },
    cameras() {
      this.cameraCount;
      return Array.from(this.cameraMap.values());
    },
  },
  props: {
    title: {
      type: String,
      default: '联动任务弹窗',
    },
    visible: {
      type: Boolean,
      default: false,
    },
    linkage: {
      type: Object,
      default: () => ({}),
    },
    appendToBody: {
      type: Boolean,
      default: true,
    },
    destroyOnClose: {
      type: Boolean,
      default: false,
    },
  },
  watch: {
    linkage: {
      handler() {
        this.cameraMap.clear();
        this.cameraCount = 0;
        if (!this.linkage.task_id) return;
        this.getInitData();
      },
      immediate: true,
      deep: true,
    },
  },
  data() {
    return {
      maxValue: '',
      minValue: '',
      cameraCount: 0, // 只做触发，无实际意义
      device: {
        nvr_id: '',
        camera_id: '',
        camera_name: '',
        channel_no: '',
        file_type: '',
        file_path: '',
        patrol_status: 0,
      },
      pointObj: {
        name: '',
        linkageValue: '',
        signalLabel: '',
      },
      cameraMap: new Map(),
      stompSub: null,
      showRes: false,
    };
  },
  methods: {
    handleNameClick(item) {
      this.showRes = false;
      this.device = item;
    },
    // 结果点击
    handleResClick(item) {
      if (!item.value_disp) return;
      this.showRes = true;
      this.device = item;
    },
    async getInitData() {
      const res = await this.$api.getLinkageApi(`device/${this.linkage.task_id}`);
      this.pointObj = {
        name: res.name || res.patrolpoint_name,
        linkageValue: res.yc_name || res.yx_name,
        signalLabel: `${this.linkageSignalMap[res.signal_type]}变化`,
      };

      res.cameras.forEach((item) => {
        this.cameraCount += 1;
        this.cameraMap.set(item.camera_id, {
          ...item,
          value_disp: '',
        });
      });

      const hisRes = await this.$api.postHistoryApi('query', {
        index: 'history.item',
        match: {
          execute_id: this.linkage.execute_id,
        },
        order: {
          task_end_time: 'desc',
        },
        from: 0,
        size: 10,
      });
      if (hisRes.data && hisRes.data.length > 0) {
        hisRes.data.forEach((item) => {
          if (this.cameraMap.has(item.camera_id)) {
            this.cameraCount += 1;
            const cameraItem = this.cameraMap.get(item.camera_id);
            this.cameraMap.set(item.camera_id, {
              ...cameraItem,
              value_disp: cameraItem.value_disp || item.value_disp, // TODO 时间降序此处任务如果有了值就不更新值，历史数据可能重复，可能有问题
              file_type: item.file_type,
              file_path: item.file_path,
            });
          }
        });
      }
      if (this.cameraMap.size > 0) {
        this.showRes = false;
        this.device = Array.from(this.cameraMap.values())[0];
        this.device.file_type = 2;
      }
    },
    // 选择区域
    onRegionSelect(res) {
      this.maxValue = res.maxTemp;
      this.minValue = res.minTemp;
    },
    onDialogOpen() {
      this.$bus.$on('stompPatrolPush', (body) => {
        const { action, data } = JSON.parse(body);
        if (!data || !data.length) return;
        if (action === MessageAction.PATROL.TASK_ITEM) {
          data.forEach((item) => {
            // TODO 此处过滤条件可能需要根据字段修改
            if (item.execute_id === this.linkage.execute_id && this.cameraMap.has(item.camera_id)) {
              const cameraItem = this.cameraMap.get(item.camera_id);
              this.cameraMap.set(item.camera_id, {
                ...cameraItem,
                value_disp: item.value_disp,
                file_type: item.file_type,
                file_path: item.file_path,
              });
              this.cameraCount += 1;
            }
          });
        }
      });
    },
    onDialogClose() {
      this.$bus.$off('stompPatrolPush');
    },
  },
};
</script>

<style lang="scss" scoped>
.sy-row {
  border: $border-default;
  color: $text-regular;
  .link-img {
    width: 100%;
    height: 100%;
    cursor: pointer;
  }
  .left {
    position: relative;
  }
  .right {
    height: 100%;
    display: flex;
    flex-direction: column;
    border-left: $border-default;
    .text-primary {
      color: $text-secondary;
    }
    .text-light {
      color: $text-primary;
    }
    .right-top {
      display: flex;
      flex-direction: column;
      .top-item {
        flex: auto;
        display: flex;
        border-bottom: $border-default;
        align-items: center;
        .top-name {
          border-right: $border-default;
          width: 100px;
          display: flex;
          align-items: center;
          justify-content: center;
          height: 100%;
          background: $bg-header;
        }
        .top-value {
          flex: 1;
          height: 5vh;
          display: flex;
          align-items: center;
          padding-left: 10px;
        }
      }
    }
    .right-center {
      height: 5vh;
      display: flex;
      align-items: center;
      justify-content: center;
      border-bottom: $border-default;
      background: $bg-header;
      .point-name {
        flex: 1;
        text-align: center;
      }
      .result {
        width: 80px;
        text-align: center;
      }
    }
    .right-bottom {
      flex: auto;
      display: flex;
      flex-direction: column;
      overflow: auto;
      .bottom-item {
        display: flex;
        border-bottom: $border-default;
        align-items: center;
        .bottom-name {
          cursor: pointer;
          text-align: center;
          flex: 1;
          display: flex;
          justify-content: center;
          align-items: center;
          border-right: $border-default;
          height: 5vh;
        }
        .bottom-value {
          text-align: center;
          width: 80px;
        }
        .pointer {
          cursor: pointer;
        }
      }
    }
  }
}
</style>
