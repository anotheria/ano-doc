package net.anotheria.asg.util.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * Utility class for populating beans.
 */
public class PopulateUtility {

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(PopulateUtility.class);

    public static void populate(Object bean, HttpServletRequest from) {
        Class c = bean.getClass();
        Field[] fields = c.getDeclaredFields();
        for (Field f:fields){
            log.debug("populating field "+f);
            String value = from.getParameter(f.getName());
            log.debug("  with value "+value);
            if (value == null)
                continue;

            try {
                f.setAccessible(true);
                if (f.getType().equals(java.util.List.class)) {
                    log.debug("list "+f.getName()+", skipped");
                    continue;
                }
                if (f.getType().equals(String.class)) {
                    f.set(bean, value);
                } else if (f.getType().equals(boolean.class)) {
                    if (value !=null && value.equals("on")){ //special handling for checkboxing, on = true, null = false.
                        f.setBoolean(bean, true);
                    } else {
                        f.setBoolean(bean, Boolean.parseBoolean(value));
                    }
                } else if (f.getType().equals(int.class) ) {
                    f.setInt(bean, Integer.parseInt(value));
                } else if (f.getType().equals(float.class) ) {
                    f.setFloat(bean, Float.parseFloat(value));
                } else if (f.getType().equals(double.class) ) {
                    f.setDouble(bean, Double.parseDouble(value));
                } else if (f.getType().equals(long.class)) {
                    f.setLong(bean, Long.parseLong(value));
                } else if (f.getType().equals(Date.class)) {
                    f.set(bean, value);
                } else {
                    throw new RuntimeException("Unsupported type " + f.getType());
                }

            }catch(Exception e){
                log.error("Can't populate "+bean+" with "+from, e);
            }
        }

    }
}
