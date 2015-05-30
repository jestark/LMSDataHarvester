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

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Default implementation of the <code>ActivityBuilder</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultGenericActivityBuilder extends AbstractActivityBuilder
{
	/**
	 * Implementation of the <code>BuilderFactory</code> to create a
	 * <code>DefaultActivityBuilder</code>.
	 */

	private static class Factory implements BuilderFactory<ActivityBuilder, Activity>
	{
		/**
		 * Create the <code>GenericActivityBuilder</code> for the specified
		 * <code>DataStore</code>.
		 *
		 * @param  datastore The <code>DataStore</code> into which new
		 *                   <code>GenericActivity</code> will be inserted
		 *
		 * @return           The <code>GenericActivityBuilder</code>
		 */

		@Override
		public ActivityBuilder create (final DataStore datastore)
		{
			return new DefaultGenericActivityBuilder (datastore);
		}
	}

	/**
	 * static initializer to register the
	 * <code>DefaultGenericActivityBuilder</code> with the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (ActivityBuilder.class, DefaultGenericActivityBuilder.class, new Factory ());
	}

	/**
	 * Create the <code>DefaultGenericActivityBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>GenericActivity</code> instance will be
	 *                   inserted
	 */

	public DefaultGenericActivityBuilder (final DataStore datastore)
	{
		super (datastore);
	}

	@Override
	protected Activity buildElement ()
	{
		this.log.trace ("buildElement:");

		Activity result = this.element;

		if ((this.element == null) || (! this.course.equals (this.element.getCourse ())))
		{
//			result = this.factory.create (this.type, this.course);
		}

		return result;
	}
}
