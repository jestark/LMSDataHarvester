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

import ca.uoguelph.socs.icc.edm.domain.element.metadata.Container;
import ca.uoguelph.socs.icc.edm.domain.element.metadata.MetaData;

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
	/**
	 * Factory for creating the builders.
	 *
	 * @param  <T> The <code>Element</code> interface type of the builder
	 */

	private static final class Factory<T extends Element> implements Container.Receiver<T, Builder<T>>
	{
		@Override
		public <U extends T> Builder<T> apply (final MetaData<T, U> metadata)
		{
			assert metadata != null : "metadata is NULL";

			return new BuilderImpl<T, U> (metadata);
		}
	}

	/** The Logger */
	protected final Logger log;

	/** The builder */
	protected final Builder<T> builder;

	/** The <code>DataStore</code> */
	protected final DataStore datastore;

	/** The <code>Element</code> produced by the <code>ElementBuilder</code> */
	protected T element;

	/**
	 * Get an instance of the <code>Builder</code> which corresponds to the
	 * specified <code>Element</code> implementation class.
	 *
	 * @param  <T>       The <code>Element</code> type produced by the builder
	 * @param  element   The <code>Element</code> implementation class, not
	 *                   null
	 * @param  datastore The <code>DataStore</code> instance, not null
	 */

	protected static <T extends Element> Builder<T> getBuilder (final DataStore datastore, final Class<? extends Element> element)
	{
		assert datastore != null : "manager is NULL";
		assert element != null : "element is NULL";

		return datastore.getMetaDataContainer ()
			.inject (element, new Factory<T> ());
	}

	/**
	 * Create the <code>AbstractBuilder</code>.
	 *
	 * @param  impl      The <code>Element</code> implementation class produced
	 *                   by the <code>ElementBuilder</code>
	 * @param  datastore The <code>DataStore</code> into which new
	 *                   <code>Element</code> instances will be inserted
	 */

	protected AbstractBuilder (final DataStore datastore, final Builder<T> builder)
	{
		assert datastore != null : "datastore is NULL";
		assert builder != null : "builder is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.datastore = datastore;
		this.builder = builder;
	}

	/**
	 * Create an instance of the <code>Element</code>.
	 *
	 * @return                       The new <code>Element</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 */

	public final T build ()
	{
		this.log.trace ("build:");

		this.element = this.builder.build (this.element);
		this.datastore.insert (this.element);

		return this.element;
	}

	/**
	 * Reset the <code>ElementBuilder</code>.  This method will set all of the
	 * fields for the <code>Element</code> to be built to <code>null</code>.
	 */

	public final void clear ()
	{
		this.log.trace ("clear:");

		this.builder.clear ();

		this.element = null;
	}

	/**
	 * Load a <code>Element</code> instance into the
	 * <code>ElementBuilder</code>.  This method resets the
	 * <code>ElementBuilder</code> and initializes all of its parameters from
	 * the specified <code>Element</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  element                  The <code>Element</code> to load into
	 *                                  the <code>ElementBuilder</code>, not
	 *                                  null
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
