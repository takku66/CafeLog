// Initialize and add the map
const GMAP = {
    map: null,
    geocoder: null,
    markers: [],
    markerIndex: {},
    infoWindow: null,
    isGettingCurrentLatLng: false,
    currentPosition: async function(){
        const latlng = await getCurrentLatLng(35.6809591, 139.7673068);
        return latlng;
    }
}

async function initMap() {

	GMAP.geocoder = new google.maps.Geocoder();
	GMAP.map = new google.maps.Map(document.getElementById('map'), {
		zoom: 15,
		center: GMAP.currentPosition
	});
	marking({
        map: GMAP.map, 
        position: GMAP.currentPosition
    });
    
}
function toMatrixOptions(objectArray){
	console.log(objectArray.map(obj => `${obj.lat}, ${obj.lng}`));
	return objectArray.map(obj => `${obj.lat}, ${obj.lng}`);
}

/**
 * DistanceMatrixAPIを呼び出す
 */
function callDistanceMatrix(paramObject){
	// Add Distance Matrix here
	const service = new google.maps.DistanceMatrixService();

	// instantiate Distance Matrix service
	const matrixOptions = {
		// origins: [`"${StartPositions.lat}, ${StartPositions.lng}"`], // technician locations
		origins: paramObject.origins,
		// destinations: [`"${DestinationPositions.lat}, ${DestinationPositions.lng}"`], // customer address
		destinations: paramObject.destinations,
		travelMode: paramObject.travelMode,
		unitSystem: google.maps.UnitSystem.IMPERIAL
	};
	// Call Distance Matrix service
	service.getDistanceMatrix(matrixOptions, paramObject.callback);
}



/**
 * マーカーをつける
 * @param {*} map GoogleMapAPIのmapインスタンス
 * @param {*} latlngMap lat,lngそれぞれをキーにもつオブジェクトの配列  
 * e.g.[{lat: 1.1111, lng: 2.2222222}, {lat: 3.33333, lng: 4.444444}]
 */
function marking(markerOptions){
	// Loop through startPositions, adding markers
    // create marker for a city
    const options = {};
    options.map = markerOptions.map;
    options.position = markerOptions.position;
    if(markerOptions.title){
        options.title = markerOptions.title;
    }
    if(markerOptions.icon){
        options.icon = {};
        options.icon.url = markerOptions.icon.url;
        options.icon.scaledSize = markerOptions.icon.scaledSize;
    }

    const marker = new google.maps.Marker(options);
    GMAP.markers.push(marker);
    
    if(markerOptions.infoWindow){
        marker.addListener("click", () => {
            markerOptions.infoWindow.open({
                anchor: marker,
                map: GMAP.map,
                shouldFocus: false,
            })
        });

    }
    
    return marker;
}
function addMarkerIndex(marker, key){
    GMAP.markerIndex[key] = marker;
}
function findMarker(key){
    return GMAP.markerIndex[key];
}

// TODO: 店名をいれると地図上にマーカーが出て、
// それを選択してお気に入りのカフェとして登録できるようにする
function collectLatLng(address){
	
	GMAP.geocoder.geocode( { 'address': address}, function(results, status) {
	if (status == 'OK') {
		// GMAP.map.setCenter(results[0].geometry.location);
		// let marker = new google.maps.Marker({
		// 	map: GMAP.map,
		// 	position: results[0].geometry.location
		// });
		console.log(`name=${address}, status=${status}, lat=${results[0].geometry.location.lat()}, lng=${results[0].geometry.location.lng()}`);
	} else {
		console.log(`name=${address}, status=${status}`);
	}
	});
}

function moveCenter(latlngMap){
    GMAP.map.setCenter(new google.maps.LatLng(latlngMap.lat, latlngMap.lng));
}

function fitZoom(){
	// 範囲内に収める
	const minX = GMAP.marker[0].getPosition().lng();
	const minY = GMAP.marker[0].getPosition().lat();
	const maxX = GMAP.marker[0].getPosition().lng();;
	const maxY = GMAP.marker[0].getPosition().lat();;
	for(let i=0; i<10; i++){
        let lt = GMAP.marker[i].getPosition().lat();
        let lg = GMAP.marker[i].getPosition().lng();
        if (lg <= minX){ minX = lg; }
        if (lg > maxX){ maxX = lg; }
        if (lt <= minY){ minY = lt; }
        if (lt > maxY){ maxY = lt; }
	}
	const sw = new google.maps.LatLng(maxY, minX);
	const ne = new google.maps.LatLng(minY, maxX);
	const bounds = new google.maps.LatLngBounds(sw, ne);
	GMAP.map.fitBounds(bounds);
}



function getCurrentLatLng(deflat, deflng){
    GMAP.isGettingCurrentLatLng = true;
    return new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                console.log(position);
                GMAP.isGettingCurrentLatLng = false;
                resolve({lat: position.coords.latitude, lng: position.coords.longitude});
            },
            (error) => {
                switch (error.code) {
                    case 1: //PERMISSION_DENIED
                        alert("位置情報の利用が許可されていません");
                        break;
                    case 2: //POSITION_UNAVAILABLE
                        alert("現在位置が取得できませんでした");
                        break;
                    case 3: //TIMEOUT
                        alert("タイムアウトになりました");
                        break;
                    default:
                        alert("想定外のエラー(エラーコード:" + error.code + ")");
                        break;
                }
                GMAP.isGettingCurrentLatLng = false;
                // reject({error:error.code, lat: deflat, lng: deflng});
                resolve({lat: deflat, lng: deflng});
            });
    });
}
function clearAllMarker(){
    for(let i = 0; i < GMAP.markers.length; i++){
        clearMarker(i);
    }
}
function clearMarker(key){
    GMAP.markers[key].setMap(null);
}

class InfoWindowCreator{

    constructor(title, htmlContent, googleMapParams){
        this.title = title;
        this.content = htmlContent;
        this.startPosition = googleMapParams.startPosition;
        this.category = googleMapParams.category;
        this.lat = googleMapParams.lat;
        this.lng = googleMapParams.lng;
        this.travelMode = googleMapParams.travelMode;
        
    }

    create(){
        const url = linkUrlDirectionMap(`${this.startPosition.lat}, ${this.startPosition.lng}`, `${this.category} ${this.title}`);
        const html = `
            <h3 class="infoWindow title _forwardmap ${this.lat} ${this.lng}">${this.title}</h3>
            <div class="infoWindow content">${this.content}</div>
            <a href="${url}" target="_blank">経路を開く</a>
        `;
        return new google.maps.InfoWindow({
            content: html,
        });
    }
}

function openDirectionMap(origin, destination){
    window.open(`https://www.google.com/maps/dir/?api=1&origin=${origin}&destination=${destination}&travelmode=transit`);
}
function linkUrlDirectionMap(origin, destination){
    return `https://www.google.com/maps/dir/?api=1&origin=${origin}&destination=${destination}&travelmode=transit`;
}