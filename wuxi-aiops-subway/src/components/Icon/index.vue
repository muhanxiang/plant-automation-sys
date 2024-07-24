<template>
  <i v-if="isExternal" class="svg-icon svg-icon-external" v-on="$listeners" :style="iconStyle" />
  <svg v-else :class="[iconClass, { gradient: gradient }]" aria-hidden="true" v-on="$listeners">
    <use :xlink:href="iconName" />
    <defs>
      <linearGradient id="linear_gradient" x1="0" y1="0" x2="0" y2="1">
        <stop offset="0" :stop-color="startColor" />
        <stop offset="1" :stop-color="stopColor" />
      </linearGradient>
    </defs>
  </svg>
</template>

<script>
// doc: https://panjiachen.github.io/vue-element-admin-site/feature/component/svg-icon.html#usage
import { isExternal } from '@/utils';

export default {
  name: 'Icon',
  props: {
    name: {
      type: String,
      required: true,
    },
    // 图标渐变
    gradient: {
      type: Boolean,
      default: false,
    },
    startColor: {
      type: String,
      default: '#DBE5F0',
    },
    stopColor: {
      type: String,
      default: '#8FC4FF',
    },
    className: {
      type: String,
      default: 'sy-icon',
    },
  },
  computed: {
    isExternal() {
      return isExternal(this.name);
    },
    iconName() {
      return `#icon-${this.name}`;
    },
    iconClass() {
      return this.className ? `svg-icon ${this.className}` : 'svg-icon';
    },
    iconStyle() {
      return {
        mask: `url(${this.name}) no-repeat 50% 50%`,
        '-webkit-mask': `url(${this.name}) no-repeat 50% 50%`,
      };
    },
  },
};
</script>

<style lang="scss" scoped>
.svg-icon {
  width: 1em;
  height: 1em;
  color: #00baff;
  fill: currentColor;
  overflow: hidden;
  cursor: pointer;

  &.gradient {
    fill: url(#linear_gradient);
  }
}

.svg-icon-external {
  display: inline-block;
  mask-size: cover !important;
  background-color: currentColor;
}
</style>
