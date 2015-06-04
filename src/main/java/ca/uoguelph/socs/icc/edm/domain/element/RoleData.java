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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Role;

import ca.uoguelph.socs.icc.edm.domain.builder.DefaultRoleBuilder;

import ca.uoguelph.socs.icc.edm.domain.element.metadata.DefinitionBuilder;

/**
 * Implementation of the <code>Role</code> interface.  It is expected that
 * instances of this class will be accessed though the <code>Role</code>
 * interface, along with the relevant manager, and builder.  See the
 * <code>Role</code> interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.DefaultRoleBuilder
 */


public class RoleData extends AbstractElement implements Role, Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key for the role */
	private Long id;

	/** The name of the role */
	private String name;

	/**
	 * Static initializer to register the <code>RoleData</code> class with the
	 * factories.
	 */

	static
	{
		DefinitionBuilder<RoleData, Role.Properties> builder = DefinitionBuilder.newInstance (Role.class, RoleData.class, Role.Properties.class);
		builder.setCreateMethod (RoleData::new);

		builder.addUniqueAttribute (Role.Properties.ID, Long.class, false, false, RoleData::getId, RoleData::setId);
		builder.addUniqueAttribute (Role.Properties.NAME, String.class, true, false, RoleData::getName, RoleData::setName);

		AbstractElement.registerElement (builder.build (), DefaultRoleBuilder.class);
	}

	/**
	 * Create the <code>Role</code> with null values.
	 */

	public RoleData ()
	{
		this.id = null;
		this.name = null;
	}

	/**
	 * Create a new <code>Role</code> instance.
	 *
	 * @param  name The name of the <code>Role</code>, not null
	 */

	public RoleData (final String name)
	{
		this ();

		assert name != null : "name is NULL";

		this.name = name;
	}

	/**
	 * Compare two <code>Role</code> instances to determine if they are
	 * equal.  The <code>Role</code> instances are compared based upon their
	 * names.
	 *
	 * @param  obj The <code>Role</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Role</code> instances
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
		else if (obj instanceof Role)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.name, ((Role) obj).getName ());

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Role</code> instance.
	 * The hash code is computed based upon the name of the instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1069;
		final int mult = 919;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.name);

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Role</code>
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
	 * be used by a <code>DataStore</code> when the <code>Role</code> instance
	 * is loaded, or by the <code>RoleBuilder</code> implementation to set the
	 * <code>DataStore</code> identifier, prior to storing a new
	 * <code>Role</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	protected void setId (final Long id)
	{
		this.id = id;
	}

	/**
	 * Get the name of the <code>Role</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Role</code>
	 */

	@Override
	public String getName()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>Role</code>.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>Role</code> instance is
	 * loaded.
	 *
	 * @param name The name of the <code>Role</code>
	 */

	protected void setName (final String name)
	{
		assert name != null : "name is NULL";

		this.name = name;
	}

	/**
	 * Get a <code>String</code> representation of the <code>Role</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Role</code>
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