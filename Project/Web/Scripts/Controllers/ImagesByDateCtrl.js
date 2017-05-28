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

	app.controller('ImagesByDateCtrl', function ($scope, $routeParams, ImageService) {
        
        function getDaysInMonth(month, year) {
            var date = new Date(year, month, 1);
            var days = [];
            while (date.getMonth() === month) {
                days.push(new Date(date));
                date.setDate(date.getDate() + 1);
            }
            return days;
        }

        $scope.downloadFits = function (image) {
            ImageService.getFitsImage($routeParams.id, image.imagePath)
                .success(function(data) {
                    var fileName = $scope.choosenDate.toISOString().slice(0,10).replace(/-/g,"");
                    fileName += " " + image.imagePath.substr(image.imagePath.length - 11);
                    download(data.Body, fileName, "image/fits");
                })
                .error(function() {

                });
        }

        function DateToString(date) {
            var mm = date.getMonth() + 1; // getMonth() is zero-based
            var dd = date.getDate();

            return [date.getFullYear(),
                    (mm>9 ? '' : '0') + mm,
                    (dd>9 ? '' : '0') + dd
                    ].join('');
        }

        function getImgCounts(dates) {
            for(var i = 0; i < dates.length; i++) {
                let index = i;
                var date = DateToString(dates[i]);

                
                ImageService.getListOfImagesForDate($routeParams.id, date)
                    .success(function (data) {
                        //console.log(data);
                        $scope.events[index].count = data.length;
                        if(data.length) {
                            $scope.events[index].status = "full";
                        } else {
                            $scope.events[index].status = "";
                        }
                    });
            }
        }

        function generateImageCounts() {
            $scope.events = getDaysInMonth($scope.choosenDate.getMonth(), $scope.choosenDate.getFullYear());

            //console.log("events", $scope.events);
            getImgCounts($scope.events);
        }

        $scope.dateChanged = function () {

            var date = DateToString($scope.choosenDate);
            ImageService.getListOfImagesForDate($routeParams.id, date)
                .success(function (data) {
                    //console.log(data);
                    $scope.images = data;
                    GetAllImagesData();
                });
		
            generateImageCounts();
                
        };

        function GetAllImagesData() {
            for(var i = 0; i < $scope.images.length; i++) {
                GetImage($scope.images[i]);
            }
        }

        function GetImage(image) {

            ImageService.getImage($routeParams.id, image.imagePath)
                .success(function(data) {
                    //console.log(data);
                    image.src = data.Body;
                })
                .error(function() {

                });
        }

        function getDayClass(data) {
            var date = data.date,
            mode = data.mode;

            if (mode === 'day') {
                var dayToCheck = new Date(date).setHours(0,0,0,0);

                for (var i = 0; i < $scope.events.length; i++) {
                    
                    var currentDay = new Date($scope.events[i]).setHours(0,0,0,0);

                    if (dayToCheck === currentDay) {
                        return $scope.events[i].status;
                    }
                }
            }

            return '';
            
        }

        $scope.options = {
            customClass: getDayClass,
            showWeeks: true
        };
			
        $scope.telescope = {};
        $scope.telescope.Id = $routeParams.id;
        
		$scope.choosenDate = new Date();
        $scope.dateChanged();

	});

	
}());
