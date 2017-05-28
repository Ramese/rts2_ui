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

	app.controller('SelectorCtrl', function ($scope, $routeParams, TelescopeService) {

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
            var queues = $scope.selector.d.queue_names[1];

            for(var index in queues) {
                var qName = queues[index];
                var targetsIds = $scope.selector.d[qName + '_ids'][1];

                for(var index2 in targetsIds) {
                    var idTarget = targetsIds[index2];
                    getTargetInfo(idTarget);
                }
            }
        }

        $scope.toDateChaned = function(to) {
            $scope.toDate = to;
        }

        $scope.fromDateChanged = function(from) {
            $scope.fromDate = from;
        }

        $scope.queueChanged = function (qN) {
            $scope.queueName = qN;
        }

        $scope.changeTarget = function(choosenTarget) {
            $scope.choosenTarget = choosenTarget;
            //console.log(choosenTarget);
        };

        $scope.addToQueue = function() {
            var id = $scope.telescope.Id;
            var device = "SEL";
            var queue = $scope.queueName;
            //console.log($scope.choosenTarget);
            var idTarget = $scope.choosenTarget[0];

            var from;
            if($scope.fromDate)
                from = Math.floor($scope.fromDate.getTime() / 1000);
            var to;
            if($scope.toDate)
                to = Math.floor($scope.toDate.getTime() / 1000);

            var rep = $scope.rep;
            var sep = $scope.sep;

            //console.log(id, device, queue, idTarget, from, to, rep, sep);

            TelescopeService.addToQueue(id, device, queue, idTarget, from, to, rep, sep)
                .then(function (data) {
                    //console.log(data);
                    loadData();
                });
        }


        $scope.findTargets = function(search) {

            if(search.length > 2){
                TelescopeService.searchTargets($routeParams.idTelescope, search)
                    .then(function(data) {
                        //console.log("targets", data);
                        $scope.targets = JSON.parse(data.data.Body);
                        //console.log("targets", $scope.targets);
                    });
            }
        };

		function loadData() {
            TelescopeService.getDeviceProperties($routeParams.idTelescope, "SEL")
                .success(function (data) {
                    if (data.Code == 200) {
                        $scope.selector = JSON.parse(data.Body);
                        //console.log($scope.selector);

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
