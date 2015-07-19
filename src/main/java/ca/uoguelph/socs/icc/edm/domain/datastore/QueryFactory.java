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

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;
import ca.uoguelph.socs.icc.edm.domain.metadata.Receiver;

/**
 * Factory to produce <code>Query</code> instances.
 *
 * @author  James E. Stark
 * @version 2.0
 * @param   <T> The <code>Element</code> interface type of the <code>Query</code>
 */

final class QueryFactory<T extends Element> implements Receiver<T, Query<T>>
{
	/** The Backend to be used by the <code>Query</code> */
	private final Backend backend;

	/** The <code>Selector</code> which defines the <code>Query</code> */
	private final Selector selector;

	/**
	 * Create the <code>QueryFactory</code>, for the specified
	 * <code>Selector</code> on the specified <code>Backend</code>.
	 *
	 * @param  backend The <code>Backend</code>
	 * @param  selector The <code>Selector</code>
	 */

	public QueryFactory (final Backend backend, final Selector selector)
	{
		assert backend != null : "backend is NULL";
		assert selector != null : "selector is NULL";

		this.backend = backend;
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

		return new QueryImpl<T, U> (metadata, selector, type, backend);
	}
}
