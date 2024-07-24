<template>
<div class="a">
        <div class="b">
              <img src="../assets/imgs/logo_point.png" style="position: absolute;top: 13%;left: 1%;">
              <span style="color: white;position: absolute;left:11.5%;top:8.5%;font-size: 25px;font-weight: bold;line-height: 48px;">厂站自动化点表管理及信息核对系统</span>
              <el-button-group style="position: absolute;top: 15%;left: 36%;">
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="home">首页</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="stationmanagement">点表管理</el-button>
                  <el-button class="annu" style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" >校核记录</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="AlertMessage">告警信息</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="VersionControl">版本管理</el-button>
              </el-button-group>
              <img src="../assets/imgs/y1.png" style="position: absolute;left: 86.5%;top: 25%;">
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
              <!-- <span style="color: white;position: absolute;left: 89%;top: 28%;color:#FFFFFF ;">{{username}}</span>
              <span style="color: white;position: absolute;left: 91.5%;top: 29%;">江苏电网-临时组织</span> -->
              <el-badge :value=alertCount :max="99" class="item" style="position: absolute;left: 76.9%;top: 26%;"></el-badge>
              <img src="../assets/imgs/y2.png" style="position: absolute;left: 76%;top: 25.5%;width: 22px;height: 23px;cursor: pointer;" @click="AlertMessage">
              <!-- <img src="../assets/imgs/y3.png" style="position: absolute;left: 76%;top: 25.5%;width: 22px;height: 23px;cursor: pointer;"> -->
              <img src="../assets/imgs/y5.png" style="position: absolute;left: 79%;top: 27%;width: 22px;height: 22px;cursor: pointer;" type="primary" @click="butClick">
              <img src="../assets/imgs/y6.png" style="position: absolute;left: 82%;top: 25.5%;width: 25px;height: 25px;cursor: pointer;" @click="tuichu">
          </div>
          <div style="position: absolute;left: 1%;top: 8.2%;width: 5%;height: 3%;font-weight: bold;color: #03A095;background-color: #F1F2F6;border-radius: 8px 8px 0px 0px;"><span style="position: absolute;left: 17%;top: 24%;">校核查询</span></div>
          <div style="position: absolute;left: 1%;top: 11%;width: 98%;height: 87.8%;background-color: #F1F2F6;border-radius: 0px 8px 8px 8px;">
            <span style="position: absolute;left: 1.4%;top: 1%;font-family: Microsoft YaHei;color: black;font-weight:bold">查询条件</span>
            <span style="position: absolute;left: 1.4%;top: 15%;font-family: Microsoft YaHei;color: black;font-weight:bold">校核日期</span>
            <span style="position: absolute;left: 21.8%;top: 15%;font-family: Microsoft YaHei;color: black;font-weight:bold">校核记录</span>
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
                <span style="position: absolute;color: black;font-size: 18px;top:35%;left: 44%;">校核执行时间：</span>
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
                <el-button style="position: absolute;left: 72%;top: 20%;height: 60%;width: 5%;background: #03A095;color:white;border: 1px solid #03A095;font-size: 16px;" @click="chaxun">查询</el-button>
              </div>
            <div style="position: absolute;background-color: #FDFDFC;left: 0.7%;top: 18.2%;width: 19.1%;height: 80.5%;border-radius: 8px;box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);">
                <div class="zz" style="position: absolute;width: 96.8%;height: 97%;top: 1.5%;left: 1.6%;background: #FFFFFF;border-radius: 8px;border: 1px solid #E0E0E0;">
                  <el-table
                    highlight-current-row
                    @current-change="handleCurrentChange"
                    :data="timeData"
                    height="98%"
                    border
                    :key="randomKey"
                    @header-click="handleHeaderClick"
                    @row-click="handleRowClick"
                    @cell-click="cellClick"
                    :cell-style="d5000pab"
                    :header-cell-style="{background:'#03A095',color:'white'}"
                    style="width: 97%;left: 1.3%;top: 1%;">
                    <el-table-column
                      prop="time"
                      label="校核日期"
                      width="333"
                      align="center">
                    </el-table-column>
                </el-table>
                </div>    
            </div>
            <div  style="position: absolute;background-color: #FDFDFC;left: 20.8%;top: 18.2%;width: 78.5%;height: 80.5%;border-radius: 8px;box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);">
                <div class="jhjl" style="position: absolute;width: 98.8%;height: 97%;top: 1.5%;left: 0.6%;background: #FFFFFF;border-radius: 8px;border: 1px solid #E0E0E0;">
                  <el-table
                    :data="actAllTableData"
                    height="92%"
                    border
                    :cell-style="d5000tab"
                    :row-class-name="rowClassName"
                    :header-cell-style="{background:'#03A095 ',color:'white'}"
                    :key="ResultRandomKey"
                    style="width: 99%;left: 0.5%;top: 1.2%;">
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
                    width="265">
                    </el-table-column>
                    <el-table-column
                    property="type"
                    label="校核类型"
                    align="center"
                    width="320">
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
                        <div @click="handleCellClick(scope.row)" style="cursor: pointer;">
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
                </div>  
            <!-- <div class="block">
              <el-pagination
                class="my-pagination"
                button-class="custom-pagination-button"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange123"
                :current-page="currentPage4"
                background
                pager-count="5"
                :page-size="20"
                layout="total, prev, pager, next, jumper"
                :total="200"
                style="position:absolute;top:92.7%;left: 63%;">
              </el-pagination>
            </div> -->
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
              <el-table :data="gridData" :key="ResultRandomKey" style="width: 100%; color: black; overflow:auto;top: -30px;left: 8px;" :header-cell-style="{background:'#03A095',color:'white'}" border height="596">
                <el-table-column  property="st_name" label="厂站名称" width="160" align="center"></el-table-column>
                <el-table-column  property="ydoyxname" label="厂站对象名称" width="345" align="center"></el-table-column>
                <el-table-column  property="ydoyxno" label="厂站点号" width="80" align="center"></el-table-column>
                <el-table-column  property="d5000yxname" label="新一代对象名称" width="345" align="center"></el-table-column>
                <el-table-column  property="d5000yxno" label="新一代点号" width="100" align="center"></el-table-column>
                <el-table-column  property="equtime" label="比对时间" width="250" align="center"></el-table-column>
                <el-table-column  property="equresult" label="比对结果" width="80" align="center"></el-table-column>
                <el-table-column  property="reason" label="原因" width="481" align="center"></el-table-column>
              </el-table>
              <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisiblexx = false">关 闭</el-button>
                <!-- <el-button type="primary" @click="soAlert">提 交</el-button> -->
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
      <ModifyPwd :visible.sync="changePwd" @submit="handleChangePwd" />
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import axios from 'axios';
export default {
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
      changePwd: false,
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
      alertCount:'',
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
      xuanArea:[],
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
    this.start123()
    this.selectStationAll()
    this.AlertCount()
        this.timer = setInterval(() => {
          this.AlertCount()
        }, 10000);
  },
  methods: {
    home(){
      localStorage.clear();
      this.$router.push('/Home')
    },
    stationmanagement(){
      localStorage.clear();
      this.$router.push('/pointManagement')
    },
    VersionControl(){
        localStorage.clear();
        this.$router.push('/VersionControl')
      },
    AlertMessage(){
      localStorage.clear();
        this.$router.push('/alertManagement')
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
        var url = 'http://localhost:8090/Query/tree'
        return axios.get(url).then(ret => {
          this.TreeData=ret.data.data
          this.xuanxiang1=ret.data.data
        })
      },
      changee(){
        this.station=''
        var url = 'http://localhost:8090/Query/selectt'
        return axios.get(url,{    
              params: {
                  city: this.value1,
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
    handleCellClick(abc) {
      console.log(abc)
      var url
      this.gridData=[]
      if(abc.type.includes('遥信')){
        url = 'http://localhost:8090/Query/yxRecords'
        return axios.get(url,{    
            params: {
                station: this.station,
                time: this.TIME
            }
        }).then(ret => {
          let num
          for(let i=0;i<ret.data.data.length;i++){
              num=i+1
              this.gridData[i]={st_name:ret.data.data[i].st_name,ydoyxname:ret.data.data[i].ydo_yx_name,ydoyxno:ret.data.data[i].ydo_yx_dot_no,d5000yxname:ret.data.data[i].d5000_yx_name,d5000yxno:ret.data.data[i].d5000_yx_dot_no,equtime:ret.data.data[i].timestring,equresult:ret.data.data[i].yx_comparisonresult,reason:ret.data.data[i].reason}
            }
            this.ResultRandomKey = Math.random()
          this.dialogVisiblexx = true
        })
      }
      else if(abc.type.includes('遥测')){
        url = 'http://localhost:8090/Query/ycRecords'
        return axios.get(url,{    
            params: {
                station: this.station,
                time: this.TIME
            }
        }).then(ret => {
          console.log(ret.data.data)
          let num
          for(let i=0;i<ret.data.data.length;i++){
              num=i+1
              this.gridData[i]={st_name:ret.data.data[i].st_name,ydoyxname:ret.data.data[i].ydo_yc_name,ydoyxno:ret.data.data[i].ydo_yc_dot_no,d5000yxname:ret.data.data[i].d5000_yc_name,d5000yxno:ret.data.data[i].d5000_yc_dot_no,equtime:ret.data.data[i].timestring,equresult:ret.data.data[i].yc_comparisonresult,reason:ret.data.data[i].reason}
            }
            this.ResultRandomKey = Math.random()
          this.dialogVisiblexx = true
        })
      }
      else if(abc.type.includes('遥控')){
        url = 'http://localhost:8090/Query/ykRecords'
        return axios.get(url,{    
            params: {
                station: this.station,
                time: this.TIME
            }
        }).then(ret => {
          let num
          for(let i=0;i<ret.data.data.length;i++){
              num=i+1
              this.gridData[i]={st_name:ret.data.data[i].st_name,ydoyxname:ret.data.data[i].ydo_yk_name,ydoyxno:ret.data.data[i].ydo_yk_dot_no,d5000yxname:ret.data.data[i].d5000_yk_name,d5000yxno:ret.data.data[i].d5000_yk_dot_no,equtime:ret.data.data[i].timestring,equresult:ret.data.data[i].yk_comparisonresult,reason:ret.data.data[i].reason}
            }
            this.ResultRandomKey = Math.random()
          this.dialogVisiblexx = true
        })
      }
      else{
        url = 'http://localhost:8090/Query/ytRecords'
        return axios.get(url,{    
            params: {
                station: this.station,
                time: this.TIME
            }
        }).then(ret => {
          let num
          for(let i=0;i<ret.data.data.length;i++){
              num=i+1
              this.gridData[i]={st_name:ret.data.data[i].st_name,ydoyxname:ret.data.data[i].ydo_yt_name,ydoyxno:ret.data.data[i].ydo_yt_dot_no,d5000yxname:ret.data.data[i].d5000_yt_name,d5000yxno:ret.data.data[i].d5000_yt_dot_no,equtime:ret.data.data[i].timestring,equresult:ret.data.data[i].yt_comparisonresult,reason:ret.data.data[i].reason}
            }
            this.ResultRandomKey = Math.random()
          this.dialogVisiblexx = true
        })
      }


      // return axios.get(url,{    
      //       params: {
      //           station: this.station,
      //           time: this.TIME
      //       }
      //   }).then(ret => {
      //     let num
      //     for(let i=0;i<ret.data.data.length;i++){
      //         num=i+1
      //         this.gridData[i]={st_name:ret.data.data[i].st_name,ydoyxname:ret.data.data[i].ydo_yx_name,ydoyxno:ret.data.data[i].ydo_yx_dot_no,d5000yxname:ret.data.data[i].d5000_yx_name,d5000yxno:ret.data.data[i].d5000_yx_dot_no,equtime:ret.data.data[i].timestring,equresult:ret.data.data[i].yx_comparisonresult,reason:ret.data.data[i].reason}
      //       }
      //       this.ResultRandomKey = Math.random()
      //     this.dialogVisiblexx = true
      //   })
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
      handleHeaderClick() {
        this.dialogVisibless=true
        this.zjhData[0]={qy_name:this.value1,bdz_name:this.station}
        //console.log('点击了表格头部：', column);
        // 执行您的逻辑
      },
      handleRowClick(row, event) {
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
        var url = 'http://localhost:8090/Query/allResult'
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
        var url = 'http://localhost:8090/Query/TimeResult'
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
        if(localStorage.length===3){
          const station = localStorage.getItem('station');
          const time = localStorage.getItem('time');
          const area = localStorage.getItem('city');
          this.value1=area
          this.station=station
          // this.chaxun()
          this.TIME=time
          this.timeData[0]={time:time}
          var url = 'http://localhost:8090/Query/allResult'
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
        }
      },
      selectStationAll(){
        var url = 'http://localhost:8090/Query/SelectStationAll'
        return axios.get(url).then(ret => {
          this.xuanArea=ret.data.data
        })
      },
      handleCurrentChange(val) {
        this.currentRow = val;
      },
      d5000tab({row,columnIndex}){
        if (columnIndex == 4) {  
          return {color:'#03A095'}
        }
        const station = localStorage.getItem('station');
        const time = localStorage.getItem('time');
        const type = localStorage.getItem('type');
        const typee = row.type+'异常'
          if (row.sta_name==station && row.time==time && typee===type && row.result=='异常') {
              return 'background:rgba(28, 100, 159, 0.9); color:white;';
          }
      },
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
      },
      handleCurrentChange123(val) {
        console.log(`当前页: ${val}`);
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
.jhjl /deep/ .el-table, 
.jhjl /deep/ .el-table tr,
.jhjl /deep/ .el-table td {
  background-color: transparent;
  color:black;
  /* padding:3px; */
  /* height: 51.5px; */
}
.jhjl /deep/ .el-table tbody tr:hover>td { 
    /* background-color:#9fdfdb; */
}
.jhjl /deep/ .el-table__body-wrapper::-webkit-scrollbar-thumb {
    border-radius: 3px;
}
.jhjl /deep/ .el-table__body-wrapper::-webkit-scrollbar{
    width: 8px;
}
.zz /deep/ .el-table__body-wrapper::-webkit-scrollbar{
    width: 8px;
}
.jhjl >>> .el-table th.is-leaf {
  /* 去除上边框 */
    border: none;
}
.jhjl >>> .el-table::before{
  /* 去除下边框 */
  height: 0;
}
.jhjl .el-table--border::after,.yddy .el-table--group::after{
     width: 0;
}
.jhjl .el-table{
    border: 0px solid rgba(255, 255, 255, 0.4);
}
.jhjl >>> .el-table__row>td{
  border-color:rgba(255, 255, 255, 0.4);
}
.zz /deep/ .el-table, 
.zz /deep/ .el-table__expanded-cell{
  background-color: transparent;
}
.zz /deep/ .el-table tr,
.zz /deep/ .el-table td {
  background-color: transparent;
  color:black;
  padding:3px;
  height: 47.5px;
}
.zz /deep/ .el-table tbody tr:hover>td { 
    background-color:transparent;
}
.zz >>> .el-table__row>td{
  border-color:rgba(255, 255, 255, 0.4);
}
.zz .el-table{
    border: 0.2px solid rgba(255, 255, 255, 0.4);
}
.zz /deep/ .el-table::before {
	height: 0;
}
.zz /deep/ .el-table--border::after {
	height: 0;
}
.xxl /deep/.el-input__inner {
    color: black;
    font-size: 16px;
}

::v-deep .el-table__body tr.current-row>td {
            /* background-color: red !important; */
  background: #94e5e0!important;
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
  ::v-deep .el-input__inner {
    background-color: transparent;
    color: black;
    border: 1px solid #E4E4E4;
  }
  .annu{
      background: linear-gradient(0deg, rgba(0,194,183,0.2) 0%, rgba(0,194,183,0.99) 50%, rgba(0,194,183,0.2) 100%);
  }

</style>