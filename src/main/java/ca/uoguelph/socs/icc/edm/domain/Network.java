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

import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation the network from which logged <code>Action</code>
 * originated.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     NetworkBuilder
 * @see     NetworkLoader
 */

public abstract class Network extends Element
{
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
	 * Get an instance of the <code>NetworkBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>NetworkBuilder</code> instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Network</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static NetworkBuilder builder (final DomainModel model)
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
	 * Get an <code>NetworkBuilder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an
	 * <code>NetworkBuilder</code> on the specified <code>DomainModel</code> and
	 * initializes it with the contents of this <code>Network</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>NetworkBuilder</code>
	 */

	@Override
	public NetworkBuilder getBuilder (final DomainModel model)
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
