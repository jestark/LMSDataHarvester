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
import ca.uoguelph.socs.icc.edm.domain.ActivityGroup;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMemberBuilder;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;

import ca.uoguelph.socs.icc.edm.domain.manager.AbstractManager;

public final class DefaultActivityGroupMemberBuilder extends AbstractBuilder<Activity> implements ActivityGroupMemberBuilder
{
	private static final class Factory implements BuilderFactory<ActivityGroupMemberBuilder>
	{
		public ActivityGroupMemberBuilder create (DomainModel model)
		{
			return new DefaultActivityGroupMemberBuilder ((AbstractManager<Activity>) model.getActivityManager ());
		}
	}

	/** The logger */
	private final Logger log;

	/** The parent <code>Activity</code> */
	private ActivityGroup parent;

	/** The name of the <code>Activity</code> */
	private String name;

	public DefaultActivityGroupMemberBuilder (AbstractManager<Activity> manager)
	{
		super (manager);
		this.log = LoggerFactory.getLogger (DefaultActivityGroupMemberBuilder.class);
	}

	@Override
	protected Activity build ()
	{
		return null;
	}

	@Override
	public void clear ()
	{
		this.log.trace ("Reseting the builder");

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

	public ActivityGroup getParent ()
	{
		return this.parent;
	}
}
