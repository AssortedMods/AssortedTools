package com.grim3212.assorted.suits.client;

import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.suits.client.handlers.ChickenJumpHandler;
import com.grim3212.assorted.suits.config.SuitsClientConfig;

public class SuitsClient {

    public static final SuitsClientConfig CLIENT_CONFIG = new SuitsClientConfig();

    public static void init() {
        ClientServices.CLIENT.registerClientTickEnd(ChickenJumpHandler::tick);
    }
}
