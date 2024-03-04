var scaleFactor = 30;

function getLocalURL() {
  var http = location.protocol;
  var slashes = http.concat("//");
  var host = slashes.concat(window.location.hostname);
  host = host + ':' + window.location.port + '/topology/';
  return host;
}

function getInstanceID() {
  return 'test';
}


function doGet(url, returnTo) {
  url = getLocalURL() + url;
  var client = new XMLHttpRequest();
  client.open("GET", url, true);
  client.responseType = 'json';
  client.onreadystatechange=function() {
    if (client.readyState==4) {
      if ((client.status>=200) && (client.status <300)) {
        //assume that input is JSON

        returnTo(client.response);
      } else {
        alert("The request did not succeed.");
      }
    }
  }
  client.send();
}


function getCsStyleUrl() {
  return getInstanceID() + "/graph/cs/style";
}

function getCsGraphUrl() {
  return getInstanceID() + "/graph/cs";
}


function setCsStyle(cyStyle) {
  cy.style().fromJson(JSON.stringify(cyStyle)).update();
}

function setCsElements(cyElement) {
  cy.add(cyElement);

var options = {
  name: 'cose',

  // Called on `layoutready`
  ready               : function() {},

  // Called on `layoutstop`
  stop                : function() {},

  // Whether to animate while running the layout
  animate             : true,

  // Number of iterations between consecutive screen positions update (0 -> only updated on the end)
  refresh             : 4,

  // Whether to fit the network view after when done
  fit                 : true,

  // Padding on fit
  padding             : 30,

  // Constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
  boundingBox         : undefined,

  // Whether to randomize node positions on the beginning
  randomize           : true,

  // Whether to use the JS console to print debug messages
  debug               : false,

  // Node repulsion (non overlapping) multiplier
  nodeRepulsion       : 400000,

  // Node repulsion (overlapping) multiplier
  nodeOverlap         : 10,

  // Ideal edge (non nested) length
  idealEdgeLength     : 10,

  // Divisor to compute edge forces
  edgeElasticity      : 100,

  // Nesting factor (multiplier) to compute ideal edge length for nested edges
  nestingFactor       : 5,

  // Gravity force (constant)
  gravity             : 250,

  // Maximum number of iterations to perform
  numIter             : 100,

  // Initial temperature (maximum node displacement)
  initialTemp         : 200,

  // Cooling factor (how the temperature is reduced between consecutive iterations
  coolingFactor       : 0.95,

  // Lower temperature threshold (below this point the layout will end)
  minTemp             : 1.0
};

  var layout = cy.layout(options);
  //layout.run();
}