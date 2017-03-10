var app = angular.module('jmpcApp', []);

app.controller('CardListController', function($scope, $http) {

	$http.get("cards").then(function(response) {
		$scope.cards = response.data.cards;
	});
	
});
