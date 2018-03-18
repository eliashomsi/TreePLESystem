import Vue from 'vue'
import Router from 'vue-router'
import Map from '@/components/TreeMap'
import Municipalities from '@/components/Municipalities'
import Trees from '@/components/Trees'
import Residents from '@/components/Residents'
import Transactions from '@/components/Transactions'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'TreeMap',
      component: Map
    },
    {
      path: '/municipalities',
      name: 'Municipalities',
      component: Municipalities
    },
    {
      path: '/trees',
      name: 'Trees',
      component: Trees
    },
    {
      path: '/residents',
      name: 'Residents',
      component: Residents
    },
    {
      path: '/transactions',
      name: 'Transactions',
      component: Transactions
    }
  ]
})
