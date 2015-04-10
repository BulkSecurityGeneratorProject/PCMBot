'use strict';

angular.module('jhipsterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pCM', {
                parent: 'entity',
                url: '/pCM',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'jhipsterApp.pCM.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pCM/pCMs.html',
                        controller: 'PCMController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pCM');
                        return $translate.refresh();
                    }]
                }
            })
            .state('pCMDetail', {
                parent: 'entity',
                url: '/pCM/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'jhipsterApp.pCM.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pCM/pCM-detail.html',
                        controller: 'PCMDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pCM');
                        return $translate.refresh();
                    }]
                }
            });
    });
