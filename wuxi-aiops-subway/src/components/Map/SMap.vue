<template>
  <div class="sy-smap" ref="container">
    <canvas :id="stageId" ref="stage"></canvas>
  </div>
</template>

<script>
import { fabric } from 'fabric';
import { round, uniqueId } from 'lodash';
import { BASE_URL } from '@/config';

export default {
  name: 'SMap',
  computed: {
    stageId() {
      return uniqueId('stage-');
    },
    mapUrl() {
      return `${BASE_URL}svg/subway.svg`;
    },
    clientRect() {
      const rect = this.$refs.container.getBoundingClientRect();
      return {
        ratio: round(rect.width / rect.height, 2),
        width: round(rect.width, 2),
        height: round(rect.height, 2),
      };
    },
  },
  data() {
    return {
      loading: false,
      draging: false,
      drawing: false,
      scale: 1,
      stage: null,
      stageX: 0,
      stageY: 0,
      group: null,
    };
  },
  mounted() {
    this.initStage();
  },
  methods: {
    initStage() {
      if (this.stage) {
        this.stage.dispose();
        this.stage = null;
      }
      this.loading = true;
      const { width, height } = this.clientRect;
      this.stage = new fabric.Canvas(this.$refs.stage, {
        width,
        height,
        selection: false,
        hoverCursor: 'default',
        containerClass: 'container',
        renderOnAddRemove: false,
        enableRetinaScaling: true,
        preserveObjectStacking: true,
      });
      this.stage.on({
        'mouse:up': () => {
          // const { absolutePointer } = opt;
          // console.log('mouse:up: ', absolutePointer);
          if (this.draging) {
            this.draging = false;
          } else if (this.drawing) {
            this.drawing = false;
          }
        },
        'mouse:out': () => {
          this.draging = false;
          if (this.drawing) {
            this.drawing = false;
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
          this.loading = false;
        },
      });
      // 加载SVG地图
      fabric.loadSVGFromURL(this.mapUrl, (objects, options) => {
        if (objects) {
          this.group = fabric.util.groupSVGElements(objects, options);
          const scale = round(fabric.util.findScaleToFit(this.group, this.clientRect), 2);
          this.group.set({
            top: height / 2,
            left: width / 2,
            scaleX: scale,
            scaleY: scale,
            originX: 'center',
            originY: 'center',
            selectable: true,
            hasBorders: false,
            hasControls: false,
          });

          this.stage.add(this.group);
          this.stage.requestRenderAll();
        }
      });
    },
    // 依据中心缩放
    zoomCenter(zoom) {
      if (!this.stage) return;
      const { top, left } = this.stage.getCenter();
      this.stage.zoomToPoint({ x: left, y: top }, zoom);
      this.stage.requestRenderAll();
    },
  },
};
</script>

<style lang="scss" scoped>
.sy-smap {
  width: 100%;
  height: 100%;
}
</style>
