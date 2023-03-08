package net.anotheria.anodoc.util;

import net.anotheria.anodoc.data.IBasicStoreableObject;
import net.anotheria.anodoc.data.ICompositeDataObject;
import net.anotheria.anodoc.data.IPlainDataObject;
import net.anotheria.anodoc.data.Module;
import net.anotheria.anodoc.service.IModuleFactory;
import net.anotheria.anodoc.service.IModuleStorage;
import net.anotheria.anodoc.service.NoStoredModuleEntityException;
import net.anotheria.anodoc.util.storage.IStorage;
import net.anotheria.anodoc.util.storage.StorageFactory;
import net.anotheria.asg.util.listener.IModuleListener;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This storage stores everything in a hashtable and stores this
 * in the plain form (only IPlanDataObject and ICompositeDataObject)
 * in ONE file.
 *
 * @author another
 * @version $Id: $Id
 */
@ConfigureMe(name = "anodoc.storage")
public class CommonHashtableModuleStorage implements IModuleStorage {

    /**
     * {@link Logger} instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonHashtableModuleStorage.class);

    /**
     * Key for storage directory.
     */
    public static final String DEF_KEY_CFG_STORAGE_DIRECTORY = "storage.dir";
    /**
     * Internal storage.
     */
    private Hashtable<String, Module> internalStorage;
    /**
     * Name of the file.
     */
    private String filename;
    /**
     * The factory for modules and documents.
     */
    private IModuleFactory factory;
    /**
     * Configuration key for the storage dir.
     */
    private String cfgKeyStorageDir;
    /**
     * Default name for storage directory.
     */
    public static final String DEF_STORAGE_DIR = ".";
    /**
     * Storage directory path.
     */
    @Configure
    private String storageDir = DEF_STORAGE_DIR;

    /**
     * Period for checking file in milliseconds.
     */
    private static final long FILE_CHECK_PERIOD = 5 * 1000; //todo make it configureable by ConfigureMe
    /**
     * Task for file's changes watching.
     */
    private FileWatcher fileWatchingTimer;

    /**
     * List with listeners. This list is a CopyOnWriteArrayList, hence its safe to add a new listener anytime. However, typically you will add a listener on init of some stuff.
     */
    private List<IModuleListener> listeners = new CopyOnWriteArrayList<IModuleListener>();
    /**
     * Bucket name for asg content.
     */
    @Configure
    private String bucketName;
    /**
     * Google project id.
     */
    @Configure
    private String projectId;
    /**
     * Credential path for google service account file.
     */
    @Configure
    private String credentialsPath;

    /**
     * Storage type.
     */
    @Configure
    private String storageType;
    /**
     * Access key.
     */
    @Configure
    private String accessKey;

    /**
     * Secret key.
     */
    @Configure
    private String secretKey;
    /**
     * External storage.
     */
    private IStorage externalStorage;

    /**
     * <p>Constructor for CommonHashtableModuleStorage.</p>
     *
     * @param aFilename a {@link java.lang.String} object.
     * @param aFactory  a {@link net.anotheria.anodoc.service.IModuleFactory} object.
     */
    public CommonHashtableModuleStorage(String aFilename, IModuleFactory aFactory) {
        this(aFilename, aFactory, DEF_KEY_CFG_STORAGE_DIRECTORY);
    }

