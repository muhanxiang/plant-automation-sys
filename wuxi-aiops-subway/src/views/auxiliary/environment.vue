<template>
  <el-container class="sy-container sy-ambient">
    <div class="sy-view">
      <div class="sy-card">
        <div class="sy-card-header">
          <ul>
            <li v-for="(item, index) in dataTop" :key="index" class="sy-btn-regular" :class="{ active: index === 0 }">
              <a>{{ item.name }}</a>
            </li>
          </ul>
        </div>
        <div class="sy-card-body">
          <div class="sy-card-body-top">
            <ul>
              <li
                v-for="(item, index) in dataTopCenter"
                :key="index"
                class="sy-top-center"
                :class="{ active: index === 0 }"
              >
                <a class="sy-top-center-img"></a>
                <span
                  :style="{
                    color: item.title === '风速' ? '#00CBFF' : item.title === '风向' ? '#FFCC00' : '#56FEFE',
                  }"
                  >{{ item.title }}</span
                >
                <a>{{ item.text }}</a>
              </li>
            </ul>
          </div>
          <div class="sy-card-body-bottom">
            <div class="sy-card-body-bottom-header sy-form">
              <el-form ref="form" :model="form" :inline="true">
                <el-form-item style="width: auto">
                  <el-date-picker type="date" placeholder="请选择时间" v-model="form.startTime"> </el-date-picker>
                </el-form-item>
                <el-form-item style="width: auto"><span>至</span></el-form-item>
                <el-form-item style="width: auto">
                  <el-date-picker type="date" placeholder="请选择时间" v-model="form.endTime"> </el-date-picker>
                </el-form-item>
              </el-form>
            </div>
            <div class="sy-card-body-bottom-echarts">
              <EnvChart />
            </div>
          </div>
        </div>
      </div>
    </div>
  </el-container>
</template>
<script>
import EnvChart from './components/EnvChart';

export default {
  name: 'Environment',
  components: {
    EnvChart,
  },
  data() {
    return {
      dataTop: [],
      dataTopCenter: [],
      form: {},
    };
  },
  created() {},
  mounted() {
    this.getDataInit(); // 初始化按钮数据
  },
  methods: {
    async getDataInit() {
      this.dataTop = [
        { name: '微气象传感器' },
        { name: '温湿度' },
        { name: '水浸传感器' },
        { name: '水位传感器' },
        { name: '空调' },
      ];
      this.dataTopCenter = [
        { title: '', text: '27°~19°' },
        { title: '风速', text: '44m/s' },
        { title: '风向', text: '东南' },
        { title: '湿度', text: '15%' },
      ];
    },
  },
};
</script>
<style lang="scss" scoped>
.sy-container {
  .sy-card {
    display: flex;
    flex: 1;
    color: #00cbff;
    flex-direction: column;

    &-header {
      color: #00cbff;
      width: 100%;
      height: 3vh;
      line-height: 3vh;

      ul {
        display: flex;
        flex: 1;
        height: 100%;
        align-items: center;
      }
    }

    &-body {
      display: flex;
      flex: 1;
      flex-direction: column;

      &-top {
        padding: 20px;
        border-bottom: 1px solid #0a4e82;
        ul {
          display: flex;
          justify-content: space-around;
          padding: 3vh 5vw;

          li {
            display: flex;
            align-items: center;
            width: 20%;
            height: 10vh;
            line-height: 10vh;
            background: {
              image: url('~@/assets/img/bg_weather_normal.png');
              repeat: no-repeat;
              size: 100% 100%;
            }

            .sy-top-center-img {
              width: 60px;
              height: 60px;
              margin: 0 50px 0 30px;
            }

            &:nth-child(1) .sy-top-center-img {
              background: {
                image: url('~@/assets/img/icon_weather.png');
                repeat: no-repeat;
                size: 100% 100%;
              }
            }

            &:nth-child(2) .sy-top-center-img {
              background: {
                image: url('~@/assets/img/icon_wind.png');
                repeat: no-repeat;
                size: 100% 100%;
              }
            }

            &:nth-child(3) .sy-top-center-img {
              background: {
                image: url('~@/assets/img/icon_wdir.png');
                repeat: no-repeat;
                size: 100% 100%;
              }
            }

            &:nth-child(4) .sy-top-center-img {
              background: {
                image: url('~@/assets/img/icon_humidity.png');
                repeat: no-repeat;
                size: 100% 100%;
              }
            }

            a {
              font-size: 30px;
              font-family: Microsoft YaHei;
              font-weight: bold;
              color: #b7cffc;
            }

            span {
              padding-right: 10px;
            }

            &:hover {
              background: {
                image: url('~@/assets/img/bg_weather_active.png');
                repeat: no-repeat;
                size: 100% 100%;
              }
            }
          }

          .active {
            background: {
              image: url('~@/assets/img/bg_weather_active.png');
              repeat: no-repeat;
              size: 100% 100%;
            }
          }
        }
      }

      &-bottom {
        &-header {
          padding: 30px 180px;

          .el-form--inline {
            justify-content: flex-end;
            padding: 0;

            span {
              padding: 0 20px;
              color: $text-regular;
              font-size: 12px;
            }
          }
        }
      }
    }
  }
}
</style>
