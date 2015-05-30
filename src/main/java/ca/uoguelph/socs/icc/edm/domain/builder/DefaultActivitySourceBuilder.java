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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivitySourceBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Default implementation of the <code>ActivitySourceBuilder</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultActivitySourceBuilder extends AbstractBuilder<ActivitySource> implements ActivitySourceBuilder
{
	/**
	 * Implementation of the <code>BuilderFactory</code> to create a
	 * <code>DefaultActivitySourceBuilder</code>.
	 */

	private static class Factory implements BuilderFactory<ActivitySourceBuilder, ActivitySource>
	{
		/**
		 * Create the <code>ActivitySourceBuilder</code> for the specified
		 * <code>DataStore</code>.
		 *
		 * @param  datastore The <code>DataStore</code> into which new
		 *                   <code>ActivitySource</code> will be inserted
		 *
		 * @return           The <code>ActivitySourceBuilder</code>
		 */

		@Override
		public ActivitySourceBuilder create (final DataStore datastore)
		{
			return new DefaultActivitySourceBuilder (datastore);
		}
	}

	/** The name of the Activity Source */
	private String name;

	/**
	 * static initializer to register the
	 * <code>DefaultActivitySourceBuilder</code> with the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (ActivitySourceBuilder.class, DefaultActivitySourceBuilder.class, new Factory ());
	}

	/**
	 * Create the <code>DefaultActivitySourceBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>ActivitySource</code> instance will be
	 *                   inserted
	 */

	protected DefaultActivitySourceBuilder (final DataStore datastore)
	{
		super (ActivitySource.class, datastore);
	}

	@Override
	protected ActivitySource buildElement ()
	{
		if (this.name == null)
		{
			this.log.error ("Can not build: The activity source's name is not set");
			throw new IllegalStateException ("name not set");
		}

		ActivitySource result = this.element;

		if ((this.element == null) || (! this.name.equals (this.element.getName ())))
		{
//			result = this.factory.create (this.name);
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
	 * Load a <code>ActivitySource</code> instance into the
	 * <code>ActivitySourceBuilder</code>.  This method resets the
	 * <code>ActivitySourceBuilder</code> and initializes all of its parameters
	 * from the specified <code>ActivitySource</code> instance.  The parameters
	 * are validated as they are set.
	 *
	 * @param  source                   The <code>ActivitySource</code> to load
	 *                                  into the
	 *                                  <code>ActivitySourceBuilder</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>ActivitySource</code> instance to
	 *                                  be loaded are not valid
	 */

	@Override
	public void load (final ActivitySource source)
	{
		this.log.trace ("Load ActivitySource: {}", source);

		super.load (source);
		this.setName (source.getName ());
	}

	/**
	 * Get the name of the <code>ActivitySource</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivitySource</code>
	 */

	@Override
	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>ActivitySource</code>.
	 *
	 * @param  name                     The name of the
	 *                                  <code>ActivitySource</code>, not null
	 *
	 * @return                          This <code>ActivitySourceBuilder</code>
	 * @throws IllegalArgumentException If the name is an empty
	 */

	@Override
	public ActivitySourceBuilder setName (final String name)
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
}
