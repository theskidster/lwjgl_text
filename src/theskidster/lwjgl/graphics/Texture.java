package theskidster.lwjgl.graphics;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import org.lwjgl.system.MemoryStack;

/**
 * @author J Hoffman
 * Created: Dec 26, 2018
 */

public class Texture {
    
    public int width;
    public int height;
    
    public Texture(String fileName, int width, int height) {
        this.width = width;
        this.height = height;
        ByteBuffer bb;
        
        try(MemoryStack ms = MemoryStack.stackPush()) {
            IntBuffer w = ms.mallocInt(1);
            IntBuffer h = ms.mallocInt(1);
            IntBuffer c = ms.mallocInt(1);
            
            bb = stbi_load(getClass().getResource("/theskidster/lwjgl/assets/").toString().substring(6) + fileName, w, h, c, STBI_rgb_alpha);
        }
        
        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, bb);
    }
    
}