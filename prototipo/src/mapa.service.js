/**
  Servicio para controlar el mapa, marcadores, track de la ruta y excursionistas
*/
(function () {
"use strict";

var markers = []; // Excursionistas sobre el mapa
var map; // Google maps
var track; // Track de la ruta (polyline)

angular.module('MapaExcursionistas')
.factory('MapaFactory', MapaFactory);

function MapaFactory() {

  cargarMapa();
  cargarRutaGPX();
  cargarOpcionesMapa();

  var Actions = {
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

  return Actions;
}

function cargarMapa() {
  var mapOptions = {
    zoom: 17,
    mapTypeId: google.maps.MapTypeId.ROADMAP,
    streetViewControl: false,
    mapTypeControlOptions: {
      position: google.maps.ControlPosition.TOP_LEFT,
      style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR
    }
  };

  map = new google.maps.Map(document.getElementById("map"), mapOptions);
}


function cargarRutaGPX() {
  //Carga el gpx descargando del XML los datos
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
      track = new google.maps.Polyline({
        path: points,
        strokeColor: "#FF00AA",
        strokeOpacity: .7,
        strokeWeight: 4
      });
      track.setMap(map);
      map.fitBounds(bounds);
    }
  });
}

function cargarOpcionesMapa() {
  var buttonOptions = {
    gmap: map,
    name: 'Mostrar/Ocultar track',
    position: google.maps.ControlPosition.TOP_LEFT,
    action: function(){
      track.map ? track.setMap(null) :
                  track.setMap(map);
    }
  }
  var botonTrack = new buttonControl(buttonOptions);
}

})();
