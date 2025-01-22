package net.anotheria.asg.service;

import net.anotheria.asg.exception.ASGRuntimeException;

/**
 * <p>Abstract BaseFixtureService class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public abstract class BaseFixtureService extends AbstractASGService implements IFixtureService{

	/** {@inheritDoc} */
	@Override
	public void setUp() {
		reset();
	}

	/** {@inheritDoc} */
	@Override
	public void tearDown() {
	}

	@Override
	public void purgeLanguageFromAllObjects(String language) throws ASGRuntimeException {
		// Should be overriden.
	}
}
