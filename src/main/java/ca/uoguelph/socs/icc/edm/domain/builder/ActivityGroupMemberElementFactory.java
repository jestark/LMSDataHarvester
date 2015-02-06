/* Copyright (C) 2015 James E. Stark
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

import ca.uoguelph.socs.icc.edm.domain.Activity;

import ca.uoguelph.socs.icc.edm.domain.core.ActivityGroup;
import ca.uoguelph.socs.icc.edm.domain.core.ActivityGroupMember;

/**
 * Factory interface to create new <code>ActivityGroupMember</code> instances.
 * Implementations of this interface provide the functionality required to
 * create new instances of a class implementing the
 * <code>ActivityGroupMember</code> interface (a sub-interface of
 * <code>Activity</code>).  It also provides the functionality required to set the
 * <code>DataStore</code> ID for the <code>ActivityGroupMember</code> instance.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.ActivityTypeBuilder
 */

public interface ActivityGroupMemberElementFactory extends ElementFactory<Activity>
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

	public abstract ActivityGroupMember create (ActivityGroup parent, String name);
}
