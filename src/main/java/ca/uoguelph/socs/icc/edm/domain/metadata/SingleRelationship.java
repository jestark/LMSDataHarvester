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

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Representation of a relationship for an <code>Element</code> with a
 * cardinality of one.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the owning <code>Element</code>
 * @param   <V> The type of the associated <code>Element</code>
 */

final class SingleRelationship<T extends Element, V extends Element> extends Relationship<T, V>
{
	/** The <code>Property</code> for the relationship that id being manipulated */
	private final Property<V> property;

	/** The assocated <code>PropertyReference</code> */
	private final SingleReference<T, V> reference;

	/**
	 * Create the <code>SingleRelationShip</code>.
	 *
	 * @param  type      The <code>Element</code> interface class, not null
	 * @param  property  The <code>Property</code>, not null
	 * @param  reference The <code>PropertyReference</code>, not null
	 */

	protected SingleRelationship (final Class<T> type, final Property<V> property, final SingleReference<T, V> reference)
	{
		super (type, property.getPropertyType ());

		assert reference != null : "reference is NULL";

		this.property = property;
		this.reference = reference;
	}

	/**
	 * Determine if a value can be inserted into the <code>Element</code> to
	 * create a relationship.  It is generally safe to insert a value into an
	 * <code>Element</code> if the <code>Element</code> allows multiple values
	 * for the relationship, of if the <code>Element</code> instance does not
	 * currently have a value for the relationship.
	 * <p>
	 * For the single-valued relationship, a new relationship can only be
	 * created with another <code>Element</code> if the existing value is
	 * <code>null</code>.
	 *
	 * @param  model   The <code>DomainModel</code>, not null
	 * @param  element The <code>Element</code> instance to test, not null
	 *
	 * @return         <code>true</code> if the relationship can be safely
	 *                 inserted, <code>false</code> otherwise
	 */

	@Override
	protected boolean canInsert (final DomainModel model, final T element)
	{
		this.log.trace ("canInsert: element={}", element);

		assert model != null : "model is null";
		assert element != null : "element is NULL";
		assert model.contains (element) : "element is not in the model";

		return model.contains (element) && this.reference.getValue (element) == null;
	}

	/**
	 * Determine if a value can be safely removed from the <code>Element</code>
	 * to break the relationship.  It is generally safe to remove a
	 * relationship if the <code>Element</code> represented does not require
	 * the relationship for unique identification.
	 * <p>
	 * For the single valued relationship, a relationship can be broken if it
	 * is not required, as specified in the associated <code>Property</code>.
	 *
	 * @return <code>true</code> if the relationship can be safely removed,
	 *         <code>false</code> otherwise
	 */

