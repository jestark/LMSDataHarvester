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

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.GradeBuilder;

import ca.uoguelph.socs.icc.edm.domain.manager.ManagerProxy;

public final class DefaultGradeBuilder extends AbstractBuilder<Grade> implements GradeBuilder
{
	/**
	 * Implementation of the <code>BuilderFactory</code> to create a
	 * <code>DefaultGradeBuilder</code>.
	 */

	private static class Factory implements BuilderFactory<Grade, GradeBuilder>
	{
		/**
		 * Create the <code>GradeBuilder</code>.  The supplied
		 * <code>ManagerProxy</code> will be used by the builder to access the
		 * <code>GradeManager</code> to perform operations on the
		 * <code>DataStore</code>.
		 *
		 * @param  manager The <code>ManagerProxy</code> used to the 
		 *                 <code>GradeManager</code> instance, not null
		 *
		 * @return         The <code>GradeBuilder</code>
		 */

		@Override
		public GradeBuilder create (final ManagerProxy<Grade> manager)
		{
			return new DefaultGradeBuilder (manager);
		}
	}

	/** The logger */
	private final Logger log;

	/** The activity associated with the grade */
	private Activity activity;

	/** The enrolment associated with the grade */
	private Enrolment enrolment;

	/** The grade */
	private Integer grade;

	/**
	 * static initializer to register the <code>DefaultGradeBuilder</code> with the
	 * factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (GradeBuilder.class, DefaultGradeBuilder.class, new Factory ());
	}

	protected DefaultGradeBuilder (final ManagerProxy<Grade> manager)
	{
		super (manager);
		
		this.log = LoggerFactory.getLogger (DefaultGradeBuilder.class);

		this.clear ();
	}

	@Override
	public Grade build ()
	{
		if (this.activity == null)
		{
			this.log.error ("Can not build: The activity is not set");
			throw new IllegalStateException ("activity not set");
		}

		if (this.enrolment == null)
		{
			this.log.error ("Can not build: The enrolment is not set");
			throw new IllegalStateException ("enrolment not set");
		}

		if (this.grade == null)
		{
			this.log.error ("Can not build: The grade is not set");
			throw new IllegalStateException ("grade not set");
		}

		return null; //this.factory.create (this.enrolment, this.activity, this.grade);
	}

	@Override
	public void clear ()
	{
		this.activity = null;
		this.enrolment = null;
		this.grade = null;
	}

	@Override
	public Activity getActivity ()
	{
		return this.activity;
	}

	@Override
	public GradeBuilder setActivity (final Activity activity)
	{
		if (grade == null)
		{
			this.log.error ("The specified activity is NULL");
			throw new NullPointerException ("The specified activity is NULL");
		}

		this.activity = activity;

		return this;
	}

	@Override
	public Enrolment getEnrolment ()
	{
		return this.enrolment;
	}

	@Override
	public GradeBuilder setEnrolment (final Enrolment enrolment)
	{
		if (grade == null)
		{
			this.log.error ("The specified Enrolment is NULL");
			throw new NullPointerException ("The specified Enrolment is NULL");
		}

		this.enrolment = enrolment;

		return this;
	}

	@Override
	public Integer getGrade ()
	{
		return this.grade;
	}

	@Override
	public GradeBuilder setGrade (final Integer grade)
	{
		if (grade == null)
		{
			this.log.error ("The specified grade is NULL");
			throw new NullPointerException ("The specified grade is NULL");
		}

		this.grade = grade;

		return this;
	}
}
