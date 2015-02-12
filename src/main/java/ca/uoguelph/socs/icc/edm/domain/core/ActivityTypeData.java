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

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityTypeBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.ActivityTypeElementFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.ActivityTypeFactory;

/**
 * Implementation of the <code>ActivityType</code> interface.  It is expected
 * that instances of this class will be accessed though the
 * <code>ActivityType</code> interface, along with the relevant manager, and
 * builder.  See the <code>ActivityType</code> interface documentation for
 * details.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityTypeBuilder
 * @see     ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivityTypeManager
 */

public class ActivityTypeData implements ActivityType, Serializable
{
	/**
	 * Implementation of the <code>ActivityTypeElementFactory</code> interface.
	 * Allows the builders to create instances of <code>ActivityTypeData</code>.
	 */

	private static final class ActivityTypeDataFactory implements ActivityTypeElementFactory
	{
		/**
		 * Create a new <code>ActivityType</code> instance.
		 *
		 * @param  source The <code>ActivitySource</code> for the
		 *                <code>ActivityType</code>, not null
		 * @param  name   The name of the <code>ActivityType</code>, not null
		 * @return        The new <code>ActivityType</code> instance
		 */

		@Override
		public ActivityType create (ActivitySource source, String name)
		{
			return new ActivityTypeData (source, name);
		}

		/**
		 * Write the specified <code>DataStore</code> ID number into the
		 * <code>Action</code>.
		 *
		 * @param  type The <code>ActivityType</code> to which the ID number is
		 *              assigned, not null
		 * @param  id   The ID number assigned to the <code>ActivityType</code>, not
		 *              null
		 */

		@Override
		public void setId (ActivityType type, Long id)
		{
			((ActivityTypeData) type).setId (id);
		}

		/**
		 * Add the specified <code>Action</code> to the specified
		 * <code>ActivityType</code>.
		 *
		 * @param  type   The <code>ActivityType</code> to which the
		 *                <code>Action</code> is to be added, not null
		 * @param  action The <code>Action</code> to add to the
		 *                <code>ActivityType</code>, not null
		 *
		 * @return        <code>True</code> if the <code>Action</code> was
		 *                successfully added to the <code>ActivityType</code>,
		 *                <code>False</code> otherwise
		 */

		@Override
		public boolean addAction (ActivityType type, Action action)
		{
			return ((ActivityTypeData) type).addAction (action);
		}

		/**
		 * Remove the specified <code>Action</code> from the specified
		 * <code>ActivityType</code>. 
		 *
		 * @param  type   The <code>ActivityType</code> from which the <code>Action</code>
		 *                is to be removed, not null
		 * @param  action The <code>Action</code> to remove from the
		 *                <code>ActivityType</code>, not null
		 *
		 * @return        <code>True</code> if the <code>Action</code> was
		 *                successfully removed from the <code>ActivityType</code>,
		 *                <code>False</code> otherwise
		 */

		@Override
		public boolean removeAction (ActivityType type, Action action)
		{
			return ((ActivityTypeData) type).removeAction (action);
		}
	}
	
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;
	
	/** The primary key of the activity type */
	private Long id;

	/** The name of the activity type */
	private String name;

	/** The source of the activity type */
	private ActivitySource source;

	/** The set of actions which are associated with the activity type */
	private Set<Action> actions;

	/**
	 * Static initializer to register the <code>ActivityTypeData</code> class with
	 * the factories.
	 */

	static
	{
		(ActivityTypeFactory.getInstance ()).registerElement (ActivityTypeData.class, DefaultActivityTypeBuilder.class, new ActivityTypeDataFactory ());
	}

	/**
	 * Create the <code>ActivityType</code> with null values.
	 */

	public ActivityTypeData ()
	{
		this.id = null;
		this.name = null;
		this.source = null;
		this.actions = null;
	}

	/**
	 * Create a new <code>ActivityType</code> instance.
	 *
	 * @param  source The <code>ActivitySource</code> for the
	 *                <code>ActivityType</code>, not null
	 * @param  name   The name of the <code>ActivityType</code>, not null
	 */

