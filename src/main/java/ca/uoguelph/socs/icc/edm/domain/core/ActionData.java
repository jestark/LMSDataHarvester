/* Copyright (C) 2014 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.core;

import java.io.Serializable;
import java.util.Set;

import java.util.HashSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultActionBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.ActionElementFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.ActionFactory;
import ca.uoguelph.socs.icc.edm.domain.manager.DefaultActionManager;

/**
 * Implementation of the Action interface.  It is expected that this object
 * will be accessed though the Action interface, and its relevant managers.
 * See the Action interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Action
 * @see     ActionManager
 */

public class ActionData implements Action, Serializable
{
	private static final class ActionDataFactory implements ActionElementFactory
	{
		@Override
		public Action create (String name)
		{
			return new ActionData (name);
		}

		@Override
		public void setId (Action Action, Long id)
		{
			((ActionData) Action).setId (id);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The datastore id of this action */
	private Long id;

	/** The name of this action */
	private String name;

	/** A set of the Activity Types associated with this action */
	private Set<ActivityType> types;

	static
	{
		(ActionFactory.getInstance ()).registerElement (ActionData.class, DefaultActionManager.class, DefaultActionBuilder.class, new ActionDataFactory ());
	}

	/**
	 * Create the <code>Action</code> with null values.  Default no-argument
	 * constructor used by the <code>DataStore</code> (particularly the Java
	 * Persistence API) when loading an instance of the class.  This constructor
	 * initializes all values to null and expects that the values in the class
	 * will be set via the (protected) mutator methods.
	 */

	public ActionData ()
	{
		this.id= null;
		this.name = null;
		this.types = null;
	}

	public ActionData (String name)
	{
		this ();

		this.name = name;

		this.types = new HashSet<ActivityType> ();
	}

	/**
	 * Override java.lang.object's equals method to compare to Actions based on
	 * their names.
	 *
	 * @param  obj The reference object with which to compare
	 * @return <code>true</code> if the two actions have the same name (or if
	 * the references are identical), <code>false</code> otherwise.
	 */

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof ActionData)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.appendSuper (super.equals (obj));
			ebuilder.append (this.name, ((ActionData) obj).name);

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Override of java.lang.object's hashCode method to compute a hash code
	 * based on the name of the Action.
	 *
	 * @return The hash code.
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1051;
		final int mult = 941;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.name);

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the datastore ID for the Action.
	 *
	 * @return The unique numeric ID of the Action.
	 */

	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Internal method used by the datastore and managers to set the datastore
	 * ID for the Action.
	 *
	 * @param id The unique numeric ID of the Action.
	 */

	protected void setId (Long id)
	{
		this.id = id;
	}

	/**
	 * Get a set containing all of the ActivityTypes associated with this
	 * Action.
	 *
	 * @return The Set of Activity Types associated with this Action.
	 */

	public Set<ActivityType> getTypes ()
	{
		return new HashSet<ActivityType> (this.types);
	}

	/**
	 * Internal method used by the datastore and managers to set the assocated
	 * Activity Type.
	 *
	 * @param types The Set of Activity Types associated with this Action.
	 */

	protected void setTypes (Set<ActivityType> types)
	{
		this.types = types;
	}

	/**
	 * Internal methos used by the managers to associate an activity type with
	 * the action.
	 *
	 * @param type The Activity Type to associate with the action.
	 * @return <code>true</code> if the Activity Type was successfully added,
	 * <code>false</code> otherwise.
	 */

	protected boolean addType (ActivityType type)
	{
		return this.types.add (type);
	}

	/**
	 * Get the name of the Action.
	 *
	 * @return The name of the Action.
	 */

	public String getName()
	{
		return this.name;
	}

	/**
	 * Internal method used by the datastore and managers to set the name of the
	 * Action.
	 *
	 * @param name The name of the Action.
	 */

	protected void setName (String name)
	{
		this.name = name;
	}

	/**
	 * Override java.lang.Object's toString function to display the name of the
	 * Action.
	 *
	 * @return A string identifying the Action.
	 */

	@Override
	public String toString()
	{
		return this.name;
	}
}
