# Jakarta Data Spring Adapter

Right out of the gates: this library is not a Jakarta Data implementation by any means, rather, it is a bridge between Jakarta Data implementations and Spring projects.

## Motivation 

Becuase of [various differences between Spring Data and Jakarta Data](https://github.com/spring-projects/spring-data-jpa/issues/3372), it is quite hard for Spring Data to
integrate Jakarta Data into an already existing module. However, there are implementationf for Jakarta Data that are quite popular, like Hibernate. This library provides a 
seamless way to integrate Jakarta Data into Spring applications. 

## Usage. Step 1. Dependency.

There're only two steps that you need to do to make this work. First - include this dependency into your `pom.xml`:
// TODO

In case of Gradle Groovy:
// TODO

In case of Gradle Kotlin dialect:
// TODO

## Usage. Step 2. Annotation.

And after that, put the `@EnableJakartaDataRepositories` annotation on any of your confiugration classes, for instance, like that:

```(java)
@EnableJakartaDataRepositories
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
         SpringApplication.run(Application.class, args);
    }
}
```

By adding this annotation, you bootstrap the mechanism of creating new Spring beans out of Jakarta Data generated repositories implementations. For instance, you had the following repository:

```(java)
@jakarta.data.repository.Repository // note annotation package
public PostRepository extends CrudRepository<Post, Long> {
}
```

then you can safely inject it onto your beans if needed:

```
@Component // Spring's @Component
public class Service {

   @Autowired
   private PostRepository postRepository;

   // the logic involving PostRepository
}
```
