var express = require('express');
var jade = require('pug');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next){
    var n = Math.random();
    if(n > 0.5){
        res.send(jade.render("h Hello \nscript !{script}", { script: 'alert(' + n + ')'}));
    }else{
        next();
    }
});


module.exports = router;
