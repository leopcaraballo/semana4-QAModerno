package com.sofka.dynamicloading.tasks;

import com.sofka.dynamicloading.models.DynamicLoadingExample;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Open;

public class OpenDynamicLoadingExample implements Task {

    private final DynamicLoadingExample example;

    public OpenDynamicLoadingExample(DynamicLoadingExample example) {
        this.example = example;
    }

    public static OpenDynamicLoadingExample page(DynamicLoadingExample example) {
        return Tasks.instrumented(OpenDynamicLoadingExample.class, example);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Open.url(example.url()));
    }
}