	@Override
	protected boolean canRemove ()
	{
		this.log.trace ("canRemove:");

		return ! this.property.hasFlags (Property.Flags.REQUIRED);
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
	 * @param  model   The <code>DomainModel</code>, not null
	 * @param  element The <code>Element</code> to operate on, not null
	 * @param  value   The <code>Element</code> to be inserted, not null
	 *
	 * @return         <code>true</code> if the value was successfully
	 *                 inserted, <code>false</code> otherwise
	 */

	@Override
	protected boolean insert (final DomainModel model, final T element, final V value)
	{
		this.log.trace ("insert: element={}, value={}", element, value);
		this.log.debug ("inserting Relationship: {} -> {}", this.type.getSimpleName (), this.value.getSimpleName ());

		assert model != null : "model is null";
		assert element != null : "element is NULL";
		assert value != null : "value is NULL";
		assert model.contains (element) : "element is not in the model";
		assert model.contains (value) : "value is not in the model";

		boolean result = false;

		if (model.contains (element) && this.reference.getValue (element) == null)
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
	protected boolean remove (final T element, final V value)
	{
		this.log.trace ("remove: element={}, value={}", element, value);
		this.log.debug ("removing Relationship: {} -> {}", this.type.getSimpleName (), this.value.getSimpleName ());

		assert element != null : "element is NULL";
		assert value != null : "value is NULL";
		assert value == this.reference.getValue (element) : "The value to be removed is not equal to the value in the element";

		boolean result = false;

		if ((! this.property.hasFlags (Property.Flags.REQUIRED))
				&& (value == this.reference.getValue (element)))
		{
			this.reference.setValue (element, null);
			result = true;
		}

		return result;
	}

	/**
	 * Determine if the relationship represented by this
	 * <code>Relationship</code> instance can be safely connected for the
	 * specified <code>Element</code> instance.
	 *
	 * @param  model   The <code>DomainModel</code>, not null
	 * @param  element The <code>Element</code> to process, not null
	 *
	 * @return         <code>true</code> if the relationship can be created,
	 *                 <code>false</code> otherwise
	 */

	@Override
	public boolean canConnect (final DomainModel model, final T element)
	{
		this.log.trace ("canConnect: element={}", element);

		assert model != null : "model is null";
		assert element != null : "element is NULL";

		V value = this.reference.getValue (element);

		return (value == null) || (this.getInverse (value.getClass ())
			.canInsert (model, value));
	}

	/**
	 * Determine if the relationship represented by this
	 * <code>Relationship</code> instance can be safely disconnected for the
	 * specified <code>Element</code> instance.
	 *
	 * @param  model   The <code>DomainModel</code>, not null
	 * @param  element The <code>Element</code> to process, not null
	 *
	 * @return         <code>true</code> if the relationship can be broken,
	 *                 <code>false</code> otherwise
	 */

	@Override
	public boolean canDisconnect (final DomainModel model, final T element)
	{
		this.log.trace ("canDisconnect: element={}", element);

		assert model != null : "model is null";
		assert element != null : "element is NULL";

		return this.getInverse (this.reference.getValue (element).getClass ()).canRemove ();
	}

	/**
	 * Connect the relationship represented by this <code>Relationship</code>
	 * instance for the specified <code>Element</code> instance.  This method
	 * will write the inverse relationship into the <code>Element</code>
	 * instance for the <code>Element</code> on the other side of the
	 * relationship.
	 *
	 * @param  model   The <code>DomainModel</code>, not null
	 * @param  element The <code>Element</code> to process, not null
	 *
	 * @return         <code>true</code> if the relationship was successfully
	 *                 created <code>false</code> otherwise
	 */

	@Override
	public boolean connect (final DomainModel model, final T element)
	{
		this.log.trace ("connect: element={}", element);
		this.log.debug ("Connecting Relationship: {} -> {}", this.type.getSimpleName (), this.value.getSimpleName ());

		assert model != null : "model is null";
		assert element != null : "element is NULL";
		assert model.contains (element) : "element is not in the model";

		V value = this.reference.getValue (element);

		return (value == null)
			|| (model.contains (element)
			&& this.getInverse (value.getClass ()).insert (model, value, element));
	}

	/**
	 * Disconnect the relationship represented by this
	 * <code>Relationship</code> instance for the specified
	 * <code>Element</code> instance.  This method will remove the inverse
	 * relationship from the <code>Element</code> instance for the
	 * <code>Element</code> on the other side of the relationship.
	 *
	 * @param  model   The <code>DomainModel</code>, not null
	 * @param  element The <code>Element</code> to process, not null
	 *
	 * @return         <code>true</code> if the relationship was successfully
	 *                 broken <code>false</code> otherwise
	 */

	@Override
	public boolean disconnect (final DomainModel model, final T element)
	{
		this.log.trace ("disconnect: element={}", element);
		this.log.debug ("Disconnecting Relationship: {} -> {}", this.type.getSimpleName (), this.value.getSimpleName ());

		assert model != null : "model is null";
		assert element != null : "element is NULL";
		assert model.contains (element) : "element is not in the model";

		V value = this.reference.getValue (element);

		return (value == null)
			|| (model.contains (element)
			&& this.getInverse (value.getClass ()).remove (value, element));
	}
}
