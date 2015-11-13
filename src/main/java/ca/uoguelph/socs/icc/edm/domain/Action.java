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
 * A representation of the <code>Action</code> that a <code>User</code>
 * performed upon an <code>Activity</code> as recored in a
 * <code>LogEntry</code>.  Some instances of the <code>Action</code> interface
 * are general (such as viewing the content associated with an instance of the
 * <code>Activity</code> interface) and will be associated with may instances
 * of the <code>ActivityType</code> interface.  Other instances of the
 * <code>Action</code> may be specific to one particular instance of a
 * <code>ActvityType</code>, such as submitting an assignment.
 * <p>
 * Within the domain model the <code>Action</code> interface is a root level
 * element, as such instances of the <code>Action</code> interface are not
 * dependant upon any other domain model element to exist.  An associated
 * instance of the <code>Action</code> interface is required for an instance of
 * the <code>LogEntry</code> interface to exist.  If a particular instance of
 * the <code>Action</code> interface is deleted, then all of the associated
 * instances of the <code>LogEntry</code> interface must be deleted as well.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActionBuilder
 */

public abstract class Action extends Element
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>Action</code>*/
	protected static final MetaData<Action> METADATA;

	/** The <code>DataStore</code> identifier of the <code>Action</code> */
	public static final Property<Action, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>Action</code> */
	public static final Property<Action, DomainModel> MODEL;

	/** The name of the <code>Action</code> */
	public static final Property<Action, String> NAME;

	/** Select the <code>Action</code> instance by its id */
	public static final Selector<Action> SELECTOR_ID;

	/** Select all of the <code>Action</code> instances */
	public static final Selector<Action> SELECTOR_ALL;

	/** Select an <code>Action</code> instance by its name */
	public static final Selector<Action> SELECTOR_NAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Action</code>.
	 */

	static
	{
		ID = Property.of (Action.class, Long.class, "id",
				Action::getId, Action::setId);

		MODEL = Property.of (Action.class, DomainModel.class, "domainmodel",
				Action::getDomainModel, Action::setDomainModel);

		NAME = Property.of (Action.class, String.class, "name",
				Action::getName, Action::setName, Property.Flags.REQUIRED);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);
		SELECTOR_NAME = Selector.of (Selector.Cardinality.SINGLE, NAME);

		SELECTOR_ALL = Selector.builder (Action.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		METADATA = MetaData.builder (Action.class)
			.addProperty (ID)
			.addProperty (MODEL)
			.addProperty (NAME)
			.addSelector (SELECTOR_NAME)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.build ();
	}

	/**
	 * Get an instance of the <code>ActionBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>ActionBuilder</code> instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Action</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static ActionBuilder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return null;
	}

	/**
	 * Create the <code>Action</code>.
	 */

	protected Action ()
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
	 * Compare two <code>Action</code> instances to determine if they are
	 * equal.  The <code>Action</code> instances are compared based upon their
	 * names.
	 *
	 * @param  obj The <code>Action</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Action</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof Action)
			&& Objects.equals (this.getName (), ((Action) obj).getName ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Action</code> instance.
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
	 * Get a <code>String</code> representation of the <code>Action</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Action</code>
	 *         instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
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
		return Action.METADATA.properties ();
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
		return Action.METADATA.selectors ();
	}

	/**
	 * Get an <code>ActionBuilder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an
	 * <code>ActionBuilder</code> on the specified <code>DomainModel</code> and
	 * initializes it with the contents of this <code>Action</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>ActionBuilder</code>
	 */

	@Override
	public ActionBuilder getBuilder (final DomainModel model)
	{
		return Action.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
	}

	/**
	 * Get the name of the <code>Action</code>.
	 *
	 * @return A String containing the name of the <code>Action</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>Action</code>.  This method is intended to be
	 * used to initialize a new <code>Action</code> instance
	 *
	 * @param  name The name of the <code>Action</code>
	 */

	protected abstract void setName (final String name);
}
