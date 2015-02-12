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

import java.util.Set;

/**
 * A representation of a parent element in a hierarchy of <code>Activity</code>
 * instances.  This interface along with <code>ActivityGroupMember</code>
 * represent a hierarchy of sub-activities.  Sub-activities will implement one
 * or both of <code>ActivityGroup</code> and <code>ActivityGroupMember</code>.
 * The <code>Activity</code> at the top of the hierarchy and any sub-activities
 * in the middle of the hierarchy will implement <code>ActivityGroup</code>
 * while <code>ActivityGroupMember</code> will be implemented by any
 * sub-activities in the middle of the hierarchy and the sub-activity forming
 * the bottom of the hierarchy.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityGroupMember
 */

public interface ActivityGroup extends Activity
{
	/**
	 * Get the <code>Set</code> of <code>ActivityGroupMember</code> instances (or
	 * Sub-Activities) associated with the <code>ActvityGroup</code>.
	 *
	 * @return The <code>Set</code> of sub-activities
	 */

	public abstract Set<ActivityGroupMember> getChildren();
}
