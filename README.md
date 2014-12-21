# How to use the Backend #

POST    /login  { "username": "ruben", "123456" }

POST    /signup { "username": "ruben" , "123456", "first_name": "ruben", "last_name": "barros"}

GET     /user/:id/group 

GET     /user/:user/group/:group

POST    /group { "name": "isep", "password": "123456" }

PUT     /user/:id/group {"user" : 1, "group": 1}

DELETE  /user/:user/group/:group

GET     /group