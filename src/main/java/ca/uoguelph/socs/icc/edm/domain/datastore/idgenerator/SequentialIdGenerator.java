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

package ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator;

import java.util.Collection;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

import com.google.auto.service.AutoService;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * An <code>IdGenerator</code> which return ID numbers from a sequence.  ID
 * numbers returned by this <code>IdGenerator</code> come from a sequence which
 * is incremented by one before the new ID number is returned.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class SequentialIdGenerator implements IdGenerator
{
	/**
	 * Dagger Component to create <code>SequentialIdGenerator</code> instances.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@GeneratorScope
	@Component (modules = {IdGeneratorModule.class})
	public static interface IdGeneratorComponent extends IdGenerator.IdGeneratorComponent
	{
		/**
		 * Create the <code>IdGenerator</code>.
		 *
		 * @return The <code>IdGenerator</code>
		 */

		@Override
		public abstract IdGenerator createIdGenerator ();
	}

	/**
	 * Dagger Module to create the <code>SequentialIdGenerator</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module (includes = {DomainModel.DomainModelModule.class})
	protected static final class IdGeneratorModule
	{
		/**
		 * Create the <code>SequentialIdGenerator</code>.
		 *
		 * @param  ids The <code>Collection</code> of used ID numbers, not null
		 * @return     The <code>SequentialIdGenerator</code>
		 */

		@Provides
		@GeneratorScope
		public IdGenerator createIdGenerator (final Collection<Long> ids)
		{
			return new SequentialIdGenerator (ids.parallelStream ()
					.reduce (0L, Long::max)
					.longValue () + 1);
		}
	}

	/**
	 * Representation of the <code>SequentialIdGenerator</code> used by the
	 * <code>ServiceLoader</code> to load it into the JVM.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@AutoService (IdGenerator.Definition.class)
	public static class Definition implements IdGenerator.Definition
	{
		/**
		 * Get the <code>IdGenerator</code> implementation represented by this
		 * <code>Definition</code>.
		 *
		 * @return  The <code>IdGenerator</code> implementation class
		 */

		@Override
		public Class<? extends IdGenerator> getIdGeneratorClass ()
		{
			return SequentialIdGenerator.class;
		}

		/**
		 * Create an <code>IdGeneratorComponent</code> for the specified
		 * <code>DomainModel</code>, and <code>Element</code> class.
		 *
		 * @param  model   The <code>DomainModel</code>, not null
		 * @param  element The <code>Element</code> class, not null
		 * @return         The <code>IdGeneratorComponent</code>
		 */

		@Override
		public IdGeneratorComponent createComponent (
				final DomainModel model,
				final Class<? extends Element> element)
		{
			return SequentialIdGenerator.createComponent (model, element);
		}
	}

	/** The next value to be returned by the generator. */
	private long currentid;

	/**
	 * Create an <code>IdGeneratorComponent</code> for the specified
	 * <code>DomainModel</code>.  This method produces a Dagger Component which
	 * will create and initialize a <code>SequentialIdGenerator</code>.  The
	 * <code>IdGenerator</code> instance is initialized with a
	 * <code>Collection</code> of previously used ID numbers for the specified
	 * <code>Element</code> class.
	 *
	 * @param  model   The <code>DomainModel</code>, not null
	 * @param  element The <code>Element</code> class, not null
	 * @return         The <code>IdGeneratorComponent</code>
	 */

	public static IdGeneratorComponent createComponent (
			final DomainModel model,
			final Class<? extends Element> element)
	{
		return DaggerSequentialIdGenerator_IdGeneratorComponent.builder ()
			.build ();
	}

	/**
	 * Create a new <code>SequentialIdGenerator</code>, with a specified
	 * starting value for the sequence.
	 *
	 * @param  start The initial value for the <code>IdGenerator</code>
	 */

	private SequentialIdGenerator (final long start)
	{
		this.currentid = start;
	}

	/**
	 * Return the next available id number.  This method increments the ID
	 * number sequence and returns the newly calculated number.
	 *
	 * @return A <code>Long</code> containing the next id number
	 */

	@Override
	public Long nextId ()
	{
		Long result = Long.valueOf (this.currentid);

		this.currentid += 1;

		return result;
	}
}
