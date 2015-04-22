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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGeneratorFactory;

public abstract class AbstractDataStoreQuery<T extends Element, U extends T> implements DataStoreQuery<T>
{
	/** The Query factory */
	private static final QueryFactory FACTORY;

	/** The logger for this DataStoreQuery instance */
	protected final Logger log;

	/** The data store instance which is being queried */
	protected final DataStore datastore;

	/** The class of objects to be returned by this query */
	protected final Class<T> type;

	/** The class of objects to be queried from the database */
	protected final Class<U> impl;

	/** <code>DataStore</code> ID number generator */
	private IdGenerator generator;

	/**
	 * static initializer to create the Query Factory.
	 */

	static
	{
		FACTORY = new QueryFactory ();
	}

	/**
	 * Get an instance of the <code>DataStoreQuery</code> for the specified
	 * <code>DataStore</code> and <code>Element</code>.
	 *
	 * @param <T>       The <code>Element</code> type of the
	 *                  <code>DataStoreQuery</code>
	 * @param datastore The <code>DataStore</code> upon which the query is to
	 *                  be created, not null
	 * @param type      The <code>Element</code> interface class, not null
	 * @param impl      The implementation class for which the query is to be
	 *                  created, not null
	 *
	 * @return          The <code>DataStoreQuery</code>
	 */

	public static final <T extends Element> DataStoreQuery<T> getInstance (final DataStore datastore, final Class<T> type, final Class<? extends Element> impl)
	{
		assert datastore != null : "datastore is NULL";
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";

		return FACTORY.create (type, impl, datastore);
	}

	/**
	 * Register an <code>Element</code> implementation with the factory.
	 *
	 * @param <T>  The type of the element <code>Element</code>.
	 * @param <U>  The type of the implementation class
	 * @param type The <code>Element</code> interface class, not null
	 * @param impl The implementation class to be used in the query, not null
	 */

	public static final <T extends Element, U extends T> void registerElement (final Class<T> type, final Class<U> impl)
	{
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";

		FACTORY.registerClass (type, impl);
	}

	/**
	 * Close all of the <code>DataStoreQuery</code> instances for the specified
	 * <code>DataStore</code>.
	 *
	 * @param datastore The <code>DataStore</code> which is being close, not
	 *                  null
	 */

	protected static final void closeAll (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		FACTORY.remove (datastore);
	}

	/**
	 * Create the <code>AbstractDataStoreQuery</code>.
	 *
	 * @param  datastore The DataStore instance to be queried, not null
	 * @param  type      The type of objects to return from this query, not null
	 * @param  impl      The type of objects to query from the datastore, not
	 *                   null
	 * @see    JPADataStore#createQuery
	 */

	protected AbstractDataStoreQuery (final DataStore datastore, final Class<T> type, final Class<U> impl)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.datastore = datastore;
		this.type = type;
		this.impl = impl;

		this.generator = null;
	}

	/**
	 * Get the class representation of the interface type for this query.
	 *
	 * @return The class object representing the interface type;
	 */

	protected final Class<T> getInterfaceType ()
	{
		return this.type;
	}

	/**
	 * Get the class representation of the implementation type for this query.
	 *
	 * @return The class object representing the implementation type
	 */

	protected final Class<U> getImplementationType ()
	{
		return this.impl;
	}

	/**
	 * Close the <code>DataStoreQuery</code>, releasing any resources which are
	 * currently in use.
	 */

	protected void close ()
	{
		this.generator = null;
	}

	/**
	 * Get a reference to the <code>DataStore</code> upon which the
	 * <code>DataStoreQuery</code> operates.
	 *
	 * @return A reference to the <code>DataStore</code>
	 */

	public final DataStore getDataStore ()
	{
		return this.datastore;
	}

	/**
	 * Get the next available DataStore ID number.  The number will be chosen
	 * by the IdGenerator algorithm set in the <code>DataStoreProfile</code>
	 *
	 * @return A Long containing the ID number
	 */

	public final Long nextId ()
	{
		if (this.generator == null)
		{
			this.generator = (IdGeneratorFactory.getInstance ()).create (this.type, this);
		}

		return this.generator.nextId ();
	}
}
