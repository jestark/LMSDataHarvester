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

import java.util.Objects;

import javax.annotation.CheckReturnValue;

import com.google.common.base.MoreObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Representation of a navigable relationship, for an <code>Element</code>
 * interface class.  This class represents a relationship which is rooted on a
 * <code>Property</code> instance, within the associated <code>Element</code>
 * interface class.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the owning <code>Element</code>
 * @param   <V> The type of the associated <code>Element</code>
 */

final class PropertyRelationship<T extends Element, V extends Element> implements Relationship<T, V>
{
	/** The Logger */
	private final Logger log;

	/** The other side of the <code>Relationship</code> */
	private final InverseRelationship<V, T> inverse;

	/** The <code>Property</code> */
	private final Property<T, V> property;

	/**
	 * Create the <code>PropertyRelationship</code> from the specified values.
	 *
	 * @param  property The <code>Property</code>, not null
	 * @param  inverse  The <code>Property</code> for the inverse relationship,
	 *                  not null
	 */

	public static <T extends Element, V extends Element> PropertyRelationship<T, V> of (
			final Property<T, V> property,
			final Property<V, T> inverse)
	{
		assert inverse != null : "inverse is NULL";
		assert property != null : "property is NULL";

		return new PropertyRelationship<T, V> (PropertyInverseRelationship.of (inverse), property);
	}

	/**
	 * Create the <code>PropertyRelationship</code> from the specified values.
	 *
	 * @param  inverse  The <code>Selector</code> for the inverse relationship,
	 *                  not null
	 * @param  property The <code>Property</code>, not null
	 */

	public static <T extends Element, V extends Element> PropertyRelationship<T, V> of (
			final Property<T, V> property,
			final Selector<T> inverse)
	{
		assert inverse != null : "inverse is NULL";
		assert property != null : "property is NULL";

		return new PropertyRelationship<T, V> (SelectorInverseRelationship.of (property, inverse), property);
	}

	/**
	 * Create the <code>PropertyRelationShip</code>.
	 *
	 * @param  inverse   The <code>Inverse</code> relationship, not null
	 * @param  property  The <code>Property</code>, not null
	 */

	private PropertyRelationship (final InverseRelationship<V, T> inverse,
			final Property<T, V> property)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.inverse = inverse;
		this.property = property;
	}

	/**
	 * Compare two <code>PropertyRelationship</code> instances to determine if
	 * they are equal.
	 *
	 * @param  obj The <code>PropertyRelationship</code> instance to compare to
	 *             the one represented by the called instance
	 *
	 * @return     <code>true</code> if the two
	 *             <code>PropertyRelationship</code> instances are equal,
	 *             <code>false</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof PropertyRelationship)
				&& Objects.equals (this.inverse, ((PropertyRelationship) obj).inverse)
				&& Objects.equals (this.property, ((PropertyRelationship) obj).property);
	}

	/**
	 * Compute a hashCode for the <code>PropertyRelationship</code> instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.inverse, this.property);
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>PropertyRelationship</code> instance, including the identifying
	 * fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>PropertyRelationship</code> instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("property", this.property)
			.add ("inverse", this.inverse)
			.toString ();
	}

	/**
	 * Connect the relationship represented by this <code>Relationship</code>
	 * instance for the specified <code>Element</code> instance.  This method
	 * will write the inverse relationship into the <code>Element</code>
	 * instance for the <code>Element</code> on the other side of the
	 * relationship.
	 *
	 * @param  element The <code>Element</code> to process, not null
	 *
	 * @return         <code>true</code> if the relationship was successfully
	 *                 created <code>false</code> otherwise
	 */

	@Override
	public boolean connect (final T element)
	{
		this.log.trace ("connect: element={}", element);
		this.log.debug ("Connecting Relationship: {} -> {}",
				this.property.getElementClass ().getSimpleName (),
				this.property.getValueClass ().getSimpleName ());

		assert element != null : "element";
		assert element.getDomainModel () != null : "missing DomainModel";
		assert element.getDomainModel ().contains (element) : "element is not in the model";

		return element.getDomainModel ().contains (element) && this.property.stream (element)
			.allMatch (x -> this.inverse.insert (x, element));
	}

	/**
	 * Disconnect the relationship represented by this
	 * <code>Relationship</code> instance for the specified
	 * <code>Element</code> instance.  This method will remove the inverse
	 * relationship from the <code>Element</code> instance for the
	 * <code>Element</code> on the other side of the relationship.
	 *
	 * @param  element The <code>Element</code> to process, not null
	 *
	 * @return         <code>true</code> if the relationship was successfully
	 *                 broken <code>false</code> otherwise
	 */

	@Override
	public boolean disconnect (final T element)
	{
		this.log.trace ("disconnect: element={}", element);
		this.log.debug ("Disconnecting Relationship: {} -> {}",
				this.property.getElementClass ().getSimpleName (),
				this.property.getValueClass ().getSimpleName ());

		assert element != null : "element";
		assert element.getDomainModel () != null : "missing DomainModel";
		assert element.getDomainModel ().contains (element) : "element is not in the model";

		return element.getDomainModel ().contains (element) && this.property.stream (element)
			.allMatch (x -> this.inverse.remove (x, element));
	}
}
