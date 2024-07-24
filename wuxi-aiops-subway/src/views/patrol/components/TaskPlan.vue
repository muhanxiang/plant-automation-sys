<template>
  <el-dialog
    custom-class="sy-dialog sy-dialog--large"
    :visible.sync="isVisible"
    :title="title"
    center
    @open="onDialogOpen"
    @close="onDialogClose"
    @opened="onDialogOpened"
  >
    <el-row class="task-plan" v-loading="planLoad">
      <el-col class="task-plan-left" :span="8">
        <div class="task-plan-left__search">
          <el-input
            v-model.trim="pointName"
            size="small"
            placeholder="请输入点位名称"
            suffix-icon="el-icon-search"
            :maxlength="20"
            clearable
            @input="onPointSearch"
          >
          </el-input>
          <el-button class="sy-btn-regular" type="primary" size="medium" @click="handleResetTree">重置</el-button>
        </div>
        <div class="task-plan-left__wrapper">
          <sy-tree
            v-show="!pointSearch"
            class="task-plan-left__tree"
            ref="tree"
            node-key="guid"
            :data="treeData"
            :props="treeProps"
            :load="loadTreeData"
            :filter-node-method="filterTreeNode"
            lazy
            show-checkbox
            highlight-current
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
          <div v-show="pointSearch" v-loading="pointLoad" class="task-plan-left__group">
            <el-checkbox-group v-if="pointList.length > 0" v-model="pointSelect" class="sy-checkbox-list">
              <el-checkbox v-for="item in pointList" :key="item.guid" :label="item.guid">
                {{ item.name || '-' }}
              </el-checkbox>
            </el-checkbox-group>
            <sy-empty v-else-if="!pointLoad" description="暂无搜索结果"></sy-empty>
          </div>
        </div>
      </el-col>
      <el-col class="task-plan-right" :span="16">
        <el-form
          ref="addForm"
          class="task-plan-right__form sy-form"
          size="small"
          label-position="top"
          :model="planModel"
          :rules="planRules"
          inline
        >
          <div class="form-main">
            <div class="form-main-info">
              <el-form-item label="任务名称" prop="name">
                <el-input v-model="planModel.name" placeholder="请输入任务名称" :maxlength="20" clearable></el-input>
              </el-form-item>
              <el-form-item label="巡视类型" prop="patrol_type">
                <el-select v-model="planModel.patrol_type" popper-class="sy-popper" placeholder="请选择巡视类型">
                  <el-option
                    v-for="item in patrolTypes"
                    :label="item.value"
                    :value="item.key"
                    :key="item.key"
                  ></el-option>
                </el-select>
              </el-form-item>
              <!-- <el-form-item label="巡视设备" prop="source">
                <el-select

                  v-model="planModel.source"
                  popper-class="sy-popper"
                  placeholder="请选择巡视设备"
                >
                  <el-option
                    v-for="item in taskSources"
                    :label="item.value"
                    :value="item.key"
                    :key="item.key"
                  ></el-option>
                </el-select>
              </el-form-item> -->
              <el-form-item label="执行类型" prop="task_type">
                <el-select v-model="planModel.task_type" popper-class="sy-popper" placeholder="请选择执行类型">
                  <el-option
                    v-for="item in taskTypes"
                    :label="item.value"
                    :value="item.key"
                    :key="item.key"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="任务优先级" prop="priority">
                <el-radio-group class="sy-switch" v-model="planModel.priority">
                  <el-radio-button v-for="item in taskPriority" :label="item.key" :key="item.key">
                    {{ item.value }}
                  </el-radio-button>
                </el-radio-group>
              </el-form-item>
            </div>
            <div class="form-main-time">
              <!-- 定期执行 -->
              <template v-if="planModel.task_type === TaskType.REGULAR">
                <el-form-item label="执行时间:" prop="start_time">
                  <el-date-picker
                    v-model="planModel.start_time"
                    type="datetime"
                    placeholder="请选择执行时间"
                    format="yyyy-MM-dd HH:mm:ss"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    :picker-options="regularTime"
                  >
                  </el-date-picker>
                </el-form-item>
              </template>
              <!-- 周期执行 -->
              <template v-else-if="planModel.task_type === TaskType.CYCLE">
                <el-form-item label="开始时间:" prop="cycle_start_time">
                  <el-date-picker
                    v-model="planModel.cycle_start_time"
                    type="datetime"
                    placeholder="请选择开始时间"
                    format="yyyy-MM-dd HH:mm:ss"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    :picker-options="cycleStartTime"
                  >
                  </el-date-picker>
                </el-form-item>
                <el-form-item label="结束时间:" prop="cycle_end_time">
                  <el-date-picker
                    v-model="planModel.cycle_end_time"
                    type="datetime"
                    placeholder="请选结束时间"
                    default-time="23:59:59"
                    format="yyyy-MM-dd HH:mm:ss"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    :picker-options="cycleEndTime"
                  >
                  </el-date-picker>
                </el-form-item>
                <el-form-item label="执行时间">
                  <el-time-picker
                    v-model="planModel.cycle_execute_time"
                    format="HH:mm:ss"
                    value-format="HH:mm:ss"
                    placeholder="请选择执行时间"
                  >
                  </el-time-picker>
                </el-form-item>
                <el-form-item label="周期类型">
                  <el-select
                    class="sy-select"
                    v-model="planModel.cycle_type"
                    popper-class="sy-popper"
                    placeholder="请选择周期类型"
                    @change="onTaskCycleChange"
                  >
                    <el-option
                      v-for="item in taskCycles"
                      :label="item.value"
                      :value="item.key"
                      :key="item.key"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="执行周期（月）" v-if="planModel.cycle_type === TaskCycle.MONTH">
                  <el-select
                    class="sy-select"
                    v-model="planModel.cycle_month"
                    popper-class="sy-popper"
                    placeholder="请选择执行日期"
                    collapse-tags
                    multiple
                  >
                    <el-option v-for="item in taskMonths" :key="item.key" :label="item.value" :value="item.key">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="执行周期（周）" v-else>
                  <el-select
                    class="sy-select"
                    v-model="planModel.cycle_week"
                    popper-class="sy-popper"
                    placeholder="请选择执行日期"
                    collapse-tags
                    multiple
                  >
                    <el-option v-for="item in taskWeeks" :key="item.key" :label="item.value" :value="item.key">
                    </el-option>
                  </el-select>
                </el-form-item>
              </template>
              <!-- 间隔执行 -->
              <template v-else-if="planModel.task_type === TaskType.INTERVAL">
                <el-form-item label="开始时间:" prop="interval_start_time">
                  <el-date-picker
                    v-model="planModel.interval_start_time"
                    type="datetime"
                    placeholder="请选择开始时间"
                    format="yyyy-MM-dd HH:mm:ss"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    :picker-options="intervalStartTime"
                  >
                  </el-date-picker>
                </el-form-item>
                <el-form-item label="结束时间:">
                  <el-date-picker
                    v-model="planModel.interval_end_time"
                    type="datetime"
                    placeholder="请选择结束时间"
                    default-time="23:59:59"
                    format="yyyy-MM-dd HH:mm:ss"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    :picker-options="intervalEndTime"
                  >
                  </el-date-picker>
                </el-form-item>
                <el-form-item label="执行时间">
                  <el-time-picker
                    v-model="planModel.interval_execute_time"
                    format="HH:mm:ss"
                    value-format="HH:mm:ss"
                    placeholder="请选择执行时间"
                  >
                  </el-time-picker>
                </el-form-item>
                <el-form-item label="间隔类型">
                  <el-select v-model="planModel.interval_type" popper-class="sy-popper" placeholder="请选择间隔类型">
                    <el-option
                      v-for="item in taskIntervals"
                      :label="item.value"
                      :value="item.key"
                      :key="item.key"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="执行间隔">
                  <el-input-number
                    v-model="planModel.interval_number"
                    placeholder="请输入执行间隔"
                    :max="10"
                    :min="1"
                  ></el-input-number>
                </el-form-item>
              </template>
            </div>
          </div>
          <div class="form-type">
            <div class="form-type-item">
              <div class="form-type-item__header">
                <div class="form-type-item__title">设备类型</div>
                <el-checkbox label="全选" @change="allSelectDevice" v-model="checkAllDevice"></el-checkbox>
              </div>
              <el-form-item prop="device_types">
                <el-checkbox-group
                  class="form-type-item__group"
                  v-model="planModel.device_types"
                  @change="caclCheckAll"
                >
                  <el-checkbox :title="item.value" v-for="item in deviceTypes" :key="item.key" :label="item.key">
                    {{ item.value }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </div>
            <div class="form-type-item">
              <div class="form-type-item__header">
                <div class="form-type-item__title">仪表类型</div>
                <div>
                  <el-checkbox label="全选" @change="allSelectMeter" v-model="checkAllMeter"></el-checkbox>
                </div>
              </div>
              <el-form-item prop="meter_types">
                <el-checkbox-group class="form-type-item__group" @change="caclCheckAll" v-model="planModel.meter_types">
                  <el-checkbox :title="item.value" v-for="(item, i) in meterTypes" :key="i" :label="item.key">
                    {{ item.value }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </div>
            <div class="form-type-item">
              <div class="form-type-item__header">
                <div class="form-type-item__title">外观类型</div>
                <div>
                  <el-checkbox label="全选" @change="allSelectAppearance" v-model="checkAllAppearance"></el-checkbox>
                </div>
              </div>
              <el-form-item prop="appearance_types">
                <el-checkbox-group
                  class="form-type-item__group"
                  v-model="planModel.appearance_types"
                  @change="caclCheckAll"
                >
                  <el-checkbox :title="item.value" v-for="(item, i) in appearanceTypes" :key="i" :label="item.key">
                    {{ item.value }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </div>
            <div class="form-type-item">
              <div class="form-type-item__header">
                <div class="form-type-item__title">识别类型</div>
                <el-checkbox label="全选" @change="allSelectRecognition" v-model="checkAllRecognition"></el-checkbox>
              </div>
              <el-form-item prop="recognition_types">
                <el-checkbox-group
                  class="form-type-item__group"
                  @change="caclCheckAll"
                  v-model="planModel.recognition_types"
                >
                  <el-checkbox :title="item.value" v-for="(item, i) in recognitionTypes" :key="i" :label="item.key">
                    {{ item.value }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </div>
          </div>
        </el-form>
      </el-col>
    </el-row>

    <template #footer>
      <el-button class="sy-btn-regular" type="warning" @click="onDialogAction('cancel')">取 消</el-button>
      <el-button class="sy-btn-regular" type="primary" @click="onDialogAction('submit')">提 交</el-button>
    </template>
  </el-dialog>
</template>

<script>
import { mapGetters } from 'vuex';
import { debounce } from 'lodash';
import { TaskType, TaskCycle, TaskInterval } from '@/enums';
import Option from '@/enums/option';
import TaskTree from '@/mixins/task-tree';

export default {
  name: 'TaskPlan',
  mixins: [TaskTree],
  computed: {
    ...mapGetters(['stationId']),
    isVisible: {
      get() {
        return this.visible;
      },
      set(bool) {
        this.$emit('update:visible', bool);
      },
    },
    title() {
      return this.guid ? '任务方案编辑' : '任务方案新增';
    },
    TaskType() {
      return TaskType;
    },
    TaskCycle() {
      return TaskCycle;
    },
    taskTypes() {
      return Option.TaskTypes;
    },
    taskWeeks() {
      return Option.TaskWeeks;
    },
    taskMonths() {
      return Option.TaskMonths;
    },
    taskCycles() {
      return Option.TaskCycles;
    },
    taskSources() {
      return Option.TaskSources;
    },
    taskPriority() {
      return Option.TaskPriorities;
    },
    taskIntervals() {
      return Option.TaskIntervals;
    },
    meterTypes() {
      return Option.MeterTypes;
    },
    deviceTypes() {
      return Option.DeviceTypes;
    },
    patrolTypes() {
      return Option.PatrolTypes;
    },
    appearanceTypes() {
      return Option.AppearanceTypes;
    },
    recognitionTypes() {
      return Option.RecognitionTypes;
    },
    pointSearch() {
      return this.pointName.length > 0;
    },
    regularTime() {
      return {
        disabledDate: (date) => {
          return this.$dayjs(date).isBefore(this.$dayjs(), 'day');
        },
      };
    },
    cycleStartTime() {
      return {
        disabledDate: (date) => {
          return (
            this.$dayjs(date).isBefore(this.$dayjs(), 'day') ||
            (this.planModel.cycle_end_time && this.$dayjs(date).isAfter(this.planModel.cycle_end_time, 'day'))
          );
        },
      };
    },
    cycleEndTime() {
      return {
        disabledDate: (date) => {
          return this.$dayjs(date).isBefore(this.planModel.cycle_start_time || this.$dayjs(), 'day');
        },
      };
    },
    intervalStartTime() {
      return {
        disabledDate: (date) => {
          return (
            this.$dayjs(date).isBefore(this.$dayjs(), 'day') ||
            (this.planModel.interval_end_time && this.$dayjs(date).isAfter(this.planModel.interval_end_time, 'day'))
          );
        },
      };
    },
    intervalEndTime() {
      return {
        disabledDate: (date) => {
          return this.$dayjs(date).isBefore(this.planModel.interval_start_time || this.$dayjs(), 'day');
        },
      };
    },
    planRules() {
      const isIntervalTask = this.planModel.task_type === TaskType.INTERVAL;
      const isRegularTask = this.planModel.task_type === TaskType.REGULAR;
      const isCycleTask = this.planModel.task_type === TaskType.CYCLE;
      const isCycleWeek = this.planModel.cycle_type === TaskCycle.WEEK;
      const isCycleMonth = this.planModel.cycle_type === TaskCycle.MONTH;
      return {
        name: [{ type: 'string', required: true, message: '请输入任务名称' }],
        // source: [{ type: 'number',required: true, message: '请选择巡视设备' }],
        priority: [{ type: 'number', required: true, message: '请选择任务优先级' }],
        task_type: [{ type: 'number', required: true, message: '请选择执行类型' }],
        patrol_type: [{ type: 'number', required: true, message: '请选择巡视类型' }],
        start_time: [{ type: 'string', required: isRegularTask, message: '请选择定期执行时间' }],
        cicle_type: [{ type: 'number', required: isCycleTask, message: '请选择周期类型' }],
        cycle_week: [{ type: 'string', required: isCycleTask && isCycleWeek, message: '请选择执行周期' }],
        cycle_month: [{ type: 'string', required: isCycleTask && isCycleMonth, message: '请选择执行周期' }],
        cycle_end_time: [{ type: 'string', required: isCycleTask, message: '请选择周期结束时间' }],
        cycle_start_time: [{ type: 'string', required: isCycleTask, message: '请选择周期开始时间' }],
        cycle_execute_time: [{ type: 'string', required: isCycleTask, message: '请选择周期执行时间' }],
        interval_type: [{ type: 'number', required: isIntervalTask, message: '请选择间隔类型' }],
        interval_number: [{ type: 'number', required: isIntervalTask, message: '请选择间隔周期' }],
        interval_end_time: [{ type: 'string', required: isIntervalTask, message: '请选择间隔结束时间' }],
        interval_start_time: [{ type: 'string', required: isIntervalTask, message: '请选择间隔开始时间' }],
        interval_execute_time: [{ type: 'string', required: isIntervalTask, message: '请选择间隔执行时间' }],
      };
    },
  },
  props: {
    guid: {
      type: String,
      default: '',
    },
    visible: {
      type: Boolean,
      default: false,
    },
  },
  watch: {
    pointSearch(bool) {
      if (bool) {
        const checkedKeys = this.$refs.tree.getCheckedKeys(true);
        this.pointSelect = Array.from(new Set([...this.pointSelect, ...checkedKeys]));
      } else {
        this.$nextTick(() => {
          this.pointSelect && this.$refs.tree.setCheckedKeys(this.pointSelect);
        });
      }
    },
  },
  data() {
    return {
      intervalType: 0,
      pointName: '',
      pointList: [],
      pointSelect: [],
      deviceAreas: [],
      checkedNodes: [],
      pointLoad: false,
      checkAllMeter: false,
      checkAllDevice: false,
      checkAllAppearance: false,
      checkAllRecognition: false,
      planLoad: false,
      planModel: {
        name: '', // 方案名称
        items: [], // 树形菜单
        source: 1, // 巡视设备
        priority: 2, // 任务优先级
        task_type: undefined, // 执行类型
        patrol_type: undefined, // 巡视类型
        start_time: undefined, // 执行时间
        cycle_type: TaskCycle.MONTH, // 周期类型
        cycle_week: '', // 周期-周
        cycle_month: '', // 周期-月
        cycle_end_time: undefined, // 周期结束时间
        cycle_start_time: undefined, // 周期开始时间
        cycle_execute_time: undefined, // 周期执行时间
        interval_type: TaskInterval.HOUR, // 间隔类型
        interval_number: 0, // 间隔数量
        interval_end_time: undefined, // 间隔结束时间
        interval_start_time: undefined, // 间隔开始时间
        interval_execute_time: undefined, // 间隔执行时间
        meter_types: [],
        device_types: [],
        appearance_types: [],
        recognition_types: [],
      },
    };
  },
  created() {
    this.getDeviceAreas();
  },
  methods: {
    handleResetTree() {
      this.pointName = '';
      this.pointList = [];
      this.pointSelect = [];
      this.$refs.tree.setCheckedKeys([]);
    },
    // 搜索巡视点位
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
    async onDialogOpen() {
      // 编辑时查询详情
      if (!this.guid) return;
      this.planLoad = true;

      const task = await this.$api.getTaskApi(`query/${this.guid}`, {});
      this.planModel = {
        station_id: this.stationId,
        guid: this.guid,
        name: task.name, // 巡检名称
        items: [], // 树形菜单
        source: task.source, // 巡视设备
        priority: task.priority, // 任务优先级
        task_type: task.task_type, // 执行类型
        patrol_type: task.patrol_type, // 巡视类型
        start_time: task.start_time, // 执行时间
        cycle_type: task.cycle_month !== '0' ? TaskCycle.MONTH : TaskCycle.WEEK,
        cycle_week: task.cycle_week ? task.cycle_week.split(',').map(Number) : [], // 周期（日）
        cycle_month: task.cycle_month ? task.cycle_month.split(',').map(Number) : [], // 周期（月）
        cycle_end_time: task.cycle_end_time, // 周期结束时间
        cycle_start_time: task.cycle_start_time, // 周期开始时间
        cycle_execute_time: task.cycle_execute_time, // 周期执行时间
        interval_type: task.interval_type, // 间隔类型
        interval_number: task.interval_number, // 间隔数量
        interval_end_time: task.interval_end_time, // 间隔结束时间
        interval_start_time: task.interval_start_time, // 间隔开始时间
        interval_execute_time: task.interval_execute_time, // 间隔执行时间
        meter_types: task.meter_types.split(',').map(Number), // 仪表类型
        device_types: task.device_types.split(',').map(Number), // 设备类型
        appearance_types: task.appearance_types.split(',').map(Number), // 外观类型
        recognition_types: task.recognition_types.split(',').map(Number), // 识别类型
      };

      this.caclCheckAll();

      this.checkedNodes = task.items.map((item) => ({
        ...item,
        guid: item.station || item.area || item.bay || item.device || item.component || item.patrolpoint,
        level: item.relate_type,
      }));
      let itemsCheckedNodes = this.checkedNodes.map((item) => item.guid);
      this.$nextTick(() => {
        this.$refs.tree.setCheckedKeys(itemsCheckedNodes);

        this.planLoad = false;
      });
    },
    onDialogClose() {
      this.$refs.addForm.resetFields();
    },
    onDialogOpened() {
      this.$refs.addForm.clearValidate();
      // 编辑时不需要全选
      if (this.guid) return;
      this.allSelectMeter(true);
      this.allSelectDevice(true);
      this.allSelectAppearance(true);
      this.allSelectRecognition(true);
    },
    async onDialogAction(action) {
      switch (action) {
        case 'cancel':
          this.isVisible = false;
          break;
        case 'submit':
          {
            const vali = await this.$refs.addForm.validate();
            if (!vali) return;

            let planPoints = [];
            if (this.pointSearch) {
              if (this.pointSelect.length === 0) {
                return this.$message({
                  type: 'warning',
                  message: '请选择巡视点位！',
                });
              }
              planPoints = this.pointSelect.map((guid) => ({ patrolpoint_id: guid, relate_type: 5 }));
            } else {
              if (this.checkedNodes.length === 0) {
                return this.$message({
                  type: 'warning',
                  message: '请选择巡视点位！',
                });
              }
              // 编辑提交时处理父子节点同时存在情况
              for (let i = 0; i < this.checkedNodes.length; i++) {
                const node = this.checkedNodes[i];
                // 如果父节点存在则要移除其子节点
                if (node && node.level >= 2 && node.children && node.children.length > 0) {
                  node.children.forEach((child) => {
                    const index = this.checkedNodes.findIndex((item) => item && item.guid === child.guid);
                    if (index > -1) this.checkedNodes[index] = null;
                  });
                }
              }
              this.checkedNodes.forEach((item) => {
                switch (item && item.level) {
                  case 0:
                    planPoints.push({ station_id: item.guid, relate_type: item.level });
                    break;
                  case 1:
                    planPoints.push({ area_id: item.guid, relate_type: item.level });
                    break;
                  case 2:
                    planPoints.push({ bay_id: item.guid, relate_type: item.level });
                    break;
                  case 3:
                    planPoints.push({ device_id: item.guid, relate_type: item.level });
                    break;
                  case 4:
                    planPoints.push({ component_id: item.guid, relate_type: item.level });
                    break;
                  case 5:
                    planPoints.push({ patrolpoint_id: item.guid, relate_type: item.level });
                    break;
                  default:
                    break;
                }
              });
            }
            const isIntervalTask = this.planModel.task_type === TaskType.INTERVAL;
            const isCycleTask = this.planModel.task_type === TaskType.CYCLE;
            const isCycleWeek = this.planModel.cycle_type === TaskCycle.WEEK;
            const isCycleMonth = this.planModel.cycle_type === TaskCycle.MONTH;
            const planModel = {
              station_id: this.stationId,
              guid: this.guid || undefined,
              items: planPoints,
              name: this.planModel.name,
              source: this.planModel.source,
              priority: this.planModel.priority,
              task_type: this.planModel.task_type,
              patrol_type: this.planModel.patrol_type,
              start_time: this.planModel.task_type !== TaskType.IMMEDIATE ? this.planModel.start_time || null : null,
              cycle_week: isCycleTask ? (isCycleWeek ? this.planModel.cycle_week.join(',') : 0) : 0,
              cycle_month: isCycleTask ? (isCycleMonth ? this.planModel.cycle_month.join(',') : 0) : 0,
              cycle_end_time: isCycleTask ? this.planModel.cycle_end_time || null : null,
              cycle_start_time: isCycleTask ? this.planModel.cycle_start_time || null : null,
              cycle_execute_time: isCycleTask ? this.planModel.cycle_execute_time : null,
              interval_type: isIntervalTask ? this.planModel.interval_type : TaskInterval.HOUR,
              interval_number: isIntervalTask ? this.planModel.interval_number : 0,
              interval_end_time: isIntervalTask ? this.planModel.interval_end_time || null : null,
              interval_start_time: isIntervalTask ? this.planModel.interval_start_time || null : null,
              interval_execute_time: isIntervalTask ? this.planModel.interval_execute_time : null,
              meter_types: this.planModel.meter_types.length > 0 ? this.planModel.meter_types.join(',') : '',
              device_types: this.planModel.device_types.length > 0 ? this.planModel.device_types.join(',') : '',
              appearance_types:
                this.planModel.appearance_types.length > 0 ? this.planModel.appearance_types.join(',') : '',
              recognition_types:
                this.planModel.recognition_types.length > 0 ? this.planModel.recognition_types.join(',') : '',
            };

            if (this.guid) {
              this.$api
                .postTaskApi(`update/${this.guid}`, {
                  data: planModel,
                })
                .then((res) => {
                  if (res?.success) {
                    this.$emit('success');

                    this.$message({
                      type: 'success',
                      message: '任务编辑成功！',
                    });
                    this.$nextTick(() => {
                      this.$refs.tree.setCheckedKeys([]);
                    });
                  } else {
                    this.$message({
                      type: 'error',
                      message: res.detail,
                    });
                  }
                });
            } else {
              this.$api
                .postTaskApi('create', {
                  data: planModel,
                })
                .then((res) => {
                  if (res?.success) {
                    this.$emit('success');

                    this.$message({
                      type: 'success',
                      message: '任务新增成功！',
                    });
                    this.$nextTick(() => {
                      this.$refs.tree.setCheckedKeys([]);
                    });
                  } else {
                    this.$message({
                      type: 'error',
                      message: res.detail,
                    });
                  }
                });
            }
          }
          break;
        default:
          break;
      }
    },
    // 树结构复选框
    onNodeCheckChange(data, checked) {
      //checkBox中不考虑父子关联情况,提交的时候处理
      if (checked) {
        //选中并且不存在处理
        if (!this.checkedNodes.find((item) => item && item.guid === data.guid)) {
          this.checkedNodes.push(data);
        }
      } else {
        //子节点取消选中干掉
        const index = this.checkedNodes.findIndex((item) => item && item.guid === data.guid);
        if (index > -1) {
          this.checkedNodes.splice(index, 1);
        }
      }
    },
    onTaskCycleChange() {
      this.planModel.cycle_week = '';
      this.planModel.cycle_month = '';
    },
    allSelectMeter(val) {
      this.checkAllMeter = val;
      if (val) {
        this.planModel.meter_types = this.meterTypes.map((item) => item.key);
      } else {
        this.planModel.meter_types = [];
      }
    },
    // 设备类型全选
    allSelectDevice(val) {
      this.checkAllDevice = val;
      if (val) {
        this.planModel.device_types = this.deviceTypes.map((item) => item.key);
      } else {
        this.planModel.device_types = [];
      }
    },
    // 外观类型全选
    allSelectAppearance(val) {
      this.checkAllAppearance = val;
      if (val) {
        this.planModel.appearance_types = this.appearanceTypes.map((item) => item.key);
      } else {
        this.planModel.appearance_types = [];
      }
    },
    // 识别类型全选
    allSelectRecognition(val) {
      this.checkAllRecognition = val;
      if (val) {
        this.planModel.recognition_types = this.recognitionTypes.map((item) => item.key);
      } else {
        this.planModel.recognition_types = [];
      }
    },
    // 计算是否全选
    caclCheckAll() {
      this.checkAllMeter = this.planModel.meter_types.length === this.meterTypes.length;
      this.checkAllDevice = this.planModel.device_types.length === this.deviceTypes.length;
      this.checkAllRecognition = this.planModel.recognition_types.length === this.recognitionTypes.length;
      this.checkAllAppearance = this.planModel.appearance_types.length === this.appearanceTypes.length;
    },
    // 仪表类型全选
    getDeviceAreas() {
      this.$api.getBaseApi('area').then((res) => {
        if (!res) return;
        this.deviceAreas = res.results.map((item) => {
          return { key: item.id, value: item.name };
        });
      });
    },

    // 只要执行方式改变则清除所有周期时间等
    taskTypeChange() {
      this.planModel.start_time = undefined;
      this.planModel.cycle_type = TaskCycle.MONTH;
      this.planModel.cycle_week = [];
      this.planModel.cycle_month = [];
      this.planModel.cycle_end_time = undefined;
      this.planModel.cycle_start_time = undefined;
      this.planModel.cycle_execute_time = undefined;
      this.planModel.interval_type = TaskInterval.HOUR;
      this.planModel.interval_number = 0;
      this.planModel.interval_end_time = undefined;
      this.planModel.interval_start_time = undefined;
      this.planModel.interval_execute_time = undefined;
    },
  },
};
</script>

<style lang="scss" scoped>
.task-plan {
  width: 100%;
  height: 100%;
  border: $border-default;

  &-left {
    display: flex;
    flex-direction: column;
    height: 100%;
    padding: 10px;
    border-right: $border-default;

    &__search {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 1vh;

      > .el-button {
        margin-left: 10px;
      }
    }
    &__wrapper {
      flex: auto;
      width: 100%;
      overflow: hidden;
    }
    &__tree {
      width: 100%;
      height: 100%;
      overflow: auto;
    }
    &__group {
      width: 100%;
      height: 100%;
      overflow: auto;
    }
  }

  &-right {
    display: flex;
    flex-direction: column;
    height: 100%;
    overflow: hidden auto;

    &__form {
      display: flex;
      flex-direction: column;
      height: 100%;

      .form-main {
        flex: none;
        width: 100%;
        padding: 10px;

        ::v-deep .el-form-item {
          &__label {
            padding-bottom: 5px;
          }
          &__content {
            > .el-select,
            > .el-date-editor,
            > .el-input-number {
              width: 100%;
            }
          }
        }

        &-info {
          display: grid;
          grid-auto-rows: 70px;
          grid-template-columns: repeat(3, minmax(33.33%, 1fr));
        }
        &-time {
          display: grid;
          grid-auto-rows: 70px;
          grid-template-columns: repeat(3, 33.33%);
          gap: 5px;
          overflow: hidden;
        }
      }
      .form-type {
        flex: auto;
        width: 100%;
        overflow-y: auto;

        &-item {
          .el-form-item {
            margin: 0;
          }

          &__header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 10px 20px;
            background: {
              image: url('~@/assets/img/bg_checkbox.png');
              repeat: no-repeat;
              size: 100% 100%;
            }
          }
          &__group {
            display: grid;
            padding: 10px;
            grid-template-columns: repeat(5, minmax(120px, 1fr));

            ::v-deep .el-checkbox {
              display: flex;
              align-items: center;
              padding: 5px;
              width: 100%;

              &__label {
                font-size: 14px;
                flex: 1;
                @include text-ellipsis();
              }
            }
          }
        }
      }
    }
  }
}
</style>
