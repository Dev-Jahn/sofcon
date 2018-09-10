
const http = require("http");
const u = require("url");
const query = require("querystring");
const mongo = require("mongodb").MongoClient;
const fs = require("fs");

// Database Name
const DbName = "softcon";

// Collection Name
const ColLearning = "Learning";
const ColTrip = "Trip";
const ColUser = "User";
const ColPlaces = "Places";

var server = http.createServer(function(req, res) {
    
	var url = req.url;
	var strucedUrl = u.parse(url);
	var path = strucedUrl.pathname;
	var cmds = path.split("/");
	var parsedQuery = query.parse(strucedUrl.query, '&', '=');

	if(cmds[1] == "Learning") {
		if(req.method == "GET") {
			var test = {test: parsedQuery.test};
			mongo.connect("mongodb://127.0.0.1:27017",{useNewUrlParser : true} , function(err,db) {
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
			if(cmds[2] =="findPlace") {
				var lat = parseFloat(parsedQuery.lat);
				var lon = parseFloat(parsedQuery.lon);
				var len = parseFloat(parsedQuery.len);
				var lim = parseFloat(parsedQuery.lim);
				var flag = parseFloat(parsedQuery.flag);

				if(flag  == 0) {
					mongo.connect("mongodb://127.0.0.1:27017",{useNewUrlParser : true} ,function(err,db) {
						if(err) throw err;
						var dbo = db.db(DbName);
						var resarr = new Array();
						dbo.collection(ColPlaces).find({}).project({"_id":false, "placeId":false, "name_eng":false}).toArray(function(err, result) {
						if(err) throw err;
						var cnt = 0;
						result.forEach(function(item, index, array) {
							if(dis(lat, lon, array[index]["latitude"], array[index]["longitude"]) < len && cnt < lim) {
								resarr.push(array[index]);
								cnt += 1;
							}
						});

						res.writeHead(200, {'Content-Type': 'application/json;charset=utf-8'});
						res.end(JSON.stringify(resarr));
						db.close();
						});
					});
				} else if(flag == 1) {
				}
			}
		}
	} else if(cmds[1] == "UI") {
		if(req.method == "POST") {
			var TravelData = {
				travel_id : parsedQuery.travel_id,
				UID : parsedQuery.uid,
				People_count : parseInt(parsedQuery.people_count),
				Title : parsedQuery.title,
				days : parseInt(parsedQuery.days),
				syy : parseInt(parsedQuery.syy), smm : parseInt(parsedQuery.smm), sdd : parseInt(parsedQuery.sdd),
				eyy : parseInt(parsedQuery.eyy), emm : parseInt(parsedQuery.emm), edd : parseInt(parsedQuery.edd),
				DailyDiary : []
			};
			DiaryParse(req, result => {
				console.log(result);
				TravelData['DailyDiary'].push(result);
			});
			TravelData['DailyDiary'].push();
			if(cmds[2] == "insert") {
				if(req.method == "GET") {
					mongo.connect("mongodb://127.0.0.1:27017", {useNewUrlParser : true}, function(err, db) {
						if(err) throw err;
						var dbo = db.db(DbName);
						dbo.collection(ColTrip).insert(TravelData, function(err, result) {
							if (err) throw err;
							res.writeHead(200, {'Content-type' : 'application/json'});
							res.end(JSON.stringify({'result': 'true'}));
						});
					//	dbo.collection(ColTrip).find(UID).project({"_id":false}).toArray(function(err, result) {
					//		if(err) throw err;
					//		if(result.length == 1){
					//			dbo.collection(ColTrip).updateOne();
					//		} else {
					//			dbo.collection(ColTrip).insertOnde();
					//		}
					//	});
					});
				}
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
	}else if(cmds[1] == "Sign") {
		var UID = parsedQuery.UID;
		var pwd = parsedQuery.pwd;
		var User = {UID : UID,
					PWD : pwd}
		if(cmds[2] == "Up"){
			if(req.method == "GET") {
				var phonenum = parsedQuery.pnum;
				mongo.connect("mongodb://127.0.0.1:27017", {useNewUrlParser : true}, function(err, db) {
					if(err) throw err;
					var dbo = db.db(DbName);
					dbo.collection(ColUser).findOne(User , function(err, result) {
						if(err) throw err;
						if(result == null) {
							var InsertUser = { UID : UID,
												PWD : pwd,
												phonenum : phonenum,
												pos : [],
												neg : []
							};
							dbo.collection(ColUser).insertOne(InsertUser, function(err, result) {
								if(err) throw err;
								res.writeHead(200, {'Contect-Type':'application/json;cherset=utf-8'});
								res.end(JSON.stringify({"result":true}));
							});
						} else {
							res.writeHead(200, {'Conetent-Type': 'application/json;charset=utf-8'});
							res.end(JSON.stringify({'result' : false}));
						}
					});
				});
			}
		} else if(cmds[2] == "In") {
			if(req.method == "GET") {
				mongo.connect("mongodb://127.0.0.1:27017", {useNewUrlParser : true}, function(err, db) {
					if(err) throw err;
					var dbo = db.db(DbName);
					dbo.collection(ColUser).findOne(User , function(err, result) {
						if(err) throw err;
						if(result == null) {
							res.writeHead(200, {'Conetent-Type': 'application/json;charset=utf-8'});
							res.end(JSON.stringify({'result' : false}));
						} else {
							res.writeHead(200, {'Conetent-Type': 'application/json;charset=utf-8'});
							res.end(JSON.stringify({'result' : true}));
						}
					});
				});
			}
		}
	}else {
		res.writeHead(404, {'Content-Type': 'text/plain;charset=utf-8'});
		res.end("wrong Query");
	}
});

function toRad(Value){
	return Value* (Math.PI / 180)
}

function dis(lat1, lon1, lat2, lon2) {
	var R = 6371;
	var dLat = toRad(lat2 - lat1);
	var dLon = toRad(lon2 - lon1);;
	var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
			Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
			Math.sin(dLon / 2) * Math.sin(dLon / 2);
	var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	var d = R * c;
	return d;
}

function DiaryParse(req, callback) {
	const FORM_URLENCODED = 'application/x-www-form-urlencoded';
	if(rq.headers['content-type'] === FORM_URLENCODED) {
		let body = '';
		req.on('data', function(data) {
			body += data;
		});
		req.on('end', function() {
			callback(query.parse(body));
		});
	} else {
		callback(null);
	}
}

server.listen(8080, '10.146.0.3', function() {
	console.log("server running...");
});
