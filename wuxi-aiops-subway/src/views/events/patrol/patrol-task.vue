<template>
  <TreeView @selected="handleTreeSelect">
    <div class="sy-table-header flex-auto">
      <el-form class="sy-table-form search-form" ref="tableForm" :model="tableForm" inline>
        <el-form-item prop="patrolType" label="巡视类型:">
          <el-select
            v-model="tableForm.patrolType"
            placeholder="请选择巡视类型"
            popper-class="sy-popper"
            clearable
            @change="getTableData(true)"
          >
            <el-option v-for="(val, key) in patrolTypeMap" :key="key" :label="val" :value="+key"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="taskStatus" label="任务状态:">
          <el-select
            v-model="tableForm.taskStatus"
            placeholder="请选择任务状态"
            popper-class="sy-popper"
            clearable
            @change="getTableData(true)"
          >
            <el-option v-for="(val, key) in taskStateMap" :key="key" :label="val" :value="+key"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="timeRange" label="巡视时间:">
          <el-date-picker
            class="sy-date-picker"
            v-model="tableForm.timeRange"
            type="datetimerange"
            popper-class="sy-popper"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            :default-time="['00:00:00', '23:59:59']"
            :picker-options="pickerOptions"
            @change="getTableData(true)"
          >
          </el-date-picker>
        </el-form-item>

        <el-form-item class="el-form-item__action el-form-item-last">
          <div class="head-operate">
            <el-button
              class="sy-btn-corner sy-btn-corner--bl"
              icon="el-icon-search"
              size="small"
              @click="handleTableAction('query')"
              >查询
            </el-button>
            <el-button
              class="sy-btn-corner sy-btn-corner--bl"
              icon="el-icon-refresh-right"
              size="small"
              @click="handleTableAction('reset')"
              >重置
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
    <el-table class="sy-table" v-loading="tableLoad" :data="tableData" border>
      <sy-empty slot="empty" description="暂无任务数据"></sy-empty>

      <el-table-column
        v-bind="column"
        v-for="column in tableColumns"
        :key="column.prop"
        align="center"
        show-overflow-tooltip
      >
        <template v-if="column.prop === 'taskProgress'" v-slot="{ row }">
          <sy-progress :percentage="row.taskProgress" text-inside></sy-progress>
        </template>
        <template v-else-if="column.prop === 'task_start_time' || column.prop === 'task_end_time'" v-slot="{ row }">
          {{ row[column.prop] | formatDateTime }}
        </template>

        <template v-else-if="column.prop === 'operation'" v-slot="{ row }">
          <el-button class="sy-btn-corner sy-btn-corner--br" size="mini" @click="handleTableAction('check', row)"
            >详情
          </el-button>
          <el-button
            v-if="row.task_status === taskStatus.DOING"
            class="sy-btn-corner sy-btn-corner--br"
            size="mini"
            :disabled="row.disabled"
            @click="handleTaskAction('pause', row)"
            >暂停
          </el-button>
          <el-button
            v-if="row.task_status === taskStatus.PAUSE"
            class="sy-btn-corner sy-btn-corner--br"
            size="mini"
            :disabled="row.disabled"
            @click="handleTaskAction('resume', row)"
            >继续
          </el-button>
          <el-button
            v-if="[taskStatus.UNDO, taskStatus.DOING, taskStatus.PAUSE, taskStatus.WAITING].includes(row.task_status)"
            class="sy-btn-corner sy-btn-corner--br"
            size="mini"
            :disabled="row.disabled"
            @click="handleTaskAction('terminate', row)"
            >终止
          </el-button>
          <el-button
            v-show="row.task_status === taskStatus.DONE"
            class="sy-btn-corner sy-btn-corner--br"
            size="mini"
            @click="handleTableAction('export', row)"
            >导出
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="sy-table-footer">
      <el-pagination
        class="sy-pagination"
        background
        :total="tableTotal"
        :page-size.sync="tableSize"
        :current-page.sync="tablePage"
        @current-change="onTablePageChange"
        layout="prev, pager, next, total, jumper"
      >
      </el-pagination>
    </div>
  </TreeView>
</template>

<script>
import { mapGetters } from 'vuex';
import { saveAs } from 'file-saver';
import { arrToMap, getReportFullPath } from '@/utils';
import { TaskStatus, MessageAction, CustomPatrolTypes, NonTaskPatrolTypes } from '@/enums';
import Option from '@/enums/option';
import TreeView from '@/components/TreeView';

