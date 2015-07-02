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

package ca.uoguelph.socs.icc.edm.domain;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Create new <code>Enrolment</code> instances.  This class extends
 * <code>AbstractBuilder</code>, adding the functionality required to
 * create <code>Enrolment</code> instances.  The "finalgrade" and "usable"
 * fields may be modified in place.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Enrolment
 */

public final class EnrolmentBuilder extends AbstractBuilder<Enrolment>
{
	/**
	 * Get an instance of the <code>EnrolmentBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The <code>EnrolmentBuilder</code> instance
	 */

	public static EnrolmentBuilder getInstance (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return new EnrolmentBuilder (datastore, AbstractBuilder.getBuilder (datastore, datastore.getElementClass (Enrolment.class)));
	}

	/**
	 * Get an instance of the <code>EnrolmentBuilder</code> for the specified
	 * <code>DataStore</code>, loaded with the data from the specified
	 * <code>Enrolment</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  enrolment The <code>Enrolment</code>, not null
	 *
	 * @return           The <code>EnrolmentBuilder</code> instance
	 */

	public static EnrolmentBuilder getInstance (final DataStore datastore, Enrolment enrolment)
	{
		assert datastore != null : "datastore is NULL";
		assert enrolment != null : "enrolment is NULL";

		EnrolmentBuilder builder = EnrolmentBuilder.getInstance (datastore);
		builder.load (enrolment);

		return builder;
	}

	/**
	 * Get an instance of the <code>EnrolmentBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The <code>EnrolmentBuilder</code> instance
	 */


	public static EnrolmentBuilder getInstance (final DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		return EnrolmentBuilder.getInstance (model.getDataStore ());
	}

	/**
	 * Get an instance of the <code>EnrolmentBuilder</code> for the specified
	 * <code>DomainModel</code>, loaded with the data from the specified
	 * <code>Enrolment</code>.
	 *
	 * @param  model     The <code>DomainModel</code>, not null
	 * @param  enrolment The <code>Enrolment</code>, not null
	 *
	 * @return           The <code>EnrolmentBuilder</code> instance
	 */

	public static EnrolmentBuilder getInstance (final DomainModel model, Enrolment enrolment)
	{
		if (enrolment == null)
		{
			throw new NullPointerException ("enrolment is NULL");
		}

		EnrolmentBuilder builder = EnrolmentBuilder.getInstance (model);
		builder.load (enrolment);

		return builder;
	}

	/**
	 * Create the <code>EnrolmentBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  builder   The <code>Builder</code>, not null
	 */

	protected EnrolmentBuilder (final DataStore datastore, final Builder<Enrolment> builder)
	{
		super (datastore, builder);
	}

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

		this.builder.setProperty (Enrolment.Properties.ID, enrolment.getId ());
	}

	/**
	 * Get the <code>Course</code> in which the <code>User</code> represented
	 * by the <code>Enrolment</code> instance is enrolled.
	 *
	 * @return The <code>Course</code> instance
	 */

	public Course getCourse ()
	{
		return this.builder.getPropertyValue (Enrolment.Properties.COURSE);
	}

	/**
	 * Set the <code>Course</code> in which the <code>User</code> is enrolled.
	 *
	 * @param  course                   The <code>Course</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Course</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public void setCourse (final Course course)
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

		this.builder.setProperty (Enrolment.Properties.COURSE, course);
	}

	/**
	 * Get the <code>Role</code> of the <code>User</code> represented by this
	 * <code>Enrolment</code>, in the associated <code>Course</code>.
	 *
	 * @return The <code>Role</code> instance
	 */

	public Role getRole ()
	{
		return this.builder.getPropertyValue (Enrolment.Properties.ROLE);
	}

	/**
	 * Set the <code>Role</code> of the <code>User</code> in the
	 * <code>Course</code>.
	 *
	 * @param  role                      The <code>Role</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Role</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public void setRole (final Role role)
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

		this.builder.setProperty (Enrolment.Properties.ROLE, role);
	}

	/**
	 * Get the final grade for the <code>User</code> represented by this
	 * <code>Enrolment</code>, in the associated <code>Course</code>.  The
	 * final grade will be null if no final grade was assigned for the
	 * <code>User</code> associated with this <code>Enrolment</code>.
	 *
	 * @return An <code>Integer</code> containing the final grade, or null if
	 *         there is no final grade
	 */

	public Integer getFinalGrade ()
	{
		return this.builder.getPropertyValue (Enrolment.Properties.FINALGRADE);
	}

	/**
	 * Set the final grade for the <code>User</code> in the
	 * <code>Course</code>.
	 *
	 * @param  finalgrade The final grade for the <code>User</code> in the
	 *                    course, on the interval [0, 100]
	 *
	 * @throws IllegalArgumentException If the value is less than zero or
	 *                                  greater than 100
	 */

	public void setFinalGrade (final Integer finalgrade)
	{
		this.log.trace ("setFinalGrade: finalgrade={}", finalgrade);

		if ((finalgrade != null) && ((finalgrade < 0) || (finalgrade > 100)))
		{
			this.log.error ("Grade must be between 0 and 100");
			throw new IllegalArgumentException ("Grade must be between 0 and 100");
		}

		this.builder.setProperty (Enrolment.Properties.FINALGRADE, finalgrade);
	}

	/**
	 * Determine if the <code>User</code> has given their consent for the data
	 * associated with this <code>Enrolment</code> to be used for research.
	 *
	 * @return <code>True</code> if the <code>User</code> has consented,
	 *         <code>False</code> otherwise.
	 */

	public Boolean isUsable ()
	{
		return this.builder.getPropertyValue (Enrolment.Properties.USABLE);
	}

	/**
	 * Set the usable flag for the data related to the <code>User</code> in the
	 * <code>Course</code>. This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Enrolment</code> instance is
	 * loaded.
	 *
	 * @param  usable Indication if the data may be used for research, not null
	 */

	public void setUsable (final Boolean usable)
	{
		this.log.trace ("setUsable: usable={}", usable);

		if (usable == null)
		{
			this.log.error ("usable is NULL");
			throw new NullPointerException ("usable is NULL");
		}

		this.builder.setProperty (Enrolment.Properties.USABLE, usable);
	}
}
