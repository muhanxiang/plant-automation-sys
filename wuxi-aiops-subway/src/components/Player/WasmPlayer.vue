<template>
  <div class="sy-player" ref="player" @click="$emit('click')">
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
      <div :id="playerId" class="sy-player-node"></div>
      <el-slider
        v-if="duration > 0"
        class="sy-player-slider"
        v-model="progress"
        :max="duration"
        :tip-formatter="(current) => Math.round((current / duration) * 100) + '%'"
        @change="onSliderChange"
      />
    </template>
    <div v-else class="sy-player-empty">
      <sy-icon class="sy-player-empty__icon" name="empty" />
      <span class="sy-player-empty__text">暂无接入设备</span>
    </div>

    <ImageModal :visible.sync="imageModal" :image-url="imageUrl" :title="title"></ImageModal>
  </div>
</template>

<script>
import flvjs from 'flv.js';
import { uniqueId } from 'lodash';
import { baseUrl } from '@/config';

export default {
  name: 'WasmPlayer',
  components: {
    ImageModal: () => import('@/components/Modal/Image'),
  },
  computed: {
    playerId() {
      return uniqueId('player_');
    },
  },
  props: {
    videoUrl: {
      require: true,
      type: String,
      default: 'https://sf1-hscdn-tos.pstatp.com/obj/media-fe/xgplayer_doc_video/flv/xgplayer-demo-480p.flv',
    },
    nvrId: {
      type: [Number, String],
      default: '100110000000000000',
    },
    streamId: {
      type: [Number, String],
      default: '',
    },
    cameraId: {
      type: [Number, String],
      default: '',
    },
    channelNo: {
      type: [Number, String],
      default: '100110000003010001',
    },
    title: {
      type: String,
      default: '',
    },
    duration: {
      type: Number,
      default: 0,
    },
    autoplay: {
      type: Boolean,
      default: true,
    },
    screenshot: {
      type: Boolean,
      default: false,
    },
    options: {
      type: Object,
      default() {
        return {};
      },
    },
  },
  watch: {
    videoUrl(url) {
      this.initPlayer(url);
    },
  },
  data() {
    return {
      video: null,
      player: null,
      interval: null,
      recording: false,
      progress: 0,
      imageUrl: '',
      imageTitle: '',
      imageModal: false,
      baseOptions: {
        Height: true,
        BigPlay: true,
        HideKbs: true,
        openAudio: true,
        decodeType: 'auto',
        cfKbs: (e) => {
          console.log(e);
        },
      },
    };
  },
  mounted() {
    if (this.videoUrl) {
      this.initPlayer(this.videoUrl);
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
    onDurationChange() {
      // console.log('onDurationChange: ', this.video.duration);
    },
    onSliderChange(val) {
      if (!this.interval) {
        this.interval = setInterval(() => {
          this.progress++;
          if (this.progress === this.duration) {
            clearInterval(this.interval);
            this.interval = null;
          }
        }, 1000);
      }
      this.$emit('seeking', val);
    },
    initSlider() {
      if (this.duration > 0 && !this.interval) {
        this.interval = setInterval(() => {
          this.progress++;
          if (this.progress === this.duration) {
            clearInterval(this.interval);
            this.interval = null;
          }
        }, 1000);
      }
    },

    initPlayer(url) {
      if (flvjs.isSupported()) {
        // 切换流之前先切断流
        this.destroyVideo();

        this.player = new WasmPlayer(null, this.playerId, this.callback, {
          ...this.baseOptions,
          ...this.options,
        });
        try {
          this.player.play(url, this.autoplay ? 1 : 0, this.options.currentTime || 0);
        } catch (error) {
          console.log(error);
        }
        // 初始化播放进度条
        this.initSlider();
      } else {
        this.$message.warn('当前浏览器暂不支持FLV视频流，建议使用谷歌/火狐浏览器！');
      }
    },
    destroyPlayer() {
      if (this.video) {
        this.video.removeEventListener('durationchange', this.onDurationChange);
      }
      if (this.player) {
        this.player.destroy();
        this.player = null;
      }
      if (this.interval) {
        clearInterval(this.interval);
        this.interval = null;
      }
    },
    callback(/** event */) {
      this.video = this.$refs.player.getElementsByTagName('video')[0];
      if (this.video) {
        // console.log('callback: ', event);
        this.video.addEventListener('durationchange', this.onDurationChange);
      }
    },
    playVideo() {
      if (!this.player) return;
      this.player.play();
      this.playing = true;
    },
    pauseVideo() {
      if (!this.player) return;
      this.player.pause();
      this.playing = false;
    },
    destroyVideo() {
      if (!this.player) return;
      this.player.destroy();
      this.playing = false;

      if (this.interval) {
        clearInterval(this.interval);
        this.interval = null;
      }
    },
    openAudio() {
      if (!this.player) return;
      this.player.openAudio();
    },
    closeAudio() {
      if (!this.player) return;
      this.player.closeAudio();
    },
    fullScreen() {
      if (!this.player) return;
      this.player.fullScreen();
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

  &:hover {
    .sy-player-slider {
      visibility: visible;
    }
  }
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
    color: #fff;
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
  &-slider {
    z-index: 100;
    display: none;
    position: absolute;
    left: 150px;
    right: 150px;
    bottom: 0;
    margin: 8px 0;
    visibility: hidden;
    transition: visibility 5s;
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
}
</style>
