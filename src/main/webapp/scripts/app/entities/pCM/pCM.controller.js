'use strict';

angular.module('jhipsterApp')
    .controller('PCMController', function ($scope, PCM, ParseLinks) {
        $scope.pCMs = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            PCM.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.pCMs = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            PCM.update($scope.pCM,
                function () {
                    $scope.loadAll();
                    $('#savePCMModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            PCM.get({id: id}, function(result) {
                $scope.pCM = result;
                $('#savePCMModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            PCM.get({id: id}, function(result) {
                $scope.pCM = result;
                $('#deletePCMConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PCM.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePCMConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.pCM = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
