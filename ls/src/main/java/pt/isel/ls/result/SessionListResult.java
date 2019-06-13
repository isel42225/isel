package pt.isel.ls.result;

import pt.isel.ls.model.Session;

import java.util.List;

public class SessionListResult implements Result
{

    private final List<Session> sessions;

    public SessionListResult(List<Session> sessions)
    {
        this.sessions = sessions;
    }


    public List<Session> getSessions() {
        return sessions;
    }
}
