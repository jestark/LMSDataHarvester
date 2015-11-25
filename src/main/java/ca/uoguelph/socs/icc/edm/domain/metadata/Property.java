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
 * @param   <T> The <code>Element</code> type of the <code>Property</code>
 * @param   <V> The type of the value for the <code>Property</code>
 */

public final class Property<T extends Element, V>
{
	/**
	 * Meta-data for the <code>Property</code> instance.  The four flags
	 * contained in this Enum, along with the type, are used to describe the
	 * value represented by the <code>Property</code>. A <code>Property</code>
	 * which has a type that is a subclass of <code>Element</code> is
	 * considered to represent a relationship, with the relationship acting as
	 * a pseudo-flag.
	 * <p>
	 * The flags contained in this Enum, along with the relationship may be
	 * used with the following constraints:
	 * <ol>
	 *  <li> A <code>Property</code> that represents a relationship can not be
	 *       <code>REQUIRED</code> and <code>MUTABLE</code>.
	 *  <li> To be <code>RECOMMENDED</code> the <code>Property</code> must
	 *       represent a relationship and must not be <code>REQUIRED</code> or
	 *       <code>MUTABLE</code>
	 * </ol>
	 */

	public static enum Flags
	{
		/**
		 * Indicates that the value associated with the <code>Property</code>
		 * represents a relationship with another <code>Element</code>.
		 */

		RELATIONSHIP,

		/**
		 * Indicates that the value associated with <code>Property</code> must
		 * not be null.  For relationships, required <code>Property</code>
		 * instances represent dependencies.
		 */

		REQUIRED,

		/**
		 * Indicates that the <code>Property</code> represents a relationship
		 * that is not <code>REQUIRED</code>, but is a part of the definition
		 * of the <code>Element</code>
		 */

		RECOMMENDED,

		/**
		 * Indicates that the <code>Builder</code> may update the value(s)
		 * associated with the <code>Property</code>.   For relationships,
		 * mutable <code>Property</code> instances are treated as dependencies.
		 */

		MUTABLE;
	}

	/** The <code>Element</code> interface class */
	private final Class<T> element;

	/** The value */
	private final Class<V> value;

	/** The name of the <code>Property</code> */
	private final String name;

	/** The <code>Reference</code> for operating on the <code>Element</code> */
	private final Reference<T, V> reference;

	/** The flags for the <code>Property</code> */
	private final Set<Flags> flags;

	/**
	 * Create a Read-Only <code>Property</code>.
	 *
	 * @param  element The <code>Element</code> interface class, not null
	 * @param  value   The value class, not null
	 * @param  name    The name of the <code>Property</code>, not null
	 * @param  get     Method reference to retrieve the value, not null
	 *
	 * @return         The <code>Property</code>
	 */

	public static <T extends Element, V> Property<T, V> of (
			final Class<T> element,
			final Class<V> value,
			final String name,
			final Function<T, V> get)
	{
		Preconditions.checkNotNull (element, "element");
		Preconditions.checkNotNull (value, "value");
		Preconditions.checkNotNull (name, "name");
		Preconditions.checkNotNull (get, "get");
		Preconditions.checkArgument (name.length () > 0, "name can not be empty");

		Set<Flags> nflags = (Element.class.isAssignableFrom (value))
			? EnumSet.of (Flags.RELATIONSHIP)
			: EnumSet.noneOf (Property.Flags.class);

		return new Property<T, V> (element, value, name, SingleReference.of (get), nflags);
	}

	/**
	 * Create a Single-Valued <code>Property</code>.
	 *
	 * @param  element The <code>Element</code> interface class, not null
	 * @param  value   The value class, not null
	 * @param  name    The name of the <code>Property</code>, not null
	 * @param  get     Method reference to retrieve the value, not null
	 * @param  set     Method reference to set the value, not null
	 *
	 * @return         The <code>Property</code>
	 */

	public static <T extends Element, V> Property<T, V> of (
			final Class<T> element,
			final Class<V> value,
			final String name,
			final Function<T, V> get,
			final BiConsumer<T, V> set)
	{
		Preconditions.checkNotNull (element, "element");
		Preconditions.checkNotNull (value, "value");
		Preconditions.checkNotNull (name, "name");
		Preconditions.checkNotNull (get, "get");
		Preconditions.checkNotNull (set, "set");
		Preconditions.checkArgument (name.length () > 0, "name can not be empty");

		Set<Flags> nflags = (Element.class.isAssignableFrom (value))
			? EnumSet.of (Flags.RELATIONSHIP)
			: EnumSet.noneOf (Property.Flags.class);

		return new Property<T, V> (element, value, name, SingleReference.of (get, set), nflags);
	}

	/**
	 * Create a Single-Valued <code>Property</code>, with the specified flags.
	 *
	 * @param  element The <code>Element</code> interface class, not null
	 * @param  value   The value class, not null
	 * @param  name    The name of the <code>Property</code>, not null
	 * @param  get     Method reference to retrieve the value, not null
	 * @param  set     Method reference to set the value, not null
	 * @param  flags   The <code>Flags</code> for the <code>Property</code>
	 *
	 * @return         The <code>Property</code>
	 */

