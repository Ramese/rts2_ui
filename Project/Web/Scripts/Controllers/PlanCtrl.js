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

	app.controller('PlanCtrl', function ($scope, $routeParams, TelescopeService) {
        
        var msInDay = new Date(2016, 10, 17, 0, 0, 0, 0) - new Date(2016, 10, 16, 0, 0, 0, 0);
        var sInDay = msInDay / 1000;

        $scope.choosenDate = new Date();
        $scope.choosenDate.setHours(0,0,0,0);
        console.log($scope.choosenDate);

        $scope.endDate = new Date($scope.choosenDate.getTime() + msInDay);
        console.log($scope.endDate);

        $scope.periods = [];

        $scope.DateChanged = function () {
            $scope.choosenDate.setHours(0,0,0,0);
            console.log($scope.choosenDate, $scope.choosenDate.getTime());

            $scope.endDate = new Date($scope.choosenDate.getTime() + msInDay);
            console.log($scope.endDate, $scope.endDate.getTime());
        };

        function fakePeriod(from, to) {
            var half = (to - from)/2;

            var p = {
                IdPlan: 0,
                IdTarget: 0,
                TargetName: "Fake",
                Start: Math.floor((Math.random() * half) + from),
                End: to - Math.floor(Math.random() * half),
                RA: "p[6]",
                DEC: "p[7]",
                AltStart: "[8]",
                AzStart: "[9]"

            };

            return p;
        }

        function emptyPeriod(from, to) {

            var p = {
                IdPlan: 0,
                IdTarget: 0,
                TargetName: "Empty",
                Start: from,
                End: to,
                RA: "p[6]",
                DEC: "p[7]",
                AltStart: "[8]",
                AzStart: "[9]"

            };

            return p;
        }

        function getObjectFromArray(p) {
            return {
                IdPlan: p[0],
                IdTarget: p[1],
                TargetName: p[2],
                Start: p[4],
                End: p[5],
                RA: p[6],
                DEC: p[7],
                AltStart: [8],
                AzStart: [9]

            };
        }

        $scope.getWidthForPeriod = function (p) {
            console.log(p);
            console.log(sInDay);
            return ((p.End - p.Start) / msInDay)*100;
        }

        function sortPeriods(p1, p2) {
            return p1.Start - p2.Start;
        }

        function checkPeriods() {
            for(var i = 0; i < $scope.periods.length-1; i++) {
                if($scope.periods[i].End > $scope.periods[i+1].Start) {
                    console.log("chyba period", $scope.periods[i], $scope.periods[i+1]);
                }
            }
        }

        function addFake() {
            if($scope.periods.length == 0) {
                $scope.periods.push(fakePeriod($scope.choosenDate.getTime(), $scope.endDate.getTime()));
                return;
            }

            var index = Math.floor((Math.random() * ($scope.periods.length-1)) + 0);

            if(index == 0) {
                var p = fakePeriod($scope.choosenDate.getTime(), $scope.periods[index].Start);

                $scope.periods.push(p);
                
            } else if(index == $scope.periods.length - 1) {

                var p = fakePeriod($scope.periods[index].End, $scope.endDate.getTime());

                $scope.periods.push(p);
            } else {
                var p = fakePeriod($scope.periods[index-1].End, $scope.periods[index].Start);

                $scope.periods.push(p);
            }

            $scope.periods.sort(sortPeriods);
        }

        function addEmptySlots() {
            var periods = $scope.periods;
            var tmpPeriods = [];

            if(periods.length == 0) {
                periods.push(emptyPeriod($scope.choosenDate.getTime(), $scope.endDate.getTime()));
                return;
            }

            periods.sort(sortPeriods);

            if(periods[0].Start > $scope.choosenDate.getTime()) {
                tmpPeriods.push(emptyPeriod($scope.choosenDate.getTime(), periods[0].Start));
            }

            if(periods[periods.length-1].End < $scope.endDate.getTime()) {
                tmpPeriods.push(emptyPeriod(periods[periods.length-1].End, $scope.endDate.getTime()));
            }

            for(var i = 0; i < periods.length-1; i++) {
                tmpPeriods.push(emptyPeriod(periods[i].End, periods[i+1].Start));
            }

            for(var i = 0; i < tmpPeriods.length; i++) {
                periods.push(tmpPeriods[i]);
            }
            
            periods.sort(sortPeriods);

            console.log("here", periods);
        }

        function getGraph () {
            $scope.periods = [];

            for(var i = 0; i < $scope.plan.d.length; i++) {
                $scope.periods.add(getObjectFromArray($scope.plan.d[i]))
            }

            for(var i = 0; i < 3; i++) {
                addFake();
                checkPeriods();
            }

            console.log("afterfake", $scope.periods);

            addEmptySlots();
        }

		function loadData() {
            TelescopeService.getPlan($routeParams.id, $scope.choosenDate.getTime()/1000, $scope.endDate.getTime()/1000)
                .success(function (data) {
                    if (data.Code == 200) {
                        $scope.plan = JSON.parse(data.Body);
                        console.log($scope.plan);

                        getGraph();
                    } else {
                        console.log("Chyba!", data);
                    }
                })
                .error(function () {
                    
                });
        }

        function init() {
            loadData();
        }
		
        init();
	});

	
}());
