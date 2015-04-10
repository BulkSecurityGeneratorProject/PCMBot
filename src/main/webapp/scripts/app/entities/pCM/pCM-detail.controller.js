'use strict';

angular.module('jhipsterApp')
    .controller('PCMDetailController', function ($scope, $stateParams, PCM) {
        $scope.pCM = {};
        $scope.load = function (id) {
            PCM.get({id: id}, function(result) {
              $scope.pCM = result;
            });
        };
        $scope.load($stateParams.id);
    });
