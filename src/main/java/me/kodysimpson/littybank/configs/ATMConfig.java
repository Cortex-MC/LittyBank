package me.kodysimpson.littybank.configs;

import lombok.Getter;
import me.kodysimpson.littybank.models.FeeType;
import me.kodysimpson.simpapi.config.Config;
import me.kodysimpson.simpapi.config.ConfigManager;

@Config(fileName = "atm", fileType = ConfigManager.FileType.YAML)
@Getter
public class ATMConfig {

    private final double atmCost = 10000.0;

    private final FeeType feeType = FeeType.RATE;
    private final double feeRate = 0.25;
    private final double fixedFeeAmount = 3.0;

}