<template>
  <el-dialog
    custom-class="sy-dialog sy-dialog--mini"
    :visible.sync="isVisible"
    title="修改密码"
    @open="onDialogOpen"
    @close="onDialogClose"
    center
  >
    <el-form
      ref="changeForm"
      :model="changeForm"
      label-width="125px"
      size="mini"
      class="dialog-aside sy-form"
      :rules="changeRules"
    >
      <div class="dialog-aside__body">
        <el-form-item label="账号:" prop="username">
          <el-input
            v-model.trim="changeForm.username"
            name="username"
            placeholder="请输入账号"
            :maxlength="20"
            size="medium"
            clearable
            autocomplete="off"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="原密码:" prop="oldPwd">
          <el-input
            v-model.trim="changeForm.oldPwd"
            :show-password="!!changeForm.oldPwd"
            type="password"
            name="oldPwd"
            placeholder="请输入原密码"
            :maxlength="20"
            size="medium"
            clearable
          >
          </el-input>
        </el-form-item>
        <el-form-item label="新密码:" prop="newPwd">
          <el-input
            v-model.trim="changeForm.newPwd"
            :show-password="!!changeForm.oldPwd"
            type="password"
            name="newPwd"
            placeholder="请输入新密码"
            :maxlength="20"
            size="medium"
            clearable
          >
          </el-input>
        </el-form-item>
        <el-form-item label="确认密码:" prop="confirmPwd">
          <el-input
            v-model.trim="changeForm.confirmPwd"
            :show-password="!!changeForm.oldPwd"
            type="password"
            name="confirmPwd"
            placeholder="请再次输入新密码"
            :maxlength="20"
            size="medium"
            clearable
          >
          </el-input>
        </el-form-item>
        <el-form-item label="验证码:" prop="captcha">
          <el-input
            v-model="changeForm.captcha"
            name="captcha"
            placeholder="请输入验证码"
            :maxlength="10"
            size="medium"
            @keyup.enter.native="onModalAction('submit')"
          >
            <img slot="suffix" :src="captchaImg" alt="验证码" @click="refreshCaptcha" />
          </el-input>
        </el-form-item>
      </div>
    </el-form>
    <template #footer>
      <el-button class="sy-btn-regular" type="warning" size="medium" @click="onModalAction('cancel')">取 消 </el-button>
      <el-button class="sy-btn-regular" type="primary" size="medium" :loading="loading" @click="onModalAction('submit')"
        >提 交
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import { sm3Digest, sm4Encrypt } from '@/utils';

