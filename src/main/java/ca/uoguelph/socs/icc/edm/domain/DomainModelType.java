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

package ca.uoguelph.socs.icc.edm.domain;

/**
 * An enumeration of all of the components of the domain model.  The 
 * enumeration is used to construct the required mappings between the 
 * abstract components of the domain model (interfaces, managers, etc) and
 * their implementations.
 *
 * @author James E. Stark
 * @version 1.0
 */

public enum DomainModelType
{
	/**
	 * The Action interface.
	 */

	ACTION ("Action"),

	/**
	 * The Activity interface.
	 */

	ACTIVITY ("Activity"),

	/**
	 * The ActivitySource Interface.
	 */

	ACTIVITYSOURCE ("ActivitySource"),

	/**
	 * The ActivityType Interface.
	 */

	ACTIVITYTYPE ("ActivityType"),

	/**
	 * The Course interface.
	 */

	COURSE ("Course"),

	/**
	 * The Enrolment interface.
	 */

	ENROLMENT ("Enrolment"),

	/**
	 * The Grade interface.
	 */

	GRADE ("Grade"),

	/**
	 * The LogEntry interface.
	 */

	LOGENTRY ("LogEntry"),

	/**
	 * The Role interface.
	 */

	ROLE ("Role"),

	/**
	 * The User interface.
	 */

	USER ("User");

	/**
	 * The name of the constant (and interface).
	 */

	private final String name;

	/**
	 * Create the Domain Model Type Enum.
	 *
	 * @param name The name of the constant
	 */

	private DomainModelType (String name)
	{
		this.name = name;
	}

	/**
	 * Get a human readable (and printable) version of the constant (it's name)
	 *
	 * @return A string with the constants name.
	 */

	public String getName ()
	{
		return this.name;
	}

	/**
	 * Override java.lang.Object's toString method to output a human readable
	 * version of the constant.
	 *
	 * @return A string with the constants name.
	 */

	@Override
	public String toString ()
	{
		return this.getName ();
	}
}
