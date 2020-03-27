#### Get all meals
`curl --location --request GET 'http://localhost:8080/topjava/rest/meals'`

#### Get meal
- @param int id - meal id

`curl --location --request GET 'http://localhost:8080/topjava/rest/meals/id'`

#### Get filtered meals by date and time
- @param @Nullable LocalDate startDate - date from
- @param @Nullable LocalTime startTime - time from
- @param @Nullable LocalDate endDate - date to
- @param @Nullable LocalTime endTime - time to

`curl --location --request GET 'http://localhost:8080/topjava/rest/meals/between?startDate=dateFrom&startTime=timeFrom&endDate=dateTo&endTime=timeTo'`

#### Create meal
- @param LocalDateTime dateTime - creation time meal
- @param String description - description meal
- @param int calories - meal calories

`curl --location --request POST 'http://localhost:8080/topjava/rest/meals' --header 'Content-Type: application/json' --data-raw '{"dateTime":"dateTime","description":"description","calories":calories}'`

#### Update meal
- @param int id - id meal
- @param LocalDateTime dateTime - creation time meal
- @param String description - description meal
- @param int calories - meal calories

`curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/id' --header 'Content-Type: application/json' --data-raw '{"dateTime":"dateTime","description":"description","calories":calories}'`

#### Delete meal
- @param int id - id meal

`curl --location --request DELETE 'http://localhost:8080/topjava/rest/meals/id'`

---
#### Get all users
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/users'`

#### Get user
- @param int id - id user

`http://localhost:8080/topjava/rest/admin/users/id`