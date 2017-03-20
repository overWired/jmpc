var app = angular.module('jmpcApp', []);

app.controller('CardListController', function($scope, $http) {
	$http.get("cards").then(function(response) {
		$scope.cards = response.data;
	});
});

app.controller('MusicPlayerController', function($scope, $http) {
	$http.get("musicPlayer").then(function(response) {
		$scope.player = response.data;
	});
});

app.controller("PlaylistController", function($scope, $http) {
	$http.get("playlist.json").then(function(response) {
		$scope.playlist = response.data;
	});
});
