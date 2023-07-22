package com.ruseps.world.content.ice_token_conversion;

import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.content.dialogue.Dialogue;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.dialogue.DialogueType;
import com.ruseps.world.entity.impl.player.Player;

public class IceTokenConversionManager {

    public static boolean onBank(Player player, int itemId) {
        if(itemId == IceTokenConstants.DIAMOND_COIN_ID) {

            long tokensToConvert = player.getInventory().getAmount(IceTokenConstants.DIAMOND_COIN_ID);

            long coinsInInventory = player.getInventory().getAmount(19994);

            long coinsToReceive = tokensToConvert * IceTokenConstants.PRICE_PER_TOKEN;

            long coinsPossible = coinsToReceive + coinsInInventory;

            if(coinsPossible > Integer.MAX_VALUE) {
                tokensToConvert = (Integer.MAX_VALUE - coinsInInventory) / IceTokenConstants.PRICE_PER_TOKEN;

                coinsToReceive = tokensToConvert * IceTokenConstants.PRICE_PER_TOKEN;
            }

            int finalCoinsToReceive = (int) coinsToReceive;
            int finalTokensToConvert = (int) tokensToConvert;
            
            Dialogue TOKENS_TO_COINS_DIALOG = new Dialogue() {

                @Override
                public DialogueType type() {
                    return DialogueType.ITEM_STATEMENT;
                }

                @Override
                public String[] item() {
                    return new String[]{
                            "19992",
                            "180",
                            "Diamond Coins"
                    };
                }

                @Override
                public String[] dialogue() {
                    return new String[]{
                            "Are you sure you want to convert", Misc.format(finalTokensToConvert)+" tokens to "+Misc.formatRunescapeStyle(finalCoinsToReceive)+" coins?",
                    };
                }

                @Override
                public Dialogue nextDialogue() {
                    return new Dialogue() {

                        @Override
                        public DialogueType type() {
                            return DialogueType.OPTION;
                        }

                        @Override
                        public String[] dialogue() {
                            return new String[]{
                                    "Convert "+ Misc.format(finalTokensToConvert)+" tokens to "+Misc.formatRunescapeStyle(finalCoinsToReceive)+" coins",
                                    "Cancel"
                            };
                        }

                        @Override
                        public boolean action(int option) {
                            player.getPA().closeDialogueOnly();

                            if(option == 1) {
                                if (player.getInventory().contains(new Item(IceTokenConstants.DIAMOND_COIN_ID, finalTokensToConvert))) {
                                    player.getInventory().delete(IceTokenConstants.DIAMOND_COIN_ID, finalTokensToConvert);
                                    player.getInventory().add(19994, finalCoinsToReceive);
                                    player.sendMessage("<col=2F519F>You have converted " + Misc.format(finalTokensToConvert) + " Ice Tokens " + Misc.formatRunescapeStyle(finalCoinsToReceive) + " coins.");
                            }
                            if(option == 2) {
                                    player.getPacketSender().sendInterfaceRemoval();                                    }
                            }

                            return false;
                        }

                    };
                }

            };

            DialogueManager.start(player, TOKENS_TO_COINS_DIALOG);
            return true;
        } else if(itemId == 19994) {
            int coinsToConvert = player.getInventory().getAmount(19994);
            int tokensInInventory = player.getInventory().getAmount(IceTokenConstants.DIAMOND_COIN_ID);

            int tokensToReceive = coinsToConvert / IceTokenConstants.PRICE_PER_TOKEN;

            if(tokensToReceive + tokensInInventory > Integer.MAX_VALUE) {
                player.sendMessage("<col=ff0000>You have too many Diamonds already in your inventory to do this.");
                return true;
            }

            int finalTokensToReceive = tokensToReceive;
            int finalCoinsToConvert = coinsToConvert;
            Dialogue COINS_TO_TOKENS_DIALOG = new Dialogue() {

                @Override
                public DialogueType type() {
                    return DialogueType.ITEM_STATEMENT;
                }

                @Override
                public String[] item() {
                    return new String[]{
                            "19994",
                            "180",
                            "Coins"
                    };
                }

                @Override
                public String[] dialogue() {
                    return new String[]{
                            "Are you sure you want to convert", Misc.formatRunescapeStyle(finalCoinsToConvert) + " coins to " + Misc.format(finalTokensToReceive) + " diamonds?",
                    };
                }

                @Override
                public Dialogue nextDialogue() {
                    return new Dialogue() {

                        @Override
                        public DialogueType type() {
                            return DialogueType.OPTION;
                        }

                        @Override
                        public String[] dialogue() {
                            return new String[]{
                                    "Convert " + Misc.formatRunescapeStyle(finalCoinsToConvert) + " coins to " + Misc.format(finalTokensToReceive) + " diamonds",
                                    "Cancel"
                            };
                        }

                        @Override
                        public boolean action(int option) {
                            player.getPA().closeDialogueOnly();

                            if (option == 1) {
                                if (player.getInventory().contains(new Item(19994, finalCoinsToConvert))) {
                                    player.getInventory().delete(19994, finalCoinsToConvert);
                                    player.getInventory().add(IceTokenConstants.DIAMOND_COIN_ID, finalTokensToReceive);
                                    player.sendMessage("<col=2F519F>You have converted " + Misc.formatRunescapeStyle(finalCoinsToConvert) + " coins to " + Misc.format(finalTokensToReceive) + " diamonds.");
                                }
                            }
                            return false;
                        }

                    };
                }
            };

            DialogueManager.start(player, COINS_TO_TOKENS_DIALOG);
            return true;
        }
        return false;
    }


}
