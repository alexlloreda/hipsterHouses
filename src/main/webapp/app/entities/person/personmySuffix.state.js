(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('personmySuffix', {
            parent: 'entity',
            url: '/personmySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'People'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/person/peoplemySuffix.html',
                    controller: 'PersonMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('personmySuffix-detail', {
            parent: 'entity',
            url: '/personmySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Person'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/person/personmySuffix-detail.html',
                    controller: 'PersonMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Person', function($stateParams, Person) {
                    return Person.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'personmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('personmySuffix-detail.edit', {
            parent: 'personmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/person/personmySuffix-dialog.html',
                    controller: 'PersonMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Person', function(Person) {
                            return Person.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('personmySuffix.new', {
            parent: 'personmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/person/personmySuffix-dialog.html',
                    controller: 'PersonMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                email: null,
                                phoneNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('personmySuffix', null, { reload: 'personmySuffix' });
                }, function() {
                    $state.go('personmySuffix');
                });
            }]
        })
        .state('personmySuffix.edit', {
            parent: 'personmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/person/personmySuffix-dialog.html',
                    controller: 'PersonMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Person', function(Person) {
                            return Person.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('personmySuffix', null, { reload: 'personmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('personmySuffix.delete', {
            parent: 'personmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/person/personmySuffix-delete-dialog.html',
                    controller: 'PersonMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Person', function(Person) {
                            return Person.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('personmySuffix', null, { reload: 'personmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
