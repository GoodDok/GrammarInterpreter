# GrammarInterpreter

Parses and executes simple assignment statements, holding the program variable state.  

Current grammar supports work with integer values, primitive binary operations, using variables, reassignment.  
Variable names restrictions are in accordance with the Java ones.  
Integer values are limited by +-10^9 so can fit into 32bit variables.


### Grammar in use

```
1. statements -> statement ("\n" statement)* (multiline)
2. statement -> ident "=" expr 
3. expr -> sum
4. sum -> sum ("+" | "-") product | product
5. product = product ("*" | "/") term 
6. term = "(" expr ")" | wholeNumberSigned
7. wholeNumber = "-" wholeNumberUnsigned | wholeNumberUnsigned
8. wholeNumberUnsigned = digit | digit wholeNumberUnsigned
9. digit = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
```

### Parser generator in use
[scala-parser-combinators](https://github.com/scala/scala-parser-combinators)
The grammar above was translated into the code almost 1-to-1, see the examples in 
[Getting Started](https://github.com/scala/scala-parser-combinators/blob/1.2.x/docs/Getting_Started.md).  
In short, the framework provides universal `Parser` entity that can be combined with other entities 
with the help of several operators: 
1. `p ~ q` -- sequential composition. `p ~ q` succeeds if `p` succeeds and `q` succeeds on the input left over by `p`.
2. `p | q` -- alternative composition. `p | q` succeeds if `p` succeeds or `q` succeeds.
3. `p ^^ f` -- function application. `p ^^ f` succeeds if `p` succeeds; it returns `f` applied to the result of `p`.
4. Others

Also the framework provides `PackratParser` entity to overcome the left recursion problem. 

The code related to the grammar can be found in [the custom parser](src/main/scala/org/made2020/grammar/GrammarParsers.scala)

### How to run

The GrammarInterpreter can be run against a single file of instructions and outputs the full set of variables 
and their values at the end of parsing and execution. 

```bash 
sbt clean assembly
java -cp target\scala-2.13\GrammarInterpreter-assembly-0.1.jar org.made2020.grammar.GrammarApp <full_file_path>
```