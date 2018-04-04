<template>
	<div id="residents">
    <h1> Residents </h1>

    <div id="newResident">
      <h3> create new Resident: </h3>

      <label> name </label>
      <input type=text placeholder="name" v-model="newResident.name">
      
      <label> password </label>
      <input type=password placeholder="password" v-model="newResident.password">
      
      <label> email </label>
      <input type=email placeholder="email" v-model="newResident.email">
      
      <label> Type </label>
      <select v-model="newResident.type">
        <option> resident </option>
        <option> environmentalscientist </option>
        <option> municipalarborist </option>
      </select>

      <label> latitude </label>
      <input type text v-model="newResident.residentLocation.lat" placeholder="latitude Location">

      <label> longitude </label>
      <input type text v-model="newResident.residentLocation.lng" placeholder="longitude Location">

      <button @click="createResident(newResident)">Create</button>
    </div>


    <div id="listing">
      <h3> listing all residents: </h3>
      <ol>
        <li v-for="resident in residents">
          <p> name: {{resident.name}} </p>
          <p> email: {{resident.email}} </p>
          <p> type: {{resident.type}} </p>
          <p> residentLocation: lng: {{resident.propertyLocation.lng}}  lat:{{resident.propertyLocation.lat}} </p>
          <h2> ----------------------- </h2>
        </li>
      </ol>
    </div>

    <div class="alert alert-secondary" role="alert" v-if="errorResident" style="color:red">Error: {{errorResident.response.data.message}}  </div>

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
  name: 'residents',
  data () {
    return {
      residents: [],
      newResident: {name: '', password: '', email: '', type: '', residentLocation: { lng: '', lat: '' }},
      errorResident: '',
      response: []
    }
  },
  created: function () {
    this.updateView()
  },
  methods: {
    createResident: function (newResident) {
      AXIOS.post('/residents/', {}, {
        params: {name: newResident.name, password: newResident.password, email: newResident.email, longitude: newResident.residentLocation.lng, latitude: newResident.residentLocation.lat, type: newResident.type}
      })
      .then(response => {
        // JSON responses are automatically parsed.
        this.updateView()
      })
      .catch(e => {
        this.errorResident = e
      })
    },
    updateView: function () {
      //  Initializing residents from backend
      AXIOS.get('/residents')
      .then(response => {
        // JSON responses are automatically parsed.
        this.residents = response.data
      })
      .catch(e => {
        this.errorResident = e
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

#newResident {
  float:right;
}

#newResident input, #newResident select {
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
</style>