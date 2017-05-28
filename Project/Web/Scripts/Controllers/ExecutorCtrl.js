/*global angular,console, $*/

/*
README:

For dependencies declaration use file controllers.js, where is inicialized module name "controllers".

If you insert dependencies here, you override all modeles with name "controllers" (YOU DONT WANT!).
*/
/*jslint eqeq: true*/
/*jslint bitwise: true*/
(function () {
	'use strict';

	var app = angular.module('controllers');

	app.controller('ExecutorCtrl', function ($scope, $routeParams, TelescopeService) {

        $scope.getTargetTooltip = function(target) {
            if(!target)
                return undefined;

            var tooltip = "RA: ";
            tooltip += Math.round(target[2]);
            tooltip += " DEC: ";
            tooltip += Math.round(target[3]);

            tooltip = "ID: " + target[0] + " " + tooltip;

            return tooltip;
        };

        $scope.targets = [];

        function getTargetInfo(idTarget) {
            if(!$scope.targets[idTarget])
                TelescopeService.getTargetById($scope.telescope.Id, idTarget)
                    .then(function(data) {
                        
                        $scope.targets[idTarget] = JSON.parse(data.data.Body);
                        $scope.targets[idTarget] = $scope.targets[idTarget].d[0];
                        //console.log($scope.targets[idTarget]);
                    });
        }

        function callGetInfoForAllTargets() {

            var targetsIds = $scope.executor.d.next_executed_ids[1];

            for(var index in targetsIds) {
                var idTarget = targetsIds[index];
                getTargetInfo(idTarget);
            }
            
        }
        

		function loadData() {
            TelescopeService.getDeviceProperties($routeParams.idTelescope, "EXEC")
                .success(function (data) {
                    if (data.Code == 200) {
                        $scope.executor = JSON.parse(data.Body);
                        console.log($scope.executor);

                        callGetInfoForAllTargets();
                        
                    } else {
                        console.log("Chyba!", data);
                    }
                })
                .error(function () {
                    
                });
        }

        function init() {
            $scope.telescope = {};
            $scope.telescope.Id = $routeParams.idTelescope;
            
            loadData();
        }
		
        init();
	});

	
}());
