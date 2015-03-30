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

package ca.uoguelph.socs.icc.edm.domain.builder;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.CourseManager;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.EnrolmentBuilder;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.RoleManager;
import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.UserManager;

import ca.uoguelph.socs.icc.edm.domain.core.UserEnrolmentData;

import ca.uoguelph.socs.icc.edm.domain.manager.ManagerProxy;

public final class DefaultEnrolmentBuilder extends AbstractBuilder<Enrolment, EnrolmentElementFactory> implements EnrolmentBuilder
{
	/**
	 * Implementation of the <code>BuilderFactory</code> to create a
	 * <code>DefaultEnrolmentBuilder</code>.
	 */

	private static class Factory implements BuilderFactory<Enrolment, EnrolmentBuilder>
	{
		/**
		 * Create the <code>EnrolmentBuilder</code>.  The supplied
		 * <code>ManagerProxy</code> will be used by the builder to access the
		 * <code>EnrolmentManager</code> to perform operations on the
		 * <code>DataStore</code>.
		 *
		 * @param  manager The <code>ManagerProxy</code> used to the
		 *                 <code>EnrolmentManager</code> instance, not null
		 *
		 * @return         The <code>EnrolmentBuilder</code>
		 */

		@Override
		public EnrolmentBuilder create (final ManagerProxy<Enrolment> manager)
		{
			return new DefaultEnrolmentBuilder (manager);
		}
	}

	/** The course in which the user is enrolled */
	private Course course;

	/** The user's final grade in the course */
	private Integer grade;

	/** The user's role in the course */
	private Role role;

	/** The user associated with the enrolment */
	private User user;

	/** Flag indicating if the user has given permission to use the data */
	private Boolean usable;

	/**
	 * static initializer to register the <code>DefaultEnrolmentBuilder</code>
	 * with the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (EnrolmentBuilder.class, DefaultEnrolmentBuilder.class, new Factory ());
	}

	/**
	 * Create the <code>DefaultEnrolmentBuilder</code>.
	 *
	 * @param  manager The <code>EnrolmentManager</code> which the
	 *                 <code>EnrolmentBuilder</code> will use to operate on the
	 *                 <code>DataStore</code>
	 */

	protected DefaultEnrolmentBuilder (final ManagerProxy<Enrolment> manager)
	{
		super (Enrolment.class, EnrolmentElementFactory.class, manager);

		this.clear ();
	}

	@Override
	protected Enrolment buildElement ()
	{
		if (this.user == null)
		{
			this.log.error ("Can not build: The enrolment's user is not set");
			throw new IllegalStateException ("user not set");
		}

		if (this.role == null)
		{
			this.log.error ("Can not build: The role's course is not set");
			throw new IllegalStateException ("role not set");
		}

		if (this.course == null)
		{
			this.log.error ("Can not build: The enrolment's course is not set");
			throw new IllegalStateException ("course not set");
		}

		Enrolment result = this.element;

		if ((this.element == null) || ((this.user.equals (((UserEnrolmentData) this.element).getUser ())) && (this.role.equals (this.element.getRole ())) && (this.course.equals (this.element.getCourse ()))))
		{
			result = this.factory.create (this.user, this.course, this.role, this.grade, this.usable);
		}
		else
		{
			this.factory.setUsable (this.element, this.usable);
			this.factory.setFinalGrade (this.element, this.grade);
		}

		return result;
	}

	@Override
	protected void postInsert ()
	{
		CourseElementFactory cfactory = AbstractBuilder.getFactory (CourseElementFactory.class, this.course.getClass ());
		UserElementFactory ufactory = AbstractBuilder.getFactory (UserElementFactory.class, this.user.getClass ());

		cfactory.addEnrolment (this.course, this.element);
		ufactory.addEnrolment (this.user, this.element);
	}

