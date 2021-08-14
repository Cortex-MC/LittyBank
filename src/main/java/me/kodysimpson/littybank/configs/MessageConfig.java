package me.kodysimpson.littybank.configs;

import lombok.Getter;
import me.kodysimpson.simpapi.config.Config;
import me.kodysimpson.simpapi.config.ConfigManager;

@Getter
@Config(fileName = "messages", fileType = ConfigManager.FileType.JSON)
public class MessageConfig {

    private final String cantAfford = "&cYou can't afford this you broke bitch.";
    private final String transactionError = "Transaction Error. Try again later.";
    private final String poopSauce = "this is poop sauce";
    private final String wow = "weewfew";

    @Override
    public String toString() {
        return "MessageConfig{" +
                "cantAfford='" + cantAfford + '\'' +
                ", transactionError='" + transactionError + '\'' +
                ", poopSauce='" + poopSauce + '\'' +
                ", wow='" + wow + '\'' +
                '}';
    }
}
