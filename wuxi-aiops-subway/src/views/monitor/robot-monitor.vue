<template>
  <el-row id="robot_monitor" class="sy-row" :gutter="10">
    <el-col class="sy-col" :span="6">
      <div class="sy-view">
        <div class="sy-card">
          <div class="sy-card-body sy-card-body-left">
            <div class="sy-card-body-left-top">
              <div class="sy-form sy-form-search">
                <el-input placeholder="请输入关键词搜索" v-model="filterText" :maxlength="20" clearable></el-input>
              </div>
              <ul>
                <li v-for="(item, index) in dataLeftTop" :key="index" class="sy-btn-regular">
                  <a>{{ item.name }}</a>
                </li>
              </ul>
            </div>
            <div class="sy-card-body-left-center">
              <div class="sy-tree">
                <sy-tree
                  class="filter-tree"
                  ref="tree"
                  node-key="id"
                  :data="data"
                  :props="defaultProps"
                  :filter-node-method="filterNode"
                  show-checkbox
                >
                  <template v-slot="{ data, node }">
                    <div class="el-tree-node__custom">
                      <sy-icon v-if="data.icon" :name="data.icon" />
                      <span class="el-tree-node__label" :title="node.label">{{ node.label }}</span>
                      <i v-if="data.focus" class="el-tree-node__icon el-icon-star-off" />
                    </div>
                  </template>
                </sy-tree>
              </div>
            </div>
            <div class="sy-card-body-left-bottom">
              <el-row :gutter="10">
                <el-col :span="8" v-for="(item, index) in dataLeftBottom" :key="index">
                  <div :class="'slb-color ' + item.className">
                    <span>{{ item.name }}</span>
                    <p>{{ item.num }}</p>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </div>
      </div>
    </el-col>
    <el-col class="sy-col" :span="18">
      <div class="sy-view">
        <div class="sy-card">
          <div class="sy-card-header sy-card-header-right">
            <el-form class="sy-form sy-flex" ref="form" :model="form">
              <el-form-item label="筛选项1:">
                <el-select
                  popper-class="sy-popper"
                  class="sy-select__suffix"
                  v-model="form.region1"
                  placeholder="请选择"
                >
                  <el-option label="区域一" value="shanghai"></el-option>
                  <el-option label="区域二" value="beijing"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="筛选项2:">
                <el-select
                  popper-class="sy-popper"
                  class="sy-select__suffix"
                  v-model="form.region2"
                  placeholder="请选择"
                >
                  <el-option label="区域一" value="shanghai"></el-option>
                  <el-option label="区域二" value="beijing"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="筛选项3:">
                <el-select
                  popper-class="sy-popper"
                  class="sy-select__suffix"
                  v-model="form.region3"
                  placeholder="请选择"
                >
                  <el-option label="区域一" value="shanghai"></el-option>
                  <el-option label="区域二" value="beijing"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item class="el-form-item-last">
                <div>
                  <el-button class="sy-btn-regular" type="primary">筛选</el-button>
                  <el-button class="sy-btn-regular">重置</el-button>
                </div>
              </el-form-item>
            </el-form>
          </div>
          <div class="sy-card-body sy-card-body-right">
            <el-table class="sy-table" v-loading="tableLoad" :data="tableData" border>
              <sy-empty slot="empty" description="暂无机器人数据"></sy-empty>

              <el-table-column
                v-bind="column"
                v-for="column in tableColumns"
                :key="column.prop"
                align="center"
                show-overflow-tooltip
              >
                <template v-if="column.prop === 'operation'" v-slot="{ row }">
                  <el-button class="sy-btn-regular" @click="handleClick(row)" type="text" size="small">查看</el-button>
                  <el-button class="sy-btn-regular" type="text" size="small">编辑</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="sy-table-footer">
              <el-pagination
                class="sy-pagination"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="1"
                :page-size="10"
                :total="400"
                background
              >
              </el-pagination>
            </div>
          </div>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: 'RobotMonitor',
  computed: {
    tableColumns() {
      return [
        { type: 'index', label: '序号', width: 80 },
        { prop: 'station_name', label: '站所名称', minWidth: 100 },
        { prop: 'robot_name', label: '机器人名称', minWidth: 100 },
        { prop: 'robotStatus', label: '机器人状态' },
        { prop: 'operation', label: '人工操作', minWidth: 150 },
      ];
    },
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
  },
  data() {
    return {
      dataLeft: [],
      dataLeftTop: [],
      dataLeftBottom: [],
      activeName: ['1'],
      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: '',
      },
      filterText: '',
      data: [],
      defaultProps: {
        children: 'children',
        label: 'label',
      },
      tableLoad: false,
      tableData: [],
    };
  },
  created() {
    this.getDataInit();
  },
  mounted() {},
  methods: {
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    async getDataInit() {
      this.dataLeft = [{ name: '实时监控' }, { name: '视频场景' }, { name: '视频轮巡' }];
      this.dataLeftTop = [{ name: '筛选' }, { name: '重置' }];
      this.dataLeftBottom = [
        { name: '总数', className: 'slb-color-blue', num: 168 },
        { name: '在线', className: 'slb-color-green', num: 150 },
        { name: '离线', className: 'slb-color-red', num: 18 },
      ];
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
    },
  },
};
</script>

