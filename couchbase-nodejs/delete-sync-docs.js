var express = require("express");
var couchbase = require("couchbase");
var app = express();

var bucket = (new couchbase.Cluster("http://p-couchbase01.yousee.idk:8091/pools")).openBucket("dk.yousee.randy", "AM6eEKUVdpz6BVMt");

var ViewQuery = couchbase.ViewQuery;
var query = ViewQuery.from('randy', 'syncByDate');
query.stale(ViewQuery.Update.BEFORE);

query.range("2016-12-01", "2016-12-25", true);

bucket.query(query, function(error, results) {
    if(error) {
        return console.log(error);
    }
    console.log("Found " + results.length + " documents to delete");
    for(var i=0;i<results.length; i++) {
    	console.log("removing " + (i) + "/" + results.length, results[i].id, results[i].key);        

    	bucket.remove(results[i].id, function(error, result) {
            console.log("Deleting ", result);
        });
    	
    }
});

