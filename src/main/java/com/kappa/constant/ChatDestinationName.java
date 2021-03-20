package com.kappa.constant;

public final class ChatDestinationName {

    public static final String PRIVATE_CHAT = "/amq.direct";

    public static final String GROUP_CHAT = "/group.chat";

    public static final String ERROR = "/error";

    public static final String ONLINE_USER = "/topic/online-user";

    public static final String OFFLINE_USER = "/topic/offline-user";

    public static final String GREETINGS = "/topic/greetings";
}
