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
import ca.uoguelph.socs.icc.edm.domain.ActivityGroup;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;

/**
 * Supplementary factory interface for adding and removing child activities.
 * This interface specifies the methods required to add and remove child
 * activities, it is intended to be implemented in a class that also implements
 * one of the <code>NamedActivityElementFactory</code>  or 
 * <code>SubActivityElementFactory</code>, interfaces.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     NamedActivityElementFactory
 * @see     ActivityGroupMemberElementFactory
 * @see     ca.uoguelph.socs.icc.edm.domain.ActivityBuilder
 * @see     ca.uoguelph.socs.icc.edm.domain.ActivityGroupMemberBuilder
 */

public interface ActivityGroupElementFactory extends AbstractActivityElementFactory
{
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

	public abstract boolean addChild (ActivityGroup group, ActivityGroupMember member);

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

	public abstract boolean removeChild (ActivityGroup group, ActivityGroupMember member);
}
