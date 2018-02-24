import axios from 'axios'
var config = require('../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

function ParticipantDto(name) {
  this.name = name
  this.events = []
}

function EventDto(name, date, start, end) {
  this.name = name
  this.eventDate = date
  this.startTime = start
  this.endTime = end
}

export default {
  name: 'eventregistration',
  data() {
    return {
      participants: [],
      newParticipant: '',
      errorParticipant: '',
      events: [],
      newEvent: {},
      newRegistration: { participant: '', event: '' },
      errorRegistration: '',
      errorEvent: '',
      response: []
    }
  },
  created: function() {
    // Initializing participants from backend
    AXIOS.get('/participants/')
      .then(response => {
        // JSON responses are automatically parsed.
        this.participants = response.data
      })
      .catch(e => {
        this.errorParticipant = e;
      });

    // Initializing events from backend
    AXIOS.get('/events/')
      .then(response => {
        // JSON responses are automatically parsed.
        this.events = response.data
      }).catch(e => {
        this.errorEvent = e;
      })
  },
  methods: {
    createParticipant: function(participantName) {
      AXIOS.post(`/participants/` + participantName, {}, {
          params: {}
        })
        .then(response => {
          // JSON responses are automatically parsed.
          this.participants.push(response.data)
          this.newParticipant = ''
          this.errorParticipant = ''
        })
        .catch(e => {
          var errorMsg = e.response.data.message
          console.log(errorMsg)
          this.errorParticipant = errorMsg
        });
    },
    createEvent: function(newEvent) {
      AXIOS.post(`/events/` + newEvent.name, {}, {
          params: { startTime: newEvent.startTime, endTime: newEvent.endTime, date: newEvent.eventDate }
        })
        .then(response => {
          // JSON responses are automatically parsed.
          this.events.push(response.data)
          this.newEvent = {}
          this.errorEvent = ''
        })
        .catch(e => {
          var errorMsg = e.response.data.message
          console.log(errorMsg)
          this.errorEvent = errorMsg
        });
    },
    register: function(newRegistration) {
      AXIOS.post('/register/', {}, {
          params: { participant: newRegistration.participant, event: newRegistration.event }
        })
        .then(response => {
          this.newRegistration = { participant: '', event: '' }
          this.errorRegistration = ''

          //update participant
          AXIOS.get('/participants/')
            .then(response => {
              // JSON responses are automatically parsed.
              this.participants = response.data
            })
            .catch(e => {
              this.errorParticipant = e;
            });
        })
        .catch(e => {
          var errorMsg = e.response.data.message
          console.log(errorMsg)
          this.errorRegistration = errorMsg + " " + newRegistration
        });
    },
  }
}
