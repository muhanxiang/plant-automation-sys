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
        <div class="signal-left" v-loading="chartLoad">
          <sy-chart v-if="chartData && chartData.length > 0" ref="chart" type="line" :option="chartOption"></sy-chart>
          <sy-empty v-else-if="!chartLoad"></sy-empty>
        </div>
      </el-col>
      <el-col :span="8" class="sy-col">
        <div class="signal-right">
          <div class="signal-right-item signal-right-time">
            <div class="signal-right-item__title">时间范围</div>
            <div class="signal-right-item__content">
              <el-date-picker
                v-model="startTime"
                type="datetime"
                placeholder="选择开始时间"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                default-time="00:00:00"
                :picker-options="startTimeOptions"
                clearable
                @change="onTimeRangeChange"
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
                @change="onTimeRangeChange"
              >
              </el-date-picker>
            </div>
          </div>
          <div class="signal-right-item">
            <div class="signal-right-item__title">历史值</div>
            <div class="signal-right-item__content">
              <div class="desc-item">
                <div class="desc-item__cell">平均值</div>
                <div class="desc-item__cell value">{{ avgValue }}</div>
              </div>
              <div class="desc-item">
                <div class="desc-item__cell">最大值</div>
                <div class="desc-item__cell value">{{ maxValue }}</div>
              </div>
              <div class="desc-item">
                <div class="desc-item__cell">最小值</div>
                <div class="desc-item__cell value">{{ minValue }}</div>
              </div>
            </div>
          </div>
          <div class="signal-right-item">
            <div class="signal-right-item__title">实时值</div>
            <div class="signal-right-item__content">
              <div class="desc-item">
                <div class="desc-item__cell">当前值</div>
                <div class="desc-item__cell value">{{ curtValue }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  name: 'PatrolPoint',
  ...mapGetters(['stationId']),
  props: {
    guid: {
      type: String,
      default: '',
    },
    title: {
      type: String,
      default: '巡视点位名称',
    },
    visible: {
      type: Boolean,
      default: false,
    },
    precision: {
      type: Number,
      default: 1,
    },
  },
  computed: {
    isVisible: {
      get() {
        return this.visible;
      },
      set(val) {
        this.$emit('update:visible', val);
      },
    },
    valuePrec() {
      return Number.isInteger(this.precision) ? this.precision : 1;
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
    chartOption() {
      return {
        grid: {
          top: '5%',
          bottom: '10%',
        },
        xAxis: {
          axisLabel: {
            interval: 'auto',
          },
        },
        tooltip: {
          axisPointer: { type: 'shadow' },
          formatter: (params) => {
            const item = params[0];
            return `
                <div>${this.$dayjs(item.data.time).format('YYYY-MM-DD HH:mm:ss')}</div>
                <div style="margin-top: 10px">
                  <span style="margin-right: 20px">${item.marker}${item.seriesName}</span>
                  <span>${item.data.value_disp || '-'}</span>
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
          source: this.chartData.map((item) => ({
            name: this.$dayjs(item.patrol_time).format('YYYY-MM-DD\nHH:mm:ss'),
            time: item.patrol_time,
            value: +item.value,
            value_disp: item.value_disp || '-',
          })),
        },
        series: [
          {
            type: 'line',
            name: '巡视点位值',
            symbol: 'circle',
            symbolSize: 5,
            smooth: true,
            areaStyle: {
              opacity: 0.5,
            },
            itemStyle: {
              normal: {
                lineStyle: {
                  width: 1,
                },
              },
            },
            encode: {
              x: 'name',
              y: 'value',
            },
          },
        ],
      };
    },
  },
  data() {
    return {
      chartLoad: false,
      chartData: [],
      startTime: '',
      endTime: '',
      avgValue: '',
      maxValue: '',
      minValue: '',
      curtValue: '',
    };
  },
  methods: {
    onTimeRangeChange() {
      this.getHistoryData();
    },
    onDialogOpen() {
      this.getHistoryData();
    },
    onDialogClose() {
      this.chartData = [];
      this.startTime = '';
      this.endTime = '';
      this.avgValue = '';
      this.maxValue = '';
      this.minValue = '';
      this.curtValue = '';
    },
    getHistoryData() {
      if (!this.guid) return;

      this.chartLoad = true;
      this.$api
        .postHistoryApi('query', {
          index: 'history.item',
          from: 0,
          size: 100,
          match: {
            station_id: this.stationId,
            patrolpoint_id: this.guid || undefined,
          },
          order: { patrol_time: 'desc' },
          range: {
            patrol_time: this.startTime || this.endTime ? `${this.startTime || ''},${this.endTime || ''}` : undefined,
          },
        })
        .then((res) => {
          if (res?.data?.length > 0) {
            this.chartData = res.data.reverse();

            const patrolpoint = res.data[0];
            if (/\d+/.test(patrolpoint.value_disp)) {
              // const unitVal = patrolpoint.value_disp.match(/[-\d.]+(.*)$/)[1];
              const unitVal = patrolpoint.unit.match(/\$(.*)/)[1];

              let maxVal,
                minVal,
                totalVal = 0;
              const length = res.data.length;
              for (let index = 0; index < length; index++) {
                const { value } = res.data[index];
                const numVal = +value || 0;
                // console.log('numVal: ', numVal);
                totalVal += numVal;
                if (maxVal === undefined || maxVal < numVal) maxVal = numVal;
                if (minVal === undefined || minVal > numVal) minVal = numVal;
              }
              this.maxValue = `${maxVal.toFixed(this.valuePrec)}${unitVal}`;
              this.minValue = `${minVal.toFixed(this.valuePrec)}${unitVal}`;
              this.avgValue = `${(totalVal / length).toFixed(this.valuePrec)}${unitVal}`;
              try {
                this.curtValue = `${(+res.data[0].value).toFixed(this.valuePrec)}${unitVal}`;
              } catch (error) {
                this.curtValue = patrolpoint.value_disp;
              }
            } else {
              this.avgValue = '-';
              this.maxValue = '-';
              this.minValue = '-';
              this.curtValue = patrolpoint.value_disp;
            }
          } else {
            this.chartData = [];
            this.avgValue = '-';
            this.maxValue = '-';
            this.minValue = '-';
            this.curtValue = '-';
          }
        })
        .finally(() => {
          this.chartLoad = false;
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.signal {
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
      align-items: center;
      flex-direction: column;
      justify-content: center;

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
      &__content {
        display: flex;
        align-items: center;
        flex-direction: column;
        justify-content: center;
        flex: auto;
        width: 100%;

        > .el-date-editor {
          width: 250px;

          + .el-date-editor {
            margin-top: 2vh;
          }
        }

        .desc {
          &-item {
            display: flex;
            width: 100%;
            &__cell {
              color: $text-secondary;
              border-bottom: $border-default;
              width: 50%;
              height: 6vh;
              line-height: 6vh;
              text-align: center;
              &:first-of-type {
                border-right: $border-default;
              }
            }
            .value {
              color: $color-white;
            }
            .status {
              color: $color-primary;
            }
          }
        }
      }
    }
    &-time {
      flex: 1;
    }
  }
}
</style>
