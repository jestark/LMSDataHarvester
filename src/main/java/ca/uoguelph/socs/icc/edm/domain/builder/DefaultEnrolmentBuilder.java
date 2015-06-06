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
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.EnrolmentBuilder;
import ca.uoguelph.socs.icc.edm.domain.Role;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

public final class DefaultEnrolmentBuilder extends AbstractBuilder<Enrolment, Enrolment.Properties> implements EnrolmentBuilder
{
	/**
	 * static initializer to register the <code>DefaultEnrolmentBuilder</code>
	 * with the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (DefaultEnrolmentBuilder.class, DefaultEnrolmentBuilder::new);
	}

	/**
	 * Create the <code>DefaultEnrolmentBuilder</code>.
	 *
	 * @param  impl      The implementation class of the <code>Element</code>
	 *                   to be built
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>Enrolment</code> instance will be
	 *                   inserted
	 */

	protected DefaultEnrolmentBuilder (final Class<?> impl, final DataStore datastore)
	{
		super (impl, datastore);
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
		this.log.trace ("load: enrolment={}", enrolment);

		if (enrolment == null)
		{
			this.log.error ("Attempting to load a NULL Enrolment");
			throw new NullPointerException ();
		}

		super.load (enrolment);
		this.setCourse (enrolment.getCourse ());
		this.setFinalGrade (enrolment.getFinalGrade ());
		this.setRole (enrolment.getRole ());

		this.setPropertyValue (Enrolment.Properties.ID, enrolment.getId ());
	}

	@Override
	public Course getCourse ()
	{
		return this.getPropertyValue (Course.class, Enrolment.Properties.COURSE);
	}

	@Override
	public EnrolmentBuilder setCourse (final Course course)
	{
		this.log.trace ("setCourse: course={}", course);

		if (course == null)
		{
			this.log.error ("Course is NULL");
			throw new NullPointerException ("Course is NULL");
		}

		if (! this.datastore.contains (course))
		{
			this.log.error ("This specified Course does not exist in the DataStore");
			throw new IllegalArgumentException ("Course is not in the DataStore");
		}

		this.setPropertyValue (Enrolment.Properties.COURSE, course);

		return this;
	}

	@Override
	public Role getRole ()
	{
		return this.getPropertyValue (Role.class, Enrolment.Properties.ROLE);
	}

	@Override
	public EnrolmentBuilder setRole (final Role role)
	{
		this.log.trace ("setRole: role={}", role);

		if (role == null)
		{
			this.log.error ("Role is NULL");
			throw new NullPointerException ("Role is NULL");
		}

		if (! this.datastore.contains (role))
		{
			this.log.error ("This specified Role does not exist in the DataStore");
			throw new IllegalArgumentException ("Role is not in the DataStore");
		}

		this.setPropertyValue (Enrolment.Properties.ROLE, role);

		return this;
	}

	@Override
	public Integer getFinalGrade ()
	{
		return this.getPropertyValue (Integer.class, Enrolment.Properties.FINALGRADE);
	}

	@Override
	public EnrolmentBuilder setFinalGrade (final Integer finalgrade)
	{
		this.log.trace ("setFinalGrade: finalgrade={}", finalgrade);

		if ((finalgrade != null) && ((finalgrade < 0) || (finalgrade > 100)))
		{
			this.log.error ("Grade must be between 0 and 100");
			throw new IllegalArgumentException ("Grade must be between 0 and 100");
		}

		this.setPropertyValue (Enrolment.Properties.FINALGRADE, finalgrade);

		return this;
	}

	@Override
	public Boolean isUsable ()
	{
		return this.getPropertyValue (Boolean.class, Enrolment.Properties.USABLE);
	}

	@Override
	public EnrolmentBuilder setUsable (final Boolean usable)
	{
		this.log.trace ("setUsable: usable={}", usable);

		if (usable == null)
		{
			this.log.error ("usable is NULL");
			throw new NullPointerException ("usable is NULL");
		}

		this.setPropertyValue (Enrolment.Properties.USABLE, usable);

		return this;
	}
}
