package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

@Slf4j
public class FluxAndMonoGeneratorService {
  public Flux<String> namesFlux() {
    return Flux.fromIterable(List.of("alex", "ben", "chloe")).log();
  }

  public Flux<String> namesFlux_map(int stringLength) {
    return Flux.fromIterable(List.of("alex", "ben", "chloe"))
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .doOnNext(System.out::println)
        .doOnSubscribe(s -> System.out.println("Subscription is: " + s))
        .doOnComplete(
            () -> {
              System.out.println("Inside the complete callback");
            })
        .doFinally(signalType -> System.out.println("Inside doFinally: " + signalType))
        .log();
  }

  public Flux<String> namesFlux_immutability() {
    var namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));
    namesFlux.map(String::toUpperCase).log();
    return namesFlux;
  }

  public Mono<List<String>> namesMono_flatMap(int stringLength) {
    return Mono.just("alex")
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .flatMap(this::splitStringMono)
        .log();
  }

  public Flux<String> namesMono_flatMapMany(int stringLength) {
    return Mono.just("alex")
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .flatMapMany(this::splitString)
        .log();
  }

  private Mono<List<String>> splitStringMono(String s) {
    var charArray = s.split("");
    return Mono.just(List.of(charArray));
  }

  public Flux<String> namesFlux_flatmap(int stringLength) {
    return Flux.fromIterable(List.of("alex", "ben", "chloe"))
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .flatMap(this::splitString)
        .log();
  }

  public Flux<String> namesFlux_flatmap_async(int stringLength) {
    return Flux.fromIterable(List.of("alex", "ben", "chloe"))
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .flatMap(this::splitString_withDelay)
        .log();
  }

  public Flux<String> namesFlux_concatmap(int stringLength) {
    return Flux.fromIterable(List.of("alex", "ben", "chloe"))
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .concatMap(this::splitString_withDelay)
        .log();
  }

  public Flux<String> namesFlux_transform(int stringLength) {
    Function<Flux<String>, Flux<String>> filterMap =
        name -> name.map(String::toUpperCase).filter(s -> s.length() > stringLength);
    return Flux.fromIterable(List.of("alex", "ben", "chloe"))
        .transform(filterMap)
        .flatMap(this::splitString)
        .defaultIfEmpty("default")
        .log();
  }

  public Flux<String> namesFlux_transform_switchifEmpty(int stringLength) {
    Function<Flux<String>, Flux<String>> filterMap =
        name ->
            name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitString);
    var defaultFlux = Flux.just("default").transform(filterMap);
    return Flux.fromIterable(List.of("alex", "ben", "chloe"))
        .transform(filterMap)
        .flatMap(this::splitString)
        .switchIfEmpty(defaultFlux)
        .log();
  }

  public Flux<String> explore_concat() {
    var abc = Flux.just("A", "B", "C");
    var def = Flux.just("D", "E", "F");
    return Flux.concat(abc, def).log();
  }

  public Flux<String> explore_concatwith() {
    var abc = Flux.just("A", "B", "C");
    var def = Flux.just("D", "E", "F");
    return abc.concatWith(def).log();
  }

  public Flux<String> explore_concatwith_Mono() {
    var abc = Mono.just("A");
    var def = Mono.just("B");
    return abc.concatWith(def).log();
  }

  public Flux<String> explore_merge() {
    var abc = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
    var def = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));
    return Flux.merge(abc, def).log();
  }

  public Flux<String> explore_mergewith() {
    var abc = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
    var def = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));
    return abc.mergeWith(def).log();
  }

  public Flux<String> explore_zip() {
    var abc = Flux.just("A", "B", "C");
    var def = Flux.just("D", "E", "F");
    return Flux.zip(abc, def, (first, second) -> first + second).log();
  }

  public Flux<String> explore_zip_1() {
    var abc = Flux.just("A", "B", "C");
    var def = Flux.just("D", "E", "F");
    var _123 = Flux.just("1", "2", "3");
    var _456 = Flux.just("4", "5", "6");
    return Flux.zip(abc, def, _123, _456)
        .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4())
        .log();
  }

  public Mono<String> explore_mergeZipWith_Mono() {
    var abc = Mono.just("A");
    var def = Mono.just("B");
    return abc.zipWith(def).map(t2 -> t2.getT1() + t2.getT1()).log();
  }

  public Flux<String> exception_flux() {
    return Flux.just("A", "B", "C")
        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .log();
  }

  public Flux<String> explore_onErrorReturn() {
    return Flux.just("A", "B", "C")
        .concatWith(Flux.error(new IllegalAccessException("Exception Occurred")))
        .onErrorReturn("D")
        .log();
  }

  public Flux<String> explore_doOnError() {
    return Flux.just("A", "B", "C")
        .concatWith(Flux.error(new IllegalAccessException("Exception Occurred")))
        .doOnError(
            ex -> {
              log.error("Exception is {}", ex.getMessage());
            })
        .log();
  }

  public Mono<Object> explore_Mono_doOnError() {
    return Mono.just("A")
        .map(
            value -> {
              throw new RuntimeException("Exception Occurred");
            })
        .onErrorReturn("abc")
        .log();
  }

  public Flux<String> explore_onErrorResume(Exception e) {
    var recoverFlux = Flux.just("D", "E", "F");
    return Flux.just("A", "B", "C")
        .concatWith(Flux.error(e))
        .onErrorResume(
            ex -> {
              log.error("Exception is ", ex);
              if (ex instanceof IllegalStateException) {
                return recoverFlux;
              } else {
                return Flux.error(ex);
              }
            })
        .log();
  }

  public Flux<String> explore_onErrorContinue() {
    var recoverFlux = Flux.just("D", "E", "F");
    return Flux.just("A", "B", "C")
        .map(
            name -> {
              if (name.equals("B")) {
                throw new IllegalStateException("Exception Occureed");
              }
              return name;
            })
        .concatWith(Flux.just("D"))
        .onErrorContinue(
            (ex, name) -> {
              log.error("Exception is ", ex);
              log.info("name is {}", name);
            })
        .log();
  }

  public Flux<String> explore_onErrorMap() {
    var recoverFlux = Flux.just("D", "E", "F");
    return Flux.just("A", "B", "C")
        .map(
            name -> {
              if (name.equals("B")) {
                throw new IllegalStateException("Exception Occureed");
              }
              return name;
            })
        .concatWith(Flux.just("D"))
        .onErrorMap(
            ex -> {
              log.error("");
              return new ReactorException(ex, ex.getMessage());
            })
        .log();
  }

  public Flux<String> splitString(String name) {
    var charArray = name.split("");
    return Flux.fromArray(charArray);
  }

  public Flux<String> splitString_withDelay(String name) {
    var charArray = name.split("");
    var delay = new Random().nextInt(1000);
    return Flux.fromArray(charArray).delayElements(Duration.ofMillis(delay));
  }

  public Mono<String> nameMono() {
    return Mono.just("alex");
  }

  public static void main(String[] args) {
    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
    fluxAndMonoGeneratorService
        .namesFlux()
        .subscribe(
            name -> {
              System.out.println("Name is: " + name);
            });

    fluxAndMonoGeneratorService
        .nameMono()
        .subscribe(
            name -> {
              System.out.println("Name is: " + name);
            });
  }
}
