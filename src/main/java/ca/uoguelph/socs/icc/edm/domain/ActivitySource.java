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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of the source of a <code>Activity</code> in the domain
 * model.  Instances of the <code>ActivitySource</code> interface represent the
 * system from which the data for an <code>Activity</code> with a particular
 * <code>ActivityType</code> was originally harvested.  For example, an
 * <code>ActivitySource</code> could be "moodle" for data collected from the
 * Moodle Learning Management System, or "manual" for data that was collected
 * by the instructor manually.
 * <p>
 * Within the domain model the <code>ActivitySource</code> interface is a root
 * level element, as such instances of the <code>ActivitySource</code>
 * interface are not dependant upon any other domain model element to exist.
 * An associated instance of the <code>ActivitySource</code> interface is
 * required for an instance of the <code>ActivityType</code> interface to
 * exist.  If a particular instance of the <code>ActivitySource</code>
 * interface is deleted, then all of the associated instances of the
 * <code>ActivityType</code> interface must be deleted as well.
 * <p>
 * Once created, <code>ActivitySource</code> instances are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivitySourceBuilder
 * @see     ActivitySourceLoader
 */

public abstract class ActivitySource extends Element
{
	/** The name of the <code>ActivitySource</code> */
	public static final Property<String> NAME;

	/** The <code>ActivityType</code> instances associated with the <code>ActivitySource</code> */
	public static final Property<ActivityType> TYPES;

	/** Select an <code>ActivitySource</code> instance by its name */
	public static final Selector SELECTOR_NAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>ActivitySource</code>.
	 */

	static
	{
		NAME = Property.getInstance (String.class, "name", false, true);
		TYPES = Property.getInstance (ActivityType.class, "types", true, false);

		SELECTOR_NAME = Selector.getInstance (NAME, true);

		Definition.getBuilder (ActivitySource.class, Element.class)
			.addProperty (NAME, ActivitySource::getName, ActivitySource::setName)
			.addRelationship (TYPES, ActivitySource::getTypes, ActivitySource::addType, ActivitySource::removeType)
			.addSelector (SELECTOR_NAME)
			.build ();
	}

	/**
	 * Compare two <code>ActivitySource</code> instances to determine if they
	 * are equal.  The <code>ActivitySource</code> instances are compared based
	 * upon their names.
	 *
	 * @param  obj The <code>ActivitySource</code> instance to compare to the
	 *             one represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>ActivitySource</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof ActivitySource)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.getName (), ((ActivitySource) obj).getName ());

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>ActivitySource</code>
	 * instance.  The hash code is computed based upon the name of the
	 * instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1097;
		final int mult = 883;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.getName ());

		return hbuilder.toHashCode ();
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>ActivitySource</code> instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>ActivitySource</code> instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("name", this.getName ());

		return builder.toString ();
	}

	/**
	 * Get an <code>ActivitySourceBuilder</code> instance for the specified
	 * <code>DataStore</code>.  This method creates an
	 * <code>ActivitySourceBuilder</code> on the specified
	 * <code>DataStore</code> and initializes it with the contents of this
	 * <code>ActivitySource</code> instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The initialized <code>ActivitySourceBuilder</code>
	 */

	@Override
	public ActivitySourceBuilder getBuilder (final DataStore datastore)
	{
		assert datastore != null : "datastore is null";

		return new ActivitySourceBuilder (datastore)
			.load (this);
	}

	/**
	 * Get the <code>MetaData</code> instance for this
	 * <code>ActivitySource</code> using the specified <code>DataStore</code>.
	 *
	 * @return The <code>MetaData</code>
	 */

	@Override
	protected MetaData<ActivitySource> getMetaData ()
	{
		return this.getDomainModel ()
			.getDataStore ()
			.getProfile ()
			.getCreator (ActivitySource.class, this.getClass ());
	}

	/**
	 * Get the name of the <code>ActivitySource</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivitySource</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>ActivitySource</code>.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>ActivitySource</code> instance is loaded.
	 *
	 * @param name The name of the <code>ActivitySource</code>
	 */

	protected abstract void setName (String name);

	/**
	 * Get the <code>Set</code> of <code>ActivityType</code> instances for the
	 * <code>ActivitySource</code>.  If there are no <code>ActivityType</code>
	 * instances associated with the <code>ActivitySource</code> then the
	 * <code>Set</code> will be empty.
	 *
	 * @return A <code>Set</code> of <code>ActivityType</code> instances
	 */

	public abstract Set<ActivityType> getTypes ();

	/**
	 * Initialize the <code>Set</code> of dependent <code>ActivityType</code>
	 * instances.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>ActivitySource</code> instance is
	 * loaded.
	 *
	 * @param  types The <code>Set</code> of <code>ActivityType</code>
	 *               instances to be associated with the
	 *               <code>ActivitySource</code>
	 */

	protected abstract void setTypes (Set<ActivityType> types);

	/**
	 * Add the specified <code>ActivityType</code> to the
	 * <code>ActivitySource</code>.
	 *
	 * @param  type   The <code>ActivityType</code> to add, not null
	 *
	 * @return        <code>True</code> if the <code>ActivityType</code> was
	 *                successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addType (ActivityType type);

	/**
	 * Remove the specified <code>ActivityType</code> from the
	 * <code>ActivitySource</code>.
	 *
	 * @param  type   The <code>ActivityType</code> to remove, not null
	 *
	 * @return        <code>True</code> if the <code>ActivityType</code> was
	 *                successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeType (ActivityType type);
}
