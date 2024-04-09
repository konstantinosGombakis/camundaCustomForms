package com.camunda;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

public class TemperatureTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.getExecution().setVariable("assignee", delegateTask.getAssignee());
    }
}
