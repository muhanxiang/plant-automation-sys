<template>
  <el-dialog
    center
    custom-class="sy-dialog sy-dialog--medium"
    :visible.sync="isVisible"
    :title="title"
    append-to-body
    @open="onDialogOpen"
    @close="onDialogClose"
  >
    <div class="action-name">
      <span>联动名称：</span>
      <el-input
        class="action-name__input"
        v-model="form.name"
        placeholder="请输入联动名称"
        size="mini"
        clearable
        style="width: 300px"
      ></el-input>
    </div>
    <el-row class="sy-row add-action_wrapper">
      <el-col class="sy-col col-left" :span="8">
        <div class="action_title">信号类型</div>
        <el-select
          class="sy-select"
          v-model="form.signalType"
          placeholder="请选择信号类型"
          popper-class="sy-popper"
          style="width: 100%"
          value-key="value"
          size="medium"
          @change="form.signalContent = ''"
        >
          <el-option v-for="item in linkageSignals" :value="item.key" :label="item.value" :key="item.key">
            {{ item.value }}
          </el-option>
        </el-select>
        <div class="action_title">信号名称</div>
        <div>
          <el-input v-model="signalText" placeholder="请输入信号名称" size="medium" :maxlength="20" clearable>
            <sy-icon slot="suffix" name="filter" style="width: 22px; height: 80%; margin-top: 3px" />
          </el-input>
        </div>
        <div class="content-list">
          <template v-if="form.signalType === linkageSignalEnum.YX_SIGNAL">
            <template v-if="filterYxData && filterYxData.length > 0">
              <div
                class="content-item"
                @click="updateSignalContent(signal)"
                :class="{ 'is-selected': form.signalContent === signal.guid, disabled: linkageDisabled }"
                v-for="signal in filterYxData"
                :key="signal.id"
              >
                {{ signal.name }}
              </div>
            </template>
            <sy-empty v-else></sy-empty>
          </template>
          <template v-else-if="form.signalType === linkageSignalEnum.YC_SIGNAL">
            <template v-if="filterYcData && filterYcData.length > 0">
              <div
                class="content-item"
                @click="updateSignalContent(signal)"
                :class="{ 'is-selected': form.signalContent === signal.guid, disabled: linkageDisabled }"
                v-for="signal in filterYcData"
                :key="signal.id"
              >
                {{ signal.name }}
              </div>
            </template>
          </template>
          <template v-else-if="form.signalType === linkageSignalEnum.ZF_SIGNAL">
            <template v-if="filterZfData && filterZfData.length > 0">
              <div
                class="content-item"
                @click="updateSignalContent(signal)"
                :class="{ 'is-selected': form.signalContent === signal.guid, disabled: linkageDisabled }"
                v-for="signal in filterZfData"
                :key="signal.id"
              >
                {{ signal.name }}
              </div>
            </template>
            <sy-empty v-else></sy-empty>
          </template>
          <sy-empty v-else-if="form.signalType === ''"></sy-empty>
        </div>
      </el-col>
      <el-col class="sy-col col-center" :span="8">
        <div class="center-wrarpper">
          <div class="center-bg">触发条件</div>
          <div class="center-condition">
            <div
              class="condition-item"
              v-for="condition in linkageSymbols"
              :key="condition.key"
              :class="{ 'active-condition': form.condition === condition.key, disabled: linkageDisabled }"
            >
              <div class="condition-left">
                <el-checkbox
                  :disabled="linkageDisabled"
                  :value="form.condition === condition.key"
                  @change="conditionChange(condition.key, $event)"
                ></el-checkbox>
              </div>
              <div class="condition-right">{{ condition.value }}</div>
            </div>
          </div>
        </div>
        <div class="center-bottom">
          <div class="center-bg">触发值</div>
          <div class="action_value"></div>
          <el-input
            :disabled="linkageDisabled"
            style="width: 125px;margin-botton"
            class="sy-input"
            v-model="form.value"
            size="small"
            clearable
            :maxlength="20"
          ></el-input>
        </div>
      </el-col>
      <el-col class="sy-col col-right" :span="8">
        <div class="action_title">联动类型</div>
        <div class="linkage_item">
          <div class="linkage_item_check">
            <el-checkbox
              v-model="form.linkageType"
              :true-label="linkageTypeEnum.SEQ_CONTROL"
              :false-label="linkageTypeEnum.PATROL_TASK"
              @change="
                form.linkageContent = '';
                form.name = '';
              "
            ></el-checkbox>
          </div>
          <div class="linkage_item_name">巡视点位</div>
          <div class="linkage_item_check">
            <el-checkbox
              v-model="form.linkageType"
              :true-label="linkageTypeEnum.PATROL_TASK"
              :false-label="linkageTypeEnum.SEQ_CONTROL"
              @change="
                form.linkageContent = '';
                form.name = '';
              "
            ></el-checkbox>
          </div>
          <div class="linkage_item_name">巡视任务</div>
        </div>
        <div class="action_title">联动内容</div>
        <div>
          <el-input v-model="lankageText" placeholder="请输入联动内容" size="medium" :maxlength="20" clearable>
            <sy-icon slot="suffix" name="filter" style="width: 22px; height: 80%; margin-top: 3px" />
          </el-input>
        </div>
        <!-- 巡视点位 -->
        <div v-if="form.linkageType === linkageTypeEnum.SEQ_CONTROL" class="content-list">
          <template v-if="filterPointData && filterPointData.length > 0">
            <div
              class="content-item"
              @click="
                form.linkageContent = item.guid;
                form.name = item.name;
              "
              :class="{ 'is-selected': form.linkageContent === item.guid }"
              v-for="item in filterPointData"
              :key="item.guid"
            >
              {{ item.name }}
            </div>
          </template>
          <sy-empty v-else></sy-empty>
        </div>
        <!-- 巡视任务 -->
        <div v-else-if="form.linkageType === linkageTypeEnum.PATROL_TASK" class="content-list">
          <template v-if="filterTaskData && filterTaskData.length > 0">
            <div
              class="content-item"
              @click="
                form.linkageContent = item.guid;
                form.name = item.name;
              "
              :class="{ 'is-selected': form.linkageContent === item.guid }"
              v-for="item in filterTaskData"
              :key="item.guid"
            >
              {{ item.name }}
            </div>
          </template>
          <sy-empty v-else></sy-empty>
        </div>
        <sy-empty v-else></sy-empty>
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
import { LinkageType, LinkageSignal } from '@/enums';
import Option from '@/enums/option';

