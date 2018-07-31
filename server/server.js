
const http = require('http');
const mongoose = require('mongoose');
let url = require('url');

mongoose.connect('mongodb://localhost:27017/softcon');
let db = mongoose.connection;

db.on('error', function(){
    console.log('Connection Failed!');
});
// 5. 연결 성공
db.once('open', function() {
    console.log('Connected!');
});

let server = http.createServer((request, response) => {
    return request
        .on('error', (err) => {
            console.error(err);
        })
        .on('data', (data) => {
            console.log(data);
        })
        .on('end', () => {
            response.on('error', (err) => {
                console.error(err);
            });
            response.statusCode = 200;
            response.setHeader('Content-Type', 'text/plain');
        });

    response.writeHead(200, {'Content-Type': 'text/html'});
    response.end('Hello node.js!!');

});

server.listen(8080, function(){
    console.log('Server is running...');
});

var student = mongoose.Schema({
    name : 'string',
    address : 'string',
    age : 'number'
});

// 7. 정의된 스키마를 객체처럼 사용할 수 있도록 model() 함수로 컴파일
var Student = mongoose.model('Schema', student);

// 8. Student 객체를 new 로 생성해서 값을 입력
var newStudent = new Student({name:'Hong Gil Dong', address:'서울시 강남구 논현동', age:'22'});

// 9. 데이터 저장
/*newStudent.save(function(error, data){
    if(error){
        console.log(error);
    }else{
        console.log('Saved!')
    }
});
*/

