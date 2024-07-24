<template>
  <div class="sy-player" v-loading="loading">
    <template v-if="videoUrl">
      <header class="sy-player-header">
        <span class="sy-player-header__title">{{ title }}</span>
        <span class="sy-player-header__action">
          <i
            :class="recording ? 'el-icon-video-camera-solid' : 'el-icon-video-camera'"
            @click="handlePlayerAction('record')"
          />
          <i class="el-icon-picture-outline" @click="handlePlayerAction('screenshot')" />
          <i class="el-icon-circle-close" @click="handlePlayerAction('close')" />
        </span>
      </header>
      <easy-player
        ref="player"
        :video-url="videoUrl"
        :live="live"
        :loop="loop"
        :muted="muted"
        :speed="speed"
        :aspect="aspect"
        :fluent="fluent"
        :poster="poster"
        :stretch="stretch"
        :timeout="timeout"
        :isaspect="isaspect"
        :autoplay="autoplay"
        :decodeWasm="decodeWasm"
        :isresolution="isresolution"
        :resolutiondefault="defaultResolution"
        :show-custom-button="customButton"
        @play="onVideoPlay"
        @pause="onVideoPause"
        @ended="onVideoEnded"
        @message="onVideoMessage"
        @video-url="onVideoUrlChange"
        @timeupdate="onVideoTimeUpdate"
      ></easy-player>
    </template>
    <div v-else class="sy-player-empty">
      <sy-icon class="sy-player-empty__icon" name="empty" />
      <span class="sy-player-empty__text">暂无接入设备</span>
    </div>

    <ImageModal :visible.sync="imageModal" :image-url="imageUrl" :title="title"></ImageModal>
  </div>
</template>

<script>
import { baseUrl } from '@/config';
import EasyPlayer from '@easydarwin/easyplayer';

export default {
  name: 'SyPlayer',
  components: {
    EasyPlayer,
    ImageModal: () => import('@/components/Modal/Image'),
  },
  props: {
    // 视频地址
    videoUrl: {
      require: true,
      type: String,
      default: 'https://sf1-hscdn-tos.pstatp.com/obj/media-fe/xgplayer_doc_video/flv/xgplayer-demo-480p.flv',
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
    // 是否直播
    live: {
      type: Boolean,
      default: true,
    },
    // 是否轮播
    loop: {
      type: Boolean,
      default: false,
    },
    // 是否静音
    muted: {
      type: Boolean,
      default: false,
    },
    // 是否显示倍速按钮,当 live=true 时此属性不生效;
    speed: {
      type: Boolean,
      default: true,
    },
    // 视频标题
    title: {
      type: String,
      default: '',
    },
    // 视频显示宽高比
    aspect: {
      type: String,
      default: '16:9',
    },
    // 流畅模式
    fluent: {
      type: Boolean,
      default: true,
    },
    // 视频封面
    poster: {
      type: String,
      default: '',
    },
    // 截图尺寸
    picsize: {
      type: Number,
      default: 255,
    },
    // 不同分辨率是否强制拉伸
    stretch: {
      type: Boolean,
      default: false,
    },
    // 加载超时(秒)
    timeout: {
      type: Number,
      default: 20,
    },
    // 自动播放
    autoplay: {
      type: Boolean,
      default: true,
    },
    // 视频显示是否强制宽高比
    isaspect: {
      type: Boolean,
      default: true,
    },
    // 强制H265解码(支持:HLS/FLV/WS)
    decodeWasm: {
      type: Boolean,
      default: false,
    },
    // 是否显示自定义按钮(极速/流畅,拉伸/标准)
    customButton: {
      type: Boolean,
      default: true,
    },
    // 是否选择清晰度
    isresolution: {
      type: Boolean,
      default: false,
    },
    // 默认清晰度(yh:原始分辨率/fhd:超清/hd:高清/sd:标清)
    defaultResolution: {
      type: String,
      default: 'hd',
    },
    // HTTP-FLV播放配置
    flvOptions: {
      type: Object,
      default: () => ({ hasaudio: true, hasvideo: true }),
    },
  },
  data() {
    return {
      video: null,
      player: null,
      loading: false,
      playing: false,
      recording: false,
      imageUrl: '',
      imageTitle: '',
      imageModal: false,
    };
  },
  mounted() {
    if (this.videoUrl) {
      this.loading = true;
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
              if (!res?.code < 0) return;
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
          this.$api
            .getVideoApi('getpicture', {
              devidx: this.nvrId,
              channelno: this.channelNo,
              picsize: this.picsize,
            })
            .then((res) => {
              if (!res) return;
              const image = `${baseUrl}/video/api/v2/getpicture?devidx=${this.nvrId}&channelno=${this.channelNo}&picsize=${this.picsize}`;

              this.imageUrl = image;
              this.imageModal = true;
              this.$emit('screenshot', {
                image: `${baseUrl}/video/api/v2/getpicture?devidx=${this.nvrId}&channelno=${this.channelNo}&picsize=${this.picsize}`,
                title: this.title,
              });
            });
          break;
        default:
          break;
      }
    },
    onVideoPlay(progress) {
      this.playing = true;
      this.loading = false;
      console.log('onVideoPlay: ', progress);
    },
    onVideoPause(progress) {
      this.playing = false;
      console.log('onVideoPause: ', progress);
    },
    onVideoEnded() {
      this.playing = false;
      console.log('onVideoEnded！');
    },
    onVideoMessage(msg) {
      console.log('onVideoMessage: ', msg);
      switch (msg.type) {
        case 'error':
          if (this.loading) {
            this.loading = false;
          }
          if (msg.message) {
            this.$message({
              type: 'error',
              message: msg.message,
            });
          }
          break;
        default:
          break;
      }
    },
    onVideoUrlChange(data) {
      console.log('onVideoUrlChange: ', data);
    },
    onVideoTimeUpdate(/* progress */) {
      // console.log('onVideoTimeUpdate: ', progress);
    },

    initPlayer() {
      this.$nextTick(() => {
        this.video = this.$refs.player.getElementsByTagName('video')[0];
      });
    },
    destroyPlayer() {
      if (this.player) {
        this.player.destroy();
        this.player = null;
        this.playing = false;
      }
    },
    playVideo() {
      this.player && this.player.play();
    },
    pauseVideo() {
      this.player && this.player.pause();
    },
  },
};
</script>

<style lang="scss" scoped>
.sy-player {
  position: relative;
  width: 100%;
  height: 100%;
  padding-top: 3vh;
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
