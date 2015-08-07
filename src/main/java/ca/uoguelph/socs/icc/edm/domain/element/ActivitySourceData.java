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

import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;

import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;

import ca.uoguelph.socs.icc.edm.domain.metadata.Implementation;

/**
 * Implementation of the <code>ActivitySource</code> interface.  It is expected
 * that instances of this class will be accessed though the
 * <code>ActivitySource</code> interface, along with the relevant manager, and
 * builder.  See the <code>ActivitySource</code> interface documentation for
 * details.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class ActivitySourceData extends ActivitySource implements Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key of the activity source */
	private Long id;

	/** The name of the activity source */
	private String name;

	/** The set of activity types which are associated with the source */
	private Set<ActivityType> types;

	/**
	 * Static initializer to register the <code>ActivitySourceData</code> class
	 * with the factories.
	 */

	static
	{
		Profile.registerCreator (Implementation.getInstance (ActivitySource.metadata, ActivitySourceData.class, ActivitySourceData::new));
	}

	/**
	 * Create the <code>ActivitySource</code> with null values.
	 */

	protected ActivitySourceData ()
	{
		this.id = null;
		this.name = null;

		this.types = new HashSet<ActivityType> ();
	}

	/**
	 * Compare two <code>ActivitySource</code> instances to determine if they
	 * are equal.  The <code>ActivitySource</code> instances are compared based
	 * upon their names.
	 *
	 * @param  obj The <code>ActivitySource</code> instance to compare to the
	 *             one represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>ActivitySource</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof ActivitySource)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.name, ((ActivitySource) obj).getName ());

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>ActivitySource</code>
	 * instance.  The hash code is computed based upon the name of the
	 * instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1097;
		final int mult = 883;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.name);

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the
	 * <code>ActivitySource</code> instance.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

	@Override
	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>ActivitySource</code>
	 * instance is loaded, or by the <code>ActivitySourceBuilder</code>
	 * implementation to set the <code>DataStore</code> identifier, prior to
	 * storing a new <code>ActivitySource</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final Long id)
	{
		this.id = id;
	}

	/**
	 * Get the name of the <code>ActivitySource</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivitySource</code>
	 */

	@Override
	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>ActivitySource</code>.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>ActivitySource</code> instance is loaded.
	 *
	 * @param name The name of the <code>ActivitySource</code>
	 */

	@Override
	protected void setName (final String name)
	{
		assert name != null : "name is NULL";

		this.name = name;
	}

	/**
	 * Get the <code>Set</code> of <code>ActivityType</code> instances for the
	 * <code>ActivitySource</code>.  If there are no <code>ActivityType</code>
	 * instances associated with the <code>ActivitySource</code> then the
	 * <code>Set</code> will be empty.
	 *
	 * @return A <code>Set</code> of <code>ActivityType</code> instances
	 */

	@Override
	public Set<ActivityType> getTypes ()
	{
		return new HashSet<ActivityType> (this.types);
	}

	/**
	 * Initialize the <code>Set</code> of dependent <code>ActivityType</code>
	 * instances.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>ActivitySource</code> instance is
	 * loaded.
	 *
	 * @param  types The <code>Set</code> of <code>ActivityType</code>
	 *               instances to be associated with the
	 *               <code>ActivitySource</code>
	 */

	@Override
	protected void setTypes (final Set<ActivityType> types)
	{
		assert types != null : "types is NULL";

		this.types = types;
	}

	/**
	 * Add the specified <code>ActivityType</code> to the
	 * <code>ActivitySource</code>.
	 *
	 * @param  type   The <code>ActivityType</code> to add, not null
	 *
	 * @return        <code>True</code> if the <code>ActivityType</code> was
	 *                successfully added, <code>False</code> otherwise
	 */

	@Override
	protected boolean addType (final ActivityType type)
	{
		assert type != null : "type is NULL";

		return this.types.add (type);
	}

	/**
	 * Remove the specified <code>ActivityType</code> from the
	 * <code>ActivitySource</code>.
	 *
	 * @param  type   The <code>ActivityType</code> to remove, not null
	 *
	 * @return        <code>True</code> if the <code>ActivityType</code> was
	 *                successfully removed, <code>False</code> otherwise
	 */

	@Override
	protected boolean removeType (final ActivityType type)
	{
		assert type != null : "type is NULL";

		return this.types.remove (type);
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>ActivitySource</code> instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>ActivitySource</code> instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("name", this.name);

		return builder.toString ();
	}
}
