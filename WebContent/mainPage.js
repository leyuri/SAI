L.mapbox.accessToken = 'pk.eyJ1Ijoic3VhbWoiLCJhIjoiY2ptc203cWl0MGM1dDNwbnVwemdjZTFnbyJ9.v4QFpeaIi88xAl73QkEBUg';
var map = L.mapbox.map('map', 'mapbox.dark')
.setView([37.5624615, 126.98696440000003], 10);


var friendsIdArr = [];
var stationsNameArr = [];
$(document).ready(function() {
    var Obj = new Object();
	
	$("#middlePoint").click(function(){
		alert("미들 포인트 실행");
		var data = JSON.stringify(Obj);
		
		$.ajax({
			url: "./SearchMiddlePoint.do",
			type: "post",
			data: {
				"data" : data 
			},
			datatype: "json"	
		});

	})
	
	
	
   $.ajax({
      url : "./LoadFriendsToMap.do",
      type : "post",
      dataType : "json",
      success: function(data){
//처음 입력
         var memberId = [];
         var memberName = [];
         
         memberId.push(data[0].memberId);
         memberName.push(data[0].memberName);
         
         var properties = new Object();
         properties['station'] = data[0].memberStation;
         properties['memberId'] = memberId;
         properties['memberName'] = memberName;
         properties['marker-color'] = "#548cba";
         properties['marker-size'] = "large";
         properties['marker-symbol'] = "ferry";

         var xy = new Array();
         xy.push(data[0].wgsX);
         xy.push(data[0].wgsY);

         var geometry = new Object();
         geometry['type'] = "Point";
         geometry['coordinates'] = xy;

         var features = new Array();

         var features_child = new Object();
         features_child['type'] = "Feature";
         features_child['properties'] = properties;
         features_child['geometry'] = geometry;

         features.push(features_child);
         //alert(JSON.stringify(features));
         
         function plus(features, station , id ,name){
            for (feature in features){// 전체객체에서 하나씩 가져옴  features=배열
               alert("feature : " + feature + " // " + features[feature].properties.station);
               if(features[feature].properties.station == station){
                  features[feature].properties.memberId.push(id);
                  features[feature].properties.memberName.push(name);
               }
            };
            return false;
         };
         
//두번쨰
         for(var i = 1; i < Object.keys(data).length; i++){
            if(plus(features, data[i].memberStation , data[i].memberId ,data[i].memberName) == false){
               var memberId = [];
               var memberName = [];
               
               memberId.push(data[i].memberId);
               memberName.push(data[i].memberName);
               
               var properties = new Object();
               properties['station'] = data[i].memberStation;
               properties['memberId'] = memberId;
               properties['memberName'] = memberName;
               properties['marker-color'] = "#548cba";
               properties['marker-size'] = "large";
               properties['marker-symbol'] = "ferry";

               var xy = new Array();
               xy.push(data[i].wgsX);
               xy.push(data[i].wgsY);

               var geometry = new Object();
               geometry['type'] = "Point";
               geometry['coordinates'] = xy;

               var features_child = new Object();
               features_child['type'] = "Feature";
               features_child['properties'] = properties;
               features_child['geometry'] = geometry;

               features.push(features_child);
            }
         }
         
         var geojson = new Object();
         geojson['type'] = 'FeatureCollection';
         geojson['features'] = features;
         
         alert(JSON.stringify(geojson));

 
         var featureLayer = L.mapbox.featureLayer()
         .setGeoJSON(geojson)
         .addTo(map);


         var friendsIdArr = [];
         var stationsNameArr = [];


         $(document).ready(function() {
              alert("welcome");
              
              $('#test').on('click', function() {
                 alert("test click");
                 console.log("friendsIdArr : " + friendsIdArr[0]);
                 console.log("friendsIdArr : " + friendsIdArr[1]);
                 console.log("friendsIdArr : " + friendsIdArr[2]);
                 
                 console.log("stationsNameArr : " + stationsNameArr[0]);
                 console.log("stationsNameArr : " + stationsNameArr[1]);
                 console.log("stationsNameArr : " + stationsNameArr[2]);
              });

              
           });

         var friendsStr = "";
         var stationStr = "";
         
         var sArr = new Array();
         
         featureLayer.eachLayer(function(layer) {
        	 var fObj = new Object();
        	 
            alert("layer.feature.properties.station : " + layer.feature.properties.station);

            var memberArr = [layer.feature.properties.station];
            var content = "<h1>" + layer.feature.properties.station +"</h1>";
            for(i in layer.feature.properties.memberId){
              memberArr.push(layer.feature.properties.memberId[i]);   
              console.log("memberArr : " + memberArr[i]);
              content +='<input type="checkbox" name="'+layer.feature.properties.station+
              '" id="'+layer.feature.properties.memberId[i]+
              '" value="'+layer.feature.properties.memberId[i]+
              '"><label for="'+ layer.feature.properties.memberId[i]+
              '">'+ layer.feature.properties.memberName[i]+'</label><br>';
           }
           content += '<button id=" '+layer.feature.properties.station+'"  class="'+layer.feature.properties.station+'" > '+layer.feature.properties.station+'</button>';
           
           $('#map').on('click', '.'+layer.feature.properties.station, function() {
              alert("btn click \n" + $(this).text());
              $("#map input[name="+layer.feature.properties.station+"]:checked").each(function() {
                 friendsIdArr.push($(this).val());
                 stationsNameArr.push( layer.feature.properties.station );
                 
                 friendsStr += '<input type="button" value="' + $(this).val() + '"></input>';
                 stationStr += '<input type="button" value="' + layer.feature.properties.station + '"></input>';
                 
                 $("#friends").html(friendsStr);
                 $("#station").html(stationStr);
                 
                 alert("타입을 확인해봅시당~ : " + typeof(layer.feature.geometry.coordinates[0]));
                 fObj['stationName'] = layer.feature.properties.station;
                 fObj['mapX'] = layer.feature.geometry.coordinates[0];
                 fObj['mapY'] = layer.feature.geometry.coordinates[1];
                 
                 sArr.push(fObj);
                 alert("sArr : " + JSON.stringify(sArr));
                 Obj['stationList'] = sArr;
                 alert("Obj : " + JSON.stringify(Obj));

              });
              
           });
           
           	//$("#hid").attr('value', Obj)
           
            layer.bindPopup(content);

         });
  
      }
   });
});