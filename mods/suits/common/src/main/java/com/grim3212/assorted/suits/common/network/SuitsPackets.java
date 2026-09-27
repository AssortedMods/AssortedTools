package com.grim3212.assorted.suits.common.network;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.platform.services.INetworkHelper;
import com.grim3212.assorted.suits.Constants;
import net.minecraft.resources.Identifier;

public class SuitsPackets {

    public static void init() {
        Services.NETWORK.register(new INetworkHelper.MessageHandler<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "chicken_suit_jump"), ChickenSuitUpdatePacket.class,
                ChickenSuitUpdatePacket::encode, ChickenSuitUpdatePacket::decode, ChickenSuitUpdatePacket::handle, INetworkHelper.MessageBoundSide.SERVER));
    }
}
