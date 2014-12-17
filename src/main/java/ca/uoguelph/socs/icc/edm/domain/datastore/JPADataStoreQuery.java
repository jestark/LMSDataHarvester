/* Copyright (C) 2014 James E. Stark
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

package ca.uoguelph.socs.icc.edm.datastore;

import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.NoResultException;
import javax.persistence.Parameter;
import javax.persistence.TypedQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModelElement;

/**
 * Add, remove and retrieve data from a database using the Java Persistence
 * API.
 *
 * @author  James E. Stark
 * @version 1.0
 *
 * @param   <T>          The type (interface) of the objects to be returned
 * @param   <X>          The type (implementation) of the objects to be queried
 * @see     JPADataStore
 */

public final class JPADataStoreQuery<T extends DomainModelElement, X extends T> implements DataStoreQuery<T>
{
	/** The data store instance which is being queried */
	private final JPADataStore datastore;

	/** The class of objects to be returned by this query */
	private final Class<T> type;

	/** The class of objects to be queried from the database */
	private final Class<X> impl;

	/** Cache of database queries  */
	private final Map<String, TypedQuery<X>> queries;

	/** The logger for this DataStoreQuery instance */
	private final Log log;

	/**
	 * Create the <code>JPADataStoreQuery</code>.
	 *
	 * @param  datastore The DataStore instance to be queried, not null
	 * @param  type      The type of objects to return from this query, not null
	 * @param  impl      The type of objects to query from the database, not null
	 * @see    JPADataStore#getQuery
	 */

	protected JPADataStoreQuery (JPADataStore datastore, Class<T> type, Class<X> impl)
	{
		this.datastore = datastore;
		this.type = type;
		this.impl = impl;

		this.queries = new HashMap<String, TypedQuery<X>> ();
		this.log = LogFactory.getLog (JPADataStoreQuery.class);
	}

	/**
	 * Get the class representation of the interface type for this query.
	 *
	 * @return The class object representing the interface type;
	 */

	protected Class<T> getInterfaceType ()
	{
		return this.type;
	}

	/**
	 * Get the class representation of the implementation type for this query.
	 *
	 * @return The class object representing the implementation type
	 */

	protected Class<X> getImplementationType ()
	{
		return this.impl;
	}

	/**
	 * Close the data store query. This method is intended to be used only by the
	 * <code>JPADataStore</code>'s <code>close</code> method to instruct this
	 * query to perform any necessary clean-up operations.
	 *
	 * @see JPADataStore#close
	 */

	protected void close ()
	{
		this.queries.clear ();
	}

	/**
	 * Create a TypedQuery corresponding to the specified name.  The query must
	 * exist in the database mapping configuration.
	 *
	 * @param  name                     The name of the query to fetch, not null.
	 * @return                          The typed query corresponding to the
	 *                                  specified name
	 * @throws IllegalArgumentException if the specified query does not exist.
	 */

	private TypedQuery<X> getQuery (String name)
	{
		// A null query name is invalid, about if we find one.
		if (name == null)
		{
			this.log.error ("Query name is NULL");
			throw new NullPointerException ();
		}

		String queryname = impl.getName () + ":" + name;

		// Have the data store create the query only if there is no cached copy.  In
		// that case we need to catch the illegal argument exception coming from the
		// JPA entity manager to generate an error log.
		if (! this.queries.containsKey (queryname))
		{
			try
			{
				this.queries.put (queryname, (this.datastore.getEntityManager ()).createNamedQuery (queryname, this.impl));
			}
			catch (IllegalArgumentException ex)
			{
				this.log.error ("Failed to load query", ex);
				throw ex;
			}
		}

		return this.queries.get (queryname);
	}

	/**
	 * Create a TypedQuery corresponding to the specified name, and initialize it
	 * from the provided map of query parameters.  The query must exist in the
	 * database mapping.
	 * <p>
	 * The parameter map may be null, however all of the parameters to the query
	 * (if any) must have a value.  All of the parameters included in the
	 * parameter map must have non-null values.
	 * <p>
	 * All of the parameters for the query must have values.  If a query parameter
	 * is not included in the parameter map then the value for the parameter from
	 * the previous execution of the query will be used, if it is available.
	 * Since reusing query parameters will probably lead to unexpected results, a
	 * warning will be emitted if a parameter is missing from the parameter map.
	 * This method will abort if a parameter is missing and there is no value
	 * available from a previous query execution.
	 * <p>
	 * The <code>getParameters</code> method will return a list of all of the
	 * query parameters.
	 *
	 * @param  name                     The name of the query to fetch, not null
	 * @param  parameters               A Map of parameter names and their
	 *                                  corresponding values for this query.  The
	 *                                  Map may be null, the values must not be
	 *                                  null.
	 * @return                          The initialized typed query corresponding
	 *                                  to the specified name.
	 * @throws IllegalArgumentException if a parameter is missing, or the query
	 *                                  does not exist
	 * @throws NullPointerException     if a value to a parameter is null
	 * @see    #getParameters
	 */

