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

package ca.uoguelph.socs.icc.edm.domain.core;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultEnrolmentBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.EnrolmentElementFactory;

/**
 * Implementation of the <code>Enrolment</code> interface with user data.  It
 * is expected that instances of this class will be accessed though the
 * <code>Enrolment</code> interface, along with the relevant manager, and
 * builder.  See the <code>Enrolment</code> interface documentation for
 * details.
 * <p>
 * This class implements the <code>Enrolment</code> and adds a link to the
 * relevant user-identifying data.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.DefaultEnrolmentBuilder
 * @see     ca.uoguelph.socs.icc.edm.domain.manager.DefaultEnrolmentManager
 */

public class UserEnrolmentData extends EnrolmentData implements Enrolment, Serializable
{
	/**
	 * Implementation of the <code>EnrolmentElementFactory</code> interface.
	 * Allows the builders to create instances of
	 * <code>UserEnrolmentData</code>.
	 */

	private static final class Factory extends AbstractElement.Factory<Enrolment> implements EnrolmentElementFactory
	{
		/**
		 * Create a new <code>Enrolment</code> instance.
		 *
		 * @param  user   The <code>User</code> enrolled in the
		 *                <code>Course</code>, not null
		 * @param  course The <code>Course</code> in which the
		 *                <code>User</code> is enrolled, not null
		 * @param  role   The <code>Role</code> of the <code>User</code> in the
		 *                <code>Course</code>, not null
		 * @param  grade  The final grade assigned to the <code>User</code> in
		 *                the <code>Course</code>
		 * @param  usable Indication if the <code>User</code> has consented to
		 *                their data being used for research
		 *
		 * @return        The new <code>Enrolment</code> instance
		 */

		@Override
		public Enrolment create (final User user, final Course course, final Role role, final Integer grade, final Boolean usable)
		{
			assert user != null : "user is NULL";
			assert course != null : "course is NULL";
			assert role != null : "role is NULL";
			assert usable != null : "usable is NULL";
			assert ((grade == null) || ((grade >= 0) && (grade <= 100))) : "grade out of range, must be between 0 and 100";

			return new UserEnrolmentData (user, course, role, grade, usable);
		}

		/**
		 * Set the usable flag for the specified <code>Enrolment</code>.
		 *
		 * @param  enrolment The <code>Enrolment</code> for which the usable
		 *                   flag is to be set, not null
		 * @param  usable    Indication if the <code>User</code> has given
		 *                   consent for their data to be used for research,
		 *                   not null
		 */

		public void setUsable (final Enrolment enrolment, final Boolean usable)
		{
			assert enrolment instanceof UserEnrolmentData : "enrolment is not an instance of EnrolmentData";
			assert usable != null : "usable is NULL";

			((UserEnrolmentData) enrolment).setUsable (usable);
		}

		/**
		 * Set the final grade for the specified <code>Enrolment</code>.
		 *
		 * @param  enrolment The <code>Enrolment</code> for which the final
		 *                   grade is to be set
		 * @param  grade The final grade assigned to the <code>User</code>
		 */

		public void setFinalGrade (final Enrolment enrolment, final Integer grade)
		{
			assert enrolment instanceof UserEnrolmentData : "enrolment is not an instance of EnrolmentData";
			assert ((grade == null) || ((grade >= 0) && (grade <= 100))) : "grade out of range, must be between 0 and 100";

			((UserEnrolmentData) enrolment).setFinalGrade (grade);
		}

		/**
		 * Add the specified <code>Grade</code> to the specified
		 * <code>Enrolment</code>.
		 *
		 * @param  enrolment The <code>Enrolment</code> to which the
		 *                   <code>Grade</code> is to be added, not null
		 * @param  grade     The <code>Grade</code> to add to the
		 *                   <code>Enrolment</code>, not null
		 *
		 * @return           <code>True</code> if the <code>Grade</code> was
		 *                   successfully added to the <code>Enrolment</code>,
		 *                   <code>False</code> otherwise
		 */

		public boolean addGrade (final Enrolment enrolment, final Grade grade)
		{
			assert enrolment instanceof UserEnrolmentData : "enrolment is not an instance of UserEnrolmentData";
			assert grade != null : "grade is NULL";

			return ((UserEnrolmentData) enrolment).addGrade (grade);
		}

		/**
		 * Remove the specified <code>Grade</code> from the specified
		 * <code>Enrolment</code>.
		 *
		 * @param  enrolment The <code>Enrolment</code> from which the
		 *                   <code>Grade</code> is to be removed, not null
		 * @param  grade     The <code>Grade</code> to remove from the
		 *                   <code>Enrolment</code>, not null
		 *
		 * @return           <code>True</code> if the <code>Grade</code> was
		 *                   successfully removed from the
		 *                   <code>Enrolment</code>, <code>False</code>
		 *                   otherwise
		 */

		public boolean removeGrade (final Enrolment enrolment, final Grade grade)
		{
			assert enrolment instanceof UserEnrolmentData : "enrolment is not an instance of UserEnrolmentData";
			assert grade != null : "grade is NULL";

			return ((UserEnrolmentData) enrolment).removeGrade (grade);
		}

