
/**
 * Helper Function:
 * Deletes one item entry from the supplied list.
 */
function deleteFromCollection(collection, item) {
	var pos = collection.indexOf(item);       
	// if we found it: delete it from the collection
	if (pos != -1) {
		collection.splice(pos, 1);
	}            
}

/**
 * Helper Function:
 * Matches two strings ignoring case, and non characters
 */
function fuzzyMatch(string1, string2) {
	if (string1 == null || string2 == null)
		return;
	string1 = string1.replace(/[^a-z0-9]/gi, '');
	string2 = string2.replace(/[^a-z0-9]/gi, '');
	return string1.toLowerCase() == string2.toLowerCase(); 
}

/**
 * comparator for sorting by start start date
 */
function sortByStartDate(experienceA, experienceB) {
	var a = experienceA.start;
	var b = experienceB.start;
	if (a == undefined)
		return -1;
	if (b == undefined)
		return 1;
	if (b.year-a.year != 0) {		
		return b.year-a.year;
	} 
	return b.month-a.month;
}

/**
 * removes all style information, html tags and multiple whitespaces
 * but preserves line breaks.
 *
 * @param text
 */
function revoveHTMLAndStyleButNotBr(text) {
	text = text.replace(/<style(.)*style>/gm, '');
	text = text.replace(/\n/g, ' ');
	text = text.replace(/<br>|<\/br>/gm, '\n');
	text = $('<div/>').html(text).text();
	text = text.replace(/[ \t\r]{2,}/g, ' ');
	text = text.replace(/\n/gm, '</br>');	
	return text.trim();
}

/**
 * the angular main module for the cv application
 */
var cv = angular.module('cvio', ['ngResource', 'ui.bootstrap']);

/**
 * The controller for List view of the application
 */
