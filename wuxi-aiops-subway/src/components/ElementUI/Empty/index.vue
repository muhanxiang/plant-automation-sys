<template>
  <el-empty class="sy-empty" v-bind="$props">
    <sy-icon slot="image" :name="icon" :style="{ fontSize: `${imageSize}px` }" />
  </el-empty>
</template>

<script>
export default {
  name: 'SyEmpty',
  props: {
    type: {
      type: String,
      default: 'default',
    },
    icon: {
      type: String,
      default: 'nodata',
    },
    image: {
      type: String,
      default: '',
    },
    imageSize: {
      type: Number,
      default: 80,
    },
    description: {
      type: String,
      default: '暂无数据',
    },
  },
};
</script>

<style lang="scss" scoped>
.sy-empty {
  width: 100%;
  height: 100%;

  ::v-deep .el-empty {
    &__image {
      .sy-icon {
        color: $text-placeholder;
      }
    }

    &__description {
      margin-top: 1vh;

      > p {
        font-size: 16px;
        font-weight: 600;
        color: $text-placeholder;
      }
    }
  }
}
</style>
