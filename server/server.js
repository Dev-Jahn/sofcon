
const http = require("http");
const u = require("url");
const query = require("querystring");
const mongo = require("mongodb").MongoClient;
const fs = require("fs");

// Database Name
const DbName = "softcon";

// Collection Name
const Col_Learning = "Learning";
const Col_Trip = "Trip";
const Col_Diary = "Diary";

var server = http.createServer(function(req, res) {
    
	var url = req.url;
	var strucedUrl = u.parse(url);
	var path = strucedUrl.pathname;
	var cmds = path.split("/");
	var parsedQuery = query.parse(strucedUrl.query, '&', '=');

	if(cmds[1] == "Learning") {
		if(req.method == "GET") {
			var test = {test: parsedQuery.test};
			mongo.connect("mongodb://127.0.0.1:27017", function(err,db) {
				if(err) 
					res.end("db connect error");
				var dbo = db.db(DbName);
				dbo.collection("test").findOne(test).project({"_id":false}).toArray(function(err,result) {
					if(err) throw err;
					res.writeHead(200, {'Content-Type': 'text/plain;charset=utf-8'});
					res.end(JSON.stringify(result));
					db.close();
				});
			});
		}
	} else if(cmds[1] == "navi") {
		if(req.method == "GET") {
			var searchData = {
				cls : "",
				placeid : 0,
				name_kor : "",
				name_eng : "",
				loc : [0,0]
			};
			if(cmds[2] =="findPlace") {
				var x = parseFloat(parsedQuery.x);
				var y = parseFloat(parsedQuery.y);
				mongo.connect("mongodb://127.0.0.1:27017",{useNewUrlParser : true} ,function(err,db) {
					if(err) throw err;
					var dbo = db.db(DbName);
					dbo.collection("Places").find({}).project({"_id":false}).toArray(function(err, result) {
					if(err) throw err;
					res.writeHead(200, {'Content-Type': 'text/plain;charset=utf-8'});
					res.end(JSON.stringify(result));
					db.close();
					});
				});
			}
		}
	} else if(cmds[1] == "UI") {
		if(req.method == "GET") {
			var test = {test: "for test" };
			var PlaceData = {place : "",
				people_count : 0,
				days : 0,
				syy : 0, smm : 0, sdd : 0,
				eyy : 0, emm : 0, edd : 0,
			};
			var UID = {UID : parsedQuery.UID};
			if(cmds[2] == "finduid") {
				mongo.connect("mongodb://127.0.0.1:27017",{useNewUrlParser : true} ,function(err,db) {
					if (err) throw err;
					var dbo = db.db(DbName);
					dbo.collection(Col_Trip).find(UID).project({"_id":false}).toArray(function(err, result) {
						if(err) throw err;
						res.writeHead(200, {'Content-Type': 'text/html'});
						res.end(JSON.stringify(result));
						db.close();
					});
				});
			} else if(cmds[2] == "insert") {
			} else if(cmds[2] == "else") {
				mongo.connect("mongodb://127.0.0.1:27017", function(err,db) {
					if(err) 
						res.end("db connect error");
					var dbo = db.db(DbName);
					dbo.collection("test").find(test).project({"_id":false}).toArray(function(err,result) {
						if(err) throw err;
						res.writeHead(200, {'Content-Type': 'application/json'});
						res.end(JSON.stringify(result));
						db.close();
					});
				});
			}
		} 
	}else {
		res.writeHead(404, {'Content-Type': 'text.plain'});
		res.end("wrong Query");
	}
});

server.listen(3000, function() {
	console.log("server running...");
});
