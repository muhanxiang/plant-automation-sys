<template>
  <el-row id="task_identify" class="sy-row" :gutter="10">
    <el-col class="sy-col" :span="5">
      <div class="identify-aside sy-view">
        <div class="identify-aside-search">
          <el-input
            v-model.trim="pointName"
            size="small"
            placeholder="请输入点位名称"
            suffix-icon="el-icon-search"
            :maxlength="20"
            clearable
            @input="onPointSearch"
          ></el-input>
          <el-button class="sy-btn-regular" type="primary" size="medium" @click="handleResetTree">重置</el-button>
        </div>

        <div class="identify-aside-wrapper">
          <sy-tree
            v-show="!pointSearch"
            class="identify-aside-tree"
            ref="tree"
            node-key="guid"
            empty-text="暂无巡视点位"
            :props="treeProps"
            :load="loadTreeData"
            :filter-node-method="filterTreeNode"
            lazy
            show-checkbox
            check-strictly
            highlight-current
            check-on-click-node
            @check-change="onNodeCheckChange"
          >
            <template v-slot="{ data, node }">
              <div class="el-tree-node__custom">
                <sy-icon v-if="data.icon" :name="data.icon" />
                <span class="el-tree-node__label" :title="node.label">{{ node.label }}</span>
                <i v-if="data.focus" class="el-tree-node__icon el-icon-star-off" />
              </div>
            </template>
          </sy-tree>
          <div v-show="pointSearch" v-loading="pointLoad" class="identify-aside-group">
            <el-radio-group v-if="pointList.length > 0" class="sy-radio-list" v-model="currPointId">
              <el-radio
                v-for="item in pointList"
                :key="item.guid"
                :label="item.guid"
                @change="onPointChange(item, $event)"
              >
                {{ item.name }}
              </el-radio>
            </el-radio-group>
            <sy-empty v-else-if="!pointLoad" description="暂无搜索结果"></sy-empty>
          </div>
        </div>
      </div>
    </el-col>
    <el-col :span="12" class="sy-col">
      <div class="identify-info sy-view">
        <!-- <div class="sy-title">识别信息</div> -->
        <div class="identify-info-player">
          <sy-player v-bind="patrolCamera"></sy-player>
        </div>
        <div class="identify-info-bottom">
          <div class="identify-info-preset">
            <div class="identify-preset-header">
              <span class="identify-preset-label">巡视点位</span>
              <span class="identify-preset-point">
                {{ patrolPoint ? patrolPoint.name : '未选择点位' }}
              </span>
            </div>
            <div class="identify-preset-wrapper">
              <ul class="identify-preset-list" v-if="patrolPresets.length > 0">
                <li class="identify-preset-item" v-for="item in patrolPresets" :key="item.guid">
                  <span class="identify-preset-item__name" :title="item.name">{{ item.presetName }}</span>
                  <el-button class="sy-btn-regular" type="primary" size="small" @click="handleCallPreset(item)">
                    调用预置位
                  </el-button>
                </li>
              </ul>
              <sy-empty v-else description="暂无预置位"></sy-empty>
            </div>
          </div>
          <div class="identify-info-result">
            <div class="identify-result-header">
              <span class="identify-result-label">识别结果</span>
              <span v-if="identifyResult" class="identify-result-value">
                {{ identifyResult.value_disp || '-' }}
              </span>
            </div>
            <div class="identify-result-wrapper">
              <FileViewer v-if="identifyResult" class="identify-result-viewer" :data="identifyResult" />
              <sy-empty v-else description="暂无识别结果"></sy-empty>
            </div>
          </div>
        </div>
      </div>
    </el-col>
    <el-col :span="7" class="sy-col">
      <div class="identify-history sy-view">
        <div class="sy-title">识别历史</div>
        <div class="identify-history-swiper">
          <el-carousel
            v-if="historyImages.length > 0"
            ref="carousel"
            class="sy-carousel"
            trigger="click"
            :loop="false"
            :autoplay="false"
          >
            <el-carousel-item v-for="(item, index) in historyImages" :key="index">
              <FileViewer :data="item">
                <span slot="info">
                  {{ `${item.patrolpoint_name}: ${item.value_disp || '-'}` }}
                </span>
              </FileViewer>
            </el-carousel-item>
          </el-carousel>
          <sy-empty v-else description="暂无识别历史"></sy-empty>
        </div>
        <div class="identify-history-chart">
          <sy-chart
            v-if="historyData.length > 0"
            type="line"
            :option="historyOption"
            @zrclick="handleChartClick"
          ></sy-chart>
          <sy-empty v-else description="暂无识别历史"></sy-empty>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import { debounce } from 'lodash';