export default {
  name: 'LinkageAction',
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
      return this.action.guid ? '编辑联动' : '新增联动';
    },
    linkageDisabled() {
      return this.action.signalType === LinkageSignal.ZF_SIGNAL;
    },
    linkageTypeEnum() {
      return LinkageType;
    },
    linkageSignalEnum() {
      return LinkageSignal;
    },
    linkageSignals() {
      if (this.linkageDisabled) {
        return Option.LinkageSignals.filter((item) => item.key === 2);
      } else {
        return Option.LinkageSignals.filter((item) => item.key < 2);
      }
    },
    linkageSymbols() {
      return Option.LinkageSymbols;
    },
    filterTaskData() {
      return this.taskData.filter((item) => item.name.indexOf(this.lankageText) !== -1);
    },
    filterPointData() {
      return this.pointData.filter((item) => item.name.indexOf(this.lankageText) !== -1);
    },
    filterYxData() {
      return this.yxData.filter((item) => item.name.indexOf(this.signalText) !== -1);
    },
    filterYcData() {
      return this.ycData.filter((item) => item.name.indexOf(this.signalText) !== -1);
    },
    filterZfData() {
      return this.zfData.filter((item) => item.name.indexOf(this.signalText) !== -1);
    },
  },
  props: {
    visible: {
      required: true,
      type: Boolean,
      default: false,
    },
    action: {
      required: true,
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {
      lankageText: '',
      signalText: '',
      pointData: [],
      taskData: [],
      yxData: [],
      ycData: [],
      zfData: [],
      filterText: '',
      signalList: [],
      form: {
        signalContent: '',
        linkageType: '',
        signalType: '',
        value: '',
        condition: '',
        linkageContent: '',
        name: '',
        guid: '',
      },
    };
  },
  methods: {
    updateSignalContent(signal) {
      if (this.linkageDisabled) return;
      this.form.signalContent = signal.guid;
    },
    conditionChange(val, checked) {
      if (checked) {
        this.form.condition = val;
      } else {
        this.form.condition = '';
      }
    },
    onDialogOpen() {
      this.lankageText = '';
      this.signalText = '';
      this.form = JSON.parse(JSON.stringify(this.action));
    },
    onDialogClose() {},
    onDialogAction(action) {
      switch (action) {
        case 'cancel':
          this.isVisible = false;
          break;
        case 'submit':
          {
            const { linkageType, linkageContent, signalType, condition, value, guid, signalContent, name } = this.form;
            if (condition === '')
              return this.$message({
                type: 'warning',
                message: '请选择触发条件',
              });
            if (value === '')
              return this.$message({
                type: 'warning',
                message: '请输入触发值',
              });
            if (signalContent === '')
              return this.$message({
                type: 'warning',
                message: '请选择信号内容',
              });
            if (linkageContent === '')
              return this.$message({
                type: 'warning',
                message: '请选择联动内容',
              });

            const params = {
              guid,
              linkage_type: linkageType,
              station_id: this.stationId,
              signal_type: signalType,
              symbol: condition,
              name,
              task_id: '',
              patrolpoint_id: '',
              yx_point_id: signalType === LinkageSignal.YX_SIGNAL ? signalContent : '',
              yc_point_id: signalType === LinkageSignal.YC_SIGNAL ? signalContent : '',
              yx_value: signalType === LinkageSignal.YX_SIGNAL ? value : '',
              yc_value: signalType === LinkageSignal.YC_SIGNAL ? Number(value) : '',
            };
            params.guid === '' && delete params.guid;
            params.yx_value === '' && delete params.yx_value;
            params.yc_value === '' && delete params.yc_value;
            params.yc_point_id === '' && delete params.yc_point_id;
            params.yx_point_id === '' && delete params.yx_point_id;
            this.$emit('complete', params);
            this.isVisible = false;
          }
          break;
        default:
          break;
      }
    },
    //巡视点位
    getDataInitPoint() {
      return this.$api.getBaseApi('patrolpoint', {
        component__device__bay__area__station: this.stationId,
      });
    },
    //巡视任务
    getDataInitTask() {
      return this.$api.getBaseApi('task', {
        station_id: this.stationId,
      });
    },
    //遥信
    getDataInitYx() {
      return this.$api.getBaseApi('yxpoint', {
        device__bay__area__station: this.stationId,
      });
    },
    //遥测
    getDataInitYc() {
      return this.$api.getBaseApi('ycpoint', {
        device__bay__area__station: this.stationId,
      });
    },
    // 主辅信号
    getZFSignalList() {
      return this.$api.getBaseApi('linkageaction', {
        signal_type: 2,
      });
    },
    async load() {
      const [pointData, taskData, yxData, ycData, zfData] = await Promise.all([
        this.getDataInitPoint(),
        this.getDataInitTask(),
        this.getDataInitYx(),
        this.getDataInitYc(),
        this.getZFSignalList(),
      ]);
      this.pointData = pointData.results || [];
      this.taskData = taskData.results || [];
      this.yxData = yxData.results || [];
      this.ycData = ycData.results || [];
      this.zfData = zfData.results || [];
    },
  },
  mounted() {
    this.load();
  },
};
</script>

