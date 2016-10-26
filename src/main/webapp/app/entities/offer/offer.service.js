(function() {
    'use strict';
    angular
        .module('hipsterHousesApp')
        .factory('Offer', Offer);

    Offer.$inject = ['$resource'];

    function Offer ($resource) {
        var resourceUrl =  'api/offers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
