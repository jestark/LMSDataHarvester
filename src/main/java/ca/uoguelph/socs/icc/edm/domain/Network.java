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
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.Persister;
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

	public static final class Builder implements Element.Builder<Network>
	{
		/** The Logger */
		private final Logger log;

		/** Helper to operate on <code>Network</code> instances*/
		private final Persister<Network> persister;

		/** Method reference to the constructor of the implementation class */
		private final Supplier<Network> supplier;

		/** The loaded of previously created <code>Action</code> */
		private Network network;

		/** The <code>DataStore</code> id number for the <code>Network</code> */
		private Long id;

		/** The name of the <code>Network</code> */
		private String name;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  supplier  Method reference to the constructor of the
		 *                   implementation class, not null
		 * @param  persister The <code>Persister</code> used to store the
		 *                   <code>Network</code>, not null
		 */

		protected Builder (final Supplier<Network> supplier, final Persister<Network> persister)
		{
			assert supplier != null : "supplier is NULL";
			assert persister != null : "persister is NULL";

			this.log = LoggerFactory.getLogger (this.getClass ());

			this.persister = persister;
			this.supplier = supplier;

			this.network = null;
			this.id = null;
			this.name = null;
		}

		/**
		 * Create an instance of the <code>Network</code>.
		 *
		 * @return                       The new <code>Network</code> instance
		 * @throws IllegalStateException If any if the fields is missing
		 * @throws IllegalStateException If there isn't an active transaction
		 */

		@Override
		public Network build ()
		{
			this.log.trace ("build:");

			if (this.name == null)
			{
				this.log.error ("Attempting to create an Network without a name");
				throw new IllegalStateException ("name is NULL");
			}

			Network result = this.supplier.get ();
			result.setId (this.id);
			result.setName (this.name);

			this.network = this.persister.insert (this.network, result);

			return this.network;
		}

		/**
		 * Reset the builder.  This method will set all of the fields for the
		 * <code>Element</code> to be built to <code>null</code>.
		 *
		 * @return This <code>Builder</code>
		 */

		public Builder clear ()
		{
			this.log.trace ("clear:");

			this.network = null;
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
		 * @param  network                  The <code>Network</code>, not null
		 *
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>Network</code> instance to be
		 *                                  loaded are not valid
		 */

		public Builder load (final Network network)
		{
			this.log.trace ("load: network={}", network);

			if (network == null)
			{
				this.log.error ("Attempting to load a NULL Network");
				throw new NullPointerException ();
			}

			this.network = network;
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
		public Long getId ()
		{
			return this.id;
		}

		/**
		 * Get the name of the <code>Network</code>.
		 *
		 * @return A <code>String</code> containing the name of the
		 *         <code>Network</code>
		 */

		public String getName ()
		{
			return this.name;
		}

		/**
		 * Set the name of the <code>Network</code>.
		 *
		 * @param  name                     The name of the
		 *                                  <code>Network</code>, not null
		 *
		 * @throws IllegalArgumentException If the name is empty
		 */

		public Builder setName (final String name)
		{
			this.log.trace ("setName: name={}", name);

			if (name == null)
			{
				this.log.error ("name is NULL");
				throw new NullPointerException ("Name is NULL");
			}

			if (name.length () == 0)
			{
				this.log.error ("name is an empty string");
				throw new IllegalArgumentException ("name is empty");
			}

			this.name = name;

			return this;
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
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>Builder</code> instance
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

		return null;
	}

	/**
	 * Create the <code>Network</code>.
	 */

	protected Network ()
	{
		super ();
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
	 * Compare two <code>Network</code> instances to determine if they are
	 * equal.  The <code>Network</code> instances are compared based upon their
	 * names.
	 *
	 * @param  obj The <code>Network</code> instance to compare to the one
	 *             represented by the called instance
	 *
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
	 * Get the <code>Set</code> of <code>Property</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Property</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Property<? extends Element, ?>> properties ()
	{
		return Network.METADATA.properties ();
	}

	/**
	 * Get the <code>Set</code> of <code>Selector</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Selector</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Selector<? extends Element>> selectors ()
	{
		return Network.METADATA.selectors ();
	}

	/**
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates a <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>Network</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
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
