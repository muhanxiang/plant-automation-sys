<template>
  <div class="sy-player" v-loading="loading">
    <div v-show="videoUrl" class="sy-player-wrapper">
      <header class="sy-player-header">
        <span class="sy-player-header__title">{{ title }}</span>
        <span class="sy-player-header__action">
          <!-- <i
            :class="recording ? 'el-icon-video-camera-solid' : 'el-icon-video-camera'"
            @click="handlePlayerAction('record')"
          /> -->
          <i class="el-icon-picture-outline" @click="handlePlayerAction('screenshot')" />
          <i class="el-icon-circle-close" @click="handlePlayerAction('close')" />
        </span>
      </header>
      <div ref="player" :id="playerId" class="sy-player-node"></div>
    </div>
    <div v-show="!videoUrl && !loading" class="sy-player-empty">
      <sy-icon class="sy-player-empty__icon" name="empty" />
      <span class="sy-player-empty__text">暂无接入设备</span>
    </div>

    <ImageModal :visible.sync="imageModal" :image-url="imageUrl" :title="title"></ImageModal>
  </div>
</template>

<script>
// import { baseUrl } from '@/config';
import { uniqueId } from 'lodash';

export default {
  name: 'JessiPlayer',
  components: {
    ImageModal: () => import('@/components/Modal/Image'),
  },
  computed: {
    playerId() {
      return uniqueId('player_');
    },
  },
  props: {
    // 视频地址
    videoUrl: {
      require: true,
      type: String,
      default: '',
      // default: 'https://sf1-hscdn-tos.pstatp.com/obj/media-fe/xgplayer_doc_video/flv/xgplayer-demo-480p.flv',
    },
    // 录像机ID
    nvrId: {
      type: [Number, String],
      default: '',
    },
    // 摄像机ID
    cameraId: {
      type: [Number, String],
      default: '',
    },
    // 视频流ID
    streamId: {
      type: [Number, String],
      default: '',
    },
    // 通道号
    channelNo: {
      type: [Number, String],
      default: '',
    },
    title: {
      type: String,
      default: '',
    },
    // 自动播放
    autoplay: {
      type: Boolean,
      default: true,
    },
    // 视频配置
    options: {
      type: Object,
      default: () => ({}),
    },
  },
  watch: {
    videoUrl(url) {
      // console.log('videoUrl: ', url);
      if (!url) {
        this.closeVideo();
      } else {
        this.$nextTick(() => {
          if (!this.player) {
            this.initPlayer();
          } else {
            this.playVideo();
          }
        });
      }
    },
    imageModal(bool) {
      if (!bool) {
        // 图片模态框关闭时释放截图对象
        if (this.imageBlob) {
          URL.revokeObjectURL(this.imageBlob);
          this.imageBlob = null;
        }
      }
    },
  },
  data() {
    return {
      video: null,
      player: null,
      playing: false,
      loading: false,
      recording: false,
      imageUrl: '',
      imageTitle: '',
      imageModal: false,
      imageBlob: null,
    };
  },
  mounted() {
    if (this.videoUrl) {
      this.initPlayer();
    }
  },
  beforeDestroy() {
    this.destroyPlayer();
  },
  methods: {
    handlePlayerAction(action) {
      switch (action) {
        case 'close':
          if (this.nvrId && this.streamId) {
            this.$api.getVideoApi('pauseplayback', {
              devidx: this.nvrId,
              stream: this.streamId,
              pause: 1, // 播放-0/暂停-1
            });
          }
          this.$emit('close');
          this.destroyPlayer();
          break;
        case 'record':
          if (!this.nvrId || !this.channelNo) return;
          this.$api
            .getVideoApi(this.recording ? 'stopRecord' : 'startRecord', {
              devidx: this.nvrId,
              channelno: this.channelNo,
            })
            .then((res) => {
              console.log(res);
              if (res?.code < 0) return;
              this.recording = !this.recording;
              if (this.recording) {
                this.$message.success('视频录制开始！');
              } else {
                this.$message.info('视频录制结束！');
              }
            });
          break;
        case 'screenshot':
          if (!this.nvrId || !this.channelNo) return;
          this.imageBlob = this.screenshot({ type: 'blob' });
          this.imageUrl = URL.createObjectURL(this.imageBlob);
          this.imageModal = true;

          this.$emit('screenshot', {
            title: this.title,
            image: this.imageBlob,
          });
          // this.$api
          //   .getVideoApi('getpicture', {
          //     devidx: this.nvrId,
          //     channelno: this.channelNo,
          //     picsize: this.picsize,
          //   })
          //   .then((res) => {
          //     if (!res) return;
          //     const image = `${baseUrl}/video/api/v2/getpicture?devidx=${this.nvrId}&channelno=${this.channelNo}&picsize=${this.picsize}`;

          //     this.imageUrl = image;
          //     this.imageModal = true;
          //   });
          break;
        default:
          break;
      }
    },

    initPlayer() {
      this.loading = true;

      this.player = new Jessibuca({
        container: this.$refs.player,
        background: '', // 背景图片
        debug: false, // 开启控制台调试
        forceNoOffscreen: true, // 禁用离屏模式(提升渲染能力)
        hasAudio: false, // 是否有音频,设为false则不解码音频数据,提升性能
        heartTimeout: 10, // 心跳超时时长
        hiddenAutoPause: false, // 监听'visibilityState'自动暂停播放
        isFlv: false, // 是否检验flv协议
        isResize: false, // 是否Contain显示画面
        isNotMute: true, // 是否开启声音
        isFullResize: false, // 是否Cover显示画面
        keepScreenOn: false, // 开启屏幕常亮
        loadingText: '加载中...', // 加载提示文案
        loadingTimeout: 10, // 加载超时时长
        rotate: 0, // 设置旋转角度, 只支持 0/180/270 三个值
        showBandwidth: false, // 是否显示网速
        supportDblclickFullscreen: true, // 是否双击全屏
        timeout: 10, // 连接超时时长
        useMSE: false, // 开启MSE硬解码
        useWCS: false, // 开启WCS硬解码
        videoBuffer: 1, // 最大缓冲时长
        operateBtns: {
          play: true, // 播放暂停
          audio: true, // 声音
          record: false, // 录像
          fullscreen: true, // 全屏
          screenshot: true, // 截图
        },
        ...this.options,
      });

      if (this.videoUrl && this.autoplay) {
        this.player.play(this.videoUrl);
      }

      this.player.on('log', (data) => {
        console.log('log: ', data);
      });
      // 视频初始化
      this.player.on('load', () => {
        this.loading = false;
      });
      // 视频开始播放
      this.player.on('play', () => {
        // console.log('play: ');
        this.playing = true;
        this.$emit('play');
      });
      // 视频暂停播放
      this.player.on('pause', () => {
        // console.log('pause: ');
        this.playing = false;
        this.$emit('pause');
      });
      // 视频渲染开始
      this.player.on('start', () => {
        // console.log('start: ', this.videoUrl);
      });
      // 视频错误信息
      this.player.on('error', (err) => {
        console.error('error: ', err);
        switch (err) {
          case Jessibuca.ERROR.playError:
            console.log('播放错误');
            break;
          case Jessibuca.ERROR.fetchError:
            console.log('Http请求失败');
            break;
          case Jessibuca.ERROR.websocketError:
            console.log('WebSocket请求失败');
            break;
          case Jessibuca.ERROR.wasmDecodeError:
            console.log('Wasm解码失败');
            break;
          case Jessibuca.ERROR.webcodecsH265NotSupport:
            console.log('WebCodecs解码h265失败');
            break;
          case Jessibuca.ERROR.mediaSourceH265NotSupport:
            console.log('MediaSource解码h265失败');
            break;
          default:
            break;
        }
      });
      // 视频声音事件
      // this.player.on('mute', (val) => {
      //   console.log('mute: ', val);
      // });
      // 视频状态信息
      // this.player.on('stats', (data) => {
      //   console.log('stats: ', data);
      // });
      // 解析音频信息
      // this.player.on('audioInfo', (data) => {
      //   console.log('audioInfo: ', data);
      // });
      // 解析视频信息
      // this.player.on('videoInfo', (data) => {
      //   console.log('videoInfo: ', data);
      // });
      // 视频播放进度
      // this.player.on('timeUpdate', (timestamp) => {
      //   console.log('timeUpdate: ', timestamp);
      // });
      // 视频全屏播放
      // this.player.on('fullscreen', (val) => {
      //   console.log('fullscreen: ', val);
      // });
      // 视频渲染状态
      // this.player.on('performance', (performance) => {
      //   console.log('performance: ', performance);
      // });
      // 视频播放超时
      // this.player.on('delayTimeout', () => {
      //   console.log('delayTimeout: ');
      // });
      // 视频加载超时
      // this.player.on('loadingTimeout', () => {
      //   console.log('loadingTimeout: ');
      // });
      // 视频录制开始
      // this.player.on('recordStart', (timestamp) => {
      //   console.log('recordStart: ', timestamp);
      // });
      // 视频录制结束
      // this.player.on('recordEnd', (timestamp) => {
      //   console.log('recordEnd: ', timestamp);
      // });
      // 视频录制时长
      // this.player.on('recordingTimestamp', (timestamp) => {
      //   console.log('recordingTimestamp: ', timestamp);
      // });
    },
    destroyPlayer() {
      if (this.player) {
        this.player.destroy();
        this.player = null;
        this.playing = false;
      }
    },

    initVideo() {
      this.$nextTick(() => {
        this.video = this.$refs.player.getElementsByTagName('video')[0];
      });
    },
    muteVideo(mute) {
      if (!this.player) return;
      if (mute) {
        this.player.mute();
      } else {
        this.player.cancelMute();
      }
    },
    playVideo() {
      if (!this.player) return;

      if (this.player.isPlaying()) {
        this.player.close();
      }
      return new Promise((resolve, reject) => {
        this.player
          .play(this.videoUrl)
          .then((res) => {
            // console.log(res);
            resolve(res);
            this.playing = true;
          })
          .catch((err) => {
            // console.error(err);
            reject(err);
          });
      });
    },
    pauseVideo() {
      if (!this.player) return;
      return new Promise((resolve, reject) => {
        this.player
          .pause()
          .then((res) => {
            resolve(res);
            this.playing = false;
          })
          .catch((err) => {
            reject(err);
          });
      });
    },
    clearVideo() {
      if (!this.player) return;
      this.player.clearView();
    },
    closeVideo() {
      if (!this.player) return;
      this.player.close();
      this.playing = false;
    },
    resizeVideo() {
      if (!this.player) return;
      this.player.resize();
    },
    destroyVideo() {
      if (!this.player) return;
      this.player.destroy();
      this.playing = false;
    },

    resumeAudio() {
      if (!this.player) return;
      this.player.audioResume();
    },
    screenshot({
      type = 'download' /** blob/base64/download */,
      format = 'png' /** png/jpeg/webp */,
      quality = 0.92 /** 取值0~1 */,
      filename = Date.now(),
    }) {
      if (!this.player) return;
      return this.player.screenshot(filename || Date.now(), format || 'png', quality || 0.92, type || 'download');
    },
    setRotate(deg) {
      if (!this.player) return;
      if (![0, 180, 270].contains(deg)) return;
      this.player.setRotate(deg);
    },
    setVolume(vol) {
      if (!this.player) return;
      this.player.setVolume(vol);
    },
    setScaleMode(mode) {
      if (!this.player) return;
      this.player.setScaleMode(mode);
    },
    setBufferTime(time) {
      if (!this.player) return;
      this.player.setBufferTime(time);
    },
    setFullscreen(flag) {
      if (!this.player) return;
      this.player.setFullscreen(flag);
    },
    startRecord(fileName, fileType = 'webm' /** mp4/webm */) {
      if (!this.player) return;
      this.player.startRecord(fileName, fileType);
    },
    stopRecord() {
      if (!this.player) return;
      this.player.stopRecordAndSave();
    },
  },
};
</script>

