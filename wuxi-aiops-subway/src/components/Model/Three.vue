<template>
  <div class="sy-three" v-loading="loading">
    <div :id="sceneId" class="sy-three-scene" ref="scene"></div>
    <sy-progress class="sy-three-progress" type="circle" :percentage="score" custom>
      <b>{{ score }}</b>
      <span>综合评分</span>
    </sy-progress>
  </div>
</template>

<script>
import * as THREE from 'three';
import { uniqueId } from 'lodash';
import { assetUrl } from '@/config';
// import 'three/examples/jsm/libs/fflate.module';
import { FBXLoader } from 'three/examples/jsm/loaders/FBXLoader';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';

export default {
  name: 'ThreeModel',
  props: {
    // 基础路径
    rootPath: {
      type: String,
      default: `${assetUrl}/data/model/`,
    },
  },
  data() {
    return {
      loading: false,
      score: 90,
      scene: null,
      camera: null,
      renderer: null,
      controls: null,
    };
  },
  computed: {
    sceneId() {
      return uniqueId('scene-');
    },
  },
  mounted() {
    this.initModel();
  },
  beforeDestroy() {
    this.renderer && this.renderer.dispose();
    this.controls && this.controls.dispose();
  },
  methods: {
    format(percent) {
      return `${percent} \n 综合评分`;
    },
    initModel() {
      this.loading = true;

      const container = this.$refs.scene;
      const { width, height } = container.getBoundingClientRect();
      // 初始化场景
      this.scene = new THREE.Scene();
      // 添加点光源
      const pointLight = new THREE.PointLight(0xffffff);
      pointLight.position.set(200, 200, 200);
      this.scene.add(pointLight);
      // 添加环境光
      const ambientLight = new THREE.AmbientLight(0xffffff);
      this.scene.add(ambientLight);
      // 初始化相机
      this.camera = new THREE.PerspectiveCamera(45, width / height, 1, 1000);
      this.camera.position.set(20, 10, 20);
      this.camera.lookAt(this.scene.position);
      // 初始化渲染器
      this.renderer = new THREE.WebGLRenderer({ alpha: true, antialias: true });
      this.renderer.setSize(width, height);
      this.renderer.setPixelRatio(window.devicePixelRatio);
      this.renderer.domElement.style.outline = 'none';
      container.appendChild(this.renderer.domElement);
      // 初始化控制器
      this.controls = new OrbitControls(this.camera, this.renderer.domElement);
      this.controls.enablePan = true; // 开启拖拽
      this.controls.enableZoom = true; // 开启缩放
      this.controls.enableDamping = true; // 开启惯性
      this.controls.dampingFactor = 0.25; // 阻尼系数
      this.controls.maxAzimuthAngle = Math.PI / 2;
      this.controls.minAzimuthAngle = -Math.PI / 2;
      this.controls.maxPolarAngle = Math.PI / 2;
      this.controls.minPolarAngle = -Math.PI / 2;
      // 加载模型文件
      // this.loadFBXModel();
      this.loadGLBModel();
      this.renderLoop();
    },
    renderLoop(loop) {
      if (!this.renderer) return;
      loop && loop();
      this.renderer.render(this.scene, this.camera);
      requestAnimationFrame(() => this.renderLoop(loop));
    },
    loadFBXModel() {
      const loader = new FBXLoader();
      loader.load(
        `${this.rootPath}transformer.fbx`,
        (model) => {
          console.log(model);
          this.scene.add(model);
          this.loading = false;
        },
        (xhr) => {
          console.log((xhr.loaded / xhr.total) * 100 + '% loaded');
        },
        (err) => {
          console.error(err);
          this.loading = false;
        }
      );
    },
    loadGLBModel() {
      const loader = new GLTFLoader();
      loader.load(
        `${this.rootPath}transformer.glb`,
        (model) => {
          console.log(model);
          // 修改模型材质
          // model.scene.traverse((obj) => {
          //   obj.material = {};
          // });
          // const group = new THREE.Group();
          // group.add(model.scene);
          // const box = new THREE.Box3();
          // box.setFromObject(group);
          // const object = new THREE.Object3D();
          // object.add(group);
          // object.position.set(0, 0, 0);
          // this.scene.add(object);
          // model.scene.position.set(-10, 0, 0);
          this.scene.add(model.scene);
          this.renderLoop(() => this.scene.rotateY(Math.PI / 180));

          this.loading = false;
        },
        (xhr) => {
          console.log((xhr.loaded / xhr.total) * 100 + '% loaded');
        },
        (err) => {
          console.error(err);
          this.loading = false;
        }
      );
    },
  },
};
</script>

<style lang="scss" scoped>
.sy-three {
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
          font-size: 40px;
          font-weight: 600;
          font-family: DS-DIGIB;
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
