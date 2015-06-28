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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Transaction;

import ca.uoguelph.socs.icc.edm.domain.builder.AbstractBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.AbstractActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.AbstractSubActivityBuilder;

import ca.uoguelph.socs.icc.edm.domain.loader.AbstractLoader;

/**
 * Access and manipulate <code>Element</code> instances contained within the
 * encapsulated <code>DataStore</code>.  This class provides a High-level
 * interface to the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.1
 */

public final class DomainModel
{
	/** The log */
	private final Logger log;

	/** The data store which contains all of the data */
	private final DataStore datastore;

	/**
	 * Get the <code>Set</code> of <code>Element</code> interface classes which
	 * have implementations registered with the <code>DataStore</code>.
	 *
	 * @return A <code>Set</code> containing the <code>Element</code> interface
	 *         classes
	 */

	public static Set<Class<? extends Element>> getElements ()
	{
		return null;
	}

	/**
	 * Create the <code>DomainModel</code>.
	 *
	 * @param  datastore The <code>DataStore</code> which contains all of the
	 *                   data represented by this <code>DomainModel</code>,
	 *                   not null
	 */

	public DomainModel (final DataStore datastore)
	{
		this.log = LoggerFactory.getLogger (DomainModel.class);

		this.datastore = datastore;
	}

	/**
	 * Determine if the <code>DomainModel</code> is mutable.
	 *
	 * @return <code>True</code> if the <code>DomainModel</code> is mutable,
	 *         <code>False</code> otherwise
	 */

