// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import store from './store'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

var axios = require('axios')
axios.defaults.baseURL = "http://localhost:8443/api"
axios.defaults.withCredentials = true
Vue.prototype.$axios = axios
Vue.config.productionTip = false

Vue.use(ElementUI)
Vue.use(mavonEditor)
// let bc = JSON.parse(localStorage.getItem("user"))

router.beforeEach((to, from, next) => {

  if (store.state.username && to.path.startsWith('/admin')) {
    initAdminMenu(router, store)
  }
  if (store.state.username && to.path.startsWith('/login')) {
    next({
      name: 'admin'
      // name: 'admin/dashboard'
    })
  }
  // console.log(bc, 123)
  if (to.meta.requireAuth) {
    // console.log(store.state.user.username)
    if (store.state.username) {
      axios.get('/authentication').then(resp => {
        if (resp) next()
      })
    } else {
      next({
        path: 'login',
        query: { redirect: to.fullPath }
      })
    }
  } else {
    next()
  }
}
)

// http response 拦截器
axios.interceptors.response.use(
  response => {
    return response
  },
  error => {
    if (error) {
      store.commit('logout')
      router.replace('/login')
    }
    // 返回接口返回的错误信息
    return Promise.reject(error)
  })

const initAdminMenu = (router, store) => {
  // 防止重复触发加载菜单操作
  if (store.state.adminMenus.length > 0) {
    return
  }
  axios.get('/menu').then(resp => {
    if (resp && resp.status === 200) {
      var fmtRoutes = formatRoutes(resp.data)
      router.addRoutes(fmtRoutes)
      store.commit('initAdminMenu', fmtRoutes)
    }
  })
}

const formatRoutes = (routes) => {
  let fmtRoutes = []
  if (routes) {
    routes.forEach(route => {
      if (route.children) {
        route.children = formatRoutes(route.children)
      }
      let fmtRoute = {
        path: route.path,
        component: resolve => {
          require(['./components/admin/' + route.component + '.vue'], resolve)
        },
        name: route.name,
        nameZh: route.nameZh,
        iconCls: route.iconCls,
        meta: {
          requireAuth: true
        },
        children: route.children
      }
      fmtRoutes.push(fmtRoute)
    })
    return fmtRoutes
  }

}

/* eslint-disable no-new */
new Vue({
  el: '#app',
  render: h => h(App),
  router,
  store,
  components: { App },
  template: '<App/>'
})
