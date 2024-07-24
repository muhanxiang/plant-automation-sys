<template>
  <div class="device-statis" v-loading="loading">
    <header class="device-statis-header home-title">设备统计</header>

    <ul class="device-statis-list">
      <li class="device-statis-item" v-for="(val, key) in deviceStatis" :key="key">
        <div class="device-statis-item__value">
          <sy-icon :name="key" />
          <span class="sy-digit">{{ val.value }}</span>
        </div>
        <div class="device-statis-item__name">{{ val.name }}</div>
      </li>
    </ul>
    <sy-chart class="device-statis-chart" type="bar" :option="deviceOption"></sy-chart>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';

const DeviceType = {
  nvr: '硬盘录像机',
  camera: '摄像机',
  robot: '机器人',
};

const DeviceState = {
  online: '在线',
  offline: '离线',
};

export default {
  name: 'DeviceStatis',
  computed: {
    ...mapGetters(['stationId']),
    deviceOption() {
      const colors = ['#035fa9', '#f72222'];
      return {
        grid: {
          top: '50px',
        },
        legend: {
          left: 'auto',
          right: '5%',
        },
        xAxis: {
          axisLabel: {
            interval: 0,
            fontSize: 14,
          },
        },
        dataset: {
          source: Object.entries(this.deviceStatus).map(([key, val]) => {
            return { ...val, name: DeviceType[key] || '-' };
          }),
        },
        series: Object.entries(DeviceState).map(([key, val], index) => ({
          type: 'bar',
          name: val,
          // stack: 'device',
          barMaxWidth: 30,
          datasetIndex: 0,
          label: {
            show: true,
            position: 'top',
            color: '#fff',
            fontSize: 14,
            fontWeight: 500,
          },
          itemStyle: {
            color: colors[index],
          },
          encode: { x: 'name', y: key },
        })),
      };
    },
  },
  data() {
    return {
      loading: false,
      deviceStatis: {
        camera: { name: '可见光摄像机', value: 0 },
        infrared: { name: '红外热成像仪', value: 0 },
        // robot: { name: '巡视机器人', value: 0 },
        // host: { name: '巡视主机', value: 0 },
      },
      deviceStatus: {
        nvr: { online: 0, offline: 0 },
        camera: { online: 0, offline: 0 },
      },
    };
  },
  mounted() {
    this.getDeviceStaticData();
  },
  methods: {
    getDeviceStaticData() {
      this.loading = true;
      this.$api
        .getBaseApi('detector', {
          station_id: this.stationId,
        })
        .then((res) => {
          // console.log('res: ', res);
          if (!res) return;
          this.deviceStatis = {
            camera: { name: '可见光摄像机', value: res.ipc_count },
            infrared: { name: '红外热成像仪', value: res.ifr_count },
            // robot: { name: '巡视机器人', value: res.robot_count },
            // host: { name: '巡视主机', value: res.host_count },
          };
        })
        .finally(() => {
          this.loading = false;
        });

      this.$api.getMediaApi(`count/${this.stationId}/`).then((res) => {
        if (!res) return;
        const { nvr_total, nvr_online, camera_total, camera_online } = res;
        this.deviceStatus = {
          nvr: { online: nvr_online, offline: nvr_total - nvr_online },
          camera: { online: camera_online, offline: camera_total - camera_online },
        };
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.device-statis {
  flex: auto;
  width: 100%;
  padding: 15px;
  background: url('~@/assets/img/bg_panel.png') no-repeat center/100% 100%;

  &-header {
    padding-left: 20px;
  }
  &-list {
    display: flex;
    align-items: center;
    justify-content: space-around;
    width: 100%;
    height: 15vh;
  }
  &-item {
    display: inline-flex;
    align-items: center;
    flex-direction: column;

    &__name {
      color: $text-regular;
      font-size: 16px;
      margin-top: 1vh;
    }
    &__value {
      display: inline-flex;
      align-items: center;
      flex-direction: column;
      justify-content: center;
      width: 80px;
      height: 80px;
      color: $text-regular;
      font-size: 24px;
      border-radius: 50%;
      border: 4px solid #035fa9;

      .sy-icon {
        margin-bottom: 0.5vh;
        font-size: 24px;
      }
    }
  }
  &-chart {
    width: 100%;
    height: 25vh;
    margin-top: 1vh;
  }
}
</style>
