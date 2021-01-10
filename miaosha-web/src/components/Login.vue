<template>
  <div class="label">
    <h1 style="margin-top: 40px;margin-bottom: 40px;">Welcome!</h1>
    <div>
      <el-form status-icon :rules="rules" :model="user" ref="user">
        <el-form-item prop="phone">
          <el-input placeholder="Phone" class="input" v-model="user.phone"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" placeholder="Password" v-model="user.password" class="input"
                    onpaste="return false" oncopy="return false" oncut="return false"></el-input>
        </el-form-item>
      </el-form>
      <el-button class="input" type="primary" @click="login">Login</el-button>
      <el-button type="text" @click="toRegister">I haven't registered</el-button>
    </div>
    <vcode :show="isShow" @success="success" @close="close"></vcode>
  </div>
</template>

<script>
import vcode from "vue-puzzle-vcode";
import {getToken, setToken} from "../utils/auth";

export default {
  name: "login",
  components: {
    vcode
  },
  data() {
    let validatePhone = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入手机号'));
      } else {
        if (this.user.phone !== '') {
          let regex = /^(13[0-9]{9})|(15[0-9]{9})|(17[0-9]{9})|(18[0-9]{9})|(19[0-9]{9})$/;
          if (!regex.test(this.user.phone)) {
            callback(new Error('手机号格式不正确'))
          } else
            this.$refs.user.validateField('password');
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
          callback();
        }
      }
    };
    return {
      isShow: false,
      user: {
        password: "",
        phone: "",
      },
      rules: {
        password: [
          {validator: validatePass, trigger: 'blur'}
        ],
        phone: [
          {validator: validatePhone, trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    toRegister() {
      this.$router.push("/register")
    },
    login() {
      if (this.user.name == "") {
        this.info(2, "姓名不能为空")
        return
      }
      if (this.user.password == "") {
        this.info(2, "密码不能为空")
        return
      }
      if (this.user.password.length < 6) {
        this.info(2, "密码长度不能少于6")
        return
      }
      this.isShow = true
    },
    success() {
      this.isShow = false
      let uservo = {
        phone:this.user.phone,
        password: this.$md5(this.user.password)
      }
      console.log(localStorage.getItem("Ip"))
      this.$http.post("http://localhost:8082/login", uservo).then(response => {
        const {data} = response
        const {token} = data.data
        console.log('token '+token)
        setToken(token)
        console.log('getT '+getToken())
        this.$router.push("/home")
      })

    },
    close() {
      this.isShow = false
    },
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
  width: 350px;
  height: 350px;
  border-radius: 5px;
}

.input {
  width: 270px;
  height: 40px;
}
</style>
