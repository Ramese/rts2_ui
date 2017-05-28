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

	app.controller('CurrentImgCtrl', function ($scope, $routeParams, TelescopeService) {
		
        function drawPixel(ctx, r, g, b, a, x, y) {
			ctx.fillStyle = "rgba(" + r + "," + g + "," + b + "," + (a / 255) + ")";
			ctx.fillRect(x, y, 1, 1);
		}
        
        function drawPixelBW(ctx, intensity, x, y) {
			drawPixel(ctx, intensity, intensity, intensity, 255, x, y);
		}
		
		function draw() {
			var x_max = $scope.imageData.width,
                y_max = $scope.imageData.height,
                ctx = $("#currentImage")[0].getContext('2d'),
                y,
                x,
                color;

			for (y = 0; y < y_max; y += 1) {
				for (x = 0; x < x_max; x += 1) {
					color = $scope.imageData.imageData[y * y_max + x];
					color = color / (Math.pow(2, 16)-1); // depends on data_typ property of ccd sensor!!!
					color = color * 255;
					
					drawPixelBW(ctx, color, x, y);
				}
			}
		}
		
		$scope.GetCurrentImage = function (device) {
			TelescopeService.getCurrentImage($routeParams.id, device)
				.success(function (data) {
					if (data.Code == 200) {
						
						$scope.imageData = data.Image;
						console.log("imagedata", $scope.imageData);
						draw();
						
					} else {
						console.log("Chyba!", data);
					}
				})
				.error(function () {
				
				});
		};
		
		$scope.GetExposeData = function (device) {
			
			TelescopeService.getExposeData($routeParams.id, device)
				.success(function (data) {
					if (data.Code == 200) {
						
						$scope.imageData = data.Image;
						console.log("imagedata", $scope.imageData);
						draw();
						
					} else {
						console.log("Chyba!", data);
					}
				})
				.error(function () {
				
				});
		};
		
	});

	
}());
