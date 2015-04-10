'use strict';

angular.module('jhipsterApp')
    .controller('MainController', function ($scope, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.myData = [
            {
                "firstName": "Cox",
                "lastName": "Carney",
                "company": "Enormo",
                "employed": true
            },
            {
                "firstName": "Lorraine",
                "lastName": "Wise",
                "company": "Comveyer",
                "employed": false
            },
            {
                "firstName": "Nancy",
                "lastName": "Waters",
                "company": "Fuelton",
                "employed": false
            }
        ];

        $scope.gridOptions = {
            data: 'myData',
            rowTemplate: '<div ng-click="grid.appScope.fnOne(row, col)" ng-repeat="col in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ui-grid-cell></div>',
            //'<div ng-class="{\'my-style-1\':row.entity.Field1===1,  \'my-style-2\':row.entity.Field1===2,  \'my-style-2\':row.entity.Field1===3}" <div ng-repeat="col in colContainer.renderedColumns track by col.colDef.name"  class="ui-grid-cell" ui-grid-cell></div></div>',
            /*  columnDefs: [{
             field: 'firstName',
             displayName: 'FName'
             }, {
             field: 'lastName',
             displayName: 'LName'
             // cellTemplate: '<button ng-click="grid.appScope.toggle(col.displayName,row.entity[col.field])">{{row.entity[col.field]}}</button>'
             }, {
             field: 'company',
             displayName: 'Company'
             // cellTemplate: '<input type="checkbox" ng-click="grid.appScope.toggle(col.displayName,row.entity[col.field])">'
             }]
             };*/
        };

        $scope.toggle = function(name, value) {
            alert(name + ':' + value);
        };

        $scope.fnOne = function(row, col) {
            alert(col.displayName + ": " + row.entity[col.field]);
        }



    });


