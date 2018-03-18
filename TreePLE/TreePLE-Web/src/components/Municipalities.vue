<template>
	<div id="municipalities">
    <h1> Municipalities </h1>
    <div id="listing">
      <h3> listing all municipalities: </h3>
      <ol>
        <li v-for="m in municipalities">
          {{m.name}}
        </li>
      </ol>
    </div>

    <div id="newmunicipality">
      name:  <input placeholder="Municipality Name" v-model="newMunicipality" type=text>
      <button @click="createMunicipality(newMunicipality)">Create</button>
    </div>

    <div class="alert alert-secondary" role="alert" v-if="errorMunicipality" style="color:red">Error: {{errorMunicipality.response.data.message}}  </div>
    
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
      response: []
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
    updateView: function () {
      //  Initializing trees from backend
      AXIOS.get('/municipalities')
      .then(response => {
        // JSON responses are automatically parsed.
        this.municipalities = response.data
      })
      .catch(e => {
        this.errorMunicipality = e
      })
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
</style>