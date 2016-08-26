package net.anotheria.asg.util.locking.config;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.LoggerFactory;

/**
 * Configuration for  Locking.
 *
 * Actually   contains  'autolocking' property  and  timeout.
 *
 * @author: h3llka
 * @author another
 * @version $Id: $Id
 */
@ConfigureMe(name = "lockingconfig")
public class LockingConfig {

	/**
	 * LockingConfig "autolocking" - is Autolocking enabled.
	 */
	@Configure
	private boolean autolocking;
	/**
	 * LockingConfig "timeout" - unlock  timeout.
	 */
	@Configure
	private long timeout;

	/**
	 * Actually getInstance method.
	 *
	 * @return Instance of LockingConfig
	 */
	public static LockingConfig getInstance() {
		return LockingConfigInstanceHolder.instance;
	}

	private LockingConfig() {
		this.autolocking = false;
		this.timeout = 0;
	}

	/**
	 * <p>isAutolocking.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isAutolocking() {
		return autolocking;
	}

	/**
	 * <p>Setter for the field <code>autolocking</code>.</p>
	 *
	 * @param autolocking a boolean.
	 */
	public void setAutolocking(boolean autolocking) {
		this.autolocking = autolocking;
	}

	/**
	 * <p>Getter for the field <code>timeout</code>.</p>
	 *
	 * @return a long.
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * <p>Setter for the field <code>timeout</code>.</p>
	 *
	 * @param timeout a long.
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
	private static class LockingConfigInstanceHolder{
		private static final LockingConfig instance = new LockingConfig();
		static{
			try{
				ConfigurationManager.INSTANCE.configure(instance);
			}catch(Exception e){
				try{
					LoggerFactory.getLogger(LockingConfig.class).warn("Couldn't configure LockingConfig, stick to defaults: " + instance);
				}catch(Exception ignoredlockingexception){
					//ignored
				}
			}
		}
	}
}
