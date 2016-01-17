/* Copyright (C) 2015, 2016 James E. Stark
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
 * Implementation of the <code>DataStoreProxy</code> using the
 * <code>TranslationTable</code>.  This <code>Retriever</code> implementation
 * fetches <code>Element</code> instances by inspecting the
 * <code>TranslationTable</code>.  If the <code>TranslationTable</code>
 * contains an <code>Element</code> instance corresponding to the supplied
 * <code>Element</code> instance, for the destination <code>Datastore</code>,
 * then that instance is returned.  Otherwise this <code>Retriever</code>
 * implementation will return null.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the <code>Element</code>
 */

public final class TableRetriever<T extends Element> implements Retriever<T>
{
	/** The Log */
	private final Logger log;

	/** The <code>DomainModel</code> */
	private final DomainModel model;

	/**
	 * Create the <code>TableRetriever</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 */

	@Inject
	TableRetriever (final DomainModel model)
	{
		assert model != null : "model is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.model = model;
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

		Optional<T> result = Optional.empty ();

		if (this.model.contains (element))
		{
			this.log.debug ("Element is already in the DomainModel, skipping query: {}", element);
			result = Optional.of (element);
		}
		else if (this.model != element.getDomainModel ())
		{
			this.log.debug ("Returning Cached Element from the TranslationTable: {}", element);
			result = TranslationTable.getInstance ().get (element, this.model);
		}

		return result;
	}
}
