<template>
  <div id="header" height="auto">
    <header class="header-main">
      <div class="header-main-time">{{ dateTime }}</div>

      <div class="header-main-left">
        <div class="header-main-logo"/>

        <div class="header-main-menu header-main-menu--left">
          <router-link
            class="header-main-menu__item left"
            v-for="(item, index) in menuLeft"
            :to="item.path"
            :key="index"
          >
            {{ item.meta ? item.meta.title : '导航菜单' }}
          </router-link>
        </div>
      </div>

      <div class="header-main-center">{{ title }}</div>

      <div class="header-main-right">
        <div class="header-main-menu header-main-menu--right">
          <router-link
            class="header-main-menu__item right"
            v-for="(item, index) in menuRight"
            :to="item.path"
            :key="index + 3"
          >
            {{ item.meta ? item.meta.title : '导航菜单' }}
          </router-link>

          <AppMessage />
        </div>
      </div>
    </header>

    <header v-show="subMenu" class="header-minor">
      <span class="header-minor-left">
        <span class="header-minor-title">{{ menuTitle }}</span>
        <!-- <span class="header-minor-subtitle">{{ menuSubtitle }}</span> -->
      </span>
      <ul class="header-minor-menu" v-if="menuList.length > 0">
        <template v-for="route in menuList">
          <router-link
            v-if="!route.meta.hidden"
            class="header-minor-menu__item"
            :to="`${activeMenu.path}/${route.path}`"
            :key="route.path"
          >
            {{ route.meta ? route.meta.title : '菜单标题' }}
          </router-link>
        </template>
      </ul>
    </header>
  </div>
</template>
<script>
import { mapGetters } from 'vuex';
import { upperCase } from 'lodash';
import AppMessage from './Message.vue';

export default {
  name: 'LayoutHeader',
  components: {
    AppMessage,
  },
  computed: {
    ...mapGetters(['logo', 'title', 'navMenu', 'activeMenu']),
    subMenu() {
      return this.activeMenu && this.activeMenu.meta && this.activeMenu.meta.submenu;
    },
    menuList() {
      return this.subMenu ? this.activeMenu.children : [];
    },
    menuLeft() {
      return this.navMenu.slice(0, 3);
    },
    menuRight() {
      return this.navMenu.slice(3);
    },
    menuTitle() {
      return this.$route.meta.title || '页面标题';
    },
    menuSubtitle() {
      const subtitle = this.$route.meta.subtitle || 'PAGE SUBTITLE';
      return upperCase(subtitle);
    },
  },
  data() {
    return {
      dateTime: this.$dayjs().format('YYYY年MM月DD日HH:mm:ss'),
    };
  },
  created() {
    this.dateTimer = setInterval(() => {
      this.dateTime = this.$dayjs().format('YYYY年MM月DD日HH:mm:ss');
    }, 1000);
  },
  destroyed() {
    if (this.dateTimer) {
      clearInterval(this.dateTimer);
      this.dateTimer = null;
    }
  },
};
</script>

<style lang="scss" scoped>
.header-main {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  width: 100%;
  height: 10vh;
  padding: 0 10px 2vh 10px;
  border-bottom: 1vh solid transparent;
  border-image: url('~@/assets/img/bg_header_border.png') 0 0 100%;
  background: {
    image: url('~@/assets/img/bg_header.png');
    repeat: no-repeat;
    position: center;
    size: 100% 100%;
  }

  &-left,
  &-right {
    flex: 1;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  &-logo {
    width: 120px;
    height: auto;
    // margin-top: 1vh;
  }
  &-time {
    display: flex;
    align-items: center;
    position: absolute;
    left: 10px;
    top: 7vh;
    font-size: 18px;
    font-weight: 600;
    white-space: nowrap;
    color: $text-secondary;
  }
  &-center {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 38vw;
    height: 100%;
    padding-bottom: 1vh;
    color: transparent;
    font-size: 40px;
    font-family: BigYoungBoldGB;
    background-clip: text;
    background-image: linear-gradient(180deg, #dbe5f0 0%, #8fc4ff 100%);
  }
  &-menu {
    display: inline-flex;
    align-items: center;

    &__item {
      position: relative;
      width: 150px;
      height: 50px;
      line-height: 50px;
      color: $text-secondary;
      font-size: 18px;
      text-align: center;

      + .header-main-menu__item {
        margin-left: -20px;
      }
      &::after {
        content: '';
        display: block;
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: url('~@/assets/img/bg_menu_item.png') no-repeat center/100% 100%;
      }
      &.right::after {
        transform: rotateY(180deg);
      }
      &.router-link-active {
        color: $text-regular;
        font-weight: 600;
      }
    }
  }
}

.header-minor {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 5vh;
  padding-left: 20px;
  background: linear-gradient(180deg, rgba(10, 39, 72, 0) 0%, rgba(7, 36, 66, 0.47) 100%);

  &-left {
    display: inline-flex;
    align-items: center;
    font-family: BigYoungBoldGB;
  }
  &-title {
    width: 200px;
    height: 40px;
    font-size: 24px;
    line-height: 40px;
    text-align: center;
    background: url('~@/assets/img/bg_title_main.png') no-repeat center/100% 100%;
  }
  &-subtitle {
    margin-left: 10px;
    color: $text-secondary;
    font-size: 16px;
  }
  &-menu {
    display: flex;
    align-items: center;

    &__item {
      position: relative;
      width: 140px;
      height: 40px;
      font-size: 20px;
      color: $text-secondary;
      line-height: 40px;
      text-align: center;

      + .header-minor-menu__item::before {
        content: '';
        position: absolute;
        top: 0;
        left: -10px;
        width: 20px;
        height: 100%;
        background: url('~@/assets/img/bg_menu_border.png') no-repeat center/100% 100%;
      }
      &.router-link-active {
        color: $text-regular;
        font-weight: 600;
      }
    }
  }
}
</style>
