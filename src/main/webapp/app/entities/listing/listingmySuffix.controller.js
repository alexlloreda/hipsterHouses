(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .controller('ListingMySuffixController', ListingMySuffixController);

    ListingMySuffixController.$inject = ['$scope', '$state', 'Listing', 'ListingSearch'];

    function ListingMySuffixController ($scope, $state, Listing, ListingSearch) {
        var vm = this;
        
        vm.listings = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Listing.query(function(result) {
                vm.listings = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ListingSearch.query({query: vm.searchQuery}, function(result) {
                vm.listings = result;
            });
        }    }
})();
