package me.kodysimpson.littybank.configs.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountTierConfig {

    private String displayName;
    private double cost;
    private double interestRate;
    private Material item;

}
