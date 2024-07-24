<template>
  <div class="area">
    <!-- <div class="area-right-top">{{ area.name }}</div> -->
    <div class="area-content" v-loading="loading">
      <template v-if="!areaList || areaList.length === 0">
        <sy-empty v-if="!loading"></sy-empty>
      </template>
      <div v-else class="area-content-wrapper">
        <div v-for="bay in areaList" :key="bay.guid" class="area-content-item sy-corner" @click="handleCard(bay)">
          <div class="area-content-item-top" :title="bay.name">{{ bay.name }}</div>
          <div class="area-content-item-bottom">
            <div class="acib-left">
              <div class="icon">
                <sy-image :src="`${baseUrl}/${bay.avatar}`" :image-size="50" lazy></sy-image>
              </div>
              <div class="acib-stats">
                <div class="acib-stats-item">
                  <span>告警</span>
                  <span class="number">{{ bay.warning || 0 }}</span>
                </div>
                <div class="acib-stats-item">
                  <span>缺陷</span>
                  <span class="number">{{ bay.defects || 0 }}</span>
                </div>
              </div>
            </div>
            <div class="acib-right">
              <sy-empty v-if="!bay.sensor_count || bay.sensor_count.length === 0" :image-size="50"></sy-empty>
              <template v-else>
                <div class="acib-right-item" v-for="sensor in bay.sensor_count" :key="sensor.type">
                  <span class="acib-right-item-label">
                    <sy-icon :name="iconMap[sensor.type]"></sy-icon>
                    <span>{{ sensorTypeMap[sensor.type] }}</span>
                  </span>
                  <span class="number">{{ sensor.count }}</span>
                </div>
              </template>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="area-right-bottom" @click="handleBack">返回</div>
  </div>
</template>

<script>
import { arrToMap } from '@/utils';
import { baseUrl } from '@/config';
import Option from '@/enums/option';

export default {
  name: 'AreaOverview',
  computed: {
    sensorTypeMap() {
      return arrToMap(Option.SensorTypes);
    },
    iconMap() {
      return arrToMap(Option.SensorTypes, 'key', 'icon');
    },
    baseUrl() {
      return baseUrl;
    },
  },
  data() {
    return {
      area: {},
      loading: false,
      areaList: [],
    };
  },
  methods: {
    handleCard(bay) {
      this.$router.push({
        path: '/diagnosis/overview/diagnosis-bay',
        query: {
          guid: bay.guid,
          name: bay.name,
        },
      });
    },
    handleBack() {
      this.$router.go(-1);
    },
    async getDataInit() {
      this.loading = true;
      this.area = {
        ...this.$route.query,
      };
      const { results } = await this.$api.getSensorApi(`bay/${this.area.guid}`).finally(() => {
        this.loading = false;
      });
      this.loading = false;
      if (!results || results.length === 0) return;
      this.areaList = results;
    },
  },
  mounted() {
    this.getDataInit();
  },
};
</script>

<style lang="scss" scoped>
.area {
  position: relative;
  height: 100%;
  border: $border-default;
  &-right-top {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 400px;
    height: 25px;
    position: absolute;
    right: 0;
    top: 0;
    font-size: 16px;
    z-index: 2;
    background-image: $bg-popper;
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
    width: 100%;
    height: 100%;
    padding: 20px;
    overflow: hidden auto;
    &-wrapper {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      width: 100%;
      gap: 20px;
    }
    &-item {
      display: flex;
      flex-direction: column;
      height: 220px;
      border: $border-default;
      cursor: pointer;
      box-shadow: 0 0 5px $border-light;
      transition: box-shadow 0.3s;
      &:hover {
        box-shadow: 0 0 10px 2px $border-light;
      }
      &-top {
        font-size: 16px;
        text-align: center;
        padding: 5px;
        background-color: $bg-base;
        border-bottom: $border-default;
        @include text-ellipsis();
      }
      &-bottom {
        display: flex;
        align-items: center;
        height: calc(100% - 30px);
        flex: auto;
        .acib {
          &-left {
            display: flex;
            flex-direction: column;
            width: 150px;
            height: 100%;
            border-right: $border-default;
            font-size: 38px;
            .icon {
              display: flex;
              align-items: center;
              justify-content: center;
              height: calc(100% - 30px);
              background-color: $bg-video;
              ::v-deep .el-image {
                width: 100%;
                height: 100%;
                &:hover {
                  object-fit: cover;
                }
              }
            }
          }
          &-stats {
            display: flex;
            font-size: 16px;
            background-color: $bg-label;
            &-item {
              display: flex;
              align-items: center;
              justify-content: center;
              flex: auto;
              height: 30px;
              font-size: 14px;
              border-top: $border-default;
              &:first-of-type {
                border-right: $border-default;
              }
              .number {
                font-family: 'DS-DIGIB';
                font-size: 18px;
                padding-left: 20px;
              }
            }
          }
          &-right {
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;
            flex: auto;
            background-color: $bg-label;
            height: 100%;
            box-shadow: inset 0 0 10px $bg-base;
            padding: 0 40px;
            font-size: 16px;
            &-item {
              display: flex;
              align-items: center;
              justify-content: space-between;
              padding-bottom: 10px;
              width: 100%;
              &-label {
                display: inline-flex;
                align-items: center;
              }
              &:first-of-type {
                padding-top: 10px;
              }
              .number {
                font-family: 'DS-DIGIB';
                font-size: 18px;
              }
              span {
                flex: none;
                span {
                  padding-left: 10px;
                }
              }
            }
          }
        }
      }
    }
  }
}
</style>
