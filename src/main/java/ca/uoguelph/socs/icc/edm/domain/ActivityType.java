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
	/** The name of the <code>ActivityType</code> */
	public static final Property<String> NAME;

	/** The associated <code>ActivitySource</code> */
	public static final Property<ActivitySource> SOURCE;

	/** Select an <code>ActivityType</code> instance by name and <code>ActivitySource</code> */
	public static final Selector SELECTOR_NAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>ActivityType</code>.
	 */

	static
	{
		NAME = Property.getInstance (String.class, "name", Property.Flags.REQUIRED);
		SOURCE = Property.getInstance (ActivitySource.class, "source", Property.Flags.REQUIRED);

		SELECTOR_NAME = Selector.getInstance ("name", true, NAME, SOURCE);

		Definition.getBuilder (ActivityType.class, Element.class)
			.addProperty (NAME, ActivityType::getName, ActivityType::setName)
			.addRelationship (SOURCE, ActivityType::getSource, ActivityType::setSource)
			.addRelationship (Activity.class, Activity.TYPE, Activity.SELECTOR_TYPE)
			.addSelector (SELECTOR_NAME)
			.build ();
	}

	/**
	 * Get an instance of the <code>ActivityTypeBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 *
	 * @return                       The <code>ActivityTypeBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>ActivityType</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is
	 *                               immutable
	 */

	public static ActivityTypeBuilder builder (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return new ActivityTypeBuilder (datastore);
	}

	/**
	 * Get an instance of the <code>ActivityTypeBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>ActivityTypeBuilder</code> instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>ActivityType</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static ActivityTypeBuilder builder (final DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		return ActivityType.builder (model.getDataStore ());
	}

	/**
	 * Compare two <code>ActivityType</code> instances to determine if they are
	 * equal.  The <code>ActivityType</code> instances are compared based upon
	 * their associated <code>ActivitySource</code> and their names.
	 *
	 * @param  obj The <code>ActivityType</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>ActivityType</code>
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
		else if (obj instanceof ActivityType)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.getName (), ((ActivityType) obj).getName ());
			ebuilder.append (this.getSource (), ((ActivityType) obj).getSource ());

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>ActivityType</code> instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>ActivityType</code> instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("source", this.getSource ());
		builder.append ("name", this.getName ());

		return builder.toString ();
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>ActivityType</code>
	 * instance.  The hash code is computed based upon the associated
	 * <code>ActivitySource</code> and name of the instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1009;
		final int mult = 997;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.getName ());
		hbuilder.append (this.getSource ());

		return hbuilder.toHashCode ();
	}

	/**
	 * Get an <code>ActivityTypeBuilder</code> instance for the specified
	 * <code>DataStore</code>.  This method creates an
	 * <code>ActivityTypeBuilder</code> on the specified <code>DataStore</code>
	 * and initializes it with the contents of this <code>ActivityType</code>
	 * instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The initialized <code>ActivityTypeBuilder</code>
	 */

	@Override
	public ActivityTypeBuilder getBuilder (final DataStore datastore)
	{
		assert datastore != null : "datastore is null";

		return ActivityType.builder (datastore)
			.load (this);
	}

	/**
	 * Get the <code>MetaData</code> instance for this
	 * <code>ActivityType</code> using the specified <code>DataStore</code>.
	 *
	 * @return The <code>MetaData</code>
	 */

	@Override
	protected MetaData<ActivityType> metadata ()
	{
		return this.getDomainModel ()
			.getDataStore ()
			.getProfile ()
			.getCreator (ActivityType.class, this.getClass ());
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
