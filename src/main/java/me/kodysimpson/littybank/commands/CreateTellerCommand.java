package me.kodysimpson.littybank.commands;

import me.kodysimpson.simpapi.command.SubCommand;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateTellerCommand extends SubCommand {


    @Override
    public String getName() {
        return "spawnteller";
    }

    @Override
    public String getDescription() {
        return "Spawns a teller at your current location";
    }

    @Override
    public String getSyntax() {
        return "dwdwdwdwdw";
    }

    @Override
    public void perform(Player p, String[] args) {

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Bank Teller");
        //npc.addTrait();
        npc.spawn(p.getLocation());

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
