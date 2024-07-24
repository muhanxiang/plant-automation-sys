<template>
  <TreeView id="task_manage" @selected="handleTreeSelect">
    <div class="sy-table-header search">
      <el-form class="sy-table-form search-form" ref="tableForm" :model="tableForm" inline>
        <el-form-item class="sy-form-search" prop="name" label="任务名称:">
          <el-input
            class="el-form-item__input"
            v-model="tableForm.name"
            suffix-icon="el-icon-search"
            placeholder="请输入任务名称"
            :maxlength="20"
            clearable
            @change="getTableData(true)"
          ></el-input>
        </el-form-item>

        <el-form-item class="sy-form-search" prop="patrol_type" label="巡视类型:">
          <el-select
            class="el-form-item__select sy-select"
            v-model="tableForm.patrol_type"
            popper-class="sy-popper"
            placeholder="请选择巡视类型"
            clearable
            @change="getTableData(true)"
          >
            <el-option v-for="item in patrolTypes" :key="item.key" :label="item.value" :value="item.key"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="sy-form-search" prop="task_type" label="执行类型:">
          <el-select
            class="el-form-item__select sy-select"
            v-model="tableForm.task_type"
            placeholder="请选择执行类型"
            popper-class="sy-popper"
            clearable
            @change="getTableData(true)"
          >
            <el-option v-for="item in taskTypes" :key="item.key" :label="item.value" :value="item.key"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item class="el-form-item__action el-form-item-last">
          <div class="head-operate">
            <el-button
              class="sy-btn-corner sy-btn-corner--bl"
              icon="el-icon-plus"
              size="small"
              @click="handleTaskAction(null, 'create')"
              >新增
            </el-button>
            <el-button
              class="sy-btn-corner sy-btn-corner--bl"
              icon="el-icon-refresh-right"
              size="small"
              @click="handleReset"
              >重置
            </el-button>
            <el-button
              class="sy-btn-corner sy-btn-corner--bl"
              icon="el-icon-delete"
              size="small"
              @click="handleBatchDelete"
              >删除
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
    <el-table class="sy-table" v-loading="tableLoad" :data="tableData" border @selection-change="handleSelectionChange">
      <sy-empty slot="empty" description="暂无任务数据"></sy-empty>

      <el-table-column
        v-for="column in tableColumns"
        :key="column.prop"
        v-bind="column"
        align="center"
        show-overflow-tooltip
      >
        <template v-if="column.prop === 'taskPriority'" v-slot="{ row }">
          <el-rate v-model="row.taskPriority" disabled text-color="#ff9900"> </el-rate>
        </template>
        <template v-if="column.prop === 'operation'" v-slot="{ row }">
          <el-button class="sy-btn-corner sy-btn-corner--br" size="mini" @click="handleTaskAction(row, 'edit')">
            编辑
          </el-button>

          <template v-if="row.approved === 1">
            <template v-if="row.enabled">
              <el-button class="sy-btn-corner sy-btn-corner--br" size="mini" @click="handleTaskAction(row, 'start')"
                >执行
              </el-button>
              <el-button class="sy-btn-corner sy-btn-corner--br" size="mini" @click="handleTaskAction(row, 'disable')"
                >停用
              </el-button>
            </template>
            <el-button
              v-else
              class="sy-btn-corner sy-btn-corner--br"
              size="mini"
              @click="handleTaskAction(row, 'enable')"
              >启用
            </el-button>
          </template>
          <template v-else-if="row.approved === 0">
            <el-button class="sy-btn-corner sy-btn-corner--br" size="mini" @click="handleTaskAction(row, 'audit')"
              >审核
            </el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>
    <div class="sy-table-footer">
      <el-pagination
        class="sy-pagination"
        background
        :total="tableTotal"
        :page-size="tableSize"
        :current-page.sync="tablePage"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        layout="prev, pager, next, total, jumper"
      >
      </el-pagination>
    </div>

    <AuditModal :visible.sync="taskAudit" @submit="handleTaskAudit" />
    <TaskPlan :visible.sync="taskPlan" :guid="taskItem.guid" @success="handleTaskSubmit" />
  </TreeView>
</template>

<script>
import { arrToMap } from '@/utils';
import { mapGetters } from 'vuex';
import { StationMode } from '@/enums';

