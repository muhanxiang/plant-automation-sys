<template>
  <div class="sy-firtemp" v-loading="loading">
    <div v-if="canvas" class="sy-firtemp-stage" ref="container">
      <canvas :id="stageId" ref="stage"></canvas>
    </div>
    <sy-image v-else-if="!loading && imgUrl" class="sy-firtemp-image" :src="imgUrl" :image-size="imageSize"></sy-image>
  </div>
</template>

<script>
import { fabric } from 'fabric';
import { round, uniqueId } from 'lodash';
import { addEvent, removeEvent, getFirFullPath } from '@/utils';
import Config from '../config';
import Firtemp from './firtemp';

export default {
  name: 'FirTemp',
  computed: {
    stageId() {
      return uniqueId('stage-');
    },
    firUrl({ data }) {
      return data.file_path && getFirFullPath(data.file_path);
    },
    imgUrl() {
      return this.firTemp && this.firTemp.image;
    },
    clientRect() {
      if (this.$refs.container) {
        const rect = this.$refs.container.getBoundingClientRect();
        return {
          ratio: round(rect.width / rect.height, 2),
          width: round(rect.width, 2),
          height: round(rect.height, 2),
        };
      } else {
        return null;
      }
    },
  },
  props: {
    data: {
      type: Object,
      default: () => ({}),
    },
    canvas: {
      type: Boolean,
      default: true,
    },
    showTemp: {
      type: Boolean,
      default: true,
    },
    imageSize: {
      type: Number,
      default: 80,
    },
  },
  watch: {
    data: {
      deep: true,
      handler() {
        this.initStage();
      },
    },
  },
  data() {
    return {
      loading: false,
      draging: false, // 是否拖拽
      drawing: false, // 是否绘制
      rendered: false, // 渲染完成
      firTemp: null,
      scale: 1,
      stage: null,
      stageX: 0,
      stageY: 0,
      group: null,
      groupX: 0,
      groupY: 0,
    };
  },
  mounted() {
    if (this.canvas) {
      this.initStage();
    } else {
      this.initFirtemp();
    }
    this.onWindowResize();
  },
  beforeDestroy() {
    if (this.stage) {
      this.stage.dispose();
      this.stage = null;
    }
  },
  methods: {
    onWindowResize() {
      const onResize = () =>
        requestAnimationFrame(() => {
          if (this.canvas) {
            this.initStage();
          } else {
            this.initFirtemp();
          }
        });
      // 监听窗口resize事件
      addEvent(window, 'resize', onResize);
      this.$once('hook:destroyed', () => removeEvent(window, 'resize', onResize));
    },
    async initStage() {
      if (!this.firUrl) {
        return this.$message({
          type: 'warning',
          message: '文件地址为空！',
        });
      }
      if (this.stage) {
        this.stage.dispose();
        this.stage = null;
      }
      this.loading = true;

      // 初始化红外测温对象
      this.firTemp = await new Firtemp(this.firUrl, this.showTemp);

      const image = new Image();
      image.onload = () => {
        const bgImage = new fabric.Image(image);
        this.scale = this.clientRect ? round(fabric.util.findScaleToCover(bgImage, this.clientRect), 2) : 1;
        this.stage = new fabric.Canvas(this.$refs.stage, {
          width: bgImage.width * this.scale,
          height: bgImage.height * this.scale,
          selection: false,
          hoverCursor: 'default',
          containerClass: 'container',
          renderOnAddRemove: false,
          enableRetinaScaling: true,
          preserveObjectStacking: true,
        });
        this.stage.setBackgroundImage(
          bgImage,
          () => {
            this.stage.requestRenderAll();
          },
          { scaleX: this.scale, scaleY: this.scale }
        );
        this.stage.on({
          'mouse:up': () => {
            // const { absolutePointer } = opt;
            // console.log('mouse:up: ', absolutePointer);
            if (this.draging) {
              this.draging = false;
            } else if (this.drawing) {
              this.drawing = false;
              this.showFirTemp();
            }
          },
          'mouse:out': () => {
            this.draging = false;
            if (this.drawing) {
              this.drawing = false;
              this.showFirTemp();
            }
          },
          'mouse:down': (opt) => {
            const { e, pointer, absolutePointer } = opt;
            if (e.altKey || e.ctrlKey) {
              // console.log('mouse:down:draging ', pointer);
              this.draging = true;
              this.drawing = false;
              this.stageX = pointer.x;
              this.stageY = pointer.y;
            } else {
              // console.log('mouse:down:drawing ', absolutePointer);
              const stageW = this.stage.getWidth();
              const stageH = this.stage.getHeight();
              // 背景区域外点击无效
              if (
                absolutePointer.x < 0 ||
                absolutePointer.x > stageW ||
                absolutePointer.y < 0 ||
                absolutePointer.y > stageH
              ) {
                return;
              }
              this.draging = false;
              this.drawing = true;
              if (this.group) {
                this.stage.remove(this.group);
                this.stage.requestRenderAll();
              }
              const rect = new fabric.Rect({
                originX: 'center',
                originY: 'center',
                fill: 'transparent',
                stroke: Config.TempStrokeColor,
                strokeWidth: 1,
              });
              this.group = new fabric.Group([rect], {
                top: absolutePointer.y,
                left: absolutePointer.x,
                width: 0,
                height: 0,
                strokeWidth: 1,
                selectable: false,
                hasBorders: false,
                hasControls: false,
              });
              this.stage.add(this.group);
              this.stage.requestRenderAll();
              this.groupX = absolutePointer.x;
              this.groupY = absolutePointer.y;
            }
          },
          'mouse:move': (opt) => {
            if (this.draging) {
              const { pointer } = opt;
              // console.log('mouse:move:draging ', pointer);
              let viewport = this.stage.viewportTransform;
              viewport[4] += pointer.x - this.stageX;
              viewport[5] += pointer.y - this.stageY;
              // this.stage.setViewportTransform(viewport);
              this.stage.requestRenderAll();
              this.stageX = pointer.x;
              this.stageY = pointer.y;
            } else if (this.drawing) {
              const { absolutePointer } = opt;
              // console.log('mouse:move:drawing ', absolutePointer);
              const { top, left } = this.group;
              const stageW = this.stage.getWidth();
              const stageH = this.stage.getHeight();
              const groupW = Math.abs(absolutePointer.x - this.groupX);
              const groupH = Math.abs(absolutePointer.y - this.groupY);
              this.group.set({
                top: absolutePointer.y > top ? top : absolutePointer.y,
                left: absolutePointer.x > left ? left : absolutePointer.x,
                width: groupW,
                height: groupH,
              });
              // this.group.setObjectsCoords();
              this.group.item(0).set({ width: groupW, height: groupH });
              // 框选越界时绘制结束
              if (
                absolutePointer.x <= 0 ||
                absolutePointer.x >= stageW ||
                absolutePointer.y <= 0 ||
                absolutePointer.y >= stageH
              ) {
                this.drawing = false;
                this.showFirTemp();
              } else {
                this.stage.requestRenderAll();
              }
            }
          },
          'mouse:wheel': (opt) => {
            const { e } = opt;
            // console.log('mouse:wheel: ', opt);
            let zoom = this.stage.getZoom();
            zoom += e.deltaY > 0 ? 0.02 : -0.02;
            if (zoom > 2) zoom = 2;
            if (zoom < 0.5) zoom = 0.5;
            this.zoomCenter(round(zoom, 2));
            e.preventDefault();
            e.stopPropagation();
          },
          'after:render': () => {
            this.rendered = true;
            this.loading = false;
          },
        });
      };
      image.onerror = () => {
        this.loading = false;
      };
      image.src = this.firTemp.image;
      // console.log('firTemp.image:', this.firTemp.image);
    },
    async initFirtemp() {
      this.loading = true;
      try {
        this.firTemp = await new Firtemp(this.firUrl, this.showTemp);
        this.loading = false;
      } catch (error) {
        this.loading = false;
      }
    },
    // 依据中心缩放
    zoomCenter(zoom) {
      if (!this.stage) return;
      const { top, left } = this.stage.getCenter();
      this.stage.zoomToPoint({ x: left, y: top }, zoom);
      this.stage.requestRenderAll();
    },
    // 框选测温显示
    showFirTemp() {
      try {
        const { top, left, width, height } = this.group;
        // this.group.item(0).set({ width, height });
        const result = this.firTemp.getFirTemp({
          x: round(left / this.scale),
          y: round(top / this.scale),
          w: round(width / this.scale),
          h: round(height / this.scale),
        });
        this.$emit('select', result);
        // console.log('result: ', result, top, left, width, height);
        if (this.showTemp) {
          const { maxTemp, maxCoord, minTemp, minCoord } = result;
          const maxText = new fabric.Text(`${maxTemp}℃`, {
            top: maxCoord.y * this.scale,
            left: maxCoord.x * this.scale,
            originX: 'center',
            originY: 'center',
            fill: Config.TempTextColor,
            fontSize: Config.TempFontSize * this.scale,
            fontWeight: 600,
          });
          const minText = new fabric.Text(`${minTemp}℃`, {
            top: minCoord.y * this.scale,
            left: minCoord.x * this.scale,
            originX: 'center',
            originY: 'center',
            fill: Config.TempTextColor,
            fontSize: Config.TempFontSize * this.scale,
            fontWeight: 600,
          });
          this.group.addWithUpdate(maxText);
          this.group.addWithUpdate(minText);
          // 立即渲染避免框选越界
          this.stage.renderAll();
        }
      } catch (error) {
        console.log('error:', error);
      }
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
.sy-firtemp {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;

  &-stage {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
  }
}
</style>
