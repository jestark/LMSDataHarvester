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

/**
 * Representation of a inverse relationship for an <code>Element</code> with
 * a cardinality of one.
 *
 * @version 1.0
 * @param   <T> The type of the owning <code>Element</code>
 * @param   <V> The type of the associated <code>Element</code>
 */

final class SingleInverseRelationship<T extends Element, V extends Element> implements InverseRelationship<T, V>
{
	/** The Logger */
	private final Logger log;

	/** The assocated <code>PropertyReference</code> */
	private final Accessor<T, V> reference;

	/**
	 * Create the <code>SingleInverseRelationship</code> from the specified
	 * values.
	 *
	 * @param  reference The <code>Accessor</code>, not null
	 */

	public static <T extends Element, V extends Element> SingleInverseRelationship<T, V> of (
			final Accessor<T, V> reference)
	{
		assert reference != null : "reference is NULL";

		return new SingleInverseRelationship<T, V> (reference);
	}

	/**
	 * Create the <code>SingleInverse</code>.
	 *
	 * @param  reference The <code>Accessor</code>, not null
	 */

	private SingleInverseRelationship (final Accessor<T, V> reference)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.reference = reference;
	}

	/**
	 * Insert the specified value into the specified <code>Element</code> to
	 * create the relationship.
	 * <p>
	 * This method sets the value of the associated field to the specified
	 * value, if the value of the associated field is <code>null</code>. If the
	 * field is not-null then the operation will fail, returning
	 * <code>false</code>.
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

		boolean result = false;

		if (element.getDomainModel ().contains (element) && this.reference.getValue (element) == null)
		{
			this.reference.setValue (element, value);
			result = true;
		}

		return result;
	}

	/**
	 * Remove the specified value from the specified <code>Element</code> to
	 * break the relationship.
	 * <p>
	 * This method sets the value of the associated field to <code>null</code>,
	 * if the value of the associated is equal to the specified value and the
	 * field if not required to uniquely identify the <code>Element</code>
	 * instance.
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
		assert value == this.reference.getValue (element) : "The value to be removed is not equal to the value in the element";

		boolean result = false;

		if ((! this.reference.getProperty ().hasFlags (Property.Flags.REQUIRED))
				&& (value == this.reference.getValue (element)))
		{
			this.reference.setValue (element, null);
			result = true;
		}

		return result;
	}
}
