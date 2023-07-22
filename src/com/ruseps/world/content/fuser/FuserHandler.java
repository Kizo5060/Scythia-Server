package com.ruseps.world.content.fuser;

import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.world.entity.impl.player.Player;

public class FuserHandler {

    public static void openInterface(FuserEnum combine, Player player) {
        resetInterface(player);
        fillList(player);
        fillInterface(combine,player);
        player.getPacketSender().sendInterface(43500);
    }

    public static void resetInterface(Player player){

        int interfaceId = 43513;
        for(int i = 0; i < 20; i++) {
            player.getPacketSender().sendItemOnInterface(interfaceId,0,0);
            interfaceId++;
        }
        player.getPacketSender().sendItemOnInterface(43537,0,0);

         interfaceId = 43502;
        for(int i = 0; i < 10; i++) {
            player.getPacketSender().sendString(interfaceId,"");
            interfaceId++;
        }
    }

    public static void fillList(Player player){
        int interfaceId = 43502;
        for(FuserEnum combine : FuserEnum.values()) {
            player.getPacketSender().sendString(interfaceId,""+ItemDefinition.forId(combine.getEndItem()).getName());
            interfaceId++;
        }
    }

    public static void fillInterface(FuserEnum combineEnum, Player player){

        int interfaceId = 43513;
        for(int i = 0; i < combineEnum.requirements.length; i++) {
            player.getPacketSender().sendItemOnInterface(interfaceId,combineEnum.requirements[i].getId(),combineEnum.requirements[i].getAmount());
            interfaceId++;
        }

        player.getPacketSender().sendItemOnInterface(43537,combineEnum.getEndItem(),1);

    }
}
