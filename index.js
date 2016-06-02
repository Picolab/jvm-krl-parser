var path = require('path');
var execFile = require('child_process').execFile;

var jar_path = path.resolve(__dirname, 'parser/krl_parser.jar');

module.exports = function(file, callback){
  execFile('java', ['-jar', jar_path, file], function(err, stdout, stderr){
    if(err){
      callback(err);
      return;
    }
    var ast;
    try{
      ast = JSON.parse(stdout);
    }catch(e){
      err = new Error('out:\n' + stdout + '\nerr:\n' + stderr);
      ast = undefined;
    }
    callback(err, ast);
  });
};
