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

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * <code>Retriever</code> implementation using a <code>Query</code>.  This
 * <code>Retriever</code> implementation uses a <code>Query</code> to fetch
 * an <code>Element</code> instance corresponding to the supplied
 * <code>Element</code> instance from the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the <code>Element</code> on which we are operating
 */

public final class QueryRetriever<T extends Element> implements Retriever<T>
{
	/** The Log */
	private final Logger log;

	/** The <code>DomainModel</code> */
	private final DomainModel model;

	/** The <code>Query</code> used to fetch <code>Element</code> instances */
	private final Query<T> query;

	/**
	 * Create the <code>QueryRetriever</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @param  query The <code>Query</code>, not null
	 */

	protected QueryRetriever (final DomainModel model, final Query<T> query)
	{
		assert model != null : "model is NULL";
		assert query != null : "query is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.model = model;
		this.query = query;
	}

	/**
	 * Retrieve the <code>Element</code> instance from the
	 * <code>DataStore</code> corresponding specified <code>Element</code>
	 * instance.  If the specified <code>Element</code> instance is already in
	 * the <code>DataStore</code> then that will be returned.  Otherwise, this
	 * method will return the matching <code>Element</code> instance from the
	 * <code>DataStore</code> or <code>null</code> if there is no matching
	 * <code>Element</code> instance in the <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance, not null
	 *
	 * @return         The <code>Element</code> instance in the
	 *                 <code>DataStore</code> or <code>null</code>
	 */

	@Override
	public T fetch (final T element)
	{
		this.log.trace ("fetch: element={}", element);

		assert element != null : "element is NULL";

		if (! this.model.isOpen ())
		{
			this.log.error ("datastore is closed");
			throw new IllegalStateException ("datastore is closed");
		}

		return (this.model.contains (element)) ? element : this.query.setAllValues (element).query ();
	}
}
