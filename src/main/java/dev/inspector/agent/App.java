package dev.inspector.agent;

import dev.inspector.agent.model.Config;
import dev.inspector.agent.model.Transport;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        Config config = new Config("TEST-INGESTION-KEY");
        Transport transport = new Transport(config);


    }
}
