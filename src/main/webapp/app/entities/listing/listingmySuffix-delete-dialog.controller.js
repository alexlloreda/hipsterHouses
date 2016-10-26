(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .controller('ListingMySuffixDeleteController',ListingMySuffixDeleteController);

    ListingMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Listing'];

    function ListingMySuffixDeleteController($uibModalInstance, entity, Listing) {
        var vm = this;

        vm.listing = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Listing.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
