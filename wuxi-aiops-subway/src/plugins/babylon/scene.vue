<template>
  <canvas id="renderCanvas" />
</template>

<script>
import babylon from './index';

export default {
  props: {
    path: {
      type: String,
      default: '',
    },
    code: {
      type: String,
      default: '',
    },
  },
  data() {
    return {};
  },
  mounted() {
    babylon.init('renderCanvas');
    babylon.node_name = this.code;
    if (this.path.length > 0) {
      babylon.addModel(this.path);
    }
  },
  watch: {
    path: {
      handler(value) {
        babylon.removeModel();
        babylon.addModel(value);
      },
    },
  },
  methods: {},
};
</script>

<style>
#renderCanvas {
  position: absolute;
  width: 100%;
  height: 100%;
}
</style>
