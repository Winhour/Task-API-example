README

API zapisuje informacje w bazie H2 api-db.mv.db i jest postawione na serwerze Tomcat (podstawowy ze Spring Boota)

Sprawdzone zapytania (przy pomocy programu Postman):

TASK

GET localhost:8080/tasks -> wypisanie wszystkich zadañ
GET localhost:8080/tasks/{id} -> wypisanie wybranego zadania wed³ug id
GET localhost:8080/tasks/search/done -> wypisanie wszystkich ukoñczonych zadañ

POST localhost:8080/tasks -> dodanie zadania z body w jsonie
np.

{
    "title": "Test Task",
    "description": "Testing tasks",
    "status": "done",
    "deadline": "2021-12-25T23:59:59.999"
}

PUT localhost:8080/tasks/{id} -> edycja konkretnego zadania

DELETE localhost:8080/tasks/{id} -> usuwanie konkretnego zadania

PATCH localhost:8080/tasks/{id}/status/{state} -> modyfikacja statusu dla konkretnego zadania, gdzie {state} jest nowym statusem

PATCH localhost:8080/tasks/{task_id}/add/{user_id} -> przypisanie u¿ytkowanika o id {user_id} do zadania o id {task_id}

PATCH localhost:8080/tasks/{task_id}/remove/{user_id} -> pozbycie siê okreœlonego przypisania


USER

GET localhost:8080/users -> wypisanie wszystkich u¿ytkowników

GET localhost:8080/users/{id} -> wypisanie konkretnego u¿ytkownika

POST localhost:8080/users -> dodanie nowego u¿ytkownika
np.
{
    "name": "Jane",
    "surname": "Doe",
    "email": "janedoe@gmail.com",
    "phone_number": 689111235
}

DELETE localhost:8080/users/{?} -> usuwanie konkretnego u¿ytkownika
