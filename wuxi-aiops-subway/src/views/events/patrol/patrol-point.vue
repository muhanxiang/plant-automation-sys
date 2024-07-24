<template>
  <el-row id="patrol_point" class="sy-row" :gutter="10">
    <el-col class="sy-col-vertical" :span="6">
      <div class="point-aside sy-view">
        <div class="point-aside-filter">
          <el-input
            v-model.trim="pointName"
            size="small"
            suffix-icon="el-icon-search"
            placeholder="请输入关键词搜索"
            :maxlength="20"
            clearable
            @input="onPointSearch"
          ></el-input>
          <el-button class="sy-btn-regular" type="primary" size="medium" @click="handleResetTree">重置</el-button>
        </div>
        <sy-tree
          v-show="!pointSearch"
          class="point-aside-tree"
          ref="tree"
          node-key="guid"
          :data="treeData"
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
        <div v-show="pointSearch" v-loading="pointLoad" class="point-aside-group">
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
    </el-col>
    <el-col class="sy-col-vertical" :span="18">
      <div v-loading="historyLoad" class="point-history sy-view">
        <div class="sy-title">历史曲线</div>
        <el-form class="point-history-form" :model="searchForm" ref="form" size="medium" inline>
          <el-form-item>
            <el-checkbox class="sy-checkbox" v-model="searchForm.threePhase" @change="getPointData"
              >三相对比
            </el-checkbox>
          </el-form-item>
          <el-form-item label="巡视时间:">
            <el-date-picker
              class="sy-date-picker"
              v-model="searchForm.timeRange"
              type="datetimerange"
              size="small"
              popper-class="sy-popper"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              :default-time="['00:00:00', '23:59:59']"
              :picker-options="pickerOptions"
              @change="getPointData"
            >
            </el-date-picker>
          </el-form-item>
        </el-form>
        <div class="point-history-chart">
          <sy-chart
            v-if="existData"
            class="chart"
            type="line"
            :option="chartOption"
            @click="handleChartClick"
            @zrclick="handleZRenderClick"
          ></sy-chart>
          <sy-empty v-else description="暂无历史数据"></sy-empty>
        </div>
      </div>

      <div class="point-result sy-view">
        <div class="sy-title">识别结果</div>
        <el-row class="point-result-wrapper" :gutter="10">
          <el-col class="point-result-info" :span="12">
            <ul class="point-result-info__list">
              <li class="info-item">任务名称：{{ currHistory.task_name || '-' }}</li>
              <li class="info-item">设备名称：{{ currHistory.device_name || '-' }}</li>
              <li class="info-item">部件名称：{{ currHistory.component_name || '-' }}</li>
              <li class="info-item">巡视点位：{{ currHistory.patrolpoint_name || '-' }}</li>
              <li class="info-item">巡检设备：{{ currHistory.patrolDevice }}</li>
              <li class="info-item">巡视结果：{{ currHistory.patrolResult }}</li>
              <li class="info-item">巡视结论：{{ currHistory.patrolStatus }}</li>
              <li class="info-item">巡检时间：{{ currHistory.patrol_time || '-' }}</li>
            </ul>
          </el-col>
          <el-col class="point-result-viewer" :span="12">
            <FileViewer :data="currHistory">
              <span slot="info">
                {{ `${currHistory.patrolpoint_name}: ${currHistory.value_disp || '-'}` }}
              </span>
            </FileViewer>
          </el-col>
        </el-row>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import { mapGetters } from 'vuex';
import { debounce, has } from 'lodash';
import { CustomPatrolTypes } from '@/enums';
import { PhaseColorMap } from '@/enums/color';
import { arrToMap, isEmptyObject } from '@/utils';
import Option from '@/enums/option';
import PointTree from '@/mixins/point-tree';
import DatePicker from '@/mixins/date-picker';
import FileViewer from '@/components/FileViewer';

