<template>
  <el-dialog
    custom-class="sy-dialog sy-dialog--medium"
    :visible.sync="isVisible"
    :title="title"
    append-to-body
    center
    @open="onDialogOpen"
    @close="onDialogClose"
  >
    <el-row class="sy-row result-audit">
      <el-col :span="15" class="sy-col result-audit__left">
        <IdentifyResult :data="data" :editable="true" @save="handleSaveResult"></IdentifyResult>
      </el-col>
      <el-col class="sy-col result-audit__right" :span="9">
        <div class="result-audit__info">
          <h5 class="result-audit__title">
            <span>巡视信息</span>
            <el-button
              v-if="patrolData.camera_id"
              type="primary"
              class="sy-btn-regular"
              size="small"
              @click="videoCheck"
              >监控视频
            </el-button>
          </h5>
          <ul class="result-audit__list">
            <li>
              <label>巡视点位：</label>
              <span>{{ patrolData.patrolpoint_name }}</span>
            </li>
            <li>
              <label>巡视设备：</label>
              <span>{{ patrolData.patrolDevice }}</span>
            </li>
            <li>
              <label>巡视结果：</label>
              <span>{{ patrolData.patrolResult }}</span>
            </li>
            <li>
              <label>巡视结论：</label>
              <span :class="`sy-status-${patrolData.patrol_status}`">{{ patrolData.patrolStatus }}</span>
            </li>
          </ul>
        </div>

        <el-form class="result-audit__form" label-position="top" size="small" :model="formModel" :rules="formRules">
          <h5 class="result-audit__title">人工审核</h5>
          <el-form-item prop="patrol_status" label="审核结论:">
            <el-radio-group v-model="formModel.patrol_status">
              <el-radio v-for="item in patrolStates" :key="item.key" :label="item.key">
                {{ item.value }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="value_disp" label="审核结果:">
            <el-input v-model="formModel.value_disp" placeholder="请输入审核结果"></el-input>
          </el-form-item>
          <el-form-item prop="modify_content" label="审核意见:">
            <el-input
              class="sy-input-textarea"
              v-model="formModel.modify_content"
              placeholder="请输入审核意见"
              type="textarea"
              resize="none"
              :maxlength="100"
              autosize
            >
            </el-input>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <template #footer>
      <!-- <el-button class="sy-btn-regular" type="warning" @click="onDialogAction('defect')">转缺陷</el-button> -->
      <el-button class="sy-btn-regular" type="warning" size="medium" @click="onDialogAction('cancel')">取 消</el-button>
      <el-button class="sy-btn-regular" type="primary" size="medium" @click="onDialogAction('submit')"
        >提 交
      </el-button>
    </template>

    <CameraModel :visible.sync="cameraModal" :camera="patrolCamera" :preset="patrolPreset"></CameraModel>
  </el-dialog>
</template>

<script>
import { mapGetters } from 'vuex';
import { arrToMap } from '@/utils';
import Option from '@/enums/option';
import CameraModel from '@/components/Modal/Camera';
import IdentifyResult from '@/components/Canvas/Identify';

export default {
  name: 'PatrolResult',
  components: {
    CameraModel,
    IdentifyResult,
  },
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
    patrolStates() {
      return Option.PatrolStates;
    },
    patrolStateMap() {
      return arrToMap(Option.PatrolStates);
    },
    formRules() {
      return {
        value_disp: [{ type: 'string', required: true, message: '请输入审核结果' }],
        patrol_status: [{ type: 'number', required: true, message: '请选择审核结论' }],
        modify_content: [{ type: 'string', required: true, message: '请确认审核意见' }],
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
      default: '巡视结果审核',
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
        rectangle: '',
        value_disp: '',
        patrol_status: 0,
        modify_content: '',
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
    // 视频调阅
    async videoCheck() {
      const { camera_id, host_id, robot_id, patrolpoint_id, recognition_type } = this.patrolData;

      this.getPresetData({
        guid: patrolpoint_id,
        camera_id: camera_id || host_id || robot_id,
        recognition_type: recognition_type,
      });
    },
    handleSaveResult(res) {
      this.formModel.rectangle = res.rectangle;
      this.formModel.value_disp = res.value_disp;
    },
    onDialogOpen() {
      const { host_name, robot_name, camera_name, rectangle, value, value_disp, patrol_status, modify_content } =
        this.data;

      this.formModel = {
        rectangle: rectangle,
        value_disp: value_disp,
        patrol_status: patrol_status,
        modify_content: modify_content,
      };
      this.patrolData = {
        ...this.data,
        patrolResult: value_disp || value || '-',
        patrolStatus: this.patrolStateMap[patrol_status] || '-',
        patrolDevice: camera_name || robot_name || host_name || '-',
      };
    },
    onDialogClose() {},
    onDialogAction(action) {
      switch (action) {
        case 'cancel':
          this.isVisible = false;
          break;
        case 'submit':
          this.$api
            .postHistoryApi('update', {
              index: 'history.item',
              id: this.patrolData.id,
              data: {
                modified: true,
                modifier: this.username,
                rectangle: this.formModel.rectangle,
                value_disp: this.formModel.value_disp,
                patrol_status: this.formModel.patrol_status,
                modify_content: this.formModel.modify_content,
                modify_time: this.$dayjs().format('YYYY-MM-DD HH:mm:ss'),
              },
            })
            .then((res) => {
              if (res?.result === 1) {
                this.$message.success('巡视结果审核成功！');

                this.$emit('success');
                this.isVisible = false;
              } else {
                this.$message.warning('巡视结果审核失败！');
              }
            });
          break;
        case 'defect':
          break;
        default:
          break;
      }
    },
    // 获取巡视点位预置位
    getPresetData({ guid, camera_id }) {
      let params = { virtual: false, patrolpoint: guid, camera: camera_id };
      this.$api.getBaseApi('preset', params).then((res) => {
        if (!res?.results?.length) return;
        this.patrolPreset = res.results[0];
        this.patrolCamera = this.patrolPreset.camera;
        // console.log('this.patrolCamera: ', this.patrolCamera);
        this.cameraModal = true;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.result-audit {
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
  }
  &__right {
    display: flex;
    flex-direction: column;
  }
  &__info {
    width: 100%;
    padding: 10px;
    border-bottom: $border-default;
  }
  &__list {
    li {
      display: flex;

      label {
        flex: none;
      }
      span {
        flex: auto;
        @include text-ellipsis(2);
      }
    }
    > li + li {
      margin-top: 1vh;
    }
    span {
      font-weight: 600;
      color: $text-primary;
    }
  }
  &__form {
    flex: auto;
    padding: 10px;
    overflow: hidden;

    ::v-deep .el-form-item {
      margin-bottom: 2vh;

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
        height: 15vh;
        overflow-y: auto;

        &__inner {
          height: 100% !important;
        }
      }
    }
  }
}
</style>
