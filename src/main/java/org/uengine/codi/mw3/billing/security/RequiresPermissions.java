package org.uengine.codi.mw3.billing.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermissions {
    /**
     * The permissions
     */
    Permission[] value();

    /**
     * The logical operation for the permission checks in case multiple permissions are specified. AND is the default
     */
    Logical logical() default Logical.AND;
}
