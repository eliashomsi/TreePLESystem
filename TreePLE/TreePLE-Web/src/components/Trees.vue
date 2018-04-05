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
      <input type=text v-model="newTree.diameter" placeholder="Tree Diameter (cm)">

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
          <p> id: <router-link :to="{ name: 'TreeMapSelectTree', params: { id: tree.id }}"> {{tree.id}}</router-link> </p>
          <p> species:   <a  href="#" @click="calculateBioInSpecies(tree.species)" > {{tree.species}}  </a> </p>
          <p> status:   <a  href="#" @click="calculateBioInStatus(tree.status)" > {{tree.status}}  </a> </p>
          <p> diameter:   <a  href="#" @click="calculateBioInDiameter(tree.diameter)" > {{tree.diameter}}  </a> </p>
          <p> municipality:   <a  href="#" @click="calculateBioInMunicipality(tree.municipality.name)" > {{tree.municipality.name}}  </a> </p>
          <p> treeLocation: lng:{{tree.treeLocation.lng}}  lat:{{tree.treeLocation.lat}} </p>
          <h2> ----------------------- </h2>
        </li>
      </ol>
    </div>

    <div class="alert alert-secondary" role="alert" v-if="errorTree" style="color:red">Error: {{errorTree.response.data.message}}  </div>

    <!-- The Modal for bio data-->
    <div id="myModal3" class="modal">
      <!-- Modal content -->
      <div class="modal-content">
        <span class="close">&times;</span>
        <h2> most frequent in selection</h2>

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
    calculateBioInMunicipality: function (name) {
      let treesInPolygon = []

      for (var i = 0; i < this.trees.length; i++) {
        var e = this.trees[i]
        if (e.municipality.name.localeCompare(name) === 0) {
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
      document.getElementById('countTrees').innerHTML = 'trees count: ' + treesInPolygon.length + ' selected from total of ' + this.trees.length

      // fill
      this.populateList(list1, speciesSorted, treesInPolygon.length)
      this.populateList(list2, statusSorted, treesInPolygon.length)
      this.populateList(list3, municipalitySorted, treesInPolygon.length)
      this.populateList(list4, diameterSorted, treesInPolygon.length)

      modal.style.display = 'block'
      var span = document.getElementsByClassName('close')[0]
      // When the user clicks on <span> (x), close the modal
      span.onclick = function () {
        modal.style.display = 'none'
      }
    },
    calculateBioInDiameter: function (diameter) {
      let treesInPolygon = []

      for (var i = 0; i < this.trees.length; i++) {
        var e = this.trees[i]
        if (parseInt(e.diameter) === parseInt(diameter)) {
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
      var span = document.getElementsByClassName('close')[0]
      // When the user clicks on <span> (x), close the modal
      span.onclick = function () {
        modal.style.display = 'none'
      }
    },
    calculateBioInStatus: function (statusName) {
      let treesInPolygon = []

      for (var i = 0; i < this.trees.length; i++) {
        var e = this.trees[i]
        if (e.status.localeCompare(statusName) === 0) {
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
      var span = document.getElementsByClassName('close')[0]
      // When the user clicks on <span> (x), close the modal
      span.onclick = function () {
        modal.style.display = 'none'
      }
    },
    calculateBioInSpecies: function (speciesName) {
      let treesInPolygon = []

      for (var i = 0; i < this.trees.length; i++) {
        var e = this.trees[i]
        if (e.species.localeCompare(speciesName) === 0) {
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
      var span = document.getElementsByClassName('close')[0]
      // When the user clicks on <span> (x), close the modal
      span.onclick = function () {
        modal.style.display = 'none'
      }
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
#myModal3 ul {
  list-style-type: none;
  text-align: center;
  margin-left:-3em;
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
</style>