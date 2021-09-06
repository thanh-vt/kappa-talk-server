package com.kappa.constant;

/**
  * @created 25/04/2021 - 12:52:14 SA
  * @project vengeance
  * @author thanhvt
  * @description
  * @since 1.0
**/
public final class ChatDestinationName {

    public static final String PRIVATE_CHAT = "/amq.direct";

    public static final String GROUP_CHAT = "/group.chat";

    public static final String ERROR = "/error";

    public static final String ONLINE_USER = "/topic/online-user";

    public static final String OFFLINE_USER = "/topic/offline-user";

    public static final String GREETINGS = "/topic/greetings";
}
