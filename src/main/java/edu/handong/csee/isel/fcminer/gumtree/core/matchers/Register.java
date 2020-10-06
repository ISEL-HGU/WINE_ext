package edu.handong.csee.isel.fcminer.gumtree.core.matchers;

import edu.handong.csee.isel.fcminer.gumtree.core.gen.Registry;
import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@IndexAnnotated
public @interface Register {
    String id();

    int priority() default Registry.Priority.MEDIUM;

    boolean defaultMatcher() default false;
}
