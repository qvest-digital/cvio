describe('Helper Functions', function() {

	beforeEach(function() {
	});

	describe('deleteFromCollection', function(){

        it('should delete simple elements correctly', function() {
        	var list = ['a', 'b', 'c'];
        	deleteFromCollection(list, 'b');
        	expect('a').toEqual( list[0] );
        	expect('c').toEqual( list[1] );
        });
        
        it('should delete objects correctly, based on ==', function() {
        	var object = {'b':'2', 'y':'2'};
        	var list = [ {'a':'1', 'x':'2'},
        	             object,
        	             {'c':'3', 'z':'2'},
        	             ];
        	deleteFromCollection(list, object );
        	expect('1').toEqual( list[0]['a'] );
        	expect('3').toEqual( list[1]['c'] );
        });

        it('should not throw an error for unknown elements', function() {
        	var list = ['a', 'b', 'c'];
        	deleteFromCollection(list, 'x');
        	expect('a').toEqual( list[0] );
        	expect('b').toEqual( list[1] );
        	expect('c').toEqual( list[2] );
        });

	});
	
	describe('fuzzyMatch', function(){

        it('should match strings which differ in non characters', function() {
        	expect(true).toEqual(fuzzyMatch("Hallo", "Hallo"));
        	expect(false).toEqual(fuzzyMatch("Ballo", "Hallo"));
        	expect(true).toEqual(fuzzyMatch("Hello World!", "hello   world"));
        	expect(false).toEqual(fuzzyMatch("EJB3", "EJB4"));
        });        
        
    });

});
