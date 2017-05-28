/*jslint browser: true*/
/*global $, jQuery, angular, console */
/*jslint plusplus: true */

/*
README:

Here is inicialized module "controllers", you can specify dependecies.

*/

(function () {
    'use strict';
    
    angular.module('controllers', [])
    
        .controller('AboutCtrl', ['$scope', '$rootScope', function ($scope, $rootScope) {
            $rootScope.title = "About | W-Lexicon";
        }])
    
        .controller('Error404Ctrl', ['$scope', '$location', '$window', '$translate', 'CONSTANTS', function ($scope, $location, $window, $translate, CONSTANTS) {

			$scope.$root.title = $translate.instant('TITLE_NOT_FOUND') + " | " + CONSTANTS.WEB_NAME;
		}]);
    
}());