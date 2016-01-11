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

import java.util.Objects;
import java.util.regex.Pattern;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.element.MoodleLogData;

/**
 * Implementation of the <code>Matcher</code> using the URL.  This
 * <code>Matcher</code> determines if a log entry if referring to a
 * <code>SubActivity</code> by matching a regular expression against the URL.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class URLMatcher implements Matcher
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
	 * Compare two <code>URLMatcher</code> instances to determine if they are
	 * equal.
	 *
	 * @param  obj The <code>URLMatcher</code> instance to compare to the one
	 *             represented by the called instance
	 * @return     <code>true</code> if the two <code>URLMatcher</code>
	 *             instances are equal, <code>false</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof URLMatcher)
			&& Objects.equals (this.activity, ((URLMatcher) obj).activity)
			&& Objects.equals (this.subActivity, ((URLMatcher) obj).subActivity)
			&& Objects.equals (this.pattern, ((URLMatcher) obj).pattern);
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>URLMatcher</code>
	 * instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.activity, this.subActivity, this.pattern);
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>URLMatcher</code> instance.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>URLMatcher</code> instance
	 */

	@Override
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("activity", this.activity)
			.add ("subActivity", this.subActivity)
			.add ("pattern", this.pattern)
			.toString ();
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
	 * @param  entry The <code>MoodleLogData</code> to process, not null
	 * @return        <code>true</code>, <code>false</code> otherwise
	 */

	@Override
	public boolean matches (final MoodleLogData entry)
	{
		return this.pattern.matcher (entry.getUrl ()).matches ();
	}
}
