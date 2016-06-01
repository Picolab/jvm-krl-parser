var execFile = require('child_process').execFile;

module.exports = function(file, callback){
  execFile('java', ['-jar', 'parser/krl_parser.jar', file], function(err, stdout, stderr){
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
