package pt.isel.ls.view.html;

public class GetTheatersHtmlView  {

   /* private static final String PAGE_TITLE = "LIST OF Theaters";

    private Element build(Result target){
        Element page  = initPage(PAGE_TITLE);
        Element body = new Element("body");

        CinemaHtmlMapper map = new CinemaHtmlMapper();
        List<Theaters> list = (List<Theaters>)target;

        Element theaterList = map.mapList(list);

        body.addElement(theaterList);  //Add cinema list to body
        page.addElement(body);  //Add body to page

        return page;
    }

    @Override
    public void show(Result target, Writer writer) throws CommandIOException {
        try
        {
            build(target).writeTo(writer);
        }catch (IOException e)
        {
            throw new CommandIOException(
                    String.format("Error writing HTML page using write %s",writer)
            );
        }
    }*/

   /* @Override
    public HttpContent getContent(Result target) {
        return build(target);
    }*/
}
