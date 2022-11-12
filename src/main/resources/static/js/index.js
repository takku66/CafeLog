
/**
 * 開始地点の住所とspotdata.jsで取得したカフェの情報をもとに、<br>
 * GoogoleMapAPIから開始地点からの各距離の算出を行う。<br>
 * 距離の算出ができなくても、カフェリストの表示とマーカーの設定、GoogleMAPへの遷移処理は機能させる
 */
async function search(){

	// 検索される度にリストやマーカーが追加されるため、一旦クリア
	cafeList.clearCafeList();
	clearAllMarker();

	// デフォルトの出発地点は現在地。取得できなかったら東京駅
	startPositionValue = await getCurrentLatLng("35.6809591", "139.7673068");
	
	// TRANSITオプションが使えないので、方針転換。距離だけの算出のため、徒歩だけでよい
	let selectedTravelMode = OPTIONS.TRAVEL_MODE.walking;

	const destinationList = spotdata;

	// GoogleAPIの１回のリクエスト許容量が25のため
	const MAX_REQUEST = 25;

	// 開始地点とカフェまでの距離算出と一覧の描画を行う
	for(let i = 0, max = spotdata.length; i < max; i+=MAX_REQUEST){
		const requestData = spotdata.slice(i, i + MAX_REQUEST);
		const sliceDestination = destinationList.slice(i, i + MAX_REQUEST);
		callDistanceMatrix(new MatrixOptions()
			.setOrigins([startPositionValue])
			.setDestinations(requestData)
			.setTravelMode(selectedTravelMode)
			// .setTransitOptions(transitOptions)
			.setCallBack((response, status) => {
				console.log(response);
				cafeList.drawDistanceList(response, sliceDestination, startPositionValue, selectedTravelMode);
				if (status !== "OK") {
					throw new Error("Distance Matrix処理中にエラーが発生しました。");
				}

			})
		);

	}

	for(let destination of destinationList){
		const marker = marking({
			map: map, 
			position: {lat: destination.lat, lng: destination.lng}, 
			icon: {
				url: "./img/cafe.png", 
				scaledSize: new google.maps.Size(50, 50)
			},
			infoWindow: new InfoWindowCreator(destination.name, "content", 
												{startPosition: startPositionValue,
												category: "喫茶店", 
												lat: destination.lat, 
												lng: destination.lng, 
												travelMode: selectedTravelMode})
												.create(),
			infoWindowCallback: () => {
				const markerTitle = document.getElementsByClassName(`_forwardmap ${destination.lat} ${destination.lng}`)[0];
				markerTitle.addEventListener("click", () => {
					openDirectionMap(`${startPositionValue.lat},${startPositionValue.lng}`, `${destination.lat},${destination.lng},${destination.name}`);
				});
			}
		});
		addMarkerIndex(marker, destination.name);
	}
	marking({
        map: map, 
        position: startPositionValue
    });
	
}
	
function toggleStartPosition(){
	const txtFreePosition = document.getElementById("txt-start-position");
	const chkCurrentPosition = document.getElementById("chk-current-position");

	if(chkCurrentPosition.checked){
		txtFreePosition.disabled = true;
	}else{
		txtFreePosition.disabled = false;
	}
}

function apiCount(){
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || "";
    const token = document.querySelector('meta[name="_csrf"]')?.content || "";
    const url = (CONTEXT_ROOT == "/" ? "" : CONTEXT_ROOT) + "/countapi";
    fetch(url, {
        method: "POST",
        headers: {
            "charset": "UTF-8",
            "Content-Type": "application/json",
            [csrfHeader]: token,
        },
    }).then((res) => {
        if (!res.ok) {
            throw new Error(`${res.status}${res.statusText}`);
        }
        console.log(res);
        return res.text();
    }).then((data) => {
        console.log(data);
        search();
    }).catch((reason) => {
        console.log(reason);
    });
}