<style lang="scss" scoped>
.sy-player {
  width: 100%;
  height: 100%;
  transition: all 0.3s;
  border: $border-default;
  background-color: $bg-video;

  &.active {
    border: 2px solid $border-lighter;
  }
  &.mode-4 {
    width: 50%;
    height: 50%;
  }
  &.mode-9 {
    width: 25%;
    height: 25%;
  }
  &-wrapper {
    position: relative;
    width: 100%;
    height: 100%;
    padding-top: 3vh;
  }
  &-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 3vh;
    padding: 0 10px;
    color: $text-regular;
    font-size: 16px;
    font-weight: bold;
    background-color: #222;

    &__action {
      > i {
        font-size: 18px;
        cursor: pointer;

        + i {
          margin-left: 5px;
        }
        &:hover {
          color: $text-primary;
        }
        &.el-icon-video-camera-solid {
          color: $text-primary;
        }
      }
    }
  }
  &-node {
    width: 100%;
    height: 100%;
  }
  &-empty {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    font-size: 18px;
    color: $text-primary;

    &__icon {
      margin-right: 5px;
      margin-bottom: 10px;
      font-size: 32px;
    }
  }
  .player-wrapper {
    width: 100%;
    height: 100%;
    ::v-deep .player-content,
    ::v-deep .video-wrapper {
      width: 100% !important;
      height: 100% !important;
      padding-bottom: unset !important;
    }
  }
}
</style>
