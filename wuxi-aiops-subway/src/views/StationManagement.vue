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
                <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%; color: #33BBFF;box-shadow: 0 3px 0 0 #f8d568;" >校核管理</el-button>
                <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="historyquery">校核查询</el-button>
                <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="AlertMessage">告警信息</el-button>
                <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 88px;height: 30px;padding:1%;" @click="VersionControl">版本控制</el-button>
            </el-button-group>
            <img src="../assets/imgs/y1.png" style="position: absolute;left: 70%;top: 47%;">
            <span style="color: white;position: absolute;left: 72%;top: 41%;color:#00AAFF ;">{{username}}</span>
            <span style="color: white;position: absolute;left: 72%;top: 62%;">江苏电网-临时组织</span>
            <el-badge :value=alertCount :max="99" class="item" style="position: absolute;left: 85.5%;top: 46.2%;"></el-badge>
            <img src="../assets/imgs/y2.png" style="position: absolute;left: 85%;top: 52%;width: 22px;height: 25px;cursor: pointer;" @click="AlertMessage">
            <img src="../assets/imgs/y3.png" style="position: absolute;left: 88%;top: 52%;width: 22px;height: 25px;cursor: pointer;">
            <img src="../assets/imgs/y5.png" style="position: absolute;left: 91%;top: 52%;width: 22px;height: 25px;cursor: pointer;" type="primary" @click="butClick">
            <img src="../assets/imgs/y6.png" style="position: absolute;left: 94%;top: 52%;width: 22px;height: 25px;cursor: pointer;" @click="tuichu">
        </div>
        <img src="../assets/imgs/logo1.png" style="position: absolute;top: 32px;left: 36%;">

      <div id="itreee1" class="treee1" style="position: absolute;left: 0.5%;top: 13%;width: 350px;height: 86%;opacity: 0.4;box-shadow: 0 0 3.5px 3.5px #fff inset;border-radius: 4px;"></div>
      <div id="itree1" class="tree1" style="position: absolute;left: 0.5%;top: 13%;width: 350px;height: 86%">
          <hr style="position: absolute;background-color: #F8B858;width: 4px;height: 22px;left: 15px;top: 7px;">
          <span style="position: absolute;font-size: 20px;color: #33BBFF;font-weight: bold;top: 8px;left: 30px;">厂站索引</span>
          <hr style="position: absolute;width: 347px;top: 38px;opacity: 0.5;">
          <el-input v-model="filterText" style="position: absolute;top: 47px;width: 250px;left: 10px;"></el-input>
          <el-button style="position:absolute;top: 47px;left: 270px;background-color: transparent;color: white;border: 1px solid #2B7AB2;" @click.native="search">搜索</el-button>
          <hr style="position: absolute;width: 347px;top: 93px;opacity: 0.3;">
          <el-tree style="position: absolute;width: 340px;height: 85%;top: 108px;left: 5px;color: white;"
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
      <div id="iyddyy" class="yddyy" style="position: absolute;left: 19.2%;top: 13%;width: 40%;height: 63.8%;opacity: 0.4;box-shadow: 0 0 3.5px 3.5px #fff inset;border-radius: 4px;"></div>
      <div id="iyddy" class="yddy" style="position: absolute;left: 19.2%;top: 13%;width: 40%;height: 63.8%;">
          <hr style="position: absolute;background-color: #F8B858;width: 4px;height: 4.5%;left: 2%;top: 1.3%;">
          <span style="position: absolute;font-size: 20px;color: #33BBFF;font-weight: bold;top: 1.8%;left: 3.5%;">远动平面点表</span>
          <span style="position: absolute;font-size: 16px;left: 33.5%;top: 2.4%;">当前时间：</span>
          <el-select v-model="YDTime_New" placeholder="请选择" @change="YDSelectChange" style="position: absolute;left: 43%;top:0.7%">
            <el-option
              v-for="item in YDTimeList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
          <el-radio-group v-model="radio1" style="position: absolute;right: 0%;top:0.7%">
            <el-radio-button label="遥信" @click.native="yd1yaoxinfun"></el-radio-button>
            <el-radio-button label="遥测" @click.native="yd1yaocefun"></el-radio-button>
            <el-radio-button label="遥控" @click.native="yd1yaocongfun"></el-radio-button>
          </el-radio-group>
          <hr style="position: absolute;width: 99.5%;top: 8%;opacity: 0.5;left: 0%;">
          <el-table
            :data="yaoyd1finall"
            height="505"
            :key="randomKey"
            border
            style="width: 95%;top: 58px;left: 2.4%;"
            :header-cell-style="{background:'#1D8ED2',color:'white'}"
            :cell-style="dyydtab"
          >
            <el-table-column
              prop="num"
              label="序号"
              width="126"
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
              width="147"
              align="center">
            </el-table-column>
          </el-table>
      </div>
      <!-- <div id="iyddrr" class="yddrr" style="position: absolute;left: 886px;top: 13%;width: 508px;height: 63.8%;opacity: 0.4;box-shadow: 0 0 3.5px 3.5px #fff inset;border-radius: 4px;"></div>
      <div id="iyddr" class="yddr" style="position: absolute;left: 876px;top: 13%;width: 498px;height: 63.8%;">
          <hr style="position: absolute;background-color: #F8B858;width: 4px;height: 22px;left: 25px;top: 7px;">
          <span style="position: absolute;font-size: 20px;color: #33BBFF;font-weight: bold;top: 8px;left: 40px;">第二平面远动点表</span>
          <el-radio-group v-model="radio2" style="position: absolute;right: -20px;">
            <el-radio-button label="遥信" @click.native="yd2yaoxinfun"></el-radio-button>
            <el-radio-button label="遥测" @click.native="yd2yaocefun"></el-radio-button>
            <el-radio-button label="遥控" @click.native="yd2yaocongfun"></el-radio-button>
          </el-radio-group>
          <hr style="position: absolute;width: 505px;top: 38px;opacity: 0.5;left: 10px;">  
          <el-table
            :data="yaoyd2finall"
            height="512"
            :key="randomKey"
            border
            style="width: 95%;top: 58px;left: 27px;"
            :header-cell-style="{background:'#1D8ED2',color:'white'}"
            :cell-style="drydtab"
            >
            <el-table-column
              prop="num"
              label="序号"
              width="86"
              align="center">
            </el-table-column>
            <el-table-column
              prop="name"
              label="信息名称"
              width="300"
              align="center">
            </el-table-column>
            <el-table-column
              prop="address"
              label="点号"
              width="87"
              align="center">
            </el-table-column>
          </el-table>
      </div> -->

      <div id="id50000" class="d50000" style="position: absolute;left: 59.5%;top: 13%;width: 40%;height: 63.8%;opacity: 0.4;box-shadow: 0 0 3.5px 3.5px #fff inset;border-radius: 4px;"></div>
      <div id="id5000" class="d5000" style="position: absolute;left: 59.5%;top: 13%;width: 40%;height: 63.8%;">
          <hr style="position: absolute;background-color: #F8B858;width: 4px;height: 4.5%;left: 2%;top: 1.3%;">
          <span style="position: absolute;font-size: 20px;color: #33BBFF;font-weight: bold;top: 1.8%;left: 3.5%;">D5000点表</span>
          <span style="position: absolute;font-size: 16px;left: 33.5%;top: 2.4%;">当前时间：</span>
          <el-select v-model="D5000Time_New" placeholder="请选择" @change="D5000SelectChange" style="position: absolute;left: 43%;top:0.7%">
            <el-option
              v-for="item in D5000TimeList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
          <el-radio-group v-model="radio3" style="position: absolute;right: 0%;top:0.7%">
            <el-radio-button label="遥信" @click.native="d5000yaoxinfun"></el-radio-button>
            <el-radio-button label="遥测" @click.native="d5000yaocefun"></el-radio-button>
            <el-radio-button label="遥控" @click.native="d5000yaocongfun"></el-radio-button>
          </el-radio-group>
          <hr style="position: absolute;width: 99.5%;top: 8%;opacity: 0.5;left: 0%;"> 
          <el-table
            :data="D5000DataFinall"
            height="505"
            :key="randomKey"
            border
            style="width: 95%;top: 58px;left: 2.4%;"
            :header-cell-style="{background:'#1D8ED2',color:'white'}"
            :cell-style="d5000tab"
            >
            <el-table-column
              prop="num"
              label="序号"
              width="126"
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
              width="147"
              align="center">
            </el-table-column>
          </el-table>
      </div>
      <div id="ijhjgg" class="jhjgg" style="position: absolute;left: 369px;top: 77.7%;width: 1542px;height: 21.2%;opacity: 0.4;box-shadow: 0 0 3.5px 3.5px #fff inset;border-radius: 4px;"></div>
      <div id="ijhjg" class="jhjg" style="position: absolute;left: 369px;top: 77.7%;width: 1542px;height: 21.2%;">
        <hr style="position: absolute;background-color: #F8B858;width: 4px;height: 22px;left: 15px;top: 7px;">
        <span style="position: absolute;font-size: 20px;color: #33BBFF;font-weight: bold;top: 8px;left: 30px;">校核结果</span>
        <hr style="position: absolute;width: 1539px;opacity: 0.5;top: 46px;"> 
        <el-button style="position: absolute;top: 5px;right: 12px;color: white;background-color: #33BBFF;opacity: 0.2;width: 98px;height: 40px;" ></el-button>
        <el-button style="position: absolute;top: 5px;right: 120px;color: white;background-color: #33BBFF;opacity: 0.2;width: 142px;height: 40px;" ></el-button>
        <el-button style="position: absolute;top: 5px;right: 272px;color: white;background-color: #33BBFF;opacity: 0.2;width: 132px;height: 40px;" ></el-button>
        <el-button style="position: absolute;top: 5px;right: 415px;color: white;background-color: #33BBFF;opacity: 0.2;width: 98px;height: 40px;" ></el-button>

        <el-button style="position: absolute;top: 5px;right: 415px;color: white;background-color: transparent;" @click="exportDB">点表导出</el-button>
        <el-button slot="trigger" style="position: absolute;top: 5px;right: 272px;color: white;background-color: transparent;" @click="filetij">excel文件校核</el-button>
        <el-button style="position: absolute;top: 5px;right: 120px;color: white;background-color: transparent;" @click="d5000check">D5000规范校核</el-button>
        <el-button style="position: absolute;top: 5px;right: 12px;color: white;background-color: transparent;" @click="TreeDate">手动校核</el-button>
        <div style="position: absolute;left: 10px;top: 27%;width: 1520px;height: 66%;background-color:black;opacity: 0.3;border: 1px solid white;"></div>
          <div id="result" style="position: absolute;left: 10px;top: 57px;width: 1490px;height: 102px; color: white;background-color:transparent;">
            <span style="position: absolute;top: 10px;left: 20px;white-space: pre-wrap;">{{checkResultName}} {{checkResultTime}}</span>
            <span style="position: absolute;top: 50px;left: 20px;white-space: pre-wrap;">{{FileResultName}} {{FileResultTime}}</span>
            <el-button style="position: absolute;top: 2px;left: 450px;color: white;background-color: transparent;border: transparent;" @click="dialogTableVisible1 = true">{{checkResults}}</el-button>
            <el-button style="position: absolute;top: 38px;left: 439px;color: white;background-color: transparent;border: transparent;" @click="dialogTableVisible = true">{{FileResults}}</el-button>
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

      <el-dialog title="导入excel点表信息" :visible.sync="dialogVisible1">
      <el-form ref="importFormRef" :model="importForm" label-width="130px">
        <el-form-item label="厂站信息:" prop="kgCode" style="width: 60%;">
          <!-- <el-input v-model="importForm.kgCode" ></el-input> -->
          <el-select v-model="uploadtest.name" filterable placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="上传文件:" prop="excel">
          <el-upload :action="actionUrl" :on-success="handleUploadSuccess" :on-error="handleUploadError" :data="uploadtest" :show-file-list="true" accept=".xlsx, .xls">
            <el-button size="small" type="primary">点击上传</el-button>
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
      <el-dialog title="点表导出功能" :visible.sync="dialogVisible_db">
      <!-- <span style="position: absolute;top: 20%;left: 8%;">厂站名称：</span> -->
      <el-form ref="importFormRef" :model="importForm" label-width="130px">
        <el-form-item label="厂站信息:" prop="kgCode" style="width: 60%;">
          <el-input v-model="station" placeholder="请输入内容" :disabled="true" style="width: 48%;"></el-input>
        </el-form-item>
        <el-form-item label="时间信息:" prop="kgCode" style="width: 60%;">
          <el-select v-model="exceltime" filterable placeholder="请选择">
            <el-option
              v-for="item in optionss"
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
  computed: {
    ...mapGetters(['username']),
  },
  data() {
    return {
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
      yaoxin1:[{
          num: '1',
          name: '远动一遥信1#',
          address: '123'
        }, {
          num: '2',
          name: '远动一遥信2#',
          address: '123'
        }, {
          num: '3',
          name: '远动一遥信3#',
          address: '123'
        }, {
          num: '4',
          name: '远动一遥信4#',
          address: '123'
        }, {
          num: '5',
          name: '远动一遥信5#',
          address: '123'
        },
        {
          num: '6',
          name: '远动一遥信6#',
          address: '123'
        }, {
          num: '7',
          name: '远动一遥信7#',
          address: '123'
        }],
        yaoce1:[{
          num: '1',
          name: '远动一遥测1#',
          address: '123'
        }, {
          num: '2',
          name: '远动一遥测2#',
          address: '123'
        }, {
          num: '3',
          name: '远动一遥测3#',
          address: '123'
        }, {
          num: '4',
          name: '远动一遥测4#',
          address: '123'
        }, {
          num: '5',
          name: '远动一遥测5#',
          address: '123'
        }],
        yaocong1:[{
          num: '1',
          name: '远动一遥控1#',
          address: '123'
        }, {
          num: '2',
          name: '远动一遥控2#',
          address: '123'
        }, {
          num: '3',
          name: '远动一遥控3#',
          address: '123'
        }, {
          num: '4',
          name: '远动一遥控4#',
          address: '123'
        },{
          num: '5',
          name: '远动一遥控5#',
          address: '123'
        }, {
          num: '6',
          name: '远动一遥控6#',
          address: '123'
        }, {
          num: '7',
          name: '远动一遥控7#',
          address: '123'
        }, {
          num: '8',
          name: '远动一遥控8#',
          address: '123'
        }, {
          num: '9',
          name: '远动一遥控9#',
          address: '123'
        }],
        yaoxin2:[{
          num: '1',
          name: '远动二遥信1#',
          address: '123'
        }, {
          num: '2',
          name: '远动二遥信2#',
          address: '123'
        }, {
          num: '3',
          name: '远动二遥信3#',
          address: '123'
        }, {
          num: '4',
          name: '远动二遥信4#',
          address: '123'
        }, {
          num: '5',
          name: '远动二遥信5#',
          address: '123'
        }, {
          num: '6',
          name: '远动二遥信6#',
          address: '123'
        }, {
          num: '7',
          name: '远动二遥信7#',
          address: '123'
        }],
        yaoce2:[{
          num: '1',
          name: '远动二遥测1#',
          address: '123'
        }, {
          num: '2',
          name: '远动二遥测2#',
          address: '123'
        }, {
          num: '3',
          name: '远动二遥测3#',
          address: '123'
        }, {
          num: '4',
          name: '远动二遥测4#',
          address: '123'
        }, {
          num: '5',
          name: '远动二遥测5#',
          address: '123'
        }],
        yaocong2:[{
          num: '1',
          name: '远动二遥控1#',
          address: '123'
        }, {
          num: '2',
          name: '远动二遥控2#',
          address: '123'
        }, {
          num: '3',
          name: '远动二遥控3#',
          address: '123'
        }, {
          num: '4',
          name: '远动二遥控4#',
          address: '123'
        }, {
          num: '5',
          name: '远动二遥控5#',
          address: '123'
        }, {
          num: '6',
          name: '远动二遥控6#',
          address: '123'
        }, {
          num: '7',
          name: '远动二遥控7#',
          address: '123'
        }, {
          num: '8',
          name: '远动二遥控8#',
          address: '123'
        }, {
          num: '9',
          name: '远动二遥控9#',
          address: '123'
        }],
        d5000xin:[{
          num: '1',
          name: 'd5000遥信1#',
          address: '123'
        }, {
          num: '2',
          name: 'd5000遥信2#',
          address: '123'
        }, {
          num: '3',
          name: 'd5000遥信3#',
          address: '123'
        },{
          num: '4',
          name: 'd5000遥信4#',
          address: '123'
        }, {
          num: '5',
          name: 'd5000遥信5#',
          address: '123'
        }, {
          num: '6',
          name: 'd5000遥信6#',
          address: '123'
        },
        {
          num: '7',
          name: 'd5000遥信7#',
          address: '123'
        }, {
          num: '8',
          name: 'd5000遥信8#',
          address: '123'
        }],
        d5000yaoce:[{
          num: '1',
          name: 'd5000遥测1#',
          address: '123'
        }, {
          num: '2',
          name: 'd5000遥测2#',
          address: '123'
        }, {
          num: '3',
          name: 'd5000遥测3#',
          address: '123'
        }, {
          num: '4',
          name: 'd5000遥测4#',
          address: '123'
        }, {
          num: '5',
          name: 'd5000遥测5#',
          address: '123'
        }, {
          num: '6',
          name: 'd5000遥测6#',
          address: '123'
        }, {
          num: '7',
          name: 'd5000遥测7#',
          address: '123'
        }, {
          num: '8',
          name: 'd5000遥测8#',
          address: '123'
        }, {
          num: '9',
          name: 'd5000遥测9#',
          address: '123'
        }, {
          num: '10',
          name: 'd5000遥测10#',
          address: '123'
        }],
//遥控data        
        d5000yaocong:[{
          num: '1',
          name: 'd5000遥控1#',
          address: '123'
        }, {
          num: '2',
          name: 'd5000遥控2#',
          address: '123'
        }, {
          num: '3',
          name: 'd5000遥控3#',
          address: '123'
        }, {
          num: '4',
          name: 'd5000遥控4#',
          address: '123'
        }],
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
      gridData: [
        {
          no:1,
          filename:'NSC200.INI',
          ydono:'1',
          ydofilename:'17',
          ydtno:'1',
          ydtfilename:'19',
          result:'不一致',
          reason:'文件大小不同'
        },
        {
          no:2,
          filename:'NSGJ104.PAR',
          ydono:'2',
          ydofilename:'17',
          ydtno:'2',
          ydtfilename:'19',
          result:'不一致',
          reason:'文件内容不同'
        }, {
          no:3,
          filename:'NSNODE.DAT',
          ydono:'3',
          ydofilename:'17',
          ydtno:'3',
          ydtfilename:'19',
          result:'不一致',
          reason:'文件内容不同'
        }, {
          no:4,
          filename:'nsplc.dev',
          ydono:'4',
          ydofilename:'17',
          ydtno:'4',
          ydtfilename:'19',
          result:'不一致',
          reason:'文件内容不同'
        }, {
          no:5,
          filename:'updown.dat',
          ydono:'5',
          ydofilename:'17',
          ydtno:'5',
          ydtfilename:'19',
          result:'不一致',
          reason:'文件内容不同'
        }],
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
        testxx:{
          station:'三官殿',
          time:'2023-09-04 10:54:30'
        }
    };
  },
  mounted () {
     //this.yd1yaoxinfun()
    // this.yd2yaoxinfun()
    // this.d5000yaoxinfun()
    // this.ydoyaoxinfun()
    this.TreeDate()
    this.AlertCount()
      this.timer = setInterval(() => {
        this.AlertCount()
      }, 5000);
  },
  methods: {
    home(){
      this.$router.push('/Test1')
    },
    stationmanagement(){
      this.$router.push('/StationManagement')
    },
    historyquery(){
      this.$router.push('/HistoryQuery')
    },
    d5000check(){
      this.$router.push('/d5000check')
    },
    filetij(){
      this.uploadtest.name=[]
      this.dialogVisible1=true
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
          return 'background:rgba(28, 100, 159, 0.4); color:white';
      }
    },
    drydtab({rowIndex}){
      if (rowIndex %2!=0) {
          return 'background:rgba(28, 100, 159, 0.4); color:white;';
      }
    },
    d5000tab({rowIndex}){
      if (rowIndex %2!=0) {
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
    yd2yaocongfun(e){
      this.yaoyd2finall=[]
      if(e.target.tagName != 'INPUT'){
        return
      }
      for(let i=0;i<this.yaocong2.length;i++){
        this.yaoyd2finall[i]={
          num:this.yaocong2[i].num,
          name:this.yaocong2[i].name,
          address:this.yaocong2[i].address
        }
      }
      this.randomKey = Math.random()
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
      for(let i=0;i<this.d5000yaocong.length;i++){
        this.D5000DataFinall[i]={
          num:this.d5000yaocong[i].num,
          name:this.d5000yaocong[i].name,
          address:this.d5000yaocong[i].address
        }
      }
      this.randomKey = Math.random()
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
        var url = 'http://localhost:8090/Query/stationList'
        return axios.get(url).then(ret => {
          this.TreeData=ret.data.data
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
            //console.log(ret.data.data)
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
        // this.station=node.label
        const str='电网'
        //const sstr=':'
        const aaa=node.label
        if(aaa.includes(str)){
          //console.log()
          this.station=''
          this.xuanTime=''
          this.YDxuanTime=''
        }
        // else if(aaa.includes(sstr)){
        //   this.xuanTime=node.label
        //   this.Stationqury()
        //   this.ydoyxdata()
        //   this.ydtyxdata()
        //   this.checkResultsdata()
        //   this.fileResultsdata()
        //   this.fileCheckResultsdata()
        //   this.ToExcelTimeList()
        // }
        else{
          this.station=node.label
          this.xuanTime=''
          this.YDxuanTime=''
          this.Stationqury()
          this.ydoyxdata()
          this.ydtyxdata()
          this.checkResultsdata()
          this.fileResultsdata()
          this.fileCheckResultsdata()
          this.ToExcelTimeList()
          this.ToYDTimeList()
        }
        this.radio1='遥信'
        if(node.plabel!=null){
          this.yd1time=node.label,
          this.yd1station=node.plabel
          this.yd1yaoxinfun()
        }
      },
      handleUploadSuccess() {
        // 上传成功后，把返回的数据保存到uploadData中
        //this.uploadData = response.data;
        //this.dialogVisible1=false
        alert('上传成功!');
      },
      handleUploadError(){
        alert('上传失败!');
      },
      exportDB(){
        this.exceltime=''
        this.dialogVisible_db=true;
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
  color: white;
}
</style>