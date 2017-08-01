package code.ponfee.pay;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import code.ponfee.commons.reflect.ClassUtils;
import code.ponfee.commons.resource.ResourceScanner;

public class Test {

    @org.junit.Test
    public void test1() {
        //for (Entry<String, String> entry : new ResourcesScanner("/**/").scan4text("*.properties").entrySet()) {
        //    System.out.println(entry.getValue());
        //}
        
        Set<String> fields;
        Class<?> pre = null;
        for (Class<?> clazz : new ResourceScanner("code.ponfee.pay").scan4class()) {
            fields = new HashSet<>();
            while (!clazz.isInterface() && !clazz.equals(Object.class)) {
                //System.out.println(clazz.getName());
                for (Field field : clazz.getDeclaredFields()) {
                    //if ( Modifier.isStatic(field.getModifiers())) continue;
                    if ("serialVersionUID".equals(field.getName())) continue;
                    
                    if (!fields.add(field.getName())) {
                        System.out.println(ClassUtils.getClassName(pre));
                        System.out.println(ClassUtils.getClassName(clazz));
                        System.out.println(field.toGenericString());
                        System.out.println("======================================\n");
                    }
                }
                
                pre = clazz;
                clazz = clazz.getSuperclass();
            }
        }
        
        
        /*for (Class<?> clazz : new ResourcesScanner("redis.clients").scan4class()) {
            System.out.println(clazz.getName());
        }*/
    }
    
    public static void main(String[] args) {
        String ip = "192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100";
        System.out.println(ip.substring(0, ip.indexOf(",")));
    }
    
}