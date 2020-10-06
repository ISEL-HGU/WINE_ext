package edu.handong.csee.isel.fcminer.gumtree.core.gen;

import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@IndexAnnotated
@Target(ElementType.TYPE)
public @interface Register {
    String id();
    String[] accept() default { };
    int priority() default Registry.Priority.MEDIUM;
}
