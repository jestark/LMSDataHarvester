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

import java.util.Set;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;

import javax.annotation.CheckReturnValue;

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
 * @see     Definition
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
	 *  <li> To be <code>MULTIVALUED</code> a <code>Property</code> must
	 *       represent a relationship between two <code>Element</code>
	 *       instances.
	 *  <li> <code>MULTIVALUED</code> <code>Property</code> instances may not
	 *       be <code>REQUIRED</code>.
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

		MUTABLE,

		/**
		 * Indicates that the <code>Property</code> represents a collection of
		 * values
		 */

		MULTIVALUED;
	}

	/** The flags for the <code>Property</code> */
	private final Set<Flags> flags;

	/** The name of the <code>Property</code> */
	private final String name;

	/** The <code>Element</code> type of the <code>Property</code> */
	private final Class<T> eleemnt;

	/** The Java type of the <code>Property</code> */
	private final Class<V> type;

	/**
 	 * Create the <code>Property</code>.
 	 *
	 * @param  element The <code>Element</code> interface class, not null
	 * @param  type    The type of the value associated with the
	 *                 <code>Property</code>, not null
	 * @param  name    The name of the <code>Property</code>, not null
	 *
	 * @return         The <code>Property</code>
	 */

	public static <T extends Element, V> Property<T, V> of (final Class<T> element, final Class<V> type, final String name)
	{
		Preconditions.checkNotNull (element, "element");
		Preconditions.checkNotNull (type, "type");
		Preconditions.checkNotNull (name, "name");
		Preconditions.checkArgument (name.length () > 0, "name can not be empty");

		Set<Flags> nflags = (Element.class.isAssignableFrom (type))
				? EnumSet.of (Flags.RELATIONSHIP)
				: EnumSet.noneOf (Property.Flags.class);

		return new Property<V> (name, element, type, nflags);
	}

	/**
	 * Create the <code>Property</code>.
	 *
	 * @param  element The <code>Element</code> interface class, not null
	 * @param  type    The type of the value associated with the
	 *                 <code>Property</code>, not null
	 * @param  name    The name of the <code>Property</code>, not null
	 * @param  flags   The <code>Flags</code> for the <code>Property</code>
	 *
	 * @return         The <code>Property</code>
	 */

	public static <T extends Element, V> Property<T, V> of (final Class<T> element, final Class<V> type, final String name, final Flags... flags)
	{
		Preconditions.checkNotNull (element, "element");
		Preconditions.checkNotNull (type, "type");
		Preconditions.checkNotNull (name, "name");
		Preconditions.checkArgument (name.length () > 0, "name can not be empty");

		Set<Flags> nflags = (Element.class.isAssignableFrom (type))
				? EnumSet.of (Flags.RELATIONSHIP)
				: EnumSet.noneOf (Property.Flags.class);

		for (Flags f : flags)
		{
			nflags.add (f);
		}

		Preconditions.checkArgument ((! nflags.contains (Flags.RELATIONSHIP)) && (! nflags.contains (Flags.RECOMMENDED))
				|| nflags.contains (Flags.RELATIONSHIP) && (! nflags.contains (Flags.REQUIRED))
				|| nflags.equals (EnumSet.of (Flags.REQUIRED)),
				"The specified flags can not be used together");

		return new Property<V> (name, element, type, nflags);
	}

	/**
	 * Create the <code>Property</code>.
	 *
	 * @param  name    The name of the <code>Property</code>, not null
	 * @param  element The <code>Element</code> interface class, not null
	 * @param  type    The Java type of the <code>Property</code>, not null
	 * @param  flags   The <code>Flags</code> for the <code>Property</code>
	 */

	private Property (final String name, final Class<T> element, final Class<V> type, final Set<Flags> flags)
	{
		this.name = name;
		this.element = element;
		this.type = type;
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
			&& Objects.equals (this.name, ((Property) property).name ())
			&& Objects.equals (this.element, ((Property) property).element ())
			&& Objects.equals (this.type, ((Property) property).type ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Property</code> instance.
	 *
	 * @return An integer containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.name, this.element, this.type);
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
			.add ("name", this.name)
			.add ("element", this.element)
			.add ("type", this.type)
			.add ("flags", this.flags)
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
		return this.type;
	}
}
