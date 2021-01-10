<template>
  <div>
    <el-menu
      :default-active="active"
      class="el-menu-vertical-demo"
      @select="handleSelect">
        <el-menu-item index="home">
          <i class="el-icon-s-home"></i>
          <span slot="title">我的主页</span>
        </el-menu-item>
        <el-menu-item index="market">
          <i class="el-icon-bank-card"></i>
          <span slot="title">天天神券</span>
        </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "LeftNavBar",
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
      if (key === 'home'){
        this.$router.push('/home')
      }else if (key === 'market'){
        this.$router.push('/market')
      }
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
