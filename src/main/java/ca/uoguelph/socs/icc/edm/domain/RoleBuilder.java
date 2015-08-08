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

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Creator;

/**
 * Create new <code>Role</code> instances.  This class extends
 * <code>AbstractBuilder</code>, adding the functionality required to
 * create <code>Role</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Role
 */

public final class RoleBuilder extends AbstractBuilder<Role>
{
	/**
	 * Get an instance of the <code>RoleBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 *
	 * @return                       The <code>RoleBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Role</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static RoleBuilder getInstance (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return AbstractBuilder.getInstance (datastore, Role.class, RoleBuilder::new);
	}

	/**
	 * Get an instance of the <code>RoleBuilder</code> for the specified
	 * <code>DataStore</code>, loaded with the data from the specified
	 * <code>Role</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 * @param  role                  The <code>Role</code>, not null
	 *
	 * @return                       The <code>RoleBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Role</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static RoleBuilder getInstance (final DataStore datastore, Role role)
	{
		assert datastore != null : "datastore is NULL";
		assert role != null : "role is NULL";

		RoleBuilder builder = RoleBuilder.getInstance (datastore);
		builder.load (role);

		return builder;
	}

	/**
	 * Get an instance of the <code>RoleBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>RoleBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Role</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */


	public static RoleBuilder getInstance (final DomainModel model)
	{
		return RoleBuilder.getInstance (AbstractBuilder.getDataStore (model));
	}

	/**
	 * Get an instance of the <code>RoleBuilder</code> for the specified
	 * <code>DomainModel</code>, loaded with the data from the specified
	 * <code>Role</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 * @param  role                  The <code>Role</code>, not null
	 *
	 * @return                       The <code>RoleBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Role</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static RoleBuilder getInstance (final DomainModel model, Role role)
	{
		if (role == null)
		{
			throw new NullPointerException ("role is NULL");
		}

		RoleBuilder builder = RoleBuilder.getInstance (model);
		builder.load (role);

		return builder;
	}

	/**
	 * Create the <code>RoleBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  metadata  The meta-data <code>Creator</code> instance, not null
	 */

	protected RoleBuilder (final DataStore datastore, final Creator<Role> metadata)
	{
		super (datastore, metadata);
	}

	/**
	 * Load a <code>Role</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>Role</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  role                     The <code>Role</code>, not null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Role</code> instance to be
	 *                                  loaded are not valid
	 */

	@Override
	public void load (final Role role)
	{
		this.log.trace ("load: role={}", role);

		if (role == null)
		{
			this.log.error ("Attempting to load a NULL Role");
			throw new NullPointerException ();
		}

		super.load (role);
		this.setName (role.getName ());

		this.builder.setProperty (Role.ID, role.getId ());
	}

	/**
	 * Get the name of the <code>Role</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Role</code>
	 */

	public String getName ()
	{
		return this.builder.getPropertyValue (Role.NAME);
	}

	/**
	 * Set the name of the <code>Role</code>.
	 *
	 * @param  name                     The name of the <code>Role</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If the name is empty
	 */

	public void setName (final String name)
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

		this.builder.setProperty (Role.NAME, name);
	}
}
