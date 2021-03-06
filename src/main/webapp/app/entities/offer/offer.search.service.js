(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .factory('OfferSearch', OfferSearch);

    OfferSearch.$inject = ['$resource'];

    function OfferSearch($resource) {
        var resourceUrl =  'api/_search/offers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
