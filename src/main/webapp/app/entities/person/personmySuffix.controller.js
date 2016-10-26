(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .controller('PersonMySuffixController', PersonMySuffixController);

    PersonMySuffixController.$inject = ['$scope', '$state', 'Person', 'PersonSearch'];

    function PersonMySuffixController ($scope, $state, Person, PersonSearch) {
        var vm = this;
        
        vm.people = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Person.query(function(result) {
                vm.people = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PersonSearch.query({query: vm.searchQuery}, function(result) {
                vm.people = result;
            });
        }    }
})();
