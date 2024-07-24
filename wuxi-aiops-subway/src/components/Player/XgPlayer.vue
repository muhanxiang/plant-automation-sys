<template>
  <div class="sy-player" @click="$emit('click')">
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
      <div :id="playerId" ref="player" class="sy-player-node"></div>
    </template>
    <div v-else class="sy-player-empty">
      <sy-icon class="sy-player-empty__icon" name="empty" />
      <span class="sy-player-empty__text">暂无接入设备</span>
    </div>

    <ImageModal :visible.sync="imageModal" :image-url="imageUrl" :title="title"></ImageModal>
  </div>
</template>

<script>
import { uniqueId } from 'lodash';
import { baseUrl } from '@/config';
import 'xgplayer';
import FlvPlayer from 'xgplayer-flv';
import FlvjsPlayer from 'xgplayer-flv.js';
// import { progress } from 'sy-player/dist/controls';

export default {
  name: 'XgPlayer',
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
    type: {
      type: String,
      default: 'flvjs',
    },
    title: {
      type: String,
      default: '',
    },
    autoplay: {
      type: Boolean,
      default: true,
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
      if (this.player) {
        if (this.player.hasStart) {
          this.player.src = url;
        } else {
          this.player.start(url);
        }
      }
    },
  },
  data() {
    return {
      player: null,
      loading: false,
      playing: false,
      recording: false,
      imageUrl: '',
      imageTitle: '',
      imageModal: false,
      baseOptions: {
        width: '100%',
        height: '100%',
        airplay: false, // 播放镜像
        autoplay: true, // 自动播放
        controls: true, // 视频控制条
        controlsList: ['nodownload'], // 控制条选项配置
        cssFullscreen: false, // 网页样式全屏
        download: true, // 视频下载
        definitionActive: 'click', // 清晰度控件触发方式
        defaultPlaybackRate: 1, // 默认播放速度
        errorTips: '请刷新重试！', // 自定义错误提示
        execBeforePluginsCall: [], // 插件生效前执行回调
        fluid: false, // 流式布局
        fitVideoSize: 'auto', // 自适应宽高
        ignores: [], // 关闭内置控件
        keyShortcut: 'on', // 键盘快捷键
        // 键盘快捷调整配置
        keyShortcutStep: {
          currentTime: 10, // 播放进度调整步长，默认10秒
          volume: 0.1, // 音量调整步长，默认0.1
        },
        lang: 'zh-cn', // 国际化 zh-cn|en|jp
        loop: false, // 循环播放
        lastPlayTime: 0, // 视频起播时间（单位：秒）
        lastPlayTimeHideDelay: 0, // 提示文字展示时长（单位：秒）
        miniplayer: false, // 迷你播放器
        // 迷你播放器配置
        miniplayerConfig: {
          bottom: 200,
          right: 0,
          width: 320,
          height: 180,
        },
        pip: false, // 画中画
        poster: '', // 封面图
        pluginRule: () => {}, // 功能插件开关配置
        playsinline: false, // 内联模式
        progressDot: [], // 进度条特殊点标记
        playbackRate: [0.5, 0.75, 1, 1.5, 2], // 播放速度选项
        playbackRateUnit: 'X', // 播放速度单位
        rotateFullscreen: false, // 横屏全屏
        volume: 0.6, // 视频音量
        videoInit: true, // 初始化显示视频首帧
        cors: true, // 请求是否跨域
        isLive: true, // 是否直播
        preloadTime: 30, // 预加载时长
        minCachedTime: 5, // 最小缓存时长
        // 自定义配置
        customConfig: {},
        // 弹幕配置
        danmu: {
          panel: true, // 弹幕控制面板
          defaultOff: true, // 默认关闭弹幕
          closeDefaultBtn: true, // 默认使用西瓜播放器开关
          comments: [], // 弹幕数组
          area: {
            // 弹幕显示区域
            start: 0, // 区域顶部到播放器顶部所占播放器高度的比例
            end: 0.5, // 区域底部到播放器顶部所占播放器高度的比例
          },
        },
        // 本地视频预览
        preview: {},
        // 视频旋转配置
        rotate: {
          clockwise: false, // 顺时针旋转
          innerRotate: true, // 只旋转内部VIDEO
        },
        // 截图配置
        screenShot: {
          format: '.png', // 图片下载后缀
          quality: 0.92, // 截图质量
          saveImg: true, // 是否下载截图
          type: 'image/png', // 图片格式
        },
        // 视频预览配置
        thumbnail: {
          pic_num: 44,
          width: 160,
          height: 90,
          col: 10,
          row: 10,
          urls: [],
          isShowCoverPreview: false,
        },
      },
      flvOptions: {
        type: 'flv', // 视频流类型
        cors: true, // 启用CORS
        isLive: true, // 是否为实时流
        hasVideo: true, // 是否有视频
        hasAudio: false, // 是否有音频
        withCredentials: false, // 请求携带Cookie
        // duration: 0, // 视频总时长，单位毫秒
        // filesize: 0, // 视频流大小，单位字节
        // segments: [], // 多选播放配置
      },
      flvConfig: {
        accurateSeek: true, // 精确跳转视频帧
        autoCleanupSourceBuffer: false, // 是否自动清理SourceBuffer
        autoCleanupMaxBackwardDuration: 180, // 自动清理SourceBuffer最大缓冲时长
        autoCleanupMinBackwardDuration: 120, // 自动清理SourceBuffer最小缓冲时长
        customSeekHandler: undefined, // 自定义查找函数
        deferLoadAfterSourceOpen: true, // MediaSource sourceopen事件触发后加载
        enableWorker: true, // 启用独立解码线程
        enableStashBuffer: false, // 启用IO缓冲区，实时播放时设为false
        fixAudioTimestampGap: false, // 自动填充静音音频帧
        headers: undefined, // 自定义请求头
        isLive: false, // 视频配置过忽略
        lazyLoad: true, // 启用视频缓冲
        lazyLoadMaxDuration: 180, // 视频缓冲时长
        lazyLoadRecoverDuration: 30, // 视频恢复时长
        referrerPolicy: 'no-referrer-when-downgrade', // 使用FetchStreamLoader时推荐策略
        rangeLoadZeroStart: false, // 范围查找时第一次请求发送Range: bytes=0-
        reuseRedirectedURL: false, // 重定向子序列请求
        seekType: 'range', // 'range'-范围请求/'param'-URL参数请求
        seekParamEnd: 'bend', // ''param'查找结束参数
        seekParamStart: 'bstart', // ''param'查找开始参数
        stashInitialSize: 128, // IO缓冲区大小，默认384KB
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
    initPlayer(url) {
      if (this.type === 'flvjs') {
        this.player = new FlvjsPlayer({
          // id: this.$refs.player,
          id: this.playerId,
          url: url,
          width: '100%',
          height: '100%',
          autoplay: true, // 自动播放
          controls: true, // 视频控制条
          controlsList: ['nodownload'], // 控制条选项配置
          cssFullscreen: false, // 网页样式全屏
          download: true, // 视频下载
          definitionActive: 'hover', // 清晰度控件触发方式
          defaultPlaybackRate: 1, // 默认播放速度
          errorTips: '请刷新重试！', // 自定义错误提示
          fluid: false, // 流式布局
          fitVideoSize: 'auto', // 自适应宽高
          keyShortcut: 'on', // 键盘快捷键
          lang: 'zh-cn', // 国际化 zh-cn|en|jp
          pip: false, // 画中画
          volume: 0.6, // 视频音量
          videoInit: false, // 初始化显示视频首帧
          flvOptionalConfig: this.flvConfig, // Flvjs自定义配置
          ...this.flvOptions, // Flvjs配置项
          ...this.options,
        });
      } else {
        this.player = new FlvPlayer({
          // id: this.$refs.player,
          id: this.playerId,
          url: url,
          width: '100%',
          height: '100%',
          autoplay: true, // 自动播放
          controls: true, // 视频控制条
          controlsList: ['nodownload'], // 控制条选项配置
          cssFullscreen: false, // 网页样式全屏
          download: true, // 视频下载
          definitionActive: 'hover', // 清晰度控件触发方式
          defaultPlaybackRate: 1, // 默认播放速度
          errorTips: '请刷新重试！', // 自定义错误提示
          fluid: false, // 流式布局
          fitVideoSize: 'auto', // 自适应宽高
          keyShortcut: 'on', // 键盘快捷键
          lang: 'zh-cn', // 国际化 zh-cn|en|jp
          pip: false, // 画中画
          volume: 0.6, // 视频音量
          videoInit: false, // 初始化显示视频首帧

          cors: true, // 请求是否跨域
          isLive: false, // 是否直播
          preloadTime: 30, // 预加载时长
          minCachedTime: 5, // 最小缓存时长
          ...this.options,
        });
      }

      this.player.on('error', (err) => {
        console.log('error: ', err);
      });
      this.player.on('screenShot', (data) => {
        console.log('screenShot', data);
      });
      // this.player.on('play', () => {
      //   console.log('play');
      // });
      // this.player.on('pause', () => {
      //   console.log('pause');
      // });
      // this.player.on('ended', () => {
      //   console.log('ended');
      // });
      // this.player.on('seeking', () => {
      //   console.log('seeking', this.player.currentTime);
      // });
      // this.player.on('volumechange', (val) => {
      //   console.log('volumechange', val);
      // });
      // this.player.on('bufferedChange', (data) => {
      //   console.log('bufferedChange', data);
      // });
      // this.player.on('durationchange', (data) => {
      //   console.log('durationchange', data);
      // });
      // this.player.on('definitionChange', (data) => {
      //   console.log('definitionChange', data);
      // });
      // this.player.on('playbackrateChange', (data) => {
      //   console.log('playbackrateChange', data);
      // });
    },
    destroyPlayer() {
      if (!this.player) return;

      this.player.destroy();
      this.player = null;
      this.playing = false;
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
    startVideo(url) {
      if (!this.player) return;
      this.player.start(url);
    },
    reloadVideo() {
      if (!this.player) return;
      this.player.reload();
    },
    replayVideo() {
      if (!this.player) return;
      this.player.replay();
    },
    destroyVideo(isDelDom) {
      if (!this.player) return;
      this.player.destroy(isDelDom);
      this.playing = false;
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
  &-node {
    width: 100% !important;
    height: 100%;
    outline: none;
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
