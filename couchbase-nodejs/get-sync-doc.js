var express = require("express");
var couchbase = require("couchbase");
var app = express();

var bucket = (new couchbase.Cluster("http://p-couchbase01.yousee.idk:8091/pools")).openBucket("dk.yousee.randy", "AM6eEKUVdpz6BVMt");

bucket.get("8a15bcb0-ef54-42a1-8d47-8090ceafbe91", function(error, res) {
	console.log(JSON.stringify(res.value, null, 4));
});
