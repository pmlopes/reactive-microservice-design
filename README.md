# From monolith to scalable microservices with Vert.x

## Setting up Development Environment

In this section, we'll take a brief overview at the software development
tools you can use to develop reactive microservices. You will be
introduced to Apache Maven, Git, Docker, Docker Compose and IntelliJ
IDE.

### Introduction

A microservice is an autonomous sub application for a strictly defined
and preferably small domain.

An application built from microservices is **scalable**, **resilient**, and
**flexible**. At least, if the services and their infrastructure are well
designed.

One requirement on the used frameworks to achieve scalability and
resilience is that they are lightweight. Lightweightness comes in
different flavors. Microservices should be stopped and started fastly,
and should consume few resources. The development and maintenance of
microservices should be easy.

In the Java world, Spring or more traditional Java EE are popular
frameworks to build microservices. If one reads carefully the previous
requirements:

* stopped and start fastly
* consume few resources

It is not evident that these frameworks are the best choice for the
implementation of micro services. One can argue that these frameworks
can be made lightweight with their variants: Spring Boot or Eclipse
Microprofile. Again a deeper look to the requirements shows:

* scalable
* resilient
* flexible

These properties are not intrinsic to the given frameworks. In this
course you will be presented with a simple application built with such
frameworks and you will see the why they are not the right tool. You
will learn about Eclipse Vert.x as a reactive microservice toolkit. The
course will be hands on with many code exercises following a refactor
path from monolith to reactive microservice.

### What tools you can use for develop reactive microservices

Developing microservices is not different than developing other
applications. A microservice is not language or runtime specific. This
makes it quite hard to define the "right" tool for the job as one can
have a microservice written in:

* Java
* JavaScript
* C\#
* Haskell
* Etc...

And obviously having such languages will bring different runtimes:

* JVM
* Node.JS
* .Net
* x86 Assembly

### Know your tools

In this course we will be focusing on Eclipse Vert.x which is a JVM
based runtime. Eclipse Vert.x is polyglot which means that the
developers are not limited by the constraints of a language e.g.: Java
but can use Kotlin, JavaScript, Scala, Groovy, Ruby, etc... if these
languages have features known to best solve the problem being handled.

With this in mind a minimal set of tools can be defined:

* JVM runtime - Java 8 or above
* A build management tool - Apache Maven
* Source Control Management tool - Git
* Container/Orchestration tool - Docker and Docker Compose
* An IDE - IntelliJ IDEA

### Getting to know the IDE

The decision to pick IntelliJ IDEA for this course is not random.
IntelliJ IDEA is a popular IDE that supports out of the box polyglot
development (Java, Kotlin, Groovy) and with extra plugins:

* Scala
* JavaScript
* Ruby
* Etc...

It also sports out of the box support for all the tools we just listed:

* Apache Maven
* Git
* Docker (with plugin)

Having a single tool is very productive as one can avoid context
switching between tools and all actions feel natural.

### Introduction to ACME Bank application and its limitations

For this tutorial we will start with a monolith application, known as:
*ACME Bank*. This hypothetical application will during the course be
refactored to a reactive microservice architecture using Eclipse Vert.x.

The *ACME Bank* is a very simple application that exposes a *REST* API
and a web interface capable of doing the following tasks:

* Create accounts.
* Load the state of a given account from a database.
* Create Transactions (fund transfers) between 2 accounts.
* Load the state of a given transaction from a database.
* Serve a simple web application to interact in a user friendly way.

As the development of the application continues the following has been
observed:

1.  As the application requirements grow, the codebase will grow with
    it.
2.  The architecture was defined before hand and is locked as the
    project evolves.
3.  Change is expensive.
4.  The application is not resilient.
5.  Application is not scalable.

## Reactive Programming, Systems and Microservices

In this section, we'll introduce you to the three "R"s: Reactive
Programming; Reactive Systems; and Reactive Microservices. You'll learn
the differences between Reactive Programming and Reactive Systems and
their benefits as well as what problems Reactive Microservices solve and
how can a tool such as Eclipse Vert.x help.

