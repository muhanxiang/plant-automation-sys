<template>
  <div id="patrol_record">
    <el-menu class="patrol-menu" mode="horizontal" :default-active="activeMenu" router>
      <template v-for="route in menuList">
        <el-menu-item
          v-if="!route.meta.hidden"
          :key="route.path"
          :index="`/events/patrol/${route.path}`"
          :route="route"
        >
          {{ route.meta.title }}
        </el-menu-item>
      </template>
    </el-menu>
    <div class="patrol-view">
      <!-- <keep-alive></keep-alive> -->
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import { patrolRoutes } from '@/router';

export default {
  name: 'PointRecord',
  data() {
    return {};
  },
  computed: {
    menuList() {
      return patrolRoutes;
    },
    activeMenu() {
      return this.$route.fullPath;
    },
  },
  methods: {
    onNavMenuSelect(index) {
      this.activeMenu = index;
    },
  },
};
</script>

<style lang="scss" scoped>
#patrol_record {
  width: 100%;
  height: 100%;

  .patrol-menu {
    display: flex;
    align-items: flex-end;
    width: 100%;
    height: 3vh;
    border-bottom: none;
    background-color: transparent;

    .el-menu-item {
      z-index: 20;
      width: 150px;
      height: 30px;
      padding: 0;
      font-size: 16px;
      font-family: BigYoungBoldGB;
      line-height: 30px;
      text-align: center;
      border-bottom: none;
      background: transparent;

      &:first-child {
        width: 165px;
        margin-right: -15px;
        padding-right: 15px;
        border-left: $border-base;
        box-shadow: inset 10px 0 10px -5px $border-base;
        overflow: hidden;

        &::after {
          left: -15px;
        }
      }
      &::after {
        z-index: -1;
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        border: $border-default;
        box-shadow: inset 0 0 10px $border-base;
        box-sizing: border-box;
        transform: skew(45deg);
      }
      &.is-active {
        &::after {
          border-top: 3px solid $border-lighter;
          box-shadow: inset 0 0 10px $border-lighter;
        }
      }
    }
  }

  .patrol-view {
    width: 100%;
    height: calc(100% - 3vh);

    > div {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