import { mediaUrl } from '@/config';
import { getImgFullPath } from '@/utils';
import { MessageAction, RecognitionType, PatrolStatus } from '@/enums';
import PointTree from '@/mixins/point-tree';
import FileViewer from '@/components/FileViewer';

export default {
  name: 'TaskIdentify',
  mixins: [PointTree],
  components: { FileViewer },
  computed: {
    videoUrl() {
      if (this.patrolCamera) {
        const { nvr, channel_no } = this.patrolCamera;
        return `${mediaUrl}/${nvr}/${channel_no}.flv`;
      } else {
        return '';
      }
    },
    pointSearch() {
      return this.pointName.length > 0;
    },
    historyImages() {
      return this.historyData.map((item) => ({ ...item, id: item.id, imgUrl: getImgFullPath(item.file_path) }));
    },
    historyOption() {
      return {
        grid: {
          bottom: '15%',
        },
        xAxis: {
          inverse: true,
          axisLabel: {
            interval: 'auto',
          },
        },
        legend: {
          top: '5%',
        },
        tooltip: {
          axisPointer: {
            type: 'shadow',
          },
          formatter: (params) => {
            const { data, marker, seriesName } = params[0];
            // console.log('patrol_time: ', data.patrol_time);
            return `
                <div>${data.patrol_time}</div>
                <div style="margin-top: 10px">
                  <span style="margin-right: 20px">${marker}${seriesName}</span>
                  <span>${data.value}</span>
                </div>
            `;
          },
        },
        dataZoom: [
          {
            type: 'inside',
            start: 0,
            end: 50,
            minSpan: 25,
            maxSpan: 75,
          },
          {
            type: 'slider',
            left: '8%',
            right: '8%',
            height: '5%',
            start: 0,
            end: 50,
            minSpan: 25,
            maxSpan: 75,
            textStyle: {
              overflow: 'break',
            },
          },
        ],
        dataset: {
          source: this.historyData.map((item) => {
            return {
              ...item,
              patrolTime: this.$dayjs(item.patrol_time).format('YYYY-MM-DD\nHH:mm:ss'),
            };
          }),
        },
        series: [
          {
            name: '识别结果',
            type: 'line',
            smooth: true,
            lineStyle: {
              width: 1,
            },
            areaStyle: {
              opacity: 0.1,
            },
            encode: {
              x: 'patrolTime',
              y: 'value',
            },
          },
        ],
      };
    },
  },
  watch: {
    pointSearch(bool) {
      if (bool) {
        const checkedKeys = this.$refs.tree.getCheckedKeys(true);
        this.currPointId = checkedKeys[0];
      } else {
        this.$nextTick(() => {
          this.currPointId && this.$refs.tree.setCheckedKeys([this.currPointId]);
        });
      }
    },
  },
  data() {
    return {
      leafOnly: true, // 仅叶子节点可选中
      identifyFlag: false,
      identifyResult: null,
      historyData: [],
      currPointId: '',
      patrolPoint: {},
      patrolCamera: {},
      patrolPresets: [],
      pointLoad: false,
      pointList: [],
      pointName: '',
    };
  },
  mounted() {
    this.onStompPatrolPush();
  },
  methods: {
    // 订阅消息
    onStompPatrolPush() {
      this.$bus.$on('stompPatrolPush', (body) => {
        const { action, data } = JSON.parse(body);
        if (!this.identifyFlag || !data || !data.length) return;
        switch (action) {
          case MessageAction.PATROL.TASK_ITEM:
            {
              const result = data[0];
              if (this.currPointId === result.patrolpoint_id && this.patrolCamera.guid === result.camera_id) {
                const { value_disp, patrol_status, patrolpoint_name } = result;
                switch (patrol_status) {
                  case PatrolStatus.SUCCESS:
                    this.$message({
                      type: 'success',
                      message: `${patrolpoint_name}识别成功！`,
                    });
                    this.identifyResult = { ...result, value_disp: value_disp || '-' };
                    this.historyData.push(this.identifyResult);
                    break;
                  case PatrolStatus.DEFECTS:
                    this.$message({
                      type: 'info',
                      message: `${patrolpoint_name}识别缺陷！`,
                    });
                    this.identifyResult = { ...result, value_disp: value_disp || '-' };
                    this.historyData.push(this.identifyResult);
                    break;
                  case PatrolStatus.WARNING:
                    this.$message({
                      type: 'success',
                      message: `${patrolpoint_name}识别告警！`,
                    });
                    this.identifyResult = { ...result, value_disp: value_disp || '-' };
                    this.historyData.push(this.identifyResult);
                    break;
                  case PatrolStatus.FAILURE:
                    this.$message({
                      type: 'error',
                      message: `${patrolpoint_name}识别失败！`,
                    });
                    this.identifyResult = { ...result, value_disp: value_disp || '-' };
                    break;
                  default:
                    break;
                }
                this.identifyFlag = false;
              }
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
    // 重置树选择
    handleResetTree() {
      this.pointName = '';
      this.pointList = [];
      this.currPointId = undefined;
      this.$refs.tree.setCheckedKeys([]);
    },
    handleChartClick({ dataIndex }) {
      this.$refs.carousel.setActiveItem(dataIndex);
    },
    // 调用预置位
    handleCallPreset(preset) {
      const { slave, camera, virtual, patrolpoint } = preset;
      this.patrolCamera = {
        ...camera,
        title: camera.name,
        nvrId: camera.nvr,
        cameraId: camera.guid,
        channelNo: camera.channel_no,
        videoUrl: `${mediaUrl}/${camera.nvr}/${camera.channel_no}.flv`,
      };
      this.patrolPoint.deviceName = preset.cameraName;
      this.currPointId = virtual ? slave.guid : patrolpoint.guid;
      this.$api
        .postPatrolApi('single', {
          camera_id: camera.guid,
          patrolpoint_id: this.currPointId,
        })
        .then((res) => {
          if (res?.success) {
            this.$message({
              type: 'success',
              message: '识别指令发送成功，请等待识别结果！',
            });
            this.identifyFlag = true;
          } else {
            this.$message({
              type: 'error',
              message: '识别指令发送失败！',
            });
          }
        });
    },
    // 搜索巡视点位
    onPointSearch: debounce(function (val) {
      if (!val) {
        this.pointList = [];
        return;
      }
      this.pointLoad = true;
      this.$api
        .getBaseApi('patrolpoint', {
          name__contains: val,
        })
        .then((res) => {
          if (!res?.results) return;
          this.pointList = res.results;
        })
        .finally(() => {
          this.pointLoad = false;
        });
    }, 300),
    // 巡视点位改变
    onPointChange(item) {
      this.patrolCamera = {};
      this.patrolPoint = item;
      this.getPointPresetList(item);
      this.getPointHistoryData(item.guid);
    },
    // 树节点选中改变
    onNodeCheckChange(data, checked) {
      // console.log('onNodeCheckChange: ', data, checked);
      if (checked) {
        this.$refs.tree.setCheckedKeys([data.guid]);

        this.patrolPoint = data;
        this.patrolCamera = {};
        this.getPointPresetList(data);
        this.getPointHistoryData(data.guid);
      } else {
        const checkedKeys = this.$refs.tree.getCheckedKeys();

        if (checkedKeys.length === 0) {
          this.historyData = [];
          this.patrolPoint = {};
          this.patrolCamera = {};
          this.patrolPresets = [];
          this.identifyResult = null;
        }
      }
    },
    getPointPresetList(point) {
      const isSlave =
        point.recognition_type === RecognitionType.METER || point.recognition_type === RecognitionType.STATUS;
      const params = isSlave ? { virtual: true, slave: point.guid } : { virtual: false, patrolpoint: point.guid };

      this.$api.getBaseApi('preset', params).then((res) => {
        if (!res?.results?.length) {
          this.patrolCamera = {};
          this.patrolPresets = [];
          return;
        }
        this.patrolPresets = res.results.map((item) => ({
          ...item,
          cameraName: item.camera?.name || '-',
          presetName: (isSlave ? item.slave?.name : item.name) || '-',
        }));
        const camera = this.patrolPresets[0].camera;
        this.patrolCamera = {
          ...camera,
          title: camera.name,
          nvrId: camera?.nvr,
          cameraId: camera?.guid,
          channelNo: camera?.channel_no,
          videoUrl: `${mediaUrl}/${camera.nvr}/${camera.channel_no}.flv`,
        };
        // 默认调用预置位
        this.handleCallPreset(this.patrolPresets[0]);
      });
    },
    getPointHistoryData(patrolpointId) {
      this.$api
        .postHistoryApi('query', {
          index: 'history.item',
          from: 0,
          size: 10,
          match: { patrolpoint_id: patrolpointId },
          order: { patrol_time: 'desc' },
        })
        .then((res) => {
          if (!res?.data) {
            this.historyData = [];
            return;
          }
          this.historyData = res.data;
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.sy-swiper {
  width: 100%;
  height: 100%;
  padding: 50px;
}

.identify-aside {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  padding: 10px;

  &-search {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 1vh;

    .el-input {
      flex: auto;
      margin-right: 10px;
    }
  }
  &-wrapper {
    flex: auto;
    width: 100%;
    overflow: hidden;
  }
  &-tree {
    width: 100%;
    height: 100%;
    overflow: auto;
  }
  &-group {
    width: 100%;
    height: 100%;
    overflow: auto;
  }
}

.identify-info {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
  width: 100%;
  height: 100%;
  padding: 5px;
  border: $border-default;

  &-player {
    z-index: 90;
    width: 100%;
    height: 50vh;
    border: $border-default;
  }
  &-bottom {
    display: flex;
    align-items: center;
    width: 100%;
    height: 31vh;
    border: $border-default;
  }
  &-preset {
    width: 50%;
    height: 100%;
    border-right: $border-default;
  }
  &-result {
    position: relative;
    width: 50%;
    height: 100%;
  }
}

.identify-preset {
  &-player {
    display: flex;
    justify-content: center;
    flex-direction: column;
    padding: 0 20px;
    height: 120px;
    font-size: 14px;
  }
  &-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    height: 4vh;
    padding: 0 20px;
    font-size: 16px;
    border-bottom: $border-default;
  }
  &-label {
    flex: none;
    margin-right: 10px;
    font-size: 16px;
    font-family: BigYoungBoldGB;
  }
  &-point {
    flex: auto;
    min-width: 0;
    font-size: 14px;
    font-weight: 600;
    color: $text-primary;
    @include text-ellipsis(2);
  }
  &-wrapper {
    width: 100%;
    height: calc(100% - 4vh);
  }
  &-list {
    width: 100%;
    height: 100%;
    overflow-y: auto;
  }
  &-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    padding: 5px 20px;
    border-bottom: $border-default;

    &:hover {
      color: $text-primary;
      background-color: $bg-hover;
    }
    &__name {
      flex: auto;
      margin-right: 10px;
    }
  }
}

.identify-result {
  width: 100%;
  height: 100%;

  &-desc {
    margin-bottom: 10px;
    > span {
      font-size: 14px;
    }
    > b {
      margin-top: 1vh;
      margin-left: 10px;
      font-size: 16px;
      font-weight: bold;
      color: $text-primary;
    }
  }
  &-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    height: 4vh;
    padding: 0 20px;
    font-size: 16px;
    border-bottom: $border-default;
  }
  &-label {
    flex: none;
    margin-right: 10px;
    font-size: 16px;
    font-family: BigYoungBoldGB;
  }
  &-value {
    min-width: 0;
    font-size: 14px;
    font-weight: 600;
    color: $text-primary;
    @include text-ellipsis(2);
  }
  &-wrapper {
    width: 100%;
    height: calc(100% - 4vh);
  }
  .res-img {
    height: calc(100% - 38px);
  }
  .res-img-hide {
    visibility: hidden;
  }
}

.identify-history {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
  width: 100%;
  height: 100%;
  padding: 5px;
  border: $border-default;

  &-swiper {
    position: relative;
    width: 100%;
    height: 40vh;
    border: $border-default;
  }

  &-chart {
    width: 100%;
    height: 41vh;
    border: $border-default;
  }
}

.sy-carousel {
  width: 100%;
  height: 100%;

  ::v-deep .el-carousel {
    &__container {
      width: 100%;
      height: 100%;
    }
    &__arrow {
      color: $color-primary;
      font-size: 16px;
      border-radius: 5px;
      border: $border-default;
    }
    &__indicators {
      display: flex;
      align-items: center;
    }
    &__button {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      background-color: $bg-base;

      &.is-active {
        background-color: $bg-hover;
      }
    }
  }
}
</style>
