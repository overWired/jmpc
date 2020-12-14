var app = angular.module('jmpcApp', []);

app.controller('JukeboxController', function($scope, $http) {
    $scope.player = {};

	$http.get("/music/cards").then(function(response) {
		$scope.cards = response.data;
	});

    $scope.play = function(trackId) {
        $http.get("/player/play?trackId=" + trackId);
    };

	$scope.status = function() {
	    $http.get("player/status").then(function(response) {
            $scope.player = response.data;
      	});
	};

	$scope.status();

    // function to handle a server-sent event
    var handleCallback = function(event) {
        var message = event.data;
        console.log("received message: " + message);
        $scope.$apply(function() {
            $scope.player = JSON.parse(message);
        });
    };

    // register to receive the server-sent event
    var source = new EventSource('/sse/status');
    source.addEventListener('message', handleCallback, false);

});
