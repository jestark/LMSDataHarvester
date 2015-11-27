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

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.datastore.Persister;

/**
 * Implementation of the <code>Role</code> interface.  It is expected that
 * instances of this class will be accessed though the <code>Role</code>
 * interface, along with the relevant manager, and builder.  See the
 * <code>Role</code> interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class RoleData extends Role
{
	/**
	 * <code>Builder</code> for <code>RoleData</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 * @see     ca.uoguelph.socs.icc.edm.domain.Role.Builder
	 */

	public static final class Builder extends Role.Builder
	{
		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  persister The <code>Persister</code> used to store the
		 *                   <code>Role</code>, not null
		 */

		private Builder (final Persister<Role> persister)
		{
			super (persister);
		}

		/**
		 * Create an instance of the <code>Role</code>.
		 *
		 * @return The new <code>Role</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected Role createElement ()
		{
			this.log.trace ("createElement");

			return new RoleData (this);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key for the <code>Role</code> */
	private @Nullable Long id;

	/** The name of the <code>Role</code> */
	private String name;

	/**
	 * Create the <code>Role</code> with null values.
	 */

	protected RoleData ()
	{
		this.id = null;
		this.name = null;
	}

	/**
	 * Create an <code>Role</code> from the supplied <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected RoleData (final Builder builder)
	{
		super (builder);

		this.id = builder.getId ();
		this.name = Preconditions.checkNotNull (builder.getName (), "name");
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Role</code>
	 * instance.
	 *
	 * @return The <code>DataStore</code> identifier
	 */

	@Override
	@CheckReturnValue
	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used to initialize the <code>DataStore</code> identifier on a new
	 * <code>Role</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final @Nullable Long id)
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
	 * used to initialize a new <code>Role</code> instance.
	 *
	 * @param name The name of the <code>Role</code>
	 */

	@Override
	protected void setName (final String name)
	{
		assert name != null : "name is NULL";

		this.name = name;
	}
}
