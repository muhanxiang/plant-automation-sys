<template>
  <el-dialog
    custom-class="sy-dialog sy-dialog--medium nofooter"
    :title="title"
    :visible.sync="isVisible"
    :append-to-body="appendToBody"
    :destroy-on-close="destroyOnClose"
    @open="onDialogOpen"
    @close="onDialogClose"
  >
    <Firtemp canvas @select="onRegionSelect"></Firtemp>
  </el-dialog>
</template>

<script>
import Firtemp from '../Canvas/FirTemp';

export default {
  name: 'FirtempModal',
  components: {
    Firtemp,
  },
  computed: {
    isVisible: {
      get() {
        return this.visible;
      },
      set(bool) {
        this.$emit('update:visible', bool);
      },
    },
  },
  props: {
    title: {
      type: String,
      default: '红外测温结果',
    },
    visible: {
      type: Boolean,
      default: false,
    },
    appendToBody: {
      type: Boolean,
      default: true,
    },
    destroyOnClose: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {};
  },
  methods: {
    onDialogOpen() {},
    onDialogClose() {},
    onRegionSelect(res) {
      console.log('onRegionSelect: ', res);
    },
  },
};
</script>
