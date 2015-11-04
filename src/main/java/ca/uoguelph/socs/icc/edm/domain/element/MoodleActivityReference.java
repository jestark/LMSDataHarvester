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

import java.util.List;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityReference;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

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

public class MoodleActivityReference extends ActivityReference
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

	/** The associated <code>LogEntry</code> instances */
	private List<LogEntry> log;

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
		this.log = new ArrayList<LogEntry> ();
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
		return (obj == this) ? true : (obj instanceof MoodleActivityReference)
			&& super.equals (obj)
			&& Objects.equals (this.instanceId, ((MoodleActivityReference) obj).getInstanceId ());
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
		return Objects.hash (super.hashCode (), this.getInstanceId ());
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
		return this.toStringHelper ()
			.add ("instanceId", this.instanceId)
			.toString ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Activity</code>
	 * instance.
	 *
	 * @return The <code>DataStore</code> identifier
	 */

	@Override
	@CheckReturnValue
	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to be
	 * used to initialize the <code>DataStore</code> on a new
	 * <code>ActivityReference</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final @Nullable Long id)
	{
		this.id = id;
	}

	/**
	 * Get a reference to the <code>Activity</code>.  This method uses the
	 * <code>instanceid</code> to load the <code>Activity</code> from the moodle
	 * database before returning it.
	 *
	 * @return The <code>Activity</code>
	 */

	@Override
	public Activity getActivity ()
	{
		if (this.activity == null)
		{
			if (this.instanceId == null)
			{
				throw new IllegalStateException ("instance ID is NULL");
			}

			this.activity = this.getDomainModel ()
				.getQuery (Activity.class,
						Activity.getActivityClass (this.getType ()),
						Activity.SELECTOR_ID)
				.setValue (Activity.ID, this.instanceId)
				.query ();

			if (this.activity == null)
			{
				throw new IllegalStateException (String.format ("Failed to load data for Activity: %s/%d", this.type.getName (), this.instanceId));
			}

			ActivityReference.METADATA.getRelationship (ActivityReference.ACTIVITY)
				.connect (this.getDomainModel (), this);
		}

		return this.propagateDomainModel (this.activity);
	}

	/**
	 * Set the reference to the <code>Activity</code> which contains the actual
	 * data.  This operation is unsupported due the way that Moodle stores the
	 * <code>Activity</code> in the database.
	 *
	 * @param  activity The <code>Activity</code>, not null
	 */

	@Override
	protected void setActivity (final Activity activity)
	{
		throw new UnsupportedOperationException ();
	}

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
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
	 * associated.  This method is intended to be used to initialize a new
	 * <code>Activity</code> instance.
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
	 * associated.  This method is intended to be used to initialize a new 
	 * <code>ActivityReference</code> instance.
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
	 * Get a <code>List</code> of all of the <code>LogEntry</code> instances
	 * which act upon the <code>ActivityReference</code>.
	 *
	 * @return A <code>List</code> of <code>LogEntry</code> instances
	 */

	@Override
	public List<LogEntry> getLog ()
	{
		this.log.forEach (x -> this.propagateDomainModel (x));

		return Collections.unmodifiableList (this.log);
	}

	/**
	 * Initialize the <code>List</code> of <code>LogEntry</code> instances
	 * associated with the <code>ActivityReference</code> instance.  This
	 * method is intended to be used to initialize a new
	 * <code>ActivityReference</code> instance.
	 *
	 * @param  log The <code>List</code> of <code>LogEntry</code> instances,
	 *             not null
	 */

	@Override
	protected void setLog (final List<LogEntry> log)
	{
		assert log != null : "log is NULL";

		this.log = log;
	}

	/**
	 * Add the specified <code>LogEntry</code> to the specified
	 * <code>ActivityReference</code>.
	 *
	 * @param  entry    The <code>LogEntry</code> to add, not null
	 *
	 * @return          <code>True</code> if the <code>LogEntry</code> was
	 *                  successfully added, <code>False</code> otherwise
	 */

	@Override
	protected boolean addLog (final LogEntry entry)
	{
		assert entry != null : "entry is NULL";

		return this.log.add (entry);
	}

	/**
	 * Remove the specified <code>LogEntry</code> from the specified
	 * <code>ActivityReference</code>.
	 *
	 * @param  entry    The <code>LogEntry</code> to remove, not null
	 *
	 * @return          <code>True</code> if the <code>LogEntry</code> was
	 *                  successfully removed, <code>False</code> otherwise
	 */

	@Override
	protected boolean removeLog (final LogEntry entry)
	{
		assert entry != null : "entry is NULL";

		return this.log.remove (entry);
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Element</code>
	 * containing the instance specific data for the <code>Activity</code>.
	 *
	 * @return The <code>DataStore</code> identifier
	 */

	public Long getInstanceId ()
	{
		return this.instanceId;
	}

	/**
	 * Set the <code>DataStore</code> identifier for the <code>Element</code>
	 * containing the instance specific data for the <code>Activity</code>.  This
	 * method is intended to be used to initialize a new
	 * <code>ActivityReference</code> instance.
	 *
	 * @param  instanceId The identifier
	 */

	protected void setInstanceId (final Long instanceId)
	{
		assert instanceId != null : "instanceId is NULL";

		this.instanceId = instanceId;
	}
}
