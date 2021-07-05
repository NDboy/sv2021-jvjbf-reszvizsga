package cinema;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MovieService {

    private AtomicLong atomicLong = new AtomicLong();

    private ModelMapper modelMapper;

    private List<Movie> movies = new ArrayList<>();

    public MovieService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<MovieDTO> listMovies(Optional<String> title) {
        return movies.stream()
                .filter(m -> title.isEmpty() || m.getTitle().toLowerCase().equals(title.get().toLowerCase()))
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .toList();
    }

    public MovieDTO findMovieById(long id) {
        Movie movie = findMovieByIdPrivate(id);
        return modelMapper.map(movie, MovieDTO.class);
    }

    private Movie findMovieByIdPrivate(long id) {
        return movies.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElseThrow(() -> new MovieNotFoundException("Movie not found!"));
    }

    public MovieDTO createMovie(CreateMovieCommand command) {
        Movie movie = new Movie(atomicLong.incrementAndGet(), command.getTitle(), command.getDate(), command.getSpaces(), command.getSpaces());
        movies.add(movie);
        return modelMapper.map(movie, MovieDTO.class);
    }

    public MovieDTO reserveToMovie(long id, CreateReservationCommand command) {
        Movie movie = findMovieByIdPrivate(id);
        movie.reserve(command.getReservation());
        return modelMapper.map(movie, MovieDTO.class);
    }

    public MovieDTO updateDate(long id, UpdateDateCommand command) {
        Movie movie = findMovieByIdPrivate(id);
        movie.setDate(command.getDate());
        return modelMapper.map(movie, MovieDTO.class);
    }

    public void deleteAll() {
        atomicLong = new AtomicLong();
        movies.clear();
    }



}
