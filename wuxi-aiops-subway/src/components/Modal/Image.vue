<template>
  <el-dialog
    custom-class="sy-dialog sy-dialog--small"
    :title="title"
    :visible.sync="isVisible"
    :append-to-body="appendToBody"
    :destroy-on-close="destroyOnClose"
    center
    @open="onDialogOpen"
    @close="onDialogClose"
  >
    <sy-image :src="imageUrl" :preview-src-list="[imageUrl]"></sy-image>
  </el-dialog>
</template>

<script>
export default {
  name: 'ImageModal',
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
      default: '巡检记录图片',
    },
    visible: {
      type: Boolean,
      default: false,
    },
    imageUrl: {
      type: String,
      default: '',
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
  },
};
</script>

<style lang="scss" scoped>
.sy-image {
  width: 100%;
  height: 100%;
}
</style>