const OPTIONS = {
	TRAVEL_MODE: {
		walking: 'WALKING',		// 徒歩
		transit: 'TRANSIT',		// 交通期間
		driving: 'DRIVING',		// 車
		bicycling: 'BICYCLING',		// 自転車
		ja: function(mode){
			switch(mode){
				case OPTIONS.TRAVEL_MODE.walking:
					return "徒歩";
				case OPTIONS.TRAVEL_MODE.transit:
					return "交通機関";
				case OPTIONS.TRAVEL_MODE.driving:
					return "車";
				case OPTIONS.TRAVEL_MODE.bicycling:
					return "自転車";
				default:
					return "移動手段不明";
			}
		},
	},
}
const MatrixOptions = function(){
	this.origins = {};
	this.destinations = {};
	this.travelMode = OPTIONS.TRAVEL_MODE.driving;
	// this.transitOptions = {};
	this.callback = function(response, status){
		console.log("status=" + status);
		console.log(response);
	}

	this.setOrigins = function(origins){
		this.origins = toMatrixOptions(origins);
		return this;
	}
	this.setDestinations = function(destinations){
		this.destinations = toMatrixOptions(destinations);
		return this;
	}
	this.setTravelMode = function(travelMode){
		this.travelMode = travelMode;
		return this;
	}
	this.setCallBack = function(callback){
		this.callback = callback;
		return this;
	}
}
const MarkingOptions = {

}

/**
 * 結果一覧のオブジェクト
 */
class CafeList {
	#resultListArea = null;
	#distanceFilter = null;
	#distanceSort = null;
	#expandButton = null;

