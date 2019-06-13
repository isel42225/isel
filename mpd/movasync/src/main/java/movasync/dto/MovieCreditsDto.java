package movasync.dto;

public class MovieCreditsDto {
    private final int id;
    private CreditDto[] cast;
    private CreditDto[] crew;

    public MovieCreditsDto(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public CreditDto[] getCast() {
        return cast;
    }

    public CreditDto[] getCrew() {
        return crew;
    }
}
