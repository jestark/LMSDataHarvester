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

package ca.uoguelph.socs.icc.edm.domain.factory;

import ca.uoguelph.socs.icc.edm.domain.AbstractManagerFactory;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityManager;
import ca.uoguelph.socs.icc.edm.domain.DomainModelType;

public final class ActivityFactory extends AbstractManagerFactory<Activity, ActivityManager>
{
	private static ActivityFactory instance;

	static
	{
		ActivityFactory.instance = null;
	}

	public static ActivityFactory getInstance ()
	{
		if (ActivityFactory.instance == null)
		{
			ActivityFactory.instance = new ActivityFactory ();
		}

		return ActivityFactory.instance;
	}

	private ActivityFactory ()
	{
		super (DomainModelType.ACTIVITY);
	}
}