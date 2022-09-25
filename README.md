# SpringBootApplication
This is an example of SpringBoot Application using Eclipse IDE - version 2022-06 (4.24.0).
This document reports the steps of the development from the early begining and its evolution.

## What it is about?
It manages a collection of Movies through CRUD functionalities.

This application is based on: https://alison.com/topic/learn/134591/learning-step-by-step-spring-and-spring-boot-learning-outcomes

### Entities
- Movie (attributes: id, imagePath, title, releaseDate, voteAverage)
- MovieDetails (attributes: id, overview, List<Genre>)
- Enum Genre

### Technologies
- Springboot - framework for dependency injection and inversion of control
- Java 17
- Web Tools - for HTML pages editing
- H2 - lightweight Java database
- JPA
- JSON
- Lombok - annotations library for standard Java code like getters/setters, etc.
- Thymeleaf - server-side Java template engine for both web and standalone environments
- Bootstrap - version 5.2.0

## Setting up the environment

### Installing Spring Boot in Eclipse
- Open eclipse
- Menu Help → Eclipse Marketplace
- Search and install:
  - Spring boot
  - Web Tools - for .html files

### Creating a Spring Boot project
- After installing Spring Boot, create a new project
  - File → New → (Other) → Spring Boot → Spring Starter Project
  - Give a Name, Group, and Description to the project → click Next
  - Select the dependencies
  - This project uses:
    - Spring Web - for embedded Tomcat and REST API.
    - H2 database
    - Lombok - for getters, setters, constructors, equal, hashcode
    - Spring Data JPA
- Dependencies are handled by Maven through the pom.xml file.
- If some dependency is required in the future, get it in: https://mvnrepository.com/
  - Search the library - copy and paste in the pom.xml file
