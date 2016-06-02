# jvm-krl-parser
extracting the KRL parser from KRE

## Running
```sh
$ java -jar parser/krl_parser.jar <your-krl-file>
```
That will output the ast in json.

### From node.js
```sh
$ npm install --save jvm-krl-parser
```
```js
var parser = require('jvm-krl-parser');

parser(krl_file_path, function(err, ast){
  // ...
});
```

## Contributing

The antlr grammar file is here: `parser/RuleSet.g`

The main class entry-point for the jar is here: `parser/src/KRLParser.java`

To re-compile the jar:
```sh
$ cd parser/
$ ./compile.sh
```

## License
MIT
