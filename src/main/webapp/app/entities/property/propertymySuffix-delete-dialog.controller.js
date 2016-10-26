(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .controller('PropertyMySuffixDeleteController',PropertyMySuffixDeleteController);

    PropertyMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Property'];

    function PropertyMySuffixDeleteController($uibModalInstance, entity, Property) {
        var vm = this;

        vm.property = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Property.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
