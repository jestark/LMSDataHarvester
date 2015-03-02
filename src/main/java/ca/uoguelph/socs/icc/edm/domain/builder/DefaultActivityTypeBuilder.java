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

package ca.uoguelph.socs.icc.edm.domain.builder;

import java.util.Set;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivitySourceManager;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.ActivityTypeBuilder;

import ca.uoguelph.socs.icc.edm.domain.manager.ManagerProxy;

public final class DefaultActivityTypeBuilder extends AbstractBuilder<ActivityType, ActivityTypeElementFactory> implements ActivityTypeBuilder
{
	/**
	 * Implementation of the <code>BuilderFactory</code> to create a
	 * <code>DefaultActivityTypeBuilder</code>.
	 */

	private static class Factory implements BuilderFactory<ActivityType, ActivityTypeBuilder>
	{
		/**
		 * Create the <code>ActivityTypeBuilder</code>.  The supplied
		 * <code>ManagerProxy</code> will be used by the builder to access the
		 * <code>ActivityTypeManager</code> to perform operations on the
		 * <code>DataStore</code>.
		 *
		 * @param  manager The <code>ManagerProxy</code> used to the 
		 *                 <code>ActivityTypeManager</code> instance, not null
		 *
		 * @return         The <code>ActivityTypeBuilder</code>
		 */

		@Override
		public ActivityTypeBuilder create (final ManagerProxy<ActivityType> manager)
		{
			return new DefaultActivityTypeBuilder (manager);
		}
	}

	/** The logger */
	private final Logger log;

	/** The source of the <code>ActivityType</code> */
	private ActivitySource source;

	/** The name of the <code>ActivityType</code> */
	private String name;

	/** The <code>Set</code> of associated <code>Action</code> instances */
	private Set<Action> actions;

	/**
	 * static initializer to register the <code>DefaultActivityTypeBuilder</code> with the
	 * factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (ActivityTypeBuilder.class, DefaultActivityTypeBuilder.class, new Factory ());
	}

	/**
	 * Create the <code>DefaultActivityTypeBuilder</code>.
	 *
	 * @param  manager The <code>ActivityTypeManager</code> which the
	 *                 <code>ActivityTypeBuilder</code> will use to operate on the
	 *                 <code>DataStore</code>
	 */

	protected DefaultActivityTypeBuilder (final ManagerProxy<ActivityType> manager)
	{
		super (ActivityType.class, ActivityTypeElementFactory.class, manager);

		this.log = LoggerFactory.getLogger (DefaultActivityTypeBuilder.class);
	}

	@Override
	protected ActivityType buildElement ()
	{
		if (this.source == null)
		{
			this.log.error ("Can not build: The activity type's activity source is not set");
			throw new IllegalStateException ("activity source not set");
		}

		if (this.name == null)
		{
			this.log.error ("Can not build: The activity type's name is not set");
			throw new IllegalStateException ("name not set");
		}

		ActivityType result = this.element;

		if ((this.element == null) || (! this.source.equals (this.element.getSource ())) || (! this.name.equals (this.element.getName ())))
		{
			result = (getFactory (ActivityTypeElementFactory.class, this.manager.getElementImplClass (ActivityType.class))).create (this.source, this.name);
		}

		return null;
	}

	@Override
	protected void postInsert ()
	{
	}

	@Override
	protected void postRemove ()
	{
	}

	/**
	 * Reset the <code>ElementBuilder</code>.  This method will set all of the
	 * fields for the <code>Element</code> to be built to <code>null</code>.
	 */

	@Override
	public void clear ()
	{
		this.log.trace ("Reseting the builder");

		super.clear ();
		this.name = null;
		this.source = null;
	}

	/**
	 * Load a <code>ActivityType</code> instance into the
	 * <code>ActivityTypeBuilder</code>.  This method resets the
	 * <code>ActivityTypeBuilder</code> and initializes all of its parameters from
	 * the specified <code>ActivityType</code> instance.  The parameters are
	 * validated as they are set.
	 *
	 * @param  type                     The <code>ActivityType</code> to load into
	 *                                  the <code>ActivityTypeBuilder</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If any of the fields in the 
	 *                                  <code>ActivityType</code> instance to be
	 *                                  loaded are not valid
	 */

	@Override
	public void load (final ActivityType type)
	{
		this.log.trace ("Load ActivityType: {}", type);

		super.load (type);
		this.setName (type.getName ());
		this.setActivitySource (type.getSource ());
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
	 * Set the name of the <code>ActivityType</code>.
	 *
	 * @param  name                     The name of the <code>ActivityType</code>,
	 *                                  not null
	 *
	 * @return                          This <code>ActivityTypeBuilder</code>
	 * @throws IllegalArgumentException If the name is an empty
	 */

	@Override
	public ActivityTypeBuilder setName (final String name)
	{
		if (name == null)
		{
			this.log.error ("name is NULL");
			throw new NullPointerException ("name is NULL");
		}

		if (name.length () == 0)
		{
			this.log.error ("name is an empty string");
			throw new IllegalArgumentException ("name is empty");
		}

		this.name = name;

		return this;
	}

	/**
	 * Get the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @return The <code>ActivitySource</code> instance
	 */

	@Override
	public ActivitySource getActivitySource ()
	{
		return this.source;
	}

	/**
	 * Set the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @param  source                   The <code>ActivitySource</code> for the
	 *                                  <code>ActivityType</code>
	 *
	 * @return                          This <code>ActivityTypeBuilder</code>
	 * @throws IllegalArgumentException If the <code>AcivitySourse</code> does not
	 *                                  exist in the <code>DataStore</code>
	 */

	@Override
	public ActivityTypeBuilder setActivitySource (final ActivitySource source)
	{
		if (source == null)
		{
			this.log.error ("source is NULL");
			throw new NullPointerException ("source is NULL");
		}

		this.source = (this.manager.getManager (ActivitySource.class, ActivitySourceManager.class)).fetch (source);

		if (this.source == null)
		{
			this.log.error ("The specified ActivitySource does not exist in the DataStore: {}", source);
			throw new IllegalArgumentException ("ActivitySource is not in the DataStore");
		}

		return this;
	}

	/**
	 * Get the <code>Set</code> of <code>Action</code> instances which are
	 * associated with the <code>ActivityType</code>.  If there are no
	 * associated <code>Action</code> instances, then the <code>Set</code>
	 * will be empty.
	 *
	 * @return A <code>Set</code> of <code>Action</code> instances
	 */

	public Set<Action> getActions ()
	{
		return new HashSet<Action> (this.actions);
	}

	/**
	 * Create an association between the <code>ActivityType</code> and an
	 * <code>Action</code>.
	 *
	 * @param  action                   The <code>Action</code> to be associated
	 *                                  with the <code>ActivityType</code>, not
	 *                                  null
	 * @return                          This <code>ActivityTypeBuilder</code>
	 * @throws IllegalArgumentException If the <code>Action</code> does not exist
	 *                                  in the <code>DataStore</code>
	 */

	public ActivityTypeBuilder addAction (final Action action)
	{
		return this;
	}

	/**
	 * Break an association between the <code>ActivityType</code> and an
	 * <code>Action</code>.
	 *
	 * @param  action                The <code>Action</code> to remove from the
	 *                               <code>ActivityType</code>, not null
	 *
	 * @return                       This <code>ActivityTypeBuilder</code>
	 * @throws IllegalStateException If there are any log entries containing the
	 *                               <code>ActivityType</code> and the
	 *                               <code>Action</code>
	 */

	public ActivityTypeBuilder removeAction (final Action action)
	{
		return this;
	}
}
