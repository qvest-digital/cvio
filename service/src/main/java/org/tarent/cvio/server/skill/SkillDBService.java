package org.tarent.cvio.server.skill;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tarent.cvio.server.common.CVIOConfiguration;

import com.google.inject.Inject;

public class SkillDBService implements SkillDB {
	
	private static Logger logger = LoggerFactory.getLogger(SkillDBService.class);
	
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String CATEGORY = "category";
	private static final String CREATION_DATE = "creationDate";

	private enum SkillLabels implements Label { SKILL, CATEGOY };
	
	private GraphDatabaseService graphDb;
	
	@Inject
	public SkillDBService(CVIOConfiguration cfg) {
		logger.info("opening database in "+cfg.getDatabasePath());
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( cfg.getDatabasePath() );
	    registerShutdownHook();
	}

	private void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook( new Thread() {	    	
	    	@Override
	        public void run() {
	    		logger.info("shutdown neo4j database ..");
	    		graphDb.shutdown();
	    		logger.info("shutdown neo4j is down!");
	        }
	    });
	}
	
	@Override
	public List<Skill> getAllSkills() {
		try ( Transaction tx = graphDb.beginTx() ) {		
			ArrayList<Skill> result = new ArrayList<Skill>();
			for (Node node : GlobalGraphOperations.at(graphDb).getAllNodesWithLabel(SkillLabels.SKILL)) {
				result.add(skillFromNode(node));
			}
			tx.success();
			return result;
		}
			
	}

	@Override
	public Skill getSkillByName(String name) {
		Skill skill = null;
		try ( Transaction tx = graphDb.beginTx() ) {		
			Iterator<Node> iter = graphDb.findNodesByLabelAndProperty(SkillLabels.SKILL, NAME, name).iterator();
			if (iter.hasNext()) {
				skill = skillFromNode(iter.next());
			}
			
			tx.success();
			return skill;
        }
	}

	@Override
	public void createSkill(Skill newSkill) {
		try ( Transaction tx = graphDb.beginTx() ) {		
			Node newSkillNode = graphDb.createNode(SkillLabels.SKILL);
			updateNodeWithSkill(newSkill, newSkillNode);
			tx.success();
        }
	}

	private String getDateTime() {
		 DateTime dt = new DateTime();
		 DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		 return fmt.print(dt);
	}	

	private Skill skillFromNode(Node node) {
		Skill skill = new Skill();
		skill.setName((String)node.getProperty(NAME));
		skill.setDescription((String)node.getProperty(DESCRIPTION));
		skill.setCategory((String)node.getProperty(CATEGORY));
		skill.setCreationDate(new DateTime(node.getProperty(CREATION_DATE)));
		return skill;
	}

	private void updateNodeWithSkill(Skill newSkill, Node newSkillNode) {
		newSkillNode.setProperty(NAME, newSkill.getName());
		newSkillNode.setProperty(DESCRIPTION, newSkill.getDescription());
		newSkillNode.setProperty(CREATION_DATE, getDateTime());			
		newSkillNode.setProperty(CATEGORY, newSkill.getCategory());
	}
}
