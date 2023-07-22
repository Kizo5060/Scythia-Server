package com.ruseps.world.content.groupironman.impl;

import com.ruseps.world.content.dialogue.Dialogue;
import com.ruseps.world.content.dialogue.DialogueType;
import com.ruseps.world.entity.impl.player.Player;

public class InvitationDialogue extends Dialogue {

    public InvitationDialogue(Player inviter, Player other) {
        this.inviter = inviter;
        this.other = other;
    }

    private Player inviter, other;

    @Override
    public DialogueType type() {
        return DialogueType.OPTION;
    }


    @Override
    public String[] dialogue() {
        return new String[]{"Join " + inviter.getUsername() + "'s group", "Don't join " + inviter.getUsername() + "'s group."};
    }
    @Override
    public void specialAction() {
        other.setDialogueActionId(395);
    };
}
