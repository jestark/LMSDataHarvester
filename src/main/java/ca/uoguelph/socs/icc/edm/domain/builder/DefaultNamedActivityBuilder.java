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

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Default implementation of the <code>NamedActivityBuilder</code>
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultNamedActivityBuilder extends AbstractActivityBuilder<Activity> implements NamedActivityBuilder<Activity>
{
	/**
	 * static initializer to register the
	 * <code>DefaultNamedActivityBuilder</code> with the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (DefaultNamedActivityBuilder.class, DefaultNamedActivityBuilder::new);
	}

	/**
	 * Create the <code>DefaultNamedActivityBuilder</code>.
	 *
	 * @param  impl      The implementation class of the <code>Element</code>
	 *                   to be built
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>NamedActivity</code> instance will be
	 *                   inserted
	 */

	protected DefaultNamedActivityBuilder (final Class<?> impl, final DataStore datastore)
	{
		super (impl, datastore);
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

		if (activity == null)
		{
			this.log.error ("Attempting to load a NULL Activity");
			throw new NullPointerException ();
		}

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
		return this.builder.getPropertyValue (Activity.Properties.NAME);
	}

	/**
	 * Set the name of the <code>Activity</code>.
	 *
	 * @param  name                     The name of the <code>Activity</code>,
	 *                                  not null
	 *
	 * @throws IllegalArgumentException if the name is empty
	 */

	public void setName (final String name)
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

		this.builder.setProperty (Activity.Properties.NAME, name);
	}
}
