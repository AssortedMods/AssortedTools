package com.grim3212.assorted.suits.client;

import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.suits.client.handlers.ChickenJumpHandler;

public class SuitsClient {

    public static void init() {
        ClientServices.CLIENT.registerClientTickEnd(ChickenJumpHandler::tick);
    }
}
