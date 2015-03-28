/* Copyright (C) 2015 James E. Stark
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

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityManager;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.SubActivityBuilder;

import ca.uoguelph.socs.icc.edm.domain.manager.ManagerProxy;

/**
 * Default implementation of the <code>SubActivityBuilder</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultSubActivityBuilder extends AbstractBuilder<Activity, SubActivityElementFactory> implements SubActivityBuilder
{
	/**
	 * Implementation of the <code>BuilderFactory</code> to create a
	 * <code>DefaultSubActivityBuilder</code>.
	 */

	private static class Factory implements BuilderFactory<Activity, SubActivityBuilder>
	{
		/**
		 * Create the <code>ElementBuilder</code>.  The supplied
		 * <code>ManagerProxy</code> will be used by the builder to access the
		 * <code>ActivityManager</code> to perform operations on the
		 * <code>DataStore</code>.
		 *
		 * @param  manager The <code>ManagerProxy</code> used to the
		 *                 <code>ActivityManager</code> instance, not null
		 *
		 * @return         The <code>ActivitGroupMemberyBuilder</code>
		 */

		@Override
		public SubActivityBuilder create (final ManagerProxy<Activity> manager)
		{
			return new DefaultSubActivityBuilder (manager);
		}
	}

	/** The parent <code>Activity</code> */
	private final Activity parent;

	/** The name of the <code>Activity</code> */
	private String name;

	/**
	 * static initializer to register the
	 * <code>DefaultSubActivityBuilder</code> with the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (SubActivityBuilder.class, DefaultSubActivityBuilder.class, new Factory ());
	}

	/**
	 * Create the <code>DefaultSubActivityBuilder</code>.
	 *
	 * @param  manager The <code>SubActivityManager</code> which the
	 *                 <code>ActivityGroupBuilderBuilder</code> will use to
	 *                 operate on the <code>DataStore</code>
	 */

	public DefaultSubActivityBuilder (final ManagerProxy<Activity> manager)
	{
		super (Activity.class, SubActivityElementFactory.class, manager);

		this.parent = this.validateParent ((Activity) this.manager.getArgument ());
	}

	@Override
	protected Activity buildElement ()
	{
		this.log.trace ("buildElement:");

		if (this.name == null)
		{
			this.log.error ("Can not build:  The name of the Activity Group Member is not set");
			throw new IllegalStateException ("name not set");
		}

		Activity result = this.element;

		if ((this.element == null) || (! this.name.equals (this.element.getName ())))
		{
			result = this.factory.create (this.parent, this.name);
		}

		return result;
	}

	@Override
	protected void postInsert ()
	{
		this.log.trace ("postInsert:");

		AbstractActivityElementFactory factory = AbstractBuilder.getFactory (AbstractActivityElementFactory.class, this.parent.getClass ());

		factory.addSubActivity (this.parent, ((SubActivity) this.element));
	}

	@Override
	protected void postRemove ()
	{
		this.log.trace ("postremove:");

		AbstractActivityElementFactory factory = AbstractBuilder.getFactory (AbstractActivityElementFactory.class, this.parent.getClass ());

		factory.removeSubActivity (this.parent, ((SubActivity) this.element));
	}

	/**
	 * Reset the <code>ElementBuilder</code>.  This method will set all of the
	 * fields, with the exception of the parent <code>ActivityGroup</code> for
	 * the <code>Element</code> to be built to <code>null</code>.
	 */

	@Override
	public void clear ()
	{
		this.log.trace ("clear:");

		super.clear ();
		this.name = null;
	}

	/**
	 * Load a <code>Activity</code> instance into the
	 * <code>ActivityBuilder</code>.  This method resets the
	 * <code>ActivityBuilder</code> and initializes all of its parameters from
	 * the specified <code>Activity</code> instance.  The parameters are
	 * validated as they are set.
	 *
	 * @param  activity                 The <code>Activity</code> to load into
	 *                                  the <code>ActivityBuilder</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Activity</code> instance to be
	 *                                  loaded are not valid
	 */

	@Override
	public void load (final Activity activity)
	{
		this.log.trace ("load: activity={}", activity);

		if (! this.parent.equals (((SubActivity) activity).getParent ()))
		{
			this.log.error ("Can not load:  Parent activity instances are not equal");
			throw new IllegalArgumentException ("Parent activity instances are different");
		}

		super.load (activity);
		this.setName (activity.getName ());
	}

	/**
	 * Get the name of the <code>Activity</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>SubActivity</code>
	 */

	@Override
	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>SubActivity</code>.
	 *
	 * @param  name                     The name of the
	 *                                  <code>SubActivity</code>, not null
	 *
	 * @return                          This <code>SubActivityBuilder</code>
	 * @throws IllegalArgumentException If the name is an empty
	 */

	@Override
	public SubActivityBuilder setName (final String name)
	{
		this.log.trace ("setName: name={}", name);

		if (name == null)
		{
			this.log.error ("Attempting to set a NULL name");
			throw new NullPointerException ();
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
	 * Get the parent <code>Activity</code> instance for the
	 * <code>SubActivity</code> instance.
	 *
	 * @return The parent <code>Activity</code>
	 */

	@Override
	public Activity getParent ()
	{
		return this.parent;
	}

	/**
	 * Validate the parent <code>Activity</code>.
	 *
	 * @param  parent The parent <code>Activity</code>, not null
	 *
	 * @return        A reference to the parent activity in the
	 *                <code>DataStore</code>
	 */

	private Activity validateParent (final Activity parent)
	{
		assert parent != null : "parent is NULL";

		Activity nparent = (this.manager.getManager (Activity.class, ActivityManager.class)).fetch (parent);

		if (nparent == null)
		{
			this.log.error ("The parent Activity does not exist in the DataStore: {}", parent);
			throw new IllegalArgumentException ("Activity is not in the DataStore");
		}

		return nparent;
	}
}
