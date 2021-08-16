package me.kodysimpson.littybank.models;

import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.configs.models.AccountTierConfig;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum AccountTier {

    SILVER, GOLD, PLATINUM;

    public String getAsString(){

        return switch (this) {
            case SILVER -> "SILVER";
            case GOLD -> "GOLD";
            case PLATINUM -> "PLATINUM";
        };

    }

    public Material getAsMaterial(){

        return switch (this) {
            case SILVER -> Material.IRON_INGOT;
            case GOLD -> Material.GOLD_INGOT;
            case PLATINUM -> Material.NETHERITE_INGOT;
            default -> null;
        };

    }

    public static AccountTier matchTier(String s) {
        switch (s) {
            case "SILVER":
                return SILVER;
            case "GOLD":
                return GOLD;
            case "PLATINUM":
                return PLATINUM;
            default:
                return null;
        }
    }

    public static AccountTier matchTier(Material m) {
        switch (m) {
            case IRON_INGOT:
                return SILVER;
            case GOLD_INGOT:
                return GOLD;
            case NETHERITE_INGOT:
                return PLATINUM;
            default:
                return null;
        }
    }

    private static List<Material> getTierItems(){
        return List.of(LittyBank.getPlugin().getAccountConfig().getSavingsAccountTiers().get(AccountTier.SILVER).getItem(),
                LittyBank.getPlugin().getAccountConfig().getSavingsAccountTiers().get(AccountTier.GOLD).getItem(),
                LittyBank.getPlugin().getAccountConfig().getSavingsAccountTiers().get(AccountTier.PLATINUM).getItem());
    }

    public static boolean isValidTier(ItemStack item) {
        return getTierItems().contains(item.getType()) && item.getItemMeta().getDisplayName().contains("Tier");
    }

    public static AccountTier getTierFromItem(ItemStack item) {

        for (Map.Entry<AccountTier, AccountTierConfig> configEntry : LittyBank.getPlugin().getAccountConfig().getSavingsAccountTiers().entrySet()) {
            if (configEntry.getValue().getItem() == item.getType()) {
                return configEntry.getKey();
            }
        }
        return null;
    }
}
