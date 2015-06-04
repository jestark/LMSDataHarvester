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

package ca.uoguelph.socs.icc.edm.domain.datastore.jpa;

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

import ca.uoguelph.socs.icc.edm.domain.datastore.Query;
import ca.uoguelph.socs.icc.edm.domain.datastore.AbstractQuery;

/**
 * Add, remove and retrieve data from a database using the Java Persistence
 * API.
 *
 * @author  James E. Stark
 * @version 2.0
 *
 * @param   <T>          The type (interface) of the objects to be returned
 * @param   <X>          The type (implementation) of the objects to be queried
 * @see     JPADataStore
 */

public final class JPAQuery<T extends Element> extends AbstractQuery<T>
{
	/** Cache of database queries  */
	private final TypedQuery<T> query;

	/**
	 * Create the <code>JPADataStoreQuery</code>.
	 *
	 * @param  datastore The DataStore instance to be queried, not null
	 * @param  type      The type of objects to return from this query, not null
	 */

	protected JPAQuery (final String name, final Class<T> type, final EntityManager manager)
	{
		super (name, type);

		this.query = manager.createNamedQuery (null, this.type);

	}

	/**
	 * Get the <code>Set</code> of the parameter names for the
	 * <code>Query</code>.
	 *
	 * @return A <code>Set</code> containing the names of the parameters
	 */

	public Set<String> getParameters ()
	{
		this.log.trace ("getParameters:");

		Set<String> parameters = new HashSet<String> ();

		for (Parameter<?> p : this.query.getParameters ())
		{
			parameters.add (p.getName ());
		}

		return parameters;
	}

	/**
	 * Set a value for a specific parameter.
	 *
	 * @param  name                     The name of the parameter
	 * @param  value                    The value to be queried for the
	 *                                  parameter
	 *
	 * @return                          This <code>Query</code>
	 * @throws IllegalArgumentException if the specified parameter does not
	 *                                  exist
	 * @see    #getParameters
	 */

	public Query<T> setParameter (final String name, final Object value)
	{
		this.log.trace ("setParameter: name={}, value={}", name, value);

		this.query.setParameter (name, value);

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

		T result = null;

		try
		{
			result = this.query.getSingleResult ();
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

		return new ArrayList<T> (this.query.getResultList ());
	}
}
