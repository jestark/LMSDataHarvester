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

package ca.uoguelph.socs.icc.edm.domain.loader;

import java.util.List;
import java.util.Map;

import java.util.HashMap;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementLoader;

import ca.uoguelph.socs.icc.edm.domain.datastore.AbstractQuery;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

import ca.uoguelph.socs.icc.edm.domain.element.metadata.Selector;

/**
 * Top level interface for all operations involving the domain model and the
 * underlying data-store.  This class and its subclasses are responsible for
 * all operations related to adding, retrieving or removing elements from the
 * domain model and its underlying data-store.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>Element</code> to be processed
 */

public abstract class AbstractLoader<T extends Element> implements ElementLoader<T>
{
	/** <code>ElementLoader</code> factories */
	private static final Map<Class<?>, Function<DataStore, ?>> factories;

	/** The logger */
	protected final Logger log;

	/** The type of <code>Element</code> which is being managed */
	private final Class<T> type;

	/** The <code>DomainModel</code> instance which owns this Loader. */
	protected final DataStore datastore;

	/**
	 * static initializer to create the factory.
	 */

	static
	{
		factories = new HashMap<Class<?>, Function<DataStore, ?>> ();
	}

	/**
	 * Get an instance of an <code>ElementLoader</code> interface for the
	 * specified <code>DataStore</code>.  This method is intended to be used by
	 * the <code>DomainModel</code> and <code>ElementLoader</code> instances
	 * to get an instance of the requeested <code>ElementLoader</code> as
	 * required.
	 *
	 * @param  <T>       The type of <code>ElementLoader</code> to return
	 * @param  <U>       The type of <code>Element</code> returned by the
	 *                   <code>ElementLoader</code>
	 * @param  element   The <code>Element</code> implementation class, not null
	 * @param  datastore The <code>DataStore</code> upon which the
	 *                   <code>ElementLoader</code> instance is to operate,
	 *                   not null
	 *
	 * @return           An instance of the requested
	 *                   <code>ElementLoader</code> interface on the specified
	 *                   <code>DataStore</code>
	 */

	@SuppressWarnings("unchecked")
	public static final <T extends ElementLoader<U>, U extends Element> T getInstance (final Class<U> element, final DataStore datastore)
	{
		assert element != null : "element is NULL";
		assert datastore != null : "datastore is NULL";

		assert AbstractLoader.factories.containsKey (element) : "No ElementLoader registered for " + element.getSimpleName ();

		return (T) (AbstractLoader.factories.get (element)).apply (datastore);
	}

	/**
	 * Register an <code>ElementLoader</code> implementation with the factory.
	 * This method is intended to be used by the <code>ElementLoader</code>
	 * implementations to register themselves with the factory so that they may
	 * be instantiated on demand.
	 *
	 * @param  <T>     The type of <code>ElementLoader</code> to register
	 * @param  <U>     The type of <code>Element</code> returned by the
	 *                 <code>ElementLoader</code>
	 * @param  element The <code>Element</code> interface class, not null
	 * @param  factory Method reference to the <code>ElementLoader</code>
	 *                 implementation class constructor, not null
	 */

	protected static final <T extends ElementLoader<U>, U extends Element> void registerLoader (final Class<U> element, final Function<DataStore, T> factory)
	{
		assert element != null : "element is NULL";
		assert factory != null : "factory is NULL";

		assert (! AbstractLoader.factories.containsKey (element)) : "Class already registered: " + element.getSimpleName ();

		AbstractLoader.factories.put (element, factory);
	}

	/**
	 * Create the <code>AbstractLoader</code>.
	 *
	 * @param  type      The type of <code>Element</code> which is being
	 *                   managed, not null
	 * @param  datastore The <code>DataStore</code> upon which this
	 *                   <code>ElementLoader</code> will operate, not null
	 */

	public AbstractLoader (final Class<T> type, final DataStore datastore)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.type = type;
		this.datastore = datastore;
	}

	/**
	 * Get an instance of the <code>Query</code>.  This method will
	 * get a <code>Query</code> for the <code>Element</code> type of
	 * the Loader using the specified implementation class.
	 *
	 * @param  name The name of the <code>Query</code> to retrieve
	 * @param  impl The implementation class to be returned by the
	 *              <code>Query</code>
	 *
	 * @return      The <code>Query</code> instance
	 */

	protected final Query<T> fetchQuery (final Selector selector, final Class<? extends Element> impl)
	{
		this.log.trace ("fetchQuery: selector={}, impl={}", selector, impl);

		assert selector != null : "selector is NULL";
		assert impl != null : "impl is NULL";
		assert this.type.isAssignableFrom (impl) : "impl does not extend " + this.type.getSimpleName ();

		return AbstractQuery.getInstance (this.datastore, selector, impl);
	}

	/**
	 * Get an instance of the <code>Query</code>.  This method will
	 * get a <code>Query</code> for the <code>Element</code> type of
	 * the Loader using the default implementation class for the
	 * <code>DataStore</code>.
	 *
	 * @param  name The name of the <code>Query</code> to retrieve
	 *
	 * @return The <code>Query</code> instance
	 */

	protected final Query<T> fetchQuery (final Selector selector)
	{
		this.log.trace ("fetchQuery: selector={}", selector);

		assert selector != null : "name is NULL";

		return this.fetchQuery (selector, this.datastore.getElementClass (this.type));
	}

	/**
	 * Retrieve an <code>Element</code> instance from the
	 * <code>DataStore</code> based on its <code>DataStore</code> identifier.
	 *
	 * @param  id The <code>DataStore</code> identifier of the
	 *            <code>Element</code> to retrieve, not null
	 *
	 * @return    The requested <code>Element</code>
	 */

	@Override
	public T fetchById (final Long id)
	{
		this.log.trace ("fetchById: id={}", id);

		if (id == null)
		{
			this.log.error ("Attempting to fetch an element with a NULL id");
			throw new NullPointerException ();
		}

		Query<T> query = this.fetchQuery (Element.Selectors.ID);
		query.setProperty (Element.Properties.ID, id);

		return query.query ();
	}

	/**
	 * Retrieve a <code>List</code> of all of the <code>Element</code>
	 * instances from the <code>DataStore</code>.
	 *
	 * @return A <code>List</code> of <code>Element</code> instances
	 */

	@Override
	public List<T> fetchAll ()
	{
		this.log.trace ("fetchAll:");

		return (this.fetchQuery (Element.Selectors.ALL)).queryAll ();
	}
}
