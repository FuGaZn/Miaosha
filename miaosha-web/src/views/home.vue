<template>
  <div>
    <layout></layout>
    <div style="margin-left: 900px; width: 250px;" class="coupon">
      <h1 style="font-size: 18px; color: rgba(69,71,73,0.9);text-align: left;padding-left: 30px;">红包卡券</h1>
      <div class="coupon" v-for="item in myCoupons" :key="item.cid">
        <div style="width: 240px;height: 70px;background-color: rgba(255,86,88,0.9);margin: 5px;float:left;">
          <div style="float:left;width: 240px;color: white">
            <span style="height: 70px;text-align: center;line-height: 70px;">{{ item.description }}</span>
          </div>
        </div>
      </div>

    </div>
    <div style="width: 500px;margin-left: 260px;position: relative;margin-top: -30px">
      <span
        style="position: absolute;left: 10px;top: 10px;font-size: 20px;font-weight: bold;color: #3c3f41">{{ userInfo.name }}</span>
      <span style="position: absolute;left: 10px;top: 60px;color: #3c3f41">手机号：{{ userInfo.phone }}</span>
    </div>
  </div>
</template>

<script>
import Layout from '@/components/layout'
import {getToken} from "../utils/auth";

export default {
  name: "home",
  components: {Layout},
  created() {
    this.initMyCoupons()
    this.initUserInfo()
    localStorage.removeItem('InQueue')
  },
  data() {
    return {
      userInfo: {},
      myCoupons: []
    }
  },
  methods: {
    initUserInfo() {
      this.$http.get("http://localhost:8082/user/info", {
        headers: {
          'X-token': getToken()
        }
      }).then(response => {
        const {data} = response
        this.userInfo = data.data.userInfo
      })
    },
    initMyCoupons() {
      // let uid = 1
      this.$http.get("http://localhost:8082/coupon/list/my", {
        headers: {
          'X-token': getToken(),
        }
      }).then(response => {
        const {data} = response
        this.myCoupons = data.data.coupons
      })
    },
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
