<template>
  <div class="sy-identify" :class="{ editable: editable }" v-loading="loading">
    <div class="sy-identify-header">
      <div class="sy-identify-result">
        <span>{{ data.patrolpoint_name }}</span>
        <!-- <label :class="`sy-status-${data.patrol_status}`">{{ patrolStatus }}</label> -->
      </div>
      <div v-if="showEdit" class="sy-identify-toolbar">
        <span class="sy-identify-toolbar__action">
          <el-tooltip v-if="editing" content="退出" placement="top" :hide-after="1000">
            <i class="el-icon-close sy-text" @click="handleBtnAction('quit')"></i>
          </el-tooltip>
          <el-tooltip v-else content="编辑" placement="top" :hide-after="1000">
            <i
              class="el-icon-edit-outline sy-text"
              :class="{ disabled: !canEdit }"
              @click="handleBtnAction('edit')"
            ></i>
          </el-tooltip>
          <el-tooltip content="删除" placement="top" :hide-after="1000">
            <i class="el-icon-delete sy-text" :class="{ disabled: !canEdit }" @click="handleBtnAction('remove')"></i>
          </el-tooltip>
          <el-tooltip content="清空" placement="top" :hide-after="1000">
            <i class="el-icon-refresh sy-text" :class="{ disabled: !canEdit }" @click="handleBtnAction('clear')"></i>
          </el-tooltip>
          <el-tooltip content="保存" placement="top" :hide-after="1000">
            <i
              class="el-icon-folder-checked sy-text"
              :class="{ disabled: !canEdit }"
              @click="handleBtnAction('save')"
            ></i>
          </el-tooltip>
        </span>
      </div>
    </div>
    <div v-if="editable" class="sy-identify-wrapper" ref="container" title="按住CTRL拖动">
      <canvas v-if="showCanvas" :id="stageId" ref="stage"></canvas>
      <sy-empty v-else></sy-empty>
    </div>
    <div v-else class="sy-identify-image" ref="container">
      <sy-image :src="imgUrl" :image-size="imageSize"></sy-image>
      <canvas :id="stageId" ref="stage"></canvas>
    </div>
  </div>
</template>

<script>
import { fabric } from 'fabric';
import { round, uniqueId } from 'lodash';
import { RecognitionType } from '@/enums';
import { addEvent, removeEvent, arrToMap, getFirFullPath, getImgFullPath } from '@/utils';

import Config from '../config';
import Option from '@/enums/option';
import Firtemp from '../FirTemp/firtemp';

