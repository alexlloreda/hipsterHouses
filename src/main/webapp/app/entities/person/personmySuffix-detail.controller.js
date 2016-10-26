(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .controller('PersonMySuffixDetailController', PersonMySuffixDetailController);

    PersonMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Person', 'Listing', 'Offer'];

    function PersonMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Person, Listing, Offer) {
        var vm = this;

        vm.person = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hipsterHousesApp:personUpdate', function(event, result) {
            vm.person = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
