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

import java.util.Map;
import java.util.List;
import java.util.Set;

import java.util.HashMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.MemDataStore;

import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.SequentialIdGenerator;

import ca.uoguelph.socs.icc.edm.domain.element.ActivitySourceData;
import ca.uoguelph.socs.icc.edm.domain.element.ActivityTypeData;
import ca.uoguelph.socs.icc.edm.domain.element.GenericActivity;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Profile;
import ca.uoguelph.socs.icc.edm.domain.metadata.ProfileBuilder;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of the content of a <code>Course</code>.  All of the
 * content in a <code>Course</code> is represented by a <code>Set</code> of
 * <code>Activity</code> instances.  <code>Activity</code> instances will exist
 * for assignments, quizzes, forums and lessons among many other things.  Some
 * of these will be graded and some will not be graded.  Graded
 * <code>Activity</code> instances will have <code>Grade</code> instances
 * associated with them.
 * <p>
 * Since the <code>Activity</code> interface represents all of the content of a
 * <code>Course</code>, its implementation can be complex.  Each instance of
 * the <code>Activity</code> interface must contain the
 * <code>ActviityType</code>, the associated <code>Course</code>,
 * <code>Grade</code>, and <code>LogEntry</code> instances.  Depending on the
 * <code>ActivityType</code> an <code>Activity</code> instance can contain
 * additional data including, but not limited to, <code>SubActivity</code>
 * instances.  All </code>SubActivity</code> instances must implement the
 * <code>Activity</code> interface, but may return local data where necessary.
 * <p>
 * Instances of the <code>Activity</code> interface have strong dependencies on
 * the associated instances of the <code>ActivityType</code> and
 * <code>Course</code> interfaces.  If an instance of one of these interfaces
 * is deleted, then the associated instance of the <code>Activity</code>
 * interface and its dependants must me deleted as well.  Similarly, instances
 * of the <code>Grade</code> and <code>LogEntry</code> interfaces, along with
 * any <code>SubActivity</code> instances have a strong dependency on the
 * associated instance of the <code>Activity</code> interface, and must be
 * deleted along with the instance of the <code>Activity</code> interface.
 * <p>
 * Once created, with the exception of adding and removing <code>Grade</code>
 * and <code>LogEntry</code> instances (and sub-activities if they exist),
 * <code>Activity</code> instances are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityBuilder
 * @see     ActivityLoader
 */

public abstract class Activity extends ParentActivity
{
	/** Internal <code>DomainModel</code> for sources and types */
	private static DomainModel store;

	/** <code>ActivityType</code> to implementation class map */
	private static final Map<ActivityType, Class<? extends Activity>> activities;

	/** The associated <code>Course</code> */
	public static final Property<Course> COURSE;

	/** The associated <code>ActivityType</code> */
	public static final Property<ActivityType> TYPE;

	/** The name of the <code>Activity</code> */
	public static final Property<String> NAME;

	/** The <code>LogEntry</code> instances associated with the <code>Activity</code> */
	public static final Property<LogEntry> LOGENTRIES;

	/** Select all <code>Activity</code> instances by <code>ActivityType</code> */
	public static final Selector SELECTOR_TYPE;

	/** The primary key for the <code>Activity</code> */
	protected Long id;

	/** The associated <code>Course</code> */
	protected Course course;

	/** The type of the <code>Activity</code> */
	protected ActivityType type;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Activity</code>.
	 */

	static
	{
		activities = new HashMap<> ();

		COURSE = Property.getInstance (Course.class, "course", Property.Flags.REQUIRED);
		TYPE = Property.getInstance (ActivityType.class, "type", Property.Flags.REQUIRED);
		NAME = Property.getInstance (String.class, "name", Property.Flags.REQUIRED);

		LOGENTRIES = Property.getInstance (LogEntry.class, "logentries", Property.Flags.MULTIVALUED);

		SELECTOR_TYPE = Selector.getInstance (TYPE, false);

		Definition.getBuilder (Activity.class, Element.class)
			.addProperty (NAME, Activity::getName)
			.addRelationship (COURSE, Activity::getCourse, Activity::setCourse)
			.addRelationship (TYPE, Activity::getType, Activity::setType)
			.addRelationship (LOGENTRIES, Activity::getLog, Activity::addLog, Activity::removeLog)
			.addSelector (SELECTOR_TYPE)
			.build ();
	}

	/**
	 * Determine if an <code>Activity</code> implementation class has been
	 * registered for the specified <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 *
	 * @return      <code>true</code> if an <code>Activity</code>
	 *              implementation class has been registered for the specified
	 *              <code>ActivityType</code>, <code>false</code> otherwise
	 */

	public static final boolean hasActivityClass (final ActivityType type)
	{
		assert type != null : "type is NULL";

		return Activity.activities.containsKey (type);
	}

