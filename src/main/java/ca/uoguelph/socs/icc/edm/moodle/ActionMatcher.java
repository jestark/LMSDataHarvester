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

import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.element.MoodleLogData;

/**
 * Implementation of the <code>Matcher</code> using the associated
 * <code>Action</code> instance.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class ActionMatcher implements Matcher
{
	/** The <code>Activity</code> class */
	final Class<? extends Activity> activity;

	/** The <code>SubActivity</code> class*/
	final Class<? extends SubActivity> subActivity;

	/** The name of the <code>Action</code> */
	final String action;

	/**
	 * Create a new <code>ActionMatcher</code> instance.
	 *
	 * @param  activity    The <code>Activity</code>, not null
	 * @param  subActivity The associated <code>SubActivity</code>, not null
	 * @param  action      The name of the <code>Action</code>, not null
 	 * @return             The <code>ActionMatcher</code>
	 */

	public static Matcher create (final Class<? extends Activity> activity, final Class<? extends SubActivity> subActivity, final String action)
	{
		Preconditions.checkNotNull (activity, "activity");
		Preconditions.checkNotNull (subActivity, "subActivity");
		Preconditions.checkNotNull (action, "action");

		return new ActionMatcher (activity, subActivity, action);
	}

	/**
	 * Create a new <code>ActionMatcher</code> instance.
	 *
	 * @param  activity    The <code>Activity</code>, not null
	 * @param  subActivity The associated <code>SubActivity</code>, not null
	 * @param  action      The name of the <code>Action</code>, not null
	 */

	private ActionMatcher (final Class<? extends Activity> activity, final Class<? extends SubActivity> subActivity, final String action)
	{
		assert activity != null : "activity is NULL";
		assert subActivity != null : "subActivity is NULL";
		assert action != null : "action is NULL";

		this.activity = activity;
		this.subActivity = subActivity;
		this.action = action;
	}

	/**
	 * Get the <code>Activity</code> class.
	 *
	 * @return The <code>Activity</code> class
	 */

	@Override
	public Class<? extends Activity> getActivityClass ()
	{
		return this.activity;
	}

	/**
	 * Get the <code>SubActivity</code> class.
	 *
	 * @return The <code>SubActivity</code> class
	 */

	@Override
	public Class<? extends SubActivity> getSubActivityClass ()
	{
		return this.subActivity;
	}

	/**
	 * Determine if the specified <code>Action</code> references a
	 * <code>SubActivity</code>.
	 *
	 * @param  entry The <code>MoodleLogData</code> to process, not null
	 * @return        <code>true</code>, <code>false</code> otherwise
	 */

	@Override
	public boolean matches (final MoodleLogData entry)
	{
		return this.action.equals (entry.getActionName ());
	}
}