export default {
  name: 'ChangePwd',
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  computed: {
    isVisible: {
      get() {
        return this.visible;
      },
      set(bool) {
        this.$emit('update:visible', bool);
      },
    },
    changeRules() {
      const validateNewPwd = (rule, value, callback) => {
        //正则表达式,需同时包含英文字母大小写、特殊字符、数字
        const regPwd =
          /^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\W_]+$)(?![a-z0-9]+$)(?![a-z\W_]+$)(?![0-9\W_]+$)[a-zA-Z0-9#?!@$%^&*-._()]{6,20}$/;
        if (value === '' || !regPwd.test(value)) {
          callback(new Error('密码长度6-20位(需同时包含英文字母、特殊字符、数字)'));
        } else if (value === this.changeForm.oldPwd) {
          callback(new Error('新密码与原密码重复！'));
        } else if (this.changeForm.confirmPwd !== '') {
          this.$refs.changeForm.validateField('confirmPwd');
          callback();
        } else {
          callback();
        }
      };
      const validateConfirmPwd = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入新密码'));
        } else if (value !== this.changeForm.newPwd) {
          callback(new Error('两次输入密码不一致！'));
        } else {
          callback();
        }
      };
      return {
        username: [{ required: true, trigger: 'blur', message: '请输入账号' }],
        oldPwd: [{ required: true, trigger: 'blur', message: '请输入原密码' }],
        newPwd: [{ validator: validateNewPwd, required: true, trigger: 'blur' }],
        confirmPwd: [{ validator: validateConfirmPwd, required: true, trigger: 'blur' }],
        captcha: [{ required: true, trigger: 'blur', message: '请输入验证码' }],
      };
    },
  },
  data() {
    return {
      captchaImg: '',
      loading: false,
      changeForm: {
        username: '',
        oldPwd: '',
        newPwd: '',
        confirmPwd: '',
        captcha: '',
      },
    };
  },
  created() {
    this.refreshCaptcha();
  },
  methods: {
    onModalAction(action) {
      switch (action) {
        case 'cancel':
          this.isVisible = false;
          break;
        case 'submit':
          this.$refs.changeForm.validate((valid) => {
            if (valid) {
              this.loading = true;
              const sm4Pwd1 = sm4Encrypt(this.changeForm.oldPwd);
              const sm4Pwd2 = sm4Encrypt(this.changeForm.newPwd);
              this.$api
                .postUserApi('update/password', {
                  username: this.changeForm.username,
                  password: sm4Pwd1,
                  newpassword: sm4Pwd2,
                  check: sm3Digest(`${this.changeForm.username}${sm4Pwd2}`),
                  captcha: this.changeForm.captcha,
                  captcha_id: this.changeForm.captcha_id,
                })
                .then((res) => {
                  this.changeForm.captcha = ''; //请求了就需要重新输入验证码
                  this.$refs.changeForm.validateField('captcha');
                  if (!res) {
                    setTimeout(() => {
                      this.loading = false;
                    }, 1000);
                    this.refreshCaptcha();
                    return;
                  } else if (res.detail) {
                    switch (res.detail) {
                      case '密码更改成功':
                        this.$message({
                          type: 'success',
                          message: '密码更改成功！',
                        });
                        this.$emit('submit', this.changeForm);

                        this.loading = false;
                        this.isVisible = false;
                        break;
                      case '用户不存在':
                        Object.keys(this.changeForm).forEach((key) => (this.changeForm[key] = ''));
                        this.loading = false;
                        this.refreshCaptcha();
                        break;
                      case '用户密码不正确':
                        this.loading = false;
                        this.refreshCaptcha();
                        this.changeForm.oldPwd = '';
                        this.$refs.changeForm.validateField('oldPwd');
                        break;
                      default:
                        break;
                    }
                  } else {
                    this.$message({
                      type: 'error',
                      message: '密码更改失败！',
                    });
                    this.loading = false;
                    this.refreshCaptcha();
                  }
                })
                .finally(() => {
                  this.changeForm.captcha = ''; //请求了就需要重新输入验证码
                  this.$refs.changeForm.validateField('captcha');
                  this.loading = false;
                  this.refreshCaptcha();
                });
            } else {
              this.loading = false;
            }
          });
          break;
        default:
          break;
      }
    },
    onDialogOpen() {
      this.isShow = true;
      this.refreshCaptcha();
    },
    onDialogClose() {
      this.isShow = false;
      this.$refs.changeForm.resetFields();
      Object.keys(this.changeForm).forEach((key) => (this.changeForm[key] = ''));
    },
    refreshCaptcha() {
      this.$api.getUserApi('captcha').then((res) => {
        if (!res) return;
        const { captcha_id, captcha } = res.data;
        this.changeForm.captcha_id = captcha_id;
        this.captchaImg = captcha;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.dialog-aside {
  height: 100%;

  &__body {
    display: flex;
    height: 100%;
    font-size: 16px;
    width: 95%;
    margin: 0 auto;
    flex-direction: column;
    ::v-deep .el-input__count {
      background: initial;
    }
    .el-input {
      font-size: 14px;

      &__suffix {
        &-inner {
          height: 100%;

          img {
            display: block;
            background-color: $bg-hover;
            color: $text-regular;
            height: 100%;
            text-align: middle;
            cursor: pointer;
            font-size: 12px;
          }
        }
      }
    }
  }

  .el-form-item {
    flex: 1;
    &:last-child {
      margin: 0;
    }
    &__content {
      height: 100%;
    }
  }
}
</style>
