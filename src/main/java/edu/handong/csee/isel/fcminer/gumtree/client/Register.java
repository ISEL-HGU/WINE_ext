package edu.handong.csee.isel.fcminer.gumtree.client;

import org.atteo.classindex.IndexAnnotated;

import edu.handong.csee.isel.fcminer.gumtree.core.gen.Registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@IndexAnnotated
public @interface Register {
    String name() default no_value;
    String description() default "";
    int priority() default Registry.Priority.MEDIUM;

    // FIXME currently unused, will be useful only for help purpose
    Class<? extends Option.Context> options() default NoOption.class;

    String no_value = "";
    class NoOption implements Option.Context {
        @Override
        public Option[] values() {
            return new Option[]{};
        }
    }
}
