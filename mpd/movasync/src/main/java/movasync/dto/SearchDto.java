package movasync.dto;

public class SearchDto {
    private final int page;
    private final int total_results;
    private final int total_pages;

    private SearchItemDto[] results;

    public SearchDto(int page, int total_results, int total_pages) {
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
    }

    public SearchItemDto[] getResults() {
        return results;
    }

    public int getPage() {
        return page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }


}
