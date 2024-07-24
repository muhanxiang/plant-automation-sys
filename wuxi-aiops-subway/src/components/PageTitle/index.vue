<template>
  <el-header class="page-header" height="57px">
    <div class="page-header-left">
      <div class="page-header-left__title">{{ title }}</div>
      <img :src="oblique" />
      <div class="page-header-left__desc">{{ enTitle }}</div>
    </div>
    <div class="page-header-right">
      <span>任务日历</span>
      <img :src="obliqueBlack" />
      <span>二级选项</span>
      <img :src="obliqueBlack" />
      <span>三级选项</span>
    </div>
  </el-header>
</template>

<script>
import oblique from '@/assets/img/bg_oblique.png';
import obliqueBlack from '@/assets/img/bg_oblique.png';

export default {
  name: 'PageTitle',
  props: {
    title: {
      //中文标题
      type: String,
      required: true,
    },
    enTitle: {
      //英文标题
      type: String,
      required: true,
    },
  },
  data() {
    return {
      oblique,
      obliqueBlack,
    };
  },
};
</script>

<style lang="scss" scoped>
.page-header {
  background: url('~@/assets/img/bg_shadow.png') no-repeat center/100% 100%;
  display: flex;
  align-items: center;
  .page-header-left {
    display: flex;
    align-items: center;
    flex: 1;
    &__title {
      width: 200px;
      height: 40px;
      line-height: 40px;
      color: #feffff;
      font-size: 24px;
    }
    &__desc {
      margin-left: 20px;
      color: #035fa9;
      font-size: 16px;
      font-weight: 400;
      line-height: 30px;
    }
  }
  .page-header-right {
    display: flex;
    align-items: center;
    height: 40px;
    line-height: 40px;
  }
}
</style>
