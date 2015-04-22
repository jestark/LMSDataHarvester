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

import ca.uoguelph.socs.icc.edm.domain.Element;

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

public final class JPADataStoreQuery<T extends Element, X extends T> extends AbstractDataStoreQuery<T, X>
{
	/** The data store instance which is being queried */
	protected final JPADataStore datastore;

	/** Cache of database queries  */
	private final Map<String, TypedQuery<X>> queries;

	/**
	 * Create the <code>JPADataStoreQuery</code>.
	 *
	 * @param  datastore The DataStore instance to be queried, not null
	 * @param  type      The type of objects to return from this query, not null
	 * @param  impl      The type of objects to query from the database, not
	 *                   null
	 * @see    JPADataStore#createQuery
	 */

	protected JPADataStoreQuery (final JPADataStore datastore, final Class<T> type, final Class<X> impl)
	{
		super (datastore, type, impl);

		this.datastore = datastore;

		this.queries = new HashMap<String, TypedQuery<X>> ();
	}

	/**
	 * Close the data store query. This method is intended to be used only by
	 * the <code>JPADataStore</code>'s <code>close</code> method to instruct
	 * this query to perform any necessary clean-up operations.
	 *
	 * @see JPADataStore#close
	 */

	protected void close ()
	{
		this.log.trace ("close:");

		this.queries.clear ();
//		this.generator = null;
	}

	/**
	 * Create a <code>TypedQuery</code> corresponding to the specified name.
	 * This method resolves the full name of the query and retrieves the
	 * corresponding <code>TypedQuery</code> object from the
	 * <code>EntityManager</code>.
	 *
	 * @param  <Q>                      The return type of the query
	 * @param  cls                      The return type of the query, not null
	 * @param  name                     The name of the query to fetch, not null
	 *
	 * @return                          The typed query corresponding to the
	 *                                  specified name
	 * @throws IllegalArgumentException if the specified query does not exist
	 */

	private <Q> TypedQuery<Q> getQuery (final Class<Q> cls, final String name)
	{
		this.log.trace ("getQuery cls={}, name={}", cls, name);

		TypedQuery<Q> query = null;

		if (name == null)
		{
			this.log.error ("Query name is NULL");
			throw new NullPointerException ();
		}

		try
		{
			query = (this.datastore.getEntityManager ()).createNamedQuery (this.impl.getSimpleName () + ":" + name, cls);
		}
		catch (IllegalArgumentException ex)
		{
			this.log.error ("Failed to load query", ex);
			throw ex;
		}

		return query;
	}

	/**
	 * Create a <code>TypedQuery</code> corresponding to the specified name.
	 * This method will retrieve the specified query from the JPA
	 * <code>EntityManager</code> is the query is not already cached.
	 *
	 * @param  name                     The name of the query to fetch, not null
	 *
	 * @return                          The typed query corresponding to the
	 *                                  specified name
	 * @throws IllegalArgumentException if the specified query does not exist
	 */

	private TypedQuery<X> getQuery (final String name)
	{
		this.log.trace ("getQuery: name={}", name);

		if (! this.queries.containsKey (name))
		{
			this.queries.put (name, this.getQuery (this.impl, name));
		}

		return this.queries.get (name);
	}

	/**
	 * Create a TypedQuery corresponding to the specified name, and initialize
	 * it from the provided map of query parameters.  The query must exist in
	 * the database mapping.
	 * <p>
	 * The parameter map may be null, however all of the parameters to the
	 * query (if any) must have a value.  All of the parameters included in the
	 * parameter map must have non-null values.
	 * <p>
	 * All of the parameters for the query must have values.  If a query
	 * parameter is not included in the parameter map then the value for the
	 * parameter from the previous execution of the query will be used, if it
	 * is available. Since reusing query parameters will probably lead to
	 * unexpected results, a warning will be emitted if a parameter is missing
	 * from the parameter map. This method will abort if a parameter is missing
	 * and there is no value available from a previous query execution.
	 * <p>
	 * The <code>getParameters</code> method will return a list of all of the
	 * query parameters.
	 *
	 * @param  name                     The name of the query to fetch, not
	 *                                  null
	 * @param  parameters               A <code>Map</code> of parameter names
	 *                                  and their corresponding values for this
	 *                                  query. The <code>Map</code> may be
	 *                                  null, the values must not be null.
	 * @return                          The initialized typed query
	 *                                  corresponding to the specified name.
	 * @throws IllegalArgumentException if a parameter is missing, or the query
	 *                                  does not exist
	 * @throws NullPointerException     if a value to a parameter is null
	 * @see    #getParameters
	 */

