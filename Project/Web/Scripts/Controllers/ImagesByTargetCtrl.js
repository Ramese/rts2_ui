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

	app.controller('ImagesByTargetCtrl', function ($scope, $routeParams, ImageService, TelescopeService) {
        
        $scope.downloadFits = function (image) {
            ImageService.getFitsImage($routeParams.idTelescope, image.imagePath)
                .success(function(data) {
                    var fileName = "";//$scope.choosenDate.toISOString().slice(0,10).replace(/-/g,"");
                    fileName += "" + image.imagePath.substr(image.imagePath.length - 11);
                    download(data.Body, fileName, "image/fits");
                })
                .error(function() {

                });
        }

        $scope.targetDefault = function () {

            if($routeParams.idTarget)
            {
                ImageService.getListOfImagesForTarget($routeParams.idTelescope, $routeParams.idTarget)
                    .success(function (data) {
                        //console.log(data);
                        $scope.images = data;
                        GetAllImagesData();
                    })
                    .error(function () {
                        
                    });

                TelescopeService.getTargetById($routeParams.idTelescope, $routeParams.idTarget)
                    .then(function(data) {
                        //console.log("ttt", data);
                        $scope.choosenTarget = JSON.parse(data.data.Body).d[0];
                    })
            }
            
		
        };

        function GetAllImagesData() {
            for(var i = 0; i < $scope.images.length; i++) {
                GetImage($scope.images[i]);
            }
        }

        function GetImage(image) {

            ImageService.getImage($routeParams.idTelescope, image.imagePath)
                .success(function(data) {
                    //console.log(data);
                    image.src = data.Body;
                })
                .error(function() {

                });
        }

        $scope.changeTarget = function (choosenTarget) {
            
            ImageService.getListOfImagesForTarget($routeParams.idTelescope, choosenTarget[0])
			.success(function (data) {
				console.log(data);
                $scope.images = data;
                GetAllImagesData();
			})
			.error(function () {
				
			});
        };

        function setImgCount(index, count) {
            console.log(index, count);
            $scope.targets.d[index].imgCount = count;
        }

        function imageCount() {
            for(var i = 0; i < $scope.targets.d.length; i++) {
                var target = $scope.targets.d[i];
                let index = i;
                console.log(i, target);
                ImageService.getListOfImagesForTarget($routeParams.idTelescope, target[0])
                    .then(function (data) {
                        //target.imgCount = data.length;
                        console.log(data);
                        setImgCount(index, data.data.length);
                    });
                
                
            }
        };

        $scope.findTargets = function(search) {

            if(search.length > 2){
                TelescopeService.searchTargets($routeParams.idTelescope, search)
                    .then(function(data) {
                        //console.log("targets", data);
                        $scope.targets = JSON.parse(data.data.Body);
                        imageCount();
                    });
            }
        };
        
        $scope.idTarget = $routeParams.idTarget;

        $scope.telescope = {};
        $scope.telescope.Id = $routeParams.idTelescope;

        $scope.choosenTarget = {};

        $scope.targetDefault();

	});

	
}());