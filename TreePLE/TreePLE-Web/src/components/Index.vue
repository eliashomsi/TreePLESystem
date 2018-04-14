<template>
	<div id="index">
    <h1> Welcome to TreePLE System </h1>
      <br>
    
    <div id="intro">
      <br>
    <div id="buttons">
        <div class="alert alert-secondary" role="alert" v-if="errorResident" style="color:red">Error: {{errorResident.response.data.message}}  </div>
        <div class="alert alert-primary" role="alert" v-if="errorLogin" style="color:red">Error: {{errorLogin.response.data.message}}  </div>
        <div class="alert alert-success" role="alert" v-if="loggedin" style="color:red"> 
          Welcome: {{loggedin.email}} Proceed to <router-link :to="{ name: 'TreeMapResident', params: { resident: loggedin.email }}">Map</router-link>  </div>
       
        <br>
        <button class="mainButton" onclick="document.getElementById('id01').style.display='block'" style="padding:1.3em; width:auto; margin:1em;">Register</button>

        <div id="id01" class="modal">      
          <div class="modal-content animate">
            <div class="imgcontainer">
              <span onclick="document.getElementById('id01').style.display='none'" class="close" title="Close Modal">&times;</span>
              <img src="../assets/logo.png" alt="PartyBNB" class="avatar">
            </div>
            <div class="container">
              <label><i>Name</i></label>
              <br>
              <input type="text" placeholder="Name" name="name" v-model="newResident.name" required>
              <br>
              <label><i>email</i></label>
              <br>
              <input type="email" placeholder="email" name="email" v-model="newResident.email" required>
              <br>
              <label><i>Password</i></label>
              <br>
              <input type="password" placeholder="********" name="password" v-model="newResident.password" required>
              <br>
              
              <label><i> Type </i></label>
              <br>
              <select v-model="newResident.type">
                <option> resident </option>
                <option> environmentalscientist </option>
                <option> municipalarborist </option>
              </select>
              <br><br>
            
              <button @click="createResident(newResident)" onclick="document.getElementById('id01').style.display='none'">Register</button>
            </div>

            <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">
              <b>Cancel</b>
            </button>
            <br>
            <br>

          </div>
        </div>


        <button onclick="document.getElementById('id02').style.display='block'" style="padding:1.3em; width:auto; margin:1em;">Login</button>
        <div id="id02" class="modal">      
          <div class="modal-content animate">
            <div class="imgcontainer">
              <span onclick="document.getElementById('id02').style.display='none'" class="close" title="Close Modal">&times;</span>
              <img src="../assets/logo.png" alt="PartyBNB" class="avatar">
            </div>
            <div class="container">
              <label><i>Email</i></label>
              <br>
              <input type="email" placeholder="Email" v-model="newLogin.email" required>
              <br>
              <label><i>Password</i></label>
              <br>
              <input type="password" placeholder="********"   v-model="newLogin.password" required>
              <br>
              <br>
              <button onclick="document.getElementById('id02').style.display='none'" @click="login(newLogin)">Login</button>
            </div>
            <button type="button" onclick="document.getElementById('id02').style.display='none'" class="cancelbtn">
              <b>Cancel</b>
            </button>
            <br>
            <br>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// login scripts
var modal = document.getElementById('id01')
var modal2 = document.getElementById('id02')
window.onclick = function (event) {
  if (event.target === modal) {
    modal.style.display = 'none'
  } else if (event.target === modal2) {
    modal2.style.display = 'none'
  }
}

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
  name: 'index',
  data () {
    return {
      residents: [],
      newResident: {name: '', password: '', email: '', type: '', residentLocation: { lng: '', lat: '' }},
      newLogin: {email: '', password: ''},
      errorResident: '',
      response: [],
      loggedin: '',
      errorLogin: ''
    }
  },
  created: function () {
    console.log('default')
  },
  methods: {
    createResident: function (newResident) {
      AXIOS.post('/residents/', {}, {
        params: {name: newResident.name, password: newResident.password, email: newResident.email, longitude: 22, latitude: 22, type: newResident.type}
      })
      .then(response => {
        // JSON responses are automatically parsed.
        this.errorResident = ''
        this.errorLogin = ''
      })
      .catch(e => {
        this.errorResident = e
      })
    },
    updateView: function () {
      console.log('updateview')
    },
    login: function (newLogin) {
      AXIOS.post('/login/', {}, {
        params: {email: newLogin.email, password: newLogin.password}
      })
      .then(response => {
        // JSON responses are automatically parsed.
        this.loggedin = response.data
        this.errorLogin = ''
      })
      .catch(e => {
        this.errorLogin = e
      })
    }
  }
}
</script>

<style scoped>
#intro {
  background-image: url("../assets/index-background.png");
  height: 46em;
  background-repeat: no-repeat;
  background-size: cover;
}

#buttons {
  position: relative;
  top: 7em;
  font-size: 1.5em;
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
.cancelbtn{
    width: auto;
    padding: 10px 8px;
    background-color: #AC3933;
}

.imgcontainer{
    text-align: center;
    margin: 24px 0 12px 0;
    width:100%;
    text-align:center;
    position: relative;
}



img.avatar{
    width: 30%;
    border-radius: 50%;
    margin: auto 0;
}

.container{
    padding: 16px;
    text-align:center;
    width:100%;
}
span.psw{
    float: right;
    padding-top: 16px;
}

.modal{
    display: none;
    position: fixed;
    text-align:center;
    z-index: 1;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: #000000;
    background-color: #00000034;
    padding-top: 60px;
}

.modal-content{
    background-color: #ffffff;
    margin: 5% auto 15% auto;
    border: 1px solid #888888;
    text-align:center;
    padding:auto auto;
    width: 25%;
}

.close{
    position: absolute;
    right: 25px;
    top: 0;
    color: #692424;
    font-size: 35px;
    font-weight: bold;
}

.close:hover, .close:focus{
    cursor: #ff0000;
    cursor: pointer;
}

.animate {
    -webkit-animation: animatezoom 0.6s;
    animation: animatezoom 0.6s;
}

.mobile-element{
    text-align:center;
}
@-webkit-keyframes animatezoom{
    from {-webkit-transform: scale(0)}
    to {-webkit-transform: scale(1)}
}

@keyframes animatezoom{
    from {transform: scale(0)}
    to {transform: scale(1)}
}

@media screen and (max-width: 300px){
    scan.psw{
        display: block;
        float: none;
    }
    .cancelbtn{
    }
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