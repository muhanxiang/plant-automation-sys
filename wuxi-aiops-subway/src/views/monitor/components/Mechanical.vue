<template>
  <el-dialog
    custom-class="sy-dialog sy-dialog--medium"
    :title="title"
    :visible.sync="isVisible"
    center
    width="500"
    @open="onDialogOpen"
    @close="onDialogClose"
  >
    <el-row class="sy-row">
      <el-col :span="16" class="sy-col">
        <div class="machine-left" v-loading="chartLoad">
          <sy-chart v-if="chartData.length > 0" ref="chart" type="line" :option="chartOption"></sy-chart>
          <sy-empty v-else-if="!chartLoad"></sy-empty>
        </div>
      </el-col>
      <el-col :span="8" class="sy-col">
        <div class="machine-right">
          <div class="machine-right-item">
            <div class="machine-right-item__title">时间范围</div>
            <div class="machine-right-item__content">
              <el-date-picker
                v-model="startTime"
                type="datetime"
                placeholder="选择开始时间"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                default-time="00:00:00"
                :picker-options="startTimeOptions"
                clearable
                @change="handleTimeChange"
              >
              </el-date-picker>
              <el-date-picker
                v-model="endTime"
                type="datetime"
                placeholder="选择结束时间"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                default-time="23:59:59"
                :picker-options="endTimeOptions"
                clearable
                @change="handleTimeChange"
              >
              </el-date-picker>
            </div>
          </div>
          <div class="machine-right-item action-time">
            <div class="machine-right-item__title">动作时间</div>
            <ul class="machine-right-item__list">
              <li
                :class="`desc-item ${item.file_path === filePath ? 'is-active' : ''}`"
                v-for="item in fileList"
                :key="item.file_path"
                @click="handleFileChange(item)"
              >
                {{ item.time | formatDateTime }}
              </li>
            </ul>
          </div>
          <div class="machine-right-item">
            <div class="machine-right-item__title">波形类型</div>
            <ul class="machine-right-item__content">
              <li
                v-for="item in chartTypes"
                :key="item.key"
                :class="`desc-item ${item.key === chartType ? 'is-active' : ''}`"
                @click="handleChartChange(item.key)"
              >
                {{ item.value }}
              </li>
            </ul>
          </div>
        </div>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
// 闸刀状态
const ChartType = {
  BREAKER_CLOSE: 66,
  BREAKER_OPEN: 67,
  MOTOR_CURRENT: 68,
};

