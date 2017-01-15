var app = angular.module('jmpcApp', []);

app.controller('CardListController', function($scope, $http) {

	$http.get("data.json").then(function(response) {
		$scope.cards = response.data.cards;
	});
	
});