	private TypedQuery<X> getQuery (String name, Map<String, Object> parameters)
	{
		TypedQuery<X> query = this.getQuery (name);
		Set<Parameter<?>> qparams = query.getParameters ();
		Set<String> pnames = parameters.keySet ();

		for (Parameter<?> param : qparams)
		{
			if ((parameters != null) && (parameters.containsKey (param.getName ())))
			{
				if (parameters.get (param.getName ()) != null)
				{
					query.setParameter (param.getName (), parameters.get (param.getName ()));
					pnames.remove (param.getName ());
				}
				else
				{
					if (this.log.isErrorEnabled ())
					{
						this.log.error ("NULL value specified for parameter: " + param.getName ());
					}

					throw new NullPointerException ("NULL value specified for parameter: " + param.getName ());
				}
			}
			else if (query.isBound (param))
			{
				// The caller didn't supply a value for this parameter, so the previous
				// value will be used.  It's not an error, but may lead to bad results so
				// issue a warning
				if (this.log.isWarnEnabled ())
				{
					this.log.warn ("Using previous value for parameter: " + param.getName ());
				}
			}
			else
			{
				if (this.log.isErrorEnabled ())
				{
					this.log.error ("No value specified for parameter: " + param.getName ());
				}

				throw new IllegalArgumentException ("No value specified for parameter: " + param.getName ());
			}
		}

		// Log INFO messages for any extra parameters.
		for (String pname : pnames)
		{
			if (this.log.isInfoEnabled ())
			{
				this.log.info ("Unused parameter: " + pname);
			}
		}

		return query;
	}

	/**
	 * Get the set of parameter names for the specified query.
	 *
	 * @param  name                 The name of the query, not null
	 * @return                      A set containing the names of all of the
	 *                              parameters
	 */

	public Set<String> getParameters (String name)
	{
		Set<String> params = new HashSet<String> ();

		for (Parameter<?> p : (this.getQuery (name)).getParameters ())
		{
			params.add (p.getName ());
		}

		return params;
	}

	/**
	 * Retrieve an object from the database based on the value of its primary key.
	 * Note that the value of the primary key must not be less than zero.
	 *
	 * @param  id The id (primary key) of the object to retrieve, not null
	 * @return    The object with an ID equal to the specified value or null if
	 *            that object does not exist in the database.
	 */

	@Override
	public T query (Long id)
	{
		if (id == null)
		{
			this.log.error ("Attempting to query using a NULL id");
			throw new NullPointerException ();
		}

		if (id < 0)
		{
			if (this.log.isErrorEnabled ())
			{
				this.log.error ("query id: " + id + " < 0, positive values are required");
			}

			throw new IllegalArgumentException ("Key ids must be greater then zero.");
		}

		return (this.datastore.getEntityManager ()).find (this.type, id);
	}

	/**
	 * Fetch the object from the database which matches the specified query, with
	 * the specified parameters.  The specified query must be defined in the JPA
	 * database mapping.
	 * <p>
	 * The parameter map may be null, however all of the parameters to the query
	 * (if any) must have a value.  All of the parameters included in the
	 * parameter map must have non-null values.
	 * <p>
	 * All of the parameters for the query must have values.  If a query parameter
	 * is not included in the parameter map then the value for the parameter from
	 * the previous execution of the query will be used, if it is available.
	 * Since reusing query parameters will probably lead to unexpected results, a
	 * warning will be emitted if a parameter is missing from the parameter map.
	 * This method will abort if a parameter is missing and there is no value
	 * available from a previous query execution.
	 * <p>
	 * The <code>getParameters</code> method will return a list of all of the
	 * query parameters.
	 *
	 * @param name                      The name of the query to execute, not null
	 * @param parameters                A Map of parameter names and their
	 *                                  corresponding values for this query.  The
	 *                                  Map may be null, the values must not be
	 *                                  null.
	 * @return                          The object which matches the specified
	 *                                  query, null if that object does not exist
	 *                                  in the database.
	 * @throws IllegalArgumentException if a parameter is missing, or the query
	 *                                  does not exist
	 * @throws NullPointerException     if a value to a parameter is null
	 * @see    #getParameters
	 */

	@Override
	public T query (String name, Map<String, Object> parameters)
	{
		T result = null;

		try
		{
			result = this.getQuery (name, parameters).getSingleResult ();
		}
		catch (NoResultException ex)
		{
			if (this.log.isDebugEnabled ())
			{
				this.log.debug ("Query did not return a result: " + name, ex);
			}
		}
		catch (NonUniqueResultException ex)
		{
			if (this.log.isFatalEnabled ())
			{
				this.log.fatal ("Query returned multiple results: " + name, ex);
				throw ex;
			}
		}

		return result;
	}

	/**
	 * Retrieve a List of all of the objects in the database of the type which
	 * corresponds to this DataStoreQuery instance.  If there are no object of
	 * the corresponding type in the database then the list will be empty.
	 *
	 * @return A (possibly empty) list of objects.
	 */

