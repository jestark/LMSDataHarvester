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

import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
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
 * @see     ActionLoader
 * @see     ActionBuilder
 */

public abstract class Action extends Element
{
	/** The name of the <code>Action</code> */
	public static final Property<String> NAME;

	/** Select an <code>Action</code> instance by its name */
	public static final Selector SELECTOR_NAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Action</code>.
	 */

	static
	{
		NAME = Property.getInstance (String.class, "name", Property.Flags.REQUIRED);

		SELECTOR_NAME = Selector.getInstance (NAME, true);

		Definition.getBuilder (Action.class, Element.class)
			.addProperty (NAME, Action::getName, Action::setName)
			.addRelationship (LogEntry.class, LogEntry.ACTION, LogEntry.SELECTOR_ACTION)
			.addSelector (SELECTOR_NAME)
			.build ();
	}

	/**
	 * Get an instance of the <code>ActionBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 *
	 * @return                       The <code>ActionBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Action</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is
	 *                               immutable
	 */

	public static ActionBuilder builder (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return new ActionBuilder (datastore);
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
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		return Action.builder (model.getDataStore ());
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
	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("name", this.getName ());

		return builder.toString ();
	}

	/**
	 * Get an <code>ActionBuilder</code> instance for the specified
	 * <code>DataStore</code>.  This method creates an
	 * <code>ActionBuilder</code> on the specified <code>DataStore</code> and
	 * initializes it with the contents of this <code>Action</code> instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The initialized <code>ActionBuilder</code>
	 */

	@Override
	public ActionBuilder getBuilder (final DataStore datastore)
	{
		assert datastore != null : "datastore is null";

		return Action.builder (datastore)
			.load (this);
	}

	/**
	 * Get the <code>MetaData</code> instance for this <code>Action</code>
	 * using the specified <code>DataStore</code>.
	 *
	 * @return The <code>MetaData</code>
	 */

	@Override
	protected MetaData<Action> metadata ()
	{
		return this.getDomainModel ()
			.getDataStore ()
			.getProfile ()
			.getCreator (Action.class, this.getClass ());
	}

	/**
	 * Get the name of the <code>Action</code>.
	 *
	 * @return A String containing the name of the <code>Action</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>Action</code>.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>Action</code> instance
	 * is loaded.
	 *
	 * @param  name The name of the <code>Action</code>
	 */

	protected abstract void setName (final String name);
}
