package us.jts.fortress.rest;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * This class wraps JAXBContext and is used for simple caching mechanism during Fortress XML processing.
 * The intent is to leave future extension point in case schema validation is needed which prevents handling in cache itself.
 *
 * @author Shawn McKinney
 */
@SuppressWarnings( "rawtypes" )
public class JAXBCachedEntry
{
    private final Class cachedClass;
    private final JAXBContext context;

    /**
     * Public constructor requires the entity class to be passed.
     *
     * @param type contains reference to object of type class.
     * @throws JAXBException thrown in the event new instance cannot be created.
     */
    public JAXBCachedEntry( Class type ) throws JAXBException
    {
        context = JAXBContext.newInstance( type );
        cachedClass = type;
    }

    /**
     * Return the class that is associated with this cached JAXBContext.
     *
     * @return class associated with JAXContext
     */
    public Class getCachedClass()
    {
        return cachedClass;
    }

    /**
     * Return the JAXBContext object associated with this wrapper class.
     *
     * @return handle to JAXBContext object.
     */
    public JAXBContext getContext()
    {
        return context;
    }
}