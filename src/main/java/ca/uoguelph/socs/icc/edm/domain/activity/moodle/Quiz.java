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

package ca.uoguelph.socs.icc.edm.domain.activity.moodle;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;

import ca.uoguelph.socs.icc.edm.domain.builder.DefaultNamedActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.NamedActivityElementFactory;

import ca.uoguelph.socs.icc.edm.domain.core.ActivityInstance;
import ca.uoguelph.socs.icc.edm.domain.core.GenericNamedActivity;

/**
 * Implementation of the <code>Activity</code> interface for the moodle/Quiz
 * <code>ActivitySource</code>/<code>ActivityType</code>.  It is expected that
 * this class will be accessed though the <code>Activity</code> interface,
 * along with the relevant manager, and builder.  See the <code>Activity</code>
 * interface documentation for details.
 * <p>
 * This class was generated from the <code>NamedActivity</code> template, with
 * the following values:
 * <p>
 * <ul>
 * <li>ActivitySource = moodle
 * <li>ActivityType   = Quiz
 * <li>ClassName      = Quiz
 * <li>Builder        = DefaultNamedActivityBuilder
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.1
 */

public class Quiz extends GenericNamedActivity
{
	/**
	 * Implementation of the <code>NamedActivityElementFactory</code>.  Allows the
	 * builders to create instances of <code>Quiz</code>.
	 */

	private static final class Factory extends ActivityInstance.Factory implements NamedActivityElementFactory
	{
		/**
		 * Create a new <code>Activity</code> instance.
		 *
		 * @param  type    The <code>ActivityType</code> of the
		 *                 <code>Activity</code>, not null
		 * @param  course  The <code>Course</code> which is associated with the
		 *                 <code>Activity</code> instance, not null
		 * @param  name    The name of the <code>Activity</code>, not null
		 *
		 * @return         The new <code>Activity</code> instance
		 */

		public Activity create (final ActivityType type, final Course course, final String name)
		{
			assert type != null : "type is NULL";
			assert course != null : "course is NULL";
			assert name != null : "name is NULL";

			return new Quiz (type, course, name);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Register the <code>Quiz</code> with the factories on initialization.
	 */

	static
	{
		GenericNamedActivity.registerActivity (Quiz.class, DefaultNamedActivityBuilder.class, NamedActivityElementFactory.class, new Factory (), "moodle", "Quiz");
	}

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	public Quiz ()
	{
		super ();
	}

	/**
	 * Create the <code>Activity</code> instance.
	 *
	 * @param  type    The <code>ActivityType</code> of the
	 *                 <code>Activity</code>, not null
	 * @param  course  The <code>Course</code> which is associated with the
	 *                 <code>Activity</code> instance, not null
	 * @param  name    The name of the <code>Activity</code>, not null
	 */

	public Quiz (final ActivityType type, final Course course, final String name)
	{
		super (type, course, name);
	}
}
