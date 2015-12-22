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
 * Dummy <code>DataStore</code> implementation.  The classes in this package
 * provide an implementation of the <code>DataStore</code> which does nothing.
 * A <code>Query</code> will always return no results, and insert is a no-op.
 * The Dummy implementation is intended to be used for testing other pieces of
 * code.
 *
 * @author  James E. Stark
 * @version 1.0
 */

@ParametersAreNonnullByDefault
package ca.uoguelph.socs.icc.edm.domain.datastore.dummy;

import javax.annotation.ParametersAreNonnullByDefault;