export default {
  name: 'Mechanical',
  computed: {
    isVisible: {
      get() {
        return this.visible;
      },
      set(val) {
        this.$emit('update:visible', val);
      },
    },
    startTimeOptions() {
      return {
        disabledDate: (date) => {
          if (!this.endTime && this.$dayjs().isAfter(date, 'minute')) return false;
          if (
            this.endTime &&
            this.$dayjs(date).isBefore(this.endTime, 'minute') &&
            this.$dayjs().isAfter(date, 'minute')
          )
            return false;
          return true;
        },
      };
    },
    endTimeOptions() {
      return {
        disabledDate: (date) => {
          if (!this.startTime && this.$dayjs().isAfter(date, 'minute')) return false;
          if (
            this.startTime &&
            this.$dayjs(date).isAfter(this.startTime, 'minute') &&
            this.$dayjs().isAfter(date, 'minute')
          )
            return false;
          return true;
        },
      };
    },
    chartTypes() {
      return [
        { key: 66, value: '断路器合闸波形' },
        { key: 67, value: '断路器分闸波形' },
        { key: 68, value: '储能电机电流波形' },
      ];
    },
    chartOption() {
      let timeData = [];
      let maxVal = 0;
      if (this.chartData?.length > 0) {
        timeData = this.chartData.map((_, index) => index);

        maxVal = Math.max.apply(null, this.chartData);
      }
      return {
        grid: {
          top: '50px',
          left: 0,
          right: '10%',
          bottom: '6%',
          containLabel: true,
        },
        xAxis: {
          type: 'category',
          name: '时间/0.1毫秒',
          nameGap: 15,
          nameLocation: 'end',
          nameTextStyle: {
            color: '#ffffff',
            fontSize: 16,
            verticalAlign: 'top',
            align: 'right',
            lineHeight: 70,
          },
          axisLabel: {
            interval: 'auto',
          },
          data: timeData,
        },
        yAxis: {
          type: 'value',
          name: '电流值大小(单位: A)',
          nameGap: 35,
          nameLocation: 'end',
          nameTextStyle: {
            color: '#ffffff',
            fontSize: 16,
            width: 200,
            align: 'left',
            verticalAlign: 'middle',
          },
        },
        visualMap: {
          type: 'continuous',
          bottom: '6%',
          right: '10px',
          max: maxVal,
          splitNumber: 5,
          itemWidth: 15,
          itemHieght: 15,
          textGap: 5,
          textStyle: {
            color: '#fff',
            fontSize: 10,
          },
          inRange: {
            color: ['#93CE07', '#FBDB0F', '#FC7D02', '#FD0100', '#AA069F', '#AC3B2A'],
          },
          precision: 3,
        },
        dataZoom: [
          {
            show: false,
            type: 'slider',
            handleSize: '100%',
            filterMode: 'filter',
            textStyle: {
              overflow: 'break',
            },
          },
        ],
        series: [
          {
            type: 'line',
            smooth: 1,
            data: this.chartData,
            sampling: 'lttb',
            markLine: {
              lineStyle: {
                type: 'solid',
                color: '#FD0100',
              },
              label: { position: 'middle', color: '##FD0100', fontWeight: 'bold', fontSize: 16 },
              data: [
                {
                  name: '最大值',
                  type: 'max',
                },
              ],
            },
            markPoint: {
              itemStyle: { color: '#FD0100' },
              data: this.peeksData.map((item, index) => ({
                coord: [item, this.chartData[item]],
                value: this.peeksNames[index],
              })),
            },
          },
        ],
      };
    },
  },
  props: {
    guid: {
      type: String,
      default: '',
    },
    title: {
      type: String,
      default: '机械特性名称',
    },
    visible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      startTime: '',
      endTime: '',
      filePath: '',
      fileList: [],
      chartLoad: false,
      chartType: ChartType.BREAKER_CLOSE,
      chartData: [],
      peeksData: [],
      peeksNames: [],
      actionNames: [],
      actionWaves: [],
      actionPeeks: [],
      energyNames: [],
      energyWaves: [],
      energyPeeks: [],
    };
  },
  methods: {
    handleTimeChange() {
      this.getHistoryData();
    },
    handleFileChange(item) {
      this.filePath = item.file_path;
      this.getWaveformData();
    },
    handleChartChange(type) {
      if (this.chartType !== type) {
        this.chartType = type;
        this.getHistoryData();

        // switch (this.chartType) {
        //   case ChartType.BREAKER_OPEN:
        //   case ChartType.BREAKER_CLOSE:
        //     this.chartData = this.actionWaves;
        //     this.peeksData = this.actionPeeks;
        //     break;
        //   case ChartType.MOTOR_CURRENT:
        //   default:
        //     this.chartData = this.energyWaves;
        //     this.peeksData = this.energyPeeks;
        //     break;
        // }
      }
    },
    onDialogOpen() {
      this.getHistoryData();
    },
    onDialogClose() {
      this.chartType = ChartType.BREAKER_CLOSE;
      this.startTime = '';
      this.endTime = '';
      this.filePath = '';
      this.fileList = [];
      this.chartData = [];
      this.peeksData = [];
      this.peeksNames = [];
      this.isVisible = false;
    },
    async getHistoryData() {
      if (!this.guid) return;

      this.chartLoad = true;
      this.$api
        .getMeasureApi('query/mfvalue', {
          id: this.guid,
          start: this.startTime || undefined,
          end: this.endTime || undefined,
          flag: this.chartType === ChartType.MOTOR_CURRENT ? ChartType.BREAKER_CLOSE : this.chartType,
          is_valid: true,
          samples: 500,
        })
        .then((res) => {
          if (res?.results?.length > 0) {
            this.fileList = res.results;
            this.filePath = this.fileList[0].file_path;
            this.getWaveformData();
          } else {
            this.filePath = '';
            this.fileList = [];
            this.chartData = [];
            this.peeksData = [];
            this.peeksNames = [];
          }
        })
        .finally(() => {
          this.chartLoad = false;
        });
    },
    getWaveformData() {
      if (!this.guid || !this.filePath) {
        this.chartData = [];
        this.peeksData = [];
        this.peeksNames = [];
        return;
      }
      this.chartLoad = true;
      this.$api
        .getMechanicalApi('analysis', { path: `data/${this.filePath}` })
        .then((res) => {
          if (!res) return;
          this.actionNames = res.current_action_peak_names || [];
          this.actionWaves = res.current_action_wave || [];
          this.actionPeeks = res.current_action_peaks || [];
          this.energyNames = res.current_energy_peak_names || [];
          this.energyWaves = res.current_energy_wave || [];
          this.energyPeeks = res.current_energy_peaks || [];

          switch (this.chartType) {
            case ChartType.BREAKER_OPEN:
            case ChartType.BREAKER_CLOSE:
              this.chartData = this.actionWaves;
              this.peeksData = this.actionPeeks;
              this.peeksNames = this.actionNames;
              break;
            case ChartType.MOTOR_CURRENT:
            default:
              this.chartData = this.energyWaves;
              this.peeksData = this.energyPeeks;
              this.peeksNames = this.energyNames;
              break;
          }
        })
        .catch(() => {
          this.chartData = [];
          this.peeksData = [];
          this.peeksNames = [];
        })
        .finally(() => {
          this.chartLoad = false;
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.machine {
  &-left {
    height: 100%;
    border: $border-default;
    padding-top: 10px;
  }
  &-right {
    display: flex;
    flex-direction: column;
    height: 100%;
    border: $border-default;

    &-item {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      flex-direction: column;

      &__title {
        width: 100%;
        height: 5vh;
        font-size: 18px;
        font-weight: 600;
        line-height: 5vh;
        text-align: center;
        color: $color-primary;
        background-color: $bg-label;
      }
      &__list {
        flex: auto;
        width: 100%;
        overflow: auto;
      }
      &__content {
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        width: 100%;

        > .el-date-editor {
          width: 250px;
          margin: 1vh 0;
        }
      }
      &__list,
      &__content {
        .desc {
          &-item {
            flex: none;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 4vh;
            width: 100%;
            border-bottom: $border-default;
            cursor: pointer;

            &:last-of-type {
              border-bottom: none;
            }
            &:hover {
              color: $text-secondary;
              background-color: $bg-hover1;
            }
            &.is-active {
              color: $text-secondary;
              background-color: $bg-hover2;
            }
          }
        }
      }
    }
    .action-time {
      flex: 1;
      overflow: auto;
    }
  }
}
</style>
