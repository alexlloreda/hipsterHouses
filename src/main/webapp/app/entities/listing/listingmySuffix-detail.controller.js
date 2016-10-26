(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .controller('ListingMySuffixDetailController', ListingMySuffixDetailController);

    ListingMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Listing', 'Property', 'Offer', 'Person'];

    function ListingMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Listing, Property, Offer, Person) {
        var vm = this;

        vm.listing = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hipsterHousesApp:listingUpdate', function(event, result) {
            vm.listing = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
