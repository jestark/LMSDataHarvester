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

import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

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
	 * Constants representing all of the properties of an <code>SubActivity</code>.
	 * A <code>Property</code> represents a piece of data contained within the
	 * <code>SubActivity</code> instance.
	 */

	public static class Properties extends Activity.Properties
	{
		/** The parent <code>Activity</code> */
		public static final Property<Activity> PARENT = Property.getInstance (SubActivity.class, Activity.class, "parent", false, true);
	}

	/**
	 * Constants representing all of the selectors of an
	 * <code>SubActivity</code>.  A <code>Selector</code> represents the
	 * <code>Set</code> of <code>Property</code> instances used to load an
	 * <code>Grade</code> from the <code>DataStore</code>.
	 */

	public static class Selectors extends Element.Selectors {}

	/**
	 * Get the parent <code>Activity</code> instance for the
	 * <code>SubActivity</code>.
	 *
	 * @return The parent <code>Activity</code>
	 */

	public abstract Activity getParent ();
}