cv.controller('ListCtrl', ['$scope', 'Skills', '$http', function($scope, Skills, $http) {
	
	/**
	 * The List of Skill-Items
	 */
    $scope.skillItems = Skills.query();
    
    /**
     * The Skill-Items, which are currently selected as search filter
     */
    $scope.searchSkillItems = [];

	/**
	 * The list of CVSs
	 */
	$scope.cvs =  { };
	
	/**
	 * current host url for exporting a cv.
	 */
	$scope.host = location.href;

    // just load the list of all cvs into $scope.cvs
    $http.get('/api/cv/cvs?fields=familyName&fields=givenName&fields=skills')
        .success(function(data, status, headers, config) {
            $scope.cvs = data;
        })
        .error(function(data, status, headers, config) {
            alert("error while loading "+status);
        });

    /**
     * Remove one Skill-Item from the search filter
     */
    $scope.removeSearchSkill = function(skillItem) {
    	deleteFromCollection($scope.searchSkillItems, skillItem);
    }

    /**
     * Add a Skill-Item matching the supplied name
     * to the search filter. 
     */
    $scope.addSearchTerm = function(term) {
        for (var i=0; i<$scope.skillItems.length; i++) {
        	var item = $scope.skillItems[i];
           	if (item.name == term && $scope.searchSkillItems.indexOf(item) == -1) {            	
            	$scope.searchSkillItems.push(item);
            	$scope.searchTerm = '';
                break;
            }
        }
    }
    
    /**
     * calculates the match of an cv for a search in $scope.searchSkillItems
     * @return returns an interger value between 0 an 100
     */    
    $scope.calculateSearchScorePercent = function(cvEntry) {
    	if (! $scope.searchSkillItems.length > 0) {
    		return 100;
    	}
    	var cvSkills = cvEntry.skills;
    	var score = 0;
    	for (var i=0; i<$scope.searchSkillItems.length; i++) {
           	var skillItem = $scope.searchSkillItems[i];
           	if (cvSkills[skillItem.id] > 0 ) {
           		score += cvSkills[skillItem.id];
           	}
    	}
    	return Math.round(100 * score / ($scope.searchSkillItems.length * 3));
    }
    
    /**
     * Filter for a list of cvs.
     * The filter checks, if all of the currently selected search items are
     * selected in the cv.
     */
    $scope.bySearchCriteria = function() {
    	return function(cvEntry) {
    		return $scope.calculateSearchScorePercent(cvEntry) > 0;
	    }
    }
    
    
    /**
     + Delete cv
     */
    $scope.deleteCV = function(cv) {
        var bool = confirm(cv.familyName+", " +cv.givenName + " wirklich löschen? ");

        if(bool == true) {
            $http.delete(cv.ref)
            	.success(function(data, status, headers, config) {
                    deleteFromCollection($scope.cvs, cv);
            	})
            	.error(function(data, status, headers, config) {
            		alert("error while deletion");
            	});
        }
    };
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
    $scope.isBusyWithSaving = false;

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
        deleteFromCollection($scope.cv.educations, education);
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
        deleteFromCollection($scope.cv.jobs, job);
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
                $scope.isBusyWithSaving = false;
            })
            .error(function(data, status, headers, config) {
            	$scope.isBusyWithSaving = false;
                alert("error while loading "+status);
            });
    }

    /**
     * Saves the cv data.
     * If the currentUri is known, this will be an update of the cv.
     * Otherwise we create a new one.
     */
    $scope.save = function() {
    	
    	// prevent from double saving
    	if($scope.isBusyWithSaving)
    		return;    	
    	$scope.isBusyWithSaving = true;
    	
    	// sort experience lists
    	$scope.cv.jobs.sort(sortByStartDate);
    	$scope.cv.educations.sort(sortByStartDate);
    

    	// update
        if ($scope.currentUri) { 
            $http.put($scope.currentUri, $scope.cv)
                .success(function(data, status, headers, config) {
                    $scope.modified = false;
                    $scope.isBusyWithSaving = false;
                })
                .error(function(data, status, headers, config) {
                	$scope.isBusyWithSaving = false;
                    alert("error while saving "+status);
                });

        } 
        
        // create new
        else { 
            $http.post('/api/cv/cvs', $scope.cv)
                .success(function(data, status, headers, config) {
                    $scope.ignoreNextWatch = true;
                    // Load the newly created cv data from the server
                    // supplied with the http location header
                    $scope.loadUri(headers('Location'));
                	$scope.isBusyWithSaving = false;
                })
                .error(function(data, status, headers, config) {
                	$scope.isBusyWithSaving = false;
                    alert("error while saving "+status);
                });
        }
    };
    
    

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
    
    /**
     * Save the current cv id for exporting the cv.
     */
    $scope.exportId = location.search.split('/api/cv/cvs/')[1];

}]);

/**
 * This is a factory for an $response to access the skill items on the server
 */
cv.factory('Skills', function($resource){
    return $resource('/api/skills' + '/:item', {item: '@id'}, {
        query: {method:'GET', params:{}, isArray:true},
    });
});

/**
 * Controller for the skill tab
 * This is not ready for now
 */