export default {
  name: 'PatrolPoint',
  mixins: [PointTree, DatePicker],
  components: {
    FileViewer,
  },
  computed: {
    ...mapGetters(['stationId']),
    patrolStateMap() {
      return arrToMap(Option.PatrolStates);
    },
    pointSearch() {
      return this.pointName.length > 0;
    },
    existData() {
      return (
        !isEmptyObject(this.historyData) &&
        !!(this.historyData['value'] || this.historyData['A'] || this.historyData['B'] || this.historyData['C'])
      );
    },
    exisThreePhase() {
      return has(this.historyData, 'A') || has(this.historyData, 'B') || has(this.historyData, 'C');
    },
    chartOption() {
      const times = Array.from(
        new Set(
          Object.keys(this.historyData)
            .map((item) => this.historyData[item])
            .filter((item) => !!item)
            .reduce((pre, next) => (pre.push(...next.map((item) => item.patrol_time)), pre), [])
        )
      ).sort((pre, next) => this.$dayjs(next).diff(pre));
      const onePhase = has(this.historyData, 'value');
      return {
        grid: {
          bottom: '15%',
        },
        xAxis: {
          data: times,
          inverse: true,
          axisTick: {
            show: true,
          },
          axisLabel: {
            interval: 'auto',
            fontSize: 14,
            formatter: (value) => value.replace(' ', '\n'),
          },
        },
        yAxis: {
          show: true,
        },
        legend: {
          top: '5%',
          itemWidth: 20,
          itemHeight: 20,
          textStyle: {
            fontSize: 18,
          },
          tooltip: {
            formatter: (params) => {
              let str = '';
              params.forEach(
                (item) =>
                  (str = `${str}
                <div>${this.$dayjs(item.data.patrol_time).format('YYYY-MM-DD HH:mm:ss')}</div>
                <div style="margin-top: 5px">
                  <span style="margin-right: 10px">${item.marker}${item.seriesName}</span>
                  <span>${item.data.value}</span>
                </div>
            `)
              );
              return str;
            },
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
            height: '5%',
            bottom: '5%',
            start: 0,
            end: 75,
            minSpan: 25,
            maxSpan: 100,
            textStyle: {
              overflow: 'break',
            },
          },
        ],
        series: Object.keys(this.historyData).map((item) => {
          return {
            id: item,
            type: 'line',
            name: onePhase ? '巡视结果' : `${item}相`,
            symbol: 'circle',
            symbolSize: 10,
            smooth: true,
            connectNulls: true,
            itemStyle: {
              color: PhaseColorMap[item],
            },
            areaStyle: this.exisThreePhase
              ? {
                  opacity: 0.5,
                  color: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                      {
                        offset: 0,
                        color: `${PhaseColorMap[item]}ff`, // 0% 处的颜色
                      },
                      {
                        offset: 1,
                        color: `${PhaseColorMap[item]}00`, // 100% 处的颜色
                      },
                    ],
                  },
                }
              : {
                  opacity: 0.5,
                  color: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                      {
                        offset: 0,
                        color: 'rgba(73,146,255,1)',
                      },
                      {
                        offset: 1,
                        color: 'rgba(0,0,0,1)',
                      },
                    ],
                  },
                },
            data: times.map((time) => {
              if (!this.historyData[item]) return '-';
              const data = this.historyData[item].find((historyItem) => historyItem.patrol_time === time);
              return data
                ? {
                    ...data,
                    name: this.$dayjs(data.patrol_time).format('MM/DD HH:mm:ss'),
                    value: data.value ?? '-',
                    patrolResult: data.value_disp || data.value || '-',
                    patrolStatus: this.patrolStateMap[data.patrol_status] || '-',
                    patrolDevice: data.camera_name || data.host_name || data.robot_name || '-',
                  }
                : null;
            }),
          };
        }),
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
      isShortcuts: true,
      historyLoad: false,
      pointLoad: false,
      pointList: [],
      pointName: '',
      searchForm: {
        threePhase: false,
        timeRange: [],
      },
      historyData: {},
      currHistory: {},
      currPointId: '',
      currPointName: '',
    };
  },
  mounted() {
    this.getPointHistoryData();
  },
  methods: {
    handleResetTree() {
      this.pointName = '';
      this.pointList = [];
      this.currPointId = undefined;
      this.$refs.tree.setCheckedKeys([]);
    },
    handleChartClick({ seriesId, dataIndex }) {
      this.currHistory = this.historyData[seriesId][dataIndex];
    },
    // 非三项对比时监听图表'zrclick'事件否则监听图表'click'事件
    handleZRenderClick({ dataIndex, target }) {
      // 点不处理
      if (target && target.constructor.name === 'Sub') return;

      if (this.exisThreePhase) {
        const [seriesA, seriesB, seriesC] = this.chartOption.series;
        this.currHistory =
          seriesA && seriesA.data[dataIndex] !== 0
            ? seriesA.data[dataIndex]
            : seriesB && seriesB.data[dataIndex] !== 0
            ? seriesB.data[dataIndex]
            : seriesC && seriesC.data[dataIndex] !== 0
            ? seriesC.data[dataIndex]
            : {};
      } else {
        let historyItem =
          this.historyData['A'] || this.historyData['B'] || this.historyData['C'] || this.historyData['value'] || null;
        this.currHistory = historyItem ? historyItem[dataIndex] : {};
      }
    },
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
    onPointChange(item) {
      // this.currPointId = item.guid;
      this.currPointName = item.name;
      this.getPointData();
    },
    onNodeCheckChange(data, checked) {
      // console.log('onNodeCheckChange: ', data, checked);
      if (checked) {
        this.$refs.tree.setCheckedKeys([data.guid]);

        this.currPointId = data.guid;
        this.currPointName = data.name;
        this.getPointData();
      } else {
        const checkedKeys = this.$refs.tree.getCheckedKeys();
        if (checkedKeys.length === 0) {
          this.historyData = {};
          this.currHistory = {};
          this.currPointId = '';
          this.currPointName = '';
        }
      }
    },
    getPointData() {
      if (this.searchForm.threePhase) {
        this.getPointTriphaseData();
      } else {
        this.getPointHistoryData();
      }
    },
    // 点位巡视历史数据
    async getPointHistoryData() {
      this.historyLoad = true;
      this.currHistory = {};
      this.historyData = {};

      const { timeRange } = this.searchForm;
      if (!this.currPointId) {
        const result = await this.$api.postHistoryApi('query', {
          index: 'history.item',
          from: 0,
          size: 1,
          match: { station_id: this.stationId },
          mustnot: { patrol_type: CustomPatrolTypes },
          order: { patrol_time: 'desc' },
          range: {
            patrol_time: timeRange?.length > 0 ? timeRange.join(',') : undefined,
          },
        });
        if (result?.data?.length > 0) {
          this.currPointId = result.data[0].patrolpoint_id;
          this.currPointName = result.data[0].patrolpoint_name;
        }
      }
      const history = await this.$api.postHistoryApi('query', {
        index: 'history.item',
        from: 0,
        size: 20,
        match: {
          station_id: this.stationId,
          patrolpoint_id: this.currPointId || undefined,
        },
        mustnot: { patrol_type: CustomPatrolTypes },
        order: { patrol_time: 'desc' },
        range: {
          patrol_time: timeRange?.length > 0 ? timeRange.join(',') : undefined,
        },
      });
      if (history?.data?.length > 0) {
        const historyData = history.data.map((item) => ({
          ...item,
          patrolResult: item.value_disp || item.value || '-',
          patrolStatus: this.patrolStateMap[item.patrol_status] || '-',
          patrolDevice: item.camera_name || item.robot_name || item.host_name || '-',
        }));
        this.$set(this.historyData, 'value', historyData);

        this.currHistory = this.historyData['value'][0];
      }
      this.historyLoad = false;
    },
    // 点位三相对比数据
    async getPointTriphaseData() {
      this.historyLoad = true;

      this.currHistory = {};
      this.historyData = {};
      const { timeRange } = this.searchForm;
      if (!this.currPointName) {
        const result = await this.$api.postHistoryApi('query', {
          index: 'history.item',
          from: 0,
          size: 1,
          match: { station_id: this.stationId },
          mustnot: { patrol_type: CustomPatrolTypes },
          order: { patrol_time: 'desc' },
          range: {
            patrol_time: timeRange?.length > 0 ? timeRange.join(',') : undefined,
          },
        });
        if (result?.data?.length > 0) {
          this.currPointId = result.data[0].patrolpoint_id;
          this.currPointName = result.data[0].patrolpoint_name;
        }
      }
      const triphase = await this.$api.postHistoryApi('triphaseSearch', {
        from: 0,
        size: 20,
        match: {
          station_id: this.stationId,
          patrolpoint_name: this.currPointName,
        },
        mustnot: { patrol_type: CustomPatrolTypes },
        order: { patrol_time: 'desc' },
        range: {
          patrol_time: timeRange?.length > 0 ? timeRange.join(',') : undefined,
        },
      });
      if (!isEmptyObject(triphase?.data)) {
        Object.entries(triphase.data).forEach(([key, val]) => {
          if (val) {
            const arr = val.map((item) => ({
              ...item,
              patrolResult: item.value_disp || item.value || '-',
              patrolStatus: this.patrolStateMap[item.patrol_status] || '-',
              patrolDevice: item.camera_name || item.robot_name || item.host_name || '-',
            }));
            this.$set(this.historyData, key, arr);
          }
        });
        let historyItem =
          this.historyData['A'] || this.historyData['B'] || this.historyData['C'] || this.historyData['value'] || null;
        this.currHistory = historyItem ? historyItem[0] : {};
      }
      this.historyLoad = false;
    },
  },
};
</script>

