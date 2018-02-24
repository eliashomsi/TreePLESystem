import Vue from 'vue'
import Router from 'vue-router'
import Eventregistration from '@/components/Eventregistration'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Eventregistration',
      component: Eventregistration
    },
    {
      path: '/app',
      name: 'Eventregistration',
      component: Eventregistration
    }
  ]
})
