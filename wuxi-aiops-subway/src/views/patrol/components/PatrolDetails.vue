<template>
  <el-dialog
    custom-class="sy-dialog sy-dialog--medium nofooter"
    :visible.sync="isVisible"
    :title="title"
    center
    @open="handleOpen"
  >
    <el-row class="sy-row patrol-details">
      <el-col :span="14" class="sy-col left">
        <FileViewer :data="currHistory">
          <span slot="info">
            {{ `${currHistory.patrolpoint_name}: ${currHistory.value_disp || '-'}` }}
          </span>
        </FileViewer>
      </el-col>
      <el-col :span="10" class="sy-col right">
        <div class="right-title">巡视信息</div>
        <div class="right-item">
          <div class="right-label"><sy-icon name="point" class="icon"></sy-icon></div>
          <div class="text-over right-value">
            <label>巡视点位：</label
            ><span :title="currHistory.patrolpoint_name">{{ currHistory.patrolpoint_name }}</span>
          </div>
        </div>
        <div class="right-item">
          <div class="right-label"><sy-icon name="camera" class="icon"></sy-icon></div>
          <div class="text-over right-value">
            <label>巡视设备：</label><span :title="currHistory.patrolDevice">{{ currHistory.patrolDevice }}</span>
          </div>
        </div>
        <div class="right-item">
          <div class="right-label"><sy-icon name="trend" class="icon"></sy-icon></div>
          <div class="text-over right-value">
            <label>巡视结果：</label>
            <span :title="currHistory.patrolResult">{{ currHistory.patrolResult }}</span>
          </div>
        </div>
        <div class="right-item">
          <div class="right-label"><sy-icon name="warning" class="icon"></sy-icon></div>
          <div class="text-over right-value">
            <label>巡视结论：</label
            ><span :class="`sy-status-${currHistory.patrol_status}`" :title="currHistory.patrolStatus">{{
              currHistory.patrolStatus
            }}</span>
          </div>
        </div>
        <div class="right-title">历史曲线</div>
        <div class="right-chart">
          <sy-chart
            ref="chart"
            v-if="historyData && historyData.length > 0"
            type="line"
            :option="historyOption"
            @zrclick="handleChartClick"
          ></sy-chart>
          <sy-empty v-else></sy-empty>
        </div>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import { getImgFullPath, arrToMap } from '@/utils';
import Option from '@/enums/option';
import FileViewer from '@/components/FileViewer';

export default {
  name: 'PatrolDetails',
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
    alarmLevelMap() {
      return arrToMap(Option.AlarmLevels);
    },
    patrolStateMap() {
      return arrToMap(Option.PatrolStates);
    },
    imgUrl() {
      return getImgFullPath(this.data.file_path);
    },
    historyOption() {
      return {
        grid: {
          bottom: '10%',
        },
        xAxis: {
          inverse: true,
          axisLabel: {
            inverse: 'auto',
          },
        },
        tooltip: {
          axisPointer: { type: 'shadow' },
          formatter: (params) => {
            const item = params[0];
            return `
                <div>${this.$dayjs(item.data.patrol_time).format('YYYY-MM-DD HH:mm:ss')}</div>
                <div style="margin-top: 10px">
                  <span style="margin-right: 20px">${item.marker}${item.seriesName}</span>
                  <span>${item.data.value}</span>
                </div>
            `;
          },
        },
        dataZoom: [
          {
            type: 'inside',
            start: 0,
            end: 75,
            minSpan: 25,
            maxSpan: 100,
          },
          {
            type: 'slider',
            left: '5%',
            right: '5%',
            bottom: '3%',
            height: '5%',
            start: 0,
            end: 75,
            minSpan: 25,
            maxSpan: 100,
            textStyle: {
              overflow: 'break',
            },
          },
        ],
        dataset: {
          source: [...this.historyData].map((item) => ({
            name: this.$dayjs(item.patrol_time).format('YYYY-MM-DD\nHH:mm:ss'),
            value: item.value ?? '-',
            value_disp: item.value_disp ?? '-',
            patrol_time: item.patrol_time,
            recognition_type: item.recognition_type,
          })),
        },
        series: [
          {
            type: 'line',
            name: '巡视结果',
            symbol: 'circle',
            symbolSize: 5,
            smooth: true,
            areaStyle: {
              opacity: 0.5,
            },
            itemStyle: {
              normal: {
                lineStyle: {
                  width: 1, //设置线条粗细
                },
              },
            },
          },
        ],
      };
    },
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    title: {
      type: String,
      default: '巡视结果详情',
    },
    data: {
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {
      maxValue: '',
      minValue: '',
      currHistory: {},
      historyData: [],
    };
  },
  methods: {
    handleChartClick({ dataIndex }) {
      console.log(dataIndex);
      this.currHistory = this.historyData[dataIndex];
    },
    async handleOpen() {
      // 获取任务历史
      const params = {
        index: 'history.item',
        from: 0,
        size: 10,
        match: {
          patrolpoint_id: this.data.patrolpoint_id,
          camera_id: this.data?.camera_id || undefined,
          robot_id: this.data?.robot_id || undefined,
          host_id: this.data?.host_id || undefined,
        },
        order: { patrol_time: 'desc' },
      };
      const { alarm, host_name, robot_name, camera_name, value, value_disp, patrol_status } = this.data;
      // 如果是历史记录告警取记录中的，推送 告警取 value_disp
      const patrolAlarm = alarm
        ? alarm.map((item) => `${item.name}(${this.alarmLevelMap[item.level]})`).join(' ')
        : '-';
      this.currHistory = {
        ...this.data,
        patrolAlarm,
        patrolResult: value_disp || value || '-',
        patrolStatus: this.patrolStateMap[patrol_status] || '-',
        patrolDevice: camera_name || robot_name || host_name || '-',
      };

      const res = await this.$api.postHistoryApi('query', params);
      this.historyData = res.data
        ? res.data.map((item) => {
            const patrolAlarm = item.alarm
              ? item.alarm.map((item) => `${item.name}(${this.alarmLevelMap[item.level]})`).join(';')
              : '-';
            return {
              ...item,
              patrolAlarm,
              patrolResult: item.value_disp || item.value || '-',
              patrolStatus: this.patrolStateMap[item.patrol_status] || '-',
              patrolDevice: item.camera_name || item.robot_name || item.host_name || '-',
            };
          })
        : [];
    },
    // 选择区域
    onRegionSelect(res) {
      this.maxValue = res.maxTemp;
      this.minValue = res.minTemp;
    },
  },
};
</script>

<style lang="scss" scoped>
.patrol-details {
  border: $border-default;

  .left {
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
  }
  .right {
    border-left: $border-default;
    display: flex;
    flex-direction: column;

    .right-title {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 100%;
      height: 4vh;
      font-size: 20px;
      font-weight: 600;
      color: $text-primary;
      background: $bg-header;
    }
    .right-item {
      display: flex;
      align-items: center;
      border-bottom: $border-default;

      .right-label {
        flex: none;
        display: flex;
        align-items: center;
        padding: 0 10px;

        .sy-icon {
          font-size: 20px;
        }
      }
      .right-value {
        flex: auto;
        height: 4vh;
        line-height: 4vh;
        padding-left: 10px;
        color: $text-secondary;
        border-left: $border-default;
        @include text-ellipsis;

        > label {
          color: $text-secondary;
        }
      }
      .warning-class {
        color: $color-danger;
      }
      .success-class {
        color: $color-success;
      }
    }
    .right-chart {
      flex: auto;
      padding: 10px;
    }
  }
}
</style>
