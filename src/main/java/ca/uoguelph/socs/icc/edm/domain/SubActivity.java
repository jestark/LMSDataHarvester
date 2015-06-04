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

import ca.uoguelph.socs.icc.edm.domain.Activity;

/**
 * A representation of a child element in a hierarchy of <code>Activity</code>
 * instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public interface SubActivity extends Activity
{
	/**
	 * Enumeration of all of the properties of an <code>SubActivity</code>.
	 * Properties represent the data contained within the
	 * <code>SubActivity</code> instance.
	 */

	public static enum Properties { ID, NAME, PARENT };

	/**
	 * Get the parent <code>Activity</code> instance for the
	 * <code>SubActivity</code>.
	 *
	 * @return The parent <code>Activity</code>
	 */

	public abstract Activity getParent ();
}
