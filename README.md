# API Template (Javalin + Hibernate)

This is a template project for creating REST APIs with **Javalin** and **Hibernate**.

## How to use
1. Add a new **Entity** in `app.entities`
2. Add a matching **DTO** in `app.dtos`
3. Create a **DAO** in `app.daos`
4. Create a **Controller** in `app.controllers`
5. Add **Routes** in `app.routes` and register them in `Routes.java`

## Run
Server starts on **http://localhost:7076/api/v1**

Example:  
`GET http://localhost:7076/api/v1/sample1` → returns all samples

## Notes
- `hibernate.hbm2ddl.auto` is set to `create` by default → change to `update` when keeping data.
- Tests run with Testcontainers (Postgres).


## Creator
[AsgerSH](https://github.com/AsgerSH) (github)