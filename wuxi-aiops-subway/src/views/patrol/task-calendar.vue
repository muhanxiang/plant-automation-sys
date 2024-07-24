<template>
  <el-row id="task_calendar" class="sy-row" :gutter="10">
    <el-col class="sy-col" :span="19">
      <CalendarView
        ref="calendar"
        class="calendar-view"
        @filterTasks="handleFilterTask"
        @toMonitor="handleGoToMonitor"
      ></CalendarView>
    </el-col>
    <el-col class="sy-col" :span="5">
      <div class="calendar-aside">
        <el-form class="calendar-form" label-position="left" size="small">
          <el-form-item class="el-form-item__search" label="任务名称:">
            <el-input v-model="taskName" placeholder="请输入任务名称" clearable @change="handleFilterTask"></el-input>
            <el-button class="sy-btn-regular" type="primary" size="medium" @click="handleFilterTask">查询</el-button>
          </el-form-item>
          <el-form-item label="任务状态:">
            <el-checkbox
              class="el-form-item__detailbox"
              v-model="checkAll"
              label="全选"
              @change="handleCheckAll"
            ></el-checkbox>
            <el-checkbox-group class="el-form-item__group" v-model="taskState" @change="onTaskStatesChange">
              <el-checkbox class="el-form-item__detailbox" v-for="item in taskStates" :key="item.key" :label="item.key">
                {{ item.value }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-form>
        <div class="calendar-list" v-loading="taskLoad">
          <el-collapse v-if="taskList.length > 0" class="calendar-collapse" v-model="taskActive" accordion>
            <el-collapse-item v-for="(task, index) in taskList" :key="index" :name="index" :title="task.task_name">
              <ul class="el-collapse-item__list">
                <li>
                  <label class="task-label">任务名称：</label>
                  <span>{{ task.task_name }}</span>
                </li>
                <li>
                  <label class="task-label">任务状态：</label>
                  <span>{{ task.taskState }}</span>
                </li>
                <li>
                  <label class="task-label">巡视类型：</label>
                  <span>{{ task.patrolType }}</span>
                </li>
                <li>
                  <label class="task-label">执行方式：</label>
                  <span>{{ task.taskExcute }}</span>
                </li>
                <li>
                  <label class="task-label">开始时间：</label>
                  <span>{{ task.start | formatDateTime }}</span>
                </li>
                <li>
                  <label class="task-label">结束时间：</label>
                  <span>{{ task.end | formatDateTime }}</span>
                </li>
                <li>
                  <label class="task-label">任务进度：</label>
                  <sy-progress
                    class="el-collapse-item__progress"
                    :percentage="task.progress"
                    :stroke-width="10"
                  ></sy-progress>
                </li>
                <li class="el-collapse-item__detail" v-show="task.task_status !== taskStatus.UNDO">
                  <el-button type="text" @click="handleGoToMonitor(task)">
                    <span>查看详情</span>
                    <i class="el-icon-d-arrow-right"></i>
                  </el-button>
                </li>
              </ul>
            </el-collapse-item>
          </el-collapse>
          <sy-empty v-else description="暂无巡视任务"></sy-empty>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import { TaskStatus } from '@/enums';
import Option from '@/enums/option';
import CalendarView from './components/CalendarView';

export default {
  name: 'TaskCalendar',
  components: {
    CalendarView,
  },
  computed: {
    taskStatus() {
      return TaskStatus;
    },
    taskStates() {
      return Option.TaskStates;
    },
  },
  props: {
    percentage: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      checkAll: true,
      taskLoad: false,
      taskName: '',
      taskActive: '',
      taskList: [], // 激活的任务事件
      taskState: Option.TaskStates.map((item) => item.key),
    };
  },
  methods: {
    handleCheckAll(val) {
      if (val) {
        this.taskState = this.taskStates.map((item) => item.key);
      } else {
        this.taskState = [];
      }
      this.handleFilterTask();
    },
    handleFilterTask() {
      this.taskLoad = true;
      let events = this.$refs.calendar.events;
      let calendarDate = this.$refs.calendar.activeDate;
      this.taskList = events
        .filter((task) => {
          return (
            this.taskState.length > 0 &&
            this.taskState.includes(task.task_status) &&
            task.task_name &&
            task.task_name.includes(this.taskName) &&
            task.start &&
            task.start.includes(calendarDate)
          );
        })
        .sort((prev, next) => (this.$dayjs(prev.start).isSameOrBefore(next.start, 'second') ? 1 : -1));
      this.taskLoad = false;
    },
    // 前往任务详情
    handleGoToMonitor(task) {
      if (!task.execute_id) return;
      if ([TaskStatus.DOING, TaskStatus.PAUSE, TaskStatus.WAITING].includes(task.task_status)) {
        this.$router.push({
          path: '/patrol/task-monitor',
          query: { taskId: task.task_id, executeId: task.execute_id, patrolType: task.patrol_type },
        });
      } else {
        this.$router.push({
          path: '/events/patrol/patrol-details',
          query: { taskId: task.task_id, executeId: task.execute_id },
        });
      }
    },
    onTaskStatesChange(val) {
      this.taskState = val;
      this.checkAll = this.taskState.length === this.taskStates.length;
      this.handleFilterTask();
    },
  },
};
</script>

<style lang="scss" scoped>
#task_calendar {
  width: 100%;
  height: 100%;

  .calendar {
    &-main {
      width: 100%;
    }

    &-aside {
      display: flex;
      flex-direction: column;
      width: 100%;
      height: 100%;
      overflow: hidden;
      border: $border-default;
    }

    &-form {
      width: 100%;
      padding: 15px 15px 0;
      background-color: #032037;

      .el-form-item {
        margin-bottom: 1vh;

        &__label {
          width: 100px;
        }
        &__search {
          ::v-deep .el-form-item__content {
            display: flex;
            align-items: center;

            .el-input {
              flex: auto;
            }
            .el-button {
              margin-left: 10px;
            }
          }
        }
        &__group {
          display: flex;
          flex-flow: row wrap;
        }
        &__detailbox {
          margin-right: 10px;
          line-height: 30px;
        }
      }
    }

    &-list {
      flex: auto;
      width: 100%;
      padding: 10px;
      overflow-y: auto;
    }

    &-collapse {
      width: 100%;

      ::v-deep .el-collapse-item {
        + .el-collapse-item {
          margin-top: 5px;
        }
        &__content {
          padding: 15px;
        }
        &__list {
          width: 100%;

          li {
            width: 100%;
            display: flex;
            align-items: center;

            > label {
              width: 80px;
            }
          }
        }
        &__detail {
          justify-content: flex-end;
        }
        &__progress {
          flex: auto;
        }
      }
    }
  }
}
</style>
