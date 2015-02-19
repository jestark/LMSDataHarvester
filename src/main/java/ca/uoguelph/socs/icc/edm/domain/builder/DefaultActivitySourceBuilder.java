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

import ca.uoguelph.socs.icc.edm.domain.AbstractBuilder;
import ca.uoguelph.socs.icc.edm.domain.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivitySourceBuilder;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;

public final class DefaultActivitySourceBuilder extends AbstractBuilder<ActivitySource> implements ActivitySourceBuilder
{
	private static class Factory implements BuilderFactory<ActivitySourceBuilder>
	{
		public ActivitySourceBuilder create (DomainModel model)
		{
			return new DefaultActivitySourceBuilder ((AbstractManager<ActivitySource>) model.getActivitySourceManager ());
		}
	}

	/** The logger */
	private final Logger log;

	/** <code>ElementFactory</code> to build the activity source */
	private final ActivitySourceElementFactory factory;

	/** The name of the Activity Source */
	private String name;

	/**
	 * static initializer to register the <code>DefaultActivitySourceBuilder</code> with the
	 * factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (ActivitySource.class, ActivitySourceBuilder.class, DefaultActivitySourceBuilder.class, new Factory ());
	}

	protected DefaultActivitySourceBuilder (AbstractManager<ActivitySource> manager)
	{
		super (manager);

		this.factory = null;
		this.log = LoggerFactory.getLogger (DefaultActivitySourceBuilder.class);
	}

	@Override
	protected ActivitySource build ()
	{
		if (this.name == null)
		{
			this.log.error ("Can not build: The activity source's name is not set");
			throw new IllegalStateException ("name not set");		
		}

		return this.factory.create (this.name);
	}

	@Override
	public void clear ()
	{
		this.name = null;
	}

	@Override
	public String getName ()
	{
		return this.name;
	}

	@Override
	public ActivitySourceBuilder setName (String name)
	{
		if (name == null)
		{
			this.log.error ("name is NULL");
			throw new NullPointerException ("name is NULL");
		}

		this.name = name;

		return this;
	}
}
