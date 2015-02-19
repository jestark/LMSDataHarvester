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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.builder.BuilderFactory;

import ca.uoguelph.socs.icc.edm.domain.factory.MappedBuilderFactory;

public abstract class AbstractBuilder<T extends Element>
{
	/** The manager (used to add new instances to the model) */
	private final AbstractManager<T> manager;

	/** The Logger */
	private final Log log;

	protected static <T extends Element, X extends ElementBuilder<T>> void registerBuilder (Class<T> element, Class<X> builder, Class<? extends X> impl, BuilderFactory<X> factory)
	{
		(MappedBuilderFactory.getInstance (element, builder)).registerClass (impl, factory);
	}

	protected AbstractBuilder (AbstractManager<T> manager)
	{
		this.log = LogFactory.getLog (AbstractBuilder.class);
		
		this.manager = manager;
	}

	public final T create ()
	{
		return this.build ();
	}

	protected abstract T build ();

	public abstract void clear ();
}
