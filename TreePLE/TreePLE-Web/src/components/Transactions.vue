<template>
  <div id="transactions">
    <h1> Transactions </h1>

    <div id="newTransaction">
      <h3> create new Transaction: </h3>

      <label> time </label>
      <input type=time placeholder="time" v-model="newTransaction.time">
      
      <label> date </label>
      <input type=date placeholder="date" v-model="newTransaction.date">

      <label> resident </label>
      <select v-model="newTransaction.resident">
        <option v-for="resident in residents">
          {{resident.email}}
        </option>
      </select>

      <label> tree id </label>
      <select v-model="newTransaction.tree">
        <option v-for="tree in trees">
          {{tree.id}}
        </option>
      </select>

      <label> status </label>
      <select v-model="newTransaction.status">
        <option v-for="status in treestatuslist">
          {{status}}
        </option>
      </select>


      <button @click="createTransaction(newTransaction)">Create</button>
    </div>

    <div id="listing">
      <h3> listing all transactions: </h3>
      <ol>
        <li v-for="transaction in transactions">
          <p> time: {{transaction.time}} </p>
          <p> date: {{transaction.date}} </p>
          <p> resident: {{transaction.resident.name}} </p>
          <p> tree: {{transaction.tree.id}} </p>
          <p> status: {{transaction.status}} </p>
          <h2> ----------------------- </h2>
        </li>
      </ol>
    </div>

    <div class="alert alert-secondary" role="alert" v-if="errorTransaction" style="color:red">Error: {{errorTransaction.response.data.message}}  </div>

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
  name: 'transactions',
  data () {
    return {
      transactions: [],
      newTransaction: {time: '', date: '', email: '', resident: '', tree: '', status: ''},
      errorTransaction: '',
      response: [],
      residents: [],
      trees: [],
      treestatuslist: []
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
      this.errorTransaction = e
    })
  },
  methods: {
    createTransaction: function (newTransaction) {
      AXIOS.post('/transactions/', {}, {
        params: {time: newTransaction.time, date: newTransaction.date, resident: newTransaction.resident, tree: newTransaction.tree, status: newTransaction.status}
      })
      .then(response => {
        // JSON responses are automatically parsed.
        this.updateView()
        this.errorTransaction = ''
      })
      .catch(e => {
        this.errorTransaction = e
      })
    },
    updateView: function () {
      //  Initializing transactions from backend
      AXIOS.get('/transactions')
      .then(response => {
        // JSON responses are automatically parsed.
        this.transactions = response.data
      })
      .catch(e => {
        this.errorTransaction = e
      })

      //  Initializing residents from backend
      AXIOS.get('/residents')
      .then(response => {
        // JSON responses are automatically parsed.
        this.residents = response.data
      })
      .catch(e => {
        this.errorTransaction = e
      })

      //  Initializing residents from backend
      AXIOS.get('/trees')
      .then(response => {
        // JSON responses are automatically parsed.
        this.trees = response.data
      })
      .catch(e => {
        this.errorTransaction = e
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

#newTransaction {
  float:right;
}

#newTransaction input, #newTransaction select {
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