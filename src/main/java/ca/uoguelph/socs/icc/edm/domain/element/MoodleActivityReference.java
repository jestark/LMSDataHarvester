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

package ca.uoguelph.socs.icc.edm.domain.element;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityReference;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Implementation;

/**
 * Moodle specific implementation of <code>ActivityReference</code>.
 * Rather than have the primary key of the tables containing the module
 * (<code>Activity</code>) data reference the core <code>Activity</code> table,
 * the Moodle database has a separate primary key for each module table and
 * stores a reference to that key in the course modules table.  Such a design
 * does not allow the module (<code>Activity</code>) data to be loaded via a
 * join to the core <code>Activity</code> instance data.  To workaround the
 * limitation imposed by the moodle database, this class extends
 * <code>ActivityReference</code> to store the reference to the module data so
 * that the <code>Activity</code> data can be loaded in a second step.
 * <p>
 * Since the Moodle database is intended to be read-only, this class does not
 * require a builder capable of setting the <code>instanceId</code> as it
 * should never be needed.
 *
 * @author  James E. Stark
 * @version 1.1
 */

public class MoodleActivityReference extends ActivityReference implements Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key of the <code>MoodleActivity</code> */
	private Long id;

	/** The instance data identifier */
	private Long instanceId;

	/** The associated <code>Activity</code> */
	private Activity activity;

	/** The type of the <code>Activity</code> */
	private ActivityType type;

	/** The associated <code>Course</code> */
	private Course course;

	/**
	 * Static initializer to register the <code>MoodleActivity</code>
	 * class with the factories.
	 */

	static
	{
		Implementation.getInstance (ActivityReference.class, MoodleActivityReference.class, MoodleActivityReference::new);
	}

	/**
	 * Create the <code>Activity</code> with null values.
	 */

	protected MoodleActivityReference ()
	{
		this.id = null;
		this.activity = null;
		this.type = null;
		this.course = null;
		this.instanceId = null;
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
		else if (obj instanceof MoodleActivityReference)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();

			ebuilder.appendSuper (super.equals (obj));
			ebuilder.append (this.instanceId, ((MoodleActivityReference) obj).getInstanceId ());

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Activity</code> instance.
	 * The hash code is computed based upon the <code>ActivityType</code>, the
	 * <code>Course</code> and the Moodle instance id.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1013;
		final int mult = 991;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());
		hbuilder.append (this.getInstanceId ());

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
		return new ToStringBuilder (this)
				.appendSuper (super.toString ())
				.append ("instanceId", this.instanceId)
				.toString ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Activity</code>
	 * instance.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of the
	 * child class.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

	@Override
	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded, or by the <code>ActivityBuilder</code> implementation to set the
	 * <code>DataStore</code> identifier, prior to storing a new
	 * <code>Activity</code> instance.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of the
	 * child class.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final Long id)
	{
		this.id = id;
	}

	/**
	 * Get a reference to the <code>Activity</code>.
	 *
	 * @return The <code>Activity</code>
	 */

	public Activity getActivity ()
	{
		if (this.activity == null)
		{
			if (this.instanceId == null)
			{
				throw new IllegalStateException ("instance ID is NULL");
			}

			this.activity = this.getDataStore ()
				.getQuery (Activity.class,
						Activity.getActivityClass (this.getType ()),
						Activity.SELECTOR_ID)
				.setValue (Activity.ID, this.instanceId)
				.query ();

			if (this.activity == null)
			{
				throw new IllegalStateException (String.format ("Failed to load data for Activity: %s/%d", this.type.getName (), this.instanceId));
			}

			// Type is transient on the loaded activity, so copy it in here
			this.getDataStore ()
				.getProfile ()
				.getMetaData (Activity.class)
				.setValue (Activity.REFERENCE, this.activity, this);
		}

		return this.propagateDomainModel (this.activity);
	}

	protected void setActivity (final Activity activity)
	{
		throw new UnsupportedOperationException ();
	}

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of the
	 * child class.
	 *
	 * @return The <code>Course</code> instance
	 */

	@Override
	public Course getCourse ()
	{
		return this.propagateDomainModel (this.course);
	}

	/**
	 * Set the <code>Course</code> with which the <code>Activity</code> is
	 * associated.  This method is intended to be used by a <code>DataStore</code>
	 * when the <code>Activity</code> instance is loaded.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of the
	 * child class.
	 *
	 * @param  course The <code>Course</code>, not null
	 */

	@Override
	protected void setCourse (final Course course)
	{
		assert course != null : "course is NULL";

		this.course = course;
	}

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of the
	 * child class.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	@Override
	public ActivityType getType ()
	{
		return this.propagateDomainModel (this.type);
	}

	/**
	 * Set the <code>ActvityType</code> with which the <code>Activity</code> is
	 * associated.  This method is intended to be used by a <code>DataStore</code>
	 * when the <code>Activity</code> instance is loaded.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of the
	 * child class.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 */

	@Override
	protected void setType (final ActivityType type)
	{
		assert type != null : "type is NULL";

		this.type = type;
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Element</code>
	 * containing the instance specific data for the <code>Activity</code>.
	 *
	 * @return A <code>Long</code> containing the identifier
	 */

	public Long getInstanceId ()
	{
		return this.instanceId;
	}

	/**
	 * Set the <code>DataStore</code> identifier for the <code>Element</code>
	 * containing the instance specific data for the <code>Activity</code>.  This
	 * method is intended to be used by a <code>DataStore</code> when the
	 * <code>Activity</code> instance is loaded.
	 *
	 * @param  instanceId The identifier
	 */

	protected void setInstanceId (final Long instanceId)
	{
		assert instanceId != null : "instanceId is NULL";

		this.instanceId = instanceId;
	}
}
