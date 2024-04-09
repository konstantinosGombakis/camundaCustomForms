package com.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculateVariables implements JavaDelegate {
    private final Double DEFAULT_WATER_AMOUNT = 28.3;
    private final Double DEFAULT_INDICATOR = 17.2;

    protected final static Logger LOGGER = LoggerFactory.getLogger(CalculateVariables.class);

    @Override
    public void execute(DelegateExecution delegateExecution) {

        Double weightInKilos = null;
        if (delegateExecution.getVariable("weight_in_kilos") != null) {
            weightInKilos = Double.parseDouble(delegateExecution.getVariable("weight_in_kilos").toString());
        }

        Double water = null;
        if (delegateExecution.getVariable("water") != null) {
            water = Double.parseDouble(delegateExecution.getVariable("water").toString());
        }

        Double indicator = null;
        if (delegateExecution.getVariable("indicator") != null) {
            indicator = Double.parseDouble(delegateExecution.getVariable("indicator").toString());
        }

        if (water == null) {
            water = DEFAULT_WATER_AMOUNT;
        }

        if (indicator == null) {
            indicator = DEFAULT_INDICATOR;
        }

        LOGGER.info("weightInKilos " + weightInKilos);
        LOGGER.info("water " + water);
        LOGGER.info("indicator " + indicator);


        delegateExecution.setVariable("water", water.toString());
        delegateExecution.setVariable("indicator", indicator.toString());

        Double calculated_weight = (weightInKilos + (indicator * weightInKilos) / 100) + water;
        delegateExecution.setVariable("calculated_weight", String.format("%.2f", calculated_weight));


        LOGGER.info("accept " + delegateExecution.getVariable("accept"));


    }
}
