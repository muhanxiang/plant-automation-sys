<template>
  <div class="login-wrapper">
    <header class="login-header">
    </header>
    <main class="login-main">
      <div class="login-main-form">
        <img src="../../assets/imgs/logo_point.png" style="position: absolute;top: 21%;left: 6.2%;">
        <span style="position: absolute;top: 68%;left: 4.4%;font-weight: bold;font-size: 22px;font-family: Microsoft YaHei;">厂站自动化管控系统</span>
        <el-form ref="form" :rules="loginRules" :model="loginForm">
          <el-form-item prop="username">
            <el-input v-model="loginForm.username" placeholder="请输入账号" :max-length="20" clearable>
              <sy-icon slot="prefix" name="user" style="color: #03A095;"/>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              type="password"
              v-model="loginForm.password"
              placeholder="请输入密码"
              :max-length="20"
              clearable
              @keyup.native.enter="handleLogin"
            >
              <sy-icon slot="prefix" name="password" style="color: #03A095;"/>
            </el-input>
          </el-form-item>
          <el-form-item prop="captcha">
            <div class="login-main-form-vcode">
              <el-input
                v-model="loginForm.captcha"
                placeholder="请输入验证码"
                :max-length="10"
                clearable
                @keyup.native.enter="handleLogin"
              >
                <sy-icon slot="prefix" name="vcode" style="color: #03A095;"/>
              </el-input>
              <div class="captcha_wrapper">
                <img class="captcha" :src="captcha" alt="图片验证码" @click="refreshCaptcha" />
              </div>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button
              :loading="logining"
              class="login-main-form-btn sy-btn-default"
              type="primary"
              @click="handleLogin"
              >登 录</el-button
            >
          </el-form-item>
          <el-form-item>
            <div class="change-pwd">
              <el-button type="text" style="color: #03A095;" @click="handleChangePwd">修改密码</el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </main>
    <!-- <footer class="login-footer">{{ footer }}</footer> -->
<div style="position: absolute;width: 100%;height: 5%;bottom: 0%;background: #000000;opacity: 0.3;"></div>
<div style="position: absolute;width: 100%;height: 5%;bottom: 0%;">
  <span style="position: absolute;top: 28%;left: 45%;">江苏万炬科技股份有限公司 | Astraeus</span>
</div>
    <ChangePwd :visible.sync="showChangePwd" />
  </div>
</template>

<script>
import { debounce } from 'lodash';
import { mapGetters } from 'vuex';
import { checkTimeout } from '@/utils';

export default {
  name: 'UserLogin',
  components: {
    ChangePwd: () => import('./components/ChangePwd'),
  },
  computed: {
    ...mapGetters(['logo', 'title', 'footer']),
  },
  data() {
    return {
      captcha: '',
      showChangePwd: false,
      logining: false,
      loginForm: {
        username: 'snk',
        password: 'qwer!234',
        captcha: '',
        captchaId: null,
      },
      loginRules: {
        username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
      },
    };
  },
  created() {
    this.refreshCaptcha();
  },
  methods: {
    handleChangePwd() {
      this.showChangePwd = true;
    },
    handleLogin: debounce(function () {
      this.logining = true;
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$store
            .dispatch('user/login', this.loginForm)
            .then(() => {
              // 系统超时自动退出
              checkTimeout(30 * 60 * 1000, () => {
                this.$store.dispatch('logout');
              });
              this.$router.replace('/Home');
            })
            .catch(() => {
              this.refreshCaptcha();
            })
            .finally(() => {
              this.logining = false;
            });
        } else {
          this.logining = false;
        }
      });
    }, 500),
    refreshCaptcha() {
      this.$api.getUserApi('captcha').then(({ success, data, detail }) => {
        if (!success)
          return this.$message({
            message: detail,
            type: 'error',
          });
        const { captcha_id, captcha } = data;
        this.loginForm.captchaId = captcha_id;
        this.captcha = captcha;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.login-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
  background: {
    image: url('~@/assets/img/sd_bj.jpg');
    repeat: no-repeat;
    size: 100% 100%;
  }
}

.login-header {
  display: flex;
  align-items: center;
  justify-content: center;
  position: absolute;
  top: 8vh;
  left: 0;
  width: 100%;
  font-size: 48px;
  letter-spacing: 5px;
  background: linear-gradient(180deg, #dbe5f0 0%, #8fc4ff 100%);
  background-clip: text;
  -webkit-text-fill-color: transparent;

  .logo {
    width: 80px;
    height: auto;
    margin-right: 40px;
  }
}
.login-main {
  position: absolute;
  top: 50%;
  left: 42%;
  width: 800px;
  height: 500px;
  margin: -250px;
  padding: 0 10px;
  background: {
    image: url('~@/assets/img/bj2.jpg');
    repeat: no-repeat;
    size: 100% 100%;
  }

  &-header {
    width: 100%;
    height: 4vh;
    font-size: 24px;
    line-height: 4vh;
    text-align: center;
    background: {
      image: url('~@/assets/img/bg_login_title.png');
      size: 100% 100%;
      repeat: no-repeat;
    }
  }

  &-form {
    display: flex;
    align-items: center;
    justify-content: center; // 确保表单在水平居中
    width: 100%; // 使用 100% 宽度适应各种设备
    height: auto; // 移除固定高度，自动适应内容

    ::v-deep .el-form {
      width: 100%; // 使用 100% 宽度，适应父容器
      max-width: 360px; // 设置最大宽度，避免过大
      position: relative; // 使用相对定位
      top: 130px; // 向下移动 10px
      left: 120px; // 向右移动 10px

      &-item {
        width: 100%;
        margin-bottom: 3vh;

        &:nth-last-child(2) {
          margin-bottom: 0;
        }

        &__content {
          width: 100%;
        }
      }

      .el-input {
        &__inner {
          height: 40px;
          padding-left: 45px;
          border-radius: 4px;
          border: 2px solid #03A095;

          &:hover,
          &:focus {
            border-color: #348bed;
          }
        }

        &__prefix {
          display: flex;
          align-items: center;
          justify-content: center;
          left: 0;
          width: 40px;
          height: 100%;
          color: $color-primary;
          font-size: 24px;
          border-right: $border-default;
        }
      }
    }

    &-vcode {
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 100%;

      .el-input {
        width: calc(100% - 110px); // 动态调整输入框宽度，适应验证码
      }

      .captcha_wrapper {
        background-color: #fff;
        display: inline-block;
        width: 100px;
        height: 40px;
        border-radius: 4px;
        border: 2px solid white;

        &:hover,
        &:focus {
          border-color: $border-light;
        }

        .captcha {
          height: 100%;
          width: 100%;
        }
      }
    }

    &-btn {
      width: 100%;
      margin-top: 1vh;
      font-size: 20px;
      font-weight: bold;
      color: $text-regular;
      background-color: #03A095;

      &:hover {
        box-shadow: inset 0 0 10px $border-lighter;
      }
    }
  }


}
.change-pwd {
  text-align: right;
}
.login-footer {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 8vh;
  color: #58a8ff;
  font-size: 18px;
  line-height: 8vh;
  text-align: center;
  background: {
    image: url('~@/assets/img/bg_login_footer.png');
    repeat: no-repeat;
    size: 100% 100%;
  }
}

</style>
