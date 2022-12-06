<template>
    <div>
        <img src="@/assets/img/filter_icon.svg" class="filter-icon" />
        <span class="distance-filter-box">
            <select id="distance-filter-select">
                <option value="-1">距離指定なし</option>
                <option value="500">500 m以内</option>
                <option value="800">800 m以内</option>
                <option value="1000">1 km以内</option>
                <option value="3000">3 km以内</option>
                <option value="5000">5 km以内</option>
                <option value="10000">10 km以内</option>
                <option value="20000">20 km以内</option>
                <option value="30000">30 km以内</option>
                <option value="50000">50 km以内</option>
            </select>
        </span>
        <span class="distance-sort-box">
            <select id="distance-sort-select">
                <option value="asc">距離が近い順</option>
                <option value="desc">距離が遠い順</option>
            </select>
        </span>
    </div>
</template>

<script>
export default {
  name: 'SpotFilter'
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
