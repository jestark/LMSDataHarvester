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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
	/** The name of the <code>Property</code> */
	private final String name;

	/** The Java type of the <code>Property</code> */
	private final Class<T> type;

	/** Indication if the value in the <code>Element</code> may be changed */
	private final boolean mutable;

	/** Indication if the value is required in the <code>Element</code> */
	private final boolean required;

	/**
 	 * Create the <code>Property</code>.
 	 *
	 * @param  type     The type of the value associated with the
	 *                  <code>Property</code>, not null
	 * @param  name     The name of the <code>Property</code>, not null
	 * @param  mutable  Indication if the <code>Property</code> can be changed
	 * @param  required Indication if the <code>Property</code> is allowed to
	 *                  be null
	 *
	 * @return          The <code>Property</code>
	 */

	public static <V> Property<V> getInstance (final Class<V> type, final String name, final boolean mutable, final boolean required)
	{
		assert type != null : "type is NULL";
		assert name != null : "name is NULL";
		assert name.length () > 0 : "name is empty";

		return new Property<V> (name, type, mutable, required);
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

	protected Property (final String name, final Class<T> type, final boolean mutable, final boolean required)
	{
		assert name != null : "name is NULL";
		assert name.length () > 0 : "name is an empty String";
		assert type != null : "type is NULL";

		this.name = name;
		this.type = type;
		this.mutable = mutable;
		this.required = required;
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
		boolean result = false;

		if (property == this)
		{
			result = true;
		}
		else if (property instanceof Property)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.name, ((Property) property).getName ());
			ebuilder.append (this.type, ((Property) property).getPropertyType ());

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Property</code> instance.
	 *
	 * @return An integer containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 883;
		final int mult = 11;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.name);
		hbuilder.append (this.type);

		return hbuilder.toHashCode ();
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
	 * Determine if the value represented by the <code>Property</code> may be
	 * changed after the <code>Element</code> has been created.
	 *
	 * @return <code>true</code> if the value represented by the
	 *         <code>Property</code> may be changed, <code>false</code>
	 *         otherwise
	 */

	public boolean isMutable ()
	{
		return this.mutable;
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
		return this.required;
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
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("name", this.name);
		builder.append ("type", this.type);
		builder.append ("mutable", this.mutable);
		builder.append ("required", this.required);

		return builder.toString ();
	}
}
