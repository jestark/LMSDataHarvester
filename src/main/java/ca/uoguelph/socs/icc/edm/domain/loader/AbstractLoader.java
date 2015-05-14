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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;
import ca.uoguelph.socs.icc.edm.domain.ElementLoader;

import ca.uoguelph.socs.icc.edm.domain.builder.AbstractBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreProfile;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;
import ca.uoguelph.socs.icc.edm.domain.datastore.AbstractDataStoreQuery;

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
	/** The Loader factory */
	private static final ElementLoaderFactory FACTORY;

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
		FACTORY = new ElementLoaderFactory ();
	}

	/**
	 * Get an instance of an <code>ElementLoader</code> interface for the
	 * specified <code>DataStore</code>.  This method is intended to be used by
	 * the <code>DomainModel</code> and <code>ElementLoader</code> instances
	 * to get an instance of the requeested <code>ElementLoader</code> as
	 * required.
	 *
	 * @param  <T>       The type of <code>ElementLoader</code> to return
	 * @param  <U>       The type of <code>Element</code> operated on by the
	 *                   <code>ElementLoader</code>
	 * @param  element   The <code>Element</code> interface class, not null
	 * @param  Loader   The <code>ElementLoader</code> interface class, not
	 *                   null
	 * @param  datastore The <code>DataStore</code> upon which the
	 *                   <code>ElementLoader</code> instance is to operate,
	 *                   not null
	 *
	 * @return           An instance of the requested
	 *                   <code>ElementLoader</code> interface on the specified
	 *                   <code>DataStore</code>
	 */

	public static final <T extends ElementLoader<U>, U extends Element> T getInstance (final Class<U> element, final Class<T> Loader, final DataStore datastore)
	{
		assert element != null : "element is NULL";
		assert Loader != null : "Loader is NULL";
		assert datastore != null : "datastore is NULL";

		return FACTORY.create (element, Loader, datastore);
	}

	/**
	 * Register an <code>ElementLoader</code> implementation with the factory.
	 * This method is intended to be used by the <code>ElementLoader</code>
	 * implementations to register themselves with the factory so that they may
	 * be instantiated on demand.
	 *
	 * @param  <T>     The type of <code>ElementLoader</code> to register
	 * @param  Loader The <code>ElementLoader</code> interface class, not null
	 * @param  impl    The <code>ElementLoader</code> implementation class,
	 *                 not null
	 * @param  factory The <code>LoaderFactory</code> instance, not null
	 */

	protected static final <T extends ElementLoader<? extends Element>> void registerLoader (final Class<T> Loader, Class<? extends T> impl, final LoaderFactory<T> factory)
	{
		FACTORY.registerFactory (Loader, impl, factory);
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
	 * Get an instance of the <code>DataStoreQuery</code>.  This method will
	 * get a <code>DataStoreQuery</code> for the <code>Element</code> type of
	 * the Loader using the implementation class specified in the
	 * <code>DataStoreProfile</code>.
	 *
	 * @return The <code>DataStoreQuery</code> instance
	 */

	protected final DataStoreQuery<T> fetchQuery ()
	{
		this.log.trace ("fetchQuery:");

		return AbstractDataStoreQuery.getInstance (this.datastore, this.type, (this.datastore.getProfile ()).getImplClass (this.type));
	}

	/**
	 * Get an instance of the <code>DataStoreQuery</code>.  This method will
	 * get a <code>DataStoreQuery</code> for the <code>Element</code> type of
	 * the Loader using the specified implementation class.
	 *
	 * @return The <code>DataStoreQuery</code> instance
	 */

	protected final DataStoreQuery<T> fetchQuery (final Class<? extends Element> impl)
	{
		this.log.trace ("fetchQuery: impl={}", impl);

		assert impl != null : "impl is NULL";
		assert this.type.isAssignableFrom (impl) : "impl does not extend " + this.type.getSimpleName ();

		return AbstractDataStoreQuery.getInstance (this.datastore, this.type, impl);
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

		return (this.fetchQuery ()).query (id);
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

		return (this.fetchQuery ()).queryAll ();
	}
}
