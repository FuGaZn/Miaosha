<template>
  <div>
    <navbar></navbar>
    <left-nav-bar style="float:left;"></left-nav-bar>
  </div>
</template>

<script>
import Navbar from "./Navbar";
import LeftNavBar from "./LeftNavBar";
export default {
  name: "index",
  components:{
    Navbar,LeftNavBar
  }
}
</script>

<style scoped>

</style>
