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
import java.util.Objects;

import java.util.function.Supplier;

import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

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
	/** The <code>MetaData</code> for the <code>ActivitySource</code> */
	protected static final MetaData<ActivitySource> METADATA;

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
		NAME = Property.getInstance (String.class, "name", Property.Flags.REQUIRED);
		TYPES = Property.getInstance (ActivityType.class, "types", Property.Flags.MULTIVALUED);

		SELECTOR_NAME = Selector.getInstance (NAME, true);

		METADATA = MetaData.builder (Element.METADATA)
			.addProperty (NAME, ActivitySource::getName, ActivitySource::setName)
			.addRelationship (TYPES, ActivitySource::getTypes, ActivitySource::addType, ActivitySource::removeType)
			.addSelector (SELECTOR_NAME)
			.build ();
	}

	/**
	 * Register an implementation.  This method handles the registration of an
	 * implementation class such that instances of it can be returned a
	 * <code>Builder</code> or a <code>Query</code>.
	 *
	 * @param  <T>      The implementation type
	 * @param  impl     The Implementation <code>Class</code>, not null
	 * @param  supplier Method reference to create a new instance, not null
	 */

	protected static <T extends ActivitySource> void registerImplementation (final Class<T> impl, final Supplier<T> supplier)
	{

	}

	/**
	 * Get an instance of the <code>ActivitySourceBuilder</code> for the
	 * specified <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>ActivitySourceBuilder</code>
	 *                               instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>ActivitySource</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static ActivitySourceBuilder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return null;
	}

	/**
	 * Create the <code>ActivitySource</code>.
	 */

	protected ActivitySource ()
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
		return (obj == this) ? true : (obj instanceof ActivitySource)
			&& Objects.equals (this.getName (), ((ActivitySource) obj).getName ());
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
		return Objects.hashCode (this.getName ());
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>ActivitySource</code> instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>ActivitySource</code> instance
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
		return ActivitySource.METADATA.getProperties ();
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
		return ActivitySource.METADATA.getSelectors ();
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
		return ActivitySource.METADATA.hasValue (property, this, value);
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
		return ActivitySource.METADATA.getStream (property, this);
	}

	/**
	 * Get an <code>ActivitySourceBuilder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an
	 * <code>ActivitySourceBuilder</code> on the specified
	 * <code>DomainModel</code> and initializes it with the contents of this
	 * <code>ActivitySource</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>ActivitySourceBuilder</code>
	 */

	@Override
	public ActivitySourceBuilder getBuilder (final DomainModel model)
	{
		return ActivitySource.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
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
	 * intended to be used to initialize a new <code>ActivitySource</code>
	 * instance.
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
	 * instances.  This method is intended to be used to initialize a new
	 * <code>ActivitySource</code> instance.
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
