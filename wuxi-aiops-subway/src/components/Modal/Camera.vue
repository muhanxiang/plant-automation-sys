<template>
  <el-dialog
    custom-class="sy-dialog sy-dialog--medium"
    :title="cameraName"
    :visible.sync="isVisible"
    :append-to-body="appendToBody"
    :destroy-on-close="destroyOnClose"
    center
    @open="onDialogOpen"
    @close="onDialogClose"
  >
    <el-row class="camera sy-row">
      <el-col class="camera-preset sy-col sy-col-vertical" :span="7">
        <div class="camera-preset-header">预置位列表</div>
        <div class="camera-preset-wrapper" v-loading="presetLoad">
          <ul class="camera-preset-list" v-if="presetList.length > 0">
            <li
              class="camera-preset-item"
              v-for="item in presetList"
              :key="item.guid"
              :title="item.name"
              :class="{ active: item.guid === videoPreset.guid }"
              @click="handleCallPreset(item)"
            >
              {{ item.name }}
            </li>
          </ul>
          <sy-empty v-else-if="!presetLoad" description="暂无预置位"></sy-empty>
        </div>
      </el-col>
      <el-col class="sy-col" :span="12">
        <sy-player :video-url="videoUrl"></sy-player>
      </el-col>
      <el-col class="camera-control sy-col sy-col-vertical" :span="5">
        <ul class="camera-control-direction">
          <li class="tl" @mousedown="sendPtzCMD(ptzCMD.PAN_UPLEFT_START)" @mouseup="sendPtzCMD(ptzCMD.PAN_UPLEFT_STOP)">
            <i class="el-icon-caret-top"></i>
          </li>
          <li @mousedown="sendPtzCMD(ptzCMD.PAN_UP_START)" @mouseup="sendPtzCMD(ptzCMD.PAN_UP_STOP)">
            <i class="el-icon-caret-top"></i>
          </li>
          <li
            class="tr"
            @mousedown="sendPtzCMD(ptzCMD.PAN_UPRIGHT_START)"
            @mouseup="sendPtzCMD(ptzCMD.PAN_UPRIGHT_STOP)"
          >
            <i class="el-icon-caret-top"></i>
          </li>
          <li @mousedown="sendPtzCMD(ptzCMD.PAN_LEFT_START)" @mouseup="sendPtzCMD(ptzCMD.PAN_LEFT_STOP)">
            <i class="el-icon-caret-left"></i>
          </li>
          <li><i class="el-icon-refresh-right"></i></li>
          <li @mousedown="sendPtzCMD(ptzCMD.PAN_RIGHT_START)" @mouseup="sendPtzCMD(ptzCMD.PAN_RIGHT_STOP)">
            <i class="el-icon-caret-right"></i>
          </li>
          <li
            class="bl"
            @mousedown="sendPtzCMD(ptzCMD.PAN_DOWNLEFT_START)"
            @mouseup="sendPtzCMD(ptzCMD.PAN_DOWNLEFT_STOP)"
          >
            <i class="el-icon-caret-bottom"></i>
          </li>
          <li @mousedown="sendPtzCMD(ptzCMD.PAN_DOWN_START)" @mouseup="sendPtzCMD(ptzCMD.PAN_DOWN_STOP)">
            <i class="el-icon-caret-bottom"></i>
          </li>
          <li
            class="br"
            @mousedown="sendPtzCMD(ptzCMD.PAN_DOWNRIGHT_START)"
            @mouseup="sendPtzCMD(ptzCMD.PAN_DOWNRIGHT_STOP)"
          >
            <i class="el-icon-caret-bottom"></i>
          </li>
        </ul>
        <div class="camera-control-item">
          <sy-icon name="lens" />
          <span>镜头：</span>
          <span class="camera-control-item__btns">
            <i
              class="el-icon-circle-plus-outline"
              @mousedown="sendPtzCMD(ptzCMD.ZOOM_IN_START)"
              @mouseup="sendPtzCMD(ptzCMD.ZOOM_IN_STOP)"
            />
            <i
              class="el-icon-remove-outline"
              @mousedown="sendPtzCMD(ptzCMD.ZOOM_OUT_START)"
              @mouseup="sendPtzCMD(ptzCMD.ZOOM_OUT_STOP)"
            />
          </span>
        </div>
        <div class="camera-control-item">
          <sy-icon name="focus" />
          <span>焦距：</span>
          <span class="camera-control-item__btns">
            <i
              class="el-icon-circle-plus-outline"
              @mousedown="sendPtzCMD(ptzCMD.FOCUS_FAR_START)"
              @mouseup="sendPtzCMD(ptzCMD.FOCUS_FAR_STOP)"
            />
            <i
              class="el-icon-remove-outline"
              @mousedown="sendPtzCMD(ptzCMD.FOCUS_NEAR_START)"
              @mouseup="sendPtzCMD(ptzCMD.FOCUS_NEAR_STOP)"
            />
          </span>
        </div>
        <el-button
          class="camera-control-focus sy-btn-regular"
          round
          @mousedown="sendPtzCMD(ptzCMD.AUTOSCAN_START)"
          @mouseup="sendPtzCMD(ptzCMD.AUTOSCAN_STOP)"
          >自动扫描
        </el-button>
        <div class="camera-control-bottom">
          <div class="camera-control-item border">
            <sy-icon name="light" />
            <span>照明：</span>
            <el-radio-group
              class="sy-switch"
              v-model="videoOption.light"
              disabled
              @change="onSwitchChange($event, 'light')"
            >
              <el-radio-button :label="1">开</el-radio-button>
              <el-radio-button :label="0">关</el-radio-button>
            </el-radio-group>
          </div>
          <div class="camera-control-item border">
            <sy-icon name="brush" />
            <span>雨刷：</span>
            <el-radio-group class="sy-switch" v-model="videoOption.brush" @change="onSwitchChange($event, 'brush')">
              <el-radio-button :label="1">开</el-radio-button>
              <el-radio-button :label="0">关</el-radio-button>
            </el-radio-group>
          </div>
        </div>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import { throttle } from 'lodash';
