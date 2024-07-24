<template>
    <div class="a">
        <div class="b">
          <img src="../assets/imgs/logo_point.png" style="position: absolute;top: 13%;left: 1%;">
              <span style="color: white;position: absolute;left:11.5%;top:8.5%;font-size: 25px;font-weight: bold;line-height: 48px;">厂站自动化点表管理及信息核对系统</span>
              <el-button-group style="position: absolute;top: 15%;left: 36%;">
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="home">首页</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="stationmanagement">点表管理</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="historyquery">校核记录</el-button>
                  <el-button class="annu" style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;">告警信息</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="VersionControl">版本管理</el-button>
              </el-button-group>
              <img src="../assets/imgs/y1.png" style="position: absolute;left: 86.5%;top: 25%;">
              <!-- <span style="color: white;position: absolute;left: 89%;top: 28%;color:#FFFFFF ;">{{username}}</span>
              <span style="color: white;position: absolute;left: 91.5%;top: 29%;">江苏电网-临时组织</span> -->
              <el-dropdown trigger="click" @command="handleCommand" style="position: absolute;left: 89%;top: 28%;">
                <span class="el-dropdown-link" style="color: white;font-size: 19px;">
                  {{username}}<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command='用户管理'>用户管理</el-dropdown-item>
                  <el-dropdown-item command='修改密码'>修改密码</el-dropdown-item>
                  <el-dropdown-item command='退出账号'>退出账号</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
              <el-badge :value=alertCount :max="99" class="item" style="position: absolute;left: 76.9%;top: 17%;"></el-badge>
              <img src="../assets/imgs/y2.png" style="position: absolute;left: 76%;top: 25.5%;width: 22px;height: 23px;cursor: pointer;">
              <!-- <img src="../assets/imgs/y3.png" style="position: absolute;left: 76%;top: 25.5%;width: 22px;height: 23px;cursor: pointer;"> -->
              <img src="../assets/imgs/y5.png" style="position: absolute;left: 79%;top: 27%;width: 22px;height: 22px;cursor: pointer;" type="primary" @click="butClick">
              <img src="../assets/imgs/y6.png" style="position: absolute;left: 82%;top: 25.5%;width: 25px;height: 25px;cursor: pointer;" @click="tuichu">
        </div>
        <div style="position: absolute;left: 1%;top: 8.2%;width: 5%;height: 3%;font-weight: bold;color: #03A095;background-color: #F1F2F6;border-radius: 8px 8px 0px 0px;"><span style="position: absolute;left: 17%;top: 24%;">告警信息</span></div>
        <div style="position: absolute;left: 1%;top: 11%;width: 98%;height: 87.8%;background-color: #F1F2F6;border-radius: 0px 8px 8px 8px;">
            <span style="position: absolute;left: 1.4%;top: 1%;font-family: Microsoft YaHei;color: black;font-weight:bold">查询条件</span>
            <span style="position: absolute;left: 1.4%;top: 15%;font-family: Microsoft YaHei;color: black;font-weight:bold">告警记录</span>
            <div  style="position: absolute;background-color: #FDFDFC;left: 0.7%;top: 4.6%;width: 98.7%;height: 9%;border-radius: 8px;box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);">
                <span style="position: absolute;color: black;font-size: 18px;top:35%;left: 2.8%;">区域：</span>
                <el-select v-model="value1" filterable placeholder="请选择" style="position: absolute;top: 23%;left:6%;height: 50%;width: 14%;" :popper-append-to-body="true" @change="changee">
                  <el-option
                    v-for="item in xuanArea"
                    :key="item.value"
                    :label="item.label"
                    :value="item.label">
                    </el-option>
                </el-select>
                <span style="position: absolute;color: black;font-size: 18px;top:35%;left: 23.8%;">厂站：</span>
                <el-select v-model="station" filterable placeholder="请选择" style="position: absolute;top: 23%;left: 27.2%;height: 50%;width: 14%;" :popper-append-to-body="false">
                    <el-option
                    v-for="item in xuanxiang2"
                    :key="item.value"
                    :label="item.label"
                    :value="item.label">
                    </el-option>
                </el-select>
                <span style="position: absolute;color: black;font-size: 18px;top:35%;left: 44%;">告警执行时间：</span>
                <div class="block">
                    <el-date-picker
                    v-model="datevalue"
                    type="daterange"
                    range-separator="-"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    style="position: absolute;top: 18%;left: 51.6%;height: 64%;"
                    popper-class="date-style">
                    </el-date-picker>
                </div>
                <el-button style="position: absolute;left: 72%;top: 20%;height: 60%;width: 5%;background: #03A095;color:white;border: 1px solid #03A095;font-size: 16px;" @click="chaxun1">查询</el-button>
            </div>
            <div style="position: absolute;background-color: #FDFDFC;left: 0.7%;top: 18.2%;width: 98.6%;height: 80.5%;border-radius: 8px;box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);"> 
              <div class="zzt" style="position: absolute;left: 0.5%;top: 1%;width: 99%;height: 98%;background: #FFFFFF;border-radius: 8px;border: 1px solid #E0E0E0;">
                <el-table
              :data="tableData"
              height="92%"
              :key="randomKey"
              :cell-style="columnStyle"
              @cell-click="cellClick"
              style="width: 99.2%;position:absolute;left:0.4%;top:1%"
              :header-cell-style="{background:'#03A095',color:'white'}"
              >
                <el-table-column
                  property="noo"
                  label="序号"
                  width="75"
                  align="center"
                >
                </el-table-column>
                <el-table-column
                  property="station_name"
                  label="变电站名称"
                  width="401"
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
                  width="294"
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
                    button-class="custom-pagination-button"
                    @current-change="handleCurrentChange123"
                    :current-page="currentPage4"
                    background
                    :pager-count="5"
                    :page-size="20"
                    layout="total, prev, pager, next, jumper"
                    :total=this.AllCount
                    style="position:absolute;top:94%;left: 70%;">
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
                    <el-table :data="gridData" style="color:black;width: 900px;overflow:auto;top: -10px;left: 10px;" :header-cell-style="{background:'#03A095',color:'white'}" height="96">
                      <el-table-column  property="na_name" label="变电站名称" width="220" align="center"></el-table-column>
                      <el-table-column  property="al_name" label="告警名称" width="220" align="center"></el-table-column>
                      <el-table-column  property="al_time" label="告警时间" width="220" align="center"></el-table-column>
                      <el-table-column  property="al_peo" label="处理人员" width="220" align="center"></el-table-column>
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
                      <el-button style="background-color: #03A095;" type="primary" @click="soAlert">提 交</el-button>
                    </span>
                  </el-dialog>
                </div>
            </div>
        </div>
        <ModifyPwd :visible.sync="changePwd" @submit="handleChangePwd" />
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
  components: {
    ModifyPwd: () => import('@/components/Modal/ModifyPwd'),
  },
  watch: {
    'datevalue'(newVal){
      if(newVal == null){
        this.datevalue=''
      }
    },
  },
  data() {
      return {
        State:0,
        changePwd: false,
        value1: '',
        station: '',
        alertCount:'',
        AllCount:'',
        datevalue:'',
        gridData:[],
        tableData:[],
        xuanArea:[],
        type:false,
        count: 1,
        randomKey:Math.random(),
        filterText:'',
        value: '',
        dialogVisiblexx: false,
        rowcount:'',
        Detailedprocessing:'',
        textarea: '',
        currentPage4:'1',
        testAlert:{
          station_name:'',
          alert_name:'',
          alert_time:'',
          alertMesssage:'',
          handle_peo:''
        },
        TreeData:[],
        citttty:'',
        xuanxiang1: [],
        xuanxiang2: [],
        xuanxiang3: [{label:'张三',value:'张三'}],
      };
    },
    mounted () {
      this.selectStationAll()
      this.UpAlert()
      // this.TreeDate()
      this.AlertCount()
      this.timer = setInterval(() => {
        this.AlertCount()
      }, 10000);
    },
    methods: {
      home(){
        this.$router.push('/Home')
      },
      stationmanagement(){
        this.$router.push('/pointManagement')
      },
      historyquery(){
        this.$router.push('/CheckQuery')
      },
      VersionControl(){
        this.$router.push('/VersionControl')
      },
    columnStyle({ row,rowIndex,columnIndex }) {
      if (columnIndex == 5) {  
        if (rowIndex %2!=0) {
          return {color:'#1D8ED2',background:'#EFEFEF'}
        }
        else{
          return {color:'#1D8ED2'}
        }
      }
      if (columnIndex == 4 && row.handle_state=='未处理') {  
        if (rowIndex %2!=0) {
          return {color:'red',background:'#EFEFEF'}
        }
        else{
          return {color:'red'}
        }
      }
      if (columnIndex == 4 && row.handle_state=='已处理') {  
        if (rowIndex %2!=0) {
          return {color:'#32B16C',background:'#EFEFEF'}
        }
        else{
          return {color:'#32B16C'}
        }
      }
      if (rowIndex %2!=0) {
          return 'background:#EFEFEF'
      }
    },
    selectStationAll(){
        var url = 'http://localhost:8090/Query/SelectStationAll'
        return axios.get(url).then(ret => {
          this.xuanArea=ret.data.data
        })
      },
    handleCellClick(abc) {
      // console.log(abc)
      this.gridData=[]
      this.textarea=''
      this.gridData[0]={na_name:abc.station_name,al_name:abc.alert_name,al_time:abc.alert_time,al_peo:abc.alert_ann}
      this.textarea=abc.handle_re
      this.dialogVisiblexx = true
    },
    async handleStation(abc) {
      // var url = 'http://localhost:8090/Query/upcity'
      // localStorage.setItem('station', abc.station_name)
      // localStorage.setItem('time', abc.alert_time)
      // localStorage.setItem('type', abc.alert_name)   
      //   try{
      //     const response = await axios.get(url,{    
      //         params: {
      //             station: abc.station_name
      //         }})
      //         localStorage.setItem('city', response.data.data[0])
      //         //console.log(response.data.data[0])
      //   }
      //   catch (error) { 
      //     //处理请求错误
      //   }
      // this.$router.push('/HistoryQuery')
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
    chaxun1(){
      this.State=1,
      this.currentPage4=1
      if(this.station===''){
        console.log()
      }
      else{
        this.SelectAlert()

      }
    },
    handleCommand(command){
          console.log(command)
          if(command==='退出账号'){
            this.tuichu()
          }
          else if(command==='用户管理'){
            this.$router.push('/system/user-manage')
          }
          else if(command==='修改密码'){
            this.changePwd = true;
          }
        },
        handleChangePwd(model) {
          this.$api.postUserApi('update/password', model).then((res) => {
            if (res?.success) {
              this.$message({
                type: 'success',
                message: '密码修改成功！',
              });
              this.changePwd = false;
            } else {
              this.$message({
                type: 'error',
                message: '密码修改失败！',
              });
            }
          });
        },
    butClick(){
				if(this.type){
					this.content = '全屏显示'
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
          this.alertCount=ret.data.count
        })
      },
      UpAlert(){
        this.tableData=[]
        var url = 'http://localhost:8090/Query/upalert'
        return axios.get(url,{    
              params: {
                  page: this.currentPage4
              }
          }).then(ret => {
          let xx=[]
          xx=ret.data.data
          xx.reverse();
          this.AllCount=xx[0].allCount
          let count=(this.currentPage4-1)*20
          for(let i=0;i<xx.length;i++){
            this.tableData[i]={
              noo:count+i+1,
              station_name:xx[i].station_name,
              alert_name:xx[i].alert_name,
              alert_time:xx[i].alert_time_string,
              handle_state:xx[i].handle_state,
              handle_re:xx[i].handle_recommendation,
              re:xx[i].handle_logo,
              alert_ann:xx[i].handle_an
            }
          }
          this.randomKey = Math.random()
        })
      },
      SelectAlert(){
        var url = 'http://localhost:8090/Query/selectalert'
        return axios.get(url,{    
              params: {
                  station: this.station,
                  time1: this.datevalue[0],
                  time2:this.datevalue[1],
                  page: this.currentPage4
              }
          }).then(ret => {
              this.tableData=[]
              for(let i=0;i<ret.data.data.length;i++){
                this.AllCount=ret.data.data[0].allCount
                let count=(this.currentPage4-1)*20
                this.tableData[i]={
                  noo:count+i+1,
                  station_name:ret.data.data[i].station_name,
                  alert_name:ret.data.data[i].alert_name,
                  alert_time:ret.data.data[i].alert_time_string,
                  handle_state:ret.data.data[i].handle_state,
                  handle_re:ret.data.data[i].handle_recommendation,
                  re:ret.data.data[i].handle_logo,
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
        this.testAlert.handle_peo=this.username
        var url = 'http://localhost:8090/Query/soalert'
        try{
          const response = await axios.post(url,this.testAlert)
          console.log(response)
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
        })
      },
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
      },
      handleCurrentChange123(val) {
        if(this.State===0){
          this.currentPage4 = val
          this.UpAlert()
        }
        else if(this.State===1){
          this.currentPage4 = val
          this.SelectAlert()
        }

      }
 }
};
</script>

<style scoped>
.a{
    position: absolute;
    height: 100%;
    width: 100%;
    background: url(../assets/imgs/pointBJ.jpg) no-repeat;
    background-size: 100%;
    background-color: #03A095;
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
/* 表格最底下边线 颜色 */
.zzt /deep/ .el-table::before {
      background-color: #ebeef5
  }
.dialog /deep/ .el-table::before {
      background-color: #ebeef5
  }
/* 表格内底线/间隔线 颜色 */
  .zzt /deep/ .el-table__body td {
    border-bottom: 1px solid #ebeef5; 
}
/* 分页选中按钮 */
.zzt /deep/ .el-pagination.is-background .el-pager li:not(.disabled).active {
  background-color: #03A095;
  color: white;
  border: 1px solid #03A095;
  text-align: center;
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

/* /deep/ .el-pagination__total {
        color: #F5FAFF;
}
/deep/ .el-pagination__jump {
        color: #F5FAFF;
      } */
    .annu{
      background: linear-gradient(0deg, rgba(0,194,183,0.2) 0%, rgba(0,194,183,0.99) 50%, rgba(0,194,183,0.2) 100%);
    }
  ::v-deep .el-input__inner {
    background-color: transparent;
    color: black;
    border: 1px solid #E4E4E4;
  }
</style>