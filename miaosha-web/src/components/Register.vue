<template>
  <div class="label">
    <h1 style="margin-top: 35px">Register</h1>
    <div>
      <el-form status-icon :rules="rules" :model="ruleForm" ref="ruleForm">
        <el-form-item prop="phone">
          <el-input placeholder="Phone number" class="input" v-model="ruleForm.phone" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" placeholder="Password" v-model="ruleForm.password" class="input"
                    auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item prop="checkPass">
          <el-input type="password" placeholder="Check password" v-model="ruleForm.checkPass" class="input"
                    auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input @blur="checkVerify" style="width: 185px;" placeholder="Verify Code"
                    v-model="ruleForm.code" class="input"></el-input>
          <el-button @click="sendVerifyCode" style="margin-left: 5px; width: 80px" type="primary"
                     :loading="sendingVerify">{{ sendingBtnContext }}
          </el-button>
        </el-form-item>
      </el-form>
      <el-button class="input" type="primary" @click="register('ruleForm')">Register</el-button>
      <div>
        <el-button type="text" @click="toLogin">I already have an account</el-button>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "register",
  props: {
    toLogin: {
      type: Function,
      default: function () {
      }
    }
  },
  data() {
    let validatePhone = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入手机号'));
      } else {
        if (this.ruleForm.phone !== '') {
          let regex = /^(13[0-9]{9})|(15[0-9]{9})|(17[0-9]{9})|(18[0-9]{9})|(19[0-9]{9})$/;
          if (!regex.test(this.ruleForm.phone)) {
            callback(new Error('手机号格式不正确'))
          } else
            this.$refs.ruleForm.validateField('password');
        }
        callback();
      }
    };
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (value.length < 8) {
          callback(new Error('密码不能少于8位'))
        } else {
          let reg = /^(?=.*[a-zA-Z])(?=.*\d).{8,}$/;
          if (!reg.test(value)) {
            callback(new Error('密码必须至少包括数字与字母'))
          } else {
            this.$refs.ruleForm.validateField('checkPass');
          }
          callback();
        }
      }
    };
    let validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      standard_verify_code: "123456",
      sendingVerify: false,
      sendingBtnContext: "Send",
      ruleForm: {
        phone: '',
        password: "",
        checkPass: '',
        code: ''
      },
      rules: {
        password: [
          {validator: validatePass, trigger: 'blur'}
        ],
        checkPass: [
          {validator: validatePass2, trigger: 'blur'}
        ],
        phone: [
          {validator: validatePhone, trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    trimStr(str) {
      return str.replace(/(^\s*)|(\s*$)/g, "");
    },
    checkVerify() {
      if (this.verify_code == "") {
        this.isErrorShow = false;
        return
      }
      if (this.standard_verify_code != this.verify_code) {
        this.isErrorShow = true
        return
      }
      if (this.standard_verify_code == this.verify_code) {
        this.isErrorShow = false
      }
    }
    ,

    sendVerifyCode() {
      this.resetTime()
    }
    ,

    resetTime() {
      var _this = this
      var timer = null
      var t = 60
      var m = 0
      var s = 0
      m = Math.floor(t / 60 % 60)
      m < 10 && (m = '0' + m)
      s = Math.floor(t % 60)

      function countDown() {
        s--
        s < 10 && (s = '0' + s)
        if (s.length >= 3) {
          s = 59
          m = '0' + (Number(m) - 1)
        }
        if (m.length >= 3) {
          m = '00'
          s = '00'
          clearInterval(timer)
        }

        if (parseInt(s) != 0) {
          _this.sendingBtnContext = s + 's'
          _this.sendingVerify = true
        } else {
          _this.sendingBtnContext = 'Resend'
          _this.sendingVerify = false
        }
      }

      timer = setInterval(countDown, 1000)
    }
    ,

    register(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let uservo = {
            phone: this.ruleForm.phone,
            password: this.$md5(this.ruleForm.password),
          }
          this.$http.post('http://localhost:8082/register', uservo).then()
        } else {
          return false;
        }
      });
    }
    ,

    info(type, content) {
      if (type === 0) {   //normal info
        this.$message(content)
      } else if (type === 1) {
        this.$message({
          message: content,
          type: 'success'
        })
      } else if (type === 2) {
        this.$message({
          message: content,
          type: 'warning'
        });
      } else if (type === 3) {
        this.$message.error(content);
      }
    }

  }
}
</script>

<style scoped>
.label {
  background-color: #ffffff;
  width: 400px;
  height: 450px;
  border-radius: 5px;
}

.input {
  width: 270px;
  height: 40px;
}
</style>
