'use strict';

describe('Controller Tests', function() {

    describe('Listing Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockListing, MockProperty, MockOffer, MockPerson;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockListing = jasmine.createSpy('MockListing');
            MockProperty = jasmine.createSpy('MockProperty');
            MockOffer = jasmine.createSpy('MockOffer');
            MockPerson = jasmine.createSpy('MockPerson');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Listing': MockListing,
                'Property': MockProperty,
                'Offer': MockOffer,
                'Person': MockPerson
            };
            createController = function() {
                $injector.get('$controller')("ListingMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hipsterHousesApp:listingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