export default {
  name: 'PatrolTask',
  components: {
    TreeView,
  },
  computed: {
    ...mapGetters(['stationId']),
    taskStatus() {
      return TaskStatus;
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
    pickerOptions() {
      return {
        disabledDate: (date) => {
          return this.$dayjs(date).isAfter(this.$dayjs(), 'day');
        },
      };
    },
    tableColumns() {
      return [
        { type: 'index', label: '序号', width: 80 },
        { prop: 'station_name', label: '站所名称', minWidth: 100 },
        { prop: 'task_name', label: '任务名称', minWidth: 100 },
        { prop: 'taskType', label: '执行类型' },
        { prop: 'taskState', label: '任务状态' },
        { prop: 'taskProgress', label: '任务进度', minWidth: 100 },
        { prop: 'patrolType', label: '巡视类型' },
        { prop: 'taskSource', label: '巡视设备' },
        { prop: 'task_start_time', label: '开始时间', minWidth: 100 },
        { prop: 'task_end_time', label: '结束时间', minWidth: 100 },
        { prop: 'operation', label: '人工操作', minWidth: 150 },
      ];
    },
  },
  data() {
    return {
      tableLoad: false,
      tablePage: 1,
      tableSize: 10,
      tableTotal: 0,
      tableData: [],
      tableForm: {
        timeRange: [],
        taskStatus: '',
        patrolType: undefined,
      },
    };
  },
  created() {
    this.getTableData();
  },
  mounted() {
    this.onStompPatrolPush();
  },
  methods: {
    handleTreeSelect(stationId) {
      this.stationId = stationId;
      this.getTableData();
    },
    handleTaskAction(action, row) {
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
    },
    async handleTableAction(action, row) {
      switch (action) {
        case 'reset':
          this.$refs.tableForm.resetFields();
          this.getTableData(true);
          break;
        case 'query':
          this.getTableData();
          break;
        case 'check':
          if ([TaskStatus.DOING, TaskStatus.PAUSE, TaskStatus.WAITING].includes(row.task_status)) {
            this.$router.push({
              path: '/patrol/task-monitor',
              query: { taskId: row.task_id, executeId: row.execute_id },
            });
          } else {
            this.$router.push({
              path: '/events/patrol/patrol-details',
              query: { taskId: row.task_id, executeId: row.execute_id },
            });
          }
          break;
        case 'export':
          {
            this.$confirm('确认导出任务报告?', {
              type: 'warning',
              callback: (action) => {
                if (action === 'confirm') {
                  this.tableLoad = true;
                  this.$api
                    .postHistoryApi('report', {
                      match: {
                        execute_id: row.execute_id,
                      },
                    })
                    .then((res) => {
                      if (res.result === -1) {
                        this.tableLoad = false;
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
                              this.tableLoad = false;
                              clearInterval(closeLoading);
                              closeLoading = null;
                            }
                          }, 3000);
                        }, 3000);
                      }
                    });
                }
              },
            });
          }
          break;
        default:
          break;
      }
    },
    onTablePageChange() {
      this.getTableData();
    },
    // 巡视消息推送
    onStompPatrolPush() {
      this.$bus.$on('stompPatrolPush', (body) => {
        const { action, data } = JSON.parse(body);
        if (!data || !data.length) return;
        switch (action) {
          case MessageAction.PATROL.TASK_INDEX:
            {
              const dataItem = data[0];
              if (!dataItem || NonTaskPatrolTypes.includes(dataItem.patrol_type)) return;

              const taskItem = this.tableData.find((item) => item.execute_id === dataItem.execute_id);
              if (taskItem) {
                taskItem.task_status = dataItem.task_status;
                taskItem.taskState = this.taskStateMap[taskItem.task_status] || '-';
                taskItem.taskProgress =
                  dataItem.patrolpoint_total && dataItem.task_progress
                    ? Math.min(Math.round((dataItem.task_progress / dataItem.patrolpoint_total) * 100), 100)
                    : 0;
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
    getData(tableSize = this.tableSize) {
      const { timeRange, taskStatus, patrolType } = this.tableForm;
      return this.$api.postHistoryApi('query', {
        index: 'history.task',
        from: tableSize * (this.tablePage - 1),
        size: tableSize,
        match: {
          station_id: this.stationId,
          patrol_type: patrolType,
          task_status: taskStatus,
        },
        mustnot: { patrol_type: CustomPatrolTypes },
        order: { task_start_time: 'desc' },
        range: { task_start_time: timeRange ? timeRange.join(',') : undefined },
      });
    },
    getTableData(reset) {
      this.tableLoad = true;
      if (reset) this.tablePage = 1;
      this.getData()
        .then((res) => {
          if (!res?.data) {
            this.tableData = [];
            this.tableTotal = 0;
            return;
          }
          this.tableData = res.data.map((item, index) => {
            return {
              ...item,
              index: (this.tablePage - 1) * this.tableSize + index + 1,
              taskType: this.taskTypeMap[item.task_type] || '-',
              taskState:
                item.task_status === TaskStatus.UNDO && item.execute_id
                  ? '排队中'
                  : this.taskStateMap[item.task_status] || '-',
              taskSource: this.taskSourceMap[item.task_source] || '-',
              patrolType: this.patrolTypeMap[item.patrol_type] || '-',
              taskProgress:
                item.patrolpoint_total && item.task_progress
                  ? Math.min(Math.round((item.task_progress / item.patrolpoint_total) * 100), 100)
                  : 0,
            };
          });
          this.tableTotal = res.total;
        })
        .finally(() => {
          this.tableLoad = false;
        });
    },
  },
};
</script>
