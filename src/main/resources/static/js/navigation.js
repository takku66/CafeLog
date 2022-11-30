class NavigationMenu {
	#navigationMenu = null;
	#contextRoot = null;
	#map = null;
	#favorite = null;
	#addSpot = null;
	#diary = null;

	#uriMap = {
		"map": "/map",
		"favorite": "/favorite",
		"add_spot": "/add_spot",
		"diary": "/diary",

	};

	constructor(){
		const _self = this;
		window.addEventListener("DOMContentLoaded", () => {
			this.#navigationMenu = document.getElementById("navigation-menu");
			this.#contextRoot = document.getElementById("contextRoot");
			this.#map = document.getElementById("nav-map");
			this.#favorite = document.getElementById("nav-favorite");
			this.#addSpot = document.getElementById("nav-add_spot");
			this.#diary = document.getElementById("nav-diary");

			this.#map.addEventListener("click", () => {
				this.forwardPage("map");
			});
			this.#favorite.addEventListener("click", () => {
				this.forwardPage("favorite");
			});
			this.#addSpot.addEventListener("click", () => {
				this.forwardPage("add_spot");
			});
			this.#diary.addEventListener("click", () => {
				this.forwardPage("diary");
			});
		});
	}

	forwardPage(uriKey){
		const contextRoot = this.#contextRoot.value == "/" 
							? "" 
							: this.#contextRoot.value;
		const path = this.#contextRoot.value + this.#uriMap[uriKey];
		document.navigation.action = path;
		document.navigation.method = "post";
		document.navigation.submit();
		
	}	
}

const navmenu = new NavigationMenu();