# EBNF of MapleMarkdown

MapleMarkdown supports:

````
# headers

* unordered lists
1. ordered lists

> quotes

_emphasis_
inline `code`

[links][1]
[1]: references

```
code blocks
```

---
horizontal rules

````

## Basic syntax elements

```
ascii = (* ASCII *)
character =
number = "0" ... "9" { "0" ... "9" }
newline = "\n"
space = " " { " " }
text = { (* ASCII minus "*", "+", "`", ""
```

## MapleMarkdown elements:

```
markdown = { paragraph | header | quote | codeblock | hr | linkReference | list }
header = "#" { "#" } line
quote = ">" line
line = { text | emphasis | inlinecode | link } newline
paragraph = { line } newline
codeblock = "```" { ascii } "```"
emphasis = "_" text "_"
inlinecode = "`" ascii "`"
list = { space } ( ordered | unordered ) line
ordered = number "." space
unordered = "*" space
hr = "---" { "-" } newline
link = "[" text "]" { space } "[" text "]"
linkReference = "[" text "]:" { space } text
```