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

import dagger.Module;
import dagger.Provides;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * <code>Retriever</code> implementation using a <code>Query</code> and the
 * <code>TranslationTable</code>.  This <code>Retriever</code> implementation
 * is a cross between the <code>QueryRetriever</code> and the
 * <code>TableRetriever</code>.  It fetches the <code>Element</code> instance
 * which corresponds to the supplied <code>Element</code> instance by first
 * inspecting the <code>TranslationTable</code>, and then by running a
 * <code>Query</code> against the <code>DataStore</code> if the corresponding
 * <code>Element</code> instance was not fount in the
 * <code>TranslationTable</code>.  If the <code>Query</code> returns a
 * corresponding <code>Element</code> instance then this <code>Retriever</code>
 * implementation will update the <code>TranslationTable</code> before
 * returning the result.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the <code>Element</code> on which we are operating
 */

public final class QueryRetriever<T extends Element> implements Retriever<T>
{
	/**
	 * Dagger Module to create a new instance of the
	 * <code>QueryRetriever</code>.  The Dagger Component which uses this module
	 * is responsible for providing the required <code>DomainModel</code>,
	 * <code>TranslationTable</code> and <code>Query</code> instances.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module
	public static abstract class RetrieverModule<T extends Element>
	{
		/**
		 * Create a new <code>QueryRetriever</code> instance.
		 *
		 * @param  model The <code>DomainModel</code>, not null
		 * @param  table The <code>TranslationTable</code>, not null
		 * @param  query The <code>Query</code>, not null
		 * @return       The <code>QueryRetriever</code>
		 */

		@Provides
		public final Retriever<T> createRetriever (final DomainModel model, final TranslationTable table, final Query<T> query)
		{
			assert model != null : "model is NULL";
			assert table != null : "table is NULL";
			assert query != null : "query is NULL";

			return new QueryRetriever<T> (model, query, table);
		}
	}

	/** The Log */
	private final Logger log;

	/** The <code>DomainModel</code> */
	private final DomainModel model;

	/** The <code>Query</code> used to fetch <code>Element</code> instances */
	private final Query<T> query;

	/** The <code>TranslationTable</code> */
	private final TranslationTable table;

	/**
	 * Create the <code>QueryRetriever</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @param  query The <code>Query</code>, not null
	 * @param  table The <code>TranslationTable</code>, not null
	 */

	private QueryRetriever (final DomainModel model, final Query<T> query, final TranslationTable table)
	{
		assert model != null : "model is NULL";
		assert query != null : "query is NULL";
		assert table != null : "table is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.model = model;
		this.query = query;
		this.table = table;
	}

	private T load (final T element)
	{
		assert element != null : "element is NULL";

		if (! this.table.contains (element, this.model))
		{
			T result = this.query.setAllValues (element).query ();

			if (result != null)
			{
				this.table.put (element, result);
			}
		}

		return this.table.get (element, this.model);
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

		return  (this.model.contains (element)) ? element : this.load (element);
	}
}
