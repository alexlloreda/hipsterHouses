(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .controller('ListingMySuffixDialogController', ListingMySuffixDialogController);

    ListingMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Listing', 'Property', 'Offer', 'Person'];

    function ListingMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Listing, Property, Offer, Person) {
        var vm = this;

        vm.listing = entity;
        vm.clear = clear;
        vm.save = save;
        vm.properties = Property.query({filter: 'listing-is-null'});
        $q.all([vm.listing.$promise, vm.properties.$promise]).then(function() {
            if (!vm.listing.property || !vm.listing.property.id) {
                return $q.reject();
            }
            return Property.get({id : vm.listing.property.id}).$promise;
        }).then(function(property) {
            vm.properties.push(property);
        });
        vm.offers = Offer.query();
        vm.people = Person.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.listing.id !== null) {
                Listing.update(vm.listing, onSaveSuccess, onSaveError);
            } else {
                Listing.save(vm.listing, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hipsterHousesApp:listingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
