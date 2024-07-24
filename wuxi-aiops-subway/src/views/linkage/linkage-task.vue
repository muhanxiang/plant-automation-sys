<template>
  <el-row class="sy-row" :gutter="10">
    <el-col class="sy-col" :span="6">
      <div class="task-aside sy-view">
        <div class="task-aside__header">任务列表</div>
        <div class="task-aside__wrapper">
          <el-collapse v-if="taskList.length > 0" class="task-aside__list sy-collapse" v-model="taskActive" accordion>
            <el-collapse-item
              class="task-aside__item"
              v-for="item in taskList"
              :key="item.guid"
              :name="item.guid"
              :title="item.name || '联动任务名称'"
            >
              <ul class="task-aside__item-info">
                <li>
                  联动信号：<span>{{ item.sign || '-' }}</span>
                </li>
                <li>
                  信号来源：<span>{{ item.source || '-' }}</span>
                </li>
                <li>
                  信号描述：<span>{{ item.content || '-' }}</span>
                </li>
                <li>
                  信号时间：<span>{{ item.time || '-' }}</span>
                </li>
              </ul>
            </el-collapse-item>
          </el-collapse>
          <sy-empty v-else description="暂无联动任务"></sy-empty>
        </div>
      </div>
    </el-col>

    <el-col class="sy-col" :span="18">
      <el-row class="task-main sy-row sy-view" :gutter="10">
        <el-col class="sy-col" :span="18">
          <div class="task-main__info">
            <div class="task-main__info-title">{{ taskData.name }}</div>
            <div class="task-main__info-wrap">
              <ul class="task-main__info-list">
                <li>
                  开始时间：<span>{{ taskData.start_time }}</span>
                </li>
                <li>
                  结束时间：<span>{{ taskData.end_time }}</span>
                </li>
                <li>
                  执行类型：<span>{{ taskData.task_type }}</span>
                </li>
                <li>
                  任务进度：
                  <sy-progress :percentage="taskData.progress" text-inside></sy-progress>
                </li>
              </ul>
              <ul class="task-main__info-statis">
                <li :span="12" v-for="(item, index) in taskData.statis" :key="index">
                  <label>{{ item.name }}</label>
                  <span>{{ item.value }}</span>
                </li>
              </ul>
            </div>
          </div>
          <ul class="task-main__list">
            <li class="task-main__record" v-for="(item, index) in taskRecords" :key="index">
              <ul class="task-main__record-info">
                <li>
                  巡检点位：<span>{{ item.patrolpoint || '-' }}</span>
                </li>
                <li>
                  巡检设备：<span>{{ item.device || '-' }}</span>
                </li>
                <li>
                  巡检时间：<span>{{ item.time || '-' }}</span>
                </li>
                <li>
                  巡检结果：<span>{{ item.result || '-' }}</span>
                </li>
              </ul>
              <img class="task-main__record-image" :src="item.image" alt="#" />
            </li>
          </ul>
        </el-col>
        <el-col class="sy-col" :span="6">
          <ul class="task-monitor">
            <li v-for="item in 3" :key="item">
              <img src="~@/assets/img/icon_camera_default.png" alt="#" />
              <span>暂无设备接入</span>
            </li>
          </ul>
        </el-col>
      </el-row>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: 'LinkageTask',
  data() {
    return {
      taskList: Array.from({ length: 10 }, (_, index) => ({ guid: `${++index}` })),
      taskActive: '',
      taskData: {
        name: '任务名称',
        task_type: '联动任务',
        start_time: '2021-06-07 12:22:34',
        end_time: '2021-06-07 12:22:34',
        progress: 60,
        statis: [
          { name: '总测点', value: 168 },
          { name: '已巡视', value: 33 },
          { name: '成功', value: 168 },
          { name: '告警', value: 15 },
          { name: '失败', value: 18 },
        ],
      },
      taskRecords: Array.from({ length: 10 }, (_, index) => ({
        guid: `${++index}`,
        patrolpoint: '巡视点位名称',
        device: '巡视设备名称',
        time: '巡视记录时间',
        result: '巡视记录结果',
        image: require('@/assets/img/img_meter.png'),
      })),
      taskActiveRight: ['1'],
      progressText: 0,
      dataRightTop: [],
      dataRightBottom: [],
    };
  },
  created() {
    this.getInitData();
  },
  methods: {
    async getInitData() {
      const res = await this.$api.getBaseApi('linkagetask');
      console.log('联动任务', res);
    },
  },
};
</script>

<style lang="scss" scoped>
.task-aside {
  width: 100%;
  height: 100%;
  padding: 10px;

  &__header {
    width: 100%;
    height: 3vh;
    padding-left: 20px;
    line-height: 3vh;
    font-size: 20px;
  }
  &__wrapper {
    width: 100%;
    height: 75vh;
    margin-top: 1vh;
    border: $border-default;
  }

  &__list {
    width: 100%;
    height: 100%;
    overflow-y: auto;
  }

  &__item {
    ::v-deep .el-collapse-item {
      &__header {
        width: 100%;
        height: 4vh;

        &:hover {
          color: $text-primary;
        }
      }
      &__content {
        padding: 10px;
      }
    }

    &.is-active {
      color: $text-primary;

      ::v-deep .el-collapse-item {
        &__header {
          color: $text-primary;
        }
        &__content {
          color: $text-primary;
        }
      }
    }
  }
}

.task-main {
  width: 100%;
  height: 100%;
  padding: 10px;

  &__info {
    border: $border-default;

    &-title {
      width: 100%;
      height: 3vh;
      padding-left: 20px;
      line-height: 3vh;
      font-size: 20px;
      border-bottom: $border-default;
    }
    &-wrap {
      padding: 10px;
    }
    &-list {
      display: flex;
      flex-flow: row wrap;
      width: 100%;

      > li {
        display: inline-flex;
        align-items: center;
        width: 50%;
        height: 5vh;
        padding: 0 10px;

        > span {
          font-size: 18px;
          color: $text-secondary;
        }
      }
      .el-progress {
        width: 300px;
      }
    }
    &-statis {
      display: flex;
      align-items: center;
      width: 100%;
      height: 8vh;
      border: $border-default;

      > li {
        flex: 1;
        display: inline-flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;

        span {
          margin-top: 1vh;
          font-size: 24px;
          color: $text-secondary;
        }
      }
    }
  }

  &__list {
    width: 100%;
    height: 55vh;
    margin-top: 1vh;
    overflow-y: auto;
    border: $border-default;
  }

  &__record {
    display: flex;
    align-items: center;
    width: 100%;
    height: 10vh;
    padding: 0 20px;
    border-bottom: $border-default;
    &:hover {
      color: $text-primary;
    }
    &-info {
      flex: auto;

      > li + li {
        margin-top: 5px;
      }
    }
    &-image {
      width: 200px;
      height: 8vh;
      object-fit: contain;

      &:hover {
        object-fit: cover;
      }
    }
  }
}

.task-monitor {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;

  > li {
    flex: 1;
    display: flex;
    align-items: center;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 100%;
    border: $border-default;

    > img {
      margin-bottom: 1vh;
    }
  }
}
</style>
