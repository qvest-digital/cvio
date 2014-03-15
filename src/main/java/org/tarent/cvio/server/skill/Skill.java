package org.tarent.cvio.server.skill;

import org.joda.time.DateTime;

/**
 * Skill Object for json serialization.
 * 
 * @author smancke
 */
public class Skill {

    /**
     * The id of the Skill.
     */
    private String id;

    /**
     * the name.
     */
    private String name;

    /**
     * the description.
     */
    private String description;

    /**
     * the category.
     */
    private String category;

    /**
     * creation time of the item.
     */
    private DateTime creationTime;

    /**
     * returns the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name.
     * 
     * @param theName the name
     */
    public void setName(final String theName) {
        this.name = theName;
    }

    /**
     * returns the description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets the description.
     * 
     * @param theDescription the description
     */
    public void setDescription(final String theDescription) {
        this.description = theDescription;
    }

    /**
     * returns the categoy.
     * 
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * sets the category.
     * 
     * @param theCategory the category
     */
    public void setCategory(final String theCategory) {
        this.category = theCategory;
    }

    /**
     * returns the creation time.
     * 
     * @return the creation time
     */
    public DateTime getCreationTime() {
        return creationTime;
    }

    /**
     * sets the creation time.
     * 
     * @param theTime creation time
     */
    public void setCreationDate(final DateTime theTime) {
        this.creationTime = theTime;
    }

    /**
     * Sets the id.
     * 
     * @param theId the skill id
     */
    public void setId(final String theId) {
        this.id = theId;
    }

    /**
     * Returns the id.
     * 
     * @return the skill id
     */
    public String getId() {
        return this.id;
    }
}
