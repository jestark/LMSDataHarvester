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

package ca.uoguelph.socs.icc.edm.domain;

public final class LoaderFactory
{
	private final DomainModel model;

	protected LoaderFactory (final DomainModel model)
	{
		assert model != null : "model is NULL";

		this.model = model;
	}

	private <T extends ElementLoader<U>, U extends Element> T getLoader (final Class<U> element, final Class<T> loader)
	{
		return null;
	}

	/**
	 * Get the <code>ActionLoader</code> for the <code>DomainModel</code>.
	 *
	 * @return A reference to the <code>ActionLoader</code>
	 */

	public ActionLoader getActionLoader ()
	{
		return this.getLoader (Action.class, ActionLoader.class);
	}

	/**
	 * Get the <code>ActivityLoader</code> for the <code>DomainModel</code>.
	 *
	 * @return A reference to the <code>ActivityLoader</code>
	 */

	public ActivityLoader getActivityLoader ()
	{
		return this.getLoader (Activity.class, ActivityLoader.class);
	}

	/**
	 * Get the <code>ActivitySourceLoader</code> for the
	 * <code>DomainModel</code>.
	 *
	 * @return A reference to the <code>ActivitySourceLoader</code>
	 */

	public ActivitySourceLoader getActivitySourceLoader ()
	{
		return this.getLoader (ActivitySource.class, ActivitySourceLoader.class);
	}

	/**
	 * Get the <code>ActivityTypeLoader</code> for the
	 * <code>DomainModel</code>.
	 *
	 * @return A reference to the <code>ActivityTypeLoader</code>
	 */

	public ActivityTypeLoader getActivityTypeLoader ()
	{
		return this.getLoader (ActivityType.class, ActivityTypeLoader.class);
	}

	/**
	 * Get the <code>CourseLoader</code> for the <code>DomainModel</code>.
	 *
	 * @return A reference to the <code>CourseLoader</code>
	 */

	public CourseLoader getCourseLoader ()
	{
		return this.getLoader (Course.class, CourseLoader.class);
	}

	/**
	 * Get the <code>EnrolmentLoader</code> for the <code>DomainModel</code>.
	 *
	 * @return A reference to the <code>EnrolmentLoader</code>
	 */

	public EnrolmentLoader getEnrolmentLoader ()
	{
		return this.getLoader (Enrolment.class, EnrolmentLoader.class);
	}

	/**
	 * Get the <code>LogEntryLoader</code> for the <code>DomainModel</code>.
	 *
	 * @return A reference to the <code>LogEntryLoader</code>
	 */

	public LogEntryLoader getLogEntryLoader ()
	{
		return this.getLoader (LogEntry.class, LogEntryLoader.class);
	}

	/**
	 * Get the <code>RoleLoader</code> for the <code>DomainModel</code>.
	 *
	 * @return A reference to the <code>RoleLoader</code>
	 */

	public RoleLoader getRoleLoader ()
	{
		return this.getLoader (Role.class, RoleLoader.class);
	}

	/**
	 * Get the <code>UserLoader</code> for the <code>DomainModel</code>.
	 *
	 * @return A reference to the <code>UserLoader</code>
	 */

	public UserLoader getUserLoader ()
	{
		return this.getLoader (User.class, UserLoader.class);
	}
}
