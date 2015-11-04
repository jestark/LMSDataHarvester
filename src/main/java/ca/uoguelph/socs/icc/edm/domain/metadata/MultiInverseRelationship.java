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

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Representation of a inverse relationship for an <code>Element</code> with
 * a cardinality greater than one.
 *
 * @version 1.0
 * @param   <T> The type of the owning <code>Element</code>
 * @param   <V> The type of the associated <code>Element</code>
 */

final class MultiInverseRelationship<T extends Element, V extends Element> implements InverseRelationship<T, V>
{
	/** The Logger */
	private final Logger log;

	/** The <code>RelationshipReference</code> used to manipulate the element */
	private final MultiAccessor<T, V> reference;

	/**
	 * Create the <code>MultiInverseRelationship</code> from the specified
	 * values.
	 *
	 * @param  reference The <code>MultiAccessor</code>, not null
	 */

	public static <T extends Element, V extends Element> MultiInverseRelationship<T, V> of (
			final MultiAccessor<T, V> reference)
	{
		assert reference != null : "reference is NULL";

		return new MultiInverseRelationship<T, V> (reference);
	}

	/**
	 * Create the <code>MultiInverseRelationship</code>
	 *
	 * @param  reference The <code>MultiAccessor</code>, not null
	 */

	private MultiInverseRelationship (final MultiAccessor<T, V> reference)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.reference = reference;
	}

	/**
	 * Insert the specified value into the specified <code>Element</code> to
	 * create the relationship.
	 * <p>
	 * This method adds the specified value to the <code>Collection</code>
	 * which contains all of the values for the relationship.  Note that it
	 * may be possible to add the same value to an <code>Element</code>
	 * multiple times, depending of the implementation of the
	 * <code>Element</code>.  The called must ensure that this does not
	 * happen.
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
				this.reference.getElementClass ().getSimpleName (),
				this.reference.getProperty ().getName ());

		assert element != null : "element";
		assert value != null : "value is NULL";
		assert element.getDomainModel () != null : "missing DomainModel";
		assert element.getDomainModel ().contains (element) : "element is not in the model";
		assert element.getDomainModel ().contains (value) : "value is not in the model";

		return element.getDomainModel ().contains (element)
			&& this.reference.addValue (element, value);
	}

	/**
	 * Remove the specified value from the specified <code>Element</code> to
	 * break the relationship.
	 * <p>
	 * This method removes the specified value from the
	 * <code>Collection</code> which contains all of the values for the
	 * relationship.
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
				this.reference.getElementClass ().getSimpleName (),
				this.reference.getProperty ().getName ());

		assert element != null : "element is NULL";
		assert value != null : "value is NULL";

		return this.reference.removeValue (element, value);
	}
}
