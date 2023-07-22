package com.ruseps.world.content.fuser;

import com.ruseps.model.Item;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.world.entity.impl.player.Player;

public enum FuserEnum {

    TEST(new Item[] {new Item(18958,1),new Item(18961,1),new Item(18960,1),new Item(18957,1),new Item(18963,1),new Item(18962,1),},2821),
    GOD(new Item[] {new Item(20658,1),new Item(20659,1),new Item(20660,1),},20657),
    BURNING(new Item[] {new Item(20658,1),new Item(20659,1),new Item(20660,1),new Item(20657,2),new Item(8788,1),new Item(19888,1),},1002),
    THANOS(new Item[] {new Item(20656,1),new Item(8788,5),new Item(19992,1000),},13080),
    OBBY(new Item[] {new Item(21071,1),new Item(8788,5),new Item(19992,1000),},13081),
    BOOSTER(new Item[] {new Item(18943,1),new Item(18947,1),new Item(18948,1),new Item(8788,40),},18941),
    GODROW(new Item[] {new Item(2572,1),new Item(10480,1),new Item(11005,1),new Item(8788,10),},2709),;


    private FuserEnum( Item[] requirements, int endItem) {
        this.requirements = requirements;
        this.endItem = endItem;
    }

    Item[] requirements;
    int endItem;


    public Item[] getRequirements() {
        return requirements;
    }

    public int getEndItem() {
        return endItem;
    }

    public static boolean checkRequirements(FuserEnum combine, Player player){
      Item[] reqs = combine.getRequirements();
      for(Item req : reqs) {
          if(!player.getInventory().contains(new Item[] {req})) {
              return false;
          }
      }
      return true;
    }
    
    public static void removeRequirements(FuserEnum combine, Player player){
      Item[] reqs = combine.getRequirements();
      for(Item req : reqs) {
          if(player.getInventory().contains(new Item[] {req})) {
              player.getInventory().delete(req.getId(),req.getAmount());
              player.sendMessage("@bla@ Removed "+req.getAmount()+"x "+ ItemDefinition.forId(req.getId()).getName() + " From your inventory!");
          }
      }
    }
}
