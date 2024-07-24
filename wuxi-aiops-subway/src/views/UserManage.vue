<template>
    <el-row class="sy-row user-manage">
      <div class="sy-table-header search">
        <el-form class="sy-table-form search-form" ref="tableForm" :model="tableForm" inline>
          <el-form-item class="sy-form-search" prop="username" label="用户账号:">
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
              <el-button class="sy-btn-corner sy-btn-corner--bl" icon="el-icon-search" size="small" @click="getTableData"
                >查询
              </el-button>
              <el-button
                class="sy-btn-corner sy-btn-corner--bl"
                icon="el-icon-plus"
                size="small"
                @click="handleTableAction('create')"
                >新增
              </el-button>
              <el-button
                class="sy-btn-corner sy-btn-corner--bl"
                icon="el-icon-refresh-right"
                size="small"
                @click="handleTableAction('reset')"
                >重置
              </el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <el-table class="sy-table" v-loading="tableLoad" :data="tableData" border>
        <sy-empty slot="empty" description="暂无用户数据"></sy-empty>
  
        <el-table-column
          v-for="column in tableColumns"
          :key="column.prop"
          v-bind="column"
          align="center"
          show-overflow-tooltip
        >
          <template v-if="column.prop === 'operation'" v-slot="{ row }">
            <el-button class="sy-btn-corner sy-btn-corner--bl" size="mini" @click="handleTableAction('update', row)"
              >编辑
            </el-button>
            <el-button class="sy-btn-corner sy-btn-corner--bl" size="mini" @click="handleTableAction('delete', row)"
              >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="sy-table-footer">
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
          case 'delete':
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
  