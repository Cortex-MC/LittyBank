package me.kodysimpson.littybank.commands;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CreateTellerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){

            Player p = (Player) sender;

            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Bank Teller");
            //npc.addTrait();
            npc.spawn(p.getLocation());

        }else{
            System.out.println("Only players can execute this command.");
        }


        return false;
    }

}
