var cv = angular.module('cv', []);

$( "#all-items-box #beginner-box" ).sortable({
//$( "#all-items-box, #beginner-box, #advanced-box, #expert-box" ).sortable({
//    connectWith: ".connectedSortable"
});//.disableSelection();

//$( "#all-items-box, #beginner-box, #advanced-box, #expert-box" ).droppable({
//    drop: function(event, ui) {
//    }
//});


cv.controller('ListCtrl', ['$scope', '$http', function($scope, $http) {
    $scope.cvs =  { };

    $http.get('/api/cv/cvs')
        .success(function(data, status, headers, config) {
            $scope.cvs = data;
        });        
}]);


cv.controller('SkillCtrl', ['$scope', '$http', function($scope, $http) {

    $scope.ratingItems = [];
    $scope.selection = 'prog';

    $scope.byCategory = function(category) {
        return function(item) {
            return item.category == category;
        }
    }

    $http.get('http://127.0.0.1/tech-rating/api/default/ratingitem')
        .success(function(data, status, headers, config) {
            $scope.ratingItems = data;
        })
        .error(function(data, status, headers, config) {
            alert("error while loading "+status);
        });
    
        
}]);

cv.controller('CvCtrl', ['$scope', '$http', function($scope, $http) {

    $scope.modified = false;
    $scope.cv =  { 
        'educations': [ {} ],
        'jobs': [
            {},           
        ],
    };
    $scope.ignoreNextWatch = false;

    $scope.addEducation = function() {
        $scope.cv.educations.unshift({});
    }

    $scope.deleteFromCollection = function(collection, item) {
        var pos = -1;
        for (var i=0; i<collection.length; i++) {
            if (job == collection[i]) {
                pos = i;
                break;
            }
        }
        if (pos != -1) {
            collection.splice(pos, 1);
        }            
    }

    $scope.deleteEducation = function(education) {
        $scope.deleteFromCollection($scope.cv.educations, education);
    }

    $scope.addJob = function() {
        $scope.cv.jobs.unshift({});
    }

    $scope.deleteJob = function(job) {
        $scope.deleteFromCollection($scope.cv.jobs, education);
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

    $scope.loadUri = function(uri) {
        $http.get(uri)
            .success(function(data, status, headers, config) {
                $scope.cv = data;
                $scope.currentUri = uri;
                $scope.modified = false;
            })
            .error(function(data, status, headers, config) {
                alert("error while loading "+status);
            });
    }

    $scope.save = function() {
        if ($scope.currentUri) { // update
            $http.put($scope.currentUri, $scope.cv)
                .success(function(data, status, headers, config) {
                    $scope.modified = false;
                })
                .error(function(data, status, headers, config) {
                    alert("error while saving "+status);
                });

        } else { // create new
            $http.post('/api/cv/cvs', $scope.cv)
                .success(function(data, status, headers, config) {
                    $scope.ignoreNextWatch = true;
                    $scope.loadUri(headers('Location'));
                })
                .error(function(data, status, headers, config) {
                    alert("error while saving "+status);
                });
        }
    };

    var cvref = location.search.split('ref=')[1];
    if (cvref) {
        $scope.loadUri(cvref);
    }

}]);

cv.directive('contenteditable', function() {                                                                                                      
    return {                                                                                                                                      
        require: 'ngModel',                                                                                                                       
        link: function(scope, elm, attrs, ctrl) {                                                                                                 
                                                                                                                                                  
            // model -> view                                                                                                                      
            ctrl.$render = function() {                                                                                                           
                elm.html(ctrl.$viewValue);                                                                                                        
            };                                                                                                                                    
                                                                                                                                                  
            elm.bind('keyup', function(event) {                                                                                                   
                scope.$apply(function() {                                                                                                         
                    ctrl.$setViewValue(elm.html());                                                                                               
                });                                                                                                                               
            });                                                                                                                                   
        }                                                                                                                                         
    }                                                                                                                                             
});

cv.directive('cvShortField', function() {
  return {
      restrict: 'E',
      require: 'ngModel',
      scope: {
          'ngModel': '=',
          'label': '@',
          'input-id': '@',
      },
      template: '<div class="form-group">\
                  <label for="{{input-id}}" class="col-sm-2 control-label">{{label}}</label>\
                  <div class="col-sm-5">\
                   <input type="text" class="form-control input-sm" id=""{{input-id}}" ng-model="ngModel">\
                  </div>\
                 </div>'
  }
});

cv.directive('cvCeFormRow', function() {
  return {
      restrict: 'E',
      require: 'ngModel',
      scope: {
          'label': '@',
          'fontStyle': '@',
          'ngModel': '=',
      },
      template: '<div class="row" style="margin-bottom: 1px;">\
                      <div class="col-sm-3" style="text-align: right;">{{label}}</div>\
                      <div class="col-sm-9" contenteditable ng-model="ngModel" style="{{fontStyle}} min-height: 24px; max-width: 450px; overflow-wrap: break-word; word-wrap: break-word;"></div>\
                    </div>'
  }
});

function generateOptionYearList() {
    var out = '';
    for (var i=new Date().getFullYear(); i>=1980; i--) {
        out += '                          <option>' + i + '</option>\n';
    }
    return out;
}
cv.directive('cvDateField', function() {
    return {
      restrict: 'E',
      require: 'ngModel',
      scope: {
          'ngModel': '=',
      },
      template: '<div class="row" style="max-width: 170px;">\
                      <div class="col-sm-5" style="padding-right: 0px;">\
                        <select class="col-sm-6 form-control input-sm"  ng-model="ngModel.month">\
                          <option>01</option>\
                          <option>02</option>\
                          <option>03</option>\
                          <option>04</option>\
                          <option>05</option>\
                          <option>06</option>\
                          <option>07</option>\
                          <option>08</option>\
                          <option>09</option>\
                          <option>10</option>\
                          <option>11</option>\
                          <option>12</option>\
                        </select>\
                      </div>\
                      <div class="col-sm-7" style="padding-left: 0px;">\
                        <select class="form-control input-sm" style="margin: 0px;" ng-model="ngModel.year">\
          '+ generateOptionYearList() +' \
                        </select>\
                      </div>\
                    </div>'
  }
});
