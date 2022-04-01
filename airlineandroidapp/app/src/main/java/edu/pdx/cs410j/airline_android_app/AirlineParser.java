package edu.pdx.cs410j.airline_android_app;

/**
 * Classes that implement this interface read some source and from it
 * create an airline.
 */
public interface AirlineParser<T extends AbstractAirline> {
    /**
     * Parses some source and returns an airline.
     *
     * @throws ParserException
     *         If the source is malformed.
     */
    public T parse() throws ParserException;
}

