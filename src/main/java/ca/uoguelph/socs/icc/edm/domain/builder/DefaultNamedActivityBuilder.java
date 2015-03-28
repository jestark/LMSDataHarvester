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
import ca.uoguelph.socs.icc.edm.domain.NamedActivityBuilder;

import ca.uoguelph.socs.icc.edm.domain.manager.ManagerProxy;

/**
 * Default implementation of the <code>NamedActivityBuilder</code>
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultNamedActivityBuilder extends DefaultActivityBuilder<NamedActivityElementFactory> implements NamedActivityBuilder
{
	/**
	 * Implementation of the <code>BuilderFactory</code> to create a
	 * <code>DefaultNamedActivityBuilder</code>.
	 */

	private static class Factory implements BuilderFactory<Activity, NamedActivityBuilder>
	{
		/**
		 * Create the <code>ActivityBuilder</code>.  The supplied
		 * <code>ManagerProxy</code> will be used by the builder to access the
		 * <code>ActivityManager</code> to perform operations on the
		 * <code>DataStore</code>.
		 *
		 * @param  manager The <code>ManagerProxy</code> used to the
		 *                 <code>ActivityManager</code> instance, not null
		 *
		 * @return         The <code>ActivityBuilder</code>
		 */

		@Override
		public NamedActivityBuilder create (final ManagerProxy<Activity> manager)
		{
			return new DefaultNamedActivityBuilder (manager);
		}
	}

	/** The name of the <code>Activity</code> */
	private String name;

	/**
	 * static initializer to register the
	 * <code>DefaultNamedActivityBuilder</code> with the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (NamedActivityBuilder.class, DefaultNamedActivityBuilder.class, new Factory ());
	}

	/**
	 * Create the <code>DefaultNamedActivityBuilder</code>.
	 *
	 * @param  manager The <code>NamedActivityManager</code> which the
	 *                 <code>NamedActivityBuilder</code> will use to operate on
	 *                 the <code>DataStore</code>
	 */

	public DefaultNamedActivityBuilder (final ManagerProxy<Activity> manager)
	{
		super (NamedActivityElementFactory.class, manager);
	}

	@Override
	protected Activity buildElement ()
	{
		this.log.trace ("buildElement");

		if (this.course == null)
		{
			this.log.error ("Can not build:  Course has not been set");
			throw new IllegalStateException ("course not set");
		}

		if (this.name == null)
		{
			this.log.error ("Can not build:  Name has not been set");
			throw new IllegalStateException ("name not set");
		}

		Activity result = this.element;

		if ((this.element == null) || (! this.course.equals (this.element.getCourse ())) || (! this.name.equals (this.element.getName ())))
		{
			result = this.factory.create (this.type, this.course, this.name);
		}

		return result;
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

		super.load (activity);
		this.setName (activity.getName ());
	}

	/**
	 * Get the name of the <code>Activity</code>.
	 *
	 * @return The name of the <code>Activity</code>
	 */

	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>Activity</code>.
	 *
	 * @param  name                     The name of the <code>Activity</code>,
	 *                                  not null
	 *
	 * @return                          This <code>NamedActivityBuilder</code>
	 * @throws IllegalArgumentException if the name is empty
	 */

	public NamedActivityBuilder setName (final String name)
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
}
