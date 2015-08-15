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

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Representation of a relationship for an <code>Element</code> with a
 * cardinality greater than one.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the owning <code>Element</code>
 * @param   <V> The type of the associated <code>Element</code>
 */

final class MultiRelationship<T extends Element, V extends Element> extends Relationship<T, V>
{
	/** The <code>Property</code> for the relationship that id being manipulated */
	private final Property<V> property;

	/** The <code>RelationshipReference</code> used to manipulate the element */
	private final RelationshipReference<T, V> reference;

	/**
	 * Create the <code>MultiRelationship</code>.
	 *
	 * @param  type      The <code>Element</code> interface class, not null
	 * @param  property  The <code>Property</code>, not null
	 * @param  reference The <code>RelationshipReference</code>, not null
	 */

	protected MultiRelationship (final Class<T> type, final Property<V> property, final RelationshipReference<T, V> reference)
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
	 * For the multi-valued relationship, a new relationship can always be
	 * inserted
	 *
	 * @param  element The <code>Element</code> instance to test, not null
	 *
	 * @return         <code>true</code> if the relationship can be safely
	 *                 inserted, <code>false</code> otherwise
	 */

	@Override
	protected boolean canInsert (final DataStore datastore, final T element)
	{
		this.log.trace ("canInsert: element={}", element);

		assert datastore != null : "datastore is null";
		assert element != null : "element is NULL";
		assert datastore.contains (element) : "element is not in the datastore";

		return datastore.contains (element);
	}

	/**
	 * Determine if a value can be safely removed from the <code>Element</code>
	 * to break the relationship.  It is generally safe to remove a
	 * relationship if the <code>Element</code> represented does not require
	 * the relationship for unique identification.
	 * <p>
	 * For the multi-valued relationship, a relationship can always be broken.
	 *
	 * @return <code>true</code> if the relationship can be safely removed,
	 *         <code>false</code> otherwise
	 */

	@Override
	protected boolean canRemove ()
	{
		this.log.trace ("canRemove:");

		return true;
	}

	/**
	 * Insert the specified value into the specified <code>Element</code> to
	 * create the relationship.
	 * <p>
	 * This method adds the specified value to the <code>Collection</code>
	 * which contains all of the values for the relationship.  Note that it may
	 * be possible to add the same value to an <code>Element</code> multiple
	 * times, depending of the implementation of the <code>Element</code>.  The
	 * called must ensure that this does not happen.
	 *
	 * @param  element The <code>Element</code> to operate on, not null
	 * @param  value   The <code>Element</code> to be inserted, not null
	 *
	 * @return         <code>true</code> if the value was successfully
	 *                 inserted, <code>false</code> otherwise
	 */

	@Override
	protected boolean insert (final DataStore datastore, final T element, final V value)
	{
		this.log.trace ("insert: element={}, value={}", element, value);

		assert datastore != null : "datastore is null";
		assert element != null : "element is NULL";
		assert value != null : "value is NULL";
		assert datastore.contains (element) : "element is not in the datastore";
		assert datastore.contains (value) : "value is not in the datastore";

		return datastore.contains (element) && this.reference.addValue (element, value);
	}

	/**
	 * Remove the specified value from the specified <code>Element</code> to
	 * break the relationship.
	 * <p>
	 * This method removes the specified value from the <code>Collection</code>
	 * which contains all of the values for the relationship.
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

		assert element != null : "element is NULL";
		assert value != null : "value is NULL";

		return this.reference.removeValue (element, value);
	}

	/**
	 * Determine if the relationship represented by this
	 * <code>Relationship</code> instance can be safely connected for the
	 * specified <code>Element</code> instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> to process, not null
	 *
	 * @return           <code>true</code> if the relationship can be created,
	 *                   <code>false</code> otherwise
	 */

	@Override
	public boolean canConnect (final DataStore datastore, final T element)
	{
		this.log.trace ("canConnect: element={}", element);

		assert datastore != null : "datastore is null";
		assert element != null : "element is NULL";

		return this.reference.getValue (element)
			.stream ()
			.allMatch (x -> this.getInverse (x.getClass ()).canInsert (datastore, x));
	}

	/**
	 * Determine if the relationship represented by this
	 * <code>Relationship</code> instance can be safely disconnected for the
	 * specified <code>Element</code> instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> to process, not null
	 *
	 * @return           <code>true</code> if the relationship can be broken,
	 *                   <code>false</code> otherwise
	 */

	@Override
	public boolean canDisconnect (final DataStore datastore, final T element)
	{
		this.log.trace ("canDisconnect: element={}", element);

		assert datastore != null : "datastore is null";
		assert element != null : "element is NULL";
		assert datastore.contains (element) : "element is not in the datastore";

		return datastore.contains (element) && this.reference.getValue (element)
			.stream ()
			.allMatch (x -> this.getInverse (x.getClass ()).canRemove ());
	}

	/**
	 * Connect the relationship represented by this <code>Relationship</code>
	 * instance for the specified <code>Element</code> instance.  This method
	 * will write the inverse relationship into the <code>Element</code>
	 * instance for the <code>Element</code> on the other side of the
	 * relationship.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> to process, not null
	 *
	 * @return           <code>true</code> if the relationship was successfully
	 *                   created <code>false</code> otherwise
	 */

	@Override
	public boolean connect (final DataStore datastore, final T element)
	{
		this.log.trace ("connect: element={}", element);

		assert datastore != null : "datastore is null";
		assert element != null : "element is NULL";
		assert datastore.contains (element) : "element is not in the datastore";

		return datastore.contains (element) && this.reference.getValue (element)
			.stream ()
			.allMatch (x -> this.getInverse (x.getClass ()).insert (datastore, x, element));
	}

	/**
	 * Disconnect the relationship represented by this
	 * <code>Relationship</code> instance for the specified
	 * <code>Element</code> instance.  This method will remove the inverse
	 * relationship from the <code>Element</code> instance for the
	 * <code>Element</code> on the other side of the relationship.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> to process, not null
	 *
	 * @return           <code>true</code> if the relationship was successfully
	 *                   broken <code>false</code> otherwise
	 */

	@Override
	public boolean disconnect (final DataStore datastore, final T element)
	{
		this.log.trace ("disconnect: element={}", element);

		assert datastore != null : "datastore is null";
		assert element != null : "element is NULL";
		assert datastore.contains (element) : "element is not in the datastore";

		return datastore.contains (element) && this.reference.getValue (element)
			.stream ()
			.allMatch (x -> this.getInverse (x.getClass ()).remove (x, element));
	}
}