### Reactive Programming vs Reactive System

Reactive Programming is a trait of event driven systems (or asynchronous
systems) where one programs towards reactions (of events). In a over
simplified way, reactive programming is the form how one writes code.
One does not assume that a method call will return the response but will
need to provide a callback, future object or promise that in the future
a response will be passed to this structure to continue the program
flow.

A reactive system builds on top of reactive programming and as defined
by the reactive manifesto provides the means for an application to be
elastic and resilient. Being elastic is important as we can accommodate
to any number of clients and resilient will ensure we can survive
failures.

Reactive microservices are the marriage of microservices and reactive
systems. Given that a microservice is:

* small in size, single responsibility
* Runs on its own process
* Independently develop, deploy, upgrade, scale
* Has its own data store
* Distributed by default
* Potential heterogeneous/polyglot
* Lightweight communication

By building on top of a reactive system the developer does not need to
focus on the complexities of distributed systems and only focus on
implementing the business logic to solve the problem.

### Understand Microservice architecture

Previously, we developers built applications in a way that is now known
as the monolith: The project starts off small, then we just add
something here, bolt on a new feature there. Then fast-forward a year or
two and you suddenly have this monster of a project where you change one
thing and the whole system can break. Everything is interconnected.

It's also much harder to scale this type of system. It's just one
monster project, so you end up having to scale by throwing more servers
at it, which ends up being very expensive.

The idea with microservices is to focus on building individual services
that do one thing and one thing well.

Let's list four key ideas:

* It's a flexible and efficient approach to building and operating
    software.
* Microservices architecture splits large applications into (much)
    smaller pieces that exist independently of each other.
* Each microservice, or piece of an application, does one thing and
    does it well.
* Microservices take a significant amount of work - i.e. what goes
    into building, deploying, and updating an enterprise application -
    and break that work into more manageable, efficient batches.

### How Eclipse Vert.x supports Reactive Microservices

Eclipse Vert.x is a tool-kit for building reactive applications on the
JVM.

It is event driven and non blocking. This means your app can handle a
lot of concurrency using a small number of kernel threads. Vert.x lets
your app scale with minimal hardware.

You can use Vert.x with multiple languages including Java, JavaScript,
Groovy, Ruby, Ceylon, Scala and Kotlin.

Vert.x doesn't preach about what language is best - you choose the
languages you want based on the task at hand and the skill-set of your
team.

Vert.x is incredibly flexible - whether it's simple network utilities,
sophisticated modern web applications, HTTP/REST microservices, high
volume event processing or a full blown back-end message-bus
application, Vert.x is a great fit.

Vert.x is not a restrictive framework or container and we don't tell you
a correct way to write an application. Instead we give you a lot of
useful bricks and let you create your app the way you want to.

### Quick introduction to Eclipse Vert.x

Vert.x is Event driven and non blocking. This is achieved by the Event
Loop which implement the Reactor Pattern. The reactor pattern,
popularized by the game industry and NodeJS is nothing more than a
single thread running an infinite loop, watching for events (e.g.: http
requests) and dispatching them to the correct handler for processing.

Vert.x implements the multi-event loop pattern, which means that there
will be an Event Loop per CPU core which means that you don't need to do
anything to take full advantage of your environment. This pattern also
ensure that once a event is started to be handled on a given loop all
subsequent handlers will be invoked on the same event loop, this avoids
threading issue for the developer who does not need to worry (in most
cases) about thread safety.

Vert.x instances form a cluster and are interconnected by the event bus.
The eventbus can deliver messages either point to point, publish
subscribe or request response in a transparent way without the need to
know which IP or process id the other node is running.

There are many many modules already available and some quite specific
for building microservices:

* Service Discovery
* Circuit Breaker
* Service Config
* Health Check

That implement the current state of the art in microservice technology.

## The means to build a Reactive Microservice

In this section, we will start diving into reactive microservices. This
is a the first part of two sections where we will explore the foundation
of any reactive microservice. The first foundation are the means to
build a reactive microservice.

