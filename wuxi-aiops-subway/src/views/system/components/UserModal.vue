<template>
  <el-dialog
    custom-class="sy-dialog sy-dialog--mini auto-height"
    :visible.sync="isVisible"
    :title="title"
    :center="true"
    :close-on-click-modal="false"
    @open="onDialogOpen"
    @close="onDialogClose"
  >
    <el-form class="sy-form user-form" ref="form" label-position="top" :model="formModel" :rules="formRules">
      <el-form-item label="账号" prop="username">
        <el-input v-model="formModel.username" placeholder="请输入账号" :maxlength="20" clearable></el-input>
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="formModel.name" placeholder="请输入姓名" :maxlength="20" clearable></el-input>
      </el-form-item>
      <el-form-item v-show="showPwd" label="密码" prop="password">
        <el-input
          v-model="formModel.password"
          type="password"
          placeholder="请输入密码"
          :maxlength="20"
          show-password
          clearable
        ></el-input>
      </el-form-item>
      <!-- <el-form-item label="角色" prop="group">
        <el-radio-group class="sy-radio-group" v-model="formModel.group">
          <el-radio v-for="item in roleTypes" :key="item.key" :label="item.key">
            {{ item.value }}
          </el-radio>
        </el-radio-group>
      </el-form-item> -->

      <!-- <el-form-item label="组织:" prop="team_id">
        <el-select v-model="formModel.team_id" placeholder="请选择组织" popper-class="sy-popper" style="width: 100%">
          <el-option v-for="item in teams" :key="item.id" :label="item.name" :value="`${item.id}`"></el-option>
        </el-select>
      </el-form-item> -->
    </el-form>

    <template #footer>
      <el-button class="sy-btn-regular" type="warning" @click="onDialogAction('cancel')">取 消</el-button>
      <el-button class="sy-btn-regular" type="primary" @click="onDialogAction('submit')">提 交</el-button>
    </template>
  </el-dialog>
</template>

<script>
import { SM3 } from 'gm-crypto';
import { sm4Encrypt } from '@/utils';
import Option from '@/enums/option';
import { RoleType } from '@/enums';

export default {
  name: 'UserModal',
  computed: {
    isVisible: {
      get() {
        return this.visible;
      },
      set(bool) {
        this.$emit('update:visible', bool);
      },
    },
    title() {
      return this.action === 'create' ? '用户新增' : '用户编辑';
    },
    showPwd() {
      return this.action === 'create';
    },
    roleTypes() {
      return Option.RoleTypes;
    },
  },
  props: {
    action: {
      type: String,
      default: 'create',
    },
    model: {
      type: Object,
      default: () => ({}),
    },
    teams: {
      type: Array,
      default: () => [],
    },
    visible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    const checkUsername = (rule, value, callback) => {
      if (!value) return callback(new Error('请输入账户'));
      const rgx = /^[a-zA-Z0-9_-]{2,10}$/;
      if (!rgx.test(value)) return callback(new Error('请输入2-10位用户名，字母、数字、-、_'));
      callback();
    };
    return {
      teamList: [],
      formModel: {
        name: '',
        userid: '',
        username: '',
        password: '',
        team_id: undefined,
        group: RoleType.OPERATOR,
      },
      formRules: {
        name: [{ required: true, trigger: 'blur', message: '请输入姓名' }],
        // group: [{ required: true, trigger: 'blur', message: '请选择角色' }],
        team_id: [{ required: true, trigger: 'blur', message: '请选择组织' }],
        username: [
          { required: true, trigger: 'blur', message: '请输入账号' },
          { validator: checkUsername, trigger: 'blur' },
        ],
        password: [{ required: this.showPwd, trigger: 'blur', message: '请输入密码' }],
      },
    };
  },
  methods: {
    onDialogOpen() {
      console.log(this.teams);
      this.formModel = JSON.parse(JSON.stringify(this.model));
      this.$nextTick(() => {
        this.$refs.form.clearValidate();
      });
    },
    onDialogClose() {
      this.$refs.form.resetFields();
    },
    async onDialogAction(action) {
      switch (action) {
        case 'cancel':
          this.isVisible = false;
          break;
        case 'submit':
          this.$refs.form.validate((valid) => {
            if (valid) {
              const { name, group, team_id, userid, username, password } = this.formModel;
              if (this.action === 'create') {
                const sm4Passwd = sm4Encrypt(password);
                const sm3Check = SM3.digest(`${username}${sm4Passwd}`, 'utf8', 'hex');

                this.$api
                  .postUserApi('create', {
                    name: name,
                    group: group,
                    team_id: `${team_id}`,
                    username: username,
                    password: sm4Passwd,
                    check: sm3Check,
                  })
                  .then((res) => {
                    if (res?.success) {
                      this.$emit('success');

                      this.$message({
                        type: 'success',
                        message: '用户新增成功！',
                      });
                    }
                  });
              } else if (this.action === 'update') {
                this.$api
                  .postUserApi('update', {
                    name: name,
                    group: group,
                    userid: userid,
                    team_id: `${team_id}`,
                  })
                  .then((res) => {
                    if (res?.success) {
                      this.$emit('success');

                      this.$message({
                        type: 'success',
                        message: '用户编辑成功！',
                      });
                    }
                  });
              }
            }
          });
          break;
        default:
          break;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.user-form {
  padding: 0 100px;
}
</style>
