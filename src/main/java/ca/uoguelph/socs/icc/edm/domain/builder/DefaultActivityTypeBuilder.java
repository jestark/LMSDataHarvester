/* Copyright (C) 2014 James E. Stark
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.AbstractBuilder;
import ca.uoguelph.socs.icc.edm.domain.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.ActivityTypeBuilder;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.factory.ActivityTypeFactory;

public final class DefaultActivityTypeBuilder extends AbstractBuilder<ActivityType> implements ActivityTypeBuilder
{
	private static class DefaultActivityTypeBuilderFactory implements BuilderFactory<ActivityTypeBuilder>
	{
		public ActivityTypeBuilder create (DomainModel model)
		{
			return new DefaultActivityTypeBuilder ((AbstractManager<ActivityType>) model.getActivityTypeManager ());
		}
	}

	/** The logger */
	private final Log log;

	/** <code>ElementFactory</code> to build the activity type */
	private final ActivityTypeElementFactory factory;

	/** The source of the <code>ActivityType</code> */
	private ActivitySource source;

	/** The name of the <code>ActivityType</code> */
	private String name;

	protected DefaultActivityTypeBuilder (AbstractManager<ActivityType> manager)
	{
		super (manager);

		this.factory = null;
		this.log = LogFactory.getLog (DefaultActivityTypeBuilder.class);
	}

	@Override
	protected ActivityType build ()
	{
		if (this.source == null)
		{
			this.log.error ("Can not build: The activity type's activity source is not set");
			throw new IllegalStateException ("activity source not set");
		}

		if (this.name == null)
		{
			this.log.error ("Can not build: The activity type's name is not set");
			throw new IllegalStateException ("name not set");
		}

		return this.factory.create (this.source, this.name);
	}

	@Override
	public void clear ()
	{
		this.name = null;
		this.source = null;
	}

	@Override
	public String getName ()
	{
		return this.name;
	}

	@Override
	public ActivityTypeBuilder setName (String name)
	{
		if (name == null)
		{
			this.log.error ("name is NULL");
			throw new NullPointerException ("name is NULL");
		}

		this.name = name;

		return this;
	}

	@Override
	public ActivitySource getActivitySource ()
	{
		return this.source;
	}

	@Override
	public ActivityTypeBuilder setActivitySource (ActivitySource source)
	{
		if (source == null)
		{
			this.log.error ("source is NULL");
			throw new NullPointerException ("source is NULL");
		}

		this.name = name;

		return this;
	}
}