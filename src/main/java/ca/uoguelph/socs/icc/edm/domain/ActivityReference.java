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

import java.io.Serializable;

import java.util.List;
import java.util.Objects;

import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

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
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>ActivityReference</code> */
	protected static final MetaData<ActivityReference> METADATA;

	/** The associated <code>Activity</code> */
	public static final Property<Activity> ACTIVITY;

	/** The associated <code>Course</code> */
	public static final Property<Course> COURSE;

	/** The associated <code>ActivityType</code> */
	public static final Property<ActivityType> TYPE;

	/** The <code>LogEntry</code> instances associated with the <code>Activity</code> */
	public static final Property<LogEntry> LOGENTRIES;

	/** Select all <code>Activity</code> instances by <code>ActivityType</code> */
	public static final Selector SELECTOR_TYPE;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Activity</code>.
	 */

	static
	{
		ACTIVITY = Property.getInstance (Activity.class, "activity", Property.Flags.RECOMMENDED);
		COURSE = Property.getInstance (Course.class, "course", Property.Flags.REQUIRED);
		TYPE = Property.getInstance (ActivityType.class, "type", Property.Flags.REQUIRED);

		LOGENTRIES = Property.getInstance (LogEntry.class, "logentries", Property.Flags.MULTIVALUED);

		SELECTOR_TYPE = Selector.getInstance (TYPE, false);

		METADATA = MetaData.builder (ActivityReference.class, Element.METADATA)
			.addRelationship (ACTIVITY, ActivityReference::getActivity, ActivityReference::setActivity)
			.addRelationship (COURSE, ActivityReference::getCourse, ActivityReference::setCourse)
			.addRelationship (TYPE, ActivityReference::getType, ActivityReference::setType)
			.addRelationship (LOGENTRIES, ActivityReference::getLog, ActivityReference::addLog, ActivityReference::removeLog)
			.addSelector (SELECTOR_TYPE)
			.build ();
	}

	/**
	 * Create the <code>ActivityReference</code>.
	 */

	protected ActivityReference ()
	{
		super ();
	}

	/**
	 * Template method to create and initialize a <code>ToStringHelper</code>.
	 *
	 * @return The <code>ToStringHelper</code>
	 */

	@Override
	@CheckReturnValue
	protected MoreObjects.ToStringHelper toStringHelper ()
	{
		return super.toStringHelper ()
			.add ("type", this.getType ())
			.add ("course", this.getCourse ());
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
		return (obj == this) ? true : (obj instanceof ActivityReference)
			&& Objects.equals (this.getType (), ((ActivityReference) obj).getType ())
			&& Objects.equals (this.getCourse (), ((ActivityReference) obj).getCourse ());
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
		return Objects.hash (this.getType (), this.getCourse ());
	}

	/**
	 * Get a <code>String</code> representation of the <code>Activity</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>Activity</code> instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return this.toStringHelper ()
			.toString ();
	}

	/**
	 * Get an <code>ActivityReferenceBuilder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates a
	 * <code>ActivityReferenceBuilder</code> on the specified
	 * <code>DomainModel</code> and initializes it with the contents of this
	 * <code>Activity</code> instance.
	 * <p>
	 * <code>ActivityReference</code> instances are created though the builder
	 * for the associated <code>Activity</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return           The initialized <code>ActivityBuilder</code>
	 */

	@Override
	public ActivityReferenceBuilder getBuilder (final DomainModel model)
	{
		return null; // new ActivityReferenceBuilder (Preconditions.checkNotNull (model, "model"))
//			.load (this);
	}

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Property</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Property<?>> properties ()
	{
		return ActivityReference.METADATA.properties ();
	}

	/**
	 * Get the <code>Set</code> of <code>Selector</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Selector</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Selector> selectors ()
	{
		return ActivityReference.METADATA.selectors ();
	}

	/**
	 * Determine if the value contained in the <code>Element</code> represented
	 * by the specified <code>Property</code> has the specified value.  If the
	 * <code>Property</code> represents a singe value, then this method will be
	 * equivalent to calling the <code>equals</code> method on the value
	 * represented by the <code>Property</code>.  This method is equivalent to
	 * calling the <code>contains</code> method for <code>Property</code>
	 * instances that represent collections.
	 *
	 * @return <code>true</code> if the value represented by the
	 *         <code>Property</code> equals/contains the specified value,
	 *         <code>false</code> otherwise.
	 */

	@Override
	public <V> boolean hasValue (final Property<V> property, final V value)
	{
		return ActivityReference.METADATA.getReference (property)
			.hasValue (this, value);
	}

	/**
	 * Get a <code>Stream</code> containing all of the values in this
	 * <code>Element</code> instance which are represented by the specified
	 * <code>Property</code>.  This method will return a <code>Stream</code>
	 * containing zero or more values.  For a single-valued
	 * <code>Property</code>, the returned <code>Stream</code> will contain
	 * exactly zero or one values.  An empty <code>Stream</code> will be
	 * returned if the associated value is null.  A <code>Stream</code>
	 * containing all of the values in the associated collection will be
	 * returned for multi-valued <code>Property</code> instances.
	 *
	 * @param  <V>      The type of the values in the <code>Stream</code>
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The <code>Stream</code>
	 */

	@Override
	public <V> Stream<V> stream (final Property<V> property)
	{
		return ActivityReference.METADATA.getReference (property)
			.stream (this);
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
	 * associated.  This method is intended to be used to initialize a new
	 * <code>ActivityReference</code>.
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
	 * associated.  This method is intended to be used to initialize a new
	 * <code>ActivityReference</code>.
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

	/**
	 * Get a <code>List</code> of all of the <code>LogEntry</code> instances
	 * which act upon the <code>ActivityReference</code>.
	 *
	 * @return A <code>List</code> of <code>LogEntry</code> instances
	 */

	public abstract List<LogEntry> getLog ();

	/**
	 * Initialize the <code>List</code> of <code>LogEntry</code> instances
	 * associated with the <code>ActivityReference</code> instance.  This
	 * method is intended to be used to initialize a new
	 * <code>ActivityReference</code>.
	 *
	 * @param  log The <code>List</code> of <code>LogEntry</code> instances,
	 *             not null
	 */

	protected abstract void setLog (List<LogEntry> log);

	/**
	 * Add the specified <code>LogEntry</code> to the specified
	 * <code>ActivityReference</code>.
	 *
	 * @param  entry    The <code>LogEntry</code> to add, not null
	 *
	 * @return          <code>True</code> if the <code>LogEntry</code> was
	 *                  successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addLog (LogEntry entry);

	/**
	 * Remove the specified <code>LogEntry</code> from the specified
	 * <code>ActivityReference</code>.
	 *
	 * @param  entry    The <code>LogEntry</code> to remove, not null
	 *
	 * @return          <code>True</code> if the <code>LogEntry</code> was
	 *                  successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeLog (LogEntry entry);
}
