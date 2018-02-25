<template>
	<div id="treeple">
	    <h2>Trees</h2>
	    <table>
	      <tr v-for="tree in trees">
	      	
	      	<td> {{tree}} </td>
	      </tr>
	    </table>

	    <p>
	      <span v-if="errorTree" style="color:red">Error: {{errorTree}} </span>
	    </p>
  	</div>
</template>


<script src="./TreePLE.js">
</script>

<style scoped>
</style>