	@Override
	public List<T> queryAll ()
	{
		return new ArrayList<T> (this.getQuery ("all").getResultList ());
	}

	/**
	 * Fetch a list of objects from the database which match the specified query,
	 * with the specified parameters.  The specified query must be defined in the
	 * JPA database mapping.
	 * <p>
	 * The parameter map may be null, however all of the parameters to the query
	 * (if any) must have a value.  All of the parameters included in the
	 * parameter map must have non-null values.
	 * <p>
	 * All of the parameters for the query must have values.  If a query parameter
	 * is not included in the parameter map then the value for the parameter from
	 * the previous execution of the query will be used, if it is available.
	 * Since reusing query parameters will probably lead to unexpected results, a
	 * warning will be emitted if a parameter is missing from the parameter map.
	 * This method will abort if a parameter is missing and there is no value
	 * available from a previous query execution.
	 * <p>
	 * The <code>getParameters</code> method will return a list of all of the
	 * query parameters.
	 *
	 * @param  name                     The name of the query to execute, not null
	 * @param  parameters               A Map of parameter names and their
	 *                                  corresponding values for this query.  The
	 *                                  Map may be null, the values must not be
	 *                                  null.
	 * @return                          The list of object which match the
	 *                                  specified query.  If no objects match then
	 *                                  the List will be empty.
	 * @throws IllegalArgumentException if a parameter is missing, or the query
	 *                                  does not exist
	 * @throws NullPointerException     if a value to a parameter is null
	 * @see    #getParameters
	 */

	@Override
	public List<T> queryAll (String name, Map<String, Object> parameters)
	{
		return new ArrayList<T> (this.getQuery (name, parameters).getResultList ());
	}

	/**
	 * Test to see if the specified entity is in the data store.  The entity to be
	 * tested must be an instance of the implementation type associated with this
	 * DataStoreQuery.
	 *
	 * @param  entity                   The entity to check, not null.
	 * @return                          <code>true</code> if the entity is in the
	 *                                  database <code>false</code> otherwise.
	 * @throws IllegalArgumentException if the entity is not an instance of the
	 *                                  implementation type associated with this
	 *                                  data store query instance.
	 */

	@Override
	public Boolean contains (T entity)
	{
		// Abort if the entity if the entity is null
		if (entity == null)
		{
			this.log.error ("Attempting to test a NULL");
			throw new NullPointerException ();
		}

		// Abort if the entity is not an instance of the implementation type
		if (! this.impl.isInstance (entity))
		{
			if (this.log.isErrorEnabled ())
			{
				this.log.error ("Object of type " + this.impl.getName () + " is required, received object of type " +  (entity.getClass ()).getName ());
			}

			throw new IllegalArgumentException (this.impl.getName () + " required, " + (entity.getClass ()).getName () + " found.");
		}

		return (this.datastore.getEntityManager ()).contains (entity);
	}

	/**
	 * Insert the specified entity into the data store.  The entity to be inserted
	 * must be an instance of the implementation type associated with this
	 * DataStoreQuery.
	 *
	 * @param  entity                   The entity to insert, not null.
	 * @throws IllegalArgumentException if the entity is not an instance of the
	 *                                  implementation type associated with this
	 *                                  data store query instance.
	 */

	@Override
	public void insert (T entity)
	{
		// Abort if the entity if the entity is null
		if (entity == null)
		{
			this.log.error ("Attempting to insert a NULL");
			throw new NullPointerException ();
		}

		// Abort if the entity is not an instance of the implementation type
		if (! this.impl.isInstance (entity))
		{
			if (this.log.isErrorEnabled ())
			{
				this.log.error ("Object of type " + this.impl.getName () + " is required, received object of type " +  (entity.getClass ()).getName ());
			}

			throw new IllegalArgumentException (this.impl.getName () + " required, " + (entity.getClass ()).getName () + " found.");
		}

		(this.datastore.getEntityManager ()).persist (entity);
	}

	/**
	 * Remove the specified entity from the data store.  The entity to be removed
	 * must be an instance of the implementation type associated with this
	 * DataStoreQuery.
	 *
	 * @param  entity                   The entity to remove, not null.
	 * @throws IllegalArgumentException if the entity is not an instance of the
	 *                                  implementation type associated with this
	 *                                  data store query instance.
	 */

	@Override
	public void remove (T entity)
	{
		// Abort if the entity if the entity is null
		if (entity == null)
		{
			this.log.error ("Attempting to remove a NULL");
			throw new NullPointerException ();
		}

		// Abort if the entity is not an instance of the implementation type
		if (! this.impl.isInstance (entity))
		{
			if (this.log.isErrorEnabled ())
			{
				this.log.error ("Object of type " + this.impl.getName () + " is required, received object of type " +  (entity.getClass ()).getName ());
			}

			throw new IllegalArgumentException (this.impl.getName () + " required, " + (entity.getClass ()).getName () + " found.");
		}

		(this.datastore.getEntityManager ()).remove (entity);
	}
}