	@Override
	protected void postRemove ()
	{
		CourseElementFactory cfactory = AbstractBuilder.getFactory (CourseElementFactory.class, this.course.getClass ());
		UserElementFactory ufactory = AbstractBuilder.getFactory (UserElementFactory.class, this.user.getClass ());

		cfactory.removeEnrolment (this.course, this.element);
		ufactory.removeEnrolment (this.user, this.element);
	}

	/**
	 * Reset the <code>ElementBuilder</code>.  This method will set all of the
	 * fields for the <code>Element</code> to be built to <code>null</code>.
	 */

	@Override
	public void clear ()
	{
		this.log.trace ("Reseting the builder");

		super.clear ();
		this.course = null;
		this.grade = null;
		this.role = null;
		this.user = null;
		this.usable = Boolean.valueOf (false);
	}

	/**
	 * Load a <code>Enrolment</code> instance into the
	 * <code>EnrolmentBuilder</code>.  This method resets the
	 * <code>EnrolmentBuilder</code> and initializes all of its parameters from
	 * the specified <code>Enrolment</code> instance.  The parameters are
	 * validated as they are set.
	 *
	 * @param  enrolment                The <code>Enrolment</code> to load into
	 *                                  the <code>EnrolmentBuilder</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Enrolment</code> instance to be
	 *                                  loaded are not valid
	 */

	@Override
	public void load (final Enrolment enrolment)
	{
		this.log.trace ("load: {}", enrolment);

		super.load (enrolment);
		this.setCourse (enrolment.getCourse ());
		this.setFinalGrade (enrolment.getFinalGrade ());
		this.setRole (enrolment.getRole ());

		this.setUser (((UserEnrolmentData) enrolment).getUser ());
	}

	@Override
	public Course getCourse ()
	{
		return this.course;
	}

	@Override
	public EnrolmentBuilder setCourse (final Course course)
	{
		if (course == null)
		{
			this.log.error ("Course is NULL");
			throw new NullPointerException ("Course is NULL");
		}

		this.course = (this.manager.getManager (Course.class, CourseManager.class)).fetch (course);

		if (this.course == null)
		{
			this.log.error ("This specified Course does not exist in the DataStore");
			throw new IllegalArgumentException ("Course is not in the DataStore");
		}

		return this;
	}

	@Override
	public Role getRole ()
	{
		return this.role;
	}

	@Override
	public EnrolmentBuilder setRole (final Role role)
	{
		if (role == null)
		{
			this.log.error ("Role is NULL");
			throw new NullPointerException ("Role is NULL");
		}

		this.role = (this.manager.getManager (Role.class, RoleManager.class)).fetch (role);

		if (this.role == null)
		{
			this.log.error ("This specified Role does not exist in the DataStore");
			throw new IllegalArgumentException ("Role is not in the DataStore");
		}

		return this;
	}

	@Override
	public User getUser ()
	{
		return this.user;
	}

	@Override
	public EnrolmentBuilder setUser (final User user)
	{
		if (user == null)
		{
			this.log.error ("User is NULL");
			throw new NullPointerException ("User is NULL");
		}

		this.user = (this.manager.getManager (User.class, UserManager.class)).fetch (user);

		if (this.user == null)
		{
			this.log.error ("This specified User does not exist in the DataStore");
			throw new IllegalArgumentException ("User is not in the DataStore");
		}

		return this;
	}

	@Override
	public Integer getFinalGrade ()
	{
		return this.grade;
	}

	@Override
	public EnrolmentBuilder setFinalGrade (final Integer finalgrade)
	{
		if ((grade != null) && ((grade < 0) || (grade > 100)))
		{
			this.log.error ("Grade must be between 0 and 100");
			throw new IllegalArgumentException ("Grade must be between 0 and 100");
		}

		this.grade = grade;

		return this;
	}

	@Override
	public Boolean isUsable ()
	{
		return this.usable;
	}

	@Override
	public EnrolmentBuilder setUsable (final Boolean usable)
	{
		if (usable == null)
		{
			this.log.error ("usable is NULL");
			throw new NullPointerException ("usable is NULL");
		}

		this.usable = usable;

		return this;
	}
}
