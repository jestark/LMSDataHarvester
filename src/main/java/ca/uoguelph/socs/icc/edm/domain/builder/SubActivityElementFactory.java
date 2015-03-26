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

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

/**
 * Factory interface to create new <code>SubActivity</code> instances.
 * Implementations of this interface provide the functionality required to
 * create new instances of a class implementing the <code>SubActivity</code>
 * interface.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.SubActivityBuilder
 */

public interface SubActivityElementFactory extends AbstractActivityElementFactory
{
	/**
	 * Create a new <code>SubActivity</code> instance.
	 *
	 * @param  activity The parent <code>Activity</code>, not null
	 * @param  name     The name of the <code>SubActivity</code>, not null
	 *
	 * @return          The new <code>SubActivity</code> instance
	 */

	public abstract SubActivity create (Activity parent, String name);
}
