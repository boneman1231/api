var mainModule = angular.module("MainModule", ['ui.bootstrap']);

mainModule.controller("ApiController",  
  function($scope, $http, $filter) {
	$scope.paramModel = {};
		
    // call http service for class select content 
	var contentUrl = '/getClasses';
    $http.get(contentUrl).success(function(data) {	  
	  $scope.classModel=data;		
    });
	
	$scope.updateClass = function () {		
		// call http service for method select content 
		var contentUrl = '/getMethods?className='+$scope.classSelect.key;
	    $http.get(contentUrl).success(function(data) {	  
		  $scope.methodModel=data;		
	    });
	};	
	
	$scope.updateMethod = function () {		
		//$scope.resultArea = JSON.stringify($scope.methodSelect);
	};	
	
	$scope.submit = function () {		
		var contentUrl = '/invoke';
		
		//TODO empty parameter input => default value
		//TODO return exception message?
		var params = [];		
		angular.forEach($scope.paramModel, function(value, key) {
			params.push(value);
		});		
		var dataObj = { className: $scope.classSelect.key, 
			methodName: $scope.methodSelect.key, params:params };
			
		$http.post(contentUrl, dataObj).success(function(data) {	  		  
			$scope.resultArea = data.name;
		});		  
	};	
  }
);

