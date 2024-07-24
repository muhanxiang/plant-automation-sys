<template>
  <span class="date-page">
    <i class="el-icon-arrow-left arrow-icon" @click="prevDate"></i>
    <span>{{ selDateTimes }}</span>
    <i class="el-icon-arrow-right arrow-icon" @click="nextDate"></i>
  </span>
</template>
<script>
export default {
  data() {
    return {
      selDateTimes: '',
    };
  },
  methods: {
    prevDate() {
      //前一天
      let odata = new Date(new Date(this.selDateTimes).getTime() - 24 * 60 * 60 * 1000); //计算当前日期 -1
      this.emitChange(odata);
    },
    nextDate() {
      //后一天
      let odata = new Date(new Date(this.selDateTimes).getTime() + 24 * 60 * 60 * 1000); //计算当前日期 +1
      this.emitChange(odata);
    },
    convertToDate(odate) {
      let date = new Date(odate);
      let y = date.getFullYear();
      let m = date.getMonth() + 1;
      let d = date.getDate();
      m = m < 10 ? '0' + m : m; //月小于10，加0
      d = d < 10 ? '0' + d : d; //day小于10，加0
      return `${y}-${m}-${d}`;
    },
    emitChange(date) {
      this.selDateTimes = this.convertToDate(date); //格式化日期并赋值
      this.$emit('input', this.selDateTimes);
    },
  },
  mounted() {
    this.emitChange(new Date());
  },
};
</script>
<style lang="scss" scoped>
.date-page {
  font-size: 20px;
  font-weight: 400;
  color: #00c6f4;
  .arrow-icon {
    margin: 0 10px;
  }
}
</style>