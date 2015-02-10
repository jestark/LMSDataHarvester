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

package ca.uoguelph.socs.icc.edm.domain.activity.${ActivitySource};

import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.core.GenericGroupedActivityGroup;

/**
 * Implementation of the <code>Activity</code> interface for the
 * ${ActivitySource}/${ActivityType}
 * <code>ActivitySource</code>/<code>ActivityType</code>.  It is expected that
 * this class will be accessed though the <code>ActivityGroup</code> interface,
 * along with the relevant manager, and builder.  See the
 * <code>ActivityGroup</code> interface documentation for details.
 * <p>
 * This class was generated from the <code>GroupedActivity</code> template, with
 * the following values:
 * <p>
 * <ul>
 * <li>ActivitySource = ${ActivitySource}
 * <li>ActivityType   = ${ActivityType}
 * <li>ClassName      = ${ClassName}
 * <li>Builder        = ${Builder}
 * <li>HashBase       = ${HashBase}
 * <li>HashMult       = ${HashMult}
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.1
 */

public class ${ClassName} extends GenericGroupedActivityGroup
{
	/**
	 * Implementation of the <code>ActivityGroupMemberElementFactory</code>.
	 * Allows the builders to create instances of <code>${ClassName}</code>.
	 */

	private static final class Factory implements ActivityGroupElementFactory, ActivityGroupMemberElementFactory
	{
		/**
		 * Create a new sub-activity (<code>ActivityGroupMember</code>) instance.
		 *
		 * @param  parent The parent <code>ActivityGroup</code>, not null
		 * @param  name   The name of the <code>ActivityGroupMember</code>, not null
		 *
		 * @return        The new sub-activity (<code>ActivityGroupMember</code>)
		 *                instance
		 */

		public ActivityGroupMember create (ActivityGroup parent, String name)
		{
			return new ${ClassName} (parent, name);
		}

		/**
		 * Add the specified <code>ActivityGroupMember</code> to the specified
		 * <code>ActivityGroup</code>.
		 *
		 * @param  group  The <code>ActivityGroup</code> to which the
		 *                <code>ActivityGroupMember</code> is to be added, not null
		 * @param  member The <code>ActivityGroupMember</code> to add to the
		 *                <code>ActivityGroup</code>, not null
		 *
		 * @return        <code>True</code> if the <code>ActivityGroupMember</code>
		 *                was successfully added to the <code>ActvityGroup</code>,
		 *                <code>False</code> otherwise
		 */

		public boolean addChild (ActivityGroup group, ActivityGroupMember member)
		{
			return ((${ClassName}) group).addChild (member);
		}

		/**
		 * Remove the specified <code>ActivityGroupMember</code> from the specified
		 * <code>ActivityGroup</code>.
		 *
		 * @param  group  The <code>ActivityGroup</code> from which the
		 *                <code>ActivityGroupMember</code> is to be removed, not null
		 * @param  member The <code>ActivityGroupMember</code> to remove from the
		 *                <code>ActivityGroup</code>, not null
		 *
		 * @return        <code>True</code> if the <code>ActivityGroupMember</code>
		 *                was successfully removed from the <code>ActvityGroup</code>,
		 *                <code>False</code> otherwise
		 */

		public boolean removeChild (ActivityGroup group, ActivityGroupMember member)
		{
			return ((${ClassName}) group).removeChild (member);
		}
	}

	/**
	 * Register the <code>${ClassName}</code> with the factories on initialization.
	 */

	static
	{
		GenericGroupedActivityGroup.registerActivity (${ClassName}.class, ${Builder}.class, new Factory ());
	}

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	public ${ClassName} ()
	{
		super ();
	}

	/**
	 * Create a new sub-activity (<code>ActivityGroupMember</code>) instance.
	 *
	 * @param  parent The parent <code>ActivityGroup</code>, not null
	 * @param  name   The name of the <code>ActivityGroupMember</code>, not null
	 */

	public ${ClassName} (ActivityGroup parent, String name)
	{
		super (parent, name);
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Activity</code> instance.
	 * The hash code is computed by the superclass, with unique values added
	 * to separate the instances of <code>${ClassName}</code> from the other
	 * subclasses of the superclass.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = ${HashBase};
		final int mult = ${HashMult};

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}