<template>
	<div id="trees">
    <h1> Trees </h1>

    <div id="newTree">
      <h3> create new tree: </h3>

      <label> Status </label>
      <select v-model="newTree.status">
        <option v-for="status in treestatuslist"> {{status}}</option>
      </select>

      <label> Species </label>
      <select v-model="newTree.species">
        <option v-for="species in treespecieslist"> {{species}}</option>
      </select>

      <label> Diameter </label>
      <input type=text v-model="newTree.diameter" placeholder="Tree Diameter">

      <label> municipality </label>
      <select v-model="newTree.municipality">
        <option v-for="m in municipalities"> {{m.name}}</option>
      </select>

      <label> latitude </label>
      <input type text v-model="newTree.treeLocation.lat" placeholder="latitude Location">

      <label> longitude </label>
      <input type text v-model="newTree.treeLocation.lng" placeholder="longitude Location">

      <button @click="createTree(newTree)">Create</button>
    </div>


    <div id="listing">
      <h3> listing all trees: </h3>
      <ol>
        <li v-for="tree in trees">
          <p> id: {{tree.id}} </p>
          <p> species: {{tree.species}} </p>
          <p> Status: {{tree.status}} </p>
          <p> diameter: {{tree.diameter}} </p>
          <p> municipality: {{tree.municipality.name}} </p>
          <p> treeLocation: lng:{{tree.treeLocation.lng}}  lat:{{tree.treeLocation.lat}} </p>
        </li>
      </ol>
    </div>

    <div class="alert alert-secondary" role="alert" v-if="errorTree" style="color:red">Error: {{errorTree.response.data.message}}  </div>

  </p>
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

//  export module
export default {
  name: 'trees',
  data () {
    return {
      trees: [],
      newTree: {id: '', species: '', status: '', diameter: '', municipality: '', treeLocation: { lng: '', lat: '' }},
      errorTree: '',
      response: [],
      treestatuslist: [],
      municipalities: [],
      treespecieslist: []
    }
  },
  created: function () {
    this.updateView()

    //  getting constant info
    AXIOS.get('/treestatuslist')
    .then(response => {
      // JSON responses are automatically parsed.
      this.treestatuslist = response.data
    })
    .catch(e => {
      this.errorTree = e
    })

    //  Initializing trees from backend
    AXIOS.get('/treespecieslist')
    .then(response => {
      // JSON responses are automatically parsed.
      this.treespecieslist = response.data
    })
    .catch(e => {
      this.errorTree = e
    })
  },
  methods: {
    createTree: function (newTree) {
      AXIOS.post('/trees/', {}, {
        params: {treespecies: newTree.species, treestatus: newTree.status, diameter: newTree.diameter, longitude: newTree.treeLocation.lng, latitude: newTree.treeLocation.lat, municipality: newTree.municipality}
      })
      .then(response => {
        // JSON responses are automatically parsed.
        this.updateView()
      })
      .catch(e => {
        this.errorTree = e
      })
    },
    updateView: function () {
      //  Initializing trees from backend
      AXIOS.get('/trees')
      .then(response => {
        // JSON responses are automatically parsed.
        this.trees = response.data
      })
      .catch(e => {
        this.errorTree = e
      })

      AXIOS.get('/municipalities')
      .then(response => {
        // JSON responses are automatically parsed.
        this.municipalities = response.data
      })
      .catch(e => {
        this.errorTree = e
      })
    }
  }
}
</script>

<style scoped>

#listing li, #listing h3 {
  float:left;
  display:block;
  clear:left;
  margin-bottom:1em;
}

#newTree {
  float:right;
}

#newTree input, #newTree select {
  display:block;
}
</style>