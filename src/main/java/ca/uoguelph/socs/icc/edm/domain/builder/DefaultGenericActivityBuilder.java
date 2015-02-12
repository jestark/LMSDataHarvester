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

import ca.uoguelph.socs.icc.edm.domain.AbstractBuilder;
import ca.uoguelph.socs.icc.edm.domain.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;

public final class DefaultGenericActivityBuilder extends DefaultActivityBuilder
{
	private static final class Factory implements BuilderFactory<ActivityBuilder>
	{
		public ActivityBuilder create (DomainModel model)
		{
			return new DefaultGenericActivityBuilder ((AbstractManager<Activity>) model.getActivityManager ());
		}
	}

	/** The logger */
	private final Logger log;

	public DefaultGenericActivityBuilder (AbstractManager<Activity> manager)
	{
		super (manager);

		this.log = LoggerFactory.getLogger (DefaultGenericActivityBuilder.class);
	}

	@Override
	protected Activity build ()
	{
		this.log.trace ("Building Generic Activity");

		return super.build (); 
	}
}
