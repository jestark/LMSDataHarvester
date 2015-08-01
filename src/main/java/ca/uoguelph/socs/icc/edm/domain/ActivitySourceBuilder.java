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

package ca.uoguelph.socs.icc.edm.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Create new <code>ActivitySource</code> instances.  This class extends
 * <code>AbstractBuilder</code>, adding the functionality required
 * to create <code>ActivitySource</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivitySource
 */

public final class ActivitySourceBuilder extends AbstractBuilder<ActivitySource>
{
	/**
	 * Get an instance of the <code>ActivitySourceBuilder</code> for the
	 * specified <code>DataStore</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 *
	 * @return                       The <code>ActivitySourceBuilder</code>
	 *                               instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>ActivitySource</code>
	 */

	public static ActivitySourceBuilder getInstance (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return AbstractBuilder.getInstance (datastore, ActivitySource.class, ActivitySourceBuilder::new);
	}

	/**
	 * Get an instance of the <code>ActivitySourceBuilder</code> for the
	 * specified <code>DataStore</code>, loaded with the data from the
	 * specified <code>ActivitySource</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 * @param  source                The <code>ActivitySource</code>, not null
	 *
	 * @return                       The <code>ActivitySourceBuilder</code>
	 *                               instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>ActivitySource</code>
	 */

	public static ActivitySourceBuilder getInstance (final DataStore datastore, ActivitySource source)
	{
		assert datastore != null : "datastore is NULL";
		assert source != null : "source is NULL";

		ActivitySourceBuilder builder = ActivitySourceBuilder.getInstance (datastore);
		builder.load (source);

		return builder;
	}

	/**
	 * Get an instance of the <code>ActivitySourceBuilder</code> for the
	 * specified <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>ActivitySourceBuilder</code>
	 *                               instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>ActivitySource</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */


	public static ActivitySourceBuilder getInstance (final DomainModel model)
	{
		return ActivitySourceBuilder.getInstance (AbstractBuilder.getDataStore (model));
	}

	/**
	 * Get an instance of the <code>ActivitySourceBuilder</code> for the
	 * specified <code>DomainModel</code>, loaded with the data from the
	 * specified <code>ActivitySource</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 * @param  source                The <code>ActivitySource</code>, not null
	 *
	 * @return                       The <code>ActivitySourceBuilder</code>
	 *                               instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>ActivitySource</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static ActivitySourceBuilder getInstance (final DomainModel model, ActivitySource source)
	{
		if (source == null)
		{
			throw new NullPointerException ("source is NULL");
		}

		ActivitySourceBuilder builder = ActivitySourceBuilder.getInstance (model);
		builder.load (source);

		return builder;
	}

	/**
	 * Create the <code>ActivitySourceBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> implementation class, not
	 *                   null
	 */

	protected ActivitySourceBuilder (final DataStore datastore, final Class<? extends Element> element)
	{
		super (datastore, element);
	}

	/**
	 * Load a <code>ActivitySource</code> instance into the builder.  This
	 * method resets the builder and initializes all of its parameters from
	 * the specified <code>ActivitySource</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  source                   The <code>ActivitySource</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>ActivitySource</code> instance to
	 *                                  be loaded are not valid
	 */

	@Override
	public void load (final ActivitySource source)
	{
		this.log.trace ("load: source={}", source);

		if (source == null)
		{
			this.log.error ("Attempting to load a NULL ActivitySource");
			throw new NullPointerException ();
		}

		super.load (source);
		this.setName (source.getName ());

		this.builder.setProperty (ActivitySource.ID, source.getId ());
	}

	/**
	 * Get the name of the <code>ActivitySource</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivitySource</code>
	 */

	public String getName ()
	{
		return this.builder.getPropertyValue (ActivitySource.NAME);
	}

	/**
	 * Set the name of the <code>ActivitySource</code>.
	 *
	 * @param  name                     The name of the
	 *                                  <code>ActivitySource</code>, not null
	 *
	 * @throws IllegalArgumentException If the name is empty
	 */

	public void setName (final String name)
	{
		this.log.trace ("setName: name={}", name);

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

		this.builder.setProperty (ActivitySource.NAME, name);
	}
}
