(function() {
    'use strict';

    angular
        .module('pulsebeatApp')
        .controller('CompanyAdminDialogController', CompanyAdminDialogController);

    CompanyAdminDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CompanyAdmin', 'User', 'Company'];

    function CompanyAdminDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CompanyAdmin, User, Company) {
        var vm = this;
        vm.companyAdmin = entity;

        // load the user data if userId is not null (update)
        if (vm.companyAdmin.userId !== null){
            User.get({'login' : vm.companyAdmin.userId},
            function(result){
                vm.user = result;
            });
        }

        // load the list of companies
        vm.companies = Company.query();

        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;

            // set user authorities to ROLE_MANAGER
            vm.user.authorities = ['ROLE_COMPANY'];

            if (vm.companyAdmin.id !== null){
                User.update(vm.user, function(result){
                    CompanyAdmin.update(vm.companyAdmin, onSaveSuccess, onSaveError);
                }, onSaveError);

            } else{
                User.save(vm.user, function(result){
                    vm.companyAdmin.userId = result.login;
                    CompanyAdmin.save(vm.companyAdmin, onSaveSuccess, onSaveError);
                }, onSaveError);
            }

        }

        function onSaveSuccess (result) {
            $scope.$emit('pulsebeatApp:companyAdminUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError (err) {
            console.log(err);
            vm.isSaving = false;
        }


    }
})();
