<template>
  <el-row id="video_monitor" class="sy-row" :gutter="10">
    <el-col class="sy-col" :span="5">
      <!--机器人控制-->
      <div class="video-tree sy-view">
        <div class="video-control-header">机器人控制</div>
        <div class="video-control-wrapper">
          <ul class="video-control-direction">
            <li
              class="tl"
              
            >
              <!-- <i class="el-icon-caret-top"></i> -->
            </li>
            <li @mousedown="sendPtzCMD(ptzCMD.PAN_UP_START)" @mouseup="sendPtzCMD(ptzCMD.PAN_UP_STOP)">
              <i class="el-icon-caret-top"></i>
            </li>
            <li
              class="tr"
              
            >
              <!-- <i class="el-icon-caret-top"></i> -->
            </li>
            <li @mousedown="sendPtzCMD(ptzCMD.PAN_LEFT_START)" @mouseup="sendPtzCMD(ptzCMD.PAN_LEFT_STOP)">
              <i class="el-icon-caret-left"></i>
            </li>
            <!-- 自动重置 -->
            <li><i class="el-icon-refresh"></i></li>
            <li @mousedown="sendPtzCMD(ptzCMD.PAN_RIGHT_START)" @mouseup="sendPtzCMD(ptzCMD.PAN_RIGHT_STOP)">
              <i class="el-icon-caret-right"></i>
            </li>
            <li
              class="bl"
              
            >
              <!-- <i class="el-icon-caret-bottom"></i> -->
            </li>
            <li @mousedown="sendPtzCMD(ptzCMD.PAN_DOWN_START)" @mouseup="sendPtzCMD(ptzCMD.PAN_DOWN_STOP)">
              <i class="el-icon-caret-bottom"></i>
            </li>
            <li
              class="br"
              
            >
              <!-- <i class="el-icon-caret-bottom"></i> -->
            </li>
          </ul>
          <div class="video-control-action">
            <div class="video-control-action__part">
              <div class="video-control-action__label">
                <sy-icon name="light" />
                <span>照明</span>
              </div>
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
            <div class="video-control-action__part">
              <div class="video-control-action__label">
                <sy-icon name="brush" />
                <span>雨刷</span>
              </div>
              <el-radio-group class="sy-switch" v-model="videoOption.brush" @change="onSwitchChange($event, 'brush')">
                <el-radio-button :label="1">开</el-radio-button>
                <el-radio-button :label="0">关</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <!-- <div class="video-control-action">
          <div class="video-control-action__part">
            <div class="video-control-action__label">
              <sy-icon name="light" />
              <span>照明</span>
            </div>
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
          <div class="video-control-action__part">
            <div class="video-control-action__label">
              <sy-icon name="brush" />
              <span>雨刷</span>
            </div>
            <el-radio-group class="sy-switch" v-model="videoOption.brush" @change="onSwitchChange($event, 'brush')">
              <el-radio-button :label="1">开</el-radio-button>
              <el-radio-button :label="0">关</el-radio-button>
            </el-radio-group>
          </div>
        </div> -->
          <!-- <div class="video-control-action">
            <div class="video-control-action__part">
              <div class="video-control-action__label">
                <sy-icon name="light" />
                <span>照明</span>
              </div>
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
            <div class="video-control-action__part">
              <div class="video-control-action__label">
                <sy-icon name="brush" />
                <span>雨刷</span>
              </div>
              <el-radio-group class="sy-switch" v-model="videoOption.brush" @change="onSwitchChange($event, 'brush')">
                <el-radio-button :label="1">开</el-radio-button>
                <el-radio-button :label="0">关</el-radio-button>
              </el-radio-group>
            </div>
          </div> -->
        </div>
        <div class="video-control-preset__title_left">机器人列表</div>
        <div class="video-tree-wrapper">
          <sy-tree
            v-show="!cameraSearch"
            v-loading="treeLoad"
            ref="tree"
            node-key="guid"
            :data="treeData"
            :props="treeProps"
            :filter-node-method="filterTreeNode"
            :lazy="false"
            show-checkbox
            check-strictly
            check-on-click-node
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
          <div v-show="cameraSearch" v-loading="cameraLoad" class="video-tree-group">
            <el-checkbox-group v-if="cameraList.length > 0" class="sy-checkbox-list" v-model="cameraSelect">
              <el-checkbox
                v-for="item in cameraList"
                :key="item.guid"
                :label="item.guid"
                @change="onCameraChange(item, $event)"
              >
                {{ item.name || '-' }}
              </el-checkbox>
            </el-checkbox-group>
            <sy-empty v-else-if="!cameraLoad" description="暂无搜索结果"></sy-empty>
          </div>
        </div>
      </div>
      <!-------------------------------;-->
    </el-col>
    <el-col class="sy-col" :span="14">
      
      <div class="video-player sy-view">
        
       
        <div class="video-player-wrapper">
          <div class="video-player-list" :class="`layout-${displayMode}`">
            <sy-player
              v-for="(item, index) in videoList.slice(0,2)"
              v-bind="item"
              :key="item.guid || index"
              :class="{ active: videoIndex === index }"
              @click.native="videoIndex = index"
              @close="onPlayerClose(index)"
            ></sy-player>
          </div>
        </div>
      </div>
    </el-col>
    <el-col class="sy-col" :span="5">
      <div class="video-control sy-view">
        <div class="video-control-header">视频控制</div>
        <div class="video-control-wrapper">
          <ul class="video-control-direction">
            <li
              class="tl"
              @mousedown="sendPtzCMD(ptzCMD.PAN_UPLEFT_START)"
              @mouseup="sendPtzCMD(ptzCMD.PAN_UPLEFT_STOP)"
            >
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
            <!-- 自动重置 -->
            <li><i class="el-icon-refresh"></i></li>
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
          <div class="video-control-speed">
            <span>速度：</span>
            <el-slider
              v-model="videoOption.speed"
              :max="videoOption.max"
              :min="videoOption.min"
              :step="videoOption.step"
              :marks="videoMarks"
            ></el-slider>
          </div>
          <div class="video-control-action">
            <el-button
              class="video-control-action__focus sy-btn-regular"
              @mousedown="sendPtzCMD(ptzCMD.AUTOSCAN_START)"
              @mouseup="sendPtzCMD(ptzCMD.AUTOSCAN_STOP)"
            >保存<br />预置位
            </el-button>

            <div class="video-control-action__right">
              <div class="video-control-action__item">
                <sy-icon name="lens" />
                <span>镜头：</span>
                <span class="video-control-action__btns">
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
              <div class="video-control-action__item">
                <sy-icon name="focus" />
                <span>焦距：</span>
                <span class="video-control-action__btns">
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
            </div>
          </div>

          <div class="video-control-action">
            <div class="video-control-action__part">
              <div class="video-control-action__label">
                <sy-icon name="light" />
                <span>照明</span>
              </div>
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
            <!-- <div class="video-control-action__part">
              <div class="video-control-action__label">
                <sy-icon name="brush" />
                <span>雨刷</span>
              </div>
              <el-radio-group class="sy-switch" v-model="videoOption.brush" @change="onSwitchChange($event, 'brush')">
                <el-radio-button :label="1">开</el-radio-button>
                <el-radio-button :label="0">关</el-radio-button>
              </el-radio-group>
            </div> -->
          </div>
        </div>
        <div class="video-control-preset">
          <div class="video-control-preset__title_right">预置位列表</div>
          <div class="video-control-preset__wrap">
            <ul v-if="videoPresets.length > 0" class="video-control-preset__list">
              <li v-for="item in videoPresets" :key="item.guid">
                <span>{{ item.name }}</span>
                <el-button class="sy-btn-regular" type="primary" size="mini" @click="handleCallPreset(item)"
                >调用预置位
                </el-button>
              </li>
            </ul>
            <sy-empty v-else description="暂无预置位"></sy-empty>
          </div>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import { mapGetters } from 'vuex';
