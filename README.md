<img src="vaguely_discernible_pancake.png" alt="logo" width="300px">

# Pancake

[WIP] Transpiles Syrup to HTML. The purpose of this is to be a fun project as well as making writing 
for my (empty) [website][0] easier.

# Syrup

Simple markup language heavily inspired by [Markdown][1] and [Vimwiki][2].
More features are planned (see below), but focus is on getting some basic version running for now.

A definition of a FSM for tokenizing Syrup symbols can be found in syrup.fsm and is used for generating 
a tokenizer in Java. The file specifying the scanner FSM is parsed using a bootstrapped FSM instance 
with the following hardcoded language:

```
fsm            = { state | comment | newline }

state          = label ":" type ":" [ "[" defaultTarget "]" ] { input "-" target } newline

comment        = "#" text newline

type, input    = integer

label, target,
defaultTarget  = text
```

State labels must be unique. Whitespace is ignored. Type designates:

```
0   nonaccepting state
1   accepting state
2   accepting state that doesn't move reader position ahead
```


### Roadmap

* [ ] Define Syrup grammar
    * [ ] Deal with lookahead issue (single backtick etc. before EOF)
* [ ] Write Syrup scanner
    * [ ] Test FSM
* [ ] Define config file syntax for customization of parser output
* [ ] Write parser
* [ ] Extend grammar
    * [ ] Integrate MathJax
    * [ ] Allow resizable images, galleries and layout
    * [ ] Tables

[0]: http://www.ariezlabs.com
[1]: https://en.wikipedia.org/wiki/Markdown
[2]: https://github.com/vimwiki/vimwiki