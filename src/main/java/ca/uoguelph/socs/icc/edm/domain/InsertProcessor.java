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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.Collection;
import java.util.Queue;

import java.util.ArrayDeque;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;

/**
 * Insert <code>Element</code> instances along with their relationships.  This
 * class analyzes an <code>Element</code> instance and inserts it into the
 * <code>DataStore</code> along with all of its dependencies and, optionally,
 * its other relationships.  Before an <code>Element</code> instance is
 * inserted into the <code>DataStore</code> its relationships are traversed
 * recursively.  Any relationships which are dependencies of the specified
 * <code>Element</code> instance are inserted immediately, and any
 * relationships which are not dependencies are queued for later processing.
 * <p>
 * Once an <code>Element</code> has been inserted into the
 * <code>DataStore</code> it is added to the <code>TranslationTable</code>.
 * The <code>TranslationTable</code> is inspected before an
 * <code>Element</code> instance is processed.  If the <code>Element</code>
 * instance is found in the <code>TranslationTable</code> then the
 * <code>Element</code> instance is not processed as it is already present in
 * the <code>DataStore</code>.
 */

public final class InsertProcessor
{
	/** The Logger */
	private final Logger log;

	/** The <code>DataStore</code> */
	private final DataStore datastore;

	/** The <code>TranslationTable</code> */
	private final TranslationTable ttable;

	/** The delayed processing queue*/
	private final Queue<Element> queue;

	/**
	 * Create the <code>InsertProcessor</code>
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  ttable    The <code>TranslationTable</code>, not null
	 */

	protected InsertProcessor (final DataStore datastore, final TranslationTable ttable)
	{
		this.log = LoggerFactory.getLogger (InsertProcessor.class);

		this.ttable = ttable;
		this.datastore = datastore;
		this.queue = new ArrayDeque<Element> ();
	}

	/**
	 * Clear the delayed processing queue
	 */

	public void clear ()
	{
		this.log.trace ("clear:");

		this.queue.clear ();
	}

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code> along with any dependencies.  This method will
	 * recursively process the specified <code>Element</code> and its
	 * dependencies, inserting <code>Element</code> and any of its dependencies
	 * which are missing from the <code>DataStore</code>.  <code>Element</code>
	 * instances which which the specified <code>Element</code> instance (or
	 * its dependencies) have non-dependency relationships are appended to the
	 * delayed processing queue.
	 *
	 * @param  element               The <code>Element</code> to insert
	 *
	 * @return                       The instance of the <code>Element</code>
	 *                               in the <code>DataStore</code>
	 * @throws IllegalStateException if a <code>REQUIRED</code>
	 *                               <code>Property</code> is NULL
	 * @throws IllegalStateException if a <code>REQUIRED</code>
	 *                               <code>Property</code> is
	 *                               <code>MULTIVALUED</code>
	 */

	public <T extends Element> T processElement (final T element)
	{
		this.log.trace ("processElement: element={}", element);

		if ((element != null) && (! this.ttable.contains (element, this.datastore)))
		{
			MetaData<Element> metadata = datastore.getProfile ().getCreator (Element.class, element.getClass ());

			this.log.debug ("Processing Relationships for Element: {}", element);

			for (Property<?> p : metadata.getProperties ())
			{
				if (p.isRelationship ())
				{
					if (p.isRequired ())
					{
						if (p.isMultivalued ())
						{
							throw new IllegalStateException ("Required Property is Multi-valued");
						}
						else if (metadata.getValue (p, element) == null)
						{
							this.log.error ("Required property {} is NULL", p.getName ());
							throw new IllegalStateException ("Encountered a Required property which is NULL");
						}
						else
						{
							this.log.debug ("Processing required relationship: {}", p.getName ());
							this.processElement ((Element) metadata.getValue (p, element));
						}
					}
					else if (p.isMutable ())
					{
						if (p.isMultivalued ())
						{
							this.log.debug ("Processing Multi-Valued Mutable relationship: {}", p.getName ());
							metadata.getValues (p, element)
								.forEach (x -> this.processElement ((Element) x));
						}
						else if (metadata.getValue (p, element) != null)
						{
							this.log.debug ("Processing Single-Valued Mutable relationship: {}", p.getName ());
							this.processElement ((Element) metadata.getValue (p, element));
						}
						else
						{
							this.log.debug ("Skipping NULL Single-Valued Mutable relationship: {}", p.getName ());
						}
					}
					else
					{
						if (p.isMultivalued ())
						{
							this.log.debug ("Queuing Multi-Valued relationship: {}", p.getName ());
							metadata.getValues (p, element)
								.forEach (x -> this.queue.add ((Element) x));
						}
						else if (metadata.getValue (p, element) != null)
						{
							this.log.debug ("Queuing Single-Valued relationship: {}", p.getName ());
							this.queue.add ((Element) metadata.getValue (p, element));
						}
						else
						{
							this.log.debug ("Skipping NULL Single-Valued relationship: {}", p.getName ());
						}
					}
				}
			}

			this.log.debug ("Copying Element into destination DataStore: {}", element);

			Element newElement = element.getBuilder (this.datastore)
				.build ();

			if (this.ttable.get (element, this.datastore) != newElement)
			{
				this.ttable.put (element, newElement);
			}
		}

		return this.ttable.get (element, this.datastore);
	}

	/**
	 * Insert all of the <code>Element</code> instances in the specified
	 * <code>Collection</code> into the <code>DataStore</code>.
	 *
	 * @param  inputs The <code>Collection</code> of <code>Element</code>
	 *                instances to insert, not null
	 *
	 * @return        A <code>Collection</code> containing all of the
	 *                <code>Element</code> instances in the
	 *                <code>DataStore</code> in the same order as the input
	 *                <code>Collection</code>
	 */

	public <T extends Element> Collection<T> processElements (final Collection<T> inputs)
	{
		this.log.trace ("processElements: inputs={}", inputs);

		assert inputs != null : "inputs is NULL";

		return inputs.stream ()
			.map (x -> this.processElement (x))
			.collect (Collectors.toList ());
	}

	/**
	 * Process all of the queued <code>Element</code> instances.
	 */

	public void processQueue ()
	{
		this.log.trace ("processQueue:");

		while (! this.queue.isEmpty ())
		{
			this.processElement (this.queue.remove ());
		}
	}
}
