package me.kodysimpson.littybank.models;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public enum AccountTier {

    SILVER, GOLD, PLATINUM;

    private static List<Material> tierMaterials = Arrays.asList(Material.GOLD_INGOT, Material.IRON_INGOT, Material.NETHERITE_INGOT);

    public double getInterestRate(){

        switch (this){
            case SILVER:
                return 0.5;
            case GOLD:
                return 1.0;
            case PLATINUM:
                return 1.5;
            default:
                return 0;
        }

    }

    public double getOpeningFee(){

        switch (this){
            case SILVER:
                return 200;
            case GOLD:
                return 500;
            case PLATINUM:
                return 1000;
            default:
                return 0;
        }

    }

    public String getAsString(){

        switch (this){
            case SILVER:
                return "SILVER";
            case GOLD:
                return "GOLD";
            case PLATINUM:
                return "PLATINUM";
            default:
                return null;
        }

    }

    public Material getAsMaterial(){

        switch (this){
            case SILVER:
                return Material.IRON_INGOT;
            case GOLD:
                return Material.GOLD_INGOT;
            case PLATINUM:
                return Material.NETHERITE_INGOT;
            default:
                return null;
        }

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

    public static boolean isValidTier(ItemStack item) {
        return tierMaterials.contains(item.getType()) && item.getItemMeta().getDisplayName().contains("Tier");
    }


}
