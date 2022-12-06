<template>
    <ul id="spot-list" class="spot-list">
        <li>
            <SpotFilter />
        </li>
    </ul>
</template>

<script>
import SpotFilter from './components/SpotFilter.vue'

export default {
  name: 'SpotList',
  components: {
    SpotFilter
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
