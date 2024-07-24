<template>
  <el-container class="sy-container sy-chromatogram">
    <el-aside width="auto">
      <div class="sy-view">
        <div class="sy-card">
          <div class="sy-card-body sy-card-body-left">
            <div class="sy-card-body-left-top">
              <div class="sy-form sy-form-search">
                <el-input v-model="input" placeholder="请输入关键词搜索" :maxlength="20" clearable></el-input>
                <span></span>
              </div>
              <ul>
                <li v-for="(item, index) in dataLeftTop" :key="index" class="sy-btn-regular">
                  <a>{{ item.name }}</a>
                </li>
              </ul>
            </div>
            <div class="sy-card-body-left-center sy-table">
              <el-table class="sy-table table-chrom" :data="tableData">
                <el-table-column prop="ser" label="序号" align="center" width="80"> </el-table-column>
                <el-table-column prop="name" label="设备名称" align="center"> </el-table-column>
              </el-table>
            </div>
          </div>
        </div>
      </div>
    </el-aside>
    <el-container class="container-chorm">
      <div class="sy-view">
        <div class="sy-card">
          <div class="sy-card-header">
            <div class="sy-title">实时数据</div>
          </div>
          <div class="sy-card-body sy-card-body-right">
            <el-table class="sy-table" v-loading="tableLoad" :data="ringhTableData" border>
              <el-table-column prop="index" label="序号" align="center" width="80"> </el-table-column>
              <el-table-column prop="name" label="名称" align="center"> </el-table-column>
              <el-table-column prop="oxygen" label="氧气" align="center"> </el-table-column>
              <el-table-column prop="monoxide" label="一氧化碳" align="center"> </el-table-column>
              <el-table-column prop="dioxide" label="二氧化碳" align="center"> </el-table-column>
              <el-table-column prop="methane" label="甲烷" align="center"> </el-table-column>
              <el-table-column prop="ethylene" label="乙烯" align="center"> </el-table-column>
              <el-table-column prop="acetylene" label="乙炔" align="center"> </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
      <div class="sy-view">
        <div class="sy-card">
          <div class="sy-card-header">
            <div class="sy-title">实时曲线</div>
          </div>
          <div class="sy-card-body sy-card-body-right sy-el-echarts">
            <OilChart />
          </div>
        </div>
      </div>
    </el-container>
  </el-container>
</template>

<script>
import OilChart from './components/OilChart';

export default {
  name: 'OilChroma',
  components: {
    OilChart,
  },
  data() {
    return {
      input: '',
      tableLoad: false,
      tableData: [],
      dataLeftTop: [],
      ringhTableData: [],
    };
  },
  created() {},
  mounted() {
    this.getDataInit(); // 初始化按钮数据
  },
  methods: {
    async getDataInit() {
      this.dataLeftTop = [{ name: '筛选' }, { name: '重置' }];
    },
  },
};
</script>

<style>
.table-chrom th.is-leaf,
.table-chrom td {
  border-bottom: 0;
}
</style>

<style lang="scss" scoped>
.sy-container {
  .sy-card {
    display: flex;
    flex: 1;
    color: #00cbff;
    flex-direction: column;

    &-header {
      position: relative;
      color: #00cbff;
      width: 100%;
      height: 3vh;
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
          padding-top: 15px;
        }
      }

      &-right {
        padding: 10px;

        .sy-el-table {
          height: 37vh;
          overflow: auto;
        }
      }
    }
  }
}

.container-chorm {
  flex-direction: column;

  .sy-view {
    &:last-child {
      margin-top: 10px;
    }

    &:first-child {
      margin-bottom: 10px;
    }
  }
}
</style>
