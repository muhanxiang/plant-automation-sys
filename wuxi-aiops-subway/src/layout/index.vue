<template>
  <el-container id="layout" direction="vertical">
    <!-- <LayoutHeader class="layout-header">
      <template #navi>
        <router-view name="navi"></router-view>
      </template>
    </LayoutHeader> -->
    <el-main class="layout-main">
      <transition
        enter-active-class="animate__animated animate__fadeIn animate__faster"
        leave-active-class="animate__animated animate__fadeOut animate__faster"
        appear-active-class="animate__animated animate__fadeIn animate__faster"
        appear
      >
        <router-view></router-view>
      </transition>
    </el-main>
  </el-container>
</template>

<script>
// import LayoutHeader from './Header';

export default {
  name: 'Layout',
  components: {
    // LayoutHeader,
  },
};
</script>

<style lang="scss">
#layout {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
  background: url('../assets/imgs/pointBJ.jpg') no-repeat center/100% 100%;

  .layout-header {
    width: 100%;
  }
  .layout-main {
    flex: auto;
    width: 100%;
    padding: 10px;
    overflow: hidden;
  }
}
</style>
