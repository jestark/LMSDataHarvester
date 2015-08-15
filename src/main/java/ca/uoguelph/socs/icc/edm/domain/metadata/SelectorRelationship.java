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

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * A representation of a uni-directional relationship.  Specifically, instances
 * of this class represent the side of the relationship which can not see the
 * other side.  A <code>Selector</code> is used to represent the
 * <code>Element</code> and retrieve instances from the <code>DataStore</code>
 * to perform the necessary checks.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the owning <code>Element</code>
 * @param   <V> The type of the associated <code>Element</code>
 */

final class SelectorRelationship<T extends Element, V extends Element> extends Relationship<T, V>
{
	/** The <code>Property</code> for owning <code>Element</code> */
	private final Property<T> property;

	/** The <code>Selector</code> for the associated <code>Element</code> */
	private final Selector<V> selector;

	/**
	 * Create the <code>SingleRelationship</code>.
	 *
	 * @param  property The <code>Property</code>, not null
	 * @param  selector The <code>Selector</code>, not null
	 */

	protected SelectorRelationship (final Property<T> property, final Selector<V> selector)
	{
		super (property.getPropertyType (), selector.getElementType ());

		this.property = property;
		this.selector = selector;
	}

	/**
	 * Determine if a value can be inserted into the <code>Element</code> to
	 * create a relationship.  It is generally safe to insert a value into an
	 * <code>Element</code> if the <code>Element</code> allows multiple values
	 * for the relationship, of if the <code>Element</code> instance does not
	 * currently have a value for the relationship.
	 * <p>
	 * For a uni-directional relationship, a relationship can be added if
	 * either the <code>Selector</code> is not expected to return a unique
	 * instance of the <code>Element</code>, or
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> instance to test, not null
	 *
	 * @return           <code>true</code> if the relationship can be safely
	 *                   inserted, <code>false</code> otherwise
	 */

	@Override
	protected boolean canInsert (final DataStore datastore, final T element)
	{
		this.log.trace ("canInsert: element={}", element);

		assert datastore != null : "datastore is null";
		assert element != null : "element is NULL";
		assert datastore.contains (element) : "element is not in the datastore";

		return datastore.contains (element) && ((! this.selector.isUnique ()) || datastore.getQuery (this.selector)
			.setProperty (this.property, element)
			.queryAll ()
			.isEmpty ());
	}

	/**
	 * Determine if a value can be safely removed from the <code>Element</code>
	 * to break the relationship.  It is generally safe to remove a
	 * relationship if the <code>Element</code> represented does not require
	 * the relationship for unique identification.
	 * <p>
	 * For the uni-directional relationship, a relationship can always be
	 * broken.
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
	 * This method is a same as <code>canInsert</code> as there is nothing to
	 * insert in a uni-directional relationship.
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
		this.log.trace ("insert: element={}, value={}");

		assert datastore != null : "datastore is null";
		assert element != null : "element is NULL";
		assert value != null : "value is NULL";
		assert datastore.contains (element) : "element is not in the datastore";
		assert datastore.contains (value) : "value is not in the datastore";

		return this.canInsert (datastore, element);
	}

	/**
	 * Remove the specified value from the specified <code>Element</code> to
	 * break the relationship.
	 * <p>
	 * This method is a no-op (unconditionally returning <code>true</code>) as
	 * there is nothing to remove from a uni-directional relationship.
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
		this.log.trace ("remove: element={}, value={}");

		assert element != null : "element is NULL";
		assert value != null : "value is NULL";

		return true;
	}

	/**
	 * Determine if the relationship represented by this
	 * <code>Relationship</code> instance can be safely connected for the
	 * specified <code>Element</code> instance.
	 * <p>
	 * For a uni-directional relationship this will always be true as there is
	 * nothing to connect.
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

		return true;
	}

	/**
	 * Determine if the relationship represented by this
	 * <code>Relationship</code> instance can be safely disconnected for the
	 * specified <code>Element</code> instance.
	 * <p>
	 * For a uni-directional relationship, the <code>Element</code> may be
	 * removed if the other side is not dependant on it, or if there are no
	 * associated <code>Element</code> instances in the <code>DataStore</code>.
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

		return datastore.contains (element) && datastore.getQuery (this.selector)
			.setProperty (this.property, element)
			.queryAll ()
			.stream ()
			.allMatch (x -> this.getInverse (x.getClass ()).canRemove ());
	}

	/**
	 * Connect the relationship represented by this <code>Relationship</code>
	 * instance for the specified <code>Element</code> instance.
	 * <p>
	 * For a uni-directional relationship, there is nothing to do, so this is a
	 * no-op.
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

		return datastore.contains (element);
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

		return datastore.contains (element) && datastore.getQuery (this.selector)
			.setProperty (this.property, element)
			.queryAll ()
			.stream ()
			.allMatch (x -> this.getInverse (x.getClass ()).remove (x, element));
	}
}
