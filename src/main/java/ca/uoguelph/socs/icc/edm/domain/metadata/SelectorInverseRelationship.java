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

import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

/**
 * Representation of a non-navigable inverse relationship.  This class
 * represents the non-navigable side of a uni-directional relationship.  As such
 * the relationship is rooted in the <code>Selector</code>, from the navigable
 * side, using the associated <code>Property</code> (from the navigable side)
 * as a reference.
 *
 * @version 1.0
 * @param   <T> The type of the owning <code>Element</code>
 * @param   <V> The type of the associated <code>Element</code>
 */

final class SelectorInverseRelationship<T extends Element, V extends Element> implements InverseRelationship<T, V>
{
	/** The Logger */
	private final Logger log;

	/** The <code>Property</code> for owning <code>Element</code> */
	private final Property<V, T> property;

	/** The <code>Selector</code> for the associated <code>Element</code> */
	private final Selector<V> selector;

	/**
	 * Create the <code>SelectorInverseRelationship</code> from the specified
	 * values.
	 *
	 * @param  property The <code>Property</code>, not null
	 * @param  selector The <code>Selector</code>, not null
	 */

	public static <T extends Element, V extends Element> SelectorInverseRelationship<T, V> of (
			final Property<V, T> property,
			final Selector<V> selector)
	{
		assert property != null : "property is NULL";
		assert selector != null : "selector is NULL";

		return new SelectorInverseRelationship<T, V> (property, selector);
	}

	/**
	 * Create the <code>SelectorInverse</code>.
	 *
	 * @param  property The <code>Property</code>, not null
	 * @param  selector The <code>Selector</code>, not null
	 */

	private SelectorInverseRelationship (final Property<V, T> property, final Selector<V> selector)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.property = property;
		this.selector = selector;
	}

	/**
	 * Compare two <code>SelectorInverseRelationship</code> instances to determine
	 * if they are equal.
	 *
	 * @param  obj The <code>SelectorInverseRelationship</code> instance to
	 *             compare to the one represented by the called instance
	 *
	 * @return     <code>true</code> if the two
	 *             <code>SelectorInverseRelationship</code> instances are equal,
	 *             <code>false</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof SelectorInverseRelationship)
				&& Objects.equals (this.property, ((SelectorInverseRelationship) obj).property)
				&& Objects.equals (this.selector, ((SelectorInverseRelationship) obj).selector);
	}

	/**
	 * Compute a hashCode for the <code>SelectorInverseRelationship</code>
	 * instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.property, this.selector);
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>SelectorInverseRelationship</code> instance, including the
	 * identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>SelectorInverseRelationship</code> instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("property", this.property)
			.add ("selector", this.selector)
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
				this.property.getName (), this.selector.getName ());

		assert element != null : "element";
		assert value != null : "value is NULL";
		assert element.getDomainModel () != null : "missing DomainModel";
		assert element.getDomainModel ().contains (element) : "element is not in the model";
		assert element.getDomainModel ().contains (value) : "value is not in the model";

		return element.getDomainModel ().contains (element)
			&& ((this.selector.getCardinality () == Selector.Cardinality.MULTIPLE)
					|| element.getDomainModel ()
					.getQuery (this.selector, this.selector.getElementClass ())
					.setValue (this.property, element)
					.queryAll ()
					.size () <= 1);
	}

	/**
	 * Remove the specified value from the specified <code>Element</code> to
	 * break the relationship.
	 * <p>
	 * This method is a no-op (unconditionally returning <code>true</code>)
	 * as there is nothing to remove from a uni-directional relationship.
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
		this.log.trace ("remove: element={}, value={}");
		this.log.debug ("removing Relationship: {} -> {}",
				this.property.getName (), this.selector.getName ());

		assert element != null : "element is NULL";
		assert value != null : "value is NULL";

		return true;
	}
}
