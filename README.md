Spring Boot Coding Dojo
---

Welcome to the Spring Boot Coding Dojo!

### Introduction

This is a simple application that requests its data from [OpenWeather](https://openweathermap.org/) and stores the result in a database. 

### Footnote
It's possible to generate the API key going to the [OpenWeather Sign up](https://openweathermap.org/appid) page.

### Final note
I added basic unit and integration tests but to be able to have a production-grade app
we need to add unit tests for all components (mappers). Also, we need to add more asserts for the existing tests. 
We will need to configure the correct DB, I used H2 only for testing purpose