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
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="stationmanagement">校核管理</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;color: #33BBFF;box-shadow: 0 3px 0 0 #f8d568;">校核查询</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="AlertMessage">告警信息</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="VersionControl">版本控制</el-button>
              </el-button-group>
              <img src="../assets/imgs/y1.png" style="position: absolute;left: 70%;top: 47%;">
              <span style="color: white;position: absolute;left: 72%;top: 41%;color:#00AAFF ;">{{username}}</span>
              <span style="color: white;position: absolute;left: 72%;top: 62%;">江苏电网-临时组织</span>
              <img src="../assets/imgs/y2.png" style="position: absolute;left: 85%;top: 52%;width: 22px;height: 25px;cursor: pointer;">
              <img src="../assets/imgs/y3.png" style="position: absolute;left: 88%;top: 52%;width: 22px;height: 25px;cursor: pointer;">
              <img src="../assets/imgs/y5.png" style="position: absolute;left: 91%;top: 52%;width: 22px;height: 25px;cursor: pointer;" type="primary" @click="butClick">
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
            <span style="position: absolute;color: white;top:55.5%;left: 50.3%;font-size: 17px;">校核执行时间：</span>
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
            <el-button style="position: absolute;left: 90%;top: 46%;height: 40%;width: 5%; color: black;font-size: 16px;" @click="chaxun">查询</el-button>
          </div>

          <div style="position: absolute;left: 0.5%;top: 25.5%;width: 24%;height: 74%;opacity: 0.4;box-shadow: 0 0 3.5px 3.5px #fff inset;border-radius: 4px;"></div>
          <div class="ztime" style="position: absolute;left: 0.5%;top: 25.5%;width: 24%;height: 74%;">
          <hr style="position: absolute;background-color: #F8B858;width: 4px;height: 22px;left: 15px;top: 8px;">
          <span style="position: absolute;font-size: 20px;color: #33BBFF;font-weight: bold;top: 8px;left: 30px;">校核日期</span>
          <hr style="position: absolute;top:6.8%;left: 0%; width:99.4%;opacity: 0.5;">
            <!-- @cell-click="cellClick" -->
            <el-table
                highlight-current-row
                @current-change="handleCurrentChange"
                :data="timeData"
                height="90%"
                border
                :key="randomKey"
                @header-click="handleHeaderClick"
                @row-click="handleRowClick"
                @cell-click="cellClick"
                :cell-style="d5000pab"
                :header-cell-style="{background:'#1D8ED2',color:'white'}"
                style="width: 97%;left: 1.3%;top: 8%;">
                <el-table-column
                prop="time"
                label="校核日期"
                width="447"
                align="center">
                </el-table-column>
            </el-table>
          </div>

          <div style="position: absolute;left: 24.9%;top: 25.5%;width: 74.6%;height: 74%;opacity: 0.4;box-shadow: 0 0 3.5px 3.5px #fff inset;border-radius: 4px;"></div>
          <div class="alltable" style="position: absolute;left: 24.9%;top: 25.5%;width: 74.6%;height: 74%;">
            <hr style="position: absolute;background-color: #F8B858;width: 4px;height: 22px;left: 15px;top: 8px;">
          <span style="position: absolute;font-size: 20px;color: #33BBFF;font-weight: bold;top: 8px;left: 30px;">校核记录</span>
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
                property="no"
                label="序号"
                align="center"
                width="55">
                </el-table-column>
                <el-table-column
                property="sta_name"
                label="变电站名称"
                align="center"
                width="225">
                </el-table-column>
                <el-table-column
                property="type"
                label="校核类型"
                align="center"
                width="325">
                </el-table-column>
                <el-table-column
                property="result"
                label="校核结果"
                align="center"
                width="225">
                </el-table-column>
                <el-table-column
                  property="reason"
                  label="原因"
                  align="center"
                  width="300">
                  <template slot-scope="scope">
                    <div @click="handleCellClick(scope.row)">
                      {{ scope.row.reason }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column
                property="time"
                label="校核时间"
                align="center"
                width="275">
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
                style="position:absolute;top:94%;left: 59.2%;">
              </el-pagination>
            </div>
          </div>
          <div class="dialog">
            <el-dialog
              title="校核结果详情"
              :visible.sync="dialogVisiblexx"
              width="98%"
              height="80%"
              style="top: -10%;"
              :before-close="handleClose"
              :close-on-click-modal="false"
            >
              <el-table :data="gridData" :key="ResultRandomKey" style="width: 100%; color: black; overflow:auto;top: -30px;left: 8px;" :header-cell-style="{background:'#1D8ED2',color:'white'}" border height="596">
                <el-table-column  property="st_name" label="变电站名称" width="170" align="center"></el-table-column>
                <el-table-column  property="ydoyxname" label="远动一遥信名称" width="345" align="center"></el-table-column>
                <el-table-column  property="ydoyxno" label="远动一遥信点号" width="80" align="center"></el-table-column>
                <el-table-column  property="d5000yxname" label="D5000遥信名称" width="345" align="center"></el-table-column>
                <el-table-column  property="d5000yxno" label="D5000遥信点号" width="80" align="center"></el-table-column>
                <el-table-column  property="equtime" label="比对时间" width="250" align="center"></el-table-column>
                <el-table-column  property="equresult" label="比对结果" width="70" align="center"></el-table-column>
                <el-table-column  property="reason" label="原因" width="501" align="center"></el-table-column>
              </el-table>
              <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisiblexx = false">取 消</el-button>
                <el-button type="primary" @click="soAlert">提 交</el-button>
              </span>
            </el-dialog>
          </div>
          <div class="dialog">
            <el-dialog
              title="厂站不同时间比对"
              :visible.sync="dialogVisibless"
              width="50%"
              :before-close="handleClose"
              :close-on-click-modal="false"
              >
              <el-table :data="zjhData" style="color:black;width: 900px;overflow:auto;top: -10px;left: 10px;" :header-cell-style="{background:'#1D8ED2',color:'white'}" height="96">
                <el-table-column  property="qy_name" label="区域" width="300" align="center"></el-table-column>
                <el-table-column  property="bdz_name" label="变电站名称" width="300" align="center"></el-table-column>
              </el-table>
              <span>类型：</span>
              <el-select v-model="xx_value" placeholder="请选择">
                <el-option
                  v-for="item in xxoptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
              <span>时间：</span>
              <el-select v-model="dy_value" placeholder="请选择">
                <el-option
                  v-for="item in dyoptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
              <span>时间：</span>
              <el-select v-model="dr_value" placeholder="请选择">
                <el-option
                  v-for="item in droptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
              <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisibless = false">取 消</el-button>
                <el-button type="primary">提 交</el-button>
              </span>
            </el-dialog>
          </div>
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
    'datevalue'(newVal){
      if(newVal == null){
        this.datevalue=''
      }
    },
  },
  data() {
    return {
      xx_value:'',
      xxoptions: [{
          value: '远动一校核',
          label: '远动一校核'
        }, {
          value: '远动二校核',
          label: '远动二校核'
        }, {
          value: 'D5000校核',
          label: 'D5000校核'
        }],
        dyoptions: [{
          value: '2023-12-05 23:14:08',
          label: '2023-12-05 23:14:08'
        }, {
          value: '2023-12-05 23:29:56',
          label: '2023-12-05 23:29:56'
        }, {
          value: '2023-12-08 23:35:50',
          label: '2023-12-08 23:35:50'
        }],
        droptions: [{
          value: '2023-12-05 23:14:08',
          label: '2023-12-05 23:14:08'
        }, {
          value: '2023-12-05 23:29:56',
          label: '2023-12-05 23:29:56'
        }, {
          value: '2023-12-08 23:35:50',
          label: '2023-12-08 23:35:50'
        }],
      dialogVisibless: false,
      dialogVisiblexx: false,
      currentPage4:1,
      zjhData:[{qy_name:' ',bdz_name:' '}],
      dr_value:'',
      dy_value:'',
      currentRow: null,
      value1: '',
      station: '',
      datevalue: '',
      type:false,
      count: 1,
      randomKey:Math.random(),
      ResultRandomKey:Math.random(),
      filterText:'',
      yaoyd1finall:[],
      yaoyd2finall:[],
      D5000DataFinall:[],
      xuanxiang1: [],
      xuanxiang2: [],
      timeData:[],
      timetest:[],
      value: '',
      table2data:[],
      actAllTableData:[],
      gridData:[{st_name:'三官殿',ydoyxname:'123',ydoyxno:'1',d5000yxname:'456',d5000yxno:'55',equtime:'2023',equresult:'异常',reason:'11'}],
      TIME:'',
      timelist:{
        station:'',
        time1:'',
        time2:''
      },
    };
  },
  mounted () {
    this.TreeDate()
    //this.start123()
  },
  methods: {
    home(){
      localStorage.clear();
      this.$router.push('/Test1')
    },
    stationmanagement(){
      localStorage.clear();
      this.$router.push('/StationManagement')
    },
    VersionControl(){
        this.$router.push('/VersionControl')
      },
    AlertMessage(){
      localStorage.clear();
        this.$router.push('/AlertMessage')
      },
    search(){
      this.$refs.tree.filter(this.filterText);
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
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
    TreeDate(){
        var url = '/Query/tree'
        return axios.get(url).then(ret => {
          this.TreeData=ret.data.data
          this.xuanxiang1=ret.data.data
          //console.log(ret.data.data)
        })
      },
      changee(){
        this.station=''
        var url = '/Query/selectt'
        return axios.get(url,{    
              params: {
                  city: this.value1,
              }
          }).then(ret => {
              this.xuanxiang2=ret.data.data
          })
      },
    handleCellClick(abc) {
      this.gridData=[]
      var url = '/PointResult/yxRecords'
      return axios.get(url,{    
            params: {
                station: this.station,
                time: this.TIME
            }
        }).then(ret => {
          //console.log(ret.data.data)
          let num
          for(let i=0;i<ret.data.data.length;i++){
              num=i+1
              this.gridData[i]={st_name:ret.data.data[i].st_name,ydoyxname:ret.data.data[i].ydo_yx_name,ydoyxno:ret.data.data[i].ydo_yx_dot_no,d5000yxname:ret.data.data[i].d5000_yx_name,d5000yxno:ret.data.data[i].d5000_yx_dot_no,equtime:ret.data.data[i].timestring,equresult:ret.data.data[i].yx_comparisonresult,reason:ret.data.data[i].reason}
            }
            this.ResultRandomKey = Math.random()
          this.dialogVisiblexx = true
        })
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
      handleHeaderClick(event, column) {
        this.dialogVisibless=true
        this.zjhData[0]={qy_name:this.value1,bdz_name:this.station}
        //console.log('点击了表格头部：', column);
        // 执行您的逻辑
      },
      handleRowClick(row, event, column) {
        // 移除其他单元格的高亮样式
        const cells = document.querySelectorAll('.highlight');
        cells.forEach(cell => cell.classList.remove('highlight'));

        // 给当前被点击的单元格添加高亮样式
        event.currentTarget.classList.add('highlight');
      },
      cellClick(rowIndex){
        localStorage.clear();
        this.actAllTableData=[]
        this.ResultRandomKey = Math.random()
        this.TIME = rowIndex.time
        var url = '/PointResult/allResult'
        return axios.get(url,{
            params: {
                station: this.station,
                time: this.TIME
            }
        }).then(ret => {
            let exAllTableData=[]
            let num
            exAllTableData=ret.data.data
            for(let i=0;i<exAllTableData.length;i++){
              num=i+1
              this.actAllTableData[i]={no:num,sta_name:exAllTableData[i].station_name,type:exAllTableData[i].type,result:exAllTableData[i].result,reason:exAllTableData[i].reason,time:exAllTableData[i].timeZhuan
              }
            }
            this.ResultRandomKey = Math.random()
        })
      },
      chaxun(){
        //localStorage.clear();
        this.actAllTableData=[]
        this.timeData=[]
        var url = '/PointResult/TimeResult'
        return axios.get(url,{    
            params: {
                city:this.value1,
                station: this.station,
                time1: this.datevalue[0],
                time2:this.datevalue[1]
            }
        }).then(ret => {
            this.timetest = ret.data.data
            for(let i=0;i<this.timetest.length;i++){
              this.timeData[i]={time:this.timetest[i]}
            }
            this.randomKey = Math.random()
        })
      },
      start123(){
        const station = localStorage.getItem('station');
        const time = localStorage.getItem('time');
        const city = localStorage.getItem('city');
        this.value1=city
        //const type = localStorage.getItem('type');
        this.station=station
        this.chaxun()
        this.TIME=time
        var url = '/PointResult/allResult'
        return axios.get(url,{    
            params: {
                station: this.station,
                time: this.TIME
            }
        }).then(ret => {
            let exAllTableData=[]
            let num
            exAllTableData=ret.data.data
            for(let i=0;i<exAllTableData.length;i++){
              num=i+1
              this.actAllTableData[i]={no:num,sta_name:exAllTableData[i].station_name,type:exAllTableData[i].type,result:exAllTableData[i].result,reason:exAllTableData[i].reason,time:exAllTableData[i].timeZhuan
              }
            }
            this.ResultRandomKey = Math.random()
        })
      },
      handleCurrentChange(val) {
        this.currentRow = val;
      },
      // rowClassName(row) {
      //   // console.log(row.row.result)
      //   // if (row.row.result=='异常') {
      //   //   console.log(111)
      //   //   return 'highlight-row';
      //   // } else {
      //   //   return '';
      //   // }
      // },
      d5000tab({row,columnIndex}){
        if (columnIndex == 4) {  
          return {color:'#1D8ED2'}
        }
        const station = localStorage.getItem('station');
        const time = localStorage.getItem('time');
        const type = localStorage.getItem('type');
        const typee = row.type+'异常'
          console.log(typee)
          if (row.sta_name==station && row.time==time && typee===type && row.result=='异常') {
              return 'background:rgba(28, 100, 159, 0.9); color:white;';
          }
      },
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
      },
      handleCurrentChange123(val) {
        console.log(`当前页: ${val}`);
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
.xxl /deep/.el-input__inner {
  background-color: white;
  opacity: 1;
  color:black;
  border: 1px solid white;
}
.xxl /deep/.el-select-dropdown {
  background-color: white;
  border: 1px solid white;
}
.el-select-dropdown__item {
  color: black;
}

.el-select-dropdown__item:hover {
  background-color: white;
}
.xxl /deep/ .popper_arrow::after{
    border-bottom-color: red;
}
.highlight {
  background-color: #fffae6; /* 设置高亮背景色 */
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
.alltable /deep/ .el-table__body-wrapper::-webkit-scrollbar{
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
.xxl /deep/.el-input__inner {
    color: black;
    font-size: 16px;
}

::v-deep .el-table__body tr.current-row>td {
            background-color: #2B7AB2 !important;
}
.dialog /deep/ .el-dialog__title {
  font-size: 20px;
  font-weight: bold;
  color: black;
}
.highlight-cell {
  background-color: red; /* 高亮颜色 */
}
.highlight-row {
  background-color: red; /* 高亮颜色 */
}
/* /deep/ .el-pagination__total {
        color: #F5FAFF;
}
/deep/ .el-pagination__jump {
        color: #F5FAFF;
      } */
</style>