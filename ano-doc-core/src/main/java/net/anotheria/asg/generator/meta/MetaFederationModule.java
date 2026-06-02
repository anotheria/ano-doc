package net.anotheria.asg.generator.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>MetaFederationModule class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaFederationModule extends MetaModule {

	private List<FederatedModuleDef> federatedModules;
	private Map<String, List<FederatedDocumentMapping>> mappings;
	
	/**
	 * <p>Constructor for MetaFederationModule.</p>
	 */
	public MetaFederationModule(){
		this(null);
	}
	
	/**
	 * <p>Constructor for MetaFederationModule.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public MetaFederationModule(String name){
		super(name);
		setStorageType( StorageType.FEDERATION);
		federatedModules = new ArrayList<FederatedModuleDef>();
		mappings = new HashMap<String, List<FederatedDocumentMapping>>();
	}
	
	/**
	 * <p>addFederatedModule.</p>
	 *
	 * @param aKey a {@link java.lang.String} object.
	 * @param aName a {@link java.lang.String} object.
	 */
	public void addFederatedModule(String aKey, String aName){
		federatedModules.add(new FederatedModuleDef(aKey, aName));
	}
	
	
	/**
	 * <p>Getter for the field <code>federatedModules</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<FederatedModuleDef> getFederatedModules() {
		return federatedModules;
	}

	/**
	 * <p>Setter for the field <code>federatedModules</code>.</p>
	 *
	 * @param federatedModules a {@link java.util.List} object.
	 */
	public void setFederatedModules(List<FederatedModuleDef> federatedModules) {
		this.federatedModules = federatedModules;
	}
	
	/**
	 * <p>addMapping.</p>
	 *
	 * @param mapping a {@link net.anotheria.asg.generator.meta.FederatedDocumentMapping} object.
	 */
	public void addMapping(FederatedDocumentMapping mapping){
		List<FederatedDocumentMapping> mappingsForDocument = mappings.get(mapping.getSourceDocument());
		if (mappingsForDocument==null){
			mappingsForDocument = new ArrayList<FederatedDocumentMapping>();
			mappings.put(mapping.getSourceDocument(), mappingsForDocument);
		}
		mappingsForDocument.add(mapping);
	}
	
	/**
	 * <p>getMappingsForDocument.</p>
	 *
	 * @param documentName a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<FederatedDocumentMapping> getMappingsForDocument(String documentName){
		List<FederatedDocumentMapping> ret = mappings.get(documentName);
		return ret == null ? new ArrayList<FederatedDocumentMapping>() : ret;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}
}