	public static <T extends Element, V> Property<T, V> of (
			final Class<T> element,
			final Class<V> value,
			final String name,
			final Function<T, V> get,
			final BiConsumer<T, V> set,
			final Flags... flags)
	{
		Preconditions.checkNotNull (element, "element");
		Preconditions.checkNotNull (value, "value");
		Preconditions.checkNotNull (name, "name");
		Preconditions.checkNotNull (get, "get");
		Preconditions.checkNotNull (set, "set");
		Preconditions.checkArgument (name.length () > 0, "name can not be empty");

		Set<Flags> nflags = (Element.class.isAssignableFrom (value))
			? EnumSet.of (Flags.RELATIONSHIP)
			: EnumSet.noneOf (Property.Flags.class);

		for (Flags f : flags)
		{
			nflags.add (f);
		}

		Preconditions.checkArgument ((! nflags.contains (Flags.RELATIONSHIP)) && (! nflags.contains (Flags.RECOMMENDED))
				|| nflags.contains (Flags.RELATIONSHIP) && (! nflags.contains (Flags.REQUIRED))
				|| nflags.contains (Flags.REQUIRED) && (! nflags.contains (Flags.MUTABLE)) && (! nflags.contains (Flags.RECOMMENDED)),
				"The specified flags can not be used together");

		return new Property<T, V> (element, value, name, SingleReference.of (get, set), nflags);
	}

	/**
	 * Create a Multi-Valued <code>Property</code>.
	 *
	 * @param  element The <code>Element</code> interface class, not null
	 * @param  value   The value class, not null
	 * @param  name    The name of the <code>Property</code>, not null
	 * @param  get     Method reference to retrieve the value, not null
	 * @param  add     Method reference to add the value, not null
	 * @param  remove  Method reference to remove the value, not null
	 *
	 * @return         The <code>Property</code>
	 */

	public static <T extends Element, V extends Element> Property<T, V> of (
			final Class<T> element,
			final Class<V> value,
			final String name,
			final Function<T, Collection<V>> get,
			final BiPredicate<T, V> add,
			final BiPredicate<T, V> remove)
	{
		Preconditions.checkNotNull (element, "element");
		Preconditions.checkNotNull (value, "value");
		Preconditions.checkNotNull (name, "name");
		Preconditions.checkNotNull (get, "get");
		Preconditions.checkNotNull (add, "add");
		Preconditions.checkNotNull (remove, "remove");
		Preconditions.checkArgument (name.length () > 0, "name can not be empty");

		return new Property<T, V> (element, value, name,
				MultiReference.of (get, add, remove), EnumSet.of (Flags.RELATIONSHIP));
	}

	/**
	 * Create a Multi-Valued <code>Property</code>, with the specified flags.
	 *
	 * @param  element The <code>Element</code> interface class, not null
	 * @param  value   The value class, not null
	 * @param  name    The name of the <code>Property</code>, not null
	 * @param  get     Method reference to retrieve the value, not null
	 * @param  add     Method reference to add the value, not null
	 * @param  remove  Method reference to remove the value, not null
	 * @param  flags   The <code>Flags</code> for the <code>Property</code>
	 *
	 * @return         The <code>Property</code>
	 */

	public static <T extends Element, V extends Element> Property<T, V> of (
			final Class<T> element,
			final Class<V> value,
			final String name,
			final Function<T, Collection<V>> get,
			final BiPredicate<T, V> add,
			final BiPredicate<T, V> remove,
			final Flags... flags)
	{
		Preconditions.checkNotNull (element, "element");
		Preconditions.checkNotNull (value, "value");
		Preconditions.checkNotNull (name, "name");
		Preconditions.checkNotNull (get, "get");
		Preconditions.checkNotNull (add, "add");
		Preconditions.checkNotNull (remove, "remove");
		Preconditions.checkArgument (name.length () > 0, "name can not be empty");

		Set<Flags> nflags = EnumSet.of (Flags.RELATIONSHIP);

		for (Flags f : flags)
		{
			nflags.add (f);
		}

		Preconditions.checkArgument (! nflags.contains (Flags.REQUIRED), "Multi-Valued Properties can not be REQUIRED");

		return new Property<T, V> (element, value, name, MultiReference.of (get, add, remove), nflags);
	}

	/**
	 * Create the <code>Property</code>.
	 *
	 * @param  element   The <code>Element</code> interface class, not null
	 * @param  value     The value class, not null
	 * @param  reference The <code>Reference</code>, not null
	 * @param  name      The name of the <code>Property</code>, not null
	 * @param  flags     The <code>Flags</code> for the <code>Property</code>
	 */

	private Property (
			final Class<T> element,
			final Class<V> value,
			final String name,
			final Reference<T, V> reference,
			final Set<Flags> flags)
	{
		this.element = element;
		this.value = value;
		this.name = name;
		this.reference = reference;
		this.flags = flags;
	}

