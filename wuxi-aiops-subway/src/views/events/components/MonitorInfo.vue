<template>
  <el-dialog
    custom-class="sy-dialog sy-dialog--small"
    :visible.sync="isVisible"
    :title="title"
    append-to-body
    center
    @open="onDialogOpen"
    @close="onDialogClose"
  >
    <el-row class="sy-row monitor-info">
      <el-col :span="12" class="sy-col monitor-info__left">
        <el-descriptions title="监测点位" size="medium" :column="1" border>
          <el-descriptions-item v-for="(item, index) in commPoint" :key="index" :label="item.label">
            {{ item.value }}
          </el-descriptions-item>
        </el-descriptions>
        <el-descriptions title="告警信息" size="medium" :column="1" border>
          <el-descriptions-item v-for="(item, index) in alarmInfo" :key="index" :label="item.label">
            {{ item.value }}
          </el-descriptions-item>
        </el-descriptions>
      </el-col>
      <el-col class="sy-col monitor-info__right" :span="12">
        <h5 class="monitor-info__title">人工审核</h5>
        <el-form
          class="monitor-info__form"
          ref="form"
          size="medium"
          label-position="top"
          :model="formModel"
          :rules="formRules"
        >
          <el-form-item prop="solved" label="处理结果:">
            <el-radio-group v-model="formModel.solved">
              <el-radio :label="true">已解决</el-radio>
              <el-radio :label="false">未解决</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="reason" label="原因分析:">
            <el-input
              class="sy-input-textarea"
              v-model="formModel.reason"
              placeholder="请输入原因分析"
              type="textarea"
              resize="none"
            ></el-input>
          </el-form-item>
          <el-form-item prop="solution" label="解决方案:">
            <el-input
              class="sy-input-textarea"
              v-model="formModel.solution"
              placeholder="请输入解决方案"
              type="textarea"
              resize="none"
            >
            </el-input>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <template #footer>
      <el-button class="sy-btn-regular" type="warning" size="medium" @click="onDialogAction('cancel')">取 消</el-button>
      <el-button class="sy-btn-regular" type="primary" size="medium" @click="onDialogAction('submit')"
        >提 交
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import { mapGetters } from 'vuex';
import { arrToMap } from '@/utils';
import Option from '@/enums/option';

// const CommpointType = {
//   0: '遥信',
//   1: '遥测',
// };

export default {
  name: 'MonitorInfo',
  computed: {
    ...mapGetters(['username']),
    isVisible: {
      get() {
        return this.visible;
      },
      set(bool) {
        this.$emit('update:visible', bool);
      },
    },
    alarmTypeMap() {
      return arrToMap(Option.AlarmTypes);
    },
    alarmLevelMap() {
      return arrToMap(Option.AlarmLevels);
    },
    alarmInfo() {
      const { alarm } = this.data;
      return [
        { label: '告警信息', value: alarm?.name || '-' },
        { label: '告警类型', value: this.alarmTypeMap[alarm?.alarm_type] || '-' },
        { label: '告警等级', value: this.alarmLevelMap[alarm?.alarm_level] || '-' },
        // { label: '告警表达式', value: alarm?.expression || '-' },
      ];
    },
    commPoint() {
      const { commpoint, value_disp } = this.data;
      return [
        { label: '点位名称', value: commpoint?.name || '-' },
        { label: '监测结果', value: value_disp || '-' },
        // { label: '点位类型', value: CommpointType[commpoint?.point_type] || '-' },
        // { label: '上限值', value: commpoint?.upper_limit ?? '-' },
        // { label: '下限值', value: commpoint?.lower_limit ?? '-' },
        // { label: '倍率值', value: commpoint?.multiply ?? '-' },
      ];
    },
    formRules() {
      return {
        reason: [{ type: 'string', required: true, message: '请输入原因分析' }],
        solution: [{ type: 'string', required: true, message: '请输入解决方案' }],
        solved: [{ type: 'boolean', required: true, message: '请确认处理结果' }],
      };
    },
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    title: {
      type: String,
      default: '监测信息维护',
    },
    data: {
      type: Object,
      required: true,
      default: () => ({}),
    },
  },
  data() {
    return {
      cameraModal: false,
      formModel: {
        reason: '',
        solution: '',
        solved: false,
      },
      patrolData: {},
      patrolPreset: {},
      patrolCamera: {
        nvr: '',
        guid: '',
        name: '',
        channel_no: '',
      },
    };
  },
  methods: {
    onDialogOpen() {
      const { reason, solution, solved } = this.data;
      this.formModel = { reason, solution, solved };
    },
    onDialogClose() {},
    onDialogAction(action) {
      switch (action) {
        case 'cancel':
          this.isVisible = false;
          break;
        case 'submit':
          this.$refs.form.validate((valid) => {
            if (!valid) return;

            const { alarm, commpoint, guid, id, reason, solution, solved, station, time, value, value_disp } = {
              ...this.data,
              ...this.formModel,
            };
            this.$api
              .postBaseApi('commpointalarm/update', {
                data: [
                  {
                    alarm: alarm?.guid,
                    station: station?.guid,
                    commpoint: commpoint?.guid,
                    id,
                    guid,
                    reason,
                    solved,
                    solution,
                    time,
                    value,
                    value_disp,
                  },
                ],
              })
              .then((res) => {
                if (res?.success) {
                  this.$message.success('监测信息提交成功！');

                  this.$emit('success');
                  this.isVisible = false;
                } else {
                  this.$message.warning('监测信息提交失败！');
                }
              });
          });
          break;
        case 'defect':
          break;
        default:
          break;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.monitor-info {
  border: $border-default;

  &__title {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 1vh;
    color: $color-primary;
    font-size: 16px;
    font-weight: 600;
    text-shadow: 0px 0px 5px $text-shadow;
  }
  &__left {
    padding: 10px;
    border-right: $border-default;

    ::v-deep .el-descriptions {
      table-layout: fixed;

      &__body {
        background-color: $bg-base;
      }
      &-row {
        display: flex;
      }
      &-item__cell {
        padding: 10px;
        @include text-ellipsis();
      }
      &-item__label {
        flex: 1;
      }
      &-item__content {
        flex: 2;
      }

      + .el-descriptions {
        margin-top: 2vh;
      }
    }
  }
  &__right {
    padding: 10px;
  }
  &__form {
    width: 100%;
    height: 100%;
    overflow: hidden;

    ::v-deep .el-form-item {
      &__label {
        padding-bottom: 5px;
        line-height: 1.5;
      }
      .el-radio {
        // margin-right: 10px;
        // background-image: none;

        &__label {
          padding-left: 5px;
        }
      }
      .el-textarea {
        height: 8vh;
        overflow-y: auto;

        &__inner {
          height: 100% !important;
        }
      }
    }
  }
}
</style>
