<template>
  <div class="sy-image-wrapper">
    <el-image
      class="sy-image"
      v-bind="$props"
      v-on="$listeners"
      :title="title"
      :lazy="lazy"
      :preview-src-list="imgList"
      @load="onImageLoad"
      @error="onImageError"
    >
      <div v-loading="loading" slot="placeholder" class="el-image__loading"></div>
      <sy-empty slot="error" icon="picture" description="图片加载失败" :image-size="imageSize"></sy-empty>
    </el-image>
  </div>
</template>

<script>
export default {
  name: 'SyImage',
  computed: {
    imgList() {
      return this.preview ? (this.previewSrcList.length > 0 ? this.previewSrcList : [this.src]) : [];
    },
  },
  props: {
    src: {
      type: String,
      default: '',
    },
    alt: {
      type: String,
      default: '',
    },
    fit: {
      type: String,
      default: 'fill',
    },
    lazy: {
      type: Boolean,
      default: false,
    },
    preview: {
      type: Boolean,
      default: true,
    },
    zIndex: {
      type: Number,
      default: 2000,
    },
    imageSize: {
      type: Number,
      default: 80,
    },
    disablePreview: {
      type: Boolean,
      default: true,
    },
    previewSrcList: {
      type: Array,
      default: () => [],
    },
    referrerPolicy: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      loading: false,
      title: '',
    };
  },
  mounted() {
    this.loading = true;
  },
  methods: {
    onImageLoad() {
      this.$emit('load');
      this.loading = false;
      this.title = this.previewSrcList && this.previewSrcList.length > 0 ? '点击查看大图' : '';
    },
    onImageError() {
      this.$emit('error');
      this.loading = false;
    },
  },
};
</script>

<style lang="scss" scoped>
.sy-image-wrapper {
  width: 100%;
  height: 100%;
}
.sy-image {
  width: 100%;
  height: 100%;

  &-wrapper {
    position: relative;
    width: 100%;
    height: 100%;
  }
  ::v-deep .el-image {
    &__inner {
      display: block;
      cursor: pointer;
    }
    &__error {
      font-size: 20px;
      color: $text-placeholder;
      background-color: transparent;
    }
    &__loading {
      width: 100%;
      height: 100%;
      background-color: $bg-base5;
    }
  }
}
</style>