cv.controller('SkillCtrl', ['$scope', 'Skills', '$http', function($scope, Skills, $http) {

    $scope.skillItems = Skills.query(),

    $scope.categorySelection = 'prog';
    $scope.categories = [
                         {
                        	 'id': 'prog',
                        	 'name': 'Programmiersprachen und Frameworks',
                         },
                         {
                        	 'id': 'build',
                        	 'name': 'Build, Deploymnet und Plattformen',
                         },
                         {
                        	 'id': 'db',
                        	 'name': 'Datenbanken',
                         },
                         {
                        	 'id': 'test',
                        	 'name': 'Software Testing',
                         },
                         {
                        	 'id': 'concept',
                        	 'name': 'Konzepte und Vorgehen',
                         },
                         {
                        	 'id': 'other',
                        	 'name': 'Sonstiges',
                         }                                                  
                         ];
    

    $scope.skillLevelHeader = {'beginner': 'Grundkenntnisse',  'advanced': 'Erfahrung', 'expert': 'Expertise'};
    $scope.skillLevels = {
    						"beginner": 1,
    						"advanced": 2,
    						"expert": 3
    					 };    					

    /**
     * This is a model map,
     * for new entries of skills, used by addNewSkill.
     * The keys in this map are the keys for the skill-boxed, and the values are the 
     * currently entered skill names.
     */
    $scope.newSkill = {};
    
    /**
     * Sets the Skill for an items by its Id.
     * @param itemId the id of the item
     * @param skillLevel string key of the skill level, e.g. 'beginner'
     */
    $scope.setSkill = function(itemId, skillLevel) {
    	$scope.cv['skills'][itemId] = $scope.skillLevels[skillLevel];
    }

    /**
     * Removes the the skill from the item
     * @param itemId the id of the item
     */
    $scope.removeSkill = function(itemId) {
    	delete $scope.cv['skills'][itemId];
    }
    
    /**
     * Create a new Skill and put it in the supplied box.
     * If we find a skill, with a (fuzzy-)matching name,
     * than no new skill is created, but we puth this skill in the box. 
     */
    $scope.addNewSkill = function(skillName, skillLevel) {
    	if (skillName == null)
    		return;
    	skillName = skillName.trim();
    	if (skillName == '')
    		return;
    	
    	// if we find an item with the same name
    	// we put this in the box
    	for (var i=0; i<$scope.skillItems.length; i++) {
    		var skillItem = $scope.skillItems[i];
    		if (fuzzyMatch(skillItem.name, skillName)) {
    			$scope.setSkill(skillItem.id, skillLevel);
        		$scope.newSkill[skillLevel] = '';
    			return;
    		}
    	}
    	
    	if (!confirm('Eintrag '+ skillName +' anlegen?\nHaben sie gepr\u00FCft, dass es nicht bereits einen \u00E4hnlichen Eintrag gibt?'))
    		return;
    	
    	// otherwise, we create a new one
    	var newSkill = new Skills();
    	newSkill.name = skillName;
    	newSkill.category = 'other';
    	newSkill.$save(function(object, responseHeaders) {
    		$http.get(responseHeaders("Location")).success(
    			      function (newObject) {
    		    			$scope.skillItems.push(newObject);
    		    			$scope.setSkill(newObject.id, skillLevel);
    			      });
    		$scope.newSkill[skillLevel] = '';
    	});
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
     * Sets the categorySelection for later filtering
     */
    $scope.setCategorySelection = function(categorySelecttion) {
    	$scope.categorySelection = categorySelecttion;
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
            
            elm.bind('paste', function(e) {
            	setTimeout(function() {
            		var text = revoveHTMLAndStyleButNotBr(elm.html());
            		scope.$apply(function () {
                		elm.html(text);
                		ctrl.$setViewValue(text);  	
            			console.log('paste: '+ text);	
            		})
            		
            	}, 10);
            });
        }                                                                                                                                         
    }                                                                                                                                             
});

/**
 * Directive for skill entry with autocompletion.
 */
cv.directive('cvSkillEntry', function() {
  return {
      restrict: 'E',
      require: 'ngModel',
      scope: {
          'ngModel': '=',
          'submitFunction': '&',
          'label': '@',
          'skillList': '=',
      },
      
      template: '<form  style="display:inline" ng-submit="submitFunction()">\
    	  			<span class="input-group pull-right" style="width: 180px;">\
      					<input type="text" class="input-sm form-control skillTypeahead" style="width:130px" ng-model="ngModel"\
    	  					typeahead="skill.name for skill in skillList| filter:$viewValue | limitTo:30">\
      					<span class="input-group-btn">\
      						<input type="submit" class="input-sm btn btn-default " value="{{label}}"/>\
      					</span>\
    	  			</span>\
    	  		</form>'  
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
          'inputId': '@',
      },
      template: '<div class="form-group">\
                  <label for="{{input-id}}" class="col-sm-2 control-label">{{label}}</label>\
                  <div class="col-sm-5">\
                   <input type="text" class="form-control input-sm" id="{{inputId}}" ng-model="ngModel">\
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
    out += '                          <option value="">---</option>\n';
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
    	                  <option value="">---</option>\
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
