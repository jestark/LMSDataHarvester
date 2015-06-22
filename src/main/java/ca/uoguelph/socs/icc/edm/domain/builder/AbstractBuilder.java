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

package ca.uoguelph.socs.icc.edm.domain.builder;

import java.util.Map;

import java.util.EnumMap;
import java.util.HashMap;

import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.element.metadata.MetaData;

/**
 * Abstract implementation of the <code>ElementBuilder</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class AbstractBuilder<T extends Element> implements ElementBuilder<T>
{
	/**
	 * Interface to hide the <code>Element</code> implementation class from the
	 * <code>ElementBuilder</code> implementation.
	 */

	private static interface BuilderData<T extends Element>
	{
		/**
		 * Get the <code>ElementBuilder</code> implementation class for the
		 * specified <code>Element</code>.
		 *
		 * @return The implementation <code>Class</code>
		 */

		public abstract Class<? extends ElementBuilder<T>> getBuilderClass ();

		/**
		 * Get an instance of the <code>Builder</code> for the
		 * <code>Element</code> implementation.
		 *
		 * @return  The <code>Builder</code> instance
		 */

		public abstract Builder<T> getBuilder ();
	}

	/**
	 * Wrapper class to hide the details of the <code>MetaData</code> from the
	 * <code>ElementBuilder</code> instance.
	 */

	private static final class BuilderDataImpl<T extends Element, U extends T> implements BuilderData<T>
	{
		/** The <code>MetaData</code> */
		private final MetaData<T, U> metadata;

		/**
		 * Create the <code>BuilderDataImpl</code>.
		 *
		 * @param  metadata The <code>MetaData</code> instance which is being
		 *                  wrapped
		 */

		public BuilderDataImpl (final MetaData<T, U> metadata)
		{
			assert metadata != null : "metadata is NULL";

			this.metadata = metadata;
		}

		/**
		 * Get the <code>ElementBuilder</code> implementation class for the
		 * specified <code>Element</code>.
		 *
		 * @return The implementation <code>Class</code>
		 */

		public Class<? extends ElementBuilder<T>> getBuilderClass ()
		{
			return this.metadata.getBuilderClass ();
		}

		/**
		 * Get an instance of the <code>Builder</code> for the
		 * <code>Element</code> implementation.
		 *
		 * @return  The <code>Builder</code> instance
		 */

		public Builder<T> getBuilder ()
		{
			return new BuilderImpl<T, U> (this.metadata.getDefinition ());
		}
	}

	/** Factory for the <code>ElementBuilder</code> implementations */
	private static final Map<Class<? extends ElementBuilder<? extends Element>>, BiFunction<Class<?>, DataStore, ? extends ElementBuilder<? extends Element>>> factories;

	/** <code>Element<code> meta-data */
	private static final Map<Class<?>, BuilderData<? extends Element>> metadata;

	/** The builder */
	protected final Builder<T> builder;

	/** The Logger */
	protected final Logger log;

	/** The <code>DataStore</code> */
	protected final DataStore datastore;

	/** The <code>Element</code> produced by the <code>ElementBuilder</code> */
	protected T element;

	/**
	 * static initializer to create the factory.
	 */

	static
	{
		factories = new HashMap<Class<? extends ElementBuilder<? extends Element>>, BiFunction<Class<?>, DataStore, ? extends ElementBuilder<? extends Element>>> ();
		metadata = new HashMap<Class<?>, BuilderData<? extends Element>> ();
	}

	/**
	 * Retrieve the <code>MetaData</code> for the specified
	 * <code>Element</code> implementation <code>Class</code>.  This is a
	 * convenience method that will retrieve the <code>MetaData</code> instance
	 * from the <code>Map</code> and cast is appropriately.
	 *
	 * @param  element The <code>Element</code> implementation class
	 *
	 * @return         The <code>MetaData</code> instance
	 */

	@SuppressWarnings("unchecked")
	private static <T extends Element> BuilderData<T> getElementMetaData (final Class<?> element)
	{
		assert element != null : "element is NULL";

		return (BuilderData<T>) AbstractBuilder.metadata.get (element);
	}

	/**
	 * Get the <code>MetaData</code> based <code>Builder</code> for the
	 * <code>Element</code> implementation <code>Class</code>.
	 *
	 * @param element The <code>Element</code> implementation class
	 *
	 * @return        The <code>Builder</code>
	 */

	private static <T extends Element> Builder<T> getBuilder (final Class<?> element)
	{
		assert element != null : "element is NULL";

		BuilderData<T> metadata = AbstractBuilder.getElementMetaData (element);

		assert metadata != null : "Element class is not registered: " + element.getSimpleName ();

		return metadata.getBuilder ();
	}

	/**
	 * Register the association between an <code>Element</code> implementation
	 * and the appropriate <code>ElementBuilder</code> implementation.  This
	 * method is intended to be used by the <code>Element</code>
	 * implementations to specify the appropriate <code>ElementBuilder</code>
	 * implementation to use for creating instances of that
	 * <code>Element</code>.
	 *
	 * @param  <T>      The interface type of the <code>Element</code>
	 * @param  metadata The implementation class, not null
	 */

	public static final <T extends Element, U extends T> void registerElement (final MetaData<T, U> metadata)
	{
		assert metadata != null : "metadata is NULL";
		assert (! AbstractBuilder.metadata.containsKey (metadata.getElementClass ())) : "Class already registered: " + (metadata.getElementClass ()).getSimpleName ();

		AbstractBuilder.metadata.put (metadata.getElementClass (), new BuilderDataImpl<T, U> (metadata));
	}

	/**
	 * Register an <code>ElementBuilder</code> implementation with the factory.
	 * This method is intended to be used by the <code>ElementBuilder</code>
	 * implementations to register themselves with the factory so that they may
	 * be instantiated on demand.
	 *
	 * @param  <T>     The <code>ElementBuilder</code> type to be registered
	 * @param  <U>     The <code>Element</code> type produced by the builder
	 * @param  builder The <code>ElementBuilder</code> implementation class,
	 *                 not null
	 * @param  factory The factory to create instances of the implementation
	 *                 class, not null
	 */

	protected static final <T extends ElementBuilder<U>, U extends Element> void registerBuilder (final Class<? extends T> builder, final BiFunction<Class<?>, DataStore, T> factory)
	{
		assert builder != null : "builder is NULL";
		assert factory != null : "factory is NULL";
		assert (! AbstractBuilder.factories.containsKey (builder)) : "Class already registered: " + builder.getSimpleName ();

		AbstractBuilder.factories.put (builder, factory);
	}

	/**
	 * Get an instance of the <code>ElementBuilder</code> which corresponds to
	 * the specified <code>Element</code> implementation class.
	 *
	 * @param  <T>       The <code>ElementBuilder</code> type to be returned
	 * @param  <U>       The <code>Element</code> type produced by the builder
	 * @param  element   The <code>Element</code> implementation class, not
	 *                   null
	 * @param  datastore The <code>DataStore</code> instance, not null
	 */

	@SuppressWarnings("unchecked")
	public static final <T extends ElementBuilder<U>, U extends Element> T getInstance (final Class<? extends Element> element, final DataStore datastore)
	{
		assert element   != null : "element is NULL";
		assert datastore != null : "manager is NULL";

		BuilderData<U> metadata = AbstractBuilder.getElementMetaData (element);

		assert metadata != null : "Element class is not registered: " + element.getSimpleName ();

		BiFunction<Class<?>, DataStore, T> factory = (BiFunction<Class<?>, DataStore, T>) AbstractBuilder.factories.get (metadata.getBuilderClass ());

		assert factory != null : "Class not registered: " + (metadata.getBuilderClass ()).getSimpleName ();

		return factory.apply (element, datastore);
	}

	/**
	 * Create the <code>AbstractBuilder</code>.
	 *
	 * @param  impl      The <code>Element</code> implementation class produced
	 *                   by the <code>ElementBuilder</code>
	 * @param  datastore The <code>DataStore</code> into which new
	 *                   <code>Element</code> instances will be inserted
	 */

	protected AbstractBuilder (final Class<?> impl, final DataStore datastore)
	{
		assert impl != null : "impl is NULL";
		assert datastore != null : "datastore is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.datastore = datastore;

		this.builder = AbstractBuilder.getBuilder (impl);
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
