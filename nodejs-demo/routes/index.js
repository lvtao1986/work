var express = require('express');
var jade = require('pug');
var router = express.Router();

var int = "init()";
var count = 0;

/* GET home page. */
router.get('/', function(req, res, next) {
	count++;
	if(count > 1){
		eval(int);
	}
	next();
});

function init (){
/* GET home page. */
router.get('/', function(req, res, next) {
  res.send(jade.render('h Hello \nscript !{script}', { script: 'alert(1)'}));
});
}

module.exports = router;
