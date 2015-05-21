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

import ca.uoguelph.socs.icc.edm.domain.builder.AbstractBuilder;
import ca.uoguelph.socs.icc.edm.domain.core.AbstractActivity;

public final class BuilderFactory
{
	private final DomainModel model;

	protected BuilderFactory (final DomainModel model)
	{
		assert model != null : "model is NULL";

		this.model = model;
	}

	/**
	 * Get an <code>ElementBuilder</code> instance of the specified type for
	 * the specified <code>Element</code> implementation class.
	 *
	 * @param  <B>     The type of <code>ElementBuilder</code> to be returned
	 * @param  builder The <code>ElementBuilder</code> interface class, not null
	 * @param  element The <code>Element</code> implementation class, not null
	 *
	 * @return         The <code>ElementBuilder</code> instance
	 */

	protected final <T extends ElementBuilder<U>, U extends Element> T getBuilder (final Class<T> builder, final Class<? extends U> element)
	{
		assert builder != null : "builder is NULL";
		assert element != null : "element is NULL";

		return AbstractBuilder.getInstance (element, null);
	}

	/**
	 * Get an <code>ElementBuilder</code> instance of the specified type.
	 *
	 * @param  <B>     The type of <code>ElementBuilder</code> to be returned
	 * @param  builder The <code>ElementBuilder</code> interface class, not null
	 *
	 * @return         The <code>ElementBuilder</code> instance
	 */

	protected final <T extends ElementBuilder<U>, U extends Element> T getBuilder (final Class<T> builder)
	{
		assert builder != null : "builder is NULL";

		return this.getBuilder (builder, null);
	}

	/**
	 * Get an instance of the <code>ActionBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @return An <code>ActionBuilder</code> instance
	 */

	public ActionBuilder getActionBuilder ()
	{
		return this.getBuilder (ActionBuilder.class);
	}

	/**
	 * Get an instance of the <code>ActivityBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @param  <T>     The type of <code>ActivityBuilder</code>
	 * @param  builder The <code>ActivityBuilder</code> interface of the
	 *                 builder to be returned, not null
	 * @param  type    The <code>ActivityType</code> of the
	 *                 <code>Activity</code> to be created by the
	 *                 <code>ActivityBuilder</code>
	 *
	 * @return         An <code>ActivityBuilder</code> instance
	 */

	public <T extends ActivityBuilder> T getActivityBuilder (final Class<T> builder, final ActivityType type)
	{
		if (builder == null)
		{
			throw new NullPointerException ();
		}

		if (type == null)
		{
			throw new NullPointerException ();
		}

		return this.getBuilder (builder, AbstractActivity.getActivityClass (type));
	}

	/**
	 * Get an instance of the <code>ActivityBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @param  type The <code>ActivityType</code> of the <code>Activity</code>
	 *              to be created by the <code>ActivityBuilder</code>
	 *
	 * @return      An <code>ActivityBuilder</code> instance
	 */

	public ActivityBuilder getActivityBuilder (final ActivityType type)
	{
		if (type == null)
		{
			throw new NullPointerException ();
		}

		return this.getBuilder (ActivityBuilder.class);
	}

	/**
	 * Get an instance of the <code>SubActivityBuilder</code> suitable for use
	 * with the specified <code>Activity</code>.
	 *
	 * @param  <T>      The type of <code>SubActivityBuilder</code> to be
	 *                  returned
	 * @param  builder  The <code>SubActivityBuilder</code> interface of
	 *                  the builder to be returned, not null
	 * @param  activity The <code>Activity</code> instance to which the new
	 *                  <code>SubActivity</code> instance is to be assigned
	 *
	 * @return          A <code>SubActivityBuilder</code> instance
	 */

	public <T extends SubActivityBuilder> T getSubActivityBuilder (Class<T> builder, Activity activity)
	{
		if (builder == null)
		{
			throw new NullPointerException ();
		}

		if (activity == null)
		{
			throw new NullPointerException ();
		}

		return this.getBuilder (builder, AbstractActivity.getSubActivityClass (activity.getClass ()));
	}

	/**
	 * Get an instance of the <code>SubActivityBuilder</code> suitable for use
	 * with the specified <code>Activity</code>.
	 *
	 * @param  activity The <code>Activity</code> instance to which the new
	 *                  <code>SubActivity</code> instance is to be assigned
	 *
	 * @return          A <code>SubActivityBuilder</code> instance
	 */

	public SubActivityBuilder getSubActivityBuilder (final Activity activity)
	{
		if (activity == null)
		{
			throw new NullPointerException ();
		}

		return this.getBuilder (SubActivityBuilder.class);
	}

	/**
	 * Get an instance of the <code>ActivitySourceBuilder</code> interface,
	 * suitable for use with the <code>DataStore</code>.
	 *
	 * @return An <code>ActivitySourceBuilder</code> instance
	 */

	public ActivitySourceBuilder getActivitySourceBuilder ()
	{
		return this.getBuilder (ActivitySourceBuilder.class);
	}

	/**
	 * Get an instance of the <code>ActivityTypeBuilder</code> interface,
	 * suitable for use with the <code>DataStore</code>.
	 *
	 * @return An <code>ActivityTypeBuilder</code> instance
	 */

	public ActivityTypeBuilder getActivityTypeBuilder ()
	{
		return this.getBuilder (ActivityTypeBuilder.class);
	}

	/**
	 * Get an instance of the <code>CourseBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @return An <code>CourseBuilder</code> instance
	 */

	public CourseBuilder getCourseBuilder ()
	{
		return this.getBuilder (CourseBuilder.class);
	}

	/**
	 * Get an instance of the <code>EnrolmentBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @return An <code>EnrolmentBuilder</code> instance
	 */

	public EnrolmentBuilder getEnrolmentBuilder ()
	{
		return this.getBuilder (EnrolmentBuilder.class);
	}

	/**
	 * Get an instance of the <code>GradeBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @return An <code>GradeBuilder</code> instance
	 */

	public GradeBuilder getGradeBuilder ()
	{
		return this.getBuilder (GradeBuilder.class);
	}

	/**
	 * Get an instance of the <code>LogEntryBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @return An <code>LogEntryBuilder</code> instance
	 */

	public LogEntryBuilder getLogEntryBuilder ()
	{
		return this.getBuilder (LogEntryBuilder.class);
	}

	/**
	 * Get an instance of the <code>NetworkBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @return An <code>NetworkBuilder</code> instance
	 */

	public NetworkBuilder getNetworkBuilder ()
	{
		return this.getBuilder (NetworkBuilder.class);
	}

	/**
	 * Get an instance of the <code>RoleBuilder</code> interface, suitable for
	 * use with the <code>DataStore</code>.
	 *
	 * @return An <code>RoleBuilder</code> instance
	 */

	public RoleBuilder getRoleBuilder ()
	{
		return this.getBuilder (RoleBuilder.class);
	}

	/**
	 * Get an instance of the <code>UserBuilder</code> interface, suitable for
	 * use with the <code>DataStore</code>.
	 *
	 * @return An <code>UserBuilder</code> instance
	 */

	public UserBuilder getUserBuilder ()
	{
		return this.getBuilder (UserBuilder.class);
	}
}
