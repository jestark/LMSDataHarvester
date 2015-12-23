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

package ca.uoguelph.socs.icc.edm.domain.datastore.memory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

import com.google.auto.factory.AutoFactory;
import com.google.auto.value.AutoValue;
import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.DomainModelFactory;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;
import ca.uoguelph.socs.icc.edm.domain.datastore.Transaction;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Memory based implementation of the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

@AutoFactory (implementing = { DataStore.DataStoreFactory.class })
public final class MemDataStore implements DataStore
{
	/**
	 * Dagger component used to create the <code>DomainModel</code> and
	 * <code>MemDataStore</code> instances.
	 */

	@Component (modules = { MemDataStoreModule.class })
	@Singleton
	public static interface MemDataStoreComponent  extends DataStore.DataStoreComponent
	{
		/**
		 * Get a reference to the <code>DomainModelFactory</code>.
		 *
		 * @return The <code>DomainModelFactory</code>
		 */

		@Override
		public abstract DomainModelFactory getDomainModelFactory ();
	}

	/**
	 * Dagger module to get <code>DataStoreFactory</code> instances.  This
	 * module exists to translate the <code>MemDataStoreFactory</code> instance
	 * created by <code>AutoFactory</code> to a
	 * <code>DataStore.DataStoreFactory</code> instance usable by the
	 * <code>DomainModel</code>.  This is usually automatic but Dagger needs it
	 * to be done explicitly.
	 */

	@Module
	static final class MemDataStoreModule
	{
		/**
		 * Get a reference to the <code>DomainStoreFactory</code>.
		 *
		 * @return The <code>DataStoreFactory</code>
		 */

		@Provides
		DataStore.DataStoreFactory getFactory (final MemDataStoreFactory factory)
		{
			return factory;
		}
	}

	/** The component for creating instances of the <code>DataStore</code>*/
	private static final DataStore.DataStoreComponent COMPONENT;

	/** The logger */
	private final Logger log;

	/** The <code>Set</code> of stored <code>Element</code> instances */
	private final Set<Wrapper<? extends Element>> elements;

	/** Indexed <code>Element</code> instances */
	private final Map<Index<?>, Element> index;

	/** The transaction manager for the <code>DataStore</code> */
	private Transaction transaction;

	/** Indication if the <code>DataStore</code> is open */
	private boolean open;

	/**
	 * Static initializer to create a constance instance of the Dagger
	 * component.
	 */

	static
	{
		COMPONENT = DaggerMemDataStore_MemDataStoreComponent.create ();
	}

	/**
	 * Create a new <code>MemDataStore</code> instance and return it
	 * encapsulated in a new <code>DomainModel</code> instance.
	 *
	 * @param  profile The <code>Profile</code>, not null
	 *
	 * @return         The <code>DomainModel</code>
	 */

	public static DomainModel create (final Profile profile)
	{
		Preconditions.checkNotNull (profile, "profile");

		return MemDataStore.COMPONENT.getDomainModelFactory ()
			.create (profile);
	}

	/**
	 * Get the instance of the <code>DataStoreComponent</code> which is used to
	 * create <code>MemDataStore</code> instances.
	 *
	 * @return The <code>DataStoreComponent</code>
	 */

	public static DataStore.DataStoreComponent getComponent ()
	{
		return MemDataStore.COMPONENT;
	}

	/**
	 * Create the <code>MemDataStore</code>.
	 *
	 * @param  profile The <code>Profile</code>
	 */

	protected MemDataStore (final Profile profile)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.open = true;
		this.transaction = null;

