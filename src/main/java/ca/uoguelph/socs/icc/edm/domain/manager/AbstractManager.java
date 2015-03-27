/* Copyright (C) 2014, 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;
import ca.uoguelph.socs.icc.edm.domain.ElementManager;

import ca.uoguelph.socs.icc.edm.domain.builder.AbstractBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreProfile;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;
import ca.uoguelph.socs.icc.edm.domain.datastore.QueryFactory;

/**
 * Top level interface for all operations involving the domain model and the
 * underlying data-store.  This class and its subclasses are responsible for
 * all operations related to adding, retrieving or removing elements from the
 * domain model and its underlying data-store.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>Element</code> to be processed
 */

public abstract class AbstractManager<T extends Element> implements ElementManager<T>
{
	/** The manager factory */
	private static final ElementManagerFactory FACTORY;

	/** The logger */
	protected final Logger log;

	/** The type of <code>Element</code> which is being managed */
	private final Class<T> type;

	/** The <code>DomainModel</code> instance which owns this manager. */
	protected final DataStore datastore;

	/**
	 * static initializer to create the factory.
	 */

	static
	{
		FACTORY = new ElementManagerFactory ();
	}

	/**
	 * Get an instance of an <code>ElementManager</code> interface for the
	 * specified <code>DataStore</code>.  This method is intended to be used by
	 * the <code>DomainModel</code> and <code>ElementManager</code> instances
	 * to get an instance of the requeested <code>ElementManager</code> as
	 * required.
	 *
	 * @param  <T>       The type of <code>ElementManager</code> to return
	 * @param  <U>       The type of <code>Element</code> operated on by the
	 *                   <code>ElementManager</code>
	 * @param  element   The <code>Element</code> interface class, not null
	 * @param  manager   The <code>ElementManager</code> interface class, not
	 *                   null
	 * @param  datastore The <code>DataStore</code> upon which the
	 *                   <code>ElementManager</code> instance is to operate,
	 *                   not null
	 *
	 * @return           An instance of the requested
	 *                   <code>ElementManager</code> interface on the specified
	 *                   <code>DataStore</code>
	 */

	public static final <T extends ElementManager<U>, U extends Element> T getInstance (final Class<U> element, final Class<T> manager, final DataStore datastore)
	{
		assert element != null : "element is NULL";
		assert manager != null : "manager is NULL";
		assert datastore != null : "datastore is NULL";

		return FACTORY.create (element, manager, datastore);
	}

	/**
	 * Register an <code>ElementManager</code> implementation with the factory.
	 * This method is intended to be used by the <code>ElementManager</code>
	 * implementations to register themselves with the factory so that they may
	 * be instantiated on demand.
	 *
	 * @param  <T>     The type of <code>ElementManager</code> to register
	 * @param  manager The <code>ElementManager</code> interface class, not null
	 * @param  impl    The <code>ElementManager</code> implementation class,
	 *                 not null
	 * @param  factory The <code>ManagerFactory</code> instance, not null
	 */

	protected static final <T extends ElementManager<? extends Element>> void registerManager (final Class<T> manager, Class<? extends T> impl, final ManagerFactory<T> factory)
	{
		FACTORY.registerFactory (manager, impl, factory);
	}

	/**
	 * Create the <code>AbstractManager</code>.
	 *
	 * @param  type      The type of <code>Element</code> which is being
	 *                   managed, not null
	 * @param  datastore The <code>DataStore</code> upon which this
	 *                   <code>ElementManager</code> will operate, not null
	 */

