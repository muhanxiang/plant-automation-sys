<template>
  <el-select
    class="sy-select"
    v-model="currCamera"
    size="small"
    value-key="camera_id"
    popper-class="sy-popper"
    placeholder="请选择摄像机"
    multiple
    collapse-tags
  >
    <el-option
      v-for="camera in cameras"
      :key="camera.camera_id"
      :label="camera.camera_name"
      :value="camera"
    ></el-option>
  </el-select>
</template>

<script>
import { TaskSource } from '@/enums';
// 选择摄像机设备
export default {
  name: 'CameraSelect',
  props: {
    multiple: {
      //多选
      type: Boolean,
      default: false,
    },
    patrolType: {
      type: String,
      default: '',
    },
    executeId: {
      //执行id
      type: String,
      default: '',
      required: true,
    },
    onlyCamera: {
      //仅仅选择摄像机
      type: Boolean,
      default: true,
    },
    value: {
      //已选中的值
      type: Array,
      default: () => [],
    },
  },
  watch: {
    value() {
      this.currCamera = this.value;
    },
    currCamera() {
      this.$emit('change', this.currCamera);
    },
    executeId: {
      handler() {
        this.executeId && this.load();
      },
      immediate: true,
    },
  },
  data() {
    return {
      currCamera: [],
      cameras: [],
    };
  },
  methods: {
    // 获取摄像机设备
    async load() {
      const { results } = await this.$api.getPatrolApi(`device/${this.executeId}`);
      this.cameras = results
        ? results
            .filter((item) => [TaskSource.DEFAULT, TaskSource.CAMERA].includes(item.patrol_device_type))
            .map((item, index) => ({ ...item, index, nvrId: item.nvr_id, cameraId: item.camera_id, checkable: true }))
        : [];

      this.$emit('loaded', this.cameras);
    },
  },
};
</script>
