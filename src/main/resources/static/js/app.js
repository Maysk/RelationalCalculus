var app = angular.module('myApp',[])
.controller('TrcToSql', ['$scope','$http', function($scope, $http){
	
	$scope.testDatabaseConnection = function(){
		var res = $http.post('db/'+"teste", {});
		console.log("Clicou");
		res.success(function(data, status, headers, config) {
			console.log(data);		
		});
		
		res.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
		});	
	}
	
	$scope.submitFormula = function(){
		var dataObj = {
				requestBody : $scope.trcformula
		}
		console.log("aqui")
		console.log(dataObj)
		
		var res = $http.post('trc/converttosqlnf', dataObj);
		res.success(function(data, status, headers, config) {
			console.log(data);
					
		});
		
		res.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
		});	
		
	}
	

	
}]);