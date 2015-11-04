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
import java.util.List;
import java.util.Map;
import java.util.Queue;

import java.util.ArrayDeque;
import java.util.EnumMap;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;
import ca.uoguelph.socs.icc.edm.domain.datastore.TranslationTable;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;

final class RelationshipProcessor
{
	public static enum Priority
	{
		DEPENDS,
		RECOMMENDS,
		DEFERRED;
	}

	private final Logger log;

	private final MetaData<Element> metadata;

	private final Map<Priority, List<Property<?>>> properties;

	private static Priority determinePriority (final Property<?> property)
	{
		return (property.hasFlags (Property.Flags.REQUIRED)
				|| property.hasFlags (Property.Flags.MUTABLE))
			? Priority.DEPENDS
			: (property.hasFlags (Property.Flags.RECOMMENDED))
			? Priority.RECOMMENDS : Priority.DEFERRED;
	}

	public RelationshipProcessor (final MetaData<Element> metadata)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.metadata = metadata;

		this.properties = this.metadata.properties ()
			.filter (p -> p.hasFlags (Property.Flags.RELATIONSHIP))
			.collect (Collectors.groupingBy (p -> determinePriority (p),
					() -> new EnumMap<Priority, List<Property<?>>> (Priority.class),
					Collectors.toList ()));
	}

	public boolean hasPriority (final Priority priority)
	{
		assert priority != null : "Priority is NULL";

		return ! this.properties.get (priority).isEmpty ();
	}

	@SuppressWarnings ("unchecked")
	public List<Element> getByPriority (final Priority priority, final Element element)
	{
		assert priority != null : "Priority is NULL";
		assert element != null : "element is NULL";

		return (List<Element>) this.properties.get (priority).stream ()
			.flatMap (p -> element.stream (p))
			.collect (Collectors.toList ());
	}
}

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
	private final Retriever<Element> retriever;

	/** */
	private final Queue<Element> recommended;

	/** The delayed processing queue*/
	private final Queue<Element> deferred;

	/**
	 * Create the <code>InsertProcessor</code>
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  ttable    The <code>TranslationTable</code>, not null
	 */

	protected InsertProcessor (final DataStore datastore, final Retriever<Element> retriever)
	{
		this.log = LoggerFactory.getLogger (InsertProcessor.class);

		this.retriever = retriever;
		this.datastore = datastore;

		this.deferred = new ArrayDeque<Element> ();
		this.recommended = new ArrayDeque<Element> ();
	}

/*	private List<Element> processQueue (final Queue<Element> queue)
	{
		this.log.trace ("processQueue: queue={}", queue);

		while (! queue.isEmpty ())
		{

		}

		return null;
	}

	private void processDependencies (final Element element)
	{
		this.log.trace ("element={}", element);

		assert element != null : "element is NULL";

		this.log.debug ("Processing Relationships for Element: {}", element);

		RelationshipProcessor p = new RelationshipProcessor (datastore.getProfile ()
			.getCreator (Element.class, element.getClass ()));

		this.recommended.addAll (p.getByPrior);
		this.deferred.addAll (p.getByPriority (RelationshipProcessor.Priority.DEFERRED));
	}
*/
	/**
	 * Clear the delayed processing queue
	 */

	public void clear ()
	{
		this.log.trace ("clear:");

		this.recommended.clear ();
		this.deferred.clear ();
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

	public <T extends Element> T insert (final T element)
	{
		this.log.trace ("insert: element={}", element);

/*		if (! this.ttable.contains (element, this.datastore))
		{

			this.log.debug ("Copying Element into destination DataStore: {}", element);

			Element newElement = element.getBuilder (this.datastore)
				.build ();

			if (this.ttable.get (element, this.datastore) != newElement)
			{
				this.ttable.put (element, newElement);
			}
		}

*/		return null; // this.ttable.get (element, this.datastore);
	}

	/**
	 * Insert all of the <code>Element</code> instances in the specified
	 * <code>Collection</code> into the <code>DataStore</code>.
	 *
	 * @param  elements The <code>Collection</code> of <code>Element</code>
	 *                  instances to insert, not null
	 *
	 * @return          A <code>Collection</code> containing all of the
	 *                  <code>Element</code> instances in the
	 *                  <code>DataStore</code> in the same order as the input
	 *                  <code>Collection</code>
	 */

	public <T extends Element> Collection<T> insert (final Collection<T> elements)
	{
		this.log.trace ("insert: elements={}", elements);

		assert elements != null : "elements is NULL";

		return null;
	}

	/**
	 * Process all of the queued <code>Element</code> instances.
	 */

	public void processDeferred ()
	{
		this.log.trace ("processDeferred:");

	}

	public void processRecommended ()
	{
		this.log.trace ("processRecommended");
	}
}
