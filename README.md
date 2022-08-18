spring with only GQL

- .graphqls file name prefix can be another than schema / query / mutation
- folder to save .graphqls should be named as graphql in resources folder. otherwise it will return 404 /graphql
- in this case, functions annotated with ```@SchemaMapping``` will make functions to be called 1 time for n user. But by using  ```@BatchMapping``` called 1 times for n user