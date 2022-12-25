<template>
  <div id="map" class="map">


  </div>
</template>

<script>
export default {
  name: 'SpotMap',
  data() {
    return {
    };
  },
  mounted() {
    window.onload = () => {
        const gmapsc = document.createElement("script");
        const apikey = document.getElementById("googleMapApiKey");
        gmapsc.src = `https://maps.googleapis.com/maps/api/js?key=${apikey.value}&callback=initMap`;
        document.body.appendChild(gmapsc);

        CONTEXT_ROOT = document.getElementById("contextRoot").value;
    };
  },
}
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>


</style>
