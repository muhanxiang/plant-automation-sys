<template>
  <TreeView @selected="handleTreeSelect">
    <div class="sy-table-header flex-auto">
      <el-form class="sy-table-header__form" ref="tableForm" :model="tableForm" inline>
        <el-form-item prop="alarmType" label="告警类型:">
          <el-select
            v-model="tableForm.alarmType"
            placeholder="请选择告警类型"
            popper-class="sy-popper"
            clearable
            @change="getTableData(true)"
          >
            <el-option v-for="(val, key) in alarmTypeMap" :key="key" :label="val" :value="+key"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="alarmLevel" label="告警等级:">
          <el-select
            v-model="tableForm.alarmLevel"
            placeholder="请选择告警等级"
            popper-class="sy-popper"
            clearable
            @change="getTableData(true)"
          >
            <el-option v-for="(val, key) in alarmLevelMap" :key="key" :label="val" :value="+key"></el-option>
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
        </el-form-item>
      </el-form>
    </div>
    <el-table class="sy-table" v-loading="tableLoad" :data="tableData" border>
      <sy-empty slot="empty" description="暂无巡视告警"></sy-empty>

      <el-table-column
        v-for="column in tableColumns"
        :key="column.prop"
        v-bind="column"
        align="center"
        show-overflow-tooltip
      >
        <template v-if="column.prop === 'alarm'" v-slot="{ row }">
          <div v-if="row.alarmList && row.alarmList.length > 0" class="el-tag-list">
            <el-tag
              v-for="(item, index) in row.alarmList"
              class="sy-tag"
              color="transparent"
              effect="plain"
              size="medium"
              :key="index"
              :type="item.alarmStatus"
              :title="item.alarmType"
            >
              {{ `${item.name}【${item.alarmLevel}】` }}
            </el-tag>
          </div>
          <span v-else>{{ row.value_disp || '-' }}</span>
        </template>
        <template v-else-if="column.prop === 'modified'" v-slot="{ row }">
          {{ row.modified ? '已审核' : '未审核' }}
        </template>
        <template v-else-if="column.prop === 'operation'" v-slot="{ row, $index }">
          <el-button
            class="sy-btn-corner sy-btn-corner--br"
            size="mini"
            @click="handleTableAction('audit', row, $index)"
            >审核
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

    <PatrolResult :visible.sync="patrolModal" :data="patrolResult" @success="handleSubmitSuccess"></PatrolResult>
  </TreeView>
</template>

<script>
import { mapGetters } from 'vuex';
import { saveAs } from 'file-saver';
import { AlarmLevelMap } from '@/enums/map';
import { arrToMap, getReportFullPath } from '@/utils';

import Option from '@/enums/option';
import TreeView from '@/components/TreeView';

export default {
  name: 'PatrolAlarm',
  components: {
    TreeView,
    PatrolResult: () => import('../components/PatrolResult'),
  },
  data() {
    return {
      tableLoad: false,
      tablePage: 1,
      tableSize: 10,
      tableIndex: 0,
      tableTotal: 0,
      tableData: [],
      tableForm: {
        timeRange: [],
        alarmType: undefined,
        alarmLevel: undefined,
      },
      patrolModal: false,
      patrolResult: {},
    };
  },
  computed: {
    ...mapGetters(['stationId']),
    alarmTypeMap() {
      return arrToMap(Option.AlarmTypes);
    },
    alarmLevelMap() {
      return arrToMap(Option.AlarmLevels);
    },
    patrolStateMap() {
      return arrToMap(Option.PatrolStates);
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
        { prop: 'patrolResult', label: '巡视结果' },
        { prop: 'patrolStatus', label: '巡视结论' },
        { prop: 'patrol_time', label: '巡视时间', minWidth: 100 },
        { prop: 'alarm', label: '告警信息', minWidth: 100 },
        { prop: 'modified', label: '是否审核' },
        { prop: 'operation', label: '人工操作', minWidth: 150 },
      ];
    },
  },
  created() {
    this.getTableData();
  },
  methods: {
    handleTreeSelect(stationId) {
      this.stationId = stationId;
      this.getTableData();
    },
    handleTableAction(action, row, index) {
      switch (action) {
        case 'query':
          this.getTableData();
          break;
        case 'reset':
          this.$refs.tableForm.resetFields();
          this.getTableData(true);
          break;
        case 'audit':
          this.tableIndex = index;
          this.patrolResult = row;
          this.patrolModal = true;
          break;
        case 'export':
          this.getReport();
          break;
        default:
          break;
      }
    },
    handleSubmitSuccess() {
      setTimeout(() => {
        this.getTableData();
      }, 500);
    },
    onTablePageChange() {
      this.getTableData();
    },
    getTableData(reset) {
      this.tableLoad = true;
      if (reset) this.tablePage = 1;
      const { timeRange, alarmType, alarmLevel } = this.tableForm;
      this.$api
        .postHistoryApi('query', {
          index: 'history.item',
          from: this.tableSize * (this.tablePage - 1),
          size: this.tableSize,
          match: {
            station_id: this.stationId,
            patrol_status: [2],
            'alarm.type': alarmType,
            'alarm.level': alarmLevel,
          },
          order: { patrol_time: 'desc' },
          range: { patrol_time: timeRange ? timeRange.join(',') : undefined },
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
              patrolResult: item.value_disp || item.value || '-',
              patrolStatus: this.patrolStateMap[item.patrol_status] || '-',
              patrolDevice: item.camera_name || item.robot_name || item.host_name || '-',
              alarmList:
                item.alarm &&
                item.alarm.map((alarm) => ({
                  ...alarm,
                  alarmType: this.alarmTypeMap[alarm.type] || '-',
                  alarmLevel: this.alarmLevelMap[alarm.level] || '-',
                  alarmStatus: AlarmLevelMap[alarm.level] || '-',
                })),
            };
          });
          this.tableTotal = Math.min(res.total, 10000);
        })
        .finally(() => {
          this.tableLoad = false;
        });
    },
    getReport() {
      if (this.tableTotal === 0) {
        this.$message({
          type: 'warning',
          message: '暂无数据可导出！',
        });
      } else {
        this.tableLoad = true;
        const { timeRange, alarmType, alarmLevel } = this.tableForm;
        this.$api
          .postHistoryApi('reportAlarm', {
            size: this.tableTotal > 200 ? 200 : this.tableTotal,
            match: {
              station_id: this.stationId,
              patrol_status: [2],
              'alarm.type': alarmType,
              'alarm.level': alarmLevel,
            },
            //order: { patrol_time: 'desc' },
            range: { patrol_time: timeRange ? timeRange.join(',') : undefined },
          })
          .then((res) => {
            if (res.result === -1) {
              this.$message({
                type: 'error',
                message: '数据导出异常！',
              });
              this.tableLoad = false;
            } else {
              const index = res.msg.lastIndexOf('/');
              const title = res.msg.substring(index + 1, res.msg.length);
              console.log(getReportFullPath(res.msg), title);
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
                }, 2000);
              }, 3000);
            }
          })
          .catch(() => {
            this.tableLoad = false;
          });
      }
    },
  },
};
</script>
