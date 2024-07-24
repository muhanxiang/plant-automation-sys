<template>
    <div class="a">
        <div class="b">
              <img src="../assets/imgs/logo_point.png" style="position: absolute;top: 13%;left: 1%;">
              <span style="color: white;position: absolute;left:11.5%;top:8.5%;font-size: 25px;font-weight: bold;line-height: 48px;">厂站自动化点表管理及信息核对系统</span>
              <el-button-group style="position: absolute;top: 15%;left: 36%;">
                  <el-button  style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="home">首页</el-button>
                  <el-button class="annu" style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" >点表管理</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="historyquery">校核记录</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="AlertMessage">告警信息</el-button>
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
              <el-badge :value=alertCount :max="99" class="item" style="position: absolute;left: 76.9%;top: 26%;"></el-badge>
              <img src="../assets/imgs/y2.png" style="position: absolute;left: 76%;top: 25.5%;width: 22px;height: 23px;cursor: pointer;" @click="AlertMessage">
              <!-- <img src="../assets/imgs/y3.png" style="position: absolute;left: 76%;top: 25.5%;width: 22px;height: 23px;cursor: pointer;"> -->
              <img src="../assets/imgs/y5.png" style="position: absolute;left: 79%;top: 27%;width: 22px;height: 22px;cursor: pointer;" type="primary" @click="butClick">
              <img src="../assets/imgs/y6.png" style="position: absolute;left: 82%;top: 25.5%;width: 25px;height: 25px;cursor: pointer;" @click="tuichu">
          </div>
          <div style="position: absolute;left: 1%;top: 8.2%;width: 5%;height: 3%;font-weight: bold;color: #03A095;background-color: #F1F2F6;border-radius: 8px 8px 0px 0px;"><span style="position: absolute;left: 17%;top: 24%;">点表管理</span></div>
          <div style="position: absolute;left: 1%;top: 11%;width: 98%;height: 87.8%;background-color: #F1F2F6;border-radius: 0px 8px 8px 8px;">
            <span style="position: absolute;left: 1.4%;top: 1%;font-family: Microsoft YaHei;color: black;font-weight:bold">厂站索引</span>
            <span style="position: absolute;left: 21.5%;top: 1%;font-family: Microsoft YaHei;color: black;font-weight:bold">厂站点表</span>
            <span style="position: absolute;left: 61%;top: 1%;font-family: Microsoft YaHei;color: black;font-weight:bold">新一代点表</span>
            <span style="position: absolute;left: 21.5%;top: 78.3%;font-family: Microsoft YaHei;color: black;font-weight:bold">校核结果</span>
            <div class="treee" style="position: absolute;background-color: #FDFDFC;left: 0.7%;top: 4.2%;width: 19.1%;height: 94.5%;border-radius: 8px;box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);">
                <el-input v-model="filterText" style="position: absolute;top: 1.8%;width: 250px;left: 15px;background-color: #FFFFFF;border: 1px solid #E4E4E4;border-radius: 4px;"></el-input>
                <el-button style="position:absolute;top: 1.8%;left: 275px;width: 20.5%;background: #03A095;border-radius: 4px;color: white;border: 1px solid #03A095;" @click.native="search">搜索</el-button>
                <el-tree style="position: absolute;width: 340px;height: 89.5%;top: 9.2%;left: 10px;color: black;border: 1px solid #E0E0E0;border-radius: 8px;"
                        class="filter-tree"
                        accordion
                        :data="TreeData"
                        :highlight-current="true"
                        :props="defaultProps"
                        :filter-node-method="filterNode"
                        ref="tree"
                        @node-click="handleNodeClick"
                        >
                </el-tree>
            </div>
            <div id="iyddy" class="yddy" style="position: absolute;left: 20.7%;top: 4.2%;width: 39%;height: 73.8%;background: #FDFDFC;border-radius: 8px;box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);">
            <!-- <span style="position: absolute;font-size: 16px;left: 2.5%;top: 3.6%;color: black;">当前时间：</span>
            <el-select v-model="YDTime_New" placeholder="请选择" @change="YDSelectChange" style="position: absolute;left: 14%;top:1.5%">
                <el-option
                v-for="item in YDTimeList"
                :key="item.value"
                :label="item.label"
                :value="item.value">
                </el-option>
            </el-select> -->
            <el-radio-group v-model="radio1" style="position: absolute;right: 1.3%;top:1.5%">
                <el-radio-button label="遥信" @click.native="ydoyxdata"></el-radio-button>
                <el-radio-button label="遥测" @click.native="ydoycdata"></el-radio-button>
                <el-radio-button label="遥控" @click.native="ydoykdata"></el-radio-button>
                <el-radio-button label="遥调" @click.native="ydoytdata"></el-radio-button>
            </el-radio-group>
            <div style="position: absolute;width: 96.8%;height: 89%;top: 10%;left: 1.6%;background: #FFFFFF;border-radius: 8px;border: 1px solid #E0E0E0;">
                <el-table
                    :data="yaoyd1finall"
                    height="96%"
                    :key="randomKey"
                    border
                    style="width: 97.4%;top: 10px;left: 1.4%;"
                    :header-cell-style="{background:'#03A095',color:'white'}"
                    :cell-style="dyydtab"
                >
                    <el-table-column
                    prop="num"
                    label="序号"
                    width="100"
                    align="center">
                    </el-table-column>
                    <el-table-column
                    prop="name"
                    label="信息名称"
                    width="455"
                    align="center">
                    </el-table-column>
                    <el-table-column
                    prop="address"
                    label="点号"
                    width="133"
                    align="center">
                    </el-table-column>
                </el-table>
            </div>
        </div>
        <div id="id5000" class="d5000" style="position: absolute;left: 60.2%;top: 4.2%;width: 39%;height: 73.8%;background: #FDFDFC;box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);border-radius: 8px;">
          <!-- <span style="position: absolute;font-size: 16px;left: 2.5%;top: 3.6%;color: black;">当前时间：</span>
          <el-select v-model="D5000Time_New" placeholder="请选择" @change="D5000SelectChange" style="position: absolute;left: 14%;top:1.5%">
            <el-option
              v-for="item in D5000TimeList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select> -->
          <el-button style="position: relative;top: 1.8%;left: 1.5%;" @click="XYDPointButton()">点表更新记录</el-button>
          <el-radio-group v-model="radio3" style="position: absolute;right: 1.3%;top:1.5%">
            <el-radio-button label="遥信" @click.native="d5000yaoxinfun"></el-radio-button>
            <el-radio-button label="遥测" @click.native="d5000yaocefun"></el-radio-button>
            <el-radio-button label="遥控" @click.native="d5000yaocongfun"></el-radio-button>
            <el-radio-button label="遥调" @click.native="d5000yaotiaofun"></el-radio-button>
          </el-radio-group>
          <div style="position: absolute;width: 96.8%;height: 89%;top: 10%;left: 1.6%;background: #FFFFFF;border-radius: 8px;border: 1px solid #E0E0E0;">
            <el-table
              :data="D5000DataFinall"
              :key="randomKey"
              height="96%"
              style="width: 97.4%;top: 10px;left: 1.4%;overflow-y: auto;"
              :header-cell-style="{background:'#03A095',color:'white'}"
              :cell-style="d5000tab"
            >
              <el-table-column
                prop="num"
                label="序号"
                width="100"
                align="center">
              </el-table-column>
              <el-table-column
                prop="name"
                label="信息名称"
                width="455"
                align="center">
              </el-table-column>
              <el-table-column
                prop="address"
                label="点号"
                width="133"
                align="center">
              </el-table-column>
            </el-table>
          </div>
        </div>
        <div id="ijhjg" class="jhjg" style="position: absolute;background-color:#FDFDFC;left: 20.7%;top: 81%;width: 78.5%;height: 17.8%;border-radius: 8px;box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);">
          <el-button size="mini" slot="trigger" style="position: absolute;height: 25%;top: 3px;right: 1.8%;color: white;background-color: #03A095;border-color: #03A095;" @click="filetij">上传校核点表</el-button>
          <el-button size="mini" slot="trigger" style="position: absolute;height: 25%;top: 3px;right: 10%;color: white;background-color: #03A095;border-color: #03A095;" @click="exportDB">新一代点表导出</el-button>
          <div style="position: absolute;left: 0.6%;top: 29%;width: 98.8%;height: 68%;background-color:#FFFFFF;border: 1px solid #E0E0E0;border-radius: 8px;"></div>
            <div id="result" style="position: absolute;left: 10px;top: 57px;width: 98.7%;height: 50%; color: black;background-color:transparent;">
              <span style="position: absolute;top: 10px;left: 20px;white-space: pre-wrap;">{{checkResultName}} {{checkResultTime}}</span>
              <span style="position: absolute;top: 50px;left: 20px;white-space: pre-wrap;">{{FileResultName}} {{FileResultTime}}</span>
              <el-button style="position: absolute;top: 2px;left: 450px;color: black;background-color: transparent;border: transparent;">{{checkResults}}</el-button>
              <el-button style="position: absolute;top: 38px;left: 439px;color: black;background-color: transparent;border: transparent;">{{FileResults}}</el-button>
            </div>
        </div>
      </div>

      <div class="dialog">
        <el-dialog title="文件校验结果" :visible.sync="dialogTableVisible" style="width: 110%;left: -4%;">
          <el-table :data="filecheckData" style="color:black;width: 1150px;overflow:auto;top: -10px;" :header-cell-style="{background:'#1D8ED2',color:'white'}" height="528">
            <el-table-column  property="no" label="序号" width="70" align="center"></el-table-column>
            <el-table-column property="staname" label="厂站名称" width="140" align="center"></el-table-column>
            <el-table-column  label="第一平面远动" width="300" align="center">
              <el-table-column property="ydofilename" label="文件名" width="140" align="center"></el-table-column>
            </el-table-column>
            <el-table-column  label="第二平面远动" width="300" align="center">
              <el-table-column property="ydtfilename" label="文件名" width="140" align="center"></el-table-column>
            </el-table-column>
              <el-table-column property="result" label="检验结果" width="80" align="center"></el-table-column>
              <el-table-column property="reason" label="原因" width="446" align="center"></el-table-column>
          </el-table>
        </el-dialog>

        <el-dialog title="点表校验结果" :visible.sync="dialogTableVisible1" style="width: 153%;left: -26.5%;">
          <el-table :data="pointThorwData" style="color:black;width: 1510px;overflow:auto;top: -10px;" :header-cell-style="{background:'#1D8ED2',color:'white'}" height="528">
            <el-table-column  property="no" label="序号" width="70" align="center"></el-table-column>
            <el-table-column  label="遥信" width="300" align="center">
              <el-table-column property="yxname" label="名称" width="130" align="center"></el-table-column>
              <el-table-column property="ydoyx" label="远动一" width="70" align="center"></el-table-column>
              <el-table-column property="ydtyx" label="远动二" width="70" align="center"></el-table-column>
              <el-table-column property="d5000yx" label="D5000" width="70" align="center"></el-table-column>
            </el-table-column>
            <el-table-column  label="遥测" width="300" align="center">
              <el-table-column property="ycname" label="名称" width="130" align="center"></el-table-column>
              <el-table-column property="ydoyc" label="远动一" width="70" align="center"></el-table-column>
              <el-table-column property="ydoyc" label="远动二" width="70" align="center"></el-table-column>
              <el-table-column property="ydoyc" label="D5000" width="70" align="center"></el-table-column>
            </el-table-column>
            <el-table-column  label="遥控" width="300" align="center">
              <el-table-column property="ykname" label="名称" width="130" align="center"></el-table-column>
              <el-table-column property="ydoyk" label="远动一" width="70" align="center"></el-table-column>
              <el-table-column property="ydoyk" label="远动二" width="70" align="center"></el-table-column>
              <el-table-column property="ydoyk" label="D5000" width="70" align="center"></el-table-column>
            </el-table-column>
            <el-table-column property="result" label="检验结果" width="80" align="center"></el-table-column>
            <el-table-column property="reason" label="原因" width="260" align="center"></el-table-column>
          </el-table>
        </el-dialog>

        <el-dialog title="上传校核文件" :visible.sync="dialogVisible1">
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
              <el-button @click="dialogVisible1 = false">关闭窗口</el-button>
            </el-form-item>
          </el-form>
        </el-dialog>
      </div>
      <div class="dialogs">
        <el-dialog title="导出新一代点表信息" :visible.sync="dialogVisible_db">
        <el-form ref="importFormRef" :model="importForm" label-width="130px">
          <el-form-item label="厂站信息:" prop="kgCode" style="width: 60%;">
            <el-input v-model="station" placeholder="请输入内容" :disabled="true" style="width: 48%;"></el-input>
          </el-form-item>
          <!-- <el-form-item label="时间信息:" prop="kgCode" style="width: 60%;">
            <el-select v-model="exceltime" filterable placeholder="请选择">
              <el-option
                v-for="item in optionss"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item style="position:relative;left: 62%;">
            <el-button @click="ToExcel">点表导出</el-button>
            <el-button @click="dialogVisible_db = false">关闭窗口</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>

      <el-dialog title="新一代点表总览" :visible.sync="XYDPointHis" style="width: 100%;left: 0%;">
          <el-table :data="XydAllData" border :key="randomKey" style="color:black;width: 1510px;overflow:auto;top: -10px;" :cell-style="columnStyle" :header-cell-style="{background:'#03A095',color:'white'}" height="528">
            <el-table-column prop="no" label="序号" width="100" align="center"></el-table-column>
            <el-table-column prop="xydhis_name" label="名称" width="270" align="center"></el-table-column>
            <el-table-column prop="xydhis_time" label="时间" width="280" align="center"></el-table-column>
            <el-table-column prop="xydhis_state" label="状态" width="270" align="center">
              <template slot-scope="scope">
                    <div @click="handleStateClick(scope.row)">
                      {{ scope.row.xydhis_state}}
                    </div>
              </template>
            </el-table-column>
          </el-table>
      </el-dialog>

      <el-dialog title="新一代变电站/发电厂总览" :visible.sync="XYDCZHis" style="width: 100%;left: 0%;">
        <el-table :data="XydCZData" border :key="randomKey" style="color:black;width: 1510px;overflow:auto;top: -10px;" :header-cell-style="{background:'#03A095',color:'white'}" height="528">
          <el-table-column prop="no" label="序号" width="100" align="center"></el-table-column>
          <el-table-column prop="xydhis_name" label="名称" width="270" align="center"></el-table-column>
          <el-table-column prop="xydhis_time" label="时间" width="280" align="center"></el-table-column>
          <el-table-column prop="xydhis_state" label="状态" width="270" align="center">
            <template slot-scope="scope">
              <div
                @click="handleCellClick(scope.row)"
                :style="{ color: scope.row.xydhis_state === '异动' ? 'red' : 'inherit' }"
              >
                {{ scope.row.xydhis_state }}
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-dialog>

        <el-dialog title="异动详情" :visible.sync="changeDetail" style="width: 140%;left: -400px;">
          <el-table :data="changeDetailData" border :key="randomKey" style="color:black;width: 1510px;overflow:auto;top: -10px;" :header-cell-style="{background:'#03A095',color:'white'}" height="528">
            <el-table-column prop="no" label="序号" width="100" align="center"></el-table-column>
            <el-table-column prop="station_name" label="名称" width="270" align="center"></el-table-column>
            <el-table-column prop="checktime" label="时间" width="280" align="center"></el-table-column>
            <el-table-column prop="different_description" label="异动项" width="270" align="center"></el-table-column>
            <el-table-column prop="type" label="类型" width="200" align="center"></el-table-column>
            <el-table-column prop="reason" label="变动" width="200" align="center"></el-table-column>
          </el-table>
        </el-dialog>

    </div>
    <ModifyPwd :visible.sync="changePwd" @submit="handleChangePwd" />
    </div>
  </template>

  <script>
  import { mapGetters } from 'vuex';
  import axios from 'axios';
  export default {
    watch: {
      filterText(val) {
        this.$refs.tree.filter(val);
      }
    },
    components: {
      ModifyPwd: () => import('@/components/Modal/ModifyPwd'),
    },
    computed: {
      ...mapGetters(['username']),
    },
    data() {
      return {
        XYDPointHis:false,
        XYDCZHis:false,
        changeDetail:false,
        changePwd: false,
        D5000Time_New:'',
        YDTime_New:'',
        type:false,
        uploadData: {
          Sta_nameParam: '',
        },
        exceltime:'',
        uploadtest: {imgType: 'jpg', name: '暂无数据', imgSize: '12kb'},
        radio1: '遥信',
        radio2: '遥信',
        radio3: '遥信',
        yd1station:'',
        selectList:'',
        yd1time:'',
        alertCount:'',
        count: 1,
        station:'',
        randomKey:Math.random(),
        filterText:'',
        dialogVisible1:false,
        dialogVisible_db:false,
        //导入表单数据
        importForm:{
          kgCode:'',
          targetUrl:'',
          targetUsername:'',
          targetPassword:'',
        },
        //存放上传文件
        fileList: [],
        yaoyd1finall:[],
        yaoyd2finall:[],
        checkResultName:'',
        checkResultTime:'',
        checkResults:'',
        FileResultName:'',
        FileResultTime:'',
        FileResults:'',
        D5000DataFinall:[],
        XydAllData:[],
        XydCZData:[],
        changeDetailData:[],
        xuanTime:'',
        YDxuanTime:'',
        actionUrl: 'http://localhost:8090/Query/uploadExcel',
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
          optionss: [],
          YDTimeList:[],
          D5000TimeList:[],
          value: '',

        data:
        [{
          label: '江苏省调自动化',
          children: [
            {
            label: '南京',
            children: [{
              label: '武热',
              children:[
                { label:'2023.06.01'},
                { label:'2023.06.15'},
                { label:'2023.06.27'},
              ]
            }, {
              label: '阳城厂'
            }]
          },
          {
            label: '南通',
            children: [{
              label: '变电站1'
            }, {
              label: '变电站2'
            },
            {
              label: '变电站3'
            },
            {
              label: '变电站4'
            }]
          }],
        }],
        pointThorwData:[{
          no:1,
          yxname:'#1发电机一次调频测试-增负荷值',
          ydoyx:'144',
          ydtyx:'144',
          d5000yx:'144',
          ycname:'阳东I线电流值',
          ydoyc:'159',
          ydtyc:'159',
          d5000yc:'159',
          ykname:'',
          ydoyk:'',
          ydtyk:'',
          d5000yk:'',
          result:'一致',
          reason:'',
        },
        {
          no:2,
          yxname:'#7发电机AVR自动/手动值',
          ydoyx:'223',
          ydtyx:'224',
          d5000yx:'223',
          ycname:'#1发电机AGC返回指令值',
          ydoyc:'116',
          ydtyc:'116',
          d5000yc:'116',
          ykname:'',
          ydoyk:'',
          ydtyk:'',
          d5000yk:'',
          result:'不一致',
          reason:'遥信点号不一致',
        }
        ],
        filecheckData:[],
          dialogTableVisible: false,
          dialogTableVisible1: false,
          dialogFormVisible: false,
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
      this.bodyScale()
      window.onresize = () => {
        return (() => {
          this.bodyScale()
        })()
      }
      this.TreeDate()
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
        this.$router.push('/StationManagement')
      },
      historyquery(){
        this.$router.push('/CheckQuery')
      },
      d5000check(){
        this.$router.push('/d5000check')
      },
      filetij(){
        this.uploadtest.name=[]
        this.dialogVisible1=true
        this.selectListtt()
      },
      AlertMessage(){
        this.$router.push('/alertManagement')
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
      bodyScale(){
            let devicewidth = document.documentElement.clientWidth
            let scale = devicewidth / 1920
            document.body.style.zoom = scale
            // document.body.style.transform='scale(1)'
      },
      dyydtab({rowIndex}){
        if (rowIndex %2!=0) {
            return 'background:#EFEFEF; color:black';
        }
      },
      drydtab({rowIndex}){
        if (rowIndex %2!=0) {
            return 'background:rgba(28, 100, 159, 0.4); color:white;';
        }
      },
      d5000tab({rowIndex}){
        if (rowIndex %2!=0) {
            return 'background:#EFEFEF; color:black;';
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
      XYDPointButton(){
        this.XydALLDate()
        this.XYDPointHis = true
      },
      columnStyle({ row,columnIndex }) {
        if (columnIndex == 3 && row.xydhis_state=='正常') {
            return {color:'#32B16C'}
        }
        if (columnIndex == 3 && row.xydhis_state=='异动') {
            return {color:'red'}
        }
      },
      AlertCount(){
          var url = 'http://localhost:8090/Query/alertCount'
          return axios.get(url).then(ret => {
            this.alertCount=ret.data.count
          })
        },
        ydoyaoxinfun(){
          this.yaoyd1finall=[]
          if(this.station!=null){
              var url = 'http://localhost:8090/Query/ydoAll'
              return axios.get(url).then(ret => {
                for(let i=0;i<ret.data.data.length;i++){
                  this.yaoyd1finall[i]={
                    num:i+1,
                    name:ret.data.data[i].yxname,
                    address:ret.data.data[i].dot_no
                  }
                }
                this.randomKey = Math.random()
              })

            }
          this.randomKey = Math.random()
        },
      yd1yaocefun(e){
        this.yaoyd1finall=[]
        if(e.target.tagName != 'INPUT'){
          return
        }
        if(this.station!=null){
            var url = 'http://localhost:8090/Query/ydo'
            return axios.get(url,{
                params: {
                    station: this.yd1station,
                    time:this.yd1time,
                    type: 'YC'
                }
            }).then(ret => {
              for(let i=0;i<ret.data.data.length;i++){
                this.yaoyd1finall[i]={
                  num:i+1,
                  name:ret.data.data[i].ycname,
                  address:ret.data.data[i].dot_no
                }
              }
              this.randomKey = Math.random()
            })
          }
        this.randomKey = Math.random()
      },
      yd1yaocongfun(e){
        if(e.target.tagName != 'INPUT'){
          return
      }
        this.yaoyd1finall=[]
        var url = 'http://localhost:8090/Query/ydoAll'
              return axios.get(url,{
                params: {
                    type: 'YK'
                }
            }).then(ret => {
                for(let i=0;i<ret.data.data.length;i++){
                  this.yaoyd1finall[i]={
                    num:i+1,
                    name:ret.data.data[i].ykname,
                    address:ret.data.data[i].dot_no
                  }
                }
                this.randomKey = Math.random()
            })
      },
      d5000yaoxinfun(){
        this.D5000DataFinall=[]
        var url = 'http://localhost:8090/Query/d5000'
        return axios.get(url,{
              params: {
                  station: this.station,
                  type: 'YX'
              }
          }).then(ret => {
              let dd = []
              dd = ret.data.data
              for(let i=0;i<dd.length;i++){
                this.D5000DataFinall[i]={
                  num:i+1,
                  name:dd[i].yx_name,
                  address:dd[i].dot_no
                }
              }
              this.randomKey = Math.random()
          })
      },
      d5000yaocefun(e){
        this.D5000DataFinall=[]
        if(e.target.tagName != 'INPUT'){
          return
        }
        var url = 'http://localhost:8090/Query/d5000'
        return axios.get(url,{
              params: {
                  station: this.station,
                  type: 'YC'
              }
          }).then(ret => {
              let dd = []
              dd = ret.data.data
              for(let i=0;i<dd.length;i++){
                this.D5000DataFinall[i]={
                  num:i+1,
                  name:dd[i].yc_name,
                  address:dd[i].dot_no
                }
              }
              this.randomKey = Math.random()
          })
      },
      d5000yaocongfun(e){
        this.D5000DataFinall=[]
        if(e.target.tagName != 'INPUT'){
          return
        }
        var url = 'http://localhost:8090/Query/d5000'
        return axios.get(url,{
              params: {
                  station: this.station,
                  type: 'YK'
              }
          }).then(ret => {
              let dd = []
              dd = ret.data.data
              for(let i=0;i<dd.length;i++){
                this.D5000DataFinall[i]={
                  num:i+1,
                  name:dd[i].yk_name,
                  address:dd[i].dot_no
                }
              }
              this.randomKey = Math.random()
          })
      },
      d5000yaotiaofun(e){
        this.D5000DataFinall=[]
        if(e.target.tagName != 'INPUT'){
          return
        }
        var url = 'http://localhost:8090/Query/d5000'
        return axios.get(url,{
              params: {
                  station: this.station,
                  type: 'YT'
              }
          }).then(ret => {
              let dd = []
              dd = ret.data.data
              for(let i=0;i<dd.length;i++){
                this.D5000DataFinall[i]={
                  num:i+1,
                  name:dd[i].yt_name,
                  address:dd[i].dot_no
                }
              }
              this.randomKey = Math.random()
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
        TreeDate(){
          var url = 'http://localhost:8090/Query/StationAllTree'
          return axios.get(url).then(ret => {
            this.TreeData=ret.data.data
          })
        },
        XydALLDate(){
          var url = 'http://localhost:8090/Query/XydAllRecord'
          return axios.get(url).then(ret => {
            console.log(ret.data.data)
            for(let i=0;i<ret.data.data.length;i++){
              this.XydAllData[i]={
                no:i+1,
                xydhis_name:'新一代点表',
                xydhis_time:ret.data.data[i].aac_time,
                xydhis_state:ret.data.data[i].state
              }
            }
            this.randomKey = Math.random()
          })
        },
        Stationqury(){
        this.D5000DataFinall=[]
        this.D5000Time_New=''
        var url = 'http://localhost:8090/Query/d5000stationallquery'
        return axios.get(url,{
              params: {
                  station: this.station,
                  time: this.xuanTime
              }
          }).then(ret => {
              let dd = []
              dd = ret.data.data
              for(let i=0;i<dd.length;i++){
                this.D5000DataFinall[i]={
                  num:i+1,
                  name:dd[i].yx_name,
                  address:dd[i].dot_no
                }
                if(i==0){
                  this.D5000Time_New=dd[i].tttime
                }
              }
              this.randomKey = Math.random()
          })
        },
        ydoyxdata(){
        this.yaoyd1finall=[]
        this.YDTime_New=''
        var url = 'http://localhost:8090/Query/ydoYXData'
        return axios.get(url,{
              params: {
                  station: this.station,
                  time: this.YDxuanTime
              }
          }).then(ret => {
              let dd = []
              dd = ret.data.data
              for(let i=0;i<dd.length;i++){
                this.yaoyd1finall[i]={
                    num:i+1,
                    name:ret.data.data[i].yxname,
                    address:ret.data.data[i].dot_no
                  }
                  if(i==0){
                    this.YDTime_New=dd[i].tttime
                  }
              }
              this.randomKey = Math.random()
          })
        },
      ydoycdata(e){
        this.yaoyd1finall=[]
        this.YDTime_New=''
        if(e.target.tagName != 'INPUT'){
          return
        }
        var url = 'http://localhost:8090/Query/ydoYCData'
        return axios.get(url,{
              params: {
                  station: this.station,
                  time: this.YDxuanTime
              }
          }).then(ret => {
              let dd = []
              dd = ret.data.data
              for(let i=0;i<dd.length;i++){
                this.yaoyd1finall[i]={
                    num:i+1,
                    name:ret.data.data[i].ycname,
                    address:ret.data.data[i].dot_no
                  }
                  if(i==0){
                    this.YDTime_New=dd[i].tttime
                  }
              }
              this.randomKey = Math.random()
          })
      },
      ydoykdata(e){
        this.yaoyd1finall=[]
        this.YDTime_New=''
        if(e.target.tagName != 'INPUT'){
          return
        }
        var url = 'http://localhost:8090/Query/ydoYKData'
        return axios.get(url,{
              params: {
                  station: this.station,
                  time: this.YDxuanTime
              }
          }).then(ret => {
              let dd = []
              dd = ret.data.data
              for(let i=0;i<dd.length;i++){
                this.yaoyd1finall[i]={
                    num:i+1,
                    name:ret.data.data[i].ykname,
                    address:ret.data.data[i].dot_no
                  }
                  if(i==0){
                    this.YDTime_New=dd[i].tttime
                  }
              }
              this.randomKey = Math.random()
          })
      },
      ydoytdata(e){
        this.yaoyd1finall=[]
        this.YDTime_New=''
        if(e.target.tagName != 'INPUT'){
          return
        }
        var url = 'http://localhost:8090/Query/ydoYTData'
        return axios.get(url,{
              params: {
                  station: this.station,
                  time: this.YDxuanTime
              }
          }).then(ret => {
              let dd = []
              dd = ret.data.data
              for(let i=0;i<dd.length;i++){
                this.yaoyd1finall[i]={
                    num:i+1,
                    name:ret.data.data[i].ytname,
                    address:ret.data.data[i].dot_no
                  }
                  if(i==0){
                    this.YDTime_New=dd[i].tttime
                  }
              }
              this.randomKey = Math.random()
          })
      },
      ydtyxdata(){
        this.yaoyd2finall=[]
        var url = 'http://localhost:8090/Query/ydtYXData'
        return axios.get(url,{
              params: {
                  station: this.station,
              }
          }).then(ret => {
              let dd = []
              dd = ret.data.data
              for(let i=0;i<dd.length;i++){
                this.yaoyd2finall[i]={
                    num:i+1,
                    name:ret.data.data[i].yxname,
                    address:ret.data.data[i].dot_no
                  }
              }
              this.randomKey = Math.random()
          })
        },
        checkResultsdata(){
          this.checkResultName=''
          this.checkResultTime=''
          this.checkResults=''
        var url = 'http://localhost:8090/Query/stationCheckRecords'
        return axios.get(url,{
              params: {
                  station: this.station,
              }
          }).then(ret => {
            this.checkResultName=ret.data.data[0].comparison+' 点表校核       '
            this.checkResultTime=ret.data.data[0].stringtime
            this.checkResults='结果：'+ret.data.data[0].checkstate
          })
        },
        D5000SelectChange(){
          this.xuanTime = this.D5000Time_New
          this.Stationqury()
        },
        YDSelectChange(){
          this.YDxuanTime = this.YDTime_New
          this.ydoyxdata()
        },
        fileResultsdata(){
          this.FileResultName=''
          this.FileResultTime=''
          this.FileResults=''
        var url = 'http://localhost:8090/Query/FileCheckRecords'
        return axios.get(url,{
              params: {
                  station: this.station,
              }
          }).then(ret => {
            this.FileResultName=ret.data.data[0].comparison+' 文件校核       '
            this.FileResultTime=ret.data.data[0].timestring
            this.FileResults='结果：'+ret.data.data[0].checkstate
          })
        },
        fileCheckResultsdata(){
          this.filecheckData=[]
          var url = 'http://localhost:8090/Query/FileCheck'
          return axios.get(url,{
                params: {
                    station: this.station,
                }
            }).then(ret => {
              let dd = []
              dd = ret.data.data
              for(let i=0;i<dd.length;i++){
                this.filecheckData[i]={
                    no:i+1,
                    staname:ret.data.data[i].st_name,
                    ydofilename:ret.data.data[i].ydo_filename,
                    ydtfilename:ret.data.data[i].ydt_filename,
                    result:ret.data.data[i].result,
                    reason:ret.data.data[i].reason,
                  }
              }
            })
        },
        submitImportForm(){
          console.log(this.uploadData)
        },
        handleNodeClick(node){
          const str='电网'
          const aaa=node.label
          if(aaa.includes(str)){
            this.station=''
            this.xuanTime=''
            this.YDxuanTime=''
          }
          else{
            this.station=node.label
            this.xuanTime=''
            this.YDxuanTime=''
            this.d5000yaoxinfun()
            this.ydoyxdata()
            this.checkResultsdata()
            this.fileResultsdata()
          }
          this.radio1='遥信'
          this.radio3='遥信'
          if(node.plabel!=null){
            this.yd1time=node.label,
            this.yd1station=node.plabel
            this.yd1yaoxinfun()
          }
        },
        async handleUploadSuccess(response) {
          var url = 'http://localhost:8090/Query/upcity'
          localStorage.setItem('station', this.uploadtest.name)
          localStorage.setItem('time', response.data[0])
          try{
            const res = await axios.get(url,{
              params: {
                station: this.uploadtest.name
              }})
            localStorage.setItem('city', res.data.data[0])
            console.log(res)
          }
          catch (error) {
            //处理请求错误
            console.log(error)
          }
          alert('上传成功!');
          this.dialogVisible1=false;
          this.$router.push('/CheckQuery');
        },
        handleUploadError(){
          alert('上传失败!');
        },
        exportDB(){
          this.exceltime=''
          this.dialogVisible_db=true;
        },
        handleStateClick(abc){
          this.XYDCZHis = true
          this.XydCZData = []
          var url = 'http://localhost:8090/Query/AcStation'
          return axios.get(url,{
                params: {
                    time: abc.xydhis_time,
                }
            }).then(ret => {
              for(let i=0;i<ret.data.data.length;i++){
                this.XydCZData[i]={no:i+1,xydhis_name:ret.data.data[i].station_name,xydhis_time:ret.data.data[i].st_time,xydhis_state:ret.data.data[i].state}
              }
              this.randomKey = Math.random()
            })
        },
      handleCellClick(row){
        if(row.xydhis_state==='异动'){
          this.changeDetail=true
          this.changeDetailData=[]
          var url = 'http://localhost:8090/Query/changeDetail'
          return axios.get(url,{
            params:{
              stationName:row.xydhis_name,
              time:row.xydhis_time
            }
          }).then(ret => {
            for(let i=0;i<ret.data.data.length;i++){
              this.changeDetailData[i]={no:i+1,station_name:ret.data.data[i].station_name,checktime:ret.data.data[i].time,different_description:ret.data.data[i].different_description,type:ret.data.data[i].type,reason:ret.data.data[i].reason}
            }
            this.randomKey = Math.random()
          })
        }
      },
        ToExcel(){
            const station = this.station;
            const link = document.createElement('a')
          console.log(station)
          console.log(link)
            link.href = `http://localhost:8090/Query/exportToExcel123?station=${encodeURIComponent(station)}`
            link.download = 'xxx.xlsx'
            link.style.display = 'none'
            document.body.appendChild(link)
            link.click()
            link.remove()
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
            }
            this.D5000TimeList=this.optionss
          })
        },
        ToYDTimeList(){
          this.optionss=[]
          this.D5000TimeList=[]
          var url = 'http://localhost:8090/Query/YDTimeList'
          return axios.get(url,{
              params: {
                  station: this.station,
              }
          }).then(ret => {
            for(let i=0;i<ret.data.data.length;i++){
              this.YDTimeList[i]={value:ret.data.data[i],label:ret.data.data[i]}
            }
          })
        },
        selectListtt(){
          var url = 'http://localhost:8090/Query/StationAllList'
          return axios.get(url).then(ret => {
            this.selectList=ret.data.data
          })
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
  .tree1 /deep/ .el-tree {
    background: none;
    overflow: auto;
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
  .tree1 /deep/ .el-tree .is-current > .el-tree-node__content {
    background-color: #66b1ff87; /* 自定义高亮颜色 */
  }
  .yddy /deep/ .el-table,
  .yddy /deep/ .el-table__expanded-cell{
    background-color: transparent;
  }
  .yddy /deep/ .el-table tr,
  .yddy /deep/ .el-table td {
    background-color: transparent;
    color:black;
    padding:4px
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
      background-color: #2B7AB2;
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
    color:black;
    padding:4px
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
      background-color: #2B7AB2;
      border-radius: 3px;
  }
  .yddy /deep/ .el-radio-button__inner {
    /* color:white; */
    color:black;
    border-color: #03A095;
    background-color:transparent;
  }
  .yddy /deep/ .el-radio-button__orig-radio:checked + .el-radio-button__inner {
        color:white;
        background-color:#03A095;
        border-color: #03A095;
        opacity: 0.9;
  }

  .d5000 /deep/ .el-radio-button__inner {
    color:black;
    border-color: #03A095;
    background-color:transparent;
  }
  .d5000 /deep/ .el-radio-button__orig-radio:checked + .el-radio-button__inner {
        background-color:#03A095;
        border-color: #03A095;
        color: white;
        opacity: 0.9;
  }
  .dialog /deep/ .el-dialog__title {
    font-size: 20px;
    font-weight: bold;
    color: black;
  }
  .dialogs /deep/ .el-dialog__title {
    font-size: 20px;
    font-weight: bold;
    color: black;
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
  ::v-deep .el-input__inner {
    background-color: transparent;
    color: black;
    border: 1px solid #E4E4E4;
  }
  .treee /deep/ .el-tree {
    background: none;
    overflow: auto;
  }
  .treee /deep/ .el-tree-node__content:hover {
    background-color: #66b1ff87;
  }
  .treee /deep/ .el-tree-node__label {
    font-size: 16px;
  }
  .treee /deep/ .el-tree-node:focus > .el-tree-node__content {
    background-color: #03A095 !important;
    color: white;
  }
  .treee /deep/ .el-tree .is-current > .el-tree-node__content {
    background-color: #03A095;
    color: white;
  }
  .annu{
    background: linear-gradient(0deg, rgba(0,194,183,0.2) 0%, rgba(0,194,183,0.99) 50%, rgba(0,194,183,0.2) 100%);
  }
</style>