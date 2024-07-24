<template>
  <el-row class="sy-row user-manage">
    <!-- <div class="b">
          <img src="@/assets/imgs/logo_point.png" style="position: absolute;top: 13%;left: 1%;">
              <span style="color: white;position: absolute;left:11.5%;top:8.5%;font-size: 25px;font-weight: bold;line-height: 48px;">厂站自动化点表管理及信息核对系统</span>
              <el-button-group style="position: absolute;top: 15%;left: 36%;">
                  <el-button class="annu" style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;">首页</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="stationmanagement">点表管理</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="historyquery">历史查询</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="AlertMessage">告警信息</el-button>
                  <el-button style="background-color: transparent;color: white;font-weight: bolder;font-size: 18px;width: 128px;height: 45px;padding:1%;border: 0px solid #03A095;" @click="VersionControl">版本管理</el-button>
              </el-button-group>
              <img src="@/assets/imgs/y1.png" style="position: absolute;left: 86.5%;top: 25%;">
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
              <img src="@/assets/imgs/y2.png" style="position: absolute;left: 76%;top: 25.5%;width: 22px;height: 23px;cursor: pointer;" @click="AlertMessage">
              <img src="@/assets/imgs/y5.png" style="position: absolute;left: 79%;top: 27%;width: 22px;height: 22px;cursor: pointer;" type="primary" @click="butClick">
              <img src="@/assets/imgs/y6.png" style="position: absolute;left: 82%;top: 25.5%;width: 25px;height: 25px;cursor: pointer;" @click="tuichu">
    </div> -->
    <div class="sy-table-header search" style="position:fixed;top: 5%;">
      <el-form class="sy-table-form search-form" ref="tableForm" :model="tableForm" inline style="position:absolute;top: 15%;">
        <el-form-item class="sy-form-search" prop="username" label="用户账号:" >
          <el-input
            class="el-form-item__input"
            v-model="tableForm.username"
            placeholder="请输入用户账号"
            :maxlength="20"
            clearable
            @change="getTableData(true)"
          ></el-input>
        </el-form-item>
        <!-- <el-form-item prop="group" label="用户角色:">
          <el-select
            class="el-form-item__select sy-select"
            v-model="tableForm.group"
            placeholder="请选择用户角色"
            popper-class="sy-popper"
            clearable
            @change="getTableData(true)"
          >
            <el-option v-for="item in roleTypes" :key="item.key" :label="item.value" :value="item.key"></el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item class="el-form-item__action el-form-item-last">
          <div class="head-operate">
            <el-button style="color: white;background-color: #03A095;border:1px solid #F1F2F6;" icon="el-icon-search" size="small" @click="getTableData"
              >查询
            </el-button>
            <el-button
              style="color: white;background-color: #03A095;border:1px solid #F1F2F6;"
              icon="el-icon-plus"
              size="small"
              @click="handleTableAction('create')"
              >新增
            </el-button>
            <el-button
              style="color: white;background-color: #03A095;border:1px solid #F1F2F6;"
              icon="el-icon-refresh-right"
              size="small"
              @click="handleTableAction('reset')"
              >重置
            </el-button>
            <el-button
              style="position: relative;left: 360%;color: white;background-color: #03A095;border:1px solid #F1F2F6;"
              icon="el-icon-s-home"
              size="small"
              @click="handleTableAction('hhome')"
              >返回主页
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
    <el-table class="sy-table" v-loading="tableLoad" :data="tableData" border style="position: absolute;top: 10%;height: 85%;">
      <sy-empty slot="empty" description="暂无用户数据"></sy-empty>

      <el-table-column
        v-for="column in tableColumns"
        :key="column.prop"
        v-bind="column"
        align="center"
        show-overflow-tooltip
      >
        <template v-if="column.prop === 'operation'" v-slot="{ row }">
          <el-button style="color: white;background-color: #03A095;" size="mini" @click="handleTableAction('update', row)"
            >编辑
          </el-button>
          <el-button style="color: white;background-color: #03A095;" size="mini" @click="handleTableAction('delete', row)"
            >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="sy-table-footer" style="position: absolute;top: 95.5%;">
      <el-pagination
        class="sy-pagination"
        layout="prev, pager, next, total, jumper"
        :background="true"
        :total="tableTotal"
        :page-size.sync="tableSize"
        :current-page.sync="tablePage"
        @current-change="onTablePageChange"
      >
      </el-pagination>
    </div>

    <UserModal
      :visible.sync="userModal"
      :action="userAction"
      :model="userModel"
      :teams="userTeams"
      @success="onUserSubmit"
    />
  </el-row>
