<template>
  <div id="app">
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <a class="navbar-brand" href="/">TreePLE  <img src="./assets/logo.png"></a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item active">
            <a class="nav-link" href="/">Home <span class="sr-only">(current)</span></a>
          </li>
          <li class="nav-item active">
            <a class="nav-link" href="/#/municipalities">Municipalities <span class="sr-only">(current)</span></a>
          </li>
          <li class="nav-item active">
            <a class="nav-link" href="/#/trees">Trees <span class="sr-only">(current)</span></a>
          </li>

           <li class="nav-item active">
            <a class="nav-link" href="/#/map">Map <span class="sr-only">(current)</span></a>
          </li>
          <li class="nav-item active">
            <a class="nav-link" href="/#/residents"> Residents <span class="sr-only">(current)</span></a>
          </li>

           <li class="nav-item active">
            <a class="nav-link" href="/#/transactions"> Transactions <span class="sr-only">(current)</span></a>
          </li>

          <li class="nav-item active">
            <a class="nav-link" href="http://ecse321-12.ece.mcgill.ca:8080/app-release.apk"> Download APK <span class="sr-only">(current)</span></a>
          </li>
        </ul>
      </div>
    </nav>
    <br>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'app'
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

img {
  display:inline;
  width:3em;
  margin-right:5em;
}

#navMenu {
  background-color:black;
}

#navMenu li {
  float: left;
  display: inline;
  margin:0;
  width:10%;
}

</style>