	/**
	 * Compare two <code>Property</code> instances to determine if they are
	 * equal.
	 *
	 * @param  property The <code>Property</code> instance to compare to the
	 *                  one represented by the called instance
	 *
	 * @return          <code>True</code> if the two <code>Property</code>
	 *                  instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object property)
	{
		return (property == this) ? true : (property instanceof Property)
			&& Objects.equals (this.element, ((Property) property).element)
			&& Objects.equals (this.value, ((Property) property).value)
			&& Objects.equals (this.name, ((Property) property).name)
			&& Objects.equals (this.flags, ((Property) property).flags)
			&& Objects.equals (this.reference, ((Property) property).reference);

	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Property</code> instance.
	 *
	 * @return An integer containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.element, this.value, this.name, this.flags, this.reference);
	}

	/**
	 * Get a <code>String</code> representation of the <code>Property</code>
	 * instance.
	 *
	 * @return A <code>String</code> representation of the <code>Property</code>
	 *         instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("element", this.element)
			.add ("value", this.value)
			.add ("name", this.name)
			.add ("flags", this.flags)
			.add ("reference", this.reference)
			.toString ();
	}

	/**
	 * Determine if the specified flag is set for this <code>Property</code>.
	 *
	 * @param  flag The flag to test
	 *
	 * @return      <code>true</code> if the specified flag is set,
	 *              <code>false</code> otherwise
	 */

	public boolean hasFlags (final Flags flag)
	{
		return this.flags.contains (flag);
	}

	/**
	 * Determine if the specified flags are set for this <code>Property</code>.
	 *
	 * @param  flags The flags to test
	 *
	 * @return      <code>true</code> if all of the specified flags are set,
	 *              <code>false</code> otherwise
	 */

	public boolean hasFlags (final Flags... flags)
	{
		return Arrays.stream (flags).allMatch (f -> this.flags.contains (f));
	}

	/**
	 * Determine if the specified flags are set for this <code>Property</code>.
	 *
	 * @param  flags The <code>Set</code> of flags to test
	 *
	 * @return      <code>true</code> if all of the specified flags are set,
	 *              <code>false</code> otherwise
	 */

	public boolean hasFlags (final Set<Flags> flags)
	{
		return this.flags.containsAll (flags);
	}

	/**
	 * Get the <code>Set</code> of <code>Flags</code> for this
	 * <code>Property</code>.
	 *
	 * @return A <code>Set</code> containing the <code>Flags</code>
	 */

	public Set<Flags> getFlags ()
	{
		return Collections.unmodifiableSet (this.flags);
	}

	/**
	 * Get the name of the <code>Property</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Property</code>
	 */

	public String getName ()
	{
		return this.name;
	}

	/**
	 * Get the <code>Element</code> interface class for the
	 * <code>Property</code>.
	 *
	 * @return The <code>Element</code> interface class
	 */

	public Class<T> getElementClass ()
	{
		return this.element;
	}

	/**
	 * Get the Java type for the value represented by the <code>Property</code>.
	 *
	 * @return The <code>Class</code> representing the type of the
	 *         <code>Property</code>
	 */

	public Class<V> getValueClass ()
	{
		return this.value;
	}

	/**
	 * Determine if the value contained in the specified <code>Element</code>
	 * has the specified value.  If the <code>Property</code> represents a
	 * single value, then this method will be equivalent to calling the
	 * <code>equals</code> method on the value represented by the
	 * <code>Property</code>.  This method is equivalent to calling the
	 * <code>contains</code> method for <code>Property</code> instances that
	 * represent collections.
	 *
	 * @param  element The <code>Element</code> instance, not null
	 * @param  value   The value to be tested against the <code>Element</code>
	 *
	 * @return <code>true</code> if the value represented by the
	 *         <code>Property</code> equals/contains the specified value,
	 *         <code>false</code> otherwise.
	 */

	public boolean hasValue (final T element, final @Nullable Object value)
	{
		Preconditions.checkNotNull (element, "element");

		return this.reference.hasValue (element, value);
	}

	/**
	 * Get a <code>Stream</code> containing all of the values in the specified
	 * <code>Element</code> instance.  This method will return a
	 * <code>Stream</code> containing zero or more values.  For a single-valued
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
		Preconditions.checkNotNull (element, "element");

		return this.reference.stream (element);
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

	protected boolean setValue (final T element, final V value)
	{
		assert element != null : "element is NULL";
		assert value != null : "value is NULL";
		assert ((! this.flags.contains (Flags.REQUIRED))
				|| this.reference.hasValue (element, null))
			: "The Property is REQUIRED and the value is non NULL";

		return ((! this.flags.contains (Flags.REQUIRED))
				|| this.reference.hasValue (element, null))
			? this.reference.setValue (element, value)
			: false;
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

	protected boolean clearValue (final T element, final V value)
	{
		assert element != null : "element is NULL";
		assert value != null : "value is NULL";
		assert (! this.flags.contains (Flags.REQUIRED)) : "The Property is REQUIRED";

		return (! this.flags.contains (Flags.REQUIRED))
			? this.reference.clearValue (element, value)
			: false;
	}
}