<style lang="scss" scoped>
.point-aside {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  padding: 10px;

  &-filter {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;

    .el-input {
      flex: auto;
      margin-right: 10px;
    }
  }
  &-tree {
    flex: auto;
    width: 100%;
    height: 72vh;
    margin-top: 1vh;
    overflow: auto;

    &-item {
      width: 100%;
      height: 3vh;
      margin-top: 1vh;
      line-height: 3vh;
      color: $text-secondary;
      font-size: 14px;
      font-weight: bold;

      &:hover {
        color: $text-primary;
        background-color: $bg-base5;
      }
    }
  }
  &-group {
    width: 100%;
    color: $text-secondary;
    flex: 1;
    overflow: auto;
    margin-top: 1vh;
  }
  &-list {
    height: 100%;
    overflow-y: auto;

    .sy-ignore {
      height: 40px;
    }
  }

  &-item {
    display: flex;
    padding: 0 10px;
    align-items: center;
    cursor: pointer;

    > label {
      flex: auto;
      @include text-ellipsis();
    }
    .checked {
      color: $text-primary;
      background: $bg-hover3;

      .point-aside-item__icon {
        font-size: 20px;
      }
    }
    ::v-deep .vue-recycle-scroller__item-view.hover {
      background: $bg-hover5;
    }
  }
}
.point-history {
  position: relative;
  width: 100%;
  height: 40vh;
  color: $color-info;
  border-bottom: $border-default;

  &-form {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    width: 100%;
    height: 5vh;

    .el-form-item {
      margin-right: 20px;
      margin-bottom: 0;
    }
  }
  &-chart {
    width: 100%;
    height: 35vh;
    border: $border-default;
  }
}
.point-result {
  position: relative;
  width: 100%;
  height: 39vh;
  padding: 10px;

  &-wrapper {
    display: flex;
    align-items: center;
    width: 100%;
    height: 100%;
  }
  &-info {
    height: 100%;
    padding-top: 3vh;
    font-size: 14px;
    color: $text-primary;

    &__list {
      display: flex;
      flex-direction: column;
      width: 100%;
      height: 100%;
      border: $border-default;

      > li {
        flex: 1;
        display: flex;
        align-items: center;
        padding: 0 20px;
        @include text-ellipsis();

        &:nth-child(odd) {
          background-color: $bg-label;
        }
      }
    }
  }
  &-viewer {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;

    .file-viewer {
      border: $border-default;
    }
  }
}
</style>
