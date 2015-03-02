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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.EnrolmentBuilder;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.User;

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

	/** The logger */
	private final Logger log;

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
	 * static initializer to register the <code>DefaultEnrolmentBuilder</code> with the
	 * factory
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

		this.log = LoggerFactory.getLogger (DefaultEnrolmentBuilder.class);
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

		return null; //this.factory.create (this.user, this.course, this.role, this.grade, this.usable);
	}

	@Override
	protected void postInsert ()
	{
	}

	@Override
	protected void postRemove ()
	{
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
		this.log.trace ("Load Enrolment: {}", enrolment);

		super.load (enrolment);
		this.setCourse (enrolment.getCourse ());
		this.setFinalGrade (enrolment.getFinalGrade ());
		this.setRole (enrolment.getRole ());

		// user ??
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

		this.course = course;

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

		this.role = role;

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

		this.user = user;

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
			this.log.error ("Usable is NULL");
			throw new NullPointerException ("Usable is NULL");
		}

		this.usable = usable;

		return this;
	}
}
