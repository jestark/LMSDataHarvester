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

import java.util.List;
import java.util.Set;
import java.util.Objects;

import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Generic representation of an <code>Activity</code> which has a name.  This
 * class acts as an abstract base class for all of the <code>Activity</code>
 * implementations which have a name.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class NamedActivity extends Activity
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>NamedActivity</code> */
	private static final MetaData<NamedActivity> METADATA;

	/** The <code>Grade</code> instances associated with the <code>Activity</code> */
	public static final Property<Grade> GRADES;

	/** The <code>SubActivity</code> instances for the <code>Activity</code> */
	public static final Property<SubActivity> SUBACTIVITIES;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>NamedActivity</code>.
	 */

	static
	{
		GRADES = Property.getInstance (Grade.class, "grade", Property.Flags.MULTIVALUED);
		SUBACTIVITIES = Property.getInstance (SubActivity.class, "subactivities", Property.Flags.RECOMMENDED, Property.Flags.MULTIVALUED);

		METADATA = Definition.getBuilder (NamedActivity.class, Activity.class)
			.addProperty (Activity.NAME, NamedActivity::getName, NamedActivity::setName)
			.addRelationship (GRADES, NamedActivity::getGrades, NamedActivity::addGrade, NamedActivity::removeGrade)
			.addRelationship (SUBACTIVITIES, NamedActivity::getSubActivities, NamedActivity::addSubActivity, NamedActivity::removeSubActivity)
			.build ();
	}

	/**
	 * Get an instance of the <code>NamedActivityBuilder</code> for the
	 * specified <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 * @param  type                  The <code>ActivityType</code>, not null
	 *
	 * @return                       The <code>NamedActivityBuilder</code>
	 *                               instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static NamedActivityBuilder builder (final DomainModel model, final ActivityType type)
	{
		Preconditions.checkNotNull (model, "model");

		return null;
	}

	/**
	 * Create the <code>NamedActivity</code>.
	 */

	protected NamedActivity ()
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
			.add ("name", this.getName ());
	}

	/**
	 * Compare two <code>NamedActivity</code> instances to determine if they are
	 * equal.  The <code>NamedActivity</code> instances are compared based upon
	 * their <code>ActivityType</code>, the associated <code>Course</code> and
	 * their names.
	 *
	 * @param  obj The <code>NamedActivity</code> instance to compare to the
	 *             one represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>NamedActivity</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof NamedActivity)
			&& super.equals (obj)
			&& Objects.equals (this.getName (), ((NamedActivity) obj).getName ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>NamedActivity</code>
	 * instance.  The hash code is computed based upon the
	 * <code>ActivityType</code>, the <code>Course</code> and the name of the
	 * instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (super.hashCode (), this.getName ());
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>NamedActivity</code> instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>NamedActivity</code> instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return this.toStringHelper ()
				.toString ();
	}

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Property</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Set<Property<?>> properties ()
	{
		return NamedActivity.METADATA.getProperties ();
	}

	/**
	 * Get the <code>Set</code> of <code>Selector</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Selector</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Set<Selector> selectors ()
	{
		return NamedActivity.METADATA.getSelectors ();
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
		return NamedActivity.METADATA.hasValue (property, this, value);
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
		return NamedActivity.METADATA.getStream (property, this);
	}

	/**
	 * Get an <code>NamedActivityBuilder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates a
	 * <code>NamedActivityBuilder</code> on the specified
	 * <code>DomainModel</code> and initializes it with the contents of this
	 * <code>NamedActivity</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>NamedActivityBuilder</code>
	 */

	@Override
	public NamedActivityBuilder getBuilder (final DomainModel model)
	{
		return NamedActivity.builder (Preconditions.checkNotNull (model, "model"), this.getType ())
			.load (this);
	}

	/**
	 * Set the name of the <code>Activity</code>.  This method is intended to
	 * be used to initialize a new <code>NamedActivity</code> instance.
	 *
	 * @param  name The name of the <code>Activity</code>, not null
	 */

	protected abstract void setName (String name);

	/**
	 * Initialize the <code>Set</code> of <code>Grade</code> instances
	 * associated with the <code>Activity</code> instance.  This method is
	 * intended to be used to initialize a new <code>Activity</code> instance.
	 *
	 * @param  grades The <code>Set</code> of <code>Grade</code> instances, not
	 *                null
	 */

	protected abstract void setGrades (Set<Grade> grades);

	/**
	 * Add the specified <code>Grade</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  grade    The <code>Grade</code> to add, not null
	 *
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addGrade (Grade grade);

	/**
	 * Remove the specified <code>Grade</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  grade    The <code>Grade</code> to remove, not null
	 *
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully removed from, <code>False</code> otherwise
	 */

	protected abstract boolean removeGrade (Grade grade);

	/**
	 * Initialize the <code>List</code> of <code>SubActivity</code> instances
	 * for the <code>Activity</code>.  This method is intended to be used to
	 * initialize a new <code>NamedActivity</code> instance.
	 *
	 * @param  subactivities The <code>List</code> of <code>SubActivity</code>
	 *                       instances, not null
	 */

	protected abstract void setSubActivities (List<SubActivity> subactivities);

	/**
	 * Add the specified <code>SubActivity</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to add, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addSubActivity (SubActivity subactivity);

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

	protected abstract boolean removeSubActivity (SubActivity subactivity);
}
