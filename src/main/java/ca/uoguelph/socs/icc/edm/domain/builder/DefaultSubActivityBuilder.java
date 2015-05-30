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
import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.SubActivityBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Default implementation of the <code>SubActivityBuilder</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultSubActivityBuilder extends AbstractBuilder<Activity> implements SubActivityBuilder
{
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
		AbstractBuilder.registerBuilder (DefaultSubActivityBuilder.class, DefaultSubActivityBuilder::new);
	}

	/**
	 * Create the <code>DefaultSubActivityBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>SubActivity</code> instance will be
	 *                   inserted
	 */

	public DefaultSubActivityBuilder (final DataStore datastore)
	{
		super (Activity.class, datastore);

/*		assert (Activity) this.manager.getArgument () != null : "parent is NULL";

		if (! (this.manager.getManager (Activity.class, ActivityManager.class)).contains ((Activity) this.manager.getArgument ()))
		{
			this.log.error ("The parent Activity does not exist in the DataStore: {}", (Activity) this.manager.getArgument ());
			throw new IllegalArgumentException ("Activity is not in the DataStore");
		}
*/
		this.parent = null; //(Activity) this.manager.getArgument ();
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
//			result = this.factory.create (this.parent, this.name);
		}

		return result;
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
}
