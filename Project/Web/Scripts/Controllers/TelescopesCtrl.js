/*global angular, console*/

/*
README:

For dependencies declaration use file controllers.js, where is inicialized module name "controllers".

If you insert dependencies here, you override all modeles with name "controllers" (YOU DONT WANT!).
*/

(function () {
	'use strict';

	var app = angular.module('controllers');

	app.controller('TelescopesCtrl', function ($scope, TelescopeService) {
        
        $scope.setDefaultPagination = function () {
            var param = {};
            
			$scope.pagingInfo = {
				Page: 1,
				ItemsPerPage: 10,
				SortBy: 'CreateDate',
				Reverse: true,
				TotalItems: 0,
				IsAnd: true,
				ParamItemsList: []
			};
		};
        
        function loadData() {
            TelescopeService.getTelescopes($scope.pagingInfo)
                .success(function (data) {
                    console.log("tels ctrl: ", data);
                    $scope.telescopes = data;
                })
                .error(function (data) {
                
                });
        }
        
        function init() {
            $scope.setDefaultPagination();
            
            loadData();
        }
        
        init();
	});

}());