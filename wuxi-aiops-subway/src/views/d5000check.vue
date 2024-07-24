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
              <el-button-group style="position: absolute;top: 71%;left: 39%;">
                  <el-button style="background-color: transparent;font-size: 18px;width: 88px;height: 30px;padding:1%;opacity: 0.4;box-shadow: 0 0 2px 2px #fff inset;"></el-button>
                  <el-button style="background-color: transparent;font-size: 18px;width: 88px;height: 30px;padding:1%;opacity: 0.4;box-shadow: 0 0 2px 2px #fff inset;"></el-button>
                  <el-button style="background-color: transparent;font-size: 18px;width: 88px;height: 30px;padding:1%;opacity: 0.4;box-shadow: 0 0 2px 2px #fff inset;"></el-button>
                  <el-button style="background-color: transparent;font-size: 18px;width: 88px;height: 30px;padding:1%;opacity: 0.4;box-shadow: 0 0 2px 2px #fff inset;"></el-button>
                  <el-button style="background-color: transparent;font-size: 18px;width: 88px;height: 30px;padding:1%;opacity: 0.4;box-shadow: 0 0 2px 2px #fff inset;"></el-button>
              </el-button-group>
              <el-button-group style="position: absolute;top: 71%;left: 39%;">
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="home">首页</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;color: #33BBFF;box-shadow: 0 3px 0 0 #f8d568;" >校核管理</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="historyquery">校核查询</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="AlertMessage">告警信息</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="VersionControl">版本控制</el-button>
              </el-button-group>
              <img src="../assets/imgs/y1.png" style="position: absolute;left: 70%;top: 47%;">
              <span style="color: white;position: absolute;left: 72%;top: 41%;color:#00AAFF ;">{{username}}</span>
              <span style="color: white;position: absolute;left: 72%;top: 62%;">江苏电网-临时组织</span>
              <el-badge :value=alertCount :max="99" class="item" style="position: absolute;left: 85.5%;top: 46.2%;"></el-badge>
              <img src="../assets/imgs/y2.png" style="position: absolute;left: 85%;top: 52%;width: 22px;height: 25px;cursor: pointer;">
              <img src="../assets/imgs/y3.png" style="position: absolute;left: 88%;top: 52%;width: 22px;height: 25px;cursor: pointer;">
              <img src="../assets/imgs/y5.png" style="position: absolute;left: 91%;top: 52%;width: 22px;height: 25px;cursor: pointer;" type="primary" @click="butClick">
              <img src="../assets/imgs/y6.png" style="position: absolute;left: 94%;top: 52%;width: 22px;height: 25px;cursor: pointer;" @click="tuichu">
          </div>
          <img src="../assets/imgs/logo1.png" style="position: absolute;top: 32px;left: 36%;">
  
            <div id="ixxll" class="xxll" style="position: absolute;left: 0.5%;top: 13%;width: 99%;height: 12%;opacity: 0.4;box-shadow: 0 0 3.5px 3.5px #fff inset;border-radius: 4px;"></div>
            <div id="ixxl" class="xxl" style="position: absolute;left: 0.5%;top: 13%;width: 99%;height: 12%">
              <hr style="position: absolute;top:35%;left: 0%; width:99.8%;opacity: 0.5;">
              <hr style="position: absolute;background-color: #F8B858;width: 4px;height: 22px;left: 15px;top: 7px;">
              <span style="position: absolute;font-size: 20px;color: #33BBFF;font-weight: bold;top: 8px;left: 30px;">D5000规范校核</span>
              <el-button style="position: absolute;left: 23.5%;top: 46%;height: 40%;width: 6.5%;color: white;background-color: #33BBFF;opacity: 0.2;" ></el-button>
              <el-button style="position: absolute;left: 30%;top: 46%;height: 40%;width: 6.5%;color: white;background-color: #33BBFF;opacity: 0.2;" ></el-button>
              <el-button style="position: absolute;left: 60%;top: 46%;height: 40%;width: 7.5%;color: white;background-color: #33BBFF;opacity: 0.2;" ></el-button>
              <el-button style="position: absolute;left: 23%;top: 46%;height: 40%;width: 6.5%; color: black;font-size: 16px;color: white;background-color: transparent;" @click="dialogTableVisible = true">典型表目录</el-button>
              <el-button style="position: absolute;left: 30%;top: 46%;height: 40%;width: 6.5%; color: black;font-size: 16px;color: white;background-color: transparent;" @click="chaxun">典型表管理</el-button>
              <el-button style="position: absolute;left: 60%;top: 46%;height: 40%;width: 7.5%; color: black;font-size: 16px;color: white;background-color: transparent;" @click="stationmanagement">校核类型切换</el-button>
            </div>
  
            <div id="iztimee" class="ztimee" style="position: absolute;left: 0.5%;top: 25.5%;width: 34%;height: 74%;opacity: 0.4;box-shadow: 0 0 3.5px 3.5px #fff inset;border-radius: 4px;"></div>
            <div id="iztime" class="ztime" style="position: absolute;left: 0.5%;top: 25.5%;width: 34%;height: 74%;">
            <hr style="position: absolute;background-color: #F8B858;width: 4px;height: 22px;left: 15px;top: 8px;">
            <span style="position: absolute;font-size: 20px;color: #33BBFF;font-weight: bold;top: 8px;left: 30px;">校核记录</span>
            <hr style="position: absolute;top:6.8%;left: 0%; width:99.4%;opacity: 0.5;">
              <el-table
                  highlight-current-row
                  @current-change="handleCurrentChange"
                  :data="timeData"
                  height="90%"
                  border
                  :key="randomKey"
                  @cell-click="cellClick"
                  :cell-style="d5000pab"
                  :header-cell-style="{background:'#1D8ED2',color:'white'}"
                  style="width: 97%;left: 1.3%;top: 8%;">
                  <el-table-column
                  prop="name"
                  label="名称"
                  width="320"
                  align="center">
                  <!-- <template slot-scope="scope">
                      <div @click="handleCellClick(scope.row)">
                        {{ scope.row.time}}
                      </div>
                    </template> -->
                  </el-table-column>
                  <el-table-column
                  prop="time"
                  label="日期"
                  width="320"
                  align="center">
                  </el-table-column>
              </el-table>
            </div>
  
            <div id="ialltablee" class="alltablee" style="position: absolute;left: 34.9%;top: 25.5%;width: 64.6%;height: 74%;opacity: 0.4;box-shadow: 0 0 3.5px 3.5px #fff inset;border-radius: 4px;"></div>
            <div id="ialltable" class="alltable" style="position: absolute;left: 34.9%;top: 25.5%;width: 64.6%;height: 74%;">
              <hr style="position: absolute;background-color: #F8B858;width: 4px;height: 22px;left: 15px;top: 8px;">
            <span style="position: absolute;font-size: 20px;color: #33BBFF;font-weight: bold;top: 8px;left: 30px;">校核异常详情</span>
            <hr style="position: absolute;top:6.8%;left: 0%; width:99.8%;opacity: 0.5;">
              <el-table
                  :data="actAllTableData"
                  height="84%"
                  border
                  :cell-style="d5000tab"
                  :row-class-name="rowClassName"
                  :header-cell-style="{background:'#1D8ED2',color:'white'}"
                  :key="ResultRandomKey"
                  style="width: 98.3%;left: 0.8%;top: 8.2%;">
                  <el-table-column
                  prop="id"
                  label="序号"
                  align="center"
                  width="85">
                  </el-table-column>
                  <el-table-column
                  prop="sta_name"
                  label="变电站名称"
                  align="center"
                  width="200">
                  </el-table-column>
                  <el-table-column
                  prop="d5000_name"
                  label="D5000校核文件名称"
                  align="center"
                  width="382">
                  </el-table-column>
                  <el-table-column
                  prop="result"
                  label="校核结果"
                  align="center"
                  width="100">
                  </el-table-column>
                  <el-table-column
                  prop="reason"
                  label="异常详情"
                  align="center"
                  width="100">
                  </el-table-column>
                  <el-table-column
                  prop="time"
                  label="校核时间"
                  align="center"
                  width="350">
                  </el-table-column>
              </el-table>
  
              <div class="block">
                <el-pagination
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange123"
                  :current-page="currentPage4"
                  :page-sizes="[100, 200, 300, 400]"
                  :page-size="100"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="400"
                  style="position:absolute;top:94%;left: 56%;">
                </el-pagination>
              </div>
            </div>

            <el-dialog title="典型表目录" :visible.sync="dialogTableVisible">
              <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane label="遥信" name="first"></el-tab-pane>
                <el-tab-pane label="遥测" name="second"></el-tab-pane>
                <el-tab-pane label="遥控" name="third"></el-tab-pane>
              </el-tabs>
              <el-table :data="gridData" :header-cell-style="{background:'#1D8ED2',color:'white'}" height="500">
                <el-table-column property="dev" label="设备" width="150" align="center"></el-table-column>
                <el-table-column property="type" label="数据类型" width="200" align="center"></el-table-column>
                <el-table-column property="name" label="信号名称" align="center"></el-table-column>
              </el-table>

            </el-dialog>
    </div>
  </template>
  
  <script>
  import { mapGetters } from 'vuex';
  import axios from 'axios';
  export default {
    computed: {
      ...mapGetters(['username']),
    },
    watch: {
    },
    data() {
      return {
        type:false,
        activeName: 'first',
        dialogTableVisible: false,
        alertCount:'',
        count: 1,
        station:'',
        randomKey:Math.random(),
        filterText:'',
        dialogFormVisible: false,
        timeData:[{
          name:'艾塘变-备调104通道1直收',
          time:'2023-07-19 17:26:05'
        },
        {
          name:'常州电厂-备调104通道1直收',
          time:'2023-07-19 17:03:51'
        }
        ],
        actAllTableData:[
          {
            id:'1',
            sta_name:'斗山',
            d5000_name:'斗山变-备调104通道1直收',
            result:'异常',
            reason:'查看更多',
            time:'2023-07-19 17:27:15'
          },
          {
            id:'2',
            sta_name:'九龙口',
            d5000_name:'华东.九龙口-备调104通道1直收',
            result:'异常',
            reason:'查看更多',
            time:'2023-07-19 17:35:53'
          },
          {
            id:'3',
            sta_name:'华能启东风电',
            d5000_name:'华启风电-备调104通道1直收',
            result:'异常',
            reason:'查看更多',
            time:'2023-07-19 17:05:29'
          },
        ],
        gridData: [{
          dev: '机组',
          type: '遥信',
          name: '键相脉冲'
        }, {
          dev: '线路',
          type: '遥信',
          name: '调频功率（火电）'
        }, {
          dev: '母线',
          type: '遥信',
          name: '母线三相电压'
        }, {
          dev: '机组',
          type: '遥测',
          name: '一次调频投入/退出信号'
        }],
        gridData0: [{
          dev: '机组',
          type: '遥信',
          name: '键相脉冲'
        }, {
          dev: '线路',
          type: '遥信',
          name: '调频功率（火电）'
        }, {
          dev: '母线',
          type: '遥信',
          name: '母线三相电压'
        }, {
          dev: '机组',
          type: '遥测',
          name: '一次调频投入/退出信号'
        }],
        gridData1: [{
          dev: '机组',
          type: '遥测',
          name: '键相脉冲'
        }, {
          dev: '线路',
          type: '遥测',
          name: '调频功率（火电）'
        }],
        gridData2: [{
          dev: '机组',
          type: '遥控',
          name: '键相脉冲'
        }],
          form: {
            name: '',
            region: '',
            date1: '',
            date2: '',
            delivery: false,
            type: [],
            resource: '',
            desc: ''
          },
          formLabelWidth: '120px',
          defaultProps: {
            children: 'children',
            label: 'label'
          },
          TreeData:[],
      };
    },
    mounted () {
      this.TreeDate()
      this.AlertCount()
        this.timer = setInterval(() => {
          this.AlertCount()
        }, 5000);
    },
    methods: {
      handleClick(tab) {
        //console.log(tab.index);
        if(tab.index==0){
          this.gridData=this.gridData0
        }
        if(tab.index==1){
          this.gridData=this.gridData1
        }
        if(tab.index==2){
          this.gridData=this.gridData2
        }
      },
      home(){
        this.$router.push('/Test1')
      },
      stationmanagement(){
        this.$router.push('/StationManagement')
      },
      historyquery(){
        this.$router.push('/HistoryQuery')
      },
      AlertMessage(){
          this.$router.push('/AlertMessage')
        },
        VersionControl(){
          this.$router.push('/VersionControl')
        },
      search(){
        this.$refs.tree.filter(this.filterText);
      },
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      dyydtab({rowIndex}){
        if (rowIndex %2!=0) {
            //return "background:#1C649F; color:white;";
            return 'background:rgba(28, 100, 159, 0.4); color:white';
        }
      },
      drydtab({rowIndex}){
        if (rowIndex %2!=0) {
            //return "background:#1C649F; color:white;";
            return 'background:rgba(28, 100, 159, 0.4); color:white;';
        }
      },
      d5000tab({rowIndex}){
        if (rowIndex %2!=0) {
            //return "background:#1C649F; color:white;";
            return 'background:rgba(28, 100, 159, 0.4); color:white;';
        }
      },
      tuichu(){
        this.$confirm('确认退出登录?', {
              type: 'warning',
            }).then(() => {
              this.$store.dispatch('user/logout').then(() => {
                this.$router.replace('/login');
              });
            });
      },
      AlertCount(){
          var url = 'http://localhost:8090/Query/alertCount'
          return axios.get(url).then(ret => {
            //console.log(ret.data.count)
            this.alertCount=ret.data.count
          })
        },
      yd2yaocefun(e){
        this.yaoyd2finall=[]
        if(e.target.tagName != 'INPUT'){
          return
        }
        for(let i=0;i<this.yaoce2.length;i++){
          this.yaoyd2finall[i]={
            num:this.yaoce2[i].num,
            name:this.yaoce2[i].name,
            address:this.yaoce2[i].address
          }
          this.randomKey = Math.random()
        }
      },
      butClick(){
                  if(this.type){
                      this.content = '全屏显示';
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
                      this.content = '取消全屏';
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
        handleNodeClick(node){
          // console.log(node.label)
          // console.log(node.plabel)
          this.radio1='遥信'
          if(node.plabel!=null){
            this.yd1time=node.label,
            this.yd1station=node.plabel
            this.yd1yaoxinfun()
          }
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
  
  .yddy /deep/ .el-table, 
  .yddy /deep/ .el-table__expanded-cell{
    background-color: transparent;
  }
  .yddy /deep/ .el-table tr,
  .yddy /deep/ .el-table td {
    background-color: transparent;
    color:white;
    padding:3px
  }
   
  .yddy >>> .el-table__row>td{
    /* 去除表格线 */
    border: none;
  }
  .yddy >>> .el-table th.is-leaf {
    /* 去除上边框 */
      border: none;
  }
  .yddy >>> .el-table::before{
    /* 去除下边框 */
    height: 0;
  }
  .yddy .el-table--border::after,.yddy .el-table--group::after{
       width: 0;
     }
  .yddy .el-table--border, .el-table--group{
       border: none;
     }
  
  .yddy /deep/ .el-table tbody tr:hover>td { background-color:transparent }
  
  
  .yddy  /deep/ .el-table__body-wrapper::-webkit-scrollbar {
      width: 6px; 
      height: 6px; 
    }
  
    .yddy /deep/ .el-table__body-wrapper::-webkit-scrollbar-thumb {
      background-color: #33BBFF;
      border-radius: 3px;
    }
  
  .yddr /deep/ .el-table, 
  .yddr /deep/ .el-table__expanded-cell{
    background-color: transparent;
  }
  .yddr /deep/ .el-table tr,
  .yddr /deep/ .el-table td {
    background-color: transparent;
    color:white;
    padding:3px
  }
   
  .yddr >>> .el-table__row>td{
    /* 去除表格线 */
    border: none;
  }
  .yddr >>> .el-table th.is-leaf {
    /* 去除上边框 */
      border: none;
  }
  .yddr >>> .el-table::before{
    /* 去除下边框 */
    height: 0;
  }
  .yddr .el-table--border::after,.yddy .el-table--group::after{
       width: 0;
     }
  .yddr .el-table--border, .el-table--group{
       border: none;
     }
  
  .yddr /deep/ .el-table tbody tr:hover>td { background-color:transparent }
  
  
  .yddr  /deep/ .el-table__body-wrapper::-webkit-scrollbar {
      width: 6px; 
      height: 6px; 
    }
  
  .yddr /deep/ .el-table__body-wrapper::-webkit-scrollbar-thumb {
      background-color: #33BBFF;
      border-radius: 3px;
  }
    ::v-deep .tree1  .el-input__inner {
    background-color: transparent;
    border-color: #2B7AB2;
    color: white;
  }
  
  .d5000 /deep/ .el-table, 
  .d5000 /deep/ .el-table__expanded-cell{
    background-color: transparent;
  }
  .d5000 /deep/ .el-table tr,
  .d5000 /deep/ .el-table td {
    background-color: transparent;
    color:white;
    padding:3px
  }
   
  .d5000 >>> .el-table__row>td{
    /* 去除表格线 */
    border: none;
  }
  .d5000 >>> .el-table th.is-leaf {
    /* 去除上边框 */
      border: none;
  }
  .d5000 >>> .el-table::before{
    /* 去除下边框 */
    height: 0;
  }
  .d5000 .el-table--border::after,.yddy .el-table--group::after{
       width: 0;
     }
  .d5000 .el-table--border, .el-table--group{
       border: none;
     }
  
  .d5000 /deep/ .el-table tbody tr:hover>td { background-color:transparent }
  
  
  .d5000  /deep/ .el-table__body-wrapper::-webkit-scrollbar {
      width: 6px; 
      height: 6px; 
    }
  
  .d5000 /deep/ .el-table__body-wrapper::-webkit-scrollbar-thumb {
      background-color: #33BBFF;
      border-radius: 3px;
  }
  
  .yddy /deep/ .el-radio-button__inner {
    color:white;
    background-color:transparent;
  }
  .yddy /deep/ .el-radio-button__orig-radio:checked + .el-radio-button__inner {
        background-color:#2B7AB2;
        opacity: 0.9;
  }
  .yddr /deep/ .el-radio-button__inner {
    color:white;
    background-color:transparent;
  }
  .yddr /deep/ .el-radio-button__orig-radio:checked + .el-radio-button__inner {
        background-color:#2B7AB2;
        opacity: 0.9;
  }
  .d5000 /deep/ .el-radio-button__inner {
    color:white;
    background-color:transparent;
  }
  .d5000 /deep/ .el-radio-button__orig-radio:checked + .el-radio-button__inner {
        background-color:#2B7AB2;
        opacity: 0.9;
  }
  
  .dialog /deep/ .el-dialog__title {
    font-size: 20px;
    font-weight: bold;
    color: black;
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
.alltable /deep/ .el-table__body-wrapper::-webkit-scrollbar{
    width: 8px;
}
.ztime /deep/ .el-table, 
.ztime /deep/ .el-table tr,
.ztime /deep/ .el-table td {
  background-color: transparent;
  color:white;
  padding:3px;
  height: 51.5px;
}
.ztime /deep/ .el-table tbody tr:hover>td { 
    background-color:#2B7AB2;
}
.ztime /deep/ .el-table__body-wrapper::-webkit-scrollbar-thumb {
    border-radius: 3px;
}
.ztime /deep/ .el-table__body-wrapper::-webkit-scrollbar{
    width: 8px;
}
.ztime >>> .el-table th.is-leaf {
  /* 去除上边框 */
    border: none;
}
.ztime >>> .el-table::before{
  /* 去除下边框 */
  height: 0;
}
.ztime .el-table--border::after,.yddy .el-table--group::after{
     width: 0;
}

.ztime .el-table{
    border: 0px solid rgba(255, 255, 255, 0.4);
}
.ztime >>> .el-table__row>td{
  border-color:rgba(255, 255, 255, 0.4);
}
::v-deep .el-table__body tr.current-row>td {
            background-color: #2B7AB2 !important;
}
  </style>