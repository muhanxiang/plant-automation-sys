<template>
  <el-row id="defect_record" class="sy-row">
    <div class="sy-table-header flex-auto">
      <el-form class="sy-table-form search-form" ref="tableForm" :model="tableForm" inline>
        <el-form-item prop="deviceName" label="设备名称:">
          <el-input
            v-model="tableForm.deviceName"
            placeholder="请输入设备名称"
            :maxlength="20"
            clearable
            @change="getTableData(true)"
          ></el-input>
        </el-form-item>
        <el-form-item prop="defectStatus" label="缺陷状态:">
          <el-select
            v-model="tableForm.defectStatus"
            placeholder="请选择缺陷状态"
            popper-class="sy-popper"
            clearable
            @change="getTableData(true)"
          >
            <el-option :value="1">已消缺</el-option>
            <el-option :value="0">未消缺</el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="timeRange" label="缺陷时间:">
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
            <el-button
              class="sy-btn-corner sy-btn-corner--bl"
              icon="el-icon-folder"
              size="small"
              @click="handleTableAction('export')"
              >导出
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
    <el-table class="sy-table" v-loading="tableLoad" :data="tableData" border>
      <sy-empty slot="empty" description="暂无缺陷记录"></sy-empty>

      <el-table-column
        v-for="column in tableColumns"
        :key="column.prop"
        v-bind="column"
        align="center"
        show-overflow-tooltip
      >
        <template v-if="column.prop === 'operation'" v-slot="{ row }">
          <el-button class="sy-btn-regular" type="primary" size="mini" @click="handleTableAction('eliminate', row)"
            >消缺
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
  </el-row>
</template>

<script>
import { mapGetters } from 'vuex';
import { arrToMap } from '@/utils';
import Option from '@/enums/option';

export default {
  name: 'DefectRecord',
  computed: {
    ...mapGetters(['stationId']),
    defectStateMap() {
      return { 0: '未消缺', 1: '已消缺' };
    },
    defectSourceMap() {
      return arrToMap(Option.TaskSources);
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
        { prop: 'measure_disp', label: '测量内容', minWidth: 100 },
        { prop: 'measure_value', label: '测量值' },
        { prop: 'defect_disp', label: '缺陷内容', minWidth: 100 },
        { prop: 'defectStatus', label: '缺陷状态' },
        { prop: 'defectSource', label: '缺陷来源' },
        { prop: 'defect_time', label: '缺陷时间' },
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
        timeRange: [
          this.$dayjs().subtract(1, 'week').format('YYYY-MM-DD HH:mm:ss'),
          this.$dayjs().format('YYYY-MM-DD HH:mm:ss'),
        ],
        deviceName: '',
        defectDisp: '',
        defectStatus: undefined,
      },
    };
  },
  created() {
    this.getTableData();
  },
  methods: {
    handleTableAction(action, row) {
      switch (action) {
        case 'reset':
          this.$refs.tableForm.resetFields();
          this.getTableData(true);
          break;
        case 'query':
          this.getTableData();
          break;
        case 'eliminate':
          console.log(row);
          break;
        case 'export':
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
      const { timeRange, deviceName, defectDisp, defectStatus } = this.tableForm;
      this.$api
        .postHistoryApi('query', {
          index: 'history.alarm',
          from: this.tableSize * (this.tablePage - 1),
          size: this.tableSize,
          match: {
            station_id: this.stationId,
            device_name: deviceName,
            defect_disp: defectDisp,
            defect_status: defectStatus,
          },
          order: { defect_time: 'desc' },
          range: { defect_time: timeRange ? timeRange.join(',') : undefined },
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
              defectStatus: this.defectStateMap[item.defect_status] || '-',
              defectSource: this.defectSourceMap[item.defect_source] || '-',
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