		/**
		 * Add the specified <code>LogEntry</code> to the specified
		 * <code>Enrolment</code>.
		 *
		 * @param  enrolment The <code>Enrolment</code> to which the
		 *                   <code>LogEntry</code> is to be added, not null
		 * @param  entry     The <code>LogEntry</code> to add to the
		 *                   <code>Enrolment</code>, not null
		 *
		 * @return           <code>True</code> if the <code>LogEntry</code> was
		 *                   successfully added to the <code>Enrolment</code>,
		 *                   <code>False</code> otherwise
		 */

		public boolean addLogEntry (final Enrolment enrolment, final LogEntry entry)
		{
			assert enrolment instanceof UserEnrolmentData : "enrolment is not an instance of UserEnrolmentData";
			assert entry != null : "entry is NULL";

			return ((UserEnrolmentData) enrolment).addLog (entry);
		}

		/**
		 * Remove the specified <code>LogEntry</code> from the specified
		 * <code>Enrolment</code>.
		 *
		 * @param  enrolment The <code>Enrolment</code> from which the
		 *                   <code>LogEntry</code> is to be removed, not null
		 * @param  entry     The <code>LogEntry</code> to remove from the
		 *                   <code>Enrolment</code>, not null
		 *
		 * @return           <code>True</code> if the <code>LogEntry</code> was
		 *                   successfully removed from the
		 *                   <code>Enrolment</code>, <code>False</code>
		 *                   otherwise
		 */

		public boolean removeLogEntry (final Enrolment enrolment, final LogEntry entry)
		{
			assert enrolment instanceof UserEnrolmentData : "enrolment is not an instance of UserEnrolmentData";
			assert entry != null : "entry is NULL";

			return ((UserEnrolmentData) enrolment).removeLog (entry);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The user which is associated with the enrolment */
	private User user;

	/**
	 * Static initializer to register the <code>UserEnrolmentData</code> class
	 * with the factories.
	 */

	static
	{
		AbstractElement.registerElement (Enrolment.class, UserEnrolmentData.class, DefaultEnrolmentBuilder.class, EnrolmentElementFactory.class, new Factory ());
	}

	/**
	 * Create the <code>Enrolment</code> with null values.
	 */

	public UserEnrolmentData ()
	{
		super ();
		this.user = null;
	}

	/**
	 * Create a new <code>Enrolment</code> instance.
	 *
	 * @param  user   The <code>User</code> enrolled in the
	 *                <code>Course</code>, not null
	 * @param  course The <code>Course</code> in which the <code>User</code> is
	 *                enrolled, not null
	 * @param  role   The <code>Role</code> of the <code>User</code> in the
	 *                <code>Course</code>, not null
	 * @param  grade  The final grade assigned to the <code>User</code> in the
	 *                <code>Course</code>
	 * @param  usable Indication if the <code>User</code> has consented to
	 *                their data being used for research
	 */

	public UserEnrolmentData (final User user, final Course course, final Role role, final Integer grade, final Boolean usable)
	{
		super (course, role, grade, usable);

		assert user != null : "user is NULL";

		this.user = user;
	}

	/**
	 * Compare two <code>Enrolment</code> instances to determine if they are
	 * equal.  The <code>Enrolment</code> instances are compared based upon the
	 * associated <code>User</code> instance as well as the <code>equals</code>
	 * method from the <code>EnrolmentData</code> class.
	 *
	 * @param  obj The <code>Enrolment</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Enrolment</code>
	 *             instances are equal, <code>False</code> otherwise
	 * @see        EnrolmentData#equals
	 */

	@Override
	public boolean equals (final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof UserEnrolmentData)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.appendSuper (super.equals (obj));
			ebuilder.append (this.user, ((UserEnrolmentData) obj).user);

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Enrolment</code> instance.
	 * The hash code is computed based upon the associated <code>User</code>
	 * instance, aw well as the <code>hashCode</code> method from the
	 * <code>EnrolmentData</code> class.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 * @see    EnrolmentData#hashCode
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1103;
		final int mult = 881;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());
		hbuilder.append (this.user);

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the <code>User</code> associated with the <code>Enrolment</code>.
	 *
	 * @return The <code>User</code> instance
	 */

	public User getUser ()
	{
		return this.user;
	}

	/**
	 * Set the <code>user</code> instance which is associated with the
	 * <code>Enrolment</code>. This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Enrolment</code> instance is
	 * loaded.
	 *
	 * @param  user The <code>User</code> instance which is associated with the
	 *              <code>Enrolment</code>
	 */

	protected void setUser (final User user)
	{
		assert user != null : "user is NULL";

		this.user = user;
	}

	/**
	 * Get the name associated with the <code>Enrolment</code>.  The contents
	 * of the <code>String</code> returned by this method are implementation
	 * dependent.  If the implementation has access to the <code>User</code>
	 * information this this method should return the result of the
	 * <code>getName</code> method for the associated <code>User</code>
	 * instance, otherwise it should return some other identifier, usually the
	 * identifier for the <code>Enrolment</code> in the <code>DataStore</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Enrolment</code>
	 * @see    User#getName
	 */

	@Override
	public String getName ()
	{
		return this.user.getName ();
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>UserEnrolmentData</code> instance, including the identifying
	 * fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>UserEnrolmentData</code> instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.appendSuper (super.toString ());
		builder.append ("user", this.user);

		return builder.toString ();
	}
}
