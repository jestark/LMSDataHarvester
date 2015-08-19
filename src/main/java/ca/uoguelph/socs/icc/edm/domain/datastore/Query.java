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
import ca.uoguelph.socs.icc.edm.domain.metadata.Receiver;
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

public abstract class Query<T extends Element>
{
	/**
	 * Factory to produce <code>Query</code> instances.
	 *
	 * @param   <T> The <code>Element</code> interface type of the <code>Query</code>
	 */

	private static final class QueryFactory<T extends Element> implements Receiver<T, Query<T>>
	{
		/** The <code>DataStore</code> to be used by the <code>Query</code> */
		private final DataStore datastore;

		/** The <code>Selector</code> which defines the <code>Query</code> */
		private final Selector selector;

		/**
		 * Create the <code>QueryFactory</code>, for the specified
		 * <code>Selector</code> on the specified <code>DataStore</code>.
		 *
		 * @param  datastore The <code>DataStore</code>
		 * @param  selector  The <code>Selector</code>
		 */

		public QueryFactory (final DataStore datastore, final Selector selector)
		{
			assert datastore != null : "datastore is NULL";
			assert selector != null : "selector is NULL";

			this.datastore = datastore;
			this.selector = selector;
		}

		/**
		 * Create the <code>Query</code> using the supplied
		 * <code>MetaData</code>.
		 *
		 * @param  metadata The <code>MetaData</code>, not null
		 * @param  type     The <code>Element</code> implementation class, not null
		 *
		 * @return          The <code>Query</code>
		 */

		@Override
		public <U extends T> Query<T> apply (final MetaData<T> metadata, final Class<U> type)
		{
			assert metadata != null : "metadata is NULL";
			assert type != null : "type is NULL";

			return new QueryImpl<T, U> (metadata, selector, type, datastore);
		}
	}

	/**
	 * Implementation of the <code>Query</code>.
	 *
	 * @param   T The <code>Element</code> interface type of the result
	 * @param   U The <code>Element</code> implementation type of the result
	 */

	private static final class QueryImpl<T extends Element, U extends T> extends Query<T>
	{
		/** The implementation type of the <code>Element</code> to fetch */
		private final Class<U> type;

		/**
		 * Create the <code>QueryImpl</code>.
		 *
		 * @param  metadata  The <code>MetaData</code>, not null
		 * @param  selector  The <code>Selector</code>, not null
		 * @param  type      The <code>Element</code> implementation class to
		 *                   retrieve, not null
		 * @param  datastore The <code>DataStore</code>, not null
		 */

		protected QueryImpl (final MetaData<T> metadata, final Selector selector, final Class<U> type, final DataStore datastore)
		{
			super (metadata, selector, datastore);

			assert type != null : "type is NULL";

			this.type = type;
		}

		/**
		 * Fetch a <code>List</code> of <code>Element</code> instances from the
		 * <code>DataStore</code> which match the specified query parameters.
		 * Values must be specified for all of the parameters for the
		 * <code>Query</code>.
		 *
		 * @return The <code>List</code> of <code>Element</code> instances
		 *         which match the parameters specified for the query.  The
		 *         <code>List</code> will be empty if no <code>Element</code>
		 *         instances match the <code>Query</code>.
		 */

		@Override
		public List<T> queryAll ()
		{
			this.log.trace ("queryAll:");

			assert this.checkValues () : "Some properties are missing values";

			return this.datastore.fetch (this.type, this.values);
		}
	}

	/** The logger for this Query instance */
	protected final Logger log;

	/** The <code>Selector</code> defining the <code>Query</code> */
	protected final Selector selector;

	/** The <code>DataStore</code> to query */
	protected final DataStore datastore;

	/** <code>Filter</code> to be built by the <code>Query</code> */
	protected final Filter<T> values;

	/**
	 * Create the <code>Query</code> for an <code>Element</code> implementation
	 * class.
	 *
	 * @param  creator   The <code>Creator</code>, not null
	 * @param  selector  The <code>Selector</code>, not null
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	public static final <T extends Element> Query<T> getInstance (final Creator<T> creator, final Selector selector, final DataStore datastore)
	{
		assert creator != null : "creator is NULL";
		assert selector != null : "selector is NULL";
		assert datastore != null : "datastore is NULL";

		return creator.inject (new QueryFactory<T> (datastore, selector));
	}

	/**
	 * Create the <code>Query</code> for an <code>Element</code> interface
	 * class.
	 *
	 * @param  metadata  The <code>MetaData</code>, not null
	 * @param  selector  The <code>Selector</code>, not null
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	public static final <T extends Element> Query<T> getInstance (final MetaData<T> metadata, final Selector selector, final DataStore datastore)
	{
		assert metadata != null : "metadata is NULL";
		assert selector != null : "selector is NULL";
		assert datastore != null : "datastore is NULL";

		return new QueryImpl<T, T> (metadata, selector, metadata.getElementType (), datastore);
	}

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

		this.values = new Filter<T> (metadata, this.selector);
	}

	/**
	 * Check that the value associated with each <code>Property</code> in the
	 * <code>Filter</code> has a non-null value.
	 *
	 * @return <code>true</code> all of the values are not <code>null</code>,
	 *         <code>false</code> otherwise
	 */

	protected boolean checkValues ()
	{
		return this.selector.getProperties ()
			.stream ()
			.map ((x) -> this.values.getValue (x))
			.noneMatch ((x) -> x == null);
	}

	/**
	 * Get the name of the <code>Query</code>.
	 *
	 * @return A <code>String</code> which identifies the <code>Query</code>.
	 */

	public final String getName ()
	{
		return this.selector.getName ();
	}

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances
	 * corresponding to the <code>Element</code>.
	 *
	 * @return A <code>Set</code> containing all of the <code>Property</code>
	 *         instances for the <code>Element</code>
	 */

	public final Set<Property<?>> getProperties ()
	{
		return this.selector.getProperties ();
	}

	/**
	 * Get the value for the specified property.
	 *
	 * @param  <V>      The type of the value associated with the property
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The value associated with the specified property
	 */

	public final <V> V getPropertyValue (final Property<V> property)
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

	public final <V> Query<T> setProperty (final Property<V> property, final V value)
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

	public final Query<T> setAllProperties (final T element)
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

	public abstract List<T> queryAll ();
}
