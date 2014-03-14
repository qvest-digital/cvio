
/**
 * the angular main module for the cv application
 */
var cv = angular.module('cvio', ['ngResource']);

/**
 * The controller for List view of the application
 */
cv.controller('ListCtrl', ['$scope', '$http', function($scope, $http) {

	$scope.cvs =  { };

    // just load the list of all cvs into $scope.cvs
    $http.get('/api/cv/cvs')
        .success(function(data, status, headers, config) {
            $scope.cvs = data;
        })
        .error(function(data, status, headers, config) {
            alert("error while loading "+status);
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
    	'skills' : {},
        'educations': [ {} ],
        'jobs': [
            {},           
        ],
    };
    
    /**
     * Flag to show if http request is running
     */
    $scope.isBusy = false;

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
                $scope.isBusy = false;
            })
            .error(function(data, status, headers, config) {
            	$scope.isBusy = false;
                alert("error while loading "+status);
            });
    }

    /**
     * Saves the cv data.
     * If the currentUri is known, this will be an update of the cv.
     * Otherwise we create a new one.
     */
    $scope.save = function() {
    	//preventive dual cv creation
    	if(!$scope.isBusy) {
    		$scope.isBusy = true;
	        if ($scope.currentUri) { // update
	            $http.put($scope.currentUri, $scope.cv)
	                .success(function(data, status, headers, config) {
	                    $scope.modified = false;
	                    $scope.isBusy = false;
	                })
	                .error(function(data, status, headers, config) {
	                	$scope.isBusy = false;
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
	                	$scope.isBusy = false;
	                    alert("error while saving "+status);
	                });
	        }
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
 * This is a factory for an $response to access the skill items on the server
 */
cv.factory('Skills', function($resource){
    return $resource('http://127.0.0.1/tech-rating/api/default/ratingitem', {}, {
        query: {method:'GET', params:{}, isArray:true},
    });
});

/**
 * Controller for the skill tab
 * This is not ready for now
 */
cv.controller('SkillCtrl', ['$scope', 'Skills', function($scope, Skills) {

    $scope.ratingItems = Skills.query(),
	
    $scope.selection = 'prog';

    $scope.skillHeader = {'beginner': 'Grundkenntnisse',  'advanced': 'Erfahrung', 'expert': 'Expertiese'};
    $scope.skillLevels = {
    						"beginner": 1,
    						"advanced": 2,
    						"expert": 3
    					 };    					
    
    /**
     * Sets the Skill for an items.
     * @param itemId the id of the item
     * @param skillKey the string key of the skill level, e.g. 'beginner'
     */
    $scope.setSkill = function(itemId, skillKey) {
    	$scope.cv['skills'][itemId] = $scope.skillLevels[skillKey];
    }

    /**
     * Removes the the skill from the item
     * @param itemId the id of the item
     */
    $scope.removeSkill = function(itemId) {
    	delete $scope.cv['skills'][itemId];
    }

    /**
     * Filter for the elements which are 
     * selected in the skill boxes
     * @param skillLevel The skill selvel of the box
     */
    $scope.bySkillSelection = function(skillLevel) {
        return function(item) {
        	return $scope.cv['skills'][item.id] == $scope.skillLevels[skillLevel];  
        }
    }

    /**
     * Filter for filtering by category
     */
    $scope.byCategory = function(category) {
        return function(item) {
            return item.category == category &&
            	! $scope.cv['skills'][item.id] ;
        }
    }
    
    /**
     * enable drag'n drop in the boxes using jquery-
     */
    $scope.init = function() {
	    $( "#all-items-box, #beginner, #advanced, #expert" ).sortable({
	    	connectWith: "#all-items-box, #beginner, #advanced, #expert",
	   	 	cursor: "move",
	    }).disableSelection()
	    
	    $("#beginner, #advanced, #expert").droppable({
	        drop: function(event, ui) {
        		var skill= $(this).attr('id'); // we use the calling box id as skill name
	        	$scope.$apply( function() {
	        		var idDraggable = ui.draggable.attr('id').substring("skill_item_".length);
	        		$scope.setSkill(idDraggable, skill);
	        	})
	        }	    	
	    });
	    $("#all-items-box").droppable({
	        drop: function(event, ui) {
	        	$scope.$apply( function() {
		            var idDraggable = ui.draggable.attr('id').substring("skill_item_".length);
		            $scope.removeSkill(idDraggable);
	        	})
	        }	    	
	    });
    }
    
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
