<template>
  <img alt="Vue logo" src="./assets/logo.png">
  <NavigationMenu />
  <main>
    <HelloWorld msg="First Custom! Hello!"/>

  </main>
</template>

<script>
import NavigationMenu from './components/NavigationMenu.vue'
import HelloWorld from './components/HelloWorld.vue'

export default {
  name: 'App',
  components: {
    NavigationMenu,
    HelloWorld
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

:root {
    --outset-gap: 1.25em;
    --inset-gap: 0.5em;
    --minimum-gap: 0.3em;
    --active-color-b: lightblue;
    --non-active: lightgray;
    --shadow-color: lightgray;
    --positive-color: bisque;
    --nav-height: 5vh;
    --main-height: 90vh;
    --sp-width: 600px;
    --pc-width: 601px;
}
html, body {
    height: 100%;
    width: 100%;
    margin: 0;
    padding: 0;
}
main {
    height: var(--main-height);
}
nav { 
    height: var(--nav-height);
}

@media screen and (max-width:600px){
    nav {
        position: fixed;
        bottom: 0;
    }
}

</style>
