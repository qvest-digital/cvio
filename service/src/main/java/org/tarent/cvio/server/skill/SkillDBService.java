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
import org.tarent.cvio.server.CVIOConfiguration;

import com.google.inject.Inject;

public class SkillDBService implements SkillDB {
	
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String CATEGORY = "category";
	private static final String CREATION_DATE = "creationDate";

	private enum SkillLabels implements Label { SKILL, CATEGOY };
	
	GraphDatabaseService graphDb;
	
	@Inject
	public SkillDBService(CVIOConfiguration cfg) {
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( cfg.getDatabasePath() );
	    registerShutdownHook();
	}

	private void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook( new Thread() {	    	
	    	@Override
	        public void run() {
	            graphDb.shutdown();
	        }
	    });
	}
	
	@Override
	public List<Skill> getAllSkills() {
		try ( Transaction tx = graphDb.beginTx() ) {		
			ArrayList<Skill> result = new ArrayList<Skill>(100);
			for (Node node : GlobalGraphOperations.at(graphDb).getAllNodesWithLabel(SkillLabels.SKILL)) {
				result.add(skillFromNode(node));
			}
			tx.success();
			return result;
		}
			
	}

	private Skill skillFromNode(Node node) {
		Skill skill = new Skill();
		skill.setName((String)node.getProperty(NAME));
		skill.setDescription((String)node.getProperty(DESCRIPTION));
		skill.setCategory((String)node.getProperty(CATEGORY));
		skill.setCreationDate(new DateTime(node.getProperty(CREATION_DATE)));
		return skill;
	}

	@Override
	public Skill getSkillByName(String name) {
		Skill skill = null;
		try ( Transaction tx = graphDb.beginTx() ) {		
			Iterator<Node> iter = graphDb.findNodesByLabelAndProperty(SkillLabels.SKILL, NAME, name).iterator();
			if (iter.hasNext())
				skill = skillFromNode(iter.next());

			tx.success();
			return skill;
        }
	}

	@Override
	public void createSkill(Skill newSkill) {
		try ( Transaction tx = graphDb.beginTx() ) {		
			Node newSkillNode = graphDb.createNode(SkillLabels.SKILL);
			newSkillNode.setProperty(NAME, newSkill.getName());
			newSkillNode.setProperty(DESCRIPTION, newSkill.getDescription());
			newSkillNode.setProperty(CREATION_DATE, getDateTime());			
			newSkillNode.setProperty(CATEGORY, newSkill.getCategory());

			tx.success();
        }
	}

	private String getDateTime() {
		 DateTime dt = new DateTime();
		 DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		 return fmt.print(dt);
	}	

}
