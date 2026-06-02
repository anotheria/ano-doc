package net.anotheria.asg.service;

import net.anotheria.asg.exception.ASGRuntimeException;

/**
 * The interface for a basic crud service.
 *
 * @author lrosenberg
 * @param <T>
 * @version $Id: $Id
 */
public interface CRUDService<T> {
	/**
	 * Returns an instance of T with the given id.
	 *
	 * @param id the id of the object.
	 * @return T
	 * @throws net.anotheria.asg.exception.ASGRuntimeException if any.
	 */
	T get(String id) throws ASGRuntimeException;
	/**
	 * Deletes the object.
	 *
	 * @param t the object to delete.
	 * @throws net.anotheria.asg.exception.ASGRuntimeException if any.
	 */
	void delete(T t) throws ASGRuntimeException;
	
	/**
	 * Updates an object.
	 *
	 * @param t the object to update.
	 * @return T
	 * @throws net.anotheria.asg.exception.ASGRuntimeException if any.
	 */
	T update(T t) throws ASGRuntimeException;
	
	/**
	 * Creates a new T.
	 *
	 * @param t a T object.
	 * @return T
	 * @throws net.anotheria.asg.exception.ASGRuntimeException if any.
	 */
	T create(T t) throws ASGRuntimeException;
}