export default {
  name: 'Identify',
  computed: {
    stageId() {
      return uniqueId('stage-');
    },
    defectTypes() {
      return Option.DefectTypes;
    },
    patrolStateMap() {
      return arrToMap(Option.PatrolStates);
    },
    canEdit() {
      return this.editable;
    },
    showEdit({ data }) {
      return this.editable && data.recognition_type === RecognitionType.APPEARANCE;
    },
    showCanvas({ data }) {
      return (
        data.recognition_type === RecognitionType.METER ||
        data.recognition_type === RecognitionType.STATUS ||
        data.recognition_type === RecognitionType.FIRTEMP ||
        data.recognition_type === RecognitionType.APPEARANCE
      );
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
        return {
          ratio: 0,
          width: 0,
          height: 0,
        };
      }
    },
    patrolStatus() {
      return this.data.patrol_status !== undefined ? this.patrolStateMap[this.data.patrol_status] : '-';
    },
  },
  props: {
    // 识别结果
    data: {
      type: Object,
      default: () => ({
        file_type: 2,
        file_path: '',
        patrol_status: 0,
        patrolpoint_name: '佳兆业2113断路器B相外观',
        recognition_type: RecognitionType.APPEARANCE,
        rectangle: '',
      }),
    },
    // 是否可用
    visible: {
      type: Boolean,
      default: false,
    },
    // 是否可编辑
    editable: {
      type: Boolean,
      default: false,
    },
    // 显示红外测温
    showTemp: {
      type: Boolean,
      default: true,
    },
    // 占位图尺寸
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
    ['stage.selection'](bool) {
      if (this.stage && this.stage.size() > 0) {
        this.stage.forEachObject((item) => {
          item.selectable = bool;
        });
      }
    },
  },
  data() {
    return {
      loading: false,
      rendered: false, // 渲染完成
      draging: false, // 是否拖拽
      drawing: false, // 是否绘制
      editing: false, // 是否编辑
      rect: null, // 测温框
      rectX: 0, // 测温框原点X
      rectY: 0, // 测温框原点Y
      group: null, // 缺陷框
      groupX: 0, // 缺陷框原点X
      groupY: 0, // 缺陷框原点Y
      scale: 1, // 缺陷图片缩放比例
      scaleX: 1,
      scaleY: 1,
      stage: null, // 画板容器
      stageX: 0, // 画板容器原点X
      stageY: 0, // 画板容器原点Y
      textbox: null, // 缺陷描述
      firTemp: null, // FIR测温对象
      imgUrl: null,
      defectType: undefined,
    };
  },
  mounted() {
    this.initStage();
    this.onWindowResize();
    this.bindKeyboardEvent();
  },
  beforeDestroy() {
    if (this.stage) {
      this.stage.dispose();
      this.stage = null;
      this.draging = false;
      this.drawing = false;
      this.editing = false;
    }
  },
  methods: {
    handleBtnAction(action) {
      switch (action) {
        case 'edit':
          if (!this.canEdit) {
            return this.$message({
              type: 'warning',
              message: '请先选择缺陷类型！',
            });
          }
          this.editing = true;
          this.stage.selection = true;
          break;
        case 'quit':
          this.editing = false;
          this.stage.selection = false;
          this.stage.discardActiveObject();
          break;
        case 'save':
          {
            // 缺陷编辑状态点击保存退出编辑
            if (this.textbox && this.textbox.isEditing) {
              this.textbox.exitEditing();
            }
            const objects = this.stage.getObjects('group');
            let rectData = [],
              valueData = [];
            objects
              .filter((item) => item.width && item.height)
              .forEach((item) => {
                const top = round(item.top / this.scale, 2),
                  left = round(item.left / this.scale, 2),
                  width = round(item.width / this.scale, 2),
                  height = round(item.height / this.scale, 2);
                rectData.push(`${left},${top},${width},${height}`);
                valueData.push(item.defect_text || '暂无缺陷描述');
              });
            // console.log('objects: ', objects, rectData, valueData);
            this.$emit('save', {
              rectangle: rectData.join(','),
              value_disp: valueData.join(','),
            });
          }
          break;
        case 'clear':
          if (!this.editing) return;
          if (this.stage.size() > 0) {
            this.stage.forEachObject((item) => {
              this.stage.remove(item);
            });
            this.stage.renderAll();
          }
          break;
        case 'remove':
          if (!this.editing) return;
          if (this.group) {
            this.stage.remove(this.group);
            this.stage.renderAll();
            this.group = null;
          } else {
            const group = this.stage.getActiveObject();
            if (group) {
              this.stage.remove(group);
              this.stage.renderAll();
            }
          }
          break;
        default:
          break;
      }
    },
    onSelectChange(val) {
      if (this.group) {
        this.group.defect_type = val;
      }
    },
    onWindowResize() {
      const onResize = () =>
        requestAnimationFrame(() => {
          this.initStage();
        });
      // 监听窗口resize事件
      addEvent(window, 'resize', onResize);
      this.$once('hook:destroyed', () => removeEvent(window, 'resize', onResize));
    },
    async initStage() {
      if (this.stage) {
        this.stage.dispose();
        this.stage = null;
      }
      // console.log('initStage: ', this.data);
      let { file_path, rectangle, value_disp, recognition_type } = this.data;
      if (!file_path) {
        return this.$message({
          type: 'warning',
          message: '文件地址为空！',
        });
      }
      this.loading = true;
      if (this.editable) {
        if (recognition_type === RecognitionType.METER || recognition_type === RecognitionType.STATUS) {
          // 表计识别
          const filePath = getImgFullPath(file_path);
          const rectList = this.parseResult(rectangle, value_disp);
          await this.initCanvas(filePath, rectList);
        } else if (recognition_type === RecognitionType.FIRTEMP) {
          // 红外识别
          const filePath = getFirFullPath(file_path);
          await this.initFirTemp(filePath);
        } else if (recognition_type === RecognitionType.APPEARANCE) {
          // 外观识别
          const filePath = getImgFullPath(file_path);
          const rectList = this.parseResult(rectangle, value_disp);
          await this.initAppearance(filePath, rectList);
        }
      } else {
        // 图片识别
        const filePath = getImgFullPath(file_path);
        const rectList = this.parseResult(rectangle, value_disp);
        await this.initImage(filePath, rectList);
      }
      this.loading = false;
    },
    async initImage(url, rects) {
      return new Promise((resolve, reject) => {
        const image = new Image();
        image.crossOrigin = 'anonymous';
        image.onload = () => {
          const bgImage = new fabric.Image(image);
          const { width, height } = this.clientRect;
          this.stage = new fabric.Canvas(this.$refs.stage, {
            width: width,
            height: height,
            selection: false,
            hoverCursor: 'default',
            containerClass: 'container',
            centeredScaling: true,
            centeredRotation: true,
            renderOnAddRemove: false,
            enableRetinaScaling: true,
            preserveObjectStacking: true,
          });
          this.stage.setBackgroundImage(
            bgImage,
            () => {
              if (rects && rects.length > 0) {
                rects.forEach((item) => {
                  const rect = this.createGroup(item, true);
                  this.stage.add(rect);
                });
              }
              this.stage.requestRenderAll();

              setTimeout(() => {
                if (this.stage) {
                  this.imgUrl = this.stage.toDataURL({
                    top: 0,
                    left: 0,
                    width: image.width,
                    height: image.height,
                    format: 'jpeg',
                    quality: 0.8,
                  });
                  resolve(this.imgUrl);
                }
              }, 500);
              // console.log('this.imgUrl: ', this.imgUrl);
            },
            { crossOrigin: 'anonymous' }
          );
        };
        image.onerror = (err) => {
          this.imgUrl = null;
          this.loading = false;
          reject(err);
        };
        image.src = url;
      });
    },
    async initCanvas(url, rects) {
      return new Promise((resolve, reject) => {
        fabric.Image.fromURL(url, (image, error) => {
          if (error) {
            this.loading = false;
            this.$message({
              type: 'error',
              message: '图片初始化失败！',
            });
            reject(error);
          }
          const { width, height } = this.clientRect;
          this.stage = new fabric.Canvas(this.$refs.stage, {
            width: width,
            height: height,
            selection: false,
            hoverCursor: 'default',
            containerClass: 'container',
            centeredScaling: true,
            centeredRotation: true,
            renderOnAddRemove: false,
            enableRetinaScaling: true,
            preserveObjectStacking: true,
          });
          this.scale = round(fabric.util.findScaleToCover(image, this.clientRect), 2);
          this.stage.setBackgroundImage(
            image,
            () => {
              if (rects && rects.length > 0) {
                rects.forEach((item) => {
                  const rect = this.createGroup(item, true);
                  this.stage.add(rect);
                });
              }
              this.stage.requestRenderAll();
              resolve();
            },
            { scaleX: this.scale, scaleY: this.scale }
          );
          this.stage.on({
            'mouse:up': () => {
              this.draging = false;
            },
            'mouse:out': () => {
              this.draging = false;
            },
            'mouse:down': (opt) => {
              const { pointer } = opt;
              this.draging = true;
              this.stageX = pointer.x;
              this.stageY = pointer.y;
            },
            'mouse:move': (opt) => {
              if (this.draging) {
                const { pointer } = opt;
                // console.log('mouse:move:draging ', pointer);
                let viewpt = this.stage.viewportTransform;
                viewpt[4] += pointer.x - this.stageX;
                viewpt[5] += pointer.y - this.stageY;
                // this.stage.setViewportTransform(viewpt);
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
              this.rendered = true;
            },
          });
        });
      });
    },
    async initFirTemp(url) {
      if (!url.endsWith('.fir')) {
        this.loading = false;
        this.$message({
          type: 'error',
          message: '文件格式错误！',
        });
        return Promise.reject();
      }
      this.firTemp = await new Firtemp(url, this.showTemp);
      return new Promise((resolve, reject) => {
        const imgUrl = this.firTemp.image;
        fabric.Image.fromURL(imgUrl, (image, error) => {
          if (error) {
            this.loading = false;
            this.$message({
              type: 'error',
              message: '图片初始化失败！',
            });
            reject(error);
          }
          this.scale = round(fabric.util.findScaleToCover(image, this.clientRect), 2);
          this.stage = new fabric.Canvas(this.$refs.stage, {
            width: image.width * this.scale,
            height: image.height * this.scale,
            selection: false,
            hoverCursor: 'default',
            containerClass: 'container',
            centeredScaling: true,
            centeredRotation: true,
            renderOnAddRemove: false,
            enableRetinaScaling: true,
            preserveObjectStacking: true,
          });
          this.stage.setBackgroundImage(
            image,
            () => {
              this.stage.requestRenderAll();
              resolve();
            },
            { scaleX: this.scale, scaleY: this.scale }
          );
          this.stage.on({
            'mouse:up': () => {
              if (this.draging) {
                this.draging = false;
              } else if (this.drawing) {
                this.drawing = false;
                if (this.group) {
                  this.showFirTemp();
                }
              }
            },
            'mouse:out': () => {
              this.draging = false;
              if (this.drawing) {
                this.drawing = false;
                if (this.group) {
                  this.showFirTemp();
                }
              }
            },
            'mouse:down': (opt) => {
              const { e, pointer, absolutePointer } = opt;
              // 按下ctrl或alt时开始拖拽
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
                  strokeWidth: Config.TempStrokeWidth,
                });
                this.group = new fabric.Group([rect], {
                  top: absolutePointer.y,
                  left: absolutePointer.x,
                  width: 0,
                  height: 0,
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
                let viewpt = this.stage.viewportTransform;
                viewpt[4] += pointer.x - this.stageX;
                viewpt[5] += pointer.y - this.stageY;
                // this.stage.setViewportTransform(viewpt);
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
                this.group.item(0).set({ width: groupW, height: groupH });
                // 框选移动越界绘制结束
                if (
                  absolutePointer.x <= 0 ||
                  absolutePointer.x >= stageW ||
                  absolutePointer.y <= 0 ||
                  absolutePointer.y >= stageH
                ) {
                  this.drawing = false;
                  this.showFirTemp();
                } else {
                  // 延迟渲染优化渲染性能
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
            },
          });
        });
      });
    },
    async initAppearance(url, rects) {
      return new Promise((resolve, reject) => {
        fabric.Image.fromURL(url, (image, error) => {
          if (error) {
            this.loading = false;
            this.$message({
              type: 'error',
              message: '图片初始化失败！',
            });
            reject(error);
          }
          // const { width, height } = this.clientRect;
          // 获取图片展示缩放比例
          this.scale = round(fabric.util.findScaleToCover(image, this.clientRect), 2);
          this.stage = new fabric.Canvas(this.$refs.stage, {
            width: image.width * this.scale,
            height: image.height * this.scale,
            selection: false,
            hoverCursor: 'default',
            containerClass: 'container',
            centeredScaling: true,
            centeredRotation: true,
            renderOnAddRemove: false,
            enableRetinaScaling: true,
            preserveObjectStacking: true,
          });
          this.stage.setBackgroundImage(
            image,
            () => {
              if (rects && rects.length > 0) {
                rects.forEach((item) => {
                  const group = this.createGroup(item, true);
                  this.stage.add(group);
                });
              }
              // console.log('size: ', this.stage.size());
              this.stage.requestRenderAll();
              resolve();
            },
            { scaleX: this.scale, scaleY: this.scale }
          );
          this.stage.on({
            'mouse:up': () => {
              // console.log('mouse:up');
              if (this.draging) {
                this.draging = false;
                this.stage.selection = this.editing;
              } else if (this.drawing) {
                this.drawing = false;
                this.updateGroup();
              }
            },
            'mouse:out': () => {
              this.draging = false;
              if (this.drawing) {
                this.drawing = false;
                this.updateGroup();
              }
            },
            'mouse:down': (opt) => {
              const { e, target, pointer, absolutePointer } = opt;
              // console.log('mouse:down: ', opt);
              // 点击矩形框时禁用点击事件
              if (target) return;
              // 按下ctrl或alt时开始拖拽
              if (e.altKey || e.ctrlKey) {
                // console.log('mouse:down:draging ', pointer);
                this.draging = true;
                this.drawing = false;
                this.stageX = pointer.x;
                this.stageY = pointer.y;
                this.stage.selection = false;
                // 可编辑且激活编辑状态时绘制矩形
              } else if (this.canEdit && this.editing) {
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

                this.group = this.createGroup({
                  top: absolutePointer.y,
                  left: absolutePointer.x,
                  width: 0,
                  height: 0,
                  text: '',
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
                let viewpt = this.stage.viewportTransform;
                viewpt[4] += pointer.x - this.stageX;
                viewpt[5] += pointer.y - this.stageY;
                // this.stage.setViewportTransform(viewpt);
                this.stage.requestRenderAll();
                this.stageX = pointer.x;
                this.stageY = pointer.y;
              } else if (this.drawing) {
                const { target, absolutePointer } = opt;
                // console.log('mouse:move:drawing ', target, absolutePointer);
                const { top, left } = this.group;
                const stageW = this.stage.getWidth();
                const stageH = this.stage.getHeight();
                this.group.set({
                  top: absolutePointer.y > top ? top : absolutePointer.y,
                  left: absolutePointer.x > left ? left : absolutePointer.x,
                  width: Math.abs(absolutePointer.x - this.groupX),
                  height: Math.abs(absolutePointer.y - this.groupY),
                });
                // 框选越界或发生碰撞时绘制结束
                if (
                  target ||
                  absolutePointer.x <= 0 ||
                  absolutePointer.x >= stageW ||
                  absolutePointer.y <= 0 ||
                  absolutePointer.y >= stageH
                ) {
                  this.drawing = false;
                  this.updateGroup();
                } else {
                  // 延迟渲染优化渲染性能
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
            },
            // 'selection:created': (opt) => {
            //   console.log('selection:created: ', opt);
            // },
            // 'selection:updated': (opt) => {
            //   console.log('selection:updated: ', opt);
            // },
          });
        });
      });
    },
    // 依据中心缩放
    zoomCenter(zoom) {
      if (!this.stage) return;
      // this.stage.setZoom(zoom);
      const { top, left } = this.stage.getCenter();
      this.stage.zoomToPoint({ x: left, y: top }, zoom);
      this.stage.requestRenderAll();
    },
    createRect({ top, left, width, height }, scale = false) {
      // 同步缩放比例
      if (scale) {
        top *= this.scale;
        left *= this.scale;
        width *= this.scale;
        height *= this.scale;
      }
      const rect = new fabric.Rect({
        top: top,
        left: left,
        width: width,
        height: height,
        fill: 'transparent',
        stroke: Config.GroupStrokeColor,
        strokeWidth: Config.GroupStrokeWidth,
        selectable: false,
        hasBorders: false,
        hasControls: false,
      });
      return rect;
    },
    // 创建矩形框组
    createGroup({ top, left, width, height, text = '', type = this.defectType }, scale = false) {
      // 同步缩放比例
      if (scale) {
        top *= this.scale;
        left *= this.scale;
        width *= this.scale;
        height *= this.scale;
      }
      const rect = new fabric.Rect({
        width: width,
        height: height,
        originX: 'center',
        originY: 'center',
        fill: 'transparent',
        stroke: Config.GroupStrokeColor,
        strokeWidth: Config.GroupStrokeWidth,
      });
      const itext = new fabric.IText(text, {
        originX: 'center',
        originY: 'center',
        fill: Config.GroupTextColor,
        fontSize: Config.GroupFontSize * this.scale,
      });
      const group = new fabric.Group([rect, itext], {
        type: 'group',
        top: top,
        left: left,
        width: width,
        height: height,
        selectable: this.stage.selection, // 同步选中状态
        hasBorders: true,
        hasControls: true,
        hoverCursor: 'move',
        lockRotation: true,
        lockScalingFlip: true,
        hasRotatingPoint: false,
        defect_text: text, // 缺陷描述
        defect_type: type, // 缺陷类型
      });
      group.setControlVisible('mtr', false);
      group.on({
        moving: (opt) => {
          const { transform } = opt;
          const pointTL = new fabric.Point(0, 0);
          const pointBR = new fabric.Point(this.stage.width, this.stage.height);
          const isContained = transform.target.isContainedWithinRect(pointTL, pointBR, true, true);
          // console.log('group:moving: ', transform.target, isContained);

          if (!isContained) {
            this.limitBoudningRect(group);
          }
        },
        scaling: (opt) => {
          const { transform } = opt;
          const pointTL = new fabric.Point(0, 0);
          const pointBR = new fabric.Point(this.stage.width, this.stage.height);
          const isContained = transform.target.isContainedWithinRect(pointTL, pointBR, true, true);
          // console.log('group:scaling: ', transform, transform.target, isContained);

          if (!isContained) {
            this.limitBoudningRect(group, true);
          }
          // 保存之前缩放比例值
          const { scaleX, scaleY } = group;
          group.scaleXp = scaleX;
          group.scaleYp = scaleY;
        },
        // modified: (opt) => {
        //   console.log('modified: ', opt);
        // },
        selected: (opt) => {
          if (!this.editing) return;
          const { target } = opt;
          // console.log('group:selected: ', opt);
          if (target) {
            // 选中对象时保存
            this.group = target;
            // 拖拽碰撞时禁止合并
            this.stage.setActiveObject(target, () => {
              // console.log('group:selected: ', target);
            });
          }
        },
        mousedblclick: (opt) => {
          if (!this.editing) return;
          const { target } = opt;
          // console.log('group:mousedblclick: ', target);
          if (target instanceof fabric.Group) {
            const itext = target.item(1);
            this.textbox = new fabric.Textbox(itext.text, {
              top: target.top,
              left: target.left,
              width: target.width,
              height: target.height,
              fill: itext.fill,
              fontSize: itext.fontSize,
              editingBorderColor: itext.fill,
              editable: true,
              selectable: false,
              hasConstrols: false,
              // onInput: (opt) => console.log(opt),
              // onSelect: (opt) => console.log(opt),
              // onDeselect: (opt) => {
              //   const { object } = opt;
              //   console.log('onDeselect: ', object);
              //   if (textbox.isEditing) {
              //     textbox.exitEditing();
              //   }
              //   return true;
              // },
            });
            this.textbox.on('event:changed', (opt) => {
              console.log('event:changed: ', opt);
            });
            this.textbox.on('editing:entered', () => {
              this.textbox.selectAll();
            });
            this.textbox.on('editing:exited', () => {
              if (itext.text !== this.textbox.text) {
                target.defect_text = itext.text = this.textbox.text;
              }
              // 隐藏text更新视图
              itext.visible = true;
              target.removeWithUpdate();
              this.stage.remove(this.textbox);
              this.stage.setActiveObject(target);
              this.stage.requestRenderAll();
              this.textbox = null;
            });
            // 隐藏text更新视图
            itext.visible = false;
            target.addWithUpdate();
            this.textbox.enterEditing();
            this.stage.add(this.textbox);
            this.stage.setActiveObject(this.textbox);
            this.stage.requestRenderAll();
          }
        },
      });
      return group;
    },
    updateGroup() {
      this.group.item(0).set({
        width: this.group.width,
        height: this.group.height,
      });
      this.group.item(1).set('text', '请描述缺陷');
      // 立即渲染避免框选越界
      this.stage.renderAll();
    },
    parseResult(rectangle, value_disp) {
      if (rectangle) {
        let rectList = [];
        const rectData = rectangle.split(',');
        const valueData = value_disp.split(',');
        for (let i = 0, len = valueData.length; i < len; i++) {
          rectList.push({
            text: valueData[i] || '暂无缺陷描述',
            left: +rectData[i * 4],
            top: +rectData[i * 4 + 1],
            width: +rectData[i * 4 + 2],
            height: +rectData[i * 4 + 3],
          });
        }
        // const rectData = rectangle.split(/^\(|\),\(|\)$/);
        // rectData.forEach((rect) => {
        //   if (rect) {
        //     const [x1, y1, x2, y2, text] = rect.split(',');
        //     rectList.push({
        //       top: x1,
        //       left: y1,
        //       width: Math.abs(x2 - x1),
        //       height: Math.abs(y2 - y1),
        //       text,
        //     });
        //   }
        // });
        return rectList;
      } else {
        return null;
      }
    },
    // 框选测温显示
    showFirTemp() {
      const { top, left, width, height } = this.group;
      const result = this.firTemp.getFirTemp({
        x: round(left / this.scale),
        y: round(top / this.scale),
        w: round(width / this.scale),
        h: round(height / this.scale),
      });
      const { maxTemp, maxCoord, minTemp, minCoord } = result;
      this.$emit('select', result);
      if (this.showTemp) {
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
    },
    // 绑定键盘事件
    bindKeyboardEvent() {
      const onKeyup = (evt) => {
        // console.log('onKeyup: ', evt);
        if (!this.editing || !this.group) return;
        switch (evt.keyCode) {
          case 13: // Enter
            {
              const object = this.stage.getActiveObject();
              if (object && object instanceof fabric.Textbox) {
                object.exitEditing();
              }
            }
            break;
          case 46: // Delete
            {
              this.stage.remove(this.group);
              this.stage.renderAll();
              this.group = null;
            }
            break;
          default:
            break;
        }
      };
      window.addEventListener('keyup', onKeyup);
      this.$once('hook:destroyed', () => {
        window.removeEventListener('keyup', onKeyup);
      });
    },
    // 限制移动边界
    limitBoudningRect(target, scaling = false) {
      const rect = target.getBoundingRect(true, true);
      const width = this.stage.getWidth();
      const height = this.stage.getHeight();
      // console.log('BoundingRect: ', rect, width, height);
      // 限制缩放
      if (scaling) {
        if (rect.width > width) {
          target.lockScalingX = true;
          target.scaleX = target.scaleXp;
          setTimeout(() => {
            target.lockScalingX = false;
          }, 10);
        } else if (rect.height > height) {
          target.lockScalingY = true;
          target.scaleY = target.scaleYp;
          setTimeout(() => {
            target.lockScalingY = false;
          }, 10);
        }
      }
      // 限制移动
      if (rect.top < 0) {
        target.top = Math.max(rect.top, 0);
      } else if (rect.top + rect.height > height) {
        target.top = Math.min(rect.top, height - rect.height);
      }
      if (rect.left < 0) {
        target.left = Math.max(rect.left, 0);
      } else if (rect.left > width - rect.width) {
        target.left = Math.min(rect.left, width - rect.width);
      }
      target.setCoords();
    },
  },
};
</script>

<style lang="scss" scoped>
.sy-identify {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  width: 100%;
  height: 100%;
  padding-top: 0;
  overflow: hidden;

  &.editable {
    padding-top: 3vh;

    .sy-identify-header {
      display: flex;
    }
  }
  &-header {
    display: none;
    align-items: center;
    justify-content: flex-end;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 3vh;
  }
  &-result {
    flex: auto;
    min-width: 0;
    font-size: 16px;
    color: $text-primary;
    @include text-ellipsis(2);

    > label {
      margin-left: 10px;
      font-weight: 600;
    }
  }
  &-toolbar {
    flex: none;
    display: inline-flex;
    align-items: center;

    &__action {
      display: inline-flex;
      align-items: center;
      margin-left: 10px;

      > i {
        padding: 2px;
        font-size: 20px;
        cursor: pointer;

        + i {
          margin-left: 5px;
        }
        &:hover {
          color: $text-primary;
        }
        &.disabled {
          cursor: not-allowed;
        }
      }
    }
  }
  &-wrapper {
    position: relative;
    width: 100%;
    height: 100%;
    overflow: hidden;
    border: $border-default;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  &-image {
    position: relative;
    width: 100%;
    height: 100%;

    > .container {
      position: absolute !important;
      top: 0;
      left: 100%;
    }
    > .lower-canvas {
      display: none;
    }
  }
}
</style>
