<template>
  <el-menu
    :default-active="this.$route.path"
    router
    mode="horizontal"
    background-color="white"
    text-color="#222"
    active-text-color="red"
    style="min-width: 1300px"
  >
    <el-menu-item v-for="(item, i) in navList" :key="i" :index="item.name">
      {{ item.navItem }}
    </el-menu-item>

    <span
      style="
        position: absolute;
        padding-top: 20px;
        right: 43%;
        font-size: 20px;
        font-weight: bold;
      "
      >White Jotter - Your Mind Palace</span
    >
    <!-- logout -->
    <i
      class="el-icon-switch-button"
      v-on:click="logout"
      style="float: right; font-size: 40px; color: #222; padding: 10px"
    ></i>
  </el-menu>
</template>

<script>
export default {
  name: "NavMenu",
  data() {
    return {
      navList: [
        { name: "/index", navItem: "首页" },
        { name: "/jotter", navItem: "笔记本" },
        { name: "/library", navItem: "图书馆" },
        { name: "/admin", navItem: "个人中心" },
      ],
    };
  },
  methods: {
    logout() {
      var _this = this;
      this.$axios.get("/logout").then((resp) => {
        if (resp.data.code === 200) {
          // 前后端状态保持一致
          _this.$store.commit("logout");
          _this.$router.replace("/login");
        }
      });
    },
  },
  $store: {
    logout(state) {
      state.user = [];
      window.localStorage.removeItem("user");
    },
  },
};
</script>

<style scoped>
a {
  text-decoration: none;
}

span {
  pointer-events: none;
}
.el-icon-switch-button {
  cursor: pointer;
  outline: 0;
}
</style>