<template>
  <div class="patrol-statis" v-loading="loading">
    <header class="patrol-statis-header home-title">巡视统计</header>

    <div class="patrol-statis-task">
      <sy-chart v-if="taskData.length > 0" type="bar" :option="patrolTask"></sy-chart>
      <sy-empty v-else-if="!loading" description="暂无任务数据"></sy-empty>
    </div>
    <!-- <div class="patrol-statis-point">
      <sy-chart v-if="pointData.length > 0" type="pie" :option="patrolPoint"></sy-chart>
      <sy-empty v-else description="暂无巡视数据"></sy-empty>
    </div> -->
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { round, sumBy } from 'lodash';
import { arrToMap, toThousands } from '@/utils';
import { PatrolColors, LinearColors } from '@/enums/color';
import { PatrolStatus, TaskStatus, MessageAction } from '@/enums';
import Option from '@/enums/option';

export default {
  name: 'PatrolStatis',
  computed: {
    ...mapGetters(['stationId']),
    patrolTypeMap() {
      return arrToMap(Option.PatrolTypes);
    },
    patrolStateMap() {
      return { 0: '正常', 2: '预警' };
    },
    patrolTask() {
      let xData = []
      let seriesParam = {
        type: 'bar',
        barMaxWidth: 30,
        itemStyle: {
          color: LinearColors[0],
        },
        label: {
          show: true,
          color: '#fff',
          fontSize: 14,
          position: 'top',
        },
        data: []
      };
      this.taskData.forEach((item) => {
        const patrolType = this.patrolTypeMap[item.patrol_type];
        if (patrolType) {
          xData.push(patrolType)
          seriesParam.data.push(item.count);
        }
      });
      return {
        baseOption: {
          grid: {
            top: '10%',
          },
          xAxis: {
            data: xData,
            axisLabel: {
              interval: 0,
              fontSize: 14,
            },
          },
          legend: {
            show: false,
            left: 'auto',
            right: '3%',
            orient: 'horizontal',
          },
          tooltip: {
            formatter: (params) => {
              if (params[0]) {
                const { marker, name, value } = params[0];
                return `${marker} ${name}: ${value}`;
              } else {
                return '';
              }
            },
          },
          series: [seriesParam],
        },
        media: [
          {
            query: {
              maxWidth: 400,
            },
            option: {
              legend: {
                itemGap: 5,
                itemWidth: 8,
                itemHeight: 4,
                textStyle: {
                  fontSize: 10,
                },
              },
              xAxis: {
                axisLabel: {
                  fontSize: 10,
                },
              },
            },
          },
        ],
      };
    },
    patrolPoint() {
      let dataSource = [];
      const normal = this.pointData.find((item) => item.patrol_status === PatrolStatus.SUCCESS);
      if (normal) {
        const ratio = this.pointTotal > 0 ? round((normal.count / this.pointTotal) * 100, 2) : 0;
        dataSource = [
          {
            name: `正常 ${ratio.toFixed(1)}%`,
            value: normal.count || 0,
            label: '正常',
          },
          {
            name: `预警 ${(100 - ratio).toFixed(1)}%`,
            value: this.pointTotal ? this.pointTotal - normal.count : 0,
            label: '预警',
          },
        ];
      }
      return {
        baseOption: {
          color: PatrolColors,
          title: {
            top: '44%',
            left: '39%',
            textAlign: 'center',
            textVerticalAlign: 'middle',
            text: toThousands(this.pointTotal),
            textStyle: {
              fontSize: 32,
              fontFamily: 'DS-DIGIB',
            },
            subtext: '巡视点位总数',
            subtextStyle: {
              color: '#fff',
            },
            itemGap: 0,
          },
          legend: {
            right: '10%',
            itemWidth: 10,
            itemHeight: 10,
            textStyle: {
              fontSize: 14,
            },
          },
          tooltip: {
            formatter: (params) => {
              const { data, marker } = params;
              return `${marker} ${data.label}: ${data.value}`;
            },
          },
          dataset: {
            source: dataSource,
            // source: this.pointData.map((item) => {
            //   const status = this.patrolStateMap[item.patrol_status];
            //   const ratio = this.pointTotal > 0 ? round((item.count / this.pointTotal) * 100, 1) : 0;
            //   return {
            //     name: `${status} ${ratio}%`,
            //     value: item.count || 0,
            //     label: status,
            //   };
            // }),
          },
          series: [
            {
              type: 'pie',
              name: '巡视点位统计',
              center: ['40%', '50%'],
              radius: ['60%', '75%'],
              label: {
                show: true,
                color: '#fff',
                fontSize: 14,
              },
            },
          ],
        },
        media: [
          {
            query: {
              maxWidth: 400,
            },
            option: {
              legend: {
                right: '5%',
                itemWidth: 8,
                itemHeight: 8,
                textStyle: {
                  fontSize: 12,
                },
              },
              series: [
                {
                  label: {
                    fontSize: 12,
                  },
                },
              ],
            },
          },
        ],
      };
    },
  },
  data() {
    return {
      loading: false,
      taskTimer: null,
      taskIndex: -1,
      taskData: [],
      pointData: [],
      pointTotal: 0,
    };
  },
  created() {
    this.getPatrolStatisData();
  },
  mounted() {
    this.onStompPatrolPush();
  },
  methods: {
    onStompPatrolPush() {
      this.$bus.$on('stompPatrolPush', (body) => {
        const { action, data } = JSON.parse(body);
        if (!data || !data.length) return;

        switch (action) {
          // 任务巡视记录
          case MessageAction.PATROL.TASK_ITEM:
            this.pointTotal += data.length;
            break;
          // 任务巡视状态
          case MessageAction.PATROL.TASK_INDEX:
            {
              const taskItem = data[0];
              if (taskItem.task_status == TaskStatus.DOING && taskItem.task_progress === 0) {
                const index = this.patrolTask.baseOption.series.findIndex((serie) => serie.id == taskItem.patrol_type);
                if (index >= 0) {
                  const serie = this.patrolTask.baseOption.series[index];
                  serie.data.splice(index, 1, serie.data[index] + 1);
                }
              }
            }
            break;
          default:
            break;
        }
      });
      this.$once('hook:beforeDestroy', () => {
        this.$bus.$off('stompPatrolPush');
      });
    },
    getPatrolStatisData() {
      this.loading = true;
      Promise.all([
        this.$api.getHistoryApi('queryCount', {
          index: 'history.task',
          groupField: 'patrol_type',
          match: { station_id: this.stationId },
        }),
        this.$api.getHistoryApi('queryCount', {
          index: 'history.item',
          groupField: 'patrol_status',
          match: { station_id: this.stationId },
        }),
      ])
        .then(([res1, res2]) => {
          if (res1?.data) {
            this.taskData = res1.data.filter((item) => item.patrol_type < 100);
          }
          if (res2?.data) {
            this.pointData = res2.data.filter((item) => item.patrol_status !== PatrolStatus.DEFECTS);
            this.pointTotal = sumBy(this.pointData, 'count');
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
.patrol-statis {
  flex: auto;
  width: 100%;
  padding: 15px;
  background: url('~@/assets/img/bg_panel.png') no-repeat center/100% 100%;

  &-header {
    padding-left: 20px;
  }

  &-task {
    width: 100%;
    height: 100%;
  }

  &-point {
    width: 100%;
    height: 21vh;
    margin-top: 1vh;
  }
}
</style>
