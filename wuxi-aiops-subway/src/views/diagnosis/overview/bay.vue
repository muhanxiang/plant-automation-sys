<template>
  <div class="bay">
    <div class="bay-content" v-loading="loading">
      <!-- 中心 -->
      <div class="bay-content-center sy-corner" ref="bay">
        <div class="bay-content-center-top">{{ bay.name }}</div>
        <div class="bay-content-center-bottom">
          <div class="accb-item">
            <span>告警</span>
            <span class="number">{{ bay.warning || 0 }}</span>
          </div>
          <div class="accb-item">
            <span>缺陷</span>
            <span class="number">{{ bay.defects || 0 }}</span>
          </div>
        </div>
      </div>
      <!-- 连线 -->
      <svg xmlns="http://www.w3.org/2000/svg" class="bay-svg" version="1.1">
        <line v-for="(path, index) in lineList" class="bay-svg__line" :key="`path${index}`" v-bind="path" />
      </svg>
      <div class="bay-content-wrapper">
        <template v-for="(item, index) in areaList">
          <!-- 做占位用 -->
          <div v-if="!item" :key="index"></div>
          <div v-else :key="item.guid" :ref="`bay-${item.guid}`" class="bay-card sy-corner" @click="handleCard(item)">
            <div class="bay-card-left">
              <div class="bay-card-left-top">
                <sy-icon :name="iconMap[item.sensor_type]"></sy-icon>
              </div>
              <div :class="`bay-card-left-bottom status-${item.status || 0}`">
                <template v-if="!item.status || item.status === 0">
                  <sy-icon name="offline"></sy-icon>
                  <span>离线</span>
                </template>
                <template v-else>
                  <sy-icon name="online"></sy-icon>
                  <span>在线</span>
                </template>
              </div>
            </div>
            <div class="bay-card-right">
              <div class="bay-card-right-top">{{ item.name }}</div>
              <div class="bay-card-right-bottom">
                <div class="burb-item">
                  <span>告警</span>
                  <span class="number">{{ item.warning || 0 }}</span>
                </div>
                <div class="burb-item">
                  <span>缺陷</span>
                  <span class="number">{{ item.defects || 0 }}</span>
                </div>
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>
    <div class="bay-right-bottom" @click="handleBack">返回</div>
  </div>
</template>

<script>
import { arrToMap } from '@/utils';
import { SensorType } from '@/enums';
import Option from '@/enums/option';

export default {
  name: 'BayOverview',
  computed: {
    iconMap() {
      return arrToMap(Option.SensorTypes, 'key', 'icon');
    },
    sensorTypeMap() {
      return SensorType;
    },
  },
  data() {
    return {
      bay: {},
      loading: false,
      areaList: [],
      lineList: [],
      device: {
        name: '35kV开关室L05断路器',
        warning: 10,
        defects: 5,
      },
    };
  },
  methods: {
    handleBack() {
      this.$router.go(-1);
    },
    handleCard(item) {
      this.$router.push({
        path: '/diagnosis/device-details',
        query: {
          guid: item.guid,
          name: item.name,
          sensorType: item.sensor_type,
          status: item.status || 0,
        },
      });
    },
    async getDataInit() {
      this.loading = true;
      this.bay = {
        ...this.$route.query,
      };
      const { results } = await this.$api.getBaseApi('sensor', { bay: this.bay.guid }).finally(() => {
        this.loading = false;
      });
      if (!results || results.length === 0) return;
      const length = results.length;
      const areas = Array.from({ length: 9 });
      switch (length) {
        case 1:
          areas[0] = results[0];
          break;
        case 2:
          areas[0] = results[0];
          areas[2] = results[1];
          break;
        case 3:
          areas[0] = results[0];
          areas[2] = results[1];
          areas[6] = results[2];
          break;
        case 4:
          areas[0] = results[0];
          areas[2] = results[1];
          areas[6] = results[2];
          areas[8] = results[3];
          break;
        case 5:
          areas[0] = results[0];
          areas[1] = results[1];
          areas[2] = results[2];
          areas[6] = results[3];
          areas[8] = results[4];
          break;
        case 6:
          areas[0] = results[0];
          areas[1] = results[1];
          areas[2] = results[2];
          areas[6] = results[3];
          areas[7] = results[4];
          areas[8] = results[5];
          break;
        case 7:
          areas[0] = results[0];
          areas[1] = results[1];
          areas[2] = results[2];
          areas[3] = results[3];
          areas[6] = results[4];
          areas[7] = results[5];
          areas[8] = results[6];
          break;
        case 8:
        default:
          areas[0] = results[0];
          areas[1] = results[1];
          areas[2] = results[2];
          areas[3] = results[3];
          areas[5] = results[4];
          areas[6] = results[5];
          areas[7] = results[6];
          areas[8] = results[7];
          break;
      }
      this.areaList = areas;
      this.drawLine();
    },
    drawLine() {
      const paths = [];
      this.$nextTick(() => {
        const { offsetLeft: x1, offsetTop: y1 } = this.$refs.bay;
        for (let key in this.$refs) {
          if (key === 'bay') continue;
          const { offsetLeft, offsetTop, offsetWidth, offsetHeight } = this.$refs[key][0];
          let x2 = offsetLeft + offsetWidth / 2,
            y2 = offsetTop + offsetHeight / 2;
          paths.push({ x1, x2, y1, y2 });
        }
        this.lineList = paths;
      });
    },
  },
  mounted() {
    this.getDataInit();
  },
};
</script>

