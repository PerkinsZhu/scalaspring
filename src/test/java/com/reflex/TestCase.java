package com.reflex;

import com.beans.Person;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * Created by PerkinsZhu on 2018/7/4 19:23
 **/
public class TestCase {

    @Test
    public void testReflex() throws Exception {
        Class clazz = Class.forName("com.beans.Person");
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            print(method.isAccessible() + "--->" + method.getName());
            method.setAccessible(true);
            print(method.isAccessible() + "--->" + method.getName());
        }
    }

    public void print(Object obj) {
        System.out.println(obj.toString());
    }
}
