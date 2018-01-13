var app = angular.module('jmpcApp', []);

app.controller('CardListController', function($scope, $http) {
	$http.get("cards").then(function(response) {
		$scope.cards = response.data;
	});
});

app.controller('MusicPlayerController', function($scope, $http) {
	$http.get("player/status").then(function(response) {
		$scope.player = response.data;
	});
});
