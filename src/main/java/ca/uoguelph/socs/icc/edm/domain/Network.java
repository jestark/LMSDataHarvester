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

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.inject.Named;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation the network from which logged <code>Action</code>
 * originated.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class Network extends Element
{
	/**
	 * Create new <code>Network</code> instances.  This class extends
	 * <code>AbstractBuilder</code>, adding the functionality required to
	 * create <code>Network</code> instances.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 * @see     Network
	 */

	public static class Builder extends Element.Builder<Network>
	{
		/** Method reference to the implementation constructor  */
		private final Function<Network.Builder, Network> creator;

		/** The <code>DataStore</code> id number for the <code>Network</code> */
		private @Nullable Long id;

		/** The name of the <code>Network</code> */
		private @Nullable String name;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model       The <code>DomainModel</code>, not null
		 * @param  idGenerator The <code>IdGenerator</code>, not null
		 * @param  retriever   The <code>Retriever</code>, not null
		 * @param  creator     Method Reference to the constructor, not null
		 */

		protected Builder (
				final DomainModel model,
				final IdGenerator idGenerator,
				final Retriever<Network> retriever,
				final Function<Network.Builder, Network> creator)
		{
			super (model, idGenerator, retriever);

			assert creator != null : "creator is NULL";
			this.creator = creator;

			this.id = null;
			this.name = null;
		}

		/**
		 * Create an instance of the <code>Network</code>.
		 *
		 * @param  network The previously existing <code>Network</code>
		 *                 instance, may be null
		 * @return         The new <code>Network</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected Network create (final @Nullable Network network)
		{
			this.log.trace ("create: network={}", network);

			return this.creator.apply (this);
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
		 * Load a <code>Network</code> instance into the builder.  This method
		 * resets the builder and initializes all of its parameters from
		 * the specified <code>Network</code> instance.  The  parameters are
		 * validated as they are set.
		 *
		 * @param  network The <code>Network</code>, not null
		 * @return         This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>Network</code> instance to be
		 *                                  loaded are not valid
		 */

		@Override
		public Builder load (final Network network)
		{
			this.log.trace ("load: network={}", network);

			super.load (network);

			this.id = network.getId ();
			this.setName (network.getName ());

			return this;
		}

		/**
		 * Get the <code>DataStore</code> identifier for the
		 * <code>Network</code> instance.
		 *
		 * @return The <code>DataStore</code> identifier
		 */

		@CheckReturnValue
		public final Long getId ()
		{
			return this.id;
		}

		/**
		 * Get the name of the <code>Network</code>.
		 *
		 * @return A <code>String</code> containing the name of the
		 *         <code>Network</code>
		 */

		@CheckReturnValue
		public final String getName ()
		{
			return this.name;
		}

		/**
		 * Set the name of the <code>Network</code>.
		 *
		 * @param  name The name of the <code>Network</code>, not null
		 * @return      This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If the name is empty
		 */

		public final Builder setName (final String name)
		{
			this.log.trace ("setName: name={}", name);

			Preconditions.checkNotNull (name);
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

	@BuilderScope
	@Component (dependencies = {IdGenerator.IdGeneratorComponent.class}, modules = {NetworkBuilderModule.class})
	protected interface BuilderComponent extends Element.BuilderComponent<Network>
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
	 * Dagger module for creating <code>Retriever</code> instances.  This module
	 * contains implementation-independent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module
	public static final class NetworkModule extends Element.ElementModule<Network>
	{
		/**
		 * Get the <code>Selector</code> used by the
		 * <code>QueryRetriever</code>.
		 *
		 * @return The <code>Selector</code>
		 */

		@Provides
		public Selector<Network> getSelector ()
		{
			return Network.SELECTOR_NAME;
		}
	}

	/**
	 * Dagger module for creating <code>Builder</code> instances.  This module
	 * contains implementation-dependent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module (includes = {NetworkModule.class})
	public static final class NetworkBuilderModule
	{
		/** Method reference to the implementation constructor  */
		private final Function<Network.Builder, Network> creator;

		/**
		 * Create the <code>NetworkBuilderModule</code>
		 *
		 * @param  creator Method reference to the Constructor, not null
		 */

		public NetworkBuilderModule (final Function<Network.Builder, Network> creator)
		{
			this.creator = creator;
		}

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model     The <code>DomainModel</code>, not null
		 * @param  generator The <code>IdGenerator</code>, not null
		 * @param  retriever The <code>Retriever</code>, not null
		 */

		@Provides
		public Builder createBuilder (
				final DomainModel model,
				final IdGenerator generator,
				final @Named ("QueryRetriever") Retriever<Network> retriever)
		{
			return new Builder (model, generator, retriever, this.creator);
		}
	}

	/**
	 * Abstract representation of an <code>Element</code> implementation class.
	 * Instances of this class are used to load the <code>Element</code>
	 * implementations into the JVM via the <code>ServiceLoader</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	protected abstract class Definition extends Element.Definition<Network>
	{
		/** The module for creating <code>Builder</code> instances */
		private final NetworkBuilderModule module;

		/**
		 * Create the <code>Definition</code>.
		 *
		 * @param  impl    The implementation class, not null
		 * @param  creator Method reference to the constructor, not null
		 */

		public Definition (
				final Class<? extends Network> impl,
				final Function<Network.Builder, Network> creator)
		{
			super (impl);

			assert creator != null : "creator is NULL";
			this.module = new NetworkBuilderModule (creator);
		}

		/**
		 * Create a new instance of the <code>BuilderComponent</code> on the
		 * specified <code>DomainModel</code>.
		 *
		 * @param model The <code>DomainModel</code>, not null
		 * @return      The <code>BuilderComponent</code>
		 */

		@Override
		protected Network.BuilderComponent getBuilderComponent (final DomainModel model)
		{
			return DaggerNetwork_BuilderComponent.builder ()
				.idGeneratorComponent (model.getIdGeneratorComponent (this.impl))
				.domainModelModule (new DomainModel.DomainModelModule (Network.class, model))
				.networkBuilderModule (this.module)
				.build ();
		}

		/**
		 * Get a <code>Stream</code> of the <code>Property</code> instances for
		 * the <code>Element</code> class represented by this
		 * <code>Definition</code>.
		 *
		 * @return A <code>Stream</code> of <code>Property</code> instances
		 */

		@Override
		public Stream<Property<Network, ?>> properties ()
		{
			return Network.METADATA.properties ();
		}

		/**
		 * Get a <code>Stream</code> of the <code>Selector</code> instances for
		 * the <code>Element</code> class represented by this
		 * <code>Definition</code>.
		 *
		 * @return A <code>Stream</code> of <code>Selector</code> instances
		 */

		@Override
		public Stream<Selector<Network>> selectors ()
		{
			return Network.METADATA.selectors ();
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>Network</code> */
	protected static final MetaData<Network> METADATA;

	/** The <code>DataStore</code> identifier of the <code>Network</code> */
	public static final Property<Network, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>Network</code> */
	public static final Property<Network, DomainModel> MODEL;

	/** The name of the <code>Network</code> */
	public static final Property<Network, String> NAME;

	/** Select the <code>Network</code> instance by its id */
	public static final Selector<Network> SELECTOR_ID;

	/** Select all of the <code>Network</code> instances */
	public static final Selector<Network> SELECTOR_ALL;

	/** Select an <code>Network</code> instance by its name */
	public static final Selector<Network> SELECTOR_NAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Network</code>.
	 */

	static
	{
		ID = Property.of (Network.class, Long.class, "id",
				Network::getId, Network::setId);

		MODEL = Property.of (Network.class, DomainModel.class, "domainmodel",
				Network::getDomainModel, Network::setDomainModel);

		NAME = Property.of (Network.class, String.class, "name",
				Network::getName, Network::setName,
				Property.Flags.REQUIRED);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);
		SELECTOR_NAME = Selector.of (Selector.Cardinality.SINGLE, NAME);

		SELECTOR_ALL = Selector.builder (Network.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		METADATA = MetaData.builder (Network.class)
			.addProperty (ID)
			.addProperty (MODEL)
			.addProperty (NAME)
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
	 *                               the <code>Network</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static Builder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return (Network.Builder) model.getBuilderComponent (Network.class)
			.getBuilder ();
	}

	/**
	 * Create the <code>Network</code>.
	 */

	protected Network ()
	{
		super ();
	}

	/**
	 * Create the <code>Network</code> instance from the specified
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected Network (final Builder builder)
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
	 * Connect all of the relationships for this <code>Network</code> instance.
	 * This method is intended to be used just after the <code>Network</code> is
	 * inserted into the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         connected, <code>false</code> otherwise
	 */

	@Override
	protected boolean connect ()
	{
		return Network.METADATA.relationships ()
				.allMatch (r -> r.connect (this));
	}

	/**
	 * Disconnect all of the relationships for this <code>Network</code>
	 * instance.  This method is intended to be used just before the
	 * <code>Network</code> is removed from the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         disconnected, <code>false</code> otherwise
	 */

	@Override
	protected boolean disconnect ()
	{
		return Network.METADATA.relationships ()
				.allMatch (r -> r.disconnect (this));
	}

	/**
	 * Compare two <code>Network</code> instances to determine if they are
	 * equal.  The <code>Network</code> instances are compared based upon their
	 * names.
	 *
	 * @param  obj The <code>Network</code> instance to compare to the one
	 *             represented by the called instance
	 * @return     <code>True</code> if the two <code>Network</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof Network)
			&& Objects.equals (this.getName (), ((Network) obj).getName ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Network</code> instance.
	 * The hash code is computed based upon the name of the instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getName ());
	}

	/**
	 * Get a <code>String</code> representation of the <code>Network</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Network</code>
	 *         instance
	 */

	@Override
	@CheckReturnValue
	public String toString()
	{
		return this.toStringHelper ()
			.toString ();
	}

	/**
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates a <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>Network</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		return Network.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
	}

	/**
	 * Get the name of the <code>Network</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Role</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>Network</code>.  This method is intended to be
	 * used to initialize a new <code>Network</code> instance.
	 *
	 * @param name The name of the <code>Network</code>
	 */

	protected abstract void setName (String name);
}
