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
 *
 * @author  James E. Stark
 * @version 1.0
 */

public interface ActivityTypeBuilder extends ElementBuilder<ActivityType>
{
	/**
	 * Get the name of the <code>ActivityType</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivityType</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>ActivityType</code>.
	 *
	 * @param  name                     The name of the
	 *                                  <code>ActivityType</code>, not null
	 *
	 * @return                          This <code>ActivityTypeBuilder</code>
	 * @throws IllegalArgumentException If the name is an empty
	 */

	public abstract ActivityTypeBuilder setName (String name);

	/**
	 * Get the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @return The <code>ActivitySource</code> instance
	 */

	public abstract ActivitySource getActivitySource ();

	/**
	 * Set the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @param  source                   The <code>ActivitySource</code> for the
	 *                                  <code>ActivityType</code>
	 *
	 * @return                          This <code>ActivityTypeBuilder</code>
	 * @throws IllegalArgumentException If the <code>AcivitySourse</code> does
	 *                                  not exist in the <code>DataStore</code>
	 */

	public abstract ActivityTypeBuilder setActivitySource (ActivitySource source);

	/**
	 * Get the <code>Set</code> of <code>Action</code> instances which are
	 * associated with the <code>ActivityType</code>.  If there are no
	 * associated <code>Action</code> instances, then the <code>Set</code>
	 * will be empty.
	 *
	 * @return A <code>Set</code> of <code>Action</code> instances
	 */

	public abstract Set<Action> getActions ();

	/**
	 * Create an association between the <code>ActivityType</code> and an
	 * <code>Action</code>.
	 *
	 * @param  action                   The <code>Action</code> to be
	 *                                  associated with the
	 *                                  <code>ActivityType</code>, not null
	 * @throws IllegalArgumentException If the <code>Action</code>
	 */

	public abstract ActivityTypeBuilder addAction (Action action);

	/**
	 * Break an association between the <code>ActivityType</code> and an
	 * <code>Action</code>.  To break an association between the
	 * <code>ActivityType</code> and the specified <code>Action</code>, both
	 * the <code>ActivityType</code> and <code>Action</code> must be exist in
	 * the <code>DataStore</code> associated with the
	 * <code>ActivityTypeBuilder</code> that is to break the association.
	 * Furthermore, there must be an existing association between the
	 * <code>ActivityType</code> and the <code>Action</code>, and there must
	 * not exist any log entries containing the specified <code>Action</code>
	 * and an <code>Activity</code> with the specified
	 * <code>ActivityType</code>.
	 *
	 * @param  action The <code>Action</code> to remove from the
	 *                <code>ActivityType</code>, not null
	 */

	public abstract ActivityTypeBuilder removeAction (Action action);
}
