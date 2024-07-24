<template>
  <div class="base-info">
    <header class="base-info-header">
      <span class="base-info-header__title home-title">基础信息</span>
      <!-- <span class="base-info-header__desc">BASIC INFORMATION</span> -->
    </header>
    <div class="base-info-company">{{ system.name }}</div>
    <div v-loading="loading" class="sy-view base-info-wrapper">
      <ul class="base-info-system">
        <li class="base-info-system__item">
          <div>
            <span class="number">{{ totalDay }}</span
            >天
          </div>
          <div>系统运行天数</div>
        </li>
        <!-- <li class="base-info-system__item weather">
          <sy-icon name="cloudy" />
          <div>多云</div>
        </li> -->
      </ul>
      <ul class="base-info-weather">
        <li class="base-info-weather__item" v-for="(val, key) in infoData" :key="key">
          <div class="base-info-weather__icon"><sy-icon :name="key" /></div>
          <div class="base-info-weather__name">{{ val.name }}</div>
          <div class="base-info-weather__value">{{ val.value }}</div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  name: 'BasicInfo',
  computed: {
    ...mapGetters(['system']),
  },
  data() {
    return {
      loading: false,
      totalDay: 0,
      infoData: {
        temperature: { name: '环境温度', value: '-' },
        humidity: { name: '环境湿度', value: '-' },
        rainfall: { name: '降雨量', value: '-' },
        windspeed: { name: '风速', value: '-' },
      },
    };
  },
  created() {
    this.getBaseInfo();

    this.totalDay = this.$dayjs().diff(this.system['date'], 'day');
  },
  methods: {
    async getBaseInfo() {
      this.loading = true;
      // 温度 ，湿度，降雨，风速, TODO暂时只接入了温湿度
      const { humidity, temperature, rainfall, windspeed } = this.system;
      this.$api
        .getMeasureApi('commpoint', {
          ids: `${humidity},${temperature}`,
        })
        .then((res) => {
          if (res?.results?.length > 0) {
            res.results.forEach((item) => {
              switch (item.id) {
                case humidity:
                  this.infoData.humidity.value = item.value_disp;
                  break;
                case temperature:
                  this.infoData.temperature.value = item.value_disp;
                  break;
                case rainfall:
                  this.infoData.rainfall.value = item.value_disp;
                  break;
                case windspeed:
                  this.infoData.windspeed.value = item.value_disp;
                  break;
              }
            });
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.base-info {
  width: 100%;
  height: 41vh;
  padding: 15px;
  background: linear-gradient(0deg, #041325 0%, transparent 100%);

  &-header {
    display: flex;
    align-items: flex-end;

    &__title {
      width: 200px;
      height: 40px;
      line-height: 40px;
      margin-right: 10px;
      padding-left: 20px;
      background: url('~@/assets/img/bg_title_main.png') no-repeat center/100% 100%;
    }
    &__desc {
      color: #035fa9;
      font-size: 16px;
      font-weight: 400;
      line-height: 30px;
    }
  }

  &-company {
    margin-top: 1vh;
    margin-bottom: 1vh;
    color: #0078e3;
    font-size: 20px;
    font-family: BigYoungBoldGB;
  }

  &-wrapper {
    width: 100;
    height: 28vh;
  }

  &-system {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    height: 12vh;

    &__item {
      display: inline-flex;
      align-items: center;
      flex-direction: column;
      justify-content: center;
      flex: 1;
      font-size: 20px;
      color: $text-regular;

      &.weather {
        font-size: 24px;

        .sy-icon {
          color: #91fff6;
          font-size: 60px;
        }
      }

      .number {
        margin-right: 10px;
        color: #035497;
        font-size: 60px;
        // font-style: italic;
        font-family: DS-DIGIB;
        -webkit-text-fill-color: transparent;
        background: linear-gradient(180deg, #dbe5f0 0%, #8fc4ff 100%);
        background-clip: text;
      }
    }
  }

  &-weather {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-top: 5px;
    width: 100%;
    height: 16vh;
    background: $bg-view;

    &__item {
      display: inline-flex;
      align-items: center;
      flex-direction: column;
      justify-content: center;
      flex: 1;
      color: $text-regular;

      &:nth-child(odd) {
        .base-info-weather__icon {
          border-color: #035fa9;
          @include a-ripple(#035fa9, 10s);
        }
        .base-info-weather__value {
          color: #0884e7;
        }
      }
      &:nth-child(even) {
        .base-info-weather__icon {
          border-color: #00d0ff;
          @include a-ripple(#00d0ff, 8s);
        }
        .base-info-weather__value {
          color: #00fffc;
        }
      }
    }
    &__icon {
      display: flex;
      align-items: center;
      justify-content: center;
      flex: none;
      width: 80px;
      height: 80px;
      color: #91fff6;
      font-size: 32px;
      border-radius: 50%;
      border: 4px solid #035fa9;

      .sy-icon {
        z-index: 20;
        font-weight: 600;
        color: #91fff6;
      }
    }
    &__name {
      margin: 5px 0;
      font-size: 16px;
    }
    &__value {
      font-size: 20px;
      font-weight: 600;
    }
  }
}
</style>
