(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .controller('OfferMySuffixDeleteController',OfferMySuffixDeleteController);

    OfferMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Offer'];

    function OfferMySuffixDeleteController($uibModalInstance, entity, Offer) {
        var vm = this;

        vm.offer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Offer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
