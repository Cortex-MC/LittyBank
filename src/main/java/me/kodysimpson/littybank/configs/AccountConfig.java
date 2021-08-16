package me.kodysimpson.littybank.configs;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import me.kodysimpson.littybank.configs.models.AccountTierConfig;
import me.kodysimpson.littybank.models.AccountTier;
import me.kodysimpson.simpapi.config.Config;
import me.kodysimpson.simpapi.config.ConfigManager;
import org.bukkit.Material;

import java.util.Map;

@Getter
@Config(fileName = "accounts", fileType = ConfigManager.FileType.YAML)
public class AccountConfig {

    private final Map<AccountTier, AccountTierConfig> savingsAccountTiers = ImmutableMap.<AccountTier, AccountTierConfig>builder()
            .put(AccountTier.SILVER, new AccountTierConfig("&#828282&lSilver Tier", 200.0, 0.05, Material.IRON_INGOT))
            .put(AccountTier.GOLD, new AccountTierConfig("&#767718&lGold Tier", 10000.0, 0.50, Material.GOLD_INGOT))
            .put(AccountTier.PLATINUM, new AccountTierConfig("&#6295c6&lPlatinum Tier", 200.0, 1.0, Material.NETHERITE_INGOT))
            .build();

    private final long compoundingPeriodSeconds = 3600; //1 hour
    private final boolean alertInterestGained = true;

}
