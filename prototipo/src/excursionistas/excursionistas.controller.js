(function () {
'use strict';

angular.module('MapaExcursionistas')
.controller('ExcursionistasController', ExcursionistasController);

ExcursionistasController.$inject = ['MapaFactory'];
function ExcursionistasController(MapaFactory) {
  var ex = this;

  ex.excursionistas = [];
  for (var i = 0; i < names.length; i++) {
    ex.excursionistas.push({
      nombre: names[i],
      lat: lats[i],
      lon: longs[i],
      estado: "Mostrar",
    });
  }
  ex.excursionistasBase = ex.excursionistas;

  ex.busqueda = function () {
    ex.excursionistas = ex.excursionistasBase.filter(x => x.nombre.includes(ex.nombre));
  }

  ex.mostrarExcursionista = function(index) {
    if (ex.excursionistas[index].estado == "Ocultar") {
      ex.excursionistas[index].estado = "Mostrar"
    } else {
      ex.excursionistas[index].estado = "Ocultar"
    }
    MapaFactory.mostrarPersona(index, ex.excursionistas[index].lat, ex.excursionistas[index].lon);
  }
}


var names = [
  "Pedro",
  "Victor",
  "Laura",
  "Juan",
  "Jose",
  "Marta",
  "David",
];

var lats = [
  40.789563,
  40.789963,
  40.789863,
  40.789763,
  40.789993,
  40.789593,
  40.789873,
];

var longs = [
  -4.0048372,
  -4.0028372,
  -4.0038372,
  -4.0018372,
  -4.0035372,
  -4.0033372,
  -4.0031372,
];

})();
