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

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;

/**
 * Write new <code>Element</code> instances into the <code>DataStore</code>.
 * This class is responsible for writing new <code>Element</code> instances
 * into the <code>DataStore</code> and making sure that there are no unintended
 * duplicates.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> type persist
 */

public final class Persister<T extends Element>
{
	/** The Logger */
	private final Logger log;

	/** The <code>DataStore</code> */
	private final DataStore datastore;

	/** The <code>IdGenerator</code> instance */
	private final IdGenerator generator;

	/** The meta-data <code>Creator</code> */
	private final MetaData<T> metadata;

	/** <code>Retriever</code> to test for duplication */
	private final Retriever<T> retriever;

	/** The <code>TranslationTable</code> */
	private final TranslationTable table;

	/**
	 * Create the <code>DataStoreProxy</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	public Persister (final DataStore datastore, final IdGenerator generator,
			final TranslationTable table, final MetaData<T> metadata, final Retriever<T> retriever)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.datastore = datastore;
		this.generator = generator;
		this.metadata = metadata;
		this.table = table;
		this.retriever = retriever;
	}

	/**
	 * Determine if all of the required fields for the specified
	 * <code>Element</code> instance are filled in.
	 *
	 * @param  element The <code>Element</code> to check, not null
	 *
	 * @return         <code>true</code> if all of the required fields are not
	 *                 null, <code>false</code> otherwise
	 */

	private boolean isComplete (final T element)
	{
		this.log.trace ("isComplete: element={}", element);

		return this.metadata.getProperties ()
			.stream ()
			.filter (x -> x.hasFlags (Property.Flags.REQUIRED))
			.map (x -> this.metadata.getValue (x, element))
			.allMatch (x -> x != null);
	}

	/**
	 * Determine if the <code>DataStore</code> contains the specified
	 * <code>Element</code> instance.
	 *
	 * @param  element The <code>Element</code> to test, not null
	 *
	 * @return         <code>true</code> if the <code>Element</code> is in the
	 *                 <code>DataStore</code>, <code>false</code> otherwise
	 */

	public boolean contains (final Element element)
	{
		this.log.trace ("contains: element={}", element);

		assert element != null : "element is NULL";

		return this.datastore.contains (element);
	}

	/**
	 * Insert the <code>Element</code> instance into the
	 * <code>DataStore</code>.  The <code>QueryProxy</code> ignores the
	 * reference <code>Element</code> since there is not association to create.
	 * As such, this method is the same as <code>insert (T element)</code>.
	 *
	 * @param  oldElement The reference <code>Element</code>
	 * @param  newElement The <code>Element</code> to store, not null
	 *
	 * @return            The <code>Element</code> stored in the
	 *                    <code>DataStore</code>
	 */

	public T insert (final T oldElement, final T newElement)
	{
		this.log.trace ("insert: oldElement={}, newElement={}", oldElement, newElement);

		assert newElement != null : "element is NULL";

		if (! this.isComplete (newElement))
		{
			this.log.error ("Required fields are missing from the Element (Wrong Builder)");
			throw new IllegalStateException ("Element is missing required fields");
		}

		if (! this.datastore.getTransaction ().isActive ())
		{
			this.log.error ("Attempting to build an Element without an active transaction");
			throw new IllegalStateException ("no active transaction");
		}

		T result = this.retriever.fetch ((newElement.equals (oldElement)) ? oldElement : newElement);

		if (result == null)
		{
			this.log.debug ("Setting ID");
			this.generator.setId (this.metadata, newElement);

			result = this.datastore.insert (newElement);

			this.log.debug ("Connecting the Element's relationships");
			if (! this.metadata.connect (this.datastore, result))
			{
				this.log.error ("Failed to connect relationships");
				throw new RuntimeException ("Failed to connect relationships");
			}

			if (result.equals (oldElement))
			{
				this.log.debug ("Inserting the Element mapping into the TranslationTable");
				this.table.put (oldElement, result);
			}
		}
		else
		{
			this.log.debug ("Using pre-existing element instance from the translation table");
		}

		return result;
	}
}
