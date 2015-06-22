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

import java.util.Map;
import java.util.Set;

import java.util.function.Function;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;

public class MetaData<T extends Element, U extends T>
{
	private final Map<String, Selector<T, U>> selectors;

	private final Definition<T, U> definition;

	private final Class<? extends ElementBuilder<T>> builder;

	protected MetaData (final Definition<T, U> definition, final Class<? extends ElementBuilder<T>> builder)
	{
		this.selectors = null;
		this.definition = definition;
		this.builder = builder;
	}

	/**
	 * Get the Java type of the <code>Element</code> interface.
	 *
	 * @return The <code>Class</code> representing the interface type
	 */

	public Class<T> getElementType ()
	{
		return this.definition.getElementType ();
	}

	/**
	 * Get the Java type of the <code>Element</code> implementation.
	 *
	 * @return The <code>Class</code> representing the implementation type
	 */

	public Class<U> getElementClass ()
	{
		return this.definition.getElementClass ();
	}

	public Class<? extends ElementBuilder<T>> getBuilderClass ()
	{
		return this.builder;
	}

	public Long getId (final U element)
	{
		assert element != null : "element is NULL";

		return element.getId ();
	}

	public void setId (final U element, final Long id)
	{
		assert element != null : "element is NULL";

	}

	public void insertRelationships (final U element)
	{
		assert element != null : "element is NULL";

	}

	public void removeRelationships (final U element)
	{
		assert element != null : "element is NULL";
	}

	public Definition<T, U> getDefinition ()
	{
		return this.definition;
	}

	public Set<Selector<T,U>> getSelectors ()
	{
		return null;
	}

	public Selector<T, U> getSelector (final String name)
	{
		assert name != null : "name is NULL";

		return this.selectors.get (name);
	}
}
