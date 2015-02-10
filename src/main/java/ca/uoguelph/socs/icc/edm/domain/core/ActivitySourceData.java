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
import java.util.Set;

import java.util.HashSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivitySourceBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.ActivitySourceElementFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.ActivitySourceFactory;

/**
 * Implementation of the <code>ActivitySource</code> interface.  It is expected
 * that instances of this class will be accessed though the
 * <code>ActivitySource</code> interface, along with the relevant manager, and
 * builder.  See the <code>ActivitySource</code> interface documentation for
 * details.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivitySourceBuilder
 * @see     ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivitySourceManager
 */

public class ActivitySourceData implements ActivitySource, Serializable
{
	/**
	 * Implementation of the <code>ActicitySourceElementFactory</code> interface.
	 * Allows the builders to create instances of <code>ActivitySourceData</code>.
	 */

	private static final class ActivitySourceDataFactory implements ActivitySourceElementFactory
	{
		/**
		 * Create a new <code>ActivitySource</code> instance.
		 *
		 * @param  name The name of the <code>ActivitySource</code>, not null
		 *
		 * @return      The new <code>ActivitySource</code> instance
		 */

		@Override
		public ActivitySource create (String name)
		{
			return new ActivitySourceData (name);
		}

		/**
		 * Write the specified <code>DataStore</code> ID number into the
		 * <code>ActivitySource</code>.
		 *
		 * @param  source The <code>ActivitySource</code> to which the ID number is
		 *                assigned, not null
		 * @param  id     The ID number assigned to the <code>ActivitySource</code>,
		 *                not null
		 */

		@Override
		public void setId (ActivitySource ActivitySource, Long id)
		{
			((ActivitySourceData) ActivitySource).setId (id);
		}

		/**
		 * Add the specified <code>ActivityType</code> to the specified
		 * <code>ActivitySource</code>.
		 *
		 * @param  source The <code>ActivitySource</code> to which the
		 *                <code>ActivityType</code> is to be added, not null
		 * @param  type   The <code>ActivityType</code> to add to the
		 *                <code>ActivitySource</code>, not null
		 *
		 * @return        <code>True</code> if the <code>ActivityType</code> was
		 *                successfully added to the <code>ActivitySource</code>,
		 *                <code>False</code> otherwise
		 */

		@Override
		public boolean addActivityType (ActivitySource source, ActivityType type)
		{
			return ((ActivitySourceData) source).addType (type);
		}

		/**
		 * Remove the specified <code>ActivityType</code> from the specified
		 * <code>ActivitySource</code>. 
		 *
		 * @param  source The <code>ActivitySource</code> from which the
		 *                <code>ActivityType</code> is to be removed, not null
		 * @param  type   The <code>ActivityType</code> to remove from the
		 *                <code>ActivitySource</code>, not null
		 *
		 * @return        <code>True</code> if the <code>ActivityType</code> was
		 *                successfully removed from the <code>ActivitySource</code>,
		 *                <code>False</code> otherwise
		 */

		@Override
		public boolean removeActivityType (ActivitySource source, ActivityType type)
		{
			return ((ActivitySourceData) source).removeType (type);
		}
	}

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
		(ActivitySourceFactory.getInstance ()).registerElement (ActivitySourceData.class, DefaultActivitySourceBuilder.class, new ActivitySourceDataFactory ());
	}

	/**
	 * Create the <code>ActivitySource</code> with null values.
	 */

	public ActivitySourceData ()
	{
		this.id = null;
		this.name = null;
		this.types = null;
	}

	/**
	 * Create a new <code>ActivitySource</code> instance.
	 *
	 * @param  name The name of the <code>ActivitySource</code>, not null
	 *
	 * @return      The new <code>ActivitySource</code> instance
	 */

	public ActivitySourceData (String name)
	{
		this ();

		this.name = name;

		this.types = new HashSet<ActivityType> ();
	}

	/**
	 * Compare two <code>ActivitySource</code> instances to determine if they are
	 * equal.  The <code>ActivitySource</code> instances are compared based upon
	 * their names.
	 *
	 * @param  obj The <code>ActivitySource</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>ActivitySource</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof ActivitySourceData)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.name, ((ActivitySourceData) obj).name);

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>ActivitySource</code>
	 * instance.  The hash code is computed based upon the name of the instance.
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

	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>ActivitySource</code>
	 * instance is loaded, or by the <code>ActivitySourceBuilder</code>
	 * implementation to set the <code>DataStore</code> identifier, prior to
	 * storing a new <code>ActivitySource</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	protected void setId (Long id)
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
	 * Set the name of the <code>ActivitySource</code>.  This method is intended
	 * to be used by a <code>DataStore</code> when the <code>ActivitySource</code>
	 * instance is loaded.
	 *
	 * @param name The name of the <code>ActivitySource</code>
	 */

	protected void setName (String name)
	{
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
	 * instances.  This method is intended to be used by a <code>DataStore</code>
	 * when the <code>ActivitySource</code> instance is loaded.
	 *
	 * @param  types The <code>Set</code> of <code>ActivityType</code> instances
	 *               to be associated with the <code>ActivitySource</code>
	 */

	protected void setTypes (Set<ActivityType> types)
	{
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

	protected boolean addType (ActivityType type)
	{
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

	protected boolean removeType (ActivityType type)
	{
		return this.types.remove (type);
	}

	/**
	 * Get a <code>String</code> representation of the <code>ActivitySource</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>ActivitySource</code>
	 *         instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("name", this.name);

		return builder.toString ();
	}
}
