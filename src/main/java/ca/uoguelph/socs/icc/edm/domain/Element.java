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

import java.io.Serializable;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.datastore.Query;
import ca.uoguelph.socs.icc.edm.domain.datastore.QueryRetriever;
import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;
import ca.uoguelph.socs.icc.edm.domain.datastore.TableRetriever;
import ca.uoguelph.socs.icc.edm.domain.datastore.TranslationTable;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Root level interface for all of the elements of the domain model.  The
 * primary purpose of the <code>Element</code> interface is to allow instances
 * of the builders and loaders to use bounded generic types when referring to
 * the domain model interfaces and their implementations.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class Element implements Serializable
{
	/**
	 * Create and modify instances of the <code>Element</code> implementations.
	 * This class is the base class for all of the builders which produce
	 * <code>Element</code> instances, containing all of the common
	 * functionality.
	 * <p>
	 * All of the builders are written to their corresponding
	 * <code>Element</code> interface.  The builder itself does not know about
	 * the details of the <code>Element</code> implementation.  Internally the
	 * builders use a <code>MetaData</code> based builder which handles the
	 * implementation details, which allows one builder class to handle all of
	 * the implementations for a given <code>Element</code> interface.  When the
	 * builder is instantiated, the <code>Element</code> implementation class is
	 * determines though an examination of the profile data from the
	 * <code>DataStore</code>, and the internal builder will be created from the
	 * <code>MetaData</code> for the selected <code>Element</code>
	 * implementation class.
	 * <p>
	 * Each builder will validate all of the inputs as they supplied to ensure
	 * that required fields are not set to <code>null</code> and that the values
	 * are within valid ranges.  When the <code>build</code> method is called,
	 * the builder will ensure that all of the required fields are present, then
	 * it will create an instance of the <code>Element</code> and insert it into
	 * the <code>DataStore</code> before returning the <code>Element</code>
	 * instance to the caller.
	 * <p>
	 * Unless it is otherwise noted all of the fields of an <code>Element</code>
	 * are immutable after the <code>Element</code> instance is created.
	 * Existing <code>Element</code> instances can be loaded into the builder,
	 * to be used as a template for creating new <code>Element</code> instances.
	 * When the builder as asked to build the <code>Element</code> instance, it
	 * will compare data which it set in the builder to the data in the existing
	 * <code>Element</code> instance.  If the builder detects that the changed
	 * fields are a subset of the mutable fields, it will modify the existing
	 * <code>Element</code> instance, rather than creating a new instance.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 * @param   <T> The type of <code>Element</code> to be created
	 */

	public static abstract class Builder<T extends Element>
	{
		/** The Logger */
		protected final Logger log;

		/** The <code>DomainModel</code> which will contain the <code>Element</code> */
		protected final DomainModel model;

		/** The <code>Retriever</code> for the <code>Element</code> being created */
		protected final Retriever<T> retriever;

		/** The <code>IdGenerator</code>*/
		protected final @Nullable IdGenerator idGenerator;

		/** The previously created or loaded <code>Element</code> instance */
		private @Nullable T element;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model       The <code>DomainModel</code>, not null
		 * @param  idGenerator The <code>IdGenerator</code>, not null
		 * @param  retriever   The <code>Retriever</code>, not null
		 */

		protected Builder (
				final DomainModel model,
				final @Nullable IdGenerator idGenerator,
				final Retriever<T> retriever)
		{
			this.log = LoggerFactory.getLogger (this.getClass ());

			assert model != null : "model is NULL";
			assert retriever != null : "model is NULL";

			this.model = model;
			this.idGenerator = idGenerator;
			this.retriever = retriever;
			this.element = null;
		}

		/**
		 * Verify a relationship.  This utility method verifies that the
		 * supplied <code>Element</code> instance is not null, and that it
		 * exists in the <code>DataStore</code> that is associated with this
		 * <code>Builder</code>.  The supplied <code>Element</code>instance will
		 * substituted with the version in the <code>DataStore</code> if
		 * necessary, and the two instances will be verified to be identical
		 * using the <code>equalsAll</code> method.
		 *
		 * @param  <R>       The type of the related <code>Element</code>
		 * @param  retriever The <code>Retriever</code> for the relationship,
		 *                   not null
		 * @param  element   The <code>Element</code> to verify
		 * @param  msg       The message to place in the exception if the
		 *                   <code>Element</code> is null, not null
		 * @return           The <code>Element</code> instance from the
		 *                   <code>DataStore</code>
		 *
		 * @throws NullPointerException     if the supplied <code>Element</code>
		 *                                  instance is null
		 * @throws IllegalArgumentException if the supplied <code>Element</code>
		 *                                  does not exist in the
		 *                                  <code>DataStore</code>
		 * @throws IllegalArgumentException if the supplied <code>Element</code>
		 *                                  is not identical to the instance in
		 *                                  the <code>DataStore</code>
		 */

		protected final <R extends Element> R verifyRelationship (
				final Retriever<R> retriever,
				final @Nullable R element,
				final String msg)
		{
			assert retriever != null : "retriever is NULL";
			assert msg != null : "msg is NULL";

			Preconditions.checkNotNull (element, msg);

			R result = retriever.fetch (element);

			Preconditions.checkArgument (result != null,
					"Element is not in the DataStore: %s", element.toString ());
			Preconditions.checkArgument (result.equalsAll (element),
					"Element is not identical to the instance in the DataStore");

			return result;
		}

		/**
		 * Create a new <code>Element</code> instance.  Implementations of this
		 * method are responsible for creating a new instance of the
		 * implementation class and populating it with data from the
		 * <code>Builder</code>.  For an <code>Element</code> implementation
		 * where only mutable fields have been changed this method should apply
		 * the modifications to the supplied <code>Element</code> instance and
		 * return it.
		 *
		 * @param  element The pre-existing <code>Element</code> instance, may
		 *                 be null
		 * @return         The new <code>Element</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		protected abstract T create (@Nullable T element);

		/**
		 * Reset the builder.  This method will set all of the fields for the
		 * <code>Element</code> to be built to <code>null</code>.
		 *
		 * @return This <code>Builder</code>
		 */

		public Builder<T> clear ()
		{
			this.element = null;

			return this;
		}

		/**
		 * Load an <code>Element</code> instance into the builder.  This method
		 * resets the builder and initializes all of its parameters from
		 * the specified <code>Element</code> instance.  The  parameters are
		 * validated as they are set.
		 *
		 * @param  element The <code>Element</code>, not null
		 * @return         This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>Element</code> instance to be
		 *                                  loaded are not valid
		 */

		public Builder<T> load (final T element)
		{
			this.element = Preconditions.checkNotNull (element, "element");

			return this;
		}

		/**
		 * Create an instance of the <code>Element</code>.  This method is
		 * responsible for creating new <code>Element</code> instances, or
		 * modifying existing <code>Element</code> instances.
		 * <p>
		 * An existing <code>Element</code> instance will be modified if the
		 * following conditions are satisfied:
		 * <ol>
		 * <li> The <code>Element</code> instance has been loaded into a
		 *      <code>Builder</code> which is associated with the
		 *      <code>DomainModel</code> which contains the <code>Element</code>
		 *      instance.
		 * <li> All of the non-mutable fields are unchanged.  For fields which
		 *      describe relationships with other <code>Element</code> instances
		 *      the references must be identical.
		 * </ol>
		 * If both of these conditions are met, the existing
		 * <code>Element</code> instance will be modified and a reference to
		 * that instance will be returned, otherwise a new <code>Element</code>
		 * instance will be created.
		 * <p>
		 * When a new <code>Element</code> instance is created, this method
		 * ensures that it is not creating a duplicate of an
		 * <code>Element</code> which already exists in the
		 * <code>DataStore</code>.  For <code>Element</code> instances, which
		 * are unique, a <code>Query</code> using a <code>Selector</code> for a
		 * unique <code>Property</code> is used to find any pre-existing
		 * instances.  If the <code>Element</code> is not unique, then if there
		 * is a previously loaded <code>Element</code> instance which is equal
		 * to the new instance, then the previously loaded <code>Element</code>
		 * instance is used to inspect the <code>TranslationTable</code> for an
		 * associated <code>Element</code> instance in the
		 * <code>DataStore</code>.  Since it is not possible to directly query
		 * the <code>DataStore</code>, it is assumed that creating a duplicate
		 * instance is intended if the inspection of the
		 * <code>TranslationTable</code> does not yield an pre-existing
		 * instance.  This method will throw and
		 * <code>IllegalStateException</code> if the <code>Element</code>
		 * instance retrieved from the <code>DataStore</code> is not identical
		 * to the new <code>Element</code> instance (tested using the
		 * <code>equalsAll</code> method).
		 * <p>
		 * Once the new <code>Element</code> instance has been created and
		 * determined to not already exist in the <code>DataStore</code>, it is
		 * inserted into the <code>DataStore</code> before being returned to the
		 * caller.
		 *
		 * @return The new <code>Element</code> instance
		 *
		 * @throws IllegalStateException If any if the fields are missing
		 * @throws IllegalStateException If there isn't an active transaction
		 * @throws IllegalStateException If the <code>Element</code> already
		 *                               exists in the <code>DataStore</code>
		 *                               but is not identical to the new
		 *                               instance
		 */

		public final T build ()
		{
			this.log.trace ("build:");

			try
			{
				T newElement = this.create (this.element);

				if (this.element != newElement)
				{
					T result = this.retriever.fetch ((newElement.equals (this.element)) ? this.element : newElement);

					if (result == null)
					{
						if (this.idGenerator != null)
						{
							this.log.debug ("Setting ID");
							newElement.setId (this.idGenerator.nextId ());
						}

						this.log.debug ("Inserting the Element into the datastore");
						this.element = this.model.insert (this.element, newElement);
					}
					else if (! result.equalsAll (newElement))
					{
						this.log.debug ("Element exists in the DataStore, but it not identical");
						throw new IllegalStateException ("Element exists in the DataStore, but it not identical");
					}
					else
					{
						this.log.debug ("Using pre-existing element instance from the translation table");
						this.element = result;
					}
				}

				return this.element;
			}
			catch (NullPointerException ex)
			{
				this.log.error ("Element is missing required fields", ex);
				throw new IllegalStateException ("Element is missing required fields", ex);
			}
		}

		/**
		 * Get a reference to the <code>DomainModel</code> which contains the
		 * <code>Element</code>.
		 *
		 * @return A reference to the <code>DomainModel</code>
		 */

		public final DomainModel getDomainModel ()
		{
			return this.model;
		}
	}

	/**
	 * Template interface for the Dagger Component used to create the
	 * <code>Builder</code> instance.  Implementations of this interface are
	 * responsible for creating the <code>Builder</code> instance.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 * @param   <B> The type of <code>Builder</code> produced by the component
	 * @param   <T> The type of <code>Element</code> produced by the
	 *              <code>Builder</code>
	 */

	protected interface BuilderComponent<T extends Element>
	{
		/**
		 * Create the Builder instance.
		 *
		 * @return The <code>Builder</code>
		 */

		public abstract T.Builder<T> getBuilder ();
	}

	@Module (includes = {DomainModel.DomainModelModule.class})
	protected static abstract class ElementModule<T extends Element>
	{
		/**
		 * Create a new <code>QueryRetriever</code> instance.
		 *
		 * @param  retriever The <code>QueryRetriever</code>, not null
		 * @return           The <code>QueryRetriever</code>
		 */

		@Provides
		@Named ("QueryRetriever")
		public final Retriever<T> getQueryRetriever (final QueryRetriever<T> retriever)
		{
			return retriever;
		}

		/**
		 * Create a new <code>TableRetriever</code> instance.
		 *
		 * @param  retriever The <code>TableRetriever</code>, not null
		 * @return           The <code>TableRetriever</code>
		 */

		@Provides
		@Named ("TableRetriever")
		public final Retriever<T> getTableRetriever (final TableRetriever<T> retriever)
		{
			return retriever;
		}

		@Provides
		public final Query<T> getRetrieverQuery (final DomainModel model, final Selector<T> selector)
		{
			return model.getQuery (selector);
		}
	}

	/**
	 * Abstract representation of an <code>Element</code> implementation class.
	 * Instances of this class are used to load the <code>Element</code>
	 * implementations into the JVM via the <code>ServiceLoader</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 * @param   <T> The type of <code>Element</code> produced by the
	 *              <code>Builder</code>
	 */

	protected abstract class Definition<T extends Element>
	{
		/** The <code>Element</code> implementation class */
		protected final Class<? extends T> impl;

		/**
		 * Create the <code>Definition</code>.
		 *
		 * @param  impl The <code>Element</code> implementation class, not null
		 */

		public Definition (final Class<? extends T> impl)
		{
			assert impl != null : "impl is NULL";
			this.impl = impl;
		}

		/**
		 * Create a new instance of the <code>BuilderComponent</code> on the
		 * specified <code>DomainModel</code>.
		 *
		 * @param model The <code>DomainModel</code>, not null
		 * @return      The <code>Builder</code>
		 */

		protected abstract BuilderComponent<T> getBuilderComponent (final DomainModel model);

		/**
		 * Get a <code>Stream</code> of the <code>Property</code> instances for
		 * the <code>Element</code> class represented by this
		 * <code>Definition</code>.
		 *
		 * @return A <code>Stream</code> of <code>Property</code> instances
		 */

		public abstract Stream<Property<T, ?>> properties ();

		/**
		 * Get a <code>Stream</code> of the <code>Selector</code> instances for
		 * the <code>Element</code> class represented by this
		 * <code>Definition</code>.
		 *
		 * @return A <code>Stream</code> of <code>Selector</code> instances
		 */

		public abstract Stream<Selector<T>> selectors ();

		/**
		 * Get the <code>Element</code> implementation class represented by this
		 * <code>Definition</code>.
		 *
		 * @return The <code>Element</code> implementation class
		 */

		public final Class<? extends T> getElementClass ()
		{
			return this.impl;
		}

		/**
		 * Get a <code>Query</code> for the specified <code>Selector</code> on
		 * the specified <code>DomainModel</code> using the <code>Element</code>
		 * implementation represented by this <code>Definition</code>.
		 *
		 * @return The <code>Query</code>
		 */

		public final Query<T> getQuery (final DomainModel model, final Selector<T> selector)
		{
			return model.getQuery (selector, this.impl);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>DomainModel</code> */
	private DomainModel model;

	/**
	 * Create the <code>Element</code> instance.
	 */

	protected Element ()
	{
		this.model = null;
	}

	/**
	 * Create the <code>Element</code> instance from the specified
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected Element (final Builder<? extends Element> builder)
	{
		assert builder != null : "builder is NULL";

		this.model = Preconditions.checkNotNull (builder.getDomainModel ());
	}

	/**
	 * Template method to create and initialize a <code>ToStringHelper</code>.
	 *
	 * @return The <code>ToStringHelper</code>
	 */

	@CheckReturnValue
	protected MoreObjects.ToStringHelper toStringHelper ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("id", this.getId ());
	}

	/**
	 * Propagate the <code>DomainModel</code> reference to the specified
	 * <code>Element</code> instance.  This is an internal method to copy the
	 * <code>DomainModel</code> reference to the target <code>Element</code>
	 * instance.  It it used to make sure that an <code>Element</code> has a
	 * reference to the <code>DomainModel</code> before it is returned by the
	 * getter method on the containing <code>Element</code> instance.
	 *
	 * @param  element The <code>Element</code>, not null
	 *
	 * @return         The supplied <code>Element</code>
	 */

	protected final <T extends Element> T propagateDomainModel (final @Nullable T element)
	{
		if ((element != null) && (this.model != null))
		{
			((Element) element).model = this.model;
		}

		return element;
	}

	/**
	 * Connect all of the relationships for this <code>Element</code> instance.
	 * This method is intended to be used just after the <code>Element</code> is
	 * inserted into the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         connected, <code>false</code> otherwise
	 */

	protected abstract boolean connect ();

	/**
	 * Disconnect all of the relationships for this <code>Element</code>
	 * instance.  This method is intended to be used just before the
	 * <code>Element</code> is removed from the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         disconnected, <code>false</code> otherwise
	 */

	protected abstract boolean disconnect ();

	/**
	 * Compare two <code>Element</code> instances to determine if they are
	 * equal using all of the instance fields.  In most cases this will be the
	 * same as the <code>equals</code> method.
	 *
	 * @param  element The <code>Element</code> instance to compare to this
	 *                 instance
	 * @return         <code>True</code> if the two <code>Enrolment</code>
	 *                 instances are equal, <code>False</code> otherwise
	 */

	public boolean equalsAll (final @Nullable Element element)
	{
		return this.equals (element);
	}

	/**
	 * Compare two <code>Element</code> instances to determine if they are
	 * equal using the minimum set fields required to identify the
	 * <code>Element</code> instance.  In almost all cases this methods will
	 * give the same result as calling <code>equals</code>.
	 *
	 * @param  element The <code>Element</code> instance to compare to this
	 *                 instance
	 * @return         <code>True</code> if the two <code>Enrolment</code>
	 *                 instances are equal, <code>False</code> otherwise
	 */

	public boolean equalsUnique (final @Nullable Element element)
	{
		return this.equals (element);
	}

	/**
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates a <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>Element</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>Builder</code>
	 */

	public abstract Builder<? extends Element> getBuilder (DomainModel model);

	/**
	 * Get a reference to the <code>DomainModel</code> which contains the
	 * <code>Element</code>.
	 *
	 * @return A reference to the <code>DomainModel</code>
	 */

	public final DomainModel getDomainModel ()
	{
		return this.model;
	}

	/**
	 * Set a reference to the <code>DomainModel</code> which contains the
	 * <code>Element</code>.  This method will set the internal reference to
	 * the <code>DomainModel</code> if the internal representation of the
	 * <code>DomainModel</code> is null.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 */

	protected final void setDomainModel (final DomainModel model)
	{
		assert this.model == null || model == this.model : "Can't change the DomainModel";

		this.model = model;
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Element</code>
	 * instance.  Some <code>Element</code> interfaces are dependent on other
	 * <code>Element</code> interfaces for their identification.  The dependent
	 * interface implementations should return the <code>DataStore</code>
	 * identifier from the interface on which they depend.
	 *
	 * @return A <code>Long</code> containing <code>DataStore</code> identifier
	 */

	@CheckReturnValue
	public abstract Long getId ();

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Element</code>
	 * instance is loaded, or by the <code>ElementBuilder</code> implementation
	 * to set the <code>DataStore</code> identifier, prior to storing a new
	 * <code>Element</code> instance.
	 * <p>
	 * <code>Element</code> implementations which are dependent on other
	 * <code>Element</code> interfaces for their <code>DataStore</code>
	 * identifier should throw an <code>UnsupportedOperationException</code>
	 * when this method is called.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	protected abstract void setId (@Nullable Long id);
}
