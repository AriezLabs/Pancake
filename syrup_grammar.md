# EBNF of Syrup

Syrup supports:

````
# headers

* unordered lists
1. ordered lists
    * nested lists

> quotes

_emphasis_
inline `code`

[reference-style links][1]
[1]: http://...

[reference-style images][2]
[2]: path/to/image/

[image galleries with simple layout][3]
[3]: [basepath/] a b c d [xx x x]
this will show basepath/a and basepath/b beside each other, c below them and d below c
basepath and layout description are optional but must be in brackets

[single images resized by width][4]
[4]: path/to/image/ 400px

```
code blocks
```

---
horizontal rules

````

## Symbols

````
newline = (*empty line*)
backtick = '`'
equal = '='
gt = '>'
dash = '-'
colon = ':'
lbracket = '['
rbracket = ']'
asterisk = '*'
underscore = '_'
period = '.'

number = '0' ... '9' { '0' ... '9' }
````

## Syrup elements:

````
syrup         = { paragraph | header | quote | codeblock | hr | linkReference | list | newline }

header        = equal { equal } paragraph

quote         = gt paragraph

paragraph     = { emphasis | inlinecode | link } newline // TODO... figure this (and syrup definition) out, resubstitute literals here

codeblock     = "```" (* any symbol except triple backtick *) "```"

emphasis      = underscore text underscore

inlinecode    = backtick (* any symbol except backtick *) backtick

list          = ( ordered | unordered ) line

ordered       = number period

unordered     = asterisk

hr            = "-----" newline

link          = lbracket text rbracket lbracket number rbracket

linkReference = lbracket number rbracket colon ( url | image )

url           = "http://" | "https://"

image         = basepath text { text } layout

basepath      = [ "[" text "]" ]

layout        = [ "[" { "x" | " " } "]" ]
````