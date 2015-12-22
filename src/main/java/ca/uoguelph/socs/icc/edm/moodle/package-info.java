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

/**
 * Convert the data in the Moodle database to completely match the domain model.
 * The Moodle database stores some of its data in ways that are impossible to
 * directly load into the <code>DomainModel</code>.  The classes in this package
 * are responsible for performing post-load conversion on the Moodle data to
 * make it match the domain model.
 *
 * @author  James E. Stark
 * @version 1.0
 */

@ParametersAreNonnullByDefault
package ca.uoguelph.socs.icc.edm.moodle;

import javax.annotation.ParametersAreNonnullByDefault;
