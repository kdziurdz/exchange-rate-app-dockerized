# exchange-rate-app
 
 ### Run:
 from root of the project:
```
mvnw spring-boot:run
```

To see api go to
```
http://localhost:8080/swagger-ui.html
```

To get JSON results go to
```
http://localhost:8080/api/1/exchange-rate-summary/?currencyCode=USD&dateFrom=2018-01-01&dateTo=2018-03-01
```

where:
- **currencyCode** - currency code. Possible values are `USD, EUR, CHF, GBP`
- **dateFrom** - starting date inclusive
- **dateTo** - starting date inclusive

Date format is `yyyy-MM-dd`
