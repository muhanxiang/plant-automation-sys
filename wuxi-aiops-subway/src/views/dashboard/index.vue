<template>
  <el-row id="home">
    <el-col class="sy-col-vertical" :span="6">
      <BasicInfo />
      <DeviceStatis />
    </el-col>

    <el-col class="sy-col-vertical" :span="12">
      <SMap class="home-smap" />

      <ul class="home-statis">
        <li class="home-statis-item">
          <span class="home-statis-icon delay-1s">
            <sy-icon name="task" />
          </span>
          <span class="home-statis-info">
            <label>累计巡视数量</label>
            <SyCount :startVal="taskStatis.start" :endVal="taskStatis.stop" :duration="duration"></SyCount>
          </span>
        </li>
        <li class="home-statis-item">
          <span class="home-statis-icon delay-2s">
            <sy-icon name="linkage" />
          </span>
          <span class="home-statis-info">
            <label>累计联动次数</label>
            <SyCount :startVal="linkageStatis.start" :endVal="linkageStatis.stop" :duration="duration"></SyCount>
          </span>
        </li>
        <li class="home-statis-item">
          <span class="home-statis-icon delay-3s">
            <sy-icon name="patrol" />
          </span>
          <span class="home-statis-info">
            <label>累计巡视点位</label>
            <SyCount :startVal="patrolStatis.start" :endVal="patrolStatis.stop" :duration="duration"></SyCount>
          </span>
        </li>
      </ul>
    </el-col>

    <el-col class="sy-col-vertical" :span="6">
      <EventNotice />
      <PatrolStatis />
    </el-col>
  </el-row>
</template>

<script>
import { mapGetters } from 'vuex';
import { TaskStatus, MessageAction, ActivePatrolTypes, LinkagePatrolTypes } from '@/enums';

import SyCount from 'vue-count-to';
import SMap from '@/components/Map/SMap';
import BasicInfo from './components/BasicInfo';
import EventNotice from './components/EventNotice';
import DeviceStatis from './components/DeviceStatis';
import PatrolStatis from './components/PatrolStatis';

export default {
  name: 'Dashboard',
  components: {
    SMap,
    SyCount,
    BasicInfo,
    EventNotice,
    DeviceStatis,
    PatrolStatis,
  },
  computed: {
    ...mapGetters(['stationId']),
  },
  data() {
    return {
      duration: 3000,
      taskStatis: { start: 0, stop: 0 },
      patrolStatis: { start: 0, stop: 0 },
      linkageStatis: { start: 0, stop: 0 },
    };
  },
  created() {
    this.getTaskStatis();
    this.getPatrolStatis();
    this.getLinkageStatis();
  },
  mounted() {
    this.onStompPatrolPush();
  },
  methods: {
    onStompPatrolPush() {
      this.$bus.$on('stompPatrolPush', (body) => {
        const { action, data } = JSON.parse(body);
        if (!data || !data.length) return;
        // console.log('onStompPatrolPush: ', action, data);
        switch (action) {
          // 任务巡视记录
          case MessageAction.PATROL.TASK_ITEM:
            this.patrolStatis.start = this.patrolStatis.stop;
            this.patrolStatis.stop += data.length;
            break;
          // 任务巡视状态
          case MessageAction.PATROL.TASK_INDEX:
            data.forEach((item) => {
              if (item.task_status == TaskStatus.DOING && item.task_progress === 0) {
                this.taskStatis.start = this.taskStatis.stop;
                this.taskStatis.stop += 1;

                if (LinkagePatrolTypes.includes(item.patrol_type)) {
                  this.linkageStatis.start = this.linkageStatis.stop;
                  this.linkageStatis.stop += 1;
                }
              }
            });
            break;
          default:
            break;
        }
      });
      this.$once('hook:beforeDestroy', () => {
        this.$bus.$off('stompPatrolPush');
      });
    },
    // 任务统计数据
    getTaskStatis() {
      this.$api
        .postHistoryApi('query', {
          index: 'history.task',
          match: {
            station_id: this.stationId,
            patrol_type: ActivePatrolTypes,
          },
        })
        .then((res) => {
          // console.log('res: ', res);
          if (!res) return;
          this.taskStatis.stop = res.total;
        });
    },
    // 巡视统计数据
    getPatrolStatis() {
      this.$api
        .getHistoryApi('queryCount', {
          index: 'history.item',
          groupField: 'patrol_status',
          match: { station_id: this.stationId },
        })
        .then((res) => {
          if (!res?.data) return;
          this.patrolStatis.stop = res.data.reduce((acc, item) => (acc += item.count || 0), 0);
        });
    },
    // 联动统计数据
    getLinkageStatis() {
      this.$api
        .postHistoryApi('query', {
          index: 'history.task',
          match: {
            station_id: this.stationId,
            patrol_type: LinkagePatrolTypes,
          },
        })
        .then((res) => {
          if (!res) return;
          this.linkageStatis.stop = res.total;
        });
    },
  },
};
</script>

<style lang="scss">
#home {
  position: relative;
  width: 100%;
  height: 100%;

  .home-title {
    color: $text-regular;
    font-size: 20px;
    font-family: BigYoungBoldGB;
    text-shadow: 0 0 2px #fff;
  }
  .home-smap {
    position: relative;
    width: 100%;
    height: 70vh;
  }
  .home-statis {
    display: flex;
    align-items: center;
    justify-content: space-evenly;
    width: 100%;

    &-item {
      display: inline-flex;
      align-items: center;
      min-width: 260px;
      height: 130px;
      padding: 0 20px;
      background: {
        image: url(~@/assets/img/bg_statis.png);
        repeat: no-repeat;
        position: center;
        size: 100% 100%;
      }
    }
    &-info {
      display: inline-flex;
      flex-direction: column;

      > label {
        font-size: 14px;
        color: #b7cffc;
      }
      > span {
        max-width: 180px;
        margin-top: 1vh;
        color: #00cbff;
        font-size: 30px;
        font-weight: bold;
        @include text-ellipsis();
      }
    }
    &-icon {
      flex: none;
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;
      width: 60px;
      height: 60px;
      margin-right: 20px;

      > img {
        width: 40px;
        height: 40px;
      }
      .sy-icon {
        font-size: 24px;
        color: $color-primary;
      }

      &::after {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        animation: rotate 3s infinite alternate;
        background: {
          image: url(~@/assets/img/bg_circle.png);
          repeat: no-repeat;
          position: center;
          size: 100%;
        }
      }
      &.delay-1s::after {
        animation-delay: 1s;
      }
      &.delay-2s::after {
        animation-delay: 2s;
      }
      &.delay-3s::after {
        animation-delay: 3s;
      }
    }
  }
}
</style>
