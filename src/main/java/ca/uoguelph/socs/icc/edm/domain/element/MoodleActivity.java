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
import java.util.Set;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.NamedActivity;
import ca.uoguelph.socs.icc.edm.domain.NamedActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Implementation;

/**
 * Moodle specific implementation of the core <code>Activity</code> data.
 * Rather than have the primary key of the tables containing the module
 * (<code>Activity</code>) data reference the core <code>Activity</code> table
 * (<code>ActivityInstance</code>), the Moodle database has a separate primary
 * key for each module table and stores a reference to that key in the course
 * modules table (core <code>Activity</code> data).  Such a design does not
 * allow the module (<code>Activity</code>) data to be loaded via a join to the
 * core <code>Activity</code> instance data (<code>ActivityInstance</code>).
 * To workaround the limitation imposed by the moodle database, this class
 * extends <code>ActivityInstance</code> to store the reference to the module
 * data so that the <code>MoodleActivityManager</code> can load the
 * <code>Activity</code> data in a second step.
 * <p>
 * Since the Moodle database is intended to be read-only, this class does not
 * require a builder capable of setting the <code>instanceId</code> as it
 * should never be needed.
 *
 * @author  James E. Stark
 * @version 1.1
 */

public class MoodleActivity extends NamedActivity
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key of the <code>MoodleActivity</code> */
	private Long id;

	/** The type of the <code>Activity</code> */
	private ActivityType type;

	/** The associated <code>Course</code> */
	private Course course;

	/** The associated <code>LogEntry</code> instances */
	private List<LogEntry> log;

	/** The instance data identifier */
	private Long instanceId;

	/**
	 * Static initializer to register the <code>MoodleActivity</code>
	 * class with the factories.
	 */

	static
	{
		Implementation.getInstance (NamedActivity.class, MoodleActivity.class, MoodleActivity::new);
	}

	/**
	 * Create the <code>Activity</code> with null values.
	 */

	protected MoodleActivity ()
	{
		this.id = null;
		this.type = null;
		this.course = null;
		this.instanceId = null;

		this.log = new ArrayList<LogEntry> ();
	}

	/**
	 * Get a reference to the <code>Activity</code>.
	 *
	 * @return The <code>Activity</code>
	 */

	private NamedActivity getActivity ()
	{
		if (this.instanceId == null)
		{
			throw new IllegalStateException ("instance ID is NULL");
		}

		NamedActivity activity = (NamedActivity) this.getDataStore ()
			.getQuery (Activity.class,
					Activity.getActivityClass (this.getType ()),
					Activity.SELECTOR_ID)
			.setValue (Activity.ID, this.instanceId)
			.query ();

		// Type is transient on the loaded activity, so copy it in here
		this.getDataStore ()
			.getProfile ()
			.getCreator (Activity.class, Activity.getActivityClass (this.type))
			.setValue (Activity.TYPE, activity, this.type);

		return activity;
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
		else if (obj instanceof MoodleActivity)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();

			ebuilder.appendSuper (super.equals (obj));
			ebuilder.append (this.instanceId, ((MoodleActivity) obj).getInstanceId ());

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
	 * Get an <code>ActivityBuilder</code> instance for the specified
	 * <code>DataStore</code>.  This method passes the <code>getBuilder</code>
	 * call though to the <code>Activity</code> instance containing the actual
	 * data.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The initialized <code>ActivityBuilder</code>
	 */

	@Override
	public NamedActivityBuilder getBuilder (final DataStore datastore)
	{
		assert datastore != null : "datastore is null";

		return this.getActivity ().getBuilder (datastore);
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
	 * Get the name of the <code>Activity</code>.  Not all <code>Activity</code>
	 * instances have names.  For those <code>Activity</code> instances which do
	 * not have names, the name of the associated <code>ActivityType</code> will
	 * be returned.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Activity</code>
	 */

	@Override
	public String getName ()
	{
		return this.getActivity ().getName ();
	}

	/**
	 * Set the name of the <code>Activity</code>.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Activity</code>
	 * instance is loaded.
	 *
	 * @param  name The name of the <code>Activity</code>, not null
	 */

	@Override
	protected void setName (final String name)
	{
		assert name != null : "name is NULL";

		throw new UnsupportedOperationException ("MoodleActivity is Read-only");
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
	 * Get the <code>Set</code> of <code>Grade</code> instances which are
	 * associated with the <code>Activity</code>.  Not all
	 * <code>Activity</code> instances are graded.  If the
	 * <code>Activity</code> does is not graded then the <code>Set</code> will
	 * be empty.
	 *
	 * @return A <code>Set</code> of <code>Grade</code> instances
	 */

	@Override
	public Set<Grade> getGrades ()
	{
		return this.getActivity ().getGrades ();
	}

	/**
	 * Initialize the <code>Set</code> of <code>Grade</code> instances
	 * associated with the <code>Activity</code> instance.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>Activity</code> instance is loaded.
	 *
	 * @param  grades The <code>Set</code> of <code>Grade</code> instances, not
	 *                null
	 */

	@Override
	protected void setGrades (final Set<Grade> grades)
	{
		assert grades != null : "grades is NULL";

		throw new UnsupportedOperationException ("MoodleActivity is Read-only");
	}

	/**
	 * Add the specified <code>Grade</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  grade    The <code>Grade</code> to add, not null
	 *
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully added, <code>False</code> otherwise
	 */

	@Override
	protected boolean addGrade (final Grade grade)
	{
		assert grade != null : "grade is NULL";

		throw new UnsupportedOperationException ("MoodleActivity is Read-only");
	}

	/**
	 * Remove the specified <code>Grade</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  grade    The <code>Grade</code> to remove, not null
	 *
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully removed from, <code>False</code> otherwise
	 */

	@Override
	protected boolean removeGrade (final Grade grade)
	{
		assert grade != null : "grade is NULL";

		throw new UnsupportedOperationException ("MoodleActivity is Read-only");
	}

	/**
	 * Get a <code>List</code> of all of the <code>LogEntry</code> instances
	 * which act upon the <code>Activity</code>.
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
	 * associated with the <code>Activity</code> instance.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>Activity</code> instance is loaded.
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
	 * <code>Activity</code>.
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

		throw new UnsupportedOperationException ("MoodleActivity is Read-only");
	}

	/**
	 * Remove the specified <code>LogEntry</code> from the specified
	 * <code>Activity</code>.
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

		throw new UnsupportedOperationException ("MoodleActivity is Read-only");
	}

	/**
	 * Get the <code>List</code> of <code>SubActivity</code> instances
	 * associated with the <code>Actvity</code>.
	 *
	 * @return The <code>List</code> of <code>SubActivity</code> instances
	 */

	@Override
	public List<SubActivity> getSubActivities ()
	{
		return this.getActivity ().getSubActivities ();
	}

	/**
	 * Initialize the <code>List</code> of <code>SubActivity</code> instances
	 * for the <code>Activity</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of
	 * the child class.
	 *
	 * @param  subactivities The <code>List</code> of <code>SubActivity</code>
	 *                       instances, not null
	 */

	@Override
	protected void setSubActivities (final List<SubActivity> subactivities)
	{
		assert subactivities != null : "subactivities is NULL";

		throw new UnsupportedOperationException ("MoodleActivity is Read-only");
	}

	/**
	 * Add the specified <code>SubActivity</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to add, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully added, <code>False</code> otherwise
	 */

	@Override
	protected boolean addSubActivity (final SubActivity subactivity)
	{
		assert subactivity != null : "subactivity is NULL";

		throw new UnsupportedOperationException ("MoodleActivity is Read-only");
	}

	/**
	 * Remove the specified <code>SubActivity</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to remove, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully removed, <code>False</code>
	 *                     otherwise
	 */

	@Override
	protected boolean removeSubActivity (final SubActivity subactivity)
	{
		assert subactivity != null : "subactivity is NULL";

		throw new UnsupportedOperationException ("MoodleActivity is Read-only");
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