	/**
	 * Get the <code>Activity</code> implementation class which is associated
	 * with the specified <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>
	 *
	 * @return      The <code>Activity </code> data class for the given
	 *              <code>ActivityType</code>, may be null
	 */

	public static final Class<? extends Activity> getActivityClass (final ActivityType type)
	{
		assert type != null : "type is NULL";

		return (Activity.activities.containsKey (type)) ? Activity.activities.get (type) : GenericActivity.class;
	}

	/**
	 * Register an association between an <code>ActivityType</code> and the
	 * class implementing the <code>Activity</code> interface for that
	 * <code>ActivityType</code>.
	 *
	 * @param  source A <code>String</code> representation of the
	 *                <code>ActivitySource</code>, not null
	 * @param  type   A <code>String</code> representation of the
	 *                <code>ActivityType</code>, not null
	 * @param  impl   The implementation class, not null
	 */

	protected static final void registerImplementation (final String source, final String type, final Class<? extends Activity> impl)
	{
		assert source != null : "source is NULL";
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert source.length () > 0 : "source is empty";

		if (Activity.store == null)
		{
			Profile activityProf = new ProfileBuilder ()
				.setName ("Activity")
				.setMutable (true)
				.setElementClass (ActivitySource.class, ActivitySourceData.class)
				.setElementClass (ActivityType.class, ActivityTypeData.class)
				.setGenerator (Element.class, SequentialIdGenerator.class)
				.build ();

			Activity.store = new DomainModel (DataStore.getInstance (MemDataStore.class, activityProf));
		}

		Activity.store.getTransaction ().begin ();

		ActivityType atype = ActivityType.builder (Activity.store)
			.setActivitySource (ActivitySource.builder (Activity.store)
					.setName (source)
					.build ())
			.setName (type)
			.build ();

		Activity.store.getTransaction ().commit ();

		assert ! Activity.activities.containsKey (atype) : "Implementation class already registered for ActivityType";

		Activity.activities.put (atype, impl);
	}

	/**
	 * Get an instance of the <code>ActivityBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 * @param  type                  The <code>ActivityType</code>, not null
	 *
	 * @return                       The <code>ActivityBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Activity</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is
	 *                               immutable
	 */

	public static ActivityBuilder builder (final DataStore datastore, final ActivityType type)
	{
		assert datastore != null : "datastore is NULL";
		assert type != null : "type is NULL";

		return new ActivityBuilder (datastore, type);
	}

	/**
	 * Get an instance of the <code>ActivityBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 * @param  type                  The <code>ActivityType</code>, not null
	 *
	 * @return                       The <code>ActivityBuilder</code> instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Activity</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static ActivityBuilder builder (final DomainModel model, final ActivityType type)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		if (type == null)
		{
			throw new NullPointerException ("type is NULL");
		}

		return Activity.builder (model.getDataStore (), type);
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
		else if (obj instanceof Activity)
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
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The initialized <code>ActivityBuilder</code>
	 */

	@Override
	public ActivityBuilder getBuilder (final DataStore datastore)
	{
		assert datastore != null : "datastore is null";

		return Activity.builder (datastore, this.getType ())
			.load (this);
	}

	/**
	 * Get the <code>MetaData</code> instance for this <code>Activity</code>
	 * using the specified <code>DataStore</code>.
	 *
	 * @return The <code>MetaData</code>
	 */

	@Override
	protected MetaData<Activity> metadata ()
	{
		return this.getDomainModel ()
			.getDataStore ()
			.getProfile ()
			.getCreator (Activity.class, this.getClass ());
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Activity</code>
	 * instance.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of
	 * the child class.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

	@Override
	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Activity</code>
	 * instance is loaded, or by the <code>ActivityBuilder</code>
	 * implementation to set the <code>DataStore</code> identifier, prior to
	 * storing a new <code>Activity</code> instance.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of
	 * the child class.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final Long id)
	{
		assert id != null : "id is NULL";

		this.id = id;
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
	 * associated.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  course The <code>Course</code>, not null
	 */

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
	 * associated.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 */

	protected void setType (final ActivityType type)
	{
		assert type != null : "type is NULL";

		this.type = type;
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

	protected abstract void setLog (List<LogEntry> log);

	/**
	 * Add the specified <code>LogEntry</code> to the specified
	 * <code>Activity</code>.
	 *
	 * @param  entry    The <code>LogEntry</code> to add, not null
	 *
	 * @return          <code>True</code> if the <code>LogEntry</code> was
	 *                  successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addLog (LogEntry entry);

	/**
	 * Remove the specified <code>LogEntry</code> from the specified
	 * <code>Activity</code>.
	 *
	 * @param  entry    The <code>LogEntry</code> to remove, not null
	 *
	 * @return          <code>True</code> if the <code>LogEntry</code> was
	 *                  successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeLog (LogEntry entry);
}
