(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .controller('PersonMySuffixDeleteController',PersonMySuffixDeleteController);

    PersonMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Person'];

    function PersonMySuffixDeleteController($uibModalInstance, entity, Person) {
        var vm = this;

        vm.person = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Person.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
