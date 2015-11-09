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
 * Representation of a inverse navigable relationship for an
 * <code>Element</code> interface class.  This class represents an inverse
 * relationship which is rooted on a <code>Property</code> instance, within the
 * associated <code>Element</code> interface class.
 *
 * @version 1.0
 * @param   <T> The type of the owning <code>Element</code>
 * @param   <V> The type of the associated <code>Element</code>
 */

final class PropertyInverseRelationship<T extends Element, V extends Element> implements InverseRelationship<T, V>
{
	/** The Logger */
	private final Logger log;

	/** The assocated <code>Property</code> */
	private final Property<T, V> property;

	/**
	 * Create the <code>PropertyInverseRelationship</code> from the specified
	 * values.
	 *
	 * @param  property The <code>Property</code>, not null
	 */

	public static <T extends Element, V extends Element> PropertyInverseRelationship<T, V> of (
			final Property<T, V> property)
	{
		assert property != null : "property is NULL";

		return new PropertyInverseRelationship<T, V> (property);
	}

	/**
	 * Create the <code>PropertyInverse</code>.
	 *
	 * @param  property The <code>Property</code>, not null
	 */

	private PropertyInverseRelationship (final Property<T, V> property)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.property = property;
	}

	/**
	 * Compare two <code>PropertyInverseRelationship</code> instances to
	 * determine if they are equal.
	 *
	 * @param  obj The <code>PropertyInverseRelationship</code> instance to
	 *             compare to the one represented by the called instance
	 *
	 * @return     <code>true</code> if the two
	 *             <code>PropertyInverseRelationship</code> instances are equal,
	 *             <code>false</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof PropertyInverseRelationship)
				&& Objects.equals (this.property, ((PropertyInverseRelationship) obj).property);
	}

	/**
	 * Compute a hashCode for the <code>PropertyInverseRelationship</code>
	 * instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.property);
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>PropertyInverseRelationship</code> instance, including the 
	 * identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>PropertyInverseRelationship</code> instance
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
	 * Insert the specified value into the specified <code>Element</code> to
	 * create the relationship.
	 *
	 * @param  element The <code>Element</code> to operate on, not null
	 * @param  value   The <code>Element</code> to be inserted, not null
	 *
	 * @return         <code>true</code> if the value was successfully
	 *                 inserted, <code>false</code> otherwise
	 */

	@Override
	public boolean insert (final T element, final V value)
	{
		this.log.trace ("insert: element={}, value={}", element, value);
		this.log.debug ("inserting Relationship: {} -> {}",
				this.property.getElementClass ().getSimpleName (),
				this.property.getValueClass ().getSimpleName ());

		assert element != null : "element";
		assert value != null : "value is NULL";
		assert element.getDomainModel () != null : "missing DomainModel";
		assert element.getDomainModel ().contains (element) : "element is not in the model";
		assert element.getDomainModel ().contains (value) : "value is not in the model";

		return element.getDomainModel ().contains (element)
				&& this.property.setValue (element, value);
	}

	/**
	 * Remove the specified value from the specified <code>Element</code> to
	 * break the relationship.
	 *
	 * @param  element The <code>Element</code> to operate on, not null
	 * @param  value   The <code>Element</code> to be removed, not null
	 *
	 * @return         <code>true</code> if the value was successfully
	 *                 removed, <code>false</code> otherwise
	 */

	@Override
	public boolean remove (final T element, final V value)
	{
		this.log.trace ("remove: element={}, value={}", element, value);
		this.log.debug ("removing Relationship: {} -> {}",
				this.property.getElementClass ().getSimpleName (),
				this.property.getValueClass ().getSimpleName ());

		assert element != null : "element is NULL";
		assert value != null : "value is NULL";
		assert this.property.hasValue (element, value) : "The Element does not have the value to be removed";

		return this.property.clearValue (element, value);
	}
}
