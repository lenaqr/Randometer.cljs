# Randometer.cljs

This is a browser implementation of Nate Soares' [Randometer][Soares].

[Soares]: https://github.com/Soares/Randometer.hs

## Overview

From [the original README][Soares]:

> This is a little set of games that have helped me gain an intuition
> for random behavior. A better knowledge of what random patterns look
> like has helped me, in real life, to better differentiate between
> chance occurrences and actual patterns.

> Humans are [biased to see patterns in random data][Tversky]: these
> games go a little way towards helping identify and correct for that
> bias, by staving off premature pattern matching.

[Tversky]: http://psych.cornell.edu/sites/default/files/Gilo.Vallone.Tversky.pdf

## Setup

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL. 

## License

Copyright © 2015 Anthony Lu

Distributed under the MIT License.
