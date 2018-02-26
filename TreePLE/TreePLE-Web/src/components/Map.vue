<template>
	<div id="map">
		<gmap-map
		    :center="center"
		    :zoom="11"
		    style="width: 500px; height: 500px"
		  >
		   <gmap-marker
		      :key="index"
		      v-for="(m, index) in markers"
		      :position="m.position"
		      :clickable="true"
		      :draggable="true"
		      @click="center=m.position"
		    ></gmap-marker>

		</gmap-map>
	</div>
</template>

<script>
  import * as VueGoogleMaps from 'vue2-google-maps'
  import Vue from 'vue'
  Vue.use(VueGoogleMaps, {
    load: {
      key: 'AIzaSyBuXIUEEYNasB92TxFn0lB2eWZtEJzXSs4',
      v: '3'
      // libraries: 'places', //// If you need to use place input
    }
  })
  export default {
    data () {
      return {
        center: {lat: 45.5017, lng: -73.5673},
        markers: [{
          position: {lat: 45.5017, lng: -73.5673}
        }, {
          position: {lat: 11.0, lng: 11.0}
        }]
      }
    }
  }
</script> 


<style scoped>
gmap-map {
	text-align: center;
	padding:3em;
}
</style>