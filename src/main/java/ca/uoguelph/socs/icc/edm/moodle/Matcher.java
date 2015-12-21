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

package ca.uoguelph.socs.icc.edm.moodle;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

/**
 * Determine if a Moodle log entry references a <code>SubActivity</code>.
 * Moodle does not have a field in its log pointing to any
 * <code>SubActivity</code> instances that may be associated with the log entry.
 * When a log entry refers to a <code>SubActivity</code> Moodle stores the ID of
 * the <code>SubActivity</code> in the <code>info</code> field.  The
 * <code>info</code> field is a text field that Moodle uses to store various
 * pieces of (undocumented) information related to the log entry.  Before
 * attempting to extract a <code>SubActivity</code> ID from the info field the
 * program must determine if the log entry is referring to a
 * <code>SubActivity</code>.
 * <p>
 * For most of the <code>Activity</code> classes, certain certain values of the
 * associated <code>Action</code> will indicate that the log entry is referring
 * to a specific <code>SubActivity</code> class.  The
 * <code>Activity</code>/<code>Action</code> combinations which refer to a
 * specific <code>SubActivity</code> class are encoded into the
 * <code>log_display</code> table in the Moodle database.
 * <p>
 * A few <code>Activity</code> classes do not use the associated
 * <code>Action</code> instance to indicate that the <code>SubActivity</code> is
 * being referenced.  For these classes the URL must be inspected to determine
 * if a <code>SubActivity</code> is being referenced.
 * <p>
 * The implementations this interface contain a reference to an
 * <code>Activity</code> and a <code>SubActivity</code> class, along with the
 * information required to determine if the <code>SubActivity</code> class is
 * being referenced by the log entry.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public interface Matcher
{
	/**
	 * Get the <code>Activity</code> class.
	 *
	 * @return The <code>Activity</code> class
	 */

	public abstract Class<? extends Activity> getActivityClass ();

	/**
	 * Get the <code>SubActivity</code> class.
	 *
	 * @return The <code>SubActivity</code> class
	 */

	public abstract Class<? extends SubActivity> getSubActivityClass ();

	/**
	 * Determine if the specified <code>Action</code> or URL references a
	 * <code>SubActivity</code>.
	 *
	 * @param  action The name of the <code>Action</code>, not null
	 * @param  url    The URL, not null
	 * @return        <code>true</code>, <code>false</code> otherwise
	 */

	public abstract boolean matches (String action, String url);
}
