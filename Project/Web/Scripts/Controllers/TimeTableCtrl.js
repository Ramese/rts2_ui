/*global angular, console*/

/*
README:

For dependencies declaration use file controllers.js, where is inicialized module name "controllers".

If you insert dependencies here, you override all modeles with name "controllers" (YOU DONT WANT!).
*/

(function () {
	'use strict';

	var app = angular.module('controllers');

	app.controller('TimeTableCtrl', ['$scope', '$window', 'Auth', '$translate', 'CONSTANTS', function ($scope, $window, Auth, $translate, CONSTANTS) {
        
        var msInDay = new Date(2016, 10, 17, 0, 0, 0, 0) - new Date(2016, 10, 16, 0, 0, 0, 0);
        msInDay /= 8;
        
        $scope.choosenDate = new Date(2016, 10, 15, 0, 0, 0, 0);

        $scope.countPeriodsPercents = function (periods) {
            var i,
                percent;
            for (i = 0; i < periods.length; i += 1) {
                percent = periods[i].endDateTime - periods[i].startDateTime;
                console.log("msInDay", msInDay);
                percent = (percent * 100.0) / (msInDay * 1.0);
                periods[i].percent = percent;
            }
        };
        
        $scope.getPeriods = function (timeTable, telescope) {
            
            var periods = [];
            
            timeTable.sort(function comparator(b, a) {
				return new Date(b.startDateTime) - new Date(a.startDateTime);
			});
            
            if (timeTable.length) {
                if (timeTable[0].startDateTime !== $scope.choosenDate) {
                    var eventX = {
                        coordinates: "free",
                        startDateTime: $scope.choosenDate,
                        endDateTime: timeTable[0].startDateTime
                    };
                    
                    periods.push(eventX);
                    periods.push(timeTable[0]);
                } else {
                    periods.push(timeTable[0]);
                }
                
            } else {
                var eventY = {
                        coordinates: "free",
                        startDateTime: $scope.choosenDate,
                        endDateTime: $scope.choosenDate
                    };
                
                eventY.endDateTime.setDate(eventY.endDateTime.getDate() + 1);
                
                periods.push(eventY);
                
                telescope.periods = periods;
                return;
            }
            
            var i;
            
            for (i = 1; i < timeTable.length; i += 1) {
                if (periods[periods.length - 1].endDateTime !== timeTable[i].startDateTime) {
                    var eventT = {
                        coordinates: "free",
                        startDateTime: periods[periods.length - 1].endDateTime,
                        endDateTime: timeTable[i].startDateTime
                    };
                    periods.push(eventT);
                    periods.push(timeTable[i]);
                } else {
                    periods.push(timeTable[i]);
                }
            }
            
            var nextDay = $scope.choosenDate;
            
            nextDay.setDate(nextDay.getDate() + 1);
            
            if (periods[periods.length - 1].endDateTime !== nextDay) {
                var eventK = {
                        coordinates: "free",
                        startDateTime: periods[periods.length - 1].endDateTime,
                        endDateTime: nextDay
                    };
                periods.push(eventK);
            }
            
            telescope.periods = periods;
        };
        
        $scope.telescopes = [];
        
        var telescope1 = {
            name: "tel1",
            id: 1
        };
        
        var telescope2 = {
            name: "tel2",
            id: 2
        };
        
        var timeTable1 = [];
        
        var event1 = {
            coordinates: 123,
            startDateTime: new Date(2016, 10, 17, 1, 0, 0, 0),
            endDateTime: new Date(2016, 10, 17, 3, 0, 0, 0)
        };
        
        var event2 = {
            coordinates: 125,
            startDateTime: new Date(2016, 10, 17, 4, 30, 0, 0),
            endDateTime: new Date(2016, 10, 17, 4, 45, 0, 0)
        };
        
        timeTable1.push(event1);
        timeTable1.push(event2);
        
        telescope1.timeTable = timeTable1;
        telescope2.timeTable = [];
        
        $scope.getPeriods(telescope1.timeTable, telescope1);
        $scope.getPeriods(telescope2.timeTable, telescope2);
        
        $scope.countPeriodsPercents(telescope1.periods);
        $scope.countPeriodsPercents(telescope2.periods);
        
        $scope.telescopes.push(telescope1);
        $scope.telescopes.push(telescope2);
        
		console.log($scope.telescopes);
	}]);

}());