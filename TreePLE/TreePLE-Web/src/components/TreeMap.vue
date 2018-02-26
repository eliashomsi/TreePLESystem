<template>
	<div id="treeple">
	    <h2>Trees</h2>
	    <table>
	      <tr v-for="tree in trees">
	      	<td> {{tree}} </td>
	      </tr>
	    </table>

	    <p>
	      <span v-if="errorTree" style="color:red">Error: {{errorTree}} </span>
	    </p>

	    <div id="myMap"></div>
  	</div>
</template>

<script>
import axios from 'axios'
var config = require('../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  name: 'treeple',
  data () {
    return {
      trees: [],
      newTree: '',
      errorTree: '',
      response: []
    }
  },
  created: function () {
    // Initializing trees from backend
    AXIOS.get(`/trees`)
      .then(response => {
        // JSON responses are automatically parsed.
        this.trees = response.data
      })
      .catch(e => {
        this.errorTree = e
      })
  }
}
</script>

<style scoped>
    #myMap {
    height:300px;
    width: 100%;
   }
</style>