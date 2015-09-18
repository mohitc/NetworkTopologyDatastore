var scaleFactor = 30;

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
  if (typeof(doReturn) === 'undefined') doReturn = true;
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

function getTopologyElement(id) {
  var url = getInstanceID() + "/" + id;
  var out = doGet(url);
  if (out != false) {
    return JSON.parse(out);
  }
  return null;
}

function getNetworkElements() {
  var url = getInstanceID() + "/nodes";
  var out = doGet(url);
  if (out != false) {
    return JSON.parse(out);
  }
  return null;
}


function getGraphNodesFromCP(parentID, cp) {
  //add ne as a parent node
  var graphNodes = [];
  var data = {};
  data.id = cp.id.toString();
  data.label = cp.label;
  data.parent = parentID.toString();
  graphNodes[0] = {};
  graphNodes[0].data = data;
  if (cp.properties!=null) {
    if ((cp.properties.XCOORD !=null) && (cp.properties.YCOORD!=null)) {
      var position = {};
      position.x = cp.properties.XCOORD * scaleFactor;
      position.y = cp.properties.YCOORD * scaleFactor;
      graphNodes[0].position = position;
    }
  }
  //Check for children
  if (cp.containedCPs != null) {
    for (var i = 0; i < cp.containedCPs.length; i++) {
      var newCP = getTopologyElement(cp.containedCPs[i]);
      if (newCP != null) {
        graphNodes = graphNodes.concat(getGraphNodesFromCP(cp.id, newCP));
      }
    }
  }
  return graphNodes;
}

function getGraphNodesFromNE(ne) {
  var graphNodes = [];
  //add ne as a parent node
  var data = {};
  data.id = ne.id.toString();
  data.label = ne.label;
  graphNodes[0] = {};
  graphNodes[0].data = data;
  if (ne.properties!=null) {
    if ((ne.properties.XCOORD !=null) && (ne.properties.YCOORD!=null)) {
      var position = {};
      position.x = ne.properties.XCOORD * scaleFactor;
      position.y = ne.properties.YCOORD * scaleFactor;
      graphNodes[0].position = position;
    }
  }
  //populate connection points
  if (ne.connectionPoints != null) {
    for (var i = 0; i < ne.connectionPoints.length; i++) {
      var cp = getTopologyElement(ne.connectionPoints[i]);
      if (cp != null) {
        graphNodes = graphNodes.concat(getGraphNodesFromCP(ne.id, cp));
      }
    }
  }
  return graphNodes;
}

function getGraphNodes() {
  var nodes = getNetworkElements();
  var graphNodes = [];
  if (nodes != null) {
    for (var i = 0; i < nodes.length; i++) {
      graphNodes = graphNodes.concat(getGraphNodesFromNE(nodes[i]));
    }
  }
  return graphNodes;
}


function getConnections() {
  var url = getInstanceID() + "/connections";
  var out = doGet(url);
  if (out != false) {
    return JSON.parse(out);
  }
  return null;
}

function getGraphConnectionFromConn(connection) {
  var data = {};
  data.id = connection.id.toString();
  data.source = connection.aEnd;
  data.target = connection.zEnd;
  var out = {};
  out.data = data;
  return out;
}


function getGraphConnections() {
  var connections = getConnections();
  var graphConnections = [];
  if (connections!=null) {
    for (var i=0;i<connections.length; i++) {
      graphConnections[i] = getGraphConnectionFromConn(connections[i]);
    }
  }
  return graphConnections;
}

function getCsStyle(instanceID) {
  var url = getInstanceID() + "/graph/cs/style";
  var out = doGet(url);
  if (out != false) {
    return JSON.parse(out);
  }
  return null;
}