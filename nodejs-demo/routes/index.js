var express = require('express');
var jade = require('pug');
var router = express.Router();

var int = "init()";
eval(int);

function init (){
/* GET home page. */
router.get('/', function(req, res, next) {
  res.send(jade.render('h Hello \nscript !{script}', { script: 'alert(1)'}));
});
}

module.exports = router;
