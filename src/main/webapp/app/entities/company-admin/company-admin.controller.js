(function() {
    'use strict';

    angular
        .module('pulsebeatApp')
        .controller('CompanyAdminController', CompanyAdminController);

    CompanyAdminController.$inject = ['$state', 'CompanyAdmin', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'User', 'Company'];

    function CompanyAdminController($state, CompanyAdmin, ParseLinks, AlertService, paginationConstants, pagingParams, User, Company) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        loadAll();

        function loadAll () {
            CompanyAdmin.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.companyAdmins = data;
                vm.page = pagingParams.page;

                // load company names and user info of Company Admins
                for(var i=0; i < vm.companyAdmins.length; i++){
                    // Get the company name
                    Company.get({'id' : vm.companyAdmins[i].companyId}, function(result){
                        // Since async, loop might be over and i might be undefined
                        // find the correct companyAdmin using id, and add

                        for(var j = 0; j < vm.companyAdmins.length; j++){
                            // multiple admins can have the same company
                            if (result.id === vm.companyAdmins[j].companyId){
                                vm.companyAdmins[j].company = result.name;
                            }
                        }

                    });

                    // get the user data
                    User.get({'login' : vm.companyAdmins[i].userId}, function(result){

                        for(var j=0; j < vm.companyAdmins.length; j++){
                            if(result.login === vm.companyAdmins[j].userId){
                                vm.companyAdmins[j].name = result.firstName + " " + result.lastName;
                                vm.companyAdmins[j].email = result.email;
                                // one to one mapping
                                break;
                            }
                        }

                    });

                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
