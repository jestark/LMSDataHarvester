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

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Generic representation of an <code>Activity</code> which has a name.  This
 * class acts as an abstract base class for all of the <code>Activity</code>
 * implementations which have a name.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class NamedActivity extends Activity
{
	/** The <code>Grade</code> instances associated with the <code>Activity</code> */
	public static final Property<Grade> GRADES;

	/** The <code>SubActivity</code> instances for the <code>Activity</code> */
	public static final Property<SubActivity> SUBACTIVITIES;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>NamedActivity</code>.
	 */

	static
	{
		GRADES = Property.getInstance (Grade.class, "grade", Property.Flags.MULTIVALUED);
		SUBACTIVITIES = Property.getInstance (SubActivity.class, "subactivities", Property.Flags.MULTIVALUED);

		Definition.getBuilder (NamedActivity.class, Activity.class)
			.addProperty (Activity.NAME, NamedActivity::getName, NamedActivity::setName)
			.addRelationship (GRADES, NamedActivity::getGrades, NamedActivity::addGrade, NamedActivity::removeGrade)
			.addRelationship (SUBACTIVITIES, NamedActivity::getSubActivities, NamedActivity::addSubActivity, NamedActivity::removeSubActivity)
			.build ();
	}
	/**
	 * Compare two <code>NamedActivity</code> instances to determine if they are
	 * equal.  The <code>NamedActivity</code> instances are compared based upon
	 * their <code>ActivityType</code>, the associated <code>Course</code> and
	 * their names.
	 *
	 * @param  obj The <code>NamedActivity</code> instance to compare to the
	 *             one represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>NamedActivity</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof NamedActivity)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();

			ebuilder.appendSuper (super.equals (obj));
			ebuilder.append (this.getName (), ((NamedActivity) obj).getName ());

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>NamedActivity</code>
	 * instance.  The hash code is computed based upon the
	 * <code>ActivityType</code>, the <code>Course</code> and the name of the
	 * instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1117;
		final int mult = 863;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());
		hbuilder.append (this.getName ());

		return hbuilder.toHashCode ();
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>NamedActivity</code> instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>NamedActivity</code> instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.appendSuper (super.toString ());
		builder.append ("name", this.getName ());

		return builder.toString ();
	}

	/**
	 * Get an <code>NamedActivityBuilder</code> instance for the specified
	 * <code>DataStore</code>.  This method creates a
	 * <code>NamedActivityBuilder</code> on the specified
	 * <code>DataStore</code> and initializes it with the contents of this
	 * <code>NamedActivity</code> instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The initialized <code>NamedActivityBuilder</code>
	 */

	@Override
	public NamedActivityBuilder getBuilder (final DataStore datastore)
	{
		assert datastore != null : "datastore is null";

		NamedActivityBuilder builder = new NamedActivityBuilder (datastore, this.getType ());
		builder.load (this);

		return builder;
	}

	/**
	 * Set the name of the <code>Activity</code>.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Activity</code>
	 * instance is loaded.
	 *
	 * @param  name The name of the <code>Activity</code>, not null
	 */

	protected abstract void setName (String name);

	/**
	 * Initialize the <code>Set</code> of <code>Grade</code> instances
	 * associated with the <code>Activity</code> instance.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>Activity</code> instance is loaded.
	 *
	 * @param  grades The <code>Set</code> of <code>Grade</code> instances, not
	 *                null
	 */

	protected abstract void setGrades (Set<Grade> grades);

	/**
	 * Add the specified <code>Grade</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  grade    The <code>Grade</code> to add, not null
	 *
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addGrade (Grade grade);

	/**
	 * Remove the specified <code>Grade</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  grade    The <code>Grade</code> to remove, not null
	 *
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully removed from, <code>False</code> otherwise
	 */

	protected abstract boolean removeGrade (Grade grade);

	/**
	 * Initialize the <code>List</code> of <code>SubActivity</code> instances
	 * for the <code>Activity</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  subactivities The <code>List</code> of <code>SubActivity</code>
	 *                       instances, not null
	 */

	protected abstract void setSubActivities (List<SubActivity> subactivities);

	/**
	 * Add the specified <code>SubActivity</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to add, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addSubActivity (SubActivity subactivity);

	/**
	 * Remove the specified <code>SubActivity</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to remove, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully removed, <code>False</code>
	 *                     otherwise
	 */

	protected abstract boolean removeSubActivity (SubActivity subactivity);
}
