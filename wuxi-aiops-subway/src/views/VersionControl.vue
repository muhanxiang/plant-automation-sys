<template>
    <div class="a">
        <div class="b">
              <img src="../assets/imgs/logo.png" style="position: absolute;top: 23%;left: 2%;">
              <span style="color: white;position: absolute;left:4%;top:7%;font-size: 16px;font-weight: 400;line-height: 48px;">国家电网</span>
              <span style="color: white;position: absolute;left:4%;top:20.5%;font-size: 12px;line-height: 48px;font-weight: 400;">STATE GRID</span>
              <hr style="position: absolute;top:50%;left: 4%; width:8.8%;"> 
              <span style="color: white;position: absolute;left:4%;top:38%;font-size: 15px;line-height: 48px;font-weight: 400;">国网江苏省电力有限公司</span>
            <span style="color: white;position: absolute;left:2.3%;top:66%;font-size:10px;transform: scale(0.7);">STATE GRID JIANGSU ELECTRIC POWER CO.,LTD</span>    
            <span style="color: white;position: absolute;left:14.3%;top:25%;font-size: 25px;font-weight: bold;line-height: 48px;">厂站自动化点表管理及信息核对系统</span>
            <span style="color: white;position: absolute;left: 18.6%;top:42%;font-weight:400;line-height: 48px;font-size: 5px;transform: scale(1.7);">STATION AUTOMATION POINT TABLE MANAGEMENT AND INFORMATION VERIFICATION SYSTEM</span>
              <!-- <span style="color: white;position: absolute;left:12.5%;top:18%;font-size: 22px;font-weight: bold;line-height: 48px;">国网江苏省调厂站自动化点表管理及信息核对系统</span>
              <span style="color: white;position: absolute;left: 10.2%;top:38%;font-weight:400;line-height: 48px;font-size: 5px;transform: scale(0.85);">STATION AUTOMATION POINT TABLE MANAGEMENT AND INFORMATION VERIFICATION SYSTEM</span> -->
              <el-button-group style="position: absolute;top: 71%;left: 39%;">
                  <el-button style="background-color: transparent;font-size: 18px;width: 88px;height: 30px;padding:1%;opacity: 0.4;box-shadow: 0 0 2px 2px #fff inset;"></el-button>
                  <el-button style="background-color: transparent;font-size: 18px;width: 88px;height: 30px;padding:1%;opacity: 0.4;box-shadow: 0 0 2px 2px #fff inset;"></el-button>
                  <el-button style="background-color: transparent;font-size: 18px;width: 88px;height: 30px;padding:1%;opacity: 0.4;box-shadow: 0 0 2px 2px #fff inset;"></el-button>
                  <el-button style="background-color: transparent;font-size: 18px;width: 88px;height: 30px;padding:1%;opacity: 0.4;box-shadow: 0 0 2px 2px #fff inset;"></el-button>
                  <el-button style="background-color: transparent;font-size: 18px;width: 88px;height: 30px;padding:1%;opacity: 0.4;box-shadow: 0 0 2px 2px #fff inset;"></el-button>
              </el-button-group>
              <el-button-group style="position: absolute;top: 71%;left: 39%;">
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;"  @click="home">首页</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="stationmanagement">校核管理</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="historyquery">校核查询</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="AlertMessage">告警信息</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;color: #33BBFF;box-shadow: 0 3px 0 0 #f8d568;">版本控制</el-button>
              </el-button-group>
              <img src="../assets/imgs/y1.png" style="position: absolute;left: 70%;top: 47%;">
              <span style="color: white;position: absolute;left: 72%;top: 41%;color:#00AAFF ;">{{username}}</span>
              <span style="color: white;position: absolute;left: 72%;top: 62%;">江苏电网-临时组织</span>
              <el-badge :value=alertCount :max="99" class="item" style="position: absolute;left: 85.5%;top: 37.2%;"></el-badge>
                <img src="../assets/imgs/y2.png" style="position: absolute;left: 85%;top: 52%;width: 22px;height: 25px;cursor: pointer;">
                <img src="../assets/imgs/y3.png" style="position: absolute;left: 88%;top: 52%;width: 22px;height: 25px;cursor: pointer;" >
              <img src="../assets/imgs/y5.png" style="position: absolute;left: 91%;top: 52%;width: 22px;height: 25px;cursor: pointer;" type="primary" @click="butClick" >
              <img src="../assets/imgs/y6.png" style="position: absolute;left: 94%;top: 52%;width: 22px;height: 25px;cursor: pointer;" @click="tuichu">
          </div>
          <img src="../assets/imgs/logo1.png" style="position: absolute;top: 32px;left: 36%;">

      <div style="position: absolute;left: 0.5%;top: 13%;width: 350px;height: 86.5%;opacity: 0.4;box-shadow: 0 0 3.5px 3.5px #fff inset;border-radius: 4px;"></div>
      <div class="tree1" style="position: absolute;left: 0.5%;top: 13%;width: 350px;height: 86.5%">
          <hr style="position: absolute;background-color: #F8B858;width: 4px;height: 22px;left: 15px;top: 7px;">
          <span style="position: absolute;font-size: 20px;color: #33BBFF;font-weight: bold;top: 8px;left: 30px;">变电站列表</span>

          <hr style="position: absolute;width: 347px;top: 38px;opacity: 0.5;">
          <el-input v-model="filterText1" style="position: absolute;top: 47px;width: 250px;left: 10px;"></el-input>
          <el-button style="position:absolute;top: 47px;left: 270px;background-color: transparent;color: white;border: 1px solid #2B7AB2;" @click.native="search">搜索</el-button>
          <hr style="position: absolute;width: 347px;top: 93px;opacity: 0.3;">
          <el-tree style="position: absolute;width: 340px;height: 28px;top: 108px;left: 5px;color: white;"
                  class="filter-tree"   
                  accordion  
                  :data="TreeData"
                  :props="defaultProps"
                  :filter-node-method="filterNode"
                  ref="tree"
                  @node-click="handleNodeClick"
                  >
          </el-tree>
      </div>
      <div style="position: absolute;left: 369px;top: 13%;width: 350px;height: 86.5%;opacity: 0.4;box-shadow: 0 0 3.5px 3.5px #fff inset;border-radius: 4px;"></div>
      <div class="tree1" style="position: absolute;left: 369px;top: 13%;width: 350px;height: 86.5%">
          <hr style="position: absolute;background-color: #F8B858;width: 4px;height: 22px;left: 15px;top: 7px;">
          <span style="position: absolute;font-size: 20px;color: #33BBFF;font-weight: bold;top: 8px;left: 30px;">版本号列表</span>

          <hr style="position: absolute;width: 347px;top: 38px;opacity: 0.5;">
          <el-input v-model="filterText2" style="position: absolute;top: 47px;width: 250px;left: 10px;"></el-input>
          <el-button style="position:absolute;top: 47px;left: 270px;background-color: transparent;color: white;border: 1px solid #2B7AB2;" @click.native="search">搜索</el-button>
          <hr style="position: absolute;width: 347px;top: 93px;opacity: 0.3;">
          <el-tree style="position: absolute;width: 340px;height: 28px;top: 108px;left: 5px;color: white;"
                  class="filter-tree"  
                  accordion      
                  :data="VersionData"
                  :props="defaultProps"
                  :filter-node-method="filterNode"
                  ref="tree"
                  @node-click="handleNodeClick"
                  >
          </el-tree>
      </div>

      <div style="position: absolute;left: 728px;top: 13%;width: 61.5%;height: 10.2%;opacity: 0.4;box-shadow: 0 0 3.5px 3.5px #fff inset;border-radius: 4px;"></div>
        <div class="xxl" style="position: absolute;left: 728px;top: 13%;width: 61.5%;height: 10.2%">
          <hr style="position: absolute;top:40.2%;left: 0%; width:99.8%;opacity: 0.5;">
          <hr style="position: absolute;background-color: #F8B858;width: 4px;height: 22px;left: 15px;top: 7px;">
          <span style="position: absolute;font-size: 20px;color: #33BBFF;font-weight: bold;top: 8px;left: 30px;">查询条件</span>
          <span style="position: absolute;font-size: 18px;top: 58%;left: 1.3%;">区域：</span>
          <span style="position: absolute;font-size: 18px;top: 58%;left: 23.8%;">变电站：</span>    
          <el-select v-model="value1" filterable placeholder="请选择" style="position: absolute;top: 48%;left:6%;height: 50%;width: 17%;" :popper-append-to-body="true" @change="changee">
                <el-option
                v-for="item in xuanxiang1"
                :key="item.value"
                :label="item.label"
                :value="item.label">
                </el-option>
            </el-select>
            <el-select v-model="station" filterable placeholder="请选择" style="position: absolute;top: 48%;left: 30%;height: 50%;width: 17%;" :popper-append-to-body="false">
                <el-option
                v-for="item in xuanxiang2"
                :key="item.value"
                :label="item.label"
                :value="item.label">
                </el-option>
            </el-select>
            <span style="position: absolute;color: white;top:59%;left: 48.3%;font-size: 17px;">选择执行时间：</span>
            <div class="block">
                <el-date-picker
                v-model="datevalue"
                type="daterange"
                range-separator="-"
                value-format="yyyy-MM-dd HH:mm:ss"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                style="position: absolute;top: 48%;left: 58.6%;height: 42%;"
                popper-class="date-style">
                </el-date-picker>
            </div>
            <el-button style="position: absolute;left: 90%;top: 49%;height: 40%;width: 7%; color: black;font-size: 16px;" @click="chaxun1">查询</el-button>
          </div>

          <div style="position: absolute;left: 37.9%;top: 24%;width: 61.6%;height: 75.4%;opacity: 0.4;box-shadow: 0 0 3.5px 3.5px #fff inset;border-radius: 4px;"></div>
          <div class="alltable" style="position: absolute;left: 37.9%;top: 24%;width: 61.6%;height: 75.4%;">
            <hr style="position: absolute;background-color: #F8B858;width: 4px;height: 22px;left: 15px;top: 8px;">
          <span style="position: absolute;font-size: 20px;color: #33BBFF;font-weight: bold;top: 8px;left: 30px;">历史版本</span>
          <hr style="position: absolute;top:5.5%;left: 0%; width:99.8%;opacity: 0.5;">
            <el-table
                :data="actAllVerData"
                height="85.8%"
                border
                :cell-style="d5000tab"
                :row-class-name="rowClassName"
                :header-cell-style="{background:'#1D8ED2',color:'white'}"
                :key="randomKey"
                style="width: 98.3%;left: 0.8%;top: 6.8%;">
                <el-table-column
                prop="no"
                label="序号"
                align="center"
                width="81">
                </el-table-column>
                <el-table-column
                prop="sta_name"
                label="变电站名称"
                align="center"
                width="360">
                </el-table-column>
                <el-table-column
                prop="version"
                label="版本"
                align="center"
                width="260">
                </el-table-column>
                <el-table-column
                prop="time"
                label="时间"
                align="center"
                width="300">
                </el-table-column>
                <el-table-column
                prop="state"
                label="状态"
                align="center"
                width="160">
                </el-table-column>
            </el-table>

            <div class="block">
              <el-pagination
                class="my-pagination"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange123"
                :current-page="currentPage4"
                background
                pager-count="5"
                :page-size="20"
                layout="total, prev, pager, next, jumper"
                :total="200"
                style="position:absolute;top:94.3%;left: 50.6%;">
              </el-pagination>
            </div>
          </div>
    </div>
