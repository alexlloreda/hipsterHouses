(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .controller('OfferMySuffixDetailController', OfferMySuffixDetailController);

    OfferMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Offer', 'Person', 'Listing'];

    function OfferMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Offer, Person, Listing) {
        var vm = this;

        vm.offer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hipsterHousesApp:offerUpdate', function(event, result) {
            vm.offer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
