<template>
  <div class="navigation">

    <nav id="navigation-menu" class="navigation-menu">
        <span id="nav-map" class="menu-item nav-map">
            <img src="@/assets/img/map_icon.svg" class="item-image" />
            <label class="item-label">MAP</label>
        </span>
        <span id="nav-favorite" class="menu-item nav-favorite">
            <img src="@/assets/img/favorite_icon.svg" class="item-image" />
            <label class="item-label">お気に入り</label>
        </span>
        <span id="nav-add_spot" class="menu-item nav-add_spot">
            <img src="@/assets/img/add_icon.svg" class="item-image" />
            <label class="item-label">登録</label>
        </span>
        <span id="nav-diary" class="menu-item nav-diary">
            <img src="@/assets/img/diary_icon.svg" class="item-image" />
            <label class="item-label">日記</label>
        </span>
    </nav>
  </div>
</template>

<script>
export default {
  name: 'NavigationMenu'
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

.navigation {
    grid-area: nav;
}
.navigation-menu {
    margin: 0;
    width: 100vw;
    padding: var(--inset-gap);
    background-color: antiquewhite;
    display: flex;
    justify-content: space-around;
}
.menu-item {
    display: grid;
    grid-template-areas: 
        "image"
        "label";
    font-size: 0.5em;
    text-align: center;
    width: 50px;
    cursor: pointer;
}
.menu-item:hover {
    opacity: 2;
}
.item-image {
    grid-area: image;
    width: 1.5rem;
    height: 1.5rem;
    margin: 0 auto;
}
.item-label {
    grid-area: label;
    text-align: center;
}

._forwardmap:hover {
    text-decoration: underline;
    font-weight: bold;
    cursor: pointer;
}

</style>
