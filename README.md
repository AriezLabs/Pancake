<img src="vaguely_discernible_pancake.png" alt="logo" width="300px">

# Pancake

[WIP] Transpiles Syrup to HTML. The purpose of this is to be a fun project as well as making writing for my (empty) [website][0] easier.

# Syrup

Simple markup language heavily inspired by [Markdown][1] and [Vimwiki][2].
More features are planned (see below), but focus is on getting some basic version running for now.
A FSM for tokenizing Syrup symbols can be found in syrup.fsm and is used for generating a tokenizer in Java.

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