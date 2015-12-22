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

import java.util.Optional;

import javax.inject.Inject;

import com.google.common.base.Preconditions;

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
	/** The <code>TranslationTable</code> */
	private static final TranslationTable table;

	/** The Log */
	private final Logger log;

	/** The <code>DomainModel</code> */
	private final DomainModel model;

	/** The <code>Query</code> used to fetch <code>Element</code> instances */
	private final Query<T> query;

	/**
	 * Static initializer to set the reference to the Translation table.
	 */

	static
	{
		table = TranslationTable.getInstance ();
	}

	/**
	 * Create the <code>QueryRetriever</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @param  query The <code>Query</code>, not null
	 */

	@Inject
	QueryRetriever (final DomainModel model, final Query<T> query)
	{
		assert model != null : "model is NULL";
		assert query != null : "query is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.model = model;
		this.query = query;
	}

	/**
	 * Add an association between the supplied and retrieved
	 * <code>Element</code> instances to the <code>TranslationTable</code>.
	 *
	 * @param  supplied  The supplied <code>Element</code> instance, not null
	 * @param  retrieved The retrieved <code>Element</code> instance, not null
	 * @return           The retrieved <code>Element</code> instance
	 */

	private T store (final T supplied, final T retrieved)
	{
		this.log.trace ("store: supplied={}, retrieved={}", supplied, retrieved);

		assert supplied != null : "supplied is NULL";
		assert retrieved != null : "retrieved is NULL";

		if (supplied.equalsAll (retrieved))
		{
			this.log.debug ("Creating association in the translation table");
			QueryRetriever.table.put (supplied, retrieved);
		}
		else
		{
			this.log.debug ("Elements are different: DataStore ({}), Supplied ({})", retrieved, supplied);
			throw new IllegalStateException ("The Element in the DataStore is not identical to the supplied Element");
		}

		return retrieved;
	}

	/**
	 * Retrieve the <code>Element</code> instance from the
	 * <code>DataStore</code> corresponding specified <code>Element</code>
	 * instance.  If the specified <code>Element</code> instance is already in
	 * the <code>DataStore</code> then that will be returned.  Otherwise, this
	 * method will return the matching <code>Element</code> instance from the
	 * <code>DataStore</code> or <code>null</code> if there is no matching
	 * <code>Element</code> instance in the <code>DataStore</code>.
	 * <p>
	 * This method will ensure that the <code>Element</code> instance returned
	 * from the <code>DataStore</code> is identical to the supplied instance by
	 * comparing them using the <code>equalsAll</code> method.  If the
	 * <code>Element</code> instances are not identical, an
	 * <code>IllegalStateException</code> will be thrown.
	 *
	 * @param  element The <code>Element</code> instance, not null
	 * @return         An <code>Optional</code> containing the
	 *                 <code>Element</code> instance in the
	 *                 <code>DataStore</code>
	 *
	 * @throws IllegalStateException if the <code>Element</code> instance in the
	 *                               <code>DataStore</code> is not identical to
	 *                               the supplied <code>Element</code> instance
	 */

	@Override
	public Optional<T> fetch (final T element)
	{
		this.log.trace ("fetch: element={}", element);

		assert element != null : "element is NULL";

		Preconditions.checkState (this.model.isOpen (), "datastore is closed");

		Optional<T> result = null;

		if (this.model.contains (element))
		{
			result = Optional.of (element);
		}
		else if (QueryRetriever.table.contains (element, this.model))
		{
			result = QueryRetriever.table.get (element, this.model);
		}
		else
		{
			result = this.query.setAllValues (element)
				.query ()
				.map (x -> this.store (element, x));
		}

		return result;
	}
}
