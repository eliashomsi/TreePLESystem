<template>
	<div id="municipalities">
    <h1> Municipalities </h1>
    <div id="listing">
      <h3> listing all municipalities: </h3>
      <ol>
        <li v-for="m in municipalities">
          <a  href="#" @click="calculateBioInMunicipality(m.name)" > {{m.name}} </a>
        </li>
      </ol>
    </div>

    <div id="newmunicipality">
      name:  <input placeholder="Municipality Name" v-model="newMunicipality" type=text>
      <button @click="createMunicipality(newMunicipality)">Create</button>
    </div>

    <div class="alert alert-secondary" role="alert" v-if="errorMunicipality" style="color:red">Error: {{errorMunicipality.response.data.message}}  </div>
    

    <!-- The Modal for bio data-->
    <div id="myModal3" class="modal">
      <!-- Modal content -->
      <div class="modal-content">
        <span class="close">&times;</span>
        <h2> most frequent in selected municipality:</h2>

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
  name: 'municipalities',
  data () {
    return {
      municipalities: [],
      newMunicipality: '',
      errorMunicipality: '',
      response: [],
      trees: [],
      errorTree: ''
    }
  },
  created: function () {
    this.updateView()
  },
  methods: {
    createMunicipality: function (newMunicipality) {
      //  Initializing trees from backend
      AXIOS.post('/municipalities/' + newMunicipality)
      .then(response => {
        // JSON responses are automatically parsed.
        this.updateView()
      })
      .catch(e => {
        this.errorMunicipality = e
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
#municipalities li {
}

#newmunicipality, #listing {
  margin-top: 4em;
  margin-left:3em;
  float:left;
  clear:both;
}
#listing li {
  width:0px;
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