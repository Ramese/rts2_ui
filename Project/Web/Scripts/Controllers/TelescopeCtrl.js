/*global angular, console*/

/*
README:

For dependencies declaration use file controllers.js, where is inicialized module name "controllers".

If you insert dependencies here, you override all modeles with name "controllers" (YOU DONT WANT!).
*/

(function () {
	'use strict';

	var app = angular.module('controllers');

	app.controller('TelescopeCtrl', function ($scope, TelescopeService, $routeParams, ImageService) {

        //var myVar = setInterval(getC, 5000);

        function publicTimeChanged () {
            var publicTimeStart = new Date();
            var publicTimeEnd = new Date();

            publicTimeStart.setHours($scope.publicStartHours);
            publicTimeStart.setMinutes($scope.publicStartMinutes);

            publicTimeEnd.setHours($scope.publicEndHours);
            publicTimeEnd.setMinutes($scope.publicEndMinutes);

            $scope.telescope.PublicTimeStart = publicTimeStart;
            $scope.telescope.PublicTimeEnd = publicTimeEnd;
        }

        $scope.getCurrent = function() {
            ImageService.getCurrent($routeParams.id)
                .success(function (data){
                    $scope.imgSrc = data.Body;
                    //console.log($scope.imgSrc);
                })
                .error(function (data){
                    console.log(data);
                });
        }
        
        
        

        $scope.save = function () {
            publicTimeChanged();

            TelescopeService.saveTelescope($scope.telescope)
                .success(function (data) {
                    $scope.telescope = data;
                })
                .error(function (data) {
                    console.log("telescope save error", data);
                });
        };
        
        function loadData() {
            TelescopeService.getTelescope($routeParams.id)
                .success(function (data) {
                    $scope.telescope = data;

                    $scope.telescope.PublicTimeEnd = new Date($scope.telescope.PublicTimeEnd);
                    $scope.telescope.PublicTimeStart = new Date($scope.telescope.PublicTimeStart);
                    
                    $scope.publicEndHours = $scope.telescope.PublicTimeEnd.getHours();
                    $scope.publicEndMinutes = $scope.telescope.PublicTimeEnd.getMinutes();

                    $scope.publicStartHours = $scope.telescope.PublicTimeStart.getHours();
                    $scope.publicStartMinutes = $scope.telescope.PublicTimeStart.getMinutes();
                })
                .error(function (data) {
                
                });
        }
        
        function init() {
            $scope.telescope = {};
            $scope.telescope.PublicTimeStart = new Date();
            $scope.telescope.PublicTimeEnd = new Date();

            if ($routeParams.id === "new") {
                console.log("new");
            } else {
                loadData();
                $scope.getCurrent();
            }
        }

        

        init();
        
	});

}());
