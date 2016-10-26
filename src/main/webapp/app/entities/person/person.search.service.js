(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .factory('PersonSearch', PersonSearch);

    PersonSearch.$inject = ['$resource'];

    function PersonSearch($resource) {
        var resourceUrl =  'api/_search/people/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();