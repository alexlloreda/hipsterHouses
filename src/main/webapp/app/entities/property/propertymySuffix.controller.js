(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .controller('PropertyMySuffixController', PropertyMySuffixController);

    PropertyMySuffixController.$inject = ['$scope', '$state', 'Property', 'PropertySearch'];

    function PropertyMySuffixController ($scope, $state, Property, PropertySearch) {
        var vm = this;
        
        vm.properties = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Property.query(function(result) {
                vm.properties = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PropertySearch.query({query: vm.searchQuery}, function(result) {
                vm.properties = result;
            });
        }    }
})();
