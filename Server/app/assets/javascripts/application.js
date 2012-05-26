// This is a manifest file that'll be compiled into application.js, which will include all the files
// listed below.
//
// Any JavaScript/Coffee file within this directory, lib/assets/javascripts, vendor/assets/javascripts,
// or vendor/assets/javascripts of plugins, if any, can be referenced here using a relative path.
//
// It's not advisable to add code directly here, but if you do, it'll appear at the bottom of the
// the compiled file.
//
// WARNING: THE FIRST BLANK LINE MARKS THE END OF WHAT'S TO BE PROCESSED, ANY BLANK LINE SHOULD
// GO AFTER THE REQUIRES BELOW.
//
//= require jquery
//= require jquery_ujs
//= require foundation
//= require_tree .

$(document).ready(function() {
	var map;
	function initialize() {
		var myOptions = {
		  zoom: 3,
		  center: new google.maps.LatLng(41.147476, -8.609505),
		  mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		map = new google.maps.Map(document.getElementById('map_canvas'),
		    myOptions);
		$.get(
		    "/draws.json",
		    {},
		    function(data) {
		      for (var index in data)
		      {
			        var image = '/marker.png';

			        var myLatlng = new google.maps.LatLng(data[index].latitude,data[index].longitude);
			        var marker = new google.maps.Marker({
			            position: myLatlng,
			            animation: google.maps.Animation.DROP,
			            icon: image,
			            title: data[index].id_creator
			        });
			        
					google.maps.event.addListener(marker, 'click', (function(index){ 
						return function(){
							var url = '/draws/'+data[index].id;
					   		window.location = url ;}; 
						})(index));
			        marker.setMap(map);
		      }
		    }
		);
	}
	google.maps.event.addDomListener(window, 'load', initialize);
});