	constructor(){
		const _self = this;
		window.addEventListener("DOMContentLoaded", () => {
			this.#resultListArea = document.getElementById("result-list");
			this.#distanceFilter = document.getElementById("distance-filter-select");
			this.#distanceSort = document.getElementById("distance-sort-select");
			this.#expandButton = document.getElementById("expand-button");
			this.#distanceFilter.addEventListener("click", () => {
				this.filter();
			});
			this.#distanceSort.addEventListener("click", () => {
				this.sort();
			});
			this.#expandButton.addEventListener("click", () => {
				this.toggleExpandList();
			});
			this.forwardImg = "./img/forward.svg";
		});
	}

	drawDistanceList(matrixResponse, destinationsParam, startPositionValue, travelMode){

		// 移動手段用のボックスがなかったら作成する
		const travelModeUl = this.createBoxIfNotExists(travelMode);

		this.createCafeList(matrixResponse, destinationsParam, startPositionValue, travelModeUl);

		this.sort();

		this.resetFilter();
	}

	createBoxIfNotExists(travelMode){

		let travelModeUl = document.getElementsByClassName(`resultlist-${travelMode}`)[0];
		if(typeof(travelModeUl) === 'undefined' || travelModeUl === null || travelModeUl.length === 0){
			travelModeUl = document.createElement("ul");
			travelModeUl.classList.add("travelModeUl");
			travelModeUl.classList.add(`resultlist-${travelMode}`);
	
			const travelModeLabel = document.createElement("span");
			travelModeLabel.textContent = OPTIONS.TRAVEL_MODE.ja(travelMode);
			travelModeLabel.classList.add("travelMode");
			travelModeLabel.classList.add("label");
	
			travelModeUl.appendChild(travelModeLabel);
			this.#resultListArea.appendChild(travelModeUl);
		}
		return travelModeUl;
	}

	createCafeList(matrixResponse, destinationsParam, startPositionValue, listByTravelMode){

		let rows = "";
		let originAddress = "";
		let destinationAddresses = "";
		if(matrixResponse){
			rows = matrixResponse.rows[0];
			originAddress = matrixResponse.originAddresses[0];
			destinationAddresses = matrixResponse.destinationAddresses;
		}
		
		const destinations = destinationsParam || [];

		for(let i = 0, ilen = destinations.length; i < ilen; i++){
			const data = (rows.elements) ? rows.elements[i] : {status: ""};
			const destination = destinations[i] || {name: "不明"};
	
			const liElm = document.createElement("li");
			const positionSpan = document.createElement("span");
			const distanceSpan = document.createElement("span");
			const durationSpan = document.createElement("span");
			const forwardMap = document.createElement("img");
			const latHidden = document.createElement("input");
			latHidden.type = "hidden";
			const lngHidden = document.createElement("input");
			lngHidden.type = "hidden";
			const addressHidden = document.createElement("input");
			addressHidden.type = "hidden";
	
			// お店の名前
			positionSpan.textContent = `${destination.name}`;
			positionSpan.classList.add("positionName");
			// 緯度
			latHidden.value = destination.lat;
			// 経度
			lngHidden.value = destination.lng;
			// GoogleMapへの遷移用画像
			forwardMap.src = this.forwardImg;


			if(data.status == 'OK'){
				// 距離
				distanceSpan.textContent = `${(Math.round(data.distance.value/100)/10).toFixed(1)} km`;
				// 所要時間
				durationSpan.textContent = `${data.duration.text}`;
				// 住所
				addressHidden.value = destinationAddresses[i];
				forwardMap.addEventListener("click", () => {
					openDirectionMap(originAddress, destinationAddresses[i] + ` ${destination.name}`);
				});
			}else{
				distanceSpan.textContent = `距離不明`;
				durationSpan.textContent = ``;
				forwardMap.addEventListener("click", () => {
					// MatrixAPIが通っていれば住所まで引っ張れるが、それがなければ若干曖昧な検索になっちゃう
					openDirectionMap(`${startPositionValue.lat}, ${startPositionValue.lng}`, `喫茶店 ${destination.name}`);
				});
			}
			distanceSpan.classList.add("distance");
			durationSpan.classList.add("duration");
			forwardMap.classList.add("forwardMap");
			latHidden.classList.add("lat");
			lngHidden.classList.add("lng");
			addressHidden.classList.add("address");
			
			liElm.appendChild(forwardMap);
			liElm.appendChild(positionSpan);
			liElm.appendChild(distanceSpan);
			liElm.appendChild(durationSpan);
			liElm.appendChild(latHidden);
			liElm.appendChild(lngHidden);
			liElm.appendChild(addressHidden);
			this.addEventCafeListClicked(liElm);
			listByTravelMode.appendChild(liElm);
		}
	}
	addEventCafeListClicked(liElm){
		liElm.addEventListener("click", (e) => {
			const name = e.currentTarget.getElementsByClassName("positionName")[0];
			const lat = e.currentTarget.getElementsByClassName("lat")[0];
			const lng = e.currentTarget.getElementsByClassName("lng")[0];
			if(lat.value != "" && lng.value != ""){
				moveCenter({lat: lat.value, lng: lng.value});
			}
			const marker = findMarker(name);
			//うまくいかない。。。
			// marker.click();
		});
	}
	clearCafeList(){
		const list = this.#resultListArea.getElementsByClassName("travelModeUl");
		for(let listByTravelMode of list){
			this.#resultListArea.removeChild(listByTravelMode);
		}
	}
	sort(){
		const isAsc = this.#distanceSort.value == "asc";
		const list = this.#resultListArea.getElementsByClassName("travelModeUl");
		for(let listByTravelMode of list){

			const targetList = Array.prototype.slice.call(listByTravelMode.getElementsByTagName("li"));
			const sortedList = targetList.sort((a, b) => {
				const adis = parseFloat(a.getElementsByClassName("distance")[0].textContent.replace(" km", ""));
				const bdis = parseFloat(b.getElementsByClassName("distance")[0].textContent.replace(" km", ""));
				if(isAsc){
					return this.sortAsc(adis, bdis);
				}else{
					return this.sortDesc(adis, bdis);
				}
			});
			for(let elm of targetList){
				elm.remove();
			}
			for(let elm of sortedList){
				listByTravelMode.appendChild(elm);
			}
		}
	}
	sortAsc(a, b){
		if(a < b){
			return -1;
		}else if(a > b){
			return 1;
		}else{
			return 0;
		}
	}
	sortDesc(a, b){
		if(a < b){
			return 1;
		}else if(a > b){
			return -1;
		}else{
			return 0;
		}
	}
	filter(){
		const filterValue = this.#distanceFilter.value;
		const list = this.#resultListArea.getElementsByClassName("travelModeUl");
		for(let listByTravelMode of list){
			const targetList = listByTravelMode.getElementsByTagName("li");
			const kmFilterValue = parseFloat(filterValue/1000);

			for(let elm of targetList){
				const distanceElm = elm.getElementsByClassName("distance")[0];
				const distance = parseFloat(distanceElm.textContent.replace(" km", ""));
				if(distance < kmFilterValue || filterValue == -1){
					elm.classList.remove("hidden");
				}else{
					elm.classList.add("hidden");
				}
			}
		}
	}
	resetSort(){
		this.#distanceSort.value = asc;
		this.sort();
	}
	resetFilter(){
		this.#distanceFilter.value = -1;
		this.filter();
	}
	toggleExpandList(){
		const resultList = this.#resultListArea.getElementsByClassName("travelModeUl")[0];
		if( !resultList){
			return;
		}
		if(this.#expandButton.classList.contains("isExpand")){
			resultList.classList.remove("expand");
			this.#expandButton.classList.remove("isExpand");
		}else{
			resultList.classList.add("expand");
			this.#expandButton.classList.add("isExpand");
		}
	}
}


const cafeList = new CafeList();
fetchSpotData();

