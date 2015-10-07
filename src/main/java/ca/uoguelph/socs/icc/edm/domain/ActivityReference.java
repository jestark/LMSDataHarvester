/* Copyright (C) 2015 James E. Stark
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Reference/Proxy for <code>Activity</code> instances.  This class exists as a
 * work-around for broken JPA implementations and broken database schema's.  As
 * such, this class is intended for internal use only.  All of its properties
 * are accessible via <code>Activity</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class ActivityReference extends Element
{
	/** The associated <code>Activity</code> */
	public static final Property<Activity> ACTIVITY;

	/** The associated <code>Course</code> */
	public static final Property<Course> COURSE;

	/** The associated <code>ActivityType</code> */
	public static final Property<ActivityType> TYPE;

	/** Select all <code>Activity</code> instances by <code>ActivityType</code> */
	public static final Selector SELECTOR_TYPE;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Activity</code>.
	 */

	static
	{
		ACTIVITY = Property.getInstance (Activity.class, "activity", Property.Flags.REQUIRED);
		COURSE = Property.getInstance (Course.class, "course", Property.Flags.REQUIRED);
		TYPE = Property.getInstance (ActivityType.class, "type", Property.Flags.REQUIRED);

		SELECTOR_TYPE = Selector.getInstance (TYPE, false);

		Definition.getBuilder (ActivityReference.class, Element.class)
			.addRelationship (ACTIVITY, ActivityReference::getActivity, ActivityReference::setActivity)
			.addRelationship (COURSE, ActivityReference::getCourse, ActivityReference::setCourse)
			.addRelationship (TYPE, ActivityReference::getType, ActivityReference::setType)
			.addSelector (SELECTOR_TYPE)
			.build ();
	}

	/**
	 * Compare two <code>Activity</code> instances to determine if they are
	 * equal.
	 *
	 * @param  obj The <code>Activity</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Activity</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof ActivityReference)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();

			ebuilder.append (this.getType (), ((Activity) obj).getType ());
			ebuilder.append (this.getCourse (), ((Activity) obj).getCourse ());

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Activity</code> instance.
	 * The hash code is computed based upon the <code>ActivityType</code> and
	 * the <code>Course</code>.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1039;
		final int mult = 953;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.getType ());
		hbuilder.append (this.getCourse ());

		return hbuilder.toHashCode ();
	}

	/**
	 * Get a <code>String</code> representation of the <code>Activity</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>Activity</code> instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("type", this.getType ());
		builder.append ("course", this.getCourse ());

		return builder.toString ();
	}

	/**
	 * Get an <code>ActivityBuilder</code> instance for the specified
	 * <code>DataStore</code>.  This method creates a <code>ActivityBuilder</code>
	 * on the specified <code>DataStore</code> and initializes it with the
	 * contents of this <code>Activity</code> instance.
	 * <p>
	 * <code>ActivityReference</code> instances are created though the builder
	 * for the associated <code>Activity</code> instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The initialized <code>ActivityBuilder</code>
	 */

	@Override
	public ActivityBuilder getBuilder (final DataStore datastore)
	{
		assert datastore != null : "datastore is null";

		return this.getActivity ().getBuilder (datastore);
	}

	/**
	 * Get the <code>MetaData</code> instance for this <code>Activity</code>
	 * using the specified <code>DataStore</code>.
	 *
	 * @return The <code>MetaData</code>
	 */

	@Override
	protected MetaData<ActivityReference> metadata ()
	{
		return this.getDomainModel ()
			.getDataStore ()
			.getProfile ()
			.getCreator (ActivityReference.class, this.getClass ());
	}

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @return The <code>Course</code> instance
	 */

	public abstract Course getCourse ();

	/**
	 * Set the <code>Course</code> with which the <code>Activity</code> is
	 * associated.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  course The <code>Course</code>, not null
	 */

	protected abstract void setCourse (Course course);

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	public abstract ActivityType getType ();

	/**
	 * Set the <code>ActvityType</code> with which the <code>Activity</code> is
	 * associated.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 */

	protected abstract void setType (ActivityType type);

	/**
	 * Get the <code>Activity</code> that this <code>ActivityReference</code>
	 * is representing.
	 *
	 * @return The <code>Activity</code>
	 */

	public abstract Activity getActivity ();

	/**
	 * Set the reference to the <code>Activity</code> which contains the actual
	 * data.
	 *
	 * @param  activity The <code>Activity</code>, not null
	 */

	protected abstract void setActivity (Activity activity);
}