	private TypedQuery<X> getQuery (final String name, final Map<String, Object> parameters)
	{
		this.log.trace ("getQuery: name={}, parameters={}", name, parameters);

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
					this.log.error ("NULL value specified for parameter: {}", param.getName ());
					throw new NullPointerException ("NULL value specified for parameter");
				}
			}
			else if (query.isBound (param))
			{
				// The caller didn't supply a value for this parameter, so the
				// previous value will be used.  It's not an error, but may
				// lead to bad results so issue a warning
				this.log.warn ("Using previous value for parameter: {}", param.getName ());
			}
			else
			{
				this.log.error ("No value specified for parameter: {}", param.getName ());
				throw new IllegalArgumentException ("No value specified for parameter");
			}
		}

		// Log INFO messages for any extra parameters.
		for (String pname : pnames)
		{
			this.log.info ("Unused parameter: {}", pname);
		}

		return query;
	}

	/**
	 * Get the set of parameter names for the specified query.
	 *
	 * @param  name The name of the query, not null
	 * @return      A set containing the names of all of the parameters
	 */

	public Set<String> getParameters (final String name)
	{
		this.log.trace ("getParameters: name={}", name);

		Set<String> params = new HashSet<String> ();

		for (Parameter<?> p : (this.getQuery (name)).getParameters ())
		{
			params.add (p.getName ());
		}

		return params;
	}

	/**
	 * Get all of the ID numbers for the class represented by this
	 * <code>DataStoreQuery</code> in the <code>DataStore</code>.
	 *
	 * @return A (possibly empty) list of <code>Long</code> integers
	 */

	public List<Long> queryAllIds ()
	{
		this.log.trace ("queryAllIds:");

		return new ArrayList<Long> ((this.getQuery (Long.class, "allid")).getResultList ());
	}

	/**
	 * Get the largest ID number for the class represented by this
	 * <code>dataStoreQuery</code> in the <code>DataStore</code>.
	 *
	 * @return A <code>Long</code> integer
	 */

	public Long queryMaxId ()
	{
		this.log.trace ("queryMaxId:");

		Long result = new Long (0);

		try
		{
			result = (this.getQuery (Long.class, "maxid")).getSingleResult ();
		}
		catch (NoResultException ex)
		{
			this.log.debug ("Query did not return a result", ex);
		}
		catch (NonUniqueResultException ex)
		{
			this.log.error ("Query returned multiple results", ex);
			throw ex;
		}

		return result;
	}

	/**
	 * Retrieve an object from the database based on the value of its primary
	 * key.  Note that the value of the primary key must not be less than zero.
	 *
	 * @param  id The id (primary key) of the object to retrieve, not null
	 * @return    The object with an ID equal to the specified value or null if
	 *            that object does not exist in the database.
	 */

	@Override
	public T query (final Long id)
	{
		this.log.trace ("query: id={}", id);

		assert id != null : "id is NULL";

		return (this.datastore.getEntityManager ()).find (this.impl, id);
	}

	/**
	 * Fetch the object from the database which matches the specified query,
	 * with the specified parameters.  The specified query must be defined in
	 * the JPA database mapping.
	 * <p>
	 * The parameter map may be null, however all of the parameters to the
	 * query (if any) must have a value.  All of the parameters included in the
	 * parameter map must have non-null values.
	 * <p>
	 * All of the parameters for the query must have values.  If a query
	 * parameter is not included in the parameter map then the value for the
	 * parameter from the previous execution of the query will be used, if it
	 * is available.  Since reusing query parameters will probably lead to
	 * unexpected results, a warning will be emitted if a parameter is missing
	 * from the parameter map.  This method will abort if a parameter is
	 * missing and there is no value available from a previous query execution.
	 * <p>
	 * The <code>getParameters</code> method will return a list of all of the
	 * query parameters.
	 *
	 * @param name                      The name of the query to execute, not
	 *                                  null
	 * @param parameters                A <code>Map</code> of parameter names
	 *                                  and their corresponding values for this
	 *                                  query.  The <code>Map</code> may be
	 *                                  null, the values must not be null.
	 * @return                          The object which matches the specified
	 *                                  query, null if that object does not exist
	 *                                  in the database.
	 * @throws IllegalArgumentException if a parameter is missing, or the query
	 *                                  does not exist
	 * @throws NullPointerException     if a value to a parameter is null
	 * @see    #getParameters
	 */

	@Override
	public T query (final String name, final Map<String, Object> parameters)
	{
		this.log.trace ("query: name={}, parameters={}", name, parameters);

		T result = null;

		try
		{
			result = this.getQuery (name, parameters).getSingleResult ();
		}
		catch (NoResultException ex)
		{
			this.log.debug ("Query did not return a result", ex);
		}
		catch (NonUniqueResultException ex)
		{
			this.log.error ("Query returned multiple results", ex);
			throw ex;
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
		this.log.trace ("queryAll:");

		return new ArrayList<T> (this.getQuery ("all").getResultList ());
	}

	/**
	 * Fetch a list of objects from the database which match the specified
	 * query, with the specified parameters.  The specified query must be
	 * defined in the JPA database mapping.
	 * <p>
	 * The parameter map may be null, however all of the parameters to the
	 * query (if any) must have a value.  All of the parameters included in the
	 * parameter map must have non-null values.
	 * <p>
	 * All of the parameters for the query must have values.  If a query
	 * parameter is not included in the parameter map then the value for the
	 * parameter from the previous execution of the query will be used, if it
	 * is available.  Since reusing query parameters will probably lead to
	 * unexpected results, a warning will be emitted if a parameter is missing
	 * from the parameter map.  This method will abort if a parameter is
	 * missing and there is no value available from a previous query execution.
	 * <p>
	 * The <code>getParameters</code> method will return a list of all of the
	 * query parameters.
	 *
	 * @param  name                     The name of the query to execute, not
	 *                                  null
	 * @param  parameters               A <code>Map</code> of parameter names
	 *                                  and their corresponding values for this
	 *                                  query.  The <code>Map</code> may be
	 *                                  null, the values must not be null.
	 * @return                          The <code>List</code> of object which
	 *                                  match the specified query.  If no
	 *                                  objects match then the
	 *                                  <code>List</code> will be empty.
	 * @throws IllegalArgumentException if a parameter is missing, or the query
	 *                                  does not exist
	 * @throws NullPointerException     if a value to a parameter is null
	 * @see    #getParameters
	 */

	@Override
	public List<T> queryAll (final String name, final Map<String, Object> parameters)
	{
		this.log.trace ("queryAll: name={}, parameters={}", name, parameters);

		return new ArrayList<T> (this.getQuery (name, parameters).getResultList ());
	}

	/**
	 * Test to see if the specified entity is in the data store.  The entity to
	 * be tested must be an instance of the implementation type associated with
	 * this <code>DataStoreQuery</code>.
	 *
	 * @param  entity                   The entity to check, not null.
	 * @return                          <code>true</code> if the entity is in
	 *                                  the database <code>false</code>
	 *                                  otherwise.
	 * @throws IllegalArgumentException if the entity is not an instance of the
	 *                                  implementation type associated with
	 *                                  this data store query instance.
	 */

	@Override
	public Boolean contains (final T entity)
	{
		this.log.trace ("contains: entity={}", entity);

		// Abort if the entity is not an instance of the implementation type
		if (! this.impl.isInstance (entity))
		{
			this.log.error ("Object of type {} is required, received object of type {}", this.impl.getName (), (entity.getClass ()).getName ());
			throw new IllegalArgumentException ("Entity is not an instance of the query implementation type");
		}

		return (this.datastore.getEntityManager ()).contains (entity);
	}

	/**
	 * Insert the specified entity into the data store.  The entity to be
	 * inserted must be an instance of the implementation type associated with
	 * this <code>DataStoreQuery</code>.
	 *
	 * @param  entity                   The entity to insert, not null.
	 * @throws IllegalArgumentException if the entity is not an instance of the
	 *                                  implementation type associated with
	 *                                  this data store query instance.
	 */

	@Override
	public T insert (final T entity)
	{
		this.log.trace ("insert: entity={}", entity);

		// Abort if the entity is not an instance of the implementation type
		if (! this.impl.isInstance (entity))
		{
			this.log.error ("Object of type {} is required, received object of type {}", this.impl.getName (), (entity.getClass ()).getName ());
			throw new IllegalArgumentException ("Entity is not an instance of the query implementation type");
		}

		(this.datastore.getEntityManager ()).persist (entity);

		return entity;
	}

	/**
	 * Remove the specified entity from the data store.  The entity to be
	 * removed must be an instance of the implementation type associated with
	 * this <code>DataStoreQuery</code>.
	 *
	 * @param  entity                   The entity to remove, not null.
	 * @throws IllegalArgumentException if the entity is not an instance of the
	 *                                  implementation type associated with
	 *                                  this data store query instance.
	 */

	@Override
	public void remove (final T entity)
	{
		this.log.trace ("remove: entity={}", entity);

		// Abort if the entity is not an instance of the implementation type
		if (! this.impl.isInstance (entity))
		{
			this.log.error ("Object of type {} is required, received object of type {}", this.impl.getName (), (entity.getClass ()).getName ());
			throw new IllegalArgumentException ("Entity is not an instance of the query implementation type");
		}

		(this.datastore.getEntityManager ()).remove (entity);
	}
}