import { PtzCMD, PresetType } from '@/enums';
import { baseUrl, mediaUrl } from '@/config';
import { typeOf } from '@/utils';

export default {
  name: 'CameraModal',
  computed: {
    isVisible: {
      get() {
        return this.visible;
      },
      set(bool) {
        this.$emit('update:visible', bool);
      },
    },
    ptzCMD() {
      return PtzCMD;
    },
    // 录像机ID
    nvrId() {
      const nvr = this.camera?.nvr;
      return typeOf(nvr) === 'String' ? nvr : nvr?.guid;
    },
    // 摄像机ID
    cameraId() {
      return this.camera?.guid;
    },
    cameraName() {
      return this.camera?.name;
    },
    // 通编道号
    channelNo() {
      return this.camera?.channel_no;
    },
  },
  props: {
    title: {
      type: String,
      default: '摄像机监控',
    },
    visible: {
      type: Boolean,
      default: false,
    },
    // 默认摄像机
    camera: {
      type: Object,
      default: () => ({}),
    },
    // 默认预置位
    preset: {
      type: Object,
      default: () => null,
    },
    appendToBody: {
      type: Boolean,
      default: true,
    },
    destroyOnClose: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      videoUrl: '',
      videoOption: { max: 4, min: 1, step: 1, speed: 2, light: 0, brush: 0 },
      videoPreset: {},
      presetList: [],
      presetLoad: false,
    };
  },
  methods: {
    handleCallPreset: throttle(function (preset) {
      const { camera, ptz_h, ptz_v, ipc_z } = preset;
      this.videoPreset = preset;
      if (!camera) {
        return this.$message.waring('暂无摄像机信息！');
      }
      if (preset.preset_type === PresetType.PTZ_PARAM) {
        this.$api
          .getVideoApi('setptzpos', {
            idx: camera?.nvr,
            no: camera?.channel_no,
            ptzh: ptz_h,
            ptzv: ptz_v,
            ptzz: ipc_z,
          })
          .then((res) => {
            if (res?.success) {
              this.$message({
                type: 'success',
                message: '预置位调用成功！',
              });
            } else {
              this.$message({
                type: 'error',
                message: '预置位调用失败！',
              });
            }
          });
      } else {
        this.$api
          .getVideoApi('callcamerapreset', {
            nvr_id: camera?.nvr,
            camera_id: camera?.gurd,
            preset: { id: preset.preset_no, name: preset.name },
          })
          .then(({ success, error }) => {
            if (!success)
              this.$message({
                type: 'error',
                message: error.detail,
              });
          });
      }
    }, 1000),
    handleScreenshot: throttle(function () {
      if (!this.nvrId || !this.channelNo) return;
      this.imageUrl = `${baseUrl}/video/api/v2/getpicture?devidx=${this.nvrId}&channelno=${this.channelNo}`;
      this.imageModal = true;
      this.imageModalTitle = '摄像机抓图';
    }, 1000),
    onDialogOpen() {
      if (!this.cameraId) return;
      this.videoUrl = `${mediaUrl}/${this.nvrId}/${this.channelNo}.flv`;
      this.getPresetList(this.cameraId);
    },
    onDialogClose() {},
    onSwitchChange(val, type) {
      switch (type) {
        case 'light':
          this.sendPtzCMD(val ? this.ptzCMD.LIGHT_OPEN : this.ptzCMD.LIGHT_CLOSE);
          break;
        case 'brush':
          this.sendPtzCMD(val ? this.ptzCMD.BRUSH_OPEN : this.ptzCMD.BRUSH_CLOSE);
          break;
        default:
          break;
      }
    },
    getPresetList(cameraId) {
      this.presetLoad = true;
      this.$api
        .getBaseApi('preset', {
          camera: cameraId,
          virtual: false,
        })
        .then((res) => {
          if (!res?.results) return;
          this.presetList = res.results;
        })
        .finally(() => {
          this.presetLoad = false;
          if (this.preset) this.handleCallPreset(this.preset);
        });
    },
    sendPtzCMD(cmd) {
      if (!this.nvrId || !this.channelNo) return;
      this.$api
        .getVideoApi('ptzControl', {
          idx: this.nvrId,
          no: this.channelNo,
          command: cmd,
          para1: this.videoOption.speed,
        })
        .then((res) => {
          if (res?.success) {
            this.$message({
              type: 'success',
              message: '控制指令发成功！',
            });
          } else {
            this.$message({
              type: 'error',
              message: '控制指令发失败！',
            });
          }
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.camera {
  border: $border-default;

  &-preset {
    &-header {
      width: 100%;
      height: 4vh;
      line-height: 4vh;
      color: $color-primary;
      font-size: 20px;
      font-weight: 600;
      text-align: center;
      background: linear-gradient(0deg, rgba(0, 140, 255, 0.19) 0%, rgba(0, 140, 255, 0) 100%);
    }
    &-wrapper {
      flex: auto;
      width: 100%;
      overflow: auto;
    }
    &-list {
      width: 100%;
      height: 100%;
      overflow-y: auto;
    }
    &-item {
      display: flex;
      align-items: center;
      width: 100%;
      height: 4vh;
      padding: 5px;
      font-size: 12px;
      color: $text-secondary;
      border-bottom: $border-default;
      cursor: pointer;

      &:hover,
      &.active {
        background-color: $bg-hover;
      }
    }
  }
  &-control {
    align-items: center;

    &-direction {
      display: grid;
      grid-template-rows: repeat(3, auto);
      grid-template-columns: repeat(3, auto);

      > li {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        width: 75px;
        height: 75px;
        font-size: 32px;
        color: $text-secondary;
        border: $border-default;
        cursor: pointer;

        &:hover,
        &:focus {
          color: $text-primary;
          box-shadow: inset 0 0 10px $bg-hover;
        }
        &.tl > i {
          transform: rotateZ(-45deg);
        }
        &.tr > i {
          transform: rotateZ(45deg);
        }
        &.bl > i {
          transform: rotateZ(45deg);
        }
        &.br > i {
          transform: rotateZ(-45deg);
        }
      }
    }
    &-bottom {
      width: 100%;
    }
    &-focus {
      font-size: 18px;
      border: $border-default;
      color: $color-primary;
      background: $color-btn;
      opacity: 0.9;
      &:hover {
        opacity: 1;
      }
    }
    &-item {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 100%;
      font-size: 16px;
      color: $text-secondary;

      .sy-icon {
        margin-right: 10px;
        font-size: 24px;
      }
      &.border {
        width: 100%;
        height: 7vh;
        border-top: $border-default;
        border-bottom: $border-default;
      }
      &__btns {
        display: inline-flex;
        align-items: center;
        justify-content: space-between;
        width: 100px;
        border-radius: 25px;
        border: $border-default;

        > i {
          font-size: 32px;
          cursor: pointer;

          &:hover {
            color: $text-primary;
          }
        }
      }
    }
  }
}
</style>