	public boolean isMutable ()
	{
		return this.datastore.isMutable ();
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
	 * its resources.  If the <code>DataStore</code> has an active
	 * <code>Transaction</code> then the transaction will be allowed to
	 * complete before the <code>DataStore</code> is closed.
	 */

	public void close ()
	{
		this.datastore.close ();
	}

	/**
	 * Get a reference to the <code>Transaction</code> instance for the
	 * <code>DataStore</code>.  A <code>Transaction</code> can only be returned
	 * for a mutable <code>DataStore</code>.
	 *
	 * @return                      A reference to the <code>Transaction</code>
	 * @throws IllegalStateExcption If the <code>DataStore</code> is immutable
	 */

	public Transaction getTransaction ()
	{
		if (! this.datastore.isMutable ())
		{
			this.log.error ("Attempting to get a Transaction for a immutable DataStore");
			throw new IllegalStateException ("DataStore is immutable");
		}

		return this.datastore.getTransaction ();
	}

	/**
	 * Get an <code>ElementLoader</code> instance corresponding to the
	 * specified <code>Element</code> interface.
	 *
	 * @param  <T>     The type of <code>ElementLoader</code> to return
	 * @param  <U>     The type of <code>Element</code> operated on by the
	 *                 <code>ElementLoader</code>
	 * @param  element The <code>Element</code> interface class, not null
	 *
	 * @return         The <code>ElementLoader</code> instance
	 */

	public <T extends ElementLoader<U>, U extends Element> T getLoader (final Class<U> element)
	{
		this.log.trace ("getLoader: element={}", element);

		if (element == null)
		{
			this.log.error ("Attempting to get a Loader for a NULL Element");
			throw new NullPointerException ();
		}

		return AbstractLoader.getInstance (element, this.datastore);
	}

	/**
	 * Get an <code>ElementBuilder</code> instance corresponding to the
	 * specified <code>Element</code> interface.  This method will return the
	 * correct <code>ElementBuilder</code> implementation for the specified
	 * <code>Element</code> interface class, for the <code>DataStore</code>.
	 *
	 * @param  <T>                      The type of <code>ElementBuilder</code>
	 *                                  to be returned
	 * @param  <U>                      The type of <code>Element</code> to be
	 *                                  built by the <code>ElementBuilder</code>
	 * @param  element                  The <code>Element</code> interface
	 *                                  class, not null
	 *
	 * @return                          The <code>ElementBuilder</code> instance
	 * @throws IllegalStateException    If the <code>DataStore</code> is
	 *                                  immutable
	 * @throws IllegalArgumentException If the provided <code>Element</code>
	 *                                  class is not an interface of is an
	 *                                  <code>Activity</code> interface
	 */

	public <T extends ElementBuilder<U>, U extends Element> T getBuilder (final Class<? extends U> element)
	{
		this.log.trace ("getBuilder: element={}", element);

		if (element == null)
		{
			this.log.error ("Attempting to get a builder for a NULL Element");
			throw new NullPointerException ();
		}

		if (! this.datastore.isMutable ())
		{
			this.log.error ("Attempting to get an ElementBuilder for an immutable DataStore");
			throw new IllegalStateException ("DataStore is immutable");
		}

		if (! element.isInterface ())
		{
			this.log.error ("Attempting to create an ElementBuilder using an implementation class");
			throw new IllegalArgumentException ("Element Interface class Required");
		}

		if (Activity.class.isAssignableFrom (element))
		{
			this.log.error ("Can't create builder for Activity");
			throw new IllegalArgumentException ("Can't create Activity builders");
		}

		return AbstractBuilder.getInstance (this.datastore.getElementClass (element), this.datastore);
	}

	/**
	 * Get an <code>ElementBuilder</code> instance corresponding to the
	 * specified <code>ActivityType</code>.  This method is a special case of
	 * the <code>getBuilder</code> method which will determine and return the
	 * correct <code>ActivityBuilder</code> based on the supplied
	 * <code>ActivityType</code> instance.  The supplied
	 * <code>ActivityType</code> instance does not need to exist in the
	 * <code>DataStore</code>.  However, there must be an identical
	 * <code>ActivityType</code> in the <code>DataStore</code>.
	 *
	 * @param  <T>     The type of <code>ActivityBuilder</code>
	 * @param  <U>     The type of <code>Activity</code> to be created by the
	 *                 <code>ActivityBuilder</code>
	 * @param  type    The <code>ActivityType</code> of the
	 *                 <code>Activity</code> to be created by the
	 *                 <code>ActivityBuilder</code>
	 *
	 * @return         An <code>ActivityBuilder</code> instance
	 */

	public <T extends ActivityBuilder<U>, U extends Activity> T getBuilder (final ActivityType type)
	{
		this.log.trace ("getBuilder: type={}", type);

		if (type == null)
		{
			this.log.error ("Attempting to get a Builder for a NULL ActivityType");
			throw new NullPointerException ();
		}

		if (! this.datastore.isMutable ())
		{
			this.log.error ("Attempting to get an ElementBuilder for an immutable DataStore");
			throw new IllegalStateException ("DataStore is immutable");
		}

		if (! this.datastore.contains (type))
		{
			this.log.error ("This specified ActivityType does not exist in the DataStore");
			throw new IllegalArgumentException ("ActivityType is not in the DataStore");
		}

		return AbstractActivityBuilder.getInstance (type, this.datastore);
	}

	/**
	 * Get an <code>ElementBuilder</code> instance corresponding to the
	 * specified <code>Activity</code>.  This method is a special case of the
	 * <code>getBuilder</code> method which will determine and return the
	 * correct <code>SubActivityBuilder</code> based on the supplied
	 * <code>Activity</code>.  The supplied <code>Activity</code> will not be
	 * entered into the builder.
	 *
	 * @param  <T>      The type of <code>SubActivityBuilder</code> to be
	 *                  returned
	 * @param  <U>      The type of <code>SubActivity</code> to be created by
	 *                  the <code>SubActivityBuilder</code>
	 * @param  activity The <code>Activity</code> instance to which the new
	 *                  <code>SubActivity</code> instance is to be assigned
	 *
	 * @return          A <code>SubActivityBuilder</code> instance
	 */

	public <T extends SubActivityBuilder<U>, U extends SubActivity> T getBuilder (final Activity activity)
	{
		this.log.trace ("getBuilder: activity={}", activity);

		if (activity == null)
		{
			this.log.error ("Attempting to get a Builder for a NULL Activity");
			throw new NullPointerException ();
		}

		if (! this.datastore.isMutable ())
		{
			this.log.error ("Attempting to get an ElementBuilder for an immutable DataStore");
			throw new IllegalStateException ("DataStore is immutable");
		}

		if (! this.datastore.contains (activity))
		{
			this.log.error ("This specified Activity does not exist in the DataStore");
			throw new IllegalArgumentException ("Activity is not in the DataStore");
		}

		return AbstractSubActivityBuilder.getInstance (activity, this.datastore);
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
	 *
	 * @return          <code>True</code> if the <code>DataStore</code>
	 *                  instance contains a reference to the
	 *                  <code>Element</code>, <code>False</code> otherwise
	 */

	public boolean contains (final Element element)
	{
		this.log.trace ("contains: element={}", element);

		if (element == null)
		{
			this.log.error ("Testing if the DataStore Contains a NULL Element");
			throw new NullPointerException ();
		}

		return this.datastore.contains (element);
	}

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.  This method will insert a copy of the specified
	 * <code>Element</code> into the <code>DataStore</code> and return a
	 * reference to the <code>Element</code> instance which was inserted.  If
	 * the specified <code>Element</code> instance already exists in the
	 * <code>DataStore</code> then the returned instance will be a reference to
	 * the specified <code>Element</code> instance.  To insert an
	 * <code>Element</code> into the <code>DataStore</code>, an active
	 * <code>Transaction</code> is required.
	 *
	 * @param  element               The <code>Element</code> to insert, not
	 *                               null
	 *
	 * @return                       A reference to the <code>Element</code> in
	 *                               the <code>DataStore</code>
	 * @throws IllegalStateException If there is not an active
	 *                               <code>Transaction</code>
	 */

	public <T extends Element> T insert (final T element)
	{
		this.log.trace ("insert: element={}", element);

		T result = element;

		if (element == null)
		{
			this.log.error ("Attempting to insert a NULL element");
			throw new NullPointerException ();
		}

		if (! this.datastore.contains (element))
		{
			if ((this.datastore.getTransaction ()).isActive ())
			{
				this.log.error ("Attempting to insert an Element without an Active Transaction");
				throw new IllegalStateException ("Active Transaction required");
			}
			else
			{
				ElementBuilder<T> builder = AbstractBuilder.getInstance (element.getClass (), this.datastore);

				builder.load (element);
				result = builder.build ();
			}
		}

		return result;
	}

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.  To remove the specified <code>Element</code>,
	 * the <code>Element</code> must exist in the <code>DataStore</code> and
	 * the <code>DataStore</code> must have an active <code>Transaction</code>.
	 *
	 * @param  element                  The <code>Element</code> to remove, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If the <code>Element</code> does not
	 *                                  exist in the <code>DataStore</code>
	 * @throws IllegalStateException    If there is not an active
	 *                                  <code>Transaction</code>
	 */

	public void remove (final Element element)
	{
		this.log.trace ("remove: element={}", element);

		if (element == null)
		{
			this.log.error ("Attempting to remove a NULL element");
			throw new NullPointerException ();
		}

		if ((this.datastore.getTransaction ()).isActive ())
		{
			this.log.error ("Attempting to insert an Element without an Active Transaction");
			throw new IllegalStateException ("Active Transaction required");
		}

		if (! this.contains (element))
		{
			this.log.error ("Attempting to remove an Element which does not exist in the DataStore");
			throw new IllegalArgumentException ("Element does not exist in the DataStore");
		}

		this.datastore.remove (element);
	}
}
