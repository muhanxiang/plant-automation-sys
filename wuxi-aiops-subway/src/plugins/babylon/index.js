import Vue from 'vue';
import * as BABYLON from 'babylonjs';
import 'babylonjs-loaders';

const bus = new Vue();

export default {
  scene: {},
  engine: {},
  picked: {},
  highlightLayer: {},
  node_name: '',
  //初始化
  init(domID) {
    let canvas = document.getElementById(domID);

    BABYLON.SceneLoaderFlags.ShowLoadingScreen = false;

    let engine = new BABYLON.Engine(canvas, true, {
      stencil: true,
    });

    let scene = this.createScene(canvas, engine);

    engine.runRenderLoop(() => {
      scene.render();
    });

    this.engine = engine;

    this.scene = scene;

    this.highlightLayer = new BABYLON.HighlightLayer('hl1', scene);

    scene.onPointerDown = () => {
      var pickResult = scene.pick(scene.pointerX, scene.pointerY);
      if (Object.keys(this.picked).length > 0) {
        let node = this.picked;
        let meshs = node._children;
        for (let i = 0; i < meshs.length; i++) {
          //meshs[i].disableEdgesRendering();
          this.highlightLayer.removeMesh(meshs[i]);
        }
      }
      if (pickResult.hit) {
        let node = pickResult.pickedMesh.parent;
        this.picked = node;
        let name = node.name;
        bus.$emit('clickNode', name); //点击模型的事件传递，在需要调用的组件监听即可
        let meshs = node._children;
        for (let i = 0; i < meshs.length; i++) {
          this.highlightLayer.addMesh(meshs[i], BABYLON.Color3.Blue()); //高亮模型
          // meshs[i].enableEdgesRendering();
          // meshs[i].edgesWidth = 4.0;
          // meshs[i].edgesColor = new BABYLON.Color4(0, 0, 1, 1);
        }
      }
    };

    scene.executeWhenReady(() => {
      //模型加载完毕执行
      if (this.node_name.length > 0) {
        this.highlightModel(this.node_name);
      }
    });
  },
  //创建三维场景
  createScene(canvas, engine) {
    let scene = new BABYLON.Scene(engine);

    scene.clearColor = new BABYLON.Color4(0, 0, 0, 0.5);

    let light = new BABYLON.PointLight('Omni', new BABYLON.Vector3(20, 20, 100), scene);

    let camera = new BABYLON.ArcRotateCamera('Camera', 0, 0.8, 100, BABYLON.Vector3.Zero(), scene);
    camera.attachControl(canvas, false);

    scene.registerBeforeRender(function () {
      light.position = camera.position;
    });

    return scene;
  },
  //添加模型到场景
  addModel(path) {
    BABYLON.SceneLoader.Append('', path, this.scene, (scene) => {
      scene.createDefaultCameraOrLight(true, true, true);
      scene.activeCamera.alpha += Math.PI;
    });
  },
  //卸载模型
  removeModel() {
    for (let i = this.scene.meshes.length - 1; i >= 0; i--) {
      this.scene.meshes[i].dispose();
    }
  },
  //高亮模型
  highlightModel(name) {
    let node = this.scene.getNodeByName(name);
    if (node) {
      this.picked = node;
      let meshs = node._children;
      for (let i = 0; i < meshs.length; i++) {
        this.highlightLayer.addMesh(meshs[i], BABYLON.Color3.Blue()); //高亮模型
      }
    }
  },
};
