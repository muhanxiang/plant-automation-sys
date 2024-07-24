<template>
  <el-row id="video_playback" class="sy-row" :gutter="10">
    <el-col class="sy-col" :span="5">
      <div class="video-tree sy-view">
        <div class="video-tree-search">
          <el-input
            v-model.trim="cameraName"
            size="medium"
            suffix-icon="el-icon-search"
            placeholder="请输入关键字搜索"
            :maxlength="20"
            clearable
            @input="onCameraSearch"
          ></el-input>
        </div>

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
            <el-radio-group v-if="cameraList.length > 0" class="sy-radio-list" v-model="cameraSelect">
              <el-radio
                v-for="item in cameraList"
                :key="item.guid"
                :label="item.guid"
                @change="onCameraChange(item, $event)"
              >
                {{ item.name }}
              </el-radio>
            </el-radio-group>
            <sy-empty v-else-if="!cameraLoad" description="暂无搜索结果"></sy-empty>
          </div>
        </div>
      </div>
    </el-col>

    <el-col class="sy-col" :span="14">
      <div class="video-player sy-view">
        <div class="video-player-header">
          <span class="video-player-header__display">
            <sy-icon
              v-for="(item, key) in displayModes"
              :key="key"
              :name="`split-screen-${item}`"
              :class="{ active: displayMode === item }"
              @click="handleSwitchMode(item)"
            />
          </span>

          <span class="video-player-header__action">
            <el-button class="sy-btn-regular" type="primary" size="small" @click="handleVideoAction('closeAll')"
            >全部关闭
            </el-button>
          </span>
        </div>
        <div class="video-player-wrapper">
          <div class="video-player-list" :class="`layout-${displayMode}`">
            <sy-player
              v-for="(item, index) in videoList"
              v-bind="{ ...item, nvrId: nvrId, cameraId: cameraId, channelNo: channelNo, streamId: item.stream }"
              :key="item.guid || index"
              :ref="`player-${index}`"
              :class="{ active: videoIndex === index }"
              @click.native="videoIndex = index"
              @play="onPlayerEvent('play', item)"
              @pause="onPlayerEvent('pause', item)"
              @close="onPlayerEvent('close', index)"
            ></sy-player>
          </div>
        </div>
      </div>
    </el-col>

    <el-col class="sy-col" :span="5">
      <div class="video-replay sy-view" v-loading="videoLoad">
        <sy-calendar class="video-replay-calendar" v-model="videoDate" @change="getVideoFileList"></sy-calendar>
        <!-- <el-form class="video-replay-form" :form="videoForm" label-position="left" label-width="80px"></el-form> -->

        <div class="video-replay-control">
          <div class="video-replay-time">
            <el-time-picker
              v-model="videoTime"
              size="small"
              range-separator="至"
              placeholder="选择录像时间"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              :clearable="false"
              is-range
            >
            </el-time-picker>
            <el-button class="sy-btn-regular" type="primary" size="medium" @click="getVideoFileList"
            >查询录像</el-button
            >
          </div>

          <div class="video-replay-action">
            <el-button
              title="慢速播放"
              size="medium"
              icon="el-icon-d-arrow-left"
              :disabled="!videoActive.videoUrl"
              circle
              @click="handleVideoAction('slower')"
            ></el-button>
            <el-button
              title="上个录像"
              size="medium"
              icon="el-icon-arrow-left"
              :disabled="!videoActive.videoUrl"
              circle
              @click="handleVideoAction('prev')"
            ></el-button>
            <el-button
              class="center"
              :title="videoPlay ? '暂停' : '播放'"
              :disabled="!videoActive.videoUrl"
              :class="{ active: videoPlay }"
              :icon="videoPlay ? 'el-icon-video-pause' : 'el-icon-video-play'"
              circle
              @click="handleVideoAction('pause', videoActive)"
            ></el-button>
            <el-button
              title="下个录像"
              size="medium"
              icon="el-icon-arrow-right"
              :disabled="!videoActive.videoUrl"
              circle
              @click="handleVideoAction('next')"
            ></el-button>
            <el-button
              title="快速播放"
              size="medium"
              icon="el-icon-d-arrow-right"
              :disabled="!videoActive.videoUrl"
              circle
              @click="handleVideoAction('faster')"
            ></el-button>
          </div>

          <div class="video-replay-speed">
            <label for="">播放速度：</label>
            <el-slider
              v-model="videoSpeed"
              :disabled="!videoPlay"
              :marks="videoMarks"
              :step="0.25"
              :min="0.25"
              :max="3"
            ></el-slider>
          </div>
        </div>

        <div class="video-replay-file">
          <h3 class="video-replay-file__title">视频录像列表</h3>
          <div class="video-replay-file__wrap">
            <ul v-if="videoFileList.length > 0" class="video-replay-file__list">
              <li
                class="video-replay-file__item"
                v-for="item in videoFileList"
                :key="item.guid"
                :class="{ active: videoActive.guid === item.guid }"
              >
                <label class="video-replay-file__name">{{ item.name }}</label>
                <span class="video-replay-file__action">
                  <el-button
                    class="sy-btn-regular"
                    size="mini"
                    :type="item.playing ? 'warning' : 'primary'"
                    @click="handleVideoAction('play', item)"
                  >{{ item.playing ? '暂停' : '播放' }}
                  </el-button>
                  <el-button
                    class="sy-btn-regular"
                    size="mini"
                    :type="item.download ? 'danger' : 'primary'"
                    @click="handleVideoAction('download', item)"
                  >{{ item.download ? '取消' : '下载' }}
                  </el-button>
                </span>
              </li>
            </ul>
            <sy-empty v-else description="暂无视频录像"></sy-empty>
          </div>
        </div>

        <!-- <el-tabs class="video-replay-tabs" type="border-card">
          <el-tab-pane label="录像列表">
          </el-tab-pane>
          <el-tab-pane label="录像下载">
          </el-tab-pane>
        </el-tabs> -->
      </div>
    </el-col>
  </el-row>
