<template>
  <div v-loading.fullscreen.lock="loading">
    <layout></layout>
    <div>
      <!--      <h1 style="text-align: left;margin-left: 280px; font-size: 22px;color: rgba(255, 86, 88, 0.9)">天天领神券</h1>-->
      <div style="margin-left: 270px;" class="coupon" v-for="item in coupons" :key="item.cid">
        <div style="width: 300px;height: 80px;background-color: rgba(255, 86, 88, 0.9);margin: 5px;float:left;">
          <div style="float:left;width: 240px;color: white">
            <span style="height: 80px;text-align: center;line-height: 80px;">{{ item.description }}</span>
          </div>
          <div @click="buyCoupon(item.cid)" style="cursor: pointer;height: 80px;width: 60px;background-color: rgb(255,60,61);
            color: white;font-size: 18px;float:left;">
            <div style="margin-top: 20px">
              <div>抢</div>
              <div>购</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import LeftNavBar from "../components/layout/LeftNavBar";
import Navbar from "../components/layout/Navbar";
import Layout from '@/components/layout'
import {getToken} from "../utils/auth";

export default {
  name: "market",
  components: {Layout, Navbar, LeftNavBar},
  created() {
    this.initCoupons()
    console.log(localStorage.getItem('InQueue'))
    if (localStorage.getItem('InQueue') !== undefined && localStorage.getItem('InQueue') !== null) {
      this.getOrderResponse()
    }
  },
  data() {
    return {
      coupons: [],
      loading: false
    }
  },
  methods: {
    initCoupons() {
      this.$http.get("http://localhost:8082/coupon/list/able").then(response => {
        const {data} = response
        this.coupons = data.data.coupons
      })
    },
    getOrderResponse(param) {
      console.log(param)
      let timer
      this.loading = true;
      this.$http.post("http://localhost:8082/coupon/order/check", {cid:  param}, {
        headers: {
          'X-token': getToken()
        }
      }).then(response => {
        const {data} = response
        const {code, message} = data
        if (code == 20000) {
          localStorage.removeItem('InQueue')
          this.loading = false
          this.info(1, '恭喜你，抢券成功！')
          this.$router.push("/home")
        } else if (code == 50001) {
          localStorage.removeItem('InQueue')
          this.loading = false
          this.info(3, '抢券失败')
        } else if (code == 50002) {
          console.log("轮询")
          timer = setTimeout(() => {
            this.getOrderResponse(param)
          }, 2000)
        }
      })
    },
    buyCoupon(cid) {
      //    let uid = 1
      //   let param = {cid, uid}
      this.$http.post("http://localhost:8082/coupon/order/create", {cid:  cid}, {
        headers: {
          'X-token': getToken()
        }
      }).then(response => {
        const {data} = response
        const {code, message} = data
        console.log(code, message)
        switch (code) {
          case 20001: // 已抢票成功
            this.info(1, message)
            break
          case 20002: // 排队
            localStorage.setItem("InQueue", '1')
            this.getOrderResponse(cid)
            break
          case 50001: // 抢票失败
            this.info(3, message)
            break
          case 50002: // 已抢光
            this.info(0, message)
            break
          default:
            this.info(3, message)
        }
      })
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
.coupon {
  -webkit-user-select: none;

  -moz-user-select: none;

  -ms-user-select: none;

  user-select: none;
}
</style>
