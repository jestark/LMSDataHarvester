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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.function.BiFunction;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.element.AbstractActivity;

/**
 * Abstract builder for <code>SubActivity</code> instances.  This class acts as
 * the common base for all of the builders which produce
 * <code>SubActivity</code> instances, handling all of the common
 * <code>SubActivity</code> components.
 * <p>
 * To create builders for <code>SubActivity</code> instances, the
 * parent <code>Activity</code> must be supplied when the builder is created.
 * The parent <code>Activity</code> is needed to determine which
 * <code>SubActivity</code> implementation class is to be created by the
 * builder.  It is possible to specify a parent <code>Activity</code> which
 * does not match the selected builder.  In this case the builder will be
 * created successfully, but an exception will occur when a field is set in the
 * builder that does not exist in the implementation, or when the
 * implementation is built and a required field is determined to be missing.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>SubActivity</code>
 * @see     SubActivity
 */

public abstract class AbstractSubActivityBuilder<T extends SubActivity> extends AbstractBuilder<T>
{
	/**
	 * Get an instance of the <code>SubActivityBuilder</code> which corresponds
	 * to the specified parent <code>Activity</code>.
	 *
	 * @param  <T>                   The <code>SubActivity</code> type of the
	 *                               builder
	 * @param  <U>                   The <code>SubActivityBuilder</code> type
	 *                               to be returned
	 * @param  parent                The parent <code>Activity</code>, not null
	 * @param  datastore             The <code>DataStore</code>, not null
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Activity</code>
	 */

	protected static <T extends SubActivity, U extends AbstractSubActivityBuilder<T>> U getInstance (final DataStore datastore, final Class<T> element, final Activity parent, final BiFunction<DataStore, Builder<T>, U> create)
	{
		assert datastore != null : "datastore is NULL";
		assert parent != null : "parent is NULL";
		assert create != null : "create is NULL";
		assert datastore.contains (parent) : "parent is not in the datastore";

		// Exception here because this is the fist time that it is checked
		if (! datastore.isOpen ())
		{
			throw new IllegalStateException ("datastore is closed");
		}

		// Exception here because this is the fist time that it is checked
		if (datastore.getProfile ().getElementClass (Activity.class) == null)
		{
			throw new IllegalStateException ("Element is not available for this datastore");
		}

		U builder = create.apply (datastore, AbstractBuilder.getBuilder (datastore, element, AbstractActivity.getSubActivityClass (parent.getClass ())));
		builder.setParent (parent);

		return builder;
	}

	/**
	 * Create the <code>DefaultSubActivityBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  builder   The <code>Builder</code>, not null
	 */

	protected AbstractSubActivityBuilder (final DataStore datastore, final Builder<T> builder)
	{
		super (datastore, builder);
	}

	/**
	 * Load a <code>SubActivity</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>SubActivity</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  subactivity              The <code>SubActivity</code>, not null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>SubActivity</code> instance to be
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

		this.builder.setProperty (SubActivity.ID, subactivity.getId ());
	}

	/**
	 * Get the name of the <code>Activity</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>SubActivity</code>
	 */

	public final String getName ()
	{
		return this.builder.getPropertyValue (SubActivity.NAME);
	}

	/**
	 * Set the name of the <code>SubActivity</code>.
	 *
	 * @param  name                     The name of the
	 *                                  <code>SubActivity</code>, not null
	 *
	 * @throws IllegalArgumentException If the name is empty
	 */

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

		this.builder.setProperty (SubActivity.NAME, name);
	}

	/**
	 * Get the parent <code>Activity</code> instance for the
	 * <code>SubActivity</code> instance.
	 *
	 * @return The parent <code>Activity</code>
	 */

	public final Activity getParent ()
	{
		return this.builder.getPropertyValue (SubActivity.PARENT);
	}

	/**
	 * Set the parent <code>Activity</code> instance for the
	 * <code>SubActivity</code> instance.
	 *
	 * @param  parent The parent <code>Activity</code>
	 */

	protected void setParent (final Activity parent)
	{
		this.log.trace ("setParent: parent={}", parent);

		assert parent != null : "parent is NULL";
		assert this.datastore.contains (parent) : "parent is not in the DataStore";

		this.builder.setProperty (SubActivity.PARENT, parent);
	}
}
