package com.sofka.dynamicloading.questions;

import com.sofka.dynamicloading.ui.DynamicLoadingPage;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class HelloWorldPresence implements Question<Boolean> {

    public static HelloWorldPresence inTheDom() {
        return new HelloWorldPresence();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        return !DynamicLoadingPage.FINISH_TEXT.resolveAllFor(actor).isEmpty();
    }
}
