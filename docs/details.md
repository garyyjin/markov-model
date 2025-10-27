## Starter Code and Using Git

**_You should have installed all software (Java, Git, VS Code) before completing this project._** You can find 
the [directions for installation here](https://coursework.cs.duke.edu/201fall25/resources-201/-/blob/main/installingSoftware.md) (including workarounds for submitting without Git if needed).

We'll be using Git and the installation of GitLab at [coursework.cs.duke.edu](https://coursework.cs.duke.edu). All code for classwork will be kept here. Git is software used for version control, and GitLab is an online repository to store code in the cloud using Git.

For this project, you **start with the URL linked to course calendar**, 
[https://coursework.cs.duke.edu/201fall25/p1-markov-fall2025](https://coursework.cs.duke.edu/201fall25/p1-markov-fall2025).

**[This document details the workflow](https://coursework.cs.duke.edu/201fall25/resources-201/-/blob/main/projectWorkflow.md) for downloading the starter code for the project, updating your code on coursework using Git, and ultimately submitting to Gradescope for autograding.** We recommend that you read and follow the directions carefully this first time working on a project! While coding, 
we recommend that you periodically (perhaps when completing a method or small section) push your changes as explained in Section 5.

## Coding in Project P1: Markov

When you fork and clone the project, **make sure you open the correct project folder in VS Code** 
following the [This document details the workflow](https://coursework.cs.duke.edu/201fall25/resources-201/-/blob/main/projectWorkflow.md).

## Java Background 

For completing p1.1 and p1.2 knowing some Java concepts will be helpful.

### What is an immutable List<String> for this project?

The sequence of words used to generate random text in this (and the next) project is represented in code by an immutable `List<String>`. 
The size of the lists used is the order of the Markov Model. The lists are immutable so that your code cannot inadvertantly change
them. This will be important in Project 2, which uses the same `BaseMarkovModel` class.

Several methods return an immutable list: `createNewContext`, `getSequence`, and `getRandomContext`. 

See the code in [`BaseMarkov`](src/BaseMarkovModel.java) for details, each
method uses `List.copyOf` to create what is essentially an immutable `ArrayList`.


The number of strings contained in a model's context is sometimes called the *order* of the model, 
the term used in the Markov programs you'll implement.  You can see some examples of order-3 `List<String>` objects below.

| | | |
| --- | --- | --- |
| "cat" | "sleeping" | "nearby" |
| | | |

and 
| | | |
| --- | --- | --- |
| "chocolate" | "doughnuts" | "explode" |
| | | |


## What is a Markov Model?

Markov models are random models with the Markov property. In this project you can think of the Markov Model as a _small language model_. 
You've likely heard about Duke/ChatGPT, Claude, Gemini and other _large language models_. In our case, we want to create a Markov model 
for generating random text that looks similar to a training text. Your code will generate one random word at a time, and the 
Markov property in our context means that the probabilities for that next word will be based on the previous words -- 
more precisely on 3 previous words in an order-3 Markov Model and the `k` previous words in an order-`k` Markov model.

An order-k Markov model uses order-k `List<String>` contexts to predict text: we sometimes call these *k-grams* where *k* 
refers to the order. 
To begin, we select a random k-gram from the *training text* (the data we use to create our model by calling the 
method `getRandomText`; we want to generate random text based on the training text). Then, we look for instances of that k-gram 
in the training text to calculate the probabilities corresponding to words that might follow. 
We then generate a new word according to these probabilities, after which we repeat the process using 
the last k-1 words from the previous k-gram and 
the newly generated word by calling the method `createNewContext`. 
Continue in that fashion to create the desired number of random words. 

Here is a concrete example. Suppose we are using an order 2 Markov model with the following training text (located in `testfile.txt`):

```
this is a test
it is only a test
do you think it is a test
this test it is ok
it is short but it is ok to be short
```

We begin with a random k-gram, suppose we get `[it, is]`. This appears 5 times in total, and is followed by `only`, `a`, `ok`, `short`, 
and again by `ok` for the five occurences of `[it is]`. So the probability (in the training text) that `it is` is followed by `ok` is 2/5 or 40%, 
and for the other words is 1/5 or 20%. To generate a random word following the 2-gram `[it, is]`, 
we would therefore choose `ok` with 2/5 probability, or `only`, `a`, or `short` with 1/5 probability each.

Rather than calculating these probabilities explicitly, your code will use these probabilities implicitly. 
In particular, the `SimpleMarkovModel.getFollows` method you write must return a `List<String>` of *all* of the words that follow after a 
given k-gram in the training text (including duplicates). This list is used by the method `BaseMarkovModel.generate` to choose 
one of these words uniformly at random. 
Words that more commonly follow will be selected with higher probability by virtue of these words being duplicated in the `List<String>`. 
In our example above with `[it is]` the `getFollows` method would return the `List<String>` `["only", "a", "ok", "short", "ok"]`.

Suppose your code chooses `ok` as the next random word. Then the random text generated so far is `it is ok`, and the current
context `List<String>` of order 2 we are using would be updated to `[is, ok]` --- by dropping the `it` and adding `ok` (by calling `createNewContext` 
with the existing context and the chosen word).

We then again find the following words in the training text, and so on and so forth, until we have generated the desired number of random words.

Of course, for a very small training text these probabilities may not be very meaningful, but random generative models like this can be much more powerful when supplied with large quantities of training data, in this case meaning very large training texts.

## The Chat201Driver class

*You will modify the `main` method in this class to answer analysis questions.*

- Some static variables used in the main method are defined at the top of class, namely:
  - `TEXT_SIZE` is the number of words to be randomly generated.
  - `RANDOM_SEED` is the random seed used to initialize the random number generator. You should always get the same random text given a particular random seed and training text.
  - `MODEL_ORDER` is the order of `WordGram`s that will be used.
  - `PRINT_MODE` can be set to true or false based on whether you want the random text generated to be printed.
  - The `filename` defined at the beginning of the main method determines the file that will be used for the training text. 
By default it is set to `data/alice.txt`, meaning the text of *Alice in Wonderland* is being used. Note that data files are located inside the data folder.
- A `BaseMarkovModel` object named `model` is created. By default, it uses `BaseMarkovModel` as the implementing class, a complete implementation of which is provided in the starter code. Later on, when you have developed `HashMarkovModel`, you can comment out the line using `BaseMarkovModel` and uncomment the line using `SimpleMarkovModel` to change which implementation you use.
- The `model` then sets the specified random seed. You should get the same result on multiple runs with the same random seed. Feel free to change the seed for fun while developing and running, but *the random seed should be set to 1234 as in the default when submitting for grading*.
- The `model` is timed in how long it takes to run two methods: first `model.trainText()` and then the method `model.generate()`.
- Finally, values are printed: The random text itself if `PRINT_MODE` is set to true and the time it took to train (that is, for `trainText()` to run) the Markov model and to generate random text using the model (that is, for `generate` to run). 

## The SimpleMarkovModel

You'll implement the class `SimpleMarkovModel` to extend `BaseMarkovModel`. This means it _inherits_ the methdods and `protected` instance 
variables from that class. You're given a starting .java file that has constructors, but is missing one key method.

### Constructors

You'll need to implement two constructors that correspond to the two constructors in `BaseMarkovModel`. In
the class you write, you'll include a *default* consructor that has the same body as the default constructor
in `BaseMarkovModel`. Your parameterized constructor must have as its first line
```
   super(size);
```
After this line include any code needed to initialize any instance variables you add.

### getFollows

You must implement `getFollows` to find and return a `List<String>` that contains each individual string that getFollows
the parameter `List<String> context`. See the examples at the beginning of this document for details. You'll find
every order-k (where k is `myOrder`) sequence in the instance varaible `mySequence` using the `subList` method and
compare it for equality with `context`, building the returned `ArrayList` with Strings that follow
the `context`. You can model the code you write on `BaseMarkovModel.differentContexts` which finds every order-k subsequence
and stores them in a local `HashSet` variable. 

You may choose to implement other methods inherited as part of answering analysis questions.


## The HashMarkovModel class

This class *must extend* the class `BaseMarkovModel` just as the class `SimpleMarkovModel` does. This means your new class will
inherit the protected instance variables you'll see in `BasetMarkovModel`. Look at the class `BaseMarkovModel` for details.
Note that you are *NOT GIVEN* a starting .java file for `HashMarkovModel.java`.

However, you will need a `HashMap` instance variable that maps from `List<String>` values (the keys) to `List<String>` (the values). You may find this useful:
```
    private HashMap<WordGram,List<String>> myMap;
```

### constructors

You'll need to implement two constructors that correspond to the two constructors in `BaseMarkovModel`. In
the class you write, you'll include a *default* consructor that has the same body as the default constructor
in `BaseMarkovModel`. Your parameterized constructor must have as its first line
```
   super(size);
```
You'll also need to assign to the `HashMap` private instance variable.

### The processTraining() method

You'll need to implement the `processTraining` method (which was not necessary in `SimpleMarkovModel`). This method
is called in `BaseMarkovModel` as the line line of both `trainDirectory` and `trainText`. In `HashMarkovModel` the
code you write will populate (*after clearing*) the instance varaible `myMap`.

Note: you *must clear the `HashMap` instance variable* (for example, if the name of the variable is `myMap`, 
you can do this by calling `myMap.clear();`). This ensures that the map does not contain stale data if `processTraining()` 
is called multiple times on different training texts.

You should loop through the words in `myWordSequence` *exactly once*. 
For each `List<String>` of size `k`, where `k` is the order of the `HashMarkov` model, 
 you'll use the `List<String>` as a key, and add the String that follow it as an entry in the`ArrayList` object 
 that's the value associated with that `key` -- note that in the explanation above you'll see `List<String>` 
 as the type of the value associated with each `List<String>` key in the map. This is possibly confusing. The key in
 the map is a _context_, the order-k sequence/subList that is used to generate random text.  The value is
 the list of *individual strings* that follows this context in the training text.

 Your code *must create an `ArrayList` value to assign to each key in the map*, the `ArrayList` holds all the "next" words.

You'll create an initial context `List<String>` object from the first `myOrder` words, then start at the String that getFollows
this context. This String may be added to `myMap`, but it will become part of the next context which you will create
by calling `createNewContext`.

are after those first strings. You'll find code very similar to this in the `SimpleMarkovModel` 
method `getFollows` since some of that logic now moves into the the `HashMarkovModel` method `processTraining`. 
When your code looks up a context `List<String> `as a key in the `HashMap` instance variable 
your code will create a new `ArrayList` as the value associated with that key the first time the context occurs. 
Then your code wil call `myMap.get(context).add(str)` where `str` is the string that occurs after the `context`. 
Note that the values in the `HashMap` have type `List<String>`, but you'll create new `ArrayList` objects 
(since `List` is an interface).

As you loop over all strings, you'll change the `context` in the body of the loop by calling `createNewContext` 
which creates a new immutable `List<String>` with the next-to-be-looped-over string after it. Think about that!


### The getFollows() method


Just like in `SimpleMarkovModel`, the `getFollows` method takes 
a `List<String>` object `context` as a parameter and should return a `List` of all the words (containing `String` objects) that follow the `context` in the training text. However, the `HashMarkovModel` implementation *must* be more efficient, as it should *not* loop over the training text, but should instead *simply lookup the context `List<String>` in the `myMap` instance variable intialized during `processTraining()`*, or return an empty `List` if the `context` is not a key in the map. 

*This means that the `getFollows` method will be O(1) instead of O(N) where N is the size of the training text.*

## JUnit Testing in P1-Markov-GenAI

To help test your `SimpleMarkovModel` and `HashMarkovModel` implementations, you are given some *unit tests* in the class
`MarkovGenerateTest.java`  located in the `src` folder. 
A unit test specifies a given input and asserts an expected outcome of running a method, 
then runs your code to confirm that the expected outcome occurs. You can see the exact tests inside of 
a Unit-test files, though it may be difficult to read/understand. the JUnit library used by these testing classes is a very 
widely-used industry standard for unit testing.

Note that by default, `MarkovGenerateTest` is testing the `BaseMarkovModel` implementation, but all
tests will fail since `getFollows` is note implemented. When you are ready to test your `HashMarkovModel` implementation, 
you will want to change which model is created in the `getModel` method of `MarkovGenerateTest` at the position shown in the screenshow 
below (if the image does not render for you, you can find them in the `figures` folder). You will also make similar modifications
to the code in `Chat201Test` since that class includes test methods that will test how your `WordGram` class works with both `Base` and `Hash` Models.


<div align="center">
  <img src="figures/getmodel.png">
</div>


In order **to run these tests** inside VS Code, click the [Test Explorer](https://code.visualstudio.com/docs/java/java-testing#_test-explorer) (beaker) 
icon on the left side of VS Code (it should be the lowest icon on the panel). You can expand the arrow 
for `p1-markov-genai` and the default package to see the unit test: `MarkovGenerateTest` (some text may be cut off).
 You can click the run triangle next to each test package to run the tests. 
 See the screenshot example below. *Note that JUnit programs are run by the JUnit library and the beaker-icon, not be running them as Java programs.* 
 You'll run the tests by clicking the triangle in the left panel, and you'll see the results in the _Test Results_ window 
 rather than in the Terminal or Debugger window in VSCode.

For example, you can test all the tests in `MarkovGenerateTest` by hovering over that label in the _TestExplorer_ panel 
which is active when you click the Beaker-Icon, and is shown in the screenshot below. You can also run each individual 
unit test by hovering and clicking on each test's run triangle. The results of the tests are in the VSCode _TEST RESULTS_ panel, 
not in the other panels where output is shown. Deciphering error JUnit error messages is not always straightforward -- 
but when the tests pass? You'll get all green.

<div align="center">
  <img src="figures/matkovtest.png">
</div>



The main benefit of JUnit tests lies in their ability to examine isolated "units" of code — that is, to check correctness of a segment with minimal reliance on other relevant code and data. Additionally, the purpose of supplying these *local* (on your own machine) tests is to allow you to catch potential problems quickly without needing to rely on the (somewhat slower) Gradescope autograder until you are reasonably confident in your code. You do not have to use them for a grade.

<details>
<summary>Expand for optional JUnit details</summary>

We use a major Java library called [**JUnit**](https://junit.org/junit5/) (specifically version 5) for creating and running these unit tests. 
It is not part of the standard Java API, so we have supplied the requisite files `JAR` files (Java ARchive files) along with this project in a folder 
called `lib` (you don't need to do anything with this).   

</details

