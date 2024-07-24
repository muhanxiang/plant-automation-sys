<template>
  <el-row id="patrol_details" class="sy-row" :gutter="10">
    <el-col class="sy-col-vertical" :span="16">
      <ul class="patrol-info-list sy-view">
        <li class="patrol-info-item" v-for="(item, index) in taskDataList" :key="index">
          <label>{{ item.label }}</label>
          <span>{{ item.value }}</span>
        </li>
      </ul>

      <div class="patrol-table">
        <el-form class="patrol-table__form" :model="deviceForm" inline size="mini">
          <el-form-item label="间隔名称:">
            <el-input
              v-model="deviceForm.bayName"
              placeholder="请输入间隔名称"
              size="small"
              clearable
              :maxlength="20"
              @change="getDeviceTableData(true)"
            ></el-input>
          </el-form-item>
          <el-form-item label="设备名称:">
            <el-input
              v-model="deviceForm.deviceName"
              placeholder="请输入设备名称"
              size="small"
              clearable
              :maxlength="20"
              @change="getDeviceTableData(true)"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="sy-btn-regular" size="medium" @click="getDeviceTableData">查询</el-button>
            <el-button type="primary" class="sy-btn-regular" size="medium" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
        <el-table
          class="sy-table clickable"
          v-loading="deviceLoad"
          :data="deviceData"
          size="mini"
          border
          @row-click="onDeviceTableClick"
        >
          <sy-empty slot="empty"></sy-empty>

          <el-table-column
            v-for="column in deviceTableColumns"
            :key="column.prop"
            v-bind="column"
            align="center"
            show-overflow-tooltip
          >
            <template v-if="column.prop === 'patrolStatus'" v-slot="{ row }">
              <span :class="`sy-status-${row.patrol_status}`">{{ row.patrolStatus }} </span>
            </template>
          </el-table-column>
        </el-table>

        <div class="patrol-table__footer">
          <el-pagination
            class="sy-pagination"
            layout="total, prev, pager, next, jumper"
            :current-page.sync="devicePage"
            :page-size="deviceSize"
            :total="deviceTotal"
            background
            small
            @current-change="onDevicePageChange"
          >
          </el-pagination>
        </div>
      </div>
      <div class="patrol-table">
        <el-form class="patrol-table__form" ref="patrolForm" size="mini" :model="patrolForm" inline>
          <el-form-item label="部件名称:" prop="componentName">
            <el-input
              v-model="patrolForm.componentName"
              placeholder="请输入部件名称"
              size="small"
              :maxlength="20"
              clearable
              @change="getPatrolTableData(true)"
            ></el-input>
          </el-form-item>
          <el-form-item label="巡视点位:" prop="patrolpointName">
            <el-input
              v-model="patrolForm.patrolpointName"
              placeholder="请输入巡视点位名称"
              size="small"
              :maxlength="20"
              clearable
              @change="getPatrolTableData(true)"
            ></el-input>
          </el-form-item>

          <el-button class="sy-btn-regular" type="primary" size="medium" @click="getPatrolTableData">查询</el-button>
          <el-button class="sy-btn-regular" type="primary" size="medium" @click="handleExportReport">导出</el-button>
          <el-button class="sy-btn-regular" type="primary" size="medium" @click="batchAudit">一键审核</el-button>
          <el-checkbox-group
            class="el-form-item__status"
            v-model="patrolForm.patrolStates"
            @change="getPatrolTableData(true)"
          >
            <el-checkbox v-for="item in patrolStates" :key="item.key" :label="item.key">
              {{ item.value }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form>
        <el-table
          class="sy-table clickable"
          v-loading="patrolLoad"
          :data="patrolData"
          size="mini"
          border
          @row-click="onPatrolTableClick"
          @selection-change="handleTableSelect"
        >
          <sy-empty slot="empty"></sy-empty>

          <el-table-column
            v-for="column in patrolTableColumns"
            :key="column.prop"
            v-bind="column"
            align="center"
            show-overflow-tooltip
          >
            <template v-if="column.prop === 'patrolStatus'" v-slot="{ row }">
              <span :class="`sy-status-${row.patrol_status}`">{{ row.patrolStatus }} </span>
            </template>
            <template v-else-if="column.prop === 'operation'" v-slot="{ row, $index }">
              <el-button
                class="sy-btn-regular"
                size="small"
                :type="row.modified ? 'success' : 'primary'"
                @click="handleAuditResult(row, $index)"
              >
                {{ row.modified ? '重新审核' : '审核' }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="patrol-table__footer">
          <el-pagination
            class="sy-pagination"
            layout="total, prev, pager, next, jumper"
            :current-page.sync="patrolPage"
            :page-size="patrolSize"
            :total="patrolTotal"
            background
            small
            @current-change="onPatrolPageChange"
          >
          </el-pagination>
        </div>
      </div>
    </el-col>

    <el-col class="sy-col-vertical" :span="8">
      <div class="patrol-statis sy-view">
        <div class="patrol-statis__progress">
          <sy-progress type="circle" :percentage="taskProgress"></sy-progress>
          <div class="progress-text">任务进度</div>
        </div>
        <ul class="patrol-statis__list">
          <li class="patrol-statis__item">
            <span class="patrol-statis__label normal_label">成功</span>
            <span class="patrol-statis__value">
              <span class="normal_value">{{ taskStatis[patrolStatus.SUCCESS] || 0 }}</span>
            </span>
          </li>
          <li class="patrol-statis__item">
            <span class="patrol-statis__label warning_label">告警</span>
            <span class="patrol-statis__value">
              <span class="warning_value">{{ taskStatis[patrolStatus.WARNING] || 0 }}</span>
            </span>
          </li>
          <li class="patrol-statis__item">
            <span class="patrol-statis__label fail_label">失败</span>
            <span class="patrol-statis__value">
              <span class="fail_value">{{ taskStatis[patrolStatus.FAILURE] || 0 }}</span>
            </span>
          </li>
        </ul>
      </div>

      <div class="patrol-viewer sy-view">
        <FileViewer :data="historyItem" :editable="false">
          <span slot="info">
            {{ `${historyItem.patrolpoint_name}: ${historyItem.value_disp || '-'}` }}
          </span>
        </FileViewer>
      </div>

      <div class="patrol-chart sy-view">
        <sy-chart
          v-if="historyData && historyData.length > 0"
          ref="chart"
          type="line"
          :option="historyOption"
          @zrclick="handleChartClick"
        ></sy-chart>
        <sy-empty v-else></sy-empty>
      </div>
    </el-col>

    <AuditModal :visible.sync="auditModal" @submit="handleBatchAudit" />
    <PatrolResult :visible.sync="patrolModal" :data="patrolItem" @success="handleResultAudit" />
  </el-row>
</template>

<script>
import { mapGetters } from 'vuex';
import { saveAs } from 'file-saver';
import { PatrolStatus, TaskStatus } from '@/enums';
import { arrToMap, formatDuration, isValidDate, getReportFullPath } from '@/utils';
import Option from '@/enums/option';
import FileViewer from '@/components/FileViewer';

export default {
  name: 'PatrolDetails',
  components: {
    FileViewer,
    AuditModal: () => import('@/components/Modal/Audit'),
    PatrolResult: () => import('../components/PatrolResult'),
  },
  computed: {
    ...mapGetters(['username']),
    executeId() {
      return this.$route.query.executeId;
    },
    patrolStatus() {
      return PatrolStatus;
    },
    patrolStates() {
      return Option.PatrolStates;
    },
    taskTypeMap() {
      return arrToMap(Option.TaskTypes);
    },
    taskStateMap() {
      return arrToMap(Option.TaskStates);
    },
    taskSourceMap() {
      return arrToMap(Option.TaskSources);
    },
    patrolTypeMap() {
      return arrToMap(Option.PatrolTypes);
    },
    patrolStateMap() {
      return arrToMap(Option.PatrolStates);
    },

    taskProgress() {
      return this.taskData.task_progress && this.taskData.patrolpoint_total
        ? Math.min(Math.round((this.taskData.task_progress / this.taskData.patrolpoint_total) * 100), 100)
        : 0;
    },
    taskDataList() {
      const {
        patrol_type,
        station_name,
        task_name,
        task_type,
        task_source,
        task_status,
        task_end_time,
        task_start_time,
        task_total_time,
      } = this.taskData;
      return [
        { label: '站所名称', value: station_name },
        { label: '任务名称', value: task_name },
        { label: '任务类型', value: this.taskTypeMap[task_type] || '-' },
        { label: '执行状态', value: this.taskStateMap[task_status] || '-' },
        { label: '巡视来源', value: this.taskSourceMap[task_source] || '-' },
        { label: '巡视类型', value: this.patrolTypeMap[patrol_type] || '-' },
        { label: '开始时间', value: task_start_time },
        { label: '结束时间', value: task_end_time },
        { label: '任务用时', value: task_total_time },
      ];
    },
    deviceTableColumns() {
      return [
        { type: 'index', label: '序号', width: 80 },
        { prop: 'station_name', label: '站所名称' },
        { prop: 'bay_name', label: '间隔名称' },
        { prop: 'area_name', label: '区域名称' },
        { prop: 'device_name', label: '设备名称' },
        { prop: 'patrolStatus', label: '巡视结论' },
      ];
    },
    patrolTableColumns() {
      return [
        { type: 'selection', width: 60 },
        { type: 'index', label: '序号', width: 80 },
        // { prop: 'station_name', label: '站所名称', minWidth: 100 },
        // { prop: 'component_name', label: '部件名称', minWidth: 100 },
        { prop: 'patrolpoint_name', label: '巡视点位', minWidth: 150 },
        { prop: 'patrolDevice', label: '巡视设备', minWidth: 150 },
        { prop: 'patrolResult', label: '巡视结果' },
        { prop: 'patrolStatus', label: '巡视结论' },
        { prop: 'operation', label: '人工操作', minWidth: 150 },
      ];
    },
    historyOption() {
      return {
        grid: {
          bottom: '15%',
        },
        legend: {
          top: '3%',
          itemWidth: 15,
          itemHeight: 15,
          textStyle: {
            fontSize: 16,
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
                  <span>${item.data.value || '-'}</span>
                </div>
            `;
          },
        },
        dataZoom: [
          {
            type: 'inside',
            start: 25,
            end: 100,
            minSpan: 25,
            maxSpan: 100,
          },
          {
            type: 'slider',
            left: '5%',
            right: '5%',
            height: '5%',
            start: 25,
            end: 100,
            minSpan: 25,
            maxSpan: 100,
            textStyle: {
              overflow: 'break',
            },
          },
        ],
        dataset: {
          source: [...this.historyData]
            .sort((a, b) => new Date(a.patrol_time).getTime() - new Date(b.patrol_time).getTime())
            .map((_) => ({
              name: this.$dayjs(_.patrol_time).format('YYYY-MM-DD\nHH:mm:ss'),
              value: _.value,
              value_disp: _.value_disp || '-',
              patrol_time: _.patrol_time,
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
            connectNulls: true,
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
  watch: {
    executeId: {
      handler() {
        this.getTaskBasicData();
        this.getTaskStatisData();
        this.getDeviceTableData();
        this.getPatrolTableData();
        this.getPatrolPointData();
      },
      immediate: true,
    },
  },
  data() {
    return {
      auditModal: false,
      patrolModal: false,
      taskData: {
        patrol_type: '',
        task_name: '',
        task_type: '',
        task_source: '',
        task_status: '',
        task_end_time: '',
        task_start_time: '',
        task_total_time: '',
      },
      taskStatis: {
        [PatrolStatus.SUCCESS]: 0,
        [PatrolStatus.WARNING]: 0,
        [PatrolStatus.FAILURE]: 0,
      },
      deviceLoad: false,
      deviceId: undefined,
      devicePage: 1,
      deviceSize: 5,
      deviceIndex: 0,
      deviceTotal: 0,
      deviceData: [],
      deviceForm: {
        bayName: '',
        deviceName: '',
      },
      patrolLoad: false,
      patrolItem: {},
      patrolPage: 1,
      patrolSize: 5,
      patrolIndex: 0,
      patrolTotal: 0,
      patrolData: [],
      patrolForm: {
        patrolStates: [],
        componentName: '',
        patrolpointName: '',
      },
      historyData: [],
      historyItem: {
        execute_id: '',
        file_type: '',
        file_path: '',
      },
      tableSelected: [],
    };
  },
  created() {
    this.getTaskBasicData();
    this.getTaskStatisData();
    this.getDeviceTableData();
    this.getPatrolTableData();
    this.getPatrolPointData();
  },
  methods: {
    handleReset() {
      this.deviceId = undefined;
      this.getPatrolTableData(true);
    },
    handleExportReport() {
      if (this.patrolTotal === 0) {
        this.$message({
          type: 'warning',
          message: '暂无数据可导出！',
        });
      } else {
        this.patrolLoad = true;
        const { componentName, patrolpointName, patrolStates } = this.patrolForm;
        this.$api
          .postHistoryApi('report', {
            size: this.patrolTotal,
            match: {
              device_id: this.device_id,
              execute_id: this.executeId,
              component_name: componentName,
              patrolpoint_name: patrolpointName,
              patrol_status: patrolStates.length > 0 ? patrolStates : undefined,
            },
          })
          .then((res) => {
            if (res.result === -1) {
              this.patrolLoad = false;
              this.$message({
                type: 'error',
                message: '数据导出异常！',
              });
            } else {
              const index = res.msg.lastIndexOf('/');
              const title = res.msg.substring(index + 1, res.msg.length);

              saveAs(getReportFullPath(res.msg), title);
              setTimeout(() => {
                // 一段时间后开启定时器，开始监听页面焦点获取情况
                let closeLoading = setInterval(() => {
                  // 开启定时器，监测页面是否获取焦点，以结束页面loading状态
                  if (document.hasFocus()) {
                    this.patrolLoad = false;
                    clearInterval(closeLoading);
                    closeLoading = null;
                  }
                }, 3000);
              }, 3000);
            }
          })
          .catch(() => {
            this.patrolLoad = false;
          });
      }
    },
    // 批量审核
    batchAudit() {
      if (this.tableSelected.length === 0) {
        return this.$message({
          type: 'warning',
          message: '请选择审核项目',
        });
      }
      this.auditModal = true;
    },
    handleBatchAudit(model) {
      const modifyTime = this.$dayjs().format('YYYY-MM-DD HH:mm:ss');
      this.$api
        .postHistoryApi('updateByQuery', {
          index: 'history.item',
          match: {
            _id: this.tableSelected.map((item) => item.id),
          },
          script: {
            inline: `ctx._source['modifier']= '${
              this.username
            }';ctx._source['modified']= true;ctx._source['modify_content']= '${
              model.approval_comments
            }';ctx._source['modify_time']= '${modifyTime}';ctx._source['patrol_status']= ${
              model.approved === 0 ? PatrolStatus.WARNING : PatrolStatus.SUCCESS
            };`,
          },
        })
        .then((res) => {
          if (res?.result === 1) {
            setTimeout(() => {
              this.getPatrolTableData();
            }, 500);
            this.$message({
              message: '批量审核成功！',
              type: 'success',
            });
            this.auditModal = false;
          }
        });
    },
    handleAuditResult(row, index) {
      this.patrolItem = row;
      this.patrolIndex = index;
      this.patrolModal = true;
    },

    handleResultAudit() {
      setTimeout(() => {
        this.getPatrolTableData();
      }, 500);
    },
    handleChartClick({ dataIndex }) {
      this.historyItem = this.historyData[dataIndex];
    },
    handleTableSelect(selection) {
      this.tableSelected = selection;
    },
    // 间隔行点击
    onDeviceTableClick(row) {
      this.deviceId = row.device_id;
      this.getPatrolTableData(true);
    },
    // 点位行点击
    onPatrolTableClick(row) {
      this.patrolItem = row;
      this.getPatrolPointData();
    },
    onDevicePageChange() {
      this.getDeviceTableData();
    },
    onPatrolPageChange() {
      this.getPatrolTableData();
    },
    // 任务基础数据
    getTaskBasicData() {
      this.$api
        .postHistoryApi('query', {
          index: 'history.task',
          from: 0,
          size: 1,
          match: { execute_id: this.executeId },
        })
        .then((res) => {
          if (!res || !res.data || !res.data.length) return;
          const taskData = res.data[0];
          this.taskData = {
            ...taskData,
            taskStatus:
              taskData.task_status === TaskStatus.UNDO && taskData.execute_id
                ? '排队中'
                : this.taskStateMap[taskData.task_status] || '-',
            task_end_time: isValidDate(taskData.task_end_time) ? taskData.task_end_time : '-',
            task_total_time:
              taskData.task_start_time &&
              taskData.task_end_time &&
              isValidDate(taskData.task_start_time) &&
              isValidDate(taskData.task_end_time)
                ? formatDuration(this.$dayjs(taskData.task_end_time).diff(taskData.task_start_time))
                : '',
          };
        });
    },
    // 任务统计数据
    getTaskStatisData() {
      this.$api
        .getHistoryApi(`queryCount`, {
          index: 'history.item',
          groupField: 'patrol_status',
          execute_id: this.executeId,
        })
        .then((res) => {
          if (!res?.data) return;
          this.taskStatis = arrToMap(res.data, 'patrol_status', 'count');
        });
    },
    // 巡视统计数据
    getDeviceTableData(reset) {
      this.deviceLoad = true;
      if (reset) this.devicePage = 1;
      const { deviceName, bayName } = this.deviceForm;
      this.$api
        .postHistoryApi('queryStatus', {
          index: 'history.item',
          from: (this.devicePage - 1) * this.deviceSize,
          size: this.deviceSize,
          match: {
            execute_id: this.executeId,
            device_name: deviceName,
            bay_name: bayName,
          },
        })
        .then((res) => {
          if (!res?.data) {
            this.deviceData = [];
            this.deviceTotal = 0;
            return;
          }
          this.deviceData = res.data.map((item) => ({
            ...item,
            area_name: item.area_name || '-',
            station_name: item.station_name || '-',
            patrolStatus: this.patrolStateMap[item.patrol_status] || '-',
          }));
          this.deviceTotal = Math.min(res.total, 10000);
        })
        .finally(() => {
          this.deviceLoad = false;
        });
    },
    // 巡视记录数据
    getPatrolTableData(reset) {
      this.patrolLoad = true;
      if (reset) this.patrolPage = 1;
      const { componentName, patrolpointName, patrolStates } = this.patrolForm;
      this.$api
        .postHistoryApi('query', {
          index: 'history.item',
          from: (this.patrolPage - 1) * this.patrolSize,
          size: this.patrolSize,
          match: {
            device_id: this.deviceId,
            execute_id: this.executeId,
            component_name: componentName,
            patrolpoint_name: patrolpointName,
            patrol_status: patrolStates.length > 0 ? patrolStates : undefined,
          },
          order: { modify_time: 'desc' },
        })
        .then((res) => {
          if (!res?.data) {
            this.patrolData = [];
            this.patrolTotal = 0;
            return;
          }
          this.patrolData = res.data.map((item) => ({
            ...item,
            patrolResult: item.value_disp || item.value || '-',
            patrolStatus: this.patrolStateMap[item.patrol_status] || '-',
            patrolDevice: item.camera_name || item.robot_name || item.host_name || '-',
          }));
          this.patrolTotal = Math.min(res.total, 10000);
        })
        .finally(() => {
          this.patrolLoad = false;
        });
    },
    // 巡视点位数据
    getPatrolPointData() {
      this.$api
        .postHistoryApi('query', {
          index: 'history.item',
          from: 0,
          size: 10,
          match: {
            patrolpoint_id: this.patrolItem.patrolpoint_id,
            camera_id: this.patrolItem.camera_id,
            robot_id: this.patrolItem.robot_id,
            host_id: this.patrolItem.host_id,
          },
          order: { patrol_time: 'desc' },
          range: { patrol_time: this.patrolItem.patrol_time && `,${this.patrolItem.patrol_time}` },
        })
        .then((res) => {
          if (!res || !res.data || !res.data.length) {
            this.historyData = [];
            this.historyItem = {};
            return;
          }
          this.historyData = res.data;
          this.historyItem = this.historyData[0];
        });
    },
  },
};
</script>

<style lang="scss" scoped>
%right-item {
  width: 100%;
  height: calc(100% - 6px);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 3px;
  margin-top: 3px;
  border-radius: 3px;
}

.patrol-info {
  &-list {
    display: flex;
    flex-wrap: wrap;
    width: 100%;
    height: 15vh;
  }

  &-item {
    display: flex;
    align-items: center;
    width: 33.33%;
    height: 5vh;
    font-size: 16px;
    line-height: 5vh;
    text-align: center;

    > label {
      width: 30%;
      height: 100%;
      border-left: $border-default;
      border-right: $border-default;
      background-color: $bg-label;
    }
    > span {
      width: 70%;
      height: 100%;
      color: $text-secondary;
      @include text-ellipsis();
    }
  }
}

.patrol-table {
  &__form {
    display: flex;
    align-items: center;
    width: 100%;
    height: 5vh;
    padding: 0 10px;
    border: $border-default;
    background: $bg-view;

    .el-form-item {
      display: inline-flex;
      align-items: center;
      margin-bottom: 0;

      &__status {
        margin-left: auto;

        ::v-deep .el-checkbox {
          margin-right: 10px;

          &__label {
            padding-left: 5px;
          }
        }
      }
    }
    .search-input {
      width: 25%;
    }
  }
  &__footer {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 4vh;
    background: {
      image: url('~@/assets/img/bg_pagination.png');
      repeat: no-repeat;
      size: 100% 100%;
    }
    .sy-pagination {
      text-align: center;
    }
  }
  ::v-deep .sy-table {
    width: 100%;
    height: 22vh;
    margin-top: 5px;
  }
}

.patrol-statis {
  display: flex;
  width: 100%;
  height: 15vh;

  &__progress {
    display: flex;
    align-items: center;
    flex-direction: column;
    justify-content: center;
    width: 200px;
    height: 100%;
    ::v-deep .el-progress-circle {
      width: 100px !important;
      height: 100px !important;
    }
    ::v-deep .el-progress__text {
      font-size: 20px !important;
    }

    .progress-text {
      margin-top: 1vh;
      font-size: 16px;
      color: $text-secondary;
    }
  }
  &__list {
    flex: auto;
    height: 100%;
  }
  &__item {
    display: flex;
    width: 100%;
    height: 5vh;
    font-size: 16px;
  }
  &__label {
    display: flex;
    align-items: center;
    justify-content: center;
    border-right: $border-default;
    border-left: $border-default;
    width: 40%;
    background: $bg-label;

    &.fail_label {
      color: $color-warning;
    }
    &.normal_label {
      color: $color-primary;
    }
    &.warning_label {
      color: $color-danger;
    }
  }
  &__value {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 60%;
    font-weight: 600;

    .normal_value {
      @extend %right-item;
      border-left: 3px solid $color-primary;
      background: $color-primary-gradient-90;
    }
    .fail_value {
      @extend %right-item;
      border-left: 3px solid $color-warning;
      background: $color-warning-gradient-90;
    }

    .warning_value {
      @extend %right-item;
      border-left: 3px solid $color-danger;
      background: $color-danger-gradient-90;
    }
  }
}

.patrol-viewer {
  width: 100%;
  height: 32vh;
}

.patrol-chart {
  width: 100%;
  height: 31vh;
  padding: 5px;
}
</style>
