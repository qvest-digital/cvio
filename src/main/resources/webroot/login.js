/**
 * Created by sebastianreimers on 17.03.14.
 */


var auth = angular.module('authenticate', ['ngResource']);

auth.controller('loginCon', ['$scope', '$http', function ($scope, $http) {

    $scope.login = function () {
        var data = {
            name:$scope.user,
            passwort:$scope.password
        }
        $http.post('/api/login/user/', data).success();
    }
}]);
