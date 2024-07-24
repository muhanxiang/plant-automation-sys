<template>
  <div class="sy-thumbnail" ref="thumbnail" v-loading="loading">
    <canvas v-show="showMap" id="minimap" ref="minimap" width="100%" height="100%"></canvas>
    <canvas id="planemap" ref="planemap" width="100%" height="100%"></canvas>

    <div class="thumb-toolbar">
      <i class="el-icon-zoom-in" title="放大" @click="handleMapAction('zoomin')"></i>
      <i class="el-icon-zoom-out" title="缩小" @click="handleMapAction('zoomout')"></i>
      <i class="el-icon-c-scale-to-original" title="重置" @click="handleMapAction('reset')"></i>
      <i class="el-icon-refresh" title="刷新" @click="handleMapAction('refresh')"></i>
    </div>
  </div>
</template>

<script>
import { round, throttle } from 'lodash';
import { fabric } from 'fabric';
import { mapGetters } from 'vuex';
import { assetUrl } from '@/config';
import { MessageAction } from '@/enums';
import { addEvent, removeEvent } from '@/utils';

export default {
  name: 'Thumbnail',
  props: {
    file: {
      type: String,
      default: 'data/pic/index.json',
    },
  },
  data() {
    return {
      loading: false,
      showMap: false,
      source: null,
      minimap: null,
      planeId: 0,
      planemap: null,
      planeX: 0,
      planeY: 0,
      planeMove: false,
      viewport: null,
      viewptX: 0,
      viewptY: 0,
      viewptMove: false,
      commPoints: [],
      patrolPoints: [],
    };
  },
  watch: {
    file(val) {
      this.getGraphData(val);
    },
  },
  computed: {
    ...mapGetters(['stationId']),
    baseMatrix() {
      return [1, 0, 0, 1, 0, 0];
    },
    planeRect() {
      const rect = this.$refs.thumbnail.getBoundingClientRect();
      return {
        ratio: round(rect.width / rect.height, 2),
        width: rect.width,
        height: rect.height,
        originX: rect.width / 2,
        originY: rect.height / 2,
        planeId: this.planeId,
      };
    },
    thumbRect() {
      if (this.source) {
        return { width: 400, height: round((500 * this.source.height) / this.source.width, 2) };
      } else {
        return { width: 400, height: 300 };
      }
    },
    retinaScale() {
      return this.planemap ? this.planemap.getRetinaScaling() : 1;
    },
  },
  mounted() {
    this.initPlaneMap();
    this.onWindowResize();
    this.onStompStatusPush();
  },
  beforeDestroy() {
    if (this.minimap) {
      this.minimap.dispose();
      this.minimap = null;
      this.viewport = null;
    }
    if (this.planemap) {
      this.planemap.dispose();
      this.planemap = null;
    }
  },
  methods: {
    handleMapAction(action) {
      switch (action) {
        case 'reset':
          this.zoomCenter(1);
          break;
        case 'zoomin':
          {
            const zoom = round(this.planemap.getZoom() + 0.1, 2);
            this.zoomCenter(zoom > 2 ? 2 : zoom);
          }
          break;
        case 'zoomout':
          {
            const zoom = round(this.planemap.getZoom() - 0.1, 2);
            this.zoomCenter(zoom < 0.5 ? 0.5 : zoom);
          }
          break;
        case 'refresh':
          this.planemap.setViewportTransform(this.baseMatrix);
          this.zoomCenter(1);
          break;
        default:
          break;
      }
    },
    handleSelectTarget(target) {
      // console.log('handleSelectTarget: ', target);

      if (target.associate.table) {
        switch (target.associate.table) {
          case 'commpoint': // 传感器遥信/遥测
            this.$emit('commpoint', target.associate.key);
            break;
          case 'patrolpoint': // 巡视点位
            this.$emit('patrolpoint', target.associate.key);
            break;
          default:
            break;
        }
      } else if (target.action.type) {
        switch (target.action.type) {
          case 'request': // 发送请求
            break;
          case 'switch': // 切换图形
            this.loadGraphData(target.action.path);
            break;
          case 'video': // 视频监控
            this.$emit('video', target);
            break;
          case 'detail': // 三维模型
            this.$emit('model', target);
            break;
          case 'mechanical': // 机械特性
            this.$emit('mechanical', target.action.guid);
            break;
          default:
            break;
        }
      }

      // 丢弃当前活动对象并触发事件
      this.planemap.discardActiveObject().requestRenderAll();
    },
    onWindowResize() {
      const onResize = () =>
        throttle(() => {
          this.planeId++;
          this.initPlaneMap();
        }, 1000)();
      // 监听窗口resize事件
      addEvent(window, 'resize', onResize);
      this.$once('hook:destroyed', () => removeEvent(window, 'resize', onResize));
    },
    onStompStatusPush() {
      this.$bus.$on('stompStatusPush', (body) => {
        const { action, data } = JSON.parse(body);
        // console.log('onStompStatusPush: ', action, data);

        if (!data || !data.length) return;
        switch (action) {
          case MessageAction.AC.COMMPOINT:
            data.forEach((item) => {
              this.updateGraphItem(item.yc_id, item.value_disp);
            });
            break;
          case MessageAction.PATROL.TASK_ITEM:
            data.forEach((item) => {
              if (this.patrolPoints.includes[item.patrolpoint_id]) {
                this.updateGraphItem(item.patrolpoint_id, item.value_disp);
              }
            });
            break;
          default:
            break;
        }
      });
      this.$once('hook:beforeDestroy', () => {
        this.$bus.$off('stompStatusPush');
      });
    },
    loadGraphData(file) {
      this.loading = true;
      this.commPoints = [];
      this.patrolPoints = [];

      this.$api
        .getJsonApi(file)
        .then((res) => {
          if (!res) return;
          // console.log('loadGraphData: ', res);
          const { objects, background, backgroundImage } = res;

          if (backgroundImage) {
            // 拼接背景图URL解决跨域问题
            backgroundImage.source = backgroundImage.src = `${assetUrl}/${backgroundImage.src}`;
            // 保存背景图尺寸
            this.source = backgroundImage;
          }
          // 预处理图元JSON数据
          objects.forEach((obj) => {
            const { type, action, associate } = obj;
            if (type === 'image') {
              // console.log('obj: ', obj.src);
              obj.selectable = true;
              obj.hoverCursor = 'pointer';
              if (!obj.src.startsWith('http')) {
                obj.src = `${assetUrl}/${obj.src}`;
              }
            }
            if (action && action.type) {
              obj.selectable = true;
              obj.hoverCursor = 'pointer';
            }
            if (associate && associate.table) {
              if (obj.associate.table === 'commpoint') {
                obj.selectable = true;
                obj.hoverCursor = 'pointer';
                this.commPoints.push(obj.associate.key);
              } else if (obj.associate.table === 'patrolpoint') {
                obj.selectable = true;
                obj.hoverCursor = 'pointer';
                this.patrolPoints.push(obj.associate.key);
              }
              // 隐藏关联后端数据的图元
              // if (obj.opacity !== undefined) {
              //   obj.originOpacity = obj.opacity;
              //   obj.opacity = 0;
              // } else {
              //   obj.originOpacity = 1;
              //   obj.opacity = 0;
              // }
            }
          });
          // 清空画布所有元素
          if (this.planemap) this.planemap.clear();

          if (background) {
            this.planemap.setBackgroundColor(background);
          }
          this.planemap.loadFromJSON(
            res,
            async () => {
              // 初始化小地图
              // this.initMiniMap();

              // 获取遥测信号初始值
              await this.getMeasureData('commpoint', this.commPoints);
              // 遍历获取巡视点位最新值
              if (this.patrolPoints.length > 0) {
                this.patrolPoints.forEach((pid) => {
                  this.getPatrolPointData(pid);
                });
              }
              this.planemap.requestRenderAll();
            },
            (jsonObj, fabricObj) => {
              if (fabricObj) {
                fabricObj.on({
                  modified: ({ target }) => {
                    // console.log('modified: ', opt);
                    target.setCoords(true);
                  },
                });
              }
            }
          );
        })
        .catch((err) => {
          console.error(err);
        })
        .finally(() => {
          this.loading = false;
        });
    },
    // 根据中心点缩放
    zoomCenter(zoom) {
      if (!this.planemap) return;
      this.planemap.zoomToPoint({ x: this.planeRect.originX, y: this.planeRect.originY }, zoom);
      this.planemap.requestRenderAll();
      // update && this.updateViewport();
    },
    initPlaneMap() {
      if (this.planemap) {
        this.planemap.dispose();
        this.planemap = null;
      }
      this.loading = true;
      const { width, height } = this.planeRect;
      this.planemap = new fabric.Canvas(this.$refs.planemap, {
        width: width,
        height: height,
        selection: false,
        containerClass: 'planemap',
        renderOnAddRemove: false,
        enableRetinaScaling: true,
        includeDefaultValues: false,
        preserveObjectStacking: true,
      });
      this.planemap.on({
        'mouse:up': () => {
          this.planeMove = false;
          // 更新元素位置
          this.planemap.forEachObject((obj) => {
            obj.setCoords(true);
          });
        },
        'mouse:down': ({ e }) => {
          // console.log('mouse:down: ', e);
          this.planeMove = true;
          this.planeX = e.clientX;
          this.planeY = e.clientY;
        },
        'mouse:move': ({ e }) => {
          if (this.planeMove) {
            // console.log('mouse:move: ', e.clientX, e.clientY);
            let vpt = this.planemap.viewportTransform;
            vpt[4] += e.clientX - this.planeX;
            vpt[5] += e.clientY - this.planeY;
            this.planemap.requestRenderAll();
            // this.toggleMiniMap(target);
            this.planeX = e.clientX;
            this.planeY = e.clientY;
            // this.updateViewport();
          }
        },
        'mouse:wheel': ({ e }) => {
          // console.log('mouse:wheel: ', e);
          let zoom = this.planemap.getZoom();
          zoom += e.deltaY > 0 ? 0.02 : -0.02;
          if (zoom > 2) zoom = 2;
          if (zoom < 0.5) zoom = 0.5;
          this.zoomCenter(round(zoom, 2));

          // this.toggleMiniMap(target);
          e.preventDefault();
          e.stopPropagation();
        },
        'after:render': () => {
          this.planemap && this.planemap.calcOffset();
        },
        'selection:cleared': () => {},
        'selection:created': ({ target }) => {
          this.handleSelectTarget(target);
        },
        'selection:updated': ({ target }) => {
          this.handleSelectTarget(target);
        },
      });
      if (this.file) {
        this.loadGraphData(this.file);
      }
    },
    initMiniMap() {
      if (this.minimap) this.minimap.dispose();

      const { width, height } = this.thumbRect;
      this.minimap = new fabric.Canvas(this.$refs.minimap, {
        width: width,
        height: height,
        selection: false,
        containerClass: 'minimap',
        backgroundColor: '#02182c',
        renderOnAddRemove: false,
        enableRetinaScaling: true,
        preserveObjectStacking: true,
      });
      const canvas = this.createCanvasEl();
      const bgImage = new fabric.Image(canvas);
      // bgImage.scaleX = 1 / this.retinaScale;
      // bgImage.scaleY = 1 / this.retinaScale;
      this.minimap.backgroundImage = bgImage;

      this.viewport = this.initViewport(bgImage);
      this.minimap.add(this.viewport);
      this.minimap.requestRenderAll();
    },
    initViewport(image) {
      const viewport = new fabric.Rect({
        top: image.top,
        left: image.left,
        width: image.width - 2,
        height: image.height - 2,
        // width: image.width / this.retinaScale - 2,
        // height: image.height / this.retinaScale - 2,
        fill: 'rgba(238, 35, 245, 0.2)',
        shadow: new fabric.Shadow('#EE23F5 0 0 5px'),
        stroke: '#EE23F5',
        strokeWidth: 1,
        hasControls: false,
      });
      viewport.on({
        mouseup: () => {
          this.viewptMove = false;
        },
        mousedown: ({ e }) => {
          // console.log('mousedown: ', e);
          this.viewptMove = true;
          this.viewptX = e.clientX;
          this.viewptY = e.clientY;
        },
        mousemove: ({ e }) => {
          if (this.viewptMove) {
            // console.log('mousemove: ', opt, e.clientX, e.clientY);
            viewport.left += e.clientX - this.viewptX;
            viewport.top += e.clientY - this.viewptY;
            this.minimap.requestRenderAll();
            this.viewptX = e.clientX;
            this.viewptY = e.clientY;
            this.updatePlaneMap();
          }
        },
        mousewheel: ({ e }) => {
          let zoom = this.planemap.getZoom();
          zoom += e.deltaY > 0 ? 0.02 : -0.02;
          if (zoom > 2) zoom = 2;
          if (zoom < 0.5) zoom = 0.5;

          this.zoomCenter(round(zoom, 2));
          e.preventDefault();
          e.stopPropagation();
        },
      });
      return viewport;
    },
    // 生成小地图背景
    createCanvasEl() {
      if (!this.source) return;

      // const planeViewport = this.planemap.viewportTransform;
      // 获取资源图->大地图缩放比例
      // const planeRatio = round(fabric.util.findScaleToCover(this.source, this.planeRect), 2);
      // const planeWidth = this.source.width * planeRatio;
      // const planeHeight = this.source.height * planeRatio;
      // 获取大地图->小地图缩放比例
      const thumbRatio = round(fabric.util.findScaleToCover(this.source, this.thumbRect), 2);
      // 获取地图视觉缩放比例
      // const scaling = this.minimap.getRetinaScaling();
      // console.log('createCanvasEl: ', planeRatio, thumbRatio, this.retinaScale);
      // this.planemap.viewportTransform = [
      //   planeRatio,
      //   0,
      //   0,
      //   planeRatio,
      //   (this.planemap.width - planeWidth) / 2,
      //   (this.planemap.height - planeHeight) / 2,
      // ];
      const canvas = this.planemap.toCanvasElement(thumbRatio, {
        top: 0,
        left: 0,
        width: this.source.width,
        height: this.source.height,
      });
      // this.planemap.viewportTransform = planeViewport;
      return canvas;
    },
    // 动态切换小地图
    toggleMiniMap(target) {
      if (!target) return;

      // target.setCoords();
      // if (this.minimap) {
      //   this.minimap.dispose();
      //   this.minimap = null;
      //   this.viewport = null;
      // }
      const rect = target.getBoundingRect();
      const { width, height } = this.planeRect;
      if (rect.top < 0 || rect.left < 0 || rect.left + rect.width > width || rect.top + rect.height > height) {
        this.showMap = true;
        // this.initMiniMap();
      } else {
        this.showMap = false;
        if (this.minimap) {
          this.minimap.dispose();
          this.minimap = null;
          this.viewport = null;
        }
      }
    },
    // 动态更新小地图
    updateMiniMap() {
      const canvas = this.createCanvasEl();
      this.minimap.backgroundImage._element = canvas;
      this.minimap.requestRenderAll();
    },
    // 动态更新平面图
    updatePlaneMap() {
      if (!this.minimap || !this.planemap || !this.viewport) return;
      // console.log(this.minimap.top, this.minimap.backgroundImage.top);
      const planeZoom = this.planemap.getZoom();
      const miniRatio = fabric.util.findScaleToCover(this.source, this.minimap);
      let vpt = this.planemap.viewportTransform;
      vpt[5] = ((this.minimap.backgroundImage.top - this.viewport.top) * planeZoom) / miniRatio;
      vpt[4] = ((this.minimap.backgroundImage.left - this.viewport.left) * planeZoom) / miniRatio;
      this.planemap.requestRenderAll();
    },
    // 动态更新视窗口
    updateViewport() {
      if (!this.minimap || !this.planemap || !this.viewport) return;
      // const viewport = this.minimap.getObjects()[0];
      const planeZoom = this.planemap.getZoom();
      const planeRatio = fabric.util.findScaleToCover(this.source, this.planemap);
      const miniRatio = fabric.util.findScaleToCover(this.source, this.minimap);
      const finalRatio = planeRatio / planeZoom;
      this.viewport.scaleX = finalRatio;
      this.viewport.scaleY = finalRatio;
      this.viewport.top =
        this.minimap.backgroundImage.top - (this.planemap.viewportTransform[5] * miniRatio) / planeZoom;
      this.viewport.left =
        this.minimap.backgroundImage.left - (this.planemap.viewportTransform[4] * miniRatio) / planeZoom;
      this.minimap.requestRenderAll();
    },
    // 更新图形元素
    updateGraphItem(id, val, prec = 1) {
      if (!this.planemap) return;
      const value = val.replace(/^([-\d.]+)(.*)$/, ($0, $1, $2) => {
        // console.log('replace: ', $0, $1, $2);
        return $2 === 'ppm' ? $0 : `${(+$1).toFixed(prec)}${$2}`;
      });
      const objects = this.planemap.getObjects();
      // console.log('objects: ', objects);
      objects.forEach((obj) => {
        if (obj.type === 'i-text' && obj.associate.key === id) {
          switch (obj.associate.table) {
            case 'commpoint':
            case 'patrolpoint':
              if (obj.text !== value) {
                obj.set({ text: value, opacity: obj.originOpacity }).setCoords();
              }
              break;
            default:
              break;
          }
        }
      });
      this.planemap.requestRenderAll();
    },
    async getMeasureData(table, keys) {
      // const { results: commpoints } = await this.$api.getBaseApi(table, { guid__in: keys.toString() });
      this.$api.getMeasureApi(table, { ids: keys.toString() }).then((res) => {
        if (!res?.results) return;
        res.results.forEach((item) => {
          // let precision = 1;
          // if (commpoints.length > 0) {
          //   const commpoint = commpoints.find((point) => point.guid === item.id);
          //   commpoint && (precision = commpoint.precision);
          // }
          this.updateGraphItem(item.id, item.value_disp);
        });
        // this.planemap.requestRenderAll();
      });
    },
    getPatrolPointData(pointId) {
      this.$api
        .postHistoryApi('query', {
          index: 'history.item',
          from: 0,
          size: 1,
          match: {
            station_id: this.stationId,
            patrolpoint_id: pointId,
          },
          order: { patrol_time: 'desc' },
        })
        .then((res) => {
          if (!res?.data) return;
          const result = res.data[0];
          this.updateGraphItem(pointId, result?.value_disp);
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.sy-thumbnail {
  position: relative;
  width: 100%;
  height: 100%;

  ::v-deep .planemap {
    width: 100% !important;
    height: 100% !important;
    overflow: hidden;

    // > canvas {
    //   width: 100% !important;
    //   height: 100% !important;
    // }
  }

  ::v-deep .minimap {
    z-index: 100;
    position: absolute !important;
    top: 0;
    right: 0;
    overflow: hidden;
    border: $border-default;
    box-shadow: 0 0 5px $border-light;
  }

  .thumb-toolbar {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    position: absolute;
    right: 0;
    bottom: 0;
    padding: 10px;
    // transform: translateY(-50%);
    border-radius: 5px 0 0 5px;
    background: linear-gradient(-45deg, #012540 0%, #003053 100%);

    > i {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 40px;
      height: 40px;
      font-size: 20px;
      color: $text-secondary;
      border-radius: 4px;
      border: $border-default;
      cursor: pointer;

      + i {
        margin-top: 1vh;
      }
      &:hover {
        color: $text-primary;
        background-color: $bg-hover5;
      }
    }
  }
}
</style>
