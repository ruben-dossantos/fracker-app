# How to use the Backend #

Host: http://crucifix.inescporto.pt:8080

POST    /login  { "username": "ruben", "password": "123456" }

POST    /signup { "username": "ruben" , "password": "123456", "first_name": "ruben", "last_name": "barros"}

### read user's group
GET     /user/:id/group 

### read friends locations
GET     /user/:user/group/:group

### create group
POST    /group { "name": "isep", "password": "123456", "owner": 1 }

### update user with a new group (join)
PUT     /user/:id/group {"user" : 1, "group": 1}

### delete group from user (leave)
DELETE  /user/:user/group/:group

### read all groups to join
GET     /group

### update user position
PUT     /user/:user       { "username": "jdias", "lat": "41.2784072", "lon": "-8.3702511" }

### update user preference distance
PUT     /user/:user/distance        { "username": "ruben", "preferenceDistance" : 5.0 }