/*
 * Author : Kasania
 * Filename : com.codrest.teriser.TeriserJavaConnector
 * Desc :
 */
package com.codrest.teriser_java_connector.core;

import com.codrest.teriser_java_connector.core.net.MessageReceiver;
import org.jetbrains.annotations.NotNull;

public class TeriserJavaConnector {

    public static Teriser Make(@NotNull String token){

        return new Teriser(token, new MessageReceiver());
    }

    public static Teriser Make(@NotNull String token, MessageReceiver messageReceiver){

        return new Teriser(token, messageReceiver);
    }

}
