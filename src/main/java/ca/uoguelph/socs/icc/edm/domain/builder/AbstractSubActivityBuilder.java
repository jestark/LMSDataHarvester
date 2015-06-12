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

import ca.uoguelph.socs.icc.edm.domain.element.AbstractActivity;

/**
 * Default implementation of the <code>SubActivityBuilder</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class AbstractSubActivityBuilder<T extends SubActivity> extends AbstractBuilder<T> implements SubActivityBuilder<T>
{
	/**
	 * Get an instance of the <code>SubActivityBuilder</code> which corresponds
	 * to the specified parent <code>Activity</code>.
	 *
	 * @param  <T>       The <code>SubActivityBuilder</code> type to be returned
	 * @param  parent    The parent <code>Activity</code>
	 * @param  datastore The <code>DataStore</code> instance, not null
	 */

	public static final <T extends SubActivityBuilder<U>, U extends SubActivity> T getInstance (final Activity activity, final DataStore datastore)
	{
		assert activity != null : "activity is NULL";
		assert datastore != null : "datastore is NULL";
		assert datastore.contains (activity) : "acitivity is not in the DataStore";

		T builder = AbstractBuilder.getInstance (AbstractActivity.getSubActivityClass (activity.getClass ()), datastore);
		((AbstractSubActivityBuilder) builder).setParent (activity);

		return builder;
	}

	/**
	 * Create the <code>DefaultSubActivityBuilder</code>.
	 *
	 * @param  impl      The implementation class of the <code>Element</code>
	 *                   to be built
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>SubActivity</code> instance will be
	 *                   inserted
	 */

	protected AbstractSubActivityBuilder (final Class<?> impl, final DataStore datastore)
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
	 * @param  subactivity              The <code>SubActivity</code> to load
	 *                                  into the
	 *                                  <code>SubActivityBuilder</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Activity</code> instance to be
	 *                                  loaded are not valid
	 */

	@Override
	public void load (final T subactivity)
	{
		this.log.trace ("load: activity={}", subactivity);

		if (subactivity == null)
		{
			this.log.error ("Attempting to load a NULL SubActivity");
			throw new NullPointerException ();
		}

		if (! (this.getParent ()).equals (subactivity.getParent ()))
		{
			this.log.error ("Can not load:  Parent activity instances are not equal");
			throw new IllegalArgumentException ("Parent activity instances are different");
		}

		super.load (subactivity);
		this.setName (subactivity.getName ());

		this.setPropertyValue ("id", subactivity.getId ());
	}

	/**
	 * Get the name of the <code>Activity</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>SubActivity</code>
	 */

	@Override
	public final String getName ()
	{
		return this.getPropertyValue (String.class, "name");
	}

	/**
	 * Set the name of the <code>SubActivity</code>.
	 *
	 * @param  name                     The name of the
	 *                                  <code>SubActivity</code>, not null
	 *
	 * @throws IllegalArgumentException If the name is empty
	 */

	@Override
	public final void setName (final String name)
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

		this.setPropertyValue ("name", name);
	}

	/**
	 * Get the parent <code>Activity</code> instance for the
	 * <code>SubActivity</code> instance.
	 *
	 * @return The parent <code>Activity</code>
	 */

	@Override
	public final Activity getParent ()
	{
		return this.getPropertyValue (Activity.class, "parent");
	}

	/**
	 * Set the parent <code>Activity</code> instance for the
	 * <code>SubActivity</code> instance.
	 *
	 * @param  activity The parent <code>Activity</code>
	 */

	private void setParent (final Activity parent)
	{
		this.log.trace ("setParent: parent={}", parent);

		assert parent != null : "parent is NULL";
		assert this.datastore.contains (parent) : "parent is not in the DataStore";

		this.setPropertyValue ("parent", parent);
	}
}
