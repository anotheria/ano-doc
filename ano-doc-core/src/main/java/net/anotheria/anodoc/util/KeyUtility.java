package net.anotheria.anodoc.util;

import net.anotheria.anodoc.data.IHelperConstants;

/**
 * Some utilities for key handling.
 *
 * @since 1.0
 * @author another
 * @version $Id: $Id
 */
public class KeyUtility {

	private KeyUtility() {
	}

	/**
	 * Extracts the name of the document type (document or list) from the key.
	 *
	 * @return name of the document type
	 * @param key a {@link java.lang.String} object.
	 */
	public static String getDocumentType(String key){
		return key.substring(0, getIndex(key));
	}	
	
	/**
	 * Extracts the name of the document from the key.
	 *
	 * @return name of the document
	 * @param key a {@link java.lang.String} object.
	 */
	public static String getDocumentName(String key){
		return key.substring(getIndex(key)+1);	
	}
	
	/**
	 * Returns the index of the delimiter in given string.
	 * @return index of delimiter
	 */
	private static int getIndex(String key){
		return key.indexOf(IHelperConstants.DELIMITER);
	}
	
	/**
	 * <p>isDocument.</p>
	 *
	 * @return true if the given key represents a document (contains document identifier in the key).
	 * @param key a {@link java.lang.String} object.
	 */
	public static boolean isDocument(String key){
		return getDocumentType(key).equals(IHelperConstants.IDENTIFIER_DOCUMENT);
	}

	/**
	 * <p>isList.</p>
	 *
	 * @return  true if the given key represents a list (contains list identifier in the key).
	 * @param key a {@link java.lang.String} object.
	 */
	public static boolean isList(String key){
		return getDocumentType(key).equals(IHelperConstants.IDENTIFIER_LIST);
	}
	
	/**
	 * <p>getListPos.</p>
	 *
	 * @return  the position of the current document from the listkey.
	 * @param listKey a {@link java.lang.String} object.
	 */
	public static String getListPos(String listKey){
		return listKey.substring(0, getIndex(listKey));
	}
	
	/**
	 * Extracts the document key from the given listkey.
	 *
	 * @param listKey is a document key combined with the list position.
	 * @return document key
	 */
	public static String getKeyFromListKey(String listKey){
		return listKey.substring(getIndex(listKey)+1);	
	}
}