<style lang="scss" scoped>
%stats {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 15px;
  flex: auto;
  span {
    font-size: 14px;
    white-space: nowrap;
  }
  .number {
    padding-left: 10px;
    font-size: 18px;
    font-family: 'DS-DIGIB';
  }
  &:last-of-type {
    border-left: 1px solid $border-dark;
  }
}

.bay {
  position: relative;
  height: 100%;
  border: $border-default;

  &-svg {
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
    &__line {
      stroke-width: 2px;
      stroke: rgba($border-light, 0.3);
    }
  }
  &-right-bottom {
    display: flex;
    align-items: center;
    justify-content: center;
    position: absolute;
    width: 88px;
    height: 40px;
    background-color: rgba($border-light, 0.8);
    border: $border-default;
    border-radius: 20px;
    right: 20px;
    bottom: 20px;
    cursor: pointer;
    &:hover {
      background-color: $border-light;
    }
  }
  &-content {
    height: 100%;
    position: relative;
    &-wrapper {
      position: relative;
      display: grid;
      place-items: center;
      grid-template-columns: repeat(3, 1fr);
      grid-template-rows: 1fr 1fr 1fr;
      height: 100%;
    }
    &-center {
      display: flex;
      flex-direction: column;
      position: absolute;
      transform: translate(-50%, -50%);
      top: 50%;
      left: 50%;
      width: 450px;
      height: 220px;
      z-index: 2;
      &-top {
        display: flex;
        align-items: center;
        justify-content: center;
        flex: auto;
        border-top: $border-default;
        background-color: $bg-base;
        font-size: 18px;
      }
      &-bottom {
        display: flex;
        background-color: $bg-table;
        .accb-item {
          @extend %stats;
        }
      }
    }
  }
  &-card {
    display: flex;
    width: 350px;
    height: 188px;
    cursor: pointer;
    box-shadow: 0 0 5px $border-light;
    transition: box-shadow 0.3s;
    &:hover {
      box-shadow: 0 0 10px 2px $border-light;
    }
    &:hover {
      box-shadow: 0 0 10px 2px $border-light;
    }
    &-left {
      display: flex;
      flex-direction: column;
      border-top: $border-default;
      align-items: center;
      justify-content: center;
      width: 200px;
      height: 100%;
      background-color: $bg-base;
      border-right: $border-default;
      &-top {
        font-size: 50px;
      }
      .status-0 {
        background-color: $color-danger;
      }
      .status-1 {
        background-color: $border-light;
      }
      &-bottom {
        display: flex;
        align-items: center;
        color: $color-white;
        border-radius: 20px;
        padding: 5px 15px;
        margin-top: 10px;
        span {
          padding-left: 5px;
        }
      }
    }
    &-right {
      display: flex;
      flex-direction: column;
      border-top: $border-default;
      height: 100%;
      &-top {
        display: flex;
        flex: auto;
        align-items: center;
        justify-content: center;
        text-align: center;
        background-color: $bg-base;
        font-size: 18px;
        padding: 0 10px;
      }
      &-bottom {
        display: flex;
        background-color: $bg-table;
        .burb {
          &-item {
            @extend %stats;
          }
        }
      }
    }
  }
}
</style>