import { mediaUrl } from '@/config';
import { debounce, throttle } from 'lodash';
import { PtzCMD, DisplayMode, MessageDest } from '@/enums';
import Option from '@/enums/option';
import VideoTree from '@/mixins/video-tree';

export default {
  name: 'VideoMonitor',
  mixins: [VideoTree],
  computed: {
    ...mapGetters(['stomp', 'stationId']),
    ptzCMD() {
      return PtzCMD;
    },
    displayModes() {
      return DisplayMode;
    },
    cameraTypes() {
      return Option.CameraTypes;
    },
    cameraSearch() {
      return this.cameraName.length > 0 || this.cameraType.length > 0;
    },
    videoActive() {
      return this.videoList.length > 0 && this.videoList[this.videoIndex];
    },
  },
  watch: {
    videoActive: {
      deep: true,
      handler(video) {
        if (video && video.videoUrl) this.getVideoPresets(video);
      },
    },
    cameraSearch(bool) {
      if (bool) {
        const checkedKeys = this.$refs.tree.getCheckedKeys(true);
        this.cameraSelect = Array.from(new Set([...this.cameraSelect, ...checkedKeys]));
      } else {
        this.$nextTick(() => {
          this.cameraSelect && this.$refs.tree.setCheckedKeys(this.cameraSelect);
        });
      }
    },
  },
  data() {
    const defaultMode = DisplayMode.FOUR;
    return {
      displayMode: defaultMode,
      cameraLoad: false,
      cameraName: '',
      cameraType: [],
      cameraList: [],
      cameraSelect: [],
      nvrId: '',
      streamId: '',
      cameraId: '',
      channelNo: 0,
      videoList: Array.from({ length: defaultMode }, () => Object.create(null)),
      videoIndex: 0,
      videoStatis: { total: 0, online: 0, offline: 0 },
      videoMarks: { 1: '1', 2: '2', 3: '3', 4: '4' },
      videoOption: { max: 4, min: 1, step: 1, speed: 2, light: 0, brush: 0 },
      videoPresets: [],
    };
  },
  created() {
    this.getVideoStatis();
    this.onStatusPlatformUpdate();
  },
  methods: {
    handleCloseAll() {
      this.videoIndex = 0;
      this.$refs.tree.setCheckedKeys([]);
      this.videoList = this.videoList.map(() => Object.create(null));
    },
    handleResetTree() {
      this.cameraName = '';
      this.cameraType = [];
      this.cameraList = [];
      this.cameraSelect = [];
      this.$refs.tree.setCheckedKeys([]);
      this.videoList = this.videoList.map(() => Object.create(null));
    },
    handleCallPreset(preset) {
      // console.log('preset: ', preset);
      const { camera, ptz_h, ptz_v, ipc_z } = preset;
      this.patrolCamera = camera;
      this.$api
        .getVideoApi('setptzpos', {
          idx: camera.nvr,
          no: camera.channel_no,
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
    },
    handleSwitchMode(mode) {
      this.displayMode = mode;
      // this.videoList = this.videoList.slice(0, mode);
      if (this.videoList.length > mode) {
        if (this.videoIndex >= mode) {
          this.videoIndex = mode - 1;
        }
        this.videoList = this.videoList.slice(0, mode);
        this.$refs.tree.setCheckedNodes(this.videoList);
      } else {
        const emptyList = Array.from({ length: mode - this.videoList.length }, () => ({
          videoUrl: '',
        }));
        this.videoList = [...this.videoList, ...emptyList];
      }
    },
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
    onCameraChange(camera, checked) {
      this.onNodeCheckChange(camera, checked);
    },
    onCameraSearch: debounce(function () {
      if (!this.cameraName && !this.cameraType.length) {
        this.cameraList = [];
        return;
      }
      this.cameraLoad = true;
      this.$api
        .getBaseApi('camera', {
          name__contains: this.cameraName,
          device_type__in: this.cameraType.join(','),
        })
        .then((res) => {
          if (!res?.results) return;
          this.cameraList = res.results;
        })
        .finally(() => {
          this.cameraLoad = false;
        });
    }, 300),
    onNodeCheckChange(data, checked) {
      // console.log('nodecheckchange: ', data, checked);
      if (checked) {
        const videoUrl = `${mediaUrl}/${data.nvr.guid}/${data.channel_no}.flv`;

        const curVideo = this.videoList[this.videoIndex];
        const selVideo = {
          ...data,
          videoUrl,
          title: data.name,
          nvrId: data.nvr && data.nvr.guid,
          cameraId: data.guid,
          channelNo: data.channel_no,
        };
        if (curVideo.videoUrl) {
          const index = this.videoList.findIndex((item) => !item.videoUrl);
          // 视频列表有空位时替换空位
          if (index >= 0) {
            this.videoList.splice(index, 1, selVideo);
          } else {
            // 视频列表没有空位直接替换当前位置
            const videos = this.videoList.splice(this.videoIndex, 1, selVideo);
            this.$refs.tree.setChecked(videos[0], false);
            if (this.cameraSearch) {
              const index = this.cameraSelect.findIndex((item) => item === videos[0].guid);
              if (index >= 0) this.cameraSelect.splice(index, 1);
            }
          }
        } else {
          // 当前选中位置未连接设备直接替换
          this.videoList.splice(this.videoIndex, 1, selVideo);
        }
      } else {
        const index = this.videoList.findIndex((item) => item.guid === data.guid);
        if (index >= 0) this.videoList.splice(index, 1, Object.create(null));
      }
    },
    // 关闭播放器
    onPlayerClose(index) {
      const video = this.videoList[index];
      this.$refs.tree.setChecked(video.guid, false);
      this.videoList.splice(index, 1, Object.create(null));
    },
    onPlayerScreenshot: throttle(function ({ image, title }) {
      // const { nvr, channel_no } = this.videoActive;
      // if (nvr?.guid === undefined || !channel_no) return;
      this.imageUrl = image;
      this.imageTitle = title;
      this.imageModal = true;
    }, 1000),

    getVideoStatis() {
      this.$api.getMediaApi(`count/${this.stationId}/`).then((res) => {
        // const total = res.nvr_total + res.camera_total;
        // const online = res.nvr_online + res.camera_online;
        const total = res.camera_total;
        const online = res.camera_online;
        const offline = total - online;
        this.videoStatis = { total, online, offline };
      });
    },
    getVideoPresets(video) {
      this.$api.getBaseApi('preset', { camera: video.guid, virtual: false }).then((res) => {
        if (!res?.results) return;
        this.videoPresets = res.results;
      });
    },
    sendPtzCMD(cmd) {
      if (!this.videoActive) return;
      this.$api
        .getVideoApi('ptzControl', {
          idx: this.videoActive.nvr.guid,
          no: this.videoActive.channel_no,
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
    onStatusPlatformUpdate() {
      if (this.stomp) {
        let stompMedia = this.stomp.subscribe({
          destination: MessageDest.STATUS_PLATFORM_MEDIA,
          callback: (msg) => {
            const { action, data } = JSON.parse(msg.body);
            console.log('摄像机状态', action, data);
            if (!data) return;
            const { nvr_status, camera_status } = data;
            if (nvr_status && nvr_status.length) {
              nvr_status.forEach((item) => {
                if (item.online) {
                  this.videoStatis.online += 1;
                  this.videoStatis.offline -= 1;
                } else {
                  this.videoStatis.online -= 1;
                  this.videoStatis.offline += 1;
                }
              });
            }
            if (camera_status && camera_status.length) {
              camera_status.forEach((item) => {
                if (item.online) {
                  this.videoStatis.online += 1;
                  this.videoStatis.offline -= 1;
                } else {
                  this.videoStatis.online -= 1;
                  this.videoStatis.offline += 1;
                }
              });
            }
          },
        });
        this.$once('hook:beforeDestroy', () => {
          if (stompMedia) {
            stompMedia.unsubscribe();
            stompMedia = null;
          }
        });
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.video-tree {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 100%;
  height: 100%;
  padding: 10px;

  &-statis {
    display: flex;
    align-items: center;
    width: 100%;
    height: 10vh;
    color: $text-regular;

    .name {
      margin-right: 50px;
      font-size: 16px;
    }
    .value {
      font-weight: bold;
      font-family: 'DS-DIGIB';
      text-shadow: 0px 0px 10px rgba(0, 183, 238, 0.72);
    }
    &__total {
      display: inline-flex;
      align-items: center;
      width: 50%;
      height: 100%;
      padding-left: 20px;
      border: $border-default;

      .value {
        font-size: 36px;
      }
    }
    &__right {
      width: 50%;
      height: 100%;
    }
    &__item {
      display: flex;
      align-items: center;
      width: 100%;
      height: 50%;
      padding-left: 20px;
      // border: $border-default;

      &.online {
        background: {
          image: url('~@/assets/img/bg_inset_blue.png');
          position: center;
          size: 100% 100%;
        }
      }
      &.offline {
        background: {
          image: url('~@/assets/img/bg_inset_red.png');
          position: center;
          size: 100% 100%;
        }
      }
      .value {
        font-size: 24px;
      }
    }
  }
  &-filter {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    margin-top: 1vh;

    > .el-button {
      margin-left: 10px;
    }
  }
  &-checkbox {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 1vh;
  }
  &-wrapper {
    width: 100%;
    height: 60vh;
    margin-top: 1vh;
  }
  &-group {
    width: 100%;
    height: 100%;
    overflow: auto;
  }
}

.video-player {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;

  &-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex: none;
    width: 100%;
    height: 5vh;
    padding: 0 10px;

    &__display {
      display: inline-flex;
      align-items: center;

      .sy-icon {
        margin-left: 10px;
        font-size: 24px;
        color: $text-placeholder;
        

        &:hover,
        &.active {
          color: $text-primary;
        }
      }
    }
    &__action {
      display: inline-flex;
      align-items: center;
    }
  }
  &-wrapper {
    flex: auto;
    width: 100%;
    border: $border-default;
  }
  &-list {
    display: grid;
    width: 100%;
    height: 100%;

    &.layout-1 {
      grid-template-rows: repeat(1,1fr);
      grid-template-columns: repeat(1, 1fr);
    }
    &.layout-4 {
      grid-template-rows: repeat(2, 1fr);
      grid-template-columns: repeat(2, 1fr);
    }
    &.layout-9 {
      grid-template-rows: repeat(3, 1fr);
      grid-template-columns: repeat(3, 1fr);
    }
    &.layout-16 {
      grid-template-rows: repeat(4, 1fr);
      grid-template-columns: repeat(4, 1fr);
    }
  }
}

.video-control {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 100%;

  &-header {
    width: 100%;
    height: 5vh;
    padding-left: 20px;
    line-height: 5vh;
    // color: $text-regular;
    font-size: 24px;
    font-weight: bold;
    font-family: BigYoungBoldGB;
    background: {
      image: url('~@/assets/img/bg_title_minor.png');
      repeat: no-repeat;
      position: 0 0;
      size: 60% 100%;
    }
  }
  &-wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
  }
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
        // background-color: $bg-hover;
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
  &-speed {
    display: flex;
    align-items: center;
    margin-top: 2vh;
    margin-bottom: 2vh;
    font-size: 14px;
    color: $text-secondary;

    ::v-deep .el-slider {
      width: 200px;

      &__runway {
        margin: 0;
        border-radius: 5px;
        border: $border-default;
        background-color: transparent;
      }
      &__button {
        background-color: $bg-base;
      }
      &__marks-text {
        color: $text-secondary;
      }
    }
  }
    &-action {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    margin-top: 2vh;

    .sy-icon {
      margin-right: 5px;
      font-size: 20px;
    }
    &__focus {
      padding: 15px;
      margin-right: 50px;
      font-size: 18px;
      color: $text-secondary;
      border: $border-default;

      &:hover {
        color: $text-primary;
        background-color: $bg-hover;
      }
    }
    &__item {
      display: flex;
      align-items: center;
      font-size: 16px;
      color: $text-secondary;

      + .video-control-action__item {
        margin-top: 1vh;
      }
    }
    &__btns {
      display: inline-flex;
      align-items: center;
      justify-content: space-between;
      width: 80px;
      border-radius: 20px;
      border: $border-default;

      > i {
        font-size: 24px;
        cursor: pointer;

        &:hover {
          color: $text-primary;
        }
      }
    }
    &__part {
      display: inline-flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      width: 50%;
      height: 8vh;
      border: $border-default;
    }
    &__label {
      display: flex;
      align-items: center;
      margin-bottom: 0.5vh;
      font-size: 16px;
      color: $text-secondary;
    }
  }
  &-item {
    display: flex;
    align-items: center;
    margin-top: 1vh;
    color: $text-regular;
    font-size: 14px;

    .svg-icon {
      margin-right: 10px;
      font-size: 28px;
    }
  }
  &-preset {
    width: 100%;
    height: 300px;
    

    // &__title {
    //   width: 100%;
    //   height: 25px;
    //   border: 1px solid red;
    //   line-height: 25px;
    //   text-align: center;
    //   font-size: 16px;
    //   font-weight: 600;
    //   font-family: BigYoungBoldGB;
    //   color: $text-secondary;
    //   border: $border-default;
    //   background: $bg-header;
    // }
  }
  
}
.video-control-preset__title_left{
    height: 35px;
    line-height: 35px;
      text-align: center;
      font-size: 16px;
      font-weight: 600;
      font-family: BigYoungBoldGB;
      color: $text-secondary;
      border: $border-default;
      background: $bg-header;
      // z-index: 999;
  }
  .video-control-preset__title_right{
      margin-top: 2px;
      height: 35px;
      line-height: 35px;
      text-align: center;
      font-size: 16px;
      font-weight: 600;
      font-family: BigYoungBoldGB;
      color: $text-secondary;
      border: $border-default;
      background: $bg-header;
      // z-index: 999;
  .el-empty.sy-empty{
    width: 100%;
    height: 250px;
    margin: 10px 0px 10px;
  }
}

</style>