### Introduce the means to have a reactive microservice

In order to have a reactive microservice there is the requirement that
the running environment is elastic and resilient. Enough of listening
and lets start looking at the current application. Build the application
and start it:

```bash
#configure a preloaded database
mvn -f db/pom.xml package
# compile and package
mvn clean package
# start the application plus a helper database
docker-compose up
```

[![asciicast](https://asciinema.org/a/xXemkW1kitW0cLUx0amySa3iB.png)](https://asciinema.org/a/xXemkW1kitW0cLUx0amySa3iB?t=0)

You should be able to navigate to `http://localhost/`
and interact with the application.

### Elasticity; What is it and why we need it

As it was stated initialy this application is not scalable. The easiest
way to assert this claim is by runnung:

```bash
# scale the deployment to 2 instances
docker-compose scale monolith=2
docker-compose ps
```

[![asciicast](https://asciinema.org/a/LqEzeIousJDB2TXOjWudzo9cN.png)](https://asciinema.org/a/LqEzeIousJDB2TXOjWudzo9cN?t=0)

Indeed we could have 2 instances but they wouldn't communicate with each
other. There is no way to use the newly available resources. Plus the
deployment fails as the requires host port is already in use.

### Resilience; Why is it so important

A reactive microservice is resilient. This is a very important property
as it safeguards the application for errors and allows it to self heal
in many situations, e.g.: network splits, service unavailable, etc...

Test that the application is not resilient:

```bash
# kill one process of the deployment
docker-compose ps
docker-compose kill monolith
```

[![asciicast](https://asciinema.org/a/tJDngcHyXCp4S5jU72dgRS5TO.png)](https://asciinema.org/a/tJDngcHyXCp4S5jU72dgRS5TO?t=0)

The whole application is now down (obviously) which renders a bad
experience for the end users.

### Refactor the ACME Bank to be Elastic and Resilient

In this step we will refactor the application and create a `account`
microservice. Creating an account project can be a complex task as one
needs to create a maven project. For the sake of time there is already
code with the basic project metadata.

#### Refactor 1

Add the account sub module to the top level pom modules so we can build
using the command:

```bash
mvn clean package
```

From the root of the project.

#### Refactor 2

We should now refactor the `monolith/AccountService` to the new
`account/AccountService` interface. Remember that Vert.x uses an
reactive programming model so all returns should be refactored to
`Handler<AsyncResult<R>>`. Open the new interface and add the missing 2
methods.

In order to enable the interface to be used in a polyglot environment,
one needs to add the annotations:

* `@VertxGen` - enables the compile time code generator for polyglot
    runtimes
* `@ProxyGen` - creates proxies to the API to simplify the message
    driven programming

#### Refactor 3

On our monolith we were using POJO's to transfer data across beans,
however in a distributed microservice we need to use a neutral encoding
for the data. Vert.x default encoding is JSON so there are a couple of
refactor actions required for the POJO.

1.  A POJO can be automatically converted to JSON if annotated with
    `@DataObject`.
2.  If the annotation enabled the property `generateConverter` then a
    compile time converter is generated during the project compilation.
3.  A `@DataObject` requires a empty constructor, a copy constructor
    from `JSON` and a `toJSON()` implementation.

You don't need to write the converter yourself, the code generator will
provide you the class `AccountConverter` once you compile for the first
time. Use that class to convert to and from `JsonObject`.

After step 2 it is required to run:

```bash
mvn compile
```

In order to generate the converter. After that the newly generated class
should be available to you.

#### Refactor 4

We can now focus on implementing the service, open the
`impl/AccountServiceImpl` class and implement the missing method.

Important to notice that even though JDBC is a blocking API in vert.x it
has been modified to be non blocking. So the update method is not
blocking but again using the standard handler style.

#### Refactor 5

We cannot be done without a test, by default JUnit is a blocking API but
by annotating the test class with the Vert.x Runner one can easily work
with async test. Once this is in place all test can take an optional
parameter: `TestContext` that can perform asynchronous assertions.

When running these tests, we need to inform JUnit that we're going to
perform an asynchronous task otherwise JUnit will assume that all
assertions where checked when the method returns. To do this you will
need to call: `TestContext#async()` and call `async.complete()` when the
test is complete.

1.  Annotate the test class with: `@RunWith(VertxUnitRunner.class)`
2.  Implement the missing test

In order to run the tests (and for simplicity in this tutorial) it is
expected to have a running database. This be done using
`docker-compose`. Then you can run the test from your IDE or from the
command line:

```bash
docker-compose start hsqldb
mvn clean test
```

Once you're done you can stop the database:

```bash
docker-compose stop hsqldb
```

[![asciicast](https://asciinema.org/a/W7I9QE9YWT983tvFJe4f4JW29.png)](https://asciinema.org/a/W7I9QE9YWT983tvFJe4f4JW29?t=0)

## Message Driven Architecture as the form of a reactive microservice

The form is how the underlying parts are molded to create the reactive
microservice. At the lowest level it all boils down to a message driven
architecture.

### Introduce Message Driven Architecture

As its core, Message Driven Architecture means that an application is
composed from autonomous components which communicate with each other
via messages. Message Driven Architecture is very common in a
distributed application, because each component sits on a different
server but they still need to work together.

But the amazing thing about Message Driven Architecture is that it can
be applied for local (non distributed) applications as well. This means
that a local Message Driven Architecture application can easily become a
distributed app, only some configuration is required, but the app code
should remained untouched. The main benefit though is that it makes it
very easy to write high quality, maintainable code i.e lowly coupled,
highly cohesive and highly testable code.

The downside is that it requires a bit of experience to become
comfortable with it. It’s not straightforward and at first, it seems
like over engineering, but once you experiment with it, it will feel
like the proper way to implement a non trivial application.

### Understand the relation between messages and asynchronous programming

Event-driven architecture (EDA), is a software architecture pattern
promoting the production, detection, consumption of, and reaction to
events. Event-Driven architectures are often design atop message-driven
architectures, where communication pattern require one of the inputs to
be text-only, the message, to differentiate how each communication
should be handled.

#### Refactor 6

Add the transaction sub module to the top level pom modules so we can
build using the command:

```bash
mvn clean package
```

From the root of the project.

#### Refactor 7

The 2 new services need to communicate with each other, to simplify this
we were already using `Service Proxies`. Take a look at the `pom.xml` of
both services and observe that the current setup is creating 2 jar
files:

* default jar
* api jar

The special `api` jar is a slim down jar just with the proxy interface
so it can be used by other services, for that add a dependency to
`account` using the classifier `api` to the `transaction` `pom.xml`
file.

#### Refactor 8

Once that the proxy is available we can now invoke method on it and
these get translated to messages delivered by the eventbus to the
`account` service.

Open the `impl/TransactionServiceImpl` class and add a class variable to
the `Account` proxy.

Finally we can test our service in isolation, for this we can mock any
service by listening on the service address and reply with a mock
response. In the `Transaction` test code create an `EventBus` consumer
to the address `AccountService.DEFAULT_ADDRESS` that replies an empty
JSON.

### How to avoid getting lost on callbacks

Asynchronous programming can get hard to read or follow once the
callbacks or handlers start to chain after each other. An example to
this problem can be observed on the method `wireTransfer` from
`impl/AccountServiceImpl` class.

What originally was a couple of lines to perform four sequential SQL
queries is now a long chain of callbacks. Although this might look like
a complex issue to solve, there are a couple of options to solve this
for example:

* Reactive Extensions
* Futures
* Co-routines

In order to use RX the developer must first get familiar with its API.
RX is quite powerful and definetely good to learn as it is used on many
realms, web, server, ui etc...

Co-routines are language specific of Kotlin and can be also applied to
JavaScript using the `async`/`await` feature of ES7.

### How to make ACME Bank code easy to read with Future composition

`Future`s are a simple concept: they represent the result of an action
that may, or may not, have occurred yet.

A Future alone is not that powerful, however once this is combined with
`CompositeFuture` we can now do simple operations on groups of futures
such as flatten the previous callaback hell.

Where we where chaining 2 SQL statements to read both the source and
target accounts, we can replace with:

```java
Future<JsonArray> getFromAccount = Future.future(f ->
  conn.querySingleWithParams(
      "SELECT id, balance FROM accounts WHERE id = ?",
      new JsonArray().add(fromAccountId),
      f.completer()));

Future<JsonArray> getToAccount = Future.future(f ->
  conn.querySingleWithParams(
      "SELECT id, balance FROM accounts WHERE id = ?",
      new JsonArray().add(toAccountId),
      f.completer()));

CompositeFuture.all(getFromAccount, getToAccount).setHandler(ar -> {
  if (ar.failed()) {
    rollbackAndReturn(conn, ar.cause(), handler);
    return;
  }

  JsonArray row1 = getFromAccount.result();
  JsonArray row2 = getToAccount.result();
  ...
```

As it can be seen both statements are now at the same indentation level
and we can also consume both results at the same moment.

Using this technique further to update the rows with the new state will
decrease even further the indentation level:

```java
Future<UpdateResult> updateFromAccount = Future.future(f ->
  conn.updateWithParams(
      "UPDATE accounts SET balance = balance - ? WHERE id = ?",
      new JsonArray().add(amount).add(fromAccountId),
      f.completer()));

Future<UpdateResult> updateToAccount = Future.future(f ->
  conn.updateWithParams(
      "UPDATE accounts SET balance = balance + ? WHERE id = ?",
      new JsonArray().add(amount).add(toAccountId),
      f.completer()));

CompositeFuture.all(updateFromAccount, updateToAccount).setHandler(ar2 -> {
  if (ar2.failed()) {
    rollbackAndReturn(conn, ar2.cause(), handler);
    return;
  }

  commit(conn, handler);
});
```

[![asciicast](https://asciinema.org/a/nClpyS1NAm0rS2llrNLnO4ZSX.png)](https://asciinema.org/a/nClpyS1NAm0rS2llrNLnO4ZSX?t=0)

## The values obtained from a Reactive Microservice architecture

In this section, we continue to explore the value that any reactive
microservice architecture offers. Building on the foundations of the
previous section we will explore the responsiveness trait that reactive
microservices have.

### Introduce core values that reactive microservices have

A reactive microservice responds in a timely manner if at all possible.
Responsiveness is the cornerstone of usability and utility, but more
than that, responsiveness means that problems may be detected quickly
and dealt with effectively. Responsive systems focus on providing rapid
and consistent response times, establishing reliable upper bounds so
they deliver a consistent quality of service. This consistent behaviour
in turn simplifies error handling, builds end user confidence, and
encourages further interaction.

### Responsiveness; What is it and why it matters

In order to complete the refactoring we nee to move the REST interface
to a service of its own, the `web` module.

#### Refactor 9

Enable the `web` module by adding it to the main `pom.xml`.

The `MainVerticle` is already given and defines the original API:

* POST `/account`
* GET `/account/:id`
* POST `/transaction`
* GET `/transaction/:id`

The router class chains handlers with a simple execution model. If the
handler is complete and is successful you call `next()` to proceed to
the next handler, or if you need to fail you can `fail()`. This simple
mechanism can help organizing asynchronous code in small blocks that can
be reused using method references.

When dealing with the POST `/account` one needs to perform the following
tasks:

1.  Validate that the body is of type JSON.
2.  Validate that the field `initialBalance` is present and is numeric.
3.  Invoke the remote service.
4.  Return the created response (Status code 202) with Location header
    pointing at the newly generated resource.

Add the missing handlers for that route using the existing helper
methods (Tip, use Java method references).

#### Refactor 10

The code is almost complete, all it's missing is the real call to create
a `Transaction`, POST `/transactions`. For this you need to add a
handler in the correct place that will use the existing service.

Remember that you don't need to assemble the `created` response
manually, just pass the success reply to the next handler.

Deploy the application and experiment scaling the services, for safety
do a clean build before:

```bash
mvn -Dmaven.test.skip=true clean package
```

Uncomment the services from `docker-compose.yml` and comment the
`monolith` service. Finally we've replaced it with our microservices.

And the start the application:

```bash
docker-compose up
```

Once it's up open a browser and navigate to http://localhost

You can now experiment scaling the account service:

```bash
# on another terminal
docker-compose scale account=2
```

Or event see how the application behaves if the `transaction` service is
down:

```bash
# on another terminal
docker-compose scale transaction=0
```

[![asciicast](https://asciinema.org/a/E26g0M2qhrLtbqEuIBH5BEjke.png)](https://asciinema.org/a/E26g0M2qhrLtbqEuIBH5BEjke?t=0)

### Maintainability and Extensibility

It should be clear now that having all these services have made the
application maintainable, as changes can be performed without affecting
the whole code base and extensible as components can evolve at their own
pace.

However there is still one big issue with the current refactor. We still
have a single point of failure:

* The database

As it is recommended by any microservice architecture we will use a
database per service.

#### Refactor 11

In the `docker-compose.yml` and add 2 new database services:

1.  `hsqldb-account` command should be:
    `java -cp /var/hsqldb.jar org.hsqldb.Server -database.0 file:/var/data/account -dbname.0 account`
2.  `hsqldb-transaction` command should be:
    `java -cp /var/hsqldb.jar org.hsqldb.Server -database.0 file:/var/data/transaction -dbname.0 transaction`

Make sure that you don't forward the ports to the host as they would
colide.

Update the configurations of the 2 services to point to the new URL, the
configuration is defined in the `config.json` file.

### How are these values present in the ACME Bank application

## Vert.x Microservice Toolbox

In this section, we will gain an understanding into the tools Vert.x
offers to work with microservices, how to solve the problem of
configuring all the services in a cluster, discovering services, how to
secure a frontend application and how to make an application failure
proof with circuit breakers.

### Vert.x Config and the configuration non issue

As it was seen in the previous section we now have a DevOps problem
while deploying the application as each single deployment needs besides
the final artifact a custom configuration file. Having such
configuration can be the cause of many failed deployment or systems to
mal function.

In order to solve the configuration issue we will use `Vert.x Config`.
We will add a Configuration retriever to our service main verticles. By
default the Config Retriever is configured with the following stores (in
this order):

1.  The Vert.x verticle config()
2.  The system properties
3.  The environment variables

### Refactor 12

Add the required dependency to the `account` service. In the main
verticle add a `ConfigRetriever` object and get the configuration using
the `getConfig` async call. Do the required changes to signal that the
application is correctly initialized using the given `Future`.

Update the `docker-compose.yml` file to use environment variables to
configure the database connection.

Start the application and experiment with scaling, note that scaling
down the `transaction` database will render that part of the application
not usable however the `accounts` part will remain working as expected.

```bash
docker-compose scale hsqldb-transaction=0
```

### Service Discovery and the EventBus

Due to the simplicity of the application service discovery is not
required but Vert.x offers integrations for:

* HttpEndpoints
* EventBusServices
* MessageSources
* MessageConsumers
* JDBCDataSources
* RedisDataSources
* MongoDataSources
* custom sources

### Security 101 with OAuth2

Currently the application is not secure as any user can create both
accounts and transactions. We will not secure the web interface using
Oauth2. Our bank is quite popular by developers so github accounts are a
good candidate to be used.

Anyone can register a new Oauth application at github using the link:
https://github.com/settings/developers

For this step there is a temporary account created that can be used
during the tutorial but is not guaranteed to live after that. If you
notice that the application is not valid anymore, see the previous link
and create a new one for your development purposes.

#### Refactor 13

Create a callback route. and store it before we add the security to the
application. By default the github application is expecting the url
`/callback`.

After the callback and before the APIs add a `OAuth2AuthHandler` to all
routes. Use the already configured auth provider and enable the callback
route you just created.