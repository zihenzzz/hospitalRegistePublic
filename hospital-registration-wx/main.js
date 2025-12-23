import App from './App'
import store from './store'
import uView from './uni_modules/uview-ui'

// #ifndef VUE3
import Vue from 'vue'

Vue.use(uView)

// 配置Vue
Vue.config.productionTip = false

App.mpType = 'app'

const app = new Vue({
  store,
  ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  app.use(store)
  return {
    app
  }
}
// #endif

