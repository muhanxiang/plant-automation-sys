<template>
  <el-dialog
    :visible.sync="isVisible"
    custom-class="sy-dialog"
    title="修改密码"
    width="25vw"
    top="30vh"
    center
    append-to-body
    @open="onDialogOpen"
  >
    <el-form class="modify-form" ref="form" :model="formModel" :rules="formRules" label-width="120px" inline>
      <el-form-item label="旧密码" prop="password">
        <el-input v-model="formModel.password" type="password" :maxlength="20" clearable></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newpasswd">
        <el-input v-model="formModel.newpasswd" type="password" :maxlength="20" clearable></el-input>
      </el-form-item>
      <el-form-item label="重复密码" prop="repeatpwd">
        <el-input v-model="formModel.repeatpwd" type="password" :maxlength="20" clearable></el-input>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button class="sy-btn-regular" type="warning" size="medium" @click="onDialogAction('cancel')">取 消</el-button>
      <el-button class="sy-btn-regular" type="primary" size="medium" @click="onDialogAction('submit')">提 交</el-button>
    </template>
  </el-dialog>
</template>

<script>
import { SM3 } from 'gm-crypto';
import { mapGetters } from 'vuex';
import { sm4Encrypt } from '@/utils';

export default {
  name: 'ModifyPwd',
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  computed: {
    ...mapGetters(['password', 'username', 'userId']),
    isVisible: {
      get() {
        return this.visible;
      },
      set(val) {
        this.$emit('update:visible', val);
      },
    },
    formRules() {
      return {
        password: [{ type: 'string', required: true, message: '请输入旧密码' }],
        newpasswd: [{ type: 'string', required: true, message: '请输入新密码' }],
        repeatpwd: [
          {
            type: 'string',
            required: true,
            message: '请输入重复密码',
            validator: (rule, value, callback) => {
              if (!value) {
                return callback(new Error('重复密码不能为空！'));
              }
              if (this.formModel.newpasswd && value !== this.formModel.newpasswd) {
                return callback(new Error('两次输入密码不一致！'));
              }
              callback();
            },
          },
        ],
      };
    },
  },
  data() {
    return {
      formModel: {
        password: '',
        newpasswd: '',
        repeatpwd: '',
      },
    };
  },
  methods: {
    onDialogOpen() {
      this.formModel = {
        password: '',
        newpasswd: '',
        repeatpwd: '',
      };
    },
    async onDialogAction(action) {
      switch (action) {
        case 'cancel':
          this.isVisible = false;
          break;
        case 'submit':
          {
            const valid = await this.$refs.form.validate();
            if (!valid) return;

            const ePwd = sm4Encrypt(this.formModel.password);
            const eNewPwd = sm4Encrypt(this.formModel.newpasswd);
            const check = SM3.digest(`${this.username}${eNewPwd}`, 'utf8', 'hex');

            if (ePwd !== this.password) {
              return this.$message({
                type: 'error',
                message: '旧密码不正确',
              });
            }
            this.$emit('submit', {
              userid: this.userId,
              username: this.username,
              password: ePwd,
              newpasswd: eNewPwd,
              check,
            });
          }
          break;
        default:
          break;
      }
    },
  },
};
</script>
