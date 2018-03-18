<template>
	<div id="treemap">
    <h1> Tree Listing </h1>
    <br>

    <div> hello {{$route.params.resident}} <br> to add a tree right click on the map </div>
    <div class="alert alert-secondary" role="alert" v-if="errorTree" style="color:red">Error: {{errorTree}}  </div>

    <gmap-map 
    id="mymap"
    :center="center"
    :zoom="11"      
    @rightclick="modalPopUp"
    >

    <gmap-marker
    v-for="(m, index) in positions"
    :key="index"
    :icon.sync="m.icon"
    :position="m.position"
    :clickable="true"
    :draggable="false"
    v-on:click='m.isClicked = !m.isClicked'
  >
      <gmap-info-window
        v-if="m.isClicked"
        :position="m.position">
        <ul id="treeBubbleInfo">
          <li> id: {{m.treeData.id}} </li>
          <li> species: {{m.treeData.species}} </li>
          <li> Status: {{m.treeData.status}} </li>
          <li> diameter: {{m.treeData.diameter}} </li>
          <li> municipality: {{m.treeData.municipality.name}} </li>
          <li> treeLocation: lng:{{m.treeData.treeLocation.lng}}  lat:{{m.treeData.treeLocation.lat}} </li>
      </ul>
    </gmap-info-window>
  </gmap-marker>

  </gmap-map>

  <!-- The Modal -->
  <div id="myModal" class="modal">
    <!-- Modal content -->
    <div class="modal-content">
      <span class="close">&times;</span>
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
      
      <button @click="createTree(newTree)">Create</button>
    </div>
  </div>

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
      icon: {
        url: './../assets/tree.png',
        size: {width: 46, height: 46, f: 'px', b: 'px'},
        scaledSize: {width: 23, height: 23, f: 'px', b: 'px'}
      },
      trees: [],
      newTree: {id: '', species: '', status: '', diameter: '', municipality: '', treeLocation: { lng: '', lat: '' }},
      errorTree: '',
      response: [],
      center: {lat: 45.5, lng: -73.5},
      positions: [],
      treestatuslist: [],
      municipalities: [],
      treespecieslist: []
    }
  },
  created: function () {
    AXIOS.get('/treestatuslist')
    .then(response => {
      // JSON responses are automatically parsed.
      this.treestatuslist = response.data
    })
    .catch(e => {
      this.errorTree = e
    })

    AXIOS.get('/treespecieslist')
    .then(response => {
      // JSON responses are automatically parsed.
      this.treespecieslist = response.data
    })
    .catch(e => {
      this.errorTree = e
    })

    this.updateView()
  },
  methods: {
    modalPopUp: function (e) {
      this.newTree.treeLocation.lat = e.latLng.lat()
      this.newTree.treeLocation.lng = e.latLng.lng()
      var modal = document.getElementById('myModal')
      modal.style.display = 'block'
      var span = document.getElementsByClassName('close')[0]
      // When the user clicks on <span> (x), close the modal
      span.onclick = function () {
        modal.style.display = 'none'
      }
    },
    createTree: function (newTree) {
      AXIOS.post('/trees/', {}, {
        params: {treespecies: newTree.species, treestatus: newTree.status, diameter: newTree.diameter, longitude: newTree.treeLocation.lng, latitude: newTree.treeLocation.lat, municipality: newTree.municipality}
      })
      .then(response => {
        // JSON responses are automatically parsed.
        var modal = document.getElementById('myModal')
        modal.style.display = 'none'
        this.updateView()
        this.errorTree = ''
      })
      .catch(e => {
        this.errorTree = e
      })
    },
    updateView: function () {
      AXIOS.get('/municipalities')
      .then(response => {
        // JSON responses are automatically parsed.
        this.municipalities = response.data
      })
      .catch(e => {
        this.errorTree = e
      })

      //  Initializing trees from backend
      AXIOS.get(`/trees`)
      .then(response => {
        // JSON responses are automatically parsed.
        this.trees = response.data
        for (var i = 0; i < this.trees.length; i++) {
          this.positions.push({ position: {lat: parseFloat(this.trees[i].treeLocation.lat), lng: parseFloat(this.trees[i].treeLocation.lng)}, icon: {url: 'http://maps.google.com/mapfiles/kml/shapes/parks.png', size: {width: 46, height: 46, f: 'px', b: 'px'}, scaledSize: {width: 23, height: 23, f: 'px', b: 'px'}}, treeData: this.trees[i], isClicked: false })
        }
      })
    .catch(e => {
      this.errorTree = e
    })
      this.$forceUpdate()
    }
  }
}
</script>

<style scoped>
#mymap {
  width: 80%;
  height:40em;
  margin: 0 auto;
} 
#treeBubbleInfo {
  list-style-type: none;
  margin: auto 0;
  padding:0;
}

/* The Modal (background) */
.modal {
    display: none; /* Hidden by default */
    position: fixed; /* Stay in place */
    z-index: 1; /* Sit on top */
    padding-top: 100px; /* Location of the box */
    left: 0;
    top: 0;
    width: 100%; /* Full width */
    height: 100%; /* Full height */
    overflow: auto; /* Enable scroll if needed */
    background-color: rgb(0,0,0); /* Fallback color */
    background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
}

/* Modal Content */
.modal-content {
    background-color: #fefefe;
    margin: auto;
    padding: 20px;
    border: 1px solid #888;
    width: 80%;
}

/* The Close Button */
.close {
    color: #aaaaaa;
    float: right;
    font-size: 28px;
    font-weight: bold;
}

.close:hover,
.close:focus {
    color: #000;
    text-decoration: none;
    cursor: pointer;
}

input {
  text-align: center; 
}

::-webkit-input-placeholder {
   text-align: center;
}

:-moz-placeholder { /* Firefox 18- */
   text-align: center;  
}

::-moz-placeholder {  /* Firefox 19+ */
   text-align: center;  
}

:-ms-input-placeholder {  
   text-align: center; 
}

</style>