</template>

<script>
import { saveAs } from 'file-saver';
import { debounce, uniqueId } from 'lodash';
import { DisplayMode } from '@/enums';
import { baseUrl, assetUrl, mediaUrl } from '@/config';
import VideoTree from '@/mixins/video-tree';

export default {
  name: 'VideoPlayback',
  mixins: [VideoTree],
  computed: {
    displayModes() {
      return DisplayMode;
    },
    cameraSearch() {
      return this.cameraName.length > 0;
    },
    videoFile() {
      return this.videoFileList[this.videoFileIndex];
    },
    videoPlay() {
      return this.videoActive ? this.videoActive.playing : false;
    },
    videoActive() {
      return this.videoList[this.videoIndex];
    },
    videoSpeedList() {
      return Object.keys(this.videoMarks)
        .map(Number)
        .sort((a, b) => a - b);
    },
  },
  watch: {
    cameraSearch(bool) {
      if (bool) {
        const checkedKeys = this.$refs.tree.getCheckedKeys(true);
        if (checkedKeys[0]) {
          this.cameraSelect = checkedKeys[0];
        }
      } else {
        this.$nextTick(() => {
          this.cameraSelect && this.$refs.tree.setCheckedKeys([this.cameraSelect]);
        });
      }
    },
    videoActive(video) {
      this.videoFileIndex = this.videoFileList.findIndex((item) => item.guid === video.guid);
    },
    videoSpeed(speed) {
      if (speed in this.videoMarks) {
        this.onVideoSpeedChange(speed);
      }
    },
    videoStreams(arr) {
      // 播放器清空时重置控制参数
      if (arr.length === 0) {
        this.streamId = '';
      } else if (arr.length === 1) {
        this.videoFileIndex = 0;
      }
    },
    // 录像文件列表更新重置状态
    videoFileList() {
      this.videoList = Array.from({ length: this.displayMode }, () => ({ videoUrl: '' }));
      this.videoIndex = 0;
      this.videoFileIndex = 0;
    },
  },
  data() {
    const defaultMode = DisplayMode.SINGLE;
    return {
      displayMode: defaultMode,
      nvrId: '',
      streamId: '',
      cameraId: '',
      channelNo: 0,
      cameraLoad: false,
      cameraName: '',
      cameraList: [],
      cameraSelect: undefined,

      videoLoad: false,
      videoDate: new Date(),
      videoTime: [this.$dayjs().startOf('d'), this.$dayjs().endOf('d')],
      videoList: Array.from({ length: defaultMode }, () => ({ videoUrl: '' })),
      videoIndex: 0,
      videoFileList: [],
      videoFileIndex: 0,
      videoSpeed: 1,
      videoSpeedIndex: 2,
      videoMarks: { 0.25: '', 0.5: '0.5', 1: '1', 2: '2', 3: '3' },

      videoType: 0,
      videoImage: '',
      videoStreams: [],
      videoRecordSel: [],
      timeStart: this.$dayjs().startOf('d'),
      timeEnd: this.$dayjs().endOf('d'),
    };
  },
  methods: {
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
        const emptyList = Array.from({ length: mode - this.videoList.length }, () => ({ videoUrl: '' }));
        this.videoList = [...this.videoList, ...emptyList];
      }
    },
    // 视频流播放控制
    handleVideoAction(action, data) {
      if (!this.nvrId) return this.$message.warning('请先选择视频设备！');

      switch (action) {
        case 'play':
          if (data.played) {
            this.handleVideoAction('pause', data);
          } else {
            this.playVideoFile(data);
          }
          break;
        case 'pause':
        {
          this.videoLoad = true;
          this.$api
            .getVideoApi('pauseplayback', {
              devidx: this.nvrId,
              stream: data.stream,
              pause: data.playing ? 1 : 0, // 播放-0/暂停-1
            })
            .then(() => {
              const player = this.$refs[`player-${this.videoIndex}`][0];
              if (data.playing) {
                player && player.pauseVideo();
                this.$message.warning('视频播放暂停！');
              } else {
                player && player.playVideo();
                this.$message.success('视频播放开始！');
              }
              this.videoActive.playing = !data.playing;
            })
            .finally(() => {
              this.videoLoad = false;
            });
        }
          break;
        case 'prev':
        {
          let fileIndex = this.videoFileIndex;
          if (fileIndex === 0) {
            fileIndex = this.videoFileList.length - 1;
          } else {
            while (this.videoFileList[fileIndex]?.playing) {
              fileIndex--;
            }
          }
          this.switchVideoFile(this.videoFileIndex, fileIndex);
        }
          break;
        case 'next':
        {
          let fileIndex = this.videoFileIndex;
          if (fileIndex === this.videoFileList.length - 1) {
            fileIndex = 0;
          } else {
            while (this.videoFileList[fileIndex]?.playing) {
              fileIndex++;
            }
          }
          this.switchVideoFile(this.videoFileIndex, fileIndex);
        }
          break;
        case 'faster':
          if (this.videoSpeedIndex < this.videoSpeedList.length - 1) {
            this.videoSpeedIndex++;
            this.videoSpeed = this.videoSpeedList[this.videoSpeedIndex];
          }
          break;
        case 'slower':
          if (this.videoSpeedIndex > 0) {
            this.videoSpeedIndex--;
            this.videoSpeed = this.videoSpeedList[this.videoSpeedIndex];
          }
          break;
        case 'download':
          if (data.download) {
            this.cancelDowndload(data);
          } else {
            this.downloadVideoFile(data);
          }
          break;
        case 'closeAll':
          this.videoIndex = 0;
          this.$refs.tree.setCheckedKeys([]);
          this.videoList = this.videoList.map(() => ({ videoUrl: '' }));
          break;
        default:
          break;
      }
    },
    // 视频录像操作
    handleVideoRecord(action) {
      switch (action) {
        case 'fecth':
        {
          if (!this.nvrId) return this.$message.warning('请先选择视频设备！');
          this.videoLoad = true;
          const date = this.$dayjs(this.videoDate).format('YYYY-MM-DD');
          const timeEnd = this.$dayjs(this.timeEnd).format('HH:mm:ss');
          const timeStart = this.$dayjs(this.timeStart).format('HH:mm:ss');
          // console.log(this.$dayjs(`${date} ${timeStart}`));
          this.$api
            .getVideoApi('gethistoryrecord', {
              devidx: this.nvrId,
              channelno: this.channelNo,
              start: this.$dayjs(`${date} ${timeStart}`).unix(),
              stop: this.$dayjs(`${date} ${timeEnd}`).unix(),
            })
            .then((res) => {
              if (!res?.code < 0) return;
              this.$message.info('开始加载录像文件！');
              const { fileName, handle, video } = res;
              this.loadHandle = handle;
              this.videoFileList.push({
                loaded: false,
                handle: handle,
                name: fileName,
                url: video,
              });
            });
        }
          break;
        case 'check':
          this.$api
            .getVideoApi('/getrecordpos', {
              devidx: this.nvrId,
              handle: this.loadHandle,
            })
            .then((res) => {
              if (!res?.code < 0) return;
              if (res.pos === 100) {
                this.$message.success('录像文件加载完成！');
                const video = this.videoFileList.find((item) => item.handle === this.loadHandle);
                if (video) video.loaded = true;
                this.videoLoad = false;
              } else {
                this.$message.loading('录像文件加载中...！');
              }
            });
          break;
        case 'stop':
          this.$api
            .getVideoApi('/stophistoryrecord', {
              devidx: this.nvrId,
              handle: this.loadHandle,
            })
            .then(() => {
              this.$message.warning('停止加载录像文件！');
              const video = this.videoFileList.find((item) => item.handle === this.loadHandle);
              if (video) video.loaded = true;
              this.videoLoad = false;
            });
          break;
        default:
          break;
      }
    },
    onCameraSearch: debounce(function () {
      if (!this.cameraName) {
        this.cameraList = [];
        return;
      }
      this.cameraLoad = true;
      this.$api
        .getBaseApi('camera', {
          name__contains: this.cameraName,
        })
        .then((res) => {
          if (!res?.results) return;
          this.cameraList = res.results;
        })
        .finally(() => {
          this.cameraLoad = false;
        });
    }, 300),
    onCameraChange(camera) {
      this.nvrId = camera.nvr?.guid;
      this.cameraId = camera.guid;
      this.channelNo = camera.channel_no;

      this.getVideoFileList();
    },
    onNodeCheckChange(data, checked) {
      // console.log('nodecheckchange: ', data, checked);
      if (checked) {
        this.$refs.tree.setCheckedKeys([data.guid]);

        this.nvrId = data.nvr?.guid;
        this.cameraId = data.guid;
        this.channelNo = data.channel_no;

        this.getVideoFileList();
      } else {
        if (this.cameraId === data.guid) {
          this.nvrId = null;
          this.cameraId = null;
          this.channelNo = null;
          this.videoFileList = [];
        }
      }
    },
    // 选择回放视频类型
    onVideoTypeChange(checkedVal) {
      if (checkedVal.length === 0) {
        this.videoType = 0;
      } else if (checkedVal.length === 10) {
        this.videoType = -1;
      } else {
        this.videoType = checkedVal.reduce((acc, val) => ((acc += Math.pow(2, val)), acc), 0);
      }
    },
    // 切换视频播放速度
    onVideoSpeedChange(speed) {
      if (!(speed in this.videoMarks)) return;
      this.$api
        .getVideoApi('setplaybackspeed', {
          devidx: this.nvrId,
          stream: this.videoActive.stream,
          speed: speed,
        })
        .then((res) => {
          if (!res?.code < 0) return;
          this.$message.success(`视频播放速度设为${speed}倍速！`);
        });
    },
    // 播放器事件
    onPlayerEvent(action, data) {
      switch (action) {
        case 'close':
        {
          const video = this.videoList[data];
          if (video) {
            video.played = false;
            video.playing = false;
            video.download = false;
            this.videoList.splice(data, 1, { videoUrl: '' });
          }
        }
          break;
        case 'play':
          data.playing = true;
          break;
        case 'pause':
          data.playing = false;
          break;
        case 'seeking':
        {
          const { stream, seekTime } = data;
          // 设置录像开始时间点
          this.playVideoStream(stream, Math.round(seekTime));
        }
          break;
        default:
          break;
      }
    },
    afterModalClose() {
      this.videoRecordSel.forEach((_, index) => {
        const dplayer = this.$refs[`dplayer-${index}`];
        dplayer && dplayer[0].destroyVideo();
      });
    },
    // 取消录像下载
    cancelDowndload(video) {
      if (!this.nvrId || video.handle == null) return;
      this.$api
        .getVideoApi('/stophistoryrecord', {
          devidx: this.nvrId,
          handle: video.handle,
        })
        .then((res) => {
          if (!res?.code < 0) return;
          video.download = false;
          video.handle = null;
        });
    },
    // 下载录像文件
    downloadVideoFile(video) {
      if (!this.nvrId) return;
      this.$api
        .getVideoApi('/gethistoryrecord', {
          devidx: this.nvrId,
          channelno: this.channelNo,
          start: this.$dayjs(video.startTime).unix(),
          stop: this.$dayjs(video.stopTime).unix(),
        })
        .then(async (res) => {
          if (res?.code < 0) return;
          // 保存录像下载状态
          video.download = true;
          video.hanlde = res.handle;

          if (res.fileName) {
            // this.$api
            //   .getVideoApi('downloadMp4', {
            //     devidx: this.nvrId,
            //     channelno: this.channelNo,
            //     filename: res.fileName,
            //   })
            //   .then((res) => {
            //     if (!res?.code < 0) return;
            //     video.handle = null;
            //     video.download = false;
            //     this.$message.success('录像下载成功！');
            //   });
            // console.log(`${assetUrl}/record/${this.nvrId}/${this.channelNo}/${res.fileName}`);
            await saveAs(
              `${baseUrl}/video/api/v2/downloadMp4?devidx=${this.nvrId}&channelno=${this.channelNo}&filename=${res.fileName}`,
              res.fileName
            );
            video.handle = null;
            video.download = false;
            this.$message.success('视频录像下载开始！');
          }
        });
    },
    // 切换录像文件
    switchVideoFile(prev, next) {
      const preVideo = this.videoFileList[prev];
      preVideo.playing = false;
      preVideo.played = false;

      const nextVideo = this.videoFileList[next];
      if (!nextVideo.playing) {
        this.playVideoFile(nextVideo, true);
        this.videoFileIndex = next;
      }
    },
    // 播放视频录像
    async playVideoFile(video, fixed = false /* 替换当前位置 */) {
      if (!this.nvrId) return;
      this.videoLoad = true;
      this.$api
        .getVideoApi('startplayback', {
          devidx: this.nvrId,
          channelno: this.channelNo,
          start: video.timeStart,
          stop: video.timeStop,
        })
        .then((res) => {
          if (!res?.code < 0 || !res.stream) return;
          this.streamId = res.stream;
          // 保存视频播放状态
          video.played = true;
          video.playing = true;
          video.stream = res.stream;
          // video.title = res.channelName;
          video.title = `${video.startTime}-${video.stopTime}`;
          video.videoUrl = `${mediaUrl}/${this.nvrId}/${res.stream}.flv`;
          // video.videoUrl = res.flv_url;

          const curVideo = this.videoList[this.videoIndex];
          if (curVideo && curVideo.playing) {
            const index = this.videoList.findIndex((item) => !item.videoUrl);
            // 固定位置或无空位时直接替换
            if (fixed || index === -1) {
              // 关闭之前视频
              const player = this.$refs[`player-${this.videoIndex}`][0];
              player && player.closeVideo();
              curVideo.playing = false;
              curVideo.played = false;

              this.videoList.splice(this.videoIndex, 1, video);
            } else {
              this.videoList.splice(index, 1, video);
            }
          } else {
            // 当前位置无视频直接替换
            this.videoList.splice(this.videoIndex, 1, video);
          }
          // if (fixed || !curVideo?.videoUrl) {
          //   this.videoList.splice(this.videoIndex, 1, video);
          // } else {
          //   const index = this.videoList.findIndex((item) => !item.videoUrl);
          //   // 视频列表有空位时替换空位
          //   if (index >= 0) {
          //     this.videoList.splice(index, 1, video);
          //   } else {
          //     // 视频列表没有空位直接替换当前位置
          //     this.videoList.splice(this.videoIndex, 1, video);
          //   }
          // }
        })
        .finally(() => {
          this.videoLoad = false;
        });
    },
    playVideoStream(video, start = 0) {
      this.$api
        .getVideoApi('playback_play', {
          devidx: this.nvrId,
          stream: video.stream,
          range: `${start}-${video.duration}`,
        })
        .then(() => {
          video.playing = true;
          const player = this.$refs[`player-${this.videoIndex}`][0];
          player && player.playVideo();
        });
    },
    // 获取回放视频
    getVideoFileList() {
      if (!this.nvrId) return this.$message.warning('请先选择视频设备！');

      this.videoLoad = true;
      const curDate = this.$dayjs(this.videoDate).format('YYYY_MM_DD');
      const startTime = this.videoTime[0] ? this.$dayjs(this.videoTime[0]).format('HH_mm_ss') : '00_00_00';
      const stopTime = this.videoTime[1] ? this.$dayjs(this.videoTime[1]).format('HH_mm_ss') : '23_59_59';
      this.$api
        .getVideoApi('getrecordfile', {
          devidx: this.nvrId,
          channelno: this.channelNo,
          start: `${curDate}_${startTime}`,
          stop: `${curDate}_${stopTime}`,
        })
        .then((res) => {
          if (!res?.code < 0 || !res?.data?.fileInfo) return;
          this.videoFileList = res.data.fileInfo.map((item) => {
            const timeStart = this.$dayjs(item.startTime).format('YYYY_MM_DD_HH_mm_ss');
            const timeStop = this.$dayjs(item.stopTime).format('YYYY_MM_DD_HH_mm_ss');
            return {
              ...item,
              guid: uniqueId('video_'),
              name: `${item.startTime}-${item.stopTime}`,
              timeStart: timeStart,
              timeStop: timeStop,
              download: false,
              playing: false,
              played: false,
            };
          });
        })
        .finally(() => {
          this.videoLoad = false;
        });
    },
    // 获取录像视频
    getVideoRecordList() {
      if (!this.nvrId) return this.$message.warning('请先选择视频设备！');

      this.videoLoad = true;
      const curDate = this.$dayjs(this.videoDate).format('YYYY-MM-DD');
      this.$api
        .getVideoApi('getMp4RecordFile', {
          devidx: this.nvrId,
          channelno: this.channelNo,
          period: curDate,
        })
        .then((res) => {
          if (!res?.code < 0) return;
          const videoFiles = ['11-18-28-10.mp4', '12-19-17-3601.mp4', '13-19-18-813.mp4'];
          this.videoRecordList = videoFiles.map((item) => {
            const fileName = item.split('.')[0];
            const lastIndex = fileName.lastIndexOf('-');
            const duration = parseInt(fileName.substring(lastIndex + 1));

            const startTime = fileName.substring(0, lastIndex).replace(/-/g, ':');
            const startDateTime = `${curDate} ${startTime}`;
            const stopDateTime = this.$dayjs(`${curDate}T${startTime}`)
              .add(duration, 'second')
              .format('YYYY-MM-DD HH:mm:ss');
            // console.log(startDateTime, stopDateTime);
            return {
              name: item,
              title: `${startDateTime}_${stopDateTime}`,
              startTime: startDateTime,
              stopTime: stopDateTime,
              videoUrl: `${assetUrl}/record/${this.nvrId}/${this.channelNo}/${curDate}/${item}`,
            };
          });
        })
        .finally(() => {
          this.videoLoad = false;
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.video-tree {
  width: 100%;
  height: 100%;
  padding: 10px;

  &-search {
    margin-bottom: 1vh;
  }
  &-wrapper {
    width: 100%;
    height: 77vh;
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
      grid-template-rows: repeat(1, 1fr);
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

.video-replay {
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: space-between;
  width: 100%;
  height: 100%;

  &-control {
    flex: auto;
    display: flex;
    align-items: center;
    flex-direction: column;
    justify-content: center;
  }
  &-time {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 1vh;

    .el-button {
      margin-left: 20px;
    }
    .el-date-editor {
      width: 250px;
    }
  }
  &-action {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 2vh;

    .el-button {
      color: $text-placeholder;
      border-width: 2px;
      border-color: $border-base;
      background-color: transparent;

      &:hover:not(.is-disabled),
      &.active {
        color: $text-primary;
        border-color: $border-lighter;
      }
      &.center {
        padding: 0;
        border: none;
        font-size: 50px;
      }
    }
  }
  &-speed {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    padding: 0 20px;
    font-size: 14px;
    white-space: nowrap;

    .el-slider {
      width: 250px;
      margin-left: 10px;
    }
  }
  &-file {
    width: 100%;
    // margin-top: 2vh;

    &__title {
      width: 100%;
      height: 4vh;
      line-height: 4vh;
      text-align: center;
      color: $text-secondary;
      font-size: 20px;
      font-family: BigYoungBoldGB;
      border: $border-default;
      background: $bg-header;
    }
    &__wrap {
      width: 100%;
      height: 25vh;
      padding-top: 5px;
    }
    &__list {
      width: 100%;
      height: 100%;
      overflow-y: auto;
    }
    &__item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 100%;
      // height: 4vh;
      padding: 5px 10px;
      font-size: 14px;
      border-bottom: $border-default;

      &:hover,
      &.active {
        color: $text-primary;
        background-color: $bg-hover;
      }
    }
    &__action {
      display: inline-flex;
      flex-wrap: nowrap;
      margin-left: 10px;
    }
  }
  &-tabs {
    width: 100%;
    margin-top: 2vh;
    background: $bg-base;

    ::v-deep .el-tabs {
      &__item {
        &.is-active {
          background-color: $bg-hover;
        }
      }
      &__content {
        width: 100%;
        height: 27vh;
        padding: 0;
        overflow-y: auto;
      }
    }
  }
}
</style>
