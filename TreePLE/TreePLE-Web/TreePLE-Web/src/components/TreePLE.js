import axios from 'axios'
var config = require('../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

function TreeDto(id, species, status, diameter, municipality, treeLocation) {
  this.id = id
  this.species = species
  this.status = status
  this.diameter = diameter
  this.municipality = municipality
  this.treeLocation = treeLocation
}

export default {
  name: 'treeple',
  data() {
    return {
      trees: [],
      newTree: '',
      errorTree: '',
      response: []
    }
  },
  created: function() {
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
