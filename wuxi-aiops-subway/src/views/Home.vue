<template>
    <div class="a">
        <div class="b">
          <img src="../assets/imgs/logo_point.png" style="position: absolute;top: 13%;left: 1%;">
              <span style="color: white;position: absolute;left:11.5%;top:8.5%;font-size: 25px;font-weight: bold;line-height: 48px;">厂站自动化点表管理及信息核对系统</span>
              <el-button-group style="position: absolute;top: 15%;left: 36%;">
                  <el-button class="annu" style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;">首页</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="stationmanagement">点表管理</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="historyquery">校核记录</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="AlertMessage">告警信息</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="VersionControl">版本管理</el-button>
              </el-button-group>
              <img src="../assets/imgs/y1.png" style="position: absolute;left: 86.5%;top: 25%;">
              <!-- <span style="color: white;position: absolute;left: 89%;top: 28%;color:#FFFFFF ;">{{username}}</span> -->
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
              <!-- <span style="color: white;position: absolute;left: 91.5%;top: 29%;">江苏电网-临时组织</span> -->
              <el-badge :value=alertCount :max="99" class="item" style="position: absolute;left: 76.9%;top: 26%;"></el-badge>
              <img src="../assets/imgs/y2.png" style="position: absolute;left: 76%;top: 25.5%;width: 22px;height: 23px;cursor: pointer;" @click="AlertMessage">
              <!-- <img src="../assets/imgs/y3.png" style="position: absolute;left: 76%;top: 25.5%;width: 22px;height: 23px;cursor: pointer;" @click="Testt"> -->
              <img src="../assets/imgs/y5.png" style="position: absolute;left: 79%;top: 27%;width: 22px;height: 22px;cursor: pointer;" type="primary" @click="butClick">
              <img src="../assets/imgs/y6.png" style="position: absolute;left: 82%;top: 25.5%;width: 25px;height: 25px;cursor: pointer;" @click="tuichu">
        </div>
        <div style="position: absolute;left: 1%;top: 8.2%;width: 5%;height: 3%;font-weight: bold;color: #03A095;background-color: #F1F2F6;border-radius: 8px 8px 0px 0px;"><span style="position: absolute;left: 32%;top: 24%;">首页</span></div>
        <div style="position: absolute;left: 1%;top: 11%;width: 98%;height: 87.8%;background-color: #F1F2F6;border-radius: 0px 8px 8px 8px;">
          <span style="position: absolute;left: 1.4%;top: 2%;color: black;font-weight:bold;">运行工况</span>
          <span style="position: absolute;left: 34.5%;top: 2%;font-family: Microsoft YaHei;color: black;font-weight:bold">校核次数统计</span>
          <span style="position: absolute;left: 67.5%;top: 2%;font-family: Microsoft YaHei;color: black;font-weight:bold">最新校核记录</span>
          <span style="position: absolute;left: 1.4%;top: 51.2%;font-family: Microsoft YaHei;color: black;font-weight:bold">异常统计</span>
          <span style="position: absolute;left: 34.5%;top: 51.2%;font-family: Microsoft YaHei;color: black;font-weight:bold">缺陷统计</span>
          <span style="position: absolute;left: 67.5%;top: 51.2%;font-family: Microsoft YaHei;color: black;font-weight:bold">运维工具</span>
          <div class="second-one-t">
                    <div class="zz" style="position: absolute;left: 1.5%;top: 2.5%;width: 97%;height: 95%;background: #FFFFFF;border-radius: 8px;border: 1px solid #E0E0E0;">
                        <el-table :data="typeTableData" border style="width:96%;position:relative;top: 55.5%;left: 2%;" :row-style="{height: '0'}"  :cell-style="{padding:'1px 0'}" :row-class-name="tableRowClassName" :header-cell-style="{background:'#03A095',color:'white',borderColor:'#C0C0C0'}">
                        <el-table-column prop="stationType" label="厂站类型" width="112px" align="center"></el-table-column>
                          <el-table-column prop="stationCount" label="总数" width="112px" align="center"></el-table-column>
                          <el-table-column prop="checkCount" label="已校核数" width="112px" align="center"></el-table-column>
                        <el-table-column prop="uncheckCount" label="未校核数" width="112px" align="center"></el-table-column>
                        <el-table-column prop="checkRate" label="校核率" width="112px" align="center"></el-table-column>
                        <el-table-column prop="more" label="详细信息" width="114px" align="center"  v-slot:default="scope">
                        <template>
                            <div @click="handleVoltDisplay(scope.row)">
                            <span style="color: #03A095;cursor: pointer;">查看更多</span>
                            </div>
                        </template>
                        </el-table-column>
                    </el-table>
                    <div id="pie-f" style="width: 400px;height: 300px;position:absolute; top: -14%;left: 40%;"></div>
                    <div class="zzz" style="position: absolute;top:5%;left:-5%;">
                        <div style="background-color: #31B36A ; border-radius:50%;height: 13.5px;width: 13.5px;position:absolute;left: 65px;top: 87px;"></div>
                        <div style="background-color: #FFAC04; border-radius:50%;height: 13.5px;width: 13.5px;position:absolute;left: 65px;top: 127px;"></div>
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

                </div>
        <div class="second-two-t">
            <div style="position: absolute;left: 1.5%;top: 2.5%;width: 97%;height: 95%;background: #FFFFFF;border-radius: 8px;border: 1px solid #E0E0E0;">
            </div>
            <img src="../assets/imgs/tj1.png" style="position: absolute;left: 185px;top: 45px;">
            <img src="../assets/imgs/tj2.png" style="position: absolute;left: 165px;top: 82px;">
            <span style="position: absolute;font-size: 30px;width: 20%;height: 20%;left: 46%;top:148px;color: #03A095;">{{checkcount}}</span>
            <span style="position: absolute;top:205px;left:298px;font-size: 16px;">次</span>
            <span style="position: absolute;top:275px;left:248px;font-size:20px;font-weight: bold;">累计完成校核</span>
        </div>
        <div class="second-three-t">
            <div style="position: absolute;left: 1.5%;top: 2.5%;width: 97%;height: 95%;background: #FFFFFF;border-radius: 8px;border: 1px solid #E0E0E0;">
                <div class="mess1" style="top:8%;font-size: 14px;">
                    <span style="position: absolute;top: 9px;left: 20px;">{{top1recordName}}<br>{{top1recordTime}}</span>
                    <span class="mess3" style="cursor: pointer;" @click="handleStation1()">查看 》</span>
                </div>
                <div class="mess2" style="top:25%;font-size: 14px;">
                    <span style="position: absolute;top: 9px;left: 20px;">{{top2recordName}}<br>{{top2recordTime}}</span>
                    <span class="mess3" style="cursor: pointer;" @click="handleStation2()">查看 》</span>
                </div>
                <div class="mess1" style="top:42%;font-size: 14px;">
                    <span style="position: absolute;top: 9px;left: 20px;">{{top3recordName}}<br>{{top3recordTime}}</span>
                    <span class="mess3" style="cursor: pointer;" @click="handleStation3()">查看 》</span>
                </div>
                <div class="mess2" style="top:59%;font-size: 14px;">
                    <span style="position: absolute;top: 9px;left: 20px;">{{top4recordName}}<br>{{top4recordTime}}</span>
                    <span class="mess3" style="cursor: pointer;" @click="handleStation4()">查看 》</span>
                </div>
                <div class="mess1" style="top:76%;font-size: 14px;">
                    <span style="position: absolute;top: 9px;left: 20px;">{{top5recordName}}<br>{{top5recordTime}}</span>
                    <span class="mess3" style="cursor: pointer;" @click="handleStation5()">查看 》</span>
                </div>
            </div>
        </div>
        <div class="third-one-t">
            <div class="zzt" style="position: absolute;left: 1.5%;top: 2.5%;width: 97%;height: 95%;background: #FFFFFF;border-radius: 8px;border: 1px solid #E0E0E0;">
            <div style="position: absolute;top:-4%;left:-5%;">
              <div style="background-color: #31B36A; border-radius:50%;height: 13.5px;width: 13.5px;position:absolute;left: 65px;top: 87px;"></div>
              <div style="background-color: #FFAC04; border-radius:50%;height: 13.5px;width: 13.5px;position:absolute;left: 65px;top: 127px;"></div>
              <span class="lab3" style="top:85px;">厂站总数</span>
              <span class="lab3" style="top:83px;left: 273px;font-size: 16px;">座</span>
              <span class="lab3" style="top:125px;">缺陷厂站</span>
              <span class="lab3" style="top:123px;left: 273px;font-size: 16px;">座</span>
              <span class="lab4" style="top: 80px;">{{yctjtotal}}</span>
              <span class="lab4" style="top: 120px;">{{yctjdefect}}</span>
            </div>
            <el-table :data="typeTableData" border style="width:96%;position:relative;top: 60%;left: 2%;" :row-style="{height: '0'}" :cell-style="{padding:'1px 0'}" :row-class-name="tableRowClassName" :header-cell-style="{background:'#03A095',color:'white',borderColor:'#C0C0C0'}">
                <el-table-column prop="stationType" label="厂站类型" width="112px" align="center"></el-table-column>
              <el-table-column prop="stationCount" label="总数" width="112px" align="center"></el-table-column>
                <el-table-column prop="faultCount" label="缺陷数" width="112px" align="center"></el-table-column>
              <el-table-column prop="faultRate" label="缺陷率" width="112px" align="center"></el-table-column>
                <el-table-column prop="more" label="详细信息" width="114px" align="center" v-slot:default="scope">
                  <template>
                    <div @click="handleVoltDisplay1(scope.row)">
                      <span style="color: #03A095;cursor: pointer;">查看更多</span>
                    </div>
                  </template>
                </el-table-column>
            </el-table>
          <div id="pie-s" style="width: 400px;height: 300px;position:absolute;top: -12.5%;left: 40%;"></div>
            </div>
        </div>
        <div class="third-two-t">
            <div style="position: absolute;left: 1.5%;top: 2.5%;width: 97%;height: 95%;background: #FFFFFF;border-radius: 8px;border: 1px solid #E0E0E0;">
                <div id="zxt" style="width: 100%;height: 100%; position: relative;left: 0%;top:6%;"></div>
            </div>
        </div>
        <div class="third-three-t">
            <div style="position: absolute;left: 1.5%;top: 2.5%;width: 97%;height: 95%;background: #FFFFFF;border-radius: 8px;border: 1px solid #E0E0E0;">
                <a @click="lookfile"><img src="../assets/imgs/组1.png" style="position: absolute;left: 50px;top:38px;cursor: pointer"></a>
                <span  style="position: absolute;top: 108px;left: 50px;">文件管理</span>
              <a @click="typeMatchUploadDia=true"><img src="../assets/imgs/组1.png" style="position: absolute;left: 150px;top:38px;cursor: pointer"></a>
              <span  style="position: absolute;top: 108px;left: 144px;">典型表校核</span>
            </div>
        </div>

          <el-dialog title="点表上传" :visible.sync="typeMatchUploadDia" style="height: 400px">
            <a @click="triggerFileInput">
              <img src="../assets/imgs/组1.png" style="position: absolute;left: 150px;top:38px;cursor: pointer">
            </a>
            <input type="file" ref="fileInput" @change="handleFileChange" style="display: none;">
          </el-dialog>

          <el-dialog
            title="匹配结果"
            :visible.sync="matchResDia"
            width="90%"
            :style="{ position: 'fixed', top: '10%', left: '0', height: '80%', overflow: 'auto' }"
          >
            <el-table
              :data="matchResForm"
              border
              :key="randomKey"
              :style="{ width: '100%', height: '100%', overflow: 'auto',fontSize:'13px' }"
              :header-cell-style="{ background: '#03A095', color: 'white' }"
              height="100%"
            >
              <el-table-column prop="tableName" label="表名" width="300" align="center"></el-table-column>
              <el-table-column prop="type" label="站点类型" width="70" align="center"></el-table-column>
              <el-table-column prop="signal" label="信号类型" width="50" align="center"></el-table-column>
              <el-table-column prop="index" label="索引" width="50" align="center">
                <template v-slot="scope">
                  <span>{{ scope.row.index === -1 ? '' : scope.row.index }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="data" label="条目" width="300" align="center"></el-table-column>
              <el-table-column prop="result" label="匹配结果" width="50" align="center"></el-table-column>
              <el-table-column prop="reason" label="原因" width="200" align="center"></el-table-column>
              <el-table-column prop="template" label="匹配表对应条目" width="300" align="center"></el-table-column>
              <el-table-column label="匹配表对应索引" width="100" align="center">
                <template v-slot="scope">
                  <span>{{ scope.row.templateIndex === -1 ? '' : scope.row.templateIndex }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-dialog>


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

        <el-dialog title="运行工况数据" @close="voltLoading=false" :visible.sync="YXGKFormVisible" style="width: 153%;left: -26.5%;">
          <el-table :data="voltTableData" v-loading="voltLoading" border :key="randomKey" style="color:black;width: 1510px;overflow:auto;top: -10px;" :header-cell-style="{background:'#03A095',color:'white'}" height="528">
            <el-table-column prop="voltage" label="电压" width="300" align="center"></el-table-column>
            <el-table-column prop="stationCount" label="总数" width="300" align="center"></el-table-column>
            <el-table-column prop="checkCount" label="校核数" width="300" align="center"></el-table-column>
            <el-table-column prop="checkRate" label="校核率" width="300" align="center"></el-table-column>
            <el-table-column prop="more" label="更多" width="228" align="center">
              <template slot-scope="scope">
                      <div @click="handleNameDisplay(scope.row)">
                        <span style="color: #1D8ED2;cursor: pointer;">查看更多</span>
                      </div>
              </template>
            </el-table-column>
          </el-table>
        </el-dialog>

          <el-dialog title="站点工况数据" @close="nameLoading=false" :visible.sync="nameCheckForm" style="width: 153%;left: -26.5%;">
            <el-table :data="nameTableData" v-loading="nameLoading" border :key="randomKey" style="color:black;width: 1510px;overflow:auto;top: -10px;" :header-cell-style="{background:'#03A095',color:'white'}" height="528">
              <el-table-column prop="stationType" label="类型" width="300" align="center"></el-table-column>
              <el-table-column prop="voltage" label="电压" width="300" align="center"></el-table-column>
              <el-table-column prop="stationName" label="名称" width="300" align="center"></el-table-column>
              <el-table-column label="是否校验" width="300" align="center">
                <template v-slot="scope">
                  <span :style="{ color: scope.row.check ? 'lawngreen'  : 'red' }">{{ scope.row.check ? '是' : '否' }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-dialog>

        <el-dialog title="异常统计数据" @close="voltLoading=false" :visible.sync="YCTJFormVisible" style="width: 153%;left: -26.5%;">
          <el-table :data="voltTableData" v-loading="voltLoading" border :key="randomKey" style="color:black;width: 1510px;overflow:auto;top: -10px;" :header-cell-style="{background:'#03A095',color:'white'}" height="528">
            <el-table-column prop="voltage" label="电压" width="300" align="center"></el-table-column>
            <el-table-column prop="stationCount" label="总数" width="300" align="center"></el-table-column>
            <el-table-column prop="faultCount" label="缺陷数" width="300" align="center"></el-table-column>
            <el-table-column prop="faultRate" label="缺陷率" width="300" align="center"></el-table-column>
            <el-table-column prop="more" label="更多" width="228" align="center">
              <template slot-scope="scope">
                      <div @click="handleNameDisplay1(scope.row)">
                        <span style="color: #1D8ED2;cursor: pointer;">查看更多</span>
                      </div>
              </template>
            </el-table-column>
          </el-table>
        </el-dialog>

          <el-dialog title="站点异常数据" @close="nameLoading=false" :visible.sync="nameFaultForm" style="width: 153%;left: -26.5%;">
            <el-table :data="nameTableData" v-loading="nameLoading" border :key="randomKey" style="color:black;width: 1510px;overflow:auto;top: -10px;" :header-cell-style="{background:'#03A095',color:'white'}" height="528">
              <el-table-column prop="stationType" label="类型" width="300" align="center"></el-table-column>
              <el-table-column prop="voltage" label="电压" width="300" align="center"></el-table-column>
              <el-table-column prop="stationName" label="名称" width="300" align="center"></el-table-column>
              <el-table-column label="是否有缺陷" width="300" align="center">
                <template v-slot="scope">
                  <span :style="{ color: scope.row.fault ?  'red':'lawngreen'  }">{{ scope.row.fault ? '是' : '否' }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-dialog>

        <el-dialog title="厂站校核数据" :visible.sync="YXGKFenZhiFormVisible" style="width: 135%;left: -17%;">
          <el-table :data="YXGKXiangXiData" border :key="randomKey" style="color:black;width: 95.8%;overflow:auto;left:2.2%;top: -10px;" :header-cell-style="{background:'#03A095',color:'white'}" height="528">
            <el-table-column prop="name1" label="名称" width="301" align="center"></el-table-column>
            <el-table-column prop="checkCount1" label="校核次数" width="300" align="center"></el-table-column>
            <el-table-column prop="name2" label="名称" width="300" align="center"></el-table-column>
            <el-table-column prop="checkCount2" label="校核次数" width="301" align="center"></el-table-column>
          </el-table>
        </el-dialog>
      </div>
      <ModifyPwd :visible.sync="changePwd" @submit="handleChangePwd" />

      <el-dialog title="文件列表" :visible.sync="fileTable" style="font-weight: bold;" width="1000px" size="large" fullscreen=true>
        <el-table
            class="centered-table"
            :data="fileData"
            :key="randomKey"
            height="200%"
            highlight-current-row
            @current-change="handleCurrentChange"
            :header-cell-style="{fontSize: '16px',color:'black','border-color': '#ccc'}"
            style="position: absolute;left: 1.8%;top: 14%; width: 96.5%;height: 79.5%;overflow-y: auto;">
          <el-table-column
              prop="id"
              label="序号"
              width="100">
          </el-table-column>
          <el-table-column
              prop="fileName"
              label="文件名"
              width="280">
          </el-table-column>
          <el-table-column
              prop="createTime"
              label="创建时间"
              width="280">
          </el-table-column>
          <el-table-column
              prop="path"
              label="路径"
              width="300">
          </el-table-column>
        </el-table>
      </el-dialog>

    </div>
</template>

  <script>
  import { mapGetters } from 'vuex';
  import axios from 'axios';
  import {Message} from 'element-ui';
  // const colors = ['#FFFFFF', '#EE6666'];
  export default {
    name: 'char-t',
    components: {
      ModifyPwd: () => import('@/components/Modal/ModifyPwd.vue'),
    },
    computed: {
      ...mapGetters(['username']),
      },
    watch: {
    },
    data() {
        return {
          fileTable:false,
          changePwd: false,
          checkcount:'',
          top1recordName:'',
          top1recordTime:'',
          top2recordName:'',
          voltLoading:false,
          nameLoading:false,
          top2recordTime:'',
          top3recordName:'',
          top3recordTime:'',
          top4recordName:'',
          top4recordTime:'',
          top5recordName:'',
          top5recordTime:'',
          type:false,
          formLabelWidth: '120px',
          typeMatchUploadDia:false,
          matchResDia:false,
          matchResForm:[],
          dialogFormVisible: false,
          YXGKFenZhiFormVisible: false,
          YXGKFormVisible: false,
          YCTJFormVisible:false,
          nameCheckForm:false,
          nameFaultForm:false,
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
          fileData:[],
          YXGKXiangXiData:[],
          qxtjTime:[],
          qxtjData:[],
          YXGKData:[],
          YCTJData:[],
          public:[],
          njyiCheck:'',
          njweiCheck:'',
          voltType:'',
          bdzVoltList:[],
          fdcVoltList:[],
          voltTableData:[],
          nameTableData:[],
          typeTableData:[],
          tableData: [{
            ydcj: '已校核数',
            nrjb: '',
            nrkj: '',
            bjsf: '',
            more:'查看更多'
          }, {
            ydcj: '未校核数',
            nrjb: '',
            nrkj: '',
            bjsf: '',
          }, {
            ydcj: '校核率',
            nrjb: '',
            nrkj: '',
            bjsf: ''
          }],
          tableData1: [{
            ydcj: '缺陷',
            nrjb: '',
            nrkj: '',
            bjsf: '',
            more:'查看更多'
          }, {
            ydcj: '总数',
            nrjb: '',
            nrkj: '',
            bjsf: ''
          }],
          chart: null,
          options: {}
        };
      },
      mounted () {
        this.fiveCount()
        this.zhexiantu()
        this.initOptions()
        this.initCharts()
        this.homeData()
        this.RecordsData()
        this.timer = setInterval(() => {
          this.homeData()
        }, 10000);
        this.AlertCount()

        // this.TreeDate()

        this.timer = setInterval(() => {
          this.AlertCount()
        }, 10000);
        this.getTableData()
      },
      beforeDestroy(){
        clearInterval(this.timer)
        this.timer = null
      },
      methods: {
        triggerFileInput() {
          this.$refs.fileInput.click();
        },
        handleFileChange(event) {
          const file = event.target.files[0];
          if (file) {
            const tail=file.name.slice(file.name.indexOf('.') + 1)
            if(!(tail==='xls'||tail==='xlsx')){
              Message.error('不是点表类型文件')
              return
            }
            const formData = new FormData();
            formData.append('file', file);
            formData.append('name', file.name);
            const url = 'http://localhost:8090/Query/typeMatch';
            axios.post(url,formData,{
              headers: {
                'Content-Type': 'multipart/form-data'
              }
            }).then(ret => {
              if(ret.data.code===-1){
                Message.error(ret.data.msg)
              }
              else{
                Message.success('典型表校核成功')
                this.matchResForm=[]
                ret.data.data.forEach(item => {
                  this.matchResForm.push({
                    tableName:item.tableName,
                    type:item.type,
                    signal:item.signal,
                    index:item.index,
                    data:item.data,
                    result:item.result,
                    reason:item.reason,
                    template:item.template,
                    templateIndex:item.templateIndex
                  })
                })
                this.matchResDia=true
              }
            })
          }
        },
        handleNameDisplay(row){
          this.nameLoading=true;
          const url = 'http://localhost:8090/Query/nameData';
          const params = {
            stationType: row.stationType,
            voltage:row.voltage
          };
          this.nameTableData=[]
          axios.get(url, { params }).then(ret => {
            ret.data.data.forEach(item => {
              this.nameTableData.push(item)
            })
          }).finally(()=>{
            this.nameLoading=false;
          })
          this.nameCheckForm=true;
        },
        handleNameDisplay1(row){
          this.nameLoading=true;
          const url = 'http://localhost:8090/Query/nameData';
          const params = {
            stationType: row.stationType,
            voltage:row.voltage
          };
          this.nameTableData=[]
          axios.get(url, { params }).then(ret => {
            ret.data.data.forEach(item => {
              this.nameTableData.push(item)
            })
          }).finally(()=>{
            this.nameLoading=false;
          })
          this.nameFaultForm=true;
        },
        handleVoltDisplay(row){
          this.voltLoading=true;
          var url = 'http://localhost:8090/Query/tableData'
          const params = {
            stationType: row.stationType,
            voltage:''
          };
          this.voltTableData=[]
          if(row.stationType==='发电厂'){
            this.fdcVoltList.forEach(item=>{
              params.voltage=item
              axios.get(url, { params }).then(ret => {
                this.voltTableData.push({
                  stationType:ret.data.data[0].stationType,
                  voltage:ret.data.data[0].voltage,
                  stationCount:ret.data.data[0].stationCount,
                  checkCount:ret.data.data[0].checkCount,
                  checkRate:ret.data.data[0].checkRate,
                  faultCount:ret.data.data[0].faultCount,
                  faultRate:ret.data.data[0].faultRate,
                })
              }).finally(()=>{
                this.voltLoading=false;
              })
            })
          }
          else{
            this.bdzVoltList.forEach(item=>{
              params.voltage=item
              axios.get(url, { params }).then(ret => {
                this.voltTableData.push({
                  stationType:ret.data.data[0].stationType,
                  voltage:ret.data.data[0].voltage,
                  stationCount:ret.data.data[0].stationCount,
                  checkCount:ret.data.data[0].checkCount,
                  checkRate:ret.data.data[0].checkRate,
                  faultCount:ret.data.data[0].faultCount,
                  faultRate:ret.data.data[0].faultRate,
                })
              }).finally(()=>{
                this.voltLoading=false;
              })
            })
          }
          this.YXGKFormVisible = true
          this.voltType=row.stationType
  },
        handleVoltDisplay1(row){
          this.voltLoading=true;
          var url = 'http://localhost:8090/Query/tableData'
          const params = {
            stationType: row.stationType,
            voltage:''
          };
          this.voltTableData=[]
          if(row.stationType==='发电厂'){
            this.fdcVoltList.forEach(item=>{
              params.voltage=item
              axios.get(url, { params }).then(ret => {
                this.voltTableData.push({
                  stationType:ret.data.data[0].stationType,
                  voltage:ret.data.data[0].voltage,
                  stationCount:ret.data.data[0].stationCount,
                  checkCount:ret.data.data[0].checkCount,
                  checkRate:ret.data.data[0].checkRate,
                  faultCount:ret.data.data[0].faultCount,
                  faultRate:ret.data.data[0].faultRate,
                })
              }).finally(()=>{
                this.voltLoading=false;
              })
            })
          }
          else{
            this.bdzVoltList.forEach(item=>{
              params.voltage=item
              axios.get(url, { params }).then(ret => {
                this.voltTableData.push({
                  stationType:ret.data.data[0].stationType,
                  voltage:ret.data.data[0].voltage,
                  stationCount:ret.data.data[0].stationCount,
                  checkCount:ret.data.data[0].checkCount,
                  checkRate:ret.data.data[0].checkRate,
                  faultCount:ret.data.data[0].faultCount,
                  faultRate:ret.data.data[0].faultRate,
                })
              }).finally(()=>{
                this.voltLoading=false;
              })
            })
          }
          this.YCTJFormVisible = true
          this.voltType=row.stationType
        },
        home(){
          this.$router.push('/')
        },
        stationmanagement(){
          this.$router.push('/pointManagement')
        },
        historyquery(){
          this.$router.push('/CheckQuery')
        },
        AlertMessage(){
          this.$router.push('/alertManagement')
        },
        VersionControl(){
          this.$router.push('/VersionControl')
        },
        yxgkhandleMore(){
          alert('运行工况')
        },
        yxgkStation(){
          this.YXGKFenZhiFormVisible = false
        },
        handleMore(){
          this.YCTJFormVisible = true
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
              data: this.qxtjTime,
              axisLine: {
                onZero: false,
                lineStyle: {
                  // color: colors[0]
                }
              }
            },
            yAxis: {
              type: 'value',
              axisLine: {
                onZero: false,
                lineStyle: {
                  // color: colors[0]
                }
              },
              splitLine: {
                  show: true,
                  lineStyle:{
                      type:'dashed',
                      opacity: 1
                  }
              }
            },
            series: [
              {
                data: this.qxtjData,
                type: 'line',
                  smooth: 0.6,
                  symbol: 'none',
                  lineStyle: {
                      color: '#03A095',
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
              color:['#FFAC04','#31B36A'],
              series: datas.map(function (data, idx) {
                  var top = idx * 33.3;
                  return {
                  type: 'pie',
                  radius: [30, 60],
                  top: top + '%',
                  left: 'center',
                  width: 408,
                  label: {
                      alignTo: 'edge',
                      formatter: '{name|{b}} {time|{c} }\n\n',
                      minMargin: 5,
                      edgeDistance: 64,
                      lineHeight: 15,
                      rich: {
                        time: {
                            fontSize: 14,
                            color:'black'
                        },
                        name:{
                            fontSize: 14,
                            color:'black'
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
        getTableData(){
          var url = 'http://localhost:8090/Query/tableData'
          axios.get(url).then(ret => {
            this.yxgkonlinerate=ret.data.data[0].checkRate
            this.yxgktotal= ret.data.data[0].stationCount
            this.yxgkonline=ret.data.data[0].checkCount
            this.yxgkoffline=ret.data.data[0].stationCount-ret.data.data[0].checkCount
            this.yctjtotal=ret.data.data[0].stationCount
            this.yctjdefect=ret.data.data[0].faultCount
          })
          const params = {
            stationType: '变电站',
          };
          axios.get(url, { params }).then(ret => {
            this.typeTableData.push({
              stationType:ret.data.data[0].stationType,
              stationCount:ret.data.data[0].stationCount,
              checkCount:ret.data.data[0].checkCount,
              checkRate:ret.data.data[0].checkRate,
              faultCount:ret.data.data[0].faultCount,
              faultRate:ret.data.data[0].faultRate,
              uncheckCount:ret.data.data[0].stationCount-ret.data.data[0].checkCount
            })
          })
          params.stationType='发电厂'
          axios.get(url, { params }).then(ret => {
            this.typeTableData.push({
              stationType:ret.data.data[0].stationType,
              stationCount:ret.data.data[0].stationCount,
              checkCount:ret.data.data[0].checkCount,
              checkRate:ret.data.data[0].checkRate,
              faultCount:ret.data.data[0].faultCount,
              faultRate:ret.data.data[0].faultRate,
              uncheckCount:ret.data.data[0].stationCount-ret.data.data[0].checkCount
            })
          })
          // url='http://localhost:8090/Query/voltList'
          // axios.get(url, { params }).then(ret => {
          //   this.fdcVoltList=ret.data.data
          // })
          // params.stationType='变电站'
          // axios.get(url, { params }).then(ret => {
          //   this.bdzVoltList=ret.data.data
          // })
          this.fdcVoltList=['6kV','500kV','110kV','220kV','35kV','10kV']
          this.bdzVoltList=['500kV','1000kV','110kV','220kV','35kV','10kV']
          return 0
        },
      YXGK_Data(){
        var url = 'http://localhost:8090/Query/stationInfomation'
        return axios.get(url).then(ret => {
            console.log(ret.data.data)
            this.tableData[0].nrjb=ret.data.data[0].yiCheck
            this.tableData[0].nrkj=ret.data.data[1].yiCheck
            this.tableData[0].bjsf=ret.data.data[2].yiCheck
            this.tableData[1].nrjb=ret.data.data[0].weiCheck
            this.tableData[1].nrkj=ret.data.data[1].weiCheck
            this.tableData[1].bjsf=ret.data.data[2].weiCheck
            this.tableData[2].nrjb=ret.data.data[0].bili
            this.tableData[2].nrkj=ret.data.data[1].bili
            this.tableData[2].bjsf=ret.data.data[2].bili
            this.tableData1[0].nrjb=ret.data.data[0].quexina
            this.tableData1[0].nrkj=ret.data.data[0].quexina
            this.tableData1[0].bjsf=ret.data.data[0].quexina
            this.tableData1[1].nrjb=ret.data.data[0].allCount
            this.tableData1[1].nrkj=ret.data.data[1].allCount
            this.tableData1[1].bjsf=ret.data.data[2].allCount

            this.yxgkonlinerate=ret.data.data[0].allcheckbili
            this.yxgktotal=ret.data.data[0].allCount
            this.yxgkonline=ret.data.data[0].allyiCheck
            this.yxgkoffline=ret.data.data[0].allweiCheck
            this.yctjtotal=ret.data.data[0].allCount
            this.yctjdefect=ret.data.data[0].allquexina

            this.getPieS()
            this.getPieF()

            for(let i=0;i<ret.data.data.length;i++){
              this.YXGKData[i]={
                name:ret.data.data[i].areaName,
                allcount:ret.data.data[i].allCount,
                yicheck:ret.data.data[i].yiCheck,
                weicheck:ret.data.data[i].weiCheck,
              }
              this.YCTJData[i]={
                name:ret.data.data[i].areaName,
                allcount:ret.data.data[i].allCount,
                quexian:ret.data.data[i].quexina,
              }
            }
            this.randomKey = Math.random()
        })
      },
      YXGK_QX(){

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
          color:['#31B36A','#FFAC04'],
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
                            color:'black'

                        },
                        name:{
                            fontSize: 14,
                            color:'black'
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

              this.checkcount=ret.data.data[0].checkcount
              // this.yxgktotal=ret.data.data[0].yxgktotal
              // this.yxgkonline=ret.data.data[0].yxgkonline
              // this.yxgkoffline=ret.data.data[0].yxgkoffline
              // this.yctjtotal=ret.data.data[0].yctjtotal
              // this.yctjdefect=ret.data.data[0].yctjdefect
              // this.yxgkonlinerate=ret.data.data[0].onlinerate
              // this.getPieS()
              // this.getPieF()
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
          this.fileTable=true;
          this.fileData=[];
          var url = 'http://localhost:8090/Query/getFileList'
          return axios.get(url).then(ret => {
            for(let i=0;i<ret.data.data.length;i++){
              this.fileData[i]={
                id:i+1,
                fileName:ret.data.data[i].fileName,
                createTime:ret.data.data[i].createTime,
                path:ret.data.data[i].path,
              }
            }
            this.randomKey = Math.random()
          })
        },
        async handleStation1() {
          var url = 'http://localhost:8090/Query/upcity'
          localStorage.setItem('station', this.top1recordName)
          localStorage.setItem('time', this.top1recordTime)
            try{
            const response = await axios.get(url,{
                params: {
                    station: this.top1recordName
                }})
                localStorage.setItem('city', response.data.data[0])
                // console.log(response.data.data)
          }
          catch (error) {
            //处理请求错误
          }
          this.$router.push('/CheckQuery')
        },
        async handleStation2() {
          var url = 'http://localhost:8090/Query/upcity'
          localStorage.setItem('station', this.top2recordName)
          localStorage.setItem('time', this.top2recordTime)
            try{
            const response = await axios.get(url,{
                params: {
                    station: this.top2recordName
                }})
                localStorage.setItem('city', response.data.data[0])
          }
          catch (error) {
            //处理请求错误
          }
          this.$router.push('/CheckQuery')
        },
        async handleStation3() {
          var url = 'http://localhost:8090/Query/upcity'
          localStorage.setItem('station', this.top3recordName)
          localStorage.setItem('time', this.top3recordTime)
            try{
            const response = await axios.get(url,{
                params: {
                    station: this.top3recordName
                }})
                localStorage.setItem('city', response.data.data[0])
          }
          catch (error) {
            //处理请求错误
          }
          this.$router.push('/CheckQuery')
        },
        async handleStation4() {
          var url = 'http://localhost:8090/Query/upcity'
          localStorage.setItem('station', this.top4recordName)
          localStorage.setItem('time', this.top4recordTime)
            try{
            const response = await axios.get(url,{
                params: {
                    station: this.top4recordName
                }})
                localStorage.setItem('city', response.data.data[0])
          }
          catch (error) {
            //处理请求错误
          }
          this.$router.push('/CheckQuery')
        },
        async handleStation5() {
          var url = 'http://localhost:8090/Query/upcity'
          localStorage.setItem('station', this.top5recordName)
          localStorage.setItem('time', this.top5recordTime)
            try{
            const response = await axios.get(url,{
                params: {
                    station: this.top5recordName
                }})
                localStorage.setItem('city', response.data.data[0])
          }
          catch (error) {
            //处理请求错误
          }
          this.$router.push('/CheckQuery')
        },
        zhexiantu(){
          // 存储五个日期的数组
          let fiveDaysAgoDates = [];
          // 获取当前日期
          let currentDate = new Date();
          // 循环获取截止到今天前五天的五个日期
          for (let i = 0; i < 7; i++) {
            let date = new Date(currentDate);
            date.setDate(currentDate.getDate() - i);
            let formattedDate = `${date.getMonth() + 1 < 10 ? '0' : ''}${date.getMonth() + 1}-${date.getDate() < 10 ? '0' : ''}${date.getDate()}`;
            fiveDaysAgoDates.push(formattedDate);
          }
          fiveDaysAgoDates.sort();
          this.qxtjTime = fiveDaysAgoDates
        },
        fiveCount(){
          var url = 'http://localhost:8090/Query/FiveDayTime'
          return axios.get(url).then(ret => {
            for(let i=0;i<ret.data.data.length;i++){
              this.qxtjData[i]=ret.data.data[i]
            }
            this.initCharts()
          })
        },
        Testt(){
          this.qxtjData[0]=2
          this.qxtjData[1]=1
          this.initCharts()
        },
        TreeDate(){
          var url = 'http://localhost:8090/Query/StationAllTree'
          return axios.get(url).then(ret => {
            this.TreeData=ret.data.dattreea
            localStorage.setItem('treeList', ret.data.data)
          })

        },
        handleCommand(command){
          // console.log(command)
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

  .second-one-t /deep/ .el-table,
  .second-one-t /deep/ .el-table__expanded-cell{
    background-color: transparent;
  }
  .second-one-t /deep/ .el-table tr,
  .second-one-t /deep/ .el-table td {
    background-color: transparent!important;
    color:black;
  }
  .third-one-t /deep/ .el-table,
  .third-one-t /deep/ .el-table__expanded-cell{
    background-color: transparent;
  }
  .third-one-t /deep/ .el-table tr,
  .third-one-t /deep/ .el-table td {
    background-color: transparent!important;
    color:black;
  }
  .b .el-button{
      border-radius: inherit;
  }
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
      color: white;
      position: absolute;
      background-color:#03A095 ;
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
      color: black;
      position: absolute;
      background-color: transparent;
      left: 85px;
      width: 70px;
      height: 12px;
      font-size: 14px;

  }
  .lab3{
      color: black;
      position: absolute;
      background-color: transparent;
      top:60px;
      left: 85px;
      width: 70px;
      height: 12px;
      font-size: 14px;
  }
  .lab4{
      color: white;
      position: absolute;
      background-color:#03A095 ;
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
      width: 78%;
      height: 17%;
      background-color: #EFEFEF;
      left: 83px;
  }
  .mess2{
      position: absolute;
      width: 78%;
      height: 17%;
      background-color: transparent;
      left: 83px;
  }
  .mess3{
      position: absolute;
      top:40%;
      right: 10%;
      font-size: 15px;
      color: #03A095;
  }
  .second-one-t{
      color: black;
      background-color: #FDFDFC;
      box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);
      border-radius: 8px;
      position: absolute;
      height: 44.4%;
      width: 32.3%;
      left: 0.8%;
      top: 5.2%;
  }
  .second-two-t{
      color: black;
      background-color: #FDFDFC;
      box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);
      border-radius: 8px;
      position: absolute;
      height: 44.4%;
      width: 32.3%;
      left: 33.8%;
      top: 5.2%;
  }
  .second-three-t{
      color: black;
      background-color: #FDFDFC;
      box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);
      border-radius: 8px;
      position: absolute;
      height: 44.4%;
      width: 32.3%;
      left: 66.8%;
      top: 5.2%;
  }
  .third-one-t{
      color: black;
      background-color: #FDFDFC;
      box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);
      border-radius: 8px;
      position: absolute;
      height: 44.4%;
      width: 32.3%;
      left: 0.8%;
      top: 54.3%;
  }
  .third-two-t{
      color: black;
      background-color: #FDFDFC;
      box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);
      border-radius: 8px;
      position: absolute;
      height: 44.4%;
      width: 32.3%;
      left: 33.7%;
      top: 54.3%;
  }
  .third-three-t{
      color: black;
      background-color: #FDFDFC;
      box-shadow: 0px 0px 8px 0px rgba(0,0,0,0.35);
      border-radius: 8px;
      position: absolute;
      height: 44.4%;
      width: 32.3%;
      left: 66.9%;
      top: 54.3%;
  }
  .table-one{
      position: relative;
      top: 50%;
  }
    .zz >>> .el-table__row>td{
        /* border-color:red; */
        border: 1px solid #E0E0E0;
    }
    .zz /deep/ .el-table--border::after {
        height: 0;
    }
    .zz /deep/ .el-table::before {
        height: 0;
    }
    .zz .el-table{
        border: 1px solid #E0E0E0;
    }
    .zzt >>> .el-table__row>td{
        /* border-color:red; */
        border: 1px solid #E0E0E0;
    }
    .zzt /deep/ .el-table--border::after {
        height: 0;
    }
    .zzt /deep/ .el-table::before {
        height: 0;
    }
    .zzt .el-table{
        border: 1px solid #E0E0E0;
    }
    .el-table .el-table__header-wrapper {
        border-color: red; /* 设置表头边框颜色为蓝色 */
    }
    .annu{
      background: linear-gradient(0deg, rgba(0,194,183,0.2) 0%, rgba(0,194,183,0.99) 50%, rgba(0,194,183,0.2) 100%);
    }
  </style>