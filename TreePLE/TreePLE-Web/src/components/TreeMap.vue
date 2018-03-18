<template>
	<div id="treemap">
	    <p>
	      <span v-if="errorTree" style="color:red">Error: {{errorTree}} </span>
	    </p>

	    <gmap-map 
        :center="center"
        :zoom="11"
        style="width: 500px; height: 500px"
      >
       <gmap-marker
          v-for="(m, index) in positions"
          :key="index"
          :position="m"
          :clickable="true"
          :draggable="false"
          @click="updateRight(index)"
        ></gmap-marker>
      
    </gmap-map>
  </div>
</template>

<script>
// init script for services
import axios from 'axios'
var config = require('../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

// import google maps
import * as VueGoogleMaps from 'vue2-google-maps'
import Vue from 'vue'
Vue.use(VueGoogleMaps, {
  load: {
    key: 'AIzaSyBuXIUEEYNasB92TxFn0lB2eWZtEJzXSs4',
    v: '3'
  }
})

//  export module
export default {
  name: 'treemap',
  data () {
    return {
      trees: [],
      newTree: '',
      errorTree: '',
      response: [],
      tempPosition: {lat: 45.5, lng: -73.5},
      center: {lat: 45.5, lng: -73.5},
      positions: []
    }
  },
  created: function () {
    //  Initializing trees from backend
    AXIOS.get(`/trees`)
      .then(response => {
        // JSON responses are automatically parsed.
        this.trees = response.data
        for (var i = 0; i < this.trees.length; i++) {
          this.positions.push({lat: parseFloat(this.trees[i].treeLocation.lat), lng: parseFloat(this.trees[i].treeLocation.lng)})
        }
      })
      .catch(e => {
        this.errorTree = e
      })
  },
  methods: {
    updateRight: function (index) {
      window.alert(this.trees[index].id)
    }
  }
}
</script>

<style scoped>
  
</style>