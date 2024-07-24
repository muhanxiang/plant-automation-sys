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
                <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;color: #33BBFF;box-shadow: 0 3px 0 0 #f8d568;">首页</el-button>
                <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="stationmanagement">校核管理</el-button>
                <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="historyquery">校核查询</el-button>
                <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="AlertMessage">告警信息</el-button>
                <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="VersionControl">版本控制</el-button>
            </el-button-group>
            <img src="../assets/imgs/y1.png" style="position: absolute;left: 70%;top: 47%;">
            <span style="color: white;position: absolute;left: 72%;top: 41%;color:#00AAFF ;">{{username}}</span>
            <span style="color: white;position: absolute;left: 72%;top: 62%;">江苏电网-临时组织</span>
            <el-badge :value=alertCount :max="99" class="item" style="position: absolute;left: 85.5%;top: 46.5%;"></el-badge>
              <img src="../assets/imgs/y2.png" style="position: absolute;left: 85%;top: 52%;width: 22px;height: 25px;cursor: pointer;">
            <img src="../assets/imgs/y3.png" style="position: absolute;left: 88%;top: 52%;width: 22px;height: 25px;cursor: pointer;" @click="test">
            <img src="../assets/imgs/y5.png" style="position: absolute;left: 91%;top: 52%;width: 22px;height: 25px;cursor: pointer;" type="primary" @click="butClick" >
            <img src="../assets/imgs/y6.png" style="position: absolute;left: 94%;top: 52%;width: 22px;height: 25px;cursor: pointer;" @click="tuichu">
        </div>
        <img src="../assets/imgs/logo1.png" style="position: absolute;top: 32px;left: 36%;">

        <div class="second-one"></div>
        <div class="second-one-t">
            <span style="font-size: 22px;color:#33BBFF;position: relative;left: 10px;top: 10px;">运行工况</span>
            <span style="position:relative;left: 80%;top: 4%;"> <img src="../assets/imgs/s-o1.png" style="background-size: 100%;"></span>             <!--    :cell-style="changeCellStyle" -->
            <el-table :data="tableData" border style="width:89.1%;position:relative;top: 55.5%;left: 5%;" :row-style="{height: '0'}" :span-method="objectSpanMethod" :cell-style="{padding:'1px 0'}" :row-class-name="tableRowClassName" :header-cell-style="{background:'#1D8ED2',color:'white'}">
                <el-table-column prop="ydcj" label="远动厂站" width="112px" align="center"></el-table-column>
                <el-table-column prop="nrjb" label="江苏苏州电网" width="112px" align="center"></el-table-column>
                <el-table-column prop="nrkj" label="江苏常州电网" width="112px" align="center"></el-table-column>
                <el-table-column prop="bjsf" label="江苏徐州电网" width="112px" align="center"></el-table-column>
                <el-table-column prop="more" label="更多厂站" width="112px" align="center">
                  <template>
                    <div @click="YXGKFormVisible=true">
                      <span style="color: #1D8ED2;">查看更多</span>
                    </div>
                  </template>
                </el-table-column>
            </el-table>
            <div id="pie-f" style="width: 400px;height: 300px;position:absolute; top: -5%;left: 40%;"></div>
            <div class="zzz" style="position: absolute;top:18%;left:-5px;">
                <div style="background-color: #18CAE6; border-radius:50%;height: 13.5px;width: 13.5px;position:absolute;left: 65px;top: 87px;"></div>
                <div style="background-color: #EBDF33; border-radius:50%;height: 13.5px;width: 13.5px;position:absolute;left: 65px;top: 127px;"></div>
                <span class="lab1">{{yxgkonlinerate}}</span>
                <span class="lab1" style="top: 40px;">{{yxgktotal}}</span>
                <span class="lab1" style="top: 80px;">{{ yxgkonline }}</span>
                <span class="lab1" style="top: 120px;">{{yxgkoffline}}</span>
                <span class="lab2" style="top: 5px;">厂站校核率</span>
                <span class="lab2" style="top: 45px;">厂站总数</span>
                <span class="lab2" style="top: 85px;">厂站已校核</span>
                <span class="lab2" style="top: 125px;">厂站未校核</span>
                <span style="position: absolute;top: 5px;left: 272px;">%</span>
                <span style="position: absolute;top: 43px;left: 272px;">座</span>
                <span style="position: absolute;top: 83px;left: 272px;">座</span>
                <span style="position: absolute;top: 123px;left: 272px;">座</span>
            </div>
        </div>

        <div class="second-two"></div>
        <div class="second-two-t">
            <span style="font-size: 22px;color:#33BBFF;position: relative;left: 10px;top: 10px;">校核次数统计</span>
            <br>
            <img src="../assets/imgs/tj1.png" style="position: absolute;left: 185px;top: 85px;">
            <img src="../assets/imgs/tj2.png" style="position: absolute;left: 165px;top: 122px;">
            <span style="position: absolute;font-size: 30px;width: 155px;height: 65px;left: 279px;top:183px;color: #00FF78;">{{checkcount}}</span>
            <span style="position: absolute;top:245px;left:298px;font-size: 16px;">次</span>
            <span style="position: absolute;top:315px;left:248px;font-size:20px;font-weight: bold;">累计完成校核</span>
        </div>

        <div class="second-three"></div>
        <div class="second-three-t">
            <span style="font-size: 22px;color:#33BBFF;position: relative;left: 10px;top: 10px;">最新校核记录</span>
            <div class="mess1" style="top:20%;font-size: 14px;">
              <span style="position: absolute;top: 9px;left: 20px;">{{top1recordName}}<br>{{top1recordTime}}</span>
              <span class="mess3">查看 》</span>
            </div>
            <div class="mess2" style="top:32%;font-size: 14px;">
              <span style="position: absolute;top: 9px;left: 20px;">{{top2recordName}}<br>{{top2recordTime}}</span>
              <span class="mess3">查看 》</span>
            </div>
            <div class="mess1" style="top:44%;font-size: 14px;">
              <span style="position: absolute;top: 9px;left: 20px;">{{top3recordName}}<br>{{top3recordTime}}</span>
              <span class="mess3">查看 》</span>
            </div>
            <div class="mess2" style="top:56%;font-size: 14px;">
              <span style="position: absolute;top: 9px;left: 20px;">{{top4recordName}}<br>{{top4recordTime}}</span>
              <span class="mess3">查看 》</span>
            </div>
            <div class="mess1" style="top:68%;font-size: 14px;">
              <span style="position: absolute;top: 9px;left: 20px;">{{top5recordName}}<br>{{top5recordTime}}</span>
              <!-- <a  href="https://www.baidu.com/"><span class="mess3">查看 》</span></a> -->
              <span class="mess3">查看 》</span>
            </div>
        </div>

        <div class="third-one"></div>
          <div class="third-one-t">
            <span style="font-size: 22px;color:#33BBFF;position: relative;left: 10px;top: 10px;">异常统计</span>
            <!-- <div id="yuan1" style="position: absolute;top: 119px;left: 60px;"></div>
            <div id="yuan2" style="position: absolute;top: 159px;left: 60px;"></div> -->
            <div style="position: absolute;top:8%;left:-5px;">
              <div style="background-color: #18CAE6; border-radius:50%;height: 13.5px;width: 13.5px;position:absolute;left: 65px;top: 87px;"></div>
                <div style="background-color: #EBDF33; border-radius:50%;height: 13.5px;width: 13.5px;position:absolute;left: 65px;top: 127px;"></div>
              <span class="lab3" style="top:85px;">厂站总数</span>
              <span class="lab3" style="top:83px;left: 273px;font-size: 16px;">座</span>
              <span class="lab3" style="top:125px;">厂站缺陷</span>
              <span class="lab3" style="top:123px;left: 273px;font-size: 16px;">座</span>
              <span class="lab4" style="top: 80px;">{{yctjtotal}}</span>
              <span class="lab4" style="top: 120px;">{{yctjdefect}}</span>
            </div>
            <el-table :data="tableData1" border style="width:89.1%;position:relative;top: 60%;left: 5%;" :row-style="{height: '0'}" :span-method="objectSpanMethod1" :cell-style="{padding:'1px 0'}" :row-class-name="tableRowClassName" :header-cell-style="{background:'#1D8ED2',color:'white'}">
                <el-table-column prop="ydcj" label="远动厂站" width="112px" align="center"></el-table-column>
                <el-table-column prop="nrjb" label="江苏苏州电网" width="112px" align="center"></el-table-column>
                <el-table-column prop="nrkj" label="江苏常州电网" width="112px" align="center"></el-table-column>
                <el-table-column prop="bjsf" label="江苏徐州电网" width="112px" align="center"></el-table-column>
                <el-table-column prop="more" label="更多厂站" width="112px" align="center">
                  <template slot-scope="scope">
                    <div @click="handleMore(scope.row)">
                      <span style="color: #1D8ED2;">查看更多</span>
                    </div>
                  </template>
                </el-table-column>
            </el-table> 
          <div id="pie-s" style="width: 400px;height: 300px;position:absolute;top: -5%;left: 40%;"></div>
        </div>

        <div class="third-two"></div>
        <div class="third-two-t">
            <span style="font-size: 22px;color:#33BBFF;position: relative;left: 10px;top: 10px;">缺陷统计</span>
            <div id="zxt" style="width: 100%;height: 95%; position: relative;left: 0%;top:0%;"></div>
        </div>

        <div class="third-three"></div>
        <div class="third-three-t">
            <span style="font-size: 22px;color:#33BBFF;position: relative;left: 10px;top: 10px;">运维工具</span>
            <br><br>
            <a @click="lookfile"><img src="../assets/imgs/组1.png" style="position: absolute;left: 50px;top:68px"></a>
            <a href="https://www.baidu.com/"><img src="../assets/imgs/组2.png" style="position: absolute;left: 156px;top:68px"></a>
            <span  style="position: absolute;top: 138px;left: 50px;">文件管理</span>
            <span style="position: absolute;top: 138px;left: 156px;">设备升级</span>
        </div>

        <el-dialog title="基础信息" :visible.sync="dialogFormVisible">
            <el-form :model="form">
              <el-form-item label="南瑞继保-在线数" :label-width="formLabelWidth" style="position: absolute;left: 62%;">
                <el-input v-model="form.name" autocomplete="off" style="width: 65%;"></el-input>
              </el-form-item>
              <el-form-item label="南瑞继保-离线数" :label-width="formLabelWidth" style="position: absolute;left: 32%;">
                <el-input v-model="form.name" autocomplete="off" style="width: 65%;"></el-input>
              </el-form-item>
              <el-form-item label="在线" :label-width="formLabelWidth">
                <el-input v-model="yxgkonline1" autocomplete="off" style="width: 15%;"></el-input>
              </el-form-item>
              <el-form-item label="南瑞科技-在线数" :label-width="formLabelWidth" style="position: absolute;left: 62%;">
                <el-input v-model="form.name" autocomplete="off" style="width: 65%;"></el-input>
              </el-form-item>
              <el-form-item label="南瑞科技-离线数" :label-width="formLabelWidth" style="position: absolute;left: 32%;">
                <el-input v-model="form.name" autocomplete="off" style="width: 65%;"></el-input>
              </el-form-item>
              <el-form-item label="离线" :label-width="formLabelWidth">
                <el-input v-model="yxgkoffline1" autocomplete="off" style="width: 15%;"></el-input>
              </el-form-item>
              <el-form-item label="北京四方-在线数" :label-width="formLabelWidth" style="position: absolute;left: 62%;">
                <el-input v-model="form.name" autocomplete="off" style="width: 65%;"></el-input>
              </el-form-item>
              <el-form-item label="北京四方-离线数" :label-width="formLabelWidth" style="position: absolute;left: 32%;">
                <el-input v-model="form.name" autocomplete="off" style="width: 65%;"></el-input>
              </el-form-item>
              <el-form-item label="总数" :label-width="formLabelWidth" :disabled="false">
                <el-input v-model="yxgktotal1" style="width: 15%;"></el-input>
              </el-form-item>
              <el-form-item label="总数" :label-width="formLabelWidth">
                <el-input v-model="yctjtotal1" autocomplete="off" style="width: 15%;"></el-input>
              </el-form-item>
              <el-form-item label="缺陷" :label-width="formLabelWidth">
                <el-input v-model="yctjdefect1" autocomplete="off" style="width: 15%;"></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button type="primary" @click="homesubmit">提交</el-button>
              <el-button @click="dialogFormVisible = false">取 消</el-button>
            </div>
        </el-dialog>

        <el-dialog title="运行工况数据" :visible.sync="YXGKFormVisible" style="width: 153%;left: -26.5%;">
        <el-table :data="YXGKData" border :key="randomKey" style="color:black;width: 1510px;overflow:auto;top: -10px;" :header-cell-style="{background:'#1D8ED2',color:'white'}" height="528">
          <el-table-column prop="name" label="名称" width="300" align="center"></el-table-column>
          <el-table-column prop="allcount" label="总数" width="300" align="center"></el-table-column>
          <el-table-column prop="yicheck" label="已校核" width="300" align="center"></el-table-column>
          <el-table-column prop="weicheck" label="未校核" width="300" align="center"></el-table-column>
          <el-table-column prop="more" label="更多" width="228" align="center">
            <template slot-scope="scope">
                    <div @click="handleMore(scope.row)">
                      <span style="color: #1D8ED2;">查看更多</span>
                    </div>
            </template>
          </el-table-column>
        </el-table>
      </el-dialog>

    </div>

