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

import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

import com.google.auto.service.AutoService;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * An <code>IdGenerator</code> which returns unique random ID numbers.  The ID
 * numbers are generated using a 64bit cryptographically secure pseudo random
 * number generation algorithm.  All ID's returned by an instance of this class
 * are cached to ensure that no ID number is returned twice.  The user has the
 * option of specifying a <code>Set</code> of previously used id numbers, which
 * the instance will avoid returning.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class RandomIdGenerator implements IdGenerator
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
	 * Dagger Module to create the <code>RandomIdGenerator</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module (includes = {DomainModel.DomainModelModule.class})
	protected static final class IdGeneratorModule
	{
		/**
		 * Create the <code>RandomIdGenerator</code>.
		 *
		 * @param  ids The <code>Collection</code> of used ID numbers, not null
		 * @return     The <code>RandomIdGenerator</code>
		 */

		@Provides
		@GeneratorScope
		public IdGenerator createIdGenerator (final Collection<Long> ids)
		{
			assert ids != null : "ids is NULL";

			return new RandomIdGenerator (ids);
		}
	}

	/**
	 * Representation of the <code>RandomIdGenerator</code> used by the
	 * <code>ServiceLoader</code> to it into the JVM.
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
			return RandomIdGenerator.class;
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
			return RandomIdGenerator.createComponent (model, element);
		}
	}

	/** The <code>Set</code> of previously used id numbers. */
	private Set<Long> usedids;

	/** The random number generator. */
	private SecureRandom generator;

	/**
	 * Create an <code>IdGeneratorComponent</code> for the specified
	 * <code>DomainModel</code>.  This method produces a Dagger Component which
	 * will create and initialize a <code>RandomIdGenerator</code>.
	 *
	 * @param  model   The <code>DomainModel</code>, not null
	 * @param  element The <code>Element</code> class, not null
	 * @return         The <code>IdGeneratorComponent</code>
	 */

	public static IdGeneratorComponent createComponent (
			final DomainModel model,
			final Class<? extends Element> element)
	{
		return DaggerRandomIdGenerator_IdGeneratorComponent.builder ()
			.domainModelModule (new DomainModel.DomainModelModule (element, model))
			.build ();
	}

	/**
	 * Create a new <code>RandomIdGenerator</code>, with a <code>List</code> of
	 * previously used Ids.
	 *
	 * @param ids A <code>Collection</code> containing all of the previously
	 *            used ID numbers, not null
	 */

	private RandomIdGenerator (final Collection<Long> ids)
	{
		assert ids != null : "ids is NULL";

		this.usedids = new HashSet<Long> (ids);
		this.generator = new SecureRandom ();
	}

	/**
	 * Return the next available id number.  This method uses a random number
	 * generator to get the next ID number which it then returns.
	 *
	 * @return A <code>Long</code> containing the next id number
	 */

	@Override
	public Long nextId ()
	{
		Long result = null;

		do
		{
			result = Long.valueOf (this.generator.nextLong ());
		} while ((result < 0) || (! this.usedids.add (result)));

		return result;
	}

	/**
	 * Return the <code>Set</code> of previously used ID numbers.  Includes all
	 * of ID numbers generated by this instance of the
	 * <code>IdGenerator</code>, and previous instances.
	 *
	 * @return The <code>Set</code> of previously used id numbers.
	 */

	public Set<Long> getUsedIds ()
	{
		return new HashSet<Long> (this.usedids);
	}
}
