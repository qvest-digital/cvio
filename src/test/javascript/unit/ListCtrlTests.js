describe('List controller', function() {

	beforeEach(function() {
	});

	var allSkillList = [
	 {
	    "name" : "Wireshark",
	    "category" : "tooling",
	    "id" : "Wireshark",
	 },
	 {
	    "name" : "WS/SOAP",
	    "category" : "paradigm",
	    "id" : "WSSOAP",
	 },
	 {
	    "name" : "XSLT",
	    "category" : "prog",
	    "id" : "XSLT",
	 },
	 {
	    "name" : "YourKit Profiler",
	    "category" : "tooling",
	    "id" : "YourKitProfiler",
	 },
	 {		
	    "name" : "YouTrack",
	    "description" : "http://www.jetbrains.com/youtrack/\n\nJetBrains Bugtracker mit Agile Project Management Support, etc...",
	    "id" : "YouTrack",
	 }
	];
	
	describe('ListCtrl searching', function(){

        var scope, ctrl, skills, $httpBackend;
 
        beforeEach(module('cvio'));

        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
        	$httpBackend = _$httpBackend_;
        	$httpBackend.whenGET('/api/skills').respond(allSkillList);
        	$httpBackend.whenGET('/api/cv/cvs?fields=familyName&fields=givenName&fields=skills').respond([]);
        	
            scope = $rootScope.$new();
            scope.cv = {};
            ctrl = $controller('ListCtrl', {$scope: scope});
            $httpBackend.verifyNoOutstandingExpectation();
            $httpBackend.flush();
        }));

        it('calculateSearchScorePercent works correct', function() {
        	var cv = {
        			skills: { "Wireshark": 3,
        					   "WSSOAP" : 1 } 
        	};
        	
        	scope.searchSkillItems =  [ allSkillList[0] ];

        	
       	 	expect(100).toEqual( scope.calculateSearchScorePercent(cv) );

        	scope.searchSkillItems =  [ allSkillList[0], allSkillList[1] ];
        	expect(67).toEqual( scope.calculateSearchScorePercent(cv) );

        	scope.searchSkillItems =  [ allSkillList[0], allSkillList[1], allSkillList[2]  ];
        	expect(44).toEqual( scope.calculateSearchScorePercent(cv) );

        	scope.searchSkillItems =  [ allSkillList[2]  ];
        	expect(0).toEqual( scope.calculateSearchScorePercent(cv) );

       	 	// if no search criteria is selected, this is a full match
        	scope.searchSkillItems =  [ ];
        	expect(100).toEqual( scope.calculateSearchScorePercent(cv) );
        });
        
        it('bySearchCriteria filters correct', function() {

        	var cvMatch = { skills: { "Wireshark": 3, "WSSOAP" : 1 }};
        	var cvNonMatch = { skills: { "foo": 3 }};        	
        	scope.searchSkillItems =  [{ "id" : "Wireshark" }];
        	                        	         	
       	 	expect(true).toEqual( scope.bySearchCriteria()(cvMatch) );
       	 	expect(false).toEqual( scope.bySearchCriteria()(cvNonMatch) );
       	 	
       	 	// if no search criteria is selected, every items matches
        	scope.searchSkillItems =  [];
       	 	expect(true).toEqual( scope.bySearchCriteria()(cvNonMatch) );
        });

	});
});
