import Vue from 'vue'
import Router from 'vue-router'
import Map from '@/components/TreeMap'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'TreeMap',
      component: Map
    }
  ]
})
