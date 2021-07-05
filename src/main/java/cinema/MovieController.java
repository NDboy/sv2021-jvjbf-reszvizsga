package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cinema")
public class MovieController {

    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieDTO> listMovies(@RequestParam Optional<String> title) {
        return movieService.listMovies(title);
    }

    @GetMapping("/{id}")
    public MovieDTO findMovieById(@PathVariable("id") long id) {
        return movieService.findMovieById(id);
    }

    @PostMapping
    public MovieDTO createMovie(@Valid @RequestBody CreateMovieCommand command) {
        return movieService.createMovie(command);
    }

    @PostMapping("/{id}/reserve")
    public MovieDTO reserveToMovie(@PathVariable("id") long id, @RequestBody CreateReservationCommand command) {
        return movieService.reserveToMovie(id, command);
    }

    @PutMapping("/{id}")
    public MovieDTO updateDate(@PathVariable("id") long id, @RequestBody UpdateDateCommand command) {
        return movieService.updateDate(id, command);
    }

    @DeleteMapping
    public void deleteAll() {
        movieService.deleteAll();
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<Problem> handleNotFoundException(MovieNotFoundException mnfe){
        Problem problem = Problem.builder()
                .withType(URI.create("cinema/not-found"))
                .withTitle("Not found")
                .withStatus(Status.NOT_FOUND)
                .withDetail(mnfe.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Problem> handleInvalidNumberOfSeatsException(IllegalStateException ise){
        Problem problem = Problem.builder()
                .withType(URI.create("cinema/bad-reservation"))
                .withTitle("Invalid seat number")
                .withStatus(Status.BAD_REQUEST)
                .withDetail(ise.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }



}