</template>

<script>
import { mapGetters } from 'vuex';
import axios from 'axios';
const colors = ['#FFFFFF', '#EE6666'];
export default {
  name: 'chart',
  computed: {
    ...mapGetters(['username']),
    },
  watch: {
  },
  data() {
      return {
        checkcount:'',
        top1recordName:'',
        top1recordTime:'',
        top2recordName:'',
        top2recordTime:'',
        top3recordName:'',
        top3recordTime:'',
        top4recordName:'',
        top4recordTime:'',
        top5recordName:'',
        top5recordTime:'',
        type:false,
        formLabelWidth: '120px',
        dialogFormVisible: false,
        YXGKFormVisible: false,
        paramsss: {
                yxgktotal:'',
                yxgkonline:'',
                yxgkoffline:'',
                yctjtotal:'',
                yctjdefect:''
              },
        form: {
          yxgktotal:'',
          yxgkonline:'',
          yxgkoffline:'',
          yctjtotal:'',
          yctjdefect:''
        },
        yxgktotal:'',
        alertCount:'',
        yxgkonline:'',
        yxgkoffline:'',
        yctjtotal:'',
        yctjdefect:'',
        randomKey:Math.random(),
        yxgkonlinerate:'',
        yxgktotal1:'',
        yxgkonline1:'',
        yxgkoffline1:'',
        yctjtotal1:'',
        yctjdefect1:'',
        yxgkonlinerate1:'',
        YXGKData1:[
          {name:'江苏电网',allcount:123,yicheck:23,weicheck:100},
          {name:'江苏苏州电网',allcount:100,yicheck:23,weicheck:77},
          {name:'江苏盐城电网',allcount:100,yicheck:53,weicheck:47},
        ],
        YXGKData:[],
        YCTJData:[],
        tableData: [{
          ydcj: '已校核数',
          nrjb: '100',
          nrkj: '80',
          bjsf: '90',
          more:'查看更多'
        }, {
          ydcj: '未校核数',
          nrjb: '30',
          nrkj: '60',
          bjsf: '40',
        }, {
          ydcj: '校核率',
          nrjb: '33.33%',
          nrkj: '26.66%',
          bjsf: '30.00%'
        }],
        tableData1: [{
          ydcj: '缺陷',
          nrjb: '10',
          nrkj: '35',
          bjsf: '5',
          more:'查看更多'
        }, {
          ydcj: '总数',
          nrjb: '100',
          nrkj: '100',
          bjsf: '100'
        }],
        chart: null,
        options: {}
      };
    },
    mounted () {
      this.initOptions()
      this.initCharts()
      this.YXGK_Data()
      this.homeData()
      this.RecordsData()
      this.timer = setInterval(() => {
        this.homeData()
      }, 5000);
      this.AlertCount()
      this.timer = setInterval(() => {
        this.AlertCount()
      }, 5000);
    },
    beforeDestroy(){
      clearInterval(this.timer)
      this.timer = null
    },
    methods: {
      home(){
        this.$router.push('/')
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
      yxgkhandleMore(){
        alert('运行工况')
      },
      handleMore(){
        alert('缺陷统计')
      },
      objectSpanMethod({rowIndex, columnIndex}){
        if (columnIndex === 4) {
          if (rowIndex % 3 === 0) {
            return {
              rowspan: 3,
              colspan: 1
            };
          } else {
            return {
              rowspan: 0,
              colspan: 0
            };
          }
        }
      },
      objectSpanMethod1({rowIndex, columnIndex}){
        if (columnIndex === 4) {
          if (rowIndex % 2 === 0) {
            return {
              rowspan: 2,
              colspan: 1
            };
          } else {
            return {
              rowspan: 0,
              colspan: 0
            };
          }
        }
      },
      initOptions () {
        this.options = {
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: ['05-01', '05-05', '05-10', '05-15', '05-20', '05-25', '05-30'],
            axisLine: {
              onZero: false,
              lineStyle: {
                color: colors[0]
              }
            }
          },
          yAxis: {
            type: 'value',
            axisLine: {
              onZero: false,
              lineStyle: {
                color: colors[0]
              }
            },
            splitLine: {
                show: true,
                lineStyle:{
                    type:'dashed',
                    opacity: 0.4
                }
            }
          },
          series: [
            {
              data: [6, 11, 7, 11, 5, 8, 10],
              type: 'line',
                smooth: 0.6,
                symbol: 'none',
                lineStyle: {
                    color: '#18CAE6',
                    width: 2
                },
            }
          ]
        }
      },
      initCharts () {
        this.chart = this.$echarts.init(document.getElementById('zxt'))
        this.chart.setOption(this.options)
      },
      getPieF() {
        var myChart = this.$echarts.init(
                document.getElementById('pie-f')
        );
        var datas = [
            [
                { name: '未校核', value: this.yxgkoffline },
                { name: '已校核', value: this.yxgkonline}
            ]
        ];
        var option = {
            color:['#EBDF33','#18CAE6'],
            series: datas.map(function (data, idx) {
                var top = idx * 33.3;
                return {
                type: 'pie',
                radius: [30, 60],
                top: top + '%',
                left: 'center',
                width: 440,
                label: {
                    alignTo: 'edge',
                    formatter: '{name|{b}} {time|{c} }\n\n',
                    minMargin: 5,
                    edgeDistance: 64,
                    lineHeight: 15,
                    rich: {
                      time: {
                          fontSize: 14,
                          color:'#ffffff'
                      },
                      name:{
                          fontSize: 14,
                          color:'#ffffff'
                      }
                    },
                },
                labelLine: {
                    length: 15,
                    length2: 0,
                    maxSurfaceAngle: 80
                },
                labelLayout: function (params) {
                    const isLeft = params.labelRect.x < myChart.getWidth() / 2;
                    const points = params.labelLinePoints;
                    // Update the end point.
                    points[2][0] = isLeft
                    ? params.labelRect.x
                    : params.labelRect.x + params.labelRect.width;
                    return {
                    labelLinePoints: points
                    };
                },
                data: data
                };
            })
        };
        myChart.setOption(option);
    },
    AlertCount(){
        var url = 'http://localhost:8090/Query/alertCount'
        return axios.get(url).then(ret => {
          //console.log(ret.data.count)
          this.alertCount=ret.data.count
        })
      },
    YXGK_Data(){
      var url = 'http://localhost:8090/Query/stationInfomation'
      return axios.get(url).then(ret => {
          for(let i=0;i<ret.data.data.length;i++){
            this.YXGKData[i]={
              name:ret.data.data[i].areaName,
              allcount:ret.data.data[i].allCount,
              yicheck:ret.data.data[i].yiCheck,
              weicheck:ret.data.data[i].weiCheck,
            }
          }
          this.randomKey = Math.random()
      })
    },
    getPieS() {
      var myChart = this.$echarts.init(
            document.getElementById('pie-s')
      );
      var datas = [
            [
                { name: '总数', value: this.yctjtotal},
                { name: '缺陷', value: this.yctjdefect}
            ]
        ];
      var option = {
        color:['#EBDF33','#18CAE6'],
        series: datas.map(function (data, idx) {
                var top = idx * 33.3;
                return {
                type: 'pie',
                radius: [30, 60],
                top: top + '%',
                left: 'center',
                width: 400,
                label: {
                    alignTo: 'edge',
                    formatter: '{name|{b}} {time|{c} }\n\n',
                    minMargin: 5,
                    edgeDistance: 64,
                    lineHeight: 15,
                    rich: {
                      time: {
                          fontSize: 14,
                          color:'#ffffff'
                          
                      },
                      name:{
                          fontSize: 14,
                          color:'#ffffff'
                      }
                    }
                },
                labelLine: {
                    length: 15,
                    length2: 0,
                    maxSurfaceAngle: 80
                },
                labelLayout: function (params) {
                    const isLeft = params.labelRect.x < myChart.getWidth() / 2;
                    const points = params.labelLinePoints;
                    // Update the end point.
                    points[2][0] = isLeft
                    ? params.labelRect.x
                    : params.labelRect.x + params.labelRect.width;
                    return {
                    labelLinePoints: points
                    };
                },
                data: data
                };
            })
      };
      myChart.setOption(option);
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
      homeData(){
        var url = 'http://localhost:8090/Query/home'
        return axios.get(url).then(ret => {
            //console.log(ret.data.data)
            this.checkcount=ret.data.data[0].checkcount
            this.yxgktotal=ret.data.data[0].yxgktotal
            this.yxgkonline=ret.data.data[0].yxgkonline
            this.yxgkoffline=ret.data.data[0].yxgkoffline
            this.yctjtotal=ret.data.data[0].yctjtotal
            this.yctjdefect=ret.data.data[0].yctjdefect
            this.yxgkonlinerate=ret.data.data[0].onlinerate
            this.getPieS()
            this.getPieF()
          })
      },
      RecordsData(){
        var url = 'http://localhost:8090/Query/checkRecords'
        return axios.get(url).then(ret => {
            let exAllTableData=ret.data.data
            for(let i=0;i<exAllTableData.length;i++){
              if(i==0){
                this.top1recordName = exAllTableData[0].comparison
                this.top1recordTime = exAllTableData[0].stringtime
              }
              if(i==1){
                this.top2recordName = exAllTableData[1].comparison
                this.top2recordTime = exAllTableData[1].stringtime
              }
              if(i==2){
                this.top3recordName = exAllTableData[2].comparison
                this.top3recordTime = exAllTableData[2].stringtime
              }  
              if(i==3){
                this.top4recordName = exAllTableData[3].comparison
                this.top4recordTime = exAllTableData[3].stringtime
              }  
              if(i==4){
                this.top5recordName = exAllTableData[4].comparison
                this.top5recordTime = exAllTableData[4].stringtime
              } 
            }
          })
      },
      test(){
        this.yxgktotal1=this.yxgktotal
        this.yxgkonline1=this.yxgkonline
        this.yxgkoffline1=this.yxgkoffline
        this.yctjtotal1=this.yctjtotal
        this.yctjdefect1=this.yctjdefect

        this.dialogFormVisible = true
      },
      homesubmit(){
          this.paramsss.yxgktotal=this.yxgktotal1
          this.paramsss.yxgkonline=this.yxgkonline1
          this.paramsss.yxgkoffline=this.yxgkoffline1
          this.paramsss.yctjtotal=this.yctjtotal1
          this.paramsss.yctjdefect=this.yctjdefect1
          var url = 'http://localhost:8090/Query/solveHome'
          return axios.post(url,this.paramsss).then(),
          this.dialogFormVisible = false
      },
      lookfile(){
      }
  }
};
</script>

<style scoped>

.second-one-t /deep/ .el-table, 
.second-one-t /deep/ .el-table__expanded-cell{
  background-color: transparent;
}
.second-one-t /deep/ .el-table tr,
.second-one-t /deep/ .el-table td {
  background-color: transparent!important;
  color:white;
}
.third-one-t /deep/ .el-table, 
.third-one-t /deep/ .el-table__expanded-cell{
  background-color: transparent;
}
.third-one-t /deep/ .el-table tr,
.third-one-t /deep/ .el-table td {
  background-color: transparent!important;
  color:white;
}
.b .el-button{
    border-radius: inherit;
}
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
.el-table th,
.el-table tr,
.el-table td {
  background-color: transparent;
}
#yuan1{
    width:2%;
    height:3%;
    background-color:#18CAE6 ;
    border-radius:50%;
}
#yuan2{
    width:2%;
    height:3%;
    background-color:#EBDF33 ;
    border-radius:50%;
}
.lab1{
    color: #00FF78;
    position: absolute;
    background-color:#1C8ECD;
    width: 88px;
    height: 24px;
    font-size: 18px;
    text-align: center;
    left: 176px;
    display: flex;
    padding-top: 0.1vw;
    justify-content: center;
}
.lab2{
    color: white;
    position: absolute;
    background-color: transparent;
    left: 85px;
    width: 70px;
    height: 12px;
    font-size: 14px;
  
}
.lab3{
    color: white;
    position: absolute;
    background-color: transparent;
    top:60px;
    left: 85px;
    width: 70px;
    height: 12px;
    font-size: 14px;
}
.lab4{
    color: #00FF78;
    position: absolute;
    background-color:#1C8ECD;
    width: 88px;
    height: 24px;
    font-size: 18px;
    text-align: center;
    left: 176px;
    display: flex;
    padding-top: 0.1vw;
    justify-content: center;
}
.mess1{
    position: absolute;
    width: 460px;
    height: 54px;
    background-color: #1C649F;
    left: 83px;
}
.mess2{
    position: absolute;
    width: 460px;
    height: 54px;
    background-color: transparent;
    left: 83px;
}
.mess3{
    position: absolute;
    top:40%;
    right: 10%;
    font-size: 15px;
    color: #00AAFF;
}
.second-one{
    color: white;
    position: absolute;
    height: 42.5%;
    width: 32.8%;
    left: 0.5%;
    top: 13%;

    opacity: 0.4;
    box-shadow: 0 0 3.5px 3.5px #fff inset;
}
.second-one-t{
    color: white;
    position: absolute;
    height: 42.5%;
    width: 32.8%;
    left: 0.5%;
    top: 13%;
}
.second-two{
    color: white;
    position: absolute;
    height: 42.5%;
    width: 32.8%;
    left: 33.7%;
    top: 13%;
    opacity: 0.4;
    box-shadow: 0 0 3.5px 3.5px #fff inset;
}
.second-two-t{
    color: white;
    position: absolute;
    height: 42.5%;
    width: 32.8%;
    left: 33.7%;
    top: 13%;
}
.second-three{
    color: white;
    position: absolute;
    height: 42.5%;
    width: 32.5%;
    left: 66.9%;
    top: 13%;
    opacity: 0.4;
    box-shadow: 0 0 3.5px 3.5px #fff inset;
}
.second-three-t{
    color: white;
    position: absolute;
    height: 42.5%;
    width: 32.5%;
    left: 66.9%;
    top: 13%;
}
.third-one{
    color: white;
    position: absolute;
    height: 42.5%;
    width: 32.8%;
    left: 0.5%;
    top: 56.4%;
    opacity: 0.4;
    box-shadow: 0 0 3.5px 3.5px #fff inset;
}
.third-one-t{
    color: white;
    position: absolute;
    height: 42.5%;
    width: 32.8%;
    left: 0.5%;
    top: 56.4%;
}
.third-two{
    color: white;
    position: absolute;
    height: 42.5%;
    width: 32.8%;
    left: 33.7%;
    top: 56.4%;
    opacity: 0.4;
    box-shadow: 0 0 3.5px 3.5px #fff inset;
}
.third-two-t{
    color: white;
    position: absolute;
    height: 42.5%;
    width: 32.8%;
    left: 33.7%;
    top: 56.4%;
}
.third-three{
    color: white;
    position: absolute;
    height: 42.5%;
    width: 32.5%;
    left: 66.9%;
    top: 56.4%;
    opacity: 0.4;
    box-shadow: 0 0 3.5px 3.5px #fff inset;
}
.third-three-t{
    color: white;
    position: absolute;
    height: 42.5%;
    width: 32.5%;
    left: 66.9%;
    top: 56.4%;
}
.table-one{
    position: relative;
    top: 50%;
}
</style>