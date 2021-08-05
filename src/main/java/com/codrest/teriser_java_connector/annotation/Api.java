package com.codrest.teriser_java_connector.annotation;


import java.lang.annotation.*;




@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Api {

    String name() default "";

}
