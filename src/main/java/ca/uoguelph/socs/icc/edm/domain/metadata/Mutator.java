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

package ca.uoguelph.socs.icc.edm.domain.metadata;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Representation of a component of an <code>Element</code>.  An
 * <code>Element</code> can be modelled as a collection of properties.  This
 * Class represents a <code>Property</code>, containing its name and type.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> type of the <code>Mutator</code>
 * @param   <V> The type of the value for the <code>Mutator</code>
 */

public final class Mutator<T extends Element, V>
{
	/** The <code>Element</code> interface class */
	private final Property<T, V> property;

	/**
	 * Create the <code>Mutator</code>.
	 *
	 * @param  property The <code>Property</code>, not null
	 */

	protected Mutator (final Property<T, V> property)
	{
		assert property != null : "property is NULL";

		this.property = property;
	}

	/**
	 * Compare two <code>Mutator</code> instances to determine if they are
	 * equal.
	 *
	 * @param  mutator The <code>Mutator</code> instance to compare to the
	 *                 one represented by the called instance
	 *
	 * @return         <code>True</code> if the two <code>Mutator</code>
	 *                 instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object mutator)
	{
		return (mutator == this) ? true : (mutator instanceof Mutator)
			&& Objects.equals (this.property, ((Mutator) mutator).property);

	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Mutator</code> instance.
	 *
	 * @return An integer containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.property);
	}

	/**
	 * Get a <code>String</code> representation of the <code>Mutator</code>
	 * instance.
	 *
	 * @return A <code>String</code> representation of the <code>Mutator</code>
	 *         instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("property", this.property)
			.toString ();
	}

	/**
	 * Determine if the specified flag is set for the underlying
	 * <code>Property</code>.
	 *
	 * @param  flag The flag to test
	 *
	 * @return      <code>true</code> if the specified flag is set,
	 *              <code>false</code> otherwise
	 */

	public boolean hasFlags (final Property.Flags flag)
	{
		return this.property.hasFlags (flag);
	}

	/**
	 * Determine if the specified flags are set for the underlying
	 * <code>Property</code>.
	 *
	 * @param  flags The flags to test
	 *
	 * @return      <code>true</code> if all of the specified flags are set,
	 *              <code>false</code> otherwise
	 */

	public boolean hasFlags (final Property.Flags... flags)
	{
		return this.property.hasFlags (flags);
	}

	/**
	 * Determine if the specified flags are set for the underlying
	 * <code>Property</code>.
	 *
	 * @param  flags The <code>Set</code> of flags to test
	 *
	 * @return      <code>true</code> if all of the specified flags are set,
	 *              <code>false</code> otherwise
	 */

	public boolean hasFlags (final Set<Property.Flags> flags)
	{
		return this.property.hasFlags (flags);
	}

	/**
	 * Get the <code>Set</code> of <code>Flags</code> for the underlying
	 * <code>Property</code>.
	 *
	 * @return A <code>Set</code> containing the <code>Flags</code>
	 */

	public Set<Property.Flags> getFlags ()
	{
		return this.property.getFlags ();
	}

	/**
	 * Get the name of the underlying <code>Property</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Property</code>
	 */

	public String getName ()
	{
		return this.property.getName ();
	}

	/**
	 * Get the <code>Element</code> interface class for the underlying
	 * <code>Property</code>.
	 *
	 * @return The <code>Element</code> interface class
	 */

	public Class<T> getElementClass ()
	{
		return this.property.getElementClass ();
	}

	/**
	 * Get the Java type for the value represented by the underlying
	 * <code>Property</code>.
	 *
	 * @return The <code>Class</code> representing the type of the
	 *         <code>Property</code>
	 */

	public Class<V> getValueClass ()
	{
		return this.property.getValueClass ();
	}

	/**
	 * Determine if the value contained in the <code>Element</code> represented
	 * by the underlying <code>Property</code> has the specified value.  If the
	 * <code>Property</code> represents a single value, then this method will be
	 * equivalent to calling the <code>equals</code> method on the value
	 * represented by the <code>Property</code>.  This method is equivalent to
	 * calling the <code>contains</code> method for <code>Property</code>
	 * instances that represent collections.
	 *
	 * @param  element The <code>Element</code> instance, not null
	 * @param  value   The value to be tested against the <code>Element</code>
	 *
	 * @return <code>true</code> if the value represented by the
	 *         <code>Property</code> equals/contains the specified value,
	 *         <code>false</code> otherwise.
	 */

	public boolean hasValue (final T element, final @Nullable V value)
	{
		return this.property.hasValue (element, value);
	}

	/**
	 * Get a <code>Stream</code> containing all of the values in the specified
	 * <code>Element</code> instance which are represented by the underlying
	 * <code>Property</code>.  This method will return a <code>Stream</code>
	 * containing zero or more values.  For a single-valued
	 * <code>Property</code>, the returned <code>Stream</code> will contain
	 * exactly zero or one values.  An empty <code>Stream</code> will be
	 * returned if the associated value is null.  A <code>Stream</code>
	 * containing all of the values in the associated collection will be
	 * returned for multi-valued <code>Property</code> instances.
	 *
	 * @param  element  The <code>Element</code>, not null
	 *
	 * @return          The <code>Stream</code>
	 */

	public Stream<V> stream (final T element)
	{
		return this.property.stream (element);
	}

	/**
	 * Add the specified value to the <code>Element</code>.  For a single valued
	 * <code>Property</code>, this method will replace the existing value with
	 * the specified value.  The specified value will be added to the associated
	 * collection for multi-valued <code>Property</code> instances.
	 * <p>
	 * Note: for this method to set the value, the <code>Property</code>
	 * instance must not have REQUIRED flag, or the value must be
	 * <code>null</code>.
	 *
	 * @param  element The <code>Element</code> instance, not null
	 * @param  value   The value to be written into the <code>Element</code>,
	 *                 not null
	 *
	 * @return         <code>true</code> if the value was written into the
	 *                 <code>Element</code>, <code>false</code> otherwise
	 */

	public boolean setValue (final T element, final V value)
	{
		Preconditions.checkNotNull (element, "element");
		Preconditions.checkNotNull (value, "value");

		return this.property.setValue (element, value);
	}

	/**
	 * Remove the specified value from the specified <code>Element</code>
	 * instance.  For a single valued <code>Property</code> this method will
	 * set the corresponding value to <code>null</code> if it is currently equal
	 * to the specified value.  In the case of a multi-valued
	 * <code>Property</code>, the specified value will be removed from the
	 * associated <code>Collection</code>.
	 * <p>
	 * Note that this method will not clear the value if the
	 * <code>Property</code> instance has the REQUIRED flag.
	 *
	 * @param  element The <code>Element</code> instance, not null
	 * @param  value   The value to be removed from the <code>Element</code>,
	 *                 not null
	 *
	 * @return         <code>true</code> if the value was removed from the
	 *                 <code>Element</code>, <code>false</code> otherwise
	 */

	public boolean clearValue (final T element, final V value)
	{
		Preconditions.checkNotNull (element, "element");
		Preconditions.checkNotNull (value, "value");

		return this.property.clearValue (element, value);
	}
}
