(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .controller('OfferMySuffixDialogController', OfferMySuffixDialogController);

    OfferMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Offer', 'Person', 'Listing'];

    function OfferMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Offer, Person, Listing) {
        var vm = this;

        vm.offer = entity;
        vm.clear = clear;
        vm.save = save;
        vm.people = Person.query();
        vm.listings = Listing.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.offer.id !== null) {
                Offer.update(vm.offer, onSaveSuccess, onSaveError);
            } else {
                Offer.save(vm.offer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hipsterHousesApp:offerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
