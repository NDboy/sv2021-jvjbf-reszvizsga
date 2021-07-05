package cinema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    private Long id;
    private String title;
    private LocalDateTime date;
    private int spaces;
    private int freeSpaces;

    public void reserve(int book) {
        if (book > freeSpaces) {
            throw new IllegalStateException("Not enough spaces");
        }
        freeSpaces -= book;
    }
}
