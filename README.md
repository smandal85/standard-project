
BackBase Backend Test 2.0 (2017-07)
===================================

To run the project, just run the command: 

```mvn jetty:run```


The swagger documentation is available at

```
http://localhost:8080/api-doc
```

Or can access to the yaml version at:

```
http://localhost:8080/api-doc/swagger.yaml
```


Main endpoints:

```
http://localhost:8080/transactions
```

```
http://localhost:8080/filter?transactionType=sandbox-payment
```

```
http://localhost:8080/totalAmount?transactionType=sandbox-payment
```

Checklist TO-DO

- [ ] use of AutoWired (DI), instead of using the 'new' operator.
- [ ] use of xml context and/or annotated context
- [ ] use of JUnit, Unit tests
- [ ] Spring security context configuration
- [ ] Use of Camel
