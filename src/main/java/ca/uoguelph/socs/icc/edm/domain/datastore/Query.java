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

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.element.metadata.Property;

/**
 * Interface for retrieving <code>Element</code> instances from the
 * <code>DataStore</code>.  This interface defines the common methods required
 * for retrieving <code>Element</code> instances from the
 * <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 2.0
 * @param   <T> The type of <code>Element</code> returned by the query
 * @see     DataStore
 * @see     ca.uoguelph.socs.icc.edm.domain.Element
 */

public interface Query<T extends Element>
{
	/**
	 * Get the type of the <code>Element</code> returned by this
	 * <code>Query</code>.
	 *
	 * @return The interface type of the <code>Element</code> returned by this
	 *         <code>Query</code>
	 */

	public abstract Class<T> getElementType ();

	/**
	 * Get the implementation class of the <code>Element</code> returned by
	 * this <code>Query</code>.
	 *
	 * @return The implementation type of the <code>Element</code> returned by
	 *         this <code>Query</code>
	 */

	public abstract Class<?> getElementClass ();

	/**
	 * Get the name of the <code>Query</code>.
	 *
	 * @return A <code>String</code> which identifies the <code>Query</code>.
	 */

	public abstract String getName ();

	/**
	 * Get the <code>Property</code> associated with the <code>Element</code>
	 * with the specified name.
	 *
	 * @param  name The name of the <code>Property</code>
	 *
	 * @return      The specified <code>Property</code> instance or
	 *              <code>null</code> if a <code>Property</code> with the
	 *              specified name does not exist
	 */

	public abstract Property<?> getProperty (String name);

	/**
	 * Get the <code>Property</code> associated with the <code>Element</code>
	 * with the specified name and value type.
	 *
	 * @param  name The name of the <code>Property</code>
	 * @param  type The type of the value associated with the
	 *              <code>Property</code>
	 *
	 * @return      The specified <code>Property</code> instance or
	 *              <code>null</code> if a <code>Property</code> with the
	 *              specified name and type does not exist
	 */

	public abstract <V> Property<V> getProperty (String name, Class<V> type);

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances
	 * corresponding to the <code>Element</code>.
	 *
	 * @return A <code>Set</code> containing all of the <code>Property</code>
	 *         instances for the <code>Element</code>
	 */

	public abstract Set<Property<?>> getProperties ();

	/**
	 * Get the value for the specified property.
	 *
	 * @param  <V>      The type of the value associated with the property
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The value associated with the specified property
	 */

	public abstract <V> V getPropertyValue (Property<V> property);

	/**
	 * Set the specified property to the specified value.
	 *
	 * @param  <V>      The type of the value associated with the property
	 * @param  property The <code>Property</code>, not null
	 * @param  value    The value to set for the property
	 */

	public abstract <V> void setProperty (Property<V> property, V value);

	/**
	 * Fetch the <code>Element</code> instance from the <code>DataStore</code>
	 * which matches the properties set on the <code>Query</code>.  Values must
	 * be specified for all of the properties for the <code>Query</code>.
	 *
	 * @return The <code>Element</code> instance which matches the properties
	 *         specified for the query, null if no <code>Element</code>
	 *         instances exist in the <code>DataStore</code> that match the
	 *         <code>Query</code>
	 */

	public abstract T query ();

	/**
	 * Fetch a <code>List</code> of <code>Element</code> instances from the
	 * <code>DataStore</code> which match the specified query properties.
	 * Values must be specified for all of the properties for the
	 * <code>Query</code>.
	 *
	 * @return The <code>List</code> of <code>Element</code> instances which
	 *         match the properits specified for the query.  The
	 *         <code>List</code> will be empty if no <code>Element</code>
	 *         instances match the <code>Query</code>.
	 */

	public abstract List<T> queryAll ();
}
