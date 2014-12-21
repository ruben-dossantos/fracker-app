# How to use the Backend #

Host: http://crucifix.inescporto.pt:8080

POST    /login  { "username": "ruben", "password": "123456" }

POST    /signup { "username": "ruben" , "password": "123456", "first_name": "ruben", "last_name": "barros"}

GET     /user/:id/group 

GET     /user/:user/group/:group

POST    /group { "name": "isep", "password": "123456" }

PUT     /user/:id/group {"user" : 1, "group": 1}

DELETE  /user/:user/group/:group

GET     /group

PUT     /user/:user       { "username": "jdias", "lat": "41.2784072", "lon": "-8.3702511" }