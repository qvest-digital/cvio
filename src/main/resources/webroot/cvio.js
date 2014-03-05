
/**
 * the angular main module for the cv application
 */
var cv = angular.module('cvio', []);

/**
 * The controller for List view of the application
 */
cv.controller('ListCtrl', ['$scope', '$http', function($scope, $http) {

	$scope.cvs =  { };

    // just load the list of all cvs into $scope.cvs
    $http.get('/api/cv/cvs')
        .success(function(data, status, headers, config) {
            $scope.cvs = data;
        });        

}]);


/**
 * This is the main controller for the cv.
 */
cv.controller('CvCtrl', ['$scope', '$http', function($scope, $http) {

    /** 
     * The cv model.
     */
    $scope.cv =  { 
        'educations': [ {} ],
        'jobs': [
            {},           
        ],
    };

	/**
	 * Flag to show the user, that the data was modified and may be saved.
	 */
    $scope.modified = false;
    
    /**
     * This fields indicated, that the next call of watch() should be ignored
     * because is was not a user action, but caused by a loading roundtrip.
     * This is nor a clean solution.
     */
    $scope.ignoreNextWatch = false;

    /**
     * Event hook: Add's an entry to the education list.
     */
    $scope.addEducation = function() {
        $scope.cv.educations.unshift({});
    }
    
    /**
     * Event hook: Removes the supplied education from the list
     */
    $scope.deleteEducation = function(education) {
        $scope.deleteFromCollection($scope.cv.educations, education);
    }

    /**
     * Event hook: Add's an entry to the job list.
     */
    $scope.addJob = function() {
        $scope.cv.jobs.unshift({});
    }

    /**
     * Event hook: Removes the supplied job from the list
     */
    $scope.deleteJob = function(job) {
        $scope.deleteFromCollection($scope.cv.jobs, education);
    }

    /**
     * The $watch method is an angular method, which is called,
     * whenever the data under the selected path (here 'cv') has changed.
     * We use this here only to show the user, that he/she has to save.
     */
    $scope.$watch('cv', function(newValue, oldValue) {
        if ($scope.ignoreNextWatch) { // if we expect to get called because of loading
            $scope.ignoreNextWatch = false;
            return;
        }
        if (Object.keys(oldValue).length > 0 && newValue !== oldValue) {
            $scope.modified = true;
        }
    }, true);        

    /**
     * Loads the cv data model from the supplied uri
     */
    $scope.loadUri = function(uri) {
        $http.get(uri)
            .success(function(data, status, headers, config) {
                $scope.cv = data;
                $scope.currentUri = uri;
                $scope.ignoreNextWatch = true;
                $scope.modified = false;
            })
            .error(function(data, status, headers, config) {
                alert("error while loading "+status);
            });
    }

    /**
     * Saves the cv data.
     * If the currentUri is known, this will be an update of the cv.
     * Otherwise we create a new one.
     */
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
                    // Load the newly created cv data from the server
                    // supplied with the http location header
                    $scope.loadUri(headers('Location'));                    
                })
                .error(function(data, status, headers, config) {
                    alert("error while saving "+status);
                });
        }
    };

    /**
     * Helper Method:
     * Deletes one item entry from the supplied list.
     * Maybe there is a shorter way in JavaScript
     */
    $scope.deleteFromCollection = function(collection, item) {
    	// find the right item
        var pos = -1;       
        for (var i=0; i<collection.length; i++) {
            if (item == collection[i]) {
                pos = i;
                break;
            }
        }
        // if we found it: delete it from the collection
        if (pos != -1) {
            collection.splice(pos, 1);
        }            
    }

    /**
     * Basic initialisation:
     * We search for the ref parameter in the browser locationURI.
     * If is was supplied, we use this for loading of the cv.
     * Attention: The parsing of the location string is implemented very simple, 
     * so that it will only work, it is the last parameter in the string!
     */
    var cvref = location.search.split('ref=')[1];
    if (cvref) {
        $scope.loadUri(cvref);
    }

}]);


/**
 * Controller for the skill tab
 * This is not ready for now
 */
cv.controller('SkillCtrl', ['$scope', '$http', function($scope, $http) {

//    $http.get('http://127.0.0.1/tech-rating/api/default/ratingitem')
//    	.success(function(data, status, headers, config) {
//        $scope.ratingItems = data;
//    })
//    .error(function(data, status, headers, config) {
//        alert("error while loading "+status);
//    });
//
//    $scope.ratingItems = [];

	//example data for development
	$scope.ratingItems = [
	       {
		      "name" : "Wireshark",
		      "category" : "tooling",
		      "id" : 82,
		   },
		   {
		      "name" : "WS/SOAP",
		      "category" : "paradigm",
		      "id" : 51,
		   },
		   {
		      "name" : "XSLT",
		      "category" : "prog",
		      "id" : 165,
		   },
		   {
		      "name" : "YourKit Profiler",
		      "category" : "tooling",
		      "id" : 83,
		   },
		   {
		      "name" : "YouTrack",
		      "description" : "http://www.jetbrains.com/youtrack/\n\nJetBrains Bugtracker mit Agile Project Management Support, etc...",
		      "id" : 154,
		   }
	],
	
    $scope.selection = 'prog';
    $scope.cvSkills = {"49" : 1,
                       "165" : 1};

    /**
     * Filter for filtering by category
     */
    $scope.byCategory = function(category) {
        return function(item) {
            return item.category == category;
        }
    }
    
    /**
     * Filter for fill the different skill boxes
     */
    $scope.byBox= function(skillselection) {
        return function(item) {
            return item.id == category;
        }
    }
    
    /**
     * enable drag'n drop in the boxes
     */
    $( "#all-items-box, #beginner-box, #advanced-box, #expert-box" ).sortable({
    	connectWith: "#all-items-box, #beginner-box, #advanced-box, #expert-box",
   	 	cursor: "move",
   	 	update: function(event, ui) {
   	 		console.log(ui.item);
   	 	},
    }).disableSelection()

}]);

/**
 * Directive to make contenteditable fields work with a 
 * ng-model attribute.
 */
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

/**
 * Directive for simple formular fields.
 */
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

/**
 * Directive for form rows using a content editable
 */
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

/**
 * Simple function to generate the option-list for the years select box
 * @returns {String}
 */
function generateOptionYearList() {
    var out = '';
    for (var i=new Date().getFullYear(); i>=1980; i--) {
        out += '                          <option>' + i + '</option>\n';
    }
    return out;
}

/**
 * Directive for the compound date field of month an year.
 */
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
