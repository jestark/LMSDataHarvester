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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

/**
 * Representation of a inverse uni-directional relationship.
 *
 * @version 1.0
 * @param   <T> The type of the owning <code>Element</code>
 * @param   <V> The type of the associated <code>Element</code>
 */

final class SelectorInverseRelationship<T extends Element, V extends Element> implements InverseRelationship<T, V>
{
	/** The Logger */
	private final Logger log;

	/** The <code>Element</code> interface class */
	private final Class<V> value;

	/** The <code>Property</code> for owning <code>Element</code> */
	private final Property<T> property;

	/** The <code>Selector</code> for the associated <code>Element</code> */
	private final Selector selector;

	/**
	 * Create the <code>SelectorInverseRelationship</code> from the specified
	 * values.
	 *
	 * @param  value    The <code>Element</code> interface class, not null
	 * @param  property The <code>Property</code>, not null
	 * @param  selector The <code>Selector</code>, not null
	 */

	public static <T extends Element, V extends Element> SelectorInverseRelationship<T, V> of (
			final Class<V> value,
			final Property<T> property,
			final Selector selector)
	{
		assert value != null : "value is NULL";
		assert property != null : "property is NULL";
		assert selector != null : "selector is NULL";

		return new SelectorInverseRelationship<T, V> (value, property, selector);
	}

	/**
	 * Create the <code>SelectorInverse</code>.
	 *
	 * @param  value    The <code>Element</code> interface class, not null
	 * @param  property The <code>Property</code>, not null
	 * @param  selector The <code>Selector</code>, not null
	 */

	private SelectorInverseRelationship (final Class<V> value,
			final Property<T> property,
			final Selector selector)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.value = value;
		this.property = property;
		this.selector = selector;
	}

	private Query<V> getQuery ()
	{
		return null; // model.getQuery (Container.getInstance ()
//				.getMetaData (this.value),
//				this.selector);
	}

	/**
	 * Insert the specified value into the specified <code>Element</code> to
	 * create the relationship.
	 * <p>
	 * This method is a same as <code>canInsert</code> as there is nothing
	 * to insert in a uni-directional relationship.
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
				this.property.getName (),
				this.value.getSimpleName ());

		assert element != null : "element";
		assert value != null : "value is NULL";
		assert element.getDomainModel () != null : "missing DomainModel";
		assert element.getDomainModel ().contains (element) : "element is not in the model";
		assert element.getDomainModel ().contains (value) : "value is not in the model";

		return element.getDomainModel ().contains (element) && ((! this.selector.isUnique ()) || this.getQuery ()
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
				this.property.getName (),
				this.value.getSimpleName ());

		assert element != null : "element is NULL";
		assert value != null : "value is NULL";

		return true;
	}
}
