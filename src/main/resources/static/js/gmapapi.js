// Initialize and add the map
let map;
let geocoder;
let markers = [];
let markerIndex = {};
let infoWindow;
async function initMap() {

	geocoder = new google.maps.Geocoder();
	const currentPosition = await getCurrentLatLng("35.6809591", "139.7673068");
	map = new google.maps.Map(document.getElementById('map'), {
		zoom: 15,
		center: currentPosition
	});
	marking({
        map: map, 
        position: currentPosition
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
    markers.push(marker);
    
    if(markerOptions.infoWindow){
        marker.addListener("click", () => {
            markerOptions.infoWindow.open({
                anchor: marker,
                map,
                shouldFocus: false,
            });
        });
    }
    
    return marker;
}
function addMarkerIndex(marker, key){
    markerIndex[key] = marker;
}
function findMarker(key){
    return markerIndex[key];
}

// TODO: 店名をいれると地図上にマーカーが出て、
// それを選択してお気に入りのカフェとして登録できるようにする
function collectLatLng(address){
	
	geocoder.geocode( { 'address': address}, function(results, status) {
	if (status == 'OK') {
		map.setCenter(results[0].geometry.location);
		var marker = new google.maps.Marker({
			map: map,
			position: results[0].geometry.location
		});
		console.log(`name=${address}, lat=${results[0].geometry.location.lat()}, lng=${results[0].geometry.location.lng()}`);
	} else {
		console.log('Geocode was not successful for the following reason: ' + status);
	}
	});
}

function moveCenter(latlngMap){
    map.setCenter(new google.maps.LatLng(latlngMap.lat, latlngMap.lng));
}

function fitZoom(){
	// 範囲内に収める
	var minX = marker[0].getPosition().lng();
	var minY = marker[0].getPosition().lat();
	var maxX = marker[0].getPosition().lng();;
	var maxY = marker[0].getPosition().lat();;
	for(var i=0; i<10; i++){
	var lt = marker[i].getPosition().lat();
	var lg = marker[i].getPosition().lng();
	if (lg <= minX){ minX = lg; }
	if (lg > maxX){ maxX = lg; }
	if (lt <= minY){ minY = lt; }
	if (lt > maxY){ maxY = lt; }
	}
	var sw = new google.maps.LatLng(maxY, minX);
	var ne = new google.maps.LatLng(minY, maxX);
	var bounds = new google.maps.LatLngBounds(sw, ne);
	map.fitBounds(bounds);
}


// 現在位置の取得
isGettingCurrentLatLng = false;
function getCurrentLatLng(deflat, deflng){
    isGettingCurrentLatLng = true;
    return new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                console.log(position);
                isGettingCurrentLatLng = false;
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
                isGettingCurrentLatLng = false;
                reject({error:error.code, lat: deflat, lng: deflng});
            });
    });
}
function clearAllMarker(){
    for(let i = 0; i < markers.length; i++){
        clearMarker(i);
    }
}
function clearMarker(key){
    markers[key].setMap(null);
}

class InfoWindowCreator{

    constructor(title, htmlContent){
        this.title = title;
        this.content = htmlContent;
    }

    create(){
        const html = `<h3 class="infoWindow ">${this.title}</h3><div class="infoWindow content">${this.content}</div>`;
        return new google.maps.InfoWindow({
            content: html,
        });
    }
}

function openDirectionMap(origin, destination, travelmode){
    window.open(`https://www.google.com/maps/dir/?api=1&origin=${origin}&destination=${destination}&travelmode=transit`);
}