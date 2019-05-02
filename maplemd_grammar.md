# EBNF of MapleMarkdown

MapleMarkdown supports:

````
# headers

* unordered lists
1. ordered lists
    * nested lists

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

## Tokens

````
number = "0" ... "9" { "0" ... "9" }
newline = "\n"
ascii = { (* ASCII character *) }
text = { (* ascii minus unescaped "*", "+", "`", "_", "\n" *) }
````

## MapleMarkdown elements:

````
markdown = { paragraph | header | quote | codeblock | hr | linkReference | list | newline }
header = "#" { "#" } line
quote = ">" line
line = { text | emphasis | inlinecode | link } newline
paragraph = { line } newline
codeblock = "```" { ascii } "```"
emphasis = "_" text "_"
inlinecode = "`" ascii "`"
list = ( ordered | unordered ) line
ordered = number "."
unordered = "*"
hr = "---" { "-" } newline
link = "[" text "]" "[" text "]"
linkReference = "[" text "]" ":" text newline
````