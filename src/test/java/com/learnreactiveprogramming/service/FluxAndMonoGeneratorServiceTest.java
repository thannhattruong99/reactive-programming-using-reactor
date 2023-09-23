package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

public class FluxAndMonoGeneratorServiceTest {

  FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

  @Test
  void namesFlux() {
    // given

    // when
    var namesFlux = fluxAndMonoGeneratorService.namesFlux();

    // then
    StepVerifier.create(namesFlux).expectNext("alex", "ben", "chloe").verifyComplete();
  }

  @Test
  void namesFlux_map() {
    int stringLength = 3;
    var namesFluxMap = fluxAndMonoGeneratorService.namesFlux_map(stringLength);
    StepVerifier.create(namesFluxMap).expectNext("ALEX", "CHLOE").verifyComplete();
  }

  @Test
  void namesFlux_immutability() {
    // when
    var namesFlux = fluxAndMonoGeneratorService.namesFlux_immutability();

    // then
    StepVerifier.create(namesFlux).expectNext("alex", "ben", "chloe").verifyComplete();
  }

  @Test
  void namesFlux_flatmap() {
    int stringLength = 3;
    var namesFluxFlatMap = fluxAndMonoGeneratorService.namesFlux_flatmap(stringLength);
    StepVerifier.create(namesFluxFlatMap)
        .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
        .verifyComplete();
  }

  @Test
  void namesFlux_flatmap_async() {
    int stringLength = 3;
    var namesFluxFlatMap = fluxAndMonoGeneratorService.namesFlux_flatmap_async(stringLength);
    StepVerifier.create(namesFluxFlatMap)
        //            .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
        .expectNextCount(9)
        .verifyComplete();
  }

  @Test
  void namesFlux_concatmap() {
    int stringLength = 3;
    var namesFluxFlatMap = fluxAndMonoGeneratorService.namesFlux_concatmap(stringLength);
    StepVerifier.create(namesFluxFlatMap)
        .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
        //            .expectNextCount(9)
        .verifyComplete();
  }

  @Test
  void namesMono_flatMap() {
    int stringLength = 3;
    var namesFluxFlatMap = fluxAndMonoGeneratorService.namesMono_flatMap(stringLength);
    StepVerifier.create(namesFluxFlatMap).expectNext(List.of("A", "L", "E", "X")).verifyComplete();
  }

  @Test
  void namesMono_flatMapMany() {
    int stringLength = 3;
    var namesFluxFlatMap = fluxAndMonoGeneratorService.namesMono_flatMapMany(stringLength);
    StepVerifier.create(namesFluxFlatMap).expectNext("A", "L", "E", "X").verifyComplete();
  }

  @Test
  void namesFlux_transform() {
    int stringLength = 3;
    var namesFluxFlatMap = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);
    StepVerifier.create(namesFluxFlatMap)
        .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
        .verifyComplete();
  }

  @Test
  void namesFlux_transform_1() {
    int stringLength = 6;
    var namesFluxFlatMap = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);
    StepVerifier.create(namesFluxFlatMap)
        //            .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
        .expectNext("default")
        .verifyComplete();
  }

  @Test
  void namesFlux_transform_switchifEmpty() {
    int stringLength = 6;
    var namesFluxFlatMap =
        fluxAndMonoGeneratorService.namesFlux_transform_switchifEmpty(stringLength);
    StepVerifier.create(namesFluxFlatMap)
        //            .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
        .expectNext("D", "E", "F", "A", "U", "L", "T")
        .verifyComplete();
  }

  @Test
  void explore_concat() {
    var concatFlux = fluxAndMonoGeneratorService.explore_concat();
    StepVerifier.create(concatFlux).expectNext("A", "B", "C", "D", "E", "F").verifyComplete();
  }

  @Test
  void explore_merge() {
    var concatFlux = fluxAndMonoGeneratorService.explore_merge();
    StepVerifier.create(concatFlux).expectNext("A", "D", "B", "E", "C", "F").verifyComplete();
  }

  @Test
  void explore_mergewith() {
    var concatFlux = fluxAndMonoGeneratorService.explore_mergewith();
    StepVerifier.create(concatFlux).expectNext("A", "D", "B", "E", "C", "F").verifyComplete();
  }

  @Test
  void explore_zip() {
    var concatFlux = fluxAndMonoGeneratorService.explore_zip();
    StepVerifier.create(concatFlux).expectNext("AD", "BE", "CF").verifyComplete();
  }

  @Test
  void explore_zip_1() {
    var concatFlux = fluxAndMonoGeneratorService.explore_zip_1();
    StepVerifier.create(concatFlux).expectNext("AD14", "BE25", "CF36").verifyComplete();
  }

  @Test
  void exception_flux() {
    var value = fluxAndMonoGeneratorService.exception_flux();
    StepVerifier.create(value)
        .expectNext("A", "B", "C")
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  void exception_flux_1() {
    var value = fluxAndMonoGeneratorService.exception_flux();
    StepVerifier.create(value)
        .expectNext("A", "B", "C")
        .expectErrorMessage("Exception Occurred")
        .verify();
  }

  @Test
  void explore_onErrorReturn() {
    var value = fluxAndMonoGeneratorService.explore_onErrorReturn();
    StepVerifier.create(value).expectNext("A", "B", "C", "D").verifyComplete();
  }

  @Test
  void explore_onErrorResume() {
    var e = new IllegalStateException("Not a valid state");
    var value = fluxAndMonoGeneratorService.explore_onErrorResume(e);
    StepVerifier.create(value).expectNext("A", "B", "C", "D", "E", "F").verifyComplete();
  }

  @Test
  void explore_onErrorResume_1() {
    var e = new RuntimeException("Not a valid state");
    var value = fluxAndMonoGeneratorService.explore_onErrorResume(e);
    StepVerifier.create(value)
        .expectNext("A", "B", "C")
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  void explore_onErrorContinue() {
    var value = fluxAndMonoGeneratorService.explore_onErrorContinue();
    StepVerifier.create(value).expectNext("A", "C", "D").verifyComplete();
  }

  @Test
  void explore_onErrorMap() {
    var value = fluxAndMonoGeneratorService.explore_onErrorMap();
    StepVerifier.create(value).expectNext("A").expectError(ReactorException.class).verify();
  }

  @Test
  void explore_doOnError() {
    var value = fluxAndMonoGeneratorService.explore_doOnError();
    StepVerifier.create(value).expectNext("A", "B", "C").expectError(IllegalAccessException.class).verify();
  }

  @Test
  void explore_Mono_doOnError() {
    var value = fluxAndMonoGeneratorService.explore_Mono_doOnError();
    StepVerifier.create(value).expectNext("abc").verifyComplete();
  }
}
