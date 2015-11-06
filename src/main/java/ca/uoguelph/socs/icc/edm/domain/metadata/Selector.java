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
import java.util.HashSet;
import java.util.Objects;

import java.util.stream.Collectors;

import javax.annotation.CheckReturnValue;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

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

public final class Selector<T extends Element>
{
	/** The <code>Element</code> type described by the <code>Selector</code> */
	private final Class<T> element;

	/** The name of the <code>Selector</code> */
	private final String name;

	/** Are all of the associated <code>Property</code> instances immutable */
	private final boolean constant;

	/** Will this <code>Selector</code> yield only one <code>Element</code> */
	private final boolean unique;

	/** The <code>Property</code> instances */
	private final Set<Property<?>> properties;

	 /**
	 * Create the <code>Selector</code> using multiple <code>Property</code>
	 * instances.
	 *
	 * @param  element    The <code>Element</code> interface class, not null
	 * @param  name       The name of the <code>Selector</code>, not null
	 * @param  unique     An indication if the <code>Selector</code> uniquely
	 *                    identifies an <code>Element</code> instance
	 * @param  properties The properties to be used to create the
	 *                    <code>Selector</code>, not null
	 *
	 * @return            The <code>Selector</code>
	 */

	public static <T extends Element> Selector<T> of (final Class<T> element,
			final String name,
			final boolean unique,
			final Property<?>... properties)
	{
		Preconditions.checkNotNull (element, "element");
		Preconditions.checkNotNull (name, "name");
		Preconditions.checkArgument (name.length () > 0);

		return new Selector<T> (element, name, unique, Arrays.stream (properties).collect (Collectors.toSet ()));
	}

	/**
	 * Create the <code>Selector</code> using a single <code>Property</code>.
	 *
	 * @param  element  The <code>Element</code> interface class, not null
	 * @param  unique   An indication if the <code>Selector</code> uniquely
	 *                  identifies an <code>Element</code> instance
	 * @param  property The property to be represented by the
	 *                  <code>Selector</code>, not null
	 *
	 * @return          The <code>Selector</code>
	 */

	public static <T extends Element> Selector<T> of (final Class<T> element,
			final Property<?> property,
			final boolean unique)
	{
		Preconditions.checkNotNull (element, "element");
		Preconditions.checkNotNull (property, "property");

		return Selector.of (element, property.getName (), unique, property);
	}

	/**
	 * Create the <code>Selector</code>.
	 *
	 * @param  element    The <code>Element</code> interface class, not null
	 * @param  name       The name of the <code>Selector</code>
	 * @param  unique     An indication if the <code>Selector</code> uniquely
	 *                    identifies an <code>Element</code> instance
	 * @param  properties The <code>Set</code> of <code>Property</code>
	 *                    instances represented by the <code>Selector</code>
	 */

	private Selector (final Class<T> element, final String name, final boolean unique, final Set<Property<?>> properties)
	{
		this.element = element;
		this.name = name;
		this.unique = unique;
		this.properties = properties;

		this.constant = this.properties.stream ()
			.allMatch (p -> ! p.hasFlags (Property.Flags.MUTABLE));
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
		return (obj == this) ? true : (obj instanceof Selector)
			&& Objects.equals (this.element, ((Selector) obj).element)
			&& Objects.equals (this.name, ((Selector) obj).name)
			&& Objects.equals (this.unique, ((Selector) obj).unique)
			&& Objects.equals (this.properties, ((Selector) obj).properties);
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Selector</code> instance.
	 *
	 * @return The hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.element, this.name, this.unique, this.properties);
	}

	/**
	 * Get a <code>String</code> representation of the <code>Selector</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Selector</code>
	 *         instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("element", this.element)
			.add ("name", this.name)
			.add ("unique", this.unique)
			.add ("properties", this.properties)
			.toString ();
	}

	/**
	 * Get the <code>Element</code> interface class that is described by the
	 * <code>Selector</code>.
	 *
	 * @return The <code>Element</code> interface class
	 */

	public Class<T> getElementClass ()
	{
		return this.element;
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
	 * Determine if all of the <code>Property</code> instances associated with
	 * this <code>Selector</code> are immutable.
	 *
	 * @return <code>true</code> if all of the associated <code>Property</code>
	 *         instances are immutable, <code>false</code> otherwise
	 */

	public boolean isConstant ()
	{
		return this.constant;
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
}
