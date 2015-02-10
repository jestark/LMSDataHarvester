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

package ca.uoguelph.socs.icc.edm.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.idgenerator.IdGenerator;

public abstract class AbstractBuilder<T extends Element>
{
	/** The manager (used to add new instances to the model) */
	private final AbstractManager<T> manager;

	/** The Logger */
	private final Logger log;

	protected AbstractBuilder (AbstractManager<T> manager)
	{
		this.log = LoggerFactory.getLogger (AbstractBuilder.class);
		
		this.manager = manager;
	}

	public final T create ()
	{
		this.log.trace ("Create an instance of the Element");

		return this.build ();
	}

	protected abstract T build ();

	public abstract void clear ();
}