</template>


<script>
import { mapGetters } from 'vuex';
import axios from 'axios';
export default {
  name: 'chart',
  computed: {
    ...mapGetters(['username']),
    },
  watch: {
  },
  data() {
      return {
        actAllVerData:[],
        value1: '',
        station: '',
        alertCount:'',
        filterText1:'',
        filterText2:'',
        TreeData:[],
        VersionData:[],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        gridData:[],
        tableData:[],
        type:false,
        randomKey:Math.random(),
        value: '',
        dialogVisiblexx: false,
      };
    },
    mounted () {
      this.TreeDate()
      this.VersionHis()
      this.AlertCount()
      this.VersionDate()
      this.timer = setInterval(() => {
        this.AlertCount()
      }, 5000);
    },
    methods: {
      home(){
        this.$router.push('/Home')
      },
      stationmanagement(){
        this.$router.push('/pointManagement')
      },
      AlertMessage(){
        this.$router.push('/alertManagement')
      },
      historyquery(){
      this.$router.push('/CheckQuery')
    },
    tuichu(){
      this.$confirm('确认退出登录?', {
            type: 'warning'
          }).then(() => {
            this.$store.dispatch('user/logout').then(() => {
              this.$router.replace('/login')
            })
          })
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    TreeDate(){
        var url = 'http://localhost:8090/Query/versiontree'
        return axios.get(url).then(ret => {
          this.TreeData=ret.data.data
          //console.log(this.TreeData[0].label)
        })
      },
    VersionDate(){
        var url = 'http://localhost:8090/Query/allversiontree'
        return axios.get(url).then(ret => {
          this.VersionData=ret.data.data
          //console.log(this.TreeData[0].label)
        })
    },
    butClick(){
				if(this.type){
					this.content = '全屏显示'
                    // 取消全屏
					if(document.exitFullscreen){
						document.exitFullscreen()
					}else if(document.webkitCancelFullScreen){
						document.webkitCancelFullScreen()
					}else if(document.mozCancelFullScreen){
						document.mozCancelFullScreen()
					}else if(document.msExitFulldcreen){
						document.msExitFulldcreen()
					}
					this.type = false
				}else{
					this.content = '取消全屏'
                    // 全屏
					var element = document.documentElement
					if(element.requestFullscreen){
						element.requestFullscreen()
					}else if(element.webkitRequestFullScreen){
						element.webkitRequestFullScreen()
					}else if(element.mozRequestFullScreen){
						element.mozRequestFullScreen()
					}else if(element.msRequestFulldcreen){
						element.msRequestFulldcreen()
					}
					this.type = true
				}
			},
    AlertCount(){
        var url = 'http://localhost:8090/Query/alertCount'
        return axios.get(url).then(ret => {
          //console.log(ret.data.count)
          this.alertCount=ret.data.count
        })
      },
    VersionHis(){
      this.actAllVerData=[]
      var url = 'http://localhost:8090/Query/allversionhis'
      return axios.get(url).then(ret => {
            let dd = []
            dd = ret.data.data
            console.log(dd)
            for(let i=0;i<dd.length;i++){
              this.actAllVerData[i]={
                no:i+1,
                sta_name:dd[i].station,
                version:dd[i].version,
                time:dd[i].tttime,
                state:dd[i].state,
              }
            }
            console.log(this.actAllVerData)
            this.randomKey = Math.random()
        })
    },
  }
};
</script>

<style scoped>
.a{
    position: absolute;
    height: 100%;
    width: 100%;
    background: url(../assets/imgs/首页背景.jpg) no-repeat;
    background-size: 100%;
}
.b{
    position: absolute;
    height: 106px;
    width: 100%;
    border: 1px;
}
.b .el-button{
  border-radius: inherit;
}

.item {
  margin-top: 10px;
  margin-right: 40px;
}
.alltable /deep/ .el-table, 
.alltable /deep/ .el-table__expanded-cell{
  background-color: transparent;
}
.alltable /deep/ .el-table tr,
.alltable /deep/ .el-table td {
  background-color: transparent;
  color:white;
  padding:3px;
  height: 47.5px;
}
.alltable /deep/ .el-table tbody tr:hover>td { 
    background-color:transparent;
}
.alltable >>> .el-table__row>td{
  border-color:rgba(255, 255, 255, 0.4);
}
.alltable .el-table{
    border: 0.2px solid rgba(255, 255, 255, 0.4);
}
.alltable /deep/ .el-table::before {
	height: 0;
}
.alltable /deep/ .el-table--border::after {
	height: 0;
}
.tree1 /deep/ .el-tree {
  background: none;
}
.tree1 /deep/ .el-tree-node__content:hover {
  background-color: #66b1ff87;
}
.tree1 /deep/ .el-tree-node__label {
  font-size: 16px;
}
.tree1 /deep/ .el-tree-node:focus > .el-tree-node__content {
  background-color: #66b1ff87 !important;
}
/deep/ .el-pagination__total {
        color: #F5FAFF;
}
/deep/ .el-pagination__jump {
        color: #F5FAFF;
}

::v-deep .tree1 .el-input__inner {
  background-color: transparent;
  border-color: #2B7AB2;
  color: white;
}


</style>