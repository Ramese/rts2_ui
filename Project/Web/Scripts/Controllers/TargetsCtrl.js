/*global angular, console*/

/*
README:

For dependencies declaration use file controllers.js, where is inicialized module name "controllers".

If you insert dependencies here, you override all modeles with name "controllers" (YOU DONT WANT!).
*/

(function () {
	'use strict';

	var app = angular.module('controllers');

	app.controller('TargetsCtrl', function ($scope, TelescopeService, $routeParams) {

        $scope.setAll = function () {
            $scope.search = false;
            $scope.targetsData = $scope.allTargetsData;
        };

        $scope.setSearch = function () {
            $scope.search = true;
            $scope.targetsData = $scope.searchTargetsData;
        };

        $scope.AutonomousTarget = function (targetId, value, index) {
            TelescopeService.setAutonomousTarget($routeParams.id, targetId, value)
                .success(function (data) {
                    console.log("suc",data);
                    if(data.Code == 200) {
                        if(!$scope.search)
                            $scope.targetsData.d[index][8] = value;

                        BootstrapDialog.show({
							title: 'Success',
							closable: false,
							type: BootstrapDialog.TYPE_SUCCESS,
							message: "Change saved.",
							buttons: [{
								label: 'Close',
								cssClass: 'btn-success',
								action: function (dialogRef) {
									dialogRef.close();
								}
							}]
						});
                    } else {
                        BootstrapDialog.show({
							title: 'Error',
							closable: false,
							type: BootstrapDialog.TYPE_DANGER,
							message: data.Body,
							buttons: [{
								label: 'Close',
								cssClass: 'btn-danger',
								action: function (dialogRef) {
									dialogRef.close();
								}
							}]
						});
                    }
                    
                })
                .error(function (data) {
                    console.log("err",data);
                });
        };

        $scope.searchTargetByName = function (searchText) {
            TelescopeService.searchTargets($routeParams.id, searchText)
                .success(function (data) {
                    $scope.searchTargetsData = JSON.parse(data.Body);
                    $scope.targetsData = $scope.searchTargetsData;
                    console.log($scope.targetsData);
                })
                .error(function (data) {
                
                });
        };
        
        $scope.refreshAllTargets = function () {
            TelescopeService.getTargets($routeParams.id)
                .success(function (data) {
                    $scope.allTargetsData = JSON.parse(data.Body);
                    $scope.targetsData = $scope.allTargetsData;
                    console.log($scope.targetsData);
                })
                .error(function (data) {
                
                });
        };

        function init() {
            $scope.telescope = {};
            $scope.telescope.Id = $routeParams.id;
            $scope.search = true;
        }
        
        init();
	});

}());