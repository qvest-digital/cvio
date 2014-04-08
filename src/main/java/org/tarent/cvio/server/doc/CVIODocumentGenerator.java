package org.tarent.cvio.server.doc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateException;
import net.sf.jooreports.templates.DocumentTemplateFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to generate an open-document file with all relevant
 * data from cvio.
 * 
 * @author Viktor Hamm
 *
 */
public class CVIODocumentGenerator {

	 /**
     * the logger.
     */
    private final Logger logger = LoggerFactory
            .getLogger(CVIODocumentGenerator.class);

	
	/**
	 * Generates a document with all cv data.
	 * 
	 * @param dataModel - a {@link HashMap} data representation. 
	 */
	public void generateDocument(HashMap<Object, Object> dataModel) {
		DocumentTemplateFactory fac = new DocumentTemplateFactory();
		logger.trace("generating cv document");
		
		try {
			DocumentTemplate template = fac.getTemplate(new File("cv-template.odt"));
			template.createDocument(dataModel, new FileOutputStream("my-output.odt"));
		} catch (IOException e) {
			logger.error("error occured while creating the cv template", e.getMessage());
		} catch (DocumentTemplateException e) {
			logger.error("error occured while parsing the cv template", e.getMessage());
		}
	}
}
