'use strict';

angular.module('jhipsterApp')
    .factory('PCM', function ($resource) {
        return $resource('api/pCMs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
