/* Copyright (C) 2014, 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.datastore;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Retrieve <code>Element</code> instances from a <code>DataStore</code>.  This
 * class builds a <code>Filter</code> based on the <code>Selector</code> and
 * the input values then uses the <code>Filter</code> to query
 * <code>Element</code> instances from the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>Element</code> returned by the query
 * @see     DataStore
 * @see     ca.uoguelph.socs.icc.edm.domain.Element
 */

public interface Query<T extends Element>
{
	/**
	 * Get the <code>Selector</code>, used to create the <code>Query</code>
	 *
	 * @return The <code>Selector</code>
	 */

	public abstract Selector<T> getSelector ();

	/**
	 * Get the value for the specified property.
	 *
	 * @param  <V>      The type of the value associated with the property
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The value associated with the specified property
	 */

	public abstract <V> V getValue (Property<T, V> property);

	/**
	 * Set the specified property to the specified value.
	 *
	 * @param  <V>                      The type of the value associated with
	 *                                  the property
	 * @param  property                 The <code>Property</code>, not null
	 * @param  value                    The value to set for the property
	 *
	 * @return                          A reference to this <code>Query</code>
	 * @throws IllegalArgumentException if the specified <code>Property</code>
	 *                                  is not a member of the
	 *                                  <code>Selector</code> for this
	 *                                  <code>Query</code>
	 */

	public abstract <V> Query<T> setValue (Property<T, V> property, final V value);

	/**
	 * Set all of the values for the <code>Property</code> instances associated
	 * with the <code>Query</code> from the specified <code>Element</code>
	 * instance.  This method loads the value associated with each
	 * <code>Property</code> in the <code>Query</code> from the specified
	 * <code>Element</code>.  For an <code>Element</code> instance which has
	 * multiple values for a <code>Property</code> in the <code>Query</code> the
	 * behaviour of this method is undefined.
	 *
	 * @param  element The <code>Element</code> instance, not null
	 *
	 * @return         A reference to this <code>Query</code>
	 */

	public abstract Query<T> setAllValues (T element);

	/**
	 * Fetch the <code>Element</code> instance from the <code>DataStore</code>
	 * which matches <code>Query</code>.
	 *
	 * @return                       The <code>Element</code> instance which
	 *                               matches the properties specified for the
	 *                               query, null if no <code>Element</code>
	 *                               instances exist in the
	 *                               <code>DataStore</code> that match the
	 *                               <code>Query</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if any <code>Property</code> instance
	 *                               associated with the <code>Query</code> has
	 *                               a null value
	 */

	public abstract T query ();

	/**
	 * Fetch a <code>List</code> of <code>Element</code> instances from the
	 * <code>DataStore</code> which match the <code>Query</code>.
	 *
	 * @return                       The <code>List</code> of
	 *                               <code>Element</code> instances which match
	 *                               the query.  The <code>List</code> will be
	 *                               empty if no <code>Element</code> instances
	 *                               match the <code>Query</code>.
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if any <code>Property</code> instance
	 *                               associated with the <code>Query</code> has
	 *                               a null value
	 */

	public abstract List<T> queryAll ();

	/**
	 * Get a <code>Stream</code> of <code>Element</code> instances from the
	 * <code>DataStore</code> which match the <code>Query</code>.
	 *
	 * @return                       A <code>Stream</code> containing the
	 *                               <code>Element</code> instances with match
	 *                               the <code>Query</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if any <code>Property</code> instance
	 *                               associated with the <code>Query</code> has
	 *                               a null value
	 */

	public abstract Stream<T> stream ();
}
