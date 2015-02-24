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

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;

import ca.uoguelph.socs.icc.edm.domain.manager.AbstractManager;

public abstract class DefaultActivityBuilder extends AbstractBuilder<Activity> implements ActivityBuilder
{
	/** The logger */
	private final Logger log;

	/** The type of the Activity */
	private ActivityType type;

	/** The course which uses the Activity */
	private Course course;

	/** Is the activity stealth? */
	private Boolean stealth;

	public DefaultActivityBuilder (AbstractManager<Activity> manager)
	{
		super (manager);

		this.log = LoggerFactory.getLogger (DefaultActivityBuilder.class);
	}

	@Override
	protected Activity build ()
	{
		if (this.course == null)
		{
			this.log.error ("Can not build: The activity's course is not set");
			throw new IllegalStateException ("course not set");
		}

		if (this.stealth == null)
		{
			this.log.error ("Can not build: The activity's stealth flag is NULL");
			throw new IllegalStateException ("stealth flag is NULL");
		}

		return null; //this.factory.create (type, course, stealth);
	}

	@Override
	public void clear ()
	{
		this.course = null;
		this.stealth = null;
		this.type = null;
	}

	@Override
	public final ActivityType getActivityType ()
	{
		return this.type;
	}

	@Override
	public final Course getCourse ()
	{
		return this.course;
	}

	@Override
	public final ActivityBuilder setCourse (final Course course)
	{
		if (course == null)
		{
			this.log.error ("Course is NULL");
			throw new NullPointerException ("Course is NULL");
		}

		this.course = course;

		return this;
	}

	@Override
	public final Boolean getStealth ()
	{
		return this.stealth;
	}

	@Override
	public final ActivityBuilder setStealth (final Boolean stealth)
	{
		if (stealth == null)
		{
			this.log.error ("stealth is NULL");
			throw new NullPointerException ("stealth is NULL");
		}

		this.stealth = stealth;

		return this;
	}
}
