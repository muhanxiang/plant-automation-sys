<template>
  <div id="graph_monitor" class="graphic">
    <Thumbnail
      class="graghic-thumbnail"
      @video="viewCameraVideo"
      @model="viewDeviceModel"
      @commpoint="viewCommpointData"
      @mechanical="viewMechanicalData"
      @patrolpoint="viewPatrolpointData"
    ></Thumbnail>

    <CameraModal :visible.sync="cameraModal" :camera="cameraData"></CameraModal>
    <DeviceModal :visible.sync="deviceModal" :title="deviceTitle"></DeviceModal>
    <Mechanical :visible.sync="mechanicalModal" :title="mechanicalTitle" :guid="mechanicalKey"></Mechanical>
    <CommPoint
      :visible.sync="commpointModal"
      :title="commpointTitle"
      :guid="commpointKey"
      :precision="commpointPrec"
    ></CommPoint>
    <PatrolPoint
      :visible.sync="patrolpointModal"
      :title="patrolpointTitle"
      :guid="patrolpointKey"
      :precision="patrolpointPrec"
    ></PatrolPoint>
  </div>
</template>

<script>
import Thumbnail from '@/components/Canvas/Thumbnail';

export default {
  name: 'GraphMonitor',
  components: {
    Thumbnail,
    CommPoint: () => import('./components/CommPoint'),
    Mechanical: () => import('./components/Mechanical'),
    PatrolPoint: () => import('./components/PatrolPoint'),
    CameraModal: () => import('@/components/Modal/Camera'),
    DeviceModal: () => import('@/components/Modal/Device'),
  },
  data() {
    return {
      cameraModal: false,
      cameraData: {
        nvr: '',
        guid: '',
        name: '',
        channel_no: '',
      },
      deviceModal: false,
      deviceTitle: '设备名称',
      commpointKey: '',
      commpointPrec: 1,
      commpointModal: false,
      commpointTitle: '遥信点位名称',
      patrolpointKey: '',
      patrolpointPrec: 1,
      patrolpointModal: false,
      patrolpointTitle: '巡视点位名称',
      mechanicalKey: '',
      mechanicalModal: false,
      mechanicalTitle: '机械点位名称',
    };
  },
  methods: {
    // 摄像机视频
    viewCameraVideo(camera) {
      this.cameraId = camera?.action?.guid;
      this.$api.getBaseApi('camera', { guid: this.cameraId }).then((res) => {
        if (!res?.results) return;
        this.cameraData = res.results[0];
        this.cameraModal = true;
      });
    },
    // 设备模型
    viewDeviceModel() {
      // console.log('viewDeviceModel: ', device);
      this.deviceModal = true;
    },
    // 遥信点位
    viewCommpointData(guid) {
      this.$api.getBaseApi('commpoint', { guid }).then((res) => {
        if (!res?.results?.length) return;

        const commpoint = res.results[0];
        this.commpointTitle = commpoint.name;
        this.commpointPrec = commpoint.precision;
        this.commpointKey = guid;
        this.commpointModal = true;
      });
    },
    // 机械特性
    viewMechanicalData(guid) {
      this.$api.getBaseApi('commpoint', { guid }).then((res) => {
        if (!res?.results?.length) return;

        const commpoint = res.results[0];
        this.mechanicalTitle = commpoint?.name;
        this.mechanicalKey = guid;
        this.mechanicalModal = true;
      });
    },
    // 巡视点位
    viewPatrolpointData(guid) {
      this.$api.getBaseApi('patrolpoint', { guid }).then((res) => {
        if (!res?.results?.length) return;

        const patrolpoint = res.results[0];
        this.patrolpointTitle = patrolpoint.name;
        this.patrolpointPrec = patrolpoint.precision;
        this.patrolpointKey = guid;
        this.patrolpointModal = true;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.graphic {
  position: relative;
  width: 100%;
  height: 100%;

  .graphic-cascader {
    z-index: 100;
    position: absolute;
    top: 0;
    left: 0;
    width: 300px;
  }
}
</style>
