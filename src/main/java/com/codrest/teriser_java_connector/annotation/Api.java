package com.codrest.teriser_java_connector.annotation;


import java.lang.annotation.*;

/**
 *
 * Specifies a function to handle messages received via "MessageReceiver".
 * Among the methods specified by @Api, when data specified by the method is received, the method is called.
 * When calling a method, the data of the parameters is called in a pre-injected state, and may be null if the data does not exist.
 * If @Api annotation is not specified, messages are not received even if they are inside a module.
 *
 *
 * @Api
 * public void methodName(){
 *     ...
 * }
 *
 * @Api(name="methodName")
 * public void methodName(ArgumentType1 arg){
 *     ...
 * }
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Api {

    String name() default "";

}
