var cv = angular.module('cv', []);

cv.directive('cvShortField', function() {
  return {
      restrict: 'EA',
      require: 'ngModel',
      scope: {
          'ngModel': '=',
          'label': '@',
      },
      template: '<div class="form-group">\
                  <label for="postalcode" class="col-lg-4 control-label">{{label}}</label>\
                  <div class="col-lg-5">\
                   <input type="text" class="form-control" id="postalcode" ng-model="ngModel">\
                  </div>\
                 </div>'
  }
});

cv.directive('contenteditable', function() {
    return {
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {
            
            // model -> view
            ctrl.$render = function() {
                elm.html(ctrl.$viewValue);
            };

            /**
             * handling the maxlength parameter
             */
            elm.bind('keyup', function(event) {
                scope.$apply(function() {
                    ctrl.$setViewValue(elm.html());
                });
            });
        }
    }
});


cv.controller('CvCtrl', ['$scope', '$http', function($scope, $http) {

    $scope.modified = false;
    $scope.cv =  { 
        'jobs': [
            {},
            { 'start': '2011-01',
              'end': '2011-02',
              'type': 'Freelance',
              'company': 'tarent GmbH, Bonn',
              'project': 'OSIAM',
              'projectDescription': 'Erstellung eines Open Source Identity und Access Management Systems auf Basis von SCIM und oAuth2.0',
              'role': 'Product Owner',
              'activity': 'Verantwortung der fachlichen Anforderungen',
              'topics': 'SCIM, oAuth, Java, REST, Spring, Postgres' 
            },
            { 'start': '2011-01',
              'end': '2011-02',
              'type': 'Freelance',
              'company': 'tarent GmbH, Bonn',
              'project': 'OSIAM',
              'projectDescription': 'Erstellung eines Open Source Identity und Access Management Systems auf Basis von SCIM und oAuth2.0',
              'role': 'Product Owner',
              'activity': 'Verantwortung der fachlichen Anforderungen',
              'topics': 'SCIM, oAuth, Java, REST, Spring, Postgres' 
            },
        ],
    };
    $scope.ignoreNextWatch = false;

    $scope.addJob = function() {
        $scope.cv.jobs.unshift({});
    }

    $scope.deleteJob = function(job) {
        var pos = -1;
        for (var i=0; i<$scope.cv.jobs.length; i++) {
            if (job == $scope.cv[i]) {
                pos = i;
                break;
            }
        }
        if (pos != -1) {
            $scope.cv.jobs.splice(pos, 1);
        }            
    }

    $scope.loadUri = function(uri) {
        $http.get(uri)
            .success(function(data, status, headers, config) {
                $scope.cv = data;
                $scope.modified = false;
            })
            .error(function(data, status, headers, config) {
                alert("error while loading "+status);
            });
    }

    var cvuri = location.search.split('cvuri=')[1];
    if (cvuri) {
        $scope.loadUri(cvuri);
    }
        
    $scope.$watch('cv', function(newValue, oldValue) {
        if ($scope.ignoreNextWatch) {
            $scope.ignoreNextWatch = false;
            return;
        }
        if (Object.keys(oldValue).length > 0 && newValue !== oldValue) {
            $scope.modified = true;
        }
    }, true);        

    $scope.save = function() {
        if ($scope.cv.cvURI) { // update
            $http.put($scope.cv.cvURI, $scope.cv)
                .success(function(data, status, headers, config) {
                    $scope.modified = false;
                })
                .error(function(data, status, headers, config) {
                    alert("error while saving "+status);
                });

        } else { // create new
            $http.post('cv_api.php', $scope.cv)
                .success(function(data, status, headers, config) {
                    $scope.ignoreNextWatch = true;
                    $scope.loadUri(headers('Location'));
                })
                .error(function(data, status, headers, config) {
                    alert("error while saving "+status);
                });
        }
    };
}]);
