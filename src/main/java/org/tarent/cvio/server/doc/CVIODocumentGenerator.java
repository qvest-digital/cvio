package org.tarent.cvio.server.doc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateException;
import net.sf.jooreports.templates.DocumentTemplateFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tarent.cvio.server.skill.Skill;

/**
 * This class contains severel helper methods to generate a document
 * based on the cv data.
 * 
 * @author vhamm
 *
 */
public class CVIODocumentGenerator {

	 /**
     * the logger.
     */
    private final Logger logger = LoggerFactory.getLogger(CVIODocumentGenerator.class);

    
	/**
	 * Generates a odt document with all cv data.
	 * 
	 * @param dataModel - a {@link HashMap} data representation. 
	 * @param skills 
	 */
	public File generateDocument(HashMap<Object, Object> dataModel) {
		DocumentTemplateFactory fac = new DocumentTemplateFactory();
		logger.trace("generating cv document");
		
		try {
			final URL resource = this.getClass().getResource("cv-template.odt");
			DocumentTemplate template = fac.getTemplate(resource.openStream());
			
			File response = new File("/tmp/cv.odt");
			template.createDocument(dataModel, new FileOutputStream(response));
			
			return response;
		} catch (IOException e) {
			logger.error("error occured while creating the cv template", e);
		} catch (DocumentTemplateException e) {
			logger.error("error occured while parsing the cv template", e);
		}
		return null;
	}

	/**
	 * Collect all ids of all skills that are used in a cv
	 * 
	 * @param cvSkills - {@link HashMap} of all cv skills.
	 * 
	 * @return {@link List} of all cv skill ids.
	 */
	public ArrayList<String> getCVSkillIds(HashMap<String, String> cvSkills) {
		ArrayList<String> skillIds = new ArrayList<String>();
		for (Iterator<Entry<String, String>> iterator = cvSkills.entrySet().iterator(); iterator.hasNext();) {
			 Entry<String, String> next = iterator.next();
			 skillIds.add(next.getKey());
		}
		return skillIds;
	}
	
	/**
	 * This methods matches all used cv skills. The skills will be sorted by their category
	 * and skill level.
	 * 
	 * @param sortedSkillMap - Map of all used skills (contains {@link Skill} object
	 *  					   and the the level of a skill) 
	 * @param dataModel - the datamodel used by JODReports
	 */
	public void matchCVSkills(Map<Skill, String> sortedSkillMap, HashMap<Object, Object> dataModel) {
		List<HashMap<Skill, String>> skills = new ArrayList<HashMap<Skill, String>>();
		HashMap<Skill, String> dbSkillList = new HashMap<Skill, String>();
		HashMap<Skill, String> testSkillList = new HashMap<Skill, String>();
		HashMap<Skill, String> otherSkillList = new HashMap<Skill, String>();
		HashMap<Skill, String> buildSkillList = new HashMap<Skill, String>();
		HashMap<Skill, String> progSkillList = new HashMap<Skill, String>();
		HashMap<Skill, String> conceptSkillList = new HashMap<Skill, String>();
		
		//sort each skill by its category
		for (Iterator<Entry<Skill, String>> iterator = sortedSkillMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<Skill, String> next = iterator.next();
			Skill skill = next.getKey();
			String lvl = next.getValue();
			switch (skill.getCategory()) {
			case "other":
				otherSkillList.put(skill, lvl);
				break;
			case "db":
				dbSkillList.put(skill, lvl);
				break;
			case "test":
				testSkillList.put(skill, lvl);
				break;
			case "build":
				buildSkillList.put(skill, lvl);
				break;
			case "prog":
				progSkillList.put(skill, lvl);
				break;
			case "concept":
				conceptSkillList.put(skill, lvl);
				break;
			default:
				break;
			}
		}
		
		if(progSkillList.size() != 0) skills.add(progSkillList);
		if(dbSkillList.size() != 0) skills.add(dbSkillList);
		if(testSkillList.size() != 0) skills.add(testSkillList);
		if(otherSkillList.size() != 0) skills.add(otherSkillList);
		if(buildSkillList.size() != 0) skills.add(buildSkillList);
		if(conceptSkillList.size() != 0) skills.add(conceptSkillList);
		
		//sort the skills by their level
		HashMap<String,HashMap<String,String>> sortSkillsByLevel = sortSkillsByLevel(skills);
		dataModel.put("skills", sortSkillsByLevel);
	}

	/**
	 * This methods sorts all previous sorted skills by their representative skill levels and
	 * create a usable format to use the skills into the cv template.
	 * 
	 * @param skills - list of skill hashmaps, sorted by category.
	 * 
	 * @return {@link HashMap} of all skills sorted by category and level.
	 */
	private HashMap<String, HashMap<String, String>> sortSkillsByLevel(List<HashMap<Skill, String>> skills) {
		HashMap<String, HashMap<String, String>> skillMap = new HashMap<String, HashMap<String,String>>();
		
		//iterate over the skill list and get each category hashmap.
		for (HashMap<Skill, String> sMap : skills) {
			HashMap<String, String> levelSkills = new HashMap<String, String>();
			
			//contains skills for each different level
			StringBuilder l1 = new StringBuilder();
			StringBuilder l2 = new StringBuilder();
			StringBuilder l3 = new StringBuilder();
			String category = null;
			
			//iterate through all skills and sort them by their level
			for (Iterator<Entry<Skill, String>> iterator = sMap.entrySet().iterator(); iterator.hasNext();) {
				Entry<Skill, String> next = iterator.next();
				String lvl = next.getValue();
				category = next.getKey().getCategory();
				String name = next.getKey().getName();
				switch (lvl) {
				case "1":
					l1.append(name + ", ");
					break;
				case "2":
					l2.append(name + ", ");
					break;
				case "3":
					l3.append(name + ", ");
					break;
				default:
					break;
				}
			}
			
			//put all together.
			if(l1.toString() != null) levelSkills.put("level1", l1.toString().trim().replaceAll(",$", ""));
			if(l2.toString() != null) levelSkills.put("level2", l2.toString().trim().replaceAll(",$", ""));
			if(l3.toString() != null) levelSkills.put("level3", l3.toString().trim().replaceAll(",$", ""));
			skillMap.put(category, levelSkills);
		}
		return skillMap;
	}
	
	/**
	 * Check if any invalid characters are inside the cv data model before the document is created.
	 * 
	 * @param cvData
	 */
	public void parseCVData(Map<String, Object> cvData) {
		for (Iterator<Entry<String, Object>> it = cvData.entrySet().iterator(); it.hasNext();) {
			Entry<String, Object> next = it.next();
		
			if(next.getValue() instanceof String) {
				if(((String)next.getValue()).contains("<br>")) {
					next.setValue(next.getValue().toString().replaceAll("<br>", "\n"));
				}
			}
			
			if(next.getValue() instanceof List) {
				ArrayList<?> list = (ArrayList<?>) next.getValue();
				for(Object o : list) {
					if(o instanceof HashMap) {
						for (Iterator<?> it2 = ((HashMap<?, ?>) o).entrySet().iterator(); it2.hasNext();) {
							Entry<String, Object> next2 = (Entry<String, Object>) it2.next();
							if(next2.getValue().toString().contains("<br>")) {
								next2.setValue(next2.getValue().toString().replaceAll("<br>", "\n"));
							}
						}
					}
				}
			}
		}
	}
}
