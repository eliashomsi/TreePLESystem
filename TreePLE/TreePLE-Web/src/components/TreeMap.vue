<template>
	<div id="treemap">
    <h1> Tree Listing </h1>
    <br>

    <div> hello {{$route.params.resident}} <br> to add a tree <b> right click </b> on the map </div>
    <div class="alert alert-secondary" role="alert" v-if="errorTree" style="color:red">Error: {{errorTree.response.data.message}}  </div>
    <div class="alert alert-secondary" role="alert" v-if="errorTransaction" style="color:red">Error: {{errorTransaction.response.data.message}}  </div>
    <div >
      <h2> display statistical data </h2>
      <button value="show Polygon" v-on:click='showPolygon = !showPolygon' :disabled="showPolygon == true" >Show Polygon </button>
      <button value="show bioData insidePolygon" v-on:click='calculateBioDataPolygon' :disabled="!showPolygon" >show bioData insidePolygon </button>
    </div>

    <gmap-map 
    id="mymap"
    :center="center"
    :zoom="11"      
    @rightclick="modalPopUpNewTree"
    @center_changed="updateCenter"
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

  <gmap-polygon :paths="paths" :editable="true" :dragable="true" @paths_changed="updateEdited($event)" v-if="showPolygon">
  </gmap-polygon>
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
      <button @click="modalPopUpNewTransaction(m.treeData.id)" v-on:click='m.isClicked = !m.isClicked'> Mark This Tree</button>
      <button @click="deleteTree(m.treeData.id)" v-on:click='m.isClicked = !m.isClicked'> Delete This Tree</button>
      <button @click="modalPopUpEditTree(m.treeData.id)" v-on:click='m.isClicked = !m.isClicked'> Edit This Tree</button>
    </gmap-info-window>
  </gmap-marker>

  </gmap-map>

  <!-- The Modal tree modal-->
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
      <input type=text v-model="newTree.diameter" placeholder="Tree Diameter (cm)">

      <label> municipality </label>
      <select v-model="newTree.municipality">
        <option v-for="m in municipalities"> {{m.name}}</option>
      </select>
      
      <button @click="createTree(newTree)">Create</button>
    </div>
  </div>

  <!-- The Modal Transaction modal-->
  <div id="myModal2" class="modal">
    <!-- Modal content -->
    <div class="modal-content">
      <span class="close">&times;</span>
      <label> New Status </label>
      <select v-model="newTransaction.status">
        <option v-for="status in treestatuslist"> {{status}}</option>
      </select>
      
      <button @click="createTransaction(newTransaction)">Submit</button>
    </div>
  </div>


  <!-- The Modal for bio data-->
  <div id="myModal3" class="modal">
    <!-- Modal content -->
    <div class="modal-content">
      <span class="close">&times;</span>
      <h2> most frequent:</h2>

      <h3>species </h3>
      <ul id="species">
      </ul>
      <h3>statuses </h3>
      <ul id="status">
      </ul>
      <h3>diameter</h3>
      <ul id="diameter">
      </ul>
      <h3>municipalities</h3>
      <ul id="municipality">
      </ul>
      <span id="countTrees" > </span>
    </div>
  </div>

  <!-- The Modal for edit data-->
  <div id="myModal4" class="modal">
    <!-- Modal content -->
   <div class="modal-content">
      <span class="close">&times;</span>
     
      <label> Species </label>
      <select v-model="newTree.species">
        <option v-for="species in treespecieslist"> {{species}}</option>
      </select>

      <label> Diameter </label>
      <input type=text v-model="newTree.diameter" placeholder="Tree Diameter (cm)">

      <label> municipality </label>
      <select v-model="newTree.municipality">
        <option v-for="m in municipalities"> {{m.name}}</option>
      </select>
      
      <button @click="editTree(newTree)">Edit</button>
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
      treespecieslist: [],
      newTransaction: {time: '', date: '', email: '', resident: '', tree: '', status: ''},
      errorTransaction: '',
      paths: [
      [ { lat: 45.5, lng: -73.5 }, { lat: 45.53, lng: -73.5 }, { lat: 45.5, lng: -73.5 }, { lat: 45.5, lng: -73.5 } ]
      ],
      edited: null,
      showPolygon: false,
      deleteTreeID: 0,
      editTreeID: 0
    }
  },
  created: function () {
    this.newTransaction.resident = this.$route.params.resident
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
    updateEdited (mvcArray) {
      let paths = []
      for (let i = 0; i < mvcArray.getLength(); i++) {
        let path = []
        for (let j = 0; j < mvcArray.getAt(i).getLength(); j++) {
          let point = mvcArray.getAt(i).getAt(j)
          path.push({lat: point.lat(), lng: point.lng()})
        }
        paths.push(path)
      }
      this.edited = paths
    },
    getDistanceFromLatLonInKm: function (pos1, pos2) {
      var lat1 = pos1.lat
      var lat2 = pos2.lat
      var lon1 = pos1.lng
      var lon2 = pos2.lng
      var R = 6371// Radius of the earth in km
      var dLat = this.deg2rad(lat2 - lat1)// deg2rad below
      var dLon = this.deg2rad(lon2 - lon1)
      var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(this.deg2rad(lat1)) * Math.cos(this.deg2rad(lat2)) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2)
      var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
      var d = R * c // Distance in km
      return d
    },
    deg2rad: function (deg) {
      return deg * (Math.PI / 180)
    },
    updateCenter (center) {
      this.center = {
        lat: center.lat(),
        lng: center.lng()
      }
      this.paths = [
        [center, center, center, center]
      ]
      this.showPolygon = false
    },
    modalPopUpNewTree: function (e) {
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
    modalPopUpNewTransaction: function (e) {
      this.newTransaction.tree = e
      var modal = document.getElementById('myModal2')
      modal.style.display = 'block'
      var span = document.getElementsByClassName('close')[1]
      // When the user clicks on <span> (x), close the modal
      span.onclick = function () {
        modal.style.display = 'none'
      }
    },
    modalPopUpEditTree: function (id) {
      this.editTreeID = id
      var modal = document.getElementById('myModal4')
      modal.style.display = 'block'
      var span = document.getElementsByClassName('close')[3]
      // When the user clicks on <span> (x), close the modal
      span.onclick = function () {
        modal.style.display = 'none'
      }
    },
    deleteTree: function (e) {
      this.deleteTreeID = e
      AXIOS.post('/trees/delete', {}, {
        params: {id: this.deleteTreeID}
      })
      .then(response => {
        // JSON responses are automatically parsed.
        this.updateView()
        this.errorTree = ''
        location.reload()
      })
      .catch(e => {
        this.errorTree = e
      })
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
    createTransaction: function (newTransaction) {
      if (!newTransaction.resident) {
        window.alert('please go back and login/register')
        return
      }

      var d = new Date()
      newTransaction.time = this.padme(d.getHours(), 2) + ':' + this.padme(d.getMinutes(), 2)
      newTransaction.date = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
      AXIOS.post('/transactions/', {}, {
        params: {time: newTransaction.time, date: newTransaction.date, resident: newTransaction.resident, tree: newTransaction.tree, status: newTransaction.status}
      })
      .then(response => {
        // JSON responses are automatically parsed.
        var modal = document.getElementById('myModal2')
        modal.style.display = 'none'
        this.errorTransaction = ''
        this.updateView()
      })
      .catch(e => {
        this.errorTransaction = e
      })
    },
    padme: function (num, size) {
      var s = num + ''
      while (s.length < size) {
        s = '0' + s
      }
      return s
    },
    inside: function (point, vs) {
      // ray-casting algorithm based on
      // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
      var x = point.lat
      var y = point.lng

      var inside = false
      for (var i = 0, j = vs.length - 1; i < vs.length; j = i++) {
        var xi = vs[i].lat
        var yi = vs[i].lng
        var xj = vs[j].lat
        var yj = vs[j].lng

        var intersect = ((yi > y) !== (yj > y)) &&
            (x < (xj - xi) * (y - yi) / (yj - yi) + xi)
        if (intersect) inside = !inside
      }

      return inside
    },
    getMostFrequentData (treesInPolygon) {
      // do calculation
      var speciesDict = {}
      var statusDict = {}
      var municipalityDict = {}
      var diameterDict = {}

      for (var j = 0; j < treesInPolygon.length; j++) {
        if (speciesDict[treesInPolygon[j].species] !== undefined) {
          speciesDict[treesInPolygon[j].species] += 1
        } else {
          speciesDict[treesInPolygon[j].species] = 1
        }

        if (statusDict[treesInPolygon[j].status] !== undefined) {
          statusDict[treesInPolygon[j].status] += 1
        } else {
          statusDict[treesInPolygon[j].status] = 1
        }

        if (municipalityDict[treesInPolygon[j].municipality.name] !== undefined) {
          municipalityDict[treesInPolygon[j].municipality.name] += 1
        } else {
          municipalityDict[treesInPolygon[j].municipality.name] = 1
        }

        if (diameterDict[treesInPolygon[j].diameter] !== undefined) {
          diameterDict[treesInPolygon[j].diameter] += 1
        } else {
          diameterDict[treesInPolygon[j].diameter] = 1
        }
      }

      var speciesSorted = this.sortObjectFrequency(speciesDict)
      var statusSorted = this.sortObjectFrequency(statusDict)
      var municipalitySorted = this.sortObjectFrequency(municipalityDict)
      var diameterSorted = this.sortObjectFrequency(diameterDict)

      return {'species': speciesSorted, 'status': statusSorted, 'municipality': municipalitySorted, 'diameter': diameterSorted}
    },
    calculateBioDataPolygon: function () {
      this.showPolygon = false
      let treesInPolygon = []
      if (!this.edited) {
        return
      }

      for (var i = 0; i < this.trees.length; i++) {
        var e = this.trees[i]
        if (this.inside(e.treeLocation, this.edited[0])) {
          treesInPolygon.push(e)
        }
      }

      var mostFrequent = this.getMostFrequentData(treesInPolygon)

      var speciesSorted = mostFrequent['species']
      var statusSorted = mostFrequent['status']
      var municipalitySorted = mostFrequent['municipality']
      var diameterSorted = mostFrequent['diameter']

      var modal = document.getElementById('myModal3')

      // add elements to modal
      var list1 = document.getElementById('species')
      var list2 = document.getElementById('status')
      var list3 = document.getElementById('municipality')
      var list4 = document.getElementById('diameter')

      // fill
      this.populateList(list1, speciesSorted, treesInPolygon.length)
      this.populateList(list2, statusSorted, treesInPolygon.length)
      this.populateList(list3, municipalitySorted, treesInPolygon.length)
      this.populateList(list4, diameterSorted, treesInPolygon.length)
      document.getElementById('countTrees').innerHTML = 'trees count: ' + treesInPolygon.length + ' selected from total of ' + this.trees.length

      modal.style.display = 'block'
      var span = document.getElementsByClassName('close')[2]
      // When the user clicks on <span> (x), close the modal
      span.onclick = function () {
        modal.style.display = 'none'
      }
    },
    editTree: function (newTree) {
      AXIOS.post('/trees/edit/', {}, {
        params: {treespecies: newTree.species, id: this.editTreeID, diameter: newTree.diameter, municipality: newTree.municipality}
      })
      .then(response => {
        // JSON responses are automatically parsed.
        var modal = document.getElementById('myModal4')
        modal.style.display = 'none'
        this.updateView()
        this.errorTree = ''
      })
      .catch(e => {
        this.errorTree = e
      })
    },
    populateList (list, speciesSorted, total) {
      list.innerHTML = ''
      for (var i = 0; i < 3 && i < speciesSorted.length; i++) {
        var li = document.createElement('li')
        li.appendChild(document.createTextNode('(' + speciesSorted[i][0] + ') ~ ' + Math.round(100 * ((speciesSorted[i][1]) / total)) + '%'))
        list.appendChild(li)
      }
    },
    sortObjectFrequency: function (maxSpeed) {
      var sortable = []
      for (var vehicle in maxSpeed) {
        sortable.push([vehicle, maxSpeed[vehicle]])
      }
      sortable.sort(function (a, b) {
        return b[1] - a[1]
      })
      return sortable
    },
    hash: function (obj) {
      // some unique object-dependent key
      return obj.totallyUniqueEmployeeIdKey // just an example
    },
    updateView: function () {
      this.errorTree = ''
      this.errorTransaction = ''
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
        var selectedTree = parseInt(this.$route.params.id) - 1
        this.trees = response.data
        for (var i = 0; i < this.trees.length; i++) {
          if (i === selectedTree) {
            this.positions.push({ position: {lat: parseFloat(this.trees[i].treeLocation.lat), lng: parseFloat(this.trees[i].treeLocation.lng)}, icon: {url: 'http://maps.google.com/mapfiles/kml/shapes/star.png', size: {width: 46, height: 46, f: 'px', b: 'px'}, scaledSize: {width: 46, height: 46, f: 'px', b: 'px'}}, treeData: this.trees[i], isClicked: true })
            this.center = {lat: parseFloat(this.trees[i].treeLocation.lat), lng: parseFloat(this.trees[i].treeLocation.lng)}
          } else {
            this.positions.push({ position: {lat: parseFloat(this.trees[i].treeLocation.lat), lng: parseFloat(this.trees[i].treeLocation.lng)}, icon: {url: 'http://maps.google.com/mapfiles/kml/shapes/parks.png', size: {width: 46, height: 46, f: 'px', b: 'px'}, scaledSize: {width: 23, height: 23, f: 'px', b: 'px'}}, treeData: this.trees[i], isClicked: false })
          }
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
    width: 30%;
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

input {
  text-align: center; 
}
select, input {
  border-radius: 1em;
  border-color: none;
}
button{
    background-color: #060E3D;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    cursor: pointer;
    display:block;
    margin:auto;
    border-radius: 1em;
    display:inline-block;
}

button:hover{
    background-color: #555555;
}
button:disabled {
  background-color:gray;
}

#myModal3 ul {
  list-style-type: none;
  text-align: center;
  margin-left:-3em;
}

</style>