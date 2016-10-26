(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .controller('PropertyMySuffixDetailController', PropertyMySuffixDetailController);

    PropertyMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Property'];

    function PropertyMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Property) {
        var vm = this;

        vm.property = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hipsterHousesApp:propertyUpdate', function(event, result) {
            vm.property = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
