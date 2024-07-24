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
        <el-form-item prop="timeRange" label="监测时间:">
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
        </el-form-item>
      </el-form>
    </div>
    <el-table class="sy-table" v-loading="tableLoad" :data="tableData" border>
      <sy-empty slot="empty" description="暂无状态告警"></sy-empty>

      <el-table-column
        v-for="column in tableColumns"
        :key="column.prop"
        v-bind="column"
        align="center"
        show-overflow-tooltip
      >
        <template v-if="column.prop === 'alarmName'" v-slot="{ row }">
          <el-tag
            class="sy-tag"
            color="transparent"
            effect="plain"
            size="medium"
            :type="row.alarmStatus"
            :title="row.alarmType"
          >
            {{ `${row.alarmName}【${row.alarmLevel}】` }}
          </el-tag>
        </template>
        <template v-else-if="column.prop === 'modified'" v-slot="{ row }">
          {{ row.modified ? '已审核' : '未审核' }}
        </template>
        <template v-else-if="column.prop === 'operation'" v-slot="{ row, $index }">
          <el-button
            class="sy-btn-corner sy-btn-corner--br"
            size="mini"
            @click="handleTableAction('maintain', row, $index)"
            >维护
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
    <MonitorInfo :visible.sync="monitorModal" :data="monitorData" @success="getTableData(true)" />
  </TreeView>
</template>

<script>
import { mapGetters } from 'vuex';
import { PatrolStatus } from '@/enums';
import { AlarmLevelMap } from '@/enums/map';
import { arrToMap, formatDateTime } from '@/utils';

import Option from '@/enums/option';
import TreeView from '@/components/TreeView';

export default {
  name: 'StatusAlarm',
  components: {
    TreeView,
    MonitorInfo: () => import('../components/MonitorInfo'),
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
      monitorModal: false,
      monitorData: {},
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
        { prop: 'stationName', label: '站所名称' },
        { prop: 'commpointName', label: '监测点位', minWidth: 100 },
        { prop: 'value_disp', label: '监测结果' },
        { prop: 'timeFmt', label: '监测时间' },
        { prop: 'alarmName', label: '告警信息' },
        { prop: 'alarmType', label: '告警类型' },
        // { prop: 'alarmLevel', label: '告警等级' },
        { prop: 'isSolved', label: '是否解决' },
        { prop: 'operation', label: '人工操作', minWidth: 100 },
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
    async handleConfirmResult(form) {
      const { result } = await this.$api.postHistoryApi('update', form);
      this.monitorModal = false;
      if (result === 1) {
        this.$message({
          type: 'success',
          message: '巡视结果审核成功！',
        });
        const tableItem = this.tableData[this.tableIndex];
        if (form.data.patrol_status === PatrolStatus.WARNING) {
          this.tableData.splice(this.tableIndex, 1, {
            ...tableItem,
            ...form.data,
            patrolStatus: this.patrolStateMap[form.data.patrol_status] || '-',
          });
        } else {
          this.tableData.splice(this.tableIndex, 1);
        }
      }
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
        case 'maintain':
          this.tableIndex = index;
          this.monitorData = row;
          this.monitorModal = true;
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
      const { timeRange, alarmType, alarmLevel } = this.tableForm;
      this.$api
        .getBaseApi('commpointalarm', {
          limit: this.tableSize,
          offset: this.tableSize * (this.tablePage - 1),
          alarm__alarm_type: alarmType,
          alarm__alarm_level: alarmLevel,
          time__range: timeRange && timeRange.join(','),
        })
        .then((res) => {
          if (!res?.results) {
            this.tableData = [];
            this.tableTotal = 0;
            return;
          }
          this.tableData = res.results.map((item) => {
            return {
              ...item,
              stationName: item.station?.name || '-',
              commpointName: item.commpoint?.name,
              alarmName: item.alarm?.name || '-',
              alarmType: this.alarmTypeMap[item.alarm?.alarm_type] || '-',
              alarmLevel: this.alarmLevelMap[item.alarm?.alarm_level] || '-',
              alarmStatus: AlarmLevelMap[item.alarm?.alarm_level] || '-',
              timeFmt: item.time ? formatDateTime(item.time) : '-',
              isSolved: item.solved ? '已解决' : '未解决',
            };
          });
          this.tableTotal = res.count;
        })
        .finally(() => {
          this.tableLoad = false;
        });
    },
  },
};
</script>
