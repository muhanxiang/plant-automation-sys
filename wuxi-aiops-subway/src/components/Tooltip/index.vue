<template>
  <div class="tooltip_wrapper">
    <!-- 内容插槽 -->
    <slot @mouseleave="handleMouseLeave" @mouseover="handleMouseOver" @mousemove="handleMouseMove"></slot>

    <!-- 提示框插槽 -->
    <div class="tip" style="tipStyle" v-show="showTip" ref="tipRef">
      <slot name="tip"></slot>
    </div>
  </div>
</template>

<script>
const PositionType = {
  TOP: 'top',
  BOTTOM: 'bottom',
  LEFT: 'left',
  RIGHT: 'right',
};
export default {
  name: 'SyTooltip',
  props: {
    zIndex: {
      type: Number,
      default: 2,
    },
    position: {
      type: String, // top , bottom , left , right
      default: PositionType.BOTTOM,
    },
  },
  data() {
    return {
      tipStyle: {
        'z-index': this.zIndex,
      },
      showTip: false,
    };
  },
  methods: {
    handleMouseMove(e) {
      if (!this.showTip) return;
      const tipRef = this.$refs.tipRef;
      switch (this.position) {
        case PositionType.BOTTOM:
          this.tipStyle = {
            ...this.tipStyle,
            left: `${e.clientX}px`,
            top: `${e.clientY}px`,
          };
          break;
        case PositionType.TOP:
          this.tipStyle = {
            ...this.tipStyle,
            left: `${e.clientX - tipRef.offsetWidth}px`,
            top: `${e.clientY - tipRef.offsetHeight}px`,
          };
          break;
        case PositionType.LEFT:
          this.tipStyle = {
            ...this.tipStyle,
            left: `${e.clientX - tipRef.offsetWidth}px`,
            top: `${e.clientY}px`,
          };
          break;
        case PositionType.RIGHT:
          this.tipStyle = {
            ...this.tipStyle,
            left: `${e.clientX}px`,
            top: `${e.clientY - tipRef.offsetHeight}px`,
          };
          break;
      }
    },
    handleMouseLeave() {
      this.showTip = false;
    },
    handleMouseOver() {
      this.showTip = true;
    },
  },
  mounted() {
    document.body.appendChild(this.$el);
  },
  destroyed() {
    this.$el.parentNode.removeChild(this.$el);
  },
};
</script>
<style lang="scss" scoped>
.tooltip_wrapper {
  position: relative;
  .tip {
    border: $border-default;
    position: absolute;
  }
}
</style>