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

public abstract class AbstractQuery<T extends Element, U extends T> implements Query<T>
{
	/** The Query factory */
	private static final QueryFactory FACTORY;

	/** The logger for this DataStoreQuery instance */
	protected final Logger log;

	/** The class of objects to be returned by this query */
	protected final Class<T> type;

	/** The class of objects to be queried from the database */
	protected final Class<U> impl;

	/** The name of the <code>Query</code> */
	private final String name;

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
	 * @param  name The name of the query, not null
	 * @param  type The type of objects to return from this query, not null
	 * @param  impl The type of objects to query from the
	 *              <code>DataStore</code>, not null
	 */

	protected AbstractQuery (final String name, final Class<T> type, final Class<U> impl)
	{
		assert name != null : "name is NULL";
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert name.length () > 0 : "name is empty";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.name = name;
		this.type = type;
		this.impl = impl;
	}

	/**
	 * Get the interface <code>Class</code> of the <code>Element</code>
	 * returned by this <code>Query</code>.
	 *
	 * @return The <code>Class</code> object representing the type of
	 *         <code>Element</code> returned by this <code>Query</code>.
	 */

	public final Class<T> getInterfaceClass ()
	{
		return this.type;
	}

	/**
	 * Get the iplementation <code>Class</code> of the <code>Element</code>
	 * returned by this <code>Query</code>.
	 *
	 * @return The <code>Class</code> object representing the implementation
	 *         type of <code>Element</code> returned by this
	 *         <code>Query</code>.
	 */

	public final Class<?> getImplementationClass ()
	{
		return this.impl;
	}

	/**
	 * Get the name of the <code>Query</code>.
	 *
	 * @return A <code>String</code> which identifies the <code>Query</code>.
	 */

	public final String getName ()
	{
		return this.name;
	}
}
