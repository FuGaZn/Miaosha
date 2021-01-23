<template>
  <div>
    <navbar></navbar>
    <admin-left-nav-bar style="float: left"></admin-left-nav-bar>

    <div style="margin-left: 270px;margin-top: 30px">
      <el-button style="margin-left: 640px" size="mini" type="primary" icon="el-icon-plus" @click="addDialogVisible=true">添加</el-button>
      <el-table :data="coupons" style="width: 1000px;margin-top: 10px" :default-sort="{prop: 'publishTime'}">
        <el-table-column prop="description" label="优惠券内容" width="300">
        </el-table-column>
        <el-table-column prop="total" label="总数量" sortable width="150">
        </el-table-column>
        <el-table-column prop="sale" label="已售出" sortable width="150">
        </el-table-column>
        <el-table-column prop="publishTime" label="发布时间" sortable width="180">
        </el-table-column>
        <el-table-column label="操作" width="180px">
          <template slot-scope="scope">
            <el-button :disabled="scope.row.able === 1"
              type="primary"
              size="mini"
              @click="handlePublish(scope.$index, scope.row)">发布
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="添加优惠券" :visible.sync='addDialogVisible' width="420px">
      <el-form :model="coupon_add" size="small" label-width="80px" label-position="right">
        <el-form-item label="内容">
          <el-input type="textarea" style="width: 240px;float: left" v-model="coupon_add.description"></el-input>
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number style="width: 240px;float:left;" v-model="coupon_add.total" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item>
          <el-button style="float:left;" @click="addDialogVisible = false" type="danger">取消</el-button>
          <el-button @click="saveCoupon" style="float:left;">保存</el-button>
          <el-button @click="publishCoupon" style="float:left;" type="primary">直接发布</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import Navbar from "../components/layout/Navbar";
import AdminLeftNavBar from "../components/layout/AdminLeftNavBar";
import {getToken} from "../utils/auth";
export default {
  name: "admin",
  components:{Navbar,AdminLeftNavBar},
  data() {
    return {
      addDialogVisible:false,
      coupon_add:{
        cid:0,
        description:'',
        total:10,
        sale:0,
        publishTime:'',
        able:0,
      },
      coupons:[
      ]
    }
  },
  created() {
    this.$http.get("http://localhost:8082/coupon/list/all").then(response=>{
      const {data} = response
      const {coupons} = data.data
      this.coupons = coupons
    })
  },
  methods: {
    saveCoupon(){
      this.$prompt('请输入密码', '确定要保存吗？', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'password'
      }).then(({password}) => {
        this.$http.post("http://localhost:8082/coupon/save1",this.coupon_add,{
          headers:{
            'X-token':getToken(),
            'X-Ip':localStorage.getItem("Ip")
          }
        }).then(response=>{
          const {data} = response
          const {cid} = data.data
          this.coupon_add.able = 0
          this.coupon_add.cid = cid
          this.coupons.push(this.coupon_add)
          this.initCouponAdd()
        })
      })
    },
    publishCoupon(){
      this.$prompt('请输入密码', '确定要发布吗？', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'password'
      }).then(({password}) => {
        this.$http.post("http://localhost:8082/coupon/publish",{
          params:{
            password: password,
            coupon:this.coupon_add
          }
        },{
          headers:{
            'X-token':getToken(),
            'X-Ip':localStorage.getItem("Ip")
          }
        }).then(response=>{
          const {data} = response
          const {coupon} = data.data
       //   this.coupon_add.able = 1
          this.initCouponAdd()
          this.coupons.push(coupon)
        })
      })
    },
    initCouponAdd(){
      this.coupon_add = {
        cid:0,
        description:'',
        total:1,
        sale:0,
        publishTime:'',
        able:0,
      }
    },
    handlePublish(row, index){
      this.$prompt('请输入密码', '确定要发布吗？', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'password'
      }).then(({password}) => {
        this.$http.post("http://localhost:8082/coupon/publish",{
          params:{
            password: password,
            coupon:row
          }
        },{
          headers:{
            'X-token':getToken(),
            'X-Ip':localStorage.getItem("Ip")
          }
        }).then(response=>{
          this.coupons[index].able = 1
        })
      })
    },
    handleDelete(row, index){
      this.$prompt('请输入密码', '确定要删除吗？', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'password'
      }).then(({password}) => {
        let data = {
          password: password,
          coupon:row
        }
        this.$http.post("http://localhost:8082/coupon/delete",{
          params:{
            password: password,
            coupon:row
          }
        },{
          headers:{
            'X-token':getToken(),
            'X-Ip':localStorage.getItem("Ip")
          }
        }).then(response=>{
          this.coupons.splice(index, 1)
        })
      })
    }
  }
}
</script>

<style scoped>

</style>
