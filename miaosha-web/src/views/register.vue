<template>
  <div>
    <div style="position:fixed; left: 0; top: 0; width: 100%; height: 100%; background-color: #54a7fc;"></div>
    <div style="position: absolute; left: 50%; margin-left: -205px; top: 80px">
      <register :to-login="toLogin" style="position: absolute; left: 0px; top: 0px;"></register>
    </div>

  </div>
</template>

<script>
import Register from '../components/Register'

export default {
  name: "register-page",
  components: {Register},
  methods: {
    toLogin() {
      this.$router.push("/login")
    }
  }
}
</script>

<style scoped>

</style>
