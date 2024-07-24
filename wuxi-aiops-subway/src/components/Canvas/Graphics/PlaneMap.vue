MessageAction
<template>
  <div class="sy-stage" ref="container" v-loading="loading">
    <canvas id="stage" ref="stage" width="100%" height="100%"></canvas>

    <div class="thumb-toolbar">
      <i class="el-icon-zoom-in" title="放大" @click="handleMapAction('zoomin')"></i>
      <i class="el-icon-zoom-out" title="缩小" @click="handleMapAction('zoomout')"></i>
      <i class="el-icon-c-scale-to-original" title="原大小" @click="handleMapAction('reset')"></i>
      <i class="el-icon-refresh" title="重置" @click="handleMapAction('refresh')"></i>
    </div>
  </div>
</template>

<script>
import { round } from 'lodash';
import { fabric } from 'fabric';
import { assetUrl } from '@/config';
import { MessageAction } from '@/enums';
import { addEvent, removeEvent } from '@/utils';

export default {
  name: 'PlaneMap',
  computed: {
    baseMatrix() {
      return [1, 0, 0, 1, 0, 0];
    },
    planeRect() {
      const rect = this.$refs.container.getBoundingClientRect();
      return {
        ratio: round(rect.width / rect.height, 2),
        width: rect.width,
        height: rect.height,
        originX: rect.width / 2,
        originY: rect.height / 2,
      };
    },
    retinaScale() {
      return this.stage ? this.stage.getRetinaScaling() : 1;
    },
  },
  props: {
    filePath: {
      type: String,
      default: '',
    },
  },
  watch: {
    filePath(file) {
      this.getGraphData(file);
    },
  },
  data() {
    return {
      loading: false,
      moving: false,
      source: null,
      stage: null,
      stageX: 0,
      stageY: 0,
      nodeMap: {},
      ycPoints: [],
      yxPoints: [],
    };
  },
  mounted() {
    this.initStage();
    this.onWindowResize();
    this.onStompStatusPush();
  },
  beforeDestroy() {
    if (this.stage) {
      this.stage.dispose();
      this.stage = null;
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
            const zoom = round(this.stage.getZoom() + 0.1, 2);
            this.zoomCenter(zoom > 2 ? 2 : zoom);
          }
          break;
        case 'zoomout':
          {
            const zoom = round(this.stage.getZoom() - 0.1, 2);
            this.zoomCenter(zoom < 0.5 ? 0.5 : zoom);
          }
          break;
        case 'refresh':
          this.stage.setViewportTransform(this.baseMatrix);
          this.zoomCenter(1);
          break;
        default:
          break;
      }
    },
    onObjectSelected(target) {
      if (!target.action) return;
      switch (target.action.type) {
        case 'request': // 发送请求
          break;
        case 'switch': // 切换图形
          this.loadGraphData(target.action.path);
          break;
        case 'detail': // 三维模型
          this.$emit('model', target);
          break;
        case 'video': // 视频监控
          this.$emit('video', target);
          break;
      }
      // 丢弃当前活动对象并触发事件
      this.stage.discardActiveObject().requestRenderAll();
    },
    // 监听窗口resize事件
    onWindowResize() {
      const onResize = () =>
        requestAnimationFrame(() => {
          this.initStage();
        });
      addEvent(window, 'resize', onResize);
      this.$once('hook:destroyed', () => removeEvent(window, 'resize', onResize));
    },
    onStompStatusPush() {
      this.$bus.$on('stompStatusPush', (body) => {
        const { action, data } = JSON.parse(body);
        // console.log('onStompStatusPush: ', action, data);

        if (!data || !data.length) return;
        switch (action) {
          case MessageAction.AC.YCVALUE:
            data.forEach((item) => {
              this.updateGraphItem(item.yc_id, item);
            });
            this.stage.requestRenderAll();
            break;
          case MessageAction.AC.YXVALUE:
            data.forEach((item) => {
              this.updateGraphItem(item.yx_id, item);
            });
            this.stage.requestRenderAll();
            break;
          default:
            break;
        }
      });
      this.$once('hook:beforeDestroy', () => {
        this.$bus.$off('stompStatusPush');
      });
    },
    initStage() {
      if (this.stage) {
        this.stage.dispose();
        this.stage = null;
      }
      const { width, height } = this.planeRect;
      this.stage = new fabric.Canvas(this.$refs.stage, {
        width: width,
        height: height,
        selection: false,
        containerClass: 'stage',
        renderOnAddRemove: false,
        enableRetinaScaling: true,
        includeDefaultValues: false,
        preserveObjectStacking: true,
      });
      this.stage.on({
        'mouse:up': () => {
          this.moving = false;
          this.selection = true;
        },
        'mouse:down': ({ e }) => {
          // console.log('mouse:down: ', e);
          this.moving = true;
          this.selection = false;
          this.stageX = e.clientX;
          this.stageY = e.clientY;
        },
        'mouse:move': ({ e }) => {
          if (this.moving) {
            // console.log('mouse:move: ', e.clientX, e.clientY);
            let viewport = this.stage.viewportTransform;
            viewport[4] += e.clientX - this.stageX;
            viewport[5] += e.clientY - this.stageY;
            this.stage.requestRenderAll();
            this.stageX = e.clientX;
            this.stageY = e.clientY;
          }
        },
        'mouse:wheel': ({ e }) => {
          // console.log('mouse:wheel: ', e);
          let zoom = this.stage.getZoom();
          zoom += e.deltaY > 0 ? 0.02 : -0.02;
          if (zoom > 2) zoom = 2;
          if (zoom < 0.5) zoom = 0.5;
          this.zoomCenter(round(zoom, 2));
          // this.toggleMiniMap(target);
          e.preventDefault();
          e.stopPropagation();
        },
        'after:render': () => {
          this.stage && this.stage.calcOffset();
        },
        'selection:cleared': () => {},
        'selection:created': ({ target }) => {
          this.onObjectSelected(target);
        },
        'selection:updated': ({ target }) => {
          this.onObjectSelected(target);
        },
      });
      // 加载图形数据
      if (this.filePath) {
        this.loadGraphData(this.filePath);
      }
    },
    // 加载json接口元素
    loadGraphData(filePath) {
      this.loading = true;
      this.$api
        .getJsonApi(filePath)
        .then((res) => {
          if (!res) return;
          const { objects, background, backgroundImage } = res;
          if (background && background.startsWith('#')) {
            this.stage.setBackgroundColor(background);
          }
          if (backgroundImage) {
            // 拼接背景图URL解决跨域问题
            backgroundImage.src = `${assetUrl}/${backgroundImage.src}`;
          }
          // 预处理图元JSON数据
          objects.forEach((el) => {
            const { action, associate } = el;
            if (action && action.type) {
              el.selectable = true;
              el.hoverCursor = 'pointer';
            }
            if (associate && associate.table && associate.key) {
              // 隐藏关联后端数据的图元
              if (el.opacity !== undefined) {
                el.originOpacity = el.opacity;
                el.opacity = 0;
              } else {
                el.originOpacity = 1;
                el.opacity = 0;
              }
              if (this.nodeMap[associate.table]) {
                const nodeItem = this.nodeMap[associate.table];
                if (!nodeItem.keys.includes(associate.key)) {
                  nodeItem.keys.push(associate.key);
                  nodeItem.columns.push(associate.column);
                }
              } else {
                this.nodeMap[associate.table] = {
                  load: false,
                  name: associate.table,
                  keys: [associate.key],
                  columns: [associate.column],
                };
              }
            }
          });
          // 清空画布所有元素
          if (this.stage) this.stage.clear();
          this.stage.loadFromJSON(res, () => {
            // 根据信号类型获取初始值
            Object.entries(this.nodeMap).forEach(async ([nodeKey, nodeItem]) => {
              await this.getMeasureData(nodeKey, nodeItem.keys);
            });
            this.stage.requestRenderAll();
          });
        })
        .catch((err) => {
          console.error(err);
        })
        .finally(() => {
          this.loading = false;
        });
    },
    // 更新图形元素
    updateGraphItem(key, item, animate = false) {
      if (!this.stage) return;
      const objects = this.stage.getObjects();
      objects.forEach((el) => {
        const { associate, source } = el;
        if (associate.key === key) {
          switch (el.type) {
            case 'image':
              if (source) {
                const index = source.lastIndexOf('.');
                const imageUrl = `${source.substring(0, index - 1)}${item[associate.column]}${source.substring(index)}`;
                if (source !== imageUrl) {
                  fabric.Image.fromURL(imageUrl, (image) => {
                    image.top = el.top;
                    image.left = el.left;
                    image.width = el.width;
                    image.height = el.height;
                    image.scaleX = el.scaleX;
                    image.scaleY = el.scaleY;
                    image.source = imageUrl;
                    image.angle = el.angle;
                    image.action = el.action;
                    image.associate = el.associate;
                    image.selectable = el.selectable;
                    image.originOpacity = el.originOpacity;
                    image.opacity = animate ? 0 : el.originOpacity;
                    image.hoverCursor = 'pointer';

                    this.stage.remove(el);
                    this.stage.add(image).requestRenderAll();
                    if (animate) {
                      image.animate('opacity', 1, {
                        onChange: () => {
                          this.stage.renderAll();
                        },
                      });
                    }
                  });
                }
              }
              break;
            case 'i-text':
              if (el.text !== item[associate.column]) {
                el.set({
                  text: item[associate.column].toString(),
                  opacity: animate ? 0 : el.originOpacity,
                }).setCoords();
                if (animate) {
                  el.animate('opacity', el.originOpacity, {
                    onChange: () => {
                      this.stage.renderAll();
                    },
                  });
                }
              }
              break;
          }
        }
      });
    },
    // 根据中心点缩放
    zoomCenter(zoom) {
      if (!this.stage) return;
      this.stage.zoomToPoint({ x: this.planeRect.originX, y: this.planeRect.originY }, zoom);
      this.stage.requestRenderAll();
    },
    getMeasureData(table, keys) {
      this.$api
        .getMeasureApi(table, {
          id__regex: `^(${keys.toString().replace(/,/g, '|')})$`,
        })
        .then((res) => {
          if (!res?.results) return;
          res.results.forEach((item) => {
            this.updateGraphItem(item.id, item);
          });
          this.stage.requestRenderAll();
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.sy-stage {
  position: relative;
  width: 100%;
  height: 100%;

  ::v-deep .stage {
    width: 100% !important;
    height: 100% !important;
    overflow: hidden;

    // > canvas {
    //   width: 100% !important;
    //   height: 100% !important;
    // }
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
