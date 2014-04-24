package org.tarent.cvio.server.doc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.tarent.cvio.server.cv.CVDB;
import org.tarent.cvio.server.skill.Skill;
import org.tarent.cvio.server.skill.SkillDB;

import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;

/**
 * The export respource class provides rest methods to create different files for one cv.
 * At the moment only the creating of an odt file is supported.
 * 
 * @author vhamm
 * 
 */
@Path("/export")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentResource {

	/**
	 * The db implementation for the cvs.
	 */
	private CVDB cvdb;
	
	/**
	 * The db implementation for the skills.
	 */
	private SkillDB skilldb;
	
	/**
	 * The cvio document generator.
	 */
	private CVIODocumentGenerator cvioDocGen;
	
	@Inject
	public DocumentResource(final CVDB cvdbImpl, final SkillDB skillDbImpl, final CVIODocumentGenerator docGenImpl) {
		this.cvdb = cvdbImpl;
		this.skilldb = skillDbImpl;
		this.cvioDocGen = docGenImpl;
	}
	
	/**
     * Generates an odt document for one cv
     * 
     * @param id - the id of the cv
     * @param isAuthenticated - to protect this resource
     * 
     * @return Response - contains the created odt file
     * 
     */
	@Timed
	@GET
    @Path("/doc/{id}")
	@Produces("application/vnd.oasis.opendocument.text")
    public Response exportCV(@PathParam("id") final String id, @Auth Boolean isAuthenticated) {
    	//Get cv data
		Map<String, Object> cvData = cvdb.getCVMapById(id);
    	HashMap<Object, Object> dataModel = new HashMap<Object, Object>();

    	//check if invalid characters are in the data model.
    	cvioDocGen.parseCVData(cvData);
    	
    	//validate the model before adding
    	validateCVData(cvData);
    	
    	dataModel.put("cv", cvData);
    	
    	//get all ids of all used skills from a cv
    	@SuppressWarnings("unchecked")
		HashMap<String, String> cvSkills = (HashMap<String, String>) cvData.get("skills");
    	if(cvSkills != null) {
	    	//get all available skills
	    	List<Skill> allSkills = skilldb.getAllSkills();
	    	
	    	//filter all skills that are used in a cv. Also get the level of each skill
	    	//an put them together.
	    	Map<Skill, String> sortedSkillMap = new LinkedHashMap<Skill, String>();
	    	for(Skill s : allSkills) {
	    		if(cvSkills.containsKey(s.getId())) {
	    			sortedSkillMap.put(s, String.valueOf(cvSkills.get(s.getId())));
	    		}
	    	}
	    	
	    	//sort the skills by category and add them to the template data model
	    	cvioDocGen.matchCVSkills(sortedSkillMap, dataModel);
    	} 
    	
    	//create the document with the generated datamodel
    	File doc = cvioDocGen.generateDocument(dataModel);
    	ResponseBuilder response = Response.ok(doc);
    	response.header("Content-Disposition",
			"attachment; filename=" + doc.getName());
    	
		return response.build();
    }

	/**
	 * check if certain values are "null" and remove them from the data model
	 * 
	 * @param cvData
	 */
	private void validateCVData(Map<String, Object> cvData) {
		if(cvData.get("skills") == null || cvData.get("skills").toString().equals("{}"))
			cvData.remove("skills");
		
		if(cvData.get("educations") == null || cvData.get("educations").toString().equals("[{}]"))
			cvData.remove("educations");
		
		if(cvData.get("jobs") == null || cvData.get("jobs").toString().equals("[{}]"))
			cvData.remove("jobs");
	}
}
