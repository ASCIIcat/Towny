package com.palmergames.bukkit.towny.object.inviteobjects;

import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.command.TownCommand;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.invites.Invite;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translation;

public class PlayerJoinTownInvite implements Invite {

	private final String directSender;
	private final Resident receiver;
	private final Town sender;

	public PlayerJoinTownInvite(String directSender, Town sender, Resident receiver) {
		this.directSender = directSender;
		this.sender = sender;
		this.receiver = receiver;
	}

	@Override
	public String getDirectSender() {
		return directSender;
	}

	@Override
	public Resident getReceiver() {
		return receiver;
	}

	@Override
	public Town getSender() {
		return sender;
	}
	
	@Override
	public void accept() throws TownyException {
		Resident resident = getReceiver();
		Town town = getSender();
		
		TownCommand.townAddResident(town, resident);
		TownyMessaging.sendPrefixedTownMessage(town, Translation.of("msg_join_town", resident.getName()));
		
		resident.deleteReceivedInvite(this);
		town.deleteSentInvite(this);
	}

	@Override
	public void decline(boolean fromSender) {
		Resident resident = getReceiver();
		Town town = getSender();
		
		resident.deleteReceivedInvite(this);
		town.deleteSentInvite(this);
		
		if (!fromSender) {
			TownyMessaging.sendPrefixedTownMessage(town, Translation.of("msg_deny_invite", resident.getName()));
			TownyMessaging.sendMsg(getReceiver(), Translation.of("successful_deny"));
		} else {
			TownyMessaging.sendMsg(resident, Translation.of("town_revoke_invite", town.getName()));
		}
	}
}
