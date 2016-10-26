(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .factory('ListingSearch', ListingSearch);

    ListingSearch.$inject = ['$resource'];

    function ListingSearch($resource) {
        var resourceUrl =  'api/_search/listings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
