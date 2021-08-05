/*
 * Author : Kasania
 * Filename : MessageReceiver
 * Desc :
 */
package com.codrest.teriser_java_connector.core.net;

import java.util.function.Function;

public class MessageReceiver {
    private Function<String,String> messageExecutor;
    /*

     */
    public String onMessageReceived(String formattedJson){
        return messageExecutor.apply(formattedJson);
    }

    public void setMessageExecutor(Function<String, String> messageExecutor) {
        this.messageExecutor = messageExecutor;
    }
}
