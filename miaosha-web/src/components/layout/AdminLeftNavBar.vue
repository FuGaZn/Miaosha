<template>
  <div>
    <el-menu
      default-active="coupon_manager"
      class="el-menu-vertical-demo"
      @select="handleSelect">
      <el-menu-item index="coupon_manager">
        <i class="el-icon-bank-card"></i>
        <span slot="title">优惠券管理</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "AdminLeftNavBar",
  data() {
    return {
      active:'home'
    };
  },
  created() {
    this.active = this.$route.path.split('/')[1]
  },
  methods: {
    handleSelect(key){
      // if (key === 'coupon_manager'){
      //   this.$router.push('/admin')
      // }
    }
  }
}
</script>

<style scoped>
.el-menu-vertical-demo {
  width: 220px;
  text-align: left;
  padding-left: 20px;
  min-height: 400px;
  -webkit-user-select:none;

  -moz-user-select:none;

  -ms-user-select:none;

  user-select:none;
}
</style>
