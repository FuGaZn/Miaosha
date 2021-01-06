<template>
  <div>
    <div style="width: 100%; position: absolute; left: 0; top: 0; height: 50px;">
       <el-dropdown style="position: absolute; right:40px; margin-top: 20px" @command="handleCommand">
        <el-button type="primary" size="small"><i class="el-icon-arrow-down el-icon--center"></i></el-button>
        <el-dropdown-menu slot="dropdown" >
          <el-dropdown-item command="log_out">退出登陆</el-dropdown-item>
          <el-dropdown-item command="modify_pass">修改密码</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <el-dialog title="修改密码" :visible.sync="dialogVisible" width="290px">
      <el-form :model="modify_password">
        <el-form-item>
          <el-input type="password" v-model="modify_password.oldPassword" style="width: 240px" placeholder="输入旧密码" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input type="password" v-model="modify_password.newPassword" placeholder="输入新密码" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="modify" type="primary" style="width: 240px">确定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import {removeToken} from "../utils/auth";

export default {
  name: "nav-bar",
  props:{
    backgroundColor:{
      type:"",
      default:"background-color:#1E89E0"
    }
  },
  data(){
    return{
      dialogVisible:false,
      modify_password:{
        oldPassword:'',
        newPassword:''
      }
    }
  },
  mounted(){
  },
  methods:{
    modify(){
      let oldPasswd = this.modify_password.oldPassword
      let newPasswd = this.modify_password.newPassword
      if(oldPasswd == ''){
        this.info(2,'请填入旧密码')
        return
      }
      if (newPasswd == ''){
        this.info(2,'请填入新密码')
        return
      }
      if (newPasswd === oldPasswd){
        this.info(2,'新密码不能与原密码相同')
        return
      }
      let data = {
        oldPassword: this.$md5(this.modify_password.oldPassword),
        newPassword: this.$md5(this.modify_password.newPassword)
      }
      this.$http.post("localhost:8082/user/modify/pwd", data).then(response=>{
        const {data} = response
        const {code, message} = data
        if (code === 20000){
          this.info(1, message)
        }else{
          this.info(3, message)
        }
      })
    },
    handleCommand(command){
      if (command == 'log_out') {
        removeToken()
        this.$router.push("/login")
      }else if(command == 'modify_pass'){
        this.dialogVisible = true
      }
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

</style>
