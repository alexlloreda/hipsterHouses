(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .controller('OfferMySuffixController', OfferMySuffixController);

    OfferMySuffixController.$inject = ['$scope', '$state', 'Offer', 'OfferSearch'];

    function OfferMySuffixController ($scope, $state, Offer, OfferSearch) {
        var vm = this;
        
        vm.offers = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Offer.query(function(result) {
                vm.offers = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            OfferSearch.query({query: vm.searchQuery}, function(result) {
                vm.offers = result;
            });
        }    }
})();
