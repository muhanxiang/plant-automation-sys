<template>
  <canvas ref="canvas" :id="canvasId" :width="width" :height="height"></canvas>
</template>

<script>
import { fabric } from 'fabric';
import { uniqueId } from 'lodash';

const WIN_WIDTH = 256;

const DrawMode = {
  MODE_RECT: 3, // 轮廓
  MODE_LINE: 4, // 模拟表指针
  MODE_POINT: 5, // 模拟表刻度
  MODE_POLYGON: 6, // 数字表、干燥剂四边形
  MODE_MULTIPOLYGON: 7, // 红外测温四边形区域
  MODE_EDITLINE: 8, // 环形指针表刻度
  MODE_ELLIPSE: 9, // 环形表椭圆轮廓
  MODE_NONE: 10, // 图片浏览
  MODE_ERASE: 11, // 橡皮擦
  MODE_NEEDLECENTER: 12, // 模拟表指针旋转中心
};

const DrawStyle = {
  ELLIPSE_SIZE: 5, // 椭圆形半径
  ELLIPSE_STROKESIZE: 1, // 椭圆形轮廓宽度
  ELLIPSE_FILLCOLOR: '#feeeed', // 椭圆形填充色
  ELLIPSE_STROKECOLOR: '#FF0000', // 椭圆形轮廓色
  POINT_SIZE: 5, // 直线、矩形、多边形顶点宽度
  POINT_STROKESIZE: 1, // 直线、矩形、多边形顶点轮廓宽度
  POINT_FILLCOLOR: '#78cdd1', // 直线、矩形、多边形顶点填充色
  POINT_STROKECOLOR: '#0000FF', // 直线、矩形、多边形顶点轮廓色
  POINTERLINE_COLOR: '#00FF00', // 指针直线颜色
  POLYLINE_COLOR: '#0000FF', // 多边形直线颜色
  POLYLINE_WIDTH: 1, // 多边形直线宽度
  SCALELINE_COLOR: '#0000FF', // 刻度直线颜色
  SCALETEXT_COLOR: '#464547', // 刻度文字颜色 (70, 69, 71)
  SCALETEXT_BGCOLOR: '#f2eada', // 刻度文字背景颜色 (242, 234, 218)
  TEXT_FONTSIZE: 10, // 字体大小
};
// 自定义点
class Point {
  constructor(x, y) {
    this.x = x;
    this.y = y;
  }
}

