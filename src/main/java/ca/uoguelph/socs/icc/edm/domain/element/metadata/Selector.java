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

package ca.uoguelph.socs.icc.edm.domain.element.metadata;

import java.util.Set;
import java.util.HashSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Definition of the <code>Set</code> of <code>Property</code> instances used
 * to load an <code>Element</code> from the <code>DataStore</code>.  A
 * <code>Selector</code> can uniquely identify a single <code>Element</code>
 * instance or it my match multiple instances of a particular
 * <code>Element</code> class.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Definition
 * @see     Property
 */

public final class Selector
{
	/** The implementation class for the <code>Element</code> */
	private final Class<? extends Element> type;

	/** The name of the <code>Selector</code> */
	private final String name;

	/** Will this <code>Selector</code> yield only one <code>Element</code> */
	private final boolean unique;

	/** The <code>Property</code> instances */
	private final Set<Property<?>> properties;

	/**
	 * Create the <code>Selector</code> using multiple <code>Property</code>
	 * instances.
	 *
	 * @param  type                     The <code>Element</code> interface
	 *                                  class, not null
	 * @param  name                     The name of the <code>Selector</code>,
	 *                                  not null
	 * @param  unique                   An indication if the
	 *                                  <code>Selector</code> uniquely
	 *                                  identifies an <code>Element</code>
	 *                                  instance
	 * @param  properties               The properties to be used to create the
	 *                                  <code>Selector</code>, not null
	 *
	 * @return                          The <code>Selector</code>
	 * @throws IllegalArgumentException if a different <code>Selector</code>
	 *                                  already exists in the definition with
	 *                                  the same name
	 */

	public static Selector getInstance (final Class<? extends Element> type, final String name, final boolean unique, final Property<?>... properties)
	{
		if ((type == null) || (name == null))
		{
			throw new NullPointerException ();
		}

		if (! type.isInterface ())
		{
			throw new IllegalArgumentException ("type MUST be an interface");
		}

		if (name.length () == 0)
		{
			throw new IllegalArgumentException ("name is an empty String");
		}


		Set<Property<?>> props = new HashSet<Property<?>> ();

		for (Property<?> property : properties)
		{
			if (type != property.getElementType ())
			{
				throw new IllegalArgumentException ("Type mismatch, property does not match selector");
			}

			props.add (property);
		}

		return MetaData.registerSelector (new Selector (type, name, unique, props));
	}

	/**
	 * Create the <code>Selector</code> using a single <code>Property</code>.
	 *
	 * @param  type                     The <code>Element</code> interface
	 *                                  class, not null
	 * @param  unique                   An indication if the
	 *                                  <code>Selector</code> uniquely
	 *                                  identifies an <code>Element</code>
	 *                                  instance
	 * @param  property                 The property to be represented by the
	 *                                  <code>Selector</code>, not null
	 *
	 * @return                          The <code>Selector</code>
	 * @throws IllegalArgumentException if a different <code>Selector</code>
	 *                                  already exists in the definition with
	 *                                  the same name
	 */

	public static Selector getInstance (final Class<? extends Element> type, final boolean unique, final Property<?> property)
	{
		assert type != null : "type is NULL";
		assert property != null : "property is NULL";
		assert type == property.getElementType () : "Type mismatch, property does not match selector";

		return Selector.getInstance (type, property.getName (), unique, property);
	}

	/**
	 * Create the <code>Selector</code> for the specified <code>Element</code>.
	 * This method creates the selector for the special case where there are
	 * no <code>Property</code> instances to specify.  The <code>Selector</code>
	 * will be named "all" and will correspond to a query that returns all of
	 * the elements matching the specified interface.
	 *
	 * @param  type                     The <code>Element</code> interface
	 *                                  class, not null
	 *
	 * @return                          The <code>Selector</code>
	 * @throws IllegalArgumentException if a different <code>Selector</code>
	 *                                  already exists in the definition with
	 *                                  the same name
	 * @throws IllegalStateException    If the <code>Element</code> associated
	 *                                  with the <code>Selector</code> has not
	 *                                  been registered
	 */

	public static Selector getInstance (final Class<? extends Element> type)
	{
		assert type != null : "type is NULL";

		return MetaData.registerSelector (new Selector (type, "all", false, new HashSet<Property<?>> ()));
	}

	/**
	 * Create the <code>Selector</code>.
	 *
	 * @param  type       The <code>Element</code> interface class
	 * @param  name       The name of the <code>Selector</code>
	 * @param  unique     An indication if the <code>Selector</code> uniquely
	 *                    identifies an <code>Element</code> instance
	 * @param  properties The <code>Set</code> of <code>Property</code>
	 *                    instances represented by the <code>Selector</code>
	 */

	private Selector (final Class<? extends Element> type, final String name, final boolean unique, final Set<Property<?>> properties)
	{
		this.type = type;
		this.name = name;
		this.unique = unique;
		this.properties = properties;
	}

	/**
	 * Compare two <code>Selector</code> instances to determine if they are
	 * equal.
	 *
	 * @param  obj The <code>Selector</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>true</code> if the two <code>Selector</code> instances
	 *             are equal, <code>false</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof Selector)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.type, ((Selector) obj).type);
			ebuilder.append (this.name, ((Selector) obj).name);
			ebuilder.append (this.unique, ((Selector) obj).unique);
			ebuilder.append (this.properties, ((Selector) obj).properties);

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Selector</code> instance.
	 *
	 * @return The hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 887;
		final int mult = 7;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.type);
		hbuilder.append (this.name);
		hbuilder.append (this.unique);
		hbuilder.append (this.properties);

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the Java type of the <code>Element</code> implementation.
	 *
	 * @return The <code>Class</code> representing the implementation type
	 */

	public Class<? extends Element> getElementType ()
	{
		return this.type;
	}

	/**
	 * Get the name of the <code>Selector</code>.
	 *
	 * @return  A <code>String</code> containing the name
	 */

	public String getName ()
	{
		return this.name;
	}

	/**
	 * Determine if the <code>Selector</code> uniquely identifies an
	 * <code>Element</code> instance.
	 *
	 * @return <code>true</code> if the <code>Selector</code> uniquely
	 *         identifies an <code>Element</code> instance, <code>false</code>
	 *         otherwise
	 */

	public boolean isUnique ()
	{
		return this.unique;
	}

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances
	 * corresponding to the <code>Element</code>.
	 *
	 * @return A <code>Set</code> containing all of the <code>Property</code>
	 *         instances for the <code>Element</code>
	 */

	public Set<Property<?>> getProperties ()
	{
		return new HashSet<Property<?>> (this.properties);
	}

	/**
	 * Get a <code>String</code> representation of the <code>Selector</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Selector</code>
	 *         instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("type", this.type);
		builder.append ("name", this.name);
		builder.append ("unique", this.unique);
		builder.append ("properties", this.properties);

		return builder.toString ();
	}
}
