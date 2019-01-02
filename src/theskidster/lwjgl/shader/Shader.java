package theskidster.lwjgl.shader;

import java.util.HashMap;
import java.util.Map;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author J Hoffman
 * Created: Dec 26, 2018
 */

public class Shader {
    
    public int id;
    
    public static Map <String, Integer> uniforms = new HashMap();
    
    public Shader(int type, CharSequence src) {
        id = glCreateShader(type);
        glShaderSource(id, src);
        glCompileShader(id);
        
        if(glGetShaderi(id, GL_COMPILE_STATUS) != GL_TRUE) throw new RuntimeException(glGetShaderInfoLog(id));
    }
    
}