import Option from '@/enums/option';
import TreeView from '@/components/TreeView';
import StationTree from '@/mixins/station-tree';

export default {
  name: 'TaskManage',
  mixins: [StationTree],
  components: {
    TreeView,
    TaskPlan: () => import('./components/TaskPlan'),
    AuditModal: () => import('@/components/Modal/Audit'),
  },
  computed: {
    ...mapGetters(['stationId']),
    stationMode() {
      return StationMode;
    },
    taskTypes() {
      return Option.TaskTypes;
    },
    patrolTypes() {
      return Option.PatrolTypes;
    },
    taskTypeMap() {
      return arrToMap(Option.TaskTypes);
    },
    taskSourceMap() {
      return arrToMap(Option.TaskSources);
    },
    taskPriorityMap() {
      return arrToMap(Option.TaskPriorities);
    },
    patrolTypeMap() {
      return arrToMap(Option.PatrolTypes);
    },
    tableColumns() {
      return [
        { type: 'selection', width: 60 },
        { type: 'index', label: '序号', width: 80 },
        { prop: 'station.name', label: '站所名称', minWidth: 100 },
        { prop: 'name', label: '任务名称', minWidth: 100 },
        { prop: 'patrolType', label: '巡视类型' },
        { prop: 'taskType', label: '执行类型' },
        { prop: 'taskSource', label: '巡视设备' },
        { prop: 'taskPriority', label: '任务优先级' },
        { prop: 'isEnable', label: '是否启用' },
        { prop: 'operation', label: '人工操作', minWidth: 150 },
      ];
    },
  },
  data() {
    return {
      filterText: '',
      taskAudit: false,
      tableSelect: [],
      refNamePopover: 'popover-',
      taskPlan: false,
      taskItem: {},
      tableLoad: false,
      tableData: [],
      tablePage: 1,
      tableSize: 10,
      tableTotal: 0,
      tableForm: {
        name: '',
        status: undefined,
        task_type: undefined,
        patrol_type: undefined,
      },
      dataTree: [],
      defaultProps: {
        children: 'children',
        label: 'label',
      },
    };
  },
  methods: {
    handleTreeSelect(stationId) {
      this.stationId = stationId;
      this.getTableData();
    },
    // 关键字搜索
    handleSearch() {
      this.getTableData();
    },
    handleTaskAudit(model) {
      this.$api
        .postTaskApi(`approve/${this.taskItem.guid}`, {
          data: { ...model },
        })
        .then(() => {
          this.getTableData();
          this.$message({
            type: 'success',
            message: '任务审核成功！',
          });
          this.taskAudit = false;
        });
    },
    // 方案编辑
    handleTaskAction(task, action) {
      switch (action) {
        case 'edit': // 编辑
          this.taskItem = task;
          this.taskPlan = true;
          break;
        case 'audit': // 审核
          this.taskItem = task;
          this.taskAudit = true;
          break;
        case 'check': // 查看
          break;
        case 'start': // 开始
          this.$confirm('立即执行任务?', {
            type: 'warning',
          })
            .then(() => {
              this.$api.postPatrolApi(`execute/${task.guid}`).then((res) => {
                if (res?.success) {
                  this.$message({
                    type: 'success',
                    message: '任务执行成功！',
                  });
                  this.getTableData();
                } else {
                  this.$message({
                    type: 'error',
                    message: '任务执行失败！',
                  });
                }
              });
            })
            .catch(() => {});
          break;
        case 'create': // 新增
          this.taskItem = {
            name: '', // 方案名称
            items: [], // 树组件数据
            source: 1, // 巡视设备
            priority: 2, // 任务优先级
            task_type: 0, // 执行类型
            start_time: null, // 执行时间
            patrol_type: 0, // 巡视类型
            cycleType: 0, // 周期类型
            cycle_week: [], // 周期（日）
            cycle_month: [], // 周期（月）
            cycle_end_time: null, // 结束时间
            cycle_start_time: null, // 开始时间
            cycle_execute_time: null, // 周期执行时间
            interval_type: 1, // 间隔类型
            interval_number: 0, // 间隔数量
            interval_execute_time: null, // 间隔执行时间
            interval_end_time: null, // 间隔结束时间
            interval_start_time: null, // 间隔开始时间
            meter_types: [], // 仪表类型
            device_types: [], // 设备类型
            appearance_types: [], // 外观类型
            recognition_types: [], // 识别类型
          };
          this.taskPlan = true;
          break;
        case 'enable':
          this.$confirm('确认启用任务?', {
            type: 'warning',
          }).then(() => {
            this.$api
              .postTaskApi(`enable/${task.guid}`, {
                data: { enabled: true },
              })
              .then((res) => {
                if (res?.success) {
                  this.$message({
                    type: 'success',
                    message: '任务启用成功！',
                  });
                  this.getTableData();
                } else {
                  this.$message({
                    type: 'error',
                    message: '任务启用失败！',
                  });
                }
              });
          });

          break;
        case 'disable':
          this.$confirm('确认停用任务?', {
            type: 'warning',
          }).then(() => {
            this.$api
              .postTaskApi(`enable/${task.guid}`, {
                data: { enabled: false },
              })
              .then((res) => {
                if (res?.success) {
                  this.$message({
                    type: 'success',
                    message: '任务停用成功！',
                  });
                  this.getTableData();
                } else {
                  this.$message({
                    type: 'error',
                    message: '任务停用失败！',
                  });
                }
              });
          });
          break;
        default:
          break;
      }
    },
    // 表格多选
    handleSelectionChange(val) {
      this.tableSelect = val;
    },
    // 批量删除
    handleBatchDelete() {
      let ids = this.tableSelect.map((item) => item.guid);
      if (ids && ids.length === 0)
        return this.$message({
          type: 'warning',
          message: '请选择待删除任务！',
        });

      this.$confirm('确认删除任务?', {
        type: 'warning',
      }).then(() => {
        this.$api
          .postTaskApi('remove', {
            data: ids,
          })
          .then((res) => {
            if (res?.success) {
              this.$message({
                type: 'success',
                message: '任务删除成功！',
              });
              this.getTableData();
            } else {
              this.$message({
                type: 'success',
                message: '任务删除失败！',
              });
            }
          });
      });
    },
    // 重置
    handleReset() {
      this.$refs.tableForm.resetFields();
      this.getTableData(true);
    },
    handleTaskSubmit() {
      this.taskPlan = false;
      this.getTableData();
    },
    getTableData(reset) {
      this.tableLoad = true;
      if (reset) this.tablePage = 1;

      let statusObj = {};
      if (this.tableForm.status === '0') {
        statusObj = {
          approved: 1,
        };
      } else if (this.tableForm.status === '1') {
        statusObj = {
          enabled: true,
        };
      } else {
        // 超期判断
        statusObj = {};
      }

      this.$api
        .getBaseApi('task', {
          ordering: '-id',
          limit: this.tableSize,
          offset: this.tableSize * (this.tablePage - 1),
          station_id: this.stationId,
          name__contains: this.tableForm.name,
          task_type: this.tableForm.task_type,
          patrol_type: this.tableForm.patrol_type,
          ...statusObj,
        })
        .then((res) => {
          if (!res?.results) {
            this.tableData = [];
            this.tableTotal = 0;
            return;
          }
          this.tableData = res.results.map((item, index) => {
            return {
              ...item,
              index: (this.tablePage - 1) * this.tableSize + index + 1,
              isEnable: item.enabled ? '已启用' : '未启用',
              taskPriority: this.taskPriorityMap[item.priority],
              taskType: this.taskTypeMap[item.task_type],
              taskSource: this.taskSourceMap[item.source],
              patrolType: this.patrolTypeMap[item.patrol_type] || '-',
            };
          });
          this.tableTotal = res.count;
        })
        .finally(() => {
          this.tableLoad = false;
        });
    },
    handleSizeChange(val) {
      this.tableSize = val;
      this.getTableData();
    },
    handleCurrentChange(val) {
      this.tablePage = val;
      this.getTableData();
    },
  },
  mounted() {
    this.getTableData();
  },
};
</script>
<style lang="scss" scoped>
.sy-row {
  .left {
    border: $border-default;
    height: 100%;
    padding: 10px;

    .left-form-search {
      margin-bottom: 20px;
      display: flex;
      align-items: center;
      justify-content: space-between;

      .search-input {
        flex: auto;
        margin-right: 10px;
      }
    }
  }
  .right {
    height: 100%;
    border: $border-default;
    padding: 0 10px;
  }
}
</style>