<style lang="scss" scoped>
.action-name {
  height: 4vh;
  color: $text-primary;
  border: $border-default;
  border-bottom: none;
  padding: 15px;
  display: flex;
  align-items: center;
  &__input {
    width: 50%;
    height: 3vh;
    line-height: 3vh;
  }
}
.add-action_wrapper {
  height: calc(100% - 4vh);
  .col-left,
  .col-right {
    border: $border-default;
  }
  .col-center {
    position: relative;
    border-top: $border-default;
  }
  .action_title {
    color: $text-primary;
    text-align: center;
    padding: 0 20px;
    height: 4vh;
    line-height: 4vh;
    background: $bg-header;
  }
  .center-wrarpper {
    display: flex;
    flex-direction: column;
    align-items: center;
    background-color: $bg-base;
    height: calc(100% - 130px);
    .center-bg {
      margin-bottom: 20px;
    }
    .center-condition {
      .condition-item {
        margin-bottom: 10px;
        display: flex;

        width: 110px;
        height: 40px;
        border: $border-default;
        box-sizing: content-box;
        .condition-left {
          width: 40px;
          line-height: 40px;
          height: 40px;
          text-align: center;
          border-right: $border-default;
        }
        .condition-right {
          width: 70px;
          line-height: 40px;
          height: 40px;
          text-align: center;
          color: $text-primary;
        }
      }
      .active-condition {
        background-color: $bg-hover5;
      }
      .disabled {
        cursor: not-allowed;
      }
    }
  }
  .center-bottom {
    display: flex;
    position: absolute;
    flex-direction: column;
    align-items: center;
    height: 130px;
    bottom: 0;
    left: 0;
    width: 100%;
    border-top: $border-default;
    border-bottom: $border-default;
    .action_value {
      margin-top: 20px;
    }
  }
  .center-bg {
    width: 150px;
    color: $text-primary;
    padding: 7px 0;
    text-align: center;
    background: {
      image: url('~@/assets/img/bg_trapezoidal.png');
      size: 100% 100%;
      repeat: no-repeat;
    }
  }
  .linkage_item {
    display: flex;
    height: 40px;
    line-height: 40px;
    align-items: center;
    justify-content: center;
    .linkage_item_check {
      width: 20%;
      text-align: center;
      border-left: $border-default;
    }
    .linkage_item_name {
      width: 30%;
      text-align: center;
      border-left: $border-default;
      color: $text-secondary;
    }
  }
  .content-list {
    width: 100%;
    height: 28vh;
    overflow: auto;

    .content-item {
      color: $text-regular;
      padding: 5px 10px;
      cursor: pointer;
      @include text-ellipsis();

      &:hover {
        background-color: $bg-hover;
      }
      &:first-of-type {
        margin-top: 10px;
      }
      &.is-selected {
        background-color: $color-primary;
      }
      &.disabled {
        cursor: not-allowed;
      }
    }
  }
}
</style>
