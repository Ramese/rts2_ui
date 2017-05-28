/*global angular, console*/

/*
README:

For dependencies declaration use file controllers.js, where is inicialized module name "controllers".

If you insert dependencies here, you override all modeles with name "controllers" (YOU DONT WANT!).
*/

(function () {
	'use strict';

	var app = angular.module('controllers');

	app.controller('PublicTelescopesCtrl', function ($scope, TelescopeService) {
        
        var msInDay = new Date(2016, 10, 17, 0, 0, 0, 0) - new Date(2016, 10, 16, 0, 0, 0, 0);
        var sInDay = msInDay / 1000;

        $scope.choosenDate = new Date();
        $scope.choosenDate.setHours(0,0,0,0);
        $scope.endDate = new Date($scope.choosenDate.getTime() + msInDay);

        function sortPeriods(p1, p2) {
            return p1.Start - p2.Start;
        }

        $scope.getWidthForPeriod = function (p) {
            //console.log(p);
            //console.log(sInDay);
            return ((p.End - p.Start) / msInDay)*100;
        }

        $scope.getShowTime = function (time) {
            var t = new Date(time);
            return t.toTimeString().split(' ')[0];
        };

        function emptyPeriod(from, to) {

            var p = {
                IdPlan: 0,
                IdTarget: 0,
                TargetName: "Private time",
                Start: from,
                End: to,
                RA: "p[6]",
                DEC: "p[7]",
                AltStart: "[8]",
                AzStart: "[9]"

            };

            return p;
        }

        function checkPeriods(periods) {
            for(var i = 0; i < periods.length-1; i++) {
                if(periods[i].End > periods[i+1].Start) {
                    console.log("chyba period", periods[i], periods[i+1]);
                }
            }
        }

        function getPeriodFromTelescope(start, end) {
            return {
                IdPlan: 0,
                IdTarget: 0,
                TargetName: "Public time",
                Start: start.getTime(),
                End: end.getTime(),
                RA: 0,
                DEC: 0,
                AltStart: 0,
                AzStart: 0
            };
        }

        function addEmptySlots(periods) {
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

            for(var i = 0; i < $scope.telescopes.length; i++) {
                $scope.telescopes[i].periods = [];

                $scope.telescopes[i].PublicTimeEnd = new Date($scope.telescopes[i].PublicTimeEnd);
                $scope.telescopes[i].PublicTimeStart = new Date($scope.telescopes[i].PublicTimeStart);
            }

            for(var i = 0; i < $scope.telescopes.length; i++) {
                var t = $scope.telescopes[i];

                var start = new Date();
                var end = new Date();

                start.setHours(t.PublicTimeStart.getHours());
                start.setMinutes(t.PublicTimeStart.getMinutes());
                start.setSeconds(0, 0);

                end.setHours(t.PublicTimeEnd.getHours());
                end.setMinutes(t.PublicTimeEnd.getMinutes());
                end.setSeconds(0, 0);

                if(start.getTime() <= end.getTime()) {
                    // normal one period
                    t.periods.push(getPeriodFromTelescope(start,end));
                } else {
                    // two periods
                    var now = new Date();
                    now.setHours(0, 0, 0, 0);

                    t.periods.push(getPeriodFromTelescope(now, end));

                    now.setDate(now.getDate() + 1);
                    
                    t.periods.push(getPeriodFromTelescope(start, now));
                }

                t.periods.sort(sortPeriods);

                addEmptySlots(t.periods);
                checkPeriods(t.periods);
            }

            console.log("finish", $scope.telescopes);
        }
        
        function loadData() {
            TelescopeService.getPublicTelescopes()
                .success(function (data) {
                    console.log("tels ctrl: ", data);
                    $scope.telescopes = data;

                    getGraph();
                })
                .error(function (data) {
                
                });
        }
        
        function init() {
            
            loadData();
        }
        
        init();
	});

}());