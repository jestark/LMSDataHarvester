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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityBuilder;

import ca.uoguelph.socs.icc.edm.domain.manager.ManagerProxy;

public final class DefaultNamedActivityBuilder extends DefaultActivityBuilder
{
	/**
	 * Implementation of the <code>BuilderFactory</code> to create a
	 * <code>DefaultNamedActivityBuilder</code>.
	 */

	private static class Factory implements BuilderFactory<Activity, ActivityBuilder>
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
		public ActivityBuilder create (final ManagerProxy<Activity> manager)
		{
			return new DefaultNamedActivityBuilder (manager);
		}
	}

	/** The logger */
	private final Logger log;

	/** The name of the <code>Activity</code> */
	private String name;

	public DefaultNamedActivityBuilder (final ManagerProxy<Activity> manager)
	{
		super (manager);
		this.log = LoggerFactory.getLogger (DefaultNamedActivityBuilder.class);
	}

	@Override
	public Activity build ()
	{
		return null;
	}

	@Override
	public void clear ()
	{
		this.log.trace ("Reseting the builder");

		super.clear ();
		this.name = null;
	}

	public String getName ()
	{
		return this.name;
	}

	public void setName (final String name)
	{
		if (name == null)
		{
			this.log.error ("Attempting to set a NULL name");
			throw new NullPointerException ();
		}

		this.name = name;
	}
}
