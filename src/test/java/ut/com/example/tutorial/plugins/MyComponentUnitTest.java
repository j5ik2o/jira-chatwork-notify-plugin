package ut.com.example.tutorial.plugins;

import org.junit.Test;
import com.example.tutorial.plugins.MyPluginComponent;
import com.example.tutorial.plugins.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}