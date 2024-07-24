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
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;color: #33BBFF;box-shadow: 0 3px 0 0 #f8d568;">告警信息</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="VersionControl">版本控制</el-button>
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
          <div style="position: absolute;left: 0.5%;top: 13%;width: 99%;height: 12%;opacity: 0.4;box-shadow: 0 0 3.5px 3.5px #fff inset;border-radius: 4px;"></div>
          <div class="xxl" style="position: absolute;left: 0.5%;top: 13%;width: 99%;height: 12%">
            <hr style="position: absolute;top:35%;left: 0%; width:99.8%;opacity: 0.5;">
          <hr style="position: absolute;background-color: #F8B858;width: 4px;height: 22px;left: 15px;top: 7px;">
          <span style="position: absolute;font-size: 20px;color: #33BBFF;font-weight: bold;top: 8px;left: 30px;">查询条件</span>
          <span style="position: absolute;font-size: 18px;top: 56%;left: 5%;">区域：</span>
          <span style="position: absolute;font-size: 18px;top: 56%;left: 26%;">变电站：</span>  
          <el-select v-model="value1" filterable placeholder="请选择" style="position: absolute;top: 48%;left:8%;height: 50%;width: 14%;" :popper-append-to-body="true" @change="changee">
                <el-option
                v-for="item in xuanxiang1"
                :key="item.value"
                :label="item.label"
                :value="item.label">
                </el-option>
            </el-select>
            <el-select v-model="station" filterable placeholder="请选择" style="position: absolute;top: 48%;left: 30%;height: 50%;width: 14%;" :popper-append-to-body="false">
                <el-option
                v-for="item in xuanxiang2"
                :key="item.value"
                :label="item.label"
                :value="item.label">
                </el-option>
            </el-select>
            <span style="position: absolute;color: white;top:55.5%;left: 50.3%;font-size: 17px;">告警执行时间：</span>
            <div class="block">
                <el-date-picker
                v-model="datevalue"
                type="daterange"
                range-separator="-"
                value-format="yyyy-MM-dd HH:mm:ss"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                style="position: absolute;top: 46%;left: 56.8%;height: 40%;"
                popper-class="date-style">
                </el-date-picker>
            </div>
            <el-button style="position: absolute;left: 90%;top: 46%;height: 40%;width: 5%; color: black;font-size: 16px;" @click="chaxun1">查询</el-button>
          </div>
          <div style="position: absolute;left: 0.5%;top: 25.5%;width: 99%;height: 73.9%;opacity: 0.4;box-shadow: 0 0 3.5px 3.5px #fff inset;border-radius: 4px;"></div>
          <div class="ztable" style="position: absolute;left: 0.5%;top: 25.5%;width: 99%;height: 73.9%;">
            <hr style="position: absolute;background-color: #F8B858;width: 4px;height: 22px;left: 15px;top: 9px;">
            <span style="position: absolute;font-size: 20px;color: #33BBFF;font-weight: bold;top: 9px;left: 30px;">告警记录</span>
            <hr style="position: absolute;top:6.2%;left: 0%; width:99.8%;opacity: 0.5;">
            <el-table
              :data="tableData"
              height="85%"
              :key="randomKey"
              :cell-style="columnStyle"
              @cell-click="cellClick"
              style="width: 98.8%;position:absolute;left:0.6%;top:7.7%"
              :header-cell-style="{background:'#1D8ED2',color:'white'}"
              >
                <el-table-column
                  type="selection"
                  width="55"
                  style="position: absolute;left: 50%;">
                </el-table-column>
                <el-table-column
                  property="station_name"
                  label="变电站名称"
                  width="421"
                  align="center">
                  <template slot-scope="scope">
                    <div @click="handleStation(scope.row)">
                      {{ scope.row.station_name}}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column
                  property="alert_name"
                  label="告警名称"
                  width="350"
                  align="center">
                </el-table-column>
                <el-table-column
                  property="alert_time"
                  label="告警时间"
                  width="350"
                  align="center">
                </el-table-column>
                <el-table-column
                  property="handle_state"
                  label="处理状态"
                  width="350"
                  align="center">
                </el-table-column>
                <el-table-column
                  property="handle_re"
                  label="处理建议"
                  width="350"
                  align="center">
                  <template slot-scope="scope">
                    <div @click="handleCellClick(scope.row)">
                      {{ scope.row.re}}
                    </div>
                  </template>
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
                style="position:absolute;top:94.2%;left: 69.3%;">
              </el-pagination>
            </div>
          </div>
          <div class="dialog">
            <el-dialog
              title="告警信息处理建议"
              :visible.sync="dialogVisiblexx"
              width="50%"
              :before-close="handleClose"
              :close-on-click-modal="false"
              >
              <el-table :data="gridData" style="color:black;width: 900px;overflow:auto;top: -10px;left: 10px;" :header-cell-style="{background:'#1D8ED2',color:'white'}" height="96">
                <el-table-column  property="na_name" label="变电站名称" width="300" align="center"></el-table-column>
                <el-table-column  property="al_name" label="告警名称" width="300" align="center"></el-table-column>
                <el-table-column  property="al_time" label="告警时间" width="300" align="center"></el-table-column>
              </el-table>
              <el-input
                type="textarea"
                :rows="8"
                placeholder="请输入内容"
                v-model="textarea"
                >
              </el-input>
              <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisiblexx = false">取 消</el-button>
                <el-button type="primary" @click="soAlert">提 交</el-button>
              </span>
            </el-dialog>
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
        value1: '',
        station: '',
        alertCount:'',
        datevalue1: '',
        datevalue:'',
        gridData:[],
        tableData:[],
        type:false,
        count: 1,
        randomKey:Math.random(),
        filterText:'',
        value: '',
        dialogVisiblexx: false,
        rowcount:'',
        Detailedprocessing:'',
        textarea: '',
        currentPage4:'',
        testAlert:{
          station_name:'',
          alert_name:'',
          alert_time:'',
          alertMesssage:'',
        },
        TreeData:[],
        citttty:'',
        xuanxiang1: [],
        xuanxiang2: [],
        xuanxiang3: [{label:'张三',value:'张三'}],
      };
    },
    mounted () {
      this.UpAlert()
      this.TreeDate()
      this.AlertCount()
      this.timer = setInterval(() => {
        this.AlertCount()
      }, 5000);
    },
    methods: {
      home(){
        this.$router.push('/Home')
      },
      stationmanagement(){
        this.$router.push('/StationManagement')
      },
      historyquery(){
      this.$router.push('/HistoryQuery')
    },
    VersionControl(){
        this.$router.push('/VersionControl')
      },
    columnStyle({ row,rowIndex,columnIndex }) {
      if (columnIndex == 5) {  
        //return {color:'#1D8ED2'}
        if (rowIndex %2!=0) {
          return {color:'#1D8ED2',background:'rgba(28, 100, 159, 0.4)'}
        }
        else{
          return {color:'#1D8ED2'}
        }
      }
      if (columnIndex == 4 && row.handle_state=='未处理') {  
        if (rowIndex %2!=0) {
          return {color:'red',background:'rgba(28, 100, 159, 0.4)'}
        }
        else{
          return {color:'red'}
        }
      }
      if (columnIndex == 4 && row.handle_state=='已处理') {  
        //return {color:'#00ff33'}
        if (rowIndex %2!=0) {
          return {color:'#00ff33',background:'rgba(28, 100, 159, 0.4)'}
        }
        else{
          return {color:'#00ff33'}
        }
      }
      if (rowIndex %2!=0) {
          return 'background:rgba(28, 100, 159, 0.4)'
      }
    },
    handleCellClick(abc) {
      this.gridData=[]
      this.textarea=''
      //console.log(this.gridData[0])
      //this.Detailedprocessing=abc.handle_re
      this.gridData[0]={na_name:abc.station_name,al_name:abc.alert_name,al_time:abc.alert_time}
      this.textarea=abc.handle_re
      this.dialogVisiblexx = true
    },
    async handleStation(abc) {
      var url = 'http://localhost:8090/Query/upcity'
      localStorage.setItem('station', abc.station_name)
      localStorage.setItem('time', abc.alert_time)
      localStorage.setItem('type', abc.alert_name)   
        try{
          const response = await axios.get(url,{    
              params: {
                  station: abc.station_name
              }})
              localStorage.setItem('city', response.data.data[0])
              //console.log(response.data.data[0])
        }
        catch (error) { 
          //处理请求错误
        }
        //const city = localStorage.getItem('city')
        //console.log(city)
      this.$router.push('/HistoryQuery')
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
      changee(){
        this.station=''
        var url = 'http://localhost:8090/Query/selectt'
        return axios.get(url,{    
              params: {
                  city: this.value1
              }
          }).then(ret => {
              this.xuanxiang2=ret.data.data
          })
      },
      AlertCount(){
        var url = 'http://localhost:8090/Query/alertCount'
        return axios.get(url).then(ret => {
          //console.log(ret.data.count)
          this.alertCount=ret.data.count
        })
      },
      UpAlert(){
        this.tableData=[]
        var url = 'http://localhost:8090/Query/upalert'
        return axios.get(url).then(ret => {
          let xx=[]
          xx=ret.data.data
          //console.log(ret.data.data)
          for(let i=0;i<xx.length;i++){
            this.tableData[i]={
              station_name:xx[i].station_name,
              alert_name:xx[i].alert_name,
              alert_time:xx[i].alert_time_string,
              handle_state:xx[i].handle_state,
              handle_re:xx[i].handle_recommendation,
              re:xx[i].handle_logo,
            }
          }
          this.randomKey = Math.random()
        })
      },
      async soAlert(){
        this.testAlert.station_name=this.gridData[0].na_name
        this.testAlert.alert_name=this.gridData[0]. al_name
        this.testAlert.alert_time=this.gridData[0].al_time
        this.testAlert.alertMesssage=this.textarea
        var url = 'http://localhost:8090/Query/soalert'
        try{
          const response = await axios.post(url,this.testAlert)
          this.UpAlert()
        }
        catch (error) {
          //处理请求错误
        }
        this.dialogVisiblexx = false
      },

      TreeDate(){
        var url = 'http://localhost:8090/Query/tree'
        return axios.get(url).then(ret => {
          this.TreeData=ret.data.data
          this.xuanxiang1=ret.data.data
          //console.log(this.xuanxiang1)
        })
      },
      chaxun1(){
          //const sta = localStorage.getItem('station');
          //console.log(sta);
      }
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
    user-select: none;
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
.ztable /deep/ .el-table, 
.ztable /deep/ .el-table__expanded-cell{
  background-color: transparent;
}
.ztable /deep/ .el-table tr,
.ztable /deep/ .el-table td {
  background-color: transparent;
  color:white;
  padding:3px;
  height: 47.5px;
}
.ztable /deep/ .el-table tbody tr:hover>td { 
    background-color:transparent;
}
.ztable >>> .el-table__row>td{
  border-color:rgba(255, 255, 255, 0.4);
}
.ztable .el-table{
    border: 0.2px solid rgba(255, 255, 255, 0.4);
}
.ztable /deep/ .el-table::before {
	height: 0;
}
.ztable /deep/ .el-table--border::after {
	height: 0;
}

.ztable /deep/ .el-table__body tr:hover{
        .taskName{
            color: red;
            cursor: pointer;
        }
    }
.item {
  margin-top: 10px;
  margin-right: 40px;
}
.dialog /deep/ .el-dialog__title {
  font-size: 20px;
  font-weight: bold;
  color: black;
}

.dialog /deep/ .el-textarea__inner{
  font-family:"Microsoft";
  font-size:16px;
}

/* .block{
    .el-pagination.is-background .el-pager li:not(.disabled).active {
        background-color: #67c23a;
        color: #FFF !important;
    }
    .el-pager li:hover {
        color: #67c23a !important;
    }
} */
/deep/ .el-pagination__total {
        color: #F5FAFF;
}
/deep/ .el-pagination__jump {
        color: #F5FAFF;
      }
</style>