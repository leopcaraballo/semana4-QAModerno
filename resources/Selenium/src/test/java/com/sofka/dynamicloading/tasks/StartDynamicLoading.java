package com.sofka.dynamicloading.tasks;

import com.sofka.dynamicloading.interactions.DynamicLoadingWaits;
import com.sofka.dynamicloading.ui.DynamicLoadingPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;

public class StartDynamicLoading implements Task {

    public static StartDynamicLoading process() {
        return Tasks.instrumented(StartDynamicLoading.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                DynamicLoadingWaits.untilStartButtonIsReady(),
                Click.on(DynamicLoadingPage.START_BUTTON)
        );
    }
}
