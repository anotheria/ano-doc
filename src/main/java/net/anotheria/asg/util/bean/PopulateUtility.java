package net.anotheria.asg.util.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

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
        System.out.println(c);
        Field[] fields = c.getDeclaredFields();
        for (Field f:fields){
            System.out.println(f);
            System.out.println(f.getName());
            String value = from.getParameter(f.getName());
            System.out.println("value for "+f.getName()+" is "+value);

            try {
                f.setAccessible(true);
                if (f.getType().equals(String.class)) {
                    f.set(bean, value);
                } else if (f.getType().equals(boolean.class)) {
                    f.setBoolean(bean, Boolean.parseBoolean(value));
                } else if (f.getType().equals(int.class)) {
                    f.setInt(bean, Integer.parseInt(value));
                } else if (f.getType().equals(long.class)) {
                    f.setLong(bean, Long.parseLong(value));
                } else {
                    throw new RuntimeException("Unsupported type " + f.getType());
                }

            }catch(Exception e){
                log.error("Can't populate "+bean+" with "+from, e);
            }
        }

    }
}