    /**
     * <p>Constructor for CommonHashtableModuleStorage.</p>
     *
     * @param aFilename         a {@link java.lang.String} object.
     * @param aFactory          a {@link net.anotheria.anodoc.service.IModuleFactory} object.
     * @param aCfgKeyStorageDir a {@link java.lang.String} object.
     */
    public CommonHashtableModuleStorage(String aFilename, IModuleFactory aFactory, String aCfgKeyStorageDir) {
        internalStorage = new Hashtable<>();
        filename = aFilename;
        factory = aFactory;
        cfgKeyStorageDir = aCfgKeyStorageDir;
        ConfigurationManager.INSTANCE.configure(this);
        externalStorage = StorageFactory.createStorage(storageType, bucketName, credentialsPath, projectId, accessKey, secretKey);
        load();
        startFileWatcherTask();
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCredentialsPath() {
        return credentialsPath;
    }

    public void setCredentialsPath(String credentialsPath) {
        this.credentialsPath = credentialsPath;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Module loadModule(String ownerId, String copyId)
            throws NoStoredModuleEntityException {
        String key = makeKey(ownerId, copyId);
        if (!internalStorage.containsKey(key))
            throw new NoStoredModuleEntityException(key);
        return internalStorage.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveModule(Module module) {
        internalStorage.put(makeKey(module), module);
        save();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteModule(String ownerId, String copyId) {
        String key = makeKey(ownerId, copyId);
        internalStorage.remove(key);
        save();
    }

    @SuppressWarnings("unchecked")
    private void saveObject(String asKey, IBasicStoreableObject o, Hashtable<String, Hashtable> target) {
        if (o instanceof ICompositeDataObject)
            saveComposite(asKey, (ICompositeDataObject) o, target);
        else
            savePlain((IPlainDataObject) o, target);
    }

    @SuppressWarnings("unchecked")
    private void saveComposite(String asKey, ICompositeDataObject c, Hashtable<String, Hashtable> target) {
        Hashtable mySubTarget = new Hashtable();
        //saving subobjects
        Enumeration e = c.getKeys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            IBasicStoreableObject obj = (IBasicStoreableObject) c.getObject(key);
            saveObject(key, obj, mySubTarget);
        }
        target.put(asKey, mySubTarget);

    }


    @SuppressWarnings("unchecked")
    private void savePlain(IPlainDataObject o, Hashtable target) {
        target.put(o.getStorageId(), o);
    }

    private void printObject(IBasicStoreableObject o, int tab) {
        System.out.print(makeTab(tab) + o.getStorageId() + ": ");
        if (o instanceof ICompositeDataObject)
            printComposite((ICompositeDataObject) o, tab);
        else
            printPlain((IPlainDataObject) o);
    }

    private void printComposite(ICompositeDataObject c, int tabs) {
        System.out.println("composite");
        Enumeration<String> e = c.getKeys();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            IBasicStoreableObject obj = (IBasicStoreableObject) c.getObject(key);
            printObject(obj, tabs + 1);
        }
    }

    private void printPlain(IPlainDataObject o) {
        System.out.println(o);
    }

    private String makeTab(int tab) {
        String s = "";
        for (int i = 0; i < tab; i++)
            s += " ";
        return s;
    }


    private String makeKey(Module m) {
        LOGGER.debug("key " + m.getOwnerId() + "#" + m.getCopyId());
        return makeKey(m.getOwnerId(), m.getCopyId());
    }

    private String makeKey(String ownerId, String copyId) {
        return ownerId + "#" + copyId;
    }

    private String parseOwnerId(String key) {
        return key.substring(0, key.indexOf('#'));
    }

    private String parseCopyId(String key) {
        return key.substring(key.indexOf('#') + 1);
    }

    /**
     * <p>getFile.</p>
     *
     * @param aFilename a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    protected String getFilePath(String aFilename) {
        return storageDir + File.separator + aFilename;
    }

    /**
     * Return path for lock file by file name.
     *
     * @param aFilename file name for dat file.
     * @return file name for lock file.
     */
    protected String getFileLock(String aFilename) {
        String fileLock = getFilePath("locks" + File.separator + aFilename);
        return fileLock.substring(0, fileLock.lastIndexOf(".")) + ".lock";
    }

    @SuppressWarnings("unchecked")
    private void save() {
        LOGGER.info("Saving modules...");

        //erstmal konvertieren
        Enumeration<String> allKeys = internalStorage.keys();
        Hashtable<String, Hashtable> toSave = new Hashtable<String, Hashtable>(internalStorage.size());
        while (allKeys.hasMoreElements()) {
            String aKey = allKeys.nextElement();

            Module module = internalStorage.get(aKey);
            Hashtable moduleTarget = new Hashtable();
            saveObject("module", module, moduleTarget);
            Hashtable moduleContainer = (Hashtable) moduleTarget.get("module");
            toSave.put(makeKey(module), moduleContainer);

        }
        externalStorage.save(toSave, getFilePath(filename));
    }

    @SuppressWarnings("unchecked")
    private void load() {
        try {
            Hashtable convertedStorage = externalStorage.load(getFilePath(filename));
            internalStorage.clear();
            Enumeration e = convertedStorage.keys();
            while (e.hasMoreElements()) {
                String aKey = (String) e.nextElement();
                Hashtable holder = (Hashtable) convertedStorage.get(aKey);
                String ownerId = parseOwnerId(aKey);
                String copyId = parseCopyId(aKey);
                Module createdModule = factory.createModule(ownerId, copyId);
                createdModule.setModuleFactory(factory);
                createdModule.fillFromContainer(holder);
                internalStorage.put(aKey, createdModule);
            }
        } catch (FileNotFoundException ignorable) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("FileNotFound " + filename + ", assuming new installation");
            for (String key : internalStorage.keySet()) {
                Module module = internalStorage.get(key);
                Module createdModule = factory.createModule(module.getOwnerId(), module.getCopyId());
                createdModule.setModuleFactory(factory);
                internalStorage.put(key, createdModule);
            }
            save();
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled())
                LOGGER.error("load", e);
        }
    }

    @SuppressWarnings("unused")
    private void printStorage(@SuppressWarnings("unchecked") Hashtable holder) {
        System.err.println("======= PRINT STORAGE =======");
        @SuppressWarnings("unchecked")
        Enumeration<String> e = holder.keys();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            System.err.println("Key:" + key);
            String toPrint = "Key:" + key + " is a ";
            if (KeyUtility.isDocument(key))
                toPrint += "document";
            if (KeyUtility.isList(key))
                toPrint += "list";
            System.err.println(toPrint);
            System.err.println("\t" + holder.get(key));

        }
    }

    /**
     * <p>Setter for the field <code>storageDir</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setStorageDir(String value) {
        storageDir = value;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Adds a service listener to this module storage.
     */
    @Override
    public void addModuleListener(IModuleListener listener) {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Removes the service listener from the module storage.
     */
    @Override
    public void removeModuleListener(IModuleListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fires the module changed event. Exceptions persistence from listeners are ignored (and logged).
     */
    private void firePersistenceChangedEvent() {
        load();

        for (IModuleListener listener : listeners)
            for (Module changed : internalStorage.values())
                try {
                    listener.moduleLoaded(changed);
                } catch (Exception e) {
                    LOGGER.error("Caught uncaught exception by the listener " + listener + ", moduleLoaded(" + changed + ")", e);
                }
    }

    /**
     * Starts file watching task.
     */
    private void startFileWatcherTask() {
        if (fileWatchingTimer != null)
            fileWatchingTimer.stop();

        fileWatchingTimer = new FileWatcher(getFileLock(filename), FILE_CHECK_PERIOD) {
            @Override
            protected void onChange() {
                LOGGER.info("content of modules in " + getFilePath(filename) + " has been changed");
                firePersistenceChangedEvent();
            }
        };

        fileWatchingTimer.start();
    }
}
