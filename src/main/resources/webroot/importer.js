/**
 * the angular main module for the importer application
 */
var module = angular.module('importer', ['ngResource', 'ui.bootstrap']);

/**
 * This is a factory for an $response to access the skill items on the server
 */
module.factory('Skills', function($resource){
    return $resource('/api/skills' + '/:item', {item: '@id'}, {
        query: {method:'GET', params:{}, isArray:true},
    });
});

module.controller('importCtrl', ['$scope', 'Skills' ,'$http', function ($scope, Skills, $http) {
	
	/** holds the textarea data **/
	$scope.textarea = {
			name: ''
	};
	 
	/** get the existing skills **/
	$scope.skillList = Skills.query();
	
	/** some categories **/
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

	/** the selected category **/
	$scope.category = $scope.categories[0];
	
	/** reads the textarea and save the new skills **/
	$scope.readTextArea = function() {
		var lines = $scope.textarea.name.split('\n');
		var skills = null;
		for(var i = 0; i < lines.length; i++) {
			if(lines[i]) {
				var newSkill = new Skills();
		    	newSkill.name = lines[i];
		    	newSkill.category = $scope.category.id;
	    		newSkill.$save(function(object, responseHeaders) {
		    		$http.get(responseHeaders("Location")).success(
		  			      function (newObject) {
		  		    			$scope.skillList.push(newObject);
		  			      });
			    });
			}
		}
	}
	
}]);