function getLocalURL() {
    var http = location.protocol;
    var slashes = http.concat("//");
    var host = slashes.concat(window.location.hostname);
    host = host + ':' + window.location.port + '/rest/';
    return host;
}

function getInstanceID() {
    return 'test';
}


function doGet(url, doReturn) {
    url = getLocalURL() + url;
    if(typeof(doReturn)==='undefined') doReturn = true;
    var client = new XMLHttpRequest();
    client.open("GET", url, false);
    client.send();
    if (client.status == 200) {
        if (doReturn == false)
            return true;
        else
            return client.responseText;
    }
    else {
        alert("The request did not succeed.");
        return false;
    }
}

function getNetworkElements() {
    var url = getInstanceID() + "/nodes";
    var out = doGet(url);
    if (out!=false) {
        var nodes = JSON.parse(out);
        var graphnodes = [];
        for (i=0; i < nodes.length; i++) {
          var data = {};
          data.id = nodes[i].id.toString();
          data.label = nodes[i].label;
          graphnodes[i] = {};
          graphnodes[i].data = data;
        }
        return graphnodes;
    }
    return null;
}