</template>

<script>
import { arrToMap } from '@/utils';
import Option from '@/enums/option';
import { RoleType } from '@/enums';

export default {
  name: 'UserManage',
  components: {
    UserModal: () => import('./components/UserModal'),
  },
  computed: {
    roleTypes() {
      return Option.RoleTypes;
    },
    roleTypeMap() {
      return arrToMap(Option.RoleTypes);
    },
    userTeamMap() {
      return arrToMap(this.userTeams, 'id', 'name');
    },
    tableColumns() {
      return [
        { type: 'index', label: '序号', width: 80 },
        { prop: 'username', label: '用户账号' },
        { prop: 'name', label: '用户姓名' },
        // { prop: 'roleType', label: '用户角色' },
        { prop: 'userTeam', label: '用户组织' },
        { prop: 'operation', label: '人工操作', minWidth: 150 },
      ];
    },
  },
  data() {
    return {
      tableLoad: false,
      tablePage: 0,
      tableSize: 10,
      tableTotal: 0,
      tableData: [],
      tableForm: {
        username: '',
        group: RoleType.OPERATOR,
      },
      userAction: 'create',
      userTeams: [],
      userModal: false,
      userModel: {
        name: '',
        userid: '',
        username: '',
        password: '',
        group: RoleType.OPERATOR,
      },
    };
  },
  async created() {
    this.getTeamList();
  },
  methods: {
    onUserSubmit() {
      this.getTableData(true);
    },
    handleTableAction(action, row) {
      console.log('row: ', row);
      this.userAction = action;
      switch (action) {
        case 'reset':
          this.$refs.tableForm.resetFields();
          this.getTableData(true);
          break;
        case 'create':
          this.userModel = {
            name: '',
            userid: '',
            username: '',
            password: '',
            group: RoleType.OPERATOR,
          };
          this.userModal = true;
          break;
        case 'update':
          this.userModel = row;
          this.userModal = true;
          break;
        case 'hhome':
        this.$router.push('/Home')  
          break;
        case 'delete':
          if(row.name==='snk'){
            break;
          }
          this.$confirm('确认删除该用户?', {
            callback: (action) => {
              if (action === 'confirm') {
                this.$api
                  .postUserApi('remove', {
                    userid: row.userid,
                  })
                  .then((res) => {
                    if (res?.success) {
                      this.$message({
                        type: 'success',
                        message: '用户删除成功！',
                      });

                      this.getTableData(true);
                    }
                  });
              }
            },
          });
          break;
        default:
          break;
      }
    },
    onTablePageChange() {
      this.getTableData();
    },
    async getTableData(reset) {
      this.tableLoad = true;
      if (reset) this.tablePage = 1;

      this.$api
        .getUserApi('list', {
          limit: this.tableSize,
          offset: (this.tablePage - 1) * this.tableSize,
          username: this.tableForm.username,
        })
        .then((res) => {
          if (!res?.data) {
            this.tableData = [];
            this.tableTotal = 0;
            return;
          }
          this.tableData = res.data.map((item, index) => ({
            ...item,
            index: (this.tablePage - 1) * this.tableSize + index + 1,
            roleType: this.roleTypeMap[item.group] || '-',
            userTeam: this.userTeamMap[item.team_id] || '-',
          }));
          this.tableTotal = res.count;
        })
        .finally(() => {
          this.tableLoad = false;
        });
    },
    getTeamList() {
      this.$api.getBaseApi('team').then((res) => {
        if (!res?.results) return;
        this.userTeams = res.results;

        this.getTableData();
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.user-manage {
  display: flex;
  flex-direction: column;
}
</style>
