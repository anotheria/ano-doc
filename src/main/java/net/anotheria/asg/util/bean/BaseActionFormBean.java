package net.anotheria.asg.util.bean;

import net.anotheria.maf.bean.FormBean;

/**
 * Base action form bean.
 *
 * @author ykalapusha
 * since: 05.12.2024
 */
public abstract class BaseActionFormBean implements FormBean {
    /**
     * Is true if the form has been submitted (posted). Needed to prevent F5 hitting.
     */
    private boolean formSubmittedFlag;

    public void setFormSubmittedFlag(boolean formSubmittedFlag ){
        this.formSubmittedFlag = formSubmittedFlag;
    }

    public boolean isFormSubmittedFlag(){
        return formSubmittedFlag;
    }

}
