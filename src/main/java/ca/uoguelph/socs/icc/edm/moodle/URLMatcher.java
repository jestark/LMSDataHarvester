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

import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

/**
 * Implementation of the <code>Matcher</code> using the URL.  This
 * <code>Matcher</code> determines if a log entry if referring to a
 * <code>SubActivity</code> by matching a regular expression against the URL.
 *
 * @author  James E. Stark
 * @version 1.0
 */

final class URLMatcher implements Matcher
{
	/** The <code>Activity</code> class */
	final Class<? extends Activity> activity;

	/** The associated <code>SubActivity</code> class */
	final Class<? extends SubActivity> subActivity;

	/** The regular expression to match against the URL */
	final Pattern pattern;

	/**
	 * Create a new <code>URLMatcher</code> instance.
	 *
	 * @param  activity    The <code>Activity</code>, not null
	 * @param  subActivity The associated <code>SubActivity</code>, not null
	 * @param  pattern     Regular expression to match against the URL, not null
 	 * @return             The <code>URLMatcher</code>
	 */

	public static Matcher create (
			final Class<? extends Activity> activity,
			final Class<? extends SubActivity> subActivity,
			final String pattern)
	{
		Preconditions.checkNotNull (activity, "activity");
		Preconditions.checkNotNull (subActivity, "subActivity");
		Preconditions.checkNotNull (pattern, "pattern");

		return new URLMatcher (activity, subActivity, pattern);
	}

	/**
	 * Create a new <code>ActionMatcher</code> instance.
	 *
	 * @param  activity    The <code>Activity</code>, not null
	 * @param  subActivity The associated <code>SubActivity</code>, not null
	 * @param  pattern     Regular expression to match against the URL, not null
	 */

	private URLMatcher (
			final Class<? extends Activity> activity,
			final Class<? extends SubActivity> subActivity,
			final String pattern)
	{
		assert activity != null : "activity is NULL";
		assert subActivity != null : "subActivity is NULL";
		assert pattern != null : "pattern is NULL";

		this.activity = activity;
		this.subActivity = subActivity;
		this.pattern = Pattern.compile (pattern);
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
	 * Determine if the specified URL references a <code>SubActivity</code>.
	 *
	 * @param  action The name of the <code>Action</code>, not null
	 * @param  url    The URL, not null
	 * @return        <code>true</code>, <code>false</code> otherwise
	 */

	@Override
	public boolean matches (final String action, final String url)
	{
		return this.pattern.matcher (url).matches ();
	}
}
