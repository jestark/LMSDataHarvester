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

package ca.uoguelph.socs.icc.edm.domain.datastore;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Implementation of the <code>Query</code> interface.  This class builds a
 * <code>Filter</code> based on the <code>Selector</code> and the input values
 * then uses the <code>Filter</code> to query <code>Element</code> instances
 * from the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   T The <code>Element</code> interface type of the result
 * @param   U The <code>Element</code> implementation type of the result
 */

public final class QueryImpl<T extends Element, U extends T> implements Query<T>
{
	/** The logger for this Query instance */
	private final Logger log;

	/** The <code>MetaData</code> for the <code>Element</code> being queried*/
	private final MetaData<T, U> metadata;

	/** The <code>Selector</code> defining the <code>Query</code> */
	private final Selector selector;

	/** The <code>DataStore</code> <code>Backend</code> to query */
	private final Backend backend;

	/** <code>Filter</code> to be built by the <code>Query</code> */
	private final Filter<T, U> values;

	/**
	 * Create the <code>QueryImpl</code>.
	 *
	 * @param  metadata The <code>MetaData</code>, not null
	 * @param  selector The <code>Selector</code>, not null
	 * @param  backend The <code>Backend</code>, not null
	 */

	protected QueryImpl (final MetaData<T, U> metadata, final Selector selector, final Backend backend)
	{
		assert metadata != null : "metadata is NULL";
		assert selector != null : "selector is NULL";
		assert backend != null : "backend is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.metadata = metadata;
		this.selector = selector;
		this.backend = backend;

		this.values = new Filter<T, U> (this.metadata, this.selector);
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
	 * Get the <code>Set</code> of <code>Property</code> instances
	 * corresponding to the <code>Element</code>.
	 *
	 * @return A <code>Set</code> containing all of the <code>Property</code>
	 *         instances for the <code>Element</code>
	 */

	@Override
	public final Set<Property<?>> getProperties ()
	{
		return this.selector.getProperties ();
	}

	/**
	 * Get the name of the <code>Query</code>.
	 *
	 * @return A <code>String</code> which identifies the <code>Query</code>.
	 */

	@Override
	public final String getName ()
	{
		return this.selector.getName ();
	}

	/**
	 * Get the value for the specified property.
	 *
	 * @param  <V>      The type of the value associated with the property
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The value associated with the specified property
	 */

	@Override
	public <V> V getPropertyValue (final Property<V> property)
	{
		assert property != null : "property is NULL";
		assert (this.selector.getProperties ().contains (property)) : "invalid property";

		return this.values.getValue (property);
	}

	/**
	 * Set a value to be associated queried for the specified
	 * <code>Property</code>.
	 *
	 * @param  property                 The <code>Property</code>, not null
	 * @param  value                    The value, not null
	 *
	 * @return                          This <code>Query</code>
	 * @throws IllegalArgumentException if the specified parameter does not
	 *                                  exist
	 */

	@Override
	public <V> Query<T> setProperty (final Property<V> property, final V value)
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
	 * @return         This <code>Query</code>
	 */

	@Override
	public Query<T> setAllProperties (final T element)
	{
		this.log.trace ("setAllProperties: element={}", element);

		assert element != null : "element is NULL";

		this.values.setAllValues (element);

		return this;
	}

	/**
	 * Fetch the <code>Element</code> instance from the <code>DataStore</code>
	 * which matches the parameters set on the <code>Query</code>.  Values must
	 * be specified for all of the parameters for the <code>Query</code>.
	 *
	 * @return The <code>Element</code> instance which matches the parameters
	 *         specified for the query, null if no <code>Element</code>
	 *         instances exist in the <code>DataStore</code> that match the
	 *         <code>Query</code>
	 */

	@Override
	public T query ()
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
			result = results.get (1);
		}

		return result;
	}

	/**
	 * Fetch a <code>List</code> of <code>Element</code> instances from the
	 * <code>DataStore</code> which match the specified query parameters.
	 * Values must be specified for all of the parameters for the
	 * <code>Query</code>.
	 *
	 * @return The <code>List</code> of <code>Element</code> instances which
	 *         match the parameters specified for the query.  The
	 *         <code>List</code> will be empty if no <code>Element</code>
	 *         instances match the <code>Query</code>.
	 */

	@Override
	public List<T> queryAll ()
	{
		this.log.trace ("queryAll:");

		assert this.checkValues () : "Some properties are missing values";

		return this.backend.fetch (this.values);
	}
}
