/* Copyright (C) 2014, 2015 James E. Stark
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;

import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of the nature of a particular <code>Activity</code>.
 * Instances of the <code>ActivityType</code> interface serve to describe the
 * nature of the instances of the <code>Activity</code> interface with which
 * they are associated.  Example <code>ActivityType</code> instances include:
 * "Assignment," "Quiz," and "Presentation."  For instances of
 * <code>Activity</code> where the data was harvested from a Learning
 * Management System (such as Moodle) the <code>ActivityType</code> will be the
 * name of the module from which the data contained in the associated
 * <code>Activity</code> instances was harvested.
 * <p>
 * Instances <code>ActvityType</code> interface has a strong dependency on the
 * associated instances of the <code>ActivitySource</code> interface.  If an
 * instance of a particular <code>ActivitySource</code> is removed from the
 * <code>DataStore</code> then all of the associated instances of the
 * <code>ActivityType</code> interface must be removed as well.  Similarly,
 * instances of the <code>Activity</code> interface are dependent on the
 * associated instance of the <code>ActivityType</code> interface.  Removing an
 * instance of the <code>ActivityType</code> interface from the
 * <code>DataStore</code> will require the removal of the associated instances
 * of the <code>Activity</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityTypeBuilder
 * @see     ActivityTypeLoader
 */

public abstract class ActivityType extends Element
{
	/** The <code>MetaData</code> definition for the <code>ActivityType</code> */
	protected static final Definition<ActivityType> metadata;

	/** The <code>DataStore</code> identifier of the <code>Element</code> */
	public static final Property<Long> ID;

	/** The name of the <code>ActivityType</code> */
	public static final Property<String> NAME;

	/** The associated <code>ActivitySource</code> */
	public static final Property<ActivitySource> SOURCE;

	/** Select the <code>ActivityType</code> instance by its id */
	public static final Selector<ActivityType> SELECTOR_ID;

	/** Select all of the <code>ActivityType</code> instances */
	public static final Selector<ActivityType> SELECTOR_ALL;

	/** Select an <code>ActivityType</code> instance by name and <code>ActivitySource</code> */
	public static final Selector<ActivityType> SELECTOR_NAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>ActivityType</code>.
	 */

	static
	{
		ID = Property.getInstance (ActivityType.class, Long.class, "id", false, false);
		NAME = Property.getInstance (ActivityType.class, String.class, "name", false, true);
		SOURCE = Property.getInstance (ActivityType.class, ActivitySource.class, "source", false, true);

		SELECTOR_ID = Selector.getInstance (ActivityType.class, ID, true);
		SELECTOR_ALL = Selector.getInstance (ActivityType.class, "all", false);
		SELECTOR_NAME = Selector.getInstance (ActivityType.class, "name", true, NAME, SOURCE);

		metadata = Definition.getBuilder (ActivityType.class, Element.class)
			.addProperty (ID, ActivityType::getId, ActivityType::setId)
			.addProperty (NAME, ActivityType::getName, ActivityType::setName)
			.addRelationship (SOURCE, ActivityType::getSource, ActivityType::setSource)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.addSelector (SELECTOR_NAME)
			.build ();

		Profile.registerMetaData (metadata);
	}

	/**
	 * Get the name of the <code>ActivityType</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivityType</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>ActivityType</code>.  This method is intended
	 * to be used by a <code>DataStore</code> when the
	 * <code>ActivityType</code> instance is loaded.
	 *
	 * @param  name The name of the <code>ActivityType</code>
	 */

	protected abstract void setName (String name);

	/**
	 * Get the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @return The <code>ActivitySource</code> instance
	 */

	public abstract ActivitySource getSource ();

	/**
	 * Set the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 * This method is intended to be used by a <code>DataStore</code> when the
	 * <code>ActivityType</code> instance is loaded.
	 *
	 * @param  source The <code>ActivitySource</code> for the
	 *                <code>ActivityType</code>
	 */

	protected abstract void setSource (ActivitySource source);
}
