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
import java.util.Objects;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of the source of a <code>Activity</code> in the domain
 * model.  Instances of the <code>ActivitySource</code> interface represent the
 * system from which the data for an <code>Activity</code> with a particular
 * <code>ActivityType</code> was originally harvested.  For example, an
 * <code>ActivitySource</code> could be "moodle" for data collected from the
 * Moodle Learning Management System, or "manual" for data that was collected
 * by the instructor manually.
 * <p>
 * Within the domain model the <code>ActivitySource</code> interface is a root
 * level element, as such instances of the <code>ActivitySource</code>
 * interface are not dependant upon any other domain model element to exist.
 * An associated instance of the <code>ActivitySource</code> interface is
 * required for an instance of the <code>ActivityType</code> interface to
 * exist.  If a particular instance of the <code>ActivitySource</code>
 * interface is deleted, then all of the associated instances of the
 * <code>ActivityType</code> interface must be deleted as well.
 * <p>
 * Once created, <code>ActivitySource</code> instances are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class ActivitySource extends Element
{
	/**
	 * Create new <code>ActivitySource</code> instances.  This class extends
	 * <code>AbstractBuilder</code>, adding the functionality required
	 * to create <code>ActivitySource</code> instances.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static abstract class Builder extends Element.Builder<ActivitySource>
	{
		/** The <code>DataStore</code> id number for the <code>ActivitySource</code> */
		private @Nullable Long id;

		/** The name of the <code>ActivitySource</code> */
		private @Nullable String name;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model       The <code>DomainModel</code>, not null
		 * @param  idGenerator The <code>IdGenerator</code>, not null
		 * @param  retriever   The <code>Retriever</code>, not null
		 */

		protected Builder (
				final DomainModel model,
				final IdGenerator idGenerator,
				final Retriever<ActivitySource> retriever)
		{
			super (model, idGenerator, retriever);

			this.id = null;
			this.name = null;
		}

		/**
		 * Reset the builder.  This method will set all of the fields for the
		 * <code>Element</code> to be built to <code>null</code>.
		 *
		 * @return This <code>Builder</code>
		 */

		@Override
		public Builder clear ()
		{
			this.log.trace ("clear:");

			super.clear ();

			this.id = null;
			this.name = null;

			return this;
		}

		/**
		 * Load a <code>ActivitySource</code> instance into the builder.  This
		 * method resets the builder and initializes all of its parameters from
		 * the specified <code>ActivitySource</code> instance.  The  parameters
		 * are validated as they are set.
		 *
		 * @param  source The <code>ActivitySource</code>, not null
		 * @return        This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>ActivitySource</code> instance
		 *                                  to be loaded are not valid
		 */

		@Override
		public Builder load (final ActivitySource source)
		{
			this.log.trace ("load: source={}", source);

			super.load (source);

			this.id = source.getId ();
			this.setName (source.getName ());

			return this;
		}

		/**
		 * Get the <code>DataStore</code> identifier for the
		 * <code>ActivitySource</code> instance.
		 *
		 * @return The <code>DataStore</code> identifier
		 */

		@CheckReturnValue
		public final Long getId ()
		{
			return this.id;
		}

		/**
		 * Get the name of the <code>ActivitySource</code>.
		 *
		 * @return A <code>String</code> containing the name of the
		 *         <code>ActivitySource</code>
		 */

		@CheckReturnValue
		public final String getName ()
		{
			return this.name;
		}

		/**
		 * Set the name of the <code>ActivitySource</code>.
		 *
		 * @param  name The name of the <code>ActivitySource</code>, not null
		 * @return      This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If the name is empty
		 */

		public final Builder setName (final String name)
		{
			this.log.trace ("setName: name={}", name);

			Preconditions.checkNotNull (name, "name");
			Preconditions.checkArgument (name.length () > 0, "name is empty");

			this.name = name;

			return this;
		}
	}

	/**
	 * Dagger Component interface for creating <code>Builder</code> instances.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	protected interface BuilderComponent extends Element.BuilderComponent<ActivitySource, ActivitySource.Builder>
	{
		/**
		 * Create the Builder instance.
		 *
		 * @return The <code>Builder</code>
		 */

		@Override
		public abstract Builder getBuilder ();
	}

	/**
	 * Abstract representation of an <code>Element</code> implementation class.
	 * Instances of this class are used to load the <code>Element</code>
	 * implementations into the JVM via the <code>ServiceLoader</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	protected abstract class Definition extends Element.Definition<ActivitySource, Builder>
	{
		/**
		 * Create the <code>Definition</code>.
		 *
		 * @param  impl The <code>Element</code> implementation class, not null
		 */

		public Definition (final Class<? extends ActivitySource> impl)
		{
			super (impl);
		}

		/**
		 * Get a <code>Stream</code> of the <code>Property</code> instances for
		 * the <code>Element</code> class represented by this
		 * <code>Definition</code>.
		 *
		 * @return A <code>Stream</code> of <code>Property</code> instances
		 */

		@Override
		public Stream<Property<ActivitySource, ?>> properties ()
		{
			return ActivitySource.METADATA.properties ();
		}

		/**
		 * Get a <code>Stream</code> of the <code>Selector</code> instances for
		 * the <code>Element</code> class represented by this
		 * <code>Definition</code>.
		 *
		 * @return A <code>Stream</code> of <code>Selector</code> instances
		 */

		@Override
		public Stream<Selector<ActivitySource>> selectors ()
		{
			return ActivitySource.METADATA.selectors ();
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>ActivitySource</code> */
	protected static final MetaData<ActivitySource> METADATA;

	/** The <code>DataStore</code> identifier of the <code>ActivitySource</code> */
	public static final Property<ActivitySource, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>ActivitySource</code> */
	public static final Property<ActivitySource, DomainModel> MODEL;

	/** The name of the <code>ActivitySource</code> */
	public static final Property<ActivitySource, String> NAME;

	/** The <code>ActivityType</code> instances associated with the <code>ActivitySource</code> */
	public static final Property<ActivitySource, ActivityType> TYPES;

	/** Select the <code>ActivitySource</code> instance by its id */
	public static final Selector<ActivitySource> SELECTOR_ID;

	/** Select all of the <code>ActivitySource</code> instances */
	public static final Selector<ActivitySource> SELECTOR_ALL;

	/** Select an <code>ActivitySource</code> instance by its name */
	public static final Selector<ActivitySource> SELECTOR_NAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>ActivitySource</code>.
	 */

	static
	{
		ID = Property.of (ActivitySource.class, Long.class, "id",
				ActivitySource::getId, ActivitySource::setId);

		MODEL = Property.of (ActivitySource.class, DomainModel.class, "domainmodel",
				ActivitySource::getDomainModel, ActivitySource::setDomainModel);

		NAME = Property.of (ActivitySource.class, String.class, "name",
				ActivitySource::getName, ActivitySource::setName,
				Property.Flags.REQUIRED);

		TYPES = Property.of (ActivitySource.class, ActivityType.class, "types",
				ActivitySource::getTypes, ActivitySource::addType, ActivitySource::removeType);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);
		SELECTOR_NAME = Selector.of (Selector.Cardinality.SINGLE, NAME);

		SELECTOR_ALL = Selector.builder (ActivitySource.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		METADATA = MetaData.builder (ActivitySource.class)
			.addProperty (ID)
			.addProperty (MODEL)
			.addProperty (NAME)
			.addProperty (TYPES)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.addSelector (SELECTOR_NAME)
			.build ();
	}

	/**
	 * Get an instance of the <code>Builder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The <code>Builder</code> instance
	 *
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>ActivitySource</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static Builder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return null;
	}

	/**
	 * Create the <code>ActivitySource</code>.
	 */

	protected ActivitySource ()
	{
		super ();
	}

	/**
	 * Create the <code>ActivitySource</code> instance from the specified
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected ActivitySource (final Builder builder)
	{
		super (builder);
	}

	/**
	 * Template method to create and initialize a <code>ToStringHelper</code>.
	 *
	 * @return The <code>ToStringHelper</code>
	 */

	@Override
	@CheckReturnValue
	protected MoreObjects.ToStringHelper toStringHelper ()
	{
		return super.toStringHelper ()
			.add ("name", this.getName ());
	}

	/**
	 * Connect all of the relationships for this <code>ActivitySource</code> instance.
	 * This method is intended to be used just after the <code>ActivitySource</code> is
	 * inserted into the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         connected, <code>false</code> otherwise
	 */

	@Override
	protected boolean connect ()
	{
		return ActivitySource.METADATA.relationships ()
				.allMatch (r -> r.connect (this));
	}

	/**
	 * Disconnect all of the relationships for this <code>ActivitySource</code>
	 * instance.  This method is intended to be used just before the
	 * <code>ActivitySource</code> is removed from the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         disconnected, <code>false</code> otherwise
	 */

	@Override
	protected boolean disconnect ()
	{
		return ActivitySource.METADATA.relationships ()
				.allMatch (r -> r.disconnect (this));
	}

	/**
	 * Compare two <code>ActivitySource</code> instances to determine if they
	 * are equal.  The <code>ActivitySource</code> instances are compared based
	 * upon their names.
	 *
	 * @param  obj The <code>ActivitySource</code> instance to compare to the
	 *             one represented by the called instance
	 * @return     <code>True</code> if the two <code>ActivitySource</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof ActivitySource)
			&& Objects.equals (this.getName (), ((ActivitySource) obj).getName ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>ActivitySource</code>
	 * instance.  The hash code is computed based upon the name of the
	 * instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hashCode (this.getName ());
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>ActivitySource</code> instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>ActivitySource</code> instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return this.toStringHelper ()
			.toString ();
	}

	/**
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates a<code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>ActivitySource</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		return ActivitySource.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
	}

	/**
	 * Get the name of the <code>ActivitySource</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivitySource</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>ActivitySource</code>.  This method is
	 * intended to be used to initialize a new <code>ActivitySource</code>
	 * instance.
	 *
	 * @param name The name of the <code>ActivitySource</code>
	 */

	protected abstract void setName (String name);

	/**
	 * Get the <code>Set</code> of <code>ActivityType</code> instances for the
	 * <code>ActivitySource</code>.  If there are no <code>ActivityType</code>
	 * instances associated with the <code>ActivitySource</code> then the
	 * <code>Set</code> will be empty.
	 *
	 * @return A <code>Set</code> of <code>ActivityType</code> instances
	 */

	public abstract Set<ActivityType> getTypes ();

	/**
	 * Initialize the <code>Set</code> of dependent <code>ActivityType</code>
	 * instances.  This method is intended to be used to initialize a new
	 * <code>ActivitySource</code> instance.
	 *
	 * @param  types The <code>Set</code> of <code>ActivityType</code>
	 *               instances to be associated with the
	 *               <code>ActivitySource</code>
	 */

	protected abstract void setTypes (Set<ActivityType> types);

	/**
	 * Add the specified <code>ActivityType</code> to the
	 * <code>ActivitySource</code>.
	 *
	 * @param  type   The <code>ActivityType</code> to add, not null
	 * @return        <code>True</code> if the <code>ActivityType</code> was
	 *                successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addType (ActivityType type);

	/**
	 * Remove the specified <code>ActivityType</code> from the
	 * <code>ActivitySource</code>.
	 *
	 * @param  type   The <code>ActivityType</code> to remove, not null
	 * @return        <code>True</code> if the <code>ActivityType</code> was
	 *                successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeType (ActivityType type);
}
