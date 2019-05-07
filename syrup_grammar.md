# Definition of Syrup

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

{reference-style images}{2}
{2}: path/to/image/

{image galleries with simple layout}{3}
{3}: {basepath/} a b c d {xx x x}
this will show basepath/a and basepath/b beside each other, c in full width below them and d below c
basepath and layout description are optional but must be in brackets

{single images resized by width}{4}
{4}: path/to/image/ {400px}

```
code blocks
```

---
horizontal rules

````

## EBNF

````
syrup         = { paragraph | header | quote | codeblock | hr | linkReference | list | newline }

header        = "=" { "=" } paragraph "=" { "=" }

quote         = ">" { ">" } paragraph

paragraph     = { text | emphasis | inlinecode | link } newline

codeblock     = "```" { newline text } "```"

inlinecode    = "`" { text } "`"

code          = (* any char except unescaped "`" *)

emphasis      = "_" { text } "_"

italic        = "*" { text } "*"

list          = ( ordered | unordered ) paragraph

ordered       = number "."

unordered     = "*"

hr            = "-----" newline

link          = "[" text "]" "[" number "]"

linkReference = "[" number "]" ":" ( url | image )

url           = "http://" | "https://"

image         = basepath text { text } layout

basepath      = [ "[" text "]" ]

layout        = [ "[" { "x" | " " } "]" ]

````

## Symbols

````
eq = "="
gt = ">"
backtick = "`"
underscore = "_"
asterisk = "*"
period = "."
colon = ":"
lbracket = "["
rbracket = "]"
number = "1" ... "9" { "1" ... "9" }
whitespace = " " { " " }
newline = "\n" { "\n" }
text = (* any remaining character [ repeated ] *)
````