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

package ca.uoguelph.socs.icc.edm.domain.element.metadata;

import java.util.function.Function;

import ca.uoguelph.socs.icc.edm.domain.Element;

public class MetaDataImpl<T extends Element, U extends T> implements MetaData<T>
{
	private final Definition<T, U> definition;

	protected MetaDataImpl (Definition<T, U> definition)
	{
		this.definition = definition;
	}

	public Class<T> getElementType ()
	{
		return this.definition.getElementType ();
	}

	public Class<U> getElementClass ()
	{
		return this.definition.getElementClass ();
	}

	public Builder<T> getBuilder ()
	{
		return new BuilderImpl<T, U> (this.definition);
	}
}
