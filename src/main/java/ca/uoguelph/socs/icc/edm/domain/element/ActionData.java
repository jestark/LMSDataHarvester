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

package ca.uoguelph.socs.icc.edm.domain.element;

import java.io.Serializable;
import java.util.Set;

import java.util.HashSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;

import ca.uoguelph.socs.icc.edm.domain.builder.DefaultActionBuilder;

import ca.uoguelph.socs.icc.edm.domain.element.metadata.DefinitionBuilder;

/**
 * Implementation of the <code>Action</code> interface.  It is expected that
 * instances of this class will be accessed though the <code>Action</code>
 * interface, along with the relevant manager, and builder.  See the
 * <code>Action</code> interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.DefaultActionBuilder
 * @see     ca.uoguelph.socs.icc.edm.domain.manager.DefaultActionManager
 */

public class ActionData extends AbstractElement implements Action, Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key of the action */
	private Long id;

	/** The name of the action */
	private String name;

	/** A set of the Activity Types associated with the action */
	private Set<ActivityType> types;

	/**
	 * Static initializer to register the <code>ActionData</code> class with
	 * the factories.
	 */

	static
	{
		DefinitionBuilder<Action, ActionData> builder = DefinitionBuilder.newInstance (Action.class, ActionData.class);
		builder.setCreateMethod (ActionData::new);
		builder.setBuilder (DefaultActionBuilder.class);

		builder.addUniqueAttribute ("id", Long.class, false, false, ActionData::getId, ActionData::setId);
		builder.addUniqueAttribute ("name", String.class, true, false, ActionData::getName, ActionData::setName);

		builder.addRelationship ("types", ActivityType.class, ActionData::addType, ActionData::removeType);

		AbstractElement.registerElement (builder.build ());
	}

	/**
	 * Create the <code>Action</code> with null values.
	 */

	protected ActionData ()
	{
		this.id= null;
		this.name = null;

		this.types = new HashSet<ActivityType> ();
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
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof Action)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.name, ((Action) obj).getName ());

			result = ebuilder.isEquals ();
		}

		return result;
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
		final int base = 1051;
		final int mult = 941;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.name);

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Action</code>
	 * instance.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Action</code>
	 * instance is loaded, or by the <code>ActionBuilder</code> implementation
	 * to set the <code>DataStore</code> identifier, prior to storing a new
	 * <code>Action</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	protected void setId (final Long id)
	{
		this.id = id;
	}

	/**
	 * Get the <code>Set</code> of <code>ActivityType</code> instances
	 * containing the action.
	 *
	 * @return A <code>Set</code> of <code>ActivityType</code> instances
	 */

	@Override
	public Set<ActivityType> getTypes ()
	{
		return new HashSet<ActivityType> (this.types);
	}

	/**
	 * Initialize the <code>Set</code> of associated <code>ActivityType</code>
	 * instances.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Action</code> instance is loaded.
	 *
	 * @param  types The <code>Set</code> of <code>ActivityType</code>
	 *               instances to be associated with the <code>Action</code>
	 */

	protected void setTypes (final Set<ActivityType> types)
	{
		this.types = types;
	}

	/**
	 * Associate the specified <code>ActivityType</code> with the
	 * <code>Action</code>
	 *
	 * @param  type The <code>ActivityType</code> associate with the
	 *              <code>Action</code>
	 *
	 * @return      <code>True</code> if the <code>ActivityType</code> was
	 *              successfully added, <code>False</code> otherwise.
	 */

	protected boolean addType (final ActivityType type)
	{
		return this.types.add (type);
	}

	/**
	 * Remove the association between the specified <code>ActivityType</code>
	 * and the <code>Action</code>
	 *
	 * @param  type The <code>ActivityType</code> associate with the
	 *              <code>Action</code>
	 *
	 * @return      <code>True</code> if the <code>ActivityType</code> was
	 *              successfully added, <code>False</code> otherwise.
	 */

	protected boolean removeType (final ActivityType type)
	{
		return this.types.remove (type);
	}

	/**
	 * Get the name of the <code>Action</code>.
	 *
	 * @return A String containing the name of the <code>Action</code>
	 */

	public String getName()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>Action</code>.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>Action</code> instance
	 * is loaded.
	 *
	 * @param  name The name of the <code>Action</code>
	 */

	protected void setName (final String name)
	{
		assert name != null : "name is NULL";

		this.name = name;
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

		builder.append ("name", this.name);

		return builder.toString ();
	}
}
