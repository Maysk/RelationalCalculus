var app = angular.module('myApp',[])
.controller('TrcToSql', ['$scope','$http', function($scope, $http){
	
	$scope.submitFormula = function(){
		var dataObj = {
				value : $scope.trcformula
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