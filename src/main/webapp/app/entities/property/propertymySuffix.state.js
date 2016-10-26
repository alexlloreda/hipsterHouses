(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('propertymySuffix', {
            parent: 'entity',
            url: '/propertymySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Properties'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/property/propertiesmySuffix.html',
                    controller: 'PropertyMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('propertymySuffix-detail', {
            parent: 'entity',
            url: '/propertymySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Property'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/property/propertymySuffix-detail.html',
                    controller: 'PropertyMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Property', function($stateParams, Property) {
                    return Property.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'propertymySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('propertymySuffix-detail.edit', {
            parent: 'propertymySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/property/propertymySuffix-dialog.html',
                    controller: 'PropertyMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Property', function(Property) {
                            return Property.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('propertymySuffix.new', {
            parent: 'propertymySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/property/propertymySuffix-dialog.html',
                    controller: 'PropertyMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                streetAddress: null,
                                postalCode: null,
                                city: null,
                                stateProvince: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('propertymySuffix', null, { reload: 'propertymySuffix' });
                }, function() {
                    $state.go('propertymySuffix');
                });
            }]
        })
        .state('propertymySuffix.edit', {
            parent: 'propertymySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/property/propertymySuffix-dialog.html',
                    controller: 'PropertyMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Property', function(Property) {
                            return Property.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('propertymySuffix', null, { reload: 'propertymySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('propertymySuffix.delete', {
            parent: 'propertymySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/property/propertymySuffix-delete-dialog.html',
                    controller: 'PropertyMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Property', function(Property) {
                            return Property.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('propertymySuffix', null, { reload: 'propertymySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
