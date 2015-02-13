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

package ca.uoguelph.socs.icc.edm.domain.core;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultRoleBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.RoleElementFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.RoleFactory;

/**
 * Implementation of the <code>Role</code> interface.  It is expected that
 * instances of this class will be accessed though the <code>Role</code>
 * interface, along with the relevant manager, and builder.  See the
 * <code>Role</code> interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.DefaultRoleBuilder
 * @see     ca.uoguelph.socs.icc.edm.domain.manager.DefaultRoleManager
 */


public class RoleData implements Role, Serializable
{
	/**
	 * Implementation of the <code>RoleElementFactory</code> interface.  Allows
	 * the builders to create instances of <code>RoleData</code>.
	 */

	private static final class RoleDataFactory implements RoleElementFactory
	{
		/**
		 * Create a new <code>Role</code> instance.
		 *
		 * @param  name The name of the <code>Role</code>, not null
		 *
		 * @return      The new <code>Role</code> instance
		 */

		@Override
		public Role create (String name)
		{
			return new RoleData (name);
		}

		/**
		 * Write the specified <code>DataStore</code> ID number into the
		 * <code>Role</code>.
		 *
		 * @param  role The <code>Role</code> to which the ID number is assigned, not
		 *              null
		 * @param  id   The ID number assigned to the <code>Role</code>, not null
		 */

		@Override
		public void setId (Role role, Long id)
		{
			((RoleData) role).setId (id);
		}
	}

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
		(RoleFactory.getInstance ()).registerElement (RoleData.class, DefaultRoleBuilder.class, new RoleDataFactory ());
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

	public RoleData (String name)
	{
		this ();

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
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof RoleData)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.name, ((RoleData) obj).name);

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
	 * Set the <code>DataStore</code> identifier.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>Role</code> instance is
	 * loaded, or by the <code>RoleBuilder</code> implementation to set the
	 * <code>DataStore</code> identifier, prior to storing a new <code>Role</code>
	 * instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	protected void setId (Long id)
	{
		this.id = id;
	}

	/**
	 * Get the name of the <code>Role</code>.
	 *
	 * @return A <code>String</code> containing the name of the <code>Role</code>
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

	protected void setName (String name)
	{
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
