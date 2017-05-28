package VO;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.UUID;

/**
 *
 * @author Radek
 */
public class BaseVO {
    
    /**
     * Return "s".
     * @param s
     * @return 
     */
    protected String u(String s) {
        return '"' + s + '"';
    }
    
    protected String jsonProperty(String key, String val) {
        return u(key) + ": " + val;
    }
    
    protected String jsonProperty(String key, long val) {
        return u(key) + ": " + val;
    }
    
    protected String jsonProperty(String key, Timestamp val) {
        return u(key) + ": " + val.toString();
    }

    @Override
    public String toString() {
        String JSON = "{ ";
        
        boolean isFirst = true;
        boolean useCommas;
        
        for(Field var : this.getClass().getFields()){
            if(Modifier.isStatic(var.getModifiers()) || Modifier.isPrivate(var.getModifiers())) {
                continue;
            }
            
            if(isFirst) {
                isFirst = false;
            } else {
                JSON += ",\n";
            }
            
            Object type = var.getType();
            
            if(type.equals(String.class) || type.equals(UUID.class) || type.equals(Timestamp.class)){
                useCommas = true;
            } else if(type.equals(int.class) || type.equals(long.class)){
                useCommas = false;
            } else {
                useCommas = true;
            }
            
            //System.out.println(var.getGenericType() + " " + var.getType().equals(String.class) + " a " + var.getType().getComponentType() );
            try {
                if(useCommas && var.get(this) != null){
                    JSON += u(var.getName()) + ": " + u(var.get(this) + "");
                } else {
                    JSON += u(var.getName()) + ": " + var.get(this);
                }
                
            }catch (IllegalArgumentException ex) {
                
            } catch (Exception e) {
                
            }
        }
        
        JSON += " }";
        
        return JSON;
    }
}
