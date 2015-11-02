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

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.Persister;

/**
 * Create new <code>Role</code> instances.  This class extends
 * <code>AbstractBuilder</code>, adding the functionality required to
 * create <code>Role</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Role
 */

public final class RoleBuilder implements Builder<Role>
{
	/** The Logger */
	private final Logger log;

	/** Helper to operate on <code>Role</code> instances*/
	private final Persister<Role> persister;

	/** Method reference to the constructor of the implementation class */
	private final Supplier<Role> supplier;

	/** The loaded of previously created <code>Role</code> */
	private Role role;

	/** The <code>DataStore</code> id number for the <code>Role</code> */
	private Long id;

	/** The name of the <code>Role</code> */
	private String name;

	/**
	 * Create the <code>RoleBuilder</code>.
	 *
	 * @param  supplier  Method reference to the constructor of the
	 *                   implementation class, not null
	 * @param  persister The <code>Persister</code> used to store the
	 *                   <code>Role</code>, not null
	 */

	protected RoleBuilder (final Supplier<Role> supplier, final Persister<Role> persister)
	{
		assert supplier != null : "supplier is NULL";
		assert persister != null : "persister is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.persister = persister;
		this.supplier = supplier;

		this.role = null;
		this.id = null;
		this.name = null;
	}

	/**
	 * Create an instance of the <code>Role</code>.
	 *
	 * @return                       The new <code>Role</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If there isn't an active transaction
	 */

	@Override
	public Role build ()
	{
		this.log.trace ("build:");

		if (this.name == null)
		{
			this.log.error ("Attempting to create an Role without a name");
			throw new IllegalStateException ("name is NULL");
		}

		Role result = this.supplier.get ();
		result.setId (this.id);
		result.setName (this.name);

		this.role = this.persister.insert (this.role, result);

		return this.role;
	}

	/**
	 * Reset the builder.  This method will set all of the fields for the
	 * <code>Element</code> to be built to <code>null</code>.
	 *
	 * @return This <code>RoleBuilder</code>
	 */

	public RoleBuilder clear ()
	{
		this.log.trace ("clear:");

		this.role = null;
		this.id = null;
		this.name = null;

		return this;
	}

	/**
	 * Load a <code>Role</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>Role</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  role                     The <code>Role</code>, not null
	 *
	 * @return                          This <code>RoleBuilder</code>
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Role</code> instance to be
	 *                                  loaded are not valid
	 */

	public RoleBuilder load (final Role role)
	{
		this.log.trace ("load: role={}", role);

		if (role == null)
		{
			this.log.error ("Attempting to load a NULL Role");
			throw new NullPointerException ();
		}

		this.role = role;
		this.id = role.getId ();
		this.setName (role.getName ());

		return this;
	}

	/**
	 * Get the name of the <code>Role</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Role</code>
	 */

	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>Role</code>.
	 *
	 * @param  name                     The name of the <code>Role</code>, not
	 *                                  null
	 *
	 * @return                          This <code>RoleBuilder</code>
	 * @throws IllegalArgumentException If the name is empty
	 */

	public RoleBuilder setName (final String name)
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
