<template>
    <div id="search-box" class="search-box">
        <label class="start-position-area hidden">
            <input type="text" id="txt-start-position" class="txt-start-position" placeholder="出発地点の住所" />
            <!-- <button type="button" id="address-search-button" class="address-search-button" >Map上で検索</button> -->
            <!-- このボタンを押すと地図上で検索され、その検索でヒットしたところを出発地点にする的な -->
            <input type="checkbox" id="chk-current-position" class="chk-current-position" onchange="toggleStartPosition()">現在地
        </label>
        <label class="limit hidden">
            <input type="number" id="limit-value" class="limit-value" />
            <select id="limit-type">
                <option value="distance">m</option>
                <option value="time">分</option>
            </select>
            以内
        </label>
        <!-- <label>
            <input type="radio" id="rd-driving" class="radio-travel-mode" name="travel-mode" value="driving"><label for="rd-driving">車</label>
            <input type="radio" id="rd-transit" class="radio-travel-mode" name="travel-mode" value="transit"><label for="rd-transit">交通機関</label>
            <input type="radio" id="rd-bicycling" class="radio-travel-mode" name="travel-mode" value="bicycling"><label for="rd-bicycling">自転車</label>
            <input type="radio" id="rd-walking" class="radio-travel-mode" name="travel-mode" value="walking"><label for="rd-walking">徒歩</label>
        </label> -->
        <!-- <button type="button" id="search-button" class="search-button" onclick="search()">検索</button> -->
    </div>
</template>

<script>

export default {
  name: 'SearchBox'
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