	public ActivityTypeData (ActivitySource source, String name)
	{
		this ();

		this.name = name;
		this.source = source;

		this.actions = new HashSet<Action> ();
	}

	/**
	 * Compare two <code>ActivityType</code> instances to determine if they are
	 * equal.  The <code>ActivityType</code> instances are compared based upon their
	 * associated <code>ActivitySource</code> and their names.
	 *
	 * @param  obj The <code>ActivityType</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>ActivityType</code> instances
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
		else if (obj instanceof ActivityTypeData)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.appendSuper (super.equals (obj));
			ebuilder.append (this.name, ((ActivityTypeData) obj).name);
			ebuilder.append (this.source, ((ActivityTypeData) obj).source);

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>ActivityType</code> instance.
	 * The hash code is computed based upon the associated
	 * <code>ActivitySource</code> and name of the instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1009;
		final int mult = 997;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.name);
		hbuilder.append (this.source);

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>ActivityType</code>
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
	 * used by a <code>DataStore</code> when the <code>ActivityType</code>
	 * instance is loaded, or by the <code>ActivityTypeBuilder</code>
	 * implementation to set the <code>DataStore</code> identifier, prior to
	 * storing a new <code>ActivityType</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	protected void setId (Long id)
	{
		this.id = id;
	}

	/**
	 * Get the name of the <code>ActivityType</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivityType</code>
	 */

	@Override
	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>ActivityType</code>.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>ActivityType</code>
	 * instance is loaded.
	 *
	 * @param  name The name of the <code>ActivityType</code>
	 */

	protected void setName (String name)
	{
		this.name = name;
	}

	/**
	 * Get the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @return The <code>ActivitySource</code> instance
	 */

	@Override
	public ActivitySource getSource ()
	{
		return this.source;
	}

	/**
	 * Set the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 * This method is intended to be used by a <code>DataStore</code> when the
	 * <code>ActivityType</code> instance is loaded.
	 *
	 * @param  source The <code>ActivitySource</code> for the
	 *                <code>ActivityType</code>
	 */

	protected void setSource (ActivitySource source)
	{
		this.source = source;
	}

	/**
	 * Get the <code>Set</code> of <code>Action</code> instances which are
	 * associated with the <code>ActivityType</code>.  If there are no
	 * associated <code>Action</code> instances, then the <code>Set</code>
	 * will be empty.
	 *
	 * @return A <code>Set</code> of <code>Action</code> instances
	 */

	@Override
	public Set<Action> getActions ()
	{
		return new HashSet<Action> (this.actions);
	}

	/**
	 * Initialize the <code>Set</code> of associated <code>Action</code>
	 * instances.  This method is intended to be used by a <code>DataStore</code>
	 * when the <code>ActivityType</code> instance is loaded.
	 *
	 * @param  actions The <code>Set</code> of <code>Action</code> instances
	 *                 to be associated with the <code>ActivityType</code>
	 */

	protected void setActions (Set<Action> actions)
	{
		this.actions = actions;
	}

	/**
	 * Add the specified <code>Action</code> to the
	 * <code>ActivityType</code>.
	 *
	 * @param  action The <code>Action</code> to add, not null
	 *
	 * @return        <code>True</code> if the <code>Action</code> was
	 *                successfully added, <code>False</code> otherwise
	 */

	protected boolean addAction (Action action)
	{
		return this.actions.add (action);
	}

	/**
	 * Remove the specified <code>Action</code> from the
	 * <code>ActivityType</code>. 
	 *
	 * @param  action The <code>Action</code> to remove, not null
	 *
	 * @return        <code>True</code> if the <code>Action</code> was
	 *                successfully removed, <code>False</code> otherwise
	 */

	protected boolean removeAction (Action action)
	{
		return this.actions.remove (action);
	}

	/**
	 * Get a <code>String</code> representation of the <code>ActivityType</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>ActivityType</code>
	 *         instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("source", this.source);
		builder.append ("name", this.name);

		return builder.toString ();
	}
}
