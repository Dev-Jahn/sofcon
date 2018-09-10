var zerorpc = require("zerorpc");

var client = new zerorpc.Client();
client.connect("tcp://127.0.0.1:4242");
console.log("start");

client.invoke("recommend",
		'attraction',
		['10172901', '10183757'],
		//['324890','3808315','590748'],
		//['320359'],
		function(error, res, more) {
			console.log(res);
		});
