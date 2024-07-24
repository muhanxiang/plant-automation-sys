<template>
  <div class="event-notice">
    <header class="event-notice-header">
      <span class="event-notice-header__title home-title">重要事件</span>
      <!-- <span class="event-notice-header__desc">IMPORTANT EVENTS</span> -->
    </header>
    <div v-loading="loading" class="event-notice-wrapper">
      <el-collapse
        v-model="activeEvent"
        class="event-notice-collapse"
        accordion
        v-if="eventList && eventList.length > 0"
      >
        <el-collapse-item v-for="item in eventList" :key="item.id" :name="item.id">
          <template v-slot:title>
            <div class="event-notice-collapse__title flex">
              <span class="event-notice-collapse__point">{{ item.patrolpoint_name }}</span>
              <span class="event-notice-collapse__status" :class="`sy-status-${item.patrol_status}`">
                <i class="el-icon-warning" />{{ item.patrolStatus }}
              </span>
            </div>
          </template>
          <div class="event-notice-collapse__content">
            <div class="event-notice-collapse__chart">
              <FileViewer :data="item" :image-size="50" :canvas="false" />
            </div>
            <ul class="event-notice-collapse__info">
              <li class="event-notice-collapse__item">
                <label>识别类型：</label>
                <span>{{ item.recognitionType }}</span>
              </li>
              <!-- <li class="event-notice-collapse__item">
                <label>识别结果：</label>
                <span>{{ item.value }}</span>
              </li> -->
              <li class="event-notice-collapse__item">
                <label>巡视结果：</label>
                <span>{{ item.patrolResult }}</span>
              </li>
              <li class="event-notice-collapse__item">
                <label>巡视时间：</label>
                <span>{{ item.patrol_time }}</span>
              </li>
            </ul>
          </div>
        </el-collapse-item>
      </el-collapse>
      <sy-empty v-else-if="!loading" description="暂无事件信息"></sy-empty>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { arrToMap } from '@/utils';
import Option from '@/enums/option';
import FileViewer from '@/components/FileViewer';

export default {
  name: 'EventNotice',
  components: {
    FileViewer,
  },
  computed: {
    ...mapGetters(['stationId']),
    patrolStateMap() {
      return arrToMap(Option.PatrolStates);
    },
    recognitionTypeMap() {
      return arrToMap(Option.RecognitionTypes);
    },
    eventChart() {
      return {
        backgroundColor: 'transparent',
        grid: { left: '8%', right: '8%', bottom: '8%' },
        legend: { show: false },
        xAxis: {
          boundaryGap: false,
          axisLabel: { color: '#035FA9', fontSize: 10 },
        },
        axisPointer: {
          show: true,
          snap: true,
        },
        tooltip: {
          show: false,
        },
        yAxis: {
          axisLabel: { show: false },
          axisTick: { show: false },
        },
        dataset: {
          source: [...this.historyData],
        },
        series: {
          type: 'line',
          name: '设备温度曲线',
          symbol: 'emptyCircle',
          symbolSize: 5,
          smooth: true,
          lineStyle: { color: '#03CEFB', width: 1 },
          areaStyle: {
            color: {
              type: 'linear',
              x: 0,
              y: 1,
              x2: 0,
              y2: 0,
              global: false,
              colorStops: [
                { offset: 0, color: 'rgba(3,206,251,.2)' },
                { offset: 1, color: 'rgba(3,206,251,.8)' },
              ],
            },
          },
          encode: { x: 'time', y: 'value' },
        },
      };
    },
  },
  data() {
    return {
      loading: false,
      activeEvent: '',
      eventList: [],
      historyData: [],
    };
  },
  mounted() {
    this.loading = true;
    // TODO 还是有问题
    this.$api
      .postHistoryApi('query', {
        from: 0,
        size: 5,
        index: 'history.item',
        match: {
          station_id: this.stationId,
          patrol_status: [2],
        },
        order: { patrol_time: 'desc' },
      })
      .then((res) => {
        this.eventList = res.data
          ? res.data.map((item) => {
              return {
                ...item,
                type: 'alarm',
                id: JSON.stringify({
                  id: item.id,
                  patrolpoint_id: item.patrolpoint_id,
                }),
                patrolResult: item.value_disp || item.value || '-',
                patrolStatus: this.patrolStateMap[item.patrol_status] || '-',
                recognitionType: this.recognitionTypeMap[item.recognition_type] || '-',
                patrolDevice: item.camera_name || item.robot_name || item.host_name || '-',
              };
            })
          : [];

        this.activeEvent = this.eventList?.[0]?.id;
        if (this.activeEvent) {
          this.handleChange(this.activeEvent);
        }
      })
      .finally(() => {
        this.loading = false;
      });
  },
  methods: {
    handleChange(activeNames) {
      if (!activeNames) return;
      const { patrolpoint_id } = JSON.parse(activeNames);
      this.$api
        .postHistoryApi('query', {
          from: 0,
          size: 5,
          index: 'history.item',
          match: {
            station_id: this.stationId,
            patrol_status: [2],
            patrolpoint_id,
          },
          order: { patrol_time: 'asc' },
        })
        .then((res) => {
          this.historyData = res.data
            ? res.data.map((item) => ({
                value: item.value,
                value_disp: item.value_disp || '-',
                time: this.$dayjs(item.patrol_time).format('HH:mm'),
              }))
            : [];
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.event-notice {
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
  &-wrapper {
    width: 100%;
    height: calc(100% - 4vh);
  }
  &-collapse {
    width: 100%;
    margin-top: 1vh;
    border: none;

    ::v-deep .el-collapse-item {
      width: 100%;

      &__header {
        display: flex;
        align-items: center;
        width: 100%;
        height: 4.5vh;
        line-height: 1.1;
        border-bottom: none;
        background-color: transparent;
      }
      &__wrap {
        border-bottom: none;
        background-color: transparent;
      }
      &__content {
        padding-bottom: 0;
      }
    }

    &__title {
      flex: auto;
      display: inline-flex;
      justify-content: space-between;
      margin-right: 10px;
      font-size: 14px;
    }
    &__point {
      max-width: 300px;
      @include text-ellipsis(2);
    }
    &__status {
      flex: none;
      display: inline-flex;
      align-items: center;

      > i {
        margin-right: 5px;
        font-size: 24px;
      }
    }
    &__content {
      display: flex;
      align-items: center;
      width: 100%;
    }
    &__chart {
      flex: none;
      width: 10vw;
      height: 10vh;
    }
    &__info {
      flex: auto;
      margin-left: 10px;
      font-size: 12px;
      color: $text-regular;
    }
    &__item {
      display: flex;
      align-items: center;
      width: 100%;
      font-size: 13px;

      > label {
        width: 35%;
      }
      > span {
        width: 60%;
        @include text-ellipsis(2);
      }
    }
  }
}
</style>
