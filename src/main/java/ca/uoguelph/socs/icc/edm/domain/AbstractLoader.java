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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.List;
import java.util.Map;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Load <code>Element</code> instances from the <code>DataStore</code>.  This
 * class and its sub-classes is responsible for preparing the
 * <code>Query</code> instances required to load <code>Element</code> instances
 * from the <code>DataStore</code>.  The loader classes are defined in terms of
 * the <code>Element</code> interfaces.  A loader class exists for any
 * <code>Element</code> interfaces where it is beneficial to load the
 * <code>Element</code> instances via a <code>Query</code>.  A few of the
 * <code>Element</code> interfaces are fully defined in terms of their
 * relationships and as a result do not need a loader.
 * <p>
 * Loaders and <code>Query</code> query instances operate using a
 * <code>Selector</code> which defines the <code>Query</code> in terms of a set
 * of <code>Property</code> instances.  The loader classes translate the
 * <code>Selector</code> into a method, which can be called normally.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>Element</code> to be processed
 * @see     ca.uoguelph.socs.icc.edm.domain.metadata.Selector
 */

public abstract class AbstractLoader<T extends Element>
{
	/** The logger */
	protected final Logger log;

	/** The return type */
	private final Class<T> type;

	/** The <code>DomainModel</code> instance which owns this Loader. */
	private final DataStore datastore;

	/**
	 * Create the loader for the specified <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 * @param  type                  The <code>Element</code> interface class,
	 *                               not null
	 * @param  create                Method reference to the constructor, not
	 *                               null
	 *
	 * @return                       The loader instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Element</code> queried by the
	 *                               loader
	 */

	protected static <T extends Element, U extends AbstractLoader<T>> U getInstance (final DomainModel model, final Class<T> type, final Function<DataStore, U> create)
	{
		assert type != null : "type is NULL";
		assert create != null : "create is NULL";

		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		if (! model.getDataStore ().isOpen ())
		{
			throw new IllegalStateException ("datastore is closed");
		}

		if (model.getDataStore ().getProfile ().getElementClass (type) == null)
		{
			throw new IllegalStateException ("Element is not available for this datastore");
		}

		return create.apply (model.getDataStore ());
	}

	/**
	 * Create the <code>AbstractLoader</code>.
	 *
	 * @param  type      The <code>Element</code> interface class, not null
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	protected AbstractLoader (final Class<T> type, final DataStore datastore)
	{
		assert type != null : "type is NULL";
		assert datastore != null : "datastore is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.type = type;
		this.datastore = datastore;
	}

	/**
	 * Get an instance of the <code>Query</code>.  This method will
	 * get a <code>Query</code> for the <code>Element</code> using the
	 * specified <code>Selector</code> and implementation class.
	 *
	 * @param  selector The <code>Selector</code>, not null
	 * @param  impl     The <code>Element</code> implementation class, not null
	 *
	 * @return          The <code>Query</code> instance
	 */

	protected final Query<T> getQuery (final Selector selector, final Class<? extends T> impl)
	{
		this.log.trace ("fetchQuery: selector={}, impl={}", selector, impl);

		assert selector != null : "selector is NULL";
		assert impl != null : "impl is NULL";

		if (! this.datastore.isOpen ())
		{
			this.log.error ("Attempting to Query a closed datastore");
			throw new IllegalStateException ("datastore is closed");
		}

		return this.datastore.getQuery (this.type, impl, selector);
	}

	/**
	 * Get an instance of the <code>Query</code>.  This method will
	 * get a <code>Query</code> for the <code>Element</code> based on the
	 * specified <code>Selector</code>.
	 *
	 * @param  selector The <code>Selector</code>, not null
	 *
	 * @return          The <code>Query</code> instance
	 */

	protected final Query<T> getQuery (final Selector selector)
	{
		this.log.trace ("fetchQuery: selector={}", selector);

		assert selector != null : "selector is NULL";

		if (! this.datastore.isOpen ())
		{
			this.log.error ("Attempting to Query a closed datastore");
			throw new IllegalStateException ("datastore is closed");
		}

		return this.datastore.getQuery (this.type, selector);
	}

	/**
	 * Get an instance of the <code>Query</code>.  This method will
	 * get a <code>Query</code> for the <code>Element</code> using the
	 * <code>MetaData</code> definition.
	 *
	 * @param  selector The <code>Selector</code>, not null
	 *
	 * @return          The <code>Query</code> instance
	 */

	protected final Query<T> getDefinitionQuery (final Selector selector)
	{
		this.log.trace ("selector={}", selector);

		assert selector != null : "selector is NULL";

		if (! this.datastore.isOpen ())
		{
			this.log.error ("Attempting to Query a closed datastore");
			throw new IllegalStateException ("datastore is closed");
		}

		return this.datastore.getDefinitionQuery (this.type, selector);
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

	public abstract T fetchById (final Long id);

	/**
	 * Retrieve a <code>List</code> of all of the <code>Element</code>
	 * instances from the <code>DataStore</code>.
	 *
	 * @return A <code>List</code> of <code>Element</code> instances
	 */

	public abstract List<T> fetchAll ();
}
