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

package ca.uoguelph.socs.icc.edm.domain.core;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultEnrolmentBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.EnrolmentElementFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.EnrolmentFactory;
import ca.uoguelph.socs.icc.edm.domain.manager.DefaultEnrolmentManager;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Enrolment
 * @see     EnrolmentData
 * @see     EnrolmentManager
 */

public class UserEnrolmentData extends EnrolmentData
{
	private static final class UserEnrolmentDataFactory implements EnrolmentElementFactory
	{
		@Override
		public Enrolment create (User user, Course course, Integer grade)
		{
			UserEnrolmentData enrolment = new UserEnrolmentData ();

			enrolment.setUser (user);
			enrolment.setCourse (course);
			enrolment.setFinalGrade (grade);

			return enrolment;
		}

		@Override
		public void setId (Enrolment enrolment, Long id)
		{
			((UserEnrolmentData) enrolment).setId (id);
		}
	}

	private static final long serialVersionUID = 1L;

	static
	{
		(EnrolmentFactory.getInstance ()).registerElement (UserEnrolmentData.class, DefaultEnrolmentManager.class, DefaultEnrolmentBuilder.class, new UserEnrolmentDataFactory ());
	}

	/** The user which is associated with this enrolment */
	private User user;

	/**
	 * Create the enrolment with null values
	 */

	protected UserEnrolmentData ()
	{
		super ();
		this.user = null;
	}

	/**
	 * Get the user associated with this enrolment.
	 *
	 * @return The user which is associated with this enrolment.
	 */

	public User getUser ()
	{
		return this.user;
	}

	/**
	 * Internal method used by the datastore and managers to set the associated
	 * user.
	 *
	 * @param user The user which is associated with this enrolment.
	 */

	protected void setUser (User user)
	{
		this.user = user;
	}

	/**
	 * Override EnrolmentData's toString function to display the name of the 
	 * user.
	 *
	 * @return A string identifying the enrolment.
	 */

	@Override
	public String getName ()
	{
		return this.user.getName ();
	}
}