<style lang="scss" scoped>
.sy-container {
  .sy-card {
    display: flex;
    flex: 1;
    color: $text-primary;
    flex-direction: column;
    width: 100%;

    &-header {
      color: $text-primary;
      border-bottom: $border-default;
      height: 5.2vh;
      padding: 0 16px;
      width: 100%;

      ul {
        display: flex;
        flex: 1;
        height: 100%;
        align-items: center;
      }

      &-right {
        height: 11.6vh;
        border-bottom: $border-default;
        display: flex;
      }
    }

    &-body {
      display: flex;
      flex: 1;
      flex-direction: column;
      padding: 20px;

      &-left {
        display: flex;
        flex: 1;

        &-top {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .el-input {
            height: 3vh;
          }

          ul {
            li {
              margin-right: 6px;

              &:last-child {
                margin: 0;
              }
            }
          }
        }

        &-center {
          flex: 1;
          padding: 3vh 0;

          .el-collapse {
            border: 0;

            &-item {
              margin-bottom: 2vh;
              border: $border-default;

              &:last-child {
                margin: 0;
              }
            }
          }
        }

        &-bottom {
          height: 8vh;
          display: flex;
          justify-content: space-between;
          align-items: center;

          & > div {
            flex-grow: 1;
            height: 100%;

            & > div {
              height: 100%;

              & > div {
                height: 100%;
                display: flex;
                flex-direction: column;
                justify-content: center;
              }
            }

            span {
              font-size: 14px;
              font-weight: bold;
              padding: 0 10px 10px;
              display: block;
            }

            p {
              text-align: center;
              font-size: 30px;
              line-height: 30px;
              font-weight: bold;
              color: $text-regular;
            }
          }
        }
      }

      &-right {
        padding: 30px;

        ul {
          display: flex;
          flex: 1;
          flex-wrap: wrap;
          border-left: $border-default;
          border-top: $border-default;

          li {
            width: 50%;
            height: 50%;
            border-right: $border-default;
            border-bottom: $border-default;
            margin-right: -1px;
            margin-bottom: -1px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;

            img {
              width: 10vh;
              height: 7.2vh;
              margin-bottom: 2vh;
            }
            p {
              font-weight: bold;
              font-size: 1.8vh;
              color: $text-secondary;
            }

            & > div {
              width: 100%;
              height: 100%;
              background: {
                size: contain;
                position: center;
                repeat: no-repeat;
              }
            }
          }
        }
      }
    }
  }

  .box-card {
    flex: 1;
    box-shadow: initial;
    flex-direction: column;
    background-color: initial;
    border-radius: 0;
    border: 0;
  }
}
</style>