export default {
  name: 'PlaneMap',
  props: {
    width: {
      type: [Number, String],
      default: '100%',
    },
    height: {
      type: [Number, String],
      default: '100%',
    },
  },
  data() {
    return {
      image: null,
      canvas: null,
      polygon: null,
      zoom: 1,
      moveX: 0,
      moveY: 0,
      clickX: 0,
      clickY: 0,
      baseX: 0,
      baseY: 0,
      baseZX: 1,
      baseZY: 1,
      erase: true,
      dragmove: false,
      drawMode: DrawMode.MODE_NONE,
    };
  },
  computed: {
    canvasId() {
      return uniqueId('canvas-');
    },
  },
  mounted() {
    this.initCanvas();
  },
  methods: {
    initCanvas() {
      this.canvas = new fabric.Canvas(this.$refs.canvas, {
        width: this.width,
        height: this.height,
        selection: false, // 是否启用组选择
        backgroundColor: '#02182c', // 背景颜色
        renderOnAddRemove: false, // 增删元素是否重新渲染
        enableRetinaScaling: true, // 是否按设备像素缩放
        includeDefaultValues: false, // 是否包含默认值
        preserveObjectStacking: true, // 是否保留对象
      });
      let vm = this;
      this.canvas.on({
        'mouse:up': function (e) {
          console.log('mouse:up: ', e);
          if (vm.dragmove) {
            if (vm.clickX > 0 && vm.clickY > 0 && vm.moveX > 0 && vm.moveY > 0) {
              switch (vm.drawMode) {
                case DrawMode.MODE_MULTIPOLYGON:
                  {
                    vm.removeAreaPolygonPaint();
                    const x1 = vm.clickX;
                    const y1 = vm.clickY;
                    const x2 = vm.moveX;
                    const y2 = vm.moveY;
                    if (x1 != null && y1 != null && x2 != null && y2 != null) {
                      const lefttop = new Point(Math.min(x1, x2), Math.min(y1, y2));
                      const righttop = new Point(Math.max(x1, x2), Math.min(y1, y2));
                      const leftbottom = new Point(Math.min(x1, x2), Math.max(y1, y2));
                      const rightbottom = new Point(Math.max(x1, x2), Math.max(y1, y2));
                      vm.setAreaPolygon(lefttop, righttop, leftbottom, rightbottom);
                      vm.repaintAreaLine();
                    }
                  }
                  break;
                default:
                  break;
              }
            }
            vm.dragmove = false;
          }
        },
        'mouse:down': function (e) {
          console.log('mouse:down: ', e);
          vm.dragmove = true;
          vm.moveX = -1;
          vm.moveY = -1;
          vm.clickX = e.absolutePointer.x;
          vm.clickY = e.absolutePointer.y;
        },
        'mouse:move': function (e) {
          if (vm.dragmove) {
            vm.moveX = e.absolutePointer.x;
            vm.moveY = e.absolutePointer.y;
            switch (vm.drawMode) {
              case DrawMode.MODE_MULTIPOLYGON:
                {
                  const lefttop = new Point(Math.min(vm.clickX, vm.moveX), Math.min(vm.clickY, vm.moveY));
                  const righttop = new Point(Math.max(vm.clickX, vm.moveX), Math.min(vm.clickY, vm.moveY));
                  const leftbottom = new Point(Math.min(vm.clickX, vm.moveX), Math.max(vm.clickY, vm.moveY));
                  const rightbottom = new Point(Math.max(vm.clickX, vm.moveX), Math.max(vm.clickY, vm.moveY));
                  vm.setAreaPolygonPaint(lefttop, righttop, leftbottom, rightbottom);
                }
                break;
              default:
                break;
            }
          }
        },
        'object:moving': function (e) {
          if (e.target) {
            if (
              e.target.objType === 'lefttoprect' ||
              e.target.objType === 'righttoprect' ||
              e.target.objType === 'leftbottomrect' ||
              e.target.objType === 'rightbottomrect'
            ) {
              const lefttop = new Point(vm.polygon.lefttop.left, vm.polygon.lefttop.top);
              const righttop = new Point(vm.polygon.righttop.left, vm.polygon.righttop.top);
              const leftbottom = new Point(vm.polygon.leftbottom.left, vm.polygon.leftbottom.top);
              const rightbottom = new Point(vm.polygon.rightbottom.left, vm.polygon.rightbottom.top);
              vm.setDisplayPolygon(lefttop, righttop, leftbottom, rightbottom);
            }
            if (
              e.target.objType === 'area_lt' ||
              e.target.objType === 'area_rb' ||
              e.target.objType === 'area_lb' ||
              e.target.objType === 'area_rt'
            ) {
              vm.repaintAreaLine();
            }
          }
        },
        'object:modified': function (e) {
          if (e.target) {
            if (
              e.target.objType === 'lefttoprect' ||
              e.target.objType === 'righttoprect' ||
              e.target.objType === 'leftbottomrect' ||
              e.target.objType === 'rightbottomrect'
            ) {
              const lefttop = new Point(vm.polygon.lefttop.left, vm.polygon.lefttop.top);
              const righttop = new Point(vm.polygon.righttop.left, vm.polygon.righttop.top);
              const leftbottom = new Point(vm.polygon.leftbottom.left, vm.polygon.leftbottom.top);
              const rightbottom = new Point(vm.polygon.rightbottom.left, vm.polygon.rightbottom.top);
              vm.setDisplayPolygon(lefttop, righttop, leftbottom, rightbottom);
            }

            if (
              e.target.objType === 'area_lt' ||
              e.target.objType === 'area_rb' ||
              e.target.objType === 'area_lb' ||
              e.target.objType === 'area_rt'
            ) {
              vm.repaintAreaLine();
            }
          }
        },
        'selection:created': function (e) {
          if (vm.drawMode === DrawMode.MODE_ERASE) {
            if (e.target.line1) {
              vm.canvas.remove(e.target.line1);
            }
            if (e.target.line2) {
              vm.canvas.remove(e.target.line2);
            }
            if (e.target.line3) {
              vm.canvas.remove(e.target.line3);
            }
            if (e.target.line4) {
              vm.canvas.remove(e.target.line4);
            }
            if (e.target.rt) {
              vm.canvas.remove(e.target.rt);
            }
            if (e.target.rb) {
              vm.canvas.remove(e.target.rb);
            }
            if (e.target.lb) {
              vm.canvas.remove(e.target.lb);
            }
            if (e.target.lt) {
              vm.canvas.remove(e.target.lt.rt);
              vm.canvas.remove(e.target.lt.rb);
              vm.canvas.remove(e.target.lt.lb);
              vm.canvas.remove(e.target.lt.line1);
              vm.canvas.remove(e.target.lt.line2);
              vm.canvas.remove(e.target.lt.line3);
              vm.canvas.remove(e.target.lt.line4);
              vm.canvas.remove(e.target.lt);
            }
            vm.canvas.remove(e.target);
            vm.canvas.fire('object:modified', { target: null });
            vm.canvas.renderAll();
          }
        },
      });
    },
    initConfig(image, x, y, zx, zy, area) {
      this.image = image;
      this.baseX = x;
      this.baseY = y;
      this.baseZX = zx;
      this.baseZY = zy;
      this.setBackgroundImage(image);
      this.initObjectPara(area);
    },
    initObjectPara(area) {
      if (area) {
        try {
          for (let i = 0; i < area.lefttop_x.length; i++) {
            area.lefttop_x[i] -= this.baseX;
            area.lefttop_y[i] -= this.baseY;
            area.righttop_x[i] -= this.baseX;
            area.righttop_y[i] -= this.baseY;
            area.leftbottom_x[i] -= this.baseX;
            area.leftbottom_y[i] -= this.baseY;
            area.rightbottom_x[i] -= this.baseX;
            area.rightbottom_y[i] -= this.baseY;

            const ltx = area.lefttop_x[i];
            const lty = area.lefttop_y[i];
            const rtx = area.righttop_x[i];
            const rty = area.righttop_y[i];
            const lbx = area.leftbottom_x[i];
            const lby = area.leftbottom_y[i];
            const rbx = area.rightbottom_x[i];
            const rby = area.rightbottom_y[i];

            const lefttop = new Point(ltx * this.baseZX, lty * this.baseZY);
            const righttop = new Point(rtx * this.baseZX, rty * this.baseZY);
            const leftbottom = new Point(lbx * this.baseZX, lby * this.baseZY);
            const rightbottom = new Point(rbx * this.baseZX, rby * this.baseZY);

            this.setAreaPolygon(lefttop, righttop, leftbottom, rightbottom);
          }
          this.repaintAreaLine();
        } catch (err) {
          console.log(err);
        }
      }
    },
    doClear() {
      this.setBackgroundImage(this.image);
    },
    doUpdate() {
      const ltx = [];
      const lty = [];
      const rtx = [];
      const rty = [];
      const lbx = [];
      const lby = [];
      const rbx = [];
      const rby = [];
      const objects = this.canvas.getObjects();

      let vm = this;
      objects.forEach((obj) => {
        if (obj.objType) {
          switch (obj.objType) {
            case 'area_lt':
              ltx.push(Math.round(vm.baseX + obj.left / vm.baseZX));
              lty.push(Math.round(vm.baseY + obj.top / vm.baseZY));
              break;
            case 'area_rt':
              rtx.push(Math.round(vm.baseX + obj.left / vm.baseZX));
              rty.push(Math.round(vm.baseY + obj.top / vm.baseZY));
              break;
            case 'area_lb':
              lbx.push(Math.round(vm.baseX + obj.left / vm.baseZX));
              lby.push(Math.round(vm.baseY + obj.top / vm.baseZY));
              break;
            case 'area_rb':
              rbx.push(Math.round(vm.baseX + obj.left / vm.baseZX));
              rby.push(Math.round(vm.baseY + obj.top / vm.baseZY));
              break;
            default:
              break;
          }
        }
      });

      const area = {
        lefttop_x: ltx,
        lefttop_y: lty,
        righttop_x: rtx,
        righttop_y: rty,
        leftbottom_x: lbx,
        leftbottom_y: lby,
        rightbottom_x: rbx,
        rightbottom_y: rby,
      };
      this.$emit('onUpdate', area);
    },
    repaintAreaLine() {
      const objects = this.canvas.getObjects();
      const vm = this;
      objects.forEach((obj) => {
        if (obj.objType && obj.objType === 'area_lt') {
          const lt = obj;
          const rt = obj.rt;
          const lb = obj.lb;
          const rb = obj.rb;
          const line1 = obj.line1;
          const line2 = obj.line2;
          const line3 = obj.line3;
          const line4 = obj.line4;
          const linedlt = 0 - DrawStyle.POLYLINE_WIDTH / vm.zoom / 2;

          line1
            .set({
              x1: lt.left,
              y1: lt.top,
              x2: rt.left,
              y2: rt.top,
              left: Math.min(lt.left, rt.left) + linedlt,
              top: Math.min(lt.top, rt.top) + linedlt,
            })
            .setCoords();
          line2
            .set({
              x1: lt.left,
              y1: lt.top,
              x2: lb.left,
              y2: lb.top,
              left: Math.min(lt.left, lb.left) + linedlt,
              top: Math.min(lt.top, lb.top) + linedlt,
            })
            .setCoords();
          line3
            .set({
              x1: rt.left,
              y1: rt.top,
              x2: rb.left,
              y2: rb.top,
              left: Math.min(rt.left, rb.left) + linedlt,
              top: Math.min(rt.top, rb.top) + linedlt,
            })
            .setCoords();
          line4
            .set({
              x1: lb.left,
              y1: lb.top,
              x2: rb.left,
              y2: rb.top,
              left: Math.min(lb.left, rb.left) + linedlt,
              top: Math.min(lb.top, rb.top) + linedlt,
            })
            .setCoords();
          line1.sendToBack();
          line2.sendToBack();
          line3.sendToBack();
          line4.sendToBack();
        }
      });
      this.canvas.renderAll();
    },
    setDrawMode(mode) {
      switch (mode) {
        case DrawMode.MODE_NONE:
        case DrawMode.MODE_ERASE:
          this.drawMode = mode;
          this.setObjectSelectable(true);
          break;
        case DrawMode.MODE_RECT:
          this.drawMode = DrawMode.MODE_RECT;
          this.setObjectSelectable(false);
          break;
        case DrawMode.MODE_LINE:
          this.drawMode = DrawMode.MODE_LINE;
          this.setObjectSelectable(false);
          break;
        case DrawMode.MODE_MULTIPOLYGON:
          this.drawMode = DrawMode.MODE_MULTIPOLYGON;
          this.setObjectSelectable(false);
          break;
        default:
          break;
      }
    },
    setBackgroundImage(image) {
      this.zoom = WIN_WIDTH / image.width;
      this.canvas.clear();
      this.canvas.setHeight(image.height * this.zoom);
      this.canvas.setBackgroundImage(image);
      this.canvas.zoomToPoint({ x: 0, y: 0 }, this.zoom);
      this.canvas.requestRenderAll();
    },
    setObjectSelectable(editable) {
      this.canvas.discardActiveObject();
      const objects = this.canvas.getObjects();
      objects.forEach((obj) => {
        if (obj.objType) {
          switch (obj.objType) {
            case 'boundingrect':
              obj.set({ selectable: false });
              break;
            case 'lefttoprect':
            case 'righttoprect':
            case 'leftbottomrect':
            case 'rightbottomrect':
            case 'area_lt':
            case 'area_lb':
            case 'area_rt':
            case 'area_rb':
              obj.set({ selectable: editable });
              break;
            default:
              break;
          }
        }
      });
    },
    setAreaPolygon(lefttop, righttop, leftbottom, rightbottom) {
      const lt = new fabric.Ellipse({
        objType: 'area_lt',
        left: lefttop.x,
        top: lefttop.y,
        rx: DrawStyle.POINT_SIZE / this.zoom,
        ry: DrawStyle.POINT_SIZE / this.zoom,
        originX: 'center',
        originY: 'center',
        selectable: true,
        opacity: DrawStyle.LAYER_OPACITY,
        strokeWidth: DrawStyle.POINT_STROKESIZE / this.zoom,
        stroke: DrawStyle.POINT_STROKECOLOR,
        fill: DrawStyle.POINT_FILLCOLOR,
        hasControls: false,
        hasRotatingPoint: false,
      });
      const rt = new fabric.Ellipse({
        objType: 'area_rt',
        left: righttop.x,
        top: righttop.y,
        rx: DrawStyle.POINT_SIZE / this.zoom,
        ry: DrawStyle.POINT_SIZE / this.zoom,
        originX: 'center',
        originY: 'center',
        selectable: true,
        opacity: DrawStyle.LAYER_OPACITY,
        lt: lt,
        strokeWidth: DrawStyle.POINT_STROKESIZE / this.zoom,
        stroke: DrawStyle.POINT_STROKECOLOR,
        fill: DrawStyle.POINT_FILLCOLOR,
        hasControls: false,
        hasRotatingPoint: false,
      });
      const lb = new fabric.Ellipse({
        objType: 'area_lb',
        left: leftbottom.x,
        top: leftbottom.y,
        rx: DrawStyle.POINT_SIZE / this.zoom,
        ry: DrawStyle.POINT_SIZE / this.zoom,
        originX: 'center',
        originY: 'center',
        selectable: true,
        opacity: DrawStyle.LAYER_OPACITY,
        lt: lt,
        strokeWidth: DrawStyle.POINT_STROKESIZE / this.zoom,
        stroke: DrawStyle.POINT_STROKECOLOR,
        fill: DrawStyle.POINT_FILLCOLOR,
        hasControls: false,
        hasRotatingPoint: false,
      });
      const rb = new fabric.Ellipse({
        objType: 'area_rb',
        left: rightbottom.x,
        top: rightbottom.y,
        rx: DrawStyle.POINT_SIZE / this.zoom,
        ry: DrawStyle.POINT_SIZE / this.zoom,
        originX: 'center',
        originY: 'center',
        selectable: true,
        opacity: DrawStyle.LAYER_OPACITY,
        lt: lt,
        strokeWidth: DrawStyle.POINT_STROKESIZE / this.zoom,
        stroke: DrawStyle.POINT_STROKECOLOR,
        fill: DrawStyle.POINT_FILLCOLOR,
        hasControls: false,
        hasRotatingPoint: false,
      });

      const linedlt = 0 - DrawStyle.POLYLINE_WIDTH / this.zoom / 2;
      const line1 = new fabric.Line([lt.left, lt.top, rt.left, rt.top], {
        left: Math.min(lt.left, rt.left) + linedlt,
        top: Math.min(lt.top, rt.top) + linedlt,
        selectable: false,
        strokeWidth: DrawStyle.POLYLINE_WIDTH / this.zoom,
        stroke: DrawStyle.POLYLINE_COLOR,
        opacity: DrawStyle.LAYER_OPACITY,
      });
      const line2 = new fabric.Line([lt.left, lt.top, lb.left, lb.top], {
        left: Math.min(lt.left, lb.left) + linedlt,
        top: Math.min(lt.top, lb.top) + linedlt,
        selectable: false,
        strokeWidth: DrawStyle.POLYLINE_WIDTH / this.zoom,
        stroke: DrawStyle.POLYLINE_COLOR,
        opacity: DrawStyle.LAYER_OPACITY,
      });
      const line3 = new fabric.Line([rt.left, rt.top, rb.left, rb.top], {
        left: Math.min(rt.left, rb.left) + linedlt,
        top: Math.min(rt.top, rb.top) + linedlt,
        selectable: false,
        strokeWidth: DrawStyle.POLYLINE_WIDTH / this.zoom,
        stroke: DrawStyle.POLYLINE_COLOR,
        opacity: DrawStyle.LAYER_OPACITY,
      });
      const line4 = new fabric.Line([lb.left, lb.top, rb.left, rb.top], {
        left: Math.min(lb.left, rb.left) + linedlt,
        top: Math.min(lb.top, rb.top) + linedlt,
        selectable: false,
        strokeWidth: DrawStyle.POLYLINE_WIDTH / this.zoom,
        stroke: DrawStyle.POLYLINE_COLOR,
        opacity: DrawStyle.LAYER_OPACITY,
      });

      lt.rt = rt;
      lt.lb = lb;
      lt.rb = rb;
      lt.line1 = line1;
      lt.line2 = line2;
      lt.line3 = line3;
      lt.line4 = line4;
      this.canvas.add(lt);
      this.canvas.add(rt);
      this.canvas.add(lb);
      this.canvas.add(rb);
      this.canvas.add(line1);
      this.canvas.add(line2);
      this.canvas.add(line3);
      this.canvas.add(line4);
    },
    setAreaPolygonPaint(lefttop, righttop, leftbottom, rightbottom) {
      if (lefttop != null && righttop != null && leftbottom != null && rightbottom != null) {
        if (this.polygon === null) {
          this.polygon = {
            lefttop: null,
            righttop: null,
            leftbottom: null,
            rightbottom: null,
            line1: null,
            line2: null,
            line3: null,
            line4: null,
          };

          this.polygon.lefttop = new fabric.Ellipse({
            objType: 'lefttoprect',
            left: lefttop.x,
            top: lefttop.y,
            rx: DrawStyle.POINT_SIZE / this.zoom,
            ry: DrawStyle.POINT_SIZE / this.zoom,
            originX: 'center',
            originY: 'center',
            selectable: true,
            opacity: DrawStyle.LAYER_OPACITY,
            strokeWidth: DrawStyle.POINT_STROKESIZE / this.zoom,
            stroke: DrawStyle.POINT_STROKECOLOR,
            fill: DrawStyle.POINT_FILLCOLOR,
          });
          this.polygon.righttop = new fabric.Ellipse({
            objType: 'righttoprect',
            left: righttop.x,
            top: righttop.y,
            rx: DrawStyle.POINT_SIZE / this.zoom,
            ry: DrawStyle.POINT_SIZE / this.zoom,
            originX: 'center',
            originY: 'center',
            selectable: true,
            opacity: DrawStyle.LAYER_OPACITY,
            strokeWidth: DrawStyle.POINT_STROKESIZE / this.zoom,
            stroke: DrawStyle.POINT_STROKECOLOR,
            fill: DrawStyle.POINT_FILLCOLOR,
          });
          this.polygon.leftbottom = new fabric.Ellipse({
            objType: 'leftbottomrect',
            left: leftbottom.x,
            top: leftbottom.y,
            rx: DrawStyle.POINT_SIZE / this.zoom,
            ry: DrawStyle.POINT_SIZE / this.zoom,
            originX: 'center',
            originY: 'center',
            selectable: true,
            opacity: DrawStyle.LAYER_OPACITY,
            strokeWidth: DrawStyle.POINT_STROKESIZE / this.zoom,
            stroke: DrawStyle.POINT_STROKECOLOR,
            fill: DrawStyle.POINT_FILLCOLOR,
          });
          this.polygon.rightbottom = new fabric.Ellipse({
            objType: 'rightbottomrect',
            left: rightbottom.x,
            top: rightbottom.y,
            rx: DrawStyle.POINT_SIZE / this.zoom,
            ry: DrawStyle.POINT_SIZE / this.zoom,
            originX: 'center',
            originY: 'center',
            selectable: true,
            opacity: DrawStyle.LAYER_OPACITY,
            strokeWidth: DrawStyle.POINT_STROKESIZE / this.zoom,
            stroke: DrawStyle.POINT_STROKECOLOR,
            fill: DrawStyle.POINT_FILLCOLOR,
          });
          const linedlt = 0 - DrawStyle.POLYLINE_WIDTH / this.zoom / 2;

          this.polygon.line1 = new fabric.Line(
            [
              this.polygon.lefttop.left,
              this.polygon.lefttop.top,
              this.polygon.righttop.left,
              this.polygon.righttop.top,
            ],
            {
              left: Math.min(this.polygon.lefttop.left, this.polygon.righttop.left) + linedlt,
              top: Math.min(this.polygon.lefttop.top, this.polygon.righttop.top) + linedlt,
              selectable: false,
              strokeWidth: DrawStyle.POLYLINE_WIDTH / this.zoom,
              stroke: DrawStyle.POLYLINE_COLOR,
              opacity: DrawStyle.LAYER_OPACITY,
            }
          );
          this.polygon.line2 = new fabric.Line(
            [
              this.polygon.lefttop.left,
              this.polygon.lefttop.top,
              this.polygon.leftbottom.left,
              this.polygon.leftbottom.top,
            ],
            {
              left: Math.min(this.polygon.lefttop.left, this.polygon.leftbottom.left) + linedlt,
              top: Math.min(this.polygon.lefttop.top, this.polygon.leftbottom.top) + linedlt,
              selectable: false,
              strokeWidth: DrawStyle.POLYLINE_WIDTH / this.zoom,
              stroke: DrawStyle.POLYLINE_COLOR,
              opacity: DrawStyle.LAYER_OPACITY,
            }
          );
          this.polygon.line3 = new fabric.Line(
            [
              this.polygon.righttop.left,
              this.polygon.righttop.top,
              this.polygon.rightbottom.left,
              this.polygon.rightbottom.top,
            ],
            {
              left: Math.min(this.polygon.righttop.left, this.polygon.rightbottom.left) + linedlt,
              top: Math.min(this.polygon.righttop.top, this.polygon.rightbottom.top) + linedlt,
              selectable: false,
              strokeWidth: DrawStyle.POLYLINE_WIDTH / this.zoom,
              stroke: DrawStyle.POLYLINE_COLOR,
              opacity: DrawStyle.LAYER_OPACITY,
            }
          );
          this.polygon.line4 = new fabric.Line(
            [
              this.polygon.leftbottom.left,
              this.polygon.leftbottom.top,
              this.polygon.rightbottom.left,
              this.polygon.rightbottom.top,
            ],
            {
              left: Math.min(this.polygon.leftbottom.left, this.polygon.rightbottom.left) + linedlt,
              top: Math.min(this.polygon.leftbottom.top, this.polygon.rightbottom.top) + linedlt,
              selectable: false,
              strokeWidth: DrawStyle.POLYLINE_WIDTH / this.zoom,
              stroke: DrawStyle.POLYLINE_COLOR,
              opacity: DrawStyle.LAYER_OPACITY,
            }
          );

          this.canvas.add(this.polygon.line1);
          this.canvas.add(this.polygon.line2);
          this.canvas.add(this.polygon.line3);
          this.canvas.add(this.polygon.line4);
          this.canvas.add(this.polygon.lefttop);
          this.canvas.add(this.polygon.righttop);
          this.canvas.add(this.polygon.leftbottom);
          this.canvas.add(this.polygon.rightbottom);
        } else {
          this.polygon.lefttop
            .set({
              left: lefttop.x,
              top: lefttop.y,
              width: DrawStyle.POINT_SIZE / this.zoom,
              height: DrawStyle.POINT_SIZE / this.zoom,
              strokeWidth: DrawStyle.POLYLINE_WIDTH / this.zoom,
            })
            .setCoords();

          this.polygon.righttop
            .set({
              left: righttop.x,
              top: righttop.y,
              width: DrawStyle.POINT_SIZE / this.zoom,
              height: DrawStyle.POINT_SIZE / this.zoom,
              strokeWidth: DrawStyle.POLYLINE_WIDTH / this.zoom,
            })
            .setCoords();

          this.polygon.leftbottom
            .set({
              left: leftbottom.x,
              top: leftbottom.y,
              width: DrawStyle.POINT_SIZE / this.zoom,
              height: DrawStyle.POINT_SIZE / this.zoom,
              strokeWidth: DrawStyle.POLYLINE_WIDTH / this.zoom,
            })
            .setCoords();

          this.polygon.rightbottom
            .set({
              left: rightbottom.x,
              top: rightbottom.y,
              width: DrawStyle.POINT_SIZE / this.zoom,
              height: DrawStyle.POINT_SIZE / this.zoom,
              strokeWidth: DrawStyle.POLYLINE_WIDTH / this.zoom,
            })
            .setCoords();

          const linedlt = 0 - DrawStyle.POLYLINE_WIDTH / this.zoom / 2;

          this.polygon.line1
            .set({
              x1: this.polygon.lefttop.left,
              y1: this.polygon.lefttop.top,
              x2: this.polygon.righttop.left,
              y2: this.polygon.righttop.top,
              left: Math.min(this.polygon.lefttop.left, this.polygon.righttop.left) + linedlt,
              top: Math.min(this.polygon.lefttop.top, this.polygon.righttop.top) + linedlt,
              strokeWidth: DrawStyle.POLYLINE_WIDTH / this.zoom,
            })
            .setCoords();

          this.polygon.line2
            .set({
              x1: this.polygon.lefttop.left,
              y1: this.polygon.lefttop.top,
              x2: this.polygon.leftbottom.left,
              y2: this.polygon.leftbottom.top,
              left: Math.min(this.polygon.lefttop.left, this.polygon.leftbottom.left) + linedlt,
              top: Math.min(this.polygon.lefttop.top, this.polygon.leftbottom.top) + linedlt,
              strokeWidth: DrawStyle.POLYLINE_WIDTH / this.zoom,
            })
            .setCoords();

          this.polygon.line3
            .set({
              x1: this.polygon.righttop.left,
              y1: this.polygon.righttop.top,
              x2: this.polygon.rightbottom.left,
              y2: this.polygon.rightbottom.top,
              left: Math.min(this.polygon.righttop.left, this.polygon.rightbottom.left) + linedlt,
              top: Math.min(this.polygon.righttop.top, this.polygon.rightbottom.top) + linedlt,
              strokeWidth: DrawStyle.POLYLINE_WIDTH / this.zoom,
            })
            .setCoords();

          this.polygon.line4
            .set({
              x1: this.polygon.leftbottom.left,
              y1: this.polygon.leftbottom.top,
              x2: this.polygon.rightbottom.left,
              y2: this.polygon.rightbottom.top,
              left: Math.min(this.polygon.leftbottom.left, this.polygon.rightbottom.left) + linedlt,
              top: Math.min(this.polygon.leftbottom.top, this.polygon.rightbottom.top) + linedlt,
              strokeWidth: DrawStyle.POLYLINE_WIDTH / this.zoom,
            })
            .setCoords();

          this.canvas.renderAll();
        }
      }
    },
    removeAreaPolygonPaint() {
      this.canvas.remove(this.polygon.line1);
      this.canvas.remove(this.polygon.line2);
      this.canvas.remove(this.polygon.line3);
      this.canvas.remove(this.polygon.line4);
      this.canvas.remove(this.polygon.lefttop);
      this.canvas.remove(this.polygon.righttop);
      this.canvas.remove(this.polygon.leftbottom);
      this.canvas.remove(this.polygon.rightbottom);
      this.polygon = null;
      this.canvas.renderAll();
    },
  },
};
</script>

<style lang="scss" scoped>
.plane {
  position: relative;
  width: 100%;
  height: 100%;
}
</style>
