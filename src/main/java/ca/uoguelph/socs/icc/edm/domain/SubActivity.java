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
import java.util.Map;
import java.util.Set;

import java.util.HashMap;
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
 * A representation of a child element in a hierarchy of <code>Activity</code>
 * instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class SubActivity extends ParentActivity
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** Parent to Child <code>SubActivity</code> class mapping*/
	private static final Map<Class<? extends ParentActivity>, Class<? extends SubActivity>> subactivities;

	/** The <code>MetaData</code> for the <code>SubActivity</code> */
	protected static final MetaData<SubActivity> METADATA;

	/** The name of the <code>SubActivity</code> */
	public static final Property<String> NAME;

	/** The parent <code>Activity</code> */
	public static final Property<ParentActivity> PARENT;

	/** The <code>LogEntry</code> instances associated with the <code>SubActivity</code> */
	public static final Property<LogReference> REFERENCES;

	/** The <code>SubActivity</code> instances for the <code>SubActivity</code> */
	public static final Property<SubActivity> SUBACTIVITIES;

	/** Select the <code>SubActivity</code> instance by its id */
	public static final Selector<SubActivity> SELECTOR_ID;

	/** Select all of the <code>SubActivity</code> instances */
	public static final Selector<SubActivity> SELECTOR_ALL;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>SubActivity</code>.
	 */

	static
	{
		subactivities = new HashMap<> ();

		NAME = Property.of (String.class, "name", Property.Flags.REQUIRED);
		PARENT = Property.of (ParentActivity.class, "parent", Property.Flags.REQUIRED);

		REFERENCES = Property.of (LogReference.class, "references", Property.Flags.MULTIVALUED);
		SUBACTIVITIES = Property.of (SubActivity.class, "subactivities", Property.Flags.MULTIVALUED);

		SELECTOR_ID = Selector.of (SubActivity.class, Selector.Cardinality.KEY, ID);
		SELECTOR_ALL = Selector.of (SubActivity.class, Selector.Cardinality.MULTIPLE, "all");

		METADATA = MetaData.builder (SubActivity.class, Element.METADATA)
			.addProperty (NAME, SubActivity::getName, SubActivity::setName)
			.addProperty (PARENT, SubActivity::getParent, SubActivity::setParent)
			.addProperty (REFERENCES, SubActivity::getReferences, SubActivity::addReference, SubActivity::removeReference)
			.addProperty (SUBACTIVITIES, SubActivity::getSubActivities, SubActivity::addSubActivity, SubActivity::removeSubActivity)
//			.addRelationship ()
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.build ();
	}

	/**
	 * Register an association between an <code>Activity</code> implementation
	 * class and a <code>SubActivity</code> implementation class.
	 *
	 * @param  activity    The <code>Activity</code> implementation, not null
	 * @param  subactivity The <code>SubActivity</code> implementation, not null
	 */

	protected static final <T extends SubActivity> void registerImplementation (final Class<? extends ParentActivity> activity, final Class<T> subactivity)
	{
		assert activity != null : "activity is NULL";
		assert subactivity != null : "subactivity is NULL";
		assert (! SubActivity.subactivities.containsKey (activity)) : "activity is already registered";

		SubActivity.subactivities.put (activity, subactivity);
	}

	/**
	 * Get the <code>SubActivity</code> implementation class which is
	 * associated with the specified <code>Activity</code> implementation
	 * class.
	 *
	 * @param  activity The <code>Activity</code> implementation class
	 *
	 * @return          The <code>SubActivity</code> implementation class, may be
	 *                  null
	 */

	public static final Class<? extends SubActivity> getSubActivityClass (final Class<? extends ParentActivity> activity)
	{
		assert activity != null : "activity is NULL";
		assert SubActivity.subactivities.containsKey (activity) : "Activity is not registered";

		return SubActivity.subactivities.get (activity);
	}

	/**
	 * Get an instance of the <code>SubActivityBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 * @param  parent                The parent <code>Activity</code>, not null
	 *
	 * @return                       The <code>SubActivityBuilder</code> instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static SubActivityBuilder builder (final DomainModel model, final ParentActivity parent)
	{
		Preconditions.checkNotNull (model, "model");
		Preconditions.checkNotNull (parent, "parent");

		return null;
	}

	/**
	 * Create the <code>SubActivity</code>.
	 */

	protected SubActivity ()
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
			.add ("parent", this.getParent ())
			.add ("name", this.getName ());
	}

	/**
	 * Compare two <code>SubActivity</code> instances to determine if they are
	 * equal.  The <code>SubActivity</code> instances are compared based upon
	 * their names and the parent <code>Activity</code>.
	 *
	 * @param  obj The <code>SubActivity</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>SubActivity</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof SubActivity)
			&& Objects.equals (this.getName (), ((SubActivity) obj).getName ())
			&& Objects.equals (this.getParent (), ((SubActivity) obj).getParent ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>SubActivity</code>
	 * instance.  The hash code is computed based upon the parent
	 * <code>Activity</code> and the name of the <code>SubActivity</code>
	 * instance.
	 *
	 * @return The hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getName (), this.getParent ());
	}

	/**
	 * Get a <code>String</code> representation of the <code>SubActivity</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>SubActivity</code> instance
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
	public Stream<Property<?>> properties ()
	{
		return SubActivity.METADATA.properties ();
	}

	/**
	 * Get the <code>Set</code> of <code>Selector</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Selector</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Selector<? extends Element>> selectors ()
	{
		return SubActivity.METADATA.selectors ();
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
		return SubActivity.METADATA.getReference (property)
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
		return SubActivity.METADATA.getReference (property)
			.stream (this);
	}

	/**
	 * Get an <code>SubActivityBuilder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an
	 * <code>SubActivityBuilder</code> on the specified <code>DomainModel</code>
	 * and initializes it with the contents of this <code>SubActivity</code>
	 * instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>SubActivityBuilder</code>
	 */

	@Override
	public SubActivityBuilder getBuilder (final DomainModel model)
	{
		return SubActivity.builder (model, this.getParent ())
			.load (this);
	}

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	@Override
	public final ActivityType getType()
	{
		return this.getParent ().getType ();
	}

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @return The <code>Course</code> instance
	 */

	@Override
	public final Course getCourse ()
	{
		return this.getParent ().getCourse ();
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
	public final Set<Grade> getGrades ()
	{
		return this.getParent ().getGrades ();
	}

	/**
	 * Get the parent <code>Activity</code> instance for the
	 * <code>SubActivity</code>.
	 *
	 * @return The parent <code>Activity</code>
	 */

	public abstract ParentActivity getParent ();

	/**
	 * Set the <code>Activity</code> instance which contains the
	 * <code>SubActivity</code>.  This method is intended to be used to
	 * initialize a new <code>SubActivity</code> instance.
	 *
	 * @param  activity The <code>Activity</code> containing this
	 *                  <code>SubActivity</code> instance
	 */

	protected abstract void setParent (ParentActivity activity);

	/**
	 * Set the name of the <code>Activity</code>.  This method is intended to
	 * be used to initialize a new <code>SubActivity</code> instance.
	 *
	 * @param  name The name of the <code>SubActivity</code>, not null
	 */

	protected abstract void setName (String name);

	/**
	 * Get a <code>List</code> of all of the <code>LogReference</code>
	 * instances for the <code>SubActivity</code>.
	 *
	 * @return A <code>List</code> of <code>LogReference</code> instances
	 */

	public abstract List<LogReference> getReferences ();

	/**
	 * Initialize the <code>List</code> of <code>LogReference</code> instances
	 * associated with the <code>SubActivity</code> instance.  This method is
	 * intended to be used to initialize a new  <code>SubActivity</code>
	 * instance.
	 *
	 * @param  references The <code>List</code> of <code>LogReference</code>
	 *                    instances, not null
	 */

	protected abstract void setReferences (List<LogReference> references);

	/**
	 * Add the specified <code>LogReference</code> to the
	 * <code>SubActivity</code>.
	 *
	 * @param  reference    The <code>LogReference</code> to add, not null
	 *
	 * @return              <code>True</code> if the <code>LogReference</code>
	 *                      was successfully added, <code>False</code>
	 *                      otherwise
	 */

	protected abstract boolean addReference (LogReference reference);

	/**
	 * Remove the specified <code>LogReference</code> from the
	 * <code>SubActivity</code>.
	 *
	 * @param  reference    The <code>LogReference</code> to remove, not null
	 *
	 * @return              <code>True</code> if the <code>LogReference</code>
	 *                      was successfully removed, <code>False</code>
	 *                      otherwise
	 */

	protected abstract boolean removeReference (LogReference reference);

	/**
	 * Initialize the <code>List</code> of <code>SubActivity</code> instances
	 * for the <code>SubActivity</code>.  This method is intended to be used to
	 * initialize a new <code>SubActivity</code> instance.
	 *
	 * @param  subactivities The <code>List</code> of <code>SubActivity</code>
	 *                       instances, not null
	 */

	protected abstract void setSubActivities (List<SubActivity> subactivities);

	/**
	 * Add the specified <code>SubActivity</code> to the
	 * <code>SubActivity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to add, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addSubActivity (SubActivity subactivity);

	/**
	 * Remove the specified <code>SubActivity</code> from the
	 * <code>SubActivity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to remove, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully removed, <code>False</code>
	 *                     otherwise
	 */

	protected abstract boolean removeSubActivity (SubActivity subactivity);
}
