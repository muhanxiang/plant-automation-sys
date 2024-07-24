<template>
  <FullCalendar
    ref="calendar"
    class="sy-calendar"
    v-loading="loading"
    first-day-of-week="7"
    :options="calendarOptions"
  />
</template>

<script>
import Option from '@/enums/option';
import { mapGetters } from 'vuex';
import { round, flattenDeep, uniqueId } from 'lodash';
import { arrToMap, ILLEGAL_DATETIME } from '@/utils';
import { TaskStatus, MessageAction, CustomPatrolTypes, NonTaskPatrolTypes, TaskType, TaskInterval } from '@/enums';

import '@fullcalendar/core/vdom';
import FullCalendar from '@fullcalendar/vue';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import zhCnLocale from '@fullcalendar/core/locales/zh-cn';

export default {
  name: 'CalendarView',
  components: {
    FullCalendar,
  },
  computed: {
    ...mapGetters(['stationId']),
    patrolTypeMap() {
      return arrToMap(Option.PatrolTypes);
    },
    taskTypeMap() {
      return arrToMap(Option.TaskTypes);
    },
    taskWeekMap() {
      return arrToMap(Option.TaskWeeks);
    },
    taskMonthMap() {
      return arrToMap(Option.TaskMonths);
    },
    taskStateMap() {
      return arrToMap(Option.TaskStates);
    },
    calendarOptions() {
      return {
        plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
        height: '100%', //日历高度
        width: '100%',
        firstDay: 0,
        schedulerLicenseKey: 'CC-Attribution-NonCommercial-NoDerivatives',
        headerToolbar: {
          left: 'dayGridMonth',
          right: 'prev title next',
        },
        eventOrder: '-start',
        nextDayThreshold: '23:59:59', // 此处默认值为00:00:00 会导致跨越到第二天
        dayHeaderFormat: { weekday: 'narrow' },
        slotDuration: '01:00:00',
        slotEventOverlap: false, // 相同时间段的多个日程视觉上是否允许重叠，默认true允许
        slotMinWidth: '60',
        dayMaxEventRows: 4,
        events: this.events,
        dayCellClassNames: ['text-class'],
        // 表格每日
        dayCellContent: (arg, h) => {
          const total = this.events.filter((e) => this.$dayjs(arg.date).isSame(e.start, 'day')).length;
          return h(
            'div',
            {
              class: 'flex flex-between width100',
            },
            [
              h(
                'span',
                {
                  class: 'day-num',
                },
                arg.date.getDate()
              ),
              h('span', {}, `总数：${total}`),
            ]
          );
        },
        // 事件渲染函数
        eventContent: (arg) => {
          let {
            title,
            classNames,
            extendedProps: { progress },
          } = arg.event;
          return {
            // style="${ this.viewType === 'month' ? `width: ${progress}%;height: 100%;` : `width: 100%;height: ${progress}%` }"
            html: `
                <div class="fc-event-content" title="${title}">${title}</div>
                <div class="fc-event-progress ${classNames.join(
                  ' '
                )}" style="${`width: ${progress}%;height: 100%;`}"></div>

              `,
          };
        },
        editable: false,
        droppable: false,
        handleWindowResize: true, //随浏览器窗口变化
        initialView: 'dayGridMonth', // 初始化插件显示
        locale: zhCnLocale, // 时区
        themeSystem: 'bootstrap',
        eventClick: this.handleEventClick, // 任务点击
        dateClick: this.handleDateClick, // 当用户单击日期或时间时,触发该回调，触发此回调，您必须加载interaction插件
        customButtons: {
          prev: {
            click: this.handlePrevClick,
          },
          next: {
            click: this.handleNextClick,
          },
          timeGridDay: {
            text: '日',
            click: this.handleTimelineDay,
          },
          dayGridMonth: {
            text: '月',
            click: this.handleDayGridMonth,
          },
        },
      };
    },
  },
  data() {
    return {
      calendarApi: null,
      loading: false,
      events: [],
      activeDate: this.$dayjs().format('YYYY-MM-DD'),
      preEl: null,
      viewType: 'month',
    };
  },
  mounted() {
    this.onStompPatrolPush();
    this.calendarApi = this.$refs.calendar.getApi();
    setTimeout(() => {
      this.getCalendarData();
    }, 500);
  },
  methods: {
    // 切换：日
    handleTimelineDay() {
      this.viewType = 'day';
      this.calendarApi.changeView('timeGridDay', this.$dayjs(this.activeDate).format('YYYY-MM-DD'));
    },
    // 切换：月
    handleDayGridMonth() {
      this.viewType = 'month';
      this.calendarApi.changeView('dayGridMonth', this.$dayjs(this.activeDate).format('YYYY-MM'));
    },
    handleNextClick() {
      this.calendarApi.next();
      this.activeDate = this.$dayjs(this.calendarApi.currentData.currentDate).format('YYYY-MM-DD');
      this.getCalendarData();
    },
    handlePrevClick() {
      this.calendarApi.prev();
      this.activeDate = this.$dayjs(this.calendarApi.currentData.currentDate).format('YYYY-MM-DD');
      this.getCalendarData();
    },
    handleEventClick(info) {
      const {
        event: {
          extendedProps: { execute_id, patrol_type, task_status },
          id,
        },
      } = info;
      this.$emit('toMonitor', { id, execute_id, patrol_type, task_status });
    },
    handleDateClick(info) {
      this.activeDate = info.dateStr;
      // 添加点击高亮效果
      if (this.preEl) {
        this.preEl.style.backgroundColor = '#042139';
      }

      info.dayEl.style.backgroundColor = 'rgba(3, 95, 169, 0.5)';
      this.preEl = info.dayEl;

      this.$emit('filterTasks');
      const curDate = this.$dayjs(this.calendarApi.currentData.currentDate);

      if (curDate.isBefore(this.activeDate, 'month')) {
        this.calendarApi.next();
        this.getCalendarData();
      } else if (curDate.isAfter(this.activeDate, 'month')) {
        this.calendarApi.prev();
        this.getCalendarData();
      }
    },
    getCalendarData() {
      let curDates = this.$dayjs(this.activeDate);
      let curMonthStart = curDates.startOf('month').format('YYYY-MM-DD HH:mm:ss');
      let curMonthEnd = curDates.endOf('month').format('YYYY-MM-DD HH:mm:ss');
      this.loading = true;

      Promise.all([
        // 定期执行任务
        this.$api.getBaseApi('task', {
          station: this.stationId,
          approved: 1,
          enabled: true,
          task_type: 1,
          start_time__range: `${curMonthStart},${curMonthEnd}`,
        }),
        // 周期间隔任务
        this.$api.getBaseApi('task', {
          station: this.stationId,
          approved: 1,
          enabled: true,
          task_type__in: '2,3',
        }),
        // 已执行任务
        this.$api.postHistoryApi('query', {
          from: 0,
          size: 1000,
          index: 'history.task',
          match: { station_id: this.stationId },
          mustnot: { patrol_type: CustomPatrolTypes },
          order: { task_start_time: 'desc' },
          range: { task_start_time: `${curMonthStart},${curMonthEnd}` },
        }),
      ])
        .then(([res1, res2, res3]) => {
          let allTasks = [];
          // 定时执行任务
          if (res1 && res1.results.length > 0) {
            allTasks.push(...res1.results.map((task) => this.convertTaskToEvent(task)));
          }
          // 周期间隔任务
          if (res2 && res2.results.length > 0) {
            res2.results.forEach((task) => {
              if (task.task_type === 3) {
                allTasks.push(...this.generateIntervalTask(task).map(($task) => this.convertTaskToEvent($task)));
              } else {
                allTasks.push(...this.generatePeriodicTask(task).map(($task) => this.convertTaskToEvent($task)));
              }
            });
          }
          // 已执行的任务
          if (res3.data && res3.data.length > 0) {
            allTasks.push(...res3.data.map((task) => this.convertTaskToEvent(task, true)));
          }

          this.events = Object.freeze(flattenDeep(allTasks));
          this.$emit('filterTasks'); // 侧边栏赋值
        })
        .catch(() => {
          this.$message({
            type: 'error',
            message: '服务器异常',
          });
        })
        .finally(() => {
          this.loading = false;
        });
    },
    // 巡视消息推送
    onStompPatrolPush() {
      this.$bus.$on('stompPatrolPush', (body) => {
        const { action, data } = JSON.parse(body);
        if (!data || !data.length) return;
        switch (action) {
          case MessageAction.PATROL.TASK_INDEX:
            {
              const dataItem = data[0];
              if (!dataItem || NonTaskPatrolTypes.includes(dataItem.patrol_type)) return;

              const index = this.events.findIndex((item) => item.execute_id === dataItem.execute_id);
              let tEvents = [...this.events];
              if (index !== -1) {
                const taskItem = this.events[index];
                const event = this.convertTaskToEvent({ ...taskItem, ...dataItem }, true);
                tEvents.splice(index, 1, event);
              } else {
                tEvents.unshift(this.convertTaskToEvent(dataItem, true));
              }
              this.events = Object.freeze(tEvents);
              this.$emit('filterTasks');
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
    // 转换任务为事件
    convertTaskToEvent(task, start = false) {
      const hasEndTime =
        ![TaskStatus.UNDO, TaskStatus.DOING].includes(task.task_status) &&
        !ILLEGAL_DATETIME.includes(task.task_end_time);

      const taskName = task.task_name || task.name || '-';
      const task_status = start ? task.task_status : TaskStatus.UNDO;
      const taskState =
        task_status === TaskStatus.UNDO && task.execute_id ? '排队中' : this.taskStateMap[task_status] || '-';
      const taskProgress = start ? Math.min(round((task.task_progress / task.patrolpoint_total) * 100), 100) : 0;

      let taskExcute = this.taskTypeMap[task.task_type];
      // console.log('taskExcute: ', taskExcute);
      switch (task.task_type) {
        case TaskType.CYCLE:
          if (task.cycle_week) {
            const cycleWeek = task.cycle_week
              .split(',')
              .map((key) => this.taskWeekMap[key])
              .join('/');
            taskExcute = `每周${cycleWeek}周期执行`;
          } else if (task.cycle_month) {
            const cycleMonth = task.cycle_month
              .split(',')
              .map((key) => this.taskMonthMap[key])
              .join('/');
            taskExcute = `每月${cycleMonth}周期执行`;
          }
          break;
        case TaskType.INTERVAL:
          if (task.interval_type === TaskInterval.DAY) {
            taskExcute = `间隔${task.interval_number}天执行`;
          } else if (task.interval_type === TaskInterval.HOUR) {
            taskExcute = `间隔${task.interval_number}小时执行`;
          }
          break;
        default:
          break;
      }
      return {
        id: task.id,
        execute_id: task.execute_id,
        progress: taskProgress || 0,
        patrol_type: task.patrol_type,
        task_id: task.task_id || task.guid,
        task_name: taskName,
        task_status: task_status,
        task_type: task.task_type,
        title: `${taskState}：${taskName}`,

        className: `status-${task_status}`,
        patrolType: this.patrolTypeMap[task.patrol_type] || '-',
        taskType: this.taskTypeMap[task.task_type] || '-',
        taskState: taskState,
        taskExcute: taskExcute,
        end: hasEndTime ? task.task_end_time : null,
        start: task.task_start_time || task.start_time,
      };
    },

    // 生成间隔任务
    generateIntervalTask(task, type = 'month') {
      const { interval_start_time, interval_end_time, interval_number, interval_execute_time } = task;
      let result = [];
      let curDate = this.$dayjs(this.activeDate);
      let startDate = interval_start_time ? this.$dayjs(interval_start_time) : curDate.startOf(type);
      let endDate = interval_end_time ? this.$dayjs(interval_end_time) : curDate.endOf(type);

      // 如果为小时按照天处理
      const interval = 'day';
      if (
        (interval_start_time && startDate.isAfter(curDate.endOf('month'), interval)) ||
        (interval_end_time && endDate.isBefore(curDate.startOf('month'), interval))
      )
        return result;

      const curMonth = curDate.month();
      while (startDate.isBefore(endDate)) {
        if (type === 'month' && startDate.month() === curMonth) {
          result.push({
            ...task,
            id: `${uniqueId('interval-')}-${task.guid}`,
            start_time: `${startDate.format('YYYY-MM-DD')} ${interval_execute_time}`,
            // end_time: startDate.add(interval_number, interval).format('YYYY-MM-DD'),
          });
        }
        startDate = startDate.add(interval_number, interval);
      }
      return result;
    },
    // 生成周期任务
    generatePeriodicTask(task, type = 'month') {
      const { cycle_start_time, cycle_end_time, cycle_month, cycle_week, cycle_execute_time } = task;
      let result = [];
      let curDate = this.$dayjs(this.activeDate);
      let startDate = cycle_start_time ? this.$dayjs(cycle_start_time) : curDate.startOf(type);
      let endDate = cycle_end_time ? this.$dayjs(cycle_end_time) : curDate.endOf(type);
      if (
        (cycle_start_time && startDate.isAfter(curDate.endOf('month'), 'day')) ||
        (cycle_end_time && endDate.isBefore(curDate.startOf('month'), 'day'))
      )
        return result;
      // 以月为周期
      if (cycle_month && cycle_month.length > 0) {
        const curDays = curDate.daysInMonth();
        const cycleDays = cycle_month.split(',').map(Number);
        for (const day of cycleDays) {
          // 当月天数大于设置天数
          if (day <= curDays) {
            result.push({
              ...task,
              id: `${uniqueId('cycle-')}-${task.guid}`,
              start_time: `${curDate.date(day).format('YYYY-MM-DD')} ${cycle_execute_time}`,
            });
          }
        }
        // 以周为周期
      } else if (cycle_week && cycle_week.length > 0) {
        const curDays = curDate.daysInMonth();
        const cycleDays = cycle_week.split(',').map(Number);
        for (let i = 1; i <= curDays; i++) {
          const day = curDate.date(i).day();
          if (cycleDays.includes(day)) {
            result.push({
              ...task,
              id: `${uniqueId('cycle-')}-${task.guid}`,
              start_time: `${curDate.date(i).format('YYYY-MM-DD')} ${cycle_execute_time}`,
            });
          }
        }
      }
      return result;
    },
  },
};
</script>

<style lang="scss" scoped>
$fc-day-bg: #071b38;
$box-shadow: #00a0e9;
$box-shadow3: rgba($box-shadow, 0.3);
$box-shadow5: rgba($box-shadow, 0.5);

.sy-calendar {
  width: 100%;
  height: 100%;
  border: $border-default;

  .task-line {
    height: calc(100% - 55px);
    padding: 10px 20px 20px;
  }
  .date-day-title {
    font-size: 22px;
    padding: 15px 20px;
    color: $text-regular;
    background-color: #042643;
  }
  .date-title-header {
    display: flex;
    align-items: center;
    .date-title {
      flex: 1;
      display: flex;
      color: $text-regular;
      font-size: 18px;
      align-items: center;

      .date-item-op {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 57px;
        height: 41px;
        margin-right: 2px;
        border-radius: 4px;
        border: $border-default;
        background-color: #032c4e;
      }
      .active_date_type {
        color: $text-secondary;
        background: #031f37;
      }
    }
  }

  ::v-deep th,
  ::v-deep td {
    position: static;
    border-color: $border-table;
  }

  ::v-deep .fc {
    &-event {
      color: $text-regular;

      &-main {
        width: 100%;
        height: 100%;
      }
      &-content {
        display: flex;
        align-items: center;
        width: 100%;
        height: 100%;
        min-height: 20px;
        padding-left: 3px;
        font-size: 12px;
        // white-space: normal;
        background-color: $bg-hover5;
        cursor: pointer;
      }
      &-progress {
        z-index: -1;
        position: absolute;
        top: 0;
        left: 0;

        &.status-0 {
          background-color: $color-info;
        }
        &.status-1 {
          background-color: $color-primary;
        }
        &.status-2 {
          background-color: $color-success;
        }
        &.status-3 {
          background-color: $color-warning;
        }
        &.status-4 {
          background-color: $color-serious;
        }
        &.status-5 {
          background-color: $color-danger;
        }
        &.status-6 {
          background-color: $color-main;
        }
      }
    }

    &-daygrid {
      &-day {
        height: 10vh;

        &:hover,
        &:focus {
          background-color: $bg-hover5 !important;
        }

        &-frame {
          display: flex;
          flex-direction: column;
          width: 100%;
          height: 100%;
        }
        &-events {
          width: 100%;
          // height: 9.5vh;
          overflow-y: auto;
        }
        &-number {
          width: 100%;
          // font-size: 12px;

          .day-num {
            font-size: 14px;
            font-weight: 600;
          }
        }
        &.fc-day {
          background-color: $bg-table;
        }
        &.fc-day-other {
          color: $text-primary;
          background-color: $bg-base;
        }
        &.fc-day-today {
          background-color: $bg-hover5 !important;
          box-shadow: inset 0 0 10px $text-shadow;
        }
      }
      &-event {
        // width: 100%;
        margin: 1px;
        padding: 0;
        background: $bg-view;
        border: 1px solid $border-base;

        &-harness {
          width: 100%;
          margin: 0 !important;
          overflow: hidden;
        }
      }
      &-bottom {
        margin-top: 5px;
      }
    }

    &-popover {
      display: flex;
      flex-direction: column;
      bottom: 10px;
      border: $border-default !important;
      background: $bg-popper !important;

      &-header {
        background: $bg-header;
      }
      &-body {
        overflow: auto;

        .fc-more-popover-misc {
          margin-bottom: 5px;
        }
      }
    }

    &-toolbar {
      width: 100%;
      padding: 10px;
      margin: 0 !important;
      background-color: $bg-base;

      .fc-icon {
        border: none !important;
      }
      &-title {
        font-size: 20px;
        color: $text-primary;
      }
      &-chunk {
        display: inline-flex;
        align-items: center;

        .fc-button {
          padding: 5px 15px;
          font-size: 16px;
          color: $text-placeholder;
          border: $border-default;
          box-shadow: none !important;
          background-color: $bg-base;

          &-primary {
            color: $text-primary;
          }
          &-active {
            color: $text-regular;
            background-color: $bg-hover;
          }
          &.fc-today-button {
            padding: 0 10px !important;
            font-size: 12px;
            line-height: 16px;
            border: 1px solid #00cbff;
            background-color: #0a5982 !important;
          }

          &.fc-today-button,
          &.fc-next-button,
          &.fc-prev-button {
            padding: 0;
            border: none;
            box-shadow: none !important;
            background-color: transparent;
          }

          .fc-icon {
            display: block;
          }
        }
      }
    }

    &-timegrid {
      &-col {
        position: relative;
        &.fc-day-today {
          background: #033a68 !important;
        }
      }
      &-slot {
        height: 4vh;
      }
      &-event {
        max-width: 500px;
        min-height: 25px;
        box-shadow: none;
        background: $bg-view;
        border: 1px solid $border-base;
      }
    }

    &-timeline {
      &-slot {
        height: 3vh;

        &-label {
          border: none;
        }
        &-frame {
          width: 100%;
          height: 100%;
        }
      }
      &-header-row {
        font-size: 14px;
        color: $text-secondary;
        border-top: $border-default;
        border-bottom: $border-default;
      }
    }

    &-scrollgrid {
      border: none;
      border-image: none;
    }

    &-col-header {
      border-top: $border-default;
      border-bottom: $border-default;

      &-cell {
        border: none;

        &-cushion {
          padding: 15px !important;
          font-size: 16px;
          color: $text-regular;
        }
      }
    }
  }
}
</style>
