/* Copyright (C) 2014 James E. Stark
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.AbstractBuilder;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.EnrolmentBuilder;
import ca.uoguelph.socs.icc.edm.domain.EnrolmentManager;
import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.idgenerator.IdGenerator;

public final class DefaultEnrolmentBuilder extends AbstractBuilder<Enrolment> implements EnrolmentBuilder
{
	/** The logger */
	private final Log log;

	/** <code>ElementFactory</code> to build the enrolment */
	private final EnrolmentElementFactory factory;

	/** The course in which the user is enrolled */
	private Course course;

	/** The user's final grade in the course */
	private Integer grade;

	/** The user associated with the enrolment */
	private User user;

	protected DefaultEnrolmentBuilder (EnrolmentManager manager, EnrolmentElementFactory factory, IdGenerator generator)
	{
		super (manager);

		this.factory = factory;
		this.log = LogFactory.getLog (DefaultEnrolmentBuilder.class);
	}

	@Override
	protected Enrolment build ()
	{
		if (this.user == null)
		{
			this.log.error ("Can not build: The enrolment's user is not set");
			throw new IllegalStateException ("user not set");
		}

		if (this.course == null)
		{
			this.log.error ("Can not build: The enrolment's course is not set");
			throw new IllegalStateException ("course not set");
		}

		return this.factory.create (this.user, this.course, this.grade);
	}

	@Override
	public void clear ()
	{
		this.course = null;
		this.grade = null;
		this.user = null;
	}

	@Override
	public Course getCourse ()
	{
		return this.course;
	}

	@Override
	public EnrolmentBuilder setCourse (Course course)
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
	public User getUser ()
	{
		return this.user;
	}

	@Override
	public EnrolmentBuilder setUser (User user)
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
	public EnrolmentBuilder setFinalGrade (Integer finalgrade)
	{
		if ((grade != null) && ((grade < 0) || (grade > 100)))
		{
			this.log.error ("Grade must be between 0 and 100");
			throw new IllegalArgumentException ("Grade must be between 0 and 100");
		}

		this.grade = grade;

		return this;
	}
}
