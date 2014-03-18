/**
 * Created by sebastianreimers on 17.03.14.
 */


var auth = angular.module('authenticate',['ngResource']);

auth.controller('loginCon', ['$scope', '$http', function($scope, $http) {

    $scope.login = function () {
        $http.post('/login/user/users',$scope.user,$scope.password)
            .success(function(data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
        })
            .error(function(data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
    }
}]);