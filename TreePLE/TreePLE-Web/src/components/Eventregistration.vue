<template>
  <div id="eventregistration">
    <h2>Participants</h2>
    <table>
      <tr v-for="participant in participants">
          <td> {{participant.name}} </td>
          <td>
            <ul>
              <li v-for="event in participant.events">
                {{event.name}}
              </li>
            </ul>
          </td>
      </tr>
      <tr>
          <td>
              <input type="text" placeholder="Participant Name" v-model="newParticipant">
          </td>
          <td>
              <button @click="createParticipant(newParticipant)">Create</button>
          </td>
      </tr>
    </table>


    <h2>Events</h2>
    <table>
      <tr v-for="event in events">
         <td v-for="property in event"> {{property}}  </td>
      
      </tr>
      <tr>
          <td>
              <input type="text" placeholder="Event Name" v-model="newEvent.name">
              <input type="date" v-model="newEvent.eventDate" placeholder="YYYY-MM-DD">
              <input type="time" v-model="newEvent.startTime" placeholder="HH:mm">
              <input type="time" v-model="newEvent.endTime" placeholder="HH:mm">
          </td>
          <td>
              <button @click="createEvent(newEvent)">Create</button>
          </td>
      </tr>
    </table>

    <h2>Register</h2>
    <div>
        <label> participant </label>
        <select  v-model='newRegistration.participant'>
          <option v-for="participant in participants">
            {{ participant.name }}
          </option>
        </select>
        <br>

        <label> event </label>
        <select v-model="newRegistration.event">
          <option v-for="event in events" >
            {{ event.name }}
          </option>
        </select>
       
        <br>
        <button v-bind:disabled="(newRegistration.participant == '' || newRegistration.event == '')" @click="register(newRegistration)">Create</button>

    </div>

    <p>
      <span v-if="errorParticipant" style="color:red">Error:{{errorParticipant}}</span>
      <span v-if="errorEvent" style="color:red">Error:{{errorEvent}}</span>     
      <span v-if="errorRegistration" style="color:red">Error:{{errorRegistration}}</span>
    </p>
  </div>
</template>

<script src="./registration.js">
</script>

<style>
  #eventregistration {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    color: #2c3e50;
    background: #f2ece8;
  }
</style>