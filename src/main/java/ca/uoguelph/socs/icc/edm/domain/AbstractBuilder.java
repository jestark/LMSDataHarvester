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

import java.util.Map;
import java.util.HashMap;

import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Receiver;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Create and modify instances of the <code>Element</code> implementations.
 * This class is the base class for all of the builders which produce
 * <code>Element</code> instances, containing all of the common functionality.
 * <p>
 * All of the builders are written to their corresponding <code>Element</code>
 * interface.  The builder itself does not know about the details of the
 * <code>Element</code> implementation.  Internally the builders use a
 * <code>MetaData</code> based builder which handles the implementation
 * details, which allows one builder class to handle all of the
 * implementations for a given <code>Element</code> interface.  When the
 * builder is instantiated, the <code>Element</code> implementation class is
 * determines though an examination of the profile data from the
 * <code>DataStore</code>, and the internal builder will be created from the
 * <code>MetaData</code> for the selected <code>Element</code> implementation
 * class.
 * <p>
 * Each builder will validate all of the inputs as they supplied to ensure that
 * required fields are not set to <code>null</code> and that the values are
 * within valid ranges.  When the <code>build</code> method is called, the
 * builder will ensure that all of the required fields are present, then it
 * will create an instance of the <code>Element</code> and insert it into the
 * <code>DataStore</code> before returning the <code>Element</code> instance to
 * the caller.
 * <p>
 * Unless it is otherwise noted all of the fields of an <code>Element</code>
 * are immutable after the <code>Element</code> instance is created.  Existing
 * <code>Element</code> instances can be loaded into the builder, to be used as
 * a template for creating new <code>Element</code> instances.  When the
 * builder as asked to build the <code>Element</code> instance, it will compare
 * data which it set in the builder to the data in the existing
 * <code>Element</code> instance.  If the builder detects that the changed
 * fields are a subset of the mutable fields, it will modify the existing
 * <code>Element</code> instance, rather than creating a new instance.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class AbstractBuilder<T extends Element>
{
	/** The Logger */
	protected final Logger log;

	/** The builder */
	protected final Builder<T> builder;

	/** The <code>DataStore</code> */
	protected final DataStore datastore;

	/** The reference <code>Element</code> */
	protected T element;

	/**
	 * Create an instance of the builder for the specified <code>Element</code>
	 * on the supplied <code>DataStore</code>.
	 *
	 * @param  <T>                   The <code>Element</code> interface type
	 * @param  <U>                   The builder type
	 * @param  datastore             The <code>DataStore</code>, not null
	 * @param  element               The <code>Element</code> interface class,
	 *                               not null
	 * @param  create                Method reference to the constructor, not
	 *                               null
	 *
	 * @return                       The builder instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Element</code>
	 */

	protected static <T extends Element, U extends AbstractBuilder<T>> U getInstance (final DataStore datastore, final Class<T> element, BiFunction<DataStore, Class<? extends Element>, U> create)
	{
		assert datastore != null : "datastore is NULL";
		assert element != null : "element is NULL";

		// Exception here because this is the fist time that it is checked
		if (! datastore.isOpen ())
		{
			throw new IllegalStateException ("datastore is closed");
		}

		Class<? extends Element> impl = datastore.getProfile ()
			.getElementClass (element);

		// Exception here because this is the fist time that it is checked
		if (impl == null)
		{
			throw new IllegalStateException ("Element is not available for this datastore");
		}

		return create.apply (datastore, impl);
	}

	/**
	 * Utility method to get the <code>DataStore</code> out of the supplied
	 * <code>DomainModel</code>, which checking its mutability.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>DataStore</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	protected static DataStore getDataStore (final DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		if (! model.isMutable ())
		{
			throw new IllegalStateException ("model immutable");
		}

		return model.getDataStore ();
	}

	/**
	 * Create the <code>AbstractBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> implementation class, not
	 *                   null
	 */

	protected AbstractBuilder (final DataStore datastore, final Class<? extends Element> element)
	{
		assert datastore != null : "datastore is NULL";
		assert element != null : "element is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.datastore = datastore;
		this.builder = new Builder<T> (this.datastore.getMetaData (element));
	}

	/**
	 * Utility method to check if the specified <code>Element</code> exists in
	 * the <code>DataStore</code>.
	 *
	 * @param  <E>                   The type of the <code>Element</code>
	 * @param  element               The <code>Element</code> instance to test,
	 *                               not null
	 *
	 * @return                       <code>true</code> if the
	 *                               <code>Element</code> exists in the
	 *                               <code>DataStore</code>, <code>false</code>
	 *                               otherwise
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 */

	protected final <E extends Element> boolean checkContains (final E element)
	{
		assert element != null : "element is NULL";

		if (! this.datastore.isOpen ())
		{
			this.log.error ("datastore is closed");
			throw new IllegalStateException ("datastore is closed");
		}

		return this.datastore.contains (element);
	}

	/**
	 * Utility method to perform <code>Element</code> substitutions with a
	 * <code>Query</code>.
	 *
	 * @param  <E>      The type of the <code>Element</code> to process
	 * @param  element  The <code>Element</code> to substitute, not null
	 * @param  selector The <code>Selector</code> used to perform the
	 *                  substitution, must return unique results, not null
	 *
	 * @return          The matching <code>Element</code> instance from the
	 *                  <code>DataStore</code>, may be null
	 */

	protected final <E extends Element> E substitute (final E element, final Selector selector)
	{
		assert element != null : "element is NULL";
		assert selector != null : "selector is NULL";

		E result = element;

		if (! this.datastore.isOpen ())
		{
			this.log.error ("datastore is closed");
			throw new IllegalStateException ("datastore is closed");
		}

		if (! this.datastore.contains (element))
		{
			Query<E> query = this.datastore.getQuery (selector, element.getClass ());
			query.setAllProperties (element);

			result = query.query ();
		}

		return result;
	}

	/**
	 * Create an instance of the <code>Element</code>.
	 *
	 * @return                       The new <code>Element</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If there isn't an active transaction
	 */

	public final T build ()
	{
		this.log.trace ("build:");

		if (! this.datastore.getTransaction ().isActive ())
		{
			this.log.error ("Attempting to build an Element without an active transaction");
			throw new IllegalStateException ("no active transaction");
		}

		this.element = this.builder.build (this.element);
		this.datastore.insert (this.element);

		return this.element;
	}

	/**
	 * Reset the builder.  This method will set all of the fields for the
	 * <code>Element</code> to be built to <code>null</code>.
	 */

	public final void clear ()
	{
		this.log.trace ("clear:");

		this.builder.clear ();

		this.element = null;
	}

	/**
	 * Load a <code>Element</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>Element</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  element                  The <code>Element</code>, not null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Element</code> instance to be
	 *                                  loaded are not valid
	 */

	public void load (final T element)
	{
		this.log.trace ("load: element={}", element);

		if (this.datastore.contains (element))
		{
			this.element = element;
		}
	}
}
