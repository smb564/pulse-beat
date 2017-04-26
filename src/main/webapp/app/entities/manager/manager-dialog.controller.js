(function() {
    'use strict';

    angular
        .module('pulsebeatApp')
        .controller('ManagerDialogController', ManagerDialogController);

    ManagerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Manager', 'Company', 'User', 'Principal', 'CompanyAdmin'];

    function ManagerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Manager, Company, User, Principal, CompanyAdmin) {
        var vm = this;

        vm.manager = entity;
        vm.clear = clear;
        vm.save = save;
        vm.departments;


        // check is existing manager
        if (vm.manager.id !== null){
            // existing manger, load the data
            // get the departments of the company
            vm.departments = Company.get({'id' : vm.manager.companyId});
            vm.user = User.get({'login' : vm.manager.userId});
        }else{
            Principal.identity().then(function(currentAccount){
                CompanyAdmin.get({'id' : currentAccount.login}, function(result){
                    vm.company = Company.get({'id' : result.companyId});
                    console.log("came here");
                });
            });

            vm.user = {};
        }


        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.manager.id !== null) {
                // update user
                User.update(vm.user, function(result){
                    Manager.update(vm.manager, onSaveSuccess, onSaveError);
                }, onSaveError);
            } else {
                // new user
                User.save(vm.user, function(result){
                    vm.manager.userId = result.login;
                    Manager.save(vm.manager, onSaveSuccess, onSaveError);
                }, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pulsebeatApp:managerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
