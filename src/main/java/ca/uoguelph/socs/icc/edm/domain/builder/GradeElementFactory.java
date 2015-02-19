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

package ca.uoguelph.socs.icc.edm.domain.builder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Grade;

/**
 * Factory interface to create new <code>Grade</code> instances.
 * Implementations of this interface provide the functionality required to
 * create new instances of a class implementing the <code>Grade</code> domain
 * model interface.  Since instances of the <code>Grade</code> interface do not
 * have <code>DataStore</code> ID's, their implementations of the
 * <code>setId</code> method must throw a
 * <code>UnsupportedOperationException</code>
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.GradeBuilder
 * @see     java.lang.UnsupportedOperationException
 */

public interface GradeElementFactory extends ElementFactory<Grade>
{
	/**
	 * Create a new <code>Grade</code> instance.
	 *
	 * @param  enrolment The <code>Enrolment</code> to which the grade is
	 *                   assigned, not null
	 * @param  activity  The <code>Activity</code> for which the grade is
	 *                   assigned, not null
	 * @param  grade     The assigned grade, on the interval [0, 100], not null
	 *
	 * @return           The new <code>Grade</code> instance
	 */

	public abstract Grade create (Enrolment enrolment, Activity activity, Integer grade);
}
