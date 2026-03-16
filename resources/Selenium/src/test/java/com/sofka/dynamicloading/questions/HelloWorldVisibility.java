package com.sofka.dynamicloading.questions;

import com.sofka.dynamicloading.ui.DynamicLoadingPage;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class HelloWorldVisibility implements Question<Boolean> {

    public static HelloWorldVisibility displayed() {
        return new HelloWorldVisibility();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            return DynamicLoadingPage.FINISH_TEXT.resolveFor(actor).isVisible();
        } catch (Exception e) {
            return false;
        }
    }
}
