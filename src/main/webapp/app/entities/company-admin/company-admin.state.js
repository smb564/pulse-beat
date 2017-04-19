(function() {
    'use strict';

    angular
        .module('pulsebeatApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('company-admin', {
            parent: 'entity',
            url: '/company-admin?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CompanyAdmins'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-admin/company-admins.html',
                    controller: 'CompanyAdminController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('company-admin-detail', {
            parent: 'company-admin',
            url: '/company-admin/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CompanyAdmin'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-admin/company-admin-detail.html',
                    controller: 'CompanyAdminDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'CompanyAdmin', function($stateParams, CompanyAdmin) {
                    return CompanyAdmin.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'company-admin',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('company-admin-detail.edit', {
            parent: 'company-admin-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-admin/company-admin-dialog.html',
                    controller: 'CompanyAdminDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanyAdmin', function(CompanyAdmin) {
                            return CompanyAdmin.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-admin.new', {
            parent: 'company-admin',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-admin/company-admin-dialog.html',
                    controller: 'CompanyAdminDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                companyId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('company-admin', null, { reload: 'company-admin' });
                }, function() {
                    $state.go('company-admin');
                });
            }]
        })
        .state('company-admin.edit', {
            parent: 'company-admin',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-admin/company-admin-dialog.html',
                    controller: 'CompanyAdminDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanyAdmin', function(CompanyAdmin) {
                            return CompanyAdmin.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-admin', null, { reload: 'company-admin' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-admin.delete', {
            parent: 'company-admin',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-admin/company-admin-delete-dialog.html',
                    controller: 'CompanyAdminDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CompanyAdmin', function(CompanyAdmin) {
                            return CompanyAdmin.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-admin', null, { reload: 'company-admin' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
