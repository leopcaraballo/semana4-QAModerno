package com.sofka.dynamicloading.questions;

import com.sofka.dynamicloading.ui.DynamicLoadingPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class HelloWorldText implements Question<String> {

    public static HelloWorldText displayed() {
        return new HelloWorldText();
    }

    @Override
    public String answeredBy(Actor actor) {
        return DynamicLoadingPage.FINISH_TEXT.resolveFor(actor).getText().trim();
    }
}
