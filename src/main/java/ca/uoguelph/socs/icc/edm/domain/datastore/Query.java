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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.metadata.Creator;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Retrieve <code>Element</code> instances from a <code>DataStore</code>.  This
 * class builds a <code>Filter</code> based on the <code>Selector</code> and
 * the input values then uses the <code>Filter</code> to query
 * <code>Element</code> instances from the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 2.0
 * @param   <T> The type of <code>Element</code> returned by the query
 * @see     DataStore
 * @see     ca.uoguelph.socs.icc.edm.domain.Element
 */

public final class Query<T extends Element>
{
	/** The logger for this Query instance */
	private final Logger log;

	/** The implementation type of the <code>Element</code> to fetch */
	private final Class<? extends T> type;

	/** The <code>Selector</code> defining the <code>Query</code> */
	private final Selector selector;

	/** The <code>DataStore</code> to query */
	private final DataStore datastore;

	/** <code>Filter</code> to be built by the <code>Query</code> */
	private final Filter<T> values;

	/**
	 * Create the <code>Query</code>.
	 *
	 * @param  metadata  The <code>MetaData</code>, not null
	 * @param  selector  The <code>Selector</code>, not null
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	protected Query (final MetaData<T> metadata, final Selector selector, final DataStore datastore)
	{
		assert metadata != null : "metadata is NULL";
		assert selector != null : "selector is NULL";
		assert datastore != null : "datastore is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.selector = selector;
		this.datastore = datastore;

		this.type = metadata.getElementClass ();

		this.values = new Filter<T> (metadata, this.selector);
	}

	/**
	 * Check that the value associated with each <code>Property</code> in the
	 * <code>Filter</code> has a non-null value.
	 *
	 * @return <code>true</code> all of the values are not <code>null</code>,
	 *         <code>false</code> otherwise
	 */

	private boolean checkValues ()
	{
		return this.selector.getProperties ()
			.stream ()
			.map ((x) -> this.values.getValue (x))
			.noneMatch ((x) -> x == null);
	}

	/**
	 * Get the <code>Selector</code>, used to create the <code>Query</code>
	 *
	 * @return The <code>Selector</code>
	 */

	public Selector getSelector ()
	{
		return this.selector;
	}

	/**
	 * Get the value for the specified property.
	 *
	 * @param  <V>      The type of the value associated with the property
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The value associated with the specified property
	 */

	public final <V> V getValue (final Property<V> property)
	{
		assert property != null : "property is NULL";
		assert (this.selector.getProperties ().contains (property)) : "invalid property";

		return this.values.getValue (property);
	}

	/**
	 * Set the specified property to the specified value.
	 *
	 * @param  <V>      The type of the value associated with the property
	 * @param  property The <code>Property</code>, not null
	 * @param  value    The value to set for the property
	 *
	 * @return         A reference to this <code>Query</code>
	 */

	public final <V> Query<T> setValue (final Property<V> property, final V value)
	{
		this.log.trace ("setProperty: property={}, value={}", property, value);

		assert property != null : "property is NULL";
		assert value != null : "value is NULL";
		assert (this.selector.getProperties ().contains (property)) : "invalid property";

		this.values.setValue (property, value);

		return this;
	}

	/**
	 * Set all of the values for the <code>Property</code> instances associated
	 * with the <code>Query</code> from the specified <code>Element</code>
	 * instance.
	 *
	 * @param  element The <code>Element</code> instance, not null
	 *
	 * @return         A reference to this <code>Query</code>
	 */

	public final Query<T> setAllValues (final T element)
	{
		this.log.trace ("setAllProperties: element={}", element);

		assert element != null : "element is NULL";

		this.values.setAllValues (element);

		return this;
	}

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

	public final T query ()
	{
		this.log.trace ("query:");

		assert this.selector.isUnique () : "This query will not return unique results";

		final List<T> results = this.queryAll ();
		T result = null;

		if (results.size () > 1)
		{
			this.log.error ("Query returned multiple results");
			throw new IllegalStateException ("Query returned multiple results");
		}
		else if (results.size () == 1)
		{
			result = results.get (0);
		}

		return result;
	}

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

	public List<T> queryAll ()
	{
		this.log.trace ("queryAll:");

		assert this.checkValues () : "Some properties are missing values";

		return this.datastore.fetch (this.type, this.values);
	}

}
