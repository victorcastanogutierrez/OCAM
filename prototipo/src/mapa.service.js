(function () {
"use strict";

var markers = [];

angular.module('MapaExcursionistas')
.factory('MapaFactory', MapaFactory);

function MapaFactory() {
  var route1Latlng = new google.maps.LatLng(43.362006,-5.8684887);
  var mapOptions = {
    center: route1Latlng,
    zoom: 17,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  var map = new google.maps.Map(document.getElementById("map"), mapOptions);

  $.ajax({
    type: "GET",
    url: "tracks/cuerdalarga.gpx",
    dataType: "xml",
    success: function (xml) {
      var points = [];
      var bounds = new google.maps.LatLngBounds();
      $(xml).find("trkpt").each(function () {
        var lat = $(this).attr("lat");
        var lon = $(this).attr("lon");
        var p = new google.maps.LatLng(lat, lon);
        points.push(p);
        bounds.extend(p);
      });
      var poly = new google.maps.Polyline({
        path: points,
        strokeColor: "#FF00AA",
        strokeOpacity: .7,
        strokeWeight: 4
      });
      poly.setMap(map);
      // fit bounds to track
      map.fitBounds(bounds);
    }
  });

  var Auth = {
    mostrarPersona: function (index, latitud, longitud) {
      if (markers[index]) {
        markers[index].setMap(null);
        markers[index] = null;
      } else {
        var posicion = {lat: latitud, lng: longitud};
        var marker = new google.maps.Marker({
          position: posicion,
          map: map
        });
        marker.setMap(map);
        markers[index] = marker;
      }
    },
  }

  return Auth;
}

})();
