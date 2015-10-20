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
import java.util.EnumSet;
import java.util.Objects;

import com.google.common.base.MoreObjects;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Representation of a component of an <code>Element</code>.  An
 * <code>Element</code> can be modelled as a collection of properties.  This
 * Class represents a <code>Property</code>, containing its name and type.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the value for the <code>Property</code>
 * @see     Definition
 */

public final class Property<T>
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

	/** The Java type of the <code>Property</code> */
	private final Class<T> type;

	/**
 	 * Create the <code>Property</code>.
 	 *
	 * @param  type The type of the value associated with the
	 *              <code>Property</code>, not null
	 * @param  name The name of the <code>Property</code>, not null
	 *
	 * @return      The <code>Property</code>
	 */

	public static <V> Property<V> getInstance (final Class<V> type, final String name)
	{
		assert type != null : "type is NULL";
		assert name != null : "name is NULL";
		assert name.length () > 0 : "name is empty";

		return new Property<V> (name, type, EnumSet.noneOf (Property.Flags.class));
	}

	/**
	 * Create the <code>Property</code>.
	 *
	 * @param  type  The type of the value associated with the
	 *               <code>Property</code>, not null
	 * @param  name  The name of the <code>Property</code>, not null
	 * @param  flags The <code>Flags</code> for the <code>Property</code>
	 *
	 * @return      The <code>Property</code>
	 */

	public static <V> Property<V> getInstance (final Class<V> type, final String name, final Flags... flags)
	{
		assert type != null : "type is NULL";
		assert name != null : "name is NULL";
		assert name.length () > 0 : "name is empty";

		Set<Flags> nflags = EnumSet.noneOf (Property.Flags.class);

		for (Flags f : flags)
		{
			nflags.add (f);
		}

		assert (! Element.class.isAssignableFrom (type)) && (! nflags.contains (Flags.RECOMMENDED))
				|| Element.class.isAssignableFrom (type) && (! nflags.contains (Flags.REQUIRED))
				|| nflags.equals (EnumSet.of (Flags.REQUIRED))
			: "The specified flags can not be used together";

		return new Property<V> (name, type, nflags);
	}

	/**
	 * Create the <code>Property</code>.
	 *
	 * @param  name     The name of the <code>Property</code>, not null
	 * @param  type     The Java type of the <code>Property</code>, not null
	 * @param  mutable  Indication if the <code>Attribute</code> can be changed
	 * @param  required Indication if the <code>Attribute</code> is allowed to
	 *                  be null
	 */

	private Property (final String name, final Class<T> type, final Set<Flags> flags)
	{
		assert name != null : "name is NULL";
		assert name.length () > 0 : "name is an empty String";
		assert type != null : "type is NULL";

		this.name = name;
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
			&& Objects.equals (this.name, ((Property) property).getName ())
			&& Objects.equals (this.type, ((Property) property).getPropertyType ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Property</code> instance.
	 *
	 * @return An integer containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.name, this.type);
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
	 * Get the Java type of the <code>Property</code>.
	 *
	 * @return The <code>Class</code> representing the type of the
	 *         <code>Property</code>
	 */

	public Class<T> getPropertyType ()
	{
		return this.type;
	}

	/**
	 * Determine if the <code>Property</code> represents a single value or a
	 * collection of values.
	 *
	 * @return <code>true</code> if the <code>Property</code> represents a
	 *         collection of values, <code>false</code> if the
	 *         <code>Property</code> represents a single value
	 */

	public boolean isMultivalued ()
	{
		return this.flags.contains (Flags.MULTIVALUED);
	}

	/**
	 * Determine if the value represented by the <code>Property</code> may be
	 * changed after the <code>Element</code> has been created.
	 *
	 * @return <code>true</code> if the value represented by the
	 *         <code>Property</code> may be changed, <code>false</code>
	 *         otherwise
	 */

	public boolean isMutable ()
	{
		return this.flags.contains (Flags.MUTABLE);
	}

	/**
	 * Determine if the value represented by the property if a relationship
	 * with another <code>Element</code> instance.  The <code>Property</code>
	 * represents a relationship if type is a subclass of <code>Element</code>.
	 *
	 * @return <code>true</code> if the <code>Property</code> represents a
	 *         relationship, <code>false</code> otherwise
	 */

	public boolean isRelationship ()
	{
		return Element.class.isAssignableFrom (this.type);
	}

	/**
	 * Determine if the value represented by the <code>Property</code> is
	 * required.  If the value is not required, then it may be
	 * <code>null</code>.
	 *
	 * @return <code>true</code> if the value represented by the
	 *         <code>Property</code> is required, <code>false</code> otherwise
	 */

	public boolean isRequired ()
	{
		return this.flags.contains (Flags.REQUIRED);
	}

	/**
	 * Determine if the value represented by the <code>Property</code> is
	 * required.  If the value is not recommended, then it may be
	 * <code>null</code>.
	 *
	 * @return <code>true</code> if the value represented by the
	 *         <code>Property</code> is recommended, <code>false</code>
	 *         otherwise
	 */

	public boolean isRecommended ()
	{
		return this.flags.contains (Flags.RECOMMENDED);
	}

	/**
	 * Get a <code>String</code> representation of the <code>Property</code>
	 * instance.
	 *
	 * @return A <code>String</code> representation of the <code>Property</code>
	 *         instance
	 */

	@Override
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("name", this.name)
			.add ("type", this.type)
			.add ("flags", this.flags)
			.toString ();
	}
}
