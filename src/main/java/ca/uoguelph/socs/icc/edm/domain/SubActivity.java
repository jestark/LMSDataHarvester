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
import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of a child element in a hierarchy of <code>Activity</code>
 * instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class SubActivity extends ParentActivity
{
	/** Parent to Child <code>SubActivity</code> class mapping*/
	private static final Map<Class<? extends ParentActivity>, Class<? extends SubActivity>> subactivities;

	/** The name of the <code>SubActivity</code> */
	public static final Property<String> NAME;

	/** The parent <code>Activity</code> */
	public static final Property<ParentActivity> PARENT;

	/** The <code>LogEntry</code> instances associated with the <code>SubActivity</code> */
	public static final Property<LogReference> REFERENCES;

	/** The <code>SubActivity</code> instances for the <code>SubActivity</code> */
	public static final Property<SubActivity> SUBACTIVITIES;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>SubActivity</code>.
	 */

	static
	{
		subactivities = new HashMap<> ();

		NAME = Property.getInstance (String.class, "name", Property.Flags.REQUIRED);
		PARENT = Property.getInstance (ParentActivity.class, "parent", Property.Flags.REQUIRED);

		REFERENCES = Property.getInstance (LogReference.class, "references", Property.Flags.MULTIVALUED);
		SUBACTIVITIES = Property.getInstance (SubActivity.class, "subactivities", Property.Flags.MULTIVALUED);

		Definition.getBuilder (SubActivity.class, Element.class)
			.addProperty (NAME, SubActivity::getName, SubActivity::setName)
			.addRelationship (PARENT, SubActivity::getParent, SubActivity::setParent)
			.addRelationship (REFERENCES, SubActivity::getReferences, SubActivity::addReference, SubActivity::removeReference)
			.addRelationship (SUBACTIVITIES, SubActivity::getSubActivities, SubActivity::addSubActivity, SubActivity::removeSubActivity)
			.build ();
	}

	/**
	 * Get the <code>SubActivity</code> implementation class which is
	 * associated with the specified <code>Activity</code> implementation
	 * class.
	 *
	 * @param  activity The <code>Activity</code> implementation class
	 *
	 * @return          The <code>SubActivity</code> implementation class, may be
	 *                  null
	 */

	public static final Class<? extends SubActivity> getSubActivityClass (final Class<? extends ParentActivity> activity)
	{
		assert activity != null : "activity is NULL";
		assert SubActivity.subactivities.containsKey (activity) : "Activity is not registered";

		return SubActivity.subactivities.get (activity);
	}

	/**
	 * Register an association between an <code>Activity</code> implementation
	 * class and a <code>SubActivity</code> implementation class.
	 *
	 * @param  activity    The <code>Activity</code> implementation, not null
	 * @param  subactivity The <code>SubActivity</code> implementation, not null
	 */

	protected static final void registerImplementation (final Class<? extends ParentActivity> activity, final Class<? extends SubActivity> subactivity)
	{
		assert activity != null : "activity is NULL";
		assert subactivity != null : "subactivity is NULL";
		assert (! SubActivity.subactivities.containsKey (activity)) : "activity is already registered";

		SubActivity.subactivities.put (activity, subactivity);
	}

	/**
	 * Get an instance of the <code>SubActivityBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 * @param  parent                The parent <code>Activity</code>, not null
	 *
	 * @return                       The <code>SubActivityBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> is
	 *                               immutable
	 */

	public static SubActivityBuilder builder (final DataStore datastore, final ParentActivity parent)
	{
		assert datastore != null : "datastore is NULL";
		assert parent != null : "parent is NULL";

		return new SubActivityBuilder (datastore, parent);
	}

	/**
	 * Get an instance of the <code>SubActivityBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 * @param  parent                The parent <code>Activity</code>, not null
	 *
	 * @return                       The <code>SubActivityBuilder</code> instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static SubActivityBuilder builder (final DomainModel model, final ParentActivity parent)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		if (parent == null)
		{
			throw new NullPointerException ("parent is NULL");
		}

		return SubActivity.builder (model.getDataStore (), parent);
	}

	/**
	 * Compare two <code>SubActivity</code> instances to determine if they are
	 * equal.  The <code>SubActivity</code> instances are compared based upon
	 * their names and the parent <code>Activity</code>.
	 *
	 * @param  obj The <code>SubActivity</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>SubActivity</code>
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
		else if (obj instanceof SubActivity)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.getName (), ((SubActivity) obj).getName ());
			ebuilder.append (this.getParent (), ((SubActivity) obj).getParent ());

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>SubActivity</code>
	 * instance.  The hash code is computed based upon the parent
	 * <code>Activity</code> and the name of the <code>SubActivity</code>
	 * instance.
	 *
	 * @return The hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1123;
		final int mult = 859;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.getName ());
		hbuilder.append (this.getParent ());

		return hbuilder.toHashCode ();
	}

	/**
	 * Get a <code>String</code> representation of the <code>SubActivity</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>SubActivity</code> instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("name", this.getName ());
		builder.append ("parent", this.getParent ());

		return builder.toString ();
	}

	/**
	 * Get an <code>SubActivityBuilder</code> instance for the specified
	 * <code>DataStore</code>.  This method creates an
	 * <code>SubActivityBuilder</code> on the specified <code>DataStore</code>
	 * and initializes it with the contents of this <code>SubActivity</code>
	 * instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The initialized <code>SubActivityBuilder</code>
	 */

	@Override
	public SubActivityBuilder getBuilder (final DataStore datastore)
	{
		assert datastore != null : "datastore is null";

		return SubActivity.builder (datastore, this.getParent ())
			.load (this);
	}

	/**
	 * Get the <code>MetaData</code> instance for this <code>SubActivity</code>
	 * using the specified <code>DataStore</code>.
	 *
	 * @return The <code>MetaData</code>
	 */

	@Override
	protected MetaData<SubActivity> getMetaData ()
	{
		return this.getDomainModel ()
			.getDataStore ()
			.getProfile ()
			.getCreator (SubActivity.class, this.getClass ());
	}

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	@Override
	public final ActivityType getType()
	{
		return this.getParent ().getType ();
	}

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @return The <code>Course</code> instance
	 */

	@Override
	public final Course getCourse ()
	{
		return this.getParent ().getCourse ();
	}

	/**
	 * Get the <code>Set</code> of <code>Grade</code> instances which are
	 * associated with the <code>Activity</code>.  Not all
	 * <code>Activity</code> instances are graded.  If the
	 * <code>Activity</code> does is not graded then the <code>Set</code> will
	 * be empty.
	 *
	 * @return A <code>Set</code> of <code>Grade</code> instances
	 */

	@Override
	public final Set<Grade> getGrades ()
	{
		return this.getParent ().getGrades ();
	}

	/**
	 * Get the parent <code>Activity</code> instance for the
	 * <code>SubActivity</code>.
	 *
	 * @return The parent <code>Activity</code>
	 */

	public abstract ParentActivity getParent ();

	/**
	 * Set the <code>Activity</code> instance which contains the
	 * <code>SubActivity</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  activity The <code>Activity</code> containing this
	 *                  <code>SubActivity</code> instance
	 */

	protected abstract void setParent (ParentActivity activity);

	/**
	 * Set the name of the <code>Activity</code>.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Activity</code>
	 * instance is loaded.
	 *
	 * @param  name The name of the <code>Activity</code>, not null
	 */

	protected abstract void setName (String name);

	/**
	 * Get a <code>List</code> of all of the <code>LogReference</code>
	 * instances for the <code>SubActivity</code>.
	 *
	 * @return A <code>List</code> of <code>LogReference</code> instances
	 */

	public abstract List<LogReference> getReferences ();

	/**
	 * Initialize the <code>List</code> of <code>LogReference</code> instances
	 * associated with the <code>SubActivity</code> instance.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>SubActivity</code> instance is loaded.
	 *
	 * @param  references The <code>List</code> of <code>LogReference</code>
	 *                    instances, not null
	 */

	protected abstract void setReferences (List<LogReference> references);

	/**
	 * Add the specified <code>LogReference</code> to the
	 * <code>SubActivity</code>.
	 *
	 * @param  reference    The <code>LogReference</code> to add, not null
	 *
	 * @return              <code>True</code> if the <code>LogReference</code>
	 *                      was successfully added, <code>False</code>
	 *                      otherwise
	 */

	protected abstract boolean addReference (LogReference reference);

	/**
	 * Remove the specified <code>LogReference</code> from the
	 * <code>SubActivity</code>.
	 *
	 * @param  reference    The <code>LogReference</code> to remove, not null
	 *
	 * @return              <code>True</code> if the <code>LogReference</code>
	 *                      was successfully removed, <code>False</code>
	 *                      otherwise
	 */

	protected abstract boolean removeReference (LogReference reference);

	/**
	 * Initialize the <code>List</code> of <code>SubActivity</code> instances
	 * for the <code>SubActivity</code>.  This method is intended to be used by
	 * a <code>DataStore</code> when the <code>SubActivity</code> instance is
	 * loaded.
	 *
	 * @param  subactivities The <code>List</code> of <code>SubActivity</code>
	 *                       instances, not null
	 */

	protected abstract void setSubActivities (List<SubActivity> subactivities);

	/**
	 * Add the specified <code>SubActivity</code> to the
	 * <code>SubActivity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to add, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addSubActivity (SubActivity subactivity);

	/**
	 * Remove the specified <code>SubActivity</code> from the
	 * <code>SubActivity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to remove, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully removed, <code>False</code>
	 *                     otherwise
	 */

	protected abstract boolean removeSubActivity (SubActivity subactivity);
}
