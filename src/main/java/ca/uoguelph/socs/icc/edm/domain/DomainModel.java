/* Copyright (C) 2014, 2015, 2016 James E. Stark
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

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import dagger.Module;
import dagger.Provides;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;
import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;
import ca.uoguelph.socs.icc.edm.domain.datastore.Transaction;
import ca.uoguelph.socs.icc.edm.domain.datastore.TranslationTable;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Access and manipulate <code>Element</code> instances contained within the
 * encapsulated <code>DataStore</code>.  This class provides a High-level
 * interface to the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

@AutoFactory
public final class DomainModel implements AutoCloseable
{
	/**
	 * Synchronise an <code>Element</code> across two <code>DomainModel</code>
	 * instances.  This class copies <code>Element</code> instances along with
	 * all of the associated <code>Element</code> instances across
	 * <code>DomainModel</code> instances.  <code>Element</code> instances are
	 * inserted into the destination <code>DomainModel</code> in there natural
	 * order, to ensure that the dependencies for an <code>Element</code>
	 * instance are inserted before it.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static final class Synchronizer
	{
		/** Size of the evict queue relative to the cache, at which it is processed */
		private static final double EVICT_FACTOR;

		/** The minimum size of the evict queue before it is process */
		private static final int EVICT_MIN;

		/** The log */
		private final Logger log;

		/** The destination <code>DomainModel</code> */
		private final DomainModel dest;

		/** The <code>Element</code> instances to synchronize */
		private Map<Element, Integer> elements;

		/**
		 * Static initializer to set the Batch size
		 */

		static
		{
			EVICT_FACTOR = 0.2;
			EVICT_MIN = 100;
		}

		/**
		 * Create the <code>Synchronizer</code>.
		 *
		 * @param  dest The destination <code>DomainModel</code>, not null
		 * @return      The <code>Synchronizer</code>
		 */

		private static Synchronizer create (final DomainModel dest)
		{
			assert dest != null : "dest is NULL";

			return new Synchronizer (dest);
		}

		/**
		 * Create the <code>Synchronizer</code>.
		 *
		 * @param  dest The destination <code>DomainModel</code>, not null
		 */

		private Synchronizer (final DomainModel dest)
		{
			assert dest != null : "dest is NULL";

			this.log = LoggerFactory.getLogger (this.getClass ());

			this.dest = dest;
			this.elements = new IdentityHashMap<> ();
		}

		/**
		 * Add the specified <code>Element</code> instance to the
		 * synchronization <code>Map</code> and initialize its dependency count
		 * to zero.
		 *
		 * @param  element The <code>Element</code>, not null
		 * @return         The <code>Element</code>
		 */

		private Element processElement (final Element element)
		{
			assert element != null : "element is NULL";
			assert ! this.elements.containsKey (element) : "element is already in the sync map";

			this.elements.put (element, Integer.valueOf (0));

			return element;
		}

		/**
		 * Increment the dependency counter for the specified
		 * <code>Element</code> instance.
		 *
		 * @param  element The <code>Element</code> instance, not null
		 * @return         The <code>Element</code>
		 */

		private Element incDependency (final Element element)
		{
			assert element != null : "element is NULL";
			assert this.elements.containsKey (element) : "element is not in the sync map";

			this.elements.put (element, this.elements.get (element) + 1);

			return element;
		}

		/**
		 * Decrement the dependency counter for the specified
		 * <code>Element</code> instance.
		 *
		 * @param  element The <code>Element</code> instance, not null
		 * @return         The <code>Element</code>
		 */

		private Element decDependency (final Element element)
		{
			assert element != null : "element is NULL";
			assert this.elements.containsKey (element) : "element is not in the sync map";

			this.elements.put (element, this.elements.get (element) - 1);

			return element;
		}

		/**
		 * Process the specified queue, adding the elements and their
		 * associations to the <code>Synchronizer</code>.
		 *
		 * @param  queue The queue, not null
		 */

		private void processQueue (final Deque<Element> queue)
		{
			this.log.trace ("processQueue: queue={}", queue);

			assert queue != null : "queue is NULL";

			while (! queue.isEmpty ())
			{
				Element element = queue.remove ();

				element.associations ()
					.filter (e -> (! this.elements.containsKey (e)))
					.map (e -> this.processElement (e))
					.forEach (e -> queue.add (e));

				element.dependencies ()
					.forEach (e -> this.incDependency (e));
			}
		}

		/**
		 * Add an <code>Element</code> instance and all of the associated
		 * <code>Element</code> instances to the <code>Synchronizer</code>.
		 *
		 * @param  element The <code>Element</code>, not null
		 * @return         This <code>Synchronizer</code>
		 */

		public Synchronizer add (final Element element)
		{
			this.log.trace ("addElement: element={}", element);

			Preconditions.checkNotNull (element, "element");

			if (! this.elements.containsKey (element))
			{
				Deque<Element> queue = new ArrayDeque<> ();
				queue.add (this.processElement (element));

				this.processQueue (queue);
			}

			return this;
		}

		/**
		 * Add all of the <code>Element</code> instances in the specified
		 * <code>Collection</code>, and all of the associated
		 * <code>Element</code> instances to the <code>Synchronizer</code>.
		 *
		 * @param  elements The <code>Collection</code> of <code>Element</code>
		 *                  instances, not null
		 * @return          This <code>Synchronizer</code>
		 */

		public <T extends Element> Synchronizer addAll (final Collection<T> elements)
		{
			this.log.trace ("addAll: elements={}", elements);

			Preconditions.checkNotNull (elements, "elements");

			final Deque<Element> queue = new ArrayDeque<> ();

			elements.stream ()
				.filter (e -> (! this.elements.containsKey (e)))
				.map (e -> this.processElement (e))
				.forEach (e -> queue.add (e));

			this.processQueue (queue);

			return this;
		}

		/**
		 * Perform the synchronization.  This methods inserts all of the
		 * <code>Elements</code> that are in the <code>Synchronizer</code> into
		 * the destination <code>DomainModel</code>.
		 *
		 * @return The destination <code>DomainModel</code>
		 */

		public DomainModel synchronize ()
		{
			this.log.trace ("synchronize:");

			int cachesize = 0;

			final Deque<Element> evictions = new ArrayDeque<> ();

			Iterator<Element> iterator = this.elements.keySet ()
				.stream ()
				.sorted ()
				.iterator ();

			while (iterator.hasNext ())
			{
				this.log.debug ("Processing batch");
				this.dest.getTransaction ().begin ();

				while (iterator.hasNext ()
						&& (evictions.size () < (cachesize * Synchronizer.EVICT_FACTOR)
							|| evictions.size () < Synchronizer.EVICT_MIN))
				{
					try
					{
						Element element = iterator.next ();

						this.log.trace ("Processing: {}/id={} dependencies={}", element.getClass (), element.getId (), this.elements.get (element));
						Element n = element.getBuilder (this.dest)
							.build ();

						cachesize += 1;

						if (this.elements.get (element) == 0)
						{
							this.log.trace ("Evicting element {}/id={} dependencies={}", element.getClass (), element.getId (), this.elements.get (element));
							evictions.add (n);
						}

						element.dependencies ()
							.map (e -> this.decDependency (e))
							.filter (e -> this.elements.get (e) == 0)
							.peek (e -> this.log.trace ("Evicting dependency {}/id={} dependencies={}", e.getClass (), e.getId (), this.elements.get (e)))
							.map (e -> DomainModel.table.get (e, this.dest).get ())
							.forEach (e -> evictions.add (e));
					}
					catch (Exception ex)
					{
						this.dest.getTransaction ().rollback ();

						throw ex;
					}
				}

				this.log.debug ("Committing batch");
				this.dest.getTransaction ().commit ();

				this.log.debug ("evicting {}/{} elements", evictions.size (), cachesize);
				cachesize -= evictions.size ();

				while (! evictions.isEmpty ())
				{
					Element element = evictions.remove ();

					DomainModel.table.remove (element);
					this.dest.datastore.evict (element);
				}
			}

			return this.dest;
		}
	}

	/**
	 * Dagger Module containing common <code>DomainModel</code> operations.
	 * This module contains the operations that are common to both the
	 * <code>Element</code> components and the <code>IdGenerator</code>
	 * components.  It provides access to the <code>DomainModel</code> and the
	 * underlying <code>DataStore</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module
	public static final class DomainModelModule
	{
		/** The <code>Element</code> class */
		private final Class<? extends Element> element;

		/** The <code>DomainModel</code> */
		private final DomainModel model;

		/**
		 * Create the <code>DomainModelModule</code>.
		 *
		 * @param  element The <code>Element</code> class
		 * @param  model   The <code>DomainModel</code>
		 */

		public DomainModelModule (final Class<? extends Element> element, final DomainModel model)
		{
			this.element = element;
			this.model = model;
		}

		/**
		 * Get the <code>DomainModel</code>
		 *
		 * @return The <code>DomainModel</code>
		 */

		@Provides
		public DomainModel getDomainModel ()
		{
			return this.model;
		}

		/**
		 * Get the <code>Element</code> class.
		 *
		 * @return The <code>Element</code> class
		 */

		@Provides
		public Class<? extends Element> getElementClass ()
		{
			return element;
		}

		/**
		 * Get a <code>Collection</code> containing all of the ID numbers
		 * associated with the <code>Element</code> in the
		 * <code>DomainModel</code>.
		 *
		 * @return The <code>Collection</code> of ID numbers
		 */

		@Provides
		public Collection<Long> getAllIds ()
		{
			return this.model.datastore.getAllIds (this.element);
		}
	}

	/** The <code>TranslationTable</code> */
	private static final TranslationTable table;

	/** Cache of <code>Element</code> <code>Component</code> instances */
	private final Map<Class<? extends Element>, Element.ElementComponent<? extends Element>> elementComponents;

	/** Cache of <code>IdGenerator</code> instances */
	private final Map<Class<? extends Element>, IdGenerator.IdGeneratorComponent> idComponents;

	/** The log */
	private final Logger log;

	/** The profile */
	private final Profile profile;

	/** The data store which contains all of the data */
	private final DataStore datastore;

	/**
	 * Static initializer to create the <code>TranslationTable</code> instance.
	 */

	static
	{
		table = TranslationTable.getInstance ();
	}

	/**
	 * Create the <code>DomainModel</code>.
	 *
	 * @param  profile The <code>Profile</code>, not null
	 * @param  factory The <code>DataStoreFactory</code>, not null
	 */

	public DomainModel (
			final Profile profile,
			final @Provided DataStore.DataStoreFactory factory)
	{
		this.log = LoggerFactory.getLogger (DomainModel.class);

		this.profile = profile;
		this.datastore = factory.getDataStore (profile);

		this.elementComponents = new HashMap<> ();
		this.idComponents = new HashMap<> ();
	}

	/**
	 * Insert a new <code>Element</code> instance into the
	 * <code>DataStore</code>.  This method inserts the <code>Element</code>
	 * instance specified by <code>netElement</code> into the
	 * <code>DataStore</code>, connects its relationships and updates the
	 * <code>TranslationTable</code> creating an association with the
	 * <code>Element</code> instance specified by <code>oldElement</code>
	 *
	 * @param  definition The <code>Definition</code>, not null
	 * @param  oldElement The <code>Element</code> to be associated with the
	 *                    inserted <code>Element</code>, may be null
	 * @param  newElement The <code>Element</code> to insert, not null
	 * @return            A reference to the <code>Element</code> instance in
	 *                    the <code>DataStore</code>
	 *
	 * @throws IllegalStateException If any of the relationships for the
	 *                               <code>Element</code> to be inserted fails
	 *                               to connect
	 */

	protected <T extends Element> T insert (
			final Element.Definition<T> definition,
			final @Nullable T oldElement,
			final T newElement)
	{
		this.log.trace ("insert: definition={}, oldElement={}, newElement={}", definition, oldElement, newElement);

		assert definition != null : "definition is NULL";
		assert newElement != null : "newElement is NULL";

		Preconditions.checkState (this.datastore.getTransaction (this).isActive (), "transaction required");

		this.log.debug ("inserting element into the DataStore: {}", newElement);
		T result = this.datastore.insert (definition, newElement);

		this.log.debug ("connecting relationships");
		if (! result.connect ())
		{
			this.log.error ("Failed to connect relationships");
			throw new IllegalStateException ("Failed to connect relationships");
		}

		if (result.equalsAll (oldElement))
		{
			this.log.debug ("Inserting the Element mapping into the TranslationTable");
			DomainModel.table.put (oldElement, result);
		}

		return result;
	}

	/**
	 * Get the <code>IdGeneratorComponent</code> for the specified
	 * <code>Element</code> class.  If the <code>Profile</code> does not contain
	 * and <code>IdGenerator</code> for the specified <code>Element</code>
	 * class, then this method will recursively process the super classes for
	 * the <code>Element</code> until it finds a <code>IdGenerator</code>.  An
	 * <code>IdGeneratorComponent</code> instance is shared between all of the
	 * sub-classes for the <code>Element</code> class upon which it is defined.
	 *
	 * @param  element The <code>Element</code> class, not null
	 * @return         The <code>IdGeneratorComponent</code>
	 *
	 * @throws IllegalStateException If an <code>IdGenerator</code> for the
	 *                               <code>Element</code> class.  This should
	 *                               never happen, as <code>Profile</code>
	 *                               requires that an <code>IdGenerator</code>
	 *                               to be defined for <code>Element</code>.
	 */

	@SuppressWarnings ("unchecked")
	protected IdGenerator.IdGeneratorComponent getIdGeneratorComponent (final Class<? extends Element> element)
	{
		this.log.trace ("getIdGeneratorComponent: element={}", element);

		if (! this.idComponents.containsKey (element))
		{
			if (this.profile.hasGenerator (element))
			{
				this.idComponents.put (element, this.profile.getGenerator (this, element));
			}
			else
			{
				if (! Element.class.isAssignableFrom (element.getSuperclass ()))
				{
					throw new IllegalStateException ("Can't find an IDGenerator");
				}

				this.idComponents.put (element, this.getIdGeneratorComponent ((Class<? extends Element>) element.getSuperclass ()));
			}
		}

		return this.idComponents.get (element);
	}

	/**
	 * Get a <code>Component</code> instance for the specified
	 * <code>Element</code> class.
	 *
	 * @param  <T>     The type of the <code>Element</code>
	 * @param  element The <code>Element</code> interface class, not null
	 * @return         The <code>Component</code>
	 */

	@SuppressWarnings ("unchecked")
	protected <T extends Element> Element.ElementComponent<T> getElementComponent (final Class<T> element)
	{
		this.log.trace ("getBuilder: element={}", element);

		Preconditions.checkNotNull (element);

		if (! this.elementComponents.containsKey (element))
		{
			this.elementComponents.put (element, this.profile.getDefinition (element)
					.getComponent (this));
		}

		return (Element.ElementComponent<T>) this.elementComponents.get (element);
	}

	/**
	 * Get a <code>Component</code> instance for the specified
	 * <code>Element</code> interface and implementation classes.
	 *
	 * @param  <T>     The type of the <code>Element</code>
	 * @param  element The <code>Element</code> interface class, not null
	 * @param  impl    The <code>Element</code> implementation class, not null
	 * @return         The <code>Component</code>
	 */

	@SuppressWarnings ("unchecked")
	protected <T extends Element> Element.ElementComponent<T> getElementComponent (final Class<T> element, final Class<? extends T> impl)
	{
		this.log.trace ("getBuilder: element={}, impl={}", element, impl);

		Preconditions.checkNotNull (element);
		Preconditions.checkNotNull (impl);

		if (! this.elementComponents.containsKey (impl))
		{
			this.elementComponents.put (impl, this.profile.getDefinition (element, impl)
					.getComponent (this));
		}

		return (Element.ElementComponent<T>) this.elementComponents.get (impl);
	}

	/**
	 * Determine if the <code>DomainModel</code> is mutable.
	 *
	 * @return <code>True</code> if the <code>DomainModel</code> is mutable,
	 *         <code>False</code> otherwise
	 */

	public boolean isMutable ()
	{
		return this.profile.isMutable ();
	}

	/**
	 * Determine if the underlying <code>DataStore</code> is open.
	 *
	 * @return <code>True</code> if the underlying <code>DataStore</code> is
	 *         open, <code>False</code> otherwise
	 */

	public boolean isOpen ()
	{
		return this.datastore.isOpen ();
	}

	/**
	 * Close the <code>DataStore</code>.  Closing the <code>DataStore</code>
	 * causes the <code>DataStore</code> to flush is caches and release all of
	 * its resources.
	 * <p>
	 * If the <code>DataStore</code> has an active <code>Transaction</code>
	 * then the transaction will be allowed to complete before the
	 * <code>DataStore</code> is closed.  However, all of the entries to the
	 * current <code>DataStore</code> will be immediately removed from the
	 * <code>TranslationTable</code>.  So operations executed as part of the
	 * <code>Transaction</code> after the <code>DomainModel</code> has been
	 * closed, which require the <code>TranslationTable</code> will probably
	 * fail.
	 */

	@Override
	public void close ()
	{
		this.datastore.close ();

		if (! this.datastore.getTransaction (this).isActive ())
		{
			DomainModel.table.removeAll (this);
		}
	}

	/**
	 * Get the <code>Definition</code> for the specified <code>Element</code>.
	 * The specified <code>Element</code> class may be either an interface class
	 * or an implementation class.  For an interface class the
	 * <code>Definition</code> for the default implementation will be returned.
	 *
	 * @param  <T>     The type of the <code>Element</code>
	 * @param  element The <code>Element</code> class, not null
	 * @return         The <code>Definition</code> for the <code>Element</code>
	 *
	 * @throws IllegalArgumentException If the specified <code>Element</code>
	 *                                  class does not have a registered
	 *                                  implementation
	 */

	public <T extends Element> Element.Definition<T> getDefinition (final Class<T> element)
	{
		this.log.trace ("getDefinition: element={}", element);

		Preconditions.checkNotNull (element, "element");

		return this.profile.getDefinition (element);
	}

	/**
	 * Get the <code>Definition</code> for the specified <code>Element</code>
	 * implementation.
	 *
	 * @param  <T>     The type of the <code>Element</code>
	 * @param  element The <code>Element</code> interface class, not null
	 * @param  impl    The <code>Element</code> implementation class, not null
	 * @return         The <code>Definition</code> for the <code>Element</code>
	 *
	 * @throws IllegalArgumentException If the implementation class does not
	 *                                  have a <code>Definition</code>
	 */

	public <T extends Element> Element.Definition<T> getDefinition (final Class<T> element, final Class<? extends T> impl)
	{
		this.log.trace ("getDefinition: element={}, impl={}", element, impl);

		Preconditions.checkNotNull (element, "element");
		Preconditions.checkNotNull (impl, "impl");

		return this.profile.getDefinition (element, impl);
	}

	/**
	 * Get a <code>Query</code> for the specified <code>Element</code> using
	 * the default implementation class defined in the <code>Profile</code>.
	 *
	 * @param  <T>      The type of the <code>Element</code> returned by the
	 *                  <code>Query</code>
	 * @param  selector The <code>Selector</code>, not null
	 * @return          The <code>Query</code>
	 */

	public <T extends Element> Query<T> getQuery (final Selector<T> selector)
	{
		this.log.trace ("getQuery: selector={}", selector);

		Preconditions.checkNotNull (selector, "selector");
		Preconditions.checkArgument (this.profile.hasElement (selector.getElementClass ()),
				"no implementation for element: %s", selector.getElementClass ().getSimpleName ());

		return this.profile.getDefinition (selector.getElementClass ()).getQuery (this, selector);
	}

	/**
	 * Get a <code>Query</code> for the specified <code>Element</code> using
	 * the specified implementation class.
	 *
	 * @param  <T>      The type of the <code>Element</code> returned by the
	 *                  <code>Query</code>
	 * @param  selector The <code>Selector</code>, not null
	 * @param  impl     The <code>Element</code> implementation class, not null
	 * @return          The <code>Query</code>
	 */

	public <T extends Element> Query<T> getQuery (final Selector<T> selector, final Class<? extends T> impl)
	{
		this.log.trace ("getQuery: selector={}, impl={}", selector, impl);

		Preconditions.checkNotNull (selector, "selector");
		Preconditions.checkNotNull (impl, "impl");

		return this.datastore.createQuery (selector, impl, this, T::setDomainModel);
	}

	/**
	 * Get a <code>Sychronizer</code> instance for the <code>DomainModel</code>.
	 *
	 * @return The <code>Synchronizer</code>
	 */

	public Synchronizer getSynchronizer ()
	{
		return Synchronizer.create (this);
	}

	/**
	 * Get a reference to the <code>Transaction</code> instance for the
	 * <code>DataStore</code>.  A <code>Transaction</code> can only be returned
	 * for a mutable <code>DataStore</code>.
	 *
	 * @return A reference to the <code>Transaction</code>
	 *
	 * @throws IllegalStateExcption If the <code>DataStore</code> is immutable
	 */

	public Transaction getTransaction ()
	{
		if (! this.isMutable ())
		{
			this.log.error ("Attempting to get a Transaction for a immutable DataStore");
			throw new IllegalStateException ("DataStore is immutable");
		}

		return this.datastore.getTransaction (this);
	}

	/**
	 * Test an instance of an <code>Element</code> to determine if a reference
	 * to that <code>Element</code> instance exists in the
	 * <code>DataStore</code>.  The exact behaviour of this method is
	 * determined by the implementation of the <code>DataStore</code>.
	 * <p>
	 * If the <code>Element</code> instance was created by the current instance
	 * of the <code>DataStore</code> then this method, should return
	 * <code>True</code>.  Otherwise, this method should return
	 * <code>False</code>, even if an identical <code>Element</code> instance
	 * exists in the <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to test, not null
	 * @return         <code>True</code> if the <code>DataStore</code>
	 *                 instance contains a reference to the
	 *                 <code>Element</code>, <code>False</code> otherwise
	 */

	public boolean contains (final Element element)
	{
		this.log.trace ("contains: element={}", element);

		Preconditions.checkNotNull (element, "element");

		return (this == element.getDomainModel () && this.datastore.contains (element));
	}

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.  To remove the specified <code>Element</code>,
	 * the <code>Element</code> must exist in the <code>DataStore</code> and
	 * the <code>DataStore</code> must have an active <code>Transaction</code>.
	 *
	 * @param  element The <code>Element</code> to remove, not null
	 *
	 * @throws IllegalArgumentException If the <code>Element</code> does not
	 *                                  exist in the <code>DataStore</code>
	 * @throws IllegalStateException    If there is not an active
	 *                                  <code>Transaction</code>
	 */

	public <T extends Element> void remove (final T element)
	{
		this.log.trace ("remove: element={}", element);

		Preconditions.checkNotNull (element, "element");
		Preconditions.checkArgument (this.datastore.contains (element), "element is not in the datastore");
		Preconditions.checkState (this.datastore.getTransaction (this).isActive (), "transaction required");

		this.log.debug ("Disconnecting relationships");
		if (! element.disconnect ())
		{
			this.log.error ("Can not safely remove the element: {}", element);
			throw new IllegalStateException ("Can not break the relationships for the Element");
		}

		this.log.debug ("removing Element from the DataStore");
		this.datastore.remove (element);
		DomainModel.table.remove (element);
	}
}
