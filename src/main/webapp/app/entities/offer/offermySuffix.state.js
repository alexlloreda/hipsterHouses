(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('offermySuffix', {
            parent: 'entity',
            url: '/offermySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Offers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/offer/offersmySuffix.html',
                    controller: 'OfferMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('offermySuffix-detail', {
            parent: 'entity',
            url: '/offermySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Offer'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/offer/offermySuffix-detail.html',
                    controller: 'OfferMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Offer', function($stateParams, Offer) {
                    return Offer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'offermySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('offermySuffix-detail.edit', {
            parent: 'offermySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offer/offermySuffix-dialog.html',
                    controller: 'OfferMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Offer', function(Offer) {
                            return Offer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('offermySuffix.new', {
            parent: 'offermySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offer/offermySuffix-dialog.html',
                    controller: 'OfferMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                amount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('offermySuffix', null, { reload: 'offermySuffix' });
                }, function() {
                    $state.go('offermySuffix');
                });
            }]
        })
        .state('offermySuffix.edit', {
            parent: 'offermySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offer/offermySuffix-dialog.html',
                    controller: 'OfferMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Offer', function(Offer) {
                            return Offer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('offermySuffix', null, { reload: 'offermySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('offermySuffix.delete', {
            parent: 'offermySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offer/offermySuffix-delete-dialog.html',
                    controller: 'OfferMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Offer', function(Offer) {
                            return Offer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('offermySuffix', null, { reload: 'offermySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
