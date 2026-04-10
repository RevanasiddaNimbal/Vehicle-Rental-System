package config;

import rental.command.CancelRentalCommand;
import rental.command.RentVehicleCommand;
import rental.command.RentalCommand;
import rental.command.ReturnVehicleCommand;
import rental.model.RentalCommands;

import java.util.HashMap;
import java.util.Map;

public class CommandConfig {
    private final PrinterConfig printerConfig;
    private final ServiceConfig serviceConfig;
    private volatile Map<RentalCommands, RentalCommand> commands;

    public CommandConfig(PrinterConfig printerConfig, ServiceConfig serviceConfig) {
        this.printerConfig = printerConfig;
        this.serviceConfig = serviceConfig;
    }

    public Map<RentalCommands, RentalCommand> getCommands() {
        if (commands == null) {
            synchronized (this) {
                if (commands == null) {
                    Map<RentalCommands, RentalCommand> map = new HashMap<>();

                    map.put(RentalCommands.RENT_COMMAND,
                            new RentVehicleCommand(
                                    serviceConfig.getRentalFacade(),
                                    serviceConfig.getRentalService(),
                                    serviceConfig.getVehicleService(),
                                    serviceConfig.getCustomerService(),
                                    serviceConfig.getInvoiceService(),
                                    serviceConfig.getPaymentStrategyFactory()
                            )
                    );

                    map.put(RentalCommands.RETURN_COMMAND,
                            new ReturnVehicleCommand(
                                    serviceConfig.getRentalFacade(),
                                    serviceConfig.getRentalService(),
                                    printerConfig.getRentalPrinter(),
                                    printerConfig.getPenaltyPrinter()
                            )
                    );

                    map.put(RentalCommands.CANCEL_COMMAND,
                            new CancelRentalCommand(
                                    serviceConfig.getRentalFacade(),
                                    serviceConfig.getRentalService(),
                                    printerConfig.getRentalPrinter()
                            )
                    );

                    commands = map;
                }
            }
        }
        return commands;
    }
}
