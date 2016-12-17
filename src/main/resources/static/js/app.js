var app = angular.module('myApp',[])
.controller('TrcToSql', ['$scope','$http', function($scope, $http){
	//$scope.databasesAvailable = ["a", "b"];
	$scope.names = ["Emil", "Tobias", "Linus"];
	
	$scope.init = function() {
		var httpResponse = $http.post('db/availables', {});
		
		httpResponse.success(function(data, status, headers, config) {
			$scope.databasesAvailable = data.responseBody;
		});
		
		httpResponse.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
			$scope.databasesAvailable = null;
		});
				
	}
	
	$scope.loadDbSchema = function(){
		var httpResponse = $http.post('db/listTables/'+ $scope.dbAvalilable, {});
		
		httpResponse.success(function(data, status, headers, config) {
			console.log(data);
			$scope.dbSchema = data.responseBody;
		});
		
		httpResponse.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
			$scope.dbSchema = null;
		});
	}
	
		
	$scope.testDatabaseConnection = function(){
		var httpResponse = $http.post('db/'+"teste", {});
		httpResponse.success(function(data, status, headers, config) {
			console.log(data);
		});
		
		httpResponse.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
		});
	}
	
	
	$scope.submitFormula = function(){
		var dataObj = {
				requestBody : $scope.trcformula
		}
		
		var httpResponse = $http.post('trc/converttosqlnf', dataObj);
		httpResponse.success(function(data, status, headers, config) {
			console.log(data);
		});
		
		httpResponse.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
		});
	}
	
	
	$scope.init();
	
}]);