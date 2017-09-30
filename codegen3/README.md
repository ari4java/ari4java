# codegen3

This is the module that generates Java classes out of Swagger
defintions for *ari4java*.

It is meant to be run through a Clojure REPL, though I'm sure 
you could make it a simple executable if you want/need to.

## Running

Start up a REPL:


Run a build of ari4java to make sure everything still works:

    
It expects to find the current set of bindings 
under *resources* and will generate
Java code under 


## Description

## Todo

* Actions

    public List<Application> list() throws RestException;
    
    public void unsubscribe(String applicationName, String eventSource, AriCallback<Application> callback);
    public void unsubscribe(String applicationName, String eventSource);

* Action parameters:

 * Must read them from oldest to newest version
 * Optional parameters
 * Use enums for multi-value items
 * Permutations: single/multiple parameters
 * Permutations: sync/async methoods
 
 
 


* Create equals() and hashCode() and toString() for generated models
* Build script
* Which classes are tagged as "extends EventSource"? shouldnt it be "implements"?


# License

LGPL 
