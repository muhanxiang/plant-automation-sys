<template>
  <div class="station">
    <div class="station-content" v-loading="loading">
      <template v-if="!areaList || areaList.length === 0">
        <sy-empty v-if="!loading" description="暂无诊断信息"></sy-empty>
      </template>
      <div v-else class="station-content-wrapper">
        <div v-for="area in areaList" :key="area.guid" class="station-item sy-corner" @click="handleCard(area)">
          <div class="station-item-title">{{ area.name }}</div>
          <div class="station-item-content">
            <div class="dric-left">
              <sy-image :src="`${baseUrl}/${area.avatar}`" :image-size="50" lazy></sy-image>
            </div>
            <div class="dric-right">
              <div class="dric-right-top">
                <div>
                  <sy-icon name="camera"></sy-icon>
                  <span class="label">摄像机</span>
                  <span class="number">{{ area.camera_count }}</span>
                </div>
                <div>
                  <sy-icon name="signal"></sy-icon>
                  <span class="label">传感器</span>
                  <span class="number">{{ area.sensor_count }}</span>
                </div>
              </div>
              <div class="dric-right-bottom">
                <div class="dric-right-bottom-item">
                  <span>告警</span>
                  <span class="number">{{ area.warning || 0 }}</span>
                </div>
                <div class="dric-right-bottom-item">
                  <span>缺陷</span>
                  <span class="number">{{ area.defects || 0 }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { baseUrl } from '@/config';

export default {
  name: 'StationOverview',
  computed: {
    ...mapGetters(['company', 'mode', 'stationId']),
    baseUrl() {
      return baseUrl;
    },
  },
  data() {
    return {
      station: {},
      loading: false,
      areaList: [],
    };
  },
  methods: {
    handleCard(item) {
      this.$router.push({
        path: 'diagnosis-area',
        query: {
          guid: item.guid,
          name: item.name,
        },
      });
    },
    async getDataInit() {
      this.loading = true;
      this.station = {
        ...this.$route.query,
      };
      if (!this.station.guid) {
        this.station = {
          guid: this.stationId,
          name: this.company,
        };
      }
      const { results } = await this.$api.getSensorApi(`area/${this.station.guid}`).finally(() => {
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
%number {
  padding-left: 15px;
  font-size: 24px;
  font-family: 'DS-DIGIB';
}
.station {
  height: 100%;
  border: $border-default;
  position: relative;
  &-content {
    height: 100%;
    overflow: hidden auto;
    padding: 20px;
    &-wrapper {
      display: grid;
      gap: 20px;
      grid-template-columns: repeat(4, 1fr);
      width: 100%;
    }
  }
  &-top {
    position: absolute;
    display: flex;
    align-items: center;
    justify-content: center;
    border: $border-default;
    top: 0;
    right: 0;
    width: 400px;
    height: 25px;
    font-size: 16px;
    background-image: $bg-popper;
    z-index: 2;
  }
  &-item {
    display: flex;
    flex-direction: column;
    width: 350px;
    height: 200px;
    background-image: $bg-popper;
    cursor: pointer;
    box-shadow: 0 0 5px $border-light;
    transition: box-shadow 0.3s;

    &:hover {
      box-shadow: 0 0 10px 2px $border-light;
    }

    &-title {
      flex: none;
      width: 100%;
      height: 3vh;
      font-size: 16px;
      line-height: 3vh;
      text-align: center;
      background-color: $bg-base;
      border-bottom: $border-default;
      @include text-ellipsis();
    }
    &-content {
      display: flex;
      align-items: center;
      flex: auto;
      width: 100%;

      .dric {
        &-left {
          width: 150px;
          height: 100%;
          font-size: 14px;
          overflow: hidden;
        }
        &-right {
          display: flex;
          flex-direction: column;
          flex: auto;
          height: 100%;
          border-left: $border-default;

          &-top {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            flex: 1;
            > div {
              display: flex;
              align-items: center;
              justify-content: center;
              margin-bottom: 10px;
              font-size: 16px;
              .label {
                padding-left: 10px;
              }
              .number {
                @extend %number;
                font-size: 18px;
              }
            }
          }
          &-bottom {
            width: 100%;
            display: flex;
            border: $border-default;
            border-left: none;
            height: 3vh;
            &-item {
              display: flex;
              align-items: center;
              justify-content: center;
              flex: auto;
              &:last-of-type {
                border-left: $border-default;
              }
              .number {
                @extend %number;
                font-size: 18px;
              }
            }
          }
        }
      }
    }
  }
}
</style>