		this.elements = new HashSet<> ();
		this.index = new HashMap<> ();
	}

	/**
	 * Get a <code>Stream</code> of <code>Element</code> instances which match
	 * the specified <code>Filter</code>.  The <code>Stream</code> will be
	 * empty if no <code>Element</code> instances match.
	 *
	 * @param  <T>    The <code>Element</code> interface type
	 * @param  filter The <code>Filter</code>, not null
	 * @return        A <code>Stream</code> of <code>Element</code> instances
	 */

	<T extends Element> Stream<T> fetch (final Filter<T> filter)
	{
		this.log.trace ("fetch: filter={}", filter);

		assert filter != null : "filter is NULL";

		return this.elements.parallelStream ()
			.map (Wrapper::unwrap)
			.filter (x -> filter.getElementClass ()
					.isInstance (x))
			.map (x -> filter.getSelector ()
					.getElementClass ()
					.cast (x))
			.filter (x -> filter.test (x));
	}

	/**
	 * Get a <code>Stream</code> containing the <code>Element</code> which
	 * matches the supplied <code>Index</code>.  The <code>Stream</code> will be
	 * empty if no <code>Element</code> instances match.
	 *
	 * @param  <T>   The <code>Element</code> interface type
	 * @param  index The <code>Index</code>, not null
	 * @return       A <code>Stream</code> of <code>Element</code> instances
	 */

	<T extends Element> Stream<T> fetch (final Index<T> index)
	{
		this.log.trace ("fetch: index={}", index);

		assert index != null : "index is NULL";

		return (this.index.containsKey (index))
			? Stream.of (index.getSelector ()
					.getElementClass ()
					.cast (this.index.get (index)))
			: Stream.empty ();
	}

	/**
	 * Create a new <code>Query</code> instance for the specified
	 * <code>Selector</code>.
	 *
	 * @param  selector  The <code>Selector</code>, not null
	 * @param  impl      The <code>Element</code> implementation class, not null
	 * @param  model     The <code>DomaonModel</code>, not null
	 * @param  reference
	 * @return           The <code>Query</code>
	 */

	@Override
	public <T extends Element> Query<T> createQuery (
			Selector<T> selector,
			Class<? extends T> impl,
			DomainModel model,
			BiConsumer<T, DomainModel> reference)
	{
		assert selector != null;
		assert impl != null;

		return new MemQuery<T> (selector, impl, this);
	}

	/**
	 * Get an instance of the transaction manager for the
	 * <code>DataStore</code>.
	 *
	 * @return An instance of the transaction manager
	 */

	@Override
	public Transaction getTransaction ()
	{
		if (this.transaction == null)
		{
			this.transaction = new MemTransaction (this);
		}

		return this.transaction;
	}

	/**
	 * Determine if the <code>DataStore</code> is open.
	 *
	 * @return <code>true</code> if the <code>DataStore</code> is open,
	 *         <code>false</code> otherwise
	 */

	@Override
	public boolean isOpen ()
	{
		return this.open || this.transaction.isActive ();
	}

	/**
	 * Close the <code>DataStore</code>.  If there is an active transaction
	 * then the <code>DataStore</code> will be closed when the transaction
	 * completes.
	 */

	@Override
	public void close ()
	{
		this.log.trace ("close:");

		this.open = false;

		if (! this.transaction.isActive ())
		{
			this.elements.clear ();
			this.index.clear ();
		}
	}

	/**
	 * Determine if the specifed <code>Element</code> instance exists in the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to check, not null
	 * @return         <code>true</code> if the <code>Element</code> instance
	 *                 exists in the <code>DataStore</code>, <code>false</code>
	 *                 otherwise
	 */

	@Override
	public <T extends Element> boolean contains (final T element)
	{
		this.log.trace ("contains: element={}", element);

		assert element != null : "element is NULL";
		assert this.isOpen () : "datastore is closed";

		return this.elements.contains (new Wrapper<T> (element));
	}

	/**
	 * Get a <code>List</code> containing all of the ID numbers in the
	 * <code>DataStore</code> for instances of the specified
	 * <code>Element</code> class.
	 *
	 * @param  element The <code>Element</code> class, not null
	 * @return         A <code>List</code> of ID numbers, may be empty
	 */

	@Override
	public List<Long> getAllIds (final Class<? extends Element> element)
	{
		this.log.trace ("getAllIds: element={}", element);

		assert element != null : "element is NULL";
		assert this.isOpen () : "datastore is closed";

		return this.elements.parallelStream ()
			.map (Wrapper::unwrap)
			.filter (x -> element.isInstance (x))
			.map (Element::getId)
			.collect (Collectors.toList ());
	}

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  definition The <code>Definition</code> for the, not null
	 * @param  element    The <code>Element</code> instance to insert, not null
	 * @return            A reference to the <code>Element</code>
	 */

	@Override
	public <T extends Element> T insert (final Element.Definition<T> definition, final T element)
	{
		this.log.trace ("insert: definition={}, element={}", element);

		assert definition != null : "definition is NULL";
		assert element != null : "element is NULL";
		assert ! this.contains (element) : "element is already in the DataStore";
		assert this.transaction.isActive () : "No Active transaction";

		this.log.debug ("Inserting the Element");
		this.elements.add (new Wrapper<T> (element));

		this.log.debug ("building indexes");
		this.index.putAll (definition.selectors ()
			.filter (s -> s.getCardinality () != Selector.Cardinality.MULTIPLE && s.isConstant ())
			.collect (Collectors.toMap (s -> Index.create (s, definition.getElementClass (), element), s -> element)));

		return element;
	}

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  element  The <code>Element</code> instance to remove, not null
	 */

	@Override
	public <T extends Element> void remove (final T element)
	{
		this.log.trace ("remove: element={}", element);

		assert element != null : "element is NULL";
		assert this.contains (element) : "element is not in the DataStore";
		assert this.transaction.isActive () : "No Active transaction";

		this.log.debug ("removing element");
		this.elements.remove (new Wrapper<T> (element));

		this.log.debug ("removing indexes");
		this.index.entrySet ()
			.removeIf (x -> x.getValue () == element);
	}
}
