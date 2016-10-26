(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('listingmySuffix', {
            parent: 'entity',
            url: '/listingmySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Listings'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/listing/listingsmySuffix.html',
                    controller: 'ListingMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('listingmySuffix-detail', {
            parent: 'entity',
            url: '/listingmySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Listing'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/listing/listingmySuffix-detail.html',
                    controller: 'ListingMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Listing', function($stateParams, Listing) {
                    return Listing.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'listingmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('listingmySuffix-detail.edit', {
            parent: 'listingmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/listing/listingmySuffix-dialog.html',
                    controller: 'ListingMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Listing', function(Listing) {
                            return Listing.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('listingmySuffix.new', {
            parent: 'listingmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/listing/listingmySuffix-dialog.html',
                    controller: 'ListingMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                minimumPrice: null,
                                builtArea: null,
                                totalArea: null,
                                rooms: null,
                                bathrooms: null,
                                indoorCarParks: null,
                                outdoorCarParks: null,
                                sellPrice: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('listingmySuffix', null, { reload: 'listingmySuffix' });
                }, function() {
                    $state.go('listingmySuffix');
                });
            }]
        })
        .state('listingmySuffix.edit', {
            parent: 'listingmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/listing/listingmySuffix-dialog.html',
                    controller: 'ListingMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Listing', function(Listing) {
                            return Listing.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('listingmySuffix', null, { reload: 'listingmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('listingmySuffix.delete', {
            parent: 'listingmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/listing/listingmySuffix-delete-dialog.html',
                    controller: 'ListingMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Listing', function(Listing) {
                            return Listing.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('listingmySuffix', null, { reload: 'listingmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