	public AbstractManager (final Class<T> type, final DataStore datastore)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.type = type;
		this.datastore = datastore;
	}

	/**
	 * Get an instance of the <code>DataStoreQuery</code>.  This method will
	 * get a <code>DataStoreQuery</code> for the <code>Element</code> type of
	 * the manager using the implementation class specified in the
	 * <code>DataStoreProfile</code>.
	 *
	 * @return The <code>DataStoreQuery</code> instance
	 */

	protected final DataStoreQuery<T> fetchQuery ()
	{
		this.log.trace ("fetchQuery:");

		return (QueryFactory.getInstance ()).create (this.type, this.datastore);
	}

	/**
	 * Get an instance of the <code>DataStoreQuery</code>.  This method will
	 * get a <code>DataStoreQuery</code> for the <code>Element</code> type of
	 * the manager using the specified implementation class.
	 *
	 * @return The <code>DataStoreQuery</code> instance
	 */

	protected final DataStoreQuery<T> fetchQuery (final Class<? extends Element> impl)
	{
		this.log.trace ("fetchQuery: impl={}", impl);

		assert impl != null : "impl is NULL";
		assert this.type.isAssignableFrom (impl) : "impl does not extend " + this.type.getSimpleName ();

		return (QueryFactory.getInstance ()).create (this.type, impl, this.datastore);
	}

	/**
	 * Get an <code>ElementBuilder</code> instance of the specified type for
	 * the specified <code>Element</code> implementation class.
	 *
	 * @param  <B>      The type of <code>ElementBuilder</code> to be returned
	 * @param  builder  The <code>ElementBuilder</code> interface class, not
	 *                  null
	 * @param  element  The <code>Element</code> implementation class, not null
	 * @param  argument The
	 *
	 * @return          The <code>ElementBuilder</code> instance
	 */

	protected final <B extends ElementBuilder<T>> B getBuilder (final Class<B> builder, final Class<? extends Element> element, final Element argument)
	{
		this.log.trace ("getBuilder: builder={}, element={}, argument={}", builder, element, argument);

		assert builder != null : "builder is NULL";
		assert element != null : "element is NULL";
		assert argument != null : "argument is NULL";

		return AbstractBuilder.getInstance (builder, element, new ManagerProxy<T> (this, element, argument));
	}

	/**
	 * Get an <code>ElementBuilder</code> instance of the specified type for
	 * the specified <code>Element</code> implementation class.
	 *
	 * @param  <B>     The type of <code>ElementBuilder</code> to be returned
	 * @param  builder The <code>ElementBuilder</code> interface class, not null
	 * @param  element The <code>Element</code> implementation class, not null
	 *
	 * @return         The <code>ElementBuilder</code> instance
	 */

	protected final <B extends ElementBuilder<T>> B getBuilder (final Class<B> builder, final Class<? extends Element> element)
	{
		this.log.trace ("getBuilder: builder={}, element={}", builder, element);

		assert builder != null : "builder is NULL";
		assert element != null : "element is NULL";

		return AbstractBuilder.getInstance (builder, element, new ManagerProxy<T> (this, element));
	}

	/**
	 * Get an <code>ElementBuilder</code> instance of the specified type.
	 *
	 * @param  <B>     The type of <code>ElementBuilder</code> to be returned
	 * @param  builder The <code>ElementBuilder</code> interface class, not null
	 *
	 * @return         The <code>ElementBuilder</code> instance
	 */

	protected final <B extends ElementBuilder<T>> B getBuilder (final Class<B> builder)
	{
		this.log.trace ("getBuilder: builder={}", builder);

		assert builder != null : "builder is NULL";

		Class<? extends Element> element = (this.datastore.getProfile ()).getImplClass (this.type);

		return AbstractBuilder.getInstance (builder, element, new ManagerProxy<T> (this, element));
	}

	/**
	 * Get an instance of the specified <code>ElementManager</code>.
	 *
	 * @param  <E>     The type of <code>Element</code> operated on by the
	 *                   <code>ElementManager</code>
	 * @param  <M>     The type of <code>ElementManager</code> to be returned
	 * @param  element The <code>Element</code> interface class, not null
	 * @param  manager The <code>ElementManager</code> interface class, not null
	 *
	 * @return         An instance of the requested <code>ElementManager</code>
	 */

	protected final <M extends ElementManager<E>, E extends Element> M getManager (final Class<E> element, final Class<M> manager)
	{
		this.log.trace ("getManager: element={}, manager={}", element, manager);

		assert element != null : "element is NULL";
		assert manager != null : "manager is NULL";

		return AbstractManager.getInstance (element, manager, this.datastore);
	}

	/**
	 * Get the next available DataStore ID number.  The number will be chosen
	 * by the IdGenerator algorithm set in the <code>DataStoreProfile</code>
	 *
	 * @return A <code>Long</code> containing the ID number
	 */

	protected final Long nextId (Class<? extends Element> implClass)
	{
		return (this.fetchQuery (implClass)).nextId ();
	}

	/**
	 * Insert the specified <code>Element</code> into the
	 * <code>DataStore</code>.  This method is intended to be used by the
	 * <code>ElementBuilder</code> instances to add new <code>Element</code>
	 * instances to the <code>DataStore</code>
	 *
	 * @param  element The <code>Element</code> to insert
	 *
	 * @return         A reference to the <code>Element</code>
	 */

	protected T insertElement (final T element)
	{
		this.log.trace ("insertElement: element={}", element);

		try
		{
			return (this.fetchQuery (element.getClass ())).insert (element);
		}
		catch (Exception ex)
		{
			this.log.error ("Insert Element into DataStore Failed: {}", ex);
			throw new IllegalStateException ("DataStore Insert failed", ex);
		}
	}

	/**
	 * Determine if the <code>DomainModel</code> is mutable.
	 *
	 * @return <code>True</code> if the <code>DomainModel</code> is mutable,
	 *         <code>False</code> otherwise
	 */

	public boolean isMutable ()
	{
		return (this.getProfile()).isMutable ();
	}

	/**
	 * Determine if the underlying <code>DataStore</code> is open.
	 *
	 * @return <code>True</code> if the underlying <code>DataStore</code> is
	 *         open, <code>False</code> otherwise
	 * @see    ca.uoguelph.socs.icc.edm.domain.datastore.DataStore#isOpen
	 */

	public boolean isOpen ()
	{
		return this.datastore.isOpen ();
	}

	/**
	 * Get a copy of the <code>DataStoreProfile</code>.
	 *
	 * @return The <code>DataStoreProfile</code>
	 */

	public DataStoreProfile getProfile ()
	{
		return this.datastore.getProfile ();
	}

	/**
	 * Test an instance of an <code>Element</code> to determine if a reference
	 * to that <code>Element</code> instance exists in the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to test, not null
	 *
	 * @return          <code>True</code> if the <code>DataStore</code>
	 *                  instance contains a reference to the
	 *                  <code>Element</code>, <code>False</code> otherwise
	 */

	@Override
	public final boolean contains (final T element)
	{
		this.log.trace ("contains: element={}", element);

		return (this.fetchQuery (element.getClass ())).contains (element);
	}

	/**
	 * Retrieve an <code>Element</code> instance from the
	 * <code>DataStore</code> based on its <code>DataStore</code> identifier.
	 *
	 * @param  id The <code>DataStore</code> identifier of the
	 *            <code>Element</code> to retrieve, not null
	 *
	 * @return    The requested <code>Element</code>
	 */

	@Override
	public T fetchById (final Long id)
	{
		this.log.trace ("fetchById: id={}", id);

		return (this.fetchQuery ()).query (id);
	}

	/**
	 * Retrieve a <code>List</code> of all of the <code>Element</code>
	 * instances from the <code>DataStore</code>.
	 *
	 * @return A <code>List</code> of <code>Element</code> instances
	 */

	@Override
	public List<T> fetchAll ()
	{
		this.log.trace ("fetchAll:");

		return (this.fetchQuery ()).queryAll ();
	}

	/**
	 * Insert an <code>Element</code> into the <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to insert into the
	 *                 <code>DataStore</code>, not null
	 *
	 * @return         A reference to the <code>Element</code>
	 */

	@Override
	public final T insert (final T element)
	{
		this.log.trace ("insert: element={}", element);

		if (element == null)
		{
			this.log.error ("Attempting to insert a NULL element into the datastore");
			throw new NullPointerException ();
		}

		if (! ((this.datastore.getProfile ()).isMutable ()))
		{
			this.log.error ("Attempting to insert an element into an immutable datastore");
			throw new IllegalStateException ("DataStore is immutable");
		}

		return null;
	}

	/**
	 * Remove an <code>Element</code> from the <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to remove from the
	 *                 <code>DataStore</code>, not null
	 */

	@Override
	public final void remove (final T element)
	{
		this.log.trace ("remove: element={}", element);
	}
}
