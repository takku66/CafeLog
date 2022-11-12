let spotdata;

function fetchSpotData(){
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || "";
    const token = document.querySelector('meta[name="_csrf"]')?.content || "";
    const url = (CONTEXT_ROOT == "/" ? "" : CONTEXT_ROOT) + "/allcafe";
    fetch(url, {
        method: "GET",
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
        spotdata = JSON.parse(data);
        search();
    }).catch((reason) => {
        console.log(reason);
    });
}


