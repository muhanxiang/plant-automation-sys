<template>
  <div class="sy-babylon" v-loading="loading">
    <canvas :id="sceneId" class="sy-babylon-scene" ref="scene" />
    <sy-progress class="sy-babylon-progress" type="circle" :percentage="score" custom>
      <b>{{ score }}</b>
      <span>综合评分</span>
    </sy-progress>
  </div>
</template>

<script>
import 'babylonjs-loaders';
import * as BABYLON from 'babylonjs';
import { uniqueId } from 'lodash';
import { assetUrl } from '@/config';
import { addEvent, removeEvent } from '@/utils';

BABYLON.SceneLoaderFlags.ShowLoadingScreen = false;
// class CustomLoadingScreen {
//   constructor(text = '正在加载...', bgColor = '#fff') {
//     this.loadingUIText = text;
//     this.loadingUIBackgroundColor = bgColor;
//   }
//   displayLoadingUI() {}
//   hideLoadingUI() {}
// }

export default {
  name: 'BabylonModel',
  props: {
    // 基础路径
    rootPath: {
      type: String,
      default: `${assetUrl}/data/model/`,
    },
    // 模型文件名
    modelName: {
      type: String,
      default: 'transformer.glb',
    },
    // 默认高亮节点
    nodeActive: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      score: 90,
      scene: null,
      engine: null,
      highlight: null,
      pickedMesh: null,
      loading: false,
    };
  },
  computed: {
    sceneId() {
      return uniqueId('scene-');
    },
  },
  mounted() {
    this.initEngine();
    this.onWindowReize();
  },
  beforeDestroy() {
    if (this.scene) {
      this.scene.dispose();
      this.scene = null;
    }
    if (this.engine) {
      this.engine.dispose();
      this.engine = null;
    }
  },
  methods: {
    onWindowReize() {
      const onResize = () =>
        requestAnimationFrame(() => {
          this.engine && this.engine.resize();
        });
      addEvent(window, 'resize', onResize);
      this.$once('hook:destroyed', () => removeEvent(window, 'resize', onResize));
    },
    initEngine() {
      this.loading = true;

      const canvas = this.$refs.scene;
      this.engine = new BABYLON.Engine(canvas, true, { stencil: true });
      this.engine.enableOfflineSupport = false;
      // 搭建场景
      this.scene = this.initScene(canvas, this.engine);
      this.engine.runRenderLoop(() => {
        this.scene.activeCamera.alpha += Math.PI / 360;
        this.scene.render();
      });
      // 高亮图层
      this.highlight = new BABYLON.HighlightLayer('HightLight', this.scene);
      // console.log('this.highlight: ', this.highlight);
      // 场景搭建后执行
      this.scene.executeWhenReady(() => {
        if (this.modelName) {
          this.loadModel(this.modelName)
            .then(() => {
              if (this.nodeActive) {
                this.highlightModel(this.nodeActive);
              }
            })
            .finally(() => {
              this.loading = false;
            });
        } else {
          this.loading = false;
        }
      });
      this.scene.onPointerPick = (evt, info) => {
        console.log('onPointerPick: ', info);
        if (info.hit) {
          // 移除高亮效果
          if (this.pickedMesh) {
            this.highlight.removeMesh(this.pickedMesh);
          }
          this.pickedMesh = info.pickedMesh;
          this.highlight.addMesh(this.pickedMesh, BABYLON.Color3.Blue());
          // 模型点击回调
          this.$emit('pickMesh', this.pickedMesh);
          console.log('this.pickedMesh: ', this.pickedMesh.name);
        }
      };
      this.scene.onPointerDown = (/** evt, info, type */) => {
        // console.log('onPointerDown: ', info, type);
        // 移除高亮效果
        // if (this.pickedMesh) {
        //   let meshes = this.pickedMesh._children;
        //   if (meshes && meshes.length > 0) {
        //     for (let i = 0; i < meshes.length; i++) {
        //       this.highlight.removeMesh(meshes[i]);
        //     }
        //   }
        // }
        // if (info.hit) {
        //   this.pickedMesh = info.pickedMesh;
        //   // 模型点击回调
        //   this.$emit('clickNode', this.pickedMesh.name);
        //   // 添加高亮效果
        //   let meshes = this.pickedMesh._children;
        //   if (meshes && meshes.length > 0) {
        //     for (let i = 0; i < meshes.length; i++) {
        //       this.highlight.addMesh(meshes[i], BABYLON.Color3.Blue());
        //     }
        //   }
        // }
      };
    },
    initScene(canvas, engine) {
      const scene = new BABYLON.Scene(engine);
      scene.clearColor = new BABYLON.Color4(0, 0, 0, 0.5);
      // 点光源
      const pointLight = new BABYLON.PointLight('PointLight', new BABYLON.Vector3(20, 20, 100), scene);
      pointLight.diffuse = new BABYLON.Color3.Gray();
      // 环境光
      const hemiLight = new BABYLON.HemisphericLight('HemiLight', new BABYLON.Vector3(1, 1, 1), scene);
      hemiLight.diffuse = new BABYLON.Color3.White();
      // 相机
      const camera = new BABYLON.ArcRotateCamera('Camera', Math.PI / 2, Math.PI / 2, 10, BABYLON.Vector3.Zero(), scene);
      camera.attachControl(canvas, false);

      scene.registerBeforeRender(function () {
        pointLight.position = camera.position;
      });
      return scene;
    },
    // 加载模型
    loadModel(modelName) {
      if (!this.scene || !this.rootPath) return;
      // BABYLON.SceneLoader.Append('', path, this.scene, (scene) => {
      //   scene.createDefaultCameraOrLight(true, true, true);
      //   scene.activeCamera.alpha += Math.PI;
      // });
      return new Promise((resolve, reject) => {
        BABYLON.SceneLoader.AppendAsync(this.rootPath, modelName, this.scene)
          .then((scene) => {
            // scene.createDefaultCameraOrLight(true, true, true);
            resolve(scene);
          })
          .catch((err) => {
            console.log(`Loading model ${modelName} fail: `, err);
            reject(err);
          });
      });
    },
    // 卸载模型
    unloadModel() {
      if (!this.scene) return;
      let meshes = this.scene.meshes;
      for (let i = meshes.length - 1; i >= 0; i--) {
        this.scene.meshes[i].dispose();
      }
    },
    // 高亮模型
    highlightModel(nodeName) {
      const node = this.scene.getNodeByName(nodeName);
      if (node) {
        // 移除高亮效果
        if (this.pickedMesh) {
          this.highlight.removeMesh(this.pickedMesh);
        }
        this.pickedMesh = node;
        this.highlight.addMesh(this.pickedMesh, BABYLON.Color3.Blue());
        this.$emit('highlight', this.pickedMesh);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.sy-babylon {
  position: relative;
  width: 100%;
  height: 100%;

  canvas {
    outline: none;
  }

  &-scene {
    z-index: 90;
    width: 100%;
    height: 100%;
  }
  &-progress {
    z-index: 100;
    position: absolute;
    top: 10px;
    right: 10px;

    ::v-deep .el-progress {
      &__text {
        display: flex;
        flex-direction: column;
        justify-content: center;
        color: $text-regular;
        font-size: 16px;

        > b {
          font-size: 48px;
          font-weight: 600;
          font-family: DS-DIGIB;
          // -webkit-text-stroke: 2px #031121;
          -webkit-text-fill-color: transparent;
          background: linear-gradient(180deg, #dbe5f0 0%, #8fc4ff 100%);
          background-clip: text;
        }
      }
      &-circle__track {
        stroke: transparent;
      }
    }
  }
}
</style>
