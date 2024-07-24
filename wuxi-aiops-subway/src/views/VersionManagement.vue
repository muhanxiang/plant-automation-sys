<template>
    <div class="a">
        <div class="b">
            <img src="../assets/imgs/logo_point.png" style="position: absolute;top: 13%;left: 1%;">
            <span style="color: white;position: absolute;left:11.5%;top:8.5%;font-size: 25px;font-weight: bold;line-height: 48px;">厂站自动化点表管理及信息核对系统</span>
            <el-button-group style="position: absolute;top: 15%;left: 36%;">
                <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="home">首页</el-button>
                <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="stationmanagement">点表管理</el-button>
                <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="historyquery">校核记录</el-button>
                <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="AlertMessage">告警信息</el-button>
                <el-button class="annu" style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;">版本管理</el-button>
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
            <img src="../assets/imgs/y2.png" style="position: absolute;left: 76%;top: 25.5%;width: 22px;height: 23px;cursor: pointer;" @click="AlertMessage">
            <!-- <img src="../assets/imgs/y3.png" style="position: absolute;left: 76%;top: 25.5%;width: 22px;height: 23px;cursor: pointer;" @click="importCz"> -->
            <img src="../assets/imgs/y5.png" style="position: absolute;left: 79%;top: 27%;width: 22px;height: 22px;cursor: pointer;" type="primary" @click="butClick">
            <img src="../assets/imgs/y6.png" style="position: absolute;left: 82%;top: 25.5%;width: 25px;height: 25px;cursor: pointer;" @click="tuichu">
        </div>
        <div style="position: absolute;left: 1%;top: 8.2%;width: 5%;height: 3%;font-weight: bold;color: #03A095;background-color: #F1F2F6;border-radius: 8px 8px 0px 0px;"><span style="position: absolute;left: 17%;top: 24%;">版本管理</span></div>
        <div style="position: absolute;left: 1%;top: 11%;width: 98%;height: 87.8%;background-color: #F1F2F6;border-radius: 0px 8px 8px 8px;">
          <div class="treee" style="position: absolute;background-color: #FDFDFC;left: 0.7%;top: 4.2%;width: 19.1%;height: 94.5%;border-radius: 8px;box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);">
                <el-input v-model="filterText1" style="position: absolute;top: 1.8%;width: 250px;left: 15px;background-color: #FFFFFF;border: 1px solid #E4E4E4;border-radius: 4px;"></el-input>
                <el-button style="position:absolute;top: 1.8%;left: 275px;width: 20.5%;background: #03A095;border-radius: 4px;color: white;border: 1px solid #03A095;" @click.native="search1">搜索</el-button>
                <el-tree style="position: absolute;width: 340px;height: 89.5%;top: 9.2%;left: 10px;color: black;border: 1px solid #E0E0E0;border-radius: 8px;"
                        class="filter-tree"
                        accordion
                        :data="TreeData"
                        :highlight-current="true"
                        :props="defaultProps"
                        :filter-node-method="filterNode"
                        ref="tree1"
                        @node-click="handleNodeClick"
                        >
                </el-tree>
          </div>
          <div class="treee" style="position: absolute;background-color: #FDFDFC;left: 20.7%;top: 4.2%;width: 19.1%;height: 94.5%;border-radius: 8px;box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);">
                <el-input v-model="filterText" style="position: absolute;top: 1.8%;width: 250px;left: 15px;background-color: #FFFFFF;border: 1px solid #E4E4E4;border-radius: 4px;"></el-input>
                <el-button style="position:absolute;top: 1.8%;left: 275px;width: 20.5%;background: #03A095;border-radius: 4px;color: white;border: 1px solid #03A095;" @click.native="search">搜索</el-button>
                <el-tree style="position: absolute;width: 340px;height: 89.5%;top: 9.2%;left: 10px;font-size: 35px;color: black;border: 1px solid #E0E0E0;border-radius: 8px;"
                        class="filter-tree"
                        accordion
                        :data="TimeData"
                        :highlight-current="true"
                        :props="defaultProps"
                        :filter-node-method="filterNode"
                        ref="tree"
                        @node-click="handleNodeTime"
                        >
                </el-tree>
          </div>
          <div class="yoy" style="position: absolute;background-color: #FDFDFC;left: 40.6%;top: 4.2%;width: 58.7%;height: 9%;border-radius: 8px;box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);">
                <span style="position: absolute;color: black;font-size: 18px;top:35%;left: 2%;">厂站信息：</span>
                <el-input style="position: absolute;left: 10%;width: 15%;top: 23%;font-size: 17px;" :disabled="true" v-model="this.xuanstation"></el-input>
                <span style="position: absolute;color: black;font-size: 18px;top:35%;left: 25.5%;">时间信息：</span>
                <el-input style="position: absolute;font-size: 17px;left: 33.5%;width: 18%;top: 23%;" :disabled="true" v-model="this.xuanTime"></el-input>
                <span style="position: absolute;color: black;font-size: 18px;top:35%;left: 52%;">校核状态：</span>
                <el-select v-model="checkState" filterable placeholder="请选择" style="position: absolute;top: 23%;left:60%;height: 50%;width: 12%;" :popper-append-to-body="true" @change="changee">
                    <el-option
                    v-for="item in checkStateList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.label">
                    </el-option>
                </el-select>
                <el-radio-group v-model="radio1" style="position: absolute;right: 1.3%;top:22.5%">
                  <el-radio-button label="遥信" @click.native="YXXyin"></el-radio-button>
                  <el-radio-button label="遥测" @click.native="YCXyin"></el-radio-button>
                  <el-radio-button label="遥控" @click.native="YKXyin"></el-radio-button>
                  <el-radio-button label="遥调" @click.native="YTXyin"></el-radio-button>
                </el-radio-group>
          </div>
          <div class="wytable" style="position: absolute;background-color: #FDFDFC;left: 40.6%;top: 14.5%;width: 58.5%;height: 84%;border-radius: 8px;box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);">
                <div class="jhjl" style="position: absolute;width: 98.8%;height: 97%;top: 1.5%;left: 0.6%;background: #FFFFFF;border-radius: 8px;border: 1px solid #E0E0E0;">
                  <el-table
                    :data="actAllTableData"
                    height="92%"
                    :cell-style="d5000tab"
                    :row-class-name="rowClassName"
                    :header-cell-style="{background:'#03A095 ',color:'white'}"
                    :key="ResultRandomKey"
                    style="width: 99%;left: 0.5%;top: 1.2%;font-size: 13px;">
                    <el-table-column
                    property="no"
                    label="序号"
                    align="center"
                    width="258">
                    </el-table-column>
                    <el-table-column
                    property="info_name"
                    label="信息名称"
                    align="center"
                    width="540">
                    </el-table-column>
                    <el-table-column
                    property="dot"
                    label="点号"
                    align="center"
                    width="275">
                    </el-table-column>
                </el-table>
                <el-button style="position: absolute;top: 93.5%;width: 12%;left: 1%;background: #03A095;border-radius: 4px;color: white;border: 1px solid #03A095;" @click="importCz">导入厂站点表</el-button>
                <el-button style="position: absolute;top: 93.5%;width: 12%;left: 13%;background: #03A095;border-radius: 4px;color: white;border: 1px solid #03A095;" @click="exportDB">导出厂站点表</el-button>
                </div>
          </div>
          <div class="dialog">
            <el-dialog title="导入厂站点表信息" :visible.sync="dialogVisible">
                  <el-form ref="importFormRef" :model="importForm" label-width="130px">
                <el-form-item label="厂站信息:" prop="kgCode" style="width: 60%;">
                  <el-select v-model="uploadtest.name" filterable placeholder="请选择">
                    <el-option
                      v-for="item in selectList"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="上传文件:" prop="excel">
                  <el-upload :action="actionUrl" :on-success="handleUploadSuccess" :on-error="handleUploadError" :data="uploadtest" :show-file-list="true" accept=".xlsx, .xls">
                    <el-button size="small" type="primary" style="background-color: #03A095;border-color: #03A095;">点击上传</el-button>
                  </el-upload>
                </el-form-item>
                <el-form-item style="position:relative;left: 70%;">
                  <!-- <el-button type="primary" @click="submitImportForm">开始导入</el-button> -->
                  <el-button @click="dialogVisible = false">关闭窗口</el-button>
                </el-form-item>
              </el-form>
            </el-dialog>
          </div>
          <div class="dialogs">
            <el-dialog title="点表导出功能" :visible.sync="dialogVisible_db">
            <!-- <span style="position: absolute;top: 20%;left: 8%;">厂站名称：</span> -->
            <el-form ref="importFormRef" :model="importForm" label-width="130px">
              <el-form-item label="厂站信息:" prop="kgCode" style="width: 60%;">
                <el-input v-model="station" placeholder="请输入内容" :disabled="true" style="width: 48%;"></el-input>
              </el-form-item>
              <el-form-item label="时间信息:" prop="kgCode" style="width: 60%;">
                <el-select v-model="exceltime" filterable placeholder="请选择">
                  <el-option
                    v-for="item in TimeData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item style="position:relative;left: 62%;">
                <el-button @click="ToExcel">点表导出</el-button>
                <el-button @click="dialogVisible_db = false">关闭窗口</el-button>
              </el-form-item>
            </el-form>
          </el-dialog>
    </div>
        </div>
        <ModifyPwd :visible.sync="changePwd" @submit="handleChangePwd" />
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import axios from 'axios';
// import { TimeSelect } from 'element-ui';
export default {
  name: 'chart',
  computed: {
    ...mapGetters(['username']),
    },
  components: {
    ModifyPwd: () => import('@/components/Modal/ModifyPwd'),
  },
  watch: {
    filterText(val) {
        this.$refs.tree.filter(val);
    },
    filterText1(val) {
        this.$refs.tree1.filter(val);
    }
  },
  data() {
      return {
        actAllVerData:[],
        changePwd: false,
        radio1: '遥信',
        value1: '',
        station: '',
        xuanstation:'',
        xuanTime:'',
        alertCount:'',
        filterText1:'',
        ResultRandomKey:Math.random(),
        filterText2:'',
        filterText:'',
        actAllTableData:[],
        actionUrl: 'http://localhost:8090/Query/addCZInfo',
        TreeData:[],
        TimeData:[],
        PublicTime:'',
        dialogVisible: false,
        dialogVisible_db:false,
        VersionData:[],
        PublicTimeList:[],
        uploadtest: {imgType: 'jpg', name: '暂无数据', imgSize: '12kb'},
        checkState:'',
        exceltime:'',
        checkStateList:[{
          value: '已校核',
          label: '已校核'
        }, {
          value: '未校核',
          label: '未校核'
        }],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        //导入表单数据
        importForm:{
          kgCode:'',
          targetUrl:'',
          targetUsername:'',
          targetPassword:'',
        },
        selectList:[],
        options: [{
            value: '大屯',
            label: '大屯'
          }, {
            value: '徐塘厂',
            label: '徐塘厂'
          }, {
            value: '彭城厂',
            label: '彭城厂'
          }],
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
    importCz(){
      this.uploadtest.name=[]
      this.dialogVisible = true
      this.selectListtt()
    },
    search(){
        this.$refs.tree.filter(this.filterText);
      },
      search1(){
        this.$refs.tree.filter(this.filterText1);
      },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    TreeDate(){
        var url = 'http://localhost:8090/Query/VersionAllTree'
        return axios.get(url).then(ret => {
          this.TreeData=ret.data.data
        })
      },
    exportDB(){
      // this.exceltime=''
      this.dialogVisible_db=true;
    },
    VersionDate(){
        var url = 'http://localhost:8090/Query/allversiontree'
        return axios.get(url).then(ret => {
          this.VersionData=ret.data.data
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
    handleUploadSuccess() {
      this.actAllTableData=[]
      this.TimeData=[]
      this.ResultRandomKey = Math.random()
      this.xuanstation=''
      this.xuanTime=''
      this.TreeDate()
      alert('上传成功!');
    },
    handleUploadError(){
      alert('上传失败!');
    },
    handleNodeClick(node){
      const str='电网'
      const aaa=node.label
      if(aaa.includes(str)){
        this.checkState=''
        this.xuanTime=''
        this.station=''
      }
      else{
        this.checkState=''
        this.xuanstation=node.label
        this.station=node.label
        this.TimeQuery()
      }
    },
    TimeQuery(){
      this.TimeData=[]
      var url = 'http://localhost:8090/Query/VersionTimeList'
      return axios.get(url,{
              params: {
                  station: this.xuanstation,
              }
          }).then(ret => {
            this.TimeData=ret.data.data
            this.PublicTimeList=ret.data.data
          })
    },
    YXXyin(){
      this.radio1='遥信'
      this.handleNodeTime()
    },
    YCXyin(){
      this.radio1='遥测'
      this.handleNodeTime()
    },
    YKXyin(){
      this.radio1='遥控'
      this.handleNodeTime()
    },
    YTXyin(){
      this.radio1='遥调'
      this.handleNodeTime()
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
    handleNodeTime(node){
      if(node===undefined){
        this.xuanTime = this.PublicTime
        this.exceltime = this.PublicTime
      }
      else{
        this.radio1='遥信'
        this.PublicTime = node.label
        this.xuanTime = node.label
        this.exceltime = node.label
      }
      this.actAllTableData=[]
      var type=''
      var url = 'http://localhost:8090/Query/versionInfo'
      if(this.radio1==='遥信'){
        type='YX'
        return axios.get(url,{
          params: {
            station: this.xuanstation,
            time: this.xuanTime,
            type: type
          }
        }).then(ret => {
          for(let i=0;i<ret.data.data.length;i++){
                    this.actAllTableData[i]={
                      no:i+1,
                      info_name:ret.data.data[i].yxname,
                      dot:ret.data.data[i].dot_no
                    }
                  }
          this.ResultRandomKey = Math.random()
        })
      }
      else if(this.radio1==='遥测'){
        type='YC'
        return axios.get(url,{
          params: {
            station: this.xuanstation,
            time: this.xuanTime,
            type: type
          }
        }).then(ret => {
          for(let i=0;i<ret.data.data.length;i++){
                    this.actAllTableData[i]={
                      no:i+1,
                      info_name:ret.data.data[i].ycname,
                      dot:ret.data.data[i].dot_no
                    }
                  }
          this.ResultRandomKey = Math.random()
        })
      }
      else if(this.radio1==='遥控'){
        type='YK'
        return axios.get(url,{
          params: {
            station: this.xuanstation,
            time: this.xuanTime,
            type: type
          }
        }).then(ret => {
          for(let i=0;i<ret.data.data.length;i++){
                    this.actAllTableData[i]={
                      no:i+1,
                      info_name:ret.data.data[i].ykname,
                      dot:ret.data.data[i].dot_no
                    }
                  }
          this.ResultRandomKey = Math.random()
        })
      }
      else if(this.radio1==='遥调'){
        type='YT'
        return axios.get(url,{
          params: {
            station: this.xuanstation,
            time: this.xuanTime,
            type: type
          }
        }).then(ret => {
          for(let i=0;i<ret.data.data.length;i++){
                    this.actAllTableData[i]={
                      no:i+1,
                      info_name:ret.data.data[i].ytname,
                      dot:ret.data.data[i].dot_no
                    }
                  }
          this.ResultRandomKey = Math.random()
        })
      }
    },
    selectListtt(){
      var url = 'http://localhost:8090/Query/StationAllList'
      return axios.get(url).then(ret => {
        this.selectList=ret.data.data
      })
    },
    ToExcelTimeList(){
          this.optionss=[]
          this.D5000TimeList=[]
          var url = 'http://localhost:8090/Query/excelTimeList'
          return axios.get(url,{
              params: {
                  station: this.station,
              }
          }).then(ret => {
            for(let i=0;i<ret.data.data.length;i++){
              this.optionss[i]={value:ret.data.data[i],label:ret.data.data[i]}
              // this.D5000TimeList[i]={value:ret.data.data[i],label:ret.data.data[i]}
            }
            this.D5000TimeList=this.optionss
          })
        },
        ToExcel(){
            const station = this.station;
            const exceltime = this.exceltime;
            const link = document.createElement('a')
            link.href = `http://localhost:8090/Query/exportToExcel?station=${encodeURIComponent(station)}&exceltime=${encodeURIComponent(exceltime)}`
            link.download = 'xxx.xlsx'
            link.style.display = 'none'
            document.body.appendChild(link)
            link.click()
            link.remove()
          },
      changee(){
        if(this.checkState==='已校核'){
          const filteredData1 = this.PublicTimeList.filter(item => item.value.includes('*') || item.label.includes('*'));
          this.TimeData=filteredData1
        }
        if(this.checkState==='未校核'){
          const filteredData1 = this.PublicTimeList.filter(item => !item.value.includes('*') && !item.label.includes('*'));
          this.TimeData=filteredData1
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
  background-color: #669dd887 !important;
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
.annu{
    background: linear-gradient(0deg, rgba(0,194,183,0.2) 0%, rgba(0,194,183,0.99) 50%, rgba(0,194,183,0.2) 100%);
  }
::v-deep .el-input__inner {
  background-color: transparent;
  color: black;
  border: 1px solid #E4E4E4;
}
.yoy /deep/ .el-radio-button__inner {
    color:black;
    border-color: #03A095;
    background-color:transparent;
  }
  .yoy /deep/ .el-radio-button__orig-radio:checked + .el-radio-button__inner {
        color:white;
        background-color:#03A095;
        border-color: #03A095;
        opacity: 0.9;
  }
  .treee /deep/ .el-tree-node__label {
    font-size: 16px;
  }
  .jhjl /deep/ .el-table tr,
  .jhjl /deep/ .el-table td {
    background-color: transparent;
    color:black;
    padding:1px;
    height: 38.4px;
  }
.el-table /deep/ .el-table__body td {
    border-bottom: 1px solid #ebeef5; /* 添加底部边框作为下划线，可以根据需要调整颜色和样式 */
}
.jhjl /deep/ .el-table::before {
      background-color: #ebeef5
  }
  .dialogs /deep/ .el-dialog__title {
    font-size: 20px;
    font-weight: bold;
    color: black;
  }
/* 自定义禁用状态下的边框颜色 */
.yoy /deep/.el-input.is-disabled .el-input__inner {
  border-color: #e0e0e0; /* 设置禁用状态下的边框颜色为淡灰色 */
}
</style>