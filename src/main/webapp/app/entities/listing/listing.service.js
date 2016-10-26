(function() {
    'use strict';
    angular
        .module('hipsterHousesApp')
        .factory('Listing', Listing);

    Listing.$inject = ['$resource'];

    function Listing ($resource) {
        var resourceUrl =  'api/listings/:id';

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
