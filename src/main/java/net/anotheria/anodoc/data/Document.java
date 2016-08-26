package net.anotheria.anodoc.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import net.anotheria.anodoc.util.context.CallContext;
import net.anotheria.anodoc.util.context.ContextManager;
import net.anotheria.util.xml.XMLAttribute;
import net.anotheria.util.xml.XMLNode;

/**
 * This class represents a basic document, which is a container for properties and therefore a
 * corresponding modell object to a simple class (with attributes).
 *
 * @since 1.0
 * @author lrosenberg
 * @version $Id: $Id
 */
public class Document extends DataHolder 
		implements ICompositeDataObject, Cloneable{
	
	/**
	 * svid.
	 */
	private static final long serialVersionUID = -5433016437476873070L;
	
	/**
	 * The internal data storage. The data storage will be parsed by a Storage Service provided with anodoc. The 
	 * Document itself will not be saved, but the data it contains.
	 */
	private Hashtable<String,DataHolder> dataStorage;
	
	/**
	 * Constant used to save type_identifier as internal property name.
	 */
	public static final String PROP_TYPE_IDENTIFIER = "###my_type###";
	
	/**
	 * Constant for property name of the property under which last update timestamp is stored.
	 */
	public static final String PROP_LAST_UPDATE = "###last_update###";
	
	/**
	 * Constant for property name of the property under which Author is stored.
	 */
	public static final String PROP_AUTHOR = "###author###";

	/**
	 * Creates a new Document with given name.
	 *
	 * @param id a {@link java.lang.String} object.
	 */
	public Document(String id){
		super(id);
		dataStorage = new Hashtable<String,DataHolder>();
		putProperty(new StringProperty(IHelperConstants.IDENTIFIER_KEY, IHelperConstants.IDENTIFIER_DOCUMENT));
	}
	
	/**
	 * Creates a new document as a copy of another document.
	 *
	 * @param anotherDocument the document to be copied
	 */
	public Document(Document anotherDocument){
		super("");
		dataStorage = new Hashtable<String,DataHolder>();
		Hashtable<String,DataHolder> srcTable = anotherDocument.dataStorage;
		Enumeration<DataHolder> src = srcTable.elements();
		while(src.hasMoreElements()){
			Property p = (Property)src.nextElement();
			try{
				putProperty((Property)p.clone());
			}catch(CloneNotSupportedException e){
				throw new RuntimeException("Clone failed: "+e.getMessage());
			}
		}
		
	}

	/**
	 * Returns the DataHolder contained in this Document under the given name.
	 * A document can contain properties, documents and lists.
	 *
	 * @see net.anotheria.anodoc.data.NoSuchDataHolderException
	 * @param name of DataHolder
	 * @return DataHolder
	 * @throws net.anotheria.anodoc.data.NoSuchDataHolderException if any.
	 */
	public DataHolder getDataHolder(String name) throws NoSuchDataHolderException{
		DataHolder holder = dataStorage.get(name);
		if (holder==null)
			throw new NoSuchDataHolderException(name);
		return holder;
	} 
	
	/**
	 * Returns the Document contained in this Document under the given name.
	 *
	 * @see net.anotheria.anodoc.data.NoSuchDocumentException
	 * @param name of Document
	 * @return Document
	 * @throws net.anotheria.anodoc.data.NoSuchDocumentException if any.
	 */
	public Document getDocument(String name) throws NoSuchDocumentException{
		try{
			DataHolder holder = getDataHolder(name);
			if (holder instanceof Document)
				return (Document)holder;
		}catch(NoSuchDataHolderException e){}
		throw new NoSuchDocumentException(name);
	}
	
	/**
	 * Returns a list of all contained properties.
	 *
	 * @return properties
	 */
	public List<Property> getProperties(){
		Collection<DataHolder> holders = dataStorage.values();
		List<Property> ret = new ArrayList<Property>();
		Iterator<DataHolder> it = holders.iterator();
		while(it.hasNext()){
			DataHolder h = it.next();
			if (h instanceof Property){
				ret.add((Property)h);
			}
		}
		return ret;
	}
	
	/**
	 * Returns the DocumentList contained in this Document under the given name.
	 *
	 * @param name of DocumentList
	 * @throws net.anotheria.anodoc.data.NoSuchDocumentListException
	 * @return DocumentList
	 * @param <D> a D object.
	 */
	@SuppressWarnings("unchecked")
	public <D extends Document> DocumentList<D> getDocumentList(String name){
		try{
			DataHolder holder = getDataHolder(name);
			if (holder instanceof DocumentList)
				return (DocumentList<D>)holder;
		}catch(NoSuchDataHolderException e){}
		throw new NoSuchDocumentListException(name);
	}
	
	/**
	 * Returns the Property contained in this Document under the given name.
	 *
	 * @param name of property
	 * @return Property
	 * @throws net.anotheria.anodoc.data.NoSuchPropertyException if any.
	 */
	public Property getProperty(String name) throws NoSuchPropertyException{
		try{
			DataHolder holder = getDataHolder(name);
			if (holder instanceof Property)
				return (Property)holder;
		}catch(NoSuchDataHolderException e){}
		throw new NoSuchPropertyException(name,"Property");
	}
	
	/**
	 * Returns the IntProperty contained in this Document under the given name.
	 *
	 * @param name of property
	 * @return IntProperty
	 * @throws net.anotheria.anodoc.data.NoSuchPropertyException if any.
	 */
	public IntProperty getIntProperty(String name) throws NoSuchPropertyException{
		try{
			Property aProperty = getProperty(name);
			if (aProperty instanceof IntProperty)
				return (IntProperty) aProperty;
		}catch(NoSuchDataHolderException e){}
		throw new NoSuchPropertyException(name, "IntProperty");
	}

	/**
	 * Returns the LongProperty contained in this Document under the given name.
	 *
	 * @param name of property
	 * @return LongProperty
	 * @throws net.anotheria.anodoc.data.NoSuchPropertyException if any.
	 */
	public LongProperty getLongProperty(String name) throws NoSuchPropertyException{
		try{
			Property aProperty = getProperty(name);
			if (aProperty instanceof LongProperty)
				return (LongProperty) aProperty;
		}catch(NoSuchDataHolderException e){}
		throw new NoSuchPropertyException(name,"LongProperty");
	}
	
	/**
	 * Returns the StringProperty contained in this Document under the given name.
	 *
	 * @param name of StringProperty
	 * @return StringProperty
	 * @throws net.anotheria.anodoc.data.NoSuchPropertyException if any.
	 */
	public StringProperty getStringProperty(String name) throws NoSuchPropertyException{
		try{
			Property aProperty = getProperty(name);
			if (aProperty instanceof StringProperty)
				return (StringProperty) aProperty;
		}catch(NoSuchDataHolderException e){}
		throw new NoSuchPropertyException(name, "StringProperty");
	}

	/**
	 * Returns the BooleanProperty contained in this Document under the given name.
	 *
	 * @param name of BooleanProperty
	 * @return BooleanProperty
	 * @throws net.anotheria.anodoc.data.NoSuchPropertyException if any.
	 */
	public BooleanProperty getBooleanProperty(String name) throws NoSuchPropertyException{
		try{
			Property aProperty = getProperty(name);
			if (aProperty instanceof BooleanProperty)
				return (BooleanProperty) aProperty;
		}catch(NoSuchDataHolderException e){}
		throw new NoSuchPropertyException(name,"BooleanProperty");
	}

	/**
	 * Returns the ListProperty contained in this Document under the given name.
	 *
	 * @param name of ListProperty
	 * @return ListProperty
	 * @throws net.anotheria.anodoc.data.NoSuchPropertyException if any.
	 */
	public ListProperty getListProperty(String name) throws NoSuchPropertyException{
		try{
			Property aProperty = getProperty(name);
			if (aProperty instanceof ListProperty)
				return (ListProperty) aProperty;
		}catch(NoSuchDataHolderException e){}
		throw new NoSuchPropertyException(name, "ListProperty");
	}
	
	/**
	 * Returns the FloatProperty contained in this Document under the given name.
	 *
	 * @param name of FloatProperty
	 * @return FloatProperty
	 * @throws net.anotheria.anodoc.data.NoSuchPropertyException if any.
	 */
	public FloatProperty getFloatProperty(String name) throws NoSuchPropertyException{
		try{
			Property aProperty = getProperty(name);
			if (aProperty instanceof FloatProperty)
				return (FloatProperty) aProperty;
		}catch(NoSuchDataHolderException e){}
		throw new NoSuchPropertyException(name, "FloatProperty");
	}
	
	/**
	 * Returns the DoubleProperty contained in this Document under the given name.
	 *
	 * @param name of DoubleProperty
	 * @return DoubleProperty
	 * @throws net.anotheria.anodoc.data.NoSuchPropertyException if any.
	 */
	public DoubleProperty getDoubleProperty(String name) throws NoSuchPropertyException{
		try{
			Property aProperty = getProperty(name);
			if (aProperty instanceof DoubleProperty)
				return (DoubleProperty) aProperty;
		}catch(NoSuchDataHolderException e){}
		throw new NoSuchPropertyException(name, "DoubleProperty");
	}

	/**
	 * Returns the DocumentList contained in this Document under the given name in any case,
	 * which means that if no DocumentList is contained it a new will be created.
	 * This function is protected because it implies very much knowledge about the module
	 * structure and shouldn't be called from outside a document.
	 *
	 * @see net.anotheria.anodoc.data.NoSuchDocumentListException
	 * @param name of Document
	 * @return DocumentList
	 * @param <D> a D object.
	 */
	protected <D extends Document> DocumentList<D> getDocumentListAnyCase(String name) {
		try{
			return getDocumentList(name);
		}catch(NoSuchDocumentListException e){
			
		}
		DocumentList<D> newList = createDocumentList(name);
		putList(newList);
		return newList;
	}
	
	/**
	 * Returns the Document contained in this Document under the given name in any case,
	 * which means that if no Document is contained it a new will be created.
	 * This function is protected because it implies very much knowledge about the module
	 * structure and shouldn't be called from outside a document.
	 *
	 * @see net.anotheria.anodoc.data.NoSuchDocumentException
	 * @param name of document
	 * @return Doucument
	 */
	protected Document getDocumentAnyCase(String name) {
		try{
			return getDocument(name);
		}catch(NoSuchDocumentException e){}
		Document newDoc = createDocument(name);
		putDocument(newDoc);
		return newDoc;
	}
	
	/**
	 * <p>getListPropertyAnyCase.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link net.anotheria.anodoc.data.ListProperty} object.
	 */
	protected ListProperty getListPropertyAnyCase(String name){
		 try{
		 	return getListProperty(name);
		 }catch(NoSuchPropertyException e){}
		 ListProperty list = new ListProperty(name);
		 putProperty(list);
		 return list;
	}
	
	/**
	 * Creates a new DocumentList. Overwrite this, if your document
	 * uses special lists.
	 * Called by getDocumentListAnyCase
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param <D> a D object.
	 * @return a {@link net.anotheria.anodoc.data.DocumentList} object.
	 */
	protected <D extends Document> DocumentList<D> createDocumentList(String name){
		return new DocumentList<D>(name);
	}
	
	/**
	 * Creates a new Document. Overwrite this, if your document
	 * uses special subdocuments (which should almost ever be the case).
	 * Called by getDocumentAnyCase
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link net.anotheria.anodoc.data.Document} object.
	 */
	protected Document createDocument(String name){
		return new Document(name);
	}
	
	/**
	 * <p>createListProperty.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link net.anotheria.anodoc.data.ListProperty} object.
	 */
	protected ListProperty createListProperty(String name){
		return new ListProperty(name);
	}

	/**
	 * <p>removeDataHolder.</p>
	 *
	 * @param holder a {@link net.anotheria.anodoc.data.DataHolder} object.
	 */
	protected void removeDataHolder(DataHolder holder){
		dataStorage.remove(holder.getId());
	}
	
	/**
	 * <p>removeDataHolder.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 */
	protected void removeDataHolder(String id){
		dataStorage.remove(id);
	}
	
	/**
	 * Puts the given DataHolder (which can be a document, a list or a property)
	 * in the internal storage.
	 *
	 * @param holder a {@link net.anotheria.anodoc.data.DataHolder} object.
	 */
	protected void addDataHolder(DataHolder holder){
		dataStorage.put(holder.getId(), holder);
	}
	
	/**
	 * Puts the given StringProperty in the internal storage.
	 *
	 * @param p a {@link net.anotheria.anodoc.data.StringProperty} object.
	 */
	public void putStringProperty(StringProperty p){
		putProperty(p);
	}
	
	/**
	 * Puts the given IntProperty in the internal storage.
	 *
	 * @param p a {@link net.anotheria.anodoc.data.IntProperty} object.
	 */
	public void putIntProperty(IntProperty p){
		putProperty(p);
	}

	/**
	 * Puts the given LongProperty in the internal storage.
	 *
	 * @param p a {@link net.anotheria.anodoc.data.LongProperty} object.
	 */
	public void putLongProperty(LongProperty p){
		putProperty(p);
	}

	/**
	 * Puts the given BooleanProperty in the internal storage.
	 *
	 * @param p a {@link net.anotheria.anodoc.data.BooleanProperty} object.
	 */
	public void putBooleanProperty(BooleanProperty p){
		putProperty(p);
	}

	/**
	 * Puts the given ListProperty in the internal storage.
	 *
	 * @param p a {@link net.anotheria.anodoc.data.ListProperty} object.
	 */
	public void putListProperty(ListProperty p){
		putProperty(p);
	}
	
	/**
	 * Puts the given FloatProperty in the internal storage.
	 *
	 * @param p a {@link net.anotheria.anodoc.data.FloatProperty} object.
	 */
	public void putFloatProperty(FloatProperty p){
		putProperty(p);
	}
	
	/**
	 * Puts the given FloatProperty in the internal storage.
	 *
	 * @param p a {@link net.anotheria.anodoc.data.DoubleProperty} object.
	 */
	public void putDoubleProperty(DoubleProperty p){
		putProperty(p);
	}

	/**
	 * Puts the given Property in the internal storage.
	 *
	 * @param p a {@link net.anotheria.anodoc.data.Property} object.
	 */
	public void putProperty(Property p){
		addDataHolder(p);
	}
	
	/**
	 * <p>removeProperty.</p>
	 *
	 * @param p a {@link net.anotheria.anodoc.data.Property} object.
	 */
	public void removeProperty(Property p){
		removeDataHolder(p);
	}
	
	/**
	 * <p>removeProperty.</p>
	 *
	 * @param propertyName a {@link java.lang.String} object.
	 */
	public void removeProperty(String propertyName){
		removeDataHolder(propertyName);
	}

	/**
	 * Puts the given Document in the internal storage.
	 *
	 * @param doc a {@link net.anotheria.anodoc.data.Document} object.
	 */
	public void putDocument(Document doc){
		addDataHolder(doc);
	}
	
	/**
	 * Puts the given DocumentList in the internal storage.
	 *
	 * @param list a {@link net.anotheria.anodoc.data.DocumentList} object.
	 */
	public void putList(DocumentList<? extends Document> list){
		addDataHolder(list);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Returns the string representation of this document.
	 */
	@Override public String toString(){
		return "DOC "+getId()+" "+dataStorage;
	}
	
	///////////// STORAGE /////////////
	
	
	/**
	 * Returns the keys (names) of the contained documents.
	 *
	 * @see net.anotheria.anodoc.data.ICompositeDataObject#getKeys()
	 * @return a {@link java.util.Enumeration} object.
	 */
	public Enumeration<String> getKeys() {
		return dataStorage.keys();
	}

	/**
	 * {@inheritDoc}
	 *
	 * Returns the contained object stored under given key.
	 * @see net.anotheria.anodoc.data.ICompositeDataObject#getObject(String)
	 */
	public Object getObject(String key) {
		return dataStorage.get(key);
	}

	/**
	 * Returns the storage id which should be used by a storage to
	 * store this document.
	 *
	 * @see net.anotheria.anodoc.data.IBasicStoreableObject#getStorageId()
	 * @return a {@link java.lang.String} object.
	 */
	public String getStorageId() {
		return IHelperConstants.IDENTIFIER_DOCUMENT+
				IHelperConstants.DELIMITER+
				getId();
	} 
	
	/////////////////// TYPE IDENTIFIER FOR DATENBANK und FACTORY
	/**
	 * Returns the type identifier of this document.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTypeIdentifier(){
		return getString(PROP_TYPE_IDENTIFIER);
	}

	/**
	 * Sets the type identifier for this document. The type identifier is
	 * especially useful for queries and factory-reassembling.
	 *
	 * @param anIdentifier a {@link java.lang.String} object.
	 */
	public void setTypeIdentifier(String anIdentifier){
		setString(PROP_TYPE_IDENTIFIER, anIdentifier);
	}
	
	//// WEITERE NUETZLICHE FUNKTION ////
	/**
	 * Returns the string value of the according StringProperty,
	 * or empty string (see getEmptyString) if none set.
	 *
	 * @param fieldId a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 * @throws net.anotheria.anodoc.data.NoSuchPropertyException if any.
	 */
	public String getString(String fieldId) throws NoSuchPropertyException{
		try{
			return getStringProperty(fieldId).getString();
		}catch(NoSuchPropertyException e){
			return getEmptyString();
		}
	}
	
	/**
	 * Returns the long value of the according LongProperty,
	 * or an empty long (see getEmptyLong) if none set.
	 *
	 * @return long value
	 * @param fieldId a {@link java.lang.String} object.
	 * @throws net.anotheria.anodoc.data.NoSuchPropertyException if any.
	 */
	public long getLong(String fieldId) throws NoSuchPropertyException{
		try{
			return getLongProperty(fieldId).getlong();
		}catch(NoSuchPropertyException e){
			return getEmptyLong();
		}
	}
	
	/**
	 * Returns the int value of the according IntProperty,
	 * or an empty int (see getEmptyInt) if none set.
	 *
	 * @return int value
	 * @param fieldId a {@link java.lang.String} object.
	 * @throws net.anotheria.anodoc.data.NoSuchPropertyException if any.
	 */
	public int getInt(String fieldId) throws NoSuchPropertyException{
		try{
			return getIntProperty(fieldId).getInt();
		}catch(NoSuchPropertyException e){
			return getEmptyInt();
		}
	}

	/**
	 * Returns list of Property by fieldId.
	 *
	 * @param fieldId fieldId
	 * @return list of Property or empty list if no Property was found by diven fieldId
	 * @throws net.anotheria.anodoc.data.NoSuchPropertyException if any.
	 */
	public List<Property> getList(String fieldId) throws NoSuchPropertyException{
		try{
			return getListProperty(fieldId).getList();
		}catch(NoSuchPropertyException e){
			return getEmptyList();
		}
	}

	/**
	 * Returns the float value of the according FloatProperty,
	 * or an empty float (see getEmptyFloat) if none set.
	 *
	 * @return float value of FloatProperty
	 * @param fieldId a {@link java.lang.String} object.
	 * @throws net.anotheria.anodoc.data.NoSuchPropertyException if any.
	 */
	public float getFloat(String fieldId) throws NoSuchPropertyException{
		try{
			return getFloatProperty(fieldId).getfloat();
		}catch(NoSuchPropertyException e){
			return getEmptyFloat();
		}
	}

	/**
	 * Returns the double value of the according DoubleProperty,
	 * or an empty double (see getEmptyDouble) if none set.
	 *
	 * @return double value of DoubleProperty
	 * @param fieldId a {@link java.lang.String} object.
	 * @throws net.anotheria.anodoc.data.NoSuchPropertyException if any.
	 */
	public double getDouble(String fieldId) throws NoSuchPropertyException{
		try{
			return getDoubleProperty(fieldId).getdouble();
		}catch(NoSuchPropertyException e){
			return getEmptyDouble();
		}
	}

	/**
	 * Returns the boolean value of the according BooleanProperty,
	 * or an empty boolean (see getEmptyBoolean) if none set.
	 *
	 * @return boolean value
	 * @param fieldId a {@link java.lang.String} object.
	 * @throws net.anotheria.anodoc.data.NoSuchPropertyException if any.
	 */
	public boolean getBoolean(String fieldId) throws NoSuchPropertyException{
		try{
			return getBooleanProperty(fieldId).getboolean();
		}catch(NoSuchPropertyException e){
			return getEmptyBoolean();
		}
	}
	
	/**
	 * <p>setList.</p>
	 *
	 * @param fieldId a {@link java.lang.String} object.
	 * @param value a {@link java.util.List} object.
	 */
	public void setList(String fieldId, List<Property> value){
		putListProperty(new ListProperty(fieldId, value));
	}

	/**
	 * Sets a StringProperty with name = fieldId and value = value in this document.
	 *
	 * @param fieldId a {@link java.lang.String} object.
	 * @param value a {@link java.lang.String} object.
	 */
	public void setString(String fieldId, String value){
		putStringProperty(new StringProperty(fieldId, value));
	}
	
	/**
	 * Sets a LongProperty with name = fieldId and value = value in this document.
	 *
	 * @param fieldId a {@link java.lang.String} object.
	 * @param value a long.
	 */
	public void setLong(String fieldId, long value){
		putLongProperty(new LongProperty(fieldId, value));
	}
	
	/**
	 * Sets a IntProperty with name = fieldId and value = value in this document.
	 *
	 * @param fieldId a {@link java.lang.String} object.
	 * @param value a int.
	 */
	public void setInt(String fieldId, int value){
		putIntProperty(new IntProperty(fieldId, value));
	}
	
	/**
	 * Sets a FloatProperty with name = fieldId and value = value in this document.
	 *
	 * @param fieldId a {@link java.lang.String} object.
	 * @param value a float.
	 */
	public void setFloat(String fieldId, float value){
		putFloatProperty(new FloatProperty(fieldId, value));
	}

	/**
	 * Sets a FloatProperty with name = fieldId and value = value in this document.
	 *
	 * @param fieldId a {@link java.lang.String} object.
	 * @param value a double.
	 */
	public void setDouble(String fieldId, double value){
		putDoubleProperty(new DoubleProperty(fieldId, value));
	}

	/**
	 * Sets a BooleangProperty with name = fieldId and value = value in this document.
	 *
	 * @param fieldId a {@link java.lang.String} object.
	 * @param value a boolean.
	 */
	public void setBoolean(String fieldId, boolean value){
		putBooleanProperty(new BooleanProperty(fieldId, value));
	}

	/**
	 * Returns the initial value for a string (empty string - ""). Overwrite it if you wish another values. An empty value will always be returned if
	 * you call get'Type' and the corresponding property doesn't exists.
	 *
	 * @return ""
	 */
	public String getEmptyString() {
		return "";
	}

	/**
	 * Returns the initial value for a long (0). Overwrite it if you wish another values. An empty value will always be returned if
	 * you call get'Type' and the corresponding property doesn't exists.
	 *
	 * @return 0
	 */
	public long getEmptyLong(){
		return 0;
	}
	
	/**
	 * Returns the initial value for a int (0). Overwrite it if you wish another values. An empty value will always be returned if
	 * you call get'Type' and the corresponding property doesn't exists.
	 *
	 * @return 0
	 */
	public int getEmptyInt(){
		return 0;
	}
	
	/**
	 * Returns the initial value for a float (0.0). Overwrite it if you wish another values. An empty value will always be returned if
	 * you call get'Type' and the corresponding property doesn't exists.
	 *
	 * @return 0
	 */
	public float getEmptyFloat(){
		return 0;
	}

	/**
	 * Returns the initial value for a double (0.0). Overwrite it if you wish another values. An empty value will always be returned if
	 * you call get'Type' and the corresponding property doesn't exists.
	 *
	 * @return 0.0
	 */
	public double getEmptyDouble(){
		return 0.0;
	}

	/**
	 * Returns the initial value for a boolean (false). Overwrite it if you wish another values. An empty value will always be returned if
	 * you call get'Type' and the corresponding property doesn't exists.
	 *
	 * @return false
	 */
	public boolean getEmptyBoolean(){
		return false;
	}
	
	/**
	 * <p>getEmptyList.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<Property> getEmptyList(){
		return new ArrayList<Property>();
	}
	
	/**
	 * Returns the cumulative size of the contained DataHolders.
	 *
	 * @see net.anotheria.anodoc.data.DataHolder#getSizeInBytes()
	 * @return size of the contained DataHolders
	 */
	public long getSizeInBytes() {
		int sum = 0;
		Enumeration<DataHolder> values = dataStorage.elements();
		while(values.hasMoreElements()){
			sum += values.nextElement().getSizeInBytes();
		}
		return sum;
	}

	/**
	 * <p>renameTo.</p>
	 *
	 * @param newId a {@link java.lang.String} object.
	 */
	public void renameTo(String newId){
		setId(newId);
	}
	
	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override public Object clone() throws CloneNotSupportedException{
		Object ret = super.clone();
		((Document)ret).dataStorage = (Hashtable<String,DataHolder>)dataStorage.clone();
		return ret;
	}

	/** {@inheritDoc} */
	@Override public XMLNode toXMLNode(){
		XMLNode root = new XMLNode("document");
		
		root.addAttribute(new XMLAttribute("documentId", getId()));
		for (Iterator<DataHolder> it = dataStorage.values().iterator(); it.hasNext();)
			root.addChildNode(it.next().toXMLNode());
		
		return root;
	}
	
	/**
	 * <p>getPropertyValue.</p>
	 *
	 * @param propertyName a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getPropertyValue(String propertyName){
		return getProperty(propertyName).getValue();
	}
	
	/**
	 * <p>setLastUpdateNow.</p>
	 */
	public void setLastUpdateNow(){
		setLong(PROP_LAST_UPDATE, System.currentTimeMillis());
	}
	
	/**
	 * <p>getLastUpdateTimestamp.</p>
	 *
	 * @return a long.
	 */
	public long getLastUpdateTimestamp(){
		return getLong(PROP_LAST_UPDATE); 
	}
	
	/**
	 * <p>setCallContextAuthor.</p>
	 */
	public void setCallContextAuthor(){
		CallContext callContext = ContextManager.getCallContext();
		String author = callContext != null? callContext.getCurrentAuthor(): "UNKNOWN";
		setString(PROP_AUTHOR, author);
	}
	
	/**
	 * <p>getAuthor.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getAuthor(){
		return getString(PROP_AUTHOR); 
	}
	
	/**
	 * <p>copyToStringList.</p>
	 *
	 * @param properties a {@link java.util.List} object.
	 * @return a {@link java.util.List} object.
	 */
	protected List<String> copyToStringList(List<Property> properties){
		ArrayList<String> ret = new ArrayList<String>(properties.size());
		for (Property p : properties)
			ret.add(((StringProperty)p).getString());
		return ret;
	}
	
	/**
	 * <p>copyFromStringList.</p>
	 *
	 * @param strings a {@link java.util.List} object.
	 * @return a {@link java.util.List} object.
	 */
	protected List<Property> copyFromStringList(List<String> strings){
		ArrayList<Property> ret = new ArrayList<Property>();
		for (String s : strings)
			ret.add(new StringProperty(s,s));
		return ret;
	}
	
	/**
	 * <p>copyToIntegerList.</p>
	 *
	 * @param properties a {@link java.util.List} object.
	 * @return a {@link java.util.List} object.
	 */
	protected  List<Integer> copyToIntegerList(List<Property> properties){
		ArrayList<Integer> ret = new ArrayList<Integer>(properties.size());
		for (Property p : properties)
			ret.add(((IntProperty)p).getInt());
		return ret;
	}
	
	/**
	 * <p>copyFromIntegerList.</p>
	 *
	 * @param integers a {@link java.util.List} object.
	 * @return a {@link java.util.List} object.
	 */
	protected List<Property> copyFromIntegerList(List<Integer> integers){
		ArrayList<Property> ret = new ArrayList<Property>();
		for (int i : integers)
			ret.add(new IntProperty(""+ i,i));
		return ret;
	}
	
	/**
	 * <p>copyToLongList.</p>
	 *
	 * @param properties a {@link java.util.List} object.
	 * @return a {@link java.util.List} object.
	 */
	protected  List<Long> copyToLongList(List<Property> properties){
		ArrayList<Long> ret = new ArrayList<Long>(properties.size());
		for (Property p : properties)
			ret.add(((LongProperty)p).getLong());
		return ret;
	}
	
	/**
	 * <p>copyFromLongList.</p>
	 *
	 * @param longs a {@link java.util.List} object.
	 * @return a {@link java.util.List} object.
	 */
	protected List<Property> copyFromLongList(List<Long> longs){
		ArrayList<Property> ret = new ArrayList<Property>();
		for (long l: longs)
			ret.add(new LongProperty(""+ l, l));
		return ret;
	}
	
	/**
	 * <p>copyToBooleanList.</p>
	 *
	 * @param properties a {@link java.util.List} object.
	 * @return a {@link java.util.List} object.
	 */
	protected  List<Boolean> copyToBooleanList(List<Property> properties){
		ArrayList<Boolean> ret = new ArrayList<Boolean>(properties.size());
		for (Property p : properties)
			ret.add(((BooleanProperty)p).getBoolean());
		return ret;
	}
	
	/**
	 * <p>copyFromBooleanList.</p>
	 *
	 * @param booleans a {@link java.util.List} object.
	 * @return a {@link java.util.List} object.
	 */
	protected List<Property> copyFromBooleanList(List<Boolean> booleans){
		ArrayList<Property> ret = new ArrayList<Property>();
		for (boolean b: booleans)
			ret.add(new BooleanProperty(""+ b, b));
		return ret;
	}
	
	/**
	 * <p>copyToDoubleList.</p>
	 *
	 * @param properties a {@link java.util.List} object.
	 * @return a {@link java.util.List} object.
	 */
	protected  List<Double> copyToDoubleList(List<Property> properties){
		ArrayList<Double> ret = new ArrayList<Double>(properties.size());
		for (Property p : properties)
			ret.add(((DoubleProperty)p).getDouble());
		return ret;
	}
	
	/**
	 * <p>copyFromDoubleList.</p>
	 *
	 * @param doubles a {@link java.util.List} object.
	 * @return a {@link java.util.List} object.
	 */
	protected List<Property> copyFromDoubleList(List<Double> doubles){
		ArrayList<Property> ret = new ArrayList<Property>();
		for (double d: doubles)
			ret.add(new DoubleProperty(""+ d, d));
		return ret;
	}
	
	/**
	 * <p>copyToFloatList.</p>
	 *
	 * @param properties a {@link java.util.List} object.
	 * @return a {@link java.util.List} object.
	 */
	protected  List<Float> copyToFloatList(List<Property> properties){
		ArrayList<Float> ret = new ArrayList<Float>(properties.size());
		for (Property p : properties)
			ret.add(((FloatProperty)p).getFloat());
		return ret;
	}
	
	/**
	 * <p>copyFromFloatList.</p>
	 *
	 * @param floats a {@link java.util.List} object.
	 * @return a {@link java.util.List} object.
	 */
	protected List<Property> copyFromFloatList(List<Float> floats){
		ArrayList<Property> ret = new ArrayList<Property>();
		for (float d: floats)
			ret.add(new FloatProperty(""+ d, d));
		return ret;
	}
}
