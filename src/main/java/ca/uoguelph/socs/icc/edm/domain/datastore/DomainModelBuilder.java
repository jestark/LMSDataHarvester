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

package ca.uoguelph.socs.icc.edm.datastore;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModelElement;
import ca.uoguelph.socs.icc.edm.domain.DomainModelType;

/**
 * Create instances of the <code>DomainModel</code>.  This class will build up
 * a <code>DomainModelProfile</code> and use that profile to create an instance
 * of the <code>DomainModel</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     DomainModelProfile
 */

public abstract class DomainModelBuilder
{
	/** Internal copy of the profile which is being built. */
	private DomainModelProfile profile;

	/** The logger */
	private final Log log;

	/**
	 * Create the <code>DomainModelBuilder</code>.
	 */

	public DomainModelBuilder ()
	{
		this.log = LogFactory.getLog (DomainModelBuilder.class);
		this.profile = new DomainModelProfile (new Boolean (false));
	}

	/**
	 * Reset the builder to a blank state.
	 */

	public void clear ()
	{
		this.profile = new DomainModelProfile (new Boolean (false));
	}

	/**
	 * Determine if the <code>DomainModel</code> and its underlying
	 * <code>DataStore</code> can be changed.
	 *
	 * @return <code>true</code> if the <code>DomainModel</code> and its
	 *         underlying <code>DataStore</code> can be changed,
	 *         <code>false</code> otherwise
	 */

	public final Boolean isMutable ()
	{
		return this.profile.isMutable ();
	}

	/**
	 * Set the mutability of the <code>DomainModel</code>.
	 *
	 * @param  mutable The designed mutability of the <code>DomainModel</code>,
	 *                 not null
	 */

	public final void setMutable (Boolean mutable)
	{
		if (mutable == null)
		{
			this.log.error ("Mutable is NULL");
			throw new NullPointerException ("Mutable is NULL");
		}

		if (mutable != this.profile.isMutable ())
		{
			this.log.debug ("Not setting Mutable as it is unchanged");
		}
		else
		{
			this.profile = new DomainModelProfile (mutable, profile);
		}
	}

	/**
	 * Get the set of elements contained in this profile.
	 *
	 * @return A <code>Set</code> containing the <code>DomainModelType</code> of
	 *         all of the elements in the profile
	 */

	public final Set<DomainModelType> getElements ()
	{
		return this.profile.getElements ();
	}

	/**
	 * Determine if the <code>DataStore</code> contains a representation of a
	 * given domain model interface.
	 *
	 * @param  element Domain model interface class, not null
	 * @return         <code>true</code> if the <code>DataStore</code> contains a
	 *                 representation of the specified domain model interface,
	 *                 <code>false</code> otherwise
	 */

	public final Boolean isAvalable (DomainModelType element)
	{
		Boolean available = null;

		try
		{
			available = this.profile.isAvailable (element);
		}
		catch (NullPointerException ex)
		{
			this.log.error (ex.getMessage ());
			throw ex;
		}
		catch (IllegalArgumentException ex)
		{
			this.log.debug (ex.getMessage ());
		}

		return available;
	}

	/**
	 * Get the <code>DataStore</code> ID generation class for the specified domain
	 * model interface.
	 *
	 * @param  element Domain model interface class, not null
	 * @return         The associated ID generator class
	 */

	public final Class<? extends IdGenerator> getGenerator (DomainModelType element)
	{
		Class<? extends IdGenerator> generator = null;

		try
		{
			generator = this.profile.getGenerator (element);
		}
		catch (NullPointerException ex)
		{
			this.log.error (ex.getMessage ());
			throw ex;
		}
		catch (IllegalArgumentException ex)
		{
			this.log.debug (ex.getMessage ());
		}

		return generator;
	}

	/**
	 * Get the implementation class to be used for the specified domain model
	 * interface.
	 *
	 * @param  element Domain model interface class, not null
	 * @return         The class used to represent the interface in the
	 *                 <code>DataStore</code>
	 */

	public final Class<? extends DomainModelElement> getImplClass (DomainModelType element)
	{
		Class<? extends DomainModelElement> impl = null;

		try
		{
			impl = this.profile.getImplClass (element);
		}
		catch (NullPointerException ex)
		{
			this.log.error (ex.getMessage ());
			throw ex;
		}
		catch (IllegalArgumentException ex)
		{
			this.log.debug (ex.getMessage ());
		}

		return impl;
	}

	/**
	 * Add an entry to the profile for the specified element type.  A profile
	 * entry has the following components:
	 * <ul>
	 *   <li> The <code>DomainModelType</code> which represents a domain model
	 *        interface.  A single entry must exist for each of the domain model
	 *        interfaces.  Subsequent entries for a given element will replace
	 *        previous entries.
	 *   <li> An indication if the interface is represented by the database.  If
	 *        the interface is not represented, requests for the manager
	 *        associated with the interface will throw an
	 *        <code>IllegalStateException</code>.
	 *   <li> The implementation of the interface to be used, which must implement
	 *        the interface associated with the specified element.
	 *   <li> The ID number generator to be used.
	 * </ul>
	 *
	 * @param  element                  The <code>DomainModelType</code> of the
	 *                                  element which is being added, not null
	 * @param  available                Indication if the element is available in
	 *                                  the <code>DataStore</code>, not null
	 * @param  impl                     The implementation class to be used with
	 *                                  the element, not null
	 * @param  generator                The <code>IdGenerator</code> to be used
	 *                                  for the element, not null
	 * @throws IllegalArgumentException if the implementation class does not
	 *                                  implement the interface associated with
	 *                                  element
	 */

	public final void setEntry (DomainModelType element, Boolean available, Class<? extends DomainModelElement> impl, Class<? extends IdGenerator> generator)
	{
		try
		{
			this.profile.addEntry (element, available, impl, generator);
		}
		catch (NullPointerException ex)
		{
			this.log.error (ex.getMessage ());
			throw ex;
		}
		catch (IllegalArgumentException ex)
		{
			this.log.error (ex.getMessage ());
			throw ex;
		}
	}

	/**
	 * Validate and return the complete <code>DomainModelProfile</code>.  This
	 * method ensures that all of the required entries are in the profile before
	 * returning a copy of the profile.
	 *
	 * @return                       The complete <code>DomainModelProfile</code>
	 * @throws IllegalStateException if any of the domain model interfaces
	 *                               (enumerated in <code>DomainModelType</code>)
	 *                               are missing
	 */

	protected DomainModelProfile buildProfile ()
	{
		boolean abort = false;

		Set<DomainModelType> elements = this.profile.getElements ();

		for (DomainModelType t : DomainModelType.values ())
		{
			if (! elements.contains (t))
			{
				if (this.log.isErrorEnabled ())
				{
					this.log.error ("Element missing from domain model profile:" + t.getName ());
				}

				abort = true;
			}
		}

		if (abort)
		{
			throw new IllegalStateException ("Domain Model Profile is missing Elements");
		}

		return new DomainModelProfile (this.profile);
	}

	public abstract DomainModel createDomainModel ();
}
