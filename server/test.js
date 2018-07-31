const http = require("http");
const u = require("url");
const query = require("querystring");

const mongo = require("mongodb").MongoClient;

var fs = require("fs");

var server = http.createServer(function(req, res) {
    var url = req.url;

    var strucedUrl = u.parse(url);
    var path = strucedUrl.pathname;
    var cmds = path.split("/");

    console.log(cmds[1]);

    if(cmds[1] == "html") {
        console,log("Html ok");
        if(req.method == "GET") {
            console.log(path);
            fs.readFile(filepath, 'utf-8', function(error, data) {
                if(error) {
                    res.writeHead(404, {'Contetn-Type': 'text/html'});
                    res.end("<h1>404 Page not found!</h1>");
                }
                else {
                    res.writeHead(200, {'Content-Type':'text/html'});
                    res.end(data);
                }
            });
        }
    }
});

server,listen(8080,function() {
    console.log("sercer listening...");
});