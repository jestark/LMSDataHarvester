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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

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

public abstract class AbstractSubActivityBuilder<T extends SubActivity> implements Builder<T>
{
	/** The Logger */
	protected final Logger log;

	/** Helper to operate on <code>SubActivity</code> instances*/
	protected final DataStoreRWProxy<SubActivity> subActivityProxy;

	/** The parent */
	protected final ParentActivity parent;

	/** The loaded or previously built <code>SubActivity</code> instance */
	protected SubActivity oldSubActivity;

	/** The <code>DataStore</code> id number for the <code>SubActivity</code> */
	protected Long id;

	/** The name of the <code>SubActivity</code> */
	protected String name;

	/**
	 * Create the <code>DefaultSubActivityBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  parent    The code <code>ParentActivity</code>, not null
	 */

	protected AbstractSubActivityBuilder (final DataStore datastore, final ParentActivity parent)
	{
		assert datastore != null : "datastore is NULL";
		assert parent != null : "parent is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		// The ParentActivity class exists as compromise for JPA.  As such it
		// does not have metadata, so we have to use instance of here instead
		// of a proper OO method.
		if (parent instanceof Activity)
		{
			this.parent = DataStoreProxy.getInstance (datastore.getProfile ().getMetaData (Activity.class), Activity.SELECTOR_ID, datastore)
				.fetch ((Activity) parent);
		}
		else
		{
			this.parent = DataStoreProxy.getInstance (datastore.getProfile ().getMetaData (SubActivity.class), SubActivity.SELECTOR_ID, datastore)
				.fetch ((SubActivity) parent);
		}

		if (this.parent == null)
		{
			this.log.error ("The Parent Activity does not exist in the DataStore");
			throw new IllegalStateException ("Parent is not in the DataStore");
		}

		Class<? extends SubActivity> sclass = SubActivity.getSubActivityClass (this.parent.getClass ());

		if (sclass == null)
		{
			throw new IllegalStateException ("No registered Subactivity classes corresponding to the specified parent");
		}

		this.subActivityProxy = DataStoreRWProxy.getInstance (datastore.getProfile ().getCreator (SubActivity.class, sclass), SubActivity.SELECTOR_ID, datastore);

		this.id = null;
		this.name = null;
		this.oldSubActivity = null;
	}

	/**
	 * Reset the builder.  This method will set all of the fields for the
	 * <code>Element</code> to be built to <code>null</code>.
	 *
	 * @return This <code>ActionBuilder</code>
	 */

	public AbstractSubActivityBuilder<T> clear ()
	{
		this.log.trace ("clear:");

		this.id = null;
		this.name = null;
		this.oldSubActivity = null;

		return this;
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

	public AbstractSubActivityBuilder<T> load (final T subactivity)
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

		this.id = subactivity.getId ();
		this.setName (subactivity.getName ());
		this.oldSubActivity = subactivity;

		return this;
	}

	/**
	 * Get the name of the <code>Activity</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>SubActivity</code>
	 */

	public final String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>SubActivity</code>.
	 *
	 * @param  name                     The name of the
	 *                                  <code>SubActivity</code>, not null
	 *
	 * @throws IllegalArgumentException If the name is empty
	 */

	public final AbstractSubActivityBuilder<T> setName (final String name)
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

	public final ParentActivity getParent ()
	{
		return this.parent;
	}
}
