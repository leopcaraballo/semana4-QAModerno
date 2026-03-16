package com.sofka.dynamicloading.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

import com.sofka.dynamicloading.interactions.DynamicLoadingWaits;
import com.sofka.dynamicloading.models.DynamicLoadingExample;
import com.sofka.dynamicloading.questions.HelloWorldPresence;
import com.sofka.dynamicloading.questions.HelloWorldText;
import com.sofka.dynamicloading.questions.HelloWorldVisibility;
import com.sofka.dynamicloading.questions.LoaderVisibility;
import com.sofka.dynamicloading.tasks.OpenDynamicLoadingExample;
import com.sofka.dynamicloading.tasks.StartDynamicLoading;

import net.serenitybdd.annotations.Managed;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;

@ExtendWith(SerenityJUnit5Extension.class)
class DynamicLoadingTests {

    private static final String EXPECTED_TEXT = "Hello World!";

    @Managed(driver = "chrome")
    WebDriver browser;

    private Actor tester;

    @BeforeEach
    void setUp() {
        tester = Actor.named("Sofka QA");
        tester.can(BrowseTheWeb.with(browser));
    }

    @Test
    @DisplayName("Test 1: Debe mostrar Hello World en el ejemplo 1 con espera explícita")
    void shouldDisplayHelloWorldInExample1() {
        tester.attemptsTo(OpenDynamicLoadingExample.page(DynamicLoadingExample.EXAMPLE_1));

        assertTrue(tester.asksFor(HelloWorldPresence.inTheDom()));
        assertFalse(tester.asksFor(HelloWorldVisibility.displayed()));

        tester.attemptsTo(
                StartDynamicLoading.process(),
            DynamicLoadingWaits.untilLoaderIsVisible(),
            DynamicLoadingWaits.untilLoaderDisappears(),
                DynamicLoadingWaits.untilHelloWorldIsVisible()
        );

        assertEquals(EXPECTED_TEXT, tester.asksFor(HelloWorldText.displayed()));
    }

    @Test
    @DisplayName("Test 2: Debe renderizar Hello World en el ejemplo 2 con espera explícita")
    void shouldRenderHelloWorldInExample2() {
        tester.attemptsTo(OpenDynamicLoadingExample.page(DynamicLoadingExample.EXAMPLE_2));

        assertFalse(tester.asksFor(HelloWorldPresence.inTheDom()));

        tester.attemptsTo(
                StartDynamicLoading.process(),
            DynamicLoadingWaits.untilLoaderIsVisible(),
            DynamicLoadingWaits.untilLoaderDisappears(),
                DynamicLoadingWaits.untilHelloWorldIsVisible()
        );

        assertTrue(tester.asksFor(HelloWorldPresence.inTheDom()));
        assertEquals(EXPECTED_TEXT, tester.asksFor(HelloWorldText.displayed()));
    }

    @Test
    @DisplayName("Test 3: Debe ocultar el loader antes de mostrar Hello World")
    void shouldHideLoaderBeforeShowingHelloWorld() {
        tester.attemptsTo(
                OpenDynamicLoadingExample.page(DynamicLoadingExample.EXAMPLE_1),
                StartDynamicLoading.process(),
                DynamicLoadingWaits.untilLoaderIsVisible(),
                DynamicLoadingWaits.untilLoaderDisappears(),
                DynamicLoadingWaits.untilHelloWorldIsVisible()
        );

        assertFalse(tester.asksFor(LoaderVisibility.displayed()));
        assertTrue(tester.asksFor(HelloWorldVisibility.displayed()));
        assertEquals(EXPECTED_TEXT, tester.asksFor(HelloWorldText.displayed()));
    }
}