- Installing Lombok
  - Download: https://projectlombok.org/download
  - Execute (full tutorial: https://projectlombok.org/)

### Testing the web structure
- The `resources/static` folder holds static contents like css/html pages.
- In the `static` directory, create index.html file → edit some html tags
- Create a new folder for the css and create a new css file (styles.css)
- To check that it is working, run the application and access: http://localhost:8080/index.html

Example:
index.html

```HTML
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Resources test</title>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
</head>
<body>
    <h1>Test</h1>
</body>
</html>
```

styles.css

```CSS
 @charset "ISO-8859-1";
h1{
    background: red;
}
```
## Creating the model (Entities)
- Under the java folder, create new package, give the name entities (or model).
- In the entities package, create the application's entities
- Example: Movie (it is a POJO)
  - Entities have Lombok Annotations for getters, setters, and constructors

## Configuring `application.properties` file 
- To spring understand that the H2 database is used, configure the `application.properties` file (located in the `resources` folder)
```
spring.datasource.username=sa //sa is the default user by convention
```

Change the server port (localhost:8080 is already in use for web application).
Example:

```
server.port = 8081
```

- After configuration in `application.properties`, run the application and access localhost:8081/h2
- Check if `JDBC URL = jdbc:h2:file:~/Movies` (as stated in the application.properties)
- According to the H2 Database: https://www.h2database.com/html/cheatSheet.html, `jdbc:h2:~/test` where 'test' in the user home directory

## Creating a repository class.
The repository class is a hook for the JPA persistence.
- Under folder java/spring create a new package repository
- Create interface MovieRepository

```Java
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>{
    
}
```

## Populating the database
- Create a new package (named `boot`) under the `java/spring` folder, and create a new class named `DataLoader`. This class is a Component - The spring component creates it as a Bean. This class will feed the database when it is empty to allow running the application.
  - DataLoader implements CommandLineRunner

### Reading data from the repository
- Using `findAll()` method from JpaRepository and showing the objects in the log (console).

```Java
@Component
public class DataLoader implements CommandLineRunner{

    //slf4j
    private Logger log = LoggerFactory.getLogger(DataLoader.class);
    
    private MovieRepository movieRepository;

    @Autowired
    public void setMovieRepository(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        
        if (movieRepository.count()<1) {
            Movie movie1 = new Movie();
            movie1.setTitle("Test");
            movie1.setReleaseDate("10/10/10");
            movie1.setVoteAverage(4.5);

            movieRepository.save(movie1);

            Movie movie2 = new Movie();
            movie2.setTitle("Test2");
            movie2.setReleaseDate("11/11/11");
            movie2.setVoteAverage(3.7);

            movieRepository.save(movie2);

            Movie movie3 = new Movie();
            movie3.setTitle("Test3");
            movie3.setReleaseDate("22/22/22");
            movie3.setVoteAverage(2.5);

            movieRepository.save(movie3);

//            List<Movie> movies = movieRepository.findAll();
//
//            for (Movie movie: movies) {
//                log.info("Movie found: " + movie.toString());
//            }
//
//            List<Movie> moviesByTitle = movieRepository.findByTitle("Test");
//
//            for (Movie movie: moviesByTitle) {
//                log.info("Movie found: " + movie.toString());
//            }
        }
        log.info("Movie count in database: " + movieRepository.count());

    }
}
```

- Run application
- To check working, access: http://localhost:8081/h2/
- Press the button ‘stop’ before re-running the application to not get the “Database may be already in use” error.

### Creating custom queries in JpaRepository - named queries
- Add methods in the MovieRepository interface:

```Java
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>{
    
    //There can be more than one movie with the same name
    List<Movie> findByTitle(String title);
}
```

- In the DataLoader class:

```Java
            List<Movie> movies = movieRepository.findAll();

            for (Movie movie: movies) {
                log.info("Movie found: " + movie.toString());
            }

            List<Movie> moviesByTitle = movieRepository.findByTitle("Test");

            for (Movie movie: moviesByTitle) {
                log.info("Movie found: " + movie.toString());
            }
```
- Run application (stop it before re-run) and observe the log in console 

## Controller class entry point to the REST api methods.
- Create a package `controller` under folder java/spring
- Create a new `Controller` class in the controller package 
  - `@RestController` to provide HTTP methods
  - `@RequestMapping` provides entry points for REST methods.

 ```Java
@RestController
@RequestMapping(path="/movies/")← it’s the root for following paths
public class MovieController {
    
    private MovieRepository movieRepository;
    
    @Autowired
    public void setMovieRepository(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    //id is the endpoint as path variable
    @RequestMapping(path="{id}", method=RequestMethod.GET) 
    public Optional<Movie> getMovie(@PathVariable(name="id") Long id) {
        return movieRepository.findById(id);
    }

    //Receive a JSON String (springframework)
    @RequestMapping(method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    public Movie saveMovie(@RequestBody Movie movieToSave) {
        return movieRepository.save(movieToSave);
    }

    @RequestMapping(path= "{id}", method=RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE)
    public Movie updateMovie(@RequestBody Movie movieToUpdate, @PathVariable(name="id") Long id) {
        //find the object in the repository
        Optional<Movie> foundMovie = movieRepository.findById(id);
        if (foundMovie.isPresent()) {
            Movie found = foundMovie.get();
            found.setImagePath(movieToUpdate.getImagePath());
            found.setTitle(movieToUpdate.getTitle());
            found.setReleaseDate(movieToUpdate.getReleaseDate());
            found.setVoteAverage(movieToUpdate.getVoteAverage());
            return movieRepository.save(found);
        }
        else {
            log.info("No movie found with given id");
            return movieToUpdate;
        }
    }
    
    @RequestMapping(path= "{id}", method=RequestMethod.DELETE)
    public void deleteMovie(@PathVariable(name="id") Long id) {
        //find the object in the repository
        Optional<Movie> foundMovie = movieRepository.findById(id);
        if (foundMovie.isPresent()) {
            Movie found = foundMovie.get();
            movieRepository.delete(found);
        }        
    }
}
```
- The repository class gain a method `findById`, returning a existing object or null (Optional<T>)
 
```Java
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>{
    
    //There can be more than one movie with the same name
    List<Movie> findByTitle(String title);
    
    Optional<Movie> findById(Long id);
}
```
## Testing application with the REST client
- Use a rest client as RESTClient for Firefox, Postman, etc. (Postman is used)

### Test **GET**
  - Go to the REST Client select GET, and pass localhost:8081/movies/{id}
  - Where /movies/ is mapped in the @RequestMapping annotation in the Repository class as root path.
  - Example: localhost:8081/movies/270
  - The body should present the result of the GET method in a JSON format.
  - To get a code response, include *http:*//localhost:8081/movies/{id} (for Firefox client)
  - It returns 200 in the header, if the object exists.

### Test **POST**
  - Go to the REST Client, select POST and pass http://localhost:8081/movies/
  - In the Body, construct a JSON String for the object to be created - JSON (application/json) as Content-Type
  - Example: 

```JSON
{
    "imagePath":null,
    "title" : "Finding Nemo",
    "releaseDate" : "2003",
    "voteAverage" : 5.2
}
```
It returns 200 in the header, if the object is recorded in the database.
Check http://localhost:8081/h2/ and see a new line in the table

### Test **PUT**
  - Open the H2 database, and choose an id.
  - Go to the REST Client, select PUT function and pass http://localhost:8081/api/movies/{id}
  - Example: http://localhost:8081/movies/1
  - Provide a new JSON String in the Body
  - Postman - select raw → choose JSON(application/json) as Content-Type
  - Example:

```JSON
{
    "imagePath":null,
    "title" : "New Movie",
    "releaseDate" : "04/04/2004",
    "voteAverage" : 4.4
}
```

It returns 200 in the header, if the object is updated in the database.
Check http://localhost:8081/h2/ and see the change in the chosen id.

### Test **DELETE**
  - Open the H2 database, and choose an id.
  - Go to the REST Client, select DELETE function and pass http://localhost:8081/movies/{id}
    - It returns 200 in the header, if the object is deleted in the database.
    - Check http://localhost:8081/h2/ , refresh and see the change in the chosen id.

More about REST: https://www.javadevjournal.com/spring-boot/spring-boot-with-h2-database/

## Handling exceptions
- Using Controller Advice to give elegant error track trace
- Create a new package named `advice` under the `controller` folder, and create a new class named `Advice` and use `@RestControllerAdvice` annotation.
- Create a new package `dto` under the `controller` folder, and create a new class `MessageDto` (Data Transfer Object) to carry the messages in the application, which is a POJO (getters and setter).
- The Advice class will handle the messages in the application through the MessageDto.

```Java
@RestControllerAdvice
public class Advice {
    
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDto processNullPointerException(NullPointerException exception) {
        MessageDto message = new MessageDto();
        message.setMessage("Null pointer error found in request, try again later.");
        message.setType("NULL POINTER ERROR");
        return message;
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDto processNoSuchElementException(NoSuchElementException exception) {
        MessageDto message = new MessageDto();
        message.setMessage("No Such Element Exception error found in request, try again later.");
        message.setType("NO SUCH ELEMENT ERROR");
        return message;
    }
}
```

If the delete method has no internal treatment like:

```Java
@RequestMapping(path= "{id}", method=RequestMethod.DELETE)
    public void deleteMovie(@PathVariable(name="id") Long id) {
        //find the object in the repository
        Optional<Movie> foundMovie = movieRepository.findById(id);
//        if (foundMovie.isPresent()) {
            Movie found = foundMovie.get();
            movieRepository.delete(found);
//        }        
    }
```
Then, when requesting in the Client (Postman) a delete function, like: http://localhost:8081/movies/50 (id=50 does NOT exist)
A message error is given as response in the body (@ResponseBody), instead of a long stack-trace in the console.

```JSON
{
    "message": "No Such Element Exception error found in request, try again later.",
    "type": "NO SUCH ELEMENT ERROR"
}
```

## Refactoring the REST api
- Best practice: transactions separated from controllers.
- Create a service package under the java directory and create a new MovieService class, which will handle the functionalities of the repository.

```Java
@Service
public class MovieService {
    
    private MovieRepository movieRepository;
    
    @Autowired
    public void setMovieRepository(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    private Logger log = LoggerFactory.getLogger(MovieService.class);

    public Optional<Movie> getMovie(Long id) {
        return movieRepository.findById(id);
    }
    
    public Movie saveMovie(Movie movieToSave) {
        return movieRepository.save(movieToSave);
    }
    
    public Movie updateMovie(Movie movieToUpdate, Long id) {
        //find the object in the repository
        Optional<Movie> foundMovie = movieRepository.findById(id);
        if (foundMovie.isPresent()) {
            Movie found = foundMovie.get();
            found.setImagePath(movieToUpdate.getImagePath());
            found.setTitle(movieToUpdate.getTitle());
            found.setReleaseDate(movieToUpdate.getReleaseDate());
            found.setVoteAverage(movieToUpdate.getVoteAverage());
            return movieRepository.save(found);
        }
        else {
            log.info("No movie found with given id");
            return movieToUpdate;
        }
    }
    
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}
```
Modify Controller class for have endpoints with the Service class:
  
```Java
@RestController
@RequestMapping(path="/movies/") ← it’s the root for following paths
public class MovieController {
    
    private MovieService movieService;
    
    @Autowired
    public void setMovieService(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(path="{id}") ← =/movies/{id}
    public Optional<Movie> getMovie(@PathVariable(name="id") Long id) {
        return movieService.getMovie(id);
    }
    
    @PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
    public Movie saveMovie(@RequestBody Movie movieToSave) {
        return movieService.saveMovie(movieToSave);
    }
    
    @PutMapping(path="{id}", consumes=MediaType.APPLICATION_JSON_VALUE)
    public Movie updateMovie(@RequestBody Movie movieToUpdate, @PathVariable(name="id") Long id) {
        return movieService.updateMovie(movieToUpdate, id);
    }
    
    @DeleteMapping(path="{id}")
    public void deleteMovie(@PathVariable(name="id") Long id) {
        movieService.deleteMovie(id);
    }
}
```
## Creating a navigation bar with HTML5, CSS3, and Bootstrap
- Create a folder `layout` under the `templates` directory.
- Inside the `layout` folder, create an HTML file named `header.html`.
  - This will contain the navigation bar.
- Create a folder named images under the folder `resources/static/`
- Move index.html file to templates folder
- Where to place index.html: https://javadeveloperzone.com/spring-boot/spring-boot-index-page-example/

##  Using Thymeleaf
- Thymeleaf templates are just HTML static files (.html extension) that work both in browsers and web applications.
- By default, these templates are stored in `src/main/resources/templates/` folder.
- Spring Boot automatically picks and renders these HTML files when required.

Source: https://attacomsian.com/blog/spring-boot-thymeleaf-example
- In `src/main/resources/static/` holds `/css` and `/images` files
- In `src/main/resources/templates/` holds HTML files (index.html, etc.)

### Spring Boot Whitelabel Error page (type=Not Found, status=404)
verify that you have the correct thymeleaf dependency within your pom.xml:
```XML
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```
Source: https://techhelpnotes.com/java-spring-boot-whitelabel-error-page-typenot-found-status404/
ring Boot Whitelabel Error page (type=Not Found, status=404)
verify that you have the correct thymeleaf dependency within your pom.xml:
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
Source: https://techhelpnotes.com/java-spring-boot-whitelabel-error-page-typenot-found-status404/

## Using bootstrap
- Go to <https://mvnrepository.com/> and search for Bootstrap, copy and paste the dependency in the pom.xml file.

```XML
<dependency>
    <groupId>org.webjars</groupId>
    <artifactId>bootstrap</artifactId>
    <version>5.2.0</version>
</dependency>
```
- Put the reference link to bootstrap webjar in the header.html file
  - Include Bootstrap’s CSS and JS. Place the <link> and <script> tag in the <head> for CSS and JavaScript bundle (including Popper for positioning dropdowns, poppers, and tooltips).
-See documentation for the current links. https://getbootstrap.com/docs/5.2/getting-started/introduction/

## @RestController to work request with JSON
- `@RestController` holds `@Controller` and `@ResponseBody`.
- When you use annotation `@ResponseBody`, you tell spring not to try to find a view with the returned name.
  - If you want redirection to HTML page, just remove the annotation from the controller method.
- **Methods returning objects to JSON must use `@ResponseBody`**
- Attention to the project structure:
  - @SpringBootApplication in the level above @Controller 
- Example:

```
-com.example.spring
  --@SpringBootApplication 
-com.example.spring.controllers
  --@Controller
-com.example.spring.entities
  --@Entity
```
  
```Java
@Controller //if use @RestController html redirections don’t work
@RequestMapping(path="/movies/")
public class MovieController {
    
    private MovieService movieService;
    
    @Autowired
    public void setMovieService(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(path="{id}")
    @ResponseBody
    public Optional<Movie> getMovie(@PathVariable(name="id") Long id) {
        return movieService.getMovie(id);
    }
    
    @PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Movie saveMovie(@RequestBody Movie movieToSave) {
        return movieService.saveMovie(movieToSave);
    }
    
    @PutMapping(path="{id}", consumes=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Movie updateMovie(@RequestBody Movie movieToUpdate, @PathVariable(name="id") Long id) {
        return movieService.updateMovie(movieToUpdate, id);
    }
    
    @DeleteMapping(path="{id}")
    @ResponseBody
    public void deleteMovie(@PathVariable(name="id") Long id) {
        movieService.deleteMovie(id);
    }
    
    @GetMapping(path="/")
    public String index() {
        return "index";
    }
    
    @GetMapping(path="/add")
    public String addMovie(Model model){
        model.addAttribute("movie", new Movie());
        return "editMovie";
    }
    
    @PostMapping(path="/store")
    public String storeMovie(Movie movie) {
        movieService.saveMovie(movie);
        return "redirect:/";
    }
    
    @GetMapping(path="/list")
    public String getAllMovies(Model model) {
        model.addAttribute("movies", movieService.findAll());
        return "listMovies";
    }
    
    @GetMapping(path="/update/{id}")
    public String updateMovie(Model model, @PathVariable(value="id") Long id) {
        model.addAttribute("movie", movieService.getMovie(id));
        return "editMovie";
    }
    
    @GetMapping(path="/delete/{id}")
    public String removeMovie(@PathVariable(value="id") Long id) {
        movieService.deleteMovie(id);
        return "redirect:/movies/list";
    }
}
```
- Because the path root in the controller class is stated as @RequestMapping(path="/movies/"), pay attention to the href tags in the HTML pages.

## Spring Jackson Annotation
- It is used to establish code conventions
- Put the dependency in the pom.xml:

```XML
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
</dependency>
```

Source: <https://github.com/FasterXML/jackson-annotations>
- Example: Instead of camelCase convention, use underscore convention

```Java
@Entity
@AllArgsConstructor
@NoArgsConstructor //for the call to super() constructor made by inheritance
@Getter
@Setter
@ToString
public class Movie {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @JsonProperty("image_path")
    private String imagePath;//image address
    private String title;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("vote_average")
    private Double voteAverage;
}
```

## Lorem Picsum - The Lorem Ipsum for photos.
- The DataLoader class contains a method to generate random pictures from https://picsum.photos/

## One-to-one relationship
Movie has a MovieDetail as one-to-one relationship.

Movie:

```Java
@Entity
@AllArgsConstructor
@NoArgsConstructor //for the call to super() constructor made by inheritance
@Getter
@Setter
@ToString
public class Movie {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@JsonProperty("image_path")
	private String imagePath;//image address
	private String title;
	@JsonProperty("release_date")
	private String releaseDate;
	@JsonProperty("vote_average")
	private Double voteAverage;
	@OneToOne(cascade = CascadeType.ALL) //if Movie is deleted, then its child details is also deleted
	@JoinColumn(name = "movie_details_id", referencedColumnName = "id")
	private MovieDetails details;
}
```

MovieDetails:

```Java
@Entity
@Getter
@Setter
@NoArgsConstructor 
public class MovieDetails{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String overview; //summary
	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass = Genre.class)
	@Column(name="genres")
	private List<Genre> genres;

//	For bidirectional relationship, uncomment and generate the database again.	
//	@OneToOne(mappedBy = "details")
//	private Movie movie;

}
```

For unidirectional relationship Movie → MovieDetails, there's no need to have a property Movie in MovieDetails class.

In this link: <https://www.baeldung.com/jpa-one-to-one> there's explanation of other strategies of one-to-one annotations.

### '@Enumerated(EnumType.STRING) or @Enumerated(EnumType.ORDINAL)'
@Enumerated(EnumType.STRING) = save the enum as String
@Enumerated(EnumType.ORDINAL) = save the enum as the integer corresponding to enum's position

## Using '@Builder' and '@JsonInclude'
**Problem description**
A service 'getMovie()' requires to return just the object’s attributes (and not the related objects). For example, Movie has 'Long id', 'String imagePath', 'String title', 'Double voteAverage' as class attributes, plus the 'details' attribute which is 'MovieDetails' type. Another 'service getMovieDetails()' requires to return all the object Movie attributes, including the details. How to deliver an object with different versions?

**Solution**
The class Movie gains the '@Builder' and '@JsonInclude(JsonInclude.Include.NON_NULL)' annotation.
In the MovieService class, instead of method getMovie() returns just the object Movie from repository, it returns a copy of the object with the required attributes using the 'build()' method from @Builder pattern.

```Java
	public Movie getMovie(Long id) {
		Optional<Movie> found = movieRepository.findById(id);
		 Movie movie = found.get();
         return Movie.builder()
         		.id(movie.getId())
         		.imagePath(movie.getImagePath())
         		.title(movie.getTitle())
         		.releaseDate(movie.getReleaseDate())
         		.voteAverage(movie.getVoteAverage())
         		.build();
	}
```
However, this method return a Movie object with 'details' attribute = null, but the '@JsonInclude(JsonInclude.Include.NON_NULL)' prevents of returning null attributes.

## 'orphanRemoval = true'

'orphanRemoval' marks "child" entity to be removed when it's no longer referenced from the "parent" entity, e.g. when you remove the child entity from the corresponding collection of the parent entity.

When updating Movie, a new MovieDetails is inserted and the old child is removed from the database.

