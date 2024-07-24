<template>
  <TreeView @selected="handleTreeSelect">
    <div class="sy-table-header flex-auto">
      <el-form class="sy-card-header sy-form clearfix" ref="tableForm" :model="tableForm" inline>
        <el-form-item prop="patrolPoint" label="巡视点位:">
          <el-input
            v-model="tableForm.patrolPoint"
            placeholder="请输入巡视点位"
            :maxlength="20"
            clearable
            @change="getTableData(true)"
          ></el-input>
        </el-form-item>
        <el-form-item prop="recogntType" label="识别类型:">
          <el-select
            v-model="tableForm.recogntType"
            placeholder="请选择识别类型"
            popper-class="sy-popper"
            clearable
            @change="getTableData(true)"
          >
            <el-option v-for="(val, key) in recognitionTypeMap" :key="key" :label="val" :value="+key"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="auditStatus" label="审核结论:">
          <el-select
            v-model="tableForm.auditStatus"
            placeholder="请选择审核结论"
            popper-class="sy-popper"
            clearable
            @change="getTableData(true)"
          >
            <el-option v-for="(val, key) in patrolStateMap" :key="key" :label="val" :value="+key"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="timeRange" label="审核时间:">
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
      <sy-empty slot="empty" description="暂无审核记录"></sy-empty>

      <el-table-column
        v-for="column in tableColumns"
        :key="column.prop"
        v-bind="column"
        align="center"
        show-overflow-tooltip
      >
        <template v-if="column.prop === 'operation'" v-slot="{ row }">
          <el-button class="sy-btn-corner sy-btn-corner--br" size="mini" @click="handleTableAction('edit', row)"
            >编辑
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="sy-table-footer">
      <el-pagination
        class="sy-pagination"
        layout="prev, pager, next, total, jumper"
        :current-page.sync="tablePage"
        :page-size.sync="tableSize"
        :total="tableTotal"
        background
        @current-change="onTablePageChange"
      >
      </el-pagination>
    </div>

    <PatrolResult :visible.sync="auditModal" :data="auditData" @success="handleSubmitSuccess"></PatrolResult>
  </TreeView>
</template>

<script>
import { mapGetters } from 'vuex';
import { arrToMap } from '@/utils';
import Option from '@/enums/option';
import TreeView from '@/components/TreeView';

export default {
  name: 'AuditRecord',
  components: {
    TreeView,
    PatrolResult: () => import('../components/PatrolResult'),
  },
  computed: {
    ...mapGetters(['stationId']),
    patrolStateMap() {
      return arrToMap(Option.PatrolStates);
    },
    recognitionTypeMap() {
      return arrToMap(Option.RecognitionTypes);
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
        { prop: 'task_name', label: '巡视任务', minWidth: 100 },
        { prop: 'patrolpoint_name', label: '巡视点位', minWidth: 100 },
        { prop: 'patrolDevice', label: '巡视设备', minWidth: 100 },
        { prop: 'recogntType', label: '识别类型' },
        // { prop: 'value', label: '识别结果' },
        { prop: 'value_disp', label: '审核结果' },
        { prop: 'auditStatus', label: '审核结论' },
        { prop: 'modifier', label: '审核人员' },
        { prop: 'modify_time', label: '审核时间', minWidth: 100 },
        { prop: 'modify_content', label: '审核意见', minWidth: 100 },
        { prop: 'operation', label: '人工操作', minWidth: 150 },
      ];
    },
  },
  data() {
    return {
      auditData: {},
      auditModal: false,
      tableLoad: false,
      tablePage: 1,
      tableSize: 10,
      tableTotal: 0,
      tableData: [],
      tableSelect: [],
      tableForm: {
        timeRange: [],
        enabled: undefined,
        auditStatus: undefined,
        recogntType: undefined,
        patrolPoint: '',
      },
    };
  },
  created() {
    this.getTableData();
  },
  methods: {
    handleTreeSelect(stationId) {
      this.stationId = stationId;
      this.getTableData();
    },
    handleSubmitSuccess() {
      setTimeout(() => {
        this.getTableData(true);
      }, 500);
    },
    //选中列表
    handleSelectionChange(val) {
      this.tableSelect = val;
    },
    handleTableAction(action, row) {
      switch (action) {
        case 'reset':
          this.$refs.tableForm.resetFields();
          this.getTableData(true);
          break;
        case 'query':
          this.getTableData();
          break;
        case 'check':
          this.$router.push({
            path: '/patrol/task-monitor',
            query: { taskId: row.task_id, executeId: row.execute_id },
          });
          break;
        case 'delete':
          break;
        case 'edit':
          this.auditData = row;
          this.auditModal = true;
          break;
        default:
          break;
      }
    },
    onTablePageChange() {
      this.getTableData();
    },
    getTableData(reset) {
      this.tableLoad = true;
      if (reset) this.tablePage = 1;
      const { timeRange, auditStatus, patrolPoint, recogntType } = this.tableForm;
      this.$api
        .postHistoryApi('query', {
          index: 'history.item',
          from: this.tableSize * (this.tablePage - 1),
          size: this.tableSize,
          match: {
            station_id: this.stationId,
            modified: true,
            patrol_status: auditStatus ? [auditStatus] : undefined,
            patrolpoint_name: patrolPoint,
            recognition_type: recogntType,
          },
          order: { modify_time: 'desc' },
          range: { modify_time: timeRange ? timeRange.join(',') : undefined },
        })
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
              value: item.value || '-',
              modifier: item.modifier || '-',
              value_disp: item.value_disp || '-',
              modify_content: item.modify_content || '-',
              auditStatus: this.patrolStateMap[item.patrol_status] || '-',
              recogntType: this.recognitionTypeMap[item.recognition_type] || '-',
              patrolDevice: item.camera_name || item.robot_name || item.host_name || '-',
            };
          });
          this.tableTotal = Math.min(res.total, 10000);
        })
        .finally(() => {
          this.tableLoad = false;
        });
    },
  },
};